package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;
import openflow.protocol.match.POFMatch;

/**
 * Modify field value with +increment value. could be negative (complement) to minus
 *
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public class OFActionModifyField extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + POFMatch.MINIMUM_LENGTH + 8;

    protected POFMatch matchField;
    protected int increment;

    public OFActionModifyField(){
        super.setType(OFActionType.MODIFY_FIELD);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public void readFrom(ByteBuf data){
        super.readFrom(data);
        matchField = new POFMatch();
        matchField.readFrom(data);
        this.increment = data.readInt();
        data.readBytes(new byte[4]);
    }

    public void writeTo(ByteBuf data){
        super.writeTo(data);
        matchField.writeTo(data);
        data.writeInt(increment);
        data.writeBytes(new byte[4]);
    }



    public String toString(){
        return super.toString() +
                ";fm=" + matchField.toString() +
                ";inc=" + increment;
    }

    public POFMatch getMatchField() {
        return matchField;
    }

    public void setMatchField(POFMatch fieldMatch) {
        this.matchField = fieldMatch;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((matchField == null) ? 0 : matchField.hashCode());
        result = prime * result + increment;
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
        OFActionModifyField other = (OFActionModifyField) obj;
        if (matchField == null) {
            if (other.matchField != null)
                return false;
        } else if (!matchField.equals(other.matchField))
            return false;
        if (increment != other.increment)
            return false;
        return true;
    }

    @Override
    public OFActionModifyField clone() throws CloneNotSupportedException {
        OFActionModifyField action = (OFActionModifyField) super.clone();
        if(null != matchField){
            action.setMatchField(matchField.clone());
        }
        return action;
    }
}
