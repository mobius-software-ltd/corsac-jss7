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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGIndex;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGInterlock;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ASNCGEncountered;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.HighLayerCompatibilityIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectionInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNEventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.SubscriberStateWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.CUGIndexImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public abstract class InitialDPRequestBaseImpl extends CircuitSwitchedCallMessageImpl implements InitialDPRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = 0)
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1, defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup calledPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1, defaultImplementation = CallingPartyNumberIsupImpl.class)
    private CallingPartyNumberIsup callingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1, defaultImplementation = CallingPartysCategoryIsupImpl.class)
    private CallingPartysCategoryIsup callingPartysCategory;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1)
    private ASNCGEncountered cgEncountered;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1, defaultImplementation = IPSSPCapabilitiesImpl.class)
    private IPSSPCapabilities IPSSPCapabilities;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false,index = -1, defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup locationNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = false,index = -1, defaultImplementation = OriginalCalledNumberIsupImpl.class)
    private OriginalCalledNumberIsup originalCalledPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 23,constructed = false,index = -1, defaultImplementation = HighLayerCompatibilityIsupImpl.class)
    private HighLayerCompatibilityIsup highLayerCompatibility;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 25,constructed = false,index = -1, defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup additionalCallingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 27,constructed = true,index = -1)
    private BearerCapabilityWrapperImpl bearerCapability;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 28,constructed = false,index = -1)
    private ASNEventTypeBCSM eventTypeBCSM;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29,constructed = false,index = -1,defaultImplementation = RedirectingPartyIDIsupImpl.class)
    private RedirectingPartyIDIsup redirectingPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false,index = -1, defaultImplementation = RedirectionInformationIsupImpl.class)
    private RedirectionInformationIsup redirectionInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 17,constructed = false,index = -1,defaultImplementation = CauseIsupImpl.class)
    private CauseIsup cause;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 32,constructed = true,index = -1,defaultImplementation = ServiceInteractionIndicatorsTwoImpl.class)
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 37,constructed = false,index = -1,defaultImplementation = CarrierImpl.class)
    private Carrier carrier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 45,constructed = false,index = -1,defaultImplementation = CUGIndexImpl.class)
    private CUGIndex cugIndex;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 46,constructed = false,index = -1,defaultImplementation = CUGInterlockImpl.class)
    private CUGInterlock cugInterlock;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 47,constructed = false,index = -1)
    private ASNNull cugOutgoingAccess;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1, defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = true,index = -1)
    private SubscriberStateWrapperImpl subscriberState;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = true,index = -1, defaultImplementation = LocationInformationImpl.class)
    private LocationInformation locationInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 53,constructed = true,index = -1)
    private ExtBasicServiceCodeWrapperImpl extBasicServiceCode;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 54,constructed = false,index = -1, defaultImplementation = CallReferenceNumberImpl.class)
    private CallReferenceNumber callReferenceNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 55,constructed = false,index = -1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString mscAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 56,constructed = false,index = -1, defaultImplementation = CalledPartyBCDNumberImpl.class)
    private CalledPartyBCDNumber calledPartyBCDNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 57,constructed = false,index = -1, defaultImplementation = TimeAndTimezoneImpl.class)
    private TimeAndTimezone timeAndTimezone;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 58,constructed = false,index = -1)
    private ASNNull callForwardingSSPending;
    
    /**
     * This constructor is only for deserialisation purpose
     */
    public InitialDPRequestBaseImpl() {
    }

    public InitialDPRequestBaseImpl(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered cgEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions,
            HighLayerCompatibilityIsup highLayerCompatibility, DigitsIsup additionalCallingPartyNumber,
            BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, CauseIsup cause,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
            LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
            CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress, CalledPartyBCDNumber calledPartyBCDNumber,
            TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending) {
        
    	this.serviceKey = new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);
    	this.calledPartyNumber = calledPartyNumber;
        this.callingPartyNumber = callingPartyNumber;
        this.callingPartysCategory = callingPartysCategory;
        
        if(cgEncountered!=null)
        	this.cgEncountered = new ASNCGEncountered(cgEncountered);
        	
        this.IPSSPCapabilities = IPSSPCapabilities;
        this.locationNumber = locationNumber;
        this.originalCalledPartyID = originalCalledPartyID;
        this.extensions = extensions;
        this.highLayerCompatibility = highLayerCompatibility;
        this.additionalCallingPartyNumber = additionalCallingPartyNumber;
        
        if(bearerCapability!=null)
        	this.bearerCapability = new BearerCapabilityWrapperImpl(bearerCapability);
        
        if(eventTypeBCSM!=null)
        	this.eventTypeBCSM = new ASNEventTypeBCSM(eventTypeBCSM);
        	
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
    	
        return this.serviceKey.getIntValue();
    }

    @Override
    public CalledPartyNumberIsup getCalledPartyNumber() {
        return this.calledPartyNumber;
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
    public CGEncountered getCGEncountered() {
    	if(cgEncountered==null)
    		return null;
    	
        return cgEncountered.getType();
    }

    @Override
    public IPSSPCapabilities getIPSSPCapabilities() {
        return IPSSPCapabilities;
    }

    @Override
    public LocationNumberIsup getLocationNumber() {
        return locationNumber;
    }

    @Override
    public OriginalCalledNumberIsup getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public HighLayerCompatibilityIsup getHighLayerCompatibility() {
        return highLayerCompatibility;
    }

    @Override
    public DigitsIsup getAdditionalCallingPartyNumber() {
    	if(additionalCallingPartyNumber!=null)
    		additionalCallingPartyNumber.setIsGenericNumber();
    	
        return additionalCallingPartyNumber;
    }

    @Override
    public BearerCapability getBearerCapability() {
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
    public RedirectingPartyIDIsup getRedirectingPartyID() {
        return redirectingPartyID;
    }

    @Override
    public RedirectionInformationIsup getRedirectionInformation() {
        return redirectionInformation;
    }

    @Override
    public CauseIsup getCause() {
        return cause;
    }

    @Override
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    @Override
    public Carrier getCarrier() {
        return carrier;
    }

    @Override
    public CUGIndex getCugIndex() {
        return cugIndex;
    }

    @Override
    public CUGInterlock getCugInterlock() {
        return cugInterlock;
    }

    @Override
    public boolean getCugOutgoingAccess() {
        return cugOutgoingAccess!=null;
    }

    @Override
    public IMSI getIMSI() {
        return imsi;
    }

    @Override
    public SubscriberState getSubscriberState() {
    	if(subscriberState==null)
    		return null;
    	
        return subscriberState.getSubscriberState();
    }

    @Override
    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    @Override
    public ExtBasicServiceCode getExtBasicServiceCode() {
    	if(extBasicServiceCode==null)
    		return null;
    	
        return extBasicServiceCode.getExtBasicServiceCode();
    }

    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return callReferenceNumber;
    }

    @Override
    public ISDNAddressString getMscAddress() {
        return mscAddress;
    }

    @Override
    public CalledPartyBCDNumber getCalledPartyBCDNumber() {
        return calledPartyBCDNumber;
    }

    @Override
    public TimeAndTimezone getTimeAndTimezone() {
        return timeAndTimezone;
    }

    @Override
    public boolean getCallForwardingSSPending() {
        return callForwardingSSPending!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        
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

