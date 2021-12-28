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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardGVNSIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AlertingPatternWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.ForwardGVNSIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.HighLayerCompatibilityIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectionInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGCallIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGInterLockCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericDigitsSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericNumbersSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.CUGCallIndicatorImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.CUGInterLockCodeImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GenericDigitsSetImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GenericNumbersSetImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ISDNAccessRelatedInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.RouteListImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceInteractionIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.ForwardCallIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class InitiateCallAttemptRequestImpl extends CircuitSwitchedCallMessageImpl implements
        InitiateCallAttemptRequest {
	private static final long serialVersionUID = 1L;
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = false,index = -1,defaultImplementation = OriginalCalledNumberIsupImpl.class)
    private OriginalCalledNumberIsup originalCalledPartyID;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 2,constructed = true,index = -1)
	private SendingLegIDWrapperImpl legToBeCreated;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 3,constructed = false,index = -1, defaultImplementation = CallingPartysCategoryIsupImpl.class)
    private CallingPartysCategoryIsup callingPartysCategory;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 4,constructed = false,index = -1,defaultImplementation = RedirectingPartyIDIsupImpl.class)
    private RedirectingPartyIDIsup redirectingPartyID;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 5,constructed = false,index = -1, defaultImplementation = RedirectionInformationIsupImpl.class)
    private RedirectionInformationIsup redirectionInformation;

    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 6,constructed = true,index = -1)
    private BearerCapabilityWrapperImpl bearerCapability;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 7,constructed = false,index = -1, defaultImplementation = CUGCallIndicatorImpl.class)
	private CUGCallIndicator cugCallIndicator;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 8,constructed = false,index = -1, defaultImplementation = CUGInterLockCodeImpl.class)
	private CUGInterLockCode cugInterLockCode;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 9,constructed = false,index = -1, defaultImplementation = ForwardCallIndicatorsImpl.class)
    private ForwardCallIndicators forwardCallIndicators;

    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 10,constructed = true,index = -1, defaultImplementation = GenericDigitsSetImpl.class)
	private GenericDigitsSet genericDigitsSet;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 11,constructed = true,index = -1, defaultImplementation = GenericNumbersSetImpl.class)
	private GenericNumbersSet genericNumberSet;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 12,constructed = false,index = -1, defaultImplementation = HighLayerCompatibilityIsupImpl.class)
    private HighLayerCompatibilityIsup highLayerCompatibility;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 13,constructed = false,index = -1, defaultImplementation = ForwardGVNSIsupImpl.class)
	private ForwardGVNSIsup forwardGVNSIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = DestinationRoutingAddressImpl.class)
    private DestinationRoutingAddress destinationRoutingAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = AlertingPatternWrapperImpl.class)
    private AlertingPatternWrapper alertingPattern;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1, defaultImplementation = ISDNAccessRelatedInformationImpl.class)
    private ISDNAccessRelatedInformation isdnAccessRelatedInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1, defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup travellingClassMark;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29,constructed = false,index = -1, defaultImplementation = ServiceInteractionIndicatorsImpl.class)
    private ServiceInteractionIndicators serviceInteractionIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false,index = -1, defaultImplementation = CallingPartyNumberIsupImpl.class)
    private CallingPartyNumberIsup callingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 14,constructed = false,index = -1, defaultImplementation = RouteListImpl.class)
    private RouteList routeList;
    
    public InitiateCallAttemptRequestImpl() {
    }

    public InitiateCallAttemptRequestImpl(OriginalCalledNumberIsup originalCalledPartyID,LegType legToBeCreated,CallingPartysCategoryIsup callingPartysCategory,
    		RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,BearerCapability bearerCapability,
    		CUGCallIndicator cugCallIndicator,CUGInterLockCode cugInterLockCode,ForwardCallIndicators forwardCallIndicators,
    		GenericDigitsSet genericDigitsSet,GenericNumbersSet genericNumberSet,HighLayerCompatibilityIsup highLayerCompatibility,
    		ForwardGVNSIsup forwardGVNSIndicator,DestinationRoutingAddress destinationRoutingAddress,AlertingPattern alertingPattern,
    		CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber, RouteList routeList) {
    	this(destinationRoutingAddress, alertingPattern, null, null, extensions, serviceInteractionIndicators, callingPartyNumber);
    	
    	this.originalCalledPartyID=originalCalledPartyID;
    	
    	if(legToBeCreated!=null)
    		this.legToBeCreated=new SendingLegIDWrapperImpl(new SendingLegIDImpl(legToBeCreated));
    		
    	this.callingPartysCategory=callingPartysCategory;
    	this.redirectingPartyID=redirectingPartyID;
    	this.redirectionInformation=redirectionInformation;
    	
    	if(bearerCapability!=null)
    		this.bearerCapability=new BearerCapabilityWrapperImpl(bearerCapability);
    	
    	this.cugCallIndicator=cugCallIndicator;
    	this.cugInterLockCode=cugInterLockCode;
    	this.forwardCallIndicators=forwardCallIndicators;
    	this.genericDigitsSet=genericDigitsSet;
    	this.genericNumberSet=genericNumberSet;
    	this.highLayerCompatibility=highLayerCompatibility;
    	this.forwardGVNSIndicator=forwardGVNSIndicator;
    	this.routeList=routeList;
    }
    
    public InitiateCallAttemptRequestImpl(DestinationRoutingAddress destinationRoutingAddress,AlertingPattern alertingPattern,
    		ISDNAccessRelatedInformation isdnAccessRelatedInformation,LocationNumberIsup travellingClassMark, 
    		CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber) {
        this.destinationRoutingAddress = destinationRoutingAddress;
        
        if(alertingPattern!=null)
        	this.alertingPattern=new AlertingPatternWrapperImpl(alertingPattern);
        
        this.isdnAccessRelatedInformation=isdnAccessRelatedInformation;
        this.travellingClassMark=travellingClassMark;
        this.extensions = extensions;
        this.serviceInteractionIndicators=serviceInteractionIndicators;
        this.callingPartyNumber = callingPartyNumber;
    }

    public INAPMessageType getMessageType() {
        return INAPMessageType.initiateCallAttempt_Request;
    }

    public int getOperationCode() {
        return INAPOperationCode.initiateCallAttempt;
    }

    @Override
    public OriginalCalledNumberIsup getOriginalCalledPartyID() {
		return originalCalledPartyID;
	}

    @Override
    public LegType getLegToBeCreated() {
		if(legToBeCreated==null || legToBeCreated.getSendingLegID()==null)
			return null;
		
		return legToBeCreated.getSendingLegID().getSendingSideID();
	}

    @Override
    public CallingPartysCategoryIsup getCallingPartysCategory() {
		return callingPartysCategory;
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
    public BearerCapability getBearerCapability() {
		if(bearerCapability==null)
			return null;
		
		return bearerCapability.getBearerCapability();
	}

    @Override
    public CUGCallIndicator getCUGCallIndicator() {
		return cugCallIndicator;
	}

    @Override
    public CUGInterLockCode getCUGInterLockCode() {
		return cugInterLockCode;
	}

    @Override
    public ForwardCallIndicators getForwardCallIndicators() {
		return forwardCallIndicators;
	}

    @Override
    public GenericDigitsSet getGenericDigitsSet() {
		return genericDigitsSet;
	}

    @Override
    public GenericNumbersSet getGenericNumberSet() {
		return genericNumberSet;
	}

    @Override
    public HighLayerCompatibilityIsup getHighLayerCompatibility() {
		return highLayerCompatibility;
	}

    @Override
    public ForwardGVNSIsup getForwardGVNSIndicator() {
		return forwardGVNSIndicator;
	}

	@Override
    public DestinationRoutingAddress getDestinationRoutingAddress() {
        return destinationRoutingAddress;
    }

	@Override
    public AlertingPattern getAlertingPattern() {
    	if(alertingPattern==null) 
    		return null;
    	
		return alertingPattern.getAlertingPattern();
	}

	@Override
    public ISDNAccessRelatedInformation getISDNAccessRelatedInformation() {
		return isdnAccessRelatedInformation;
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
    public ServiceInteractionIndicators getServiceInteractionIndicators() {
		return serviceInteractionIndicators;
	}

	@Override
    public CallingPartyNumberIsup getCallingPartyNumber() {
        return callingPartyNumber;
    }

	@Override
    public RouteList getRouteList() {
        return routeList;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("InitiateCallAttemptIndication [");
        this.addInvokeIdInfo(sb);

        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.legToBeCreated != null && this.legToBeCreated.getSendingLegID()!=null && this.legToBeCreated.getSendingLegID().getSendingSideID()!=null) {
            sb.append(", legToBeCreated=");
            sb.append(this.legToBeCreated.getSendingLegID().getSendingSideID());
        }
        if (this.callingPartysCategory != null) {
            sb.append(", callingPartysCategory=");
            sb.append(callingPartysCategory.toString());
        }
        if (this.redirectingPartyID != null) {
            sb.append(", redirectingPartyID=");
            sb.append(redirectingPartyID.toString());
        }
        if (this.redirectionInformation != null) {
            sb.append(", redirectionInformation=");
            sb.append(redirectionInformation.toString());
        }
        if (this.bearerCapability != null && this.bearerCapability.getBearerCapability()!=null) {
            sb.append(", bearerCapability=");
            sb.append(bearerCapability.getBearerCapability().toString());
        }
        if (this.cugCallIndicator != null) {
            sb.append(", cugCallIndicator=");
            sb.append(cugCallIndicator.toString());
        }
        if (this.cugInterLockCode != null) {
            sb.append(", cugInterLockCode=");
            sb.append(cugInterLockCode.toString());
        }
        if (this.forwardCallIndicators != null) {
            sb.append(", forwardCallIndicators=");
            sb.append(forwardCallIndicators.toString());
        }
        if (this.genericDigitsSet != null) {
            sb.append(", genericDigitsSet=");
            sb.append(genericDigitsSet.toString());
        }
        if (this.genericNumberSet != null) {
            sb.append(", genericNumberSet=");
            sb.append(genericNumberSet.toString());
        }
        if (this.highLayerCompatibility != null) {
            sb.append(", highLayerCompatibility=");
            sb.append(highLayerCompatibility.toString());
        }
        if (this.forwardGVNSIndicator != null) {
            sb.append(", forwardGVNSIndicator=");
            sb.append(forwardGVNSIndicator.toString());
        }
        if (this.destinationRoutingAddress != null) {
            sb.append(", destinationRoutingAddress=");
            sb.append(destinationRoutingAddress.toString());
        }
        if (this.alertingPattern != null && this.alertingPattern.getAlertingPattern()!=null) {
            sb.append(", alertingPattern=");
            sb.append(alertingPattern.getAlertingPattern().toString());
        }
        if (this.isdnAccessRelatedInformation != null) {
            sb.append(", isdnAccessRelatedInformation=");
            sb.append(isdnAccessRelatedInformation.toString());
        }
        if (this.travellingClassMark != null) {
            sb.append(", travellingClassMark=");
            sb.append(travellingClassMark.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.serviceInteractionIndicators != null) {
            sb.append(", serviceInteractionIndicators=");
            sb.append(serviceInteractionIndicators);
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber);
        }
        if (this.routeList != null) {
            sb.append(", routeList=");
            sb.append(routeList);
        }

        sb.append("]");

        return sb.toString();
    }
}
