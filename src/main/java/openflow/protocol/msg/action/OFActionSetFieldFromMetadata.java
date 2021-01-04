package openflow.protocol.msg.action;

import io.netty.buffer.ByteBuf;
import openflow.protocol.match.POFMatch;

/**
 * Set field value from metadata. <br>
 * Write Src: metadata value start from {@link #metadataOffset}, length = {@link #fieldSetting}.length <br>
 * Write Des: field start from {@link #fieldSetting}.offset, length = {@link #fieldSetting}.length <br>
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public class OFActionSetFieldFromMetadata extends OFAction {
    public static int MINIMUM_LENGTH = OFAction.MINIMUM_LENGTH + POFMatch.MINIMUM_LENGTH + 8;

    protected POFMatch fieldSetting;
    protected short metadataOffset;     //bit

    public OFActionSetFieldFromMetadata(){
        super.setType(OFActionType.SET_FIELD_FROM_METADATA);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public void readFrom(ByteBuf data){
        super.readFrom(data);
        fieldSetting = new POFMatch();
        fieldSetting.readFrom(data);
        this.metadataOffset = data.readShort();
        data.readBytes(new byte[6]);
    }

    public void writeTo(ByteBuf data){
        super.writeTo(data);
        fieldSetting.writeTo(data);
        data.writeShort(metadataOffset);
        data.writeBytes(new byte[6]);
    }



    public String toString(){
        return super.toString() +
                ";fs=" + fieldSetting.toString() +
                ";mos=" + metadataOffset;
    }

    public POFMatch getFieldSetting() {
        return fieldSetting;
    }

    public void setFieldSetting(POFMatch fieldSetting) {
        this.fieldSetting = fieldSetting;
    }

    public short getMetadataOffset() {
        return metadataOffset;
    }

    public void setMetadataOffset(short metadataOffset) {
        this.metadataOffset = metadataOffset;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((fieldSetting == null) ? 0 : fieldSetting.hashCode());
        result = prime * result + metadataOffset;
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
        OFActionSetFieldFromMetadata other = (OFActionSetFieldFromMetadata) obj;
        if (fieldSetting == null) {
            if (other.fieldSetting != null)
                return false;
        } else if (!fieldSetting.equals(other.fieldSetting))
            return false;
        return metadataOffset == other.metadataOffset;
    }

    @Override
    public OFActionSetFieldFromMetadata clone() throws CloneNotSupportedException {
        OFActionSetFieldFromMetadata action = (OFActionSetFieldFromMetadata) super.clone();
        if(null != fieldSetting){
            action.setFieldSetting(fieldSetting.clone());
        }
        return action;
    }
}
