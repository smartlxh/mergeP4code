/*
Copyright 2013-present Barefoot Networks, Inc. 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

// This is P4 sample source for basic_switching
#include "includes/intrinsic_metadata.p4"
#include "includes/constants.p4"

//--------------------------------header-------------------------------------
header_type ethernet_t {
    fields {
        dstAddr : 48;
        srcAddr : 48;
        etherType : 16;
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



header ethernet_t ethernet;

header sdnp_t sdnp;
header ipv4_t ipv4;

//-----------------------------parser---------------------------------------
parser start {
    return parse_ethernet;
}

#define ETHERTYPE_VLAN 0x8100
#define ETHERTYPE_IPV4 0x0800
#define ETHERTYPE_SDNP 0x5555

parser parse_ethernet {
    extract(ethernet);
    return select(latest.etherType) {
        // ETHERTYPE_VLAN : parse_vlan;
        // ETHERTYPE_IPV4 : parse_ipv4;
        ETHERTYPE_SDNP : parse_sdnp;
        default: ingress;
    }
}

parser parse_sdnp {
    extract(sdnp);
    return parse_ipv4;
}

parser parse_ipv4 {
    extract(ipv4);
    return ingress;
}


//-----------------------------pipeline-------------------------------------

action sdnp_forward(egress_spec) {
    modify_field(ig_intr_md_for_tm.ucast_egress_port, egress_spec);
    //standard_metadata.egress_spec = egress_spec;
    //ipv4.ttl = ipv4.ttl - 1;
}

action nop() {
}

action drop_pkt() {
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
        sdnp_forward; nop; drop_pkt;
    }
    
}


control ingress {
    apply(sdnp);
}



