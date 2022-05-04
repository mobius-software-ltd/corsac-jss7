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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNIA5String;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,lengthIndefinite = false)
public class PromptAndCollectUserInformationResponeChoiseImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 0,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
    public DigitsIsup digitsResponse;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 1,constructed = false,index = -1)
    public ASNIA5String ia5Response;
    
    public PromptAndCollectUserInformationResponeChoiseImpl() {
    }

    public PromptAndCollectUserInformationResponeChoiseImpl(DigitsIsup digitsResponse) {
    	this.digitsResponse=digitsResponse;
    }

    public PromptAndCollectUserInformationResponeChoiseImpl(String ia5Response) {
    	if(ia5Response!=null)
    		this.ia5Response=new ASNIA5String(ia5Response,"IA5Response",null,null,false);    		
    }

    public DigitsIsup getDigitsResponse() {
    	if(digitsResponse!=null)
    		digitsResponse.setIsGenericDigits();
    	
        return digitsResponse;
    }

    public String getIA5Response() {
    	if(ia5Response==null)
    		return null;
    	
    	return ia5Response.getValue();
    }
    
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("PromptAndCollectUserInformationResponseIndication [");
        
        if (this.digitsResponse != null) {
            sb.append(", digitsResponse=");
            sb.append(digitsResponse.toString());
        }

        if (this.ia5Response != null) {
            sb.append(", ia5Response=");
            sb.append(ia5Response.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(digitsResponse==null && ia5Response==null)
			throw new ASNParsingComponentException("either digits or ia5 should be set for prompt and collect user information response", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
