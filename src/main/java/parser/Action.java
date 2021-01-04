package parser;

import openflow.factory.BasicFactory;
import openflow.protocol.OFGlobal;
import openflow.protocol.match.POFMatch;
import openflow.protocol.match.POFMatchX;
import openflow.protocol.msg.action.*;
import openflow.protocol.msg.instruction.OFInstruction;
import openflow.protocol.msg.instruction.OFInstructionApplyActions;
import openflow.protocol.msg.instruction.OFInstructionType;
import openflow.protocol.msg.instruction.OFInstructionWriteMetadataFromPacket;
import util.GenerateBytes;
import util.Transform;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private String name;
    private int id;
    private List<RuntimeData> runtimeDataList;
    private List<Primitive> primitivesList;

    private List<OFInstruction> insList;
    private List<OFAction> tmpActionList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<RuntimeData> getRuntimeDataList() {
        return runtimeDataList;
    }

    public String getRuntimeNameById(int id){
        return runtimeDataList.get(id).getName();
    }

    public void setRuntimeDataList(List<RuntimeData> runtimeDataList) {
        this.runtimeDataList = runtimeDataList;
    }

    public List<Primitive> getPrimitivesList() {
        return primitivesList;
    }

    public void setPrimitivesList(List<Primitive> primitivesList) {
        this.primitivesList = primitivesList;
    }

    private OFActionDrop dropAction = null;
    private OFActionOutput outputAction =null;
    POFInsRuntimeDataRec outputRuntimeRec = null;
    List<POFInsRuntimeDataRec> runtimeDataRec = new ArrayList<>();

    public void ParseActon2POFIns(P4JsonParser parser){
        insList = new ArrayList<>();
        tmpActionList = new ArrayList<>();

        int prim_num  = primitivesList.size();
        for(int i=0;i<prim_num;i++){
            Primitive aPrimitive = primitivesList.get(i);
            PrimitiveTypes primType = aPrimitive.getType();
            switch(primType){
                case drop:
                    dropAction = (OFActionDrop) BasicFactory.getInstance().getAction(OFActionType.DROP);
                    dropAction.setReason(0);
                    break;
                case assign:
                    List<OPParameter> assignPar = aPrimitive.parametersList;
                    OPParameter left = assignPar.get(0);
                    OPParameter right = assignPar.get(1);

                    String leftHeaderName = left.getHeaderName();
                    String leftFieldName = left.getFieldName();
                    Header leftHeader = parser.getHeaderVarByNameMap.get(leftHeaderName).getHeaderType();
                    Field leftField = leftHeader.findFieldByName(leftFieldName);

                    String rightType = right.getType();
                    switch(rightType){
                        case "runtime_data"://runtime_data can be viewed a immediate value in POF
                            if(!(leftHeader.getName().equals("scalars_0"))&&!(leftHeader.getName().equals("ingress_intrinsic_metadata_for_tm_t"))&&!(leftHeader.getName().equals("ingress_intrinsic_metadata_t")))
                            { //left is not the matadata
                                OFActionSetField setField = (OFActionSetField)BasicFactory.getInstance().getAction(OFActionType.SET_FIELD);
                                POFMatchX FieldSetting  = new POFMatchX();
                                FieldSetting.setFieldName(leftFieldName);
                                FieldSetting.setFieldId((short)0);
                                FieldSetting.setOffset((short)leftField.getOffset());
                                int len = leftField.getLength();
                                FieldSetting.setLength((short)len);
                                FieldSetting.setValue(new byte[(len+7)/8]);
                                FieldSetting.setMask(GenerateBytes.generatePartRight1Bits((len+7)/8,len));
                                setField.setFieldSetting(FieldSetting);
                                tmpActionList.add(setField);

                                POFInsRuntimeDataRec tmp = new POFInsRuntimeDataRec();
                                tmp.InsIndex = insList.size();
                                tmp.ActionIndex = tmpActionList.size()-1;
                                tmp.ParIndex = 0;
                                tmp.RuntimeDataId = right.runtimeDataId;
                                runtimeDataRec.add(tmp);

                                if(tmpActionList.size()== OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION){
                                    OFInstructionApplyActions ins1 = (OFInstructionApplyActions) BasicFactory.getInstance().getInstruction(OFInstructionType.APPLY_ACTIONS);
                                    ins1.setActionList(tmpActionList);
                                    insList.add(ins1);
                                    tmpActionList = new ArrayList<>();
                                }
                            }
                            else if(leftHeader.getName().equals("ingress_intrinsic_metadata_for_tm_t")&&leftFieldName.equals("ucast_egress_port")){
                                outputAction = (OFActionOutput) BasicFactory.getInstance().getAction(OFActionType.OUTPUT);
                                outputAction.setPortIdValueType((byte)0);
                                outputAction.setMetadataOffset((short)0);
                                outputAction.setMetadataLength((short)0);
                                outputAction.setPacketOffset((short)0);
                                outputAction.setPortId(0);
                                outputRuntimeRec = new POFInsRuntimeDataRec();
                                outputRuntimeRec.ParIndex=4;
                                outputRuntimeRec.RuntimeDataId = right.runtimeDataId;
                            }
                            else{
                                //.........
                            }
                            break;

                        case "hexstr":
                            if(!(leftHeader.getName().equals("scalars_0"))&&!(leftHeader.getName().equals("ingress_intrinsic_metadata_for_tm_t"))&&!(leftHeader.getName().equals("ingress_intrinsic_metadata_t")))
                            {//left is not the matadata
                                OFActionSetField setField = (OFActionSetField)BasicFactory.getInstance().getAction(OFActionType.SET_FIELD);
                                POFMatchX FieldSetting  = new POFMatchX();
                                FieldSetting.setFieldName(leftFieldName);
                                FieldSetting.setFieldId((short)0);
                                FieldSetting.setOffset((short)leftField.getOffset());
                                int len = leftField.getLength();
                                FieldSetting.setLength((short)len);

                                String value = right.getValue();
                                byte[] byteValue = Transform.hexStrWith0x2Bytes(value);
                                byte[] realFieldSetting = Transform.bytes2SpecLenBytes(byteValue,len);

                                FieldSetting.setValue(realFieldSetting);
                                FieldSetting.setMask(GenerateBytes.generatePartRight1Bits((len+7)/8,len));
                                setField.setFieldSetting(FieldSetting);
                                tmpActionList.add(setField);

                                if(tmpActionList.size()== OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION){
                                    OFInstructionApplyActions ins1 = (OFInstructionApplyActions) BasicFactory.getInstance().getInstruction(OFInstructionType.APPLY_ACTIONS);
                                    ins1.setActionList(tmpActionList);
                                    insList.add(ins1);
                                    tmpActionList = new ArrayList<>();
                                }
                            }
                            else if(leftHeader.getName().equals("ingress_intrinsic_metadata_for_tm_t")&&leftFieldName.equals("ucast_egress_port")){
                                outputAction = (OFActionOutput) BasicFactory.getInstance().getAction(OFActionType.OUTPUT);
                                outputAction.setPortIdValueType((byte)0);
                                outputAction.setMetadataOffset((short)0);
                                outputAction.setMetadataLength((short)0);
                                outputAction.setPacketOffset((short)0);

                                String value = right.getValue();
                                byte[] byteValue = Transform.hexStrWith0x2Bytes(value);
                                int portid = Transform.bytes2Int(byteValue);

                                outputAction.setPortId(portid);
                                outputRuntimeRec = null;
                            }
                            else{
                                //.........
                            }
                            break;

                        case "field":  //standard_metadata  egress_spec  cannot be the right value temporarily
                            if(!(leftHeader.getName().equals("scalars_0"))&&!(leftHeader.getName().equals("ingress_intrinsic_metadata_for_tm_t"))&&!(leftHeader.getName().equals("ingress_intrinsic_metadata_t")))
                            { //left is not the matadata

                                String rightHeaderName = right.getHeaderName();
                                String rightFieldName = right.getFieldName();
                                Header rightHeader = parser.getHeaderVarByNameMap.get(rightHeaderName).getHeaderType();
                                Field rightField = rightHeader.findFieldByName(rightFieldName);

                                //then  check the right type
                                if(!(rightHeader.getName().equals("scalars_0"))&&!(rightHeader.getName().equals("ingress_intrinsic_metadata_for_tm_t"))&&!(leftHeader.getName().equals("ingress_intrinsic_metadata_t"))){
                                    //right is not the metadata.
                                    // packet_field assign to packet_field. need to first assign to the metadata,than assign the field from the metadata
                                    // two packet_field's length must be equal
                                    OFInstructionWriteMetadataFromPacket ins1 = (OFInstructionWriteMetadataFromPacket) BasicFactory.getInstance().getInstruction(OFInstructionType.WRITE_METADATA_FROM_PACKET);
                                    ins1.setPacketOffset((short)rightField.getOffset());
                                    ins1.setMetadataOffset((short)64);
                                    ins1.setWriteLength((short)rightField.length);

                                    OFActionSetFieldFromMetadata act1 = (OFActionSetFieldFromMetadata)BasicFactory.getInstance().getAction(OFActionType.SET_FIELD_FROM_METADATA);
                                    POFMatch dstMatch = new POFMatch();
                                    dstMatch.setFieldId((short)0);
                                    dstMatch.setOffset((short)leftField.getOffset());
                                    dstMatch.setLength((short)leftField.length);
                                    act1.setFieldSetting(dstMatch);
                                    act1.setMetadataOffset((short)64);

                                    if(tmpActionList.size()!=0){
                                        OFInstructionApplyActions ins2 = (OFInstructionApplyActions) BasicFactory.getInstance().getInstruction(OFInstructionType.APPLY_ACTIONS);
                                        ins2.setActionList(tmpActionList);
                                        insList.add(ins1);
                                        tmpActionList = new ArrayList<>();
                                    }
                                    insList.add(ins1);
                                    tmpActionList.add(act1);
                                }
                                else if(rightHeader.getName().equals("ingress_intrinsic_metadata_t")&&rightFieldName.equals("ingress_port")){
                                    OFActionSetFieldFromMetadata act1 = (OFActionSetFieldFromMetadata)BasicFactory.getInstance().getAction(OFActionType.SET_FIELD_FROM_METADATA);
                                    POFMatch dstMatch = new POFMatch();
                                    dstMatch.setFieldId((short)0);
                                    dstMatch.setOffset((short)(leftField.getOffset()+1));// here, offset+1, because in P4, the "ingress_port" is 9 bit, POF is 8 bit
                                    dstMatch.setLength((short)8);
                                    act1.setFieldSetting(dstMatch);
                                    act1.setMetadataOffset((short)16);
                                    tmpActionList.add(act1);

                                    if(tmpActionList.size()== OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION){
                                        OFInstructionApplyActions ins1 = (OFInstructionApplyActions) BasicFactory.getInstance().getInstruction(OFInstructionType.APPLY_ACTIONS);
                                        ins1.setActionList(tmpActionList);
                                        insList.add(ins1);
                                        tmpActionList = new ArrayList<>();
                                    }

                                }
                                else{
                                    //..........................
                                }
                            }
                            else if(leftHeader.getName().equals("ingress_intrinsic_metadata_for_tm_t")&&leftFieldName.equals("ucast_egress_port")){
                                String rightHeaderName = right.getHeaderName();
                                String rightFieldName = right.getFieldName();
                                Header rightHeader = parser.getHeaderVarByNameMap.get(rightHeaderName).getHeaderType();
                                Field rightField = rightHeader.findFieldByName(rightFieldName);

                                //then  check the right type
                                if(!(rightHeader.getName().equals("scalars_0"))&&!(rightHeader.getName().equals("ingress_intrinsic_metadata_for_tm_t"))&&!(rightHeader.getName().equals("ingress_intrinsic_metadata_t"))){
                                    //right is not the metadata.
                                    // packet_field assign to standard_metadata.egress_spec.
                                    // two packet_field's length must be equal

                                    // ???????????  should not do this? because in the final ouput, must move the packet offset by Ins"mov_packet_offset"
                                    outputAction = (OFActionOutput) BasicFactory.getInstance().getAction(OFActionType.OUTPUT);
                                    outputAction.setPortIdValueType((byte)1);
                                    outputAction.setMetadataOffset((short)0);
                                    outputAction.setMetadataLength((short)0);
                                    outputAction.setPacketOffset((short)0);
                                    POFMatch outputField = new POFMatch();
                                    outputField.setFieldId((short)0);
                                    outputField.setOffset((short)rightField.getOffset());
                                    outputField.setLength((short)rightField.getLength());
                                    outputAction.setPortIdField(outputField);
                                    //tmpActionList.add(outputAction);

                                    outputRuntimeRec = null;

                                    if(tmpActionList.size()== OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION){
                                        OFInstructionApplyActions ins1 = (OFInstructionApplyActions) BasicFactory.getInstance().getInstruction(OFInstructionType.APPLY_ACTIONS);
                                        ins1.setActionList(tmpActionList);
                                        insList.add(ins1);
                                        tmpActionList = new ArrayList<>();
                                    }
                                }
                                else if(rightHeader.getName().equals("ingress_intrinsic_metadata_t")&&rightFieldName.equals("ingress_port")){
                                    outputAction = (OFActionOutput) BasicFactory.getInstance().getAction(OFActionType.OUTPUT);
                                    outputAction.setPortIdValueType((byte)1);
                                    outputAction.setMetadataOffset((short)0);
                                    outputAction.setMetadataLength((short)0);
                                    outputAction.setPacketOffset((short)0);
                                    POFMatch outputField = new POFMatch();
                                    outputField.setFieldId(POFMatch.METADATA_FIELD_ID);
                                    outputField.setOffset((short)16);
                                    outputField.setLength((short)8);
                                    outputAction.setPortIdField(outputField);
                                    //tmpActionList.add(outputAction);

                                    outputRuntimeRec = null;

                                    if(tmpActionList.size()== OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION){
                                        OFInstructionApplyActions ins1 = (OFInstructionApplyActions) BasicFactory.getInstance().getInstruction(OFInstructionType.APPLY_ACTIONS);
                                        ins1.setActionList(tmpActionList);
                                        insList.add(ins1);
                                        tmpActionList = new ArrayList<>();
                                    }

                                }
                                else{
                                    //..........................
                                }


                            }
                            else{
                                //.........................
                            }
                            break;

                        case "expression":

                            if(tmpActionList.size()!= 0){
                                OFInstructionApplyActions ins1 = (OFInstructionApplyActions) BasicFactory.getInstance().getInstruction(OFInstructionType.APPLY_ACTIONS);
                                ins1.setActionList(tmpActionList);
                                insList.add(ins1);
                                tmpActionList = new ArrayList<>();
                            }

                            ExperNode ExpRoot = right.getRoot();
                            List<ExperNode> stack = new ArrayList<>();
                            tmpSpaceCount tmpCount = new tmpSpaceCount();
                            tmpCount.count=1;
                            ExpRoot.tree2Ins(ExpRoot,stack,insList,runtimeDataRec,tmpCount);

                            if(!(leftHeader.getName().equals("scalars_0"))&&!(leftHeader.getName().equals("ingress_intrinsic_metadata_for_tm_t"))&&!(leftHeader.getName().equals("ingress_intrinsic_metadata_t")))
                            { //left is not the matadata
                                OFActionSetFieldFromMetadata ActSet1 = (OFActionSetFieldFromMetadata)BasicFactory.getInstance().getAction(OFActionType.SET_FIELD_FROM_METADATA);

                                POFMatch assignField = new POFMatch();
                                assignField.setFieldId((short)0);
                                assignField.setOffset((short)leftField.getOffset());
                                assignField.setLength((short)leftField.getLength());
                                ActSet1.setFieldSetting(assignField);
                                ExperNode expResult = stack.get(0);
                                ActSet1.setMetadataOffset((short)(expResult.getOffset()+expResult.getLength()-leftField.getLength()));


                                tmpActionList.add(ActSet1);

                                if(tmpActionList.size()== OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION){
                                    OFInstructionApplyActions ins1 = (OFInstructionApplyActions) BasicFactory.getInstance().getInstruction(OFInstructionType.APPLY_ACTIONS);
                                    ins1.setActionList(tmpActionList);
                                    insList.add(ins1);
                                    tmpActionList = new ArrayList<>();
                                }
                            }
                            else if(leftHeader.getName().equals("ingress_intrinsic_metadata_for_tm_t")&&leftFieldName.equals("ucast_egress_port")){
                                outputAction = (OFActionOutput) BasicFactory.getInstance().getAction(OFActionType.OUTPUT);
                                outputAction.setPortIdValueType((byte)1);
                                POFMatch assignField = new POFMatch();
                                assignField.setFieldId(POFMatch.METADATA_FIELD_ID);
                                ExperNode expResult = stack.get(0);
                                assignField.setOffset((short)expResult.getOffset());
                                assignField.setLength((short)expResult.getLength());
                                outputAction.setMetadataOffset((short)0);
                                outputAction.setMetadataLength((short)0);
                                outputAction.setPacketOffset((short)0);
                                outputAction.setPortIdField(assignField);

                                outputRuntimeRec = null;
                            }
                            else{
                                //.........
                            }

                            tmpCount = null;
                            break;
                    }
                    break;


                case count:
                case addHeader:
                case removeHeader:
                    break;
            }
        }

        if(dropAction!=null)
        {
            tmpActionList.add(dropAction);
        }
        /*else if(outputAction!=null)
        {
            tmpActionList.add(outputAction);
            if(outputRuntimeRec!=null){
                outputRuntimeRec.InsIndex = insList.size();
                outputRuntimeRec.ActionIndex = tmpActionList.size()-1;
                runtimeDataRec.add(outputRuntimeRec);
            }
        }*/

        if(tmpActionList.size()!=0){
            OFInstructionApplyActions ins1 = (OFInstructionApplyActions) BasicFactory.getInstance().getInstruction(OFInstructionType.APPLY_ACTIONS);
            ins1.setActionList(tmpActionList);
            insList.add(ins1);
            tmpActionList = new ArrayList<>();
        }
    }

    public List<OFInstruction> getInsList(){
        return insList;
    }

    public List<POFInsRuntimeDataRec> getRuntimeDataRec(){
        return runtimeDataRec;
    }

    public OFActionOutput getOutputAction(){
        return outputAction;
    }

    public POFInsRuntimeDataRec getOutputRuntimeRec(){
        return outputRuntimeRec;
    }


    public Action(){
        runtimeDataList = new ArrayList<RuntimeData>();
        primitivesList = new ArrayList<Primitive>();
    }
}

