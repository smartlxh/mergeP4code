package openflow.protocol.msg.statistics;

import io.netty.buffer.ByteBuf;

/**
 * @author twd
 * @description The base class for all pof statistics.
 * @date 2019-12-03
 */
public interface OFStatistics {
    /**
     * Returns the wire length of this message in bytes
     * @return the length
     */
    public int getLength();

    /**
     * Read this message off the wire from the specified ByteBuffer
     * @param data
     */
    public void readFrom(ByteBuf data);

    /**
     * Write this message's binary format to the specified ByteBuffer
     * @param data
     */
    public void writeTo(ByteBuf data);
}
