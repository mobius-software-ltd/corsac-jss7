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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.PromptAndCollectUserInformationResponeChoiseImpl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class PromptAndCollectUserInformationResponseImpl extends CircuitSwitchedCallMessageImpl implements
        PromptAndCollectUserInformationResponse {
	private static final long serialVersionUID = 1L;

	@ASNChoise
    private PromptAndCollectUserInformationResponeChoiseImpl promptAndCollectUserInformationResponeChoiseImpl;


    public PromptAndCollectUserInformationResponseImpl() {
    }

    public PromptAndCollectUserInformationResponseImpl(DigitsIsup digitsResponse) {
    	if(digitsResponse!=null)
    		this.promptAndCollectUserInformationResponeChoiseImpl = new PromptAndCollectUserInformationResponeChoiseImpl(digitsResponse);    	
    }

    public PromptAndCollectUserInformationResponseImpl(String ia5Response) {
    	if(ia5Response!=null) {
    		this.promptAndCollectUserInformationResponeChoiseImpl=new PromptAndCollectUserInformationResponeChoiseImpl(ia5Response);
    	}
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.promptAndCollectUserInformation_Response;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.promptAndCollectUserInformation;
    }

    @Override
    public DigitsIsup getDigitsResponse() {
    	if(promptAndCollectUserInformationResponeChoiseImpl==null)
    		return null;
    	
    	return promptAndCollectUserInformationResponeChoiseImpl.getDigitsResponse();
    }

    @Override
    public String getIA5Response() {
    	if(promptAndCollectUserInformationResponeChoiseImpl==null)
    		return null;
    	
    	return promptAndCollectUserInformationResponeChoiseImpl.getIA5Response();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.promptAndCollectUserInformationResponeChoiseImpl != null)
        	sb.append(this.promptAndCollectUserInformationResponeChoiseImpl.toString());
        
        this.addInvokeIdInfo(sb);        
        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(promptAndCollectUserInformationResponeChoiseImpl==null)
			throw new ASNParsingComponentException("one of child elements should be set for prompt and collect response", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
