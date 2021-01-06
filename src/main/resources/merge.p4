#include "includes/intrinsic_metadata.p4"
#include "includes/constants.p4"
header_type sdnp_t {
fields { 
dpid_src : 32;
port_src : 16;
port_dst : 16;
dpid_dst : 32;
}
}
header_type ethernet_t {
fields { 
dstAddr : 48;
srcAddr : 48;
etherType : 16;
}
}
header_type tag_t {
    fields {
        outport : 48;
        etherType : 16;
    }
}
header sdnp_t sdnp;
header ethernet_t ethernet;
header tag_t tag;
parser start {
    return parse_ethernet;
}
parser parse_ethernet {
    extract(ethernet);
    return select(latest.etherType) {
0x5555 : parse_sdnp;
0x1111 : parse_tag;
        default: ingress;
    }
}
parser parse_tag {
    extract(tag);
    return ingress;
}
parser parse_sdnp {
extract(sdnp);
return ingress;
}


action sdnp_forward(egress_spec) {
    modify_field(ig_intr_md_for_tm.ucast_egress_port, egress_spec);
    //standard_metadata.egress_spec = egress_spec;
    //ipv4.ttl = ipv4.ttl - 1;
}

action nop_sdnp() {
}

action drop_pkt_sdnp() {
    drop();
}



table sdnp {
    reads {
        sdnp.dpid_src : ternary;
        sdnp.port_src : ternary;
	sdnp.dpid_dst : ternary;
        sdnp.port_dst : ternary;
    }
    actions {
        sdnp_forward; nop_sdnp; drop_pkt_sdnp;
    }
    
}









































































action tag_forward(egress_spec) {
    
    modify_field(ethernet.etherType,tag.etherType);
    modify_field(ig_intr_md_for_tm.ucast_egress_port, egress_spec);
    remove_header(tag);
}
table tagIdentify {
    reads {
        tag.outport : ternary;
    }
    actions {
        tag_forward;
    }
    
}
control ingress { 
if(ethernet.etherType == 0x1111){
apply(tagIdentify);
}
if(ethernet.etherType == 0x5555){
apply(sdnp);
}
}
