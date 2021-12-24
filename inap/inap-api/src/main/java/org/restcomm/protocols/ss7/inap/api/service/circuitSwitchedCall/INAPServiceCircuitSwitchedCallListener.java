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

import org.restcomm.protocols.ss7.inap.api.INAPServiceListener;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface INAPServiceCircuitSwitchedCallListener extends INAPServiceListener {

	void onActivateServiceFilteringRequest(ActivateServiceFilteringRequest ind);

	void onEventNotificationChargingRequest(EventNotificationChargingRequest ind);

	void onRequestNotificationChargingEventRequest(RequestNotificationChargingEventRequest ind);

	void onServiceFilteringResponse(ServiceFilteringResponse ind);

    void onInitialDPRequest(InitialDPRequest ind);

    void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind);

    void onApplyChargingRequest(ApplyChargingRequest ind);

    void onEventReportBCSMRequest(EventReportBCSMRequest ind);

    void onContinueRequest(ContinueRequest ind);

    void onApplyChargingReportRequest(ApplyChargingReportRequest ind);

    void onReleaseCallRequest(ReleaseCallRequest ind);

    void onConnectRequest(ConnectRequest ind);

    void onCallInformationRequestRequest(CallInformationRequest ind);

    void onCallInformationReportRequest(CallInformationReportRequest ind);

    void onActivityTestRequest(ActivityTestRequest ind);

    void onActivityTestResponse(ActivityTestResponse ind);

    void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind);

    void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind);

    void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind);

    void onConnectToResourceRequest(ConnectToResourceRequest ind);

    void onResetTimerRequest(ResetTimerRequest ind);

    void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind);

    void onSendChargingInformationRequest(SendChargingInformationRequest ind);

    void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind);

    void onPlayAnnouncementRequest(PlayAnnouncementRequest ind);

    void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind);

    void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind);

    void onCancelRequest(CancelRequest ind);

    void onInitiateCallAttemptRequest(InitiateCallAttemptRequest initiateCallAttemptRequest);

    void onCollectInformationRequest(CollectInformationRequest ind);

    void onCallGapRequest(CallGapRequest ind);
    
    void onAnalysedInformationRequest(AnalysedInformationRequest ind);

    void onAnalyseInformationRequest(AnalyseInformationRequest ind);

    void onCancelStatusReportRequest(CancelStatusReportRequest ind);
    
    void onCollectedInformationRequest(CollectedInformationRequest ind);
    
    void onHoldCallInNetworkRequest(HoldCallInNetworkRequest ind);
    
    void onOMidCallRequest(OMidCallRequest ind);
    
    void onTMidCallRequest(TMidCallRequest ind);
    
    void onOAnswerRequest(OAnswerRequest ind);
    
    void onOriginationAttemptAuthorizedRequest(OriginationAttemptAuthorizedRequest ind);
    
    void onRouteSelectFailureRequest(RouteSelectFailureRequest ind);
    
    void onOCalledPartyBusyRequest(OCalledPartyBusyRequest ind);
    
    void onONoAnswerRequest(ONoAnswerRequest ind);
    
    void onODisconnectRequest(ODisconnectRequest ind);
    
    void onTermAttemptAuthorizedRequest(TermAttemptAuthorizedRequest ind);
    
    void onTBusyRequest(TBusyRequest ind);
    
    void onTNoAnswerRequest(TNoAnswerRequest ind);
    
    void onTAnswerRequest(TNoAnswerRequest ind);
    
    void onTDisconnectRequest(TDisconnectRequest ind);
    
    void onSelectRouteRequest(SelectRouteRequest ind);
    
    void onSelectFacilityRequest(SelectFacilityRequest ind);
    
    void onRequestCurrentStatusReportRequest(RequestCurrentStatusReportRequest ind);
    
    void onRequestCurrentStatusReportResponse(RequestCurrentStatusReportResponse ind);
    
    void onRequestEveryStatusChangeReportRequest(RequestEveryStatusChangeReportRequest ind);
    
    void onRequestFirstStatusMatchReportRequest(RequestFirstStatusMatchReportRequest ind);
    
    void onStatusReportRequest(StatusReportRequest ind);
    
    void onUpdateRequest(UpdateRequest ind);
    
    void onUpdateResponse(UpdateResponse ind);
    
    void onRetrieveRequest(RetrieveRequest ind);
    
    void onRetrieveResponse(RetrieveResponse ind);
    
    void onSignalingInformationRequest(SignallingInformationRequest ind);
    
    void onReleaseCallPartyConnectionRequest(ReleaseCallPartyConnectionRequest ind);
    
    void onReconnectRequest(ReconnectRequest ind);
    
    void onHoldCallPartyConnectionRequest(HoldCallPartyConnectionRequest ind);  
        
    void onHandOverRequest(HandOverRequest ind);  
    
    void onDialogueUserInformationRequest(DialogueUserInformationRequest ind);   
    
    void onCallLimitRequest(CallLimitRequest ind);
    
    void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind);             
}