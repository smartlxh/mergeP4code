package openflow.protocol.port;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public enum OFPort {
    OFPP_MAX                ((int)0x00ffffff),
    OFPP_IN_PORT            ((int)0xfffffff8),
    OFPP_TABLE              ((int)0xfffffff9),
    OFPP_NORMAL             ((int)0xfffffffa),
    OFPP_FLOOD              ((int)0xfffffffb),
    OFPP_ALL                ((int)0xfffffffc),
    OFPP_CONTROLLER         ((int)0xfffffffd),
    OFPP_LOCAL              ((int)0xfffffffe),
    OFPP_ANY                ((int)0xffffffff);

    protected int value;

    private OFPort(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
