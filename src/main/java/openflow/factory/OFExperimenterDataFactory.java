package openflow.factory;

import io.netty.buffer.ByteBuf;
import openflow.protocol.experimenter.OFExperimenterData;
import openflow.protocol.experimenter.OFExperimenterDataType;
import openflow.protocol.experimenter.OFExperimenterId;

/**
 * @author twd
 * @description
 * @date 2019-12-12
 */
public interface OFExperimenterDataFactory {
    /**
     * Retrieves an OFVendorData instance corresponding to the specified
     * OFVendorId and OFVendorDataType. There are 3 possible cases for
     * how this will be called:
     *
     * 1) If the vendor id in the OFVendor message is an unknown value,
     *    then this method is called with both vendorId and vendorDataType
     *    set to null. In this case typically the factory method should
     *    return an instance of OFGenericVendorData that just contains
     *    the raw byte array of the vendor data.
     *
     * 2) If the vendor id is known but no vendor data type has been
     *    registered for the data in the message, then vendorId is set to
     *    the appropriate OFVendorId instance and OFVendorDataType is set
     *    to null. This would typically be handled the same way as #1
     *
     * 3) If both the vendor id and and vendor data type are known, then
     *    typically you'd just call the method in OFVendorDataType to
     *    instantiate the appropriate subclass of OFVendorData.
     *
     * @param experimenterId the experimenterId of the containing OFVendor message
     * @param experimenterDataType the type of the OFExperimenterData to be retrieved
     * @return an OFExperimenterData instance
     */
    public OFExperimenterData getExperimenterData(OFExperimenterId experimenterId,
                                                  OFExperimenterDataType experimenterDataType);

    /**
     * Attempts to parse and return the OFVendorData contained in the given
     * ChannelBuffer, beginning right after the vendor id.
     * @param experimenterId the experimenter id that was parsed from the OFVendor message.
     * @param data the ChannelBuffer from which to parse the vendor data
     * @param length the length to the end of the enclosing message.
     * @return an OFVendorData instance
     */
    public OFExperimenterData parseExperimenterData(int experimenterId, ByteBuf data,
                                                    int length);
}
