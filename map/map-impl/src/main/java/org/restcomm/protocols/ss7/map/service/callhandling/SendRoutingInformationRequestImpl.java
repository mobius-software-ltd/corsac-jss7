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

package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.ASNEMLPPPriorityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.ExtExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ASNInterrogationTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SuppressMTSSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ASNISTSupportIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ASNForwardingReasonImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/*
 *
 * @author cristian veliscu
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendRoutingInformationRequestImpl extends CallHandlingMessageImpl implements SendRoutingInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ISDNAddressStringImpl msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private CUGCheckInfoImpl cugCheckInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNInteger numberOfForwarding;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNInterrogationTypeImpl interrogationType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNNull orInterrogation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNInteger orCapability;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ISDNAddressStringImpl gmscAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private CallReferenceNumberImpl callReferenceNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNForwardingReasonImpl forwardingReason;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1)
    private ExtBasicServiceCodeWrapperImpl basicServiceGroup;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1)
    private ExternalSignalInfoImpl networkSignalInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1)    
    private CamelInfoImpl camelInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)    
    private ASNNull suppressionOfAnnouncement;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1)    
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1)    
    private AlertingPatternImpl alertingPattern;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1)    
    private ASNNull ccbsCall;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=false,index=-1)    
    private ASNInteger supportedCCBSPhase;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=true,index=-1)    
    private ExtExternalSignalInfoImpl additionalSignalInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=false,index=-1)    
    private ASNISTSupportIndicatorImpl istSupportIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=false,index=-1)    
    private ASNNull prePagingSupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1)    
    private CallDiversionTreatmentIndicatorImpl callDiversionTreatmentIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=false,index=-1)    
    private ASNNull longFTNSupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=false,index=-1)    
    private ASNNull suppressVtCSI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=23,constructed=false,index=-1)    
    private ASNNull suppressIncomingCallBarring;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=24,constructed=false,index=-1)    
    private ASNNull gsmSCFInitiatedCall;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=25,constructed=true,index=-1)    
    private ExtBasicServiceCodeWrapperImpl basicServiceGroup2;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=26,constructed=true,index=-1)    
    private ExternalSignalInfoImpl networkSignalInfo2;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=27,constructed=false,index=-1)    
    private SuppressMTSSImpl suppressMTSS;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=28,constructed=false,index=-1)    
    private ASNNull mtRoamingRetrySupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=29,constructed=false,index=-1)    
    private ASNEMLPPPriorityImpl callPriority;
    
    private long mapProtocolVersion;

    public SendRoutingInformationRequestImpl() {
        this(3);
    }

    public SendRoutingInformationRequestImpl(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendRoutingInformationRequestImpl(ISDNAddressStringImpl msisdn, ISDNAddressStringImpl gmscAddress,
            InterrogationType interrogationType, MAPExtensionContainerImpl extensionContainer) {
        this(3, msisdn, gmscAddress, interrogationType, extensionContainer);
    }

    public SendRoutingInformationRequestImpl(long mapProtocolVersion, ISDNAddressStringImpl msisdn, ISDNAddressStringImpl gmscAddress,
            InterrogationType interrogationType, MAPExtensionContainerImpl extensionContainer) {
        this.msisdn = msisdn;
        this.gmscAddress = gmscAddress;
        
        if(interrogationType!=null) {
        	this.interrogationType = new ASNInterrogationTypeImpl();
        	this.interrogationType.setType(interrogationType);
        }
        
        this.extensionContainer = extensionContainer;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendRoutingInformationRequestImpl(long mapProtocolVersion, ISDNAddressStringImpl msisdn, CUGCheckInfoImpl cugCheckInfo,
            Integer numberOfForwarding, InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
            ISDNAddressStringImpl gmscAddress, CallReferenceNumberImpl callReferenceNumber, ForwardingReason forwardingReason,
            ExtBasicServiceCodeImpl basicServiceGroup, ExternalSignalInfoImpl networkSignalInfo, CamelInfoImpl camelInfo,
            boolean suppressionOfAnnouncement, MAPExtensionContainerImpl extensionContainer, AlertingPatternImpl alertingPattern,
            boolean ccbsCall, Integer supportedCCBSPhase, ExtExternalSignalInfoImpl additionalSignalInfo,
            ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
            CallDiversionTreatmentIndicatorImpl callDiversionTreatmentIndicator, boolean longFTNSupported, boolean suppressVtCSI,
            boolean suppressIncomingCallBarring, boolean gsmSCFInitiatedCall, ExtBasicServiceCodeImpl basicServiceGroup2,
            ExternalSignalInfoImpl networkSignalInfo2, SuppressMTSSImpl suppressMTSS, boolean mtRoamingRetrySupported,
            EMLPPPriority callPriority) {

        if (mapProtocolVersion >= 3) {

        	if(orInterrogation)
        		this.orInterrogation = new ASNNull();
        	
        	if(orCapability!=null) {
        		this.orCapability = new ASNInteger();
        		this.orCapability.setValue(orCapability.longValue());
        	}
        	
            this.callReferenceNumber = callReferenceNumber;
            
            if(forwardingReason!=null) {
            	this.forwardingReason = new ASNForwardingReasonImpl();
            	this.forwardingReason.setType(forwardingReason);
            }
            
            if(basicServiceGroup!=null)
            	this.basicServiceGroup = new ExtBasicServiceCodeWrapperImpl(basicServiceGroup);
            
            this.camelInfo = camelInfo;
            
            if(suppressionOfAnnouncement)
            this.suppressionOfAnnouncement = new ASNNull();
            
            this.alertingPattern = alertingPattern;
            
            if(ccbsCall)
            	this.ccbsCall = new ASNNull();
            
            if(supportedCCBSPhase!=null) {
            	this.supportedCCBSPhase = new ASNInteger();
            	this.supportedCCBSPhase.setValue(supportedCCBSPhase.longValue());
            }
            
            this.additionalSignalInfo = additionalSignalInfo;
            
            if(istSupportIndicator!=null) {
            	this.istSupportIndicator = new ASNISTSupportIndicatorImpl();
            	this.istSupportIndicator.setType(istSupportIndicator);
            }
            
            if(prePagingSupported)
            	this.prePagingSupported = new ASNNull();
            
            this.callDiversionTreatmentIndicator = callDiversionTreatmentIndicator;
            
            if(longFTNSupported)
            	this.longFTNSupported = new ASNNull();
            
            if(suppressVtCSI)
            	this.suppressVtCSI = new ASNNull();
            
            if(suppressIncomingCallBarring)
            	this.suppressIncomingCallBarring = new ASNNull();
            
            if(gsmSCFInitiatedCall)
            	this.gsmSCFInitiatedCall = new ASNNull();
            
            if(basicServiceGroup2!=null)
            	this.basicServiceGroup2 = new ExtBasicServiceCodeWrapperImpl(basicServiceGroup2);
            
            this.networkSignalInfo2 = networkSignalInfo2;
            this.suppressMTSS = suppressMTSS;
            
            if(mtRoamingRetrySupported)
            	this.mtRoamingRetrySupported = new ASNNull();
            
            if(callPriority!=null) {
            	this.callPriority = new ASNEMLPPPriorityImpl();
            	this.callPriority.setType(callPriority);
            }
            
            if(interrogationType!=null) {
            	this.interrogationType = new ASNInterrogationTypeImpl();
            	this.interrogationType.setType(interrogationType);
            }
            
            this.gmscAddress = gmscAddress;
            this.extensionContainer = extensionContainer;
        }

        this.msisdn = msisdn;
        
        if(mapProtocolVersion>=2)
        	this.cugCheckInfo = cugCheckInfo;
        
        if(numberOfForwarding!=null) {
        	this.numberOfForwarding = new ASNInteger();
        	this.numberOfForwarding.setValue(numberOfForwarding.longValue());
        }
        
        this.networkSignalInfo = networkSignalInfo;
        this.mapProtocolVersion = mapProtocolVersion;

    }

    public long getMapProtocolVersion() {
        return mapProtocolVersion;
    }

    @Override
    public ISDNAddressStringImpl getMsisdn() {
        return this.msisdn;
    }

    @Override
    public CUGCheckInfoImpl getCUGCheckInfo() {
        return this.cugCheckInfo;
    }

    @Override
    public Integer getNumberOfForwarding() {
    	if(this.numberOfForwarding==null)
    		return null;
    	
        return this.numberOfForwarding.getValue().intValue();
    }

    @Override
    public InterrogationType getInterogationType() {
    	if(this.interrogationType==null)
    		return null;
    	
        return this.interrogationType.getType();
    }

    @Override
    public boolean getORInterrogation() {    	
        return this.orInterrogation!=null;
    }

    @Override
    public Integer getORCapability() {
    	if(this.orCapability==null)
    		return null;
    	
        return this.orCapability.getValue().intValue();
    }

    @Override
    public ISDNAddressStringImpl getGmscOrGsmSCFAddress() {
        return this.gmscAddress;
    }

    @Override
    public CallReferenceNumberImpl getCallReferenceNumber() {
        return this.callReferenceNumber;
    }

    @Override
    public ForwardingReason getForwardingReason() {
    	if(this.forwardingReason==null)
    		return null;
    	
        return this.forwardingReason.getType();
    }

    @Override
    public ExtBasicServiceCodeImpl getBasicServiceGroup() {
    	if(this.basicServiceGroup==null)
    		return null;
    	
        return this.basicServiceGroup.getExtBasicServiceCode();
    }

    @Override
    public ExternalSignalInfoImpl getNetworkSignalInfo() {
        return this.networkSignalInfo;
    }

    @Override
    public CamelInfoImpl getCamelInfo() {
        return this.camelInfo;
    }

    @Override
    public boolean getSuppressionOfAnnouncement() {
        return this.suppressionOfAnnouncement!=null;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public AlertingPatternImpl getAlertingPattern() {
        return this.alertingPattern;
    }

    @Override
    public boolean getCCBSCall() {
        return this.ccbsCall!=null;
    }

    @Override
    public Integer getSupportedCCBSPhase() {
    	if(this.supportedCCBSPhase==null)
    		return null;
    	
        return this.supportedCCBSPhase.getValue().intValue();
    }

    @Override
    public ExtExternalSignalInfoImpl getAdditionalSignalInfo() {
        return this.additionalSignalInfo;
    }

    @Override
    public ISTSupportIndicator getIstSupportIndicator() {
    	if(this.istSupportIndicator==null)
    		return null;
    	
        return this.istSupportIndicator.getType();
    }

    @Override
    public boolean getPrePagingSupported() {
        return this.prePagingSupported!=null;
    }

    @Override
    public CallDiversionTreatmentIndicatorImpl getCallDiversionTreatmentIndicator() {
        return this.callDiversionTreatmentIndicator;
    }

    @Override
    public boolean getLongFTNSupported() {
        return this.longFTNSupported!=null;
    }

    @Override
    public boolean getSuppressVtCSI() {
        return this.suppressVtCSI!=null;
    }

    @Override
    public boolean getSuppressIncomingCallBarring() {
        return this.suppressIncomingCallBarring!=null;
    }

    @Override
    public boolean getGsmSCFInitiatedCall() {
        return this.gsmSCFInitiatedCall!=null;
    }

    @Override
    public ExtBasicServiceCodeImpl getBasicServiceGroup2() {
    	if(this.basicServiceGroup2==null)
    		return null;
    	
        return this.basicServiceGroup2.getExtBasicServiceCode();
    }

    @Override
    public ExternalSignalInfoImpl getNetworkSignalInfo2() {
        return this.networkSignalInfo2;
    }

    @Override
    public SuppressMTSSImpl getSuppressMTSS() {
        return this.suppressMTSS;
    }

    @Override
    public boolean getMTRoamingRetrySupported() {
        return this.mtRoamingRetrySupported!=null;
    }

    @Override
    public EMLPPPriority getCallPriority() {
    	if(this.callPriority==null)
    		return null;
    	
        return this.callPriority.getType();
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfo_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendRoutingInformationRequest [");

        sb.append("mapProtocolVersion=");
        sb.append(mapProtocolVersion);

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn);
        }

        if (this.cugCheckInfo != null) {
            sb.append(", cugCheckInfo=");
            sb.append(this.cugCheckInfo);
        }

        if (this.numberOfForwarding != null) {
            sb.append(", numberOfForwarding=");
            sb.append(this.numberOfForwarding.getValue());
        }

        if (this.interrogationType != null) {
            sb.append(", interrogationType=");
            sb.append(this.interrogationType.getType());
        }

        if (this.orInterrogation != null)
            sb.append(", orInterrogation=TRUE");

        if (this.orCapability != null) {
            sb.append(", orCapability=");
            sb.append(this.orCapability.getValue());
        }

        if (this.gmscAddress != null) {
            sb.append(", gmscAddress=");
            sb.append(this.gmscAddress);
        }

        if (this.callReferenceNumber != null) {
            sb.append(", callReferenceNumber=");
            sb.append(this.callReferenceNumber);
        }

        if (this.forwardingReason != null) {
            sb.append(", forwardingReason=");
            sb.append(this.forwardingReason.getType());
        }

        if (this.basicServiceGroup != null && this.basicServiceGroup.getExtBasicServiceCode()!=null) {
            sb.append(", basicServiceGroup=");
            sb.append(this.basicServiceGroup.getExtBasicServiceCode());
        }

        if (this.networkSignalInfo != null) {
            sb.append(", networkSignalInfo=");
            sb.append(this.networkSignalInfo);
        }

        if (this.camelInfo != null) {
            sb.append(", camelInfo=");
            sb.append(this.camelInfo);
        }

        if (this.suppressionOfAnnouncement != null)
            sb.append(", suppressionOfAnnouncement=TRUE");

        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        if (this.alertingPattern != null) {
            sb.append(", alertingPattern=");
            sb.append(this.alertingPattern);
        }

        if (this.ccbsCall != null)
            sb.append(", ccbsCall=TRUE");

        if (this.supportedCCBSPhase != null) {
            sb.append(", supportedCCBSPhase=");
            sb.append(this.supportedCCBSPhase.getValue());
        }

        if (this.additionalSignalInfo != null) {
            sb.append(", additionalSignalInfo=");
            sb.append(this.additionalSignalInfo);
        }

        if (this.istSupportIndicator != null) {
            sb.append(", istSupportIndicator=");
            sb.append(this.istSupportIndicator.getValue());
        }

        if (this.prePagingSupported != null)
            sb.append(", prePagingSupportedr=TRUE");

        if (this.callDiversionTreatmentIndicator != null) {
            sb.append(", callDiversionTreatmentIndicator=");
            sb.append(this.callDiversionTreatmentIndicator);
        }

        if (this.longFTNSupported != null)
            sb.append(", longFTNSupported=TRUE");

        if (this.suppressVtCSI != null)
            sb.append(", suppressVtCSI=TRUE");

        if (this.suppressIncomingCallBarring != null)
            sb.append(", suppressIncomingCallBarring=TRUE");

        if (this.gsmSCFInitiatedCall != null)
            sb.append(", gsmSCFInitiatedCall=TRUE");

        if (this.basicServiceGroup2 != null && this.basicServiceGroup2.getExtBasicServiceCode()!=null) {
            sb.append(", basicServiceGroup2=");
            sb.append(this.basicServiceGroup2.getExtBasicServiceCode());
        }

        if (this.networkSignalInfo2 != null) {
            sb.append(", networkSignalInfo2=");
            sb.append(this.networkSignalInfo2);
        }

        if (this.suppressMTSS != null) {
            sb.append(", suppressMTSS=");
            sb.append(this.suppressMTSS);
        }

        if (this.mtRoamingRetrySupported != null)
            sb.append(", mtRoamingRetrySupported=TRUE");

        if (this.callPriority != null) {
            sb.append(", callPriority=");
            sb.append(this.callPriority.getType());
        }

        sb.append("]");
        return sb.toString();
    }
}