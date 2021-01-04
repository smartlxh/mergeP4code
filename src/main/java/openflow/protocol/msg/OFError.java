package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import openflow.exception.MessageParseException;
import openflow.factory.OFMessageFactory;
import openflow.factory.OFMessageFactoryAware;
import openflow.protocol.OFGlobal;
import util.Converter;
import util.HexString;
import util.ParseString;

import java.util.Arrays;
import java.util.List;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFError extends OFMessage implements OFMessageFactoryAware {
    public static int MINIMUM_LENGTH = 16;
    public static int MAXIMAL_LENGTH = 272 + 8;

    public enum OFErrorType {
        // OFPET_VENDOR_ERROR is an extension that was added in Open vSwitch and isn't
        // in the OF 1.0 spec, but it was easier to add it here instead of adding
        // generic support for extensible vendor-defined error messages. 
        // It uses the random value 0xb0c2 to avoid conflicts with other possible new
        // error types. Support for vendor-defined extended errors has been standardized
        // in the OF 1.2 spec, so this workaround is only needed for 1.0.
        OFPET_HELLO_FAILED,
        OFPET_BAD_REQUEST,
        OFPET_BAD_ACTION,
        OFPET_BAD_INSTRUCTION,
        OFPET_BAD_MATCH,

        OFPET_FLOW_MOD_FAILED,
        OFPET_GROUP_MOD_FAILED,
        OFPET_PORT_MOD_FAILED,
        OFPET_TABLE_MOD_FAILED,
        OFPET_QUEUE_OP_FAILED,

        OFPET_SWITCH_CONFIG_FAILED,
        OFPET_ROLE_REQUEST_FAILED,
        OFPET_METER_MOD_FAILED,
        OFPET_TABLE_FEATURES_FAILED,
        OFPET_SOFTWARE_FAILED,

        OFPET_EXPERIMENTER_ERROR((short)0xFFFF);

        protected short value;

        private OFErrorType() {
            this.value = (short) this.ordinal();
        }

        private OFErrorType(short value) {
            this.value = value;
        }

        public short getValue() {
            return value;
        }
    }

    public enum OFHelloFailedCode {
        OFPHFC_INCOMPATIBLE, OFPHFC_EPERM
    }

    public enum OFBadRequestCode {
        OFPBRC_BAD_VERSION,
        OFPBRC_BAD_TYPE,
        OFPBRC_BAD_MULTIPART,
        OFPBRC_BAD_EXPERIMENTER,
        OFPBRC_BAD_EXPERIMENTER_TYPE,

        OFPBRC_EPERM,
        OFPBRC_BAD_LEN,
        OFPBRC_BUFFER_EMPTY,
        OFPBRC_BUFFER_UNKNOWN,
        OFPBRC_BAD_TABLE_ID,

        OFPBRC_IS_SLAVE,
        OFPBRC_BAD_PORT,
        OFPBRC_BAD_PACKET,
        OFPBRC_MULTIPART_BUFFER_BUFFER_OVERFLOW
    }

    public enum OFBadActionCode {
        OFPBAC_BAD_TYPE,
        OFPBAC_BAD_LEN,
        OFPBAC_BAD_EXPERIMENTER,
        OFPBAC_BAD_EXPERIMENTER_TYPE,
        OFPBAC_BAD_OUT_PORT,

        OFPBAC_BAD_ARGUMENT,
        OFPBAC_EPERM,
        OFPBAC_TOO_MANY,
        OFPBAC_BAD_QUEUE,
        OFPBAC_BAD_OUT_GROUP,

        OFPBAC_MATCH_INCONSISTENT,
        OFPBAC_UNSUPPORTED_ORDER,
        OFPBAC_BAD_TAG,
        OFPBAC_BAD_SET_TYPE,
        OFPBAC_BAD_SET_LEN,

        OFPBAC_BAD_SET_ARGUMENT,
        OFPBAC_BAD_TABLE_ID
    }

    public enum OFBadInstructionCode{
        OFPBIC_UNKNOW_INST,
        OFPBIC_UNSUP_INST,
        OFPBIC_BAD_TABLE_ID,
        OFPBIC_UNSUP_METADATA,
        OFPBIC_UNSUP_METADATA_MASK,

        OFPBIC_BAD_EXPERIMENTER,
        OFPBIC_BAD_EXPERIMENTER_TYPE,
        OFPBIC_BAD_LEN,
        OFPBIC_EPERM,
        OFPBIC_TOO_MANY_ACTIONS,

        OFPBIC_TABLE_UNEXIST,
        OFPBIC_ENTRY_UNEXIST,
        OFPBIC_BAD_OFFSET,
        OFPBIC_JUM_TO_INVALID_INST
    }

    public enum OFBadMatchCode{
        OFPBMC_BAD_TYPE,
        OFPBMC_BAD_LEN,
        OFPBMC_BAD_TAG,
        OFPBMC_BAD_DL_ADDR_MASK,
        OFPBMC_BAD_NW_ADDR_MASK,

        OFPBMC_BAD_WILDCARD,
        OFPBMC_BAD_FIELD,
        OFPBMC_BAD_VALUE,
        OFPBMC_BAD_MASK,
        OFPBMC_BAD_PREERQ,

        OFPBMC_DUP_FIELD,
        OFPBMC_RPERM
    }

    public enum OFFlowModFailedCode {
        OFPFMFC_UNKNOWN,
        OFPFMFC_TABLE_FULL,
        OFPFMFC_BAD_TABLE_ID,
        OFPFMFC_OVERLAP,
        OFPFMFC_EPERM,

        OFPFMFC_BAD_TIMEOUT,
        OFPFMFC_BAD_COMMAND,
        OFPFMFC_BAD_FLAGS,
        OFPFMFC_ENTRY_EXIST,
        OFPFMFC_ENTRY_UNEXIST,

        OFPFMFC_BAD_TABLE_TYPE,
        OFPFMFC_BAD_ENTRY_ID,
        OFPFMFC_BAD_COUNTER_ID,
    }
    public enum OFTableModFailedCode{
        OFPTMFC_UNKNOWN,
        OFPTMFC_BAD_COMMAND,
        OFPTMFC_BAD_TABLE_TYPE,
        OFPTMFC_BAD_TABLE_ID,
        OFPTMFC_TABLE_EXIST,

        OFPTMFC_TABLE_UNEXIST,
        OFPTMFC_BAD_TABLE_SIZE,
        OFPTMFC_BAD_KEY_LEN,
        OFPTMFC_TABLE_UNEMPTY
    }
    public enum OFGroupModFailedCode{
        OFPGMFC_GROUP_EXISTS,
        OFPGMFC_INVALID_GROUP,
        OFPGMFC_WEIGHT_UNSUPPORTED,
        OFPGMFC_OUT_OF_GROUPS,
        OFPGMFC_OUT_OF_BUCKETS,

        OFPGMFC_CHAINING_UNSUPPORTED,
        OFPGMFC_WATCH_UNSUPPORTED,
        OFPGMFC_LOOP,
        OFPGMFC_UNKNOWN_GROUP,
        OFPGMFC_CHAINED_GROUP,

        OFPGMFC_BAD_TYPE,
        OFPGMFC_BAD_COMMAND,
        OFPGMFC_BAD_BUCKET,
        OFPGMFC_BAD_WATCH,
        OFPGMFC_EPERM,

        OFPGMFC_BAD_COUNTER_ID
    }

    public enum OFMeterModFailedCode{
        OFPMMFC_UNKNOWN,
        OFPMMFC__METER_EXISTS,
        OFPMMFC_INVALID_METER,
        OFPMMFC_UNKNOWN_METER,
        OFPMMFC_BAD_COMMAND,

        OFPMMFC_BAD_FLAGS,
        OFPMMFC_BAD_RATE,
        OFPMMFC_BAD_BURST,
        OFPMMFC_BAD_BAND,
        OFPMMFC_BAD_BAND_VALUE,

        OFPMMFC_OUT_OF_METERS,
        OFPMMFC_OUT_OF_BANDS
    }

    public enum OFPortModFailedCode {
        OFPPMFC_BAD_PORT, OFPPMFC_BAD_HW_ADDR
    }

    public enum OFQueueOpFailedCode {
        OFPQOFC_BAD_PORT, OFPQOFC_BAD_QUEUE, OFPQOFC_EPERM
    }

    public enum OFSoftwareErrorCode{
        OFPSEC_OK(0),

        OFPSEC_GET_PORT_INFO_FAILURE (0X1000),
        OFPSEC_PACKET_LEN_ERROR (0X1001),
        OFPSEC_METADATA_LEN_ERROR (0X1002),
        OFPSEC_PARA_LEN_ERROR (0X1003),
        OFPSEC_FIELD_LEN_ERROR (0X1004),

        OFPSEC_ALLOCATE_RESOURCE_FAILURE(0x5001),
        OFPSEC_ADD_EXIST_FLOW(0x5002),
        OFPSEC_DELETE_UNEXIST_FLOW(0x5003),
        OFPSEC_COUNTER_REQUEST_FAILURE(0x5004),
        OFPSEC_DELETE_NOT_EMPTY_TABLE(0x5005),
        OFPSEC_INVALID_SLOT_ID (0x5006),

        OFPSEC_INVALID_TABLE_TYPE(0x6000),
        OFPSEC_INVALID_KEY_LENGTH(0x6001),
        OFPSEC_INVALID_TABLE_SIZE(0x6002),
        OFPSEC_INVALID_MATCH_KEY(0x6003),
        OFPSEC_UNSUPPORT_INSTRUTION_LENGTH(0x6004),
        OFPSEC_UNSUPPORT_INSTRUTION_TYPE(0x6005),
        OFPSEC_UNSUPPORT_ACTION_LENGTH(0x6006),
        OFPSEC_UNSUPPORT_ACTION_TYPE(0x6007),
        OFPSEC_TABLE_NOT_CREATED  (0x6008),
        OFPSEC_UNSUPPORT_COMMAND(0x6009),
        OFPSEC_UNSUPPORT_FLOW_TABLE_COMMAND(0x600A),
        OFPSEC_UPFORWARD_TOO_LARGE_PACKET(0x600B),
        OFPSEC_BAD_TABLE_ID (0x600C),
        OFPSEC_BAD_TABLE_TYPE (0x600D),
        OFPSEC_PORT_DELETE_FAIL (0x600E),
        OFPSEC_BAD_INS_BLOCK_ID (0x600F),


        OFPSEC_CREATE_SOCKET_FAILURE(0x7001),
        OFPSEC_CONNECT_SERVER_FAILURE(0x7002),
        OFPSEC_SEND_MSG_FAILURE(0x7003),
        OFPSEC_RECEIVE_MSG_FAILURE(0x7004),
        OFPSEC_WRONG_CHANNEL_STATE(0x7005),
        OFPSEC_WRITE_MSG_QUEUE_FAILURE(0x7006),
        OFPSEC_READ_MSG_QUEUE_FAILURE(0x7007),
        OFPSEC_MESSAGE_SIZE_TOO_BIG(0x7008),
        OFPSEC_BIND_SOCKET_FAILURE (0X7009),
        OFPSEC_QUEUE_CREATE_FAIL (0X700A),
        OFPSEC_QUEUE_DELETE_FAIL (0X700B),
        OFPSEC_TASK_CREATE_FAIL (0X700C),
        OFPSEC_TASK_DELETE_FAIL (0X700D),
        OFPSEC_TIMER_CREATE_FAIL (0X700E),
        OFPSEC_TIMER_DELETE_FAIL (0X700F),
        OFPSEC_PTR_NULL (0X7010),

        OFPSEC_IPC_SEND_FAILURE(0x8001),
        /*OFPSEC_CREATE_TASK_FAILURE(0x8002),
        OFPSEC_CREATE_MSGQUEUE_FAILURE(0x8003),
        OFPSEC_CREATE_TIMER_FAILURE(0x8004),*/

        OFPSEC_ERROR (0xffff);

        protected short value;

        private OFSoftwareErrorCode() {
            this.value = (short) this.ordinal();
        }

        private OFSoftwareErrorCode(int value) {
            this.value = (short)value;
        }

        public short getValue() {
            return value;
        }
    }

    protected short errorType;
    protected short errorCode;
    protected int deviceId;
    protected short slotId;
    protected int experimenter;
    protected int experimenterErrorType;
    protected short experimenterErrorCode;
    protected OFMessageFactory factory;
    protected byte[] error;
    //protected boolean errorIsAscii;

    public OFError() {
        super();
        this.type = OFType.ERROR;
        this.length = Converter.convertIntToShort(MINIMUM_LENGTH);
    }

    /**
     * @return the errorType
     */
    public short getErrorType() {
        return errorType;
    }

    /**
     * @param errorType
     *            the errorType to set
     */
    public void setErrorType(short errorType) {
        this.errorType = errorType;
    }

    public void setErrorType(OFErrorType type) {
        this.errorType = type.getValue();
    }

    /**
     * @return true if the error is an extended vendor error
     */
    public boolean isExperimenterError() {
        return errorType == OFErrorType.OFPET_EXPERIMENTER_ERROR.getValue();
    }

    /**
     * @return the errorCode
     */
    public short getErrorCode() {
        return errorCode;
    }

    public String getErrorCodeString(){
        String errorString;

        try{
            switch(OFErrorType.values()[errorType]){
                case OFPET_HELLO_FAILED:
                    errorString = OFHelloFailedCode.values()[errorCode].toString();
                    break;
                case OFPET_BAD_REQUEST:
                    errorString = OFBadRequestCode.values()[errorCode].toString();
                    break;
                case OFPET_BAD_ACTION:
                    errorString = OFBadActionCode.values()[errorCode].toString();
                    break;
                case OFPET_BAD_INSTRUCTION:
                    errorString = OFBadInstructionCode.values()[errorCode].toString();
                    break;
                case OFPET_BAD_MATCH:
                    errorString = OFBadMatchCode.values()[errorCode].toString();
                    break;
                case OFPET_FLOW_MOD_FAILED:
                    errorString = OFFlowModFailedCode.values()[errorCode].toString();
                    break;
                case OFPET_GROUP_MOD_FAILED:
                    errorString = OFGroupModFailedCode.values()[errorCode].toString();
                    break;
                case OFPET_PORT_MOD_FAILED:
                    errorString = OFPortModFailedCode.values()[errorCode].toString();
                    break;
                case OFPET_QUEUE_OP_FAILED:
                    errorString = OFQueueOpFailedCode.values()[errorCode].toString();
                    break;
                case OFPET_METER_MOD_FAILED:
                    errorString = OFMeterModFailedCode.values()[errorCode].toString();
                    break;
                case OFPET_SOFTWARE_FAILED:
                    errorString = OFSoftwareErrorCode.values()[errorCode].toString();
                    break;
                case OFPET_TABLE_MOD_FAILED:
                    errorString = OFTableModFailedCode.values()[errorCode].toString();
                    break;
                case OFPET_SWITCH_CONFIG_FAILED:
                case OFPET_ROLE_REQUEST_FAILED:
                case OFPET_TABLE_FEATURES_FAILED:
                case OFPET_EXPERIMENTER_ERROR:
                default:
                    errorString = HexString.toHex(errorCode);
                    break;
            }
        }catch (Exception e) {
            errorString = HexString.toHex(errorCode);
        }

        return errorString;
    }

    /**
     * @param code
     *            the errorCode to set
     */
    public void setErrorCode(OFHelloFailedCode code) {
        this.errorCode = (short) code.ordinal();
    }

    public void setErrorCode(short errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorCode(OFBadRequestCode code) {
        this.errorCode = (short) code.ordinal();
    }

    public void setErrorCode(OFBadActionCode code) {
        this.errorCode = (short) code.ordinal();
    }

    public void setErrorCode(OFFlowModFailedCode code) {
        this.errorCode = (short) code.ordinal();
    }

    public void setErrorCode(OFPortModFailedCode code) {
        this.errorCode = (short) code.ordinal();
    }

    public void setErrorCode(OFQueueOpFailedCode code) {
        this.errorCode = (short) code.ordinal();
    }

    public int getExperimenterErrorType() {
        return experimenterErrorType;
    }

    public void setExperimenterErrorType(int experimenterErrorType) {
        this.experimenterErrorType = experimenterErrorType;
    }

    public short getExperimenterErrorCode() {
        return experimenterErrorCode;
    }

    public void setExperimenterErrorCode(short experimenterErrorCode) {
        this.experimenterErrorCode = experimenterErrorCode;
    }

    public OFMessage getOffendingMsg() throws MessageParseException {
        // should only have one message embedded; if more than one, just
        // grab first
        if (this.error == null)
            return null;
        final ByteBuf errorMsg = Unpooled.wrappedBuffer(this.error);
        if (this.factory == null) {
            throw new RuntimeException("MessageFactory not set");
        }

        List<OFMessage> msgList = this.factory.parseOFMessage(errorMsg);
        if (msgList == null)
            return null;
        return msgList.get(0);
    }

    /**
     * Write this offending message into the payload of the Error message
     *
     * @param offendingMsg
     */

    public void setOffendingMsg(OFMessage offendingMsg) {
        if (offendingMsg == null) {
            super.setLengthU(MINIMUM_LENGTH);
        } else {
            this.error = new byte[offendingMsg.getLengthU()];
            ByteBuf data = Unpooled.wrappedBuffer(this.error);
            offendingMsg.writeTo(data);
            super.setLengthU(MINIMUM_LENGTH + offendingMsg.getLengthU());
        }
    }

    public OFMessageFactory getFactory() {
        return factory;
    }

    @Override
    public void setMessageFactory(OFMessageFactory factory) {
        this.factory = factory;
    }

    /**
     * @return the error
     */
    public byte[] getError() {
        return error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(byte[] error) {
        this.error = error;
    }


    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public void readFrom(ByteBuf data) {
        super.readFrom(data);
        this.errorType = data.readShort();
        this.errorCode = data.readShort();
        this.deviceId = data.readInt();
        this.slotId = data.readShort();
        data.readBytes(new byte[6]);
        error = new byte[OFGlobal.OFP_ERROR_STRING_MAX_LENGTH];
        data.readBytes(error);
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);
        data.writeShort(errorType);
        data.writeShort(errorCode);
        data.writeInt(deviceId);
        data.writeShort(slotId);
        data.writeBytes(new byte[6]);
        if (error != null){
            if (error.length < OFGlobal.OFP_ERROR_STRING_MAX_LENGTH) {
                data.writeBytes(error);
                byte[] byte1=new byte[OFGlobal.OFP_ERROR_STRING_MAX_LENGTH - error.length];
                data.writeBytes(byte1);
            } else {
                data.writeBytes(error, 0, OFGlobal.OFP_ERROR_STRING_MAX_LENGTH - 1);
                data.writeByte((byte) 0);
            }
        }else{
            byte[] byte1=new byte[OFGlobal.OFP_ERROR_STRING_MAX_LENGTH];
            data.writeBytes(byte1);
        }
    }



    public String toString(){
        return super.toString() +
                "; Error:" +
                "et=" + errorType + "(0x" + Integer.toHexString(errorType) +")" +
                ";ec=" + errorCode + "(0x" + Integer.toHexString(errorCode) +")" +
                ";did=" + deviceId +
                ";estr=" + ParseString.ByteToString(error);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + deviceId;
        result = prime * result + Arrays.hashCode(error);
        result = prime * result + errorCode;
        result = prime * result + errorType;
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
        OFError other = (OFError) obj;
        if (deviceId != other.deviceId)
            return false;
        if (!Arrays.equals(error, other.error))
            return false;
        if (errorCode != other.errorCode)
            return false;
        return errorType == other.errorType;
    }

    public void computeLength()
    {
        this.length=(short) MAXIMAL_LENGTH;
    }
}
