package org.restcomm.protocols.ss7.inap;
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
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPDialogListener;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPMessage;
import org.restcomm.protocols.ss7.inap.api.INAPProvider;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.DisconnectSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPGeneralAbortReason;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPUserAbortReason;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessage;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ActivateServiceFilteringRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ActivityTestResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AnalyseInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AnalysedInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallGapRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallLimitRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CancelRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CancelStatusReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CollectInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CollectedInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ConnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ContinueRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.DialogueUserInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EventNotificationChargingRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.HandOverRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.HoldCallInNetworkRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.HoldCallPartyConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPServiceCircuitSwitchedCallListener;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.OAnswerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.OCalledPartyBusyRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ODisconnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.OMidCallRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ONoAnswerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.OriginationAttemptAuthorizedRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReconnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallPartyConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallPartyConnectionResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestCurrentStatusReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestCurrentStatusReportResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestEveryStatusChangeReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestFirstStatusMatchReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestNotificationChargingEventRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RetrieveRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RetrieveResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RouteSelectFailureRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SelectFacilityRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SelectRouteRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ServiceFilteringResponseRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SignallingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.StatusReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TAnswerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TBusyRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TDisconnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TMidCallRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TNoAnswerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TermAttemptAuthorizedRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.UpdateRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.UpdateResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
/**
 * 
 * @author yulianoifa
 *
 */
public class CallSsfExample implements INAPDialogListener, INAPServiceCircuitSwitchedCallListener {

    private INAPProvider inapProvider;
    private INAPDialogCircuitSwitchedCall currentCapDialog;
    private CallContent cc;

    public CallSsfExample() throws NamingException {
        InitialContext ctx = new InitialContext();
        try {
            String providerJndiName = "java:/restcomm/ss7/inap";
            this.inapProvider = ((INAPProvider) ctx.lookup(providerJndiName));
        } finally {
            ctx.close();
        }

        inapProvider.addINAPDialogListener(UUID.randomUUID(),this);
        inapProvider.getINAPServiceCircuitSwitchedCall().addINAPServiceListener(this);
    }

    public INAPProvider getINAPProvider() {
        return inapProvider;
    }

    public void start() {
        // Make the circuitSwitchedCall service activated
        inapProvider.getINAPServiceCircuitSwitchedCall().acivate();

        currentCapDialog = null;
    }

    public void stop() {
        inapProvider.getINAPServiceCircuitSwitchedCall().deactivate();
    }

    public void sendInitialDP(SccpAddress origAddress, SccpAddress remoteAddress, int serviceKey,
            CalledPartyNumberIsup calledPartyNumber, CallingPartyNumberIsup callingPartyNumber, LocationNumberIsup locationNumber,
            EventTypeBCSM eventTypeBCSM, LocationInformation locationInformation) throws INAPException {
        // First create Dialog
        INAPApplicationContext acn = INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B;
        currentCapDialog = inapProvider.getINAPServiceCircuitSwitchedCall().createNewDialog(acn, origAddress, remoteAddress, 0);

        currentCapDialog.addInitialDPRequest(serviceKey, callingPartyNumber, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        // This will initiate the TC-BEGIN with INVOKE component
        currentCapDialog.send();

        this.cc.step = Step.initialDPSent;
    }

    public void sendEventReportBCSM_OAnswer(AnswerSpecificInfo oAnswerSpecificInfo, LegType legID,
            MiscCallInfo miscCallInfo) throws INAPException {
        if (currentCapDialog != null && this.cc != null) {
            EventSpecificInformationBCSM eventSpecificInformationBCSM = this.inapProvider.getINAPParameterFactory()
                    .createEventSpecificInformationBCSM(oAnswerSpecificInfo,false);
            currentCapDialog.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, null, eventSpecificInformationBCSM, new LegIDImpl(legID,null),
                    miscCallInfo, null);
            currentCapDialog.send();
            this.cc.step = Step.answered;
        }
    }

    public void sendEventReportBCSM_ODisconnect(DisconnectSpecificInfo oDisconnectSpecificInfo, LegType legID,
            MiscCallInfo miscCallInfo) throws INAPException {
        if (currentCapDialog != null && this.cc != null) {
            EventSpecificInformationBCSM eventSpecificInformationBCSM = this.inapProvider.getINAPParameterFactory()
                    .createEventSpecificInformationBCSM(oDisconnectSpecificInfo,false);
            currentCapDialog.addEventReportBCSMRequest(EventTypeBCSM.oDisconnect, null, eventSpecificInformationBCSM, new LegIDImpl(legID,null),
                    miscCallInfo, null);
            currentCapDialog.send();
            this.cc.step = Step.disconnected;
        }
    }

