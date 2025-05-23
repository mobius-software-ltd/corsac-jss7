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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPDialogListener;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPMessage;
import org.restcomm.protocols.ss7.cap.api.CAPProvider;
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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest;
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
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

import com.mobius.software.common.dal.timers.TaskCallback;
/**
*
* @author sergey vetyutnev
* @author yulianoifa
*/
public class CallScfExample implements CAPDialogListener, CAPServiceCircuitSwitchedCallListener {

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
    
    public CallScfExample() throws NamingException {
        InitialContext ctx = new InitialContext();
        try {
            String providerJndiName = "java:/restcomm/ss7/cap";
            this.capProvider = ((CAPProvider) ctx.lookup(providerJndiName));
        } finally {
            ctx.close();
        }
        
        capProvider.addCAPDialogListener(UUID.randomUUID(),this);
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

    @Override
    public void onInitialDPRequest(InitialDPRequest ind) {
        this.cc = new CallContent();
        this.cc.step = Step.initialDPRecieved;

        ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
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

        ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
    }

    @Override
    public void onDialogDelimiter(CAPDialog capDialog) {
        try {
            if (this.cc != null)
				switch (this.cc.step) {
                    case initialDPRecieved:
                        // informing SSF of BCSM events processing
                        List<BCSMEvent> bcsmEventList = new ArrayList<BCSMEvent>();
                        BCSMEvent ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(
                                EventTypeBCSM.routeSelectFailure, MonitorMode.notifyAndContinue, null, null, false);
                        bcsmEventList.add(ev);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oCalledPartyBusy,
                                MonitorMode.interrupted, null, null, false);
                        bcsmEventList.add(ev);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oNoAnswer,
                                MonitorMode.interrupted, null, null, false);
                        bcsmEventList.add(ev);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oAnswer,
                                MonitorMode.notifyAndContinue, null, null, false);
                        bcsmEventList.add(ev);
                        LegID legId = this.capProvider.getCAPParameterFactory().createLegID(null, LegType.leg1);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oDisconnect,
                                MonitorMode.notifyAndContinue, legId, null, false);
                        bcsmEventList.add(ev);
                        legId = this.capProvider.getCAPParameterFactory().createLegID(null, LegType.leg2);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oDisconnect,
                                MonitorMode.interrupted, legId, null, false);
                        bcsmEventList.add(ev);
                        ev = this.capProvider.getCAPParameterFactory().createBCSMEvent(EventTypeBCSM.oAbandon,
                                MonitorMode.notifyAndContinue, null, null, false);
                        bcsmEventList.add(ev);
                        currentCapDialog.addRequestReportBCSMEventRequest(bcsmEventList, null);

                        // calculating here a new called party number if it is needed
                        String newNumber = "22123124";
                        if (newNumber != null) {
                            // sending Connect to force routing the call to a new number
                            List<CalledPartyNumberIsup> calledPartyNumber = new ArrayList<CalledPartyNumberIsup>();
                            CalledPartyNumber cpn = this.capProvider.getISUPParameterFactory().createCalledPartyNumber();
                            cpn.setAddress("5599999988");
                            cpn.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
                            cpn.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
                            cpn.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
                            CalledPartyNumberIsup cpnc = this.capProvider.getCAPParameterFactory().createCalledPartyNumber(
                                    cpn);
                            calledPartyNumber.add(cpnc);
                            DestinationRoutingAddress destinationRoutingAddress = this.capProvider.getCAPParameterFactory()
                                    .createDestinationRoutingAddress(calledPartyNumber);
                            currentCapDialog.addConnectRequest(destinationRoutingAddress, null, null, null, null, null, null,
                                    null, null, null, null, null, null, false, false, false, null, false, false);
                        }

                        currentCapDialog.send(dummyCallback);
                        break;

                    case disconnected:
                        // the call is terminated - close dialog
                        currentCapDialog.close(false, dummyCallback);
                        break;
					default:
						break;
                }
        } catch (CAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogTimeout(CAPDialog capDialog) {
        if (currentCapDialog != null && this.cc != null && this.cc.step != Step.disconnected
                && this.cc.activityTestInvokeId == null) {
            // check the SSF if the call is still alive
            currentCapDialog.keepAlive();
            try {
                this.cc.activityTestInvokeId = currentCapDialog.addActivityTestRequest();
                currentCapDialog.send(dummyCallback);
            } catch (CAPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityTestResponse(ActivityTestResponse ind) {
        if (currentCapDialog != null && this.cc != null)
			this.cc.activityTestInvokeId = null;
    }

    @Override
    public void onInvokeTimeout(CAPDialog capDialog, Integer invokeId) {
        if (currentCapDialog != null && this.cc != null)
			if (this.cc.activityTestInvokeId == invokeId)
				try {
                    currentCapDialog.close(true, dummyCallback);
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
    public void onCAPMessage(CAPMessage capMessage) {
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
    public void onCallInformationRequestRequest(CallInformationRequestRequest ind) {
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
    public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
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

    @Override
    public void onCallGapRequest(CallGapRequest ind) {
        // TODO Auto-generated method stub

    }
}
