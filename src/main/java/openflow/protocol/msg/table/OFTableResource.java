package openflow.protocol.msg.table;

import io.netty.buffer.ByteBuf;
import util.HexString;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public class OFTableResource {
    public static int MINIMUM_LENGTH = 16;

    protected int deviceId;
    protected OFTableType tableType;
    protected byte tableNum;
    protected short keyLength;

    protected int totalSize;

    public void readFrom(ByteBuf data) {
        this.deviceId   = data.readInt();
        this.tableType  = OFTableType.values()[data.readByte()];
        this.tableNum   = data.readByte();
        this.keyLength  = data.readShort();

        this.totalSize  = data.readInt();
        data.readBytes(new byte[4]);
    }

    // add by qsmywxd and remain to discuss
    public void writeZero(ByteBuf data, int length)
    {
        data.writeBytes(new byte[length]);
    }

    public void writeTo(ByteBuf data){
        data.writeInt(this.deviceId);
        data.writeByte(this.tableType.getValue());
        data.writeByte(this.tableNum);
        data.writeShort(this.keyLength);

        data.writeInt(this.totalSize);
        writeZero(data,4);
    }

    public String toBytesString(){
        String bytesString = HexString.toHex(deviceId);

        bytesString += HexString.toHex(tableType.getValue());
        bytesString += HexString.toHex(tableNum);
        bytesString += HexString.toHex(keyLength);
        bytesString += " ";

        bytesString += HexString.toHex(totalSize);
        bytesString += HexString.getByteZeros(4);

        return bytesString;
    }

    @Override
    public String toString(){
        return "did=" + this.deviceId +
                ";tt=" + this.tableType +
                ";tn=" + this.tableNum +
                ";kl=" + this.keyLength +
                ";ts=" + this.totalSize;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public OFTableType getTableType() {
        return tableType;
    }

    public void setTableType(OFTableType tableType) {
        this.tableType = tableType;
    }

    public byte getTableNum() {
        return tableNum;
    }

    public void setTableNum(byte tableNum) {
        this.tableNum = tableNum;
    }

    public short getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(short keyLength) {
        this.keyLength = keyLength;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + deviceId;
        result = prime * result + keyLength;
        result = prime * result + tableNum;
        result = prime * result + ((tableType == null) ? 0 : tableType.hashCode());
        result = prime * result + totalSize;
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
        OFTableResource other = (OFTableResource) obj;
        if (deviceId != other.deviceId)
            return false;
        if (keyLength != other.keyLength)
            return false;
        if (tableNum != other.tableNum)
            return false;
        if (tableType != other.tableType)
            return false;
        return totalSize == other.totalSize;
    }
}
