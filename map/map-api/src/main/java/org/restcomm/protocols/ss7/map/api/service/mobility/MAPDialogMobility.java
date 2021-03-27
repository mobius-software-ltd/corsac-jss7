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

package org.restcomm.protocols.ss7.map.api.service.mobility;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnIdImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.TMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AccessType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContextImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetListImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIuImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingAreaImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EctDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AgeIndicatorImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriorityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CategoryImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataWithdrawImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdrawImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationWithdrawImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdrawImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfigurationImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeListImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReferenceImpl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference2Impl;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceTypeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPDialogMobility extends MAPDialog {

    // -- Location Management Service
    Long addUpdateLocationRequest(IMSIImpl imsi, ISDNAddressStringImpl mscNumber, ISDNAddressStringImpl roamingNumber,
            ISDNAddressStringImpl vlrNumber, LMSIImpl lmsi, MAPExtensionContainerImpl extensionContainer, VLRCapabilityImpl vlrCapability,
            boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE, GSNAddressImpl vGmlcAddress, ADDInfoImpl addInfo,
            PagingAreaImpl pagingArea, boolean skipSubscriberDataUpdate, boolean restorationIndicator) throws MAPException;

    Long addUpdateLocationRequest(int customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl mscNumber,
            ISDNAddressStringImpl roamingNumber, ISDNAddressStringImpl vlrNumber, LMSIImpl lmsi, MAPExtensionContainerImpl extensionContainer,
            VLRCapabilityImpl vlrCapability, boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE,
            GSNAddressImpl vGmlcAddress, ADDInfoImpl addInfo, PagingAreaImpl pagingArea, boolean skipSubscriberDataUpdate,
            boolean restorationIndicator) throws MAPException;

    void addUpdateLocationResponse(long invokeId, ISDNAddressStringImpl hlrNumber) throws MAPException;

    void addUpdateLocationResponse(long invokeId, ISDNAddressStringImpl hlrNumber, MAPExtensionContainerImpl extensionContainer,
            boolean addCapability, boolean pagingAreaCapability) throws MAPException;

    Long addCancelLocationRequest(int customInvokeTimeout, IMSIImpl imsi, IMSIWithLMSIImpl imsiWithLmsi) throws MAPException;

    Long addCancelLocationRequest(IMSIImpl imsi, IMSIWithLMSIImpl imsiWithLmsi) throws MAPException;

    Long addCancelLocationRequest(int customInvokeTimeout, IMSIImpl imsi, IMSIWithLMSIImpl imsiWithLmsi,
            CancellationType cancellationType, MAPExtensionContainerImpl extensionContainer, TypeOfUpdate typeOfUpdate,
            boolean mtrfSupportedAndAuthorized, boolean mtrfSupportedAndNotAuthorized, ISDNAddressStringImpl newMSCNumber,
            ISDNAddressStringImpl newVLRNumber, LMSIImpl newLmsi) throws MAPException;

    Long addCancelLocationRequest(IMSIImpl imsi, IMSIWithLMSIImpl imsiWithLmsi, CancellationType cancellationType,
    		MAPExtensionContainerImpl extensionContainer, TypeOfUpdate typeOfUpdate, boolean mtrfSupportedAndAuthorized,
            boolean mtrfSupportedAndNotAuthorized, ISDNAddressStringImpl newMSCNumber, ISDNAddressStringImpl newVLRNumber, LMSIImpl newLmsi)
            throws MAPException;

    void addCancelLocationResponse(long invokeId, MAPExtensionContainerImpl extensionContainer) throws MAPException;

    Long addSendIdentificationRequest(int customInvokeTimeout, TMSIImpl tmsi) throws MAPException;

    Long addSendIdentificationRequest(TMSIImpl tmsi) throws MAPException;

    Long addSendIdentificationRequest(int customInvokeTimeout, TMSIImpl tmsi, Integer numberOfRequestedVectors,
            boolean segmentationProhibited, MAPExtensionContainerImpl extensionContainer, ISDNAddressStringImpl mscNumber,
            LAIFixedLengthImpl previousLAI, Integer hopCounter, boolean mtRoamingForwardingSupported,
            ISDNAddressStringImpl newVLRNumber, LMSIImpl lmsi) throws MAPException;

    Long addSendIdentificationRequest(TMSIImpl tmsi, Integer numberOfRequestedVectors, boolean segmentationProhibited,
    		MAPExtensionContainerImpl extensionContainer, ISDNAddressStringImpl mscNumber, LAIFixedLengthImpl previousLAI,
            Integer hopCounter, boolean mtRoamingForwardingSupported, ISDNAddressStringImpl newVLRNumber, LMSIImpl lmsi)
            throws MAPException;

    void addSendIdentificationResponse(long invokeId, IMSIImpl imsi, AuthenticationSetListImpl authenticationSetList) throws MAPException;

    void addSendIdentificationResponse_NonLast(long invokeId, IMSIImpl imsi, AuthenticationSetListImpl authenticationSetList) throws MAPException;

    void addSendIdentificationResponse(long invokeId, IMSIImpl imsi, AuthenticationSetListImpl authenticationSetList,
            CurrentSecurityContextImpl currentSecurityContext, MAPExtensionContainerImpl extensionContainer) throws MAPException;

    Long addUpdateGprsLocationRequest(int customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl sgsnNumber,
            GSNAddressImpl sgsnAddress, MAPExtensionContainerImpl extensionContainer, SGSNCapabilityImpl sgsnCapability,
            boolean informPreviousNetworkEntity, boolean psLCSNotSupportedByUE, GSNAddressImpl vGmlcAddress, ADDInfoImpl addInfo,
            EPSInfoImpl epsInfo, boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability)
            throws MAPException;

    Long addUpdateGprsLocationRequest(IMSIImpl imsi, ISDNAddressStringImpl sgsnNumber, GSNAddressImpl sgsnAddress,
    		MAPExtensionContainerImpl extensionContainer, SGSNCapabilityImpl sgsnCapability, boolean informPreviousNetworkEntity,
            boolean psLCSNotSupportedByUE, GSNAddressImpl vGmlcAddress, ADDInfoImpl addInfo, EPSInfoImpl epsInfo,
            boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability)
            throws MAPException;

    void addUpdateGprsLocationResponse(long invokeId, ISDNAddressStringImpl hlrNumber, MAPExtensionContainerImpl extensionContainer,
            boolean addCapability, boolean sgsnMmeSeparationSupported) throws MAPException;

    Long addPurgeMSRequest(int customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl vlrNumber) throws MAPException;

    Long addPurgeMSRequest(IMSIImpl imsi, ISDNAddressStringImpl vlrNumber) throws MAPException;

    Long addPurgeMSRequest(int customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl vlrNumber, ISDNAddressStringImpl sgsnNumber,
    		MAPExtensionContainerImpl extensionContainer) throws MAPException;

    Long addPurgeMSRequest(IMSIImpl imsi, ISDNAddressStringImpl vlrNumber, ISDNAddressStringImpl sgsnNumber,
    		MAPExtensionContainerImpl extensionContainer) throws MAPException;

    void addPurgeMSResponse(long invokeId, boolean freezeTMSI, boolean freezePTMSI, MAPExtensionContainerImpl extensionContainer,
            boolean freezeMTMSI) throws MAPException;

    // -- Authentication management services
    Long addSendAuthenticationInfoRequest(IMSIImpl imsi) throws MAPException;

    Long addSendAuthenticationInfoRequest(int customInvokeTimeout, IMSIImpl imsi) throws MAPException;

    Long addSendAuthenticationInfoRequest(IMSIImpl imsi, int numberOfRequestedVectors, boolean segmentationProhibited,
            boolean immediateResponsePreferred, ReSynchronisationInfoImpl reSynchronisationInfo,
            MAPExtensionContainerImpl extensionContainer, RequestingNodeType requestingNodeType, PlmnIdImpl requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException;

    Long addSendAuthenticationInfoRequest(int customInvokeTimeout, IMSIImpl imsi, int numberOfRequestedVectors,
            boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfoImpl reSynchronisationInfo,
            MAPExtensionContainerImpl extensionContainer, RequestingNodeType requestingNodeType, PlmnIdImpl requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException;

    void addSendAuthenticationInfoResponse(long invokeId, AuthenticationSetListImpl authenticationSetList) throws MAPException;

    void addSendAuthenticationInfoResponse_NonLast(long invokeId, AuthenticationSetListImpl authenticationSetList) throws MAPException;

    void addSendAuthenticationInfoResponse(long invokeId, AuthenticationSetListImpl authenticationSetList,
    		MAPExtensionContainerImpl extensionContainer, EpsAuthenticationSetListImpl epsAuthenticationSetList) throws MAPException;

    Long addAuthenticationFailureReportRequest(IMSIImpl imsi, FailureCause failureCause, MAPExtensionContainerImpl extensionContainer, Boolean reAttempt,
            AccessType accessType, byte[] rand, ISDNAddressStringImpl vlrNumber, ISDNAddressStringImpl sgsnNumber) throws MAPException;

    Long addAuthenticationFailureReportRequest(int customInvokeTimeout, IMSIImpl imsi, FailureCause failureCause, MAPExtensionContainerImpl extensionContainer,
            Boolean reAttempt, AccessType accessType, byte[] rand, ISDNAddressStringImpl vlrNumber, ISDNAddressStringImpl sgsnNumber) throws MAPException;

    void addAuthenticationFailureReportResponse(long invokeId, MAPExtensionContainerImpl extensionContainer) throws MAPException;

    // -- Fault Recovery services
    Long addResetRequest(NetworkResource networkResource, ISDNAddressStringImpl hlrNumber, List<IMSIImpl> hlrList) throws MAPException;

    Long addResetRequest(int customInvokeTimeout, NetworkResource networkResource, ISDNAddressStringImpl hlrNumber, List<IMSIImpl> hlrList) throws MAPException;

    Long addForwardCheckSSIndicationRequest() throws MAPException;

    Long addForwardCheckSSIndicationRequest(int customInvokeTimeout) throws MAPException;

    Long addRestoreDataRequest(IMSIImpl imsi, LMSIImpl lmsi, VLRCapabilityImpl vlrCapability, MAPExtensionContainerImpl extensionContainer, boolean restorationIndicator)
            throws MAPException;

    Long addRestoreDataRequest(int customInvokeTimeout, IMSIImpl imsi, LMSIImpl lmsi, VLRCapabilityImpl vlrCapability, MAPExtensionContainerImpl extensionContainer,
            boolean restorationIndicator) throws MAPException;

    void addRestoreDataResponse(long invokeId, ISDNAddressStringImpl hlrNumber, boolean msNotReachable, MAPExtensionContainerImpl extensionContainer)
            throws MAPException;

    // -- Subscriber Information services
    long addAnyTimeInterrogationRequest(SubscriberIdentityImpl subscriberIdentity, RequestedInfoImpl requestedInfo,
            ISDNAddressStringImpl gsmSCFAddress, MAPExtensionContainerImpl extensionContainer) throws MAPException;

    long addAnyTimeInterrogationRequest(long customInvokeTimeout, SubscriberIdentityImpl subscriberIdentity,
            RequestedInfoImpl requestedInfo, ISDNAddressStringImpl gsmSCFAddress, MAPExtensionContainerImpl extensionContainer)
            throws MAPException;

    void addAnyTimeInterrogationResponse(long invokeId, SubscriberInfoImpl subscriberInfo, MAPExtensionContainerImpl extensionContainer)
            throws MAPException;

    void addAnyTimeInterrogationResponse_NonLast(long invokeId, SubscriberInfoImpl subscriberInfo,
    		MAPExtensionContainerImpl extensionContainer) throws MAPException;

    long addAnyTimeSubscriptionInterrogationRequest(SubscriberIdentityImpl subscriberIdentity, RequestedSubscriptionInfoImpl requestedSubscriptionInfo,
            ISDNAddressStringImpl gsmSCFAddress, MAPExtensionContainerImpl extensionContainer, boolean isLongFTNSupported) throws MAPException;

    long addAnyTimeSubscriptionInterrogationRequest(int customTimeout, SubscriberIdentityImpl subscriberIdentity, RequestedSubscriptionInfoImpl requestedSubscriptionInfo,
            ISDNAddressStringImpl gsmSCFAddress, MAPExtensionContainerImpl extensionContainer, boolean isLongFTNSupported) throws MAPException;

    void addAnyTimeSubscriptionInterrogationResponse(long invokeId, CallForwardingDataImpl callForwardingData, CallBarringDataImpl callBarringData, ODBInfoImpl odbInfo,
            CAMELSubscriptionInfoImpl camelSubscriptionInfo, SupportedCamelPhasesImpl supportedVlrCamelPhases, SupportedCamelPhasesImpl supportedSgsnCamelPhases,
            MAPExtensionContainerImpl extensionContainer, OfferedCamel4CSIsImpl offeredCamel4CSIsInVlr, OfferedCamel4CSIsImpl offeredCamel4CSIsInSgsn,
            List<MSISDNBSImpl> msisdnBsList, List<CSGSubscriptionDataImpl> csgSubscriptionDataList, CallWaitingDataImpl callWaitingData,
            CallHoldDataImpl callHoldData, ClipDataImpl clipData, ClirDataImpl clirData, EctDataImpl ectData) throws MAPException;

    void addAnyTimeSubscriptionInterrogationResponse_NonLast(long invokeId, CallForwardingDataImpl callForwardingData, CallBarringDataImpl callBarringData, ODBInfoImpl odbInfo,
            CAMELSubscriptionInfoImpl camelSubscriptionInfo, SupportedCamelPhasesImpl supportedVlrCamelPhases, SupportedCamelPhasesImpl supportedSgsnCamelPhases,
            MAPExtensionContainerImpl extensionContainer, OfferedCamel4CSIsImpl offeredCamel4CSIsInVlr, OfferedCamel4CSIsImpl offeredCamel4CSIsInSgsn,
            List<MSISDNBSImpl> msisdnBsList, List<CSGSubscriptionDataImpl> csgSubscriptionDataList, CallWaitingDataImpl callWaitingData,
            CallHoldDataImpl callHoldData, ClipDataImpl clipData, ClirDataImpl clirData, EctDataImpl ectData) throws MAPException;

    long addProvideSubscriberInfoRequest(IMSIImpl imsi, LMSIImpl lmsi, RequestedInfoImpl requestedInfo, MAPExtensionContainerImpl extensionContainer, EMLPPPriority callPriority)
            throws MAPException;

    long addProvideSubscriberInfoRequest(long customInvokeTimeout, IMSIImpl imsi, LMSIImpl lmsi, RequestedInfoImpl requestedInfo, MAPExtensionContainerImpl extensionContainer,
            EMLPPPriority callPriority) throws MAPException;

    void addProvideSubscriberInfoResponse(long invokeId, SubscriberInfoImpl subscriberInfo, MAPExtensionContainerImpl extensionContainer) throws MAPException;

    void addProvideSubscriberInfoResponse_NonLast(long invokeId, SubscriberInfoImpl subscriberInfo, MAPExtensionContainerImpl extensionContainer) throws MAPException;

    // -- Subscriber Management services
    Long addInsertSubscriberDataRequest(IMSIImpl imsi, ISDNAddressStringImpl msisdn, CategoryImpl category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCodeImpl> bearerServiceList,
            List<ExtTeleserviceCodeImpl> teleserviceList, List<ExtSSInfoImpl> provisionedSS, ODBDataImpl odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCodeImpl> regionalSubscriptionData,
            List<VoiceBroadcastDataImpl> vbsSubscriptionData, List<VoiceGroupCallDataImpl> vgcsSubscriptionData,
            VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo) throws MAPException;

    Long addInsertSubscriberDataRequest(long customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl msisdn,
            CategoryImpl category, SubscriberStatus subscriberStatus, List<ExtBearerServiceCodeImpl> bearerServiceList,
            List<ExtTeleserviceCodeImpl> teleserviceList, List<ExtSSInfoImpl> provisionedSS, ODBDataImpl odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCodeImpl> regionalSubscriptionData,
            List<VoiceBroadcastDataImpl> vbsSubscriptionData, List<VoiceGroupCallDataImpl> vgcsSubscriptionData,
            VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo) throws MAPException;

    Long addInsertSubscriberDataRequest(IMSIImpl imsi, ISDNAddressStringImpl msisdn, CategoryImpl category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCodeImpl> bearerServiceList,
            List<ExtTeleserviceCodeImpl> teleserviceList, List<ExtSSInfoImpl> provisionedSS, ODBDataImpl odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCodeImpl> regionalSubscriptionData,
            List<VoiceBroadcastDataImpl> vbsSubscriptionData, List<VoiceGroupCallDataImpl> vgcsSubscriptionData,
            VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo, MAPExtensionContainerImpl extensionContainer,
            NAEAPreferredCIImpl naeaPreferredCI, GPRSSubscriptionDataImpl gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformationImpl lsaInformation, boolean lmuIndicator, LCSInformationImpl lcsInformation, Integer istAlertTimer,
            AgeIndicatorImpl superChargerSupportedInHLR, MCSSInfoImpl mcSsInfo,
            CSAllocationRetentionPriorityImpl csAllocationRetentionPriority, SGSNCAMELSubscriptionInfoImpl sgsnCamelSubscriptionInfo,
            ChargingCharacteristicsImpl chargingCharacteristics, AccessRestrictionDataImpl accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionDataImpl epsSubscriptionData, List<CSGSubscriptionDataImpl> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressStringImpl sgsnNumber, DiameterIdentityImpl mmeName,
            Long subscribedPeriodicRAUTAUtimer, boolean vplmnLIPAAllowed, Boolean mdtUserConsent,
            Long subscribedPeriodicLAUtimer) throws MAPException;

    Long addInsertSubscriberDataRequest(long customInvokeTimeout, IMSIImpl imsi, ISDNAddressStringImpl msisdn,
            CategoryImpl category, SubscriberStatus subscriberStatus, List<ExtBearerServiceCodeImpl> bearerServiceList,
            List<ExtTeleserviceCodeImpl> teleserviceList, List<ExtSSInfoImpl> provisionedSS, ODBDataImpl odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCodeImpl> regionalSubscriptionData,
            List<VoiceBroadcastDataImpl> vbsSubscriptionData, List<VoiceGroupCallDataImpl> vgcsSubscriptionData,
            VlrCamelSubscriptionInfoImpl vlrCamelSubscriptionInfo, MAPExtensionContainerImpl extensionContainer,
            NAEAPreferredCIImpl naeaPreferredCI, GPRSSubscriptionDataImpl gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformationImpl lsaInformation, boolean lmuIndicator, LCSInformationImpl lcsInformation, Integer istAlertTimer,
            AgeIndicatorImpl superChargerSupportedInHLR, MCSSInfoImpl mcSsInfo,
            CSAllocationRetentionPriorityImpl csAllocationRetentionPriority, SGSNCAMELSubscriptionInfoImpl sgsnCamelSubscriptionInfo,
            ChargingCharacteristicsImpl chargingCharacteristics, AccessRestrictionDataImpl accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionDataImpl epsSubscriptionData, List<CSGSubscriptionDataImpl> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressStringImpl sgsnNumber, DiameterIdentityImpl mmeName,
            Long subscribedPeriodicRAUTAUtimer, boolean vplmnLIPAAllowed, Boolean mdtUserConsent,
            Long subscribedPeriodicLAUtimer) throws MAPException;

    void addInsertSubscriberDataResponse(long invokeId, List<ExtTeleserviceCodeImpl> teleserviceList,
            List<ExtBearerServiceCodeImpl> bearerServiceList, List<SSCodeImpl> ssList, ODBGeneralDataImpl odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse) throws MAPException;

    void addInsertSubscriberDataResponse(long invokeId, List<ExtTeleserviceCodeImpl> teleserviceList,
            List<ExtBearerServiceCodeImpl> bearerServiceList, List<SSCodeImpl> ssList, ODBGeneralDataImpl odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse, SupportedCamelPhasesImpl supportedCamelPhases,
            MAPExtensionContainerImpl extensionContainer, OfferedCamel4CSIsImpl offeredCamel4CSIs, SupportedFeaturesImpl supportedFeatures)
            throws MAPException;

    Long addDeleteSubscriberDataRequest(IMSIImpl imsi, List<ExtBasicServiceCodeImpl> basicServiceList, List<SSCodeImpl> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCodeImpl regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainerImpl extensionContainer,
            GPRSSubscriptionDataWithdrawImpl gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdrawImpl lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdrawImpl specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdrawImpl epsSubscriptionDataWithdraw,
            boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted) throws MAPException;

    Long addDeleteSubscriberDataRequest(long customInvokeTimeout, IMSIImpl imsi, List<ExtBasicServiceCodeImpl> basicServiceList, List<SSCodeImpl> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCodeImpl regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainerImpl extensionContainer,
            GPRSSubscriptionDataWithdrawImpl gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdrawImpl lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdrawImpl specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdrawImpl epsSubscriptionDataWithdraw,
            boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted) throws MAPException;

    void addDeleteSubscriberDataResponse(long invokeId, RegionalSubscriptionResponse regionalSubscriptionResponse, MAPExtensionContainerImpl extensionContainer)
            throws MAPException;

    // -- International mobile equipment identities management services
    //V1 - non Huawei
    Long addCheckImeiRequest(IMEIImpl imei) throws MAPException;

    Long addCheckImeiRequest(long customInvokeTimeout, IMEIImpl imei) throws MAPException;

    //V3 - any
    Long addCheckImeiRequest(IMEIImpl imei, RequestedEquipmentInfoImpl requestedEquipmentInfo,
    		MAPExtensionContainerImpl extensionContainer) throws MAPException;

    Long addCheckImeiRequest(long customInvokeTimeout, IMEIImpl imei, RequestedEquipmentInfoImpl requestedEquipmentInfo,
    		MAPExtensionContainerImpl extensionContainer) throws MAPException;

    //V1 - Huawei
    Long addCheckImeiRequest_Huawei(IMEIImpl imei, IMSIImpl imsi) throws MAPException;

    Long addCheckImeiRequest_Huawei(long customInvokeTimeout, IMEIImpl imei, IMSIImpl imsi) throws MAPException;

    void addCheckImeiResponse(long invokeId, EquipmentStatus equipmentStatus) throws MAPException;

    void addCheckImeiResponse(long invokeId, EquipmentStatus equipmentStatus, UESBIIuImpl bmuef,
    		MAPExtensionContainerImpl extensionContainer) throws MAPException;

    // -- OAM service: activateTraceMode operation can be present in networkLocUpContext and gprsLocationUpdateContext application contexts
    Long addActivateTraceModeRequest(IMSIImpl imsi, TraceReferenceImpl traceReference, TraceTypeImpl traceType, AddressStringImpl omcId,
    		MAPExtensionContainerImpl extensionContainer, TraceReference2Impl traceReference2, TraceDepthListImpl traceDepthList, TraceNETypeListImpl traceNeTypeList,
            TraceInterfaceListImpl traceInterfaceList, TraceEventListImpl traceEventList, GSNAddressImpl traceCollectionEntity, MDTConfigurationImpl mdtConfiguration)
            throws MAPException;

    Long addActivateTraceModeRequest(int customInvokeTimeout, IMSIImpl imsi, TraceReferenceImpl traceReference, TraceTypeImpl traceType, AddressStringImpl omcId,
    		MAPExtensionContainerImpl extensionContainer, TraceReference2Impl traceReference2, TraceDepthListImpl traceDepthList, TraceNETypeListImpl traceNeTypeList,
            TraceInterfaceListImpl traceInterfaceList, TraceEventListImpl traceEventList, GSNAddressImpl traceCollectionEntity, MDTConfigurationImpl mdtConfiguration)
            throws MAPException;

    void addActivateTraceModeResponse(long invokeId, MAPExtensionContainerImpl extensionContainer, boolean traceSupportIndicator) throws MAPException;

}
