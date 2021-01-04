package openflow.protocol.msg.statistics;

import io.netty.buffer.ByteBuf;
import openflow.protocol.match.POFMatch;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public class OFAggregateStatisticsRequest implements OFStatistics {
    protected POFMatch match;
    protected byte tableId;
    protected short outPort;

    /**
     * @return the match
     */
    public POFMatch getMatch() {
        return match;
    }

    /**
     * @param match the match to set
     */
    public void setMatch(POFMatch match) {
        this.match = match;
    }

    /**
     * @return the tableId
     */
    public byte getTableId() {
        return tableId;
    }

    /**
     * @param tableId the tableId to set
     */
    public void setTableId(byte tableId) {
        this.tableId = tableId;
    }

    /**
     * @return the outPort
     */
    public short getOutPort() {
        return outPort;
    }

    /**
     * @param outPort the outPort to set
     */
    public void setOutPort(short outPort) {
        this.outPort = outPort;
    }

    @Override
    public int getLength() {
        return 44;
    }

    @Override
    public void readFrom(ByteBuf data) {
        if (this.match == null)
            this.match = new POFMatch();
        this.match.readFrom(data);
        this.tableId = data.readByte();
        data.readByte(); // pad
        this.outPort = data.readShort();
    }

    @Override
    public void writeTo(ByteBuf data) {
        this.match.writeTo(data);
        data.writeByte(this.tableId);
        data.writeByte((byte) 0);
        data.writeShort(this.outPort);
    }

    @Override
    public int hashCode() {
        final int prime = 401;
        int result = 1;
        result = prime * result + ((match == null) ? 0 : match.hashCode());
        result = prime * result + outPort;
        result = prime * result + tableId;
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
        if (!(obj instanceof OFAggregateStatisticsRequest)) {
            return false;
        }
        OFAggregateStatisticsRequest other = (OFAggregateStatisticsRequest) obj;
        if (match == null) {
            if (other.match != null) {
                return false;
            }
        } else if (!match.equals(other.match)) {
            return false;
        }
        if (outPort != other.outPort) {
            return false;
        }
        return tableId == other.tableId;
    }
}
