/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.inap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPDialogListener;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPMessage;
import org.restcomm.protocols.ss7.inap.api.INAPParameterFactory;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentException;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.inap.api.INAPProvider;
import org.restcomm.protocols.ss7.inap.api.INAPServiceBase;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPDialogState;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPGeneralAbortReason;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPUserAbortReason;
import org.restcomm.protocols.ss7.inap.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorCode;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessage;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageFactory;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPServiceCircuitSwitchedCall;
import org.restcomm.protocols.ss7.inap.dialog.INAPUserAbortPrimitiveImpl;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.ISUPParameterFactoryImpl;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCListener;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCodeType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPProviderImpl implements INAPProvider, TCListener {
	private static final long serialVersionUID = 1L;


	protected final transient Logger loger;


    private transient ConcurrentHashMap<UUID,INAPDialogListener> dialogListeners = new ConcurrentHashMap<UUID,INAPDialogListener>();

//    protected transient FastMap<Long, INAPDialogImpl> dialogs = new FastMap<Long, INAPDialogImpl>().shared();
    protected transient ConcurrentHashMap<Long, INAPDialogImpl> dialogs = new ConcurrentHashMap<Long, INAPDialogImpl>();

    private transient TCAPProvider tcapProvider = null;

    private final transient INAPParameterFactory inapParameterFactory = new INAPParameterFactoryImpl();
    private final transient ISUPParameterFactory isupParameterFactory = new ISUPParameterFactoryImpl();
    private final transient INAPErrorMessageFactory inapErrorMessageFactory = new INAPErrorMessageFactoryImpl();

    protected transient Set<INAPServiceBase> inapServices = new HashSet<INAPServiceBase>();
    private final transient INAPServiceCircuitSwitchedCall inapServiceCircuitSwitchedCall = new INAPServiceCircuitSwitchedCallImpl(
            this);
    
    public INAPProviderImpl(String name, TCAPProvider tcapProvider) {
        this.tcapProvider = tcapProvider;

        this.loger = Logger.getLogger(INAPStackImpl.class.getCanonicalName() + "-" + name);

        this.inapServices.add(this.inapServiceCircuitSwitchedCall);
        
        try {
        	//registering user information options
        	/*tcapProvider.getParser().registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, CAPGprsReferenceNumberImpl.class);
        	
        	ErrorCodeImpl errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)CAPErrorCode.cancelFailed);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, CAPErrorMessageCancelFailedImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)CAPErrorCode.requestedInfoError);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, CAPErrorMessageRequestedInfoErrorImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)CAPErrorCode.systemFailure);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, CAPErrorMessageSystemFailureImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)CAPErrorCode.taskRefused);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, CAPErrorMessageTaskRefusedImpl.class);
        	
        	//registering error options
        	tcapProvider.getParser().registerAlternativeClassMapping(CAPErrorMessageCancelFailedImpl.class, CAPErrorMessageCancelFailedImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CAPErrorMessageRequestedInfoErrorImpl.class, CAPErrorMessageRequestedInfoErrorImpl.class);        	
        	tcapProvider.getParser().registerAlternativeClassMapping(CAPErrorMessageSystemFailureImpl.class, CAPErrorMessageSystemFailureImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CAPErrorMessageTaskRefusedImpl.class, CAPErrorMessageTaskRefusedImpl.class);
        	
        	//register requests mappings
        	OperationCodeImpl opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.activityTest);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ActivityTestRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.applyChargingReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ApplyChargingReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.applyCharging);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ApplyChargingRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.assistRequestInstructions);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, AssistRequestInstructionsRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.callGap);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CallGapRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.callInformationReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CallInformationReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.callInformationRequest);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CallInformationRequestRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.cancelCode);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CancelRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.collectInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CollectInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.connect);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ConnectRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.connectToResource);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ConnectToResourceRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.continueCode);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ContinueRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.continueWithArgument);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ContinueWithArgumentRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.disconnectForwardConnection);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, DisconnectForwardConnectionRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.dFCWithArgument);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, DisconnectForwardConnectionWithArgumentRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.disconnectLeg);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, DisconnectLegRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.establishTemporaryConnection);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EstablishTemporaryConnectionRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.eventReportBCSM);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EventReportBCSMRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.furnishChargingInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, FurnishChargingInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.initialDP);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InitialDPRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.initiateCallAttempt);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InitiateCallAttemptRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.moveLeg);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, MoveLegRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.playAnnouncement);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, PlayAnnouncementRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.promptAndCollectUserInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, PromptAndCollectUserInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.releaseCall);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReleaseCallRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.requestReportBCSMEvent);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestReportBCSMEventRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.resetTimer);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ResetTimerRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.sendChargingInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendChargingInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.specializedResourceReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SpecializedResourceReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.splitLeg);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SplitLegRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.activityTestGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ActivityTestGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.applyChargingGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ApplyChargingGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.applyChargingReportGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ApplyChargingReportGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.cancelGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CancelGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.connectGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ConnectGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.continueGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ContinueGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.entityReleasedGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EntityReleasedGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.eventReportGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EventReportGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.furnishChargingInformationGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, FurnishChargingInformationGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.initialDPGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InitialDpGprsRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.releaseGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReleaseGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.requestReportGPRSEvent);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestReportGPRSEventRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.resetTimerGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ResetTimerGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.sendChargingInformationGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendChargingInformationGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.connectSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ConnectSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.continueSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ContinueSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.eventReportSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EventReportSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.furnishChargingInformationSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, FurnishChargingInformationSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.initialDPSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InitialDPSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.releaseSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReleaseSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.requestReportSMSEvent);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestReportSMSEventRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.resetTimerSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ResetTimerSMSRequestImpl.class);
        	
        	//registering request options
        	tcapProvider.getParser().registerAlternativeClassMapping(ActivityTestRequestImpl.class, ActivityTestRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ApplyChargingReportRequestImpl.class, ApplyChargingReportRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ApplyChargingRequestImpl.class, ApplyChargingRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(AssistRequestInstructionsRequestImpl.class, AssistRequestInstructionsRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CallGapRequestImpl.class, CallGapRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CallInformationReportRequestImpl.class, CallInformationReportRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CallInformationRequestRequestImpl.class, CallInformationRequestRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CancelRequestImpl.class, CancelRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CollectInformationRequestImpl.class, CollectInformationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ConnectRequestImpl.class, ConnectRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ConnectToResourceRequestImpl.class, ConnectToResourceRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ContinueRequestImpl.class, ContinueRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ContinueWithArgumentRequestImpl.class, ContinueWithArgumentRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(DisconnectForwardConnectionRequestImpl.class, DisconnectForwardConnectionRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(DisconnectForwardConnectionWithArgumentRequestImpl.class, DisconnectForwardConnectionWithArgumentRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(DisconnectLegRequestImpl.class, DisconnectLegRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EstablishTemporaryConnectionRequestImpl.class, EstablishTemporaryConnectionRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EventReportBCSMRequestImpl.class, EventReportBCSMRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(FurnishChargingInformationRequestImpl.class, FurnishChargingInformationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InitialDPRequestImpl.class, InitialDPRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InitiateCallAttemptRequestImpl.class, InitiateCallAttemptRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MoveLegRequestImpl.class, MoveLegRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(PlayAnnouncementRequestImpl.class, PlayAnnouncementRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(PromptAndCollectUserInformationRequestImpl.class, PromptAndCollectUserInformationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ReleaseCallRequestImpl.class, ReleaseCallRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RequestReportBCSMEventRequestImpl.class, RequestReportBCSMEventRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ResetTimerRequestImpl.class, ResetTimerRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendChargingInformationRequestImpl.class, SendChargingInformationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SpecializedResourceReportRequestImpl.class, SpecializedResourceReportRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SplitLegRequestImpl.class, SplitLegRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ActivityTestGPRSRequestImpl.class, ActivityTestGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ApplyChargingGPRSRequestImpl.class, ApplyChargingGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ApplyChargingReportGPRSRequestImpl.class, ApplyChargingReportGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CancelGPRSRequestImpl.class, CancelGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ConnectGPRSRequestImpl.class, ConnectGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ContinueGPRSRequestImpl.class, ContinueGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EntityReleasedGPRSRequestImpl.class, EntityReleasedGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EventReportGPRSRequestImpl.class, EventReportGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(FurnishChargingInformationGPRSRequestImpl.class, FurnishChargingInformationGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InitialDpGprsRequestImpl.class, InitialDpGprsRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ReleaseGPRSRequestImpl.class, ReleaseGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RequestReportGPRSEventRequestImpl.class, RequestReportGPRSEventRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ResetTimerGPRSRequestImpl.class, ResetTimerGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendChargingInformationGPRSRequestImpl.class, SendChargingInformationGPRSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ConnectSMSRequestImpl.class, ConnectSMSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ContinueSMSRequestImpl.class, ContinueSMSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EventReportSMSRequestImpl.class, EventReportSMSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(FurnishChargingInformationSMSRequestImpl.class, FurnishChargingInformationSMSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InitialDPSMSRequestImpl.class, InitialDPSMSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ReleaseSMSRequestImpl.class, ReleaseSMSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RequestReportSMSEventRequestImpl.class, RequestReportSMSEventRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ResetTimerSMSRequestImpl.class, ResetTimerSMSRequestImpl.class);
        	
        	//register responses mappings
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.activityTest);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ActivityTestResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.disconnectLeg);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, DisconnectLegResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.initiateCallAttempt);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, InitiateCallAttemptResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.moveLeg);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, MoveLegResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.promptAndCollectUserInformation);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, PromptAndCollectUserInformationResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.splitLeg);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, SplitLegResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.activityTestGPRS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ActivityTestGPRSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.applyChargingReportGPRS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ApplyChargingReportGPRSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.entityReleasedGPRS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, EntityReleasedGPRSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)CAPOperationCode.eventReportGPRS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, EventReportGPRSResponseImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(ActivityTestResponseImpl.class, ActivityTestResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(DisconnectLegResponseImpl.class, DisconnectLegResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InitiateCallAttemptResponseImpl.class, InitiateCallAttemptResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MoveLegResponseImpl.class, MoveLegResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(PromptAndCollectUserInformationResponseImpl.class, PromptAndCollectUserInformationResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SplitLegResponseImpl.class, SplitLegResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ActivityTestGPRSResponseImpl.class, ActivityTestGPRSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ApplyChargingReportGPRSResponseImpl.class, ApplyChargingReportGPRSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EntityReleasedGPRSResponseImpl.class, EntityReleasedGPRSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EventReportGPRSResponseImpl.class, EventReportGPRSResponseImpl.class);*/
        } catch(Exception ex) {
        	//already registered11
        }        	
    }

    public TCAPProvider getTCAPProvider() {
        return this.tcapProvider;
    }

    @Override
    public INAPServiceCircuitSwitchedCall getINAPServiceCircuitSwitchedCall() {
        return this.inapServiceCircuitSwitchedCall;
    }

    @Override
    public void addINAPDialogListener(UUID key,INAPDialogListener INAPDialogListener) {
        this.dialogListeners.put(key,INAPDialogListener);
    }

    @Override
    public INAPParameterFactory getINAPParameterFactory() {
        return inapParameterFactory;
    }

    @Override
    public ISUPParameterFactory getISUPParameterFactory() {
        return isupParameterFactory;
    }

    @Override
    public INAPErrorMessageFactory getINAPErrorMessageFactory() {
        return this.inapErrorMessageFactory;
    }

    @Override
    public void removeINAPDialogListener(UUID key) {
        this.dialogListeners.remove(key);
    }

    @Override
    public INAPDialog getINAPDialog(Long dialogId) {
            return this.dialogs.get(dialogId);       
    }

    public void start() {
        this.tcapProvider.addTCListener(this);
    }

    public void stop() {
        this.tcapProvider.removeTCListener(this);

    }

    protected void addDialog(INAPDialogImpl dialog) {
            this.dialogs.put(dialog.getLocalDialogId(), dialog);        
    }

    protected INAPDialogImpl removeDialog(Long dialogId) {
            return this.dialogs.remove(dialogId);        
    }

    private void SendUnsupportedAcn(ApplicationContextName acn, Dialog dialog, String cs) {
        StringBuffer s = new StringBuffer();
        s.append(cs + " ApplicationContextName is received: ");
        for (long l : acn.getOid()) {
            s.append(l).append(", ");
        }
        loger.warn(s.toString());

        try {
            this.fireTCAbort(dialog, INAPGeneralAbortReason.ACNNotSupported, null, false);
        } catch (INAPException e1) {
            loger.error("Error while firing TC-U-ABORT. ", e1);
        }
    }

    public void onTCBegin(TCBeginIndication tcBeginIndication) {

        ApplicationContextName acn = tcBeginIndication.getApplicationContextName();
        List<BaseComponent> comps = tcBeginIndication.getComponents();

        // ACN must be present in CAMEL
        if (acn == null) {
            loger.warn("onTCBegin: Received TCBeginIndication without application context name");

            try {
                this.fireTCAbort(tcBeginIndication.getDialog(), INAPGeneralAbortReason.UserSpecific,
                        INAPUserAbortReason.abnormal_processing, false);
            } catch (INAPException e) {
                loger.error("Error while firing TC-U-ABORT. ", e);
            }
            return;
        }

        INAPApplicationContext inapAppCtx = INAPApplicationContext.getInstance(acn.getOid());
        // Check if ApplicationContext is recognizable for CAP
        // If no - TC-U-ABORT - ACN-Not-Supported
        if (inapAppCtx == null) {
            SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Unrecognizable");
            return;
        }

        // Selecting the INAP service that can perform the ApplicationContext
        INAPServiceBase perfSer = null;
        for (INAPServiceBase ser : this.inapServices) {

            ServingCheckData chkRes = ser.isServingService(inapAppCtx);
            switch (chkRes.getResult()) {
                case AC_Serving:
                    perfSer = ser;
                    break;
                case AC_VersionIncorrect:
                    SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Unsupported");
                    return;
				default:
					break;
            }

            if (perfSer != null)
                break;
        }

        // No INAPService can accept the received ApplicationContextName
        if (perfSer == null) {
            SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Unsupported");
            return;
        }

        // INAPService is not activated
        if (!perfSer.isActivated()) {
            SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Inactive INAPService");
            return;
        }

        INAPDialogImpl inapDialogImpl = ((INAPServiceBaseImpl) perfSer).createNewDialogIncoming(inapAppCtx,
                tcBeginIndication.getDialog());
        
        this.addDialog(inapDialogImpl);
        inapDialogImpl.tcapMessageType = MessageType.Begin;
        
        inapDialogImpl.setState(INAPDialogState.InitialReceived);

        inapDialogImpl.delayedAreaState = INAPDialogImpl.DelayedAreaState.No;

        this.deliverDialogRequest(inapDialogImpl);
        if (inapDialogImpl.getState() == INAPDialogState.Expunged) {
            // The Dialog was aborter or refused
            return;
        }

        // Now let us decode the Components
        if (comps != null) {
            processComponents(inapDialogImpl, comps);
        }

        this.deliverDialogDelimiter(inapDialogImpl);

        finishComponentProcessingState(inapDialogImpl);
    }

    private void finishComponentProcessingState(INAPDialogImpl inapDialogImpl) {

        if (inapDialogImpl.getState() == INAPDialogState.Expunged)
            return;

        try {
            switch (inapDialogImpl.delayedAreaState) {
                case Continue:
                    inapDialogImpl.send();
                    break;
                case End:
                    inapDialogImpl.close(false);
                    break;
                case PrearrangedEnd:
                    inapDialogImpl.close(true);
                    break;
				default:
					break;
            }
        } catch (INAPException e) {
            loger.error("Error while finishComponentProcessingState, delayedAreaState=" + inapDialogImpl.delayedAreaState, e);
        }

        inapDialogImpl.delayedAreaState = null;
    }

    public void onTCContinue(TCContinueIndication tcContinueIndication) {

        Dialog tcapDialog = tcContinueIndication.getDialog();

        INAPDialogImpl inapDialogImpl = (INAPDialogImpl) this.getINAPDialog(tcapDialog.getLocalDialogId());
        
        if (inapDialogImpl == null) {
            loger.warn("INAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            try {
                this.fireTCAbort(tcContinueIndication.getDialog(), INAPGeneralAbortReason.UserSpecific,
                        INAPUserAbortReason.abnormal_processing, false);
            } catch (INAPException e) {
                loger.error("Error while firing TC-U-ABORT. ", e);
            }
            return;
        }
        inapDialogImpl.tcapMessageType = MessageType.Continue;
        if (inapDialogImpl.getState() == INAPDialogState.InitialSent) {
            ApplicationContextName acn = tcContinueIndication.getApplicationContextName();

            if (acn == null) {
                loger.warn("INAP Dialog is in InitialSent state but no application context name is received");
                try {
                    this.fireTCAbort(tcContinueIndication.getDialog(), INAPGeneralAbortReason.UserSpecific,
                            INAPUserAbortReason.abnormal_processing, inapDialogImpl.getReturnMessageOnError());
                } catch (INAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }

                this.deliverDialogNotice(inapDialogImpl, INAPNoticeProblemDiagnostic.AbnormalDialogAction);
                inapDialogImpl.setState(INAPDialogState.Expunged);

                return;
            }

            INAPApplicationContext inapAcn = INAPApplicationContext.getInstance(acn.getOid());
            if (inapAcn == null || !inapAcn.equals(inapDialogImpl.getApplicationContext())) {
                loger.warn(String
                        .format("Received first TC-CONTINUE. But the received ACN is not the equal to the original ACN"));

                try {
                    this.fireTCAbort(tcContinueIndication.getDialog(), INAPGeneralAbortReason.UserSpecific,
                            INAPUserAbortReason.abnormal_processing, inapDialogImpl.getReturnMessageOnError());
                } catch (INAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }

                this.deliverDialogNotice(inapDialogImpl, INAPNoticeProblemDiagnostic.AbnormalDialogAction);
                inapDialogImpl.setState(INAPDialogState.Expunged);

                return;
            }

            inapDialogImpl.delayedAreaState = INAPDialogImpl.DelayedAreaState.No;

            inapDialogImpl.setState(INAPDialogState.Active);
            this.deliverDialogAccept(inapDialogImpl);

            if (inapDialogImpl.getState() == INAPDialogState.Expunged) {
                // The Dialog was aborter
                finishComponentProcessingState(inapDialogImpl);
                return;
            }
        } else {
            inapDialogImpl.delayedAreaState = INAPDialogImpl.DelayedAreaState.No;
        }

        // Now let us decode the Components
        if (inapDialogImpl.getState() == INAPDialogState.Active) {
            List<BaseComponent> comps = tcContinueIndication.getComponents();
            if (comps != null) {
                processComponents(inapDialogImpl, comps);
            }
        } else {
            // This should never happen
            loger.error(String.format("Received TC-CONTINUE. INAPDialog=%s. But state is not Active", inapDialogImpl));
        }

        this.deliverDialogDelimiter(inapDialogImpl);

        finishComponentProcessingState(inapDialogImpl);
    }

    public void onTCEnd(TCEndIndication tcEndIndication) {

        Dialog tcapDialog = tcEndIndication.getDialog();

        INAPDialogImpl inapDialogImpl = (INAPDialogImpl) this.getINAPDialog(tcapDialog.getLocalDialogId());
        
        if (inapDialogImpl == null) {
            loger.warn("IMAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }
        inapDialogImpl.tcapMessageType = MessageType.End;
        if (inapDialogImpl.getState() == INAPDialogState.InitialSent) {
            ApplicationContextName acn = tcEndIndication.getApplicationContextName();

            if (acn == null) {
                loger.warn("INAP Dialog is in InitialSent state but no application context name is received");

                this.deliverDialogNotice(inapDialogImpl, INAPNoticeProblemDiagnostic.AbnormalDialogAction);
                inapDialogImpl.setState(INAPDialogState.Expunged);

                return;
            }

            INAPApplicationContext inapAcn = INAPApplicationContext.getInstance(acn.getOid());

            if (inapAcn == null || !inapAcn.equals(inapDialogImpl.getApplicationContext())) {
                loger.error(String.format("Received first TC-END. INAPDialog=%s. But INAPApplicationContext=%s",
                        inapDialogImpl, inapAcn));

                // inapDialogImpl.setNormalDialogShutDown();

                this.deliverDialogNotice(inapDialogImpl, INAPNoticeProblemDiagnostic.AbnormalDialogAction);
                inapDialogImpl.setState(INAPDialogState.Expunged);

                return;
            }

            inapDialogImpl.setState(INAPDialogState.Active);

            this.deliverDialogAccept(inapDialogImpl);
            if (inapDialogImpl.getState() == INAPDialogState.Expunged) {
                // The Dialog was aborter
                return;
            }
        }

        // Now let us decode the Components
        List<BaseComponent> comps = tcEndIndication.getComponents();
        if (comps != null) {
            processComponents(inapDialogImpl, comps);
        }

        // inapDialogImpl.setNormalDialogShutDown();
        this.deliverDialogClose(inapDialogImpl);

        inapDialogImpl.setState(INAPDialogState.Expunged);
    }

    public void onTCUni(TCUniIndication arg0) {
    }

    @Override
    public void onInvokeTimeout(Invoke invoke) {

        INAPDialogImpl inapDialogImpl = (INAPDialogImpl) this.getINAPDialog(invoke.getDialog().getLocalDialogId());

        if (inapDialogImpl != null) {
        	if (inapDialogImpl.getState() != INAPDialogState.Expunged) {
                // if (inapDialogImpl.getState() != INAPDialogState.Expunged && !inapDialogImpl.getNormalDialogShutDown()) {

                // Getting the INAP Service that serves the CAP Dialog
                INAPServiceBaseImpl perfSer = (INAPServiceBaseImpl) inapDialogImpl.getService();

                // Check if the InvokeTimeout in this situation is normal (may be for a class 2,3,4 components)
                // TODO: ................................

                perfSer.deliverInvokeTimeout(inapDialogImpl, invoke);
            }
        }
    }

    @Override
    public void onDialogTimeout(Dialog tcapDialog) {

        INAPDialogImpl inapDialogImpl = (INAPDialogImpl) this.getINAPDialog(tcapDialog.getLocalDialogId());

        if (inapDialogImpl != null) {
        	if (inapDialogImpl.getState() != INAPDialogState.Expunged) {
                // if (inapDialogImpl.getState() != INAPDialogState.Expunged && !inapDialogImpl.getNormalDialogShutDown()) {

                this.deliverDialogTimeout(inapDialogImpl);
            }
        }
    }

    @Override
    public void onDialogReleased(Dialog tcapDialog) {

        INAPDialogImpl inapDialogImpl = (INAPDialogImpl) this.removeDialog(tcapDialog.getLocalDialogId());

        if (inapDialogImpl != null) {
        	this.deliverDialogRelease(inapDialogImpl);
        }
    }

    public void onTCPAbort(TCPAbortIndication tcPAbortIndication) {
        Dialog tcapDialog = tcPAbortIndication.getDialog();

        INAPDialogImpl inapDialogImpl = (INAPDialogImpl) this.getINAPDialog(tcapDialog.getLocalDialogId());
        
        if (inapDialogImpl == null) {
            loger.warn("INAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }
        inapDialogImpl.tcapMessageType = MessageType.Abort;

        PAbortCauseType pAbortCause = tcPAbortIndication.getPAbortCause();

        this.deliverDialogProviderAbort(inapDialogImpl, pAbortCause);

        inapDialogImpl.setState(INAPDialogState.Expunged);
    }

    public void onTCUserAbort(TCUserAbortIndication tcUserAbortIndication) {
        Dialog tcapDialog = tcUserAbortIndication.getDialog();

        INAPDialogImpl inapDialogImpl = (INAPDialogImpl) this.getINAPDialog(tcapDialog.getLocalDialogId());
        
        if (inapDialogImpl == null) {
            loger.error("INAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }
        inapDialogImpl.tcapMessageType = MessageType.Abort;

        INAPGeneralAbortReason generalReason = null;
        // INAPGeneralAbortReason generalReason = INAPGeneralAbortReason.BadReceivedData;
        INAPUserAbortReason userReason = null;

        if (tcUserAbortIndication.IsAareApdu()) {
            if (inapDialogImpl.getState() == INAPDialogState.InitialSent) {
                generalReason = INAPGeneralAbortReason.DialogRefused;
                ResultSourceDiagnostic resultSourceDiagnostic = tcUserAbortIndication.getResultSourceDiagnostic();
                if (resultSourceDiagnostic != null) {
                	try {
	                    if (resultSourceDiagnostic.getDialogServiceUserType() == DialogServiceUserType.AcnNotSupported) {
	                        generalReason = INAPGeneralAbortReason.ACNNotSupported;
	                    } else if (resultSourceDiagnostic.getDialogServiceProviderType() == DialogServiceProviderType.NoCommonDialogPortion) {
	                        generalReason = INAPGeneralAbortReason.NoCommonDialogPortionReceived;
	                    }
                	}
                	catch(ParseException ex) {
                		
                	}
                }
            }
        } else {
            UserInformation userInfo = tcUserAbortIndication.getUserInformation();

            if (userInfo != null) {
                // Checking userInfo.Oid==INAPUserAbortPrimitiveImpl.INAP_AbortReason_OId
                if (!userInfo.isIDObjectIdentifier()) {
                    loger.warn("When parsing TCUserAbortIndication indication: userInfo.isOid() is null");
                } else {
                    if (!userInfo.getObjectIdentifier().equals(INAPUserAbortPrimitiveImpl.INAP_AbortReason_OId)) {
                        loger.warn("When parsing TCUserAbortIndication indication: userInfo.getOidValue() must be INAPUserAbortPrimitiveImpl.INAP_AbortReason_OId");
                    } else if (!userInfo.isValueObject()) {
                        loger.warn("When parsing TCUserAbortIndication indication: userInfo.isAsn() check failed");
                    } else {
                    	Object userInfoObject=userInfo.getChild();
                    	if(!(userInfoObject instanceof INAPUserAbortPrimitiveImpl))
                    		loger.warn("When parsing TCUserAbortIndication indication: userInfo has bad tag or tagClass or is not primitive");
                    	else {
                    		INAPUserAbortPrimitiveImpl inapUserAbortPrimitive = (INAPUserAbortPrimitiveImpl)userInfoObject;
                            generalReason = INAPGeneralAbortReason.UserSpecific;
                            userReason = inapUserAbortPrimitive.getINAPUserAbortReason();
                        }
                    }
                }
            }
        }

        this.deliverDialogUserAbort(inapDialogImpl, generalReason, userReason);

        inapDialogImpl.setState(INAPDialogState.Expunged);
    }

    @Override
    public void onTCNotice(TCNoticeIndication ind) {
        Dialog tcapDialog = ind.getDialog();

        INAPDialogImpl inapDialogImpl = (INAPDialogImpl) this.getINAPDialog(tcapDialog.getLocalDialogId());

        if (inapDialogImpl == null) {
            loger.error("INAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }

        this.deliverDialogNotice(inapDialogImpl, INAPNoticeProblemDiagnostic.MessageCannotBeDeliveredToThePeer);

        if (inapDialogImpl.getState() == INAPDialogState.InitialSent) {
            // inapDialogImpl.setNormalDialogShutDown();
            inapDialogImpl.setState(INAPDialogState.Expunged);
        }
    }

    private void processComponents(INAPDialogImpl inapDialogImpl, List<BaseComponent> components) {

    	// Now let us decode the Components
        for (BaseComponent c : components) {

            doProcessComponent(inapDialogImpl, c);
        }
    }

    private void doProcessComponent(INAPDialogImpl inapDialogImpl, BaseComponent c) {

        // Getting the INAP Service that serves the INAP Dialog
        INAPServiceBaseImpl perfSer = (INAPServiceBaseImpl) inapDialogImpl.getService();

        try {
            ComponentType compType = ComponentType.Invoke;
            if(c instanceof Invoke)
            	compType=ComponentType.Invoke;
            else if(c instanceof Reject)
            	compType=ComponentType.Reject;
            else if(c instanceof ReturnError)
            	compType=ComponentType.ReturnError;
            else if(c instanceof ReturnResult)
            	compType=ComponentType.ReturnResult;
            else if(c instanceof ReturnResultLast)
            	compType=ComponentType.ReturnResultLast;
            
            Long invokeId = c.getInvokeId();

            Object parameter;
            OperationCode oc;
            Long linkedId = 0L;
            Invoke linkedInvoke = null;

            switch (compType) {
                case Invoke: {
                    Invoke comp = (Invoke)c;
                    oc = comp.getOperationCode();
                    parameter = comp.getParameter();
                    linkedId = comp.getLinkedId();

                    // Checking if the invokeId is not duplicated
                    if (linkedId != null) {
                        // linkedId exists Checking if the linkedId exists
                        linkedInvoke = comp.getLinkedInvoke();

                        long[] lstInv = perfSer.getLinkedOperationList(linkedInvoke.getOperationCode().getLocalOperationCode());
                        if (lstInv == null) {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setInvokeProblemType(InvokeProblemType.LinkedResponseUnexpected);
                            inapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(inapDialogImpl, invokeId, problem, true);

                            return;
                        }

                        boolean found = false;
                        if (lstInv != null) {
                            for (long l : lstInv) {
                                if (l == comp.getOperationCode().getLocalOperationCode()) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setInvokeProblemType(InvokeProblemType.UnexpectedLinkedOperation);
                            inapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(inapDialogImpl, invokeId, problem, true);

                            return;
                        }
                    }
                }
                    break;

                case ReturnResult: {
                    // ReturnResult is not supported by CAMEL
                	ProblemImpl problem = new ProblemImpl();
                    problem.setReturnResultProblemType(ReturnResultProblemType.ReturnResultUnexpected);
                    inapDialogImpl.sendRejectComponent(null, problem);
                    perfSer.deliverRejectComponent(inapDialogImpl, invokeId, problem, true);

                    return;
                }

                case ReturnResultLast: {
                    ReturnResultLast comp = (ReturnResultLast)c;
                    oc = comp.getOperationCode();
                    parameter = comp.getParameter();
                }
                    break;

                case ReturnError: {
                    ReturnError comp = (ReturnError)c;

                    long errorCode = 0;
                    if (comp.getErrorCode() != null && comp.getErrorCode().getErrorType() == ErrorCodeType.Local)
                        errorCode = comp.getErrorCode().getLocalErrorCode();
                    if (errorCode < INAPErrorCode.minimalCodeValue || errorCode > INAPErrorCode.maximumCodeValue) {
                        // Not Local error code and not INAP error code received
                    	ProblemImpl problem = new ProblemImpl();
                        problem.setReturnErrorProblemType(ReturnErrorProblemType.UnrecognizedError);
                        inapDialogImpl.sendRejectComponent(invokeId, problem);
                        perfSer.deliverRejectComponent(inapDialogImpl, invokeId, problem, true);

                        return;
                    }

                    INAPErrorMessage msgErr = new INAPErrorMessageParameterlessImpl();
                    Object p = comp.getParameter();
                    if (p != null && p instanceof INAPErrorMessage) {
                        msgErr=(INAPErrorMessage)p;
                    }
                    else if(p != null) {
                    	ProblemImpl problem = new ProblemImpl();
                         problem.setReturnErrorProblemType(ReturnErrorProblemType.MistypedParameter);
                         inapDialogImpl.sendRejectComponent(invokeId, problem);
                         perfSer.deliverRejectComponent(inapDialogImpl, invokeId, problem, true);
                         return;
                    }
                    
                    perfSer.deliverErrorComponent(inapDialogImpl, comp.getInvokeId(), msgErr);
                    return;
                }

                case Reject: {
                    Reject comp = (Reject)c;
                    perfSer.deliverRejectComponent(inapDialogImpl, comp.getInvokeId(), comp.getProblem(),
                            comp.isLocalOriginated());

                    return;
                }

                default:
                    return;
            }

            try {
            	if(parameter!=null && !(parameter instanceof INAPMessage)) {
            		throw new INAPParsingComponentException("MAPServiceHandling: unknown incoming operation code: " + oc.getLocalOperationCode(),
                            INAPParsingComponentExceptionReason.MistypedParameter);
            	}
            	
            	INAPMessage realMessage=(INAPMessage)parameter;
            	if(realMessage!=null) {
	            	realMessage.setInvokeId(invokeId);
	            	realMessage.setINAPDialog(inapDialogImpl);
            	}
            	
                perfSer.processComponent(compType, oc, realMessage, inapDialogImpl, invokeId, linkedId);

            } catch (INAPParsingComponentException e) {

                loger.error(
                        "INAPParsingComponentException when parsing components: " + e.getReason().toString() + " - "
                                + e.getMessage(), e);

                switch (e.getReason()) {
                    case UnrecognizedOperation:
                        // Component does not supported - send TC-U-REJECT
                        if (compType == ComponentType.Invoke) {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setInvokeProblemType(InvokeProblemType.UnrecognizedOperation);
                            inapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(inapDialogImpl, invokeId, problem, true);
                        } else {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setReturnResultProblemType(ReturnResultProblemType.MistypedParameter);
                            inapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(inapDialogImpl, invokeId, problem, true);
                        }
                        break;

                    case MistypedParameter:
                        // Failed when parsing the component - send TC-U-REJECT
                        if (compType == ComponentType.Invoke) {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setInvokeProblemType(InvokeProblemType.MistypedParameter);
                            inapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(inapDialogImpl, invokeId, problem, true);
                        } else {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setReturnResultProblemType(ReturnResultProblemType.MistypedParameter);
                            inapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(inapDialogImpl, invokeId, problem, true);
                        }
                        break;
                }

            }
        } catch (INAPException e) {
            loger.error("Error processing a Component: " + e.getMessage() + "\nComponent" + c, e);
        }
    }

    private void deliverDialogDelimiter(INAPDialog inapDialog) {
    	Iterator<INAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogDelimiter(inapDialog);
        }
    }

    private void deliverDialogRequest(INAPDialog inapDialog) {
    	Iterator<INAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogRequest(inapDialog);
        }
    }

    private void deliverDialogAccept(INAPDialog inapDialog) {
    	Iterator<INAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogAccept(inapDialog);
        }
    }

    private void deliverDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason, INAPUserAbortReason userReason) {
    	Iterator<INAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogUserAbort(inapDialog, generalReason, userReason);
        }
    }

    private void deliverDialogProviderAbort(INAPDialog inapDialog, PAbortCauseType abortCause) {
    	Iterator<INAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogProviderAbort(inapDialog, abortCause);
        }
    }

    private void deliverDialogClose(INAPDialog inapDialog) {
    	Iterator<INAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogClose(inapDialog);
        }
    }

    protected void deliverDialogRelease(INAPDialog inapDialog) {
    	Iterator<INAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogRelease(inapDialog);
        }
    }

    protected void deliverDialogTimeout(INAPDialog inapDialog) {
    	Iterator<INAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogTimeout(inapDialog);
        }
    }

    protected void deliverDialogNotice(INAPDialog inapDialog, INAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
    	Iterator<INAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogNotice(inapDialog, noticeProblemDiagnostic);
        }
    }

    protected void fireTCBegin(Dialog tcapDialog, ApplicationContextName acn,boolean returnMessageOnError) throws INAPException {

        TCBeginRequest tcBeginReq = encodeTCBegin(tcapDialog, acn);
        if (returnMessageOnError)
            tcBeginReq.setReturnMessageOnError(true);

        try {
            tcapDialog.send(tcBeginReq);
        } catch (TCAPSendException e) {
            throw new INAPException(e.getMessage(), e);
        }

    }

    protected TCBeginRequest encodeTCBegin(Dialog tcapDialog, ApplicationContextName acn) throws INAPException {
        TCBeginRequest tcBeginReq = this.getTCAPProvider().getDialogPrimitiveFactory().createBegin(tcapDialog);
        tcBeginReq.setApplicationContextName(acn);
        return tcBeginReq;
    }

    protected void fireTCContinue(Dialog tcapDialog, ApplicationContextName acn,boolean returnMessageOnError) throws INAPException {
        TCContinueRequest tcContinueReq = encodeTCContinue(tcapDialog, acn);
        if (returnMessageOnError)
            tcContinueReq.setReturnMessageOnError(true);

        try {
            tcapDialog.send(tcContinueReq);
        } catch (TCAPSendException e) {
            throw new INAPException(e.getMessage(), e);
        }
    }

    protected TCContinueRequest encodeTCContinue(Dialog tcapDialog, ApplicationContextName acn) throws INAPException {
        TCContinueRequest tcContinueReq = this.getTCAPProvider().getDialogPrimitiveFactory().createContinue(tcapDialog);

        if (acn != null)
            tcContinueReq.setApplicationContextName(acn);

        return tcContinueReq;
    }

    protected void fireTCEnd(Dialog tcapDialog, boolean prearrangedEnd, ApplicationContextName acn, boolean returnMessageOnError) throws INAPException {

    	TCEndRequest endRequest = encodeTCEnd(tcapDialog, prearrangedEnd, acn);
        if (returnMessageOnError)
            endRequest.setReturnMessageOnError(true);

        try {
            tcapDialog.send(endRequest);
        } catch (TCAPSendException e) {
            throw new INAPException(e.getMessage(), e);
        }
    }

    protected TCEndRequest encodeTCEnd(Dialog tcapDialog, boolean prearrangedEnd, ApplicationContextName acn) throws INAPException {
        TCEndRequest endRequest = this.getTCAPProvider().getDialogPrimitiveFactory().createEnd(tcapDialog);

        if (!prearrangedEnd) {
            endRequest.setTermination(TerminationType.Basic);
        } else {
            endRequest.setTermination(TerminationType.PreArranged);
        }

        if (acn != null)
            endRequest.setApplicationContextName(acn);

        return endRequest;
    }

    protected void fireTCAbort(Dialog tcapDialog, INAPGeneralAbortReason generalAbortReason, INAPUserAbortReason userAbortReason,
            boolean returnMessageOnError) throws INAPException {

        TCUserAbortRequest tcUserAbort = this.getTCAPProvider().getDialogPrimitiveFactory().createUAbort(tcapDialog);

        switch (generalAbortReason) {
            case ACNNotSupported:
                tcUserAbort.setDialogServiceUserType(DialogServiceUserType.AcnNotSupported);
                tcUserAbort.setApplicationContextName(tcapDialog.getApplicationContextName());
                break;

            case UserSpecific:
                if (userAbortReason == null)
                    userAbortReason = INAPUserAbortReason.no_reason_given;
                INAPUserAbortPrimitiveImpl abortReasonPrimitive = new INAPUserAbortPrimitiveImpl(userAbortReason);
                UserInformation userInformation = TcapFactory.createUserInformation();
                userInformation.setIdentifier(INAPUserAbortPrimitiveImpl.INAP_AbortReason_OId);
                userInformation.setChildAsObject(abortReasonPrimitive);
                tcUserAbort.setUserInformation(userInformation);
                break;

            // case DialogRefused:
            // if (tcapDialog.getApplicationContextName() != null) {
            // tcUserAbort.setDialogServiceUserType(DialogServiceUserType.NoReasonGive);
            // tcUserAbort.setApplicationContextName(tcapDialog.getApplicationContextName());
            // }
            // break;

            default:
                break;
        }
        if (returnMessageOnError)
            tcUserAbort.setReturnMessageOnError(true);

        try {
            tcapDialog.send(tcUserAbort);
        } catch (TCAPSendException e) {
            throw new INAPException(e.getMessage(), e);
        }
    }
    
    @Override
    public int getCurrentDialogsCount() {
        return this.tcapProvider.getCurrentDialogsCount();
    }

}
