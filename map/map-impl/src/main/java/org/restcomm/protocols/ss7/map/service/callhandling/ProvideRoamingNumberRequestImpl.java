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
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.primitives.ASNEMLPPPriorityImpl;
import org.restcomm.protocols.ss7.map.primitives.ExtExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PagingAreaImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ProvideRoamingNumberRequestImpl extends CallHandlingMessageImpl implements ProvideRoamingNumberRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1,defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString mscNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = LMSIImpl.class)
    private LMSI lmsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1, defaultImplementation = ExternalSignalInfoImpl.class)
    private ExternalSignalInfo gsmBearerCapability;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1, defaultImplementation = ExternalSignalInfoImpl.class)
    private ExternalSignalInfo networkSignalInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull suppressionOfAnnouncement;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gmscAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1, defaultImplementation = CallReferenceNumberImpl.class)
    private CallReferenceNumber callReferenceNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private ASNNull orInterrogation;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1, defaultImplementation = AlertingPatternImpl.class)
    private AlertingPattern alertingPattern;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=false,index=-1)
    private ASNNull ccbsCall;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=15,constructed=false,index=-1, defaultImplementation = SupportedCamelPhasesImpl.class)
    private SupportedCamelPhases supportedCamelPhasesInInterrogatingNode;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=true,index=-1, defaultImplementation = ExtExternalSignalInfoImpl.class)
    private ExtExternalSignalInfo additionalSignalInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=false,index=-1)
    private ASNNull orNotSupportedInGMSC;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=17,constructed=false,index=-1)
    private ASNNull prePagingSupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=18,constructed=false,index=-1)
    private ASNNull longFTNSupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=19,constructed=false,index=-1)
    private ASNNull suppressVtCsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=20,constructed=false,index=-1, defaultImplementation = OfferedCamel4CSIsImpl.class)
    private OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=21,constructed=false,index=-1)
    private ASNNull mtRoamingRetrySupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=22,constructed=true,index=-1, defaultImplementation = PagingAreaImpl.class)
    private PagingArea pagingArea;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=23,constructed=false,index=-1)
    private ASNEMLPPPriorityImpl callPriority;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=24,constructed=false,index=-1)
    private ASNNull mtrfIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=25,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString oldMSCNumber;
    
    public ProvideRoamingNumberRequestImpl() {    	
    }
    
    public ProvideRoamingNumberRequestImpl(IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString msisdn, LMSI lmsi,
    		ExternalSignalInfo gsmBearerCapability, ExternalSignalInfo networkSignalInfo, boolean suppressionOfAnnouncement,
            ISDNAddressString gmscAddress, CallReferenceNumber callReferenceNumber, boolean orInterrogation,
            MAPExtensionContainer extensionContainer, AlertingPattern alertingPattern, boolean ccbsCall,
            SupportedCamelPhases supportedCamelPhasesInInterrogatingNode, ExtExternalSignalInfo additionalSignalInfo,
            boolean orNotSupportedInGMSC, boolean prePagingSupported, boolean longFTNSupported, boolean suppressVtCsi,
            OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode, boolean mtRoamingRetrySupported, PagingArea pagingArea,
            EMLPPPriority callPriority, boolean mtrfIndicator, ISDNAddressString oldMSCNumber) {
        super();
        this.imsi = imsi;
        this.mscNumber = mscNumber;
        this.msisdn = msisdn;
        this.lmsi = lmsi;
        this.gsmBearerCapability = gsmBearerCapability;
        this.networkSignalInfo = networkSignalInfo;
        
        if(suppressionOfAnnouncement)
        	this.suppressionOfAnnouncement = new ASNNull();
        
        this.gmscAddress = gmscAddress;
        this.callReferenceNumber = callReferenceNumber;
        
        if(orInterrogation)
        	this.orInterrogation = new ASNNull();
        
        this.extensionContainer = extensionContainer;
        this.alertingPattern = alertingPattern;
        
        if(ccbsCall)
        	this.ccbsCall = new ASNNull();
        
        this.supportedCamelPhasesInInterrogatingNode = supportedCamelPhasesInInterrogatingNode;
        this.additionalSignalInfo = additionalSignalInfo;
        
        if(orNotSupportedInGMSC)
        	this.orNotSupportedInGMSC = new ASNNull();
        
        if(prePagingSupported)
        	this.prePagingSupported = new ASNNull();
        
        if(longFTNSupported)
        	this.longFTNSupported = new ASNNull();
        
        if(suppressVtCsi)
        	this.suppressVtCsi = new ASNNull();
        
        this.offeredCamel4CSIsInInterrogatingNode = offeredCamel4CSIsInInterrogatingNode;
        
        if(mtRoamingRetrySupported)
        	this.mtRoamingRetrySupported = new ASNNull();
        
        this.pagingArea = pagingArea;
        
        if(callPriority!=null)
        	this.callPriority = new ASNEMLPPPriorityImpl(callPriority);
        	
        if(mtrfIndicator)
        	this.mtrfIndicator = new ASNNull();
        
        this.oldMSCNumber = oldMSCNumber;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.provideRoamingNumber_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.provideRoamingNumber;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public ISDNAddressString getMscNumber() {
        return this.mscNumber;
    }

    @Override
    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    @Override
    public LMSI getLmsi() {
        return this.lmsi;
    }

    @Override
    public ExternalSignalInfo getGsmBearerCapability() {
        return this.gsmBearerCapability;
    }

    @Override
    public ExternalSignalInfo getNetworkSignalInfo() {
        return this.networkSignalInfo;
    }

    @Override
    public boolean getSuppressionOfAnnouncement() {
        return this.suppressionOfAnnouncement!=null;
    }

    @Override
    public ISDNAddressString getGmscAddress() {
        return this.gmscAddress;
    }

    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return this.callReferenceNumber;
    }

    @Override
    public boolean getOrInterrogation() {
        return this.orInterrogation!=null;
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
    public boolean getCcbsCall() {
        return this.ccbsCall!=null;
    }

    @Override
    public SupportedCamelPhases getSupportedCamelPhasesInInterrogatingNode() {
        return this.supportedCamelPhasesInInterrogatingNode;
    }

    @Override
    public ExtExternalSignalInfo getAdditionalSignalInfo() {
        return this.additionalSignalInfo;
    }

    @Override
    public boolean getOrNotSupportedInGMSC() {
        return this.orNotSupportedInGMSC!=null;
    }

    @Override
    public boolean getPrePagingSupported() {
        return this.prePagingSupported!=null;
    }

    @Override
    public boolean getLongFTNSupported() {
        return this.longFTNSupported!=null;
    }

    @Override
    public boolean getSuppressVtCsi() {
        return this.suppressVtCsi!=null;
    }

    @Override
    public OfferedCamel4CSIs getOfferedCamel4CSIsInInterrogatingNode() {
        return this.offeredCamel4CSIsInInterrogatingNode;
    }

    @Override
    public boolean getMtRoamingRetrySupported() {
        return this.mtRoamingRetrySupported!=null;
    }

    @Override
    public PagingArea getPagingArea() {
        return this.pagingArea;
    }

    @Override
    public EMLPPPriority getCallPriority() {
    	if(this.callPriority==null)
    		return null;
    	
        return this.callPriority.getType();
    }

    @Override
    public boolean getMtrfIndicator() {
        return this.mtrfIndicator!=null;
    }

    @Override
    public ISDNAddressString getOldMSCNumber() {
        return this.oldMSCNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProvideRoamingNumberRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi.toString());
            sb.append(", ");
        }
        if (this.mscNumber != null) {
            sb.append("mscNumber=");
            sb.append(mscNumber.toString());
            sb.append(", ");
        }
        if (this.msisdn != null) {
            sb.append("msisdn=");
            sb.append(msisdn.toString());
            sb.append(", ");
        }
        if (this.lmsi != null) {
            sb.append("lmsi=");
            sb.append(lmsi.toString());
            sb.append(", ");
        }
        if (this.gsmBearerCapability != null) {
            sb.append("gsmBearerCapability=");
            sb.append(gsmBearerCapability.toString());
            sb.append(", ");
        }
        if (this.networkSignalInfo != null) {
            sb.append("networkSignalInfo=");
            sb.append(networkSignalInfo.toString());
            sb.append(", ");
        }
        if (this.suppressionOfAnnouncement!=null) {
            sb.append("suppressionOfAnnouncement, ");
        }
        if (this.gmscAddress != null) {
            sb.append("gmscAddress=");
            sb.append(gmscAddress.toString());
            sb.append(", ");
        }
        if (this.callReferenceNumber != null) {
            sb.append("callReferenceNumber=");
            sb.append(callReferenceNumber.toString());
            sb.append(", ");
        }
        if (this.orInterrogation!=null) {
            sb.append("orInterrogation, ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }
        if (this.alertingPattern != null) {
            sb.append("alertingPattern=");
            sb.append(alertingPattern.toString());
            sb.append(", ");
        }
        if (this.ccbsCall!=null) {
            sb.append("ccbsCall, ");
        }
        if (this.supportedCamelPhasesInInterrogatingNode != null) {
            sb.append("supportedCamelPhasesInInterrogatingNode=");
            sb.append(supportedCamelPhasesInInterrogatingNode.toString());
            sb.append(", ");
        }
        if (this.additionalSignalInfo != null) {
            sb.append("additionalSignalInfo=");
            sb.append(additionalSignalInfo.toString());
            sb.append(", ");
        }
        if (this.orNotSupportedInGMSC!=null) {
            sb.append("orNotSupportedInGMSC, ");
        }
        if (this.prePagingSupported!=null) {
            sb.append("prePagingSupported, ");
        }
        if (this.longFTNSupported!=null) {
            sb.append("longFTNSupported, ");
        }
        if (this.suppressVtCsi!=null) {
            sb.append("suppressVtCsi, ");
        }
        if (this.offeredCamel4CSIsInInterrogatingNode != null) {
            sb.append("offeredCamel4CSIsInInterrogatingNode=");
            sb.append(offeredCamel4CSIsInInterrogatingNode.toString());
            sb.append(", ");
        }
        if (this.mtRoamingRetrySupported!=null) {
            sb.append("mtRoamingRetrySupported, ");
        }
        if (this.pagingArea != null) {
            sb.append("pagingArea=");
            sb.append(pagingArea.toString());
            sb.append(", ");
        }
        if (this.callPriority != null && this.callPriority.getType()!=null) {
            sb.append("callPriority=");
            sb.append(callPriority.getType().toString());
            sb.append(", ");
        }
        if (this.mtrfIndicator!=null) {
            sb.append("mtrfIndicator, ");
        }
        if (this.oldMSCNumber != null) {
            sb.append("oldMSCNumber=");
            sb.append(oldMSCNumber.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
