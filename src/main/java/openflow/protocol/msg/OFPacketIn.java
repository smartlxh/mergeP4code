package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;
import openflow.protocol.OFGlobal;
import util.Converter;
import util.HexString;

import java.util.Arrays;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFPacketIn extends OFMessage {
    public static int MINIMUM_LENGTH = (short) (OFMessage.MINIMUM_LENGTH + 24); //32
    public static int MAXIMAL_LENGTH = (short) (OFPacketIn.MINIMUM_LENGTH + OFGlobal.OFP_PACKET_IN_MAX_LENGTH);//2080;

    public enum OFPacketInReason {
        OFPR_NO_MATCH,
        OFPR_ACTION,
        OFPR_INVALID_TTL
    }

    protected int bufferId;
    protected short totalLength;
    protected OFPacketInReason reason;
    protected byte tableId;

    protected long cookie;

    protected int deviceId;

    protected byte[] packetData;

    protected short slotId;
    protected short portId;

    public OFPacketIn() {
        super();
        this.type = OFType.PACKET_IN;
        this.length = Converter.convertIntToShort(MINIMUM_LENGTH);
    }

    /**
     * Get buffer_id
     * @return bufferId
     */
    public int getBufferId() {
        return this.bufferId;
    }

    /**
     * Set buffer_id
     * @param bufferId
     */
    public OFPacketIn setBufferId(int bufferId) {
        this.bufferId = bufferId;
        return this;
    }

    /**
     * Returns the packet data
     * @return packetData
     */
    public byte[] getPacketData() {
        return this.packetData;
    }

    /**
     * Sets the packet data, and updates the length of this message
     * @param packetData
     */
    public OFPacketIn setPacketData(byte[] packetData) {
        this.packetData = packetData;
        this.totalLength = (short) packetData.length;
        this.length = Converter.convertIntToShort(OFPacketIn.MINIMUM_LENGTH + packetData.length);
        return this;
    }

    public short getInPort() {
        return this.portId;
    }
    /**
     * Set in_port
     * @param inPort
     */
    public OFPacketIn setInPort(short inPort) {
        this.portId = inPort;
        return this;
    }

    /**
     * Get reason
     * @return reason
     */
    public OFPacketInReason getReason() {
        return this.reason;
    }

    /**
     * Set reason
     * @param reason
     */
    public OFPacketIn setReason(OFPacketInReason reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Get total_len
     * @return totalLength
     */
    public short getTotalLength() {
        return this.totalLength;
    }

    /**
     * Set total_len
     * @param totalLength
     */
    public OFPacketIn setTotalLength(short totalLength) {
        this.totalLength = totalLength;
        return this;
    }

    public byte getTableId() {
        return tableId;
    }

    public void setTableId(byte tableId) {
        this.tableId = tableId;
    }

    public long getCookie() {
        return cookie;
    }

    public void setCookie(long cookie) {
        this.cookie = cookie;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }



    public short getSlotId() {
        return slotId;
    }

    public void setSlotId(short slotId) {
        this.slotId = slotId;
    }

    public short getPortId() {
        return portId;
    }

    public void setPortId(short portId) {
        this.portId = portId;
    }

    @Override
    public void readFrom(ByteBuf data) {
        super.readFrom(data);
        this.bufferId = data.readInt();
        this.totalLength = data.readShort();
        this.reason = OFPacketInReason.values()[Converter.convertByteToShort(data.readByte())];
        this.tableId = data.readByte();

        this.cookie = data.readLong();

        this.deviceId = data.readInt();
        //data.readBytes(new byte[4]);
        this.slotId = data.readShort();
        this.portId = data.readShort();
        this.packetData = new byte[totalLength];

        //this.packetData = new byte[OFGlobal.OFP_PACKET_IN_MAX_LENGTH];
        data.readBytes(this.packetData);
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);
        data.writeInt(bufferId);
        data.writeShort(totalLength);
        data.writeByte((byte) reason.ordinal());
        data.writeByte(tableId);
        data.writeLong(cookie);
        data.writeInt(deviceId);
        // data.writeBytes(new byte[4]);
        data.writeShort(slotId);
        data.writeShort(portId);

        if(null != packetData){
            if(packetData.length > OFGlobal.OFP_PACKET_IN_MAX_LENGTH){
                data.writeBytes(this.packetData, 0, OFGlobal.OFP_PACKET_IN_MAX_LENGTH);
            }else{

                data.writeBytes(this.packetData, 0, this.totalLength);
            }
        }else{
            data.writeBytes(new byte[totalLength]);
        }

    }



    public String toString() {
        String myStr = super.toString();
        return myStr +
                "; packetIn:" +
                "bufferId=" + Converter.convertIntToLong(this.bufferId) +
                ";tl=" + totalLength +
                ";rz=" + reason +
                ";tid=" + tableId +
                ";ck=" + cookie +
                ";did=" + deviceId +
                ";data=" + HexString.toHex(packetData);
    }

    @Override
    public int hashCode() {
        final int prime = 283;
        int result = super.hashCode();
        result = prime * result + bufferId;
        result = prime * result + totalLength;
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + tableId;
        result = prime * result + (int) (cookie ^ (cookie >>> 32));
        result = prime * result + deviceId;
        result = prime * result + Arrays.hashCode(packetData);

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof OFPacketIn)) {
            return false;
        }
        OFPacketIn other = (OFPacketIn) obj;
        if (bufferId != other.bufferId) {
            return false;
        }
        if (totalLength != other.totalLength) {
            return false;
        }
        if (reason == null) {
            if (other.reason != null) {
                return false;
            }
        } else if (!reason.equals(other.reason)) {
            return false;
        }

        if (tableId != other.tableId) {
            return false;
        }
        if (cookie != other.cookie) {
            return false;
        }
        if (deviceId != other.deviceId) {
            return false;
        }

        return Arrays.equals(packetData, other.packetData);
    }

    public void computeLength() {
        this.length=(short) (OFPacketIn.MINIMUM_LENGTH + this.totalLength);
    }
}
