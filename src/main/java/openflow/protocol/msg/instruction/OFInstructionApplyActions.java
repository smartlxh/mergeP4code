package openflow.protocol.msg.instruction;

import io.netty.buffer.ByteBuf;
import openflow.factory.OFActionFactory;
import openflow.factory.OFActionFactoryAware;
import openflow.protocol.OFGlobal;
import openflow.protocol.msg.action.OFAction;
import util.HexString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFInstructionApplyActions extends OFInstruction implements OFActionFactoryAware, Cloneable {
    public static final int MINIMUM_LENGTH = OFInstruction.MINIMUM_LENGTH + 8;
    public static final int MANIMAL_LENGTH = OFInstruction.MINIMUM_LENGTH + 8 + OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION * OFAction.MAXIMAL_LENGTH;

    protected byte actionNum;
    protected List<OFAction> actionList;

    protected OFActionFactory actionFactory;

    public OFInstructionApplyActions(){
        super.setType(OFInstructionType.APPLY_ACTIONS);
        super.setLength((short)MANIMAL_LENGTH);
    }

    public short getLength() {
        return (short)MANIMAL_LENGTH;
    }


    public void readFrom(ByteBuf data){
        super.readFrom(data);
        actionNum = data.readByte();
        data.readBytes(new byte[7]);

        if(this.actionFactory == null){
            throw new RuntimeException("POFActionFactory not set");
        }
        //data.readBytes(new byte[6*48]);
        this.actionList = this.actionFactory.parseActions(data, OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION * OFAction.MAXIMAL_LENGTH);
    }




    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeByte(actionNum);
        data.writeBytes(new byte[7]);

        if(actionList == null){
            data.writeBytes(new byte[OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION * OFAction.MAXIMAL_LENGTH]);
        }else{
            OFAction action;

            if(actionNum > actionList.size()){
                throw new RuntimeException("actionNum " + actionNum + " > actionList.size()" + actionList.size());
            }

            int i;
            for(i = 0; i < actionNum && i < OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION; i++){
                action = actionList.get(i);
                if(action == null){
                    data.writeBytes(new byte[OFAction.MAXIMAL_LENGTH]);
                }else{
                    action.writeTo(data);
                    if(action.getLength() < OFAction.MAXIMAL_LENGTH ){

                        data.writeBytes(new byte[OFAction.MAXIMAL_LENGTH - action.getLength()]);
                    }
                }
            }
            if(i < OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION){

                data.writeBytes(new byte[(OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION - i) * OFAction.MAXIMAL_LENGTH]);
            }
        }
    }

    @Override
    public String toBytesString(){
        String string =  super.toBytesString() +
                HexString.toHex(actionNum) +
                HexString.getByteZeros(7);

        if(actionList == null){
            string += HexString.getByteZeros(OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION * OFAction.MAXIMAL_LENGTH);
        }else{
            OFAction action;

            if(actionNum > actionList.size()){
                throw new RuntimeException("actionNum " + actionNum + " > actionList.size()" + actionList.size());
            }

            int i;
            for(i = 0; i < actionNum && i < OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION; i++){
                action = actionList.get(i);
                if(action == null){
                    string += HexString.getByteZeros(OFAction.MAXIMAL_LENGTH);
                }else{
                    string += action.toBytesString();
                    if(action.getLength() < OFAction.MAXIMAL_LENGTH ){
                        string += HexString.getByteZeros(OFAction.MAXIMAL_LENGTH - action.getLength());
                    }
                }
            }
            if(i < OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION){
                string += HexString.getByteZeros( (OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION - i) * OFAction.MAXIMAL_LENGTH);
            }
        }

        return string;
    }

    @Override
    public String toString(){
        String string =  super.toString() +
                ";an=" + actionNum ;

        OFAction action;
        for(int i = 0; i < actionList.size(); i++){
            action = actionList.get(i);
            string += ";ac[" + i + "]=" + action.toString();
        }

        return string;
    }

    public byte getActionNum() {
        return actionNum;
    }
    public void setActionNum(byte actionNum) {
        this.actionNum = actionNum;
    }
    public List<OFAction> getActionList() {
        return actionList;
    }
    public void setActionList(List<OFAction> actionList) {
        this.actionList = actionList;
        this.actionNum = (byte) actionList.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((actionList == null) ? 0 : actionList.hashCode());
        result = prime * result + actionNum;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        OFInstructionApplyActions other = (OFInstructionApplyActions) obj;
        if (actionList == null) {
            if (other.actionList != null)
                return false;
        } else if (!actionList.equals(other.actionList))
            return false;
        if (actionNum != other.actionNum)
            return false;
        return true;
    }

    @Override
    public void setActionFactory(OFActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    @Override
    public OFInstructionApplyActions clone() throws CloneNotSupportedException {
        OFInstructionApplyActions ins = (OFInstructionApplyActions) super.clone();
        if(null != actionList
                && 0 != actionList.size()
                && 0 != actionNum){
            ins.actionList = new ArrayList<OFAction>();
            for(OFAction action : this.actionList){
                ins.actionList.add(action.clone());
            }
        }
        return ins;
    }
}
