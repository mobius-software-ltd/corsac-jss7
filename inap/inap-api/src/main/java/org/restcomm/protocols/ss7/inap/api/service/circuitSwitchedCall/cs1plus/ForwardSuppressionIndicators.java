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
ForwardSuppressionIndicators ::= SET SIZE (1..10) OF OCTET STRING (SIZE (2))
‐‐ bit assignment (for each forward suppression indicator element), 1st octet:
‐‐ H G F E D C B A
‐‐ parameter qualifier:
‐‐ 0 0 0 0 0 0 0 0 additional called number
‐‐ 0 0 0 0 0 0 0 1 additional calling party number
‐‐ 0 0 0 0 0 0 1 0 additional original called number
‐‐ 0 0 0 0 0 0 1 1 additional redirecting number
‐‐ 0 0 0 0 0 1 0 0 additional original called IN number
‐‐ 0 0 0 0 0 1 0 1 private network travelling class mark
‐‐ 0 0 0 0 0 1 1 0 business communication group ID
‐‐ 0 0 0 0 0 1 1 1 calling party subaddress
‐‐ 0 0 0 0 1 0 0 0 called party subaddress
‐‐ 0 0 0 0 1 0 1 0 location number
‐‐ bit assignment, 2nd octet:
‐‐ instruction indicator:
‐‐ x x x x x x 0 1 suppress
‐‐ x x x x x x 1 0 pass unchanged
</code>

*
* @author yulian.oifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface ForwardSuppressionIndicators {
    ForwardSuppression getForwardSuppression();
    
    InstructionIndicator getInstructionIndicator();
}