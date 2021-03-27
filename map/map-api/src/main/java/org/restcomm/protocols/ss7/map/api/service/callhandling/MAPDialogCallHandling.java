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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.ExtExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingAreaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;

import java.util.List;

/*
 *
 * @author cristian veliscu
 * @author eva ogallar
 *
 */
public interface MAPDialogCallHandling extends MAPDialog {
     Long addSendRoutingInformationRequest(ISDNAddressStringImpl msisdn, CUGCheckInfoImpl cugCheckInfo,
            Integer numberOfForwarding, InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
            ISDNAddressStringImpl gmscAddress, CallReferenceNumberImpl callReferenceNumber, ForwardingReason forwardingReason,
            ExtBasicServiceCodeImpl basicServiceGroup, ExternalSignalInfoImpl networkSignalInfo, CamelInfoImpl camelInfo,
            boolean suppressionOfAnnouncement, MAPExtensionContainerImpl extensionContainer, AlertingPatternImpl alertingPattern,
            boolean ccbsCall, Integer supportedCCBSPhase, ExtExternalSignalInfoImpl additionalSignalInfo,
            ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
            CallDiversionTreatmentIndicatorImpl callDiversionTreatmentIndicator, boolean longFTNSupported, boolean suppressVtCSI,
            boolean suppressIncomingCallBarring, boolean gsmSCFInitiatedCall, ExtBasicServiceCodeImpl basicServiceGroup2,
            ExternalSignalInfoImpl networkSignalInfo2, SuppressMTSSImpl supressMTSS, boolean mtRoamingRetrySupported,
            EMLPPPriority callPriority) throws MAPException;

     Long addSendRoutingInformationRequest(ISDNAddressStringImpl msisdn, CUGCheckInfoImpl cugCheckInfo,
            Integer numberOfForwarding, ExternalSignalInfoImpl networkSignalInfo) throws MAPException;

     Long addSendRoutingInformationRequest(int customInvokeTimeout, ISDNAddressStringImpl msisdn, CUGCheckInfoImpl cugCheckInfo,
            Integer numberOfForwarding, ExternalSignalInfoImpl networkSignalInfo) throws MAPException;

     Long addSendRoutingInformationRequest(int customInvokeTimeout, ISDNAddressStringImpl msisdn, CUGCheckInfoImpl cugCheckInfo,
            Integer numberOfForwarding, InterrogationType interrogationType, boolean orInterrogation, Integer orCapability,
            ISDNAddressStringImpl gmscAddress, CallReferenceNumberImpl callReferenceNumber, ForwardingReason forwardingReason,
            ExtBasicServiceCodeImpl basicServiceGroup, ExternalSignalInfoImpl networkSignalInfo, CamelInfoImpl camelInfo,
            boolean suppressionOfAnnouncement, MAPExtensionContainerImpl extensionContainer, AlertingPatternImpl alertingPattern,
            boolean ccbsCall, Integer supportedCCBSPhase, ExtExternalSignalInfoImpl additionalSignalInfo,
            ISTSupportIndicator istSupportIndicator, boolean prePagingSupported,
            CallDiversionTreatmentIndicatorImpl callDiversionTreatmentIndicator, boolean longFTNSupported, boolean suppressVtCSI,
            boolean suppressIncomingCallBarring, boolean gsmSCFInitiatedCall, ExtBasicServiceCodeImpl basicServiceGroup2,
            ExternalSignalInfoImpl networkSignalInfo2, SuppressMTSSImpl supressMTSS, boolean mtRoamingRetrySupported,
            EMLPPPriority callPriority) throws MAPException;

     void addSendRoutingInformationResponse(long invokeId, IMSIImpl imsi, CUGCheckInfoImpl cugCheckInfo, RoutingInfoImpl routingInfo2)
             throws MAPException;

     void addSendRoutingInformationResponse(long invokeId, IMSIImpl imsi, ExtendedRoutingInfoImpl extRoutingInfo,
    		 CUGCheckInfoImpl cugCheckInfo, boolean cugSubscriptionFlag, SubscriberInfoImpl subscriberInfo, List<SSCodeImpl> ssList,
            ExtBasicServiceCodeImpl basicService, boolean forwardingInterrogationRequired, ISDNAddressStringImpl vmscAddress,
            MAPExtensionContainerImpl extensionContainer, NAEAPreferredCIImpl naeaPreferredCI, CCBSIndicatorsImpl ccbsIndicators,
            ISDNAddressStringImpl msisdn, NumberPortabilityStatus nrPortabilityStatus, Integer istAlertTimer,
            SupportedCamelPhasesImpl supportedCamelPhases, OfferedCamel4CSIsImpl offeredCamel4CSIs, RoutingInfoImpl routingInfo2,
            List<SSCodeImpl> ssList2, ExtBasicServiceCodeImpl basicService2, AllowedServicesImpl allowedServices,
            UnavailabilityCause unavailabilityCause, boolean releaseResourcesSupported, ExternalSignalInfoImpl gsmBearerCapability)
            throws MAPException;

