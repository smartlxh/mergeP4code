package openflow.protocol.msg.instruction;

import io.netty.buffer.ByteBuf;
import openflow.protocol.match.POFMatch;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFInstructionGotoDirectTable extends OFInstruction {
    public static final int MINIMUM_LENGTH = OFInstruction.MINIMUM_LENGTH + 8 + POFMatch.MINIMUM_LENGTH;

    protected byte nextTableId;
    protected byte indexType;	//0: value; 1: field
    protected short packetOffset;       //byte
    protected int indexValue;
    protected POFMatch indexField;



    public OFInstructionGotoDirectTable(){
        super.setType(OFInstructionType.GOTO_DIRECT_TABLE);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public short getLength() {
        return (short) MINIMUM_LENGTH;
    }

    public byte getNextTableId()
    {
        return nextTableId;
    }
    public void setNextTableId(byte nextTableId)
    {
        this.nextTableId=nextTableId;
    }
    public byte getIndexType()
    {
        return indexType;
    }
    public void setIndexType(byte indexType)
    {
        this.indexType = indexType;
    }
    public short getPacketOffset()
    {
        return packetOffset;
    }
    public void setPacketOffset(short packetOffset)
    {
        this.packetOffset = packetOffset;
    }
    public int getIndexValue()
    {
        return indexValue;
    }
    public void setIndexValue(int indexValue)
    {
        this.indexValue = indexValue;
    }
    public POFMatch getIndexField()
    {
        return this.indexField;
    }
    public void setIndexField(POFMatch indexField)
    {
        this.indexField = indexField;
    }
    @Override
    public void readFrom(ByteBuf data){
        super.readFrom(data);
        nextTableId = data.readByte();
        indexType = data.readByte();
        packetOffset = data.readShort();
        data.readBytes(new byte[4]);
        if(indexType == 0){
            indexValue = data.readInt();
            data.readBytes(new byte[4]);
            indexField = null;
        }else if(indexType == 1){
            indexField = new POFMatch();
            indexField.readFrom(data);
            this.indexValue = 0;
        }else{
            indexValue = 0;
            indexField = null;
            data.readBytes(new byte[POFMatch.MINIMUM_LENGTH]);
        }

    }

    @Override
    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeByte(nextTableId);
        data.writeByte(indexType);
        data.writeShort(packetOffset);
        data.writeBytes(new byte[4]);
        if(indexType == 0){
            data.writeInt(indexValue);
            data.writeBytes(new byte[4]);
        }else if(indexType == 1 && indexField != null){
            indexField.writeTo(data);
        }else{
            data.writeBytes(new byte[POFMatch.MINIMUM_LENGTH]);
        }

    }



    @Override
    public String toString(){
        return super.toString() +
                ";nextTableId=" + nextTableId +
                ";indexType=" + indexType +
                ";packetOffset=" + packetOffset +
                ";indexValue=" + indexValue +
                ";indexField=" + indexField ;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + nextTableId;
        result = prime * result + indexType;
        result = prime * result + packetOffset;
        result = prime * result + indexValue;
        result = prime * result + indexField.hashCode();
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
        OFInstructionGotoDirectTable other = (OFInstructionGotoDirectTable) obj;
        if (nextTableId != other.nextTableId)
            return false;
        if (indexType != other.indexType)
            return false;
        if (packetOffset != other.packetOffset)
            return false;
        if (indexValue != other.indexValue)
            return false;
        return indexField.equals(other.indexField);
    }
}
