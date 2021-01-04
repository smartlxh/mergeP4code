package openflow.protocol.msg;

/**
 * @author twd
 * @description OFP_HELLO
 * @date 2019-12-02
 */
public class OFHello extends OFMessage {
    public OFHello() {
        super();
        this.type = OFType.HELLO;
    }
}
