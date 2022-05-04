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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class FacilityGroupImpl implements FacilityGroup {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNInteger trunkGroupID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNInteger privateFacilityID;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNOctetString huntGroup;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNOctetString routeIndex;

    public FacilityGroupImpl() {
    }

    public FacilityGroupImpl(Integer value,Boolean isTrunkGroupID) {
    	if(value!=null) {
    		if(isTrunkGroupID)
    			this.trunkGroupID = new ASNInteger(value,"TrunkGroupID",Integer.MIN_VALUE,Integer.MAX_VALUE,false);    			
    		else
    			this.privateFacilityID = new ASNInteger(value,"PrivateFacilityID",Integer.MIN_VALUE,Integer.MAX_VALUE,false);
    	}
    }

    public FacilityGroupImpl(ByteBuf value,Boolean isHuntGroup) {
    	if(value!=null) {
    		if(isHuntGroup)
    			this.huntGroup = new ASNOctetString(value,"HuntGroup",null,null,false);
    		else
    			this.routeIndex = new ASNOctetString(value,"RouteIndex",null,null,false);    		
    	}
    }

    public Integer getTrunkGroupID() {
    	if(trunkGroupID==null)
    		return null;
    	
        return trunkGroupID.getIntValue();
    }

    public Integer getPrivateFacilityID() {
    	if(privateFacilityID==null)
    		return null;
    	
        return privateFacilityID.getIntValue();
    }

    public ByteBuf getHuntGroup() {
    	if(huntGroup==null)
    		return null;
    	
    	return huntGroup.getValue();
    }

    public ByteBuf getRouteIndex() {
    	if(routeIndex==null)
    		return null;
    	
    	return routeIndex.getValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FacilityGroup [");

        if (this.trunkGroupID != null && this.trunkGroupID.getValue()!=null) {
            sb.append(", trunkGroupID=");
            sb.append(trunkGroupID.getValue());
        }
        
        if (this.privateFacilityID != null && this.privateFacilityID.getValue()!=null) {
            sb.append(", privateFacilityID=");
            sb.append(privateFacilityID.getValue());
        }
        
        if (this.huntGroup != null && this.huntGroup.getValue()!=null) {
            sb.append(", huntGroup=");
            sb.append(huntGroup.printDataArr());
        }
        
        if (this.routeIndex != null && this.routeIndex.getValue()!=null) {
            sb.append(", routeIndex=");
            sb.append(routeIndex.printDataArr());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(trunkGroupID==null && privateFacilityID==null && huntGroup==null && routeIndex==null)
			throw new ASNParsingComponentException("one of child items should be set for facility group", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
