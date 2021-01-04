package openflow.protocol.msg.instruction;

import io.netty.buffer.ByteBuf;
import openflow.protocol.OFGlobal;
import util.HexString;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFInstruction implements Cloneable {
    public static final int MINIMUM_LENGTH = 8;
    public static final int MAXIMAL_LENGTH = OFGlobal.OFP_MAX_INSTRUCTION_LENGTH;

    protected OFInstructionType type;
    protected short length;

    public OFInstruction()
    {
        type = OFInstructionType.GOTO_TABLE;
    }

    public OFInstructionType getType() {
        return type;
    }

    public void setType(OFInstructionType type) {
        this.type = type;
    }

    public short getLength() {
        return length;
    }

    public int getLengthU() {
        //return U16.f(length);
        return MAXIMAL_LENGTH;
    }

    public void setLength(short length) {
        this.length = length;
    }

    public String toBytesString(){
        return HexString.toHex(type.getTypeValue()) +
                HexString.toHex(length) +
                " " +
                HexString.getByteZeros(4);
    }

    public String toString() {
        return "pofinstruction:" +
                "t=" + this.getType() +
                ";l=" + this.getLength();
    }

    public void readFrom(ByteBuf data) {
        this.type = OFInstructionType.valueOf(data.readShort());
        this.length = data.readShort();
        data.readInt();
    }

    public void writeTo(ByteBuf data) {
        data.writeShort(type.getTypeValue());
        data.writeShort(length);
        data.writeInt(0);
    }

    @Override
    public int hashCode() {
        final int prime = 347;
        int result = 1;
        result = prime * result + length;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof OFInstruction)) {
            return false;
        }
        OFInstruction other = (OFInstruction) obj;
        if (length != other.length) {
            return false;
        }
        if (type == null) {
            return other.type == null;
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public OFInstruction clone() throws CloneNotSupportedException {
        return (OFInstruction) super.clone();
    }
}
