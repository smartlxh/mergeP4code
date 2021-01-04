package openflow.protocol.msg.statistics;

import io.netty.buffer.ByteBuf;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public class OFVendorStatistics implements OFStatistics {
    protected int vendor;
    protected byte[] body;

    // non-message fields
    protected int length = 0;

    @Override
    public void readFrom(ByteBuf data) {
        this.vendor = data.readInt();
        if (body == null)
            body = new byte[length - 4];
        data.readBytes(body);
    }

    @Override
    public void writeTo(ByteBuf data) {
        data.writeInt(this.vendor);
        if (body != null)
            data.writeBytes(body);
    }

    @Override
    public int hashCode() {
        final int prime = 457;
        int result = 1;
        result = prime * result + vendor;
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
        if (!(obj instanceof OFVendorStatistics)) {
            return false;
        }
        OFVendorStatistics other = (OFVendorStatistics) obj;
        return vendor == other.vendor;
    }

    @Override
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
