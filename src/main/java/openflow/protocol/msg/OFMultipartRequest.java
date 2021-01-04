package openflow.protocol.msg;

import openflow.protocol.msg.statistics.OFStatisticsMessageBase;
import util.Converter;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFMultipartRequest extends OFStatisticsMessageBase {
    public OFMultipartRequest() {
        super();
        this.type = OFType.MULTIPART_REQUEST;
        this.length = Converter.convertIntToShort(OFStatisticsMessageBase.MINIMUM_LENGTH);
    }
    public void computeLength() {
        this.length= Converter.convertIntToShort(OFStatisticsMessageBase.MINIMUM_LENGTH);
    }
}
