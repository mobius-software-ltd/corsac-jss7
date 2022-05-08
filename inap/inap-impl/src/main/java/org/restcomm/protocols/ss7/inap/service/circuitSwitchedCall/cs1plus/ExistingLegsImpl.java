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

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ExistingLegs;

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
public class ExistingLegsImpl implements ExistingLegs {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1)
    private ReceivingLegIDWrapperImpl legID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNNull linkInd;

    public ExistingLegsImpl() {
    }

    public ExistingLegsImpl(LegType legID,boolean linkInd) {
    	if(legID!=null)
    		this.legID=new ReceivingLegIDWrapperImpl(new ReceivingLegIDImpl(legID));    		
    	
    	if(linkInd)
    		this.linkInd=new ASNNull();    	
    }

    public LegType getLegID() {
    	if(legID==null || legID.getReceivingLegID()==null)
    		return null;
    	
        return legID.getReceivingLegID().getReceivingSideID();
    }

    public boolean getLinkInd() {
    	return linkInd!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ExistingLegs [");

        if (this.legID != null && this.legID.getReceivingLegID()!=null && this.legID.getReceivingLegID().getReceivingSideID()!=null) {
            sb.append(", legID=");
            sb.append(this.legID.getReceivingLegID().getReceivingSideID());
        }

        if (this.linkInd != null)
            sb.append(", linkInd");            
                
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(legID==null)
			throw new ASNParsingComponentException("leg ID should be set for existing legs", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}