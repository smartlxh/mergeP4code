package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;

/**
 * @author twd
 * @description OFCounter, used in POFCounterMod, POFCounterReply, POFCounterRequest.
 * @date 2019-12-02
 */
public class OFCounter implements Cloneable {
    public static int MINIMUM_LENGTH = 24;

    public enum OFCounterModCmd {
        OFPCC_ADD,
        OFPCC_DELETE,
        OFPCC_CLEAR,
        OFPCC_QUERY,
        OFPCC_QUERYREPLY
    }

    protected OFCounterModCmd command;
    protected short slotID;
    protected int counterId;
    protected long counterValue;
    protected long byteValue;

    public void readFrom(ByteBuf data) {
        command = OFCounterModCmd.values()[ data.readByte() ];
        byte[] padding=new byte[1];
        data.readBytes(padding);
        slotID = data.readShort();
        counterId = data.readInt();
        counterValue = data.readLong();
        byteValue = data.readLong();
    }

    public void writeTo(ByteBuf data) {
        data.writeByte((byte)command.ordinal());
        byte[] padding=new byte[1];
        data.writeBytes(padding);
        data.writeShort(slotID);
        data.writeInt(counterId);
        data.writeLong(counterValue);
        data.writeLong(byteValue);
    }


    public String toString(){
        return "cmd=" + command +
                ";cid=" + counterId +
                ";counterValue=" + counterValue +
                ";byteValue="+byteValue;
    }

    public OFCounterModCmd getCommand() {
        return command;
    }
    public void setCommand(OFCounterModCmd command) {
        this.command = command;
    }
    public int getCounterId() {
        return counterId;
    }
    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }
    public long getCounterValue() {
        return counterValue;
    }
    public void setCounterValue(long value) {
        this.counterValue = value;
    }
    public long getByteValue()
    {
        return byteValue;
    }
    public void setByteValue(long value)
    {
        this.byteValue = value;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((command == null) ? 0 : command.hashCode());
        result = prime * result + counterId;
        result = prime * result + (int) (counterValue ^ (counterValue >>> 32));
        result = prime * result + (int) (byteValue ^ (byteValue>>>32));
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OFCounter other = (OFCounter) obj;
        if (command != other.command)
            return false;
        if (counterId != other.counterId)
            return false;
        if (counterValue != other.counterValue)
            return false;
        if (byteValue != other.byteValue)
            return false;
        return true;
    }

    @Override
    public OFCounter clone() throws CloneNotSupportedException {
        return (OFCounter)super.clone();
    }
}
