package openflow.protocol.msg;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFEchoReply extends OFEchoRequest {
    public OFEchoReply() {
        super();
        this.type = OFType.ECHO_REPLY;
    }
}
