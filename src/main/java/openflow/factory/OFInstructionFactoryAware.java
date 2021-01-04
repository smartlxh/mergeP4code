package openflow.factory;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public interface OFInstructionFactoryAware {
    /**
     * Sets the POFInstructionFactory
     * @param instructionFactory
     */
    public void setInstructionFactory(OFInstructionFactory instructionFactory);
}
