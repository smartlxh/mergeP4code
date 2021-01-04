package openflow.protocol.msg.instruction;

import openflow.protocol.Instantiable;

import java.lang.reflect.Constructor;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public enum OFInstructionType {
    GOTO_TABLE(1, OFInstructionGotoTable.class, new Instantiable<OFInstruction>() {
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionGotoTable();
        }
    }),

    WRITE_METADATA(2, OFInstructionWriteMetadata.class, new Instantiable<OFInstruction>() {
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionWriteMetadata();
        }
    }),

    WRITE_ACTIONS(3, OFInstructionWriteActions.class, new Instantiable<OFInstruction>() {
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionWriteActions();
        }
    }),

    APPLY_ACTIONS(4, OFInstructionApplyActions.class, new Instantiable<OFInstruction>() {
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionApplyActions();
        }
    }),

    CLEAR_ACTIONS(5, OFInstructionClearActions.class, new Instantiable<OFInstruction>() {
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionClearActions();
        }
    }),

    METER(6, OFInstructionMeter.class, new Instantiable<OFInstruction>() {
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionMeter();
        }
    }),

    WRITE_METADATA_FROM_PACKET(7, OFInstructionWriteMetadataFromPacket.class, new Instantiable<OFInstruction>() {
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionWriteMetadataFromPacket();
        }
    }),

    GOTO_DIRECT_TABLE(8, OFInstructionGotoDirectTable.class, new Instantiable<OFInstruction>() {
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionGotoDirectTable();
        }
    }),

    CONDITIONAL_JMP(9, OFInstructionConditionalJmp.class,new Instantiable<OFInstruction>(){
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionConditionalJmp();
        }
    }),

    CALCULATE_FIELD(10, OFInstructionCalculateField.class,new Instantiable<OFInstruction>(){
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionCalculateField();
        }
    }),

    MOVE_PACKET_POFFSET(11, OFInstructionMovePacketOffset.class,new Instantiable<OFInstruction>(){
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionMovePacketOffset();
        }
    }),

    EXPERIMENTER(0xffff, OFInstructionExperimenter.class, new Instantiable<OFInstruction>() {
        @Override
        public OFInstruction instantiate() {
            return new OFInstructionExperimenter();
        }
    });

    protected static OFInstructionType[] mapping;

    protected Class<? extends OFInstruction> clazz;
    protected Constructor<? extends OFInstruction> constructor;
    protected Instantiable<OFInstruction> instantiable;
    protected short type;

    /**
     * Store some information about the OpenFlow Instruction type, including wire
     * protocol type number, length, and class
     *
     * @param type Wire protocol number associated with this POFInstruction
     * @param clazz The Java class corresponding to this type of OpenFlow Instruction
     * @param instantiable the instantiable for the POFInstruction this type represents
     */
    OFInstructionType(int type, Class<? extends OFInstruction> clazz, Instantiable<OFInstruction> instantiable) {
        this.type = (short) type;
        this.clazz = clazz;
        this.instantiable = instantiable;
        try {
            this.constructor = clazz.getConstructor();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failure getting constructor for class: " + clazz, e);
        }
        OFInstructionType.addMapping(this.type, this);
    }

    /**
     * Adds a mapping from type value to POFInstructionType enum
     *
     * @param i OpenFlow wire protocol Instruction type value
     * @param t type
     */
    static public void addMapping(short i, OFInstructionType t) {
        if (mapping == null)
            mapping = new OFInstructionType[16];
        // bring higher mappings down to the edge of our array
        if (i < 0)
            i = (short) (16 + i);
        OFInstructionType.mapping[i] = t;
    }

    /**
     * Given a wire protocol OpenFlow type number, return the POFInstructionType associated
     * with it
     *
     * @param i wire protocol number
     * @return POFInstructionType enum type
     */

    static public OFInstructionType valueOf(short i) {
        if (i < 0)
            i = (short) (16+i);
        return OFInstructionType.mapping[i];
    }

    /**
     * @return Returns the wire protocol value corresponding to this
     *         POFInstructionType
     */
    public short getTypeValue() {
        return this.type;
    }

    /**
     * @return return the POFInstruction subclass corresponding to this POFInstructionType
     */
    public Class<? extends OFInstruction> toClass() {
        return clazz;
    }

    /**
     * Returns the no-argument Constructor of the implementation class for
     * this POFInstructionType
     * @return the constructor
     */
    public Constructor<? extends OFInstruction> getConstructor() {
        return constructor;
    }

    /**
     * Returns a new instance of the POFInstruction represented by this POFInstructionType
     * @return the new object
     */
    public OFInstruction newInstance() {
        return instantiable.instantiate();
    }

    /**
     * @return the instantiable
     */
    public Instantiable<OFInstruction> getInstantiable() {
        return instantiable;
    }

    /**
     * @param instantiable the instantiable to set
     */
    public void setInstantiable(Instantiable<OFInstruction> instantiable) {
        this.instantiable = instantiable;
    }
}