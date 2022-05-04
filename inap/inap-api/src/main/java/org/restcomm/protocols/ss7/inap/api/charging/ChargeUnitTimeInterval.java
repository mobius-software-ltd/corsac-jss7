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

package org.restcomm.protocols.ss7.inap.api.charging;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
ChargeUnitTimeInterval ::= OCTET STRING (SIZE(2))
-- The ChargeUnitTimeInterval is binary coded and has the value range from 0 to 35997. It begins with 200 milliseconds and
-- then in steps of 50 milliseconds.
-- the LSBit is the least significant bit of the first octet
-- the MSBit is the most significant bit of the last octet
-- the coding of the ChargeUnitTimeInterval is the following:
-- 0 : no periodic metering
-- 1 : 200 msec
-- 2 : 250 msec
-- ..
-- 35997 : 30 minutes
-- All other values are spare.
</code>
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface ChargeUnitTimeInterval {

    Integer getData();

}
