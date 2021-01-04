package openflow.factory;

import io.netty.buffer.ByteBuf;
import openflow.protocol.msg.OFType;
import openflow.protocol.msg.statistics.OFStatistics;
import openflow.protocol.msg.statistics.OFStatisticsType;

import java.util.List;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public interface OFStatisticsFactory {
    /**
     * Retrieves an POFStatistics instance corresponding to the specified
     * POFStatisticsType
     * @param t the type of the containing POFMessage, only accepts statistics
     *           request or reply
     * @param st the type of the POFStatistics to be retrieved
     * @return an POFStatistics instance
     */
    public OFStatistics getStatistics(OFType t, OFStatisticsType st);

    /**
     * Attempts to parse and return all POFStatistics contained in the given
     * ByteBuffer, beginning at the ByteBuffer's position, and ending at
     * position+length.
     * @param t the type of the containing POFMessage, only accepts statistics
     *           request or reply
     * @param st the type of the POFStatistics to be retrieved
     * @param data the ChannelBuffer to parse for OpenFlow Statistics
     * @param length the number of Bytes to examine for OpenFlow Statistics
     * @return a list of POFStatistics instances
     */
    public List<OFStatistics> parseStatistics(OFType t, OFStatisticsType st, ByteBuf data, int length);

    /**
     * Attempts to parse and return all POFStatistics contained in the given
     * ByteBuffer, beginning at the ByteBuffer's position, and ending at
     * position+length.
     * @param t the type of the containing POFMessage, only accepts statistics
     *           request or reply
     * @param st the type of the POFStatistics to be retrieved
     * @param data the ChannelBuffer to parse for OpenFlow Statistics
     * @param length the number of Bytes to examine for OpenFlow Statistics
     * @param limit the maximum number of messages to return, 0 means no limit
     * @return a list of POFStatistics instances
     */
    public List<OFStatistics> parseStatistics(OFType t, OFStatisticsType st, ByteBuf data, int length, int limit);
}

