package org.restcomm.protocols.ss7.cap;
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

import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPDialogListener;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPMessage;
import org.restcomm.protocols.ss7.cap.api.CAPProvider;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallListener;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CollectInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionWithArgumentRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public class CallSsfExample implements CAPDialogListener, CAPServiceCircuitSwitchedCallListener {

	private CAPProvider capProvider;
	private CAPDialogCircuitSwitchedCall currentCapDialog;
	private CallContent cc;

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	public CallSsfExample() throws NamingException {
		InitialContext ctx = new InitialContext();
		try {
			String providerJndiName = "java:/restcomm/ss7/cap";
			this.capProvider = ((CAPProvider) ctx.lookup(providerJndiName));
		} finally {
			ctx.close();
		}

		capProvider.addCAPDialogListener(UUID.randomUUID(), this);
		capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);
	}

	public CAPProvider getCAPProvider() {
		return capProvider;
	}

	public void start() {
		// Make the circuitSwitchedCall service activated
		capProvider.getCAPServiceCircuitSwitchedCall().acivate();

		currentCapDialog = null;
	}

	public void stop() {
		capProvider.getCAPServiceCircuitSwitchedCall().deactivate();
	}

	public void sendInitialDP(SccpAddress origAddress, SccpAddress remoteAddress, int serviceKey,
			CalledPartyNumberIsup calledPartyNumber, CallingPartyNumberIsup callingPartyNumber,
			LocationNumberIsup locationNumber, EventTypeBCSM eventTypeBCSM, LocationInformation locationInformation)
			throws CAPException {
		// First create Dialog
		CAPApplicationContext acn = CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF;
		currentCapDialog = capProvider.getCAPServiceCircuitSwitchedCall().createNewDialog(acn, origAddress,
				remoteAddress, 0);

		currentCapDialog.addInitialDPRequest(serviceKey, calledPartyNumber, callingPartyNumber, null, null, null,
				locationNumber, null, null, null, null, null, eventTypeBCSM, null, null, null, null, null, null, null,
				false, null, null, locationInformation, null, null, null, null, null, false, null);
		// This will initiate the TC-BEGIN with INVOKE component
		currentCapDialog.send(dummyCallback);

		this.cc.step = Step.initialDPSent;
	}

	public void sendEventReportBCSM_OAnswer(OAnswerSpecificInfo oAnswerSpecificInfo, LegType legID,
			MiscCallInfo miscCallInfo) throws CAPException {
		if (currentCapDialog != null && this.cc != null) {
			EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capProvider.getCAPParameterFactory()
					.createEventSpecificInformationBCSM(oAnswerSpecificInfo);
			currentCapDialog.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, eventSpecificInformationBCSM, legID,
					miscCallInfo, null);
			currentCapDialog.send(dummyCallback);
			this.cc.step = Step.answered;
		}
	}

	public void sendEventReportBCSM_ODisconnect(ODisconnectSpecificInfo oDisconnectSpecificInfo, LegType legID,
			MiscCallInfo miscCallInfo) throws CAPException {
		if (currentCapDialog != null && this.cc != null) {
			EventSpecificInformationBCSM eventSpecificInformationBCSM = this.capProvider.getCAPParameterFactory()
					.createEventSpecificInformationBCSM(oDisconnectSpecificInfo);
			currentCapDialog.addEventReportBCSMRequest(EventTypeBCSM.oDisconnect, eventSpecificInformationBCSM, legID,
					miscCallInfo, null);
			currentCapDialog.send(dummyCallback);
			this.cc.step = Step.disconnected;
		}
	}

	@Override
	public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
		if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected) {
			// initiating BCSM events processing
		}
		ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
	}

	@Override
	public void onActivityTestRequest(ActivityTestRequest ind) {
		if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected)
			this.cc.activityTestInvokeId = ind.getInvokeId();
	}

	@Override
	public void onActivityTestResponse(ActivityTestResponse ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onContinueRequest(ContinueRequest ind) {
		this.cc.step = Step.callAllowed;
		ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		// sending Continue to use the original calledPartyAddress
	}

	@Override
	public void onConnectRequest(ConnectRequest ind) {
		this.cc.step = Step.callAllowed;
		ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		// sending Connect to force routing the call to a new number
	}

	@Override
	public void onDialogTimeout(CAPDialog capDialog) {
		if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected)
			// if the call is still up - keep the sialog alive
			currentCapDialog.keepAlive();
	}

	@Override
	public void onDialogDelimiter(CAPDialog capDialog) {
		if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected)
			if (this.cc.activityTestInvokeId != null)
				try {
					currentCapDialog.addActivityTestResponse(this.cc.activityTestInvokeId);
					this.cc.activityTestInvokeId = null;
					currentCapDialog.send(dummyCallback);
				} catch (CAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	@Override
	public void onErrorComponent(CAPDialog capDialog, Integer invokeId, CAPErrorMessage capErrorMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem, boolean isLocalOriginated) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInvokeTimeout(CAPDialog capDialog, Integer invokeId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCAPMessage(CAPMessage capMessage) {
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
	public void onCallGapRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCallInformationRequestRequest(CallInformationRequestRequest ind) {
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
	public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason,
			CAPUserAbortReason userReason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogClose(CAPDialog capDialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogRelease(CAPDialog capDialog) {
		this.currentCapDialog = null;
		this.cc = null;
	}

	@Override
	public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
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
	public void onDisconnectLegRequest(DisconnectLegRequest ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnectLegResponse(DisconnectLegResponse ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnectForwardConnectionWithArgumentRequest(DisconnectForwardConnectionWithArgumentRequest ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitiateCallAttemptRequest(InitiateCallAttemptRequest initiateCallAttemptRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitiateCallAttemptResponse(InitiateCallAttemptResponse initiateCallAttemptResponse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMoveLegRequest(MoveLegRequest ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMoveLegResponse(MoveLegResponse ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCollectInformationRequest(CollectInformationRequest ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSplitLegRequest(SplitLegRequest ind) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSplitLegResponse(SplitLegResponse ind) {
		// TODO Auto-generated method stub

	}
}
