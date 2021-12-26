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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentException;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle0100;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
globalTitle [02] OCTET STRING (SIZE(4..12)),
‐‐ octet 1 : translation type
‐‐ bit assignment, 2st octet:
‐‐ H G F E D C B A
‐‐ encoding scheme:
‐‐ x x x x 0 0 0 1 BCD, odd number of digits
‐‐ x x x x 0 0 1 0 BCD, even number of digits
‐‐ numbering plan:
‐‐ 0 0 0 1 x x x x ISDN numbering plan (E.164)
‐‐ 0 0 1 1 x x x x data numbering plan (X.121)
‐‐ 0 1 0 0 x x x x telex numbering plan (F.69)
‐‐ 0 1 0 1 x x x x maritime mobile numbering plan
‐‐ 0 1 1 0 x x x x land mobile numbering plan
‐‐ 0 1 1 1 x x x x ISDN mobile numbering plan
‐‐ bit assignment, 3nd octet:
‐‐ nature of address indicator:
‐‐ x 0 0 0 0 0 0 1 subscriber number
‐‐ x 0 0 0 0 0 1 0 unknown
‐‐ x 0 0 0 0 0 1 1 national significant number
‐‐ x 0 0 0 0 1 0 0 international number
‐‐ octets 4..12: address signals, coded '0'H..'9'H,
‐‐ 'B'H (code 11), 'C'H (code 12)
‐‐ filler '0'H is used in case of odd number of digits
</code>

*
* @author yulian.oifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface GlobalTitle {
	GlobalTitle0100 getTitle() throws INAPParsingComponentException;       
}