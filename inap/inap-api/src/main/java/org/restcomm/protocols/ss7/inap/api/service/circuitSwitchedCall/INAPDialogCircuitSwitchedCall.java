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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAMELAChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallSegmentToCancel;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ContinueWithArgumentArgExtension;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.FCIBCCCAMELSequence1;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.NAOliInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.SCIBillingChargingCharacteristics;
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
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGIndex;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGInterlock;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.OfferedCamel4Functionalities;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ChargingEvent;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CounterAndValue;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteredCallTreatment;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCharacteristics;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCriteria;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringTimeOut;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;

/**
 *
 * @author yulian.oifa
 *
 */
public interface INAPDialogCircuitSwitchedCall extends INAPDialog {

    Long addInitialDPRequest(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
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
            throws INAPException;

    Long addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberIsup calledPartyNumber,
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
            throws INAPException;

    Long addApplyChargingReportRequest(TimeDurationChargingResult timeDurationChargingResult) throws INAPException;

    Long addApplyChargingReportRequest(int customInvokeTimeout, TimeDurationChargingResult timeDurationChargingResult)
            throws INAPException;

    Long addApplyChargingRequest(CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics,
            LegType partyToCharge, CAPINAPExtensions extensions, AChChargingAddress aChChargingAddress) throws INAPException;

    Long addApplyChargingRequest(int customInvokeTimeout,
            CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics, LegType partyToCharge,
            CAPINAPExtensions extensions, AChChargingAddress aChChargingAddress) throws INAPException;

    Long addCallInformationReportRequest(List<RequestedInformation> requestedInformationList,
            CAPINAPExtensions extensions, LegType legID) throws INAPException;

    Long addCallInformationReportRequest(int customInvokeTimeout,
            List<RequestedInformation> requestedInformationList, CAPINAPExtensions extensions, LegType legID)
            throws INAPException;

    Long addCallInformationRequestRequest(List<RequestedInformationType> requestedInformationTypeList,
            CAPINAPExtensions extensions, LegType legID) throws INAPException;

    Long addCallInformationRequestRequest(int customInvokeTimeout,
            List<RequestedInformationType> requestedInformationTypeList, CAPINAPExtensions extensions, LegType legID)
            throws INAPException;

    Long addConnectRequest(DestinationRoutingAddress destinationRoutingAddress, AlertingPatternWrapper alertingPattern,
            OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions, Carrier carrier,
            CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, List<GenericNumberIsup> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberIsup chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws INAPException;

    Long addConnectRequest(int customInvokeTimeout, DestinationRoutingAddress destinationRoutingAddress,
            AlertingPatternWrapper alertingPattern, OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions,
            Carrier carrier, CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, List<GenericNumberIsup> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberIsup chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws INAPException;

    Long addContinueRequest() throws INAPException;

    Long addContinueRequest(int customInvokeTimeout) throws INAPException;

    Long addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, LegType legID, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws INAPException;

    Long addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, LegType legID, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws INAPException;

    Long addRequestReportBCSMEventRequest(List<BCSMEvent> bcsmEventList, CAPINAPExtensions extensions)
            throws INAPException;

    Long addRequestReportBCSMEventRequest(int customInvokeTimeout, List<BCSMEvent> bcsmEventList,
            CAPINAPExtensions extensions) throws INAPException;

    Long addReleaseCallRequest(CauseIsup cause) throws INAPException;

    Long addReleaseCallRequest(int customInvokeTimeout, CauseIsup cause) throws INAPException;

    Long addActivityTestRequest() throws INAPException;

    Long addActivityTestRequest(int customInvokeTimeout) throws INAPException;

    void addActivityTestResponse(long invokeId) throws INAPException;

    Long addAssistRequestInstructionsRequest(DigitsIsup correlationID, IPSSPCapabilities ipSSPCapabilities,
            CAPINAPExtensions extensions) throws INAPException;

    Long addAssistRequestInstructionsRequest(int customInvokeTimeout, DigitsIsup correlationID,
            IPSSPCapabilities ipSSPCapabilities, CAPINAPExtensions extensions) throws INAPException;

    Long addEstablishTemporaryConnectionRequest(DigitsIsup assistingSSPIPRoutingAddress, DigitsIsup correlationID, ScfID scfID,
            CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            Integer callSegmentID, NAOliInfo naOliInfo, LocationNumberIsup chargeNumber,
            OriginalCalledNumberIsup originalCalledPartyID, CallingPartyNumberIsup callingPartyNumber) throws INAPException;

    Long addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsIsup assistingSSPIPRoutingAddress,
            DigitsIsup correlationID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID, NAOliInfo naOliInfo,
            LocationNumberIsup chargeNumber, OriginalCalledNumberIsup originalCalledPartyID,
            CallingPartyNumberIsup callingPartyNumber) throws INAPException;

    Long addEstablishTemporaryConnectionRequest(DigitsIsup assistingSSPIPRoutingAddress, DigitsIsup correlationID, ScfID scfID,
            CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            NAOliInfo naOliInfo, LocationNumberIsup chargeNumber,
            OriginalCalledNumberIsup originalCalledPartyID, CallingPartyNumberIsup callingPartyNumber) throws INAPException;

    Long addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsIsup assistingSSPIPRoutingAddress,
            DigitsIsup correlationID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, NAOliInfo naOliInfo,
            LocationNumberIsup chargeNumber, OriginalCalledNumberIsup originalCalledPartyID,
            CallingPartyNumberIsup callingPartyNumber) throws INAPException;

    Long addDisconnectForwardConnectionRequest() throws INAPException;

    Long addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws INAPException;

    Long addDisconnectForwardConnectionWithArgumentRequest(
            Integer callSegmentID, CAPINAPExtensions extensions)
            throws INAPException;

    Long addDisconnectForwardConnectionWithArgumentRequest(
            int customInvokeTimeout, Integer callSegmentID,
            CAPINAPExtensions extensions) throws INAPException;

    Long addConnectToResourceRequest(CalledPartyNumberIsup resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws INAPException;

    Long addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberIsup resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws INAPException;

    Long addFurnishChargingInformationRequest(FCIBCCCAMELSequence1 FCIBCCCAMELsequence1) throws INAPException;

    Long addFurnishChargingInformationRequest(int customInvokeTimeout, FCIBCCCAMELSequence1 FCIBCCCAMELsequence1)
            throws INAPException;

    Long addSendChargingInformationRequest(SCIBillingChargingCharacteristics sciBillingChargingCharacteristics,
    		LegType partyToCharge, CAPINAPExtensions extensions) throws INAPException;

    Long addSendChargingInformationRequest(int customInvokeTimeout,
            SCIBillingChargingCharacteristics sciBillingChargingCharacteristics, LegType partyToCharge,
            CAPINAPExtensions extensions) throws INAPException;

    Long addSpecializedResourceReportRequest(Long linkedId) throws INAPException;

    Long addPlayAnnouncementRequest(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws INAPException;

    Long addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSend informationToSend,
            Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws INAPException;

    Long addPromptAndCollectUserInformationRequest(CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPINAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws INAPException;

    Long addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfo collectedInfo,
            Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPINAPExtensions extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws INAPException;

    void addPromptAndCollectUserInformationResponse_DigitsResponse(long invokeId, DigitsIsup digitsResponse)
            throws INAPException;

    Long addCancelRequest_InvokeId(Integer invokeID) throws INAPException;

    Long addCancelRequest_AllRequests() throws INAPException;

    Long addCancelRequest_CallSegmentToCancel(CallSegmentToCancel callSegmentToCancel) throws INAPException;

    Long addCancelRequest_InvokeId(int customInvokeTimeout, Integer invokeID) throws INAPException;

    Long addCancelRequest_AllRequests(int customInvokeTimeout) throws INAPException;

    Long addCancelRequest_CallSegmentToCancel(int customInvokeTimeout, CallSegmentToCancel callSegmentToCancel)
            throws INAPException;

    Long addInitiateCallAttemptRequest(
            DestinationRoutingAddress destinationRoutingAddress,
            CAPINAPExtensions extensions, LegID legToBeCreated,
            Integer newCallSegment, CallingPartyNumberIsup callingPartyNumber,
            CallReferenceNumber callReferenceNumber,
            ISDNAddressString gsmSCFAddress, boolean suppressTCsi)
            throws INAPException;

    Long addInitiateCallAttemptRequest(int customInvokeTimeout,
            DestinationRoutingAddress destinationRoutingAddress,
            CAPINAPExtensions extensions, LegID legToBeCreated,
            Integer newCallSegment, CallingPartyNumberIsup callingPartyNumber,
            CallReferenceNumber callReferenceNumber,
            ISDNAddressString gsmSCFAddress, boolean suppressTCsi)
            throws INAPException;

    void addInitiateCallAttemptResponse(long invokeId,
            SupportedCamelPhases supportedCamelPhases,
            OfferedCamel4Functionalities offeredCamel4Functionalities,
            CAPINAPExtensions extensions, boolean releaseCallArgExtensionAllowed)
            throws INAPException;

    Long addContinueWithArgumentRequest(AlertingPatternWrapper alertingPattern,
            CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryIsup callingPartysCategory,
            List<GenericNumberIsup> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess,
            LocationNumberIsup chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo,
            boolean borInterrogationRequested, boolean suppressOCsi,
            ContinueWithArgumentArgExtension continueWithArgumentArgExtension)
            throws INAPException;

    Long addContinueWithArgumentRequest(int customInvokeTimeout,
            AlertingPatternWrapper alertingPattern, CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryIsup callingPartysCategory,
            List<GenericNumberIsup> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess,
            LocationNumberIsup chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo,
            boolean borInterrogationRequested, boolean suppressOCsi,
            ContinueWithArgumentArgExtension continueWithArgumentArgExtension)
            throws INAPException;

    Long addCollectInformationRequest() throws INAPException;

    Long addCollectInformationRequest(int customInvokeTimeout) throws INAPException;

    Long addCallGapRequest(GapCriteria gapCriteria, GapIndicators gapIndicators,
                           ControlType controlType, GapTreatment gapTreatment,
                           CAPINAPExtensions capExtensions) throws INAPException;

    Long addCallGapRequest(int customInvokeTimeout, GapCriteria gapCriteria,
                           GapIndicators gapIndicators, ControlType controlType,
                           GapTreatment gapTreatment, CAPINAPExtensions capExtensions) throws INAPException;

    Long addActivateServiceFiltering(FilteredCallTreatment filteredCallTreatment, 
    						FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut, 
    						FilteringCriteria filteringCriteria, DateAndTime startTime, 
    						CAPINAPExtensions extensions) throws INAPException;

    Long addActivateServiceFiltering(int customInvokeTimeout, FilteredCallTreatment filteredCallTreatment, 
    						FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut, 
    						FilteringCriteria filteringCriteria, DateAndTime startTime, 
    						CAPINAPExtensions extensions) throws INAPException;
    
    Long addEventNotificationCharging(byte[] eventTypeCharging,  byte[] eventSpecificInformationCharging, 
    						LegID legID, CAPINAPExtensions extensions, MonitorMode monitorMode) throws INAPException;

    Long addEventNotificationCharging(int customInvokeTimeout,byte[] eventTypeCharging, 
    						byte[] eventSpecificInformationCharging, LegID legID, CAPINAPExtensions extensions, 
    						MonitorMode monitorMode) throws INAPException;
    
    Long addRequestNotificationChargingEvent(List<ChargingEvent> chargingEventList) throws INAPException;

    Long addRequestNotificationChargingEvent(int customInvokeTimeout,List<ChargingEvent> chargingEventList) throws INAPException;
    
    Long addServiceFilteringResponse(List<CounterAndValue> counterAndValue, FilteringCriteria filteringCriteria, 
    						CAPINAPExtensions extensions) throws INAPException;

    Long addServiceFilteringResponse(int customInvokeTimeout,List<CounterAndValue> counterAndValue, 
    						FilteringCriteria filteringCriteria, CAPINAPExtensions extensions) throws INAPException;
}