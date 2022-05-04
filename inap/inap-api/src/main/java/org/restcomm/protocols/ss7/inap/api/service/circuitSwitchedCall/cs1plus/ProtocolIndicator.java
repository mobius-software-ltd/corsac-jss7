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
protocolIndicator [01] OCTET STRING (SIZE(2)),
‐‐ bit assignment, 1st octet:
‐‐ H G F E D C B A
‐‐ Protocol Identifier:
‐‐ 0 0 0 0 0 0 0 0 ETSI CORE INAP CS1
‐‐ 0 0 0 0 0 0 0 1 Ericsson INAP CS1+
‐‐ 0 0 0 0 0 0 1 0
‐‐ : Reserved for future use
‐‐ 0 1 1 1 1 1 1 1
‐‐ 1 0 0 0 0 0 0 0
‐‐ : Reserved for market specific protocol subsets
‐‐ 1 1 1 1 1 1 1 1
‐‐ bit assignment, 2st octet:
‐‐ H G F E D C B A
‐‐ TCAP Dialogue Level:
‐‐ 0 0 0 0 0 0 0 0 Network Default
‐‐ 0 0 0 0 0 0 0 1 Blue TCAP Blue SCCP
‐‐ 0 0 0 0 0 0 1 0 White TCAP Blue SCCP
‐‐ 0 0 0 0 0 0 1 1 White TCAP White SCCP
‐‐ 0 0 0 0 0 1 0 0
‐‐ : Reserved for future use
‐‐ 1 1 1 1 1 1 1 1
</code>

*
* @author yulian.oifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface ProtocolIndicator {
    ProtocolIdentifier getProtocolIdentifier();
    
    TCAPDialogueLevel getTCAPDialogueLevel();
}