package openflow.protocol.msg.statistics;

import io.netty.buffer.ByteBuf;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public class OFQueueStatisticsRequest implements OFStatistics {
    protected short portNumber;
    protected int queueId;

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

    @Override
    public int getLength() {
        return 8;
    }

    @Override
    public void readFrom(ByteBuf data) {
        this.portNumber = data.readShort();
        data.readShort(); // pad
        this.queueId = data.readInt();
    }

    @Override
    public void writeTo(ByteBuf data) {
        data.writeShort(this.portNumber);
        data.writeShort((short) 0); // pad
        data.writeInt(this.queueId);
    }

    @Override
    public int hashCode() {
        final int prime = 443;
        int result = 1;
        result = prime * result + portNumber;
        result = prime * result + queueId;
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
        if (!(obj instanceof OFQueueStatisticsRequest)) {
            return false;
        }
        OFQueueStatisticsRequest other = (OFQueueStatisticsRequest) obj;
        if (portNumber != other.portNumber) {
            return false;
        }
        return queueId == other.queueId;
    }
}
