/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.api.service.mobility;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DiameterIdentity;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.TMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AccessType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.AgeIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.EPSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PagingArea;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallHoldData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClipData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ClirData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EctData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSISDNBS;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ODBInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSAllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CSGSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LCSInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformation;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSSInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.NetworkAccessMode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SGSNCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VlrCamelSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceBroadcastData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.VoiceGroupCallData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.restcomm.protocols.ss7.map.api.service.oam.MDTConfiguration;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceEventList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceInterfaceList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeList;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceReference2;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceType;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface MAPDialogMobility extends MAPDialog {

    // -- Location Management Service
    Integer addUpdateLocationRequest(IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString roamingNumber,
            ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, VLRCapability vlrCapability,
            boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo,
            PagingArea pagingArea, boolean skipSubscriberDataUpdate, boolean restorationIndicator) throws MAPException;

    Integer addUpdateLocationRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString mscNumber,
            ISDNAddressString roamingNumber, ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer,
            VLRCapability vlrCapability, boolean informPreviousNetworkEntity, boolean csLCSNotSupportedByUE,
            GSNAddress vGmlcAddress, ADDInfo addInfo, PagingArea pagingArea, boolean skipSubscriberDataUpdate,
            boolean restorationIndicator) throws MAPException;

    void addUpdateLocationResponse(int invokeId, ISDNAddressString hlrNumber) throws MAPException;

    void addUpdateLocationResponse(int invokeId, ISDNAddressString hlrNumber, MAPExtensionContainer extensionContainer,
            boolean addCapability, boolean pagingAreaCapability) throws MAPException;

    Integer addCancelLocationRequest(int customInvokeTimeout, IMSI imsi, IMSIWithLMSI imsiWithLmsi) throws MAPException;

    Integer addCancelLocationRequest(IMSI imsi, IMSIWithLMSI imsiWithLmsi) throws MAPException;

    Integer addCancelLocationRequest(int customInvokeTimeout, IMSI imsi, IMSIWithLMSI imsiWithLmsi,
            CancellationType cancellationType, MAPExtensionContainer extensionContainer, TypeOfUpdate typeOfUpdate,
            boolean mtrfSupportedAndAuthorized, boolean mtrfSupportedAndNotAuthorized, ISDNAddressString newMSCNumber,
            ISDNAddressString newVLRNumber, LMSI newLmsi) throws MAPException;

    Integer addCancelLocationRequest(IMSI imsi, IMSIWithLMSI imsiWithLmsi, CancellationType cancellationType,
    		MAPExtensionContainer extensionContainer, TypeOfUpdate typeOfUpdate, boolean mtrfSupportedAndAuthorized,
            boolean mtrfSupportedAndNotAuthorized, ISDNAddressString newMSCNumber, ISDNAddressString newVLRNumber, LMSI newLmsi)
            throws MAPException;

    void addCancelLocationResponse(int invokeId, MAPExtensionContainer extensionContainer) throws MAPException;

    Integer addSendIdentificationRequest(int customInvokeTimeout, TMSI tmsi) throws MAPException;

    Integer addSendIdentificationRequest(TMSI tmsi) throws MAPException;

    Integer addSendIdentificationRequest(int customInvokeTimeout, TMSI tmsi, Integer numberOfRequestedVectors,
            boolean segmentationProhibited, MAPExtensionContainer extensionContainer, ISDNAddressString mscNumber,
            LAIFixedLength previousLAI, Integer hopCounter, boolean mtRoamingForwardingSupported,
            ISDNAddressString newVLRNumber, LMSI lmsi) throws MAPException;

    Integer addSendIdentificationRequest(TMSI tmsi, Integer numberOfRequestedVectors, boolean segmentationProhibited,
    		MAPExtensionContainer extensionContainer, ISDNAddressString mscNumber, LAIFixedLength previousLAI,
            Integer hopCounter, boolean mtRoamingForwardingSupported, ISDNAddressString newVLRNumber, LMSI lmsi)
            throws MAPException;

    void addSendIdentificationResponse(int invokeId, IMSI imsi, AuthenticationSetList authenticationSetList) throws MAPException;

    void addSendIdentificationResponse_NonLast(int invokeId, IMSI imsi, AuthenticationSetList authenticationSetList) throws MAPException;

    void addSendIdentificationResponse(int invokeId, IMSI imsi, AuthenticationSetList authenticationSetList,
            CurrentSecurityContext currentSecurityContext, MAPExtensionContainer extensionContainer) throws MAPException;

    Integer addUpdateGprsLocationRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString sgsnNumber,
            GSNAddress sgsnAddress, MAPExtensionContainer extensionContainer, SGSNCapability sgsnCapability,
            boolean informPreviousNetworkEntity, boolean psLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo,
            EPSInfo epsInfo, boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability)
            throws MAPException;

    Integer addUpdateGprsLocationRequest(IMSI imsi, ISDNAddressString sgsnNumber, GSNAddress sgsnAddress,
    		MAPExtensionContainer extensionContainer, SGSNCapability sgsnCapability, boolean informPreviousNetworkEntity,
            boolean psLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo, EPSInfo epsInfo,
            boolean servingNodeTypeIndicator, boolean skipSubscriberDataUpdate, UsedRATType usedRATType,
            boolean gprsSubscriptionDataNotNeeded, boolean nodeTypeIndicator, boolean areaRestricted,
            boolean ueReachableIndicator, boolean epsSubscriptionDataNotNeeded, UESRVCCCapability uesrvccCapability)
            throws MAPException;

    void addUpdateGprsLocationResponse(int invokeId, ISDNAddressString hlrNumber, MAPExtensionContainer extensionContainer,
            boolean addCapability, boolean sgsnMmeSeparationSupported) throws MAPException;

    Integer addPurgeMSRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString vlrNumber) throws MAPException;

    Integer addPurgeMSRequest(IMSI imsi, ISDNAddressString vlrNumber) throws MAPException;

    Integer addPurgeMSRequest(int customInvokeTimeout, IMSI imsi, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber,
    		MAPExtensionContainer extensionContainer) throws MAPException;

    Integer addPurgeMSRequest(IMSI imsi, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber,
    		MAPExtensionContainer extensionContainer) throws MAPException;

    void addPurgeMSResponse(int invokeId, boolean freezeTMSI, boolean freezePTMSI, MAPExtensionContainer extensionContainer,
            boolean freezeMTMSI) throws MAPException;

    // -- Authentication management services
    Integer addSendAuthenticationInfoRequest(IMSI imsi) throws MAPException;

    Integer addSendAuthenticationInfoRequest(int customInvokeTimeout, IMSI imsi) throws MAPException;

    Integer addSendAuthenticationInfoRequest(IMSI imsi, int numberOfRequestedVectors, boolean segmentationProhibited,
            boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
            MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException;

    Integer addSendAuthenticationInfoRequest(int customInvokeTimeout, IMSI imsi, int numberOfRequestedVectors,
            boolean segmentationProhibited, boolean immediateResponsePreferred, ReSynchronisationInfo reSynchronisationInfo,
            MAPExtensionContainer extensionContainer, RequestingNodeType requestingNodeType, PlmnId requestingPlmnId,
            Integer numberOfRequestedAdditionalVectors, boolean additionalVectorsAreForEPS) throws MAPException;

    void addSendAuthenticationInfoResponse(int invokeId, AuthenticationSetList authenticationSetList) throws MAPException;

    void addSendAuthenticationInfoResponse_NonLast(int invokeId, AuthenticationSetList authenticationSetList) throws MAPException;

    void addSendAuthenticationInfoResponse(int invokeId, AuthenticationSetList authenticationSetList,
    		MAPExtensionContainer extensionContainer, EpsAuthenticationSetList epsAuthenticationSetList) throws MAPException;

    Integer addAuthenticationFailureReportRequest(IMSI imsi, FailureCause failureCause, MAPExtensionContainer extensionContainer, Boolean reAttempt,
            AccessType accessType, ByteBuf rand, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber) throws MAPException;

    Integer addAuthenticationFailureReportRequest(int customInvokeTimeout, IMSI imsi, FailureCause failureCause, MAPExtensionContainer extensionContainer,
            Boolean reAttempt, AccessType accessType, ByteBuf rand, ISDNAddressString vlrNumber, ISDNAddressString sgsnNumber) throws MAPException;

    void addAuthenticationFailureReportResponse(int invokeId, MAPExtensionContainer extensionContainer) throws MAPException;

    // -- Fault Recovery services
    Integer addResetRequest(NetworkResource networkResource, ISDNAddressString hlrNumber, List<IMSI> hlrList) throws MAPException;

    Integer addResetRequest(int customInvokeTimeout, NetworkResource networkResource, ISDNAddressString hlrNumber, List<IMSI> hlrList) throws MAPException;

    Integer addForwardCheckSSIndicationRequest() throws MAPException;

    Integer addForwardCheckSSIndicationRequest(int customInvokeTimeout) throws MAPException;

    Integer addRestoreDataRequest(IMSI imsi, LMSI lmsi, VLRCapability vlrCapability, MAPExtensionContainer extensionContainer, boolean restorationIndicator)
            throws MAPException;

    Integer addRestoreDataRequest(int customInvokeTimeout, IMSI imsi, LMSI lmsi, VLRCapability vlrCapability, MAPExtensionContainer extensionContainer,
            boolean restorationIndicator) throws MAPException;

    void addRestoreDataResponse(int invokeId, ISDNAddressString hlrNumber, boolean msNotReachable, MAPExtensionContainer extensionContainer)
            throws MAPException;

    // -- Subscriber Information services
    long addAnyTimeInterrogationRequest(SubscriberIdentity subscriberIdentity, RequestedInfo requestedInfo,
            ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer) throws MAPException;

    long addAnyTimeInterrogationRequest(long customInvokeTimeout, SubscriberIdentity subscriberIdentity,
            RequestedInfo requestedInfo, ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer)
            throws MAPException;

    void addAnyTimeInterrogationResponse(int invokeId, SubscriberInfo subscriberInfo, MAPExtensionContainer extensionContainer)
            throws MAPException;

    void addAnyTimeInterrogationResponse_NonLast(int invokeId, SubscriberInfo subscriberInfo,
    		MAPExtensionContainer extensionContainer) throws MAPException;

    long addAnyTimeSubscriptionInterrogationRequest(SubscriberIdentity subscriberIdentity, RequestedSubscriptionInfo requestedSubscriptionInfo,
            ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer, boolean isLongFTNSupported) throws MAPException;

    long addAnyTimeSubscriptionInterrogationRequest(int customTimeout, SubscriberIdentity subscriberIdentity, RequestedSubscriptionInfo requestedSubscriptionInfo,
            ISDNAddressString gsmSCFAddress, MAPExtensionContainer extensionContainer, boolean isLongFTNSupported) throws MAPException;

    void addAnyTimeSubscriptionInterrogationResponse(int invokeId, CallForwardingData callForwardingData, CallBarringData callBarringData, ODBInfo odbInfo,
            CAMELSubscriptionInfo camelSubscriptionInfo, SupportedCamelPhases supportedVlrCamelPhases, SupportedCamelPhases supportedSgsnCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIsInVlr, OfferedCamel4CSIs offeredCamel4CSIsInSgsn,
            List<MSISDNBS> msisdnBsList, List<CSGSubscriptionData> csgSubscriptionDataList, CallWaitingData callWaitingData,
            CallHoldData callHoldData, ClipData clipData, ClirData clirData, EctData ectData) throws MAPException;

    void addAnyTimeSubscriptionInterrogationResponse_NonLast(int invokeId, CallForwardingData callForwardingData, CallBarringData callBarringData, ODBInfo odbInfo,
            CAMELSubscriptionInfo camelSubscriptionInfo, SupportedCamelPhases supportedVlrCamelPhases, SupportedCamelPhases supportedSgsnCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIsInVlr, OfferedCamel4CSIs offeredCamel4CSIsInSgsn,
            List<MSISDNBS> msisdnBsList, List<CSGSubscriptionData> csgSubscriptionDataList, CallWaitingData callWaitingData,
            CallHoldData callHoldData, ClipData clipData, ClirData clirData, EctData ectData) throws MAPException;

    long addProvideSubscriberInfoRequest(IMSI imsi, LMSI lmsi, RequestedInfo requestedInfo, MAPExtensionContainer extensionContainer, EMLPPPriority callPriority)
            throws MAPException;

    long addProvideSubscriberInfoRequest(long customInvokeTimeout, IMSI imsi, LMSI lmsi, RequestedInfo requestedInfo, MAPExtensionContainer extensionContainer,
            EMLPPPriority callPriority) throws MAPException;

    void addProvideSubscriberInfoResponse(int invokeId, SubscriberInfo subscriberInfo, MAPExtensionContainer extensionContainer) throws MAPException;

    void addProvideSubscriberInfoResponse_NonLast(int invokeId, SubscriberInfo subscriberInfo, MAPExtensionContainer extensionContainer) throws MAPException;

    // -- Subscriber Management services
    Integer addInsertSubscriberDataRequest(IMSI imsi, ISDNAddressString msisdn, Category category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) throws MAPException;

    Integer addInsertSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, ISDNAddressString msisdn,
            Category category, SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo) throws MAPException;

    Integer addInsertSubscriberDataRequest(IMSI imsi, ISDNAddressString msisdn, Category category,
            SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo, MAPExtensionContainer extensionContainer,
            NAEAPreferredCI naeaPreferredCI, GPRSSubscriptionData gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformation lsaInformation, boolean lmuIndicator, LCSInformation lcsInformation, Integer istAlertTimer,
            AgeIndicator superChargerSupportedInHLR, MCSSInfo mcSsInfo,
            CSAllocationRetentionPriority csAllocationRetentionPriority, SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo,
            ChargingCharacteristics chargingCharacteristics, AccessRestrictionData accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionData epsSubscriptionData, List<CSGSubscriptionData> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressString sgsnNumber, DiameterIdentity mmeName,
            Long subscribedPeriodicRAUTAUtimer, boolean vplmnLIPAAllowed, Boolean mdtUserConsent,
            Long subscribedPeriodicLAUtimer) throws MAPException;

    Integer addInsertSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, ISDNAddressString msisdn,
            Category category, SubscriberStatus subscriberStatus, List<ExtBearerServiceCode> bearerServiceList,
            List<ExtTeleserviceCode> teleserviceList, List<ExtSSInfo> provisionedSS, ODBData odbData,
            boolean roamingRestrictionDueToUnsupportedFeature, List<ZoneCode> regionalSubscriptionData,
            List<VoiceBroadcastData> vbsSubscriptionData, List<VoiceGroupCallData> vgcsSubscriptionData,
            VlrCamelSubscriptionInfo vlrCamelSubscriptionInfo, MAPExtensionContainer extensionContainer,
            NAEAPreferredCI naeaPreferredCI, GPRSSubscriptionData gprsSubscriptionData,
            boolean roamingRestrictedInSgsnDueToUnsupportedFeature, NetworkAccessMode networkAccessMode,
            LSAInformation lsaInformation, boolean lmuIndicator, LCSInformation lcsInformation, Integer istAlertTimer,
            AgeIndicator superChargerSupportedInHLR, MCSSInfo mcSsInfo,
            CSAllocationRetentionPriority csAllocationRetentionPriority, SGSNCAMELSubscriptionInfo sgsnCamelSubscriptionInfo,
            ChargingCharacteristics chargingCharacteristics, AccessRestrictionData accessRestrictionData, Boolean icsIndicator,
            EPSSubscriptionData epsSubscriptionData, List<CSGSubscriptionData> csgSubscriptionDataList,
            boolean ueReachabilityRequestIndicator, ISDNAddressString sgsnNumber, DiameterIdentity mmeName,
            Long subscribedPeriodicRAUTAUtimer, boolean vplmnLIPAAllowed, Boolean mdtUserConsent,
            Long subscribedPeriodicLAUtimer) throws MAPException;

    void addInsertSubscriberDataResponse(int invokeId, List<ExtTeleserviceCode> teleserviceList,
            List<ExtBearerServiceCode> bearerServiceList, List<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse) throws MAPException;

    void addInsertSubscriberDataResponse(int invokeId, List<ExtTeleserviceCode> teleserviceList,
            List<ExtBearerServiceCode> bearerServiceList, List<SSCode> ssList, ODBGeneralData odbGeneralData,
            RegionalSubscriptionResponse regionalSubscriptionResponse, SupportedCamelPhases supportedCamelPhases,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIs, SupportedFeatures supportedFeatures)
            throws MAPException;

    Integer addDeleteSubscriberDataRequest(IMSI imsi, List<ExtBasicServiceCode> basicServiceList, List<SSCode> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCode regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainer extensionContainer,
            GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdraw lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdraw specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw,
            boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted) throws MAPException;

    Integer addDeleteSubscriberDataRequest(long customInvokeTimeout, IMSI imsi, List<ExtBasicServiceCode> basicServiceList, List<SSCode> ssList,
            boolean roamingRestrictionDueToUnsupportedFeature, ZoneCode regionalSubscriptionIdentifier, boolean vbsGroupIndication,
            boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainer extensionContainer,
            GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
            LSAInformationWithdraw lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdraw specificCSIWithdraw,
            boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw,
            boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted) throws MAPException;

    void addDeleteSubscriberDataResponse(int invokeId, RegionalSubscriptionResponse regionalSubscriptionResponse, MAPExtensionContainer extensionContainer)
            throws MAPException;

    // -- International mobile equipment identities management services
    //V1 - non Huawei
    Integer addCheckImeiRequest(IMEI imei) throws MAPException;

    Integer addCheckImeiRequest(long customInvokeTimeout, IMEI imei) throws MAPException;

    //V3 - any
    Integer addCheckImeiRequest(IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
    		MAPExtensionContainer extensionContainer) throws MAPException;

    Integer addCheckImeiRequest(long customInvokeTimeout, IMEI imei, RequestedEquipmentInfo requestedEquipmentInfo,
    		MAPExtensionContainer extensionContainer) throws MAPException;

    //V1 - Huawei
    Integer addCheckImeiRequest_Huawei(IMEI imei, IMSI imsi) throws MAPException;

    Integer addCheckImeiRequest_Huawei(long customInvokeTimeout, IMEI imei, IMSI imsi) throws MAPException;

    void addCheckImeiResponse(int invokeId, EquipmentStatus equipmentStatus) throws MAPException;

    void addCheckImeiResponse(int invokeId, EquipmentStatus equipmentStatus, UESBIIu bmuef,
    		MAPExtensionContainer extensionContainer) throws MAPException;

    // -- OAM service: activateTraceMode operation can be present in networkLocUpContext and gprsLocationUpdateContext application contexts
    Integer addActivateTraceModeRequest(IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
    		MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
            TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration)
            throws MAPException;

    Integer addActivateTraceModeRequest(int customInvokeTimeout, IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
    		MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
            TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration)
            throws MAPException;

    void addActivateTraceModeResponse(int invokeId, MAPExtensionContainer extensionContainer, boolean traceSupportIndicator) throws MAPException;

}
