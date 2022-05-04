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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus;

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

globalTitleAndSubSystemNumber [03] OCTET STRING (SIZE(5..13)),
‐‐ octet 1 : SubSystemNumber
‐‐ octets 2..13: GlobalTitle, coding see above
</code>

*
* @author yulian.oifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface GlobalTitleAndSSN extends GlobalTitle {
    Integer getSSN();
}