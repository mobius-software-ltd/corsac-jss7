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
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class FacilityGroupWrapperImpl {
	
	@ASNChoise
    private FacilityGroupImpl facilityGroup;

    public FacilityGroupWrapperImpl() {
    }

    public FacilityGroupWrapperImpl(FacilityGroup facilityGroup) {
    	if(facilityGroup instanceof FacilityGroupImpl)
    		this.facilityGroup=(FacilityGroupImpl)facilityGroup;
    	else if(facilityGroup!=null) {
    		if(facilityGroup.getHuntGroup()!=null)
    			this.facilityGroup = new FacilityGroupImpl(facilityGroup.getHuntGroup(),true);
    		else if(facilityGroup.getRouteIndex()!=null)
    			this.facilityGroup = new FacilityGroupImpl(facilityGroup.getRouteIndex(),false);
    		else if(facilityGroup.getTrunkGroupID()!=null)
    			this.facilityGroup = new FacilityGroupImpl(facilityGroup.getTrunkGroupID(),true);
    		else if(facilityGroup.getPrivateFacilityID()!=null)
    			this.facilityGroup = new FacilityGroupImpl(facilityGroup.getPrivateFacilityID(),false);
    	}
    }

    public FacilityGroup getFacilityGroup() {
    	return facilityGroup;
    }
}