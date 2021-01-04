package openflow.factory;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public interface OFActionFactoryAware {
    /**
     * Sets the POFActionFactory
     * @param actionFactory
     */
    public void setActionFactory(OFActionFactory actionFactory);
}
