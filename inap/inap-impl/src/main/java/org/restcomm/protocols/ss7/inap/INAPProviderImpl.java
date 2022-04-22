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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPDialogListener;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPMessage;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
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
import org.restcomm.protocols.ss7.inap.errors.INAPErrorMessageCancelFailedImpl;
import org.restcomm.protocols.ss7.inap.errors.INAPErrorMessageFactoryImpl;
import org.restcomm.protocols.ss7.inap.errors.INAPErrorMessageImproperCallerResponseCS1PlusImpl;
import org.restcomm.protocols.ss7.inap.errors.INAPErrorMessageOctetStringImpl;
import org.restcomm.protocols.ss7.inap.errors.INAPErrorMessageParameterlessImpl;
import org.restcomm.protocols.ss7.inap.errors.INAPErrorMessageRequestedInfoErrorImpl;
import org.restcomm.protocols.ss7.inap.errors.INAPErrorMessageSystemFailureImpl;
import org.restcomm.protocols.ss7.inap.errors.INAPErrorMessageTaskRefusedImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ActivateServiceFilteringRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ActivityTestRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ActivityTestResponseImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.AnalyseInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.AnalysedInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingReportRequestCS1Impl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingReportRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingRequestCS1Impl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ApplyChargingRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.AssistRequestInstructionsRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.CallGapRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.CallInformationReportRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.CallInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.CallLimitRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.CancelRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.CancelStatusReportRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.CollectInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.CollectedInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ConnectRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ConnectToResourceRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ContinueCS1PlusRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ContinueRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ContinueWithArgumentRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.DialogueUserInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.DisconnectForwardConnectionCS1PlusRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.DisconnectForwardConnectionRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.EstablishTemporaryConnectionRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.EventNotificationChargingRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.EventReportBCSMRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.FurnishChargingInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.HandOverRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.HoldCallInNetworkRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.HoldCallPartyConnectionRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.INAPServiceCircuitSwitchedCallImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.InitialDPRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.InitiateCallAttemptRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.OAnswerRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.OCalledPartyBusyRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ODisconnectRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.OMidCallRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ONoAnswerRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.OriginationAttemptAuthorizedRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.PlayAnnouncementRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.PromptAndCollectUserInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.PromptAndCollectUserInformationResponseImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ReconnectRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ReleaseCallPartyConnectionRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ReleaseCallRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RequestCurrentStatusReportRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RequestCurrentStatusReportResponseImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RequestEveryStatusChangeReportRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RequestFirstStatusMatchReportRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RequestNotificationChargingEventRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RequestReportBCSMEventRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ResetTimerRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RetrieveRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RetrieveResponseImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.RouteSelectFailureRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SelectFacilityRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SelectRouteRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SendChargingInformationCS1RequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SendChargingInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.ServiceFilteringResponseRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SignallingInformationRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SpecializedResourceReportCS1PlusRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.SpecializedResourceReportRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.StatusReportRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.TAnswerRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.TBusyRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.TDisconnectRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.TMidCallRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.TNoAnswerRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.TermAttemptAuthorizedRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.UpdateRequestImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.UpdateResponseImpl;
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

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

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

        this.loger = LogManager.getLogger(INAPStackImpl.class.getCanonicalName() + "-" + name);

        this.inapServices.add(this.inapServiceCircuitSwitchedCall);
        
        try {
            //lets set default for user information as octet string
        	tcapProvider.getParser().getParser(ASNUserInformationObjectImpl.class, ASNOctetString.class);                

            ErrorCodeImpl errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.cancelFailed);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageCancelFailedImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.requestedInfoError);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageRequestedInfoErrorImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.systemFailure);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageSystemFailureImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.taskRefused);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageTaskRefusedImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.improperCallerResponse);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageImproperCallerResponseCS1PlusImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.congestion);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageOctetStringImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.errorInParameterValue);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageOctetStringImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.executionError);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageOctetStringImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.illegalCombinationOfParameters);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageOctetStringImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.infoNotAvailable);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageOctetStringImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.notAuthorized);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageOctetStringImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.parameterMissing);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageOctetStringImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode(INAPErrorCode.otherError);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, INAPErrorMessageOctetStringImpl.class);
      
        	//registering error options
        	tcapProvider.getParser().registerAlternativeClassMapping(INAPErrorMessageCancelFailedImpl.class, INAPErrorMessageCancelFailedImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(INAPErrorMessageRequestedInfoErrorImpl.class, INAPErrorMessageRequestedInfoErrorImpl.class);        	
        	tcapProvider.getParser().registerAlternativeClassMapping(INAPErrorMessageSystemFailureImpl.class, INAPErrorMessageSystemFailureImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(INAPErrorMessageTaskRefusedImpl.class, INAPErrorMessageTaskRefusedImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(INAPErrorMessageImproperCallerResponseCS1PlusImpl.class, INAPErrorMessageImproperCallerResponseCS1PlusImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(INAPErrorMessageOctetStringImpl.class, INAPErrorMessageOctetStringImpl.class);
        	
        	//register requests mappings
        	OperationCodeImpl opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.initialDP);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InitialDPRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.originationAttemptAuthorized);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, OriginationAttemptAuthorizedRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.collectedInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CollectedInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.analysedInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, AnalysedInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.routeSelectFailure);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RouteSelectFailureRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.oCalledPartyBusy);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, OCalledPartyBusyRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.oNoAnswer);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ONoAnswerRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.oAnswer);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, OAnswerRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.oDisconnect);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ODisconnectRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.termAttemptAuthorized);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, TermAttemptAuthorizedRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.tBusy);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, TBusyRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.tNoAnswer);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, TNoAnswerRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.tAnswer);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, TAnswerRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.tDisconnect);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, TDisconnectRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.oMidCall);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, OMidCallRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.tMidCall);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, TMidCallRequestImpl.class);        	
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.assistRequestInstructions);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, AssistRequestInstructionsRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.establishTemporaryConnection);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EstablishTemporaryConnectionRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.disconnectForwardConnection);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, DisconnectForwardConnectionCS1PlusRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.connectToResource);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ConnectToResourceRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.connect);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ConnectRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.holdCallInNetwork);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, HoldCallInNetworkRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.releaseCall);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReleaseCallRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.requestReportBCSMEvent);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestReportBCSMEventRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.eventReportBCSM);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EventReportBCSMRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.requestNotificationChargingEvent);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestNotificationChargingEventRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.eventNotificationCharging);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EventNotificationChargingRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.collectInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CollectInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.analyseInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, AnalyseInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.selectRoute);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SelectRouteRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.selectFacility);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SelectFacilityRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.continueCode);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ContinueCS1PlusRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.initiateCallAttempt);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InitiateCallAttemptRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.resetTimer);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ResetTimerRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.furnishChargingInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, FurnishChargingInformationRequestImpl.class);
        	
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.applyCharging);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ApplyChargingRequestImpl.class);
        	
        	OperationCodeWithACN operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingRequestCS1Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingRequestCS1Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingRequestCS1Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingRequestCS1Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingRequestCS1Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingRequestCS1Impl.class);

        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.applyChargingReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ApplyChargingReportRequestImpl.class);
        	
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingReportRequestCS1Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingReportRequestCS1Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingReportRequestCS1Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingReportRequestCS1Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingReportRequestCS1Impl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, ApplyChargingReportRequestCS1Impl.class);

        	
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.requestCurrentStatusReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestCurrentStatusReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.requestEveryStatusChangeReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestEveryStatusChangeReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.requestFirstStatusMatchReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RequestFirstStatusMatchReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.statusReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, StatusReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.callGap);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CallGapRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.activateServiceFiltering);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ActivateServiceFilteringRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.serviceFilteringResponse);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ServiceFilteringResponseRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.callInformationReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CallInformationReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.callInformationRequest);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CallInformationRequestImpl.class);
        	
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.sendChargingInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendChargingInformationRequestImpl.class);
        	
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, SendChargingInformationCS1RequestImpl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, SendChargingInformationCS1RequestImpl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, SendChargingInformationCS1RequestImpl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, SendChargingInformationCS1RequestImpl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, SendChargingInformationCS1RequestImpl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, SendChargingInformationCS1RequestImpl.class);

        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.playAnnouncement);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, PlayAnnouncementRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.promptAndCollectUserInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, PromptAndCollectUserInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.specializedResourceReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SpecializedResourceReportCS1PlusRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.cancelCode);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CancelRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.cancelStatusReportRequest);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CancelStatusReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.activityTest);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ActivityTestRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.callLimit);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CallLimitRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.continueWithArgument);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ContinueWithArgumentRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.dialogueUserInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, DialogueUserInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.handOver);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, HandOverRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.holdCallPartyConnection);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, HoldCallPartyConnectionRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.reconnect);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReconnectRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.releaseCallPartyConnection);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReleaseCallPartyConnectionRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.signallingInformation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SignallingInformationRequestImpl.class);

        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.retrieve);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_data_management_AC.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, RetrieveRequestImpl.class);
        	operationWithACN=new OperationCodeWithACN(opCode, INAPApplicationContext.Ericcson_cs1plus_data_management_AC_REV_B.getOID());			
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, operationWithACN, RetrieveRequestImpl.class);

        	//registering request options
        	tcapProvider.getParser().registerAlternativeClassMapping(InitialDPRequestImpl.class, InitialDPRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(OriginationAttemptAuthorizedRequestImpl.class, OriginationAttemptAuthorizedRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CollectedInformationRequestImpl.class, CollectedInformationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(AnalysedInformationRequestImpl.class, AnalysedInformationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RouteSelectFailureRequestImpl.class, RouteSelectFailureRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(OCalledPartyBusyRequestImpl.class, OCalledPartyBusyRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ONoAnswerRequestImpl.class, ONoAnswerRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(OAnswerRequestImpl.class, OAnswerRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ODisconnectRequestImpl.class, ODisconnectRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(TermAttemptAuthorizedRequestImpl.class, TermAttemptAuthorizedRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(TBusyRequestImpl.class, TBusyRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(TNoAnswerRequestImpl.class, TNoAnswerRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(TAnswerRequestImpl.class, TAnswerRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(TDisconnectRequestImpl.class, TDisconnectRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(OMidCallRequestImpl.class, OMidCallRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(TMidCallRequestImpl.class, TMidCallRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(AssistRequestInstructionsRequestImpl.class, AssistRequestInstructionsRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(RetrieveRequestImpl.class, RetrieveRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(EstablishTemporaryConnectionRequestImpl.class, EstablishTemporaryConnectionRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(DisconnectForwardConnectionCS1PlusRequestImpl.class, DisconnectForwardConnectionCS1PlusRequestImpl.class);
        	//does nothing since its empty class
        	tcapProvider.getParser().registerAlternativeClassMapping(DisconnectForwardConnectionCS1PlusRequestImpl.class, DisconnectForwardConnectionRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(ConnectToResourceRequestImpl.class, ConnectToResourceRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(ConnectRequestImpl.class, ConnectRequestImpl.class);
        	
        	//one is choise another is sequence , so we should be fine here
        	tcapProvider.getParser().registerAlternativeClassMapping(HoldCallInNetworkRequestImpl.class, HoldCallInNetworkRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(HoldCallInNetworkRequestImpl.class, UpdateRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(ReleaseCallRequestImpl.class, ReleaseCallRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RequestReportBCSMEventRequestImpl.class, RequestReportBCSMEventRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EventReportBCSMRequestImpl.class, EventReportBCSMRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RequestNotificationChargingEventRequestImpl.class, RequestNotificationChargingEventRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EventNotificationChargingRequestImpl.class, EventNotificationChargingRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CollectInformationRequestImpl.class, CollectInformationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(AnalyseInformationRequestImpl.class, AnalyseInformationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SelectRouteRequestImpl.class, SelectRouteRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(SelectFacilityRequestImpl.class, SelectFacilityRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(ContinueCS1PlusRequestImpl.class, ContinueCS1PlusRequestImpl.class);
        	//does nothing
        	tcapProvider.getParser().registerAlternativeClassMapping(ContinueCS1PlusRequestImpl.class, ContinueRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(InitiateCallAttemptRequestImpl.class, InitiateCallAttemptRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ResetTimerRequestImpl.class, ResetTimerRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(FurnishChargingInformationRequestImpl.class, FurnishChargingInformationRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(ApplyChargingRequestImpl.class, ApplyChargingRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ApplyChargingRequestCS1Impl.class, ApplyChargingRequestCS1Impl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(ApplyChargingReportRequestImpl.class, ApplyChargingReportRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ApplyChargingReportRequestCS1Impl.class, ApplyChargingReportRequestCS1Impl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(RequestCurrentStatusReportRequestImpl.class, RequestCurrentStatusReportRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RequestEveryStatusChangeReportRequestImpl.class, RequestEveryStatusChangeReportRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RequestFirstStatusMatchReportRequestImpl.class, RequestFirstStatusMatchReportRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(StatusReportRequestImpl.class, StatusReportRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CallGapRequestImpl.class, CallGapRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ActivateServiceFilteringRequestImpl.class, ActivateServiceFilteringRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ServiceFilteringResponseRequestImpl.class, ServiceFilteringResponseRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CallInformationReportRequestImpl.class, CallInformationReportRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CallInformationRequestImpl.class, CallInformationRequestImpl.class);        	
        	tcapProvider.getParser().registerAlternativeClassMapping(SendChargingInformationCS1RequestImpl.class, SendChargingInformationCS1RequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendChargingInformationRequestImpl.class, SendChargingInformationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(PlayAnnouncementRequestImpl.class, PlayAnnouncementRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(PromptAndCollectUserInformationRequestImpl.class, PromptAndCollectUserInformationRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(SpecializedResourceReportCS1PlusRequestImpl.class, SpecializedResourceReportCS1PlusRequestImpl.class);
        	//does nothing
        	tcapProvider.getParser().registerAlternativeClassMapping(SpecializedResourceReportCS1PlusRequestImpl.class, SpecializedResourceReportRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(CancelRequestImpl.class, CancelRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CancelStatusReportRequestImpl.class, CancelStatusReportRequestImpl.class);
        	
        	//does nothing
        	tcapProvider.getParser().registerAlternativeClassMapping(ActivityTestRequestImpl.class, ActivityTestRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CallLimitRequestImpl.class, CallLimitRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ContinueWithArgumentRequestImpl.class, ContinueWithArgumentRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(DialogueUserInformationRequestImpl.class, DialogueUserInformationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(HandOverRequestImpl.class, HandOverRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(HoldCallPartyConnectionRequestImpl.class, HoldCallPartyConnectionRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ReconnectRequestImpl.class, ReconnectRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ReleaseCallPartyConnectionRequestImpl.class, ReleaseCallPartyConnectionRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SignallingInformationRequestImpl.class, SignallingInformationRequestImpl.class);
        	
        	//register responses mappings
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.activityTest);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ActivityTestResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.promptAndCollectUserInformation);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, PromptAndCollectUserInformationResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.requestCurrentStatusReport);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, RequestCurrentStatusReportResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.update);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, UpdateResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode(INAPOperationCode.retrieve);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, RetrieveResponseImpl.class);
        	        	
        	//does nothing , also not sure we should have response
        	tcapProvider.getParser().registerAlternativeClassMapping(ActivityTestResponseImpl.class, ActivityTestResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(PromptAndCollectUserInformationResponseImpl.class, PromptAndCollectUserInformationResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RequestCurrentStatusReportResponseImpl.class, RequestCurrentStatusReportResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(UpdateResponseImpl.class, UpdateResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RetrieveResponseImpl.class, RetrieveResponseImpl.class);
        	
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
            processComponents(inapDialogImpl, comps, tcBeginIndication.getOriginalBuffer());
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
                processComponents(inapDialogImpl, comps, tcContinueIndication.getOriginalBuffer());
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
            processComponents(inapDialogImpl, comps, tcEndIndication.getOriginalBuffer());
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

    private void processComponents(INAPDialogImpl inapDialogImpl, List<BaseComponent> components, ByteBuf buffer) {

    	// Now let us decode the Components
        for (BaseComponent c : components) {

            doProcessComponent(inapDialogImpl, c, buffer);
        }
    }

    private void doProcessComponent(INAPDialogImpl inapDialogImpl, BaseComponent c, ByteBuf buffer) {

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
            		realMessage.setOriginalBuffer(buffer);
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
