#include "includes/intrinsic_metadata.p4"
#include "includes/constants.p4"
header_type ipv4_t {
fields { 
version : 4;
ihl : 4;
diffserv : 8;
totalLen : 16;
identification : 16;
flags : 3;
fragOffset : 13;
ttl : 8;
protocol : 8;
hdrChecksum : 16;
srcAddr : 32;
dstAddr : 32;
}
}
header_type tcp_t {
fields { 
srcPort : 16;
dstPort : 16;
seqNum : 32;
ackNum : 32;
off : 4;
flags : 12;
window : 16;
checkSum : 16;
urgentPoint : 16;
}
}
header_type udp_t {
fields { 
srcPort : 16;
dstPort : 16;
length : 16;
checkSum : 16;
}
}
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
header ipv4_t ipv4;
header tcp_t tcp;
header udp_t udp;
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
0x0800 : parse_ipv4;
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
parser parse_ipv4 {
extract(ipv4);
return select(latest.protocol) {
0x11 : parse_udp;
0x06 : parse_tcp;
default: ingress;
}}
parser parse_udp {
extract(udp);
return ingress;
}
parser parse_tcp {
extract(tcp);
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











































































action ipv4_forward(egress_spec) {
    modify_field(ig_intr_md_for_tm.ucast_egress_port, egress_spec);
    //standard_metadata.egress_spec = egress_spec;
    //ipv4.ttl = ipv4.ttl - 1;
}

action nop() {
}

action drop_pkt() {
    drop();
}

action packet_in(){
     modify_field(ig_intr_md_for_tm.ucast_egress_port, 148);
     //standard_metadata.egress_spec = 144;//此处代码，在pof编译器代码翻译POF指令的时候会有bug
}

table ipv4 {
    reads {
        ipv4.srcAddr : ternary;
        ipv4.dstAddr : ternary;
    }
    actions {
        ipv4_forward; nop; drop_pkt; packet_in;
    }
    default_action : packet_in;
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
if(ethernet.etherType == 0x0800){
apply(ipv4);
}
}
