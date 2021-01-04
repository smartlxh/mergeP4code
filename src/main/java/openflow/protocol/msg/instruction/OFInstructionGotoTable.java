package openflow.protocol.msg.instruction;

import io.netty.buffer.ByteBuf;
import openflow.protocol.OFGlobal;
import openflow.protocol.match.POFMatch;
import util.HexString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFInstructionGotoTable extends OFInstruction {
    public static final int MINIMUM_LENGTH = OFInstruction.MINIMUM_LENGTH + 8;
    public static final int MAXIMAL_LENGTH = OFInstruction.MINIMUM_LENGTH + 8 + POFMatch.MINIMUM_LENGTH * OFGlobal.OFP_MAX_MATCH_FIELD_NUM;

    protected byte nextTableId;
    protected byte matchFieldNum;
    protected short packetOffset;           //byte
    protected List<POFMatch> matchList;

    public OFInstructionGotoTable(){
        super.setType(OFInstructionType.GOTO_TABLE);
        super.setLength((short) MAXIMAL_LENGTH);
    }

    public short getLength() {
        return (short) MAXIMAL_LENGTH;
    }

    @Override
    public void readFrom(ByteBuf data){
        super.readFrom(data);
        this.nextTableId = data.readByte();
        this.matchFieldNum = data.readByte();
        this.packetOffset = data.readShort();
        data.readBytes(new byte[4]);

        if(matchList == null){
            matchList = new ArrayList<POFMatch>();
        }else{
            matchList.clear();
        }

        POFMatch match;
        for(int i = 0; i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM; i++){
            match = new POFMatch();
            match.readFrom(data);
            matchList.add(match);
        }
    }


    @Override
    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeByte(nextTableId);
        data.writeByte(matchFieldNum);
        data.writeShort(packetOffset);
        data.writeBytes(new byte[4]);

        if(matchList == null){
            data.writeBytes(new byte[(OFGlobal.OFP_MAX_MATCH_FIELD_NUM * POFMatch.MINIMUM_LENGTH)]);
        }else{
            POFMatch match;

            if(matchFieldNum > matchList.size()){
                throw new RuntimeException("matchFieldNum " + matchFieldNum + " > matchList.size()" + matchList.size());
            }

            int i;
            for(i = 0; i < matchFieldNum && i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM; i++){
                match = matchList.get(i);
                if(match == null){
                    data.writeBytes(new byte[POFMatch.MINIMUM_LENGTH]);
                }else{
                    match.writeTo(data);
                }
            }

            if(i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM){
                data.writeBytes(new byte[(OFGlobal.OFP_MAX_MATCH_FIELD_NUM - i) * POFMatch.MINIMUM_LENGTH]);
            }
        }
    }
    @Override
    public String toBytesString(){
        StringBuilder string= new StringBuilder(super.toBytesString() +
                HexString.toHex(nextTableId) +
                HexString.toHex(matchFieldNum) +
                HexString.toHex(packetOffset) +
                " " +
                HexString.getByteZeros(4));

        if (this.matchList == null) {
            string.append(HexString.getByteZeros(OFGlobal.OFP_MAX_MATCH_FIELD_NUM * POFMatch.MINIMUM_LENGTH));
        } else {
            POFMatch match20;

            if (matchFieldNum > matchList.size()) {
                throw new RuntimeException("matchFieldNum " + matchFieldNum + " > matchList.size()" + matchList.size());
            }

            int i = 0;
            for (; i < matchFieldNum && i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM; i++) {
                match20 = matchList.get(i);
                if (match20 == null) {
                    string.append(HexString.getByteZeros(POFMatch.MINIMUM_LENGTH));
                } else {
                    //string += match20.toBytesString();
                    string.append("gototable");
                }
            }
            if (i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM) {
                string.append(HexString.getByteZeros((OFGlobal.OFP_MAX_MATCH_FIELD_NUM - i) * POFMatch.MINIMUM_LENGTH));
            }
        }
        ;
        return string.toString();
    }

    @Override
    public String toString(){
        StringBuilder string = new StringBuilder(super.toString() +
                ";ntid=" + nextTableId +
                ";fn=" + matchFieldNum +
                ";poff=" + packetOffset);
        if(this.matchList != null){
            string.append(";match(").append(matchList.size()).append(")=");
            for(POFMatch match : matchList){
                string.append(match.toString()).append(",");
            }
        }else{
            string.append(";match=null");
        }

        return string.toString();
    }

    public byte getNextTableId() {
        return nextTableId;
    }

    public void setNextTableId(byte nextTableId) {
        this.nextTableId = nextTableId;
    }

    public short getPacketOffset() {
        return packetOffset;
    }

    public void setPacketOffset(short packetOffset) {
        this.packetOffset = packetOffset;
    }

    public byte getMatchFieldNum() {
        return matchFieldNum;
    }

    public void setMatchFieldNum(byte matchFieldNum) {
        this.matchFieldNum = matchFieldNum;
    }

    public List<POFMatch> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<POFMatch> matchList) {
        this.matchList = matchList;
    }

    @Override
    public int hashCode() {
        final int prime = 701;
        int result = super.hashCode();
        result = prime * result + matchFieldNum;
        result = prime * result + ((matchList == null) ? 0 : matchList.hashCode());
        result = prime * result + nextTableId;
        result = prime * result + packetOffset;
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

        OFInstructionGotoTable other = (OFInstructionGotoTable) obj;
        if (matchFieldNum != other.matchFieldNum)
            return false;
        if (matchList == null) {
            if (other.matchList != null)
                return false;
        } else if (!matchList.equals(other.matchList))
            return false;
        if (nextTableId != other.nextTableId)
            return false;
        return packetOffset == other.packetOffset;
    }

    @Override
    public OFInstructionGotoTable clone() throws CloneNotSupportedException {
        OFInstructionGotoTable ins = (OFInstructionGotoTable) super.clone();
        if(null != matchList
                && 0 != matchList.size()
                && 0 != matchFieldNum){
            ins.matchList = new ArrayList<POFMatch>();
            for(POFMatch matchField : this.matchList){
                ins.matchList.add(matchField.clone());
            }
        }

        return ins;
    }
}
