package openflow.protocol.msg.instruction;

import io.netty.buffer.ByteBuf;
import openflow.protocol.match.POFMatch;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFInstructionMovePacketOffset extends OFInstruction {
    public static final int MINIMUM_LENGTH = OFInstruction.MINIMUM_LENGTH + 8 + POFMatch.MINIMUM_LENGTH;
    protected byte direction;		//0: forward; 1: backward
    protected byte valueType;
    protected int move_value;
    protected POFMatch move_field;

    public OFInstructionMovePacketOffset(){
        super.setType(OFInstructionType.MOVE_PACKET_POFFSET);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public short getLength() {
        return (short) MINIMUM_LENGTH;
    }


    @Override
    public void readFrom(ByteBuf data){
        super.readFrom(data);

        direction = data.readByte();
        valueType = data.readByte();
        data.readBytes(new byte[6]);

        if(valueType == 0){
            move_value = data.readInt();
            data.readBytes(new byte[4]);
            move_field = null;
        }else if(valueType == 1){
            move_value = 0;
            move_field = new POFMatch();
            move_field.readFrom(data);
        }else{
            move_value = 0;
            move_field = null;
            data.readBytes(new byte[POFMatch.MINIMUM_LENGTH]);
        }
    }

    @Override
    public void writeTo(ByteBuf data){
        super.writeTo(data);

        data.writeByte(direction);
        data.writeByte(valueType);
        data.writeBytes(new byte[6]);

        if(valueType == 0){
            data.writeInt(move_value);
            data.writeBytes(new byte[4]);
        }else if(valueType == 1 && move_field != null){
            move_field.writeTo(data);
        }else{
            data.writeBytes(new byte[POFMatch.MINIMUM_LENGTH]);
        }
    }

    public byte getDirection() {
        return direction;
    }

    public void setDirection(byte direction) {
        this.direction = direction;
    }

    public byte getValueType() {
        return valueType;
    }

    public void setValueType(byte valueType) {
        this.valueType = valueType;
    }

    public int getMove_value() {
        return move_value;
    }

    public void setMove_value(int move_value) {
        this.move_value = move_value;
    }

    public POFMatch getMove_field() {
        return move_field;
    }

    public void setMove_field(POFMatch move_field) {
        this.move_field = move_field;
    }

    @Override
    public String toString() {
        return
                super.toString() + "," +
                        "OFInstructionMovePacketOffset [direction=" + direction + ", valueType=" + valueType + ", move_value="
                        + move_value + ", move_field=" + move_field + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + direction;
        result = prime * result + ((move_field == null) ? 0 : move_field.hashCode());
        result = prime * result + move_value;
        result = prime * result + valueType;
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
        OFInstructionMovePacketOffset other = (OFInstructionMovePacketOffset) obj;
        if (direction != other.direction)
            return false;
        if (move_field == null) {
            if (other.move_field != null)
                return false;
        } else if (!move_field.equals(other.move_field))
            return false;
        if (move_value != other.move_value)
            return false;
        return valueType == other.valueType;
    }
}
