package openflow.protocol.msg;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFGetConfigReply extends OFSwitchConfig {
    public OFGetConfigReply() {
        super();
        this.type = OFType.GET_CONFIG_REPLY;
    }
}
