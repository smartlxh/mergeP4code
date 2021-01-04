package openflow.protocol.msg;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFGetConfigRequest extends OFMessage {
    public OFGetConfigRequest() {
        super();
        this.type = OFType.GET_CONFIG_REQUEST;
    }
}
