package openflow.protocol.msg;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFSetConfig extends OFSwitchConfig {
    public OFSetConfig() {
        super();
        this.type = OFType.SET_CONFIG;
    }
}
