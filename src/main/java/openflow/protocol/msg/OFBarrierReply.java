package openflow.protocol.msg;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFBarrierReply extends OFMessage {
    public OFBarrierReply() {
        super();
        this.type = OFType.BARRIER_REPLY;
    }
}
