package openflow.protocol.msg;

import io.netty.buffer.ByteBuf;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFCounterMod extends OFMessage {
    public static int MINIMUM_LENGTH = OFMessage.MINIMUM_LENGTH + OFCounter.MINIMUM_LENGTH;

    protected OFCounter counter;

    public OFCounterMod(){
        super();
        super.setType(OFType.COUNTER_MOD);
        super.setLength((short)MINIMUM_LENGTH);
        if(counter == null){
            counter = new OFCounter();
        }
    }

    public void readFrom(ByteBuf data) {
        super.readFrom(data);
        if(counter == null){
            counter = new OFCounter();
        }
        counter.readFrom(data);
    }


    public String toString(){
        return "CounterMod:" + counter.toString();
    }

    @Override
    public void writeTo(ByteBuf data) {
        super.writeTo(data);
        if(counter == null){
            counter = new OFCounter();
        }
        counter.writeTo(data);
    }

    public OFCounter getCounter() {
        return counter;
    }

    public void setCounter(OFCounter counter) {
        this.counter = counter;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((counter == null) ? 0 : counter.hashCode());
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
        OFCounterMod other = (OFCounterMod) obj;
        if (counter == null) {
            return other.counter == null;
        } else return counter.equals(other.counter);
    }

    public void computeLength()
    {
        this.length=(short) MINIMUM_LENGTH;
    }

    @Override
    public OFCounterMod clone() throws CloneNotSupportedException {
        OFCounterMod counterMod= (OFCounterMod) super.clone();
        counterMod.setCounter(this.counter.clone());
        return counterMod;
    }
}
