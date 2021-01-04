package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;
import util.Converter;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFMeterMod extends OFMessage {
    public static int MINIMUM_LENGTH = 8 + 16;

    public enum POFMeterModCmd {
        POFMC_ADD,
        POFMC_MODIFY,
        POFMC_DELETE
    }

    protected POFMeterModCmd command;
    protected short slotID;
    protected int meterId;
    protected int rate;    //kbps

    public OFMeterMod(){
        super();
        this.type = OFType.METER_MOD;
        this.length = Converter.convertIntToShort(MINIMUM_LENGTH);
    }

    @Override
    public void readFrom(ByteBuf data) {
        super.readFrom(data);
        command = POFMeterModCmd.values()[ data.readByte() ];
        data.readByte();
        slotID = data.readShort();
        meterId = data.readInt();
        rate = data.readInt();
        //padding
        byte[] padding=new byte[4];
        data.readBytes(padding);
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);
        data.writeByte((byte)command.ordinal());
        data.writeByte((byte)0);
        data.writeShort(slotID);
        data.writeInt(meterId);
        data.writeInt(rate);
        data.writeBytes(new byte[4]);
    }


    public String toString(){
        return super.toString() +
                "; MeterMod:" +
                "cmd=" + command +
                ";slotID=" + slotID +
                ";rate=" + rate +
                ";meterid=" + meterId;
    }

    public void setSlotId(short slotid){
        this.slotID = slotid;
    }
    public short getSlotId(){
        return this.slotID;
    }
    public POFMeterModCmd getCommand() {
        return command;
    }

    public void setCommand(POFMeterModCmd command) {
        this.command = command;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getMeterId() {
        return meterId;
    }

    public void setMeterId(int meterId) {
        this.meterId = meterId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((command == null) ? 0 : command.hashCode());
        result = prime * result + slotID;
        result = prime * result + meterId;
        result = prime * result + rate;
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
        OFMeterMod other = (OFMeterMod) obj;
        if (command != other.command)
            return false;
        if (meterId != other.meterId)
            return false;
        if (rate != other.rate)
            return false;
        if (slotID != other.slotID)
            return false;
        return true;
    }

    public void computeLength()
    {
        this.length=(short) MINIMUM_LENGTH;
    }

    public OFMeterMod clone() throws CloneNotSupportedException {
        OFMeterMod metermod = (OFMeterMod) super.clone();
        metermod.setCommand(this.getCommand());
        metermod.setMeterId(this.getMeterId());
        metermod.setRate(this.getRate());
        metermod.setSlotId(this.getSlotId());
        return metermod;
    }
}
