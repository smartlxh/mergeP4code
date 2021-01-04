package openflow.protocol.msg.table;

import io.netty.buffer.ByteBuf;
import openflow.protocol.msg.OFMessage;
import openflow.protocol.msg.OFType;
import util.Converter;

/**
 * OFTableMod<br>
 * {<br>
 *      {@link OFMessage} header;<br>
 *      {@link OFFlowTable} flowTable;<br>
 * } //sizeof()= 8 + {@link OFFlowTable#MINIMUM_LENGTH}
 *
 * @see OFFlowTable
 *
 * @author Song Jian (jack.songjian@huawei.com), Huawei Technologies Co., Ltd.
 *
 */
public class OFTableMod extends OFMessage implements Cloneable{
    public static final int MINIMUM_LENGTH = OFMessage.MINIMUM_LENGTH + OFFlowTable.MAXIMAL_LENGTH;

    protected OFFlowTable flowTable;

    public enum OFTableModCmd {
        OFPTC_ADD,
        OFPTC_MODIFY,
        OFPTC_DELETE
    }

    public OFTableMod(){
        super();
        this.type = OFType.TABLE_MOD;
        this.length = Converter.convertIntToShort(MINIMUM_LENGTH);
        flowTable = new OFFlowTable();
    }

    @Override
    public void readFrom(ByteBuf data) {
        super.readFrom(data);
        if(flowTable == null){
            flowTable = new OFFlowTable();
        }
        flowTable.readFrom(data);
    }


    // add by qsmywxd and remain to discuss
    public void writeZero(ByteBuf data, int length)
    {
        data.writeBytes(new byte[length]);
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);
        if(flowTable == null){
            writeZero(data, OFFlowTable.MAXIMAL_LENGTH);
        }else{
            flowTable.writeTo(data);
        }
    }

    public String toBytesString(){
//        return super.toBytesString() +
//                flowTable.toBytesString();
        return "TableMod"+flowTable.toBytesString();
    }

    public String toString(){
        return super.toString() +
                "; TableMod:" +
                flowTable.toString();
    }

    public OFFlowTable getFlowTable() {
        return flowTable;
    }

    public void setFlowTable(OFFlowTable flowTable) {
        this.flowTable = flowTable;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((flowTable == null) ? 0 : flowTable.hashCode());
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
        OFTableMod other = (OFTableMod) obj;
        if (flowTable == null) {
            return other.flowTable == null;
        } else return flowTable.equals(other.flowTable);
    }

    @Override
    public OFTableMod clone() throws CloneNotSupportedException {
        OFTableMod neoTableMod= (OFTableMod) super.clone();
        if(null != flowTable){
            neoTableMod.setFlowTable(flowTable.clone());
        }

        return neoTableMod;
    }

    public void computeLength()
    {
        this.length = (short) MINIMUM_LENGTH;
    }
}

