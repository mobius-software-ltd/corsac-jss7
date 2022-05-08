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
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.AddressAndService;

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
public class AddressAndServiceImpl implements AddressAndService {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup calledAddressValue;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNInteger serviceKey;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup callingAddressValue;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1,defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup locationNumber;
    
    public AddressAndServiceImpl() {
    }

    public AddressAndServiceImpl(DigitsIsup calledAddressValue,Integer serviceKey,DigitsIsup callingAddressValue,
    		LocationNumberIsup locationNumber) {
    	
    	this.calledAddressValue=calledAddressValue;
    	
    	if(serviceKey!=null)
    		this.serviceKey=new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);
    		
    	this.callingAddressValue=callingAddressValue;
    	this.locationNumber=locationNumber;
    }

    public DigitsIsup getCalledAddressValue() {
    	if(calledAddressValue!=null)
    		calledAddressValue.setIsGenericDigits();
    	
    	return calledAddressValue;
    }

    public Integer getServiceKey() {
    	if(serviceKey==null)
    		return null;
    	
    	return serviceKey.getIntValue();
    }

    public DigitsIsup getCallingAddressValue() {
    	if(callingAddressValue!=null)
    		callingAddressValue.setIsGenericDigits();
    	
    	return callingAddressValue;
    }

    public LocationNumberIsup getLocationNumber() {
    	return locationNumber;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AddressAndService [");

        if (this.calledAddressValue != null) {
            sb.append(", calledAddressValue=");
            sb.append(this.calledAddressValue);
        }
                
        if (this.serviceKey != null && this.serviceKey.getValue()!=null) {
            sb.append(", serviceKey=");
            sb.append(this.serviceKey);
        }

        if (this.callingAddressValue != null) {
            sb.append(", callingAddressValue=");
            sb.append(this.callingAddressValue);
        }

        if (this.locationNumber != null) {
            sb.append(", locationNumber=");
            sb.append(this.locationNumber);
        }
                
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(calledAddressValue==null)
			throw new ASNParsingComponentException("called address value should be set for address and service", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}