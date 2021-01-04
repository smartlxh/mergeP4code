package openflow.protocol.msg;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFBarrierRequest extends OFMessage {
    public OFBarrierRequest() {
        super();
        this.type = OFType.BARRIER_REQUEST;
    }
}
