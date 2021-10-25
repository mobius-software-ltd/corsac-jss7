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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.gap.GapCriteriaImpl;
import org.restcomm.protocols.ss7.cap.api.gap.GapIndicatorsImpl;
import org.restcomm.protocols.ss7.cap.api.gap.GapTreatmentImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CallingPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.DigitsImpl;
import org.restcomm.protocols.ss7.cap.api.isup.GenericNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.LocationNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.OriginalCalledNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.RedirectingPartyIDCapImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.AChChargingAddressImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.BCSMEventImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.cap.api.primitives.ScfIDImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimerID;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancelImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ContinueWithArgumentArgExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ControlType;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSMImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELSequence1Impl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSendImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResultImpl;
import org.restcomm.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.restcomm.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.restcomm.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.restcomm.protocols.ss7.inap.api.primitives.LegID;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGIndexImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 *
 */
public interface CAPDialogCircuitSwitchedCall extends CAPDialog {

    Long addInitialDPRequest(int serviceKey, CalledPartyNumberCapImpl calledPartyNumber,
            CallingPartyNumberCapImpl callingPartyNumber, CallingPartysCategoryInap callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilitiesImpl IPSSPCapabilities, LocationNumberCapImpl locationNumber,
            OriginalCalledNumberCapImpl originalCalledPartyID, CAPExtensionsImpl extensions,
            HighLayerCompatibilityInap highLayerCompatibility, DigitsImpl additionalCallingPartyNumber,
            BearerCapabilityImpl bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCapImpl redirectingPartyID,
            RedirectionInformationInap redirectionInformation, CauseCapImpl cause,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, CarrierImpl carrier, CUGIndexImpl cugIndex,
            CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, IMSIImpl imsi, SubscriberStateImpl subscriberState,
            LocationInformationImpl locationInformation, ExtBasicServiceCodeImpl extBasicServiceCode,
            CallReferenceNumberImpl callReferenceNumber, ISDNAddressStringImpl mscAddress, CalledPartyBCDNumberImpl calledPartyBCDNumber,
            TimeAndTimezoneImpl timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtensionImpl initialDPArgExtension)
            throws CAPException;

