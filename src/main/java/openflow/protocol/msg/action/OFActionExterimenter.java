package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;

/**
 * @author David Erickson (daviderickson@cs.stanford.edu)
 */
public class OFActionExterimenter extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + 8;

    protected int exterimenter;

    public OFActionExterimenter() {
        super();
        super.setType(OFActionType.EXPERIMENTER);
        super.setLength((short) MINIMUM_LENGTH);
    }

    /**
     * @return the vendor
     */
    public int getExterimenter() {
        return exterimenter;
    }

    /**
     * @param exterimenter the exterimenter to set
     */
    public void setExterimenter(int exterimenter) {
        this.exterimenter = exterimenter;
    }

    @Override
    public void readFrom(ByteBuf data) {
        super.readFrom(data);
        this.exterimenter = data.readInt();
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);
        data.writeInt(this.exterimenter);
    }

    @Override
    public int hashCode() {
        final int prime = 379;
        int result = super.hashCode();
        result = prime * result + exterimenter;
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
        if (!(obj instanceof OFActionExterimenter)) {
            return false;
        }
        OFActionExterimenter other = (OFActionExterimenter) obj;
        return exterimenter == other.exterimenter;
    }
}
