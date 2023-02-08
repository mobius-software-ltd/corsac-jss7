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

package org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;

/**
 *
<code>
LowLayerCompatibility {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE ( bound.&minLowLayerCompatibilityLength .. bound.&maxLowLayerCompatibilityLength))
-- indicates the LowLayerCompatibility for the calling party.
-- Refer to 3GPP TS 24.008 [9] for encoding.
-- It shall be coded as in the value part defined in 3GPP TS 24.008.
-- i.e. the 3GPP TS 24.008 IEI and 3GPP TS 24.008 octet length indicator
-- shall not be included.
minLowLayerCompatibilityLength ::= 1
maxLowLayerCompatibilityLength ::= 16
The purpose of the low layer compatibility information element is to provide a means which should be used for
compatibility checking by an addressed entity (e.g., a remote user or an interworking unit or a high layer function
network node addressed by the calling user). The low layer compatibility information element is transferred
transparently by a PLMN between the call originating entity (e.g. the calling user) and the addressed entity.
Except for the information element identifier, the low layer compatibility information element is coded as in ITU
recommendation Q.931.
For backward compatibility reasons coding of the modem type field according to ETS 300 102-1 (12-90) shall also be
supported.
The low layer compatibility is a type 4 information element with a minimum length of 2 octets and a maximum length
of 18 octets.
The following octets are coded
as described in
ITU Recommendation Q.931
(Coding of the modem type according to both Q.931 and
ETS 300 102-1 (12-90) shall be accepted)
</code>
 *
 * }
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface LowLayerCompatibility {
    ByteBuf getValue();
}