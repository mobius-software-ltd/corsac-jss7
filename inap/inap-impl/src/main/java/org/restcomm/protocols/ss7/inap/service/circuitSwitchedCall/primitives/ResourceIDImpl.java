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
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;

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
public class ResourceIDImpl implements ResourceID {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1, defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup lineID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1)
    private FacilityGroupWrapperImpl facilityGroup;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNInteger facilityGroupMemberID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNInteger trunkGroupID;

    public ResourceIDImpl() {
    }

    public ResourceIDImpl(Integer value,Boolean isTrunkGroupID) {
    	if(value!=null) {
    		if(isTrunkGroupID)
    			this.trunkGroupID = new ASNInteger(value,"TrunkGroupID",Integer.MIN_VALUE,Integer.MAX_VALUE,false);    			
    		else
    			this.facilityGroupMemberID = new ASNInteger(value,"FacilityGroupMemberID",Integer.MIN_VALUE,Integer.MAX_VALUE,false);
    	}
    }

    public ResourceIDImpl(FacilityGroup facilityGroup) {
    	if(facilityGroup!=null) {
    		this.facilityGroup=new FacilityGroupWrapperImpl(facilityGroup);
    	}
    }

    public ResourceIDImpl(DigitsIsup lineID) {
    	this.lineID=lineID;
    }

    public DigitsIsup getLineID() {
    	if(lineID!=null)
    		lineID.setIsGenericDigits();
    	
    	return lineID;
    }

    public FacilityGroup getFacilityGroup() {
    	if(facilityGroup==null)
    		return null;
    	
    	return facilityGroup.getFacilityGroup();
    }

    public Integer getTrunkGroupID() {
    	if(trunkGroupID==null)
    		return null;
    	
        return trunkGroupID.getIntValue();
    }

    public Integer getFacilityGroupMemberID() {
    	if(facilityGroupMemberID==null)
    		return null;
    	
        return facilityGroupMemberID.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ResourceID [");

        if (this.lineID != null) {
            sb.append(", lineID=");
            sb.append(lineID);
        }
        
        if (this.facilityGroup != null && this.facilityGroup.getFacilityGroup()!=null) {
            sb.append(", facilityGroup=");
            sb.append(facilityGroup.getFacilityGroup());
        }

        if (this.trunkGroupID != null && this.trunkGroupID.getValue()!=null) {
            sb.append(", trunkGroupID=");
            sb.append(trunkGroupID.getValue());
        }
        
        if (this.facilityGroupMemberID != null && this.facilityGroupMemberID.getValue()!=null) {
            sb.append(", facilityGroupMemberID=");
            sb.append(facilityGroupMemberID.getValue());
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(lineID==null && facilityGroup==null && facilityGroupMemberID==null && trunkGroupID==null)
			throw new ASNParsingComponentException("one if child items should be set for resource ID", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}