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
        /* Assume there are no option fields in TCP */
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

header ethernet_t ethernet;
header ipv4_t ipv4;
header tcp_t tcp;
header udp_t udp;

//-----------------------------parser---------------------------------------
parser start {
    return parse_ethernet;
}

#define ETHERTYPE_VLAN 0x8100
#define ETHERTYPE_IPV4 0x0800

parser parse_ethernet {
    extract(ethernet);
    return select(latest.etherType) {
        // ETHERTYPE_VLAN : parse_vlan;
        ETHERTYPE_IPV4 : parse_ipv4;
        default: ingress;
    }
}

parser parse_ipv4 {
    extract(ipv4);
    return select(latest.protocol) {
        0x06: parse_tcp;
        0x11: parse_udp;
        default: ingress;
    }
}

parser parse_tcp{
    extract(tcp);
    return ingress;
}

parser parse_udp{
    extract(udp);
    return ingress;
}

//-----------------------------pipeline-------------------------------------

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



control ingress {
    apply(ipv4);
}



