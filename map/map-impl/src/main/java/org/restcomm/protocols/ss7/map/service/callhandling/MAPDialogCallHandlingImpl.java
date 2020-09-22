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

import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.MAPServiceBase;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.ExtExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.AllowedServicesImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CCBSIndicatorsImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.restcomm.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SuppressMTSSImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingAreaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

import java.util.ArrayList;

/*
 *
 * @author cristian veliscu
 * @author eva ogallar
 *
 */
public class MAPDialogCallHandlingImpl extends MAPDialogImpl implements MAPDialogCallHandling {
	private static final long serialVersionUID = 1L;

	// Include these constants in MAPApplicationContextName and MAPOperationCode
    // sendRoutingInfo_Request: add constant to MAPMessageType
    // sendRoutingInfo_Response: add constant to MAPMessageType
    protected static final int locationInfoRetrievalContext = 5;
    protected static final int sendRoutingInfo = 22;
    protected static final int version = 3;

    protected MAPDialogCallHandlingImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceBase mapService, AddressStringImpl origReference, AddressStringImpl destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }

    public Long addSendRoutingInformationRequest(ISDNAddressStringImpl msisdn, CUGCheckInfoImpl cugCheckInfo,
            Integer numberOfForwarding, ExternalSignalInfoImpl networkSignalInfo) throws MAPException {
        return this.addSendRoutingInformationRequest(_Timer_Default, msisdn, cugCheckInfo, numberOfForwarding, null, false,
                null, null, null, null, null, networkSignalInfo, null, false, null, null, false, null, null, null, false, null,
                false, false, false, false, null, null, null, false, null);
    }

    public Long addSendRoutingInformationRequest(int customInvokeTimeout, ISDNAddressStringImpl msisdn, CUGCheckInfoImpl cugCheckInfo,
            Integer numberOfForwarding, ExternalSignalInfoImpl networkSignalInfo) throws MAPException {

        return this.addSendRoutingInformationRequest(customInvokeTimeout, msisdn, cugCheckInfo, numberOfForwarding, null,
                false, null, null, null, null, null, networkSignalInfo, null, false, null, null, false, null, null, null,
                false, null, false, false, false, false, null, null, null, false, null);

    }

    @Override
    public Long addSendRoutingInformationRequest(ISDNAddressStringImpl msisdn, CUGCheckInfoImpl cugCheckInfo,
            Integer numberOfForwarding, InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
            ISDNAddressStringImpl gmscAddress, CallReferenceNumberImpl callReferenceNumber, ForwardingReason forwardingReason,
            ExtBasicServiceCodeImpl basicServiceGroup, ExternalSignalInfoImpl networkSignalInfo, CamelInfoImpl camelInfo,
            boolean suppressionOfAnnouncement, MAPExtensionContainerImpl extensionContainer, AlertingPatternImpl alertingPattern,
            boolean ccbsCall, Integer supportedCCBSPhase, ExtExternalSignalInfoImpl additionalSignalInfo,
            ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
            CallDiversionTreatmentIndicatorImpl callDiversionTreatmentIndicator, boolean longFTNSupported, boolean suppressVtCSI,
            boolean suppressIncomingCallBarring, boolean gsmSCFInitiatedCall, ExtBasicServiceCodeImpl basicServiceGroup2,
            ExternalSignalInfoImpl networkSignalInfo2, SuppressMTSSImpl supressMTSS, boolean mtRoamingRetrySupported,
            EMLPPPriority callPriority) throws MAPException {

        return this
                .addSendRoutingInformationRequest(_Timer_Default, msisdn, cugCheckInfo, numberOfForwarding, interrogationType,
                        orInterrogation, orCapability, gmscAddress, callReferenceNumber, forwardingReason, basicServiceGroup,
                        networkSignalInfo, camelInfo, suppressionOfAnnouncement, extensionContainer, alertingPattern, ccbsCall,
                        supportedCCBSPhase, additionalSignalInfo, istSupportIndicator, prePagingSupported,
                        callDiversionTreatmentIndicator, longFTNSupported, suppressVtCSI, suppressIncomingCallBarring,
                        gsmSCFInitiatedCall, basicServiceGroup2, networkSignalInfo2, supressMTSS, mtRoamingRetrySupported,
                        callPriority);
    }

    @Override
    public Long addSendRoutingInformationRequest(int customInvokeTimeout, ISDNAddressStringImpl msisdn, CUGCheckInfoImpl cugCheckInfo,
            Integer numberOfForwarding, InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
            ISDNAddressStringImpl gmscAddress, CallReferenceNumberImpl callReferenceNumber, ForwardingReason forwardingReason,
            ExtBasicServiceCodeImpl basicServiceGroup, ExternalSignalInfoImpl networkSignalInfo, CamelInfoImpl camelInfo,
            boolean suppressionOfAnnouncement, MAPExtensionContainerImpl extensionContainer, AlertingPatternImpl alertingPattern,
            boolean ccbsCall, Integer supportedCCBSPhase, ExtExternalSignalInfoImpl additionalSignalInfo,
            ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
            CallDiversionTreatmentIndicatorImpl callDiversionTreatmentIndicator, boolean longFTNSupported, boolean suppressVtCSI,
            boolean suppressIncomingCallBarring, boolean gsmSCFInitiatedCall, ExtBasicServiceCodeImpl basicServiceGroup2,
            ExternalSignalInfoImpl networkSignalInfo2, SuppressMTSSImpl supressMTSS, boolean mtRoamingRetrySupported,
            EMLPPPriority callPriority) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationInfoRetrievalContext)
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2 && vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addSendRoutingInformationRequest: must be locationInfoRetrievalContext_V1, V2 or V3");

        Integer timeout=null;
        if (customInvokeTimeout == _Timer_Default)
        	timeout=getMediumTimer();
        else
        	timeout=customInvokeTimeout;

        SendRoutingInformationRequestImpl req = new SendRoutingInformationRequestImpl(this.appCntx
                .getApplicationContextVersion().getVersion(), msisdn, cugCheckInfo, numberOfForwarding, interrogationType,
                orInterrogation, orCapability, gmscAddress, callReferenceNumber, forwardingReason, basicServiceGroup,
                networkSignalInfo, camelInfo, suppressionOfAnnouncement, extensionContainer, alertingPattern, ccbsCall,
                supportedCCBSPhase, additionalSignalInfo, istSupportIndicator, prePagingSupported,
                callDiversionTreatmentIndicator, longFTNSupported, suppressVtCSI, suppressIncomingCallBarring,
                gsmSCFInitiatedCall, basicServiceGroup2, networkSignalInfo2, supressMTSS, mtRoamingRetrySupported,
                callPriority);
        
        return this.sendDataComponent(null, null, null, timeout.longValue(), (long)MAPOperationCode.sendRoutingInfo,req, true, false);        
    }

    public void addSendRoutingInformationResponse(long invokeId, IMSIImpl imsi, CUGCheckInfoImpl cugCheckInfo, RoutingInfoImpl routingInfo2)
            throws MAPException {
        this.addSendRoutingInformationResponse(invokeId, imsi, null, cugCheckInfo, false, null, null, null, false, null, null,
                null, null, null, null, null, null, null, routingInfo2, null, null, null, null, false, null);
    }
    
    protected void doAddSendRoutingInformationResponse(long invokeId, IMSIImpl imsi, CUGCheckInfoImpl cugCheckInfo, RoutingInfoImpl routingInfo2) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationInfoRetrievalContext)
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2))
            throw new MAPException(
                    "Bad application context name for addSendRoutingInformationResponse: must be locationInfoRetrievalContext_V1 or V2");
        
        SendRoutingInformationResponse res;
        res = new SendRoutingInformationResponseImplV1(this.appCntx.getApplicationContextVersion().getVersion(),imsi,routingInfo2,cugCheckInfo);
        
        this.sendDataComponent(invokeId, null, null, null, (long)MAPOperationCode.sendRoutingInfo, res, false, true);        
    }

    @Override
    public void addSendRoutingInformationResponse(long invokeId, IMSIImpl imsi, ExtendedRoutingInfoImpl extRoutingInfo,
            CUGCheckInfoImpl cugCheckInfo, boolean cugSubscriptionFlag, SubscriberInfoImpl subscriberInfo, ArrayList<SSCodeImpl> ssList,
            ExtBasicServiceCodeImpl basicService, boolean forwardingInterrogationRequired, ISDNAddressStringImpl vmscAddress,
            MAPExtensionContainerImpl extensionContainer, NAEAPreferredCIImpl naeaPreferredCI, CCBSIndicatorsImpl ccbsIndicators,
            ISDNAddressStringImpl msisdn, NumberPortabilityStatus nrPortabilityStatus, Integer istAlertTimer,
            SupportedCamelPhasesImpl supportedCamelPhases, OfferedCamel4CSIsImpl offeredCamel4CSIs, RoutingInfoImpl routingInfo2,
            ArrayList<SSCodeImpl> ssList2, ExtBasicServiceCodeImpl basicService2, AllowedServicesImpl allowedServices,
            UnavailabilityCause unavailabilityCause, boolean releaseResourcesSupported, ExternalSignalInfoImpl gsmBearerCapability)
            throws MAPException {
        doAddSendRoutingInformationResponse(false, invokeId, imsi, extRoutingInfo, cugCheckInfo, cugSubscriptionFlag,
                subscriberInfo, ssList, basicService, forwardingInterrogationRequired, vmscAddress, extensionContainer,
                naeaPreferredCI, ccbsIndicators, msisdn, nrPortabilityStatus, istAlertTimer, supportedCamelPhases,
                offeredCamel4CSIs, routingInfo2, ssList2, basicService2, allowedServices, unavailabilityCause,
                releaseResourcesSupported, gsmBearerCapability);
    }

    @Override
    public void addSendRoutingInformationResponse_NonLast(long invokeId, IMSIImpl imsi, ExtendedRoutingInfoImpl extRoutingInfo,
            CUGCheckInfoImpl cugCheckInfo, boolean cugSubscriptionFlag, SubscriberInfoImpl subscriberInfo, ArrayList<SSCodeImpl> ssList,
            ExtBasicServiceCodeImpl basicService, boolean forwardingInterrogationRequired, ISDNAddressStringImpl vmscAddress,
            MAPExtensionContainerImpl extensionContainer, NAEAPreferredCIImpl naeaPreferredCI, CCBSIndicatorsImpl ccbsIndicators,
            ISDNAddressStringImpl msisdn, NumberPortabilityStatus nrPortabilityStatus, Integer istAlertTimer,
            SupportedCamelPhasesImpl supportedCamelPhases, OfferedCamel4CSIsImpl offeredCamel4CSIs, RoutingInfoImpl routingInfo2,
            ArrayList<SSCodeImpl> ssList2, ExtBasicServiceCodeImpl basicService2, AllowedServicesImpl allowedServices,
            UnavailabilityCause unavailabilityCause, boolean releaseResourcesSupported, ExternalSignalInfoImpl gsmBearerCapability)
            throws MAPException {
        doAddSendRoutingInformationResponse(true, invokeId, imsi, extRoutingInfo, cugCheckInfo, cugSubscriptionFlag,
                subscriberInfo, ssList, basicService, forwardingInterrogationRequired, vmscAddress, extensionContainer,
                naeaPreferredCI, ccbsIndicators, msisdn, nrPortabilityStatus, istAlertTimer, supportedCamelPhases,
                offeredCamel4CSIs, routingInfo2, ssList2, basicService2, allowedServices, unavailabilityCause,
                releaseResourcesSupported, gsmBearerCapability);
    }

    protected void doAddSendRoutingInformationResponse(boolean nonLast, long invokeId, IMSIImpl imsi,
            ExtendedRoutingInfoImpl extRoutingInfo, CUGCheckInfoImpl cugCheckInfo, boolean cugSubscriptionFlag,
            SubscriberInfoImpl subscriberInfo, ArrayList<SSCodeImpl> ssList, ExtBasicServiceCodeImpl basicService,
            boolean forwardingInterrogationRequired, ISDNAddressStringImpl vmscAddress, MAPExtensionContainerImpl extensionContainer,
            NAEAPreferredCIImpl naeaPreferredCI, CCBSIndicatorsImpl ccbsIndicators, ISDNAddressStringImpl msisdn,
            NumberPortabilityStatus nrPortabilityStatus, Integer istAlertTimer, SupportedCamelPhasesImpl supportedCamelPhases,
            OfferedCamel4CSIsImpl offeredCamel4CSIs, RoutingInfoImpl routingInfo2, ArrayList<SSCodeImpl> ssList2,
            ExtBasicServiceCodeImpl basicService2, AllowedServicesImpl allowedServices, UnavailabilityCause unavailabilityCause,
            boolean releaseResourcesSupported, ExternalSignalInfoImpl gsmBearerCapability) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.locationInfoRetrievalContext)
                || vers != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addSendRoutingInformationResponse: must be locationInfoRetrievalContext_V3");
        
        SendRoutingInformationResponse res;
        res = new SendRoutingInformationResponseImplV3(this.appCntx
                .getApplicationContextVersion().getVersion(), imsi, extRoutingInfo, cugCheckInfo, cugSubscriptionFlag,
                subscriberInfo, ssList, basicService, forwardingInterrogationRequired, vmscAddress, extensionContainer,
                naeaPreferredCI, ccbsIndicators, msisdn, nrPortabilityStatus, istAlertTimer, supportedCamelPhases,
                offeredCamel4CSIs, routingInfo2, ssList2, basicService2, allowedServices, unavailabilityCause,
                releaseResourcesSupported, gsmBearerCapability);
        
        this.sendDataComponent(invokeId, null, null, null, (long)MAPOperationCode.sendRoutingInfo, res, false, !nonLast);        
    }

    @Override
    public Long addProvideRoamingNumberRequest(IMSIImpl imsi, ISDNAddressStringImpl mscNumber, ISDNAddressStringImpl msisdn, LMSIImpl lmsi,
            ExternalSignalInfoImpl gsmBearerCapability, ExternalSignalInfoImpl networkSignalInfo, boolean suppressionOfAnnouncement,
            ISDNAddressStringImpl gmscAddress, CallReferenceNumberImpl callReferenceNumber, boolean orInterrogation,
            MAPExtensionContainerImpl extensionContainer, AlertingPatternImpl alertingPattern, boolean ccbsCall,
            SupportedCamelPhasesImpl supportedCamelPhasesInInterrogatingNode, ExtExternalSignalInfoImpl additionalSignalInfo,
            boolean orNotSupportedInGMSC, boolean prePagingSupported, boolean longFTNSupported, boolean suppressVtCsi,
            OfferedCamel4CSIsImpl offeredCamel4CSIsInInterrogatingNode, boolean mtRoamingRetrySupported, PagingAreaImpl pagingArea,
            EMLPPPriority callPriority, boolean mtrfIndicator, ISDNAddressStringImpl oldMSCNumber) throws MAPException {
        return this.addProvideRoamingNumberRequest(_Timer_Default, imsi, mscNumber, msisdn, lmsi, gsmBearerCapability,
                networkSignalInfo, suppressionOfAnnouncement, gmscAddress, callReferenceNumber, orInterrogation,
                extensionContainer, alertingPattern, ccbsCall, supportedCamelPhasesInInterrogatingNode, additionalSignalInfo,
                orNotSupportedInGMSC, prePagingSupported, longFTNSupported, suppressVtCsi,
                offeredCamel4CSIsInInterrogatingNode, mtRoamingRetrySupported, pagingArea, callPriority, mtrfIndicator,
                oldMSCNumber);
    }

    @Override
    public Long addProvideRoamingNumberRequest(int customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl mscNumber,
            ISDNAddressStringImpl msisdn, LMSIImpl lmsi, ExternalSignalInfoImpl gsmBearerCapability, ExternalSignalInfoImpl networkSignalInfo,
            boolean suppressionOfAnnouncement, ISDNAddressStringImpl gmscAddress, CallReferenceNumberImpl callReferenceNumber,
            boolean orInterrogation, MAPExtensionContainerImpl extensionContainer, AlertingPatternImpl alertingPattern,
            boolean ccbsCall, SupportedCamelPhasesImpl supportedCamelPhasesInInterrogatingNode,
            ExtExternalSignalInfoImpl additionalSignalInfo, boolean orNotSupportedInGMSC, boolean prePagingSupported,
            boolean longFTNSupported, boolean suppressVtCsi, OfferedCamel4CSIsImpl offeredCamel4CSIsInInterrogatingNode,
            boolean mtRoamingRetrySupported, PagingAreaImpl pagingArea, EMLPPPriority callPriority, boolean mtrfIndicator,
            ISDNAddressStringImpl oldMSCNumber) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.roamingNumberEnquiryContext)
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2 && vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addProvideRoamingNumberRequest: must be roamingNumberEnquiryContext _V1, V2 or V3");

        Integer customTimeout=null;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        
        ProvideRoamingNumberRequestImpl req = new ProvideRoamingNumberRequestImpl(imsi, mscNumber, msisdn, lmsi,
                gsmBearerCapability, networkSignalInfo, suppressionOfAnnouncement, gmscAddress, callReferenceNumber,
                orInterrogation, extensionContainer, alertingPattern, ccbsCall, supportedCamelPhasesInInterrogatingNode,
                additionalSignalInfo, orNotSupportedInGMSC, prePagingSupported, longFTNSupported, suppressVtCsi,
                offeredCamel4CSIsInInterrogatingNode, mtRoamingRetrySupported, pagingArea, callPriority, mtrfIndicator,
                oldMSCNumber, this.appCntx.getApplicationContextVersion().getVersion());
        
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long)MAPOperationCode.provideRoamingNumber, req, true, false);
    }

    @Override
    public void addProvideRoamingNumberResponse(long invokeId, ISDNAddressStringImpl roamingNumber)
            throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.roamingNumberEnquiryContext)
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2))
            throw new MAPException(
                    "Bad application context name for addProvideRoamingNumberResponse: must be roamingNumberEnquiryContext_V1 or V2");

        ProvideRoamingNumberResponseImplV1 res = new ProvideRoamingNumberResponseImplV1(roamingNumber, this.appCntx.getApplicationContextVersion().getVersion());
        this.sendDataComponent(invokeId, null, null, null, (long)MAPOperationCode.provideRoamingNumber, res, false, true);
    }
    
    @Override
    public void addProvideRoamingNumberResponse(long invokeId, ISDNAddressStringImpl roamingNumber,
    		MAPExtensionContainerImpl extensionContainer, boolean releaseResourcesSupported, ISDNAddressStringImpl vmscAddress)
            throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.roamingNumberEnquiryContext)
                || vers != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addProvideRoamingNumberResponse: must be roamingNumberEnquiryContext_V3");

        ProvideRoamingNumberResponseImplV3 res = new ProvideRoamingNumberResponseImplV3(roamingNumber, extensionContainer,
                releaseResourcesSupported, vmscAddress, this.appCntx.getApplicationContextVersion().getVersion());
        this.sendDataComponent(invokeId, null, null, null, (long)MAPOperationCode.provideRoamingNumber, res, false, true);
    }

    @Override
    public Long addIstCommandRequest(IMSIImpl imsi, MAPExtensionContainerImpl extensionContainer) throws MAPException {
        return this.addIstCommandRequest(_Timer_Default, imsi, extensionContainer);
    }

    @Override
    public Long addIstCommandRequest(int customInvokeTimeout, IMSIImpl imsi, MAPExtensionContainerImpl extensionContainer) throws MAPException {
        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.ServiceTerminationContext)
                || (vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addIstCommandRequest: must be ServiceTerminationContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        IstCommandRequestImpl req = new IstCommandRequestImpl(imsi, extensionContainer);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long)MAPOperationCode.istCommand, req, true, false);
    }

    @Override
    public void addIstCommandResponse(long invokeId, MAPExtensionContainerImpl extensionContainer) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.ServiceTerminationContext)
                || (vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addIstCommandResponse: must be ServiceTerminationContext_V3");

        IstCommandResponseImpl res=null;
        if (extensionContainer!=null)
            res = new IstCommandResponseImpl(extensionContainer);
            
        this.sendDataComponent(invokeId, null, null, null, (long)MAPOperationCode.istCommand, res, false, true);
    }
}