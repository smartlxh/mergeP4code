package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;

/**
 * Counter
 * FIXME Could discard this action counter because flow entry and the group entry have counter already
 *
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public class OFActionCounter extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + 8;

    protected int counterId;

    public OFActionCounter(){
        super.setType(OFActionType.COUNTER);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public void readFrom(ByteBuf data){
        super.readFrom(data);
        this.counterId = data.readInt();
        data.readBytes(new byte[4]);
    }

    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeInt(counterId);
        data.writeBytes(new byte[4]);
    }


    public String toString(){
        return super.toString() +
                ";cid=" + counterId;
    }

    public int getCounterId() {
        return counterId;
    }

    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + counterId;
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
        OFActionCounter other = (OFActionCounter) obj;
        if (counterId != other.counterId)
            return false;
        return true;
    }
}
