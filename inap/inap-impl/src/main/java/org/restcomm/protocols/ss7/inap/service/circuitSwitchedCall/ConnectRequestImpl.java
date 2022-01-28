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
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardGVNSIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AlertingPatternWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.ForwardCallIndicatorsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.ForwardGVNSIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.HighLayerCompatibilityIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectionInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ScfIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ConnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGCallIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGInterLockCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericDigitsSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericNumbersSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ForwardingCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.CUGCallIndicatorImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.CUGInterLockCodeImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GenericDigitsSetImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GenericNumbersSetImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ASNForwardingCondition;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ISDNAccessRelatedInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.RouteListImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceInteractionIndicatorsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ConnectRequestImpl extends CircuitSwitchedCallMessageImpl implements ConnectRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = true,index = -1)
	private SendingLegIDWrapperImpl legToBeCreated;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 2,constructed = true,index = -1)
	private BearerCapabilityWrapperImpl bearerCapabilities;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 3,constructed = false,index = -1, defaultImplementation = CUGCallIndicatorImpl.class)
	private CUGCallIndicator cugCallIndicator;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 4,constructed = false,index = -1, defaultImplementation = CUGInterLockCodeImpl.class)
	private CUGInterLockCode cugInterLockCode;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 5,constructed = false,index = -1, defaultImplementation = ForwardCallIndicatorsIsupImpl.class)
	private ForwardCallIndicatorsIsup forwardCallIndicators;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 6,constructed = true,index = -1, defaultImplementation = GenericDigitsSetImpl.class)
	private GenericDigitsSet genericDigitsSet;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 7,constructed = true,index = -1, defaultImplementation = GenericNumbersSetImpl.class)
	private GenericNumbersSet genericNumberSet;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 8,constructed = false,index = -1, defaultImplementation = HighLayerCompatibilityIsupImpl.class)
	private HighLayerCompatibilityIsup highLayerCompatibility;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 9,constructed = false,index = -1, defaultImplementation = ForwardGVNSIsupImpl.class)
	private ForwardGVNSIsup forwardGVNSIndicator;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = DestinationRoutingAddressImpl.class)
    private DestinationRoutingAddress destinationRoutingAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = AlertingPatternWrapperImpl.class)
    private AlertingPatternWrapper alertingPattern;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup correlationID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ASNInteger cutAndPaste;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
    private ASNForwardingCondition forwardingCondition;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1,defaultImplementation = ISDNAccessRelatedInformationImpl.class)
    private ISDNAccessRelatedInformation isdnAccessRelatedInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1,defaultImplementation = OriginalCalledNumberIsupImpl.class)
    private OriginalCalledNumberIsup originalCalledPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = true,index = -1,defaultImplementation = RouteListImpl.class)
    private RouteList routeList;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1,defaultImplementation = ScfIDImpl.class)
    private ScfID scfID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1,defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup travellingClassMark;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false,index = -1,defaultImplementation = CarrierImpl.class)
    private Carrier carrier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 26,constructed = false,index = -1, defaultImplementation = ServiceInteractionIndicatorsImpl.class)
    private ServiceInteractionIndicators serviceInteractionIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 27,constructed = false,index = -1, defaultImplementation = CallingPartyNumberIsupImpl.class)
    private CallingPartyNumberIsup callingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 28,constructed = false,index = -1, defaultImplementation = CallingPartysCategoryIsupImpl.class)
    private CallingPartysCategoryIsup callingPartysCategory;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29,constructed = false,index = -1,defaultImplementation = RedirectingPartyIDIsupImpl.class)
    private RedirectingPartyIDIsup redirectingPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false,index = -1, defaultImplementation = RedirectionInformationIsupImpl.class)
    private RedirectionInformationIsup redirectionInformation;

    public ConnectRequestImpl() {
    }

    public ConnectRequestImpl(LegType legToBeCreated,BearerCapability bearerCapabilities,
    		CUGCallIndicator cugCallIndicator,CUGInterLockCode cugInterLockCode,ForwardCallIndicatorsIsup forwardCallIndicators,
    		GenericDigitsSet genericDigitsSet, GenericNumbersSet genericNumberSet,
    		HighLayerCompatibilityIsup highLayerCompatibility,ForwardGVNSIsup forwardGVNSIndicator,
    		DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern,
    		DigitsIsup correlationID, Integer cutAndPaste,OriginalCalledNumberIsup originalCalledPartyID, 
    		RouteList routeList,ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
    		ServiceInteractionIndicators serviceInteractionIndicators,CallingPartyNumberIsup callingPartyNumber,
    		CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation) {
    		this(destinationRoutingAddress, alertingPattern,correlationID, cutAndPaste, null,null,
    			originalCalledPartyID, routeList,scfID, null, extensions, carrier,serviceInteractionIndicators,
    			callingPartyNumber, callingPartysCategory, redirectingPartyID, redirectionInformation);
    		
    		if(legToBeCreated!=null)
    			this.legToBeCreated=new SendingLegIDWrapperImpl(new SendingLegIDImpl(legToBeCreated));
    		
    		if(bearerCapabilities!=null)
    			this.bearerCapabilities=new BearerCapabilityWrapperImpl(bearerCapabilities);
    		
    		this.cugCallIndicator=cugCallIndicator;
    		this.cugInterLockCode=cugInterLockCode;
    		this.forwardCallIndicators=forwardCallIndicators;
    		this.genericDigitsSet=genericDigitsSet;
    		this.genericNumberSet=genericNumberSet;  
    		this.highLayerCompatibility=highLayerCompatibility;
    		this.forwardGVNSIndicator=forwardGVNSIndicator;    	
    }
    public ConnectRequestImpl(DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern,
    		DigitsIsup correlationID, Integer cutAndPaste, ForwardingCondition forwardingCondition,
    		ISDNAccessRelatedInformation isdnAccessRelatedInformation,OriginalCalledNumberIsup originalCalledPartyID, 
    		RouteList routeList,ScfID scfID, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier,
    		ServiceInteractionIndicators serviceInteractionIndicators,CallingPartyNumberIsup callingPartyNumber,
    		CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation) {
        this.destinationRoutingAddress = destinationRoutingAddress;
        
        if(alertingPattern!=null)
        	this.alertingPattern = new AlertingPatternWrapperImpl(alertingPattern);
        
        this.correlationID=correlationID;
        
        if(cutAndPaste!=null)
        	this.cutAndPaste=new ASNInteger(cutAndPaste);
        	
        if(forwardingCondition!=null)
        	this.forwardingCondition=new ASNForwardingCondition(forwardingCondition);
        	
        this.isdnAccessRelatedInformation=isdnAccessRelatedInformation;        
        this.originalCalledPartyID = originalCalledPartyID;
        this.routeList=routeList;
        this.scfID=scfID;
        this.travellingClassMark=travellingClassMark;
        this.extensions = extensions;
        this.carrier = carrier;
        this.serviceInteractionIndicators=serviceInteractionIndicators;
        this.callingPartyNumber=callingPartyNumber;
        this.callingPartysCategory = callingPartysCategory;
        this.redirectingPartyID = redirectingPartyID;
        this.redirectionInformation = redirectionInformation;                
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.connect_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.connect;
    }
    
    public LegType getLegToBeCreated() {
    	if(legToBeCreated==null || legToBeCreated.getSendingLegID()==null)
    		return null;
    	
		return legToBeCreated.getSendingLegID().getSendingSideID();
	}

	public BearerCapability getBearerCapabilities() {
		if(bearerCapabilities==null)
			return null;
		
		return bearerCapabilities.getBearerCapability();
	}

	public CUGCallIndicator getCUGCallIndicator() {
		return cugCallIndicator;
	}

	public CUGInterLockCode getCUGInterLockCode() {
		return cugInterLockCode;
	}

	public ForwardCallIndicatorsIsup getForwardCallIndicators() {
		return forwardCallIndicators;
	}

	public GenericDigitsSet getGenericDigitsSet() {
		return genericDigitsSet;
	}

	public GenericNumbersSet getGenericNumberSet() {
		return genericNumberSet;
	}

	public HighLayerCompatibilityIsup getHighLayerCompatibility() {
		return highLayerCompatibility;
	}

	public ForwardGVNSIsup getForwardGVNSIndicator() {
		return forwardGVNSIndicator;
	}

	public ISDNAccessRelatedInformation getIsdnAccessRelatedInformation() {
		return isdnAccessRelatedInformation;
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
    public DigitsIsup getCorrelationID() {
    	if(correlationID!=null)
    		correlationID.setIsGenericNumber();
    	
        return correlationID;
	}

    @Override
    public Integer getCutAndPaste() {
    	if(cutAndPaste==null)
    		return null;
    	
		return cutAndPaste.getIntValue();
	}

	@Override
    public ForwardingCondition getForwardingCondition() {
		
		if(forwardingCondition==null)
			return null;
		return forwardingCondition.getType();
	}

	@Override
    public ISDNAccessRelatedInformation getISDNAccessRelatedInformation() {
		return isdnAccessRelatedInformation;
	}

	@Override
    public OriginalCalledNumberIsup getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

	@Override
    public RouteList getRouteList() {
		return routeList;
	}

	@Override
    public ScfID getScfID() {
		return scfID;
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
    public ServiceInteractionIndicators getServiceInteractionIndicators() {
		return serviceInteractionIndicators;
	}

    @Override
    public CallingPartyNumberIsup getCallingPartyNumber() {
		return callingPartyNumber;
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
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ConnectRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.legToBeCreated != null && this.legToBeCreated.getSendingLegID()!=null && this.legToBeCreated.getSendingLegID().getSendingSideID()!=null) {
            sb.append(", legToBeCreated=");
            sb.append(this.legToBeCreated.getSendingLegID().getSendingSideID());
        }
        if (this.bearerCapabilities != null && this.bearerCapabilities.getBearerCapability()!=null) {
            sb.append(", bearerCapabilities=");
            sb.append(this.bearerCapabilities.getBearerCapability().toString());
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
        if (this.correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID.toString());
        }
        if (this.cutAndPaste != null && this.cutAndPaste.getValue()!=null) {
            sb.append(", cutAndPaste=");
            sb.append(cutAndPaste.getValue().toString());
        }
        if (this.forwardingCondition != null && this.forwardingCondition.getType()!=null) {
            sb.append(", forwardingCondition=");
            sb.append(forwardingCondition.toString());
        }
        if (this.isdnAccessRelatedInformation != null) {
            sb.append(", isdnAccessRelatedInformation=");
            sb.append(isdnAccessRelatedInformation.toString());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.routeList != null) {
            sb.append(", routeList=");
            sb.append(routeList);
        }
        if (this.scfID != null) {
            sb.append(", scfID=");
            sb.append(scfID.toString());
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
        if (this.serviceInteractionIndicators != null) {
            sb.append(", serviceInteractionIndicators=");
            sb.append(serviceInteractionIndicators.toString());
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber.toString());
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

        sb.append("]");

        return sb.toString();
    }
}