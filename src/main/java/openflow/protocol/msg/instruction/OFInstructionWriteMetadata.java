package openflow.protocol.msg.instruction;

import io.netty.buffer.ByteBuf;
import openflow.protocol.OFGlobal;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFInstructionWriteMetadata extends OFInstruction {
    // the length should be more elegant
    public static int MINIMUM_LENGTH = OFInstruction.MINIMUM_LENGTH + 8 + OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE;

    protected short metadataOffset;     //bit
    protected short writeLength;        //bit
    protected byte[] value;

    public OFInstructionWriteMetadata(){
        super.setType(OFInstructionType.WRITE_METADATA);
        super.setLength((short)MINIMUM_LENGTH);
    }

    public short getLength() {
        return (short) MINIMUM_LENGTH;
    }


    @Override
    public void readFrom(ByteBuf data){
        super.readFrom(data);
        metadataOffset = data.readShort();
        writeLength = data.readShort();
        value=new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE];
        data.readBytes(value);
        data.readBytes(new byte[4]);
    }

    @Override
    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeShort(metadataOffset);
        data.writeShort(writeLength);
        if(value==null)
        {
            data.writeBytes(new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE]);
        }
        else{
            if(value.length> OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE)
                data.writeBytes(value,0, OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE);
            else{
                data.writeBytes(value);
                data.writeBytes(new byte[OFGlobal.OFP_MAX_FIELD_LENGTH_IN_BYTE-value.length]);
            }
        }
        data.writeBytes(new byte[4]);
    }


    @Override
    public String toString(){
        return super.toString() +
                ";mos=" + metadataOffset +
                ";wl=" + writeLength +
                ";val=" + value;
    }

    public short getMetadataOffset() {
        return metadataOffset;
    }

    public void setMetadataOffset(short metadataOffset) {
        this.metadataOffset = metadataOffset;
    }

    public short getWriteLength() {
        return writeLength;
    }

    public void setWriteLength(short writeLength) {
        this.writeLength = writeLength;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + writeLength;
        result = prime * result + metadataOffset;
        //璁＄畻涓�釜value鐨勫彇鍊煎摝
        int temp=0;
        for(byte t:value)
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
        OFInstructionWriteMetadata other = (OFInstructionWriteMetadata) obj;
        if (writeLength != other.writeLength)
            return false;
        if (metadataOffset != other.metadataOffset)
            return false;
        return value == other.value;
    }
}
