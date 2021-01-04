package openflow.protocol.msg;

import openflow.protocol.Instantiable;
import openflow.protocol.msg.table.OFFlowTableResource;
import openflow.protocol.msg.table.OFTableMod;

import java.lang.reflect.Constructor;

/**
 * @author twd
 * @description
 * @date 2019-12-02
 */
public enum OFType {
    /* Immutable messages. */
    HELLO(0, OFHello.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFHello();
        }
    }),

    ERROR(1, OFError.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFError();
        }
    }),

    ECHO_REQUEST(2, OFEchoRequest.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFEchoRequest();
        }
    }),

    ECHO_REPLY(3, OFEchoReply.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFEchoReply();
        }
    }),

    EXPERIMENTER(4, OFExperimenter.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFExperimenter();
        }
    }),//VENDOR has to change into EXPERIMENTR after OpenFlow 1.1

    /* Switch configuration messages. */
    FEATURES_REQUEST(5, OFFeaturesRequest.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFFeaturesRequest();
        }
    }),

    FEATURES_REPLY(6, OFFeaturesReply.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFFeaturesReply();
        }
    }),

    GET_CONFIG_REQUEST(7, OFGetConfigRequest.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFGetConfigRequest();
        }
    }),

    GET_CONFIG_REPLY(8, OFGetConfigReply.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFGetConfigReply();
        }
    }),

    SET_CONFIG(9, OFSetConfig.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFSetConfig();
        }
    }),

    /* Asynchronous messages. */
    PACKET_IN(10, OFPacketIn.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFPacketIn();
        }
    }),

    FLOW_REMOVED(11, OFFlowRemoved.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFFlowRemoved();
        }
    }),

    PORT_STATUS(12, OFPortStatus.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFPortStatus();
        }
    }),

    RESOURCE_REPORT(13, OFFlowTableResource.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFFlowTableResource();
        }
    }),

    /* Controller command messages. */
    PACKET_OUT(14, OFPacketOut.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFPacketOut();
        }
    }),

    FLOW_MOD(15, OFFlowMod.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFFlowMod();
        }
    }),

    GROUP_MOD(16, OFGroupMod.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFGroupMod();
        }
    }),

    PORT_MOD(17, OFPortMod.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFPortMod();
        }
    }),

    TABLE_MOD(18, OFTableMod.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFTableMod();
        }
    }),

    /* Multipart messages. */
    MULTIPART_REQUEST(19, OFMultipartRequest.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFMultipartRequest();
        }
    }),

    MULTIPART_REPLY(20, OFMultipartReply.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFMultipartReply();
        }
    }),

    /* Barrier messages. */
    BARRIER_REQUEST(21, OFBarrierRequest.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFBarrierRequest();
        }
    }),

    BARRIER_REPLY(22, OFBarrierReply.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFBarrierReply();
        }
    }),

    /* Queue Configuration messages. */
    QUEUE_GET_CONFIG_REQUEST(23, OFQueueGetConfigRequest.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFQueueGetConfigRequest();
        }
    }),

    QUEUE_GET_CONFIG_REPLY(24, OFQueueGetConfigReply.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFQueueGetConfigReply();
        }
    }),

    /* Controller role change request messages. */
    ROLE_REQUEST(25, OFRoleRequest.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFRoleRequest();
        }
    }),

    ROLE_REPLY(26, OFRoleReply.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFRoleReply();
        }
    }),

    /* Asynchronous message configuration. */
    GET_ASYNC_REQUEST(27, OFGetAsyncRequest.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFGetAsyncRequest();
        }
    }),

    GET_ASYNC_REPLY(28, OFGetAsyncReply.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFGetAsyncReply();
        }
    }),

    SET_ASYNC(29, OFSetAsync.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFGetAsyncReply();
        }
    }),

    /* Meters and rate limiters configuration messages. */
    METER_MOD(30, OFMeterMod.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFMeterMod();
        }
    }),

    COUNTER_MOD(31, OFCounterMod.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFCounterMod();
        }
    }),

    COUNTER_REQUEST(32, OFCounterRequest.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFCounterRequest();
        }
    }),

    COUNTER_REPLY(33, OFCounterReply.class, new Instantiable<OFMessage>() {
        @Override
        public OFMessage instantiate() {
            return new OFCounterReply();
        }
    });

    static OFType[] mapping;

    protected Class<? extends OFMessage> clazz;
    protected Constructor<? extends OFMessage> constructor;
    protected Instantiable<OFMessage> instantiable;
    protected byte type;

    /**
     * Store some information about the OpenFlow type, including wire protocol
     * type number, length, and derived class
     *
     * @param type Wire protocol number associated with this OFType
     * @param clazz The Java class corresponding to this type of OpenFlow
     *              message
     * @param instantiator An Instantiator<OFMessage> implementation that creates an
     *          instance of the specified OFMessage
     */
    OFType(int type, Class<? extends OFMessage> clazz, Instantiable<OFMessage> instantiator) {
        this.type = (byte) type;
        this.clazz = clazz;
        this.instantiable = instantiator;
        try {
            this.constructor = clazz.getConstructor();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failure getting constructor for class: " + clazz, e);
        }
        OFType.addMapping(this.type, this);
    }

    /**
     * Adds a mapping from type value to OFType enum
     *
     * @param i OpenFlow wire protocol type
     * @param t type
     */
    static public void addMapping(byte i, OFType t) {
        if (mapping == null)
            mapping = new OFType[64];
        OFType.mapping[i] = t;
    }

    /**
     * Remove a mapping from type value to OFType enum
     *
     * @param i OpenFlow wire protocol type
     */
    static public void removeMapping(byte i) {
        OFType.mapping[i] = null;
    }

    /**
     * Given a wire protocol OpenFlow type number, return the OFType associated
     * with it
     *
     * @param i wire protocol number
     * @return OFType enum type
     */
    static public OFType valueOf(Byte i) {
        return OFType.mapping[i];
    }

    /**
     * @return Returns the wire protocol value corresponding to this OFType
     */
    public byte getTypeValue() {
        return this.type;
    }

    /**
     * @return return the OFMessage subclass corresponding to this OFType
     */
    public Class<? extends OFMessage> toClass() {
        return clazz;
    }

    /**
     * Returns the no-argument Constructor of the implementation class for
     * this OFType
     * @return the constructor
     */
    public Constructor<? extends OFMessage> getConstructor() {
        return constructor;
    }

    /**
     * Returns a new instance of the OFMessage represented by this OFType
     * @return the new object
     */
    public OFMessage newInstance() {
        return instantiable.instantiate();
    }

    /**
     * @return the instantiable
     */
    public Instantiable<OFMessage> getInstantiable() {
        return instantiable;
    }

    /**
     * @param instantiable the instantiable to set
     */
    public void setInstantiable(Instantiable<OFMessage> instantiable) {
        this.instantiable = instantiable;
    }
}
