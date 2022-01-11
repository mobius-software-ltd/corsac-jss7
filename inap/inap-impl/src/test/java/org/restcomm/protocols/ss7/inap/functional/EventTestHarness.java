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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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
 * @author amit bhayani
 * @author servey vetyutnev
 * @author yulian.oifa
 *
 */
public class EventTestHarness implements INAPDialogListener, INAPServiceCircuitSwitchedCallListener {

    private Logger logger = null;

    protected List<TestEvent> observerdEvents = new ArrayList<TestEvent>();
    protected int sequence = 0;
    protected boolean invokeTimeoutSuppressed = false;

    EventTestHarness(Logger logger) {
        this.logger = logger;
    }

    public void suppressInvokeTimeout() {
        invokeTimeoutSuppressed = true;
    }

    public void compareEvents(List<TestEvent> expectedEvents) {

        if (expectedEvents.size() != this.observerdEvents.size()) {
            fail("Size of received events: " + this.observerdEvents.size() + ", does not equal expected events: "
                    + expectedEvents.size() + "\n" + doStringCompare(expectedEvents, observerdEvents));
        }

        for (int index = 0; index < expectedEvents.size(); index++) {
            assertEquals(expectedEvents.get(index), observerdEvents.get(index), "Received event does not match, index[" + index
                    + "]");
        }
    }

    protected String doStringCompare(List<TestEvent> expectedEvents, List<TestEvent> observerdEvents) {
        StringBuilder sb = new StringBuilder();
        int size1 = expectedEvents.size();
        int size2 = observerdEvents.size();
        int count = size1;
        if (count < size2) {
            count = size2;
        }

        for (int index = 0; count > index; index++) {
            String s1 = size1 > index ? expectedEvents.get(index).toString() : "NOP";
            String s2 = size2 > index ? observerdEvents.get(index).toString() : "NOP";
            sb.append(s1).append(" - ").append(s2).append("\n\n");
        }
        return sb.toString();
    }

