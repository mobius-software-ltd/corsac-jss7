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
