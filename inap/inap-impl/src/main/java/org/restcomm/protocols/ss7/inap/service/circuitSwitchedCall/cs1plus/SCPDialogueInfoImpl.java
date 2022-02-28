/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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