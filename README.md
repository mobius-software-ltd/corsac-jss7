# Asynchronous Java SS7 Stack and Services with clustering support

Based on Restcomm JSS7

## Introduction

Async jSS7 provides an open source software solution implementing M3UA, SCCP, TCAP, CAMEL, MAP, ISUP protocols for M3UA (SIGTRAN) over IP.
It aims to provide distrubuted jss7 based solution where different protocols may be handled on different nodes , while supporting clustering on TCAP and MAP/CAP level.
As result this version of stack is much more effecient for High load system and/or systems that has different traffic scenarios coming over same STP.

## Current Status

The initial rework has been completed and code is ready to use. The main differences between this repo and restcomm repository (in terms of operation) is in bitstring encoding (this release is using shortest possible bitstring representation while restcomm is not) and errors handling on TCAP+ layers ( due to different parsing of ASN )
The work on those 2 items and several other improvements is planned to be completed shorly ( till the end of Jan 2022 ), after which additional rework of higher layer protocols logic would be done to completely separate TCAP from upper layer protocols , errors handling of higher layers protocols changed and common module would be introduced for cap/inap/map to minimize development effort
