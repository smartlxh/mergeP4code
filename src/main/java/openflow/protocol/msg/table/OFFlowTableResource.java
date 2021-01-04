package openflow.protocol.msg.table;

import io.netty.buffer.ByteBuf;
import openflow.protocol.msg.OFMessage;
import openflow.protocol.msg.OFType;
import util.HexString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFFlowTableResource extends OFMessage {
    public static int MINIMUM_LENGTH = OFMessage.MINIMUM_LENGTH + 16;
    public static int MAXIMAL_LENGTH = OFFlowTableResource.MINIMUM_LENGTH + OFTableResource.MINIMUM_LENGTH * OFTableType.MAX_TABLE_TYPE;

    public enum POFResourceReportType{
        POFRRT_FLOW_TABLE
    }

    protected POFResourceReportType resourceType;

    protected int counterNum;

    protected int meterNum;
    protected int groupNum;

    protected Map<OFTableType, OFTableResource> tableResourcesMap;

    public OFFlowTableResource(){
        super();
        super.setType(OFType.RESOURCE_REPORT);
        super.setLength((short)MAXIMAL_LENGTH);
    }

    public POFResourceReportType getResourceType() {
        return resourceType;
    }
    public void setResourceType(POFResourceReportType type) {
        this.resourceType = type;
    }

    public Map<OFTableType, OFTableResource> getTableResourcesMap() {
        return tableResourcesMap;
    }
    public void setTableResourcesMap(Map<OFTableType, OFTableResource> tableResourcesMap) {
        this.tableResourcesMap = tableResourcesMap;
    }

    public int getCounterNum() {
        return counterNum;
    }
    public void setCounterNum(int counterNum) {
        this.counterNum = counterNum;
    }
    public int getMeterNum() {
        return meterNum;
    }
    public void setMeterNum(int meterNum) {
        this.meterNum = meterNum;
    }
    public int getGroupNum() {
        return groupNum;
    }
    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    @Override
    public void readFrom(ByteBuf data){
        super.readFrom(data);

        this.resourceType = POFResourceReportType.values()[ data.readByte() ];
        data.readBytes(new byte[3]);

        this.counterNum = data.readInt();

        this.meterNum = data.readInt();
        this.groupNum = data.readInt();

        if (this.tableResourcesMap == null) {
            this.tableResourcesMap = new ConcurrentHashMap<OFTableType, OFTableResource>();
        } else {
            this.tableResourcesMap.clear();
        }
        OFTableResource tableResource;
        for(int i = 0; i < OFTableType.MAX_TABLE_TYPE; i++){
            tableResource = new OFTableResource();
            tableResource.readFrom(data);
            if(tableResource.getTableType() != OFTableType.values()[i]){
                throw new RuntimeException("TableResource[" + i + "] Type = " + OFTableType.values()[i] +" Error!");
            }
            this.tableResourcesMap.put(OFTableType.values()[i], tableResource);
        }
    }


    // add by qsmywxd and remain to discuss
    public void writeZero(ByteBuf data, int length)
    {
        data.writeBytes(new byte[length]);
    }

    @Override
    public void writeTo(ByteBuf data){
        super.writeTo(data);
        data.writeByte( (byte)resourceType.ordinal() );
        writeZero(data,3);

        data.writeInt(counterNum);

        data.writeInt(meterNum);
        data.writeInt(groupNum);

        if (this.tableResourcesMap != null){
            OFTableResource tableResource;
            for(int i = 0; i < OFTableType.MAX_TABLE_TYPE; i++){
                tableResource = this.tableResourcesMap.get(OFTableType.values()[i]);

                if(tableResource == null){
                    writeZero(data, OFTableResource.MINIMUM_LENGTH);
                }else{
                    if(tableResource.getTableType() != OFTableType.values()[i]){
                        throw new RuntimeException("TableResource[" + i + "] Type = " + OFTableType.values()[i] +" Error!");
                    }
                    tableResource.writeTo(data);
                }
            }
        }else{
            writeZero(data, OFTableType.MAX_TABLE_TYPE * OFTableResource.MINIMUM_LENGTH);
        }
    }

    public String toBytesString(){
        //String bytesString = super.toBytesString();

        String bytesString = null;
        bytesString += HexString.toHex((byte)resourceType.ordinal());
        bytesString += HexString.getByteZeros(3);

        bytesString += HexString.toHex(counterNum);

        bytesString += HexString.toHex(meterNum);

        bytesString += HexString.toHex(groupNum);

        if (this.tableResourcesMap != null){
            OFTableResource tableResource;
            for(int i = 0; i < OFTableType.MAX_TABLE_TYPE; i++){
                tableResource = this.tableResourcesMap.get(OFTableType.values()[i]);

                if(tableResource == null){
                    bytesString += HexString.getByteZeros(OFTableResource.MINIMUM_LENGTH);
                }else{
                    if(tableResource.getTableType() != OFTableType.values()[i]){
                        throw new RuntimeException("TableResource[" + i + "] Type = " + OFTableType.values()[i] +" Error!");
                    }
                    bytesString += tableResource.toBytesString();
                }
            }
        }else{
            bytesString += HexString.getByteZeros(OFTableType.MAX_TABLE_TYPE * OFTableResource.MINIMUM_LENGTH);
        }

        return bytesString;
    }

    @Override
    public String toString(){
        String string =  super.toString() +
                "; FlowTableResource:" +
                "rt=" + this.resourceType.ordinal() +
                ";cn=" + this.counterNum +
                ";mn=" + this.meterNum +
                ";gn=" + this.groupNum;
        if (this.tableResourcesMap != null){
            OFTableResource tableResource;
            for(int i = 0; i < OFTableType.MAX_TABLE_TYPE; i++){
                tableResource = this.tableResourcesMap.get(OFTableType.values()[i]);
                string += ";t[" + i + "]=" + ((tableResource==null)? "null" : tableResource.toString());
            }
        }else{
            string += ";trd=null";
        }
        return string;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + counterNum;
        result = prime * result + groupNum;
        result = prime * result + meterNum;
        result = prime * result
                + ((resourceType == null) ? 0 : resourceType.hashCode());
        result = prime
                * result
                + ((tableResourcesMap == null) ? 0 : tableResourcesMap
                .hashCode());
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
        OFFlowTableResource other = (OFFlowTableResource) obj;
        if (counterNum != other.counterNum)
            return false;
        if (groupNum != other.groupNum)
            return false;
        if (meterNum != other.meterNum)
            return false;
        if (resourceType != other.resourceType)
            return false;
        if (tableResourcesMap == null) {
            return other.tableResourcesMap == null;
        } else return tableResourcesMap.equals(other.tableResourcesMap);
    }



    public void computeLength() {
        this.length = (short) MAXIMAL_LENGTH;
    }
}
