package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;
import openflow.protocol.OFGlobal;
import util.Converter;
import util.ParseString;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFFeaturesReply extends OFMessage {
    public static int MINIMUM_LENGTH = 16 + 3 * OFGlobal.OFP_NAME_MAX_LENGTH + OFMessage.MINIMUM_LENGTH;

    /**
     * Corresponds to bits on the capabilities field
     */
    public enum OFCapabilities {
        OFC_FLOW_STATS     (1 << 0),
        OFC_TABLE_STATS    (1 << 1),
        OFC_PORT_STATS     (1 << 2),
        OFC_GROUP_STATS    (1 << 3),
        OFC_IP_REASM       (1 << 5),
        OFC_QUEUE_STATS    (1 << 6),
        OFC_PORT_BLOCKED   (1 << 8);

        protected int value;

        private OFCapabilities(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }
    // for multiple slots
    private int deviceId;
    private short slotID;
    private short portNum;
    private short tableNum;

    private int capabilities;

    /**
     * vendor name
     */
    private String experimenterName;
    private String deviceForwardEngineName;
    private String deviceLookupEngineName;

    public OFFeaturesReply() {
        super();
        this.type = OFType.FEATURES_REPLY;
        this.length = Converter.convertIntToShort(MINIMUM_LENGTH);
    }

    /**
     * @return the capabilities
     */
    public int getCapabilities() {
        return capabilities;
    }

    /**
     * @param capabilities the capabilities to set
     */
    public void setCapabilities(int capabilities) {
        this.capabilities = capabilities;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public short getPortNum() {
        return portNum;
    }

    public void setPortNum(short portNum) {
        this.portNum = portNum;
    }

    public short getTableNum() {
        return tableNum;
    }

    public void setTableNum(short tableNum) {
        this.tableNum = tableNum;
    }

    public String getExperimenterName() {
        return experimenterName;
    }

    public void setExperimenterName(String experimenterName) {
        this.experimenterName = experimenterName;
    }

    public String getDeviceForwardEngineName() {
        return deviceForwardEngineName;
    }

    public void setDeviceForwardEngineName(String deviceForwardEngineName) {
        this.deviceForwardEngineName = deviceForwardEngineName;
    }

    public String getDeviceLookupEngineName() {
        return deviceLookupEngineName;
    }

    public void setDeviceLookupEngineName(String deviceLookupEngineName) {
        this.deviceLookupEngineName = deviceLookupEngineName;
    }

    @Override
    public void readFrom(ByteBuf data) {
        super.readFrom(data);
        this.deviceId = data.readInt();
        this.slotID = data.readShort();
        this.portNum = data.readShort();
        this.tableNum = data.readShort();

        data.readShort();
        this.capabilities = data.readInt();

        this.experimenterName = ParseString.NameByteToString(data);
        this.deviceForwardEngineName = ParseString.NameByteToString(data);
        this.deviceLookupEngineName = ParseString.NameByteToString(data);
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);
        data.writeInt(this.deviceId);
        data.writeShort(this.slotID);
        data.writeShort(this.portNum);
        data.writeShort(this.tableNum);

        data.writeShort(0);
        data.writeInt(this.capabilities);


        data.writeBytes(ParseString.NameStringToBytes(experimenterName));
        data.writeBytes(ParseString.NameStringToBytes(deviceForwardEngineName));
        data.writeBytes(ParseString.NameStringToBytes(deviceLookupEngineName));
    }



    public String toString(){
        String string = super.toString();

        string += "; FeatureReply:" +
                "did=" + deviceId +
                ";pnum=" + portNum +
                ";tnum=" + tableNum +
                ";cap=" + capabilities +
                ";vdid=" + experimenterName +
                ";fwid=" + deviceForwardEngineName +
                ";lkid=" + deviceLookupEngineName;

        return string;
    }

    @Override
    public int hashCode() {
        final int prime = 139;
        int result = super.hashCode();

        result = prime * result + deviceId;
        result = prime * result + portNum;
        result = prime * result + tableNum;

        result = prime * result + capabilities;
        result = prime * result + ((experimenterName == null) ? 0 : experimenterName.hashCode());

        result = prime * result + ((deviceForwardEngineName == null) ? 0 : deviceForwardEngineName.hashCode());
        result = prime * result + ((deviceLookupEngineName == null) ? 0 : deviceLookupEngineName.hashCode());

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
        if (!(obj instanceof OFFeaturesReply)) {
            return false;
        }
        OFFeaturesReply other = (OFFeaturesReply) obj;

        if (deviceId != other.deviceId) {
            return false;
        }
        if (portNum != other.portNum) {
            return false;
        }
        if (tableNum != other.tableNum) {
            return false;
        }
        if (capabilities != other.capabilities) {
            return false;
        }
        if (experimenterName == null) {
            if (other.experimenterName != null) {
                return false;
            }
        } else if (!experimenterName.equals(other.experimenterName)) {
            return false;
        }

        if (deviceForwardEngineName == null) {
            if (other.deviceForwardEngineName != null) {
                return false;
            }
        } else if (!deviceForwardEngineName.equals(other.deviceForwardEngineName)) {
            return false;
        }

        if (deviceLookupEngineName == null) {
            if (other.deviceLookupEngineName != null) {
                return false;
            }
        } else if (!deviceLookupEngineName.equals(other.deviceLookupEngineName)) {
            return false;
        }

        return true;
    }

    public void computeLength()
    {
        this.length=(short)MINIMUM_LENGTH;
    }
}
