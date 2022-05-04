/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.inap.functional;

/**
 *
 * @author yulian.oifa
 */
public enum EventType {
    // Dialog EventType
    DialogDelimiter, DialogRequest, DialogAccept, DialogUserAbort, DialogProviderAbort, DialogClose, DialogNotice, DialogRelease, DialogTimeout, // DialogReject,

    // Service EventType
    ErrorComponent, ProviderErrorComponent, RejectComponent, InvokeTimeout,

    // CircuitSwitchedCall EventType
    InitialDpRequest, ApplyChargingReportRequest, ApplyChargingRequest, CallInformationReportRequest, CallInformationRequest, ConnectRequest, ContinueRequest, ContinueWithArgumentRequest, EventReportBCSMRequest, RequestReportBCSMEventRequest, ReleaseCallRequest, ActivityTestRequest, ActivityTestResponse, AssistRequestInstructionsRequest, EstablishTemporaryConnectionRequest, DisconnectForwardConnectionRequest, ConnectToResourceRequest, ResetTimerRequest, FurnishChargingInformationRequest, SendChargingInformationRequest, SpecializedResourceReportRequest, PlayAnnouncementRequest, PromptAndCollectUserInformationRequest, PromptAndCollectUserInformationResponse, CancelRequest, DisconnectForwardConnectionWithArgumentRequest, InitiateCallAttemptRequest, CollectInformationRequest, CallGapRequest,
    ActivateServiceFilteringRequest, EventNotificationChargingRequest, RequestNotificationChargingEventRequest, ServiceFilteringResponseRequest, AnalysedInformationRequest, AnalyseInformationRequest, CancelStatusReportRequest, CollectedInformationRequest, HoldCallInNetworkRequest, OMidCallRequest, TMidCallRequest, OAnswerRequest, OriginationAttemptAuthorizedRequest, RouteSelectFailureRequest, OCalledPartyBusyRequest, ONoAnswerRequest, ODisconnectRequest, TermAttemptAuthorizedRequest, TBusyRequest, TNoAnswerRequest, TAnswerRequest, TDisconnectRequest, SelectRouteRequest, SelectFacilityRequest, RequestCurrentStatusReportRequest, RequestCurrentStatusReportResponse, RequestEveryStatusChangeReportRequest, RequestFirstStatusMatchReportRequest, StatusReportRequest, UpdateRequest, UpdateResponse, 
    RetrieveRequest, RetrieveResponse, SignallingInformationRequest, ReleaseCallPartyConnectionRequest, ReleaseCallPartyConnectionResponse, ReconnectRequest, HoldCallPartyConnectionRequest, HandOverRequest, DialogueUserInformationRequest, CallLimitRequest   
}
