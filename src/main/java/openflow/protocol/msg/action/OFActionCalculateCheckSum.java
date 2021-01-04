package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;

/**
 * re-calculate the checksum start from {@link #calcStartPosition} with length {@link #calcLength},
 * and write the result to {@link #checksumPosition} with length {@link #checksumLength}
 *
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 */
public class OFActionCalculateCheckSum extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + 16;
    protected byte  checksumPosType;    //0:packet 1:metadata
    protected byte  calcPosType;        //0:packet 1:metadata
    protected short checksumPosition;   //bit
    protected short checksumLength;     //bit
    protected short calcStartPosition;  //bit
    protected short calcLength;         //bit

    public OFActionCalculateCheckSum(){
        super.setType(OFActionType.CALCULATE_CHECKSUM);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public void readFrom(ByteBuf data){
        super.readFrom(data);
        checksumPosType = data.readByte();
        calcPosType = data.readByte();
        checksumPosition = data.readShort();
        checksumLength = data.readShort();
        calcStartPosition = data.readShort();
        calcLength = data.readShort();
        data.readBytes(new byte[6]);
    }

    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeByte(checksumPosType);
        data.writeByte(calcPosType);
        data.writeShort(checksumPosition);
        data.writeShort(checksumLength);
        data.writeShort(calcStartPosition);
        data.writeShort(calcLength);
        data.writeBytes(new byte[6]);
    }



    public String toString(){
        return super.toString() +
                ";checksumPosType=" + checksumPosType +
                ";calcPosType=" + calcPosType +
                ";ckpos=" + checksumPosition +
                ";cklen=" + checksumLength +
                ";clpos=" + calcStartPosition +
                ";cllen=" + calcLength;
    }
    public byte getChecksumPosType()
    {
        return checksumPosType;
    }
    public void setChecksumPosType(byte checksumPosType)
    {
        this.checksumPosType=checksumPosType;
    }
    public byte getCalcPosType()
    {
        return calcPosType;
    }
    public void setCalcPosType(byte calcPosType)
    {
        this.calcPosType=calcPosType;
    }
    public short getChecksumPosition() {
        return checksumPosition;
    }

    public void setChecksumPosition(short checksumPosition) {
        this.checksumPosition = checksumPosition;
    }

    public short getChecksumLength() {
        return checksumLength;
    }

    public void setChecksumLength(short checksumLength) {
        this.checksumLength = checksumLength;
    }

    public short getCalcStartPosition() {
        return calcStartPosition;
    }

    public void setCalcStartPosition(short calcStartPosition) {
        this.calcStartPosition = calcStartPosition;
    }

    public short getCalcLength() {
        return calcLength;
    }

    public void setCalcLength(short calcLength) {
        this.calcLength = calcLength;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + checksumPosType;
        result = prime * result + calcPosType;
        result = prime * result + calcLength;
        result = prime * result + calcStartPosition;
        result = prime * result + checksumLength;
        result = prime * result + checksumPosition;
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
        OFActionCalculateCheckSum other = (OFActionCalculateCheckSum) obj;
        if (checksumPosType != other.checksumPosType)
            return false;
        if (calcPosType != other.calcPosType)
            return false;
        if (calcLength != other.calcLength)
            return false;
        if (calcStartPosition != other.calcStartPosition)
            return false;
        if (checksumLength != other.checksumLength)
            return false;
        if (checksumPosition != other.checksumPosition)
            return false;
        return true;
    }
}
