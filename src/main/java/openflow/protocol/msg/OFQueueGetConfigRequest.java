package openflow.protocol.msg;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFQueueGetConfigRequest extends OFMessage {
    public OFQueueGetConfigRequest(){
        super();
        this.type = OFType.QUEUE_GET_CONFIG_REQUEST;
    }
}
