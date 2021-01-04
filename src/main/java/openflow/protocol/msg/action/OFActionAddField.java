package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;
import openflow.protocol.OFGlobal;
import util.HexString;

/**
 * Add a field at the start {@link #fieldPosition} and length {@link #fieldLength},  with value {@link #fieldValue}.
 *
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public class OFActionAddField extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + 8 + OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE;

    protected short fieldId;
    protected short fieldPosition;  //bit
    protected int fieldLength;      //bit

    protected byte[] fieldValue;

    public OFActionAddField(){
        super.setType(OFActionType.ADD_FIELD);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public void readFrom(ByteBuf data){
        super.readFrom(data);
        this.fieldId = data.readShort();
        this.fieldPosition = data.readShort();
        this.fieldLength = data.readInt();
        this.fieldValue=new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE];
        data.readBytes(fieldValue);

    }

    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeShort(fieldId);
        data.writeShort(fieldPosition);
        data.writeInt(fieldLength);
        if(fieldValue == null){
            data.writeBytes(new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE]);
        }else{
            if(fieldValue.length > OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE){
                data.writeBytes(fieldValue, 0, OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE);
            }else{
                data.writeBytes(fieldValue);
                data.writeBytes(new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE - fieldValue.length]);
            }
        }

    }



    public String toString(){
        return super.toString() +
                ";fid=" + fieldId +
                ";fpos=" + fieldPosition +
                ";flen=" + fieldLength +
                ";fval=" + HexString.toHexString(fieldValue);
    }

    public short getFieldId() {
        return fieldId;
    }

    public void setFieldId(short fieldId) {
        this.fieldId = fieldId;
    }

    public short getFieldPosition() {
        return fieldPosition;
    }

    public void setFieldPosition(short fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public int getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public byte[] getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(byte[] fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + fieldId;
        result = prime * result + fieldLength;
        result = prime * result + fieldPosition;
        int temp=0;
        for(byte t:fieldValue)
        {
            temp+=t;
        }
        result = prime * result + temp;
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
        OFActionAddField other = (OFActionAddField) obj;
        if (fieldId != other.fieldId)
            return false;
        if (fieldLength != other.fieldLength)
            return false;
        if (fieldPosition != other.fieldPosition)
            return false;
        if (fieldValue.length != other.fieldValue.length)
            return false;
        for(int i=0;i<fieldValue.length;i++)
        {
            if(fieldValue[i]!=other.fieldValue[i])
                return false;
        }
        return true;
    }
}
