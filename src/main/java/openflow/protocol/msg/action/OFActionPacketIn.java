package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;

/**
 * packet in with reason. Or say take the packet to controller or cpu.
 * E.g. if a packet dis match any flow entry, could use packetIn to take
 * the packet to controller to let controller to process.
 *
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public class OFActionPacketIn extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + 8;

    protected int reason;

    public OFActionPacketIn(){
        super.setType(OFActionType.PACKET_IN);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public void readFrom(ByteBuf data){
        super.readFrom(data);
        this.reason = data.readInt();
        data.readBytes(new byte[4]);
    }

    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeInt(reason);
        data.writeBytes(new byte[4]);
    }



    public String toString(){
        return super.toString() +
                ";rz=" + reason;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + reason;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        OFActionDrop other = (OFActionDrop) obj;
        return reason == other.reason;
    }
}