    @Override
    public void onDialogDelimiter(INAPDialog inapDialog) {
        this.logger.debug("onDialogDelimiter");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogDelimiter, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogRequest(INAPDialog inapDialog) {
        this.logger.debug("onDialogRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRequest, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogAccept(INAPDialog inapDialog) {
        this.logger.debug("onDialogAccept");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogAccept, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason, INAPUserAbortReason userReason) {
        this.logger.debug("onDialogUserAbort");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogProviderAbort(INAPDialog inapDialog, PAbortCauseType abortCause) {
        this.logger.debug("onDialogProviderAbort");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogProviderAbort, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogClose(INAPDialog inapDialog) {
        this.logger.debug("onDialogClose");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogClose, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogRelease(INAPDialog inapDialog) {
        this.logger.debug("onDialogRelease");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRelease, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogTimeout(INAPDialog inapDialog) {
        this.logger.debug("onDialogTimeout");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDialogNotice(INAPDialog inapDialog, INAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
        this.logger.debug("onDialogNotice");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogNotice, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onErrorComponent(INAPDialog inapDialog, Long invokeId, INAPErrorMessage inapErrorMessage) {
        this.logger.debug("onErrorComponent");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ErrorComponent, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRejectComponent(INAPDialog inapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
        this.logger.debug("onRejectComponent");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RejectComponent, inapDialog, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInvokeTimeout(INAPDialog inapDialog, Long invokeId) {
        this.logger.debug("onInvokeTimeout");
        if (!invokeTimeoutSuppressed) {
            TestEvent te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, inapDialog, sequence++);
            this.observerdEvents.add(te);
        }
    }

    @Override
    public void onINAPMessage(INAPMessage inapMessage) {
        
    }

    @Override
    public void onInitialDPRequest(InitialDPRequest ind) {
        this.logger.debug("onInitialDPRequestIndication");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InitialDpRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
        this.logger.debug("onRequestReportBCSMEventRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RequestReportBCSMEventRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onApplyChargingRequest(ApplyChargingRequest ind) {
        this.logger.debug("ApplyChargingRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ApplyChargingRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEventReportBCSMRequest(EventReportBCSMRequest ind) {
        this.logger.debug("EventReportBCSMRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EventReportBCSMRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onContinueRequest(ContinueRequest ind) {
        this.logger.debug("ContinueRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ContinueRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onApplyChargingReportRequest(ApplyChargingReportRequest ind) {
        this.logger.debug("ApplyChargingReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ApplyChargingReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onReleaseCallRequest(ReleaseCallRequest ind) {
        this.logger.debug("ReleaseCallRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ReleaseCallRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onConnectRequest(ConnectRequest ind) {
        this.logger.debug("ConnectRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ConnectRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCallInformationRequest(CallInformationRequest ind) {
        this.logger.debug("CallInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CallInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCallInformationReportRequest(CallInformationReportRequest ind) {
        this.logger.debug("CallInformationReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CallInformationReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivityTestRequest(ActivityTestRequest ind) {
        this.logger.debug("ActivityTestRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivityTestRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onActivityTestResponse(ActivityTestResponse ind) {
        this.logger.debug("ActivityTestResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivityTestResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onAssistRequestInstructionsRequest(AssistRequestInstructionsRequest ind) {
        this.logger.debug("AssistRequestInstructionsRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AssistRequestInstructionsRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onEstablishTemporaryConnectionRequest(EstablishTemporaryConnectionRequest ind) {
        this.logger.debug("EstablishTemporaryConnectionRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EstablishTemporaryConnectionRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
        this.logger.debug("DisconnectForwardConnectionRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DisconnectForwardConnectionRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
        this.logger.debug("ConnectToResourceRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ConnectToResourceRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onResetTimerRequest(ResetTimerRequest ind) {
        this.logger.debug("ResetTimerRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ResetTimerRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onFurnishChargingInformationRequest(FurnishChargingInformationRequest ind) {
        this.logger.debug("FurnishChargingInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.FurnishChargingInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSendChargingInformationRequest(SendChargingInformationRequest ind) {
        this.logger.debug("SendChargingInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SendChargingInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
        this.logger.debug("SpecializedResourceReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SpecializedResourceReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
        this.logger.debug("PlayAnnouncementRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.PlayAnnouncementRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
        this.logger.debug("PromptAndCollectUserInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onPromptAndCollectUserInformationResponse(PromptAndCollectUserInformationResponse ind) {
        this.logger.debug("PromptAndCollectUserInformationResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.PromptAndCollectUserInformationResponse, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCancelRequest(CancelRequest ind) {
        this.logger.debug("CancelRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CancelRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onContinueWithArgumentRequest(ContinueWithArgumentRequest ind) {
        this.logger.debug("ContinueWithArgumentRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ContinueWithArgumentRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onInitiateCallAttemptRequest(InitiateCallAttemptRequest ind) {
        this.logger.debug("InitiateCallAttemptRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.InitiateCallAttemptRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCollectInformationRequest(CollectInformationRequest ind) {
        this.logger.debug("CollectInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CollectInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

    @Override
    public void onCallGapRequest(CallGapRequest ind) {
        this.logger.debug("CallGapRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CallGapRequest, ind, sequence++);
        this.observerdEvents.add(te);
    }

	@Override
	public void onActivateServiceFilteringRequest(ActivateServiceFilteringRequest ind) {
		this.logger.debug("ActivateServiceFilteringRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ActivateServiceFilteringRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onEventNotificationChargingRequest(EventNotificationChargingRequest ind) {
		this.logger.debug("EventNotificationChargingRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.EventNotificationChargingRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onRequestNotificationChargingEventRequest(RequestNotificationChargingEventRequest ind) {
		this.logger.debug("RequestNotificationChargingEventRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RequestNotificationChargingEventRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onServiceFilteringResponseRequest(ServiceFilteringResponseRequest ind) {
		this.logger.debug("ServiceFilteringResponseRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ServiceFilteringResponseRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onAnalysedInformationRequest(AnalysedInformationRequest ind) {
		this.logger.debug("AnalysedInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AnalysedInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onAnalyseInformationRequest(AnalyseInformationRequest ind) {
		this.logger.debug("AnalyseInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.AnalyseInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onCancelStatusReportRequest(CancelStatusReportRequest ind) {
		this.logger.debug("CancelStatusReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CancelStatusReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onCollectedInformationRequest(CollectedInformationRequest ind) {
		this.logger.debug("CollectedInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CollectedInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onHoldCallInNetworkRequest(HoldCallInNetworkRequest ind) {
		this.logger.debug("HoldCallInNetworkRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.HoldCallInNetworkRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onOMidCallRequest(OMidCallRequest ind) {
		this.logger.debug("OMidCallRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.OMidCallRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onTMidCallRequest(TMidCallRequest ind) {
		this.logger.debug("TMidCallRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.TMidCallRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onOAnswerRequest(OAnswerRequest ind) {
		this.logger.debug("OAnswerRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.OAnswerRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onOriginationAttemptAuthorizedRequest(OriginationAttemptAuthorizedRequest ind) {
		this.logger.debug("OriginationAttemptAuthorizedRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.OriginationAttemptAuthorizedRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onRouteSelectFailureRequest(RouteSelectFailureRequest ind) {
		this.logger.debug("RouteSelectFailureRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RouteSelectFailureRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onOCalledPartyBusyRequest(OCalledPartyBusyRequest ind) {
		this.logger.debug("OCalledPartyBusyRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.OCalledPartyBusyRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onONoAnswerRequest(ONoAnswerRequest ind) {
		this.logger.debug("ONoAnswerRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ONoAnswerRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onODisconnectRequest(ODisconnectRequest ind) {
		this.logger.debug("ODisconnectRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ODisconnectRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onTermAttemptAuthorizedRequest(TermAttemptAuthorizedRequest ind) {
		this.logger.debug("TermAttemptAuthorizedRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.TermAttemptAuthorizedRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onTBusyRequest(TBusyRequest ind) {
		this.logger.debug("TBusyRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.TBusyRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onTNoAnswerRequest(TNoAnswerRequest ind) {
		this.logger.debug("TNoAnswerRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.TNoAnswerRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onTAnswerRequest(TAnswerRequest ind) {
		this.logger.debug("TAnswerRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.TAnswerRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onTDisconnectRequest(TDisconnectRequest ind) {
		this.logger.debug("TDisconnectRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.TDisconnectRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onSelectRouteRequest(SelectRouteRequest ind) {
		this.logger.debug("SelectRouteRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SelectRouteRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onSelectFacilityRequest(SelectFacilityRequest ind) {
		this.logger.debug("SelectFacilityRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SelectFacilityRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onRequestCurrentStatusReportRequest(RequestCurrentStatusReportRequest ind) {
		this.logger.debug("RequestCurrentStatusReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RequestCurrentStatusReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onRequestCurrentStatusReportResponse(RequestCurrentStatusReportResponse ind) {
		this.logger.debug("RequestCurrentStatusReportResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RequestCurrentStatusReportResponse, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onRequestEveryStatusChangeReportRequest(RequestEveryStatusChangeReportRequest ind) {
		this.logger.debug("RequestEveryStatusChangeReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RequestEveryStatusChangeReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onRequestFirstStatusMatchReportRequest(RequestFirstStatusMatchReportRequest ind) {
		this.logger.debug("RequestFirstStatusMatchReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RequestFirstStatusMatchReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onStatusReportRequest(StatusReportRequest ind) {
		this.logger.debug("StatusReportRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.StatusReportRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onUpdateRequest(UpdateRequest ind) {
		this.logger.debug("UpdateRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UpdateRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onUpdateResponse(UpdateResponse ind) {
		this.logger.debug("UpdateResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.UpdateResponse, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onRetrieveRequest(RetrieveRequest ind) {
		this.logger.debug("RetrieveRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RetrieveRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onRetrieveResponse(RetrieveResponse ind) {
		this.logger.debug("RetrieveResponse");
        TestEvent te = TestEvent.createReceivedEvent(EventType.RetrieveResponse, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onSignallingInformationRequest(SignallingInformationRequest ind) {
		this.logger.debug("SignallingInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.SignallingInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onReleaseCallPartyConnectionRequest(ReleaseCallPartyConnectionRequest ind) {
		this.logger.debug("ReleaseCallPartyConnectionRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ReleaseCallPartyConnectionRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onReconnectRequest(ReconnectRequest ind) {
		this.logger.debug("ReconnectRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.ReconnectRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onHoldCallPartyConnectionRequest(HoldCallPartyConnectionRequest ind) {
		this.logger.debug("HoldCallPartyConnectionRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.HoldCallPartyConnectionRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onHandOverRequest(HandOverRequest ind) {
		this.logger.debug("HandOverRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.HandOverRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onDialogueUserInformationRequest(DialogueUserInformationRequest ind) {
		this.logger.debug("DialogueUserInformationRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogueUserInformationRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}

	@Override
	public void onCallLimitRequest(CallLimitRequest ind) {
		this.logger.debug("CallLimitRequest");
        TestEvent te = TestEvent.createReceivedEvent(EventType.CallLimitRequest, ind, sequence++);
        this.observerdEvents.add(te);
	}
}
