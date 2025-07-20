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

package org.restcomm.protocols.ss7.inap.functional.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPDialogListener;
import org.restcomm.protocols.ss7.inap.api.INAPMessage;
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
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 *
 * @author yulian.oifa
 *
 */
public class EventTestHarness implements INAPDialogListener, INAPServiceCircuitSwitchedCallListener {
	private static final long EVENT_TIMEOUT = 10000;

	private Logger logger;

	protected Queue<TestEvent> observerdEvents = new ConcurrentLinkedQueue<TestEvent>();
	protected AtomicInteger sequence = new AtomicInteger(0);
	protected boolean invokeTimeoutSuppressed = false;

	protected Map<EventType, Semaphore> sentSemaphores = new ConcurrentHashMap<>();
	protected Map<EventType, Semaphore> receivedSemaphores = new ConcurrentHashMap<>();

	EventTestHarness(Logger logger) {
		this.logger = logger;
	}

	public void suppressInvokeTimeout() {
		invokeTimeoutSuppressed = true;
	}

	public void compareEvents(List<TestEvent> expectedEvents) {
		List<TestEvent> actualEvents = new ArrayList<TestEvent>(observerdEvents);
		if (expectedEvents.size() != actualEvents.size()) {
			String comparedEvents = doStringCompare(expectedEvents, actualEvents);

			fail("Size of received events: " + actualEvents.size() + ", does not equal expected events: "
					+ expectedEvents.size() + "\n" + comparedEvents);
		}

		for (int index = 0; index < expectedEvents.size(); index++)
			assertEquals(expectedEvents.get(index), actualEvents.get(index));
	}

	protected String doStringCompare(List<TestEvent> expectedEvents, List<TestEvent> observerdEvents) {
		StringBuilder sb = new StringBuilder();
		int size1 = expectedEvents.size();
		int size2 = observerdEvents.size();
		int count = size1;
		if (count < size2)
			count = size2;

		for (int index = 0; count > index; index++) {
			String s1 = size1 > index ? expectedEvents.get(index).toString() : "NOP";
			String s2 = size2 > index ? observerdEvents.get(index).toString() : "NOP";
			sb.append(s1).append(" - ").append(s2).append("\n\n");
		}
		return sb.toString();
	}

	private void handleAwait(EventType eventType, Map<EventType, Semaphore> semaphores) {
		Semaphore semaphore = semaphores.getOrDefault(eventType, new Semaphore(0));
		semaphores.putIfAbsent(eventType, semaphore);

		try {
			boolean isAcquired = semaphore.tryAcquire(EVENT_TIMEOUT, TimeUnit.MILLISECONDS);
			assertTrue("Event for type " + eventType + " is not acquired in " + EVENT_TIMEOUT + " milliseconds",
					isAcquired);
		} catch (InterruptedException e) {
			logger.error("Interrupted exception occured while waiting for event for type " + eventType + ": " + e);
			return;
		}

	}

	public void awaitReceived(EventType eventType) {
		handleAwait(eventType, this.receivedSemaphores);
	}

	public void awaitSent(EventType eventType) {
		handleAwait(eventType, this.sentSemaphores);
	}

	private void handleEvent(TestEvent testEvent, Map<EventType, Semaphore> semaphores) {
		this.observerdEvents.add(testEvent);

		EventType eventType = testEvent.getEventType();
		if (semaphores.containsKey(eventType))
			semaphores.get(eventType).release();
		else
			semaphores.put(eventType, new Semaphore(1));
	}

	protected void handleReceived(EventType eventType, Object eventSource) {
		TestEvent receivedEvent = TestEvent.createReceivedEvent(eventType, eventSource, sequence.getAndIncrement());
		this.handleEvent(receivedEvent, this.receivedSemaphores);
	}

	protected void handleSent(EventType eventType, Object eventSource) {
		TestEvent sentEvent = TestEvent.createSentEvent(eventType, eventSource, sequence.getAndIncrement());
		this.handleEvent(sentEvent, this.sentSemaphores);
	}

	@Override
	public void onDialogDelimiter(INAPDialog inapDialog) {
		this.logger.debug("onDialogDelimiter");
		this.handleReceived(EventType.DialogDelimiter, inapDialog);
	}