    @Override
    public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            // initiating BCSM events processing
        }
        ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
    }

    @Override
    public void onActivityTestRequest(ActivityTestRequest ind) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            this.cc.activityTestInvokeId = ind.getInvokeId();
        }
    }

    @Override
    public void onActivityTestResponse(ActivityTestResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onContinueRequest(ContinueRequest ind) {
        this.cc.step = Step.callAllowed;
        ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
        // sending Continue to use the original calledPartyAddress
    }

    @Override
    public void onConnectRequest(ConnectRequest ind) {
        this.cc.step = Step.callAllowed;
        ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
        // sending Connect to force routing the call to a new number
    }

    @Override
    public void onDialogTimeout(INAPDialog inapDialog) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            // if the call is still up - keep the sialog alive
            currentCapDialog.keepAlive();
        }
    }

    @Override
    public void onDialogDelimiter(INAPDialog inapDialog) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
            if (this.cc.activityTestInvokeId != null) {
                try {
                    currentCapDialog.addActivityTestResponse(this.cc.activityTestInvokeId);
                    this.cc.activityTestInvokeId = null;
                    currentCapDialog.send();
                } catch (INAPException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onErrorComponent(INAPDialog inapDialog, Integer invokeId, INAPErrorMessage inapErrorMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem, boolean isLocalOriginated) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInvokeTimeout(INAPDialog inapDialog, Integer invokeId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onINAPMessage(INAPMessage inapMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitialDPRequest(InitialDPRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingRequest(ApplyChargingRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReleaseCallRequest(ReleaseCallRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCallGapRequest(CallGapRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCallInformationRequest(CallInformationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCallInformationReportRequest(CallInformationReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResetTimerRequest(ResetTimerRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendChargingInformationRequest(SendChargingInformationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelRequest(CancelRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRequest(INAPDialog inapDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogAccept(INAPDialog inapDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason, INAPUserAbortReason userReason) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogProviderAbort(INAPDialog inapDialog, PAbortCauseType abortCause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogClose(INAPDialog inapDialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogRelease(INAPDialog inapDialog) {
        this.currentCapDialog = null;
        this.cc = null;
    }

    @Override
    public void onDialogNotice(INAPDialog inapDialog, INAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        // TODO Auto-generated method stub

    }

    private enum Step {
        initialDPSent, callAllowed, answered, disconnected;
    }

    private class CallContent {
        public Step step;
        public Integer activityTestInvokeId;
    }

    @Override
    public void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitiateCallAttemptRequest(InitiateCallAttemptRequest initiateCallAttemptRequest) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCollectInformationRequest(CollectInformationRequest ind) {
        // TODO Auto-generated method stub
        
    }

	@Override
	public void onActivateServiceFilteringRequest(ActivateServiceFilteringRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventNotificationChargingRequest(EventNotificationChargingRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestNotificationChargingEventRequest(RequestNotificationChargingEventRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceFilteringResponseRequest(ServiceFilteringResponseRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnalysedInformationRequest(AnalysedInformationRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnalyseInformationRequest(AnalyseInformationRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelStatusReportRequest(CancelStatusReportRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCollectedInformationRequest(CollectedInformationRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHoldCallInNetworkRequest(HoldCallInNetworkRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOMidCallRequest(OMidCallRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTMidCallRequest(TMidCallRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOAnswerRequest(OAnswerRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOriginationAttemptAuthorizedRequest(OriginationAttemptAuthorizedRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRouteSelectFailureRequest(RouteSelectFailureRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOCalledPartyBusyRequest(OCalledPartyBusyRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onONoAnswerRequest(ONoAnswerRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onODisconnectRequest(ODisconnectRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTermAttemptAuthorizedRequest(TermAttemptAuthorizedRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTBusyRequest(TBusyRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTNoAnswerRequest(TNoAnswerRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTAnswerRequest(TAnswerRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTDisconnectRequest(TDisconnectRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSelectRouteRequest(SelectRouteRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSelectFacilityRequest(SelectFacilityRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestCurrentStatusReportRequest(RequestCurrentStatusReportRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestCurrentStatusReportResponse(RequestCurrentStatusReportResponse ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestEveryStatusChangeReportRequest(RequestEveryStatusChangeReportRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestFirstStatusMatchReportRequest(RequestFirstStatusMatchReportRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusReportRequest(StatusReportRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateRequest(UpdateRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateResponse(UpdateResponse ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRetrieveRequest(RetrieveRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRetrieveResponse(RetrieveResponse ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignallingInformationRequest(SignallingInformationRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReleaseCallPartyConnectionRequest(ReleaseCallPartyConnectionRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReconnectRequest(ReconnectRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHoldCallPartyConnectionRequest(HoldCallPartyConnectionRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHandOverRequest(HandOverRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogueUserInformationRequest(DialogueUserInformationRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallLimitRequest(CallLimitRequest ind) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReleaseCallPartyConnectionResponse(ReleaseCallPartyConnectionResponse ind) {
		// TODO Auto-generated method stub
		
	}
}
