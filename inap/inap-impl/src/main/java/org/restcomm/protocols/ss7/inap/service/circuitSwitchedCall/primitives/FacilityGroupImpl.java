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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
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
    			this.trunkGroupID = new ASNInteger(value);    			
    		else
    			this.privateFacilityID = new ASNInteger(value);    			
    	}
    }

    public FacilityGroupImpl(ByteBuf value,Boolean isHuntGroup) {
    	if(value!=null) {
    		if(isHuntGroup)
    			this.huntGroup = new ASNOctetString(value);
    		else
    			this.routeIndex = new ASNOctetString(value);    		
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
}
