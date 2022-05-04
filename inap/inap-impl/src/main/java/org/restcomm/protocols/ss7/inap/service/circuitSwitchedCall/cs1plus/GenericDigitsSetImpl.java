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

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericDigitsSet;

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
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GenericDigitsSetImpl implements GenericDigitsSet {
	
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,index = -1, defaultImplementation = DigitsIsupImpl.class)
	private List<DigitsIsup> genericDigits;

    public GenericDigitsSetImpl() {
    }

    public GenericDigitsSetImpl(List<DigitsIsup> genericDigits) {
    	this.genericDigits=genericDigits;
    	if(genericDigits!=null) {
    		for(DigitsIsup curr:genericDigits)
    			curr.setIsGenericDigits();
    	}    		
    }

    public List<DigitsIsup> getGenericDigits() {
    	if(genericDigits!=null) {
    		for(DigitsIsup curr:genericDigits)
    			curr.setIsGenericDigits();
    	}    		
    	
    	return genericDigits;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("GenericDigitsSet [");
        
        List<DigitsIsup> items=getGenericDigits();
        if (items != null && items.size()!=0) {
            sb.append("genericDigits=");
            boolean isFirst=false;
            for(DigitsIsup curr:items) {
            	if(!isFirst)
            		sb.append(",");
            	
            	sb.append(curr);
            	isFirst=false;
            }         
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(genericDigits==null || genericDigits.size()==0)
			throw new ASNParsingComponentException("generic digits list should be set for generic digits", ASNParsingComponentExceptionReason.MistypedParameter);

		if(genericDigits.size()>20)
			throw new ASNParsingComponentException("generic digits list size should be betwen 1 and 20 for generic digits", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}