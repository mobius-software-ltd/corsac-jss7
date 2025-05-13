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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPDialogListener;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPMessage;
import org.restcomm.protocols.ss7.inap.api.INAPProvider;
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
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * 
 * @author yulianoifa
 *
 */
public class CallScfExample implements INAPDialogListener, INAPServiceCircuitSwitchedCallListener {

	private INAPProvider inapProvider;
	private INAPDialogCircuitSwitchedCall currentInapDialog;
	private CallContent cc;

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	public CallScfExample() throws NamingException {
		InitialContext ctx = new InitialContext();
		try {
			String providerJndiName = "java:/restcomm/ss7/cap";
			this.inapProvider = ((INAPProvider) ctx.lookup(providerJndiName));
		} finally {
			ctx.close();
		}

		inapProvider.addINAPDialogListener(UUID.randomUUID(), this);
		inapProvider.getINAPServiceCircuitSwitchedCall().addINAPServiceListener(this);
	}

	public INAPProvider getINAPProvider() {
		return inapProvider;
	}

	public void start() {
		// Make the circuitSwitchedCall service activated
		inapProvider.getINAPServiceCircuitSwitchedCall().acivate();

		currentInapDialog = null;
	}

	public void stop() {
		inapProvider.getINAPServiceCircuitSwitchedCall().deactivate();
	}

	@Override
	public void onInitialDPRequest(InitialDPRequest ind) {
		this.cc = new CallContent();
		this.cc.step = Step.initialDPRecieved;

		ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
	}

	@Override
	public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
		if (this.cc != null) {
			this.cc.eventList.add(ind);

			switch (ind.getEventTypeBCSM()) {
			case oAnswer:
				this.cc.step = Step.answered;
				break;
			case oDisconnect:
				this.cc.step = Step.disconnected;
				break;
			default:
				break;
			}
		}

		ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
	}

	@Override
	public void onDialogDelimiter(INAPDialog inapDialog) {
		try {
			if (this.cc != null)
				switch (this.cc.step) {
				case initialDPRecieved:
					// informing SSF of BCSM events processing
					List<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
					BCSMEvent ev = this.inapProvider.getINAPParameterFactory().createBCSMEvent(
							EventTypeBCSM.routeSelectFailure, MonitorMode.notifyAndContinue, null, null, false);
					bcsmEventList.add(ev);
					ev = this.inapProvider.getINAPParameterFactory().createBCSMEvent(EventTypeBCSM.oCalledPartyBusy,
							MonitorMode.interrupted, null, null, false);
					bcsmEventList.add(ev);
					ev = this.inapProvider.getINAPParameterFactory().createBCSMEvent(EventTypeBCSM.oNoAnswer,
							MonitorMode.interrupted, null, null, false);
					bcsmEventList.add(ev);
					ev = this.inapProvider.getINAPParameterFactory().createBCSMEvent(EventTypeBCSM.oAnswer,
							MonitorMode.notifyAndContinue, null, null, false);
					bcsmEventList.add(ev);
					LegID legId = this.inapProvider.getINAPParameterFactory().createLegID(null, LegType.leg1);
					ev = this.inapProvider.getINAPParameterFactory().createBCSMEvent(EventTypeBCSM.oDisconnect,
							MonitorMode.notifyAndContinue, legId, null, false);
					bcsmEventList.add(ev);
					legId = this.inapProvider.getINAPParameterFactory().createLegID(null, LegType.leg2);
					ev = this.inapProvider.getINAPParameterFactory().createBCSMEvent(EventTypeBCSM.oDisconnect,
							MonitorMode.interrupted, legId, null, false);
					bcsmEventList.add(ev);
					ev = this.inapProvider.getINAPParameterFactory().createBCSMEvent(EventTypeBCSM.oAbandon,
							MonitorMode.notifyAndContinue, null, null, false);
					bcsmEventList.add(ev);
					currentInapDialog.addRequestReportBCSMEventRequest(bcsmEventList, null, null);

					// calculating here a new called party number if it is needed
					String newNumber = "22123124";
					if (newNumber != null) {
						// sending Connect to force routing the call to a new number
						List<CalledPartyNumberIsup> calledPartyNumber = new ArrayList<CalledPartyNumberIsup>();
						CalledPartyNumber cpn = this.inapProvider.getISUPParameterFactory().createCalledPartyNumber();
						cpn.setAddress("5599999988");
						cpn.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
						cpn.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
						cpn.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
						CalledPartyNumberIsup cpnc = this.inapProvider.getINAPParameterFactory()
								.createCalledPartyNumber(cpn);
						calledPartyNumber.add(cpnc);
						DestinationRoutingAddress destinationRoutingAddress = this.inapProvider
								.getINAPParameterFactory().createDestinationRoutingAddress(calledPartyNumber);
						currentInapDialog.addConnectRequest(destinationRoutingAddress, null, null, null, null, null,
								null, null, null, null, null, null, null, null, null, null, null);
					}

					currentInapDialog.send(dummyCallback);
					break;

				case disconnected:
					// the call is terminated - close dialog
					currentInapDialog.close(false, dummyCallback);
					break;
				default:
					break;
				}
		} catch (INAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDialogTimeout(INAPDialog inapDialog) {
		if (currentInapDialog != null && this.cc != null && this.cc.step != Step.disconnected
				&& this.cc.activityTestInvokeId == null) {
			// check the SSF if the call is still alive
			currentInapDialog.keepAlive();
			try {
				this.cc.activityTestInvokeId = currentInapDialog.addActivityTestRequest();
				currentInapDialog.send(dummyCallback);
			} catch (INAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onActivityTestResponse(ActivityTestResponse ind) {
		if (currentInapDialog != null && this.cc != null)
			this.cc.activityTestInvokeId = null;
	}

	@Override
	public void onInvokeTimeout(INAPDialog inapDialog, Integer invokeId) {
		if (currentInapDialog != null && this.cc != null)
			if (this.cc.activityTestInvokeId == invokeId)
				currentInapDialog.close(true, dummyCallback);
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
	public void onINAPMessage(INAPMessage inapMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onApplyChargingRequest(ApplyChargingRequest ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContinueRequest(ContinueRequest ind) {
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
	public void onConnectRequest(ConnectRequest ind) {
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
	public void onActivityTestRequest(ActivityTestRequest ind) {
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
	public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason,
			INAPUserAbortReason userReason) {
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
		this.currentInapDialog = null;
		this.cc = null;
	}

	@Override
	public void onDialogNotice(INAPDialog inapDialog, INAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		// TODO Auto-generated method stub

	}

	private enum Step {
		initialDPRecieved, answered, disconnected;
	}

	private class CallContent {
		public Step step;
		public ArrayList<EventReportBCSMRequest> eventList = new ArrayList<EventReportBCSMRequest>();
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
	public void onCallGapRequest(CallGapRequest ind) {
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
