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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardGVNSIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ASNCGEncountered;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.ForwardGVNSIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.HighLayerCompatibilityIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectionInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNEventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.primitives.TerminalType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardGVNS;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGCallIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGInterLockCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericDigitsSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericNumbersSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.HandOverInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.LegIDs;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.RouteOrigin;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.IPAvailable;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceProfileIdentifier;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.TriggerType;
import org.restcomm.protocols.ss7.inap.primitives.ASNTerminalType;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.CUGCallIndicatorImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.CUGInterLockCodeImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GenericDigitsSetImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GenericNumbersSetImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.HandOverInfoImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.LegIDsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.RouteOriginImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ASNTriggerType;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CallingPartyBusinessGroupIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CallingPartySubaddressImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.IPAvailableImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceInteractionIndicatorsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceProfileIdentifierImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.BackwardGVNSImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.ForwardCallIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;

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
public class InitialDPRequestImpl extends CircuitSwitchedCallMessageImpl implements InitialDPRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = 0)
    private ASNInteger serviceKey;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = CallingPartyNumberIsupImpl.class)
    private CallingPartyNumberIsup dialedDigits;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1, defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup calledPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1, defaultImplementation = CallingPartyNumberIsupImpl.class)
    private CallingPartyNumberIsup callingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1, defaultImplementation = CallingPartyBusinessGroupIDImpl.class)
    private CallingPartyBusinessGroupID callingPartyBusinessGroupID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1, defaultImplementation = CallingPartysCategoryIsupImpl.class)
    private CallingPartysCategoryIsup callingPartysCategory;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1, defaultImplementation = CallingPartySubaddressImpl.class)
    private CallingPartySubaddress callingPartySubaddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1)
    private ASNCGEncountered cgEncountered;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1, defaultImplementation = IPSSPCapabilitiesImpl.class)
    private IPSSPCapabilities ipsspCapabilities;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1, defaultImplementation = IPAvailableImpl.class)
    private IPAvailable ipAvailable;
        
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false,index = -1, defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup locationNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false,index = -1, defaultImplementation = MiscCallInfoImpl.class)
    private MiscCallInfo miscCallInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = false,index = -1, defaultImplementation = OriginalCalledNumberIsupImpl.class)
    private OriginalCalledNumberIsup originalCalledPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = false,index = -1, defaultImplementation = ServiceProfileIdentifierImpl.class)
    private ServiceProfileIdentifier serviceProfileIdentifier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = false,index = -1)
    private ASNTerminalType terminalType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 16,constructed = false,index = -1)
    private ASNTriggerType triggerType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 23,constructed = false,index = -1, defaultImplementation = HighLayerCompatibilityIsupImpl.class)
    private HighLayerCompatibilityIsup highLayerCompatibility;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 24,constructed = false,index = -1, defaultImplementation = ServiceInteractionIndicatorsImpl.class)
    private ServiceInteractionIndicators serviceInteractionIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 25,constructed = false,index = -1, defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup additionalCallingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 26,constructed = false,index = -1, defaultImplementation = ForwardCallIndicatorsImpl.class)
    private ForwardCallIndicators forwardCallIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 27,constructed = true,index = -1)
    private BearerCapabilityWrapperImpl bearerCapability;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 28,constructed = false,index = -1)
    private ASNEventTypeBCSM eventTypeBCSM;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29,constructed = false,index = -1,defaultImplementation = RedirectingPartyIDIsupImpl.class)
    private RedirectingPartyIDIsup redirectingPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false,index = -1, defaultImplementation = RedirectionInformationIsupImpl.class)
    private RedirectionInformationIsup redirectionInformation;

    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = false,index = -1, defaultImplementation = LegIDsImpl.class)
	private LegIDs legIDs;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 2,constructed = false,index = -1, defaultImplementation = RouteOriginImpl.class)
	private RouteOrigin routeOrigin;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 3,constructed = false,index = -1)
	private ASNNull testIndication;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 4,constructed = false,index = -1, defaultImplementation = CUGCallIndicatorImpl.class)
	private CUGCallIndicator cugCallIndicator;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 5,constructed = false,index = -1, defaultImplementation = CUGInterLockCodeImpl.class)
	private CUGInterLockCode cugInterLockCode;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 6,constructed = true,index = -1, defaultImplementation = GenericDigitsSetImpl.class)
	private GenericDigitsSet genericDigitsSet;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 7,constructed = true,index = -1, defaultImplementation = GenericNumbersSetImpl.class)
	private GenericNumbersSet genericNumberSet;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 8,constructed = false,index = -1, defaultImplementation = CauseIsupImpl.class)
	private CauseIsup cause;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 9,constructed = true,index = -1, defaultImplementation = HandOverInfoImpl.class)
	private HandOverInfo handOverInfo;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 10,constructed = false,index = -1, defaultImplementation = ForwardGVNSIsupImpl.class)
	private ForwardGVNSIsup forwardGVNSIndicator;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 10,constructed = false,index = -1, defaultImplementation = BackwardGVNSImpl.class)
	private BackwardGVNS backwardGVNSIndicator;
    
	/**
     * This constructor is only for deserialisation purpose
     */
    public InitialDPRequestImpl() {
    }

    public InitialDPRequestImpl(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities,LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup originalCalledPartyID,CAPINAPExtensions extensions,TriggerType triggerType,
            HighLayerCompatibilityIsup highLayerCompatibility,ServiceInteractionIndicators serviceInteractionIndicators, 
            DigitsIsup additionalCallingPartyNumber,ForwardCallIndicators forwardCallIndicators,BearerCapability bearerCapability, 
            EventTypeBCSM eventTypeBCSM,  RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
            LegIDs legIDs,RouteOrigin routeOrigin,boolean testIndication,CUGCallIndicator cugCallIndicator,
            CUGInterLockCode cugInterLockCode,GenericDigitsSet genericDigitsSet,GenericNumbersSet genericNumberSet,
            CauseIsup cause,HandOverInfo handOverInfo,ForwardGVNSIsup forwardGVNSIndicator,BackwardGVNS backwardGVNSIndicator) {
    	this(serviceKey, callingPartyNumber, calledPartyNumber, callingPartyNumber, null, callingPartysCategory, 
    			null, cgEncountered, ipsspCapabilities, null, locationNumber, null, originalCalledPartyID, null, null, 
    			extensions, triggerType, highLayerCompatibility, serviceInteractionIndicators, additionalCallingPartyNumber, 
    			forwardCallIndicators,bearerCapability, eventTypeBCSM, redirectingPartyID, redirectionInformation);
    	
    	this.legIDs=legIDs;
    	this.routeOrigin=routeOrigin;
    	
    	if(testIndication)
    		this.testIndication=new ASNNull();
    	
    	this.cugCallIndicator=cugCallIndicator;
    	this.cugInterLockCode=cugInterLockCode;
    	this.genericDigitsSet=genericDigitsSet;
    	this.genericNumberSet=genericNumberSet;
    	this.cause=cause;
    	this.handOverInfo=handOverInfo;
    	this.forwardGVNSIndicator=forwardGVNSIndicator;
    	this.backwardGVNSIndicator=backwardGVNSIndicator;
    }
    
    public InitialDPRequestImpl(int serviceKey, CallingPartyNumberIsup dialedDigits, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber, CallingPartyBusinessGroupID callingPartyBusinessGroupID,
            CallingPartysCategoryIsup callingPartysCategory,CallingPartySubaddress callingPartySubaddress,
            CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities, IPAvailable ipAvailable,
            LocationNumberIsup locationNumber,MiscCallInfo miscCallInfo, OriginalCalledNumberIsup originalCalledPartyID, 
            ServiceProfileIdentifier serviceProfileIdentifier,TerminalType terminalType,CAPINAPExtensions extensions,
            TriggerType triggerType,HighLayerCompatibilityIsup highLayerCompatibility,ServiceInteractionIndicators serviceInteractionIndicators, 
            DigitsIsup additionalCallingPartyNumber,ForwardCallIndicators forwardCallIndicators,BearerCapability bearerCapability, 
            EventTypeBCSM eventTypeBCSM, RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation) {
        
    	this.serviceKey = new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);
    	
    	this.dialedDigits=dialedDigits;
        this.calledPartyNumber = calledPartyNumber;
        this.callingPartyNumber = callingPartyNumber;
        this.callingPartyBusinessGroupID=callingPartyBusinessGroupID;
        this.callingPartysCategory = callingPartysCategory;
        this.callingPartySubaddress=callingPartySubaddress;
        
        if(cgEncountered!=null)
        	this.cgEncountered = new ASNCGEncountered(cgEncountered);
        	
        this.ipsspCapabilities = ipsspCapabilities;
        this.ipAvailable=ipAvailable;
        this.locationNumber = locationNumber;
        this.miscCallInfo=miscCallInfo;
        this.originalCalledPartyID = originalCalledPartyID;
        this.serviceProfileIdentifier=serviceProfileIdentifier;
        
        if(terminalType!=null)
        	this.terminalType = new ASNTerminalType(terminalType);
        	
        this.extensions = extensions;
        
        if(triggerType!=null)
        	this.triggerType = new ASNTriggerType(triggerType);
        	
        this.highLayerCompatibility = highLayerCompatibility;
        this.serviceInteractionIndicators=serviceInteractionIndicators;
        this.additionalCallingPartyNumber = additionalCallingPartyNumber;
        
        if(bearerCapability!=null)
        	this.bearerCapability = new BearerCapabilityWrapperImpl(bearerCapability);
        
        if(eventTypeBCSM!=null)
        	this.eventTypeBCSM = new ASNEventTypeBCSM(eventTypeBCSM);
        	
        this.redirectingPartyID = redirectingPartyID;
        this.redirectionInformation = redirectionInformation;        
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.initialDP_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.initialDP;
    }

    @Override
    public int getServiceKey() {
    	if(this.serviceKey==null || this.serviceKey.getValue()==null)
    		return 0;
    	
        return this.serviceKey.getIntValue();
    }

    @Override
    public CallingPartyNumberIsup getDialedDigits() {
		return dialedDigits;
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
    public CallingPartyBusinessGroupID getCallingPartyBusinessGroupID() {
		return callingPartyBusinessGroupID;
	}

    @Override
    public CallingPartysCategoryIsup getCallingPartysCategory() {
        return callingPartysCategory;
    }

    @Override
    public CallingPartySubaddress getCallingPartySubaddress() {
		return callingPartySubaddress;
	}

	@Override
    public CGEncountered getCGEncountered() {
    	if(cgEncountered==null)
    		return null;
    	
        return cgEncountered.getType();
    }

    @Override
    public IPSSPCapabilities getIPSSPCapabilities() {
        return ipsspCapabilities;
    }
    
    @Override
    public IPAvailable getIPAvailable() {
		return ipAvailable;
	}

    @Override
    public LocationNumberIsup getLocationNumber() {
        return locationNumber;
    }

    @Override
    public MiscCallInfo getMiscCallInfo() {
		return miscCallInfo;
	}

    @Override
    public OriginalCalledNumberIsup getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    @Override
    public ServiceProfileIdentifier getServiceProfileIdentifier() {
		return serviceProfileIdentifier;
	}

    @Override
    public TerminalType getTerminalType() {
    	if(terminalType==null)
    		return null;
    	
		return terminalType.getType();
	}

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public TriggerType getTriggerType() {
    	if(triggerType==null)
    		return null;
    	
		return triggerType.getType();
	}

    @Override
    public HighLayerCompatibilityIsup getHighLayerCompatibility() {
        return highLayerCompatibility;
    }

    @Override
    public ServiceInteractionIndicators getServiceInteractionIndicators() {
		return serviceInteractionIndicators;
	}

	@Override
    public DigitsIsup getAdditionalCallingPartyNumber() {
    	if(additionalCallingPartyNumber!=null)
    		additionalCallingPartyNumber.setIsGenericNumber();
    	
        return additionalCallingPartyNumber;
    }

    @Override
    public ForwardCallIndicators getForwardCallIndicators() {
    	return forwardCallIndicators;
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
    public LegIDs getLegIDs() {
		return legIDs;
	}

    @Override
    public RouteOrigin getRouteOrigin() {
		return routeOrigin;
	}

	@Override
    public boolean getTestIndication() {
		return testIndication!=null;
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
    public GenericDigitsSet getGenericDigitsSet() {
		return genericDigitsSet;
	}

	@Override
    public GenericNumbersSet getGenericNumberSet() {
		return genericNumberSet;
	}

	@Override
    public CauseIsup getCause() {
		return cause;
	}

	@Override
    public HandOverInfo getHandOverInfo() {
		return handOverInfo;
	}

	@Override
    public ForwardGVNSIsup getForwardGVNSIndicator() {
		return forwardGVNSIndicator;
	}

	@Override
    public BackwardGVNS getBackwardGVNSIndicator() {
		return backwardGVNSIndicator;
	}

	@Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append("InitialDPRequestIndication [");
        this.addInvokeIdInfo(sb);

        toStringInternal(sb);

        sb.append("]");

        return sb.toString();
	}
	
	public void toStringInternal(StringBuilder sb) {
        sb.append(", serviceKey=");
        sb.append(serviceKey.getValue());
        if (this.dialedDigits != null) {
            sb.append(", dialedDigits=");
            sb.append(dialedDigits.toString());
        }
        if (this.calledPartyNumber != null) {
            sb.append(", calledPartyNumber=");
            sb.append(calledPartyNumber.toString());
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber.toString());
        }
        if (this.callingPartyBusinessGroupID != null) {
            sb.append(", callingPartyBusinessGroupID=");
            sb.append(callingPartyBusinessGroupID.toString());
        }
        if (this.callingPartysCategory != null) {
            sb.append(", callingPartysCategory=");
            sb.append(callingPartysCategory.toString());
        }
        if (this.callingPartySubaddress != null) {
            sb.append(", callingPartySubaddress=");
            sb.append(callingPartySubaddress.toString());
        }
        if (this.cgEncountered != null && this.cgEncountered.getType()!=null) {
            sb.append(", CGEncountered=");
            sb.append(cgEncountered.getType());
        }
        if (this.ipsspCapabilities != null) {
            sb.append(", IPSSPCapabilities=");
            sb.append(ipsspCapabilities.toString());
        }
        if (this.ipAvailable != null) {
            sb.append(", IPAvailable=");
            sb.append(ipAvailable.toString());
        }
        if (this.locationNumber != null) {
            sb.append(", locationNumber=");
            sb.append(locationNumber.toString());
        }
        if (this.miscCallInfo != null) {
            sb.append(", miscCallInfo=");
            sb.append(miscCallInfo.toString());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.serviceProfileIdentifier != null) {
            sb.append(", serviceProfileIdentifier=");
            sb.append(serviceProfileIdentifier.toString());
        }
        if (this.terminalType != null && this.terminalType.getType()!=null) {
            sb.append(", TerminalType=");
            sb.append(terminalType.getType());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.triggerType != null && this.triggerType.getType()!=null) {
            sb.append(", TriggerType=");
            sb.append(triggerType.getType());
        }
        if (this.highLayerCompatibility != null) {
            sb.append(", highLayerCompatibility=");
            sb.append(highLayerCompatibility.toString());
        }
        if (this.serviceInteractionIndicators != null) {
            sb.append(", serviceInteractionIndicators=");
            sb.append(serviceInteractionIndicators.toString());
        }
        if (this.additionalCallingPartyNumber != null) {
            sb.append(", additionalCallingPartyNumber=");
            sb.append(additionalCallingPartyNumber.toString());
        }
        if (this.forwardCallIndicators != null) {
            sb.append(", forwardCallIndicators=");
            sb.append(forwardCallIndicators.toString());
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
        if (this.legIDs != null) {
            sb.append(", legIDs=");
            sb.append(legIDs.toString());
        }
        if (this.routeOrigin != null) {
            sb.append(", routeOrigin=");
            sb.append(routeOrigin.toString());
        }
        if (this.testIndication != null) {
            sb.append(", testIndication");            
        }
        if (this.cugCallIndicator != null) {
            sb.append(", cugCallIndicator=");
            sb.append(cugCallIndicator.toString());
        }
        if (this.cugInterLockCode != null) {
            sb.append(", cugInterLockCode=");
            sb.append(cugInterLockCode.toString());
        }
        if (this.genericDigitsSet != null) {
            sb.append(", genericDigitsSet=");
            sb.append(genericDigitsSet.toString());
        }
        if (this.genericNumberSet != null) {
            sb.append(", genericNumberSet=");
            sb.append(genericNumberSet.toString());
        }
        if (this.cause != null) {
            sb.append(", cause=");
            sb.append(cause.toString());
        }
        if (this.handOverInfo != null) {
            sb.append(", handOverInfo=");
            sb.append(handOverInfo.toString());
        }
        if (this.forwardGVNSIndicator != null) {
            sb.append(", forwardGVNSIndicator=");
            sb.append(forwardGVNSIndicator.toString());
        }
        if (this.backwardGVNSIndicator != null) {
            sb.append(", backwardGVNSIndicator=");
            sb.append(backwardGVNSIndicator.toString());
        }
    }
}