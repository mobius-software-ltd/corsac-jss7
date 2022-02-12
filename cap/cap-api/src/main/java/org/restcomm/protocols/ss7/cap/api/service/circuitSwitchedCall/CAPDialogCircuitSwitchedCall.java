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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallSegmentToCancel;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ContinueWithArgumentArgExtension;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.FCIBCCCAMELSequence1;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.NAOliInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeDurationChargingResult;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapIndicators;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapTreatment;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.GenericNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AChChargingAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGIndex;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGInterlock;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.OfferedCamel4Functionalities;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;

/**
 *
 * @author sergey vetyutnev
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 *
 */
public interface CAPDialogCircuitSwitchedCall extends CAPDialog {

    Integer addInitialDPRequest(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions,
            HighLayerCompatibilityIsup highLayerCompatibility, DigitsIsup additionalCallingPartyNumber,
            BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, CauseIsup cause,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
            LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
            CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress, CalledPartyBCDNumber calledPartyBCDNumber,
            TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtension initialDPArgExtension)
            throws CAPException;

    Integer addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions,
            HighLayerCompatibilityIsup highLayerCompatibility, DigitsIsup additionalCallingPartyNumber,
            BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, CauseIsup cause,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
            LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
            CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress, CalledPartyBCDNumber calledPartyBCDNumber,
            TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtension initialDPArgExtension)
            throws CAPException;

    Integer addApplyChargingReportRequest(TimeDurationChargingResult timeDurationChargingResult) throws CAPException;

    Integer addApplyChargingReportRequest(int customInvokeTimeout, TimeDurationChargingResult timeDurationChargingResult)
            throws CAPException;

    Integer addApplyChargingRequest(CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics,
            LegType partyToCharge, CAPINAPExtensions extensions, AChChargingAddress aChChargingAddress) throws CAPException;

    Integer addApplyChargingRequest(int customInvokeTimeout,
            CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics, LegType partyToCharge,
            CAPINAPExtensions extensions, AChChargingAddress aChChargingAddress) throws CAPException;

    Integer addCallInformationReportRequest(List<RequestedInformation> requestedInformationList,
            CAPINAPExtensions extensions, LegType legID) throws CAPException;

    Integer addCallInformationReportRequest(int customInvokeTimeout,
            List<RequestedInformation> requestedInformationList, CAPINAPExtensions extensions, LegType legID)
            throws CAPException;

    Integer addCallInformationRequestRequest(List<RequestedInformationType> requestedInformationTypeList,
            CAPINAPExtensions extensions, LegType legID) throws CAPException;

    Integer addCallInformationRequestRequest(int customInvokeTimeout,
            List<RequestedInformationType> requestedInformationTypeList, CAPINAPExtensions extensions, LegType legID)
            throws CAPException;

    Integer addConnectRequest(DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern,
            OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions, Carrier carrier,
            CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, List<GenericNumberIsup> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberIsup chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException;

    Integer addConnectRequest(int customInvokeTimeout, DestinationRoutingAddress destinationRoutingAddress,
            AlertingPattern alertingPattern, OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions,
            Carrier carrier, CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, List<GenericNumberIsup> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberIsup chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException;

    Integer addContinueRequest() throws CAPException;

    Integer addContinueRequest(int customInvokeTimeout) throws CAPException;

    Integer addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, LegType legID, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws CAPException;

    Integer addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, LegType legID, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws CAPException;

    Integer addRequestReportBCSMEventRequest(List<BCSMEvent> bcsmEventList, CAPINAPExtensions extensions)
            throws CAPException;

    Integer addRequestReportBCSMEventRequest(int customInvokeTimeout, List<BCSMEvent> bcsmEventList,
            CAPINAPExtensions extensions) throws CAPException;

    Integer addReleaseCallRequest(CauseIsup cause) throws CAPException;

    Integer addReleaseCallRequest(int customInvokeTimeout, CauseIsup cause) throws CAPException;

    Integer addActivityTestRequest() throws CAPException;

    Integer addActivityTestRequest(int customInvokeTimeout) throws CAPException;

    void addActivityTestResponse(int invokeId) throws CAPException;

    Integer addAssistRequestInstructionsRequest(DigitsIsup correlationID, IPSSPCapabilities ipSSPCapabilities,
            CAPINAPExtensions extensions) throws CAPException;

    Integer addAssistRequestInstructionsRequest(int customInvokeTimeout, DigitsIsup correlationID,
            IPSSPCapabilities ipSSPCapabilities, CAPINAPExtensions extensions) throws CAPException;

    Integer addEstablishTemporaryConnectionRequest(DigitsIsup assistingSSPIPRoutingAddress, DigitsIsup correlationID, ScfID scfID,
            CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            Integer callSegmentID, NAOliInfo naOliInfo, LocationNumberIsup chargeNumber,
            OriginalCalledNumberIsup originalCalledPartyID, CallingPartyNumberIsup callingPartyNumber) throws CAPException;

    Integer addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsIsup assistingSSPIPRoutingAddress,
            DigitsIsup correlationID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID, NAOliInfo naOliInfo,
            LocationNumberIsup chargeNumber, OriginalCalledNumberIsup originalCalledPartyID,
            CallingPartyNumberIsup callingPartyNumber) throws CAPException;

    Integer addEstablishTemporaryConnectionRequest(DigitsIsup assistingSSPIPRoutingAddress, DigitsIsup correlationID, ScfID scfID,
            CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            NAOliInfo naOliInfo, LocationNumberIsup chargeNumber,
            OriginalCalledNumberIsup originalCalledPartyID, CallingPartyNumberIsup callingPartyNumber) throws CAPException;

    Integer addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsIsup assistingSSPIPRoutingAddress,
            DigitsIsup correlationID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, NAOliInfo naOliInfo,
            LocationNumberIsup chargeNumber, OriginalCalledNumberIsup originalCalledPartyID,
            CallingPartyNumberIsup callingPartyNumber) throws CAPException;

    Integer addDisconnectForwardConnectionRequest() throws CAPException;

    Integer addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws CAPException;

    Integer addDisconnectForwardConnectionWithArgumentRequest(
            Integer callSegmentID, CAPINAPExtensions extensions)
            throws CAPException;

    Integer addDisconnectForwardConnectionWithArgumentRequest(
            int customInvokeTimeout, Integer callSegmentID,
            CAPINAPExtensions extensions) throws CAPException;

    Integer addDisconnectLegRequest(LegID logToBeReleased, CauseIsup releaseCause,
            CAPINAPExtensions extensions) throws CAPException;

    Integer addDisconnectLegRequest(int customInvokeTimeout,
    		LegID logToBeReleased, CauseIsup releaseCause,
            CAPINAPExtensions extensions) throws CAPException;

    void addDisconnectLegResponse(int invokeId) throws CAPException;

    Integer addConnectToResourceRequest(CalledPartyNumberIsup resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException;

    Integer addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberIsup resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException;

    Integer addResetTimerRequest(TimerID timerID, int timerValue, CAPINAPExtensions extensions, Integer callSegmentID)
            throws CAPException;

    Integer addResetTimerRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPINAPExtensions extensions,
            Integer callSegmentID) throws CAPException;

    Integer addFurnishChargingInformationRequest(FCIBCCCAMELSequence1 FCIBCCCAMELsequence1) throws CAPException;

    Integer addFurnishChargingInformationRequest(int customInvokeTimeout, FCIBCCCAMELSequence1 FCIBCCCAMELsequence1)
            throws CAPException;

    Integer addSendChargingInformationRequest(SCIBillingChargingCharacteristics sciBillingChargingCharacteristics,
    		LegType partyToCharge, CAPINAPExtensions extensions) throws CAPException;

    Integer addSendChargingInformationRequest(int customInvokeTimeout,
            SCIBillingChargingCharacteristics sciBillingChargingCharacteristics, LegType partyToCharge,
            CAPINAPExtensions extensions) throws CAPException;

    Integer addSpecializedResourceReportRequest_CapV23(Integer linkedId) throws CAPException;

    Integer addSpecializedResourceReportRequest_CapV4(Integer linkedId, boolean isAllAnnouncementsComplete,
            boolean isFirstAnnouncementStarted) throws CAPException;

    Integer addSpecializedResourceReportRequest_CapV23(Integer linkedId, int customInvokeTimeout) throws CAPException;

    Integer addSpecializedResourceReportRequest_CapV4(Integer linkedId, int customInvokeTimeout,
            boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted) throws CAPException;

    Integer addPlayAnnouncementRequest(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException;

    Integer addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSend informationToSend,
            Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException;

    Integer addPromptAndCollectUserInformationRequest(CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPINAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException;

    Integer addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfo collectedInfo,
            Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPINAPExtensions extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException;

    void addPromptAndCollectUserInformationResponse_DigitsResponse(int invokeId, DigitsIsup digitsResponse)
            throws CAPException;

    Integer addCancelRequest_InvokeId(Integer invokeID) throws CAPException;

    Integer addCancelRequest_AllRequests() throws CAPException;

    Integer addCancelRequest_CallSegmentToCancel(CallSegmentToCancel callSegmentToCancel) throws CAPException;

    Integer addCancelRequest_InvokeId(int customInvokeTimeout, Integer invokeID) throws CAPException;

    Integer addCancelRequest_AllRequests(int customInvokeTimeout) throws CAPException;

    Integer addCancelRequest_CallSegmentToCancel(int customInvokeTimeout, CallSegmentToCancel callSegmentToCancel)
            throws CAPException;

    Integer addInitiateCallAttemptRequest(
            DestinationRoutingAddress destinationRoutingAddress,
            CAPINAPExtensions extensions, LegID legToBeCreated,
            Integer newCallSegment, CallingPartyNumberIsup callingPartyNumber,
            CallReferenceNumber callReferenceNumber,
            ISDNAddressString gsmSCFAddress, boolean suppressTCsi)
            throws CAPException;

    Integer addInitiateCallAttemptRequest(int customInvokeTimeout,
            DestinationRoutingAddress destinationRoutingAddress,
            CAPINAPExtensions extensions, LegID legToBeCreated,
            Integer newCallSegment, CallingPartyNumberIsup callingPartyNumber,
            CallReferenceNumber callReferenceNumber,
            ISDNAddressString gsmSCFAddress, boolean suppressTCsi)
            throws CAPException;

    void addInitiateCallAttemptResponse(int invokeId,
            SupportedCamelPhases supportedCamelPhases,
            OfferedCamel4Functionalities offeredCamel4Functionalities,
            CAPINAPExtensions extensions, boolean releaseCallArgExtensionAllowed)
            throws CAPException;

    Integer addContinueWithArgumentRequest(AlertingPattern alertingPattern,
            CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryIsup callingPartysCategory,
            List<GenericNumberIsup> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess,
            LocationNumberIsup chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo,
            boolean borInterrogationRequested, boolean suppressOCsi,
            ContinueWithArgumentArgExtension continueWithArgumentArgExtension)
            throws CAPException;

    Integer addContinueWithArgumentRequest(int customInvokeTimeout,
            AlertingPattern alertingPattern, CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryIsup callingPartysCategory,
            List<GenericNumberIsup> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess,
            LocationNumberIsup chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo,
            boolean borInterrogationRequested, boolean suppressOCsi,
            ContinueWithArgumentArgExtension continueWithArgumentArgExtension)
            throws CAPException;

    Integer addMoveLegRequest(LegID logIDToMove, CAPINAPExtensions extensions)
            throws CAPException;

    Integer addMoveLegRequest(int customInvokeTimeout, LegID logIDToMove,
            CAPINAPExtensions extensions) throws CAPException;

    void addMoveLegResponse(int invokeId) throws CAPException;

    Integer addCollectInformationRequest() throws CAPException;

    Integer addCollectInformationRequest(int customInvokeTimeout) throws CAPException;

    Integer addSplitLegRequest(LegID legIDToSplit, Integer newCallSegmentId, CAPINAPExtensions extensions) throws CAPException;

    Integer addSplitLegRequest(int customInvokeTimeout, LegID legIDToSplit, Integer newCallSegmentId,
            CAPINAPExtensions extensions) throws CAPException;

    void addSplitLegResponse(int invokeId) throws CAPException;

    Integer addCallGapRequest(GapCriteria gapCriteria, GapIndicators gapIndicators,
                           ControlType controlType, GapTreatment gapTreatment,
                           CAPINAPExtensions capExtensions) throws CAPException;

    Integer addCallGapRequest(int customInvokeTimeout, GapCriteria gapCriteria,
                           GapIndicators gapIndicators, ControlType controlType,
                           GapTreatment gapTreatment, CAPINAPExtensions capExtensions) throws CAPException;

}