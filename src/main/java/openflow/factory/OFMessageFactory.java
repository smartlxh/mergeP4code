package openflow.factory;

import io.netty.buffer.ByteBuf;
import openflow.exception.MessageParseException;
import openflow.protocol.msg.OFMessage;
import openflow.protocol.msg.OFType;

import java.util.List;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public interface OFMessageFactory {
    /**
     * Retrieves an POFMessage instance corresponding to the specified OFType
     * @param t the type POF the POFMessage to be retrieved
     * @return an POFMessage instance
     */
    public OFMessage getOFMessage(OFType t);

    /**
     * Attempts to parse and return a OFMessages contained in the given
     * ChannelBuffer, beginning at the ChannelBuffer's position, and ending at the
     * after the first parsed message
     * @param data the ByteBuf to parse for an OpenFlow message
     * @return a list POF POFMessage instances
     * @throws MessageParseException
     */
    public List<OFMessage> parseOFMessage(ByteBuf data) throws MessageParseException;

    /**
     * Retrieves an POFActionFactory
     * @return an POFActionFactory
     */
    public OFActionFactory getActionFactory();
}
