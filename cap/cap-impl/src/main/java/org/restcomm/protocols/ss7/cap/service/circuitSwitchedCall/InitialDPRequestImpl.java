/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CallingPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.DigitsImpl;
import org.restcomm.protocols.ss7.cap.api.isup.LocationNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.OriginalCalledNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.RedirectingPartyIDCapImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.ASNEventTypeBCSMImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ASNCGEncounteredImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapabilityWrapperImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.inap.api.isup.CallingPartysCategoryInapImpl;
import org.restcomm.protocols.ss7.inap.api.isup.HighLayerCompatibilityInapImpl;
import org.restcomm.protocols.ss7.inap.api.isup.RedirectionInformationInapImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGIndexImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class InitialDPRequestImpl extends CircuitSwitchedCallMessageImpl implements InitialDPRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = 0)
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private CalledPartyNumberCapImpl calledPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private CallingPartyNumberCapImpl callingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1)
    private CallingPartysCategoryInapImpl callingPartysCategory;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1)
    private ASNCGEncounteredImpl cgEncountered;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1)
    private IPSSPCapabilitiesImpl IPSSPCapabilities;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false,index = -1)
    private LocationNumberCapImpl locationNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = false,index = -1)
    private OriginalCalledNumberCapImpl originalCalledPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 23,constructed = false,index = -1)
    private HighLayerCompatibilityInapImpl highLayerCompatibility;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 25,constructed = false,index = -1)
    private DigitsImpl additionalCallingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 27,constructed = true,index = -1)
    private BearerCapabilityWrapperImpl bearerCapability;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 28,constructed = false,index = -1)
    private ASNEventTypeBCSMImpl eventTypeBCSM;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29,constructed = false,index = -1)
    private RedirectingPartyIDCapImpl redirectingPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false,index = -1)
    private RedirectionInformationInapImpl redirectionInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 17,constructed = false,index = -1)
    private CauseCapImpl cause;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 32,constructed = true,index = -1)
    private ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 37,constructed = false,index = -1)
    private CarrierImpl carrier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 45,constructed = false,index = -1)
    private CUGIndexImpl cugIndex;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 46,constructed = false,index = -1)
    private CUGInterlockImpl cugInterlock;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 47,constructed = false,index = -1)
    private ASNNull cugOutgoingAccess;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1)
    private IMSIImpl imsi;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = true,index = -1)
    private SubscriberStateWrapperImpl subscriberState;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = true,index = -1)
    private LocationInformationImpl locationInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 53,constructed = true,index = -1)
    private ExtBasicServiceCodeWrapperImpl extBasicServiceCode;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 54,constructed = false,index = -1)
    private CallReferenceNumberImpl callReferenceNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 55,constructed = false,index = -1)
    private ISDNAddressStringImpl mscAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 56,constructed = false,index = -1)
    private CalledPartyBCDNumberImpl calledPartyBCDNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 57,constructed = false,index = -1)
    private TimeAndTimezoneImpl timeAndTimezone;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 58,constructed = false,index = -1)
    private ASNNull callForwardingSSPending;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 59,constructed = true,index = -1)
    private InitialDPArgExtensionImpl initialDPArgExtension;

    /**
     * This constructor is only for deserialisation purpose
     */
    public InitialDPRequestImpl() {
    }

    public InitialDPRequestImpl(int serviceKey, CalledPartyNumberCapImpl calledPartyNumber,
            CallingPartyNumberCapImpl callingPartyNumber, CallingPartysCategoryInapImpl callingPartysCategory,
            CGEncountered cgEncountered, IPSSPCapabilitiesImpl IPSSPCapabilities, LocationNumberCapImpl locationNumber,
            OriginalCalledNumberCapImpl originalCalledPartyID, CAPExtensionsImpl extensions,
            HighLayerCompatibilityInapImpl highLayerCompatibility, DigitsImpl additionalCallingPartyNumber,
            BearerCapabilityImpl bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCapImpl redirectingPartyID,
            RedirectionInformationInapImpl redirectionInformation, CauseCapImpl cause,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, CarrierImpl carrier, CUGIndexImpl cugIndex,
            CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, IMSIImpl imsi, SubscriberStateImpl subscriberState,
            LocationInformationImpl locationInformation, ExtBasicServiceCodeImpl extBasicServiceCode,
            CallReferenceNumberImpl callReferenceNumber, ISDNAddressStringImpl mscAddress, CalledPartyBCDNumberImpl calledPartyBCDNumber,
            TimeAndTimezoneImpl timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtensionImpl initialDPArgExtension) {
        
    	this.serviceKey = new ASNInteger();
    	this.serviceKey.setValue(Long.valueOf(serviceKey));
    	
        this.calledPartyNumber = calledPartyNumber;
        this.callingPartyNumber = callingPartyNumber;
        this.callingPartysCategory = callingPartysCategory;
        
        if(cgEncountered!=null) {
        	this.cgEncountered = new ASNCGEncounteredImpl();
        	this.cgEncountered.setType(cgEncountered);
        }
        
        this.IPSSPCapabilities = IPSSPCapabilities;
        this.locationNumber = locationNumber;
        this.originalCalledPartyID = originalCalledPartyID;
        this.extensions = extensions;
        this.highLayerCompatibility = highLayerCompatibility;
        this.additionalCallingPartyNumber = additionalCallingPartyNumber;
        
        if(bearerCapability!=null)
        	this.bearerCapability = new BearerCapabilityWrapperImpl(bearerCapability);
        
        if(eventTypeBCSM!=null) {
        	this.eventTypeBCSM = new ASNEventTypeBCSMImpl();
        	this.eventTypeBCSM.setType(eventTypeBCSM);
        }
        
        this.redirectingPartyID = redirectingPartyID;
        this.redirectionInformation = redirectionInformation;
        this.cause = cause;
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
        this.carrier = carrier;
        this.cugIndex = cugIndex;
        this.cugInterlock = cugInterlock;
        
        if(cugOutgoingAccess)
        	this.cugOutgoingAccess = new ASNNull();
        
        this.imsi = imsi;
        
        if(subscriberState!=null)
        	this.subscriberState = new SubscriberStateWrapperImpl(subscriberState);
        
        this.locationInformation = locationInformation;
        
        if(extBasicServiceCode!=null)
        	this.extBasicServiceCode = new ExtBasicServiceCodeWrapperImpl(extBasicServiceCode);
        
        this.callReferenceNumber = callReferenceNumber;
        this.mscAddress = mscAddress;
        this.calledPartyBCDNumber = calledPartyBCDNumber;
        this.timeAndTimezone = timeAndTimezone;
        
        if(callForwardingSSPending)
        	this.callForwardingSSPending = new ASNNull();
        
        this.initialDPArgExtension = initialDPArgExtension;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.initialDP_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.initialDP;
    }

    @Override
    public int getServiceKey() {
    	if(this.serviceKey==null || this.serviceKey.getValue()==null)
    		return 0;
    	
        return this.serviceKey.getValue().intValue();
    }

    @Override
    public CalledPartyNumberCapImpl getCalledPartyNumber() {
        return this.calledPartyNumber;
    }

    @Override
    public CallingPartyNumberCapImpl getCallingPartyNumber() {
        return callingPartyNumber;
    }

    @Override
    public CallingPartysCategoryInapImpl getCallingPartysCategory() {
        return callingPartysCategory;
    }

    @Override
    public CGEncountered getCGEncountered() {
    	if(cgEncountered==null)
    		return null;
    	
        return cgEncountered.getType();
    }

    @Override
    public IPSSPCapabilitiesImpl getIPSSPCapabilities() {
        return IPSSPCapabilities;
    }

    @Override
    public LocationNumberCapImpl getLocationNumber() {
        return locationNumber;
    }

    @Override
    public OriginalCalledNumberCapImpl getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    @Override
    public CAPExtensionsImpl getExtensions() {
        return extensions;
    }

    @Override
    public HighLayerCompatibilityInapImpl getHighLayerCompatibility() {
        return highLayerCompatibility;
    }

    @Override
    public DigitsImpl getAdditionalCallingPartyNumber() {
    	if(additionalCallingPartyNumber!=null)
    		additionalCallingPartyNumber.setIsGenericNumber();
    	
        return additionalCallingPartyNumber;
    }

    @Override
    public BearerCapabilityImpl getBearerCapability() {
    	if(bearerCapability==null)
    		return null;
    	
        return bearerCapability.getBearerCapability();
    }

    @Override
    public EventTypeBCSM getEventTypeBCSM() {
    	if(eventTypeBCSM==null)
    		return null;
    	
        return eventTypeBCSM.getType();
    }

    @Override
    public RedirectingPartyIDCapImpl getRedirectingPartyID() {
        return redirectingPartyID;
    }

    @Override
    public RedirectionInformationInapImpl getRedirectionInformation() {
        return redirectionInformation;
    }

    @Override
    public CauseCapImpl getCause() {
        return cause;
    }

    @Override
    public ServiceInteractionIndicatorsTwoImpl getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    @Override
    public CarrierImpl getCarrier() {
        return carrier;
    }

    @Override
    public CUGIndexImpl getCugIndex() {
        return cugIndex;
    }

    @Override
    public CUGInterlockImpl getCugInterlock() {
        return cugInterlock;
    }

    @Override
    public boolean getCugOutgoingAccess() {
        return cugOutgoingAccess!=null;
    }

    @Override
    public IMSIImpl getIMSI() {
        return imsi;
    }

    @Override
    public SubscriberStateImpl getSubscriberState() {
    	if(subscriberState==null)
    		return null;
    	
        return subscriberState.getSubscriberState();
    }

    @Override
    public LocationInformationImpl getLocationInformation() {
        return locationInformation;
    }

    @Override
    public ExtBasicServiceCodeImpl getExtBasicServiceCode() {
    	if(extBasicServiceCode==null)
    		return null;
    	
        return extBasicServiceCode.getExtBasicServiceCode();
    }

    @Override
    public CallReferenceNumberImpl getCallReferenceNumber() {
        return callReferenceNumber;
    }

    @Override
    public ISDNAddressStringImpl getMscAddress() {
        return mscAddress;
    }

    @Override
    public CalledPartyBCDNumberImpl getCalledPartyBCDNumber() {
        return calledPartyBCDNumber;
    }

    @Override
    public TimeAndTimezoneImpl getTimeAndTimezone() {
        return timeAndTimezone;
    }

    @Override
    public boolean getCallForwardingSSPending() {
        return callForwardingSSPending!=null;
    }

    @Override
    public InitialDPArgExtensionImpl getInitialDPArgExtension() {
        return initialDPArgExtension;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("InitialDPRequestIndication [");
        this.addInvokeIdInfo(sb);

        sb.append(", serviceKey=");
        sb.append(serviceKey);
        if (this.calledPartyNumber != null) {
            sb.append(", calledPartyNumber=");
            sb.append(calledPartyNumber.toString());
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber.toString());
        }
        if (this.callingPartysCategory != null) {
            sb.append(", callingPartysCategory=");
            sb.append(callingPartysCategory.toString());
        }
        if (this.cgEncountered != null && this.cgEncountered.getType()!=null) {
            sb.append(", CGEncountered=");
            sb.append(cgEncountered.getType());
        }
        if (this.IPSSPCapabilities != null) {
            sb.append(", IPSSPCapabilities=");
            sb.append(IPSSPCapabilities.toString());
        }
        if (this.locationNumber != null) {
            sb.append(", locationNumber=");
            sb.append(locationNumber.toString());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.highLayerCompatibility != null) {
            sb.append(", highLayerCompatibility=");
            sb.append(highLayerCompatibility.toString());
        }
        if (this.additionalCallingPartyNumber != null) {
            sb.append(", additionalCallingPartyNumber=");
            sb.append(additionalCallingPartyNumber.toString());
        }
        if (this.bearerCapability != null && this.bearerCapability.getBearerCapability()!=null) {
            sb.append(", bearerCapability=");
            sb.append(bearerCapability.getBearerCapability());
        }
        if (this.eventTypeBCSM != null && this.eventTypeBCSM.getType()!=null) {
            sb.append(", eventTypeBCSM=");
            sb.append(eventTypeBCSM.getType());
        }
        if (this.redirectingPartyID != null) {
            sb.append(", redirectingPartyID=");
            sb.append(redirectingPartyID.toString());
        }
        if (this.redirectionInformation != null) {
            sb.append(", redirectionInformation=");
            sb.append(redirectionInformation.toString());
        }
        if (this.cause != null) {
            sb.append(", cause=");
            sb.append(cause.toString());
        }
        if (this.serviceInteractionIndicatorsTwo != null) {
            sb.append(", serviceInteractionIndicatorsTwo=");
            sb.append(serviceInteractionIndicatorsTwo.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }
        if (this.cugIndex != null) {
            sb.append(", cugIndex=");
            sb.append(cugIndex.toString());
        }
        if (this.cugInterlock != null) {
            sb.append(", cugInterlock=");
            sb.append(cugInterlock.toString());
        }
        if (this.cugOutgoingAccess!=null) {
            sb.append(", cugOutgoingAccess");
        }
        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(imsi.toString());
        }
        if (this.subscriberState != null && this.subscriberState.getSubscriberState()!=null) {
            sb.append(", subscriberState=");
            sb.append(subscriberState.getSubscriberState());
        }
        if (this.locationInformation != null) {
            sb.append(", locationInformation=");
            sb.append(locationInformation.toString());
        }
        if (this.extBasicServiceCode != null && this.extBasicServiceCode.getExtBasicServiceCode()!=null) {
            sb.append(", extBasicServiceCode=");
            sb.append(extBasicServiceCode.getExtBasicServiceCode());
        }
        if (this.callReferenceNumber != null) {
            sb.append(", callReferenceNumber=");
            sb.append(callReferenceNumber.toString());
        }
        if (this.mscAddress != null) {
            sb.append(", mscAddress=");
            sb.append(mscAddress.toString());
        }
        if (this.calledPartyBCDNumber != null) {
            sb.append(", calledPartyBCDNumber=");
            sb.append(calledPartyBCDNumber.toString());
        }
        if (this.timeAndTimezone != null) {
            sb.append(", timeAndTimezone=");
            sb.append(timeAndTimezone.toString());
        }
        if (this.callForwardingSSPending!=null) {
            sb.append(", callForwardingSSPending");
        }
        if (this.initialDPArgExtension != null) {
            sb.append(", initialDPArgExtension=");
            sb.append(initialDPArgExtension.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}

// added:
// CGEncountered
// Cause
// serviceInteractionIndicatorsTwo
// carrier
// cugIndex
// cugInterlock
// cugOutgoingAccess

