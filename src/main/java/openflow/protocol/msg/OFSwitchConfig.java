package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;

/**
 * @author twd
 * @description
 * @date 2019-12-19
 */
public class OFSwitchConfig extends OFMessage{
    public static int MINIMUM_LENGTH = 8 + 8;

    public enum OFConfigFlags {
        OFPC_FRAG_NORMAL,
        OFPC_FRAG_DROP,
        OFPC_FRAG_REASM,
        OFPC_FRAG_MASK
    }
    protected int dev_id;
    protected short flags;
    protected short missSendLength;

    public OFSwitchConfig() {
        super();
        super.setLength((short) MINIMUM_LENGTH);
    }

    /**
     * @return the flags
     */
    public short getFlags() {
        return flags;
    }

    /**
     * @param flags the flags to set
     */
    public OFSwitchConfig setFlags(short flags) {
        this.flags = flags;
        return this;
    }

    /**
     * @return the missSendLength
     */
    public short getMissSendLength() {
        return missSendLength;
    }

    /**
     * @param missSendLength the missSendLength to set
     */
    public OFSwitchConfig setMissSendLength(short missSendLength) {
        this.missSendLength = missSendLength;
        return this;
    }

    @Override
    public void readFrom(ByteBuf data) {
        super.readFrom(data);
        this.dev_id = data.readInt();
        this.flags = data.readShort();
        this.missSendLength = data.readShort();
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);
        data.writeInt(this.dev_id);
        data.writeShort(this.flags);
        data.writeShort(this.missSendLength);
    }

    @Override
    public int hashCode() {
        final int prime = 331;
        int result = super.hashCode();
        result = prime * result + flags;
        result = prime * result + missSendLength;
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
        if (!(obj instanceof OFSwitchConfig)) {
            return false;
        }
        OFSwitchConfig other = (OFSwitchConfig) obj;
        if (flags != other.flags) {
            return false;
        }
        if (missSendLength != other.missSendLength) {
            return false;
        }
        return true;
    }

    public void computeLength()
    {
        this.length=(short) MINIMUM_LENGTH;
    }
}
