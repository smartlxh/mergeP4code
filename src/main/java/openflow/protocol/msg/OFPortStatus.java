package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;
import openflow.protocol.port.OFPhysicalPort;
import util.Converter;
import util.HexString;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFPortStatus extends OFMessage {
    public static int MINIMUM_LENGTH = OFMessage.MINIMUM_LENGTH + 8 + OFPhysicalPort.MINIMUM_LENGTH;
    public enum POFPortReason {
        POFPR_ADD,
        POFPR_DELETE,
        POFPR_MODIFY
    }

    protected byte reason;
    protected OFPhysicalPort desc;
    /**
     * @return the reason
     */
    public byte getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(byte reason) {
        this.reason = reason;
    }

    /**
     * @return the desc
     */
    public OFPhysicalPort getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(OFPhysicalPort desc) {
        this.desc = desc;
    }

    public OFPortStatus() {
        super();
        this.type = OFType.PORT_STATUS;
        this.length = Converter.convertIntToShort(MINIMUM_LENGTH);
    }

    @Override
    public void readFrom(ByteBuf data) {
        super.readFrom(data);
        this.reason = data.readByte();
        data.readBytes(new byte[7]);    // is pad

        if (this.desc == null)
            this.desc = new OFPhysicalPort();
        this.desc.readFrom(data);
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);
        data.writeByte(this.reason);
        data.writeBytes(new byte[7]);
        this.desc.writeTo(data);
    }


    @Override
    public String toBytesString(){
        return super.toBytesString()
                + HexString.toHex(reason)
                + HexString.getByteZeros(7)
                + desc.toBytesString();
    }

    @Override
    public String toString(){
        return super.toString() +
                "; PortStatus:" +
                "rea=" + reason +
                desc.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 313;
        int result = super.hashCode();
        result = prime * result + ((desc == null) ? 0 : desc.hashCode());
        result = prime * result + reason;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof OFPortStatus)) {
            return false;
        }
        OFPortStatus other = (OFPortStatus) obj;
        if (desc == null) {
            if (other.desc != null) {
                return false;
            }
        } else if (!desc.equals(other.desc)) {
            return false;
        }
        return reason == other.reason;
    }

    public void computeLength()
    {
        this.length=(short) MINIMUM_LENGTH;
    }
}
