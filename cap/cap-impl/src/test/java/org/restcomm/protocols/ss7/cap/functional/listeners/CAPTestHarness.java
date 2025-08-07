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

package org.restcomm.protocols.ss7.cap.functional.listeners;

import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPDialogListener;
import org.restcomm.protocols.ss7.cap.api.CAPMessage;
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
import org.restcomm.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CAPServiceGprsListener;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CancelGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ConnectGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ContinueGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EventReportGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EventReportGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.FurnishChargingInformationGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ReleaseGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ResetTimerGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.SendChargingInformationGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPServiceSmsListener;
import org.restcomm.protocols.ss7.cap.api.service.sms.ConnectSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ContinueSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.EventReportSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.FurnishChargingInformationSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.InitialDPSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ReleaseSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.RequestReportSMSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ResetTimerSMSRequest;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventHarness;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 *
 * @author amit bhayani
 * @author servey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPTestHarness extends TestEventHarness<EventType> implements CAPDialogListener,
		CAPServiceCircuitSwitchedCallListener, CAPServiceGprsListener, CAPServiceSmsListener {
	private Logger logger;

	public boolean invokeTimeoutSuppressed = false;

	CAPTestHarness(Logger logger) {
		this.logger = logger;
	}

	public void suppressInvokeTimeout() {
		invokeTimeoutSuppressed = true;
	}

	@Override
	public void onDialogDelimiter(CAPDialog capDialog) {
		this.logger.debug("onDialogDelimiter");
		this.handleReceived(EventType.DialogDelimiter, capDialog);
	}

	@Override
	public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		this.logger.debug("onDialogRequest");
		this.handleReceived(EventType.DialogRequest, capDialog);
	}

	@Override
	public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
		this.logger.debug("onDialogAccept");
		this.handleReceived(EventType.DialogAccept, capDialog);
	}

	@Override
	public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason,
			CAPUserAbortReason userReason) {
		this.logger.debug("onDialogUserAbort");
		this.handleReceived(EventType.DialogUserAbort, capDialog);
	}

	@Override
	public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
		this.logger.debug("onDialogProviderAbort");
		this.handleReceived(EventType.DialogProviderAbort, capDialog);
	}

	@Override
	public void onDialogClose(CAPDialog capDialog) {
		this.logger.debug("onDialogClose");
		this.handleReceived(EventType.DialogClose, capDialog);
	}

	@Override
	public void onDialogRelease(CAPDialog capDialog) {
		this.logger.debug("onDialogRelease");
		this.handleReceived(EventType.DialogRelease, capDialog);
	}

	@Override
	public void onDialogTimeout(CAPDialog capDialog) {
		this.logger.debug("onDialogTimeout");
		this.handleReceived(EventType.DialogTimeout, capDialog);
	}

	@Override
	public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		this.logger.debug("onDialogNotice");
		this.handleReceived(EventType.DialogNotice, capDialog);
	}

	@Override
	public void onErrorComponent(CAPDialog capDialog, Integer invokeId, CAPErrorMessage capErrorMessage) {
		this.logger.debug("onErrorComponent");
		this.handleReceived(EventType.ErrorComponent, capDialog);
	}

	@Override
	public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem, boolean isLocalOriginated) {
		this.logger.debug("onRejectComponent");
		this.handleReceived(EventType.RejectComponent, capDialog);
	}

	@Override
	public void onInvokeTimeout(CAPDialog capDialog, Integer invokeId) {
		this.logger.debug("onInvokeTimeout");
		if (!invokeTimeoutSuppressed)
			this.handleReceived(EventType.InvokeTimeout, capDialog);
	}

	@Override
	public void onCAPMessage(CAPMessage capMessage) {
	}

	@Override
	public void onInitialDPRequest(InitialDPRequest ind) {
		this.logger.debug("onInitialDPRequestIndication");
		this.handleReceived(EventType.InitialDpRequest, ind);
	}

	@Override
	public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
		this.logger.debug("onRequestReportBCSMEventRequest");
		this.handleReceived(EventType.RequestReportBCSMEventRequest, ind);
	}

	@Override
	public void onApplyChargingRequest(ApplyChargingRequest ind) {
		this.logger.debug("ApplyChargingRequest");
		this.handleReceived(EventType.ApplyChargingRequest, ind);
	}

	@Override
	public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
		this.logger.debug("EventReportBCSMRequest");
		this.handleReceived(EventType.EventReportBCSMRequest, ind);
	}

	@Override
	public void onContinueRequest(ContinueRequest ind) {
		this.logger.debug("ContinueRequest");
		this.handleReceived(EventType.ContinueRequest, ind);
	}

	@Override
	public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
		this.logger.debug("ApplyChargingReportRequest");
		this.handleReceived(EventType.ApplyChargingReportRequest, ind);
	}

	@Override
	public void onReleaseCallRequest(ReleaseCallRequest ind) {
		this.logger.debug("ReleaseCallRequest");
		this.handleReceived(EventType.ReleaseCallRequest, ind);
	}

	@Override
	public void onConnectRequest(ConnectRequest ind) {
		this.logger.debug("ConnectRequest");
		this.handleReceived(EventType.ConnectRequest, ind);
	}

	@Override
	public void onCallInformationRequestRequest(CallInformationRequestRequest ind) {
		this.logger.debug("CallInformationRequestRequest");
		this.handleReceived(EventType.CallInformationRequestRequest, ind);
	}

	@Override
	public void onCallInformationReportRequest(CallInformationReportRequest ind) {
		this.logger.debug("CallInformationReportRequest");
		this.handleReceived(EventType.CallInformationReportRequest, ind);
	}

	@Override
	public void onActivityTestRequest(ActivityTestRequest ind) {
		this.logger.debug("ActivityTestRequest");
		this.handleReceived(EventType.ActivityTestRequest, ind);
	}

	@Override
	public void onActivityTestResponse(ActivityTestResponse ind) {
		this.logger.debug("ActivityTestResponse");
		this.handleReceived(EventType.ActivityTestResponse, ind);
	}

	@Override
	public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind) {
		this.logger.debug("AssistRequestInstructionsRequest");
		this.handleReceived(EventType.AssistRequestInstructionsRequest, ind);
	}

	@Override
	public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind) {
		this.logger.debug("EstablishTemporaryConnectionRequest");
		this.handleReceived(EventType.EstablishTemporaryConnectionRequest, ind);
	}

	@Override
	public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
		this.logger.debug("DisconnectForwardConnectionRequest");
		this.handleReceived(EventType.DisconnectForwardConnectionRequest, ind);
	}

	@Override
	public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
		this.logger.debug("ConnectToResourceRequest");
		this.handleReceived(EventType.ConnectToResourceRequest, ind);
	}

	@Override
	public void onResetTimerRequest(ResetTimerRequest ind) {
		this.logger.debug("ResetTimerRequest");
		this.handleReceived(EventType.ResetTimerRequest, ind);
	}

	@Override
	public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind) {
		this.logger.debug("FurnishChargingInformationRequest");
		this.handleReceived(EventType.FurnishChargingInformationRequest, ind);
	}

	@Override
	public void onSendChargingInformationRequest(SendChargingInformationRequest ind) {
		this.logger.debug("SendChargingInformationRequest");
		this.handleReceived(EventType.SendChargingInformationRequest, ind);
	}

	@Override
	public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
		this.logger.debug("SpecializedResourceReportRequest");
		this.handleReceived(EventType.SpecializedResourceReportRequest, ind);
	}

	@Override
	public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
		this.logger.debug("PlayAnnouncementRequest");
		this.handleReceived(EventType.PlayAnnouncementRequest, ind);
	}

	@Override
	public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
		this.logger.debug("PromptAndCollectUserInformationRequest");
		this.handleReceived(EventType.PromptAndCollectUserInformationRequest, ind);
	}

	@Override
	public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind) {
		this.logger.debug("PromptAndCollectUserInformationResponse");
		this.handleReceived(EventType.PromptAndCollectUserInformationResponse, ind);
	}

	@Override
	public void onCancelRequest(CancelRequest ind) {
		this.logger.debug("CancelRequest");
		this.handleReceived(EventType.CancelRequest, ind);
	}

	@Override
	public void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind) {
		this.logger.debug("ContinueWithArgumentRequest");
		this.handleReceived(EventType.ContinueWithArgumentRequest, ind);
	}

	@Override
	public void onDisconnectLegRequest(DisconnectLegRequest ind) {
		this.logger.debug("DisconnectLegRequest");
		this.handleReceived(EventType.DisconnectLegRequest, ind);
	}

	@Override
	public void onDisconnectLegResponse(DisconnectLegResponse ind) {
		this.logger.debug("DisconnectLegResponse");
		this.handleReceived(EventType.DisconnectLegResponse, ind);
	}

	@Override
	public void onDisconnectForwardConnectionWithArgumentRequest(DisconnectForwardConnectionWithArgumentRequest ind) {
		this.logger.debug("DisconnectForwardConnectionWithArgumentRequest");
		this.handleReceived(EventType.DisconnectForwardConnectionWithArgumentRequest, ind);
	}

	@Override
	public void onInitiateCallAttemptRequest(InitiateCallAttemptRequest ind) {
		this.logger.debug("InitiateCallAttemptRequest");
		this.handleReceived(EventType.InitiateCallAttemptRequest, ind);
	}

	@Override
	public void onInitiateCallAttemptResponse(InitiateCallAttemptResponse ind) {
		this.logger.debug("InitiateCallAttemptResponse");
		this.handleReceived(EventType.InitiateCallAttemptResponse, ind);
	}

	@Override
	public void onMoveLegRequest(MoveLegRequest ind) {
		this.logger.debug("MoveLegRequest");
		this.handleReceived(EventType.MoveLegRequest, ind);
	}

	@Override
	public void onMoveLegResponse(MoveLegResponse ind) {
		this.logger.debug("MoveLegResponse");
		this.handleReceived(EventType.MoveLegResponse, ind);
	}

	@Override
	public void onSplitLegRequest(SplitLegRequest ind) {
		this.logger.debug("SplitLegRequest");
		this.handleReceived(EventType.SplitLegRequest, ind);
	}

	@Override
	public void onSplitLegResponse(SplitLegResponse ind) {
		this.logger.debug("SplitLegResponse");
		this.handleReceived(EventType.SplitLegResponse, ind);
	}

	@Override
	public void onInitialDpGprsRequest(InitialDpGprsRequest ind) {
		this.logger.debug("InitialDpGprsRequest");
		this.handleReceived(EventType.InitialDpGprsRequest, ind);
	}

	@Override
	public void onRequestReportGPRSEventRequest(RequestReportGPRSEventRequest ind) {
		this.logger.debug("RequestReportGPRSEventRequest");
		this.handleReceived(EventType.RequestReportGPRSEventRequest, ind);
	}

	@Override
	public void onApplyChargingGPRSRequest(ApplyChargingGPRSRequest ind) {
		this.logger.debug("ApplyChargingGPRSRequest");
		this.handleReceived(EventType.ApplyChargingGPRSRequest, ind);
	}

	@Override
	public void onEntityReleasedGPRSRequest(EntityReleasedGPRSRequest ind) {
		this.logger.debug("EntityReleasedGPRSRequest");
		this.handleReceived(EventType.EntityReleasedGPRSRequest, ind);
	}

	@Override
	public void onEntityReleasedGPRSResponse(EntityReleasedGPRSResponse ind) {
		this.logger.debug("EntityReleasedGPRSResponse");
		this.handleReceived(EventType.EntityReleasedGPRSResponse, ind);
	}

	@Override
	public void onConnectGPRSRequest(ConnectGPRSRequest ind) {
		this.logger.debug("ConnectGPRSRequest");
		this.handleReceived(EventType.ConnectGPRSRequest, ind);
	}

	@Override
	public void onContinueGPRSRequest(ContinueGPRSRequest ind) {
		this.logger.debug("ContinueGPRSRequest");
		this.handleReceived(EventType.ContinueGPRSRequest, ind);
	}

	@Override
	public void onReleaseGPRSRequest(ReleaseGPRSRequest ind) {
		this.logger.debug("ReleaseGPRSRequest");
		this.handleReceived(EventType.ReleaseGPRSRequest, ind);
	}

	@Override
	public void onResetTimerGPRSRequest(ResetTimerGPRSRequest ind) {
		this.logger.debug("ResetTimerGPRSRequest");
		this.handleReceived(EventType.ResetTimerGPRSRequest, ind);
	}

	@Override
	public void onFurnishChargingInformationGPRSRequest(FurnishChargingInformationGPRSRequest ind) {
		this.logger.debug("FurnishChargingInformationGPRSRequest");
		this.handleReceived(EventType.FurnishChargingInformationGPRSRequest, ind);
	}

	@Override
	public void onCancelGPRSRequest(CancelGPRSRequest ind) {
		this.logger.debug("CancelGPRSRequest");
		this.handleReceived(EventType.CancelGPRSRequest, ind);
	}

	@Override
	public void onSendChargingInformationGPRSRequest(SendChargingInformationGPRSRequest ind) {
		this.logger.debug("SendChargingInformationGPRSRequest");
		this.handleReceived(EventType.SendChargingInformationGPRSRequest, ind);
	}

	@Override
	public void onApplyChargingReportGPRSRequest(ApplyChargingReportGPRSRequest ind) {
		this.logger.debug("ApplyChargingReportGPRSRequest");
		this.handleReceived(EventType.ApplyChargingReportGPRSRequest, ind);
	}

	@Override
	public void onApplyChargingReportGPRSResponse(ApplyChargingReportGPRSResponse ind) {
		this.logger.debug("ApplyChargingReportGPRSResponse");
		this.handleReceived(EventType.ApplyChargingReportGPRSResponse, ind);
	}

	@Override
	public void onEventReportGPRSRequest(EventReportGPRSRequest ind) {
		this.logger.debug("EventReportGPRSRequest");
		this.handleReceived(EventType.EventReportGPRSRequest, ind);
	}

	@Override
	public void onEventReportGPRSResponse(EventReportGPRSResponse ind) {
		this.logger.debug("EventReportGPRSResponse");
		this.handleReceived(EventType.EventReportGPRSResponse, ind);
	}

	@Override
	public void onActivityTestGPRSRequest(ActivityTestGPRSRequest ind) {
		this.logger.debug("ActivityTestGPRSRequest");
		this.handleReceived(EventType.ActivityTestGPRSRequest, ind);
	}

	@Override
	public void onActivityTestGPRSResponse(ActivityTestGPRSResponse ind) {
		this.logger.debug("ActivityTestGPRSResponse");
		this.handleReceived(EventType.ActivityTestGPRSResponse, ind);
	}

	@Override
	public void onConnectSMSRequest(ConnectSMSRequest ind) {
		this.logger.debug("ConnectSMSRequest");
		this.handleReceived(EventType.ConnectSMSRequest, ind);
	}

	@Override
	public void onEventReportSMSRequest(EventReportSMSRequest ind) {
		this.logger.debug("EventReportSMSRequest");
		this.handleReceived(EventType.EventReportSMSRequest, ind);
	}

	@Override
	public void onFurnishChargingInformationSMSRequest(FurnishChargingInformationSMSRequest ind) {
		this.logger.debug("FurnishChargingInformationSMSRequest");
		this.handleReceived(EventType.FurnishChargingInformationSMSRequest, ind);
	}

	@Override
	public void onInitialDPSMSRequest(InitialDPSMSRequest ind) {
		this.logger.debug("InitialDPSMSRequest");
		this.handleReceived(EventType.InitialDPSMSRequest, ind);
	}

	@Override
	public void onReleaseSMSRequest(ReleaseSMSRequest ind) {
		this.logger.debug("ReleaseSMSRequest");
		this.handleReceived(EventType.ReleaseSMSRequest, ind);
	}

	@Override
	public void onRequestReportSMSEventRequest(RequestReportSMSEventRequest ind) {
		this.logger.debug("RequestReportSMSEventRequest");
		this.handleReceived(EventType.RequestReportSMSEventRequest, ind);
	}

	@Override
	public void onResetTimerSMSRequest(ResetTimerSMSRequest ind) {
		this.logger.debug("ResetTimerSMSRequest");
		this.handleReceived(EventType.ResetTimerSMSRequest, ind);
	}

	@Override
	public void onContinueSMSRequest(ContinueSMSRequest ind) {
		this.logger.debug("ContinueSMSRequest");
		this.handleReceived(EventType.ContinueSMSRequest, ind);
	}

	@Override
	public void onCollectInformationRequest(CollectInformationRequest ind) {
		this.logger.debug("CollectInformationRequest");
		this.handleReceived(EventType.CollectInformationRequest, ind);
	}

	@Override
	public void onCallGapRequest(org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest ind) {
		this.logger.debug("CallGapRequest");
		this.handleReceived(EventType.CallGapRequest, ind);
	}
}
