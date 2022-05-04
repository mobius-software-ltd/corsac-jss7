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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.AddressAndService;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCriteria;

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
public class FilteringCriteriaImpl implements FilteringCriteria {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1)
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = AddressAndServiceImpl.class)
    private AddressAndService addressAndService;

    public FilteringCriteriaImpl() {
    }

    public FilteringCriteriaImpl(Integer serviceKey) {
    	if(serviceKey!=null)
    		this.serviceKey=new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);    		
    }
    
    public FilteringCriteriaImpl(AddressAndService addressAndService) {
    	this.addressAndService=addressAndService;
    }

    public Integer getServiceKey() {
    	if(serviceKey==null)
    		return null;
    	
    	return serviceKey.getIntValue();
    }

    public AddressAndService getAddressAndService() {
    	return addressAndService;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FilteringCriteria [");

        if (this.serviceKey != null && this.serviceKey.getValue()!=null) {
            sb.append(", serviceKey=");
            sb.append(serviceKey);
        }

        if (this.addressAndService != null) {
            sb.append(", addressAndService=");
            sb.append(addressAndService);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(serviceKey==null)
			throw new ASNParsingComponentException("service key should be set for filtering criteria", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(addressAndService==null)
			throw new ASNParsingComponentException("address and service should be set for filtering criteria", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}