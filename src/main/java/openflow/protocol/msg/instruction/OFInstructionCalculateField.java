package openflow.protocol.msg.instruction;

import io.netty.buffer.ByteBuf;
import openflow.protocol.match.POFMatch;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFInstructionCalculateField extends OFInstruction {
    public enum OFCalcType{
        OFPCT_ADD,				// +
        OFPCT_SUBTRACT,			// -
        OFPCT_LEFT_SHIFT,		// <<
        OFPCT_RIGHT_SHIFT,		// >>
        OFPCT_BITWISE_ADD,		// &
        OFPCT_BITWISE_OR,		// |
        OFPCT_BITWISE_XOR,		// ^
        OFPCT_BITWISE_NOR,
    }

    public static final int MINIMUM_LENGTH = OFInstruction.MINIMUM_LENGTH + 8 + POFMatch.MINIMUM_LENGTH + POFMatch.MINIMUM_LENGTH;
    protected OFCalcType calcType;
    protected byte src_valueType;				//0: use srcField_Value; 1: use srcField;
    protected POFMatch des_field;
    protected int src_value;
    protected POFMatch src_field;

    public OFInstructionCalculateField(){
        super.setType(OFInstructionType.CALCULATE_FIELD);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public short getLength() {
        return (short) MINIMUM_LENGTH;
    }

    @Override
    public void readFrom(ByteBuf data){
        super.readFrom(data);
        short ct = data.readShort();
        if(ct >= 0 && ct < OFCalcType.values().length){
            calcType = OFCalcType.values()[ct];
        }else{
            calcType = null;
        }
        src_valueType = data.readByte();
        data.readBytes(new byte[5]);
        des_field = new POFMatch();
        des_field.readFrom(data);

        if(src_valueType == 0){
            src_value = data.readInt();
            data.readBytes(new byte[4]);
            src_field = null;
        }else if(src_valueType == 1){
            src_value = 0;
            src_field = new POFMatch();
            src_field.readFrom(data);
        }else{
            src_value = 0;
            src_field = null;
            data.readBytes(new byte[POFMatch.MINIMUM_LENGTH]);
        }
    }

    @Override
    public void writeTo(ByteBuf data){
        super.writeTo(data);
        if(calcType != null){
            data.writeShort((short)calcType.ordinal());
        }else{
            data.writeShort((short) 0);
        }
        data.writeByte(src_valueType);
        data.writeBytes(new byte[5]);
        if(des_field != null){
            des_field.writeTo(data);
        }else{
            data.writeBytes(new byte[POFMatch.MINIMUM_LENGTH]);
        }
        if(src_valueType == 0){
            data.writeInt(src_value);
            data.writeBytes(new byte[4]);
        }else if(src_valueType == 1 && src_field != null){
            src_field.writeTo(data);
        }else{
            data.writeBytes(new byte[POFMatch.MINIMUM_LENGTH]);
        }
    }


    public OFCalcType getCalcType() {
        return calcType;
    }

    public void setCalcType(OFCalcType calcType) {
        this.calcType = calcType;
    }

    public byte getSrc_valueType() {
        return src_valueType;
    }

    public void setSrc_valueType(byte src_valueType) {
        this.src_valueType = src_valueType;
    }

    public POFMatch getDes_field() {
        return des_field;
    }

    public void setDes_field(POFMatch des_field) {
        this.des_field = des_field;
    }

    public int getSrc_value() {
        return src_value;
    }

    public void setSrc_value(int src_value) {
        this.src_value = src_value;
    }

    public POFMatch getSrc_field() {
        return src_field;
    }

    public void setSrc_field(POFMatch src_field) {
        this.src_field = src_field;
    }

    @Override
    public String toString() {
        return super.toString() + "," +
                "POFInstructionCalculateField [calcType=" + calcType + ", src_valueType=" + src_valueType + ", des_field="
                + des_field + ", src_value=" + src_value + ", src_field=" + src_field + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((calcType == null) ? 0 : calcType.hashCode());
        result = prime * result + ((des_field == null) ? 0 : des_field.hashCode());
        result = prime * result + ((src_field == null) ? 0 : src_field.hashCode());
        result = prime * result + src_value;
        result = prime * result + src_valueType;
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
        OFInstructionCalculateField other = (OFInstructionCalculateField) obj;
        if (calcType != other.calcType)
            return false;
        if (des_field == null) {
            if (other.des_field != null)
                return false;
        } else if (!des_field.equals(other.des_field))
            return false;
        if (src_field == null) {
            if (other.src_field != null)
                return false;
        } else if (!src_field.equals(other.src_field))
            return false;
        if (src_value != other.src_value)
            return false;
        if (src_valueType != other.src_valueType)
            return false;
        return true;
    }
}
