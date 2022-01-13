/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface AChBillingChargingCharacteristics {
    byte[] getData();
}