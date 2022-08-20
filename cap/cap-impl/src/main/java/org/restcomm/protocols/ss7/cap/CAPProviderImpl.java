/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPDialogListener;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPMessage;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.CAPParameterFactory;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.cap.api.CAPProvider;
import org.restcomm.protocols.ss7.cap.api.CAPServiceBase;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPDialogState;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.restcomm.protocols.ss7.cap.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorCode;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCall;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CAPServiceGprs;
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPServiceSms;
import org.restcomm.protocols.ss7.cap.dialog.CAPGprsReferenceNumberImpl;
import org.restcomm.protocols.ss7.cap.dialog.CAPUserAbortPrimitiveImpl;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageCancelFailedImpl;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageFactoryImpl;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageParameterlessImpl;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageRequestedInfoErrorImpl;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageSystemFailureImpl;
import org.restcomm.protocols.ss7.cap.errors.CAPErrorMessageTaskRefusedImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ActivityTestRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ActivityTestResponseImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ApplyChargingReportRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ApplyChargingRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.AssistRequestInstructionsRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.CallGapRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.CallInformationReportRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.CallInformationRequestRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.CancelRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.CollectInformationRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ConnectRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ConnectToResourceRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ContinueRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ContinueWithArgumentRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.DisconnectForwardConnectionRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.DisconnectForwardConnectionWithArgumentRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.DisconnectLegRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.DisconnectLegResponseImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.EstablishTemporaryConnectionRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.EventReportBCSMRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.FurnishChargingInformationRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.InitialDPRequestV1Impl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.InitialDPRequestV3Impl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.InitiateCallAttemptRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.InitiateCallAttemptResponseImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.MoveLegRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.MoveLegResponseImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.PlayAnnouncementRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.PromptAndCollectUserInformationRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.PromptAndCollectUserInformationResponseImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ReleaseCallRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.RequestReportBCSMEventRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ResetTimerRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.SendChargingInformationRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.SpecializedResourceReportRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.SplitLegRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.SplitLegResponseImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.ActivityTestGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.ActivityTestGPRSResponseImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.ApplyChargingGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.ApplyChargingReportGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.ApplyChargingReportGPRSResponseImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.CAPServiceGprsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.CancelGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.ConnectGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.ContinueGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.EntityReleasedGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.EntityReleasedGPRSResponseImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.EventReportGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.EventReportGPRSResponseImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.FurnishChargingInformationGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.InitialDpGprsRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.ReleaseGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.RequestReportGPRSEventRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.ResetTimerGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.SendChargingInformationGPRSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.sms.CAPServiceSmsImpl;
import org.restcomm.protocols.ss7.cap.service.sms.ConnectSMSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.sms.ContinueSMSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.sms.EventReportSMSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.sms.FurnishChargingInformationSMSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.sms.InitialDPSMSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.sms.ReleaseSMSRequestImpl;
import org.restcomm.protocols.ss7.cap.service.sms.RequestReportSMSEventRequestImpl;
import org.restcomm.protocols.ss7.cap.service.sms.ResetTimerSMSRequestImpl;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.ISUPParameterFactoryImpl;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.api.OperationCodeWithACN;
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
import org.restcomm.protocols.ss7.tcap.asn.ASNUserInformationObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCodeType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCodeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultInnerImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPProviderImpl implements CAPProvider, TCListener {
	private static final long serialVersionUID = 1L;


	protected final transient Logger loger;


    private transient ConcurrentHashMap<UUID,CAPDialogListener> dialogListeners = new ConcurrentHashMap<UUID,CAPDialogListener>();

//    protected transient FastMap<Long, CAPDialogImpl> dialogs = new FastMap<Long, CAPDialogImpl>().shared();
    protected transient ConcurrentHashMap<Long, CAPDialogImpl> dialogs = new ConcurrentHashMap<Long, CAPDialogImpl>();

    private transient TCAPProvider tcapProvider = null;

    private final transient CAPParameterFactory capParameterFactory = new CAPParameterFactoryImpl();
    private final transient ISUPParameterFactory isupParameterFactory = new ISUPParameterFactoryImpl();
    private final transient CAPErrorMessageFactory capErrorMessageFactory = new CAPErrorMessageFactoryImpl();

    protected transient Set<CAPServiceBase> capServices = new HashSet<CAPServiceBase>();
    private final transient CAPServiceCircuitSwitchedCall capServiceCircuitSwitchedCall = new CAPServiceCircuitSwitchedCallImpl(
            this);
    private final transient CAPServiceGprs capServiceGprs = new CAPServiceGprsImpl(this);
    private final transient CAPServiceSms capServiceSms = new CAPServiceSmsImpl(this);

    private CAPStackImpl stack;
    
    public CAPProviderImpl(String name, CAPStackImpl stack, TCAPProvider tcapProvider) {
        this.tcapProvider = tcapProvider;

        this.loger = LogManager.getLogger(CAPStackImpl.class.getCanonicalName() + "-" + name);
        this.stack = stack;
        this.capServices.add(this.capServiceCircuitSwitchedCall);
        this.capServices.add(this.capServiceGprs);
        this.capServices.add(this.capServiceSms);
        
        try {
        	//registering user information options
        	tcapProvider.getParser().registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, CAPGprsReferenceNumberImpl.class);
        	
        	ErrorCodeImpl errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(CAPErrorCode.cancelFailed);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, CAPErrorMessageCancelFailedImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(CAPErrorCode.requestedInfoError);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, CAPErrorMessageRequestedInfoErrorImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(CAPErrorCode.systemFailure);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, CAPErrorMessageSystemFailureImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(CAPErrorCode.taskRefused);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, CAPErrorMessageTaskRefusedImpl.class);
        	
        	//registering error options
        	tcapProvider.getParser().registerAlternativeClassMapping(CAPErrorMessageCancelFailedImpl.class, CAPErrorMessageCancelFailedImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CAPErrorMessageRequestedInfoErrorImpl.class, CAPErrorMessageRequestedInfoErrorImpl.class);        	
        	tcapProvider.getParser().registerAlternativeClassMapping(CAPErrorMessageSystemFailureImpl.class, CAPErrorMessageSystemFailureImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CAPErrorMessageTaskRefusedImpl.class, CAPErrorMessageTaskRefusedImpl.class);
        	
        	//register requests mappings
        	OperationCodeImpl opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.activityTest);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ActivityTestRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.applyChargingReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ApplyChargingReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.applyCharging);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ApplyChargingRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.assistRequestInstructions);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, AssistRequestInstructionsRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.callGap);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CallGapRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.callInformationReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CallInformationReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.callInformationRequest);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CallInformationRequestRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.cancelCode);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CancelRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.collectInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CollectInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.connect);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ConnectRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.connectToResource);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ConnectToResourceRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.continueCode);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ContinueRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.continueWithArgument);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ContinueWithArgumentRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.disconnectForwardConnection);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, DisconnectForwardConnectionRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.dFCWithArgument);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, DisconnectForwardConnectionWithArgumentRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.disconnectLeg);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, DisconnectLegRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.establishTemporaryConnection);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EstablishTemporaryConnectionRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.eventReportBCSM);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EventReportBCSMRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.furnishChargingInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, FurnishChargingInformationRequestImpl.class);
        	
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.initialDP);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InitialDPRequestV1Impl.class);
        	
        	OperationCodeWithACN operationWithACN=new OperationCodeWithACN(opCode, CAPApplicationContext.CapV3_gsmSSF_scfGeneric.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, InitialDPRequestV3Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, CAPApplicationContext.CapV4_gsmSSF_scfGeneric.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, InitialDPRequestV3Impl.class);
        	
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.initiateCallAttempt);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InitiateCallAttemptRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.moveLeg);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, MoveLegRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.playAnnouncement);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, PlayAnnouncementRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.promptAndCollectUserInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, PromptAndCollectUserInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.releaseCall);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReleaseCallRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.requestReportBCSMEvent);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestReportBCSMEventRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.resetTimer);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ResetTimerRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.sendChargingInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendChargingInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.specializedResourceReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SpecializedResourceReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.splitLeg);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SplitLegRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.activityTestGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ActivityTestGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.applyChargingGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ApplyChargingGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.applyChargingReportGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ApplyChargingReportGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.cancelGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CancelGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.connectGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ConnectGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.continueGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ContinueGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.entityReleasedGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EntityReleasedGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.eventReportGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EventReportGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.furnishChargingInformationGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, FurnishChargingInformationGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.initialDPGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InitialDpGprsRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.releaseGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReleaseGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.requestReportGPRSEvent);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestReportGPRSEventRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.resetTimerGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ResetTimerGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.sendChargingInformationGPRS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendChargingInformationGPRSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.connectSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ConnectSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.continueSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ContinueSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.eventReportSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EventReportSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.furnishChargingInformationSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, FurnishChargingInformationSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.initialDPSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InitialDPSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.releaseSMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReleaseSMSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.requestReportSMSEvent);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestReportSMSEventRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.resetTimerSMS);
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
        	tcapProvider.getParser().registerAlternativeClassMapping(InitialDPRequestV1Impl.class, InitialDPRequestV1Impl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InitialDPRequestV3Impl.class, InitialDPRequestV3Impl.class);
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
        	opCode.setLocalOperationCode(CAPOperationCode.activityTest);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ActivityTestResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.disconnectLeg);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, DisconnectLegResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.initiateCallAttempt);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, InitiateCallAttemptResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.moveLeg);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, MoveLegResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.promptAndCollectUserInformation);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, PromptAndCollectUserInformationResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.splitLeg);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, SplitLegResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.activityTestGPRS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ActivityTestGPRSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.applyChargingReportGPRS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ApplyChargingReportGPRSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.entityReleasedGPRS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, EntityReleasedGPRSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(CAPOperationCode.eventReportGPRS);
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
        	tcapProvider.getParser().registerAlternativeClassMapping(EventReportGPRSResponseImpl.class, EventReportGPRSResponseImpl.class);

        } catch(Exception ex) {
        	//already registered11
        }        	
    }

    public CAPStackImpl getCAPStack() {
    	return stack;
    }
    
    public TCAPProvider getTCAPProvider() {
        return this.tcapProvider;
    }

    @Override
    public CAPServiceCircuitSwitchedCall getCAPServiceCircuitSwitchedCall() {
        return this.capServiceCircuitSwitchedCall;
    }

    @Override
    public CAPServiceGprs getCAPServiceGprs() {
        return this.capServiceGprs;
    }

    @Override
    public CAPServiceSms getCAPServiceSms() {
        return this.capServiceSms;
    }

    @Override
    public void addCAPDialogListener(UUID key,CAPDialogListener capDialogListener) {
        this.dialogListeners.put(key,capDialogListener);
    }

    @Override
    public CAPParameterFactory getCAPParameterFactory() {
        return capParameterFactory;
    }

    @Override
    public ISUPParameterFactory getISUPParameterFactory() {
        return isupParameterFactory;
    }

    @Override
    public CAPErrorMessageFactory getCAPErrorMessageFactory() {
        return this.capErrorMessageFactory;
    }

    @Override
    public void removeCAPDialogListener(UUID key) {
        this.dialogListeners.remove(key);
    }

    @Override
    public CAPDialog getCAPDialog(Long dialogId) {
            return this.dialogs.get(dialogId);       
    }

    public void start() {
        this.tcapProvider.addTCListener(this);
    }

    public void stop() {
        this.tcapProvider.removeTCListener(this);

    }

    protected void addDialog(CAPDialogImpl dialog) {
            this.dialogs.put(dialog.getLocalDialogId(), dialog);        
    }

    protected CAPDialogImpl removeDialog(Long dialogId) {
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
            this.fireTCAbort(dialog, CAPGeneralAbortReason.ACNNotSupported, null, false);
        } catch (CAPException e1) {
            loger.error("Error while firing TC-U-ABORT. ", e1);
        }
    }

    private CAPGprsReferenceNumberImpl ParseUserInfo(UserInformation userInfo, Dialog dialog) {

        // Parsing userInfo
        
        // Checking UserData ObjectIdentifier
        if (!userInfo.isIDObjectIdentifier()) {
            loger.warn("onTCBegin: userInfo.isOid() is null");
            return null;
        }

        List<Long> oid = userInfo.getObjectIdentifier();

        if (!CAPGprsReferenceNumberImpl.CAP_Dialogue_OId.equals(oid)) {
            loger.warn("onTCBegin: userInfo.isOid() has bad value");
            return null;
        }

        if (!userInfo.isValueObject()) {
            loger.warn("onTCBegin: userInfo.isAsn() is null");
            return null;
        }

        Object userInfoObject=userInfo.getChild();
        if(!(userInfoObject instanceof CAPGprsReferenceNumberImpl)) {
        	loger.warn("onTCBegin: Error parsing CAPGprsReferenceNumber: bad tag or tag class or is primitive");
            return null;
        }

        return (CAPGprsReferenceNumberImpl)userInfoObject;
    }

    public void onTCBegin(TCBeginIndication tcBeginIndication) {

        ApplicationContextName acn = tcBeginIndication.getApplicationContextName();
        List<BaseComponent> comps = tcBeginIndication.getComponents();

        // ACN must be present in CAMEL
        if (acn == null) {
            loger.warn("onTCBegin: Received TCBeginIndication without application context name");

            try {
                this.fireTCAbort(tcBeginIndication.getDialog(), CAPGeneralAbortReason.UserSpecific,
                        CAPUserAbortReason.abnormal_processing, false);
            } catch (CAPException e) {
                loger.error("Error while firing TC-U-ABORT. ", e);
            }
            return;
        }

        CAPApplicationContext capAppCtx = CAPApplicationContext.getInstance(acn.getOid());
        // Check if ApplicationContext is recognizable for CAP
        // If no - TC-U-ABORT - ACN-Not-Supported
        if (capAppCtx == null) {
            SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Unrecognizable");
            return;
        }

        // Parsing CAPGprsReferenceNumber if exists
        CAPGprsReferenceNumberImpl referenceNumber = null;
        UserInformation userInfo = tcBeginIndication.getUserInformation();
        if (userInfo != null) {
            referenceNumber = ParseUserInfo(userInfo, tcBeginIndication.getDialog());
            if (referenceNumber == null) {
                try {
                    this.fireTCAbort(tcBeginIndication.getDialog(), CAPGeneralAbortReason.UserSpecific,
                            CAPUserAbortReason.abnormal_processing, false);
                } catch (CAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }

                return;
            }
        }

        // Selecting the CAP service that can perform the ApplicationContext
        CAPServiceBase perfSer = null;
        for (CAPServiceBase ser : this.capServices) {

            ServingCheckData chkRes = ser.isServingService(capAppCtx);
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

        // No CAPService can accept the received ApplicationContextName
        if (perfSer == null) {
            SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Unsupported");
            return;
        }

        // CAPService is not activated
        if (!perfSer.isActivated()) {
            SendUnsupportedAcn(acn, tcBeginIndication.getDialog(), "onTCBegin: Inactive CAPService");
            return;
        }

        CAPDialogImpl capDialogImpl = ((CAPServiceBaseImpl) perfSer).createNewDialogIncoming(capAppCtx,
                tcBeginIndication.getDialog());
        
        this.addDialog(capDialogImpl);
        capDialogImpl.tcapMessageType = MessageType.Begin;
        capDialogImpl.receivedGprsReferenceNumber = referenceNumber;

        capDialogImpl.setState(CAPDialogState.InitialReceived);

        capDialogImpl.delayedAreaState = CAPDialogImpl.DelayedAreaState.No;

        this.deliverDialogRequest(capDialogImpl, referenceNumber);
        if (capDialogImpl.getState() == CAPDialogState.Expunged) {
            // The Dialog was aborter or refused
            return;
        }

        // Now let us decode the Components
        if (comps != null) {
            processComponents(capDialogImpl, comps, tcBeginIndication.getOriginalBuffer());
        }

        this.deliverDialogDelimiter(capDialogImpl);

        finishComponentProcessingState(capDialogImpl);
    }

    private void finishComponentProcessingState(CAPDialogImpl capDialogImpl) {

        if (capDialogImpl.getState() == CAPDialogState.Expunged)
            return;

        try {
            switch (capDialogImpl.delayedAreaState) {
                case Continue:
                    capDialogImpl.send();
                    break;
                case End:
                    capDialogImpl.close(false);
                    break;
                case PrearrangedEnd:
                    capDialogImpl.close(true);
                    break;
				default:
					break;
            }
        } catch (CAPException e) {
            loger.error("Error while finishComponentProcessingState, delayedAreaState=" + capDialogImpl.delayedAreaState, e);
        }

        capDialogImpl.delayedAreaState = null;
    }

    public void onTCContinue(TCContinueIndication tcContinueIndication) {

        Dialog tcapDialog = tcContinueIndication.getDialog();

        CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getLocalDialogId());
        
        if (capDialogImpl == null) {
            loger.warn("CAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            try {
                this.fireTCAbort(tcContinueIndication.getDialog(), CAPGeneralAbortReason.UserSpecific,
                        CAPUserAbortReason.abnormal_processing, false);
            } catch (CAPException e) {
                loger.error("Error while firing TC-U-ABORT. ", e);
            }
            return;
        }
        capDialogImpl.tcapMessageType = MessageType.Continue;
        if (capDialogImpl.getState() == CAPDialogState.InitialSent) {
            ApplicationContextName acn = tcContinueIndication.getApplicationContextName();

            if (acn == null) {
                loger.warn("CAP Dialog is in InitialSent state but no application context name is received");
                try {
                    this.fireTCAbort(tcContinueIndication.getDialog(), CAPGeneralAbortReason.UserSpecific,
                            CAPUserAbortReason.abnormal_processing, capDialogImpl.getReturnMessageOnError());
                } catch (CAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }

                this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.AbnormalDialogAction);
                capDialogImpl.setState(CAPDialogState.Expunged);

                return;
            }

            CAPApplicationContext capAcn = CAPApplicationContext.getInstance(acn.getOid());
            if (capAcn == null || !capAcn.equals(capDialogImpl.getApplicationContext())) {
                loger.warn(String
                        .format("Received first TC-CONTINUE. But the received ACN is not the equal to the original ACN"));

                try {
                    this.fireTCAbort(tcContinueIndication.getDialog(), CAPGeneralAbortReason.UserSpecific,
                            CAPUserAbortReason.abnormal_processing, capDialogImpl.getReturnMessageOnError());
                } catch (CAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }

                this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.AbnormalDialogAction);
                capDialogImpl.setState(CAPDialogState.Expunged);

                return;
            }

            // Parsing CAPGprsReferenceNumber if exists
            // we ignore all errors this
            CAPGprsReferenceNumberImpl referenceNumber = null;
            UserInformation userInfo = tcContinueIndication.getUserInformation();
            if (userInfo != null) {
                referenceNumber = ParseUserInfo(userInfo, tcContinueIndication.getDialog());
            }
            capDialogImpl.receivedGprsReferenceNumber = referenceNumber;

            capDialogImpl.delayedAreaState = CAPDialogImpl.DelayedAreaState.No;

            capDialogImpl.setState(CAPDialogState.Active);
            this.deliverDialogAccept(capDialogImpl, referenceNumber);

            if (capDialogImpl.getState() == CAPDialogState.Expunged) {
                // The Dialog was aborter
                finishComponentProcessingState(capDialogImpl);
                return;
            }
        } else {
            capDialogImpl.delayedAreaState = CAPDialogImpl.DelayedAreaState.No;
        }

        // Now let us decode the Components
        if (capDialogImpl.getState() == CAPDialogState.Active) {
            List<BaseComponent> comps = tcContinueIndication.getComponents();
            if (comps != null) {
                processComponents(capDialogImpl, comps, tcContinueIndication.getOriginalBuffer());
            }
        } else {
            // This should never happen
            loger.error(String.format("Received TC-CONTINUE. CAPDialog=%s. But state is not Active", capDialogImpl));
        }

        this.deliverDialogDelimiter(capDialogImpl);

        finishComponentProcessingState(capDialogImpl);
    }

    public void onTCEnd(TCEndIndication tcEndIndication) {

        Dialog tcapDialog = tcEndIndication.getDialog();

        CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getLocalDialogId());
        
        if (capDialogImpl == null) {
            loger.warn("CAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }
        capDialogImpl.tcapMessageType = MessageType.End;
        if (capDialogImpl.getState() == CAPDialogState.InitialSent) {
            ApplicationContextName acn = tcEndIndication.getApplicationContextName();

            if (acn == null) {
                loger.warn("CAP Dialog is in InitialSent state but no application context name is received");

                this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.AbnormalDialogAction);
                capDialogImpl.setState(CAPDialogState.Expunged);

                return;
            }

            CAPApplicationContext capAcn = CAPApplicationContext.getInstance(acn.getOid());

            if (capAcn == null || !capAcn.equals(capDialogImpl.getApplicationContext())) {
                loger.error(String.format("Received first TC-END. CAPDialog=%s. But CAPApplicationContext=%s",
                        capDialogImpl, capAcn));

                // capDialogImpl.setNormalDialogShutDown();

                this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.AbnormalDialogAction);
                capDialogImpl.setState(CAPDialogState.Expunged);

                return;
            }

            capDialogImpl.setState(CAPDialogState.Active);

            // Parsing CAPGprsReferenceNumber if exists
            // we ignore all errors this
            CAPGprsReferenceNumberImpl referenceNumber = null;
            UserInformation userInfo = tcEndIndication.getUserInformation();
            if (userInfo != null) {
                referenceNumber = ParseUserInfo(userInfo, tcEndIndication.getDialog());
            }
            capDialogImpl.receivedGprsReferenceNumber = referenceNumber;

            this.deliverDialogAccept(capDialogImpl, referenceNumber);
            if (capDialogImpl.getState() == CAPDialogState.Expunged) {
                // The Dialog was aborter
                return;
            }
        }

        // Now let us decode the Components
        List<BaseComponent> comps = tcEndIndication.getComponents();
        if (comps != null) {
            processComponents(capDialogImpl, comps, tcEndIndication.getOriginalBuffer());
        }

        // capDialogImpl.setNormalDialogShutDown();
        this.deliverDialogClose(capDialogImpl);

        capDialogImpl.setState(CAPDialogState.Expunged);
    }

    public void onTCUni(TCUniIndication arg0) {
    }

    @Override
    public void onInvokeTimeout(Invoke invoke) {

        CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(invoke.getDialog().getLocalDialogId());

        if (capDialogImpl != null) {
        	if (capDialogImpl.getState() != CAPDialogState.Expunged) {
                // if (capDialogImpl.getState() != CAPDialogState.Expunged && !capDialogImpl.getNormalDialogShutDown()) {

                // Getting the CAP Service that serves the CAP Dialog
                CAPServiceBaseImpl perfSer = (CAPServiceBaseImpl) capDialogImpl.getService();

                // Check if the InvokeTimeout in this situation is normal (may be for a class 2,3,4 components)
                // TODO: ................................

                perfSer.deliverInvokeTimeout(capDialogImpl, invoke);
            }
        }
    }

    @Override
    public void onDialogTimeout(Dialog tcapDialog) {

        CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getLocalDialogId());

        if (capDialogImpl != null) {
        	if (capDialogImpl.getState() != CAPDialogState.Expunged) {
                // if (capDialogImpl.getState() != CAPDialogState.Expunged && !capDialogImpl.getNormalDialogShutDown()) {

                this.deliverDialogTimeout(capDialogImpl);
            }
        }
    }

    @Override
    public void onDialogReleased(Dialog tcapDialog) {

        CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.removeDialog(tcapDialog.getLocalDialogId());

        if (capDialogImpl != null) {
        	this.deliverDialogRelease(capDialogImpl);
        }
    }

    public void onTCPAbort(TCPAbortIndication tcPAbortIndication) {
        Dialog tcapDialog = tcPAbortIndication.getDialog();

        CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getLocalDialogId());
        
        if (capDialogImpl == null) {
            loger.warn("CAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }
        capDialogImpl.tcapMessageType = MessageType.Abort;

        PAbortCauseType pAbortCause = tcPAbortIndication.getPAbortCause();

        this.deliverDialogProviderAbort(capDialogImpl, pAbortCause);

        capDialogImpl.setState(CAPDialogState.Expunged);
    }

    public void onTCUserAbort(TCUserAbortIndication tcUserAbortIndication) {
        Dialog tcapDialog = tcUserAbortIndication.getDialog();

        CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getLocalDialogId());
        
        if (capDialogImpl == null) {
            loger.error("CAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }
        capDialogImpl.tcapMessageType = MessageType.Abort;

        CAPGeneralAbortReason generalReason = null;
        // CAPGeneralAbortReason generalReason = CAPGeneralAbortReason.BadReceivedData;
        CAPUserAbortReason userReason = null;

        if (tcUserAbortIndication.IsAareApdu()) {
            if (capDialogImpl.getState() == CAPDialogState.InitialSent) {
                generalReason = CAPGeneralAbortReason.DialogRefused;
                ResultSourceDiagnostic resultSourceDiagnostic = tcUserAbortIndication.getResultSourceDiagnostic();
                if (resultSourceDiagnostic != null) {
                	try {
	                    if (resultSourceDiagnostic.getDialogServiceUserType() == DialogServiceUserType.AcnNotSupported) {
	                        generalReason = CAPGeneralAbortReason.ACNNotSupported;
	                    } else if (resultSourceDiagnostic.getDialogServiceProviderType() == DialogServiceProviderType.NoCommonDialogPortion) {
	                        generalReason = CAPGeneralAbortReason.NoCommonDialogPortionReceived;
	                    }
                	}
                	catch(ParseException ex) {
                		
                	}
                }
            }
        } else {
            UserInformation userInfo = tcUserAbortIndication.getUserInformation();

            if (userInfo != null) {
                // Checking userInfo.Oid==CAPUserAbortPrimitiveImpl.CAP_AbortReason_OId
                if (!userInfo.isIDObjectIdentifier()) {
                    loger.warn("When parsing TCUserAbortIndication indication: userInfo.isOid() is null");
                } else {
                    if (!userInfo.getObjectIdentifier().equals(CAPUserAbortPrimitiveImpl.CAP_AbortReason_OId)) {
                        loger.warn("When parsing TCUserAbortIndication indication: userInfo.getOidValue() must be CAPUserAbortPrimitiveImpl.CAP_AbortReason_OId");
                    } else if (!userInfo.isValueObject()) {
                        loger.warn("When parsing TCUserAbortIndication indication: userInfo.isAsn() check failed");
                    } else {
                    	Object userInfoObject=userInfo.getChild();
                    	if(!(userInfoObject instanceof CAPUserAbortPrimitiveImpl))
                    		loger.warn("When parsing TCUserAbortIndication indication: userInfo has bad tag or tagClass or is not primitive");
                    	else {
                    		CAPUserAbortPrimitiveImpl capUserAbortPrimitive = (CAPUserAbortPrimitiveImpl)userInfoObject;
                            generalReason = CAPGeneralAbortReason.UserSpecific;
                            userReason = capUserAbortPrimitive.getCAPUserAbortReason();
                        }
                    }
                }
            }
        }

        this.deliverDialogUserAbort(capDialogImpl, generalReason, userReason);

        capDialogImpl.setState(CAPDialogState.Expunged);
    }

    @Override
    public void onTCNotice(TCNoticeIndication ind) {
        Dialog tcapDialog = ind.getDialog();

        CAPDialogImpl capDialogImpl = (CAPDialogImpl) this.getCAPDialog(tcapDialog.getLocalDialogId());

        if (capDialogImpl == null) {
            loger.error("CAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }

        this.deliverDialogNotice(capDialogImpl, CAPNoticeProblemDiagnostic.MessageCannotBeDeliveredToThePeer);

        if (capDialogImpl.getState() == CAPDialogState.InitialSent) {
            // capDialogImpl.setNormalDialogShutDown();
            capDialogImpl.setState(CAPDialogState.Expunged);
        }
    }

    private void processComponents(CAPDialogImpl capDialogImpl, List<BaseComponent> components, ByteBuf buffer) {

    	// Now let us decode the Components
        for (BaseComponent c : components) {

            doProcessComponent(capDialogImpl, c, buffer);
        }
    }

    private void doProcessComponent(CAPDialogImpl capDialogImpl, BaseComponent c, ByteBuf buffer) {

        // Getting the CAP Service that serves the CAP Dialog
        CAPServiceBaseImpl perfSer = (CAPServiceBaseImpl) capDialogImpl.getService();

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
            
            Integer invokeId = c.getInvokeId();

            Object parameter;
            OperationCode oc;
            Integer linkedId = 0;
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
                            capDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(capDialogImpl, invokeId, problem, true);

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
                            capDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(capDialogImpl, invokeId, problem, true);

                            return;
                        }
                    }
                }
                    break;

                case ReturnResult: {
                    // ReturnResult is not supported by CAMEL
                	ProblemImpl problem = new ProblemImpl();
                    problem.setReturnResultProblemType(ReturnResultProblemType.ReturnResultUnexpected);
                    capDialogImpl.sendRejectComponent(null, problem);
                    perfSer.deliverRejectComponent(capDialogImpl, invokeId, problem, true);

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

                    int errorCode = 0;
                    if (comp.getErrorCode() != null && comp.getErrorCode().getErrorType() == ErrorCodeType.Local)
                        errorCode = comp.getErrorCode().getLocalErrorCode();
                    if (errorCode < CAPErrorCode.minimalCodeValue || errorCode > CAPErrorCode.maximumCodeValue) {
                        // Not Local error code and not CAP error code received
                    	ProblemImpl problem = new ProblemImpl();
                        problem.setReturnErrorProblemType(ReturnErrorProblemType.UnrecognizedError);
                        capDialogImpl.sendRejectComponent(invokeId, problem);
                        perfSer.deliverRejectComponent(capDialogImpl, invokeId, problem, true);

                        return;
                    }

                    CAPErrorMessage msgErr = new CAPErrorMessageParameterlessImpl();
                    Object p = comp.getParameter();
                    if (p != null && p instanceof CAPErrorMessage) {
                        msgErr=(CAPErrorMessage)p;
                    }
                    else if(p != null) {
                    	ProblemImpl problem = new ProblemImpl();
                         problem.setReturnErrorProblemType(ReturnErrorProblemType.MistypedParameter);
                         capDialogImpl.sendRejectComponent(invokeId, problem);
                         perfSer.deliverRejectComponent(capDialogImpl, invokeId, problem, true);
                         return;
                    }
                    
                    stack.newErrorReceived(CAPErrorCode.translate(errorCode),capDialogImpl.getNetworkId());               
                    perfSer.deliverErrorComponent(capDialogImpl, comp.getInvokeId(), msgErr);
                    return;
                }

                case Reject: {
                    Reject comp = (Reject)c;
                    perfSer.deliverRejectComponent(capDialogImpl, comp.getInvokeId(), comp.getProblem(),
                            comp.isLocalOriginated());

                    return;
                }

                default:
                    return;
            }

            try {
            	if(parameter!=null && !(parameter instanceof CAPMessage)) {
            		throw new CAPParsingComponentException("MAPServiceHandling: unknown incoming operation code: " + oc.getLocalOperationCode(),
                            CAPParsingComponentExceptionReason.MistypedParameter);
            	}
            	
            	CAPMessage realMessage=(CAPMessage)parameter;
            	if(realMessage!=null) {
            		stack.newMessageReceived(realMessage.getMessageType().name(),capDialogImpl.getNetworkId());               
                    realMessage.setOriginalBuffer(buffer);
	            	realMessage.setInvokeId(invokeId);
	            	realMessage.setCAPDialog(capDialogImpl);
            	}
            	
                perfSer.processComponent(compType, oc, realMessage, capDialogImpl, invokeId, linkedId);

            } catch (CAPParsingComponentException e) {

                loger.error(
                        "CAPParsingComponentException when parsing components: " + e.getReason().toString() + " - "
                                + e.getMessage(), e);

                switch (e.getReason()) {
                    case UnrecognizedOperation:
                        // Component does not supported - send TC-U-REJECT
                        if (compType == ComponentType.Invoke) {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setInvokeProblemType(InvokeProblemType.UnrecognizedOperation);
                            capDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(capDialogImpl, invokeId, problem, true);
                        } else {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setReturnResultProblemType(ReturnResultProblemType.MistypedParameter);
                            capDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(capDialogImpl, invokeId, problem, true);
                        }
                        break;

                    case MistypedParameter:
                        // Failed when parsing the component - send TC-U-REJECT
                        if (compType == ComponentType.Invoke) {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setInvokeProblemType(InvokeProblemType.MistypedParameter);
                            capDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(capDialogImpl, invokeId, problem, true);
                        } else {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setReturnResultProblemType(ReturnResultProblemType.MistypedParameter);
                            capDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(capDialogImpl, invokeId, problem, true);
                        }
                        break;
                }

            }
        } catch (CAPException e) {
            loger.error("Error processing a Component: " + e.getMessage() + "\nComponent" + c, e);
        }
    }

    private void deliverDialogDelimiter(CAPDialog capDialog) {
    	Iterator<CAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogDelimiter(capDialog);
        }
    }

    private void deliverDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
    	Iterator<CAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogRequest(capDialog, capGprsReferenceNumber);
        }
    }

    private void deliverDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
    	Iterator<CAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogAccept(capDialog, capGprsReferenceNumber);
        }
    }

    private void deliverDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason, CAPUserAbortReason userReason) {
    	Iterator<CAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogUserAbort(capDialog, generalReason, userReason);
        }
    }

    private void deliverDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
    	Iterator<CAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogProviderAbort(capDialog, abortCause);
        }
    }

    private void deliverDialogClose(CAPDialog capDialog) {
    	Iterator<CAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogClose(capDialog);
        }
    }

    protected void deliverDialogRelease(CAPDialog capDialog) {
    	Iterator<CAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogRelease(capDialog);
        }
    }

    protected void deliverDialogTimeout(CAPDialog capDialog) {
    	Iterator<CAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogTimeout(capDialog);
        }
    }

    protected void deliverDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
    	Iterator<CAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogNotice(capDialog, noticeProblemDiagnostic);
        }
    }

    protected void fireTCBegin(Dialog tcapDialog, ApplicationContextName acn, CAPGprsReferenceNumber gprsReferenceNumber,
            boolean returnMessageOnError) throws CAPException {

        TCBeginRequest tcBeginReq = encodeTCBegin(tcapDialog, acn, gprsReferenceNumber);
        if (returnMessageOnError)
            tcBeginReq.setReturnMessageOnError(true);

        try {
            tcapDialog.send(tcBeginReq);
        } catch (TCAPSendException e) {
            throw new CAPException(e.getMessage(), e);
        }

    }

    protected TCBeginRequest encodeTCBegin(Dialog tcapDialog, ApplicationContextName acn,
            CAPGprsReferenceNumber gprsReferenceNumber) throws CAPException {

        TCBeginRequest tcBeginReq = this.getTCAPProvider().getDialogPrimitiveFactory().createBegin(tcapDialog);

        tcBeginReq.setApplicationContextName(acn);

        if (gprsReferenceNumber != null) {
        	UserInformation userInformation = TcapFactory.createUserInformation();
            userInformation.setIdentifier(CAPGprsReferenceNumberImpl.CAP_Dialogue_OId);
            userInformation.setChildAsObject(gprsReferenceNumber);
            tcBeginReq.setUserInformation(userInformation);            
        }
        return tcBeginReq;
    }

    protected void fireTCContinue(Dialog tcapDialog, ApplicationContextName acn, CAPGprsReferenceNumber gprsReferenceNumber,
            boolean returnMessageOnError) throws CAPException {
        TCContinueRequest tcContinueReq = encodeTCContinue(tcapDialog, acn, gprsReferenceNumber);
        if (returnMessageOnError)
            tcContinueReq.setReturnMessageOnError(true);

        try {
            tcapDialog.send(tcContinueReq);
        } catch (TCAPSendException e) {
            throw new CAPException(e.getMessage(), e);
        }
    }

    protected TCContinueRequest encodeTCContinue(Dialog tcapDialog, ApplicationContextName acn,
            CAPGprsReferenceNumber gprsReferenceNumber) throws CAPException {
        TCContinueRequest tcContinueReq = this.getTCAPProvider().getDialogPrimitiveFactory().createContinue(tcapDialog);

        if (acn != null)
            tcContinueReq.setApplicationContextName(acn);

        if (gprsReferenceNumber != null) {
        	UserInformation userInformation = TcapFactory.createUserInformation();
            userInformation.setIdentifier(CAPGprsReferenceNumberImpl.CAP_Dialogue_OId);
            userInformation.setChildAsObject(gprsReferenceNumber);
            tcContinueReq.setUserInformation(userInformation);
        }
        return tcContinueReq;
    }

    protected void fireTCEnd(Dialog tcapDialog, boolean prearrangedEnd, ApplicationContextName acn,
            CAPGprsReferenceNumber gprsReferenceNumber, boolean returnMessageOnError) throws CAPException {

    	TCEndRequest endRequest = encodeTCEnd(tcapDialog, prearrangedEnd, acn, gprsReferenceNumber);
        if (returnMessageOnError)
            endRequest.setReturnMessageOnError(true);

        try {
            tcapDialog.send(endRequest);
        } catch (TCAPSendException e) {
            throw new CAPException(e.getMessage(), e);
        }
    }

    protected TCEndRequest encodeTCEnd(Dialog tcapDialog, boolean prearrangedEnd, ApplicationContextName acn,
            CAPGprsReferenceNumber gprsReferenceNumber) throws CAPException {
        TCEndRequest endRequest = this.getTCAPProvider().getDialogPrimitiveFactory().createEnd(tcapDialog);

        if (!prearrangedEnd) {
            endRequest.setTermination(TerminationType.Basic);
        } else {
            endRequest.setTermination(TerminationType.PreArranged);
        }

        if (acn != null)
            endRequest.setApplicationContextName(acn);

        if (gprsReferenceNumber != null) {
        	UserInformation userInformation = TcapFactory.createUserInformation();
            userInformation.setIdentifier(CAPGprsReferenceNumberImpl.CAP_Dialogue_OId);
            userInformation.setChildAsObject(gprsReferenceNumber);
            endRequest.setUserInformation(userInformation);
        }
        return endRequest;
    }

    protected void fireTCAbort(Dialog tcapDialog, CAPGeneralAbortReason generalAbortReason, CAPUserAbortReason userAbortReason,
            boolean returnMessageOnError) throws CAPException {

        TCUserAbortRequest tcUserAbort = this.getTCAPProvider().getDialogPrimitiveFactory().createUAbort(tcapDialog);

        switch (generalAbortReason) {
            case ACNNotSupported:
                tcUserAbort.setDialogServiceUserType(DialogServiceUserType.AcnNotSupported);
                tcUserAbort.setApplicationContextName(tcapDialog.getApplicationContextName());
                break;

            case UserSpecific:
                if (userAbortReason == null)
                    userAbortReason = CAPUserAbortReason.no_reason_given;
                CAPUserAbortPrimitiveImpl abortReasonPrimitive = new CAPUserAbortPrimitiveImpl(userAbortReason);
                UserInformation userInformation = TcapFactory.createUserInformation();
                userInformation.setIdentifier(CAPUserAbortPrimitiveImpl.CAP_AbortReason_OId);
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
            throw new CAPException(e.getMessage(), e);
        }
    }
    
    @Override
    public int getCurrentDialogsCount() {
        return this.tcapProvider.getCurrentDialogsCount();
    }

}
