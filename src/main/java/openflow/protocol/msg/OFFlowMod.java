package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;
import openflow.factory.OFActionFactory;
import openflow.factory.OFActionFactoryAware;
import openflow.factory.OFInstructionFactory;
import openflow.factory.OFInstructionFactoryAware;
import openflow.protocol.OFGlobal;
import openflow.protocol.match.POFMatchX;
import openflow.protocol.msg.instruction.OFInstruction;
import openflow.protocol.msg.table.OFTableType;
import util.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFFlowMod extends OFMessage implements OFActionFactoryAware, OFInstructionFactoryAware, Cloneable {
    public static final int MINIMUM_LENGTH = OFMessage.MINIMUM_LENGTH + 40;  //48
    public static final int MAXIMAL_LENGTH = OFMessage.MINIMUM_LENGTH
            + 40
            + POFMatchX.MINIMUM_LENGTH * OFGlobal.OFP_MAX_MATCH_FIELD_NUM
            + OFInstruction.MAXIMAL_LENGTH * OFGlobal.OFP_MAX_INSTRUCTION_NUM;

    public enum POFFlowEntryCmd {
        POFFC_ADD,                  /* New flow. */
        POFFC_MODIFY,               /* Modify all matching flows. */
        POFFC_MODIFY_STRICT,        /* Modify entry strictly matching wildcards */
        POFFC_DELETE,               /* Delete all matching flows. */
        POFFC_DELETE_STRICT         /* Strictly match wildcards and priority. */
    }


    // Open Flow Flow Mod Flags. Use "or" operation to set multiple flags
    public static final short POFFF_SEND_FLOW_REM = 0x1; // 1 << 0
    public static final short POFFF_CHECK_OVERLAP = 0x2; // 1 << 1
    public static final short POFFF_EMERG         = 0x4; // 1 << 2

    protected byte command;
    protected byte matchFieldNum;
    protected byte instructionNum;
    protected int counterId;

    protected long cookie;
    protected long cookieMask;

    protected byte tableId;
    protected OFTableType tableType;
    protected short idleTimeout;
    protected short hardTimeout;
    protected short priority;

    protected int index;
    protected short slotId;

    protected List<POFMatchX> matchList;
    protected List<OFInstruction> instructionList;

    protected OFInstructionFactory instructionFactory;
    protected OFActionFactory actionFactory;

    public OFFlowMod() {
        super();
        this.type = OFType.FLOW_MOD;
        this.length = Converter.convertIntToShort(MINIMUM_LENGTH);
    }

    public int getLengthU() {
        return MAXIMAL_LENGTH;
    }

    /**
     * Get cookie
     * @return cookie
     */
    public long getCookie() {
        return this.cookie;
    }

    /**
     * Set cookie
     * @param cookie
     */
    public OFFlowMod setCookie(long cookie) {
        this.cookie = cookie;
        return this;
    }

    /**
     * Get command
     * @return command
     */
    public byte getCommand() {
        return this.command;
    }

    /**
     * Set command
     * @param command
     */
    public OFFlowMod setCommand(byte command) {
        this.command = command;
        return this;
    }

    public OFFlowMod setCommand(POFFlowEntryCmd cmd)
    {
        this.command = (byte) cmd.ordinal();
        return this;
    }

    /**
     * Get hard_timeout
     * @return hardTimeout
     */
    public short getHardTimeout() {
        return this.hardTimeout;
    }

    /**
     * Set hard_timeout
     * @param hardTimeout
     */
    public OFFlowMod setHardTimeout(short hardTimeout) {
        this.hardTimeout = hardTimeout;
        return this;
    }

    /**
     * Get idle_timeout
     * @return idleTimeout
     */
    public short getIdleTimeout() {
        return this.idleTimeout;
    }

    /**
     * Set idle_timeout
     * @param idleTimeout
     */
    public OFFlowMod setIdleTimeout(short idleTimeout) {
        this.idleTimeout = idleTimeout;
        return this;
    }

    /**
     * Get priority
     * @return priority
     */
    public short getPriority() {
        return this.priority;
    }

    /**
     * Set priority
     * @param priority
     */
    public OFFlowMod setPriority(short priority) {
        this.priority = priority;
        return this;
    }



    public byte getMatchFieldNum() {
        return matchFieldNum;
    }

    public void setMatchFieldNum(byte matchFieldNum) {
        this.matchFieldNum = matchFieldNum;
    }

    public byte getInstructionNum() {
        return instructionNum;
    }

    public void setInstructionNum(byte instructionNum) {
        this.instructionNum = instructionNum;
    }

    public int getCounterId() {
        return counterId;
    }

    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    public long getCookieMask() {
        return cookieMask;
    }

    public void setCookieMask(long cookieMask) {
        this.cookieMask = cookieMask;
    }

    public byte getTableId() {
        return tableId;
    }

    public void setTableId(byte tableId) {
        this.tableId = tableId;
    }

    public List<POFMatchX> getMatchList() {
        return matchList;
    }

    public List<OFInstruction> getInstructionList() {
        return instructionList;
    }

    public void setInstructionList(List<OFInstruction> instructionList) {
        this.instructionList = instructionList;
        this.instructionNum = (byte) instructionList.size();
    }

    public OFTableType getTableType() {
        return tableType;
    }

    public void setTableType(OFTableType tableType) {
        this.tableType = tableType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public OFInstructionFactory getInstructionFactory() {
        return instructionFactory;
    }

    public void setMatchList(List<POFMatchX> matchList) {
        this.matchList = matchList;
        this.matchFieldNum = (byte) matchList.size();
    }



    public short getSlotId() {
        return slotId;
    }

    public void setSlotId(short slotId) {
        this.slotId = slotId;
    }

    @Override
    public void readFrom(ByteBuf data) {
        super.readFrom(data);

        this.command = data.readByte();
        this.matchFieldNum = data.readByte();
        this.instructionNum = data.readByte();
        data.readByte();
        this.counterId = data.readInt();

        this.cookie = data.readLong();
        this.cookieMask = data.readLong();

        this.tableId = data.readByte();
        this.tableType = OFTableType.values()[ data.readByte() ];
        this.idleTimeout = data.readShort();
        this.hardTimeout = data.readShort();
        this.priority = data.readShort();

        this.index = data.readInt();
        this.slotId = data.readShort();
        //padding
        byte[] padding=new byte[2];
        data.readBytes(padding);

        if (this.matchList == null){
            this.matchList = new ArrayList<POFMatchX>();
        }else{
            this.matchList.clear();
        }
        POFMatchX matchX;
        for(int i = 0; i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM; i++){
            matchX = new POFMatchX();
            matchX.readFrom(data);
            this.matchList.add(matchX);
        }

        if(this.instructionFactory == null){
            throw new RuntimeException("POFInstructionFactory not set");
        }
        this.instructionList = this.instructionFactory.parseInstructions(data, OFGlobal.OFP_MAX_INSTRUCTION_NUM * OFInstruction.MAXIMAL_LENGTH);
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);

        data.writeByte(this.command);
        data.writeByte(this.matchFieldNum);
        data.writeByte(this.instructionNum);
        data.writeByte((byte)0);
        data.writeInt(this.counterId);

        data.writeLong(this.cookie);
        data.writeLong(this.cookieMask);

        data.writeByte(this.tableId);
        data.writeByte(this.tableType.getValue());
        data.writeShort(idleTimeout);
        data.writeShort(hardTimeout);
        data.writeShort(priority);

        data.writeInt(index);
        data.writeShort(slotId);
        data.writeBytes(new byte[2]);

        if (this.matchList == null){
            data.writeBytes(new byte[OFGlobal.OFP_MAX_MATCH_FIELD_NUM * POFMatchX.MINIMUM_LENGTH]);
        }else{
            POFMatchX matchX;

            if(matchFieldNum > matchList.size()){
                throw new RuntimeException("matchFieldNum " + matchFieldNum + " > matchList.size()" + matchList.size());
            }

            int i = 0;
            for(; i < matchFieldNum && i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM; i++){
                matchX = matchList.get(i);
                if(matchX == null){
                    data.writeBytes(new byte[POFMatchX.MINIMUM_LENGTH]);
                }else{
                    matchX.writeTo(data);
                }
            }
            if(i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM){
                data.writeBytes( new byte[(OFGlobal.OFP_MAX_MATCH_FIELD_NUM - i) * POFMatchX.MINIMUM_LENGTH]);
            }
        }

        if (this.instructionList == null){
            data.writeBytes(new byte[OFGlobal.OFP_MAX_INSTRUCTION_NUM * OFInstruction.MAXIMAL_LENGTH]);
        }else{
            OFInstruction instruction;

            if(instructionNum > instructionList.size()){
                throw new RuntimeException("instructionNum " + instructionNum + " > instructionList.size()" + instructionList.size());
            }

            int i = 0;
            for(; (i < instructionNum) && (i < OFGlobal.OFP_MAX_INSTRUCTION_NUM); i++){
                instruction = instructionList.get(i);
                if(instruction == null){
                    data.writeBytes(new byte[OFInstruction.MAXIMAL_LENGTH]);
                }else{
                    instruction.writeTo(data);
                    if(instruction.getLength() < OFInstruction.MAXIMAL_LENGTH){
                        data.writeBytes(new byte[OFInstruction.MAXIMAL_LENGTH - instruction.getLength()]);
                    }
                }
            }
            if(i < OFGlobal.OFP_MAX_INSTRUCTION_NUM){
                data.writeBytes( new byte[(OFGlobal.OFP_MAX_INSTRUCTION_NUM - i) * OFInstruction.MAXIMAL_LENGTH]);
            }
        }
    }



    @Override
    public String toString(){
        String string = super.toString();
        string += "; FlowEntry:" +
                "cmd=" + command +
                ";mfn=" + matchFieldNum +
                ";isn=" + instructionNum +
                ";cid=" + counterId +
                ";ck=" + cookie +
                ";ckm=" + cookieMask +
                ";tid=" + tableId +
                ";tt=" + tableType +
                ";it=" + idleTimeout +
                ";ht=" + hardTimeout +
                ";p=" + priority +
                ";i=" + index;
        if(this.matchList != null){
            string += ";match(" + matchFieldNum + ")=";
            for(int i=0;i<matchFieldNum;i++){
                string += matchList.get(i).toString() + ",";
            }
        }else{
            string += ";match=null";
        }

        if(this.instructionList != null){
            string += ";ins(" + instructionNum + ")=";
            for(int i=0;i<instructionNum;i++){
                string += instructionList.get(i).toString() + ",";
            }
        }else{
            string += ";ins=null";
        }

        return string;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + command;
        result = prime * result + (int) (cookie ^ (cookie >>> 32));
        result = prime * result + (int) (cookieMask ^ (cookieMask >>> 32));
        result = prime * result + counterId;
        result = prime * result + hardTimeout;
        result = prime * result + idleTimeout;
        result = prime * result + index;
        result = prime * result + ((instructionFactory == null) ? 0 : instructionFactory.hashCode());
        result = prime * result + ((instructionList == null) ? 0 : instructionList.hashCode());
        result = prime * result + instructionNum;
        result = prime * result + matchFieldNum;
        result = prime * result + ((matchList == null) ? 0 : matchList.hashCode());
        result = prime * result + priority;
        result = prime * result + tableId;
        result = prime * result + ((tableType == null) ? 0 : tableType.hashCode());
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
        OFFlowMod other = (OFFlowMod) obj;
        if (command != other.command)
            return false;
        if (cookie != other.cookie)
            return false;
        if (cookieMask != other.cookieMask)
            return false;
        if (counterId != other.counterId)
            return false;
        if (hardTimeout != other.hardTimeout)
            return false;
        if (idleTimeout != other.idleTimeout)
            return false;
        if (index != other.index)
            return false;
        if (instructionFactory == null) {
            if (other.instructionFactory != null)
                return false;
        } else if (!instructionFactory.equals(other.instructionFactory))
            return false;
        if (instructionList == null) {
            if (other.instructionList != null)
                return false;
        } else if (!instructionList.equals(other.instructionList))
            return false;
        if (instructionNum != other.instructionNum)
            return false;
        if (matchFieldNum != other.matchFieldNum)
            return false;
        if (matchList == null) {
            if (other.matchList != null)
                return false;
        } else if (!matchList.equals(other.matchList))
            return false;
        if (priority != other.priority)
            return false;
        if (tableId != other.tableId)
            return false;
        return tableType == other.tableType;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public OFFlowMod clone() throws CloneNotSupportedException {
        OFFlowMod flowMod= (OFFlowMod) super.clone();

        if(null != matchList
                && 0 != matchList.size()
                && 0 != matchFieldNum){
            List<POFMatchX> neoMatchXList = new ArrayList<POFMatchX>();
            for(POFMatchX matchX: this.matchList){
                neoMatchXList.add((POFMatchX) matchX.clone());
            }
            flowMod.setMatchList(neoMatchXList);
        }

        if(null != instructionList
                && 0 != instructionList.size()
                && 0 != instructionNum){
            List<OFInstruction> neoInstructionList = new ArrayList<OFInstruction>();
            for(OFInstruction ins: this.instructionList){
                neoInstructionList.add((OFInstruction) ins.clone());
            }
            flowMod.setInstructionList(neoInstructionList);
        }

        return flowMod;
    }

    @Override
    public void setInstructionFactory(OFInstructionFactory instructionFactory) {
        this.instructionFactory = instructionFactory;
    }

    public void computeLength()
    {
        this.length=MAXIMAL_LENGTH;
    }

    @Override
    public void setActionFactory(OFActionFactory actionFactory) {
        // TODO Auto-generated method stub
        this.actionFactory = actionFactory;
    }
}
