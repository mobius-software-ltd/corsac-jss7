package org.restcomm.protocols.ss7.map.functional.listeners;

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
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPDialogListener;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.service.callhandling.IstCommandRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.IstCommandResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandlingListener;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ForwardCheckSSIndicationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeRequest_Oam;
import org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeResponse_Oam;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPServiceOamListener;
import org.restcomm.protocols.ss7.map.api.service.oam.SendImsiRequest;
import org.restcomm.protocols.ss7.map.api.service.oam.SendImsiResponse;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivationListener;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsRequest;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImplV3;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class EventTestHarness implements MAPDialogListener, MAPServiceSupplementaryListener, MAPServiceSmsListener,
		MAPServiceMobilityListener, MAPServiceLsmListener, MAPServiceCallHandlingListener, MAPServiceOamListener,
		MAPServicePdpContextActivationListener {
	private static final long EVENT_TIMEOUT = 10000;

	private Logger logger = null;

	protected Queue<TestEvent> observerdEvents = new ConcurrentLinkedQueue<TestEvent>();
	protected AtomicInteger sequence = new AtomicInteger(0);

	protected Map<EventType, Semaphore> sentSemaphores = new ConcurrentHashMap<>();
	protected Map<EventType, Semaphore> receivedSemaphores = new ConcurrentHashMap<>();

	EventTestHarness(Logger logger) {
		this.logger = logger;
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
	public void onDialogDelimiter(MAPDialog mapDialog) {
		this.logger.debug("onDialogDelimiter");
		this.handleReceived(EventType.DialogDelimiter, mapDialog);
	}

	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer) {
		this.logger.debug("onDialogRequest");
		this.handleReceived(EventType.DialogRequest, mapDialog);
	}

	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
		this.logger.debug("onDialogAccept");
		this.handleReceived(EventType.DialogAccept, mapDialog);
	}

	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		this.logger.debug("onDialogReject");
		this.handleReceived(EventType.DialogReject, mapDialog);
	}

	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {
		this.logger.debug("onDialogUserAbort");
		this.handleReceived(EventType.DialogUserAbort, mapDialog);
	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
			MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
		this.logger.debug("onDialogProviderAbort");
		this.handleReceived(EventType.DialogProviderAbort, mapDialog);
	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {
		this.logger.debug("onDialogClose");
		this.handleReceived(EventType.DialogClose, mapDialog);
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		this.logger.debug("onDialogNotice");
		this.handleReceived(EventType.DialogNotice, mapDialog);
	}

	@Override
	public void onDialogRelease(MAPDialog mapDialog) {
		this.logger.debug("onDialogRelease");
		this.handleReceived(EventType.DialogRelease, mapDialog);
	}

	@Override
	public void onDialogTimeout(MAPDialog mapDialog) {
		this.logger.debug("onDialogTimeout");
		this.handleReceived(EventType.DialogTimeout, mapDialog);
	}

	@Override
	public void onErrorComponent(MAPDialog mapDialog, Integer invokeId, MAPErrorMessage mapErrorMessage) {
		this.logger.debug("onErrorComponent");
		this.handleReceived(EventType.ErrorComponent, mapDialog);
	}

	@Override
	public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem, boolean isLocalOriginated) {
		this.logger.debug("onRejectComponent");
		this.logger.debug("Reject component received with problem " + problem.toString());
		this.handleReceived(EventType.RejectComponent, mapDialog);
	}

	@Override
	public void onInvokeTimeout(MAPDialog mapDialog, Integer invokeId) {
		this.logger.debug("onInvokeTimeout");
		this.handleReceived(EventType.InvokeTimeout, mapDialog);
	}

	@Override
	public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
		this.logger.debug("onProcessUnstructuredSSRequest");
		this.handleReceived(EventType.ProcessUnstructuredSSRequestIndication, procUnstrReqInd);
	}

	@Override
	public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
		this.logger.debug("onProcessUnstructuredSSResponse");
		this.handleReceived(EventType.ProcessUnstructuredSSResponseIndication, procUnstrResInd);
	}

	@Override
	public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
		this.logger.debug("onUnstructuredSSRequest");
		this.handleReceived(EventType.UnstructuredSSRequestIndication, unstrReqInd);
	}

	@Override
	public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
		this.logger.debug("onUnstructuredSSResponse");
		this.handleReceived(EventType.UnstructuredSSResponseIndication, unstrResInd);
	}

	@Override
	public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
		this.logger.debug("onUnstructuredSSNotifyRequest");
		this.handleReceived(EventType.UnstructuredSSNotifyRequestIndication, unstrNotifyInd);
	}

	@Override
	public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
		this.logger.debug("onUnstructuredSSNotifyResponse");
	}

	@Override
	public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
		this.logger.debug("onForwardShortMessageRequest");
		this.handleReceived(EventType.ForwardShortMessageIndication, forwSmInd);
	}

	@Override
	public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {
		this.logger.debug("onForwardShortMessageResponse");
		this.handleReceived(EventType.ForwardShortMessageRespIndication, forwSmRespInd);
	}

	@Override
	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
		this.logger.debug("onMoForwardShortMessageRequest");
		this.handleReceived(EventType.MoForwardShortMessageIndication, moForwSmInd);
	}

	@Override
	public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
		this.logger.debug("onMoForwardShortMessageResponse");
		this.handleReceived(EventType.MoForwardShortMessageRespIndication, moForwSmRespInd);
	}

	@Override
	public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
		this.logger.debug("onMtForwardShortMessageRequest");
		this.handleReceived(EventType.MtForwardShortMessageIndication, mtForwSmInd);
	}

	@Override
	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
		this.logger.debug("onMtForwardShortMessageResponse");
		this.handleReceived(EventType.MtForwardShortMessageRespIndication, mtForwSmRespInd);
	}

	@Override
	public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
		this.logger.debug("onSendRoutingInfoForSMRequest");
		this.handleReceived(EventType.SendRoutingInfoForSMIndication, sendRoutingInfoForSMInd);
	}

	@Override
	public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
		this.logger.debug("onSendRoutingInfoForSMResponse");
		this.handleReceived(EventType.SendRoutingInfoForSMRespIndication, sendRoutingInfoForSMRespInd);
	}

	@Override
	public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
		this.logger.debug("onReportSMDeliveryStatusRequest");
		this.handleReceived(EventType.ReportSMDeliveryStatusIndication, reportSMDeliveryStatusInd);
	}

	@Override
	public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
		this.logger.debug("onReportSMDeliveryStatusResponse");
		this.handleReceived(EventType.ReportSMDeliveryStatusRespIndication, reportSMDeliveryStatusRespInd);
	}

	@Override
	public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd) {
		this.logger.debug("onInformServiceCentreRequest");
		this.handleReceived(EventType.InformServiceCentreIndication, informServiceCentreInd);
	}

	@Override
	public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
		this.logger.debug("onAlertServiceCentreRequest");
		this.handleReceived(EventType.AlertServiceCentreIndication, alertServiceCentreInd);
	}

	@Override
	public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd) {
		this.logger.debug("onAlertServiceCentreResponse");
		this.handleReceived(EventType.AlertServiceCentreRespIndication, alertServiceCentreInd);
	}

	@Override
	public void onMAPMessage(MAPMessage mapMessage) {
	}

	@Override
	public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			AddressString eriImsi, AddressString eriVlrNo) {
		this.logger.debug("onDialogRequestEricsson");
		this.handleReceived(EventType.DialogEricssonRequest, mapDialog);
	}

	@Override
	public void onUpdateLocationRequest(UpdateLocationRequest ind) {
		this.logger.debug("onUpdateLocationRequest");
		this.handleReceived(EventType.UpdateLocation, ind);
	}

	@Override
	public void onUpdateLocationResponse(UpdateLocationResponse ind) {
		this.logger.debug("onUpdateLocationResponse");
		this.handleReceived(EventType.UpdateLocationResp, ind);
	}

	@Override
	public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
		if (ind instanceof SendAuthenticationInfoRequestImplV3) {
			this.logger.debug("onSendAuthenticationInfoRequest_V3");
			this.handleReceived(EventType.SendAuthenticationInfo_V3, ind);

		} else {
			this.logger.debug("onSendAuthenticationInfoRequest_V2");
			this.handleReceived(EventType.SendAuthenticationInfo_V2, ind);
		}
	}

	@Override
	public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
		if (ind instanceof SendAuthenticationInfoResponseImplV3) {
			this.logger.debug("onSendAuthenticationInfoResp_V3");
			this.handleReceived(EventType.SendAuthenticationInfoResp_V3, ind);
		} else {
			this.logger.debug("onSendAuthenticationInfoResp_V2");
			this.handleReceived(EventType.SendAuthenticationInfoResp_V2, ind);
		}
	}

	@Override
	public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest request) {
		this.logger.debug("onAuthenticationFailureReportRequest");
		this.handleReceived(EventType.AuthenticationFailureReport, request);
	}

	@Override
	public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse response) {
		this.logger.debug("onAuthenticationFailureReportResponse");
		this.handleReceived(EventType.AuthenticationFailureReport_Resp, response);
	}

	@Override
	public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest request) {
		this.logger.debug("onAnyTimeInterrogationRequest");
		this.handleReceived(EventType.AnyTimeInterrogation, request);
	}

	@Override
	public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse response) {
		this.logger.debug("onAnyTimeInterrogationResponse");
		this.handleReceived(EventType.AnyTimeInterrogationResp, response);
	}

	@Override
	public void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest request) {
		this.logger.debug("onAnyTimeSubscriptionInterrogationRequest");
		this.handleReceived(EventType.AnyTimeSubscriptionInterrogation, request);
	}

	@Override
	public void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse response) {
		this.logger.debug("onAnyTimeSubscriptionInterrogationResponse");
		this.handleReceived(EventType.AnyTimeSubscriptionInterrogationRes, response);
	}

	@Override
	public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest request) {
		this.logger.debug("onProvideSubscriberInfoRequest");
		this.handleReceived(EventType.ProvideSubscriberInfo, request);
	}

	@Override
	public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse response) {
		this.logger.debug("onProvideSubscriberInfoResponse");
		this.handleReceived(EventType.ProvideSubscriberInfoResp, response);
	}

	@Override
	public void onCheckImeiRequest(CheckImeiRequest request) {
		this.logger.debug("onCheckImeiRequest");
		this.handleReceived(EventType.CheckImei, request);
	}

	@Override
	public void onCheckImeiResponse(CheckImeiResponse response) {
		this.logger.debug("onCheckImeiResponse");
		this.handleReceived(EventType.CheckImeiResp, response);
	}

	@Override
	public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest request) {
		this.logger.debug("onProvideSubscriberLocationRequest");
		this.handleReceived(EventType.ProvideSubscriberLocation, request);
	}

	@Override
	public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse response) {
		this.logger.debug("onProvideSubscriberLocationResponse");
		this.handleReceived(EventType.ProvideSubscriberLocationResp, response);
	}

	@Override
	public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest request) {
		this.logger.debug("onSubscriberLocationReportRequest");
		this.handleReceived(EventType.SubscriberLocationReport, request);
	}

	@Override
	public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse response) {
		this.logger.debug("onSubscriberLocationReportResponse");
		this.handleReceived(EventType.SubscriberLocationReportResp, response);
	}

	@Override
	public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest request) {
		this.logger.debug("onSendRoutingInforForLCSRequest");
		this.handleReceived(EventType.SendRoutingInfoForLCS, request);
	}

	@Override
	public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse response) {
		this.logger.debug("onSendRoutingInforForLCSResponse");
		this.handleReceived(EventType.SendRoutingInfoForLCSResp, response);
	}

	@Override
	public void onCancelLocationRequest(CancelLocationRequest request) {
		this.logger.debug("onCancelLocationRequest");
		this.handleReceived(EventType.CancelLocation, request);
	}

	@Override
	public void onCancelLocationResponse(CancelLocationResponse response) {
		this.logger.debug("onCancelLocationResponse");
		this.handleReceived(EventType.CancelLocationResp, response);
	}

	@Override
	public void onProvideRoamingNumberRequest(ProvideRoamingNumberRequest request) {
		this.logger.debug("onProvideRoamingNumberRequest");
		this.handleReceived(EventType.ProvideRoamingNumber, request);
	}

	@Override
	public void onProvideRoamingNumberResponse(ProvideRoamingNumberResponse response) {
		this.logger.debug("onProvideRoamingNumberResponse");
		this.handleReceived(EventType.ProvideRoamingNumberResp, response);
	}

	@Override
	public void onSendRoutingInformationRequest(SendRoutingInformationRequest request) {
		this.logger.debug("onSendRoutingInformationRequest");
		this.handleReceived(EventType.SendRoutingInformation, request);
	}

	@Override
	public void onSendRoutingInformationResponse(SendRoutingInformationResponse response) {
		this.logger.debug("onSendRoutingInformationResponse");
		this.handleReceived(EventType.SendRoutingInformationResp, response);
	}

	@Override
	public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest request) {
		this.logger.debug("onInsertSubscriberDataRequest");
		this.handleReceived(EventType.InsertSubscriberData, request);
	}

	@Override
	public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse response) {
		this.logger.debug("onInsertSubscriberDataResponse");
		this.handleReceived(EventType.InsertSubscriberDataResp, response);
	}

	@Override
	public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest request) {
		this.logger.debug("onDeleteSubscriberDataRequest");
		this.handleReceived(EventType.DeleteSubscriberData, request);
	}

	@Override
	public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse response) {
		this.logger.debug("onDeleteSubscriberDataResponse");
		this.handleReceived(EventType.DeleteSubscriberDataResp, response);
	}

	@Override
	public void onSendIdentificationRequest(SendIdentificationRequest request) {
		this.logger.debug("onSendIdentificationRequest");
		this.handleReceived(EventType.SendIdentification, request);
	}

	@Override
	public void onSendIdentificationResponse(SendIdentificationResponse response) {
		this.logger.debug("onSendIdentificationResponse");
		this.handleReceived(EventType.SendIdentificationResp, response);
	}

	@Override
	public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest request) {
		this.logger.debug("onUpdateGprsLocationRequest");
		this.handleReceived(EventType.UpdateGprsLocation, request);
	}

	@Override
	public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse response) {
		this.logger.debug("onUpdateGprsLocationResponse");
		this.handleReceived(EventType.UpdateGprsLocationResp, response);
	}

	@Override
	public void onPurgeMSRequest(PurgeMSRequest request) {
		this.logger.debug("onPurgeMSRequest");
		this.handleReceived(EventType.PurgeMS, request);
	}

	@Override
	public void onPurgeMSResponse(PurgeMSResponse response) {
		this.logger.debug("onPurgeMSResponse");
		this.handleReceived(EventType.PurgeMSResp, response);
	}

	@Override
	public void onResetRequest(ResetRequest request) {
		this.logger.debug("onResetRequest");
		this.handleReceived(EventType.Reset, request);
	}

	@Override
	public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest request) {
		this.logger.debug("ForwardCheckSSIndicationRequest");
		this.handleReceived(EventType.ForwardCheckSSIndication, request);
	}

	@Override
	public void onRestoreDataRequest(RestoreDataRequest request) {
		this.logger.debug("onRestoreDataRequest");
		this.handleReceived(EventType.RestoreData, request);
	}

	@Override
	public void onRestoreDataResponse(RestoreDataResponse response) {
		this.logger.debug("onRestoreDataResponse");
		this.handleReceived(EventType.RestoreDataResp, response);
	}

	@Override
	public void onSendImsiRequest(SendImsiRequest request) {
		this.logger.debug("onSendImsiRequest");
		this.handleReceived(EventType.SendImsi, request);
	}

	@Override
	public void onSendImsiResponse(SendImsiResponse response) {
		this.logger.debug("onSendImsiResponse");
		this.handleReceived(EventType.SendImsiResp, response);
	}

	@Override
	public void onRegisterSSRequest(RegisterSSRequest request) {
		this.logger.debug("onRegisterSSRequest");
		this.handleReceived(EventType.RegisterSS, request);
	}

	@Override
	public void onRegisterSSResponse(RegisterSSResponse response) {
		this.logger.debug("onRegisterSSResponse");
		this.handleReceived(EventType.RegisterSSResp, response);
	}

	@Override
	public void onEraseSSRequest(EraseSSRequest request) {
		this.logger.debug("onEraseSSRequest");
		this.handleReceived(EventType.EraseSS, request);
	}

	@Override
	public void onEraseSSResponse(EraseSSResponse response) {
		this.logger.debug("onEraseSSResponse");
		this.handleReceived(EventType.EraseSSResp, response);
	}

	@Override
	public void onActivateSSRequest(ActivateSSRequest request) {
		this.logger.debug("onActivateSSRequest");
		this.handleReceived(EventType.ActivateSS, request);
	}

	@Override
	public void onActivateSSResponse(ActivateSSResponse response) {
		this.logger.debug("onActivateSSResponse");
		this.handleReceived(EventType.ActivateSSResp, response);
	}

	@Override
	public void onDeactivateSSRequest(DeactivateSSRequest request) {
		this.logger.debug("onDeactivateSSRequest");
		this.handleReceived(EventType.DeactivateSS, request);
	}

	@Override
	public void onDeactivateSSResponse(DeactivateSSResponse response) {
		this.logger.debug("onDeactivateSSResponse");
		this.handleReceived(EventType.DeactivateSSResp, response);
	}

	@Override
	public void onInterrogateSSRequest(InterrogateSSRequest request) {
		this.logger.debug("onInterrogateSSRequest");
		this.handleReceived(EventType.InterrogateSS, request);
	}

	@Override
	public void onInterrogateSSResponse(InterrogateSSResponse response) {
		this.logger.debug("onInterrogateSSResponse");
		this.handleReceived(EventType.InterrogateSSResp, response);
	}

	@Override
	public void onReadyForSMRequest(ReadyForSMRequest request) {
		this.logger.debug("onReadyForSMRequest");
		this.handleReceived(EventType.ReadyForSM, request);
	}

	@Override
	public void onReadyForSMResponse(ReadyForSMResponse response) {
		this.logger.debug("onReadyForSMResponse");
		this.handleReceived(EventType.ReadyForSMResp, response);
	}

	@Override
	public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest request) {
		this.logger.debug("onNoteSubscriberPresentRequest");
		this.handleReceived(EventType.NoteSubscriberPresent, request);
	}

	@Override
	public void onSendRoutingInfoForGprsRequest(SendRoutingInfoForGprsRequest request) {
		this.logger.debug("onSendRoutingInfoForGprsRequest");
		this.handleReceived(EventType.SendRoutingInfoForGprs, request);
	}

	@Override
	public void onSendRoutingInfoForGprsResponse(SendRoutingInfoForGprsResponse response) {
		this.logger.debug("onSendRoutingInfoForGprsResponse");
		this.handleReceived(EventType.SendRoutingInfoForGprsResp, response);
	}

	@Override
	public void onActivateTraceModeRequest_Oam(ActivateTraceModeRequest_Oam request) {
		this.logger.debug("onActivateTraceModeRequest");
		this.handleReceived(EventType.ActivateTraceMode, request);
	}

	@Override
	public void onActivateTraceModeResponse_Oam(ActivateTraceModeResponse_Oam response) {
		this.logger.debug("onActivateTraceModeResponse");
		this.handleReceived(EventType.ActivateTraceModeResp, response);
	}

	@Override
	public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility request) {
		this.logger.debug("onActivateTraceModeRequest");
		this.handleReceived(EventType.ActivateTraceMode, request);
	}

	@Override
	public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility response) {
		this.logger.debug("onActivateTraceModeResponse");
		this.handleReceived(EventType.ActivateTraceModeResp, response);
	}

	@Override
	public void onGetPasswordRequest(GetPasswordRequest request) {
		this.logger.debug("onGetPasswordRequest");
		this.handleReceived(EventType.GetPassword, request);
	}

	@Override
	public void onGetPasswordResponse(GetPasswordResponse response) {
		this.logger.debug("onGetPasswordResponse");
		this.handleReceived(EventType.GetPasswordResp, response);
	}

	@Override
	public void onRegisterPasswordRequest(RegisterPasswordRequest request) {
		this.logger.debug("onRegisterPasswordRequest");
		this.handleReceived(EventType.RegisterPassword, request);
	}

	@Override
	public void onRegisterPasswordResponse(RegisterPasswordResponse response) {
		this.logger.debug("onRegisterPasswordResponse");
		this.handleReceived(EventType.RegisterPasswordResp, response);
	}

	@Override
	public void onIstCommandRequest(IstCommandRequest request) {
		this.logger.debug("onSendIstCommandRequest");
		this.handleReceived(EventType.IstCommand, request);
	}

	@Override
	public void onIstCommandResponse(IstCommandResponse response) {
		this.logger.debug("onSendIstCommandResponse");
		this.handleReceived(EventType.IstCommandResp, response);
	}

	public void compareEvents(List<TestEvent> expectedEvents) {
		List<TestEvent> actualEvents = new ArrayList<TestEvent>(observerdEvents);
		if (expectedEvents.size() != actualEvents.size()) {
			String comparedEvents = doStringCompare(expectedEvents, actualEvents);

			fail("Size of received events: " + actualEvents.size() + ", does not equal expected events: "
					+ expectedEvents.size() + "\n" + comparedEvents);
		}

		for (int index = 0; index < expectedEvents.size(); index++) {
			TestEvent expected = expectedEvents.get(index);
			TestEvent actual = actualEvents.get(index);

			assertEvents(expected, actual);
		}
	}

	protected void assertEvents(TestEvent expected, TestEvent actual) {
		try {
			assertEquals(expected, actual);
		} catch (AssertionError ex) {
			if (expected.getEventType() != actual.getEventType())
				fail("Event types between expected " + expected + " and actual " + actual + " are not equal");

			long diff = Math.abs(expected.getTimestamp() - actual.getTimestamp());
			if (diff > TestEvent.MAX_TIMESTAMP_DIFFERENCE) {
				String pattern = "Timestamp difference between expected %s and actual %s is more than allowed (%d): %d";
				fail(String.format(pattern, expected, actual, TestEvent.MAX_TIMESTAMP_DIFFERENCE, diff));
			}

			throw ex;
		}
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
}
