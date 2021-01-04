package openflow.factory;

/**
 * @author twd
 * @description
 * @date 2019-12-12
 */
public interface OFStatisticsFactoryAware {
    /**
     * Sets the OFStatisticsFactory
     * @param statisticsFactory
     */
    public void setStatisticsFactory(OFStatisticsFactory statisticsFactory);
}
