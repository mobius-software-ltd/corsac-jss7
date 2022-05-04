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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DialogueUserInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ProtocolIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCPDialogueInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class SCPDialogueInfoImpl implements SCPDialogueInfo {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1, defaultImplementation = ProtocolIndicatorImpl.class)
    private ProtocolIndicator protocolIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1, defaultImplementation = DialogueUserInformationImpl.class)
    private DialogueUserInformation dialogueUserInformation;

    public SCPDialogueInfoImpl() {
    }

    public SCPDialogueInfoImpl(ProtocolIndicator protocolIndicator,DialogueUserInformation dialogueUserInformation) {
    	this.protocolIndicator=protocolIndicator;
    	this.dialogueUserInformation=dialogueUserInformation;
    }

    public ProtocolIndicator getProtocolIndicator() {
    	return protocolIndicator;
    }

    public DialogueUserInformation getDialogueUserInformation() {
    	return dialogueUserInformation;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SCPDialogueInfo [");

        if (this.protocolIndicator != null) {
            sb.append(", protocolIndicator=");
            sb.append(this.protocolIndicator);
        }

        if (this.dialogueUserInformation != null) {
            sb.append(", dialogueUserInformation=");
            sb.append(this.dialogueUserInformation);
        }
                
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(protocolIndicator==null)
			throw new ASNParsingComponentException("protocol indicator should be set for scp dialogue info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}