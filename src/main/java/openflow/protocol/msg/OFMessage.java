package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Converter;
import util.HexString;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFMessage {
    private final static byte OFP_VERSION = 0x04;
    public final static int MINIMUM_LENGTH = 8;

    /**
     * OF 版本号
     */
    private byte version;
    protected OFType type;
    protected short length;
    protected int xid;

    private Logger logger = LoggerFactory.getLogger(OFMessage.class.getName());

    public OFMessage() {
        this.version = OFP_VERSION;
        this.length = Converter.convertIntToShort(MINIMUM_LENGTH);
    }

    /**
     * Get the length of this message
     * @return length
     */
    public short getLength() {
        return length;
    }

    /**
     * Get the length of this message, int
     * @return length
     */
    public int getLengthU() {
        return Converter.convertShortToInt(length);
    }

    /**
     * Set the length of this message
     * @param length
     */
    public OFMessage setLength(short length) {
        this.length = length;
        return this;
    }

    /**
     * Set the length of this message, int
     * @param length
     */
    public OFMessage setLengthU(int length) {
        this.length = Converter.convertIntToShort(length);
        return this;
    }

    /**
     * Get the type of this message
     * @return type
     */
    public OFType getType() {
        return type;
    }

    /**
     * Set the type of this message
     * @param type
     */
    public void setType(OFType type) {
        this.type = type;
    }

    /**
     * Get the OpenFlow version of this message
     * @return version
     */
    public byte getVersion() {
        return version;
    }

    /**
     * Set the OpenFlow version of this message
     * @param version
     */
    public void setVersion(byte version) {
        this.version = version;
    }

    /**
     * Get the transaction id of this message
     * @return xid
     */
    public int getXid() {
        return xid;
    }

    /**
     * Set the transaction id of this message
     * @param xid
     */
    public void setXid(int xid) {
        this.xid = xid;
    }

    /**
     * Read this message off the wire from the specified ByteBuf
     * @param data
     */
    public void readFrom(ByteBuf data) {
        this.version = data.readByte();
        this.type = OFType.valueOf(data.readByte());
        this.length = data.readShort();
        this.xid = data.readInt();
    }

    /**
     * Write this message's binary format to the specified ByteBuffer
     * @param data
     */
    public void writeTo(ByteBuf data) {
        computeLength();
        data.writeByte(version);
        data.writeByte(type.getTypeValue());
        data.writeShort(length);
        data.writeInt(xid);
    }

    /**
     * This method is called during the writeTo method for serialization and
     * is expected to set the length of the message. If your class manually
     * sets the length you should override this to do nothing.
     *
     */
    protected void computeLength() {
        this.length = MINIMUM_LENGTH;
    }


    /**
     * Returns a summary of the message
     * @return "ofmsg=v=$version;t=$type:l=$len:xid=$xid"
     */
    public String toString() {
        return "ofmsg" +
                ":v=" + this.getVersion() +
                ";t=" + this.getType() +
                ";l=" + this.getLengthU() +
                ";x=" + this.getXid();
    }

    /**
     * @return this message's hex format string
     */
    public String toBytesString(){
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(HexString.toHex(version))
                .append(HexString.toHex(type.getTypeValue()))
                .append(HexString.toHex(length))
                .append(" ")
                .append(HexString.toHex(xid));
        return sbuf.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 97;
        int result = 1;
        result = prime * result + length;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + version;
        result = prime * result + xid;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof OFMessage)) {
            return false;
        }
        OFMessage other = (OFMessage) obj;
        if (length != other.length) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        if (version != other.version) {
            return false;
        }
        return xid == other.xid;
    }
}
