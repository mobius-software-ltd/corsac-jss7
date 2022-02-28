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

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericNumbersSet;

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
public class GenericNumbersSetImpl implements GenericNumbersSet {
	
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,index = -1, defaultImplementation = DigitsIsupImpl.class)
	private List<DigitsIsup> genericNumbers;

    public GenericNumbersSetImpl() {
    }

    public GenericNumbersSetImpl(List<DigitsIsup> genericNumbers) {
    	this.genericNumbers=genericNumbers;
    	if(genericNumbers!=null) {
    		for(DigitsIsup curr:genericNumbers)
    			curr.setIsGenericNumber();
    	}    		
    }

    public List<DigitsIsup> getGenericNumbers() {
    	if(genericNumbers!=null) {
    		for(DigitsIsup curr:genericNumbers)
    			curr.setIsGenericNumber();
    	}    		
    	
    	return genericNumbers;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("GenericDigitsSet [");
        
        List<DigitsIsup> items=getGenericNumbers();
        if (items != null && items.size()!=0) {
            sb.append("genericNumbers=");
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
		if(genericNumbers==null || genericNumbers.size()==0)
			throw new ASNParsingComponentException("generic numbers list should be set for generic numbers", ASNParsingComponentExceptionReason.MistypedParameter);

		if(genericNumbers.size()>20)
			throw new ASNParsingComponentException("generic numbers list size should be betwen 1 and 20 for generic numbers", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}