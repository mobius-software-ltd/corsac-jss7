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

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceAddress;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ResourceAddressImpl implements ResourceAddress {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup ipRoutingAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1)
    private ReceivingLegIDWrapperImpl legID;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNNull none;

    public ResourceAddressImpl() {
    }

    public ResourceAddressImpl(CalledPartyNumberIsup ipRoutingAddress) {
    	this.ipRoutingAddress=ipRoutingAddress;
    }
    
    public ResourceAddressImpl(LegType legID) {
    	if(legID!=null)
    		this.legID=new ReceivingLegIDWrapperImpl(new ReceivingLegIDImpl(legID));
    }
    
    public ResourceAddressImpl(boolean none) {
    	if(none)
    		this.none=new ASNNull();
    }

    public CalledPartyNumberIsup getIPRoutingAddress() {
    	return ipRoutingAddress;
    }

    public LegType getLegID() {
    	if(legID==null || legID.getReceivingLegID()==null)
    		return null;
    	
    	return legID.getReceivingLegID().getReceivingSideID();
    }

    public boolean getNone() {
    	return none!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ResourceAddress [");

        if (this.ipRoutingAddress != null) {
            sb.append(", ipRoutingAddress=");
            sb.append(ipRoutingAddress);
        }
        
        if (this.legID != null && this.legID.getReceivingLegID()!=null && this.legID.getReceivingLegID().getReceivingSideID()!=null) {
            sb.append(", legID=");
            sb.append(this.legID.getReceivingLegID().getReceivingSideID());
        }

        if (this.getNone()) {
            sb.append(", none");            
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(ipRoutingAddress==null && legID==null && none==null)
			throw new ASNParsingComponentException("one if child items should be set for resource address", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
