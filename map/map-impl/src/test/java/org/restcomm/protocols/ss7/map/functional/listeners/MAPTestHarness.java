package org.restcomm.protocols.ss7.map.functional.listeners;

import java.util.Arrays;

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
import org.restcomm.protocols.ss7.map.functional.listeners.events.EventType;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImplV3;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventHarness;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public abstract class MAPTestHarness extends TestEventHarness<EventType> implements MAPDialogListener,
		MAPServiceSupplementaryListener, MAPServiceSmsListener, MAPServiceMobilityListener, MAPServiceLsmListener,
		MAPServiceCallHandlingListener, MAPServiceOamListener, MAPServicePdpContextActivationListener {
	private Logger logger = null;

	private MAPDialog currentDialog;

	public MAPTestHarness(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void onDialogDelimiter(MAPDialog mapDialog) {
		currentDialog = mapDialog;

		this.logger.debug("onDialogDelimiter");
		super.handleReceived(EventType.DialogDelimiter, mapDialog);
	}

	@Override
	public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			MAPExtensionContainer extensionContainer) {
		currentDialog = mapDialog;

		// assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

		this.logger.debug("onDialogRequest");
		super.handleReceived(EventType.DialogRequest, mapDialog);
	}

	@Override
	public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
		currentDialog = mapDialog;

		this.logger.debug("onDialogAccept");
		// assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

		super.handleReceived(EventType.DialogAccept, mapDialog);
	}

	@Override
	public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
			ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
		currentDialog = mapDialog;

		this.logger.debug("onDialogReject");
		super.handleReceived(EventType.DialogReject,
				Arrays.asList(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer));
	}

	@Override
	public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
			MAPExtensionContainer extensionContainer) {
		currentDialog = mapDialog;

		this.logger.debug("onDialogUserAbort");
		super.handleReceived(EventType.DialogUserAbort, Arrays.asList(mapDialog, userReason, extensionContainer));
	}

	@Override
	public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
			MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
		currentDialog = mapDialog;

		this.logger.debug("onDialogProviderAbort");
		super.handleReceived(EventType.DialogProviderAbort,
				Arrays.asList(mapDialog, abortProviderReason, abortSource, extensionContainer));
	}

	@Override
	public void onDialogClose(MAPDialog mapDialog) {
		currentDialog = mapDialog;

		this.logger.debug("onDialogClose");
		super.handleReceived(EventType.DialogClose, mapDialog);
	}

	@Override
	public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
		currentDialog = mapDialog;

		this.logger.debug("onDialogNotice");
		super.handleReceived(EventType.DialogNotice, mapDialog);
	}

	@Override
	public void onDialogRelease(MAPDialog mapDialog) {
		currentDialog = mapDialog;

		this.logger.debug("onDialogRelease");
		super.handleReceived(EventType.DialogRelease, mapDialog);
	}

	@Override
	public void onDialogTimeout(MAPDialog mapDialog) {
		currentDialog = mapDialog;

		this.logger.debug("onDialogTimeout");
		super.handleReceived(EventType.DialogTimeout, mapDialog);
	}

	@Override
	public void onErrorComponent(MAPDialog mapDialog, Integer invokeId, MAPErrorMessage mapErrorMessage) {
		currentDialog = mapDialog;

		this.logger.debug("onErrorComponent");
		super.handleReceived(EventType.ErrorComponent, mapDialog);
	}

	@Override
	public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem, boolean isLocalOriginated) {
		currentDialog = mapDialog;

		this.logger.debug("onRejectComponent");
		this.logger.debug("Reject component received with problem " + problem.toString());
		super.handleReceived(EventType.RejectComponent, mapDialog);
	}

	@Override
	public void onInvokeTimeout(MAPDialog mapDialog, Integer invokeId) {
		currentDialog = mapDialog;

		this.logger.debug("onInvokeTimeout");
		super.handleReceived(EventType.InvokeTimeout, mapDialog);
	}

	@Override
	public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
		this.logger.debug("onProcessUnstructuredSSRequest");
		super.handleReceived(EventType.ProcessUnstructuredSSRequestIndication, procUnstrReqInd);
	}

	@Override
	public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
		this.logger.debug("onProcessUnstructuredSSResponse");
		super.handleReceived(EventType.ProcessUnstructuredSSResponseIndication, procUnstrResInd);
	}

	@Override
	public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
		this.logger.debug("onUnstructuredSSRequest");
		super.handleReceived(EventType.UnstructuredSSRequestIndication, unstrReqInd);
	}

	@Override
	public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
		this.logger.debug("onUnstructuredSSResponse");
		super.handleReceived(EventType.UnstructuredSSResponseIndication, unstrResInd);
	}

	@Override
	public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
		this.logger.debug("onUnstructuredSSNotifyRequest");
		super.handleReceived(EventType.UnstructuredSSNotifyRequestIndication, unstrNotifyInd);
	}

	@Override
	public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
		this.logger.debug("onUnstructuredSSNotifyResponse");
	}

	@Override
	public void onForwardShortMessageRequest(ForwardShortMessageRequest forwSmInd) {
		this.logger.debug("onForwardShortMessageRequest");
		super.handleReceived(EventType.ForwardShortMessageIndication, forwSmInd);
	}

	@Override
	public void onForwardShortMessageResponse(ForwardShortMessageResponse forwSmRespInd) {
		this.logger.debug("onForwardShortMessageResponse");
		super.handleReceived(EventType.ForwardShortMessageRespIndication, forwSmRespInd);
	}

	@Override
	public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
		this.logger.debug("onMoForwardShortMessageRequest");
		super.handleReceived(EventType.MoForwardShortMessageIndication, moForwSmInd);
	}

	@Override
	public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
		this.logger.debug("onMoForwardShortMessageResponse");
		super.handleReceived(EventType.MoForwardShortMessageRespIndication, moForwSmRespInd);
	}

	@Override
	public void onMtForwardShortMessageRequest(MtForwardShortMessageRequest mtForwSmInd) {
		this.logger.debug("onMtForwardShortMessageRequest");
		super.handleReceived(EventType.MtForwardShortMessageIndication, mtForwSmInd);
	}

	@Override
	public void onMtForwardShortMessageResponse(MtForwardShortMessageResponse mtForwSmRespInd) {
		this.logger.debug("onMtForwardShortMessageResponse");
		super.handleReceived(EventType.MtForwardShortMessageRespIndication, mtForwSmRespInd);
	}

	@Override
	public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest sendRoutingInfoForSMInd) {
		this.logger.debug("onSendRoutingInfoForSMRequest");
		super.handleReceived(EventType.SendRoutingInfoForSMIndication, sendRoutingInfoForSMInd);
	}

	@Override
	public void onSendRoutingInfoForSMResponse(SendRoutingInfoForSMResponse sendRoutingInfoForSMRespInd) {
		this.logger.debug("onSendRoutingInfoForSMResponse");
		super.handleReceived(EventType.SendRoutingInfoForSMRespIndication, sendRoutingInfoForSMRespInd);
	}

	@Override
	public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd) {
		this.logger.debug("onReportSMDeliveryStatusRequest");
		super.handleReceived(EventType.ReportSMDeliveryStatusIndication, reportSMDeliveryStatusInd);
	}

	@Override
	public void onReportSMDeliveryStatusResponse(ReportSMDeliveryStatusResponse reportSMDeliveryStatusRespInd) {
		this.logger.debug("onReportSMDeliveryStatusResponse");
		super.handleReceived(EventType.ReportSMDeliveryStatusRespIndication, reportSMDeliveryStatusRespInd);
	}

	@Override
	public void onInformServiceCentreRequest(InformServiceCentreRequest informServiceCentreInd) {
		this.logger.debug("onInformServiceCentreRequest");
		super.handleReceived(EventType.InformServiceCentreIndication, informServiceCentreInd);
	}

	@Override
	public void onAlertServiceCentreRequest(AlertServiceCentreRequest alertServiceCentreInd) {
		this.logger.debug("onAlertServiceCentreRequest");
		super.handleReceived(EventType.AlertServiceCentreIndication, alertServiceCentreInd);
	}

	@Override
	public void onAlertServiceCentreResponse(AlertServiceCentreResponse alertServiceCentreInd) {
		this.logger.debug("onAlertServiceCentreResponse");
		super.handleReceived(EventType.AlertServiceCentreRespIndication, alertServiceCentreInd);
	}

	@Override
	public void onMAPMessage(MAPMessage mapMessage) {
	}

	@Override
	public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
			AddressString eriImsi, AddressString eriVlrNo) {
		currentDialog = mapDialog;

		this.logger.debug("onDialogRequestEricsson");
		super.handleReceived(EventType.DialogEricssonRequest, mapDialog);
	}

	@Override
	public void onUpdateLocationRequest(UpdateLocationRequest ind) {
		this.logger.debug("onUpdateLocationRequest");
		super.handleReceived(EventType.UpdateLocation, ind);
	}

	@Override
	public void onUpdateLocationResponse(UpdateLocationResponse ind) {
		this.logger.debug("onUpdateLocationResponse");
		super.handleReceived(EventType.UpdateLocationResp, ind);
	}

	@Override
	public void onSendAuthenticationInfoRequest(SendAuthenticationInfoRequest ind) {
		if (ind instanceof SendAuthenticationInfoRequestImplV3) {
			this.logger.debug("onSendAuthenticationInfoRequest_V3");
			super.handleReceived(EventType.SendAuthenticationInfo_V3, ind);

		} else {
			this.logger.debug("onSendAuthenticationInfoRequest_V2");
			super.handleReceived(EventType.SendAuthenticationInfo_V2, ind);
		}
	}

	@Override
	public void onSendAuthenticationInfoResponse(SendAuthenticationInfoResponse ind) {
		if (ind instanceof SendAuthenticationInfoResponseImplV3) {
			this.logger.debug("onSendAuthenticationInfoResp_V3");
			super.handleReceived(EventType.SendAuthenticationInfoResp_V3, ind);
		} else {
			this.logger.debug("onSendAuthenticationInfoResp_V2");
			super.handleReceived(EventType.SendAuthenticationInfoResp_V2, ind);
		}
	}

	@Override
	public void onAuthenticationFailureReportRequest(AuthenticationFailureReportRequest request) {
		this.logger.debug("onAuthenticationFailureReportRequest");
		super.handleReceived(EventType.AuthenticationFailureReport, request);
	}

	@Override
	public void onAuthenticationFailureReportResponse(AuthenticationFailureReportResponse response) {
		this.logger.debug("onAuthenticationFailureReportResponse");
		super.handleReceived(EventType.AuthenticationFailureReport_Resp, response);
	}

	@Override
	public void onAnyTimeInterrogationRequest(AnyTimeInterrogationRequest request) {
		this.logger.debug("onAnyTimeInterrogationRequest");
		super.handleReceived(EventType.AnyTimeInterrogation, request);
	}

	@Override
	public void onAnyTimeInterrogationResponse(AnyTimeInterrogationResponse response) {
		this.logger.debug("onAnyTimeInterrogationResponse");
		super.handleReceived(EventType.AnyTimeInterrogationResp, response);
	}

	@Override
	public void onAnyTimeSubscriptionInterrogationRequest(AnyTimeSubscriptionInterrogationRequest request) {
		this.logger.debug("onAnyTimeSubscriptionInterrogationRequest");
		super.handleReceived(EventType.AnyTimeSubscriptionInterrogation, request);
	}

	@Override
	public void onAnyTimeSubscriptionInterrogationResponse(AnyTimeSubscriptionInterrogationResponse response) {
		this.logger.debug("onAnyTimeSubscriptionInterrogationResponse");
		super.handleReceived(EventType.AnyTimeSubscriptionInterrogationRes, response);
	}

	@Override
	public void onProvideSubscriberInfoRequest(ProvideSubscriberInfoRequest request) {
		this.logger.debug("onProvideSubscriberInfoRequest");
		super.handleReceived(EventType.ProvideSubscriberInfo, request);
	}

	@Override
	public void onProvideSubscriberInfoResponse(ProvideSubscriberInfoResponse response) {
		this.logger.debug("onProvideSubscriberInfoResponse");
		super.handleReceived(EventType.ProvideSubscriberInfoResp, response);
	}

	@Override
	public void onCheckImeiRequest(CheckImeiRequest request) {
		this.logger.debug("onCheckImeiRequest");
		super.handleReceived(EventType.CheckImei, request);
	}

	@Override
	public void onCheckImeiResponse(CheckImeiResponse response) {
		this.logger.debug("onCheckImeiResponse");
		super.handleReceived(EventType.CheckImeiResp, response);
	}

	@Override
	public void onProvideSubscriberLocationRequest(ProvideSubscriberLocationRequest request) {
		this.logger.debug("onProvideSubscriberLocationRequest");
		super.handleReceived(EventType.ProvideSubscriberLocation, request);
	}

	@Override
	public void onProvideSubscriberLocationResponse(ProvideSubscriberLocationResponse response) {
		this.logger.debug("onProvideSubscriberLocationResponse");
		super.handleReceived(EventType.ProvideSubscriberLocationResp, response);
	}

	@Override
	public void onSubscriberLocationReportRequest(SubscriberLocationReportRequest request) {
		this.logger.debug("onSubscriberLocationReportRequest");
		super.handleReceived(EventType.SubscriberLocationReport, request);
	}

	@Override
	public void onSubscriberLocationReportResponse(SubscriberLocationReportResponse response) {
		this.logger.debug("onSubscriberLocationReportResponse");
		super.handleReceived(EventType.SubscriberLocationReportResp, response);
	}

	@Override
	public void onSendRoutingInfoForLCSRequest(SendRoutingInfoForLCSRequest request) {
		this.logger.debug("onSendRoutingInforForLCSRequest");
		super.handleReceived(EventType.SendRoutingInfoForLCS, request);
	}

	@Override
	public void onSendRoutingInfoForLCSResponse(SendRoutingInfoForLCSResponse response) {
		this.logger.debug("onSendRoutingInforForLCSResponse");
		super.handleReceived(EventType.SendRoutingInfoForLCSResp, response);
	}

	@Override
	public void onCancelLocationRequest(CancelLocationRequest request) {
		this.logger.debug("onCancelLocationRequest");
		super.handleReceived(EventType.CancelLocation, request);
	}

	@Override
	public void onCancelLocationResponse(CancelLocationResponse response) {
		this.logger.debug("onCancelLocationResponse");
		super.handleReceived(EventType.CancelLocationResp, response);
	}

	@Override
	public void onProvideRoamingNumberRequest(ProvideRoamingNumberRequest request) {
		this.logger.debug("onProvideRoamingNumberRequest");
		super.handleReceived(EventType.ProvideRoamingNumber, request);
	}

	@Override
	public void onProvideRoamingNumberResponse(ProvideRoamingNumberResponse response) {
		this.logger.debug("onProvideRoamingNumberResponse");
		super.handleReceived(EventType.ProvideRoamingNumberResp, response);
	}

	@Override
	public void onSendRoutingInformationRequest(SendRoutingInformationRequest request) {
		this.logger.debug("onSendRoutingInformationRequest");
		super.handleReceived(EventType.SendRoutingInformation, request);
	}

	@Override
	public void onSendRoutingInformationResponse(SendRoutingInformationResponse response) {
		this.logger.debug("onSendRoutingInformationResponse");
		super.handleReceived(EventType.SendRoutingInformationResp, response);
	}

	@Override
	public void onInsertSubscriberDataRequest(InsertSubscriberDataRequest request) {
		this.logger.debug("onInsertSubscriberDataRequest");
		super.handleReceived(EventType.InsertSubscriberData, request);
	}

	@Override
	public void onInsertSubscriberDataResponse(InsertSubscriberDataResponse response) {
		this.logger.debug("onInsertSubscriberDataResponse");
		super.handleReceived(EventType.InsertSubscriberDataResp, response);
	}

	@Override
	public void onDeleteSubscriberDataRequest(DeleteSubscriberDataRequest request) {
		this.logger.debug("onDeleteSubscriberDataRequest");
		super.handleReceived(EventType.DeleteSubscriberData, request);
	}

	@Override
	public void onDeleteSubscriberDataResponse(DeleteSubscriberDataResponse response) {
		this.logger.debug("onDeleteSubscriberDataResponse");
		super.handleReceived(EventType.DeleteSubscriberDataResp, response);
	}

	@Override
	public void onSendIdentificationRequest(SendIdentificationRequest request) {
		this.logger.debug("onSendIdentificationRequest");
		super.handleReceived(EventType.SendIdentification, request);
	}

	@Override
	public void onSendIdentificationResponse(SendIdentificationResponse response) {
		this.logger.debug("onSendIdentificationResponse");
		super.handleReceived(EventType.SendIdentificationResp, response);
	}

	@Override
	public void onUpdateGprsLocationRequest(UpdateGprsLocationRequest request) {
		this.logger.debug("onUpdateGprsLocationRequest");
		super.handleReceived(EventType.UpdateGprsLocation, request);
	}

	@Override
	public void onUpdateGprsLocationResponse(UpdateGprsLocationResponse response) {
		this.logger.debug("onUpdateGprsLocationResponse");
		super.handleReceived(EventType.UpdateGprsLocationResp, response);
	}

	@Override
	public void onPurgeMSRequest(PurgeMSRequest request) {
		this.logger.debug("onPurgeMSRequest");
		super.handleReceived(EventType.PurgeMS, request);
	}

	@Override
	public void onPurgeMSResponse(PurgeMSResponse response) {
		this.logger.debug("onPurgeMSResponse");
		super.handleReceived(EventType.PurgeMSResp, response);
	}

	@Override
	public void onResetRequest(ResetRequest request) {
		this.logger.debug("onResetRequest");
		super.handleReceived(EventType.Reset, request);
	}

	@Override
	public void onForwardCheckSSIndicationRequest(ForwardCheckSSIndicationRequest request) {
		this.logger.debug("ForwardCheckSSIndicationRequest");
		super.handleReceived(EventType.ForwardCheckSSIndication, request);
	}

	@Override
	public void onRestoreDataRequest(RestoreDataRequest request) {
		this.logger.debug("onRestoreDataRequest");
		super.handleReceived(EventType.RestoreData, request);
	}

	@Override
	public void onRestoreDataResponse(RestoreDataResponse response) {
		this.logger.debug("onRestoreDataResponse");
		super.handleReceived(EventType.RestoreDataResp, response);
	}

	@Override
	public void onSendImsiRequest(SendImsiRequest request) {
		this.logger.debug("onSendImsiRequest");
		super.handleReceived(EventType.SendImsi, request);
	}

	@Override
	public void onSendImsiResponse(SendImsiResponse response) {
		this.logger.debug("onSendImsiResponse");
		super.handleReceived(EventType.SendImsiResp, response);
	}

	@Override
	public void onRegisterSSRequest(RegisterSSRequest request) {
		this.logger.debug("onRegisterSSRequest");
		super.handleReceived(EventType.RegisterSS, request);
	}

	@Override
	public void onRegisterSSResponse(RegisterSSResponse response) {
		this.logger.debug("onRegisterSSResponse");
		super.handleReceived(EventType.RegisterSSResp, response);
	}

	@Override
	public void onEraseSSRequest(EraseSSRequest request) {
		this.logger.debug("onEraseSSRequest");
		super.handleReceived(EventType.EraseSS, request);
	}

	@Override
	public void onEraseSSResponse(EraseSSResponse response) {
		this.logger.debug("onEraseSSResponse");
		super.handleReceived(EventType.EraseSSResp, response);
	}

	@Override
	public void onActivateSSRequest(ActivateSSRequest request) {
		this.logger.debug("onActivateSSRequest");
		super.handleReceived(EventType.ActivateSS, request);
	}

	@Override
	public void onActivateSSResponse(ActivateSSResponse response) {
		this.logger.debug("onActivateSSResponse");
		super.handleReceived(EventType.ActivateSSResp, response);
	}

	@Override
	public void onDeactivateSSRequest(DeactivateSSRequest request) {
		this.logger.debug("onDeactivateSSRequest");
		super.handleReceived(EventType.DeactivateSS, request);
	}

	@Override
	public void onDeactivateSSResponse(DeactivateSSResponse response) {
		this.logger.debug("onDeactivateSSResponse");
		super.handleReceived(EventType.DeactivateSSResp, response);
	}

	@Override
	public void onInterrogateSSRequest(InterrogateSSRequest request) {
		this.logger.debug("onInterrogateSSRequest");
		super.handleReceived(EventType.InterrogateSS, request);
	}

	@Override
	public void onInterrogateSSResponse(InterrogateSSResponse response) {
		this.logger.debug("onInterrogateSSResponse");
		super.handleReceived(EventType.InterrogateSSResp, response);
	}

	@Override
	public void onReadyForSMRequest(ReadyForSMRequest request) {
		this.logger.debug("onReadyForSMRequest");
		super.handleReceived(EventType.ReadyForSM, request);
	}

	@Override
	public void onReadyForSMResponse(ReadyForSMResponse response) {
		this.logger.debug("onReadyForSMResponse");
		super.handleReceived(EventType.ReadyForSMResp, response);
	}

	@Override
	public void onNoteSubscriberPresentRequest(NoteSubscriberPresentRequest request) {
		this.logger.debug("onNoteSubscriberPresentRequest");
		super.handleReceived(EventType.NoteSubscriberPresent, request);
	}

	@Override
	public void onSendRoutingInfoForGprsRequest(SendRoutingInfoForGprsRequest request) {
		this.logger.debug("onSendRoutingInfoForGprsRequest");
		super.handleReceived(EventType.SendRoutingInfoForGprs, request);
	}

	@Override
	public void onSendRoutingInfoForGprsResponse(SendRoutingInfoForGprsResponse response) {
		this.logger.debug("onSendRoutingInfoForGprsResponse");
		super.handleReceived(EventType.SendRoutingInfoForGprsResp, response);
	}

	@Override
	public void onActivateTraceModeRequest_Oam(ActivateTraceModeRequest_Oam request) {
		this.logger.debug("onActivateTraceModeRequest");
		super.handleReceived(EventType.ActivateTraceMode, request);
	}

	@Override
	public void onActivateTraceModeResponse_Oam(ActivateTraceModeResponse_Oam response) {
		this.logger.debug("onActivateTraceModeResponse");
		super.handleReceived(EventType.ActivateTraceModeResp, response);
	}

	@Override
	public void onActivateTraceModeRequest_Mobility(ActivateTraceModeRequest_Mobility request) {
		this.logger.debug("onActivateTraceModeRequest");
		super.handleReceived(EventType.ActivateTraceMode, request);
	}

	@Override
	public void onActivateTraceModeResponse_Mobility(ActivateTraceModeResponse_Mobility response) {
		this.logger.debug("onActivateTraceModeResponse");
		super.handleReceived(EventType.ActivateTraceModeResp, response);
	}

	@Override
	public void onGetPasswordRequest(GetPasswordRequest request) {
		this.logger.debug("onGetPasswordRequest");
		super.handleReceived(EventType.GetPassword, request);
	}

	@Override
	public void onGetPasswordResponse(GetPasswordResponse response) {
		this.logger.debug("onGetPasswordResponse");
		super.handleReceived(EventType.GetPasswordResp, response);
	}

	@Override
	public void onRegisterPasswordRequest(RegisterPasswordRequest request) {
		this.logger.debug("onRegisterPasswordRequest");
		super.handleReceived(EventType.RegisterPassword, request);
	}

	@Override
	public void onRegisterPasswordResponse(RegisterPasswordResponse response) {
		this.logger.debug("onRegisterPasswordResponse");
		super.handleReceived(EventType.RegisterPasswordResp, response);
	}

	@Override
	public void onIstCommandRequest(IstCommandRequest request) {
		this.logger.debug("onSendIstCommandRequest");
		super.handleReceived(EventType.IstCommand, request);
	}

	@Override
	public void onIstCommandResponse(IstCommandResponse response) {
		this.logger.debug("onSendIstCommandResponse");
		super.handleReceived(EventType.IstCommandResp, response);
	}

	public MAPDialog getCurrentDialog() {
		return currentDialog;
	}
}
