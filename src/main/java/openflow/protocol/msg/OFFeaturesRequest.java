package openflow.protocol.msg;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFFeaturesRequest extends OFMessage {
    public OFFeaturesRequest() {
        super();
        this.type = OFType.FEATURES_REQUEST;
    }
}
