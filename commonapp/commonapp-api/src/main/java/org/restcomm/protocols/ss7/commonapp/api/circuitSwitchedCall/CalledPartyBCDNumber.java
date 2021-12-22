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

package org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
CalledPartyBCDNumber {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(
bound.&minCalledPartyBCDNumberLength ..
bound.&maxCalledPartyBCDNumberLength))
-- Indicates the Called Party Number, including service selection information. Refer to GSM 04.08
-- for encoding. This data type carries only the "type of number", "numbering plan
-- identification" and "number digit" fields defined in GSM 04.08;
-- it does not carry the "called party
-- BCD number IEI" or "length of called party BCD number contents".
</code>
*
* minCalledPartyBCDNumberLength ::= 1 maxCalledPartyBCDNumberLength ::= 41
*
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface CalledPartyBCDNumber extends AddressString {

    AddressNature getAddressNature();

    NumberingPlan getNumberingPlan();

    String getAddress();

    boolean isExtension();

}