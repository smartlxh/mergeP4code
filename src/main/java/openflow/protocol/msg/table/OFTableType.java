package openflow.protocol.msg.table;

/**
 * @author twd
 * @description
 * @date 2019-12-03
 */
public enum OFTableType {
    /** MaskedMatch Table*/
    OF_MM_TABLE         (0),

    /** LongestPrefixMatch Table*/
    OF_LPM_TABLE        (1),

    /** ExactMatch Table*/
    OF_EM_TABLE         (2),

    /** Linear Table*/
    OF_LINEAR_TABLE     (3),

    OF_MAX_TABLE_TYPE   (4);

    protected byte value;

    /** max table type number.*/
    public final static int MAX_TABLE_TYPE = OF_MAX_TABLE_TYPE.getValue();

    public byte getValue(){
        return value;
    }

    private OFTableType(int value){
        this.value = (byte)value;
    }
}