	@Override
	public void onDialogRequest(INAPDialog inapDialog) {
		this.logger.debug("onDialogRequest");
		this.handleReceived(EventType.DialogRequest, inapDialog);
	}

	@Override
	public void onDialogAccept(INAPDialog inapDialog) {
		this.logger.debug("onDialogAccept");
		this.handleReceived(EventType.DialogAccept, inapDialog);
	}

	@Override
	public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason,
			INAPUserAbortReason userReason) {
		this.logger.debug("onDialogUserAbort");
		this.handleReceived(EventType.DialogUserAbort, inapDialog);
	}

	@Override
	public void onDialogProviderAbort(INAPDialog inapDialog, PAbortCauseType abortCause) {
		this.logger.debug("onDialogProviderAbort");
		this.handleReceived(EventType.DialogProviderAbort, inapDialog);
	}

	@Override
	public void onDialogClose(INAPDialog inapDialog) {
		this.logger.debug("onDialogClose");
		this.handleReceived(EventType.DialogClose, inapDialog);
	}

	@Override
	public void onDialogRelease(INAPDialog inapDialog) {
		this.logger.debug("onDialogRelease");
		this.handleReceived(EventType.DialogRelease, inapDialog);
	}

	@Override
	public void onDialogTimeout(INAPDialog inapDialog) {
		this.logger.debug("onDialogTimeout");
		this.handleReceived(EventType.DialogTimeout, inapDialog);
	}

	@Override
	public void onDialogNotice(INAPDialog inapDialog, INAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		this.logger.debug("onDialogNotice");
		this.handleReceived(EventType.DialogNotice, inapDialog);
	}

	@Override
	public void onErrorComponent(INAPDialog inapDialog, Integer invokeId, INAPErrorMessage inapErrorMessage) {
		this.logger.debug("onErrorComponent");
		this.handleReceived(EventType.ErrorComponent, inapDialog);
	}

	@Override
	public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem, boolean isLocalOriginated) {
		this.logger.debug("onRejectComponent");
		this.handleReceived(EventType.RejectComponent, inapDialog);
	}

	@Override
	public void onInvokeTimeout(INAPDialog inapDialog, Integer invokeId) {
		this.logger.debug("onInvokeTimeout");
		if (!invokeTimeoutSuppressed)
			this.handleReceived(EventType.InvokeTimeout, inapDialog);
	}

	@Override
	public void onINAPMessage(INAPMessage inapMessage) {

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
	public void onCallInformationRequest(CallInformationRequest ind) {
		this.logger.debug("CallInformationRequest");
		this.handleReceived(EventType.CallInformationRequest, ind);
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
	public void onInitiateCallAttemptRequest(InitiateCallAttemptRequest ind) {
		this.logger.debug("InitiateCallAttemptRequest");
		this.handleReceived(EventType.InitiateCallAttemptRequest, ind);
	}

	@Override
	public void onCollectInformationRequest(CollectInformationRequest ind) {
		this.logger.debug("CollectInformationRequest");
		this.handleReceived(EventType.CollectInformationRequest, ind);
	}

	@Override
	public void onCallGapRequest(CallGapRequest ind) {
		this.logger.debug("CallGapRequest");
		this.handleReceived(EventType.CallGapRequest, ind);
	}

	@Override
	public void onActivateServiceFilteringRequest(ActivateServiceFilteringRequest ind) {
		this.logger.debug("ActivateServiceFilteringRequest");
		this.handleReceived(EventType.ActivateServiceFilteringRequest, ind);
	}

	@Override
	public void onEventNotificationChargingRequest(EventNotificationChargingRequest ind) {
		this.logger.debug("EventNotificationChargingRequest");
		this.handleReceived(EventType.EventNotificationChargingRequest, ind);
	}

	@Override
	public void onRequestNotificationChargingEventRequest(RequestNotificationChargingEventRequest ind) {
		this.logger.debug("RequestNotificationChargingEventRequest");
		this.handleReceived(EventType.RequestNotificationChargingEventRequest, ind);
	}

	@Override
	public void onServiceFilteringResponseRequest(ServiceFilteringResponseRequest ind) {
		this.logger.debug("ServiceFilteringResponseRequest");
		this.handleReceived(EventType.ServiceFilteringResponseRequest, ind);
	}

	@Override
	public void onAnalysedInformationRequest(AnalysedInformationRequest ind) {
		this.logger.debug("AnalysedInformationRequest");
		this.handleReceived(EventType.AnalysedInformationRequest, ind);
	}

	@Override
	public void onAnalyseInformationRequest(AnalyseInformationRequest ind) {
		this.logger.debug("AnalyseInformationRequest");
		this.handleReceived(EventType.AnalyseInformationRequest, ind);
	}

	@Override
	public void onCancelStatusReportRequest(CancelStatusReportRequest ind) {
		this.logger.debug("CancelStatusReportRequest");
		this.handleReceived(EventType.CancelStatusReportRequest, ind);
	}

	@Override
	public void onCollectedInformationRequest(CollectedInformationRequest ind) {
		this.logger.debug("CollectedInformationRequest");
		this.handleReceived(EventType.CollectedInformationRequest, ind);
	}

	@Override
	public void onHoldCallInNetworkRequest(HoldCallInNetworkRequest ind) {
		this.logger.debug("HoldCallInNetworkRequest");
		this.handleReceived(EventType.HoldCallInNetworkRequest, ind);
	}

	@Override
	public void onOMidCallRequest(OMidCallRequest ind) {
		this.logger.debug("OMidCallRequest");
		this.handleReceived(EventType.OMidCallRequest, ind);
	}

	@Override
	public void onTMidCallRequest(TMidCallRequest ind) {
		this.logger.debug("TMidCallRequest");
		this.handleReceived(EventType.TMidCallRequest, ind);
	}

	@Override
	public void onOAnswerRequest(OAnswerRequest ind) {
		this.logger.debug("OAnswerRequest");
		this.handleReceived(EventType.OAnswerRequest, ind);
	}

	@Override
	public void onOriginationAttemptAuthorizedRequest(OriginationAttemptAuthorizedRequest ind) {
		this.logger.debug("OriginationAttemptAuthorizedRequest");
		this.handleReceived(EventType.OriginationAttemptAuthorizedRequest, ind);
	}

	@Override
	public void onRouteSelectFailureRequest(RouteSelectFailureRequest ind) {
		this.logger.debug("RouteSelectFailureRequest");
		this.handleReceived(EventType.RouteSelectFailureRequest, ind);
	}

	@Override
	public void onOCalledPartyBusyRequest(OCalledPartyBusyRequest ind) {
		this.logger.debug("OCalledPartyBusyRequest");
		this.handleReceived(EventType.OCalledPartyBusyRequest, ind);
	}

	@Override
	public void onONoAnswerRequest(ONoAnswerRequest ind) {
		this.logger.debug("ONoAnswerRequest");
		this.handleReceived(EventType.ONoAnswerRequest, ind);
	}

	@Override
	public void onODisconnectRequest(ODisconnectRequest ind) {
		this.logger.debug("ODisconnectRequest");
		this.handleReceived(EventType.ODisconnectRequest, ind);
	}

	@Override
	public void onTermAttemptAuthorizedRequest(TermAttemptAuthorizedRequest ind) {
		this.logger.debug("TermAttemptAuthorizedRequest");
		this.handleReceived(EventType.TermAttemptAuthorizedRequest, ind);
	}

	@Override
	public void onTBusyRequest(TBusyRequest ind) {
		this.logger.debug("TBusyRequest");
		this.handleReceived(EventType.TBusyRequest, ind);
	}

	@Override
	public void onTNoAnswerRequest(TNoAnswerRequest ind) {
		this.logger.debug("TNoAnswerRequest");
		this.handleReceived(EventType.TNoAnswerRequest, ind);
	}

	@Override
	public void onTAnswerRequest(TAnswerRequest ind) {
		this.logger.debug("TAnswerRequest");
		this.handleReceived(EventType.TAnswerRequest, ind);
	}

	@Override
	public void onTDisconnectRequest(TDisconnectRequest ind) {
		this.logger.debug("TDisconnectRequest");
		this.handleReceived(EventType.TDisconnectRequest, ind);
	}

	@Override
	public void onSelectRouteRequest(SelectRouteRequest ind) {
		this.logger.debug("SelectRouteRequest");
		this.handleReceived(EventType.SelectRouteRequest, ind);
	}

	@Override
	public void onSelectFacilityRequest(SelectFacilityRequest ind) {
		this.logger.debug("SelectFacilityRequest");
		this.handleReceived(EventType.SelectFacilityRequest, ind);
	}

	@Override
	public void onRequestCurrentStatusReportRequest(RequestCurrentStatusReportRequest ind) {
		this.logger.debug("RequestCurrentStatusReportRequest");
		this.handleReceived(EventType.RequestCurrentStatusReportRequest, ind);
	}

	@Override
	public void onRequestCurrentStatusReportResponse(RequestCurrentStatusReportResponse ind) {
		this.logger.debug("RequestCurrentStatusReportResponse");
		this.handleReceived(EventType.RequestCurrentStatusReportResponse, ind);
	}

	@Override
	public void onRequestEveryStatusChangeReportRequest(RequestEveryStatusChangeReportRequest ind) {
		this.logger.debug("RequestEveryStatusChangeReportRequest");
		this.handleReceived(EventType.RequestEveryStatusChangeReportRequest, ind);
	}

	@Override
	public void onRequestFirstStatusMatchReportRequest(RequestFirstStatusMatchReportRequest ind) {
		this.logger.debug("RequestFirstStatusMatchReportRequest");
		this.handleReceived(EventType.RequestFirstStatusMatchReportRequest, ind);
	}

	@Override
	public void onStatusReportRequest(StatusReportRequest ind) {
		this.logger.debug("StatusReportRequest");
		this.handleReceived(EventType.StatusReportRequest, ind);
	}

	@Override
	public void onUpdateRequest(UpdateRequest ind) {
		this.logger.debug("UpdateRequest");
		this.handleReceived(EventType.UpdateRequest, ind);
	}

	@Override
	public void onUpdateResponse(UpdateResponse ind) {
		this.logger.debug("UpdateResponse");
		this.handleReceived(EventType.UpdateResponse, ind);
	}

	@Override
	public void onRetrieveRequest(RetrieveRequest ind) {
		this.logger.debug("RetrieveRequest");
		this.handleReceived(EventType.RetrieveRequest, ind);
	}

	@Override
	public void onRetrieveResponse(RetrieveResponse ind) {
		this.logger.debug("RetrieveResponse");
		this.handleReceived(EventType.RetrieveResponse, ind);
	}

	@Override
	public void onSignallingInformationRequest(SignallingInformationRequest ind) {
		this.logger.debug("SignallingInformationRequest");
		this.handleReceived(EventType.SignallingInformationRequest, ind);
	}

	@Override
	public void onReleaseCallPartyConnectionRequest(ReleaseCallPartyConnectionRequest ind) {
		this.logger.debug("ReleaseCallPartyConnectionRequest");
		this.handleReceived(EventType.ReleaseCallPartyConnectionRequest, ind);
	}

	@Override
	public void onReconnectRequest(ReconnectRequest ind) {
		this.logger.debug("ReconnectRequest");
		this.handleReceived(EventType.ReconnectRequest, ind);
	}

	@Override
	public void onHoldCallPartyConnectionRequest(HoldCallPartyConnectionRequest ind) {
		this.logger.debug("HoldCallPartyConnectionRequest");
		this.handleReceived(EventType.HoldCallPartyConnectionRequest, ind);
	}

	@Override
	public void onHandOverRequest(HandOverRequest ind) {
		this.logger.debug("HandOverRequest");
		this.handleReceived(EventType.HandOverRequest, ind);
	}

	@Override
	public void onDialogueUserInformationRequest(DialogueUserInformationRequest ind) {
		this.logger.debug("DialogueUserInformationRequest");
		this.handleReceived(EventType.DialogueUserInformationRequest, ind);
	}

	@Override
	public void onCallLimitRequest(CallLimitRequest ind) {
		this.logger.debug("CallLimitRequest");
		this.handleReceived(EventType.CallLimitRequest, ind);
	}

	@Override
	public void onReleaseCallPartyConnectionResponse(ReleaseCallPartyConnectionResponse ind) {
		this.logger.debug("ReleaseCallPartyConnectionResponse");
		this.handleReceived(EventType.ReleaseCallPartyConnectionResponse, ind);
	}
}
