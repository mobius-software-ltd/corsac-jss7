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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CounterAndValue;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CounterAndValueImpl implements CounterAndValue {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNInteger counterID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNInteger counterValue;

    public CounterAndValueImpl() {
    }

    public CounterAndValueImpl(Integer counterID,Integer counterValue) {
    	if(counterID!=null)
    		this.counterID=new ASNInteger(counterID,"CounterID",0,99,false);
    		
    	if(counterValue!=null)
    		this.counterValue=new ASNInteger(counterValue,"CounterValue",0,255,false);    		
    }

    public Integer getCounterID() {
    	if(counterID==null)
    		return null;
    	
    	return counterID.getIntValue();
    }

    public Integer getCounterValue() {
    	if(counterValue==null)
    		return null;
    	
    	return counterValue.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CounterAndValue [");

        if (this.counterID != null && this.counterID.getValue()!=null) {
            sb.append(", counterID=");
            sb.append(this.counterID);
        }

        if (this.counterValue != null || this.counterValue.getValue()!=null) {
            sb.append(", counterValue=");
            sb.append(this.counterValue);
        }
                
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(counterID==null)
			throw new ASNParsingComponentException("counter ID should be set for counter and value", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(counterValue==null)
			throw new ASNParsingComponentException("counter value should be set for counter and value", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}