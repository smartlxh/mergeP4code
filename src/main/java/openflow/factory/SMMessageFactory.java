package openflow.factory;

/**
 * TODO  NOT used in POF yet
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public interface SMMessageFactory {
    //public SMMessage getSMMessage(SMType t);

    /**
     * Attempts to parse and return a SMMessages contained in the given
     * ChannelBuffer, beginning at the ChannelBuffer's position, and ending at the
     * after the first parsed message
     * @param data the ChannelBuffer to parse for an PofManager message
     * @return a list of SMMessage instances
     * @throws MessageParseException
     */
    //public List<SMMessage> parseSMMessage(ChannelBuffer data) throws MessageParseException;
}