class RuntimeData{

    String name;
    int bitwidth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBitwidth() {
        return bitwidth;
    }

    public void setBitwidth(int bitwidth) {
        this.bitwidth = bitwidth;
    }
}

// only the Parameter for "assign" op
class OPParameter{
    String type ;//  only now support for field ,hexstr,runtime_data and expression

    //when the type is field(packet field, standard_metadata field , or metadata field)
    String headerName;//headerVar Name, for example:ipv4
    String fieldName;//field Name,      for example:ttl

    //when the type is runtime_data(eg. action parameter)
    int runtimeDataId;//

    //when the type is hexstr
    String value;

    //when the type is expression
    String expression;//in this case
    ExperNode root ; // expression tree




    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int getRuntimeDataId() {
        return runtimeDataId;
    }

    public void setRuntimeDataId(int runtimeDataId) {
        this.runtimeDataId = runtimeDataId;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ExperNode getRoot() {
        return root;
    }

    public void setRoot(ExperNode root) {
        this.root = root;
    }



}


class Primitive {
    PrimitiveTypes type;
    List<OPParameter> parametersList;

    public PrimitiveTypes getType() {
        return type;
    }

    public void setType(PrimitiveTypes type) {
        this.type = type;
    }

    public List<OPParameter> getParametersList() {
        return parametersList;
    }

    public void setParametersList(List<OPParameter> parametersList) {
        this.parametersList = parametersList;
    }

    Primitive () {
        parametersList = new ArrayList<OPParameter>();
    }

}

class tmpSpaceCount{
    public int count;
}