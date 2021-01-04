package openflow.factory;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public interface OFMessageFactoryAware {
    /**
     * Sets the message factory for this object
     *
     * @param factory
     */
    void setMessageFactory(OFMessageFactory factory);
}
