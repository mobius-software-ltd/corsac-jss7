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
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectionInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AnalysedInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CallingPartyBusinessGroupIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.DpSpecificCommonParametersImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FacilityGroupWrapperImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.RouteListImpl;

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
public class AnalysedInformationRequestImpl extends CircuitSwitchedCallMessageImpl implements AnalysedInformationRequest {
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
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1, defaultImplementation = OriginalCalledNumberIsupImpl.class)
	private OriginalCalledNumberIsup originalCalledPartyID;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1, defaultImplementation = DigitsIsupImpl.class)
	private DigitsIsup prefix;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1, defaultImplementation = RedirectingPartyIDIsupImpl.class)
	private RedirectingPartyIDIsup redirectingPartyID;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1, defaultImplementation = RedirectionInformationIsupImpl.class)
	private RedirectionInformationIsup redirectionInformation;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1, defaultImplementation = RouteListImpl.class)
	private RouteList routeList;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false,index = -1, defaultImplementation = LocationNumberIsupImpl.class)
	private LocationNumberIsup travellingClassMark;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
	private CAPINAPExtensions extensions;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = false,index = -1, defaultImplementation = LocationNumberIsupImpl.class)
	private LocationNumberIsup featureCode;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = false,index = -1, defaultImplementation = LocationNumberIsupImpl.class)
	private LocationNumberIsup accessCode;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = false,index = -1, defaultImplementation = CarrierImpl.class)
	private Carrier carrier;

    public AnalysedInformationRequestImpl() {
    }

    public AnalysedInformationRequestImpl(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		DigitsIsup prefix,RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		RouteList routeList,LocationNumberIsup travellingClassMark,CAPINAPExtensions extensions,LocationNumberIsup featureCode,
    		LocationNumberIsup accessCode,Carrier carrier) {
        this.dpSpecificCommonParameters = dpSpecificCommonParameters;
        this.dialedDigits = dialedDigits;
        this.callingPartyBusinessGroupID = callingPartyBusinessGroupID;
        this.callingPartySubaddress=callingPartySubaddress;
        
        if(callingFacilityGroup!=null)
        	this.callingFacilityGroup=new FacilityGroupWrapperImpl(callingFacilityGroup);
        
        if(callingFacilityGroupMember!=null)
        	this.callingFacilityGroupMember=new ASNInteger(callingFacilityGroupMember);
        	
        this.originalCalledPartyID=originalCalledPartyID;
        this.prefix=prefix;
        this.redirectingPartyID=redirectingPartyID;
        this.redirectionInformation=redirectionInformation;
        this.routeList=routeList;
        this.travellingClassMark=travellingClassMark;
        this.extensions = extensions;
        this.featureCode=featureCode;
        this.accessCode=accessCode;
        this.carrier=carrier;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.analyzedInformation_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.analysedInformation;
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
    	if(callingFacilityGroupMember==null)
    		return null;
    	
		return callingFacilityGroupMember.getIntValue();
	}

    @Override
    public OriginalCalledNumberIsup getOriginalCalledPartyID() {
		return originalCalledPartyID;
	}

    @Override
    public DigitsIsup getPrefix() {
    	if(prefix!=null)
    		prefix.setIsGenericDigits();
    	
		return prefix;
	}

    @Override
    public RedirectingPartyIDIsup getRedirectingPartyID() {
		return redirectingPartyID;
	}

    @Override
    public RedirectionInformationIsup getRedirectionInformation() {
		return redirectionInformation;
	}

    @Override
    public RouteList getRouteList() {
		return routeList;
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
    public LocationNumberIsup getFeatureCode() {
		return featureCode;
	}

    @Override
    public LocationNumberIsup getAccessCode() {
		return accessCode;
	}

    @Override
    public Carrier getCarrier() {
		return carrier;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AnalysedInformationRequestIndication [");
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
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.prefix != null) {
            sb.append(", prefix=");
            sb.append(prefix.toString());
        }
        if (this.redirectingPartyID != null) {
            sb.append(", redirectingPartyID=");
            sb.append(redirectingPartyID.toString());
        }
        if (this.redirectionInformation != null) {
            sb.append(", redirectionInformation=");
            sb.append(redirectionInformation.toString());
        }
        if (this.routeList != null) {
            sb.append(", routeList=");
            sb.append(routeList.toString());
        }
        if (this.travellingClassMark != null) {
            sb.append(", travellingClassMark=");
            sb.append(travellingClassMark.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.featureCode != null) {
            sb.append(", featureCode=");
            sb.append(featureCode.toString());
        }
        if (this.accessCode != null) {
            sb.append(", accessCode=");
            sb.append(accessCode.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
