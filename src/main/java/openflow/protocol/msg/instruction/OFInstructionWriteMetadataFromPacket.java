package openflow.protocol.msg.instruction;

import io.netty.buffer.ByteBuf;
import util.HexString;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFInstructionWriteMetadataFromPacket extends OFInstruction {
    public static int MINIMUM_LENGTH = OFInstruction.MINIMUM_LENGTH + 8;

    protected short metadataOffset;     //bit
    protected short packetOffset;       //bit
    protected short writeLength;        //bit

    public OFInstructionWriteMetadataFromPacket(){
        super.setType(OFInstructionType.WRITE_METADATA_FROM_PACKET);
        super.setLength((short)MINIMUM_LENGTH);
    }

    public short getLength() {
        return (short) MINIMUM_LENGTH;
    }


    @Override
    public void readFrom(ByteBuf data){
        super.readFrom(data);
        metadataOffset = data.readShort();
        packetOffset = data.readShort();
        writeLength = data.readShort();
        data.readBytes(new byte[2]);
    }


    @Override
    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeShort(metadataOffset);
        data.writeShort(packetOffset);
        data.writeShort(writeLength);
        data.writeBytes(new byte[2]);
    }

    @Override
    public String toBytesString(){
        return super.toBytesString() +
                HexString.toHex(metadataOffset) +
                HexString.toHex(packetOffset) +
                " " +
                HexString.toHex(writeLength) +
                HexString.getByteZeros(2);
    }

    @Override
    public String toString(){
        return super.toString() +
                ";mos=" + metadataOffset +
                ";pos=" + packetOffset +
                ";wl" + writeLength;
    }

    public short getMetadataOffset() {
        return metadataOffset;
    }

    public void setMetadataOffset(short metadataOffset) {
        this.metadataOffset = metadataOffset;
    }

    public short getPacketOffset() {
        return packetOffset;
    }

    public void setPacketOffset(short packetOffset) {
        this.packetOffset = packetOffset;
    }

    public short getWriteLength() {
        return writeLength;
    }

    public void setWriteLength(short writeLength) {
        this.writeLength = writeLength;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + writeLength;
        result = prime * result + metadataOffset;
        result = prime * result + packetOffset;
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
        OFInstructionWriteMetadataFromPacket other = (OFInstructionWriteMetadataFromPacket) obj;
        if (writeLength != other.writeLength)
            return false;
        if (metadataOffset != other.metadataOffset)
            return false;
        return packetOffset == other.packetOffset;
    }
}
