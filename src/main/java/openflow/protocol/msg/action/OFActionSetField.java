package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;
import openflow.protocol.match.POFMatchX;

/**
 * Set field value using {@link #fieldSetting} (value/mask).
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public class OFActionSetField extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + POFMatchX.MINIMUM_LENGTH;
    protected POFMatchX fieldSetting;

    public OFActionSetField(){
        super.setType(OFActionType.SET_FIELD);
        super.setLength((short)MINIMUM_LENGTH);
    }

    public void readFrom(ByteBuf data){
        super.readFrom(data);
        fieldSetting = new POFMatchX();
        fieldSetting.readFrom(data);
    }

    public void writeTo(ByteBuf data){
        super.writeTo(data);
        fieldSetting.writeTo(data);
    }

    public String toBytesString(){
        return super.toBytesString() +
                fieldSetting.toBytesString();
    }

    public String toString(){
        return super.toString() +
                ";fs=" + fieldSetting.toString();
    }

    public POFMatchX getFieldSetting() {
        return fieldSetting;
    }

    public void setFieldSetting(POFMatchX fieldSetting) {
        this.fieldSetting = fieldSetting;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((fieldSetting == null) ? 0 : fieldSetting.hashCode());
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
        OFActionSetField other = (OFActionSetField) obj;
        if (fieldSetting == null) {
            return other.fieldSetting == null;
        } else return fieldSetting.equals(other.fieldSetting);
    }

    @Override
    public OFActionSetField clone() throws CloneNotSupportedException {
        OFActionSetField action = (OFActionSetField) super.clone();
        if(null != fieldSetting){
            action.setFieldSetting(fieldSetting.clone());
        }
        return action;
    }
}
