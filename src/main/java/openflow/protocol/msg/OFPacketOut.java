package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;
import openflow.factory.OFActionFactory;
import openflow.factory.OFActionFactoryAware;
import openflow.protocol.OFGlobal;
import openflow.protocol.msg.action.OFAction;
import openflow.protocol.port.OFPort;
import util.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFPacketOut extends OFMessage implements OFActionFactoryAware, Cloneable {
    public static int MINIMUM_LENGTH = 24;
    public static int MAXMUN_LENGTH = MINIMUM_LENGTH + OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION * OFAction.MAXIMAL_LENGTH
            + OFGlobal.OFP_PACKET_IN_MAX_LENGTH;

    public static int BUFFER_ID_NONE = 0xffffffff;

    protected OFActionFactory actionFactory;
    protected int bufferId;
    protected int inPort;
    protected byte actionNum;
    protected int packetLen;
    protected List<OFAction> actionList;
    protected byte[] data;

    public OFPacketOut() {
        super();
        this.type = OFType.PACKET_OUT;
        this.length = Converter.convertIntToShort(MINIMUM_LENGTH);
    }

    /**
     * Get buffer_id
     * @return bufferId
     */
    public int getBufferId() {
        return this.bufferId;
    }

    /**
     * Set buffer_id
     * @param bufferId
     */
    public OFPacketOut setBufferId(int bufferId) {
        this.bufferId = bufferId;
        return this;
    }

    /**
     * Returns the packet data
     * @return packetData
     */
    public byte[] getPacketData() {
        return this.data;
    }

    /**
     * Sets the packet data
     * @param packetData
     */
    public OFPacketOut setPacketData(byte[] packetData) {
        this.data = packetData;
        return this;
    }

    /**
     * Get in_port
     * @return inPort
     */
    public int getInPort() {
        return this.inPort;
    }

    /**
     * Set in_port
     * @param inPort
     */
    public OFPacketOut setInPort(int inPort) {
        this.inPort = inPort;
        return this;
    }

    /**
     * Set in_port. Convenience method using OFPort enum.
     * @param inPort
     */
    public OFPacketOut setInPort(OFPort inPort) {
        this.inPort = inPort.getValue();
        return this;
    }

    /**
     * Get actionsNum
     * @return actionsLength
     */
    public short getActionsNumU() {
        return Converter.convertByteToShort(this.actionNum);
    }

    /**
     * Set actions_len
     * @param actionNum
     */
    public OFPacketOut setActionsNum(short actionNum) {
        this.actionNum = Converter.convertShortToByte(actionNum);
        return this;
    }

    public int getPacketLen()
    {
        return this.packetLen;
    }

    public OFPacketOut setPacketLen(int packetLen)
    {
        this.packetLen=packetLen;
        return this;
    }

    /**
     * Returns the actions contained in this message
     * @return a list of ordered OFAction objects
     */
    public List<OFAction> getActions() {
        return this.actionList;
    }

    /**
     * Sets the list of actions on this message
     * @param actions a list of ordered OFAction objects
     */
    public OFPacketOut setActions(List<OFAction> actions) {
        this.actionList = actions;
        return this;
    }

    @Override
    public void setActionFactory(OFActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    @Override
    public void readFrom(ByteBuf data) {
        super.readFrom(data);
        this.bufferId = data.readInt();
        this.inPort = data.readInt();
        this.actionNum=data.readByte();
        //just padding
        data.readBytes(new byte[3]);
        this.packetLen=data.readInt();
        if ( this.actionFactory == null)
            throw new RuntimeException("ActionFactory not set");
        this.actionList = this.actionFactory.parseActions(data, OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION * OFAction.MAXIMAL_LENGTH);
        this.data = new byte[OFGlobal.OFP_PACKET_IN_MAX_LENGTH];
        data.readBytes(this.data);
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);
        data.writeInt(bufferId);
        data.writeInt(inPort);
        data.writeByte(actionNum);
        data.writeBytes(new byte[3]);
        data.writeInt(packetLen);
        if(actionList == null){
            data.writeBytes(new byte[OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION * OFAction.MAXIMAL_LENGTH]);
        }else{
            OFAction action;
            if(actionNum > actionList.size()){
                throw new RuntimeException("actionNum " + actionNum + " > actionList.size()" + actionList.size());
            }
            int i;
            for(i = 0; i < actionNum && i < OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION; i++){
                action = actionList.get(i);
                if(action == null){
                    data.writeBytes(new byte[OFAction.MAXIMAL_LENGTH]);
                }else{
                    action.writeTo(data);
                    if(action.getLength() < OFAction.MAXIMAL_LENGTH ){
                        data.writeBytes(new byte[OFAction.MAXIMAL_LENGTH - action.getLength()]);
                    }
                }
            }
            if(i < OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION){
                data.writeBytes(new byte[(OFGlobal.OFP_MAX_ACTION_NUMBER_PER_INSTRUCTION - i) * OFAction.MAXIMAL_LENGTH]);
            }
        }
        if (this.data != null)
        {
            if(this.data.length> OFGlobal.OFP_PACKET_IN_MAX_LENGTH)
            {
                data.writeBytes(this.data,0, OFGlobal.OFP_PACKET_IN_MAX_LENGTH);
            }
            else{
                data.writeBytes(this.data);
                data.writeBytes(new byte[OFGlobal.OFP_PACKET_IN_MAX_LENGTH-this.data.length]);
            }
        }
        else{
            data.writeBytes(new byte[OFGlobal.OFP_PACKET_IN_MAX_LENGTH]);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 293;
        int result = super.hashCode();
        result = prime * result + ((actionList == null) ? 0 : actionList.hashCode());
        result = prime * result + actionNum;
        result = prime * result + packetLen;
        result = prime * result + bufferId;
        result = prime * result + inPort;
        result = prime * result + Arrays.hashCode(data);
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
        if (!(obj instanceof OFPacketOut)) {
            return false;
        }
        OFPacketOut other = (OFPacketOut) obj;
        if (actionList == null) {
            if (other.actionList != null) {
                return false;
            }
        } else if (!actionList.equals(other.actionList)) {
            return false;
        }
        if (actionNum != other.actionNum) {
            return false;
        }
        if (bufferId != other.bufferId) {
            return false;
        }
        if (inPort != other.inPort) {
            return false;
        }
        if (packetLen != other.packetLen)
        {
            return false;
        }
        return Arrays.equals(data, other.data);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OFPacketOut [actionFactory=" + actionFactory + ", actions="
                + actionList + ", actionNum=" + actionNum + ", bufferId=0x"
                + Integer.toHexString(bufferId) + ", inPort=" + inPort + ",packetLen="+packetLen+", packetData="
                + Arrays.toString(data) + "]";
    }

    public OFPacketOut clone() throws CloneNotSupportedException
    {
        OFPacketOut ofpo = (OFPacketOut)super.clone();
        //clone the cactionList
        List<OFAction> cactionList = new ArrayList<OFAction>();
        for(int i=0;i<actionNum;i++)
        {
            cactionList.add(actionList.get(i).clone());
        }
        this.setActions(cactionList);
        //clone the data
        byte[] cdata = Arrays.copyOf(this.data, data.length);
        this.setPacketData(cdata);
        return ofpo;
    }

    public void computeLength() {
        this.length=(short)MAXMUN_LENGTH;
    }
}
