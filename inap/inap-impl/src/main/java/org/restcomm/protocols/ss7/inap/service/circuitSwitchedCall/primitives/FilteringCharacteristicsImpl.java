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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCharacteristics;

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
public class FilteringCharacteristicsImpl implements FilteringCharacteristics {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNInteger interval;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNInteger numberOfCalls;

    public FilteringCharacteristicsImpl() {
    }

    public FilteringCharacteristicsImpl(Integer value,Boolean isInterval) {
    	if(value!=null) {
    		if(isInterval)
    			this.interval = new ASNInteger(value,"Interval",-1,32000,false);    			
    		else
    			this.numberOfCalls = new ASNInteger(value,"NumberOfCalls",0,255,false);    			
    	}
    }

    public Integer getInterval() {
    	if(interval==null)
    		return null;
    	
        return interval.getIntValue();
    }

    public Integer getNumberOfCalls() {
    	if(numberOfCalls==null)
    		return null;
    	
        return numberOfCalls.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FilteringCharacteristics [");

        if (this.interval != null && this.interval.getValue()!=null) {
            sb.append(", interval=");
            sb.append(interval.getValue());
        }
        
        if (this.numberOfCalls != null && this.numberOfCalls.getValue()!=null) {
            sb.append(", numberOfCalls=");
            sb.append(numberOfCalls.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(interval==null && numberOfCalls==null)
			throw new ASNParsingComponentException("either interval or number of calls should be set for filtereding characteristics", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
