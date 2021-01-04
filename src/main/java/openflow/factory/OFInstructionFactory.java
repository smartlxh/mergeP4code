package openflow.factory;

import io.netty.buffer.ByteBuf;
import openflow.protocol.msg.instruction.OFInstruction;
import openflow.protocol.msg.instruction.OFInstructionType;

import java.util.List;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public interface OFInstructionFactory {
    /**
     * Retrieves an POFInstruction instance corresponding to the specified
     * POFInstructionType
     * @param t the type of the POFInstruction to be retrieved
     * @return an POFInstruction instance
     */
    public OFInstruction getInstruction(OFInstructionType t);

    /**
     * Attempts to parse and return all POFInstructions contained in the given
     * ByteBuffer, beginning at the ByteBuffer's position, and ending at
     * position+length.
     * @param data the ChannelBuffer to parse for OpenFlow actions
     * @param length the number of Bytes to examine for OpenFlow instructions
     * @return a list of POFInstruction instances
     */
    public List<OFInstruction> parseInstructions(ByteBuf data, int length);

    /**
     * Attempts to parse and return all POFInstructions contained in the given
     * ByteBuffer, beginning at the ByteBuffer's position, and ending at
     * position+length.
     * @param data the ChannelBuffer to parse for OpenFlow instructions
     * @param length the number of Bytes to examine for OpenFlow instructions
     * @param limit the maximum number of messages to return, 0 means no limit
     * @return a list of POFInstruction instances
     */
    public List<OFInstruction> parseInstructions(ByteBuf data, int length, int limit);

}
