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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
AChBillingChargingCharacteristics ::= OCTET STRING (SIZE (minAChBillingChargingLength..maxAChBillingChargingLength))
The AChBillingChargingCharacteristics parameter specifies the charging related information
to be provided by the SSF and the conditions on which this information has to be reported
back to the SCF with the ApplyChargingReport operation.
Examples of charging related information to be provided by the SSF may be: bulk counter
values, costs, tariff change and time of charge, time stamps, durations, etc.
</code>

*
* @author yulian.oifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface AChBillingChargingCharacteristics {    
}