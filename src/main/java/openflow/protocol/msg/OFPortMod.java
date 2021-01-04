package openflow.protocol.msg;


/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public class OFPortMod extends OFPortStatus {
    public OFPortMod(){
        super();
        this.type = OFType.PORT_MOD;
    }

    public void setOpenflowEnable() {
        desc.setOpenflowEnable((byte) 1);
    }
}
