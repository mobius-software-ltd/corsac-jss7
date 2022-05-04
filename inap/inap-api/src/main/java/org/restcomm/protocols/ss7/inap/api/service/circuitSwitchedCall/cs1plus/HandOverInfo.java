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

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;

/**
 *
<code>
HandOverInfo ::= SEQUENCE {
	handOverCounter [00] INTEGER (1..127),
	sendingSCPAddress [01] CHOICE,
	sendingSCPDialogueInfo [02] SEQUENCE,
	sendingSCPCorrelationInfo [03] OCTET STRING (SIZE (16)) OPTIONAL,
	‐‐ coding is implementation dependant
	receivingSCPAddress [04] CHOICE,
	receivingSCPDialogueInfo [05] SEQUENCE,
	receivingSCPCorrelationInfo [06] OCTET STRING (SIZE (16)) OPTIONAL,
	‐‐ coding is implementation dependant
	handOverNumber [07] Number OPTIONAL,
	handOverData [08] INTEGER (0..65535) OPTIONAL
‐‐ ...
}	
</code>
 *
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface HandOverInfo {

	Integer getHandoverCounter();

    SCPAddress getSendingSCPAddress();

    SCPDialogueInfo getSendingSCPDialogueInfo();

    ByteBuf getSendingSCPCorrelationInfo(); 
    
    SCPAddress getReceivingSCPAddress();

    SCPDialogueInfo getReceivingSCPDialogueInfo();

    ByteBuf getReceivingSCPCorrelationInfo(); 
    
    CalledPartyNumberIsup getHandoverNumber();
    
    Integer getHandoverData();    
}