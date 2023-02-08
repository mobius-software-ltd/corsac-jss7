/*
 * Mobius Software LTD
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.restcomm.protocols.ss7.commonapp.api.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
Ext2-QoS-Subscribed ::= OCTET STRING (SIZE (1..3))
-- Octets 1-3 are coded according to 3GPP TS 24.008 [35] Quality of Service Octets 14-16.
-- If Quality of Service information is structured with 14 octet length, then
-- Octet 1 is coded according to 3GPP TS 24.008 [35] Quality of Service Octet 14.
Source Statistics Descriptor, octet 14 (see 3GPP TS 23.107 [81])
Bits
4 3 2 1
In MS to network direction
0 0 0 0     unknown
0 0 0 1     speech
The network shall consider all other values as unknown.
In network to MS direction
Bits 4 to 1 of octet 14 are spare and shall be coded all 0.
The Source Statistics Descriptor value is ignored if the Traffic Class is Interactive class or Background class.
Signalling Indication, octet 14 (see 3GPP TS 23.107 [81])
Bit
5
In MS to network direction and in network to MS direction:
0       Not optimised for signalling traffic
1       Optimised for signalling traffic
If set to '1' the QoS of the PDP context is optimised for signalling
The Signalling Indication value is ignored if the Traffic Class is Conversational class, Streaming class or Background class.
Bits 8 to 6 of octet 14 are spare and shall be coded all 0.
Maximum bit rate for downlink (extended), octet 15
Bits
8 7 6 5 4 3 2 1
In MS to network direction and in network to MS direction:
0 0 0 0 0 0 0 0 Use the value indicated by the Maximum bit rate for downlink in octet 9.
                    For all other values: Ignore the value indicated by the Maximum bit rate for downlink in octet 9
                    and use the following value:
0 0 0 0 0 0 0 1 The maximum bit rate is 8600 kbps + ((the binary coded value in 8 bits) * 100 kbps),
0 1 0 0 1 0 1 0 giving a range of values from 8700 kbps
to 16000 kbps in 100 kbps increments.
0 1 0 0 1 0 1 1 The maximum bit rate is 16 Mbps + ((the binary coded value in 8 bits - 01001010) * 1 Mbps),
1 0 1 1 1 0 1 0 giving a range of values from 17 Mbps to 128 Mbps in 1 Mbps increments.
1 0 1 1 1 0 1 1 The maximum bit rate is 128 Mbps + ((the binary coded value in 8 bits - 10111010) * 2 Mbps),
1 1 1 1 1 0 1 0 giving a range of values from 130 Mbps to 256 Mbps in 2 Mbps increments.
The network shall map all other values not explicitly defined onto one of the values defined in this version of the protocol. The network shall return a negotiated value which is explicitly defined in this version of the protocol.
The MS shall map all other values not explicitly defined onto the maximum value defined in this version of the protocol.
Guaranteed bit rate for downlink (extended), octet 16
Bits
8 7 6 5 4 3 2 1
In MS to network direction and in network to MS direction:
0 0 0 0 0 0 0 0 Use the value indicated by the Guaranteed bit rate for downlink in octet 13.
                    For all other values: Ignore the value indicated by the Guaranteed bit rate for downlink in octet 9
                    and use the following value:
0 0 0 0 0 0 0 1 The guaranteed bit rate is 8600 kbps + ((the binary coded value in 8 bits) * 100 kbps),
0 1 0 0 1 0 1 0 giving a range of values from 8700 kbps to 16000 kbps in 100 kbps increments.
0 1 0 0 1 0 1 1 The guaranteed bit rate is 16 Mbps + ((the binary coded value in 8 bits - 01001010) * 1 Mbps),
1 0 1 1 1 0 1 0 giving a range of values from 17 Mbps to 128 Mbps in 1 Mbps increments.
1 0 1 1 1 0 1 1 The guaranteed bit rate is 128 Mbps + ((the binary coded value in 8 bits - 10111010) * 2 Mbps),
1 1 1 1 1 0 1 0 giving a range of values from 130 Mbps to 256 Mbps in 2 Mbps increments.
</code>
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface Ext2QoSSubscribed {

    Ext2QoSSubscribed_SourceStatisticsDescriptor getSourceStatisticsDescriptor();

    boolean isOptimisedForSignallingTraffic();

    ExtQoSSubscribed_BitRateExtended getMaximumBitRateForDownlinkExtended();

    ExtQoSSubscribed_BitRateExtended getGuaranteedBitRateForDownlinkExtended();

}