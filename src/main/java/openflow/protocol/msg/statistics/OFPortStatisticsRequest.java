package openflow.protocol.msg.statistics;

import io.netty.buffer.ByteBuf;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public class OFPortStatisticsRequest implements OFStatistics {
    protected short portNumber;

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

    @Override
    public int getLength() {
        return 8;
    }

    @Override
    public void readFrom(ByteBuf data) {
        this.portNumber = data.readShort();
        data.readShort(); // pad
        data.readInt(); // pad
    }

    @Override
    public void writeTo(ByteBuf data) {
        data.writeShort(this.portNumber);
        data.writeShort((short) 0); // pad
        data.writeInt(0); // pad
    }

    @Override
    public int hashCode() {
        final int prime = 433;
        int result = 1;
        result = prime * result + portNumber;
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
        if (!(obj instanceof OFPortStatisticsRequest)) {
            return false;
        }
        OFPortStatisticsRequest other = (OFPortStatisticsRequest) obj;
        return portNumber == other.portNumber;
    }
}
