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

package org.restcomm.protocols.ss7.inap.functional;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author tamas gyorgyey
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
    RetrieveRequest, RetrieveResponse, SignallingInformationRequest, ReleaseCallPartyConnectionRequest, ReconnectRequest, HoldCallPartyConnectionRequest, HandOverRequest, DialogueUserInformationRequest, CallLimitRequest   
}
