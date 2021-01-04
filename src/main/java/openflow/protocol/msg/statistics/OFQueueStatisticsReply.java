package openflow.protocol.msg.statistics;

import io.netty.buffer.ByteBuf;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public class OFQueueStatisticsReply implements OFStatistics {
    protected short portNumber;
    protected int queueId;
    protected long transmitBytes;
    protected long transmitPackets;
    protected long transmitErrors;

    /**
     * @return the portNumber
     */
    public short getPortNumber() {
        return portNumber;
    }

    /**
     * @param portNumber the portNumber to set
     */
    public void setPortNumber(short portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * @return the queueId
     */
    public int getQueueId() {
        return queueId;
    }

    /**
     * @param queueId the queueId to set
     */
    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    /**
     * @return the transmitBytes
     */
    public long getTransmitBytes() {
        return transmitBytes;
    }

    /**
     * @param transmitBytes the transmitBytes to set
     */
    public void setTransmitBytes(long transmitBytes) {
        this.transmitBytes = transmitBytes;
    }

    /**
     * @return the transmitPackets
     */
    public long getTransmitPackets() {
        return transmitPackets;
    }

    /**
     * @param transmitPackets the transmitPackets to set
     */
    public void setTransmitPackets(long transmitPackets) {
        this.transmitPackets = transmitPackets;
    }

    /**
     * @return the transmitErrors
     */
    public long getTransmitErrors() {
        return transmitErrors;
    }

    /**
     * @param transmitErrors the transmitErrors to set
     */
    public void setTransmitErrors(long transmitErrors) {
        this.transmitErrors = transmitErrors;
    }

    @Override
    public int getLength() {
        return 32;
    }

    @Override
    public void readFrom(ByteBuf data) {
        this.portNumber = data.readShort();
        data.readShort(); // pad
        this.queueId = data.readInt();
        this.transmitBytes = data.readLong();
        this.transmitPackets = data.readLong();
        this.transmitErrors = data.readLong();
    }

    @Override
    public void writeTo(ByteBuf data) {
        data.writeShort(this.portNumber);
        data.writeShort((short) 0); // pad
        data.writeInt(this.queueId);
        data.writeLong(this.transmitBytes);
        data.writeLong(this.transmitPackets);
        data.writeLong(this.transmitErrors);
    }

    @Override
    public int hashCode() {
        final int prime = 439;
        int result = 1;
        result = prime * result + portNumber;
        result = prime * result + queueId;
        result = prime * result
                + (int) (transmitBytes ^ (transmitBytes >>> 32));
        result = prime * result
                + (int) (transmitErrors ^ (transmitErrors >>> 32));
        result = prime * result
                + (int) (transmitPackets ^ (transmitPackets >>> 32));
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
        if (!(obj instanceof OFQueueStatisticsReply)) {
            return false;
        }
        OFQueueStatisticsReply other = (OFQueueStatisticsReply) obj;
        if (portNumber != other.portNumber) {
            return false;
        }
        if (queueId != other.queueId) {
            return false;
        }
        if (transmitBytes != other.transmitBytes) {
            return false;
        }
        if (transmitErrors != other.transmitErrors) {
            return false;
        }
        return transmitPackets == other.transmitPackets;
    }
}
