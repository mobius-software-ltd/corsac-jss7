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
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.MidCallInfoType;

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
public class MidCallInfoTypeImpl implements MidCallInfoType {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1,defaultImplementation =  DigitsIsupImpl.class)
    private DigitsIsup inServiceControlCodeLow;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1,defaultImplementation =  DigitsIsupImpl.class)
    private DigitsIsup inServiceControlCodeHigh;

    public MidCallInfoTypeImpl() {
    }

    public MidCallInfoTypeImpl(DigitsIsup inServiceControlCodeLow,DigitsIsup inServiceControlCodeHigh) {
    	this.inServiceControlCodeLow=inServiceControlCodeLow;
    	this.inServiceControlCodeHigh=inServiceControlCodeHigh;
    }

    public DigitsIsup getINServiceControlCodeLow() {
    	if(inServiceControlCodeLow!=null)
    		inServiceControlCodeLow.setIsGenericDigits();
    	
    	return inServiceControlCodeLow;
    }

    public DigitsIsup getINServiceControlCodeHigh() {
    	if(inServiceControlCodeLow!=null)
    		inServiceControlCodeLow.setIsGenericDigits();
    	
    	return inServiceControlCodeHigh;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MidCallInfoType [");

        if (this.inServiceControlCodeLow != null) {
            sb.append(", inServiceControlCodeLow=");
            sb.append(this.inServiceControlCodeLow);
        }

        if (this.inServiceControlCodeHigh != null) {
            sb.append(", inServiceControlCodeHigh=");
            sb.append(this.inServiceControlCodeHigh);
        }
                
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(inServiceControlCodeLow==null)
			throw new ASNParsingComponentException("in service control code low should be set for mid call info type", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}