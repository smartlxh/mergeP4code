package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;
import openflow.protocol.match.POFMatch;

/**
 * Delete a field with field position and length.
 *
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public class OFActionDeleteField extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + 8 + POFMatch.MINIMUM_LENGTH;

    //variable
    protected short tagPosition;
    protected byte tagLengthValueType;
    protected int tagLengthValue;
    //the tagLengthField give the place where we can get the length
    //so it is not simple
    protected POFMatch tagLengthField;


    public OFActionDeleteField(){
        super.setType(OFActionType.DELETE_FIELD);
        super.setLength((short) MINIMUM_LENGTH);
    }



    public short getTagPosition() {
        return tagPosition;
    }



    public void setTagPosition(short tagPosition) {
        this.tagPosition = tagPosition;
    }



    public byte getTagLengthValueType() {
        return tagLengthValueType;
    }



    public void setTagLengthValueType(byte tagLengthValueType) {
        this.tagLengthValueType = tagLengthValueType;
    }



    public int getTagLengthValue() {
        return tagLengthValue;
    }



    public void setTagLengthValue(int tagLengthValue) {
        this.tagLengthValue = tagLengthValue;
    }



    public POFMatch getTagLengthField() {
        return tagLengthField;
    }



    public void setTagLengthField(POFMatch tagLengthField) {
        this.tagLengthField = tagLengthField;
    }



    public void readFrom(ByteBuf data){
        super.readFrom(data);
        this.tagPosition = data.readShort();
        this.tagLengthValueType = data.readByte();
        data.readBytes(new byte[5]);
        if(tagLengthValueType == 0){
            tagLengthValue = data.readInt();
            data.readBytes(new byte[4]);
            tagLengthField = null;
        }else if(tagLengthValueType == 1){
            tagLengthValue = 0;
            tagLengthField = new POFMatch();
            tagLengthField.readFrom(data);
        }else{
            tagLengthValue = 0;
            tagLengthField = null;
            data.readBytes(new byte[POFMatch.MINIMUM_LENGTH]);
        }

    }

    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeShort(tagPosition);
        data.writeByte(tagLengthValueType);
        data.writeBytes(new byte[5]);
        if(tagLengthValueType == 0){//
            data.writeInt(tagLengthValue);
            data.writeBytes(new byte[4]);
        }else if(tagLengthValueType == 1 && tagLengthField != null){
            tagLengthField.writeTo(data);
        }else{
            data.writeBytes(new byte[POFMatch.MINIMUM_LENGTH]);
        }

    }



    public String toString(){
        return super.toString() +
                ";tagPosition=" + tagPosition +
                ";tagLengthValueType=" + tagLengthValueType +
                ";tagLengthValue=" + tagLengthValue +
                ";tagLengthField=" + tagLengthField;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + tagPosition;
        result = prime * result + tagLengthValueType;
        result = prime * result + tagLengthValue;
        result = prime * result + tagLengthField.hashCode();
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
        OFActionDeleteField other = (OFActionDeleteField) obj;
        if(tagPosition != other.tagPosition)
            return false;
        if(tagLengthValueType != other.tagLengthValueType)
            return false;
        if(tagLengthValue != other.tagLengthValue)
            return false;
        if(!tagLengthField.equals(other.tagLengthField))
            return false;
        return true;
    }
}
