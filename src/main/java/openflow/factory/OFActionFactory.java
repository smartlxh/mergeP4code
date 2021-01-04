package openflow.factory;

import io.netty.buffer.ByteBuf;
import openflow.protocol.msg.action.OFAction;
import openflow.protocol.msg.action.OFActionType;

import java.util.List;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public interface OFActionFactory {
    /**
     * Retrieves an OFAction instance corresponding to the specified
     * OFActionType
     * @param t the type of the OFAction to be retrieved
     * @return an OFAction instance
     */
    public OFAction getAction(OFActionType t);

    /**
     * Attempts to parse and return all OFActions contained in the given
     * ByteBuffer, beginning at the ByteBuffer's position, and ending at
     * position+length.
     * @param data the ChannelBuffer to parse for OpenFlow actions
     * @param length the number of Bytes to examine for OpenFlow actions
     * @return a list of OFAction instances
     */
    public List<OFAction> parseActions(ByteBuf data, int length);

    /**
     * Attempts to parse and return all OFActions contained in the given
     * ByteBuffer, beginning at the ByteBuffer's position, and ending at
     * position+length.
     * @param data the ChannelBuffer to parse for OpenFlow actions
     * @param length the number of Bytes to examine for OpenFlow actions
     * @param limit the maximum number of messages to return, 0 means no limit
     * @return a list of OFAction instances
     */
    public List<OFAction> parseActions(ByteBuf data, int length, int limit);
}
