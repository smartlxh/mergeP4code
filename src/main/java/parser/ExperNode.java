package parser;

import openflow.factory.BasicFactory;
import openflow.protocol.match.POFMatch;
import openflow.protocol.msg.instruction.*;
import util.Transform;

import java.util.List;

public class ExperNode {
    private int type ;// 1 - op,2 - const,3-variable     100 - It means this is a temporary area in the metadata

    private String value;//when the type is "2 - const" or "1 - op"

    private Header header;//when the type is "3 - Variable", eg.this node is a packet field or metadata field.
    private Field field;

    private int offset;//when the type is "100 - tmp".
    private int length;


    ExperNode left;
    ExperNode right;
    ExperNode(){
        left = null;
        right = null;
        value = null;
    }

    public void postOrderTravel(ExperNode node){
        if (node == null){
            return ;
        }
        postOrderTravel(node.left);
        postOrderTravel(node.right);

        //Print it out to test whether the suffix expression is correct
        if(node.type==1||node.type==2)
            System.out.println(node.value);
        else{
            System.out.println(node.header.getName()+"."+node.field.getName());
        }
    }

    public void tree2Ins(ExperNode node, List<ExperNode> stack, List<OFInstruction> insList, List<POFInsRuntimeDataRec> runtimeDataRec, tmpSpaceCount count){
        if(node==null)return;

        tree2Ins(node.left,stack,insList,runtimeDataRec,count);
        tree2Ins(node.right,stack,insList,runtimeDataRec,count);

        int type = node.type;
        switch(type){
            case 1://op    op1 is the dst operand. op2 is the src operand.     op1 + op2   op1 - op2  op1 << op2
                ExperNode op2 = stack.remove(stack.size()-1);
                ExperNode op1 = stack.remove(stack.size()-1);

                if(op1.type==2){  //const
                    OFInstructionWriteMetadata ins1 = (OFInstructionWriteMetadata) BasicFactory.getInstance().getInstruction(OFInstructionType.WRITE_METADATA);
                    ins1.setMetadataOffset((short)(64*count.count));
                    byte[] value = Transform.hexStrWith0x2Bytes(op1.value);
                    ins1.setValue(value);
                    ins1.setWriteLength((short)(value.length*8));
                    insList.add(ins1);

                    op1.type=100;
                    op1.offset=64*count.count;
                    op1.length=value.length*8;
                    count.count++;
                }
                if(op1.type==3){ //variable
                    String headerName = op1.header.getName();
                    String fieldName = op1.field.getName();
                    if((!headerName.equals("scalars_0"))&&(!headerName.equals("ingress_intrinsic_metadata_for_tm_t"))&&(!headerName.equals("ingress_intrinsic_metadata_t"))){
                        // the field is not the metadata,so it's a packet field
                        OFInstructionWriteMetadataFromPacket ins1 = (OFInstructionWriteMetadataFromPacket) BasicFactory.getInstance().getInstruction(OFInstructionType.WRITE_METADATA_FROM_PACKET);
                        ins1.setPacketOffset((short)op1.field.getOffset());
                        ins1.setMetadataOffset((short)(64*count.count));
                        ins1.setWriteLength((short)op1.field.getLength());
                        insList.add(ins1);

                        op1.type=100;
                        op1.offset=64*count.count;
                        op1.length=op1.field.getLength();
                        count.count++;
                    }
                    else if(headerName.equals("ingress_intrinsic_metadata_t")&&fieldName.equals("ingress_port")){

                    }
                    else{
                        //........................
                    }
                }

                OFInstructionCalculateField cal = (OFInstructionCalculateField)BasicFactory.getInstance().getInstruction(OFInstructionType.CALCULATE_FIELD);
                cal.setCalcType(getOFCalcTypeByStr(node.value));
                //set dst
                POFMatch dstField = new POFMatch();
                dstField.setFieldId(POFMatch.METADATA_FIELD_ID);
                dstField.setOffset((short)op1.offset);
                dstField.setLength((short)op1.length);
                cal.setDes_field(dstField);

                //set src
                if(op2.type==2){
                    //const
                    byte[] srcValue = Transform.hexStrWith0x2Bytes(op2.value);
                    int srcIntValue = Transform.bytes2Int(srcValue);
                    cal.setSrc_valueType((byte)0);
                    cal.setSrc_value(srcIntValue);
                }
                else if(op2.type==3){
                    //varable
                    String headerName = op2.header.getName();
                    String fieldName = op2.field.getName();
                    if((!headerName.equals("scalars_0"))&&(!headerName.equals("ingress_intrinsic_metadata_for_tm_t"))&&(!headerName.equals("ingress_intrinsic_metadata_t"))){
                        // the field is not the metadata,so it's a packet field
                        cal.setSrc_valueType((byte)1);
                        POFMatch dstField1 = new POFMatch();
                        dstField1.setFieldId((short)0);
                        dstField1.setOffset((short)op2.field.getOffset());
                        dstField1.setLength((short)op2.field.getLength());
                        cal.setSrc_field(dstField1);
                    }
                    else if(headerName.equals("ingress_intrinsic_metadata_t")&&fieldName.equals("ingress_port")){
                        cal.setSrc_valueType((byte)1);
                        POFMatch dstField1 = new POFMatch();
                        dstField1.setFieldId(POFMatch.METADATA_FIELD_ID);
                        dstField1.setOffset((short)16);
                        dstField1.setLength((short)8);
                        cal.setSrc_field(dstField1);
                    }
                    else{
                        //........................
                    }
                }
                else if(op2.type==100){
                    //tmp space
                    cal.setSrc_valueType((byte)1);
                    POFMatch dstField1 = new POFMatch();
                    dstField1.setFieldId(POFMatch.METADATA_FIELD_ID);
                    dstField1.setOffset((short)op2.offset);
                    dstField1.setLength((short)op2.length);
                    cal.setSrc_field(dstField1);
                }

                insList.add(cal);
                stack.add(op1);
                break;

            case 2://const
                stack.add(node);
                break;

            case 3://variable
                stack.add(node);
                break;
        }
    }

    OFInstructionCalculateField.OFCalcType getOFCalcTypeByStr(String opType){

        OFInstructionCalculateField.OFCalcType result = null;
        switch(opType){
            case "+":
                result = OFInstructionCalculateField.OFCalcType.OFPCT_ADD;
                break;
            case "-":
                result = OFInstructionCalculateField.OFCalcType.OFPCT_SUBTRACT;
                break;
            case "<<":
                result = OFInstructionCalculateField.OFCalcType.OFPCT_LEFT_SHIFT;
                break;
            case ">>":
                result = OFInstructionCalculateField.OFCalcType.OFPCT_RIGHT_SHIFT;
                break;
            case "&":
                result = OFInstructionCalculateField.OFCalcType.OFPCT_BITWISE_ADD;
                break;
            case "|":
                result = OFInstructionCalculateField.OFCalcType.OFPCT_BITWISE_OR;
                break;
            case "^":
                result = OFInstructionCalculateField.OFCalcType.OFPCT_BITWISE_XOR;
                break;
            default:
                break;
        }
        return result;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Field getField(){
        return field;
    }

    public ExperNode getLeft() {
        return left;
    }

    public void setLeft(ExperNode left) {
        this.left = left;
    }

    public ExperNode getRight() {
        return right;
    }

    public void setRight(ExperNode right) {
        this.right = right;    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
