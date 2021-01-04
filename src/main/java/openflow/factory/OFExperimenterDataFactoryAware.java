package openflow.factory;

/**
 * Classes implementing this interface are expected to be instantiated with an
 * instance of an OFVendorDataFactory
 *
 * @author Rob Vaterlaus (rob.vaterlaus@bigswitch.com)
 */
public interface OFExperimenterDataFactoryAware {
    public void setExperimenterDataFactory(OFExperimenterDataFactory experimenterDataFactory);
}
