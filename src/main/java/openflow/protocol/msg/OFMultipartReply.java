package openflow.protocol.msg;

import openflow.protocol.msg.statistics.OFStatisticsMessageBase;
import util.Converter;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFMultipartReply extends OFStatisticsMessageBase {
    public enum OFStatisticsReplyFlags {
        REPLY_MORE      (1 << 0);

        protected short type;

        OFStatisticsReplyFlags(int type) {
            this.type = (short) type;
        }

        public short getTypeValue() {
            return type;
        }
    }

    public OFMultipartReply() {
        super();
        this.type = OFType.MULTIPART_REPLY;
        this.length = Converter.convertIntToShort(OFStatisticsMessageBase.MINIMUM_LENGTH);
    }

    public void computeLength()
    {
        this.length = Converter.convertIntToShort(OFStatisticsMessageBase.MINIMUM_LENGTH);
    }
}
