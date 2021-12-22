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

import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeWrapperImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SuppressMTSS;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.restcomm.protocols.ss7.map.primitives.ASNEMLPPPriorityImpl;
import org.restcomm.protocols.ss7.map.primitives.ExtExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.ASNISTSupportIndicatorImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ASNForwardingReasonImpl;

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

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = CUGCheckInfoImpl.class)
    private CUGCheckInfo cugCheckInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNInteger numberOfForwarding;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNInterrogationTypeImpl interrogationType;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNNull orInterrogation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNInteger orCapability;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gmscAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1, defaultImplementation = CallReferenceNumberImpl.class)
    private CallReferenceNumber callReferenceNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNForwardingReasonImpl forwardingReason;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1)
    private ExtBasicServiceCodeWrapperImpl basicServiceGroup;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1, defaultImplementation = ExternalSignalInfoImpl.class)
    private ExternalSignalInfo networkSignalInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1, defaultImplementation = CamelInfoImpl.class)    
    private CamelInfo camelInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)    
    private ASNNull suppressionOfAnnouncement;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)    
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1, defaultImplementation = AlertingPatternImpl.class)    
    private AlertingPattern alertingPattern;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1)    
    private ASNNull ccbsCall;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=false,index=-1)    
    private ASNInteger supportedCCBSPhase;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=true,index=-1, defaultImplementation = ExtExternalSignalInfoImpl.class)    
    private ExtExternalSignalInfo additionalSignalInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=false,index=-1)    
    private ASNISTSupportIndicatorImpl istSupportIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=false,index=-1)    
    private ASNNull prePagingSupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1, defaultImplementation = CallDiversionTreatmentIndicatorImpl.class)    
    private CallDiversionTreatmentIndicator callDiversionTreatmentIndicator;
    
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
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=26,constructed=true,index=-1, defaultImplementation = ExternalSignalInfoImpl.class)    
    private ExternalSignalInfo networkSignalInfo2;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=27,constructed=false,index=-1, defaultImplementation = SuppressMTSSImpl.class)    
    private SuppressMTSS suppressMTSS;
    
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

    public SendRoutingInformationRequestImpl(ISDNAddressString msisdn, ISDNAddressString gmscAddress,
            InterrogationType interrogationType, MAPExtensionContainer extensionContainer) {
        this(3, msisdn, gmscAddress, interrogationType, extensionContainer);
    }

    public SendRoutingInformationRequestImpl(long mapProtocolVersion, ISDNAddressString msisdn, ISDNAddressString gmscAddress,
            InterrogationType interrogationType, MAPExtensionContainer extensionContainer) {
        this.msisdn = msisdn;
        this.gmscAddress = gmscAddress;
        
        if(interrogationType!=null) {
        	this.interrogationType = new ASNInterrogationTypeImpl();
        	this.interrogationType.setType(interrogationType);
        }
        
        this.extensionContainer = extensionContainer;
        this.mapProtocolVersion = mapProtocolVersion;
    }

    public SendRoutingInformationRequestImpl(long mapProtocolVersion, ISDNAddressString msisdn, CUGCheckInfo cugCheckInfo,
            Integer numberOfForwarding, InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
            ISDNAddressString gmscAddress, CallReferenceNumber callReferenceNumber, ForwardingReason forwardingReason,
            ExtBasicServiceCode basicServiceGroup, ExternalSignalInfo networkSignalInfo, CamelInfo camelInfo,
            boolean suppressionOfAnnouncement, MAPExtensionContainer extensionContainer, AlertingPattern alertingPattern,
            boolean ccbsCall, Integer supportedCCBSPhase, ExtExternalSignalInfo additionalSignalInfo,
            ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, boolean longFTNSupported, boolean suppressVtCSI,
            boolean suppressIncomingCallBarring, boolean gsmSCFInitiatedCall, ExtBasicServiceCode basicServiceGroup2,
            ExternalSignalInfo networkSignalInfo2, SuppressMTSS suppressMTSS, boolean mtRoamingRetrySupported,
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
    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    @Override
    public CUGCheckInfo getCUGCheckInfo() {
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
    public ISDNAddressString getGmscOrGsmSCFAddress() {
        return this.gmscAddress;
    }

    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return this.callReferenceNumber;
    }

    @Override
    public ForwardingReason getForwardingReason() {
    	if(this.forwardingReason==null)
    		return null;
    	
        return this.forwardingReason.getType();
    }

    @Override
    public ExtBasicServiceCode getBasicServiceGroup() {
    	if(this.basicServiceGroup==null)
    		return null;
    	
        return this.basicServiceGroup.getExtBasicServiceCode();
    }

    @Override
    public ExternalSignalInfo getNetworkSignalInfo() {
        return this.networkSignalInfo;
    }

    @Override
    public CamelInfo getCamelInfo() {
        return this.camelInfo;
    }

    @Override
    public boolean getSuppressionOfAnnouncement() {
        return this.suppressionOfAnnouncement!=null;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public AlertingPattern getAlertingPattern() {
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
    public ExtExternalSignalInfo getAdditionalSignalInfo() {
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
    public CallDiversionTreatmentIndicator getCallDiversionTreatmentIndicator() {
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
    public ExtBasicServiceCode getBasicServiceGroup2() {
    	if(this.basicServiceGroup2==null)
    		return null;
    	
        return this.basicServiceGroup2.getExtBasicServiceCode();
    }

    @Override
    public ExternalSignalInfo getNetworkSignalInfo2() {
        return this.networkSignalInfo2;
    }

    @Override
    public SuppressMTSS getSuppressMTSS() {
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