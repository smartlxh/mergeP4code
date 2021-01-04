package openflow.protocol.match;

import io.netty.buffer.ByteBuf;
import openflow.protocol.OFGlobal;
import util.HexString;

import java.util.Arrays;

/**
 * @author twd
 * @description
 *  POFMatchX is a {@link POFMatch} with value/mask. <br>
 *  Value with mask presents an exact value. value and make
 *  are byte[] array stream, e.g. a ip prefix (192.168.*.*)
 *  in LPM table should presents as value=c0a8 and mask=ffff,
 *  OFMatchX.writeTo() method will add the '0's tail.
 * @date 2019-12-02
 */
public class POFMatchX {
    public static final int MINIMUM_LENGTH = 40;

    protected String fieldName;
    protected short fieldId;
    protected short offset;     //bit
    protected short length;     //bit

    protected byte[] value;
    protected byte[] mask;

    public POFMatchX(){}

    public POFMatchX(POFMatch match, byte[] value, byte[] mask){
        this.fieldName = match.getFieldName();
        this.fieldId = match.getFieldId();
        this.offset  = match.getOffset();
        this.length  = match.getLength();

        this.value = value;
        this.mask = mask;
    }

    public void readFrom(ByteBuf data){
        this.fieldId = data.readShort();
        this.offset  = data.readShort();
        this.length  = data.readShort();
        data.readShort();

        value = new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE];
        data.readBytes(value);

        mask = new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE];
        data.readBytes(mask);
    }

    public void writeTo(ByteBuf data){
        data.writeShort(fieldId);
        data.writeShort(offset);
        data.writeShort(length);
        data.writeShort((short) 0);

        if(value == null){
            data.writeBytes(new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE]);
        }else{
            if(value.length > OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE){
                data.writeBytes(value, 0, OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE);
            }else{
                data.writeBytes(value);
                data.writeBytes(new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE - value.length]);
            }
        }

        if(mask == null){
            data.writeBytes(new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE]);
        }else{
            if(mask.length > OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE){
                data.writeBytes(mask, 0, OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE);
            }else{
                data.writeBytes(mask);
                data.writeBytes(new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE - mask.length]);
            }
        }
    }

    public String toBytesString(){
        String string = HexString.toHex(fieldId) +
                HexString.toHex(offset) +
                HexString.toHex(length) +
                HexString.getByteZeros(2);


        if(value == null){
            string += HexString.getByteZeros(OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE);
        }else{
            if(value.length > OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE){
                string += HexString.toHex(value, 0, OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE);
                string += HexString.getZeros(0);
            }else{
                string += HexString.toHex(value);
                //string += HexString.ZeroEnd(0);
                string += HexString.getByteZeros(OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE - value.length );
            }
        }

        if(mask == null){
            string += HexString.getByteZeros(OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE);
        }else{
            if(mask.length > OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE){
                string += HexString.toHex(mask, 0, OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE);
                string += HexString.getZeros(0);
            }else{
                string += HexString.toHex(mask);
                string += HexString.getByteZeros(OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE - mask.length);
            }
        }
        return string;
    }

    public String toString(){
        return ";fid=" + fieldId +
                ";ofst=" + offset +
                ";len=" + length +
                ";val=" + HexString.toHex(value) +
                ";mask=" + HexString.toHex(mask);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fieldId;
        result = prime * result + length;
        result = prime * result + Arrays.hashCode(mask);
        result = prime * result + offset;
        result = prime * result + Arrays.hashCode(value);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        POFMatchX other = (POFMatchX) obj;
        if (fieldId != other.fieldId)
            return false;
        if (length != other.length)
            return false;
        if (!Arrays.equals(mask, other.mask))
            return false;
        if (offset != other.offset)
            return false;
        if (!Arrays.equals(value, other.value))
            return false;
        return true;
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
    public void setLength(short length) {
        this.length = length;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public byte[] getMask() {
        return mask;
    }

    public void setMask(byte[] mask) {
        this.mask = mask;
    }

    public String getFullHexValue(){
        String fullHexValueString = "";
        if(value == null){
            fullHexValueString += HexString.getZeros((length - 1)/8 + 1);
        }else{
            if(value.length >= OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE){
                fullHexValueString += HexString.toHex(value, 0, OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE);
            }else{
                fullHexValueString += HexString.toHex(value);
            }
        }

        return fullHexValueString;
    }

    public String getFullHexMask(){
        String fullHexMaskString = "";
        if(mask == null){
            fullHexMaskString += HexString.getZeros((length - 1)/8 + 1);
        }else{
            if(mask.length >= OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE){
                fullHexMaskString += HexString.toHex(mask, 0, OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE);
            }else{
                fullHexMaskString += HexString.toHex(mask);
            }
        }

        return fullHexMaskString;
    }

    @Override
    public POFMatchX clone() throws CloneNotSupportedException {
        POFMatchX matchX = (POFMatchX) super.clone();
        if(null != value){
            matchX.setValue(value.clone());
        }
        if(null != mask){
            matchX.setMask(mask.clone());
        }
        return matchX;
    }
}
