

# Asynchronous Java SS7 Stack and Services with clustering support

Based on Restcomm JSS7

## Introduction

Async jSS7 provides an open source software solution implementing M3UA, SCCP, TCAP, CAMEL, MAP, ISUP protocols for M3UA (SIGTRAN) over IP.
It aims to provide distrubuted jss7 based solution where different protocols may be handled on different nodes , while supporting clustering on TCAP and MAP/CAP level.
As result this version of stack is much more effecient for High load system and/or systems that has different traffic scenarios coming over same STP.

## Current Status

The work is still in progress. As for 12th of March the work is done on Map And Cap Protocols
Please do not use this code for now since the structure would be changed slightly to bring back the API interfaces for parameters encoding.
