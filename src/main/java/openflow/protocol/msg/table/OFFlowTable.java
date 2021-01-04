package openflow.protocol.msg.table;

import io.netty.buffer.ByteBuf;
import openflow.protocol.OFGlobal;
import openflow.protocol.match.POFMatch;
import util.HexString;
import util.ParseString;

import java.util.ArrayList;
import java.util.List;

/**
 * OFFlowTable describes the openflow table information <br>
 * <P>
 * OFFlowTable<br>
 * {<br>
 *      int8    command;<br>
 *      int8    tableId;<br>
 *      int8    tableType;<br>
 *      int8    matchFieldNum;<br>
 *      int32   tableSize;<br>
 *      <br>
 *      int16   keyLength;<br>
 *      int8    reserve[6];<br>
 *      <br>
 *      int8    tableName[{@link OFGlobal#OFP_NAME_MAX_LENGTH}];<br>
 *      <br>
 *      List<{@link POFMatch}> matchFieldList; <br>
 *  } //sizeof() = 16 + {@link OFGlobal#OFP_NAME_MAX_LENGTH}<br>
 *
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 */

public class OFFlowTable implements Cloneable{
    public static final int MINIMUM_LENGTH = OFGlobal.OFP_NAME_MAX_LENGTH + 16;
    public static final int MAXIMAL_LENGTH = OFGlobal.OFP_NAME_MAX_LENGTH + 16  + OFGlobal.OFP_MAX_MATCH_FIELD_NUM * POFMatch.MINIMUM_LENGTH;

    protected OFTableMod.OFTableModCmd command;
    protected byte tableId;
    protected OFTableType tableType;
    protected byte matchFieldNum;
    protected int tableSize;

    protected short keyLength;
    protected short slotId;
    protected String tableName;
    protected List<POFMatch> matchFieldList;


    public OFFlowTable(String tableName, byte tableId) {
        super();
        this.tableName = tableName;
        this.tableId = tableId;
        matchFieldList = new ArrayList<POFMatch>();
    }

    public OFFlowTable() {
        super();
        matchFieldList = new ArrayList<POFMatch>();
    }



    public short getSlotId() {
        return slotId;
    }

    public void setSlotId(short slotId) {
        this.slotId = slotId;
    }

    public void readFrom(ByteBuf data){
        command = OFTableMod.OFTableModCmd.values()[data.readByte()];
        tableId = data.readByte();
        tableType = OFTableType.values()[ data.readByte() ];
        matchFieldNum = data.readByte();
        tableSize = data.readInt();

        keyLength = data.readShort();
        slotId = data.readShort();
        data.readBytes(new byte[4]);

        tableName = ParseString.NameByteToString(data);

        POFMatch matchField;
        for(int i = 0; i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM; i++){
            matchField = new POFMatch();
            matchField.readFrom(data);
            matchFieldList.add(matchField);
        }
    }





    public void writeTo(ByteBuf data){
        data.writeByte((byte) command.ordinal());
        data.writeByte(tableId);
        data.writeByte(tableType.getValue());
        data.writeByte(matchFieldNum);
        data.writeInt(tableSize);

        data.writeShort(keyLength);
        data.writeShort(slotId);
        data.writeBytes(new byte[4]);
        data.writeBytes(ParseString.NameStringToBytes(tableName) );

        if (this.matchFieldList == null){
            data.writeBytes(new byte[OFGlobal.OFP_MAX_MATCH_FIELD_NUM * POFMatch.MINIMUM_LENGTH]);
        }else{
            POFMatch matchField;
            if(matchFieldNum > matchFieldList.size()){
                throw new RuntimeException("matchFieldNum " + matchFieldNum + " > matchFieldList.size()" + matchFieldList.size());
            }
            int i = 0;
            for(; i < matchFieldNum && i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM; i++){
                matchField = matchFieldList.get(i);
                matchField.writeTo(data);
            }
            if(i < OFGlobal.OFP_MAX_MATCH_FIELD_NUM){
                data.writeBytes(new byte[(OFGlobal.OFP_MAX_MATCH_FIELD_NUM - i) * POFMatch.MINIMUM_LENGTH]);
            }
        }
    }

