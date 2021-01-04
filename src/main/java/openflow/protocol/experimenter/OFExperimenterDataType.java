package openflow.protocol.experimenter;

import openflow.protocol.Instantiable;

/**
 * Class that represents a specific vendor data type format in an
 * OFVendor message. Typically the vendor data will begin with an integer
 * code that determines the format of the rest of the data, but this
 * class does not assume that. It's basically just a holder for an
 * instantiator of the appropriate subclass of OFVendorData.
 *
 * @author Rob Vaterlaus (rob.vaterlaus@bigswitch.com)
 */
public class OFExperimenterDataType {
    /**
     * Object that instantiates the subclass of OFVendorData
     * associated with this data type.
     */
    protected Instantiable<OFExperimenterData> instantiable;

    /**
     * Construct an empty vendor data type.
     */
    public OFExperimenterDataType() {
        super();
    }

    /**
     * Construct a vendor data type with the specified instantiable.
     * @param instantiable object that creates the subclass of OFVendorData
     *     associated with this data type.
     */
    public OFExperimenterDataType(Instantiable<OFExperimenterData> instantiable) {
        this.instantiable = instantiable;
    }

    /**
     * Returns a new instance of a subclass of OFVendorData associated with
     * this OFVendorDataType.
     *
     * @return the new object
     */
    public OFExperimenterData newInstance() {
        return instantiable.instantiate();
    }

    /**
     * @return the instantiable
     */
    public Instantiable<OFExperimenterData> getInstantiable() {
        return instantiable;
    }

    /**
     * @param instantiable the instantiable to set
     */
    public void setInstantiable(Instantiable<OFExperimenterData> instantiable) {
        this.instantiable = instantiable;
    }
}
