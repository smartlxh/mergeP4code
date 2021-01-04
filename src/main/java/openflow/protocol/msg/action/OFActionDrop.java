package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;

/**
 * Drop with reason
 *
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public class OFActionDrop extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + 8;

    public enum OFDropReason{
        OFPDR_TIMEOUT,
        OFPDR_HIT_MISS,
        OFPDR_UNKNOW
    }

    protected int reason;

    public OFActionDrop(){
        super.setType(OFActionType.DROP);
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