    Long addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberCapImpl calledPartyNumber,
            CallingPartyNumberCapImpl callingPartyNumber, CallingPartysCategoryInap callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilitiesImpl IPSSPCapabilities, LocationNumberCapImpl locationNumber,
            OriginalCalledNumberCapImpl originalCalledPartyID, CAPExtensionsImpl extensions,
            HighLayerCompatibilityInap highLayerCompatibility, DigitsImpl additionalCallingPartyNumber,
            BearerCapabilityImpl bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCapImpl redirectingPartyID,
            RedirectionInformationInap redirectionInformation, CauseCapImpl cause,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, CarrierImpl carrier, CUGIndexImpl cugIndex,
            CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, IMSIImpl imsi, SubscriberStateImpl subscriberState,
            LocationInformationImpl locationInformation, ExtBasicServiceCodeImpl extBasicServiceCode,
            CallReferenceNumberImpl callReferenceNumber, ISDNAddressStringImpl mscAddress, CalledPartyBCDNumberImpl calledPartyBCDNumber,
            TimeAndTimezoneImpl timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtensionImpl initialDPArgExtension)
            throws CAPException;

    Long addApplyChargingReportRequest(TimeDurationChargingResultImpl timeDurationChargingResult) throws CAPException;

    Long addApplyChargingReportRequest(int customInvokeTimeout, TimeDurationChargingResultImpl timeDurationChargingResult)
            throws CAPException;

    Long addApplyChargingRequest(CAMELAChBillingChargingCharacteristicsImpl aChBillingChargingCharacteristics,
            LegType partyToCharge, CAPExtensionsImpl extensions, AChChargingAddressImpl aChChargingAddress) throws CAPException;

    Long addApplyChargingRequest(int customInvokeTimeout,
            CAMELAChBillingChargingCharacteristicsImpl aChBillingChargingCharacteristics, LegType partyToCharge,
            CAPExtensionsImpl extensions, AChChargingAddressImpl aChChargingAddress) throws CAPException;

    Long addCallInformationReportRequest(List<RequestedInformationImpl> requestedInformationList,
            CAPExtensionsImpl extensions, LegType legID) throws CAPException;

    Long addCallInformationReportRequest(int customInvokeTimeout,
            List<RequestedInformationImpl> requestedInformationList, CAPExtensionsImpl extensions, LegType legID)
            throws CAPException;

    Long addCallInformationRequestRequest(List<RequestedInformationType> requestedInformationTypeList,
            CAPExtensionsImpl extensions, LegType legID) throws CAPException;

    Long addCallInformationRequestRequest(int customInvokeTimeout,
            List<RequestedInformationType> requestedInformationTypeList, CAPExtensionsImpl extensions, LegType legID)
            throws CAPException;

    Long addConnectRequest(DestinationRoutingAddressImpl destinationRoutingAddress, AlertingPatternCapImpl alertingPattern,
            OriginalCalledNumberCapImpl originalCalledPartyID, CAPExtensionsImpl extensions, CarrierImpl carrier,
            CallingPartysCategoryInap callingPartysCategory, RedirectingPartyIDCapImpl redirectingPartyID,
            RedirectionInformationInap redirectionInformation, List<GenericNumberCapImpl> genericNumbers,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, LocationNumberCapImpl chargeNumber,
            LegID legToBeConnected, CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfoImpl naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException;

    Long addConnectRequest(int customInvokeTimeout, DestinationRoutingAddressImpl destinationRoutingAddress,
            AlertingPatternCapImpl alertingPattern, OriginalCalledNumberCapImpl originalCalledPartyID, CAPExtensionsImpl extensions,
            CarrierImpl carrier, CallingPartysCategoryInap callingPartysCategory, RedirectingPartyIDCapImpl redirectingPartyID,
            RedirectionInformationInap redirectionInformation, List<GenericNumberCapImpl> genericNumbers,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, LocationNumberCapImpl chargeNumber,
            LegID legToBeConnected, CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfoImpl naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException;

    Long addContinueRequest() throws CAPException;

    Long addContinueRequest(int customInvokeTimeout) throws CAPException;

    Long addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSMImpl eventSpecificInformationBCSM, LegType legID, MiscCallInfo miscCallInfo,
            CAPExtensionsImpl extensions) throws CAPException;

    Long addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSMImpl eventSpecificInformationBCSM, LegType legID, MiscCallInfo miscCallInfo,
            CAPExtensionsImpl extensions) throws CAPException;

    Long addRequestReportBCSMEventRequest(List<BCSMEventImpl> bcsmEventList, CAPExtensionsImpl extensions)
            throws CAPException;

    Long addRequestReportBCSMEventRequest(int customInvokeTimeout, List<BCSMEventImpl> bcsmEventList,
            CAPExtensionsImpl extensions) throws CAPException;

    Long addReleaseCallRequest(CauseCapImpl cause) throws CAPException;

    Long addReleaseCallRequest(int customInvokeTimeout, CauseCapImpl cause) throws CAPException;

    Long addActivityTestRequest() throws CAPException;

    Long addActivityTestRequest(int customInvokeTimeout) throws CAPException;

    void addActivityTestResponse(long invokeId) throws CAPException;

    Long addAssistRequestInstructionsRequest(DigitsImpl correlationID, IPSSPCapabilitiesImpl ipSSPCapabilities,
            CAPExtensionsImpl extensions) throws CAPException;

    Long addAssistRequestInstructionsRequest(int customInvokeTimeout, DigitsImpl correlationID,
            IPSSPCapabilitiesImpl ipSSPCapabilities, CAPExtensionsImpl extensions) throws CAPException;

    Long addEstablishTemporaryConnectionRequest(DigitsImpl assistingSSPIPRoutingAddress, DigitsImpl correlationID, ScfIDImpl scfID,
            CAPExtensionsImpl extensions, CarrierImpl carrier, ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
            Integer callSegmentID, NAOliInfoImpl naOliInfo, LocationNumberCapImpl chargeNumber,
            OriginalCalledNumberCapImpl originalCalledPartyID, CallingPartyNumberCapImpl callingPartyNumber) throws CAPException;

    Long addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsImpl assistingSSPIPRoutingAddress,
            DigitsImpl correlationID, ScfIDImpl scfID, CAPExtensionsImpl extensions, CarrierImpl carrier,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, Integer callSegmentID, NAOliInfoImpl naOliInfo,
            LocationNumberCapImpl chargeNumber, OriginalCalledNumberCapImpl originalCalledPartyID,
            CallingPartyNumberCapImpl callingPartyNumber) throws CAPException;

    Long addEstablishTemporaryConnectionRequest(DigitsImpl assistingSSPIPRoutingAddress, DigitsImpl correlationID, ScfIDImpl scfID,
            CAPExtensionsImpl extensions, CarrierImpl carrier, ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
            NAOliInfoImpl naOliInfo, LocationNumberCapImpl chargeNumber,
            OriginalCalledNumberCapImpl originalCalledPartyID, CallingPartyNumberCapImpl callingPartyNumber) throws CAPException;

    Long addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsImpl assistingSSPIPRoutingAddress,
            DigitsImpl correlationID, ScfIDImpl scfID, CAPExtensionsImpl extensions, CarrierImpl carrier,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, NAOliInfoImpl naOliInfo,
            LocationNumberCapImpl chargeNumber, OriginalCalledNumberCapImpl originalCalledPartyID,
            CallingPartyNumberCapImpl callingPartyNumber) throws CAPException;

    Long addDisconnectForwardConnectionRequest() throws CAPException;

    Long addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws CAPException;

    Long addDisconnectForwardConnectionWithArgumentRequest(
            Integer callSegmentID, CAPExtensionsImpl extensions)
            throws CAPException;

    Long addDisconnectForwardConnectionWithArgumentRequest(
            int customInvokeTimeout, Integer callSegmentID,
            CAPExtensionsImpl extensions) throws CAPException;

    Long addDisconnectLegRequest(LegID logToBeReleased, CauseCapImpl releaseCause,
            CAPExtensionsImpl extensions) throws CAPException;

    Long addDisconnectLegRequest(int customInvokeTimeout,
    		LegID logToBeReleased, CauseCapImpl releaseCause,
            CAPExtensionsImpl extensions) throws CAPException;

    void addDisconnectLegResponse(long invokeId) throws CAPException;

    Long addConnectToResourceRequest(CalledPartyNumberCapImpl resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPExtensionsImpl extensions,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException;

    Long addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberCapImpl resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPExtensionsImpl extensions,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException;

    Long addResetTimerRequest(TimerID timerID, int timerValue, CAPExtensionsImpl extensions, Integer callSegmentID)
            throws CAPException;

    Long addResetTimerRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPExtensionsImpl extensions,
            Integer callSegmentID) throws CAPException;

    Long addFurnishChargingInformationRequest(FCIBCCCAMELSequence1Impl FCIBCCCAMELsequence1) throws CAPException;

    Long addFurnishChargingInformationRequest(int customInvokeTimeout, FCIBCCCAMELSequence1Impl FCIBCCCAMELsequence1)
            throws CAPException;

    Long addSendChargingInformationRequest(SCIBillingChargingCharacteristicsImpl sciBillingChargingCharacteristics,
    		LegType partyToCharge, CAPExtensionsImpl extensions) throws CAPException;

    Long addSendChargingInformationRequest(int customInvokeTimeout,
            SCIBillingChargingCharacteristicsImpl sciBillingChargingCharacteristics, LegType partyToCharge,
            CAPExtensionsImpl extensions) throws CAPException;

    Long addSpecializedResourceReportRequest_CapV23(Long linkedId) throws CAPException;

    Long addSpecializedResourceReportRequest_CapV4(Long linkedId, boolean isAllAnnouncementsComplete,
            boolean isFirstAnnouncementStarted) throws CAPException;

    Long addSpecializedResourceReportRequest_CapV23(Long linkedId, int customInvokeTimeout) throws CAPException;

    Long addSpecializedResourceReportRequest_CapV4(Long linkedId, int customInvokeTimeout,
            boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted) throws CAPException;

    Long addPlayAnnouncementRequest(InformationToSendImpl informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPExtensionsImpl extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException;

    Long addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSendImpl informationToSend,
            Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification, CAPExtensionsImpl extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException;

    Long addPromptAndCollectUserInformationRequest(CollectedInfoImpl collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSendImpl informationToSend, CAPExtensionsImpl extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException;

    Long addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfoImpl collectedInfo,
            Boolean disconnectFromIPForbidden, InformationToSendImpl informationToSend, CAPExtensionsImpl extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException;

    void addPromptAndCollectUserInformationResponse_DigitsResponse(long invokeId, DigitsImpl digitsResponse)
            throws CAPException;

    Long addCancelRequest_InvokeId(Integer invokeID) throws CAPException;

    Long addCancelRequest_AllRequests() throws CAPException;

    Long addCancelRequest_CallSegmentToCancel(CallSegmentToCancelImpl callSegmentToCancel) throws CAPException;

    Long addCancelRequest_InvokeId(int customInvokeTimeout, Integer invokeID) throws CAPException;

    Long addCancelRequest_AllRequests(int customInvokeTimeout) throws CAPException;

    Long addCancelRequest_CallSegmentToCancel(int customInvokeTimeout, CallSegmentToCancelImpl callSegmentToCancel)
            throws CAPException;

    Long addInitiateCallAttemptRequest(
            DestinationRoutingAddressImpl destinationRoutingAddress,
            CAPExtensionsImpl extensions, LegID legToBeCreated,
            Integer newCallSegment, CallingPartyNumberCapImpl callingPartyNumber,
            CallReferenceNumberImpl callReferenceNumber,
            ISDNAddressStringImpl gsmSCFAddress, boolean suppressTCsi)
            throws CAPException;

    Long addInitiateCallAttemptRequest(int customInvokeTimeout,
            DestinationRoutingAddressImpl destinationRoutingAddress,
            CAPExtensionsImpl extensions, LegID legToBeCreated,
            Integer newCallSegment, CallingPartyNumberCapImpl callingPartyNumber,
            CallReferenceNumberImpl callReferenceNumber,
            ISDNAddressStringImpl gsmSCFAddress, boolean suppressTCsi)
            throws CAPException;

    void addInitiateCallAttemptResponse(long invokeId,
            SupportedCamelPhasesImpl supportedCamelPhases,
            OfferedCamel4FunctionalitiesImpl offeredCamel4Functionalities,
            CAPExtensionsImpl extensions, boolean releaseCallArgExtensionAllowed)
            throws CAPException;

    Long addContinueWithArgumentRequest(AlertingPatternCapImpl alertingPattern,
            CAPExtensionsImpl extensions,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
            CallingPartysCategoryInap callingPartysCategory,
            List<GenericNumberCapImpl> genericNumbers,
            CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess,
            LocationNumberCapImpl chargeNumber, CarrierImpl carrier,
            boolean suppressionOfAnnouncement, NAOliInfoImpl naOliInfo,
            boolean borInterrogationRequested, boolean suppressOCsi,
            ContinueWithArgumentArgExtensionImpl continueWithArgumentArgExtension)
            throws CAPException;

    Long addContinueWithArgumentRequest(int customInvokeTimeout,
            AlertingPatternCapImpl alertingPattern, CAPExtensionsImpl extensions,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
            CallingPartysCategoryInap callingPartysCategory,
            List<GenericNumberCapImpl> genericNumbers,
            CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess,
            LocationNumberCapImpl chargeNumber, CarrierImpl carrier,
            boolean suppressionOfAnnouncement, NAOliInfoImpl naOliInfo,
            boolean borInterrogationRequested, boolean suppressOCsi,
            ContinueWithArgumentArgExtensionImpl continueWithArgumentArgExtension)
            throws CAPException;

    Long addMoveLegRequest(LegID logIDToMove, CAPExtensionsImpl extensions)
            throws CAPException;

    Long addMoveLegRequest(int customInvokeTimeout, LegID logIDToMove,
            CAPExtensionsImpl extensions) throws CAPException;

    void addMoveLegResponse(long invokeId) throws CAPException;

    Long addCollectInformationRequest() throws CAPException;

    Long addCollectInformationRequest(int customInvokeTimeout) throws CAPException;

    Long addSplitLegRequest(LegID legIDToSplit, Integer newCallSegmentId, CAPExtensionsImpl extensions) throws CAPException;

    Long addSplitLegRequest(int customInvokeTimeout, LegID legIDToSplit, Integer newCallSegmentId,
            CAPExtensionsImpl extensions) throws CAPException;

    void addSplitLegResponse(long invokeId) throws CAPException;

    Long addCallGapRequest(GapCriteriaImpl gapCriteria, GapIndicatorsImpl gapIndicators,
                           ControlType controlType, GapTreatmentImpl gapTreatment,
                           CAPExtensionsImpl capExtensions) throws CAPException;

    Long addCallGapRequest(int customInvokeTimeout, GapCriteriaImpl gapCriteria,
                           GapIndicatorsImpl gapIndicators, ControlType controlType,
                           GapTreatmentImpl gapTreatment, CAPExtensionsImpl capExtensions) throws CAPException;

}