     void addSendRoutingInformationResponse_NonLast(long invokeId, IMSIImpl imsi, ExtendedRoutingInfoImpl extRoutingInfo,
    		 CUGCheckInfoImpl cugCheckInfo, boolean cugSubscriptionFlag, SubscriberInfoImpl subscriberInfo, List<SSCodeImpl> ssList,
            ExtBasicServiceCodeImpl basicService, boolean forwardingInterrogationRequired, ISDNAddressStringImpl vmscAddress,
            MAPExtensionContainerImpl extensionContainer, NAEAPreferredCIImpl naeaPreferredCI, CCBSIndicatorsImpl ccbsIndicators,
            ISDNAddressStringImpl msisdn, NumberPortabilityStatus nrPortabilityStatus, Integer istAlertTimer,
            SupportedCamelPhasesImpl supportedCamelPhases, OfferedCamel4CSIsImpl offeredCamel4CSIs, RoutingInfoImpl routingInfo2,
            List<SSCodeImpl> ssList2, ExtBasicServiceCodeImpl basicService2, AllowedServicesImpl allowedServices,
            UnavailabilityCause unavailabilityCause, boolean releaseResourcesSupported, ExternalSignalInfoImpl gsmBearerCapability)
            throws MAPException;

     Long addProvideRoamingNumberRequest(IMSIImpl imsi, ISDNAddressStringImpl mscNumber, ISDNAddressStringImpl msisdn, LMSIImpl lmsi,
            ExternalSignalInfoImpl gsmBearerCapability, ExternalSignalInfoImpl networkSignalInfo, boolean suppressionOfAnnouncement,
            ISDNAddressStringImpl gmscAddress, CallReferenceNumberImpl callReferenceNumber, boolean orInterrogation,
            MAPExtensionContainerImpl extensionContainer, AlertingPatternImpl alertingPattern, boolean ccbsCall,
            SupportedCamelPhasesImpl supportedCamelPhasesInInterrogatingNode, ExtExternalSignalInfoImpl additionalSignalInfo,
            boolean orNotSupportedInGMSC, boolean prePagingSupported, boolean longFTNSupported, boolean suppressVtCsi,
            OfferedCamel4CSIsImpl offeredCamel4CSIsInInterrogatingNode, boolean mtRoamingRetrySupported, PagingAreaImpl pagingArea,
            EMLPPPriority callPriority, boolean mtrfIndicator, ISDNAddressStringImpl oldMSCNumber) throws MAPException;

     Long addProvideRoamingNumberRequest(int customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl mscNumber,
            ISDNAddressStringImpl msisdn, LMSIImpl lmsi, ExternalSignalInfoImpl gsmBearerCapability, ExternalSignalInfoImpl networkSignalInfo,
            boolean suppressionOfAnnouncement, ISDNAddressStringImpl gmscAddress, CallReferenceNumberImpl callReferenceNumber,
            boolean orInterrogation, MAPExtensionContainerImpl extensionContainer, AlertingPatternImpl alertingPattern,
            boolean ccbsCall, SupportedCamelPhasesImpl supportedCamelPhasesInInterrogatingNode,
            ExtExternalSignalInfoImpl additionalSignalInfo, boolean orNotSupportedInGMSC, boolean prePagingSupported,
            boolean longFTNSupported, boolean suppressVtCsi, OfferedCamel4CSIsImpl offeredCamel4CSIsInInterrogatingNode,
            boolean mtRoamingRetrySupported, PagingAreaImpl pagingArea, EMLPPPriority callPriority, boolean mtrfIndicator,
            ISDNAddressStringImpl oldMSCNumber) throws MAPException;

     void addProvideRoamingNumberResponse(long invokeId, ISDNAddressStringImpl roamingNumber)
            throws MAPException;
     
     void addProvideRoamingNumberResponse(long invokeId, ISDNAddressStringImpl roamingNumber,
    		 MAPExtensionContainerImpl extensionContainer, boolean releaseResourcesSupported, ISDNAddressStringImpl vmscAddress)
            throws MAPException;

     Long addIstCommandRequest(IMSIImpl imsi, MAPExtensionContainerImpl extensionContainer) throws MAPException;

     Long addIstCommandRequest(int customInvokeTimeout, IMSIImpl imsi, MAPExtensionContainerImpl extensionContainer) throws MAPException;

     void addIstCommandResponse(long invokeId, MAPExtensionContainerImpl extensionContainer) throws MAPException;
}