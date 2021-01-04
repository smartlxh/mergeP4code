package openflow.protocol;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public interface Instantiable<E> {
    /**
     * Create a new instance of a given subclass.
     * @return the new instance.
     */
    public E instantiate();
}
