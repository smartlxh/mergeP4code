package openflow.protocol.experimenter;

import io.netty.buffer.ByteBuf;

/**
 * The base class for all vendor data.
 *
 * @author Rob Vaterlaus (rob.vaterlaus@bigswitch.com)
 */
public interface OFExperimenterData {
    /**
     * @return length of the data
     */
    public int getLength();

    /**
     * Read the vendor data from the specified ChannelBuffer
     * @param data
     */
    public void readFrom(ByteBuf data, int length);

    /**
     * Write the vendor data to the specified ChannelBuffer
     * @param data
     */
    public void writeTo(ByteBuf data);
}