    public String toBytesString(){
        String string = HexString.toHex((byte)command.ordinal());
        string += HexString.toHex(tableId);
        string += HexString.toHex((byte)tableType.ordinal());
        string += HexString.toHex(matchFieldNum);
        string += " ";
        string += HexString.toHex(tableSize);
        string += HexString.toHex(keyLength);
        string += HexString.getByteZeros(6);
        string += HexString.nameToHex(tableName);
        return string;
    }

    public String toString(){
        String string = "cmd=" + command +
                ";stid=" + tableId +
                ";tt=" + tableType +
                ";mn=" + matchFieldNum +
                ";size=" + tableSize +
                ";kl=" + keyLength +
                ";tn=" + tableName;
        if(this.matchFieldList != null){
            string += ";match(" + matchFieldList.size() + ")=";
            for(POFMatch match : matchFieldList){
                string += match.toString() + ",";
            }
        }else{
            string += ";match=null";
        }
        return string;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((command == null) ? 0 : command.hashCode());
        result = prime * result + keyLength;
        result = prime * result
                + ((matchFieldList == null) ? 0 : matchFieldList.hashCode());
        result = prime * result + matchFieldNum;
        result = prime * result + tableId;
        result = prime * result
                + ((tableName == null) ? 0 : tableName.hashCode());
        result = prime * result + tableSize;
        result = prime * result
                + ((tableType == null) ? 0 : tableType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OFFlowTable other = (OFFlowTable) obj;
        if (command != other.command)
            return false;
        if (keyLength != other.keyLength)
            return false;
        if (matchFieldList == null) {
            if (other.matchFieldList != null)
                return false;
        } else if (!matchFieldList.equals(other.matchFieldList))
            return false;
        if (matchFieldNum != other.matchFieldNum)
            return false;
        if (tableId != other.tableId)
            return false;
        if (tableName == null) {
            if (other.tableName != null)
                return false;
        } else if (!tableName.equals(other.tableName))
            return false;
        if (tableSize != other.tableSize)
            return false;
        if (tableType != other.tableType)
            return false;
        return true;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public byte getTableId() {
        return tableId;
    }

    public void setTableId(byte tableId) {
        this.tableId = tableId;
    }

    public OFTableType getTableType() {
        return tableType;
    }

    public void setTableType(OFTableType type) {
        this.tableType = type;
    }

    public short getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(short keyLength) {
        this.keyLength = keyLength;
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public byte getMatchFieldNum() {
        return matchFieldNum;
    }

    public void setMatchFieldNum(byte matchFieldNum) {
        this.matchFieldNum = matchFieldNum;
    }

    public List<POFMatch> getMatchFieldList() {
        return matchFieldList;
    }

    public void setMatchFieldList(List<POFMatch> matchFieldList) {
        this.matchFieldList = matchFieldList;
        this.matchFieldNum = (byte) matchFieldList.size();
    }

    public OFTableMod.OFTableModCmd getCommand() {
        return command;
    }

    public void setCommand(OFTableMod.OFTableModCmd command) {
        this.command = command;
    }

    @Override
    public OFFlowTable clone() throws CloneNotSupportedException {
        OFFlowTable neoFlowTable= (OFFlowTable) super.clone();

        if(null != matchFieldList
                && 0 != matchFieldList.size()
                && 0 != matchFieldNum){
            List<POFMatch> neoMatchList = new ArrayList<POFMatch>();
            for(POFMatch matchField: this.matchFieldList){
                neoMatchList.add((POFMatch) matchField.clone());
            }
            neoFlowTable.setMatchFieldList(neoMatchList);
        }

        return neoFlowTable;
    }


}

