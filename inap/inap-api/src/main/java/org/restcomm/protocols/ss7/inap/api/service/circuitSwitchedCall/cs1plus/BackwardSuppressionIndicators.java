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

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
BackwardSuppressionIndicators ::= SET SIZE (1..2) OF OCTET STRING (SIZE (2))
‐‐ bit assignment (for each BackwardSuppressionIndicator element), 1st octet:
‐‐ H G F E D C B A
‐‐ Parameter qualifier:
‐‐ 0 0 0 0 0 0 0 0 additional connected number
‐‐ 0 0 0 0 0 0 0 1 connected subaddress
‐‐ bit assignment, 2nd octet:
‐‐ Instruction indicator:
‐‐ x x x x x x 0 1 suppress
‐‐ x x x x x x 1 0 pass unchanged
</code>

*
* @author yulian.oifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface BackwardSuppressionIndicators {
    BackwardSuppression getBackwardSuppression();
    
    InstructionIndicator getInstructionIndicator();
}