package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;

/**
 * take the group( {@link #groupId} ) processing
 *
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public class OFActionGroup extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + 8;

    protected int groupId;

    public OFActionGroup(){
        super.setType(OFActionType.GROUP);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public void readFrom(ByteBuf data){
        super.readFrom(data);
        this.groupId = data.readInt();
        data.readBytes(new byte[4]);
    }

    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeInt(groupId);
        data.writeBytes(new byte[4]);
    }



    public String toString(){
        return super.toString() +
                ";gid=" + groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + groupId;
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
        OFActionGroup other = (OFActionGroup) obj;
        return groupId == other.groupId;
    }
}
