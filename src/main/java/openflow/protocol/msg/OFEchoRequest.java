package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFEchoRequest extends OFMessage {
    byte[] payload;

    public OFEchoRequest() {
        super();
        this.type = OFType.ECHO_REQUEST;
    }

    @Override
    public void readFrom(ByteBuf b) {
        super.readFrom(b);
        int datalen = this.getLengthU() - MINIMUM_LENGTH;
        if (datalen > 0) {
            this.payload = new byte[datalen];
            b.readBytes(payload);
        }
    }

    /**
     * @return the payload
     */
    public byte[] getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    @Override
    public void writeTo(ByteBuf b) {
        super.writeTo(b);
        if (payload != null)
            b.writeBytes(payload);
    }
}
