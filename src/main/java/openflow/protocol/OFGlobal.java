package openflow.protocol;

/**
 * @author twd
 * @description global constant values
 * @date 2019-12-02
 */
public class OFGlobal {
    /**
     * Define invalid value. {@value}.
     */
    public static final int OFP_INVALID_VALUE = 0xFFFFFFFF;

    /**
     * Define the length of device name. {@value}.
     */
    public static final int OFP_NAME_MAX_LENGTH = 64;

    /**
     * Define the max length of error string. {@value}.
     */
    public static final int OFP_ERROR_STRING_MAX_LENGTH = 256;

    /**
     * Define the max length of packet-in. {@value}.
     */
    public static final int OFP_PACKET_IN_MAX_LENGTH = 2048;

    /**
     * Define the max length in byte unit of match field. {@value}.
     */
    public static final int OFP_MAX_FIELD_LENGTH_IN_BYTE = 16;

    /**
     * Define the max number of match field in one flow entry. {@value}.
     */
    public static final int OFP_MAX_MATCH_FIELD_NUM = 8;

    /**
     * Define the max instruction number of one flow entry. {@value}.
     */
    public static final int OFP_MAX_INSTRUCTION_NUM = 6;

    /**
     * Define the max field number of one protocol. {@value}.
     */
    public static final int OFP_MAX_PROTOCOL_FIELD_NUM  = 8;

    /**
     * Define the max action number in one instruction. {@value}.
     */
    public static final int OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION = 6;

    /**
     * Define the max action number in one group. {@value}.
     */
    public static final int OFP_MAX_ACTION_NUMBER_PER_GROUP = 6;

    /**
     * Define the max action length in unit of byte. {@value}.
     */
    public static final int OFP_MAX_ACTION_LENGTH = 44;

    /**
     * Define the max instruction length in unit of byte. {@value}.
     */
    public static final int OFP_MAX_INSTRUCTION_LENGTH = (8 + 8 + OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION * (OFP_MAX_ACTION_LENGTH + 4));
}
