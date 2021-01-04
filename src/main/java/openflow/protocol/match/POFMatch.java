package openflow.protocol.match;

import io.netty.buffer.ByteBuf;

/**
 * @author twd
 * @description match field according to offset and length
 * @date 2019-12-02
 */
public class POFMatch implements Cloneable{
    public static final int MINIMUM_LENGTH = 8;

    public static final short METADATA_FIELD_ID = (short) 0xffff;
    public static final short METADATA_MAXLEN = 128;

    protected String fieldName;	//just store, do not send
    protected short fieldId;
    protected short offset;     //bit
    protected short length;     //bit

    public POFMatch(){}

    public void readFrom(ByteBuf data){
        this.fieldId = data.readShort();
        this.offset  = data.readShort();
        this.length  = data.readShort();
        data.readShort();
    }

    public void writeTo(ByteBuf data){
        data.writeShort(fieldId);
        data.writeShort(offset);
        data.writeShort(length);
        data.writeShort((short) 0);
    }


    public String toString(){
        return "fid=" + fieldId +
                ((fieldName==null) ? "" : (";name=" + fieldName)) +
                ";ofst=" + offset +
                ";len=" + length;
    }

    public int hashCode(){
        final int prime = 641;
        int result = 1;

        result = prime * result + fieldId;
        result = prime * result + offset;
        result = prime * result + length;

        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof POFMatch)) {
            return false;
        }
        POFMatch other = (POFMatch) obj;

        if (fieldId != other.fieldId) {
            return false;
        }
        if (offset != other.offset) {
            return false;
        }
        return length == other.length;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public short getFieldId() {
        return fieldId;
    }
    public void setFieldId(short fieldId) {
        this.fieldId = fieldId;
    }
    public short getOffset() {
        return offset;
    }
    public void setOffset(short offset) {
        this.offset = offset;
    }
    public short getLength() {
        return length;
    }
    public void setLength(short i) {
        this.length = i;
    }

    @Override
    public POFMatch clone() throws CloneNotSupportedException {
        return (POFMatch) super.clone();
    }
}

