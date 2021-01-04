package openflow.protocol.msg.instruction;

import io.netty.buffer.ByteBuf;
import util.HexString;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFInstructionMeter extends OFInstruction {
    public static int MINIMUM_LENGTH = OFInstruction.MINIMUM_LENGTH + 8;

    protected int meterId;

    public OFInstructionMeter(){
        super.setType(OFInstructionType.METER);
        super.setLength((short)MINIMUM_LENGTH);
    }

    public short getLength() {
        return (short) MINIMUM_LENGTH;
    }

    @Override
    public void readFrom(ByteBuf data){
        super.readFrom(data);
        meterId = data.readInt();
        data.readBytes(new byte[4]);
    }

    @Override
    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeInt(meterId);
        data.writeBytes(new byte[4]);
    }

    @Override
    public String toBytesString(){
        return super.toBytesString() +
                HexString.toHex(meterId) +
                HexString.getByteZeros(4);
    }

    @Override
    public String toString(){
        return super.toString() +
                ";mid=" + meterId;
    }

    public int getMeterId() {
        return meterId;
    }

    public void setMeterId(int meterId) {
        this.meterId = meterId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + meterId;
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
        OFInstructionMeter other = (OFInstructionMeter) obj;
        if (meterId != other.meterId)
            return false;
        return true;
    }
}
