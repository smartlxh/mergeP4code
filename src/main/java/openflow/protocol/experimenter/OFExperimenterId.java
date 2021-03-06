package openflow.protocol.experimenter;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for the vendor ID corresponding to vendor extensions from a
 * given vendor. It is responsible for knowing how to parse out some sort of
 * data type value from the vendor data in an OFVendor message so that we can
 * dispatch to the different subclasses of OFVendorData corresponding to the
 * different formats of data for the vendor extensions.
 *
 * @author Rob Vaterlaus (rob.vaterlaus@bigswitch.com)
 */
public abstract class OFExperimenterId {
    static Map<Integer, OFExperimenterId> mapping = new HashMap<Integer, OFExperimenterId>();

    /**
     * The vendor id value, typically the OUI of the vendor prefixed with 0.
     */
    protected int id;

    /**
     * Register a new vendor id.
     * @param experimenterId the experimenter id to register
     */
    public static void registerExperimenterId(OFExperimenterId experimenterId) {
        mapping.put(experimenterId.getId(), experimenterId);
    }

    /**
     * Lookup the OFVendorId instance corresponding to the given id value.
     * @param id the integer vendor id value
     * @return the corresponding OFVendorId that's been registered for the
     *     given value, or null if there id has not been registered.
     */
    public static OFExperimenterId lookupExperimenterId(int id) {
        return mapping.get(id);
    }

    /**
     * Create an OFVendorId with the give vendor id value
     * @param id
     */
    public OFExperimenterId(int id) {
        this.id = id;
    }

    /**
     * @return the vendor id value
     */
    public int getId() {
        return id;
    }

    /**
     * This function parses enough of the data from the channel buffer to be
     * able to determine the appropriate OFVendorDataType for the data.
     *
     * @param data the channel buffer containing the vendor data.
     * @param length the length to the end of the enclosing message
     * @return the OFVendorDataType that can be used to instantiate the
     *         appropriate subclass of OFVendorData.
     */
    public abstract OFExperimenterDataType parseExperimenterDataType(ByteBuf data, int length);
}
