/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.OriginationAttemptAuthorizedRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CallingPartyBusinessGroupIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.DpSpecificCommonParametersImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FacilityGroupWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class OriginationAttemptAuthorizedRequestImpl extends CircuitSwitchedCallMessageImpl implements OriginationAttemptAuthorizedRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = DpSpecificCommonParametersImpl.class)
	private DpSpecificCommonParameters dpSpecificCommonParameters;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = CalledPartyNumberIsupImpl.class)
	private CalledPartyNumberIsup dialedDigits;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = CallingPartyBusinessGroupIDImpl.class)
	private CallingPartyBusinessGroupID callingPartyBusinessGroupID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1,defaultImplementation = CallingPartySubaddress.class)
	private CallingPartySubaddress callingPartySubaddress;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1)
	private FacilityGroupWrapperImpl callingFacilityGroup;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1)
	private ASNInteger callingFacilityGroupMember;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1, defaultImplementation = LocationNumberIsupImpl.class)
	private LocationNumberIsup travellingClassMark;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
	private CAPINAPExtensions extensions;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1, defaultImplementation = CarrierImpl.class)
	private Carrier carrier;

    public OriginationAttemptAuthorizedRequestImpl() {
    }

    public OriginationAttemptAuthorizedRequestImpl(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,Integer callingFacilityGroupMember,LocationNumberIsup travellingClassMark,
    		CAPINAPExtensions extensions,Carrier carrier) {
        this.dpSpecificCommonParameters = dpSpecificCommonParameters;
        this.dialedDigits = dialedDigits;
        this.callingPartyBusinessGroupID = callingPartyBusinessGroupID;
        this.callingPartySubaddress=callingPartySubaddress;
        
        if(callingFacilityGroup!=null)
        	this.callingFacilityGroup=new FacilityGroupWrapperImpl(callingFacilityGroup);
        
        if(callingFacilityGroupMember!=null) {
        	this.callingFacilityGroupMember=new ASNInteger();
        	this.callingFacilityGroupMember.setValue(callingFacilityGroupMember.longValue());
        }
        
        this.travellingClassMark=travellingClassMark;
        this.extensions = extensions;
        this.carrier=carrier;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.originationAttemptAuthorized_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.originationAttemptAuthorized;
    }

    @Override
    public DpSpecificCommonParameters getDpSpecificCommonParameters() {
		return dpSpecificCommonParameters;
	}

    @Override
    public CalledPartyNumberIsup getDialedDigits() {
		return dialedDigits;
	}

    @Override
    public CallingPartyBusinessGroupID getCallingPartyBusinessGroupID() {
		return callingPartyBusinessGroupID;
	}

    @Override
    public CallingPartySubaddress getCallingPartySubaddress() {
		return callingPartySubaddress;
	}

    @Override
    public FacilityGroup getCallingFacilityGroup() {
    	if(callingFacilityGroup==null)
    		return null;
    	
		return callingFacilityGroup.getFacilityGroup();
	}

    @Override
    public Integer getCallingFacilityGroupMember() {
    	if(callingFacilityGroupMember==null || callingFacilityGroupMember.getValue()==null)
    		return null;
    	
		return callingFacilityGroupMember.getValue().intValue();
	}

    @Override
    public LocationNumberIsup getTravellingClassMark() {
		return travellingClassMark;
	}

    @Override
    public CAPINAPExtensions getExtensions() {
		return extensions;
	}

    @Override
    public Carrier getCarrier() {
		return carrier;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("OriginationAttemptAuthorizedRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.dpSpecificCommonParameters != null) {
            sb.append(", dpSpecificCommonParameters=");
            sb.append(dpSpecificCommonParameters.toString());
        }
        if (this.dialedDigits != null) {
            sb.append(", dialedDigits=");
            sb.append(dialedDigits.toString());
        }
        if (this.callingPartyBusinessGroupID != null) {
            sb.append(", callingPartyBusinessGroupID=");
            sb.append(callingPartyBusinessGroupID.toString());
        }
        if (this.callingPartySubaddress != null) {
            sb.append(", callingPartySubaddress=");
            sb.append(callingPartySubaddress.toString());
        }
        if (this.callingFacilityGroup != null && this.callingFacilityGroup.getFacilityGroup()!=null) {
            sb.append(", callingFacilityGroup=");
            sb.append(callingFacilityGroup.getFacilityGroup().toString());
        }
        if (this.callingFacilityGroupMember != null && this.callingFacilityGroupMember.getValue()!=null) {
            sb.append(", callingFacilityGroupMember=");
            sb.append(callingFacilityGroupMember.getValue().toString());
        }
        if (this.travellingClassMark != null) {
            sb.append(", travellingClassMark=");
            sb.append(travellingClassMark.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
