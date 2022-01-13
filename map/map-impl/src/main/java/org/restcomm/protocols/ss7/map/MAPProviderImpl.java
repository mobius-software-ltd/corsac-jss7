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

package org.restcomm.protocols.ss7.map;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPDialogListener;
import org.restcomm.protocols.ss7.map.api.MAPDialogueAS;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.MAPServiceBase;
import org.restcomm.protocols.ss7.map.api.MAPSmsTpduParameterFactory;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPDialogState;
import org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.map.api.dialog.MAPProviderAbortReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.dialog.Reason;
import org.restcomm.protocols.ss7.map.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageFactory;
import org.restcomm.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandling;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPServiceOam;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivation;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPServiceSms;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.restcomm.protocols.ss7.map.dialog.MAPAcceptInfoImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPCloseInfoImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPOpenInfoImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPProviderAbortInfoImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPRefuseInfoImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPUserAbortInfoImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageAbsentSubscriber1Impl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageAbsentSubscriberImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageAbsentSubscriberSMImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageBusySubscriberImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageCUGRejectImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageCallBarred1Impl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageCallBarredImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageFacilityNotSupImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageFactoryImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageParameterlessImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessagePositionMethodFailureImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessagePwRegistrationFailureImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageRoamingNotAllowedImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageSMDeliveryFailure1Impl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageSMDeliveryFailureImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageSsErrorStatusImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageSsIncompatibilityImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageSubscriberBusyForMtSmsImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageSystemFailureImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageSytemFailure1Impl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageUnauthorizedLCSClientImpl;
import org.restcomm.protocols.ss7.map.errors.MAPErrorMessageUnknownSubscriberImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.IstCommandRequestImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.IstCommandResponseImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.MAPServiceCallHandlingImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.ProvideRoamingNumberRequestImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.ProvideRoamingNumberResponseImplV1;
import org.restcomm.protocols.ss7.map.service.callhandling.ProvideRoamingNumberResponseImplV3;
import org.restcomm.protocols.ss7.map.service.callhandling.SendRoutingInformationRequestImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.SendRoutingInformationResponseImplV1;
import org.restcomm.protocols.ss7.map.service.callhandling.SendRoutingInformationResponseImplV3;
import org.restcomm.protocols.ss7.map.service.lsm.MAPServiceLsmImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ProvideSubscriberLocationRequestImpl;
import org.restcomm.protocols.ss7.map.service.lsm.ProvideSubscriberLocationResponseImpl;
import org.restcomm.protocols.ss7.map.service.lsm.SendRoutingInfoForLCSRequestImpl;
import org.restcomm.protocols.ss7.map.service.lsm.SendRoutingInfoForLCSResponseImpl;
import org.restcomm.protocols.ss7.map.service.lsm.SubscriberLocationReportRequestImpl;
import org.restcomm.protocols.ss7.map.service.lsm.SubscriberLocationReportResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MAPServiceMobilityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationFailureReportRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationFailureReportResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.ResetRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.RestoreDataRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.RestoreDataResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiResponseImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.CancelLocationRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.CancelLocationRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.CancelLocationResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationResponseImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateGprsLocationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateGprsLocationResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateLocationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateLocationResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateLocationResponseImplV2;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeInterrogationResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.ProvideSubscriberInfoRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.ProvideSubscriberInfoResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DeleteSubscriberDataRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DeleteSubscriberDataResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataResponseImpl;
import org.restcomm.protocols.ss7.map.service.oam.ActivateTraceModeRequestImpl;
import org.restcomm.protocols.ss7.map.service.oam.ActivateTraceModeResponseImpl;
import org.restcomm.protocols.ss7.map.service.oam.MAPServiceOamImpl;
import org.restcomm.protocols.ss7.map.service.oam.SendImsiRequestImpl;
import org.restcomm.protocols.ss7.map.service.oam.SendImsiResponseImpl;
import org.restcomm.protocols.ss7.map.service.pdpContextActivation.MAPServicePdpContextActivationImpl;
import org.restcomm.protocols.ss7.map.service.pdpContextActivation.SendRoutingInfoForGprsRequestImpl;
import org.restcomm.protocols.ss7.map.service.pdpContextActivation.SendRoutingInfoForGprsResponseImpl;
import org.restcomm.protocols.ss7.map.service.sms.AlertServiceCentreRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.AlertServiceCentreResponseImpl;
import org.restcomm.protocols.ss7.map.service.sms.InformServiceCentreRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.MAPServiceSmsImpl;
import org.restcomm.protocols.ss7.map.service.sms.MoForwardShortMessageRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.MoForwardShortMessageResponseImpl;
import org.restcomm.protocols.ss7.map.service.sms.MtForwardShortMessageRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.MtForwardShortMessageResponseImpl;
import org.restcomm.protocols.ss7.map.service.sms.NoteSubscriberPresentRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.ReadyForSMRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.ReadyForSMResponseImpl;
import org.restcomm.protocols.ss7.map.service.sms.ReportSMDeliveryStatusRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.ReportSMDeliveryStatusResponseImplV1;
import org.restcomm.protocols.ss7.map.service.sms.ReportSMDeliveryStatusResponseImplV3;
import org.restcomm.protocols.ss7.map.service.sms.SendRoutingInfoForSMRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.SendRoutingInfoForSMResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ActivateSSRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ActivateSSResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.DeactivateSSRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.DeactivateSSResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.EraseSSRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.EraseSSResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.GetPasswordRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.GetPasswordResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.InterrogateSSRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.InterrogateSSResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.MAPServiceSupplementaryImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.RegisterPasswordRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.RegisterPasswordResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.RegisterSSRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.RegisterSSResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.UnstructuredSSNotifyRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.UnstructuredSSNotifyResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.UnstructuredSSRequestImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.UnstructuredSSResponseImpl;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCListener;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
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
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCodeType;
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
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPProviderImpl implements MAPProvider, TCListener {
	private static final long serialVersionUID = 1L;


	private final MAPStackConfigurationManagement mapCfg;
    protected final transient Logger loger;
    private transient ConcurrentHashMap<UUID,MAPDialogListener> dialogListeners = new ConcurrentHashMap<UUID,MAPDialogListener>();
//    protected transient FastMap<Long, MAPDialogImpl> dialogs = new FastMap<Long, MAPDialogImpl>().shared();
    protected transient ConcurrentHashMap<Long, MAPDialogImpl> dialogs = new ConcurrentHashMap<Long, MAPDialogImpl>();

//    /**
//     * Congestion sources name list. Congestion is where this collection is not empty
//     */
//    protected transient FastMap<String, String> congSources = new FastMap<String, String>();

    private transient TCAPProvider tcapProvider = null;

    private final transient MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();
    private final transient MAPSmsTpduParameterFactory mapSmsTpduParameterFactory = new MAPSmsTpduParameterFactoryImpl();
    private final transient MAPErrorMessageFactory mapErrorMessageFactory = new MAPErrorMessageFactoryImpl();

    protected transient Set<MAPServiceBase> mapServices = new HashSet<MAPServiceBase>();
    private final transient MAPServiceMobility mapServiceMobility = new MAPServiceMobilityImpl(this);
    private final transient MAPServiceCallHandling mapServiceCallHandling = new MAPServiceCallHandlingImpl(this);
    private final transient MAPServiceOam mapServiceOam = new MAPServiceOamImpl(this);
    private final transient MAPServicePdpContextActivation mapServicePdpContextActivation = new MAPServicePdpContextActivationImpl(
            this);
    private final transient MAPServiceSupplementary mapServiceSupplementary = new MAPServiceSupplementaryImpl(this);
    private final transient MAPServiceSms mapServiceSms = new MAPServiceSmsImpl(this);
    private final transient MAPServiceLsm mapServiceLsm = new MAPServiceLsmImpl(this);

    /**
     * public common methods
     */

    public MAPProviderImpl(String name, TCAPProvider tcapProvider,MAPStackConfigurationManagement configuration) {
        this.loger = Logger.getLogger(MAPStackImpl.class.getCanonicalName() + "-" + name);

        this.tcapProvider = tcapProvider;
        this.mapCfg=configuration;
        this.mapServices.add(this.mapServiceMobility);
        this.mapServices.add(this.mapServiceCallHandling);
        this.mapServices.add(this.mapServiceOam);
        this.mapServices.add(this.mapServicePdpContextActivation);
        this.mapServices.add(this.mapServiceSupplementary);
        this.mapServices.add(this.mapServiceSms);
        this.mapServices.add(this.mapServiceLsm);
        
        try {
        	//registering user information options
        	tcapProvider.getParser().registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPOpenInfoImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPAcceptInfoImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPCloseInfoImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPRefuseInfoImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPUserAbortInfoImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ASNUserInformationObjectImpl.class, MAPProviderAbortInfoImpl.class);
        	
        	//register error code mappings
        	ErrorCodeImpl errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.unexpectedDataValue);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.dataMissing);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.unidentifiedSubscriber);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.illegalSubscriber);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.illegalEquipment);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.teleserviceNotProvisioned);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.messageWaitingListFull);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.unauthorizedRequestingNetwork);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.resourceLimitation);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.unknownOrUnreachableLCSClient);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.incompatibleTerminal);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.noRoamingNumberAvailable);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.noSubscriberReply);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.forwardingFailed);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.orNotAllowed);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.forwardingViolation);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.numberChanged);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.unknownMSC);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.unknownEquipment);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.bearerServiceNotProvisioned);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.mmEventNotSupported);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.illegalSSOperation);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.ssNotAvailable);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.ssSubscriptionViolation);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.unknownAlphabet);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.ussdBusy);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.negativePWCheck);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.numberOfPWAttemptsViolation);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.shortTermDenial);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.longTermDenial);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageExtensionContainerImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.smDeliveryFailure);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSMDeliveryFailure1Impl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.absentSubscriberSM);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageAbsentSubscriberSMImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.systemFailure);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSytemFailure1Impl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.callBarred);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageCallBarred1Impl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.facilityNotSupported);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageFacilityNotSupImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.unknownSubscriber);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageUnknownSubscriberImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.subscriberBusyForMTSMS);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSubscriberBusyForMtSmsImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.absentSubscriber);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageAbsentSubscriber1Impl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.unauthorizedLCSClient);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageUnauthorizedLCSClientImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.positionMethodFailure);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessagePositionMethodFailureImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.busySubscriber);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageBusySubscriberImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.cugReject);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageCUGRejectImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.roamingNotAllowed);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageRoamingNotAllowedImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.ssErrorStatus);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSsErrorStatusImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.ssIncompatibility);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessageSsIncompatibilityImpl.class);
        	errorCode=new ErrorCodeImpl();
        	errorCode.setLocalErrorCode((long)MAPErrorCode.pwRegistrationFailure);
        	tcapProvider.getParser().registerLocalMapping(ReturnErrorImpl.class, errorCode, MAPErrorMessagePwRegistrationFailureImpl.class);
        	tcapProvider.getParser().registerDefaultLocalMapping(ReturnErrorImpl.class, MAPErrorMessageParameterlessImpl.class);
        	
        	//registering error options
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageExtensionContainerImpl.class, MAPErrorMessageExtensionContainerImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageAbsentSubscriberSMImpl.class, MAPErrorMessageAbsentSubscriberSMImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageSMDeliveryFailure1Impl.class, MAPErrorMessageSMDeliveryFailureImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageSMDeliveryFailure1Impl.class, MAPErrorMessageSMDeliveryFailure1Impl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageSytemFailure1Impl.class, MAPErrorMessageSytemFailure1Impl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageSytemFailure1Impl.class, MAPErrorMessageSystemFailureImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageCallBarred1Impl.class, MAPErrorMessageCallBarred1Impl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageCallBarred1Impl.class, MAPErrorMessageCallBarredImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageFacilityNotSupImpl.class, MAPErrorMessageFacilityNotSupImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageUnknownSubscriberImpl.class, MAPErrorMessageUnknownSubscriberImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageSubscriberBusyForMtSmsImpl.class, MAPErrorMessageSubscriberBusyForMtSmsImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageAbsentSubscriber1Impl.class, MAPErrorMessageAbsentSubscriber1Impl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageAbsentSubscriber1Impl.class, MAPErrorMessageAbsentSubscriberImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageUnauthorizedLCSClientImpl.class, MAPErrorMessageUnauthorizedLCSClientImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessagePositionMethodFailureImpl.class, MAPErrorMessagePositionMethodFailureImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageBusySubscriberImpl.class, MAPErrorMessageBusySubscriberImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageCUGRejectImpl.class, MAPErrorMessageCUGRejectImpl.class);        	
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageRoamingNotAllowedImpl.class, MAPErrorMessageRoamingNotAllowedImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageSsErrorStatusImpl.class, MAPErrorMessageSsErrorStatusImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageSsIncompatibilityImpl.class, MAPErrorMessageSsIncompatibilityImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessagePwRegistrationFailureImpl.class, MAPErrorMessagePwRegistrationFailureImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MAPErrorMessageParameterlessImpl.class, MAPErrorMessageParameterlessImpl.class);
        	
        	//register requests mappings
        	OperationCodeImpl opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.istCommand);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, IstCommandRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.provideRoamingNumber);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ProvideRoamingNumberRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendRoutingInfo);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendRoutingInformationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.provideSubscriberLocation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ProvideSubscriberLocationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendRoutingInfoForLCS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendRoutingInfoForLCSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.subscriberLocationReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SubscriberLocationReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.authenticationFailureReport);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, AuthenticationFailureReportRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendAuthenticationInfo);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendAuthenticationInfoRequestImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.reset);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ResetRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.restoreData);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RestoreDataRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.checkIMEI);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CheckImeiRequestImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.cancelLocation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, CancelLocationRequestImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.purgeMS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, PurgeMSRequestImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendIdentification);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendIdentificationRequestImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.updateGprsLocation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, UpdateGprsLocationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.updateLocation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, UpdateLocationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.anyTimeInterrogation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, AnyTimeInterrogationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.anyTimeSubscriptionInterrogation);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, AnyTimeSubscriptionInterrogationRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.provideSubscriberInfo);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ProvideSubscriberInfoRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.deleteSubscriberData);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, DeleteSubscriberDataRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.insertSubscriberData);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InsertSubscriberDataRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.activateTraceMode);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ActivateTraceModeRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendIMSI);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendImsiRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendRoutingInfoForGprs);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendRoutingInfoForGprsRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.alertServiceCentre);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, AlertServiceCentreRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.alertServiceCentreWithoutResult);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, AlertServiceCentreRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.mo_forwardSM);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, MoForwardShortMessageRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.informServiceCentre);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InformServiceCentreRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.mt_forwardSM);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, MtForwardShortMessageRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.noteSubscriberPresent);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, NoteSubscriberPresentRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.readyForSM);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReadyForSMRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.reportSM_DeliveryStatus);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ReportSMDeliveryStatusRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendRoutingInfoForSM);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, SendRoutingInfoForSMRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.activateSS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ActivateSSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.deactivateSS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, DeactivateSSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.eraseSS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, EraseSSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.getPassword);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, GetPasswordRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.interrogateSS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, InterrogateSSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.processUnstructuredSS_Request);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, ProcessUnstructuredSSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.registerPassword);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RegisterPasswordRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.registerSS);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, RegisterSSRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.unstructuredSS_Notify);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, UnstructuredSSNotifyRequestImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.unstructuredSS_Request);
        	tcapProvider.getParser().registerLocalMapping(InvokeImpl.class, opCode, UnstructuredSSRequestImpl.class);
        	
        	//registering request options
        	tcapProvider.getParser().registerAlternativeClassMapping(IstCommandRequestImpl.class, IstCommandRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ProvideRoamingNumberRequestImpl.class, ProvideRoamingNumberRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendRoutingInformationRequestImpl.class, SendRoutingInformationRequestImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(ProvideSubscriberLocationRequestImpl.class, ProvideSubscriberLocationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendRoutingInfoForLCSRequestImpl.class, SendRoutingInfoForLCSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SubscriberLocationReportRequestImpl.class, SubscriberLocationReportRequestImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(AuthenticationFailureReportRequestImpl.class, AuthenticationFailureReportRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendAuthenticationInfoRequestImplV1.class, SendAuthenticationInfoRequestImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendAuthenticationInfoRequestImplV1.class, SendAuthenticationInfoRequestImplV3.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(ResetRequestImpl.class, ResetRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RestoreDataRequestImpl.class, RestoreDataRequestImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(CheckImeiRequestImplV1.class, CheckImeiRequestImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CheckImeiRequestImplV1.class, CheckImeiRequestImplV3.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(CancelLocationRequestImplV1.class, CancelLocationRequestImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CancelLocationRequestImplV1.class, CancelLocationRequestImplV3.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(PurgeMSRequestImplV1.class, PurgeMSRequestImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(PurgeMSRequestImplV1.class, PurgeMSRequestImplV3.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendIdentificationRequestImplV1.class, SendIdentificationRequestImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendIdentificationRequestImplV1.class, SendIdentificationRequestImplV3.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(UpdateGprsLocationRequestImpl.class, UpdateGprsLocationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(UpdateLocationRequestImpl.class, UpdateLocationRequestImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(AnyTimeInterrogationRequestImpl.class, AnyTimeInterrogationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(AnyTimeSubscriptionInterrogationRequestImpl.class, AnyTimeSubscriptionInterrogationRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ProvideSubscriberInfoRequestImpl.class, ProvideSubscriberInfoRequestImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(DeleteSubscriberDataRequestImpl.class, DeleteSubscriberDataRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InsertSubscriberDataRequestImpl.class, InsertSubscriberDataRequestImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(ActivateTraceModeRequestImpl.class, ActivateTraceModeRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendImsiRequestImpl.class, SendImsiRequestImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(SendRoutingInfoForGprsRequestImpl.class, SendRoutingInfoForGprsRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(AlertServiceCentreRequestImpl.class, AlertServiceCentreRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MoForwardShortMessageRequestImpl.class, MoForwardShortMessageRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InformServiceCentreRequestImpl.class, InformServiceCentreRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MtForwardShortMessageRequestImpl.class, MtForwardShortMessageRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(NoteSubscriberPresentRequestImpl.class, NoteSubscriberPresentRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ReadyForSMRequestImpl.class, ReadyForSMRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ReportSMDeliveryStatusRequestImpl.class, ReportSMDeliveryStatusRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendRoutingInfoForSMRequestImpl.class, SendRoutingInfoForSMRequestImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(ActivateSSRequestImpl.class, ActivateSSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(DeactivateSSRequestImpl.class, DeactivateSSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EraseSSRequestImpl.class, EraseSSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(GetPasswordRequestImpl.class, GetPasswordRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InterrogateSSRequestImpl.class, InterrogateSSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ProcessUnstructuredSSRequestImpl.class, ProcessUnstructuredSSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RegisterPasswordRequestImpl.class, RegisterPasswordRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RegisterSSRequestImpl.class, RegisterSSRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(UnstructuredSSNotifyRequestImpl.class, UnstructuredSSNotifyRequestImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(UnstructuredSSRequestImpl.class, UnstructuredSSRequestImpl.class);
        	
        	//register responses mappings
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.istCommand);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, IstCommandResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.provideRoamingNumber);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ProvideRoamingNumberResponseImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendRoutingInfo);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, SendRoutingInformationResponseImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.provideSubscriberLocation);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ProvideSubscriberLocationResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendRoutingInfoForLCS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, SendRoutingInfoForLCSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.subscriberLocationReport);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, SubscriberLocationReportResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.authenticationFailureReport);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, AuthenticationFailureReportResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendAuthenticationInfo);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, SendAuthenticationInfoResponseImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.restoreData);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, RestoreDataResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.checkIMEI);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, CheckImeiResponseImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.cancelLocation);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, CancelLocationResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.purgeMS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, PurgeMSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendIdentification);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, SendIdentificationResponseImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.updateGprsLocation);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, UpdateGprsLocationResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.updateLocation);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, UpdateLocationResponseImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.anyTimeInterrogation);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, AnyTimeInterrogationResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.anyTimeSubscriptionInterrogation);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, AnyTimeSubscriptionInterrogationResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.provideSubscriberInfo);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ProvideSubscriberInfoResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.deleteSubscriberData);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, DeleteSubscriberDataResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.insertSubscriberData);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, InsertSubscriberDataResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.activateTraceMode);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ActivateTraceModeResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendIMSI);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, SendImsiResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendRoutingInfoForGprs);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, SendRoutingInfoForGprsResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.alertServiceCentre);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, AlertServiceCentreResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.mo_forwardSM);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, MoForwardShortMessageResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.mt_forwardSM);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, MtForwardShortMessageResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.readyForSM);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ReadyForSMResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.reportSM_DeliveryStatus);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ReportSMDeliveryStatusResponseImplV1.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.sendRoutingInfoForSM);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, SendRoutingInfoForSMResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.activateSS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ActivateSSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.deactivateSS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, DeactivateSSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.eraseSS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, EraseSSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.getPassword);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, GetPasswordResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.interrogateSS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, InterrogateSSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.processUnstructuredSS_Request);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, ProcessUnstructuredSSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.registerPassword);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, RegisterPasswordResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.registerSS);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, RegisterSSResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.unstructuredSS_Notify);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, UnstructuredSSNotifyResponseImpl.class);
        	opCode=new OperationCodeImpl();
        	opCode.setLocalOperationCode((long)MAPOperationCode.unstructuredSS_Request);
        	tcapProvider.getParser().registerLocalMapping(ReturnResultInnerImpl.class, opCode, UnstructuredSSResponseImpl.class);
        	
        	//registering response options
        	tcapProvider.getParser().registerAlternativeClassMapping(IstCommandResponseImpl.class, IstCommandResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ProvideRoamingNumberResponseImplV1.class, ProvideRoamingNumberResponseImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ProvideRoamingNumberResponseImplV1.class, ProvideRoamingNumberResponseImplV3.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendRoutingInformationResponseImplV1.class, SendRoutingInformationResponseImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendRoutingInformationResponseImplV1.class, SendRoutingInformationResponseImplV3.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(ProvideSubscriberLocationResponseImpl.class, ProvideSubscriberLocationResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendRoutingInfoForLCSResponseImpl.class, SendRoutingInfoForLCSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SubscriberLocationReportResponseImpl.class, SubscriberLocationReportResponseImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(AuthenticationFailureReportResponseImpl.class, AuthenticationFailureReportResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendAuthenticationInfoResponseImplV1.class, SendAuthenticationInfoResponseImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendAuthenticationInfoResponseImplV1.class, SendAuthenticationInfoResponseImplV3.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(RestoreDataResponseImpl.class, RestoreDataResponseImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(CheckImeiResponseImplV1.class, CheckImeiResponseImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(CheckImeiResponseImplV1.class, CheckImeiResponseImplV3.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(CancelLocationResponseImpl.class, CancelLocationResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(PurgeMSResponseImpl.class, PurgeMSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendIdentificationResponseImplV1.class, SendIdentificationResponseImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendIdentificationResponseImplV1.class, SendIdentificationResponseImplV3.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(UpdateGprsLocationResponseImpl.class, UpdateGprsLocationResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(UpdateLocationResponseImplV1.class, UpdateLocationResponseImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(UpdateLocationResponseImplV1.class, UpdateLocationResponseImplV2.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(AnyTimeInterrogationResponseImpl.class, AnyTimeInterrogationResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(AnyTimeSubscriptionInterrogationResponseImpl.class, AnyTimeSubscriptionInterrogationResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ProvideSubscriberInfoResponseImpl.class, ProvideSubscriberInfoResponseImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(DeleteSubscriberDataResponseImpl.class, DeleteSubscriberDataResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InsertSubscriberDataResponseImpl.class, InsertSubscriberDataResponseImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(ActivateTraceModeResponseImpl.class, ActivateTraceModeResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendImsiResponseImpl.class, SendImsiResponseImpl.class);

        	tcapProvider.getParser().registerAlternativeClassMapping(SendRoutingInfoForGprsResponseImpl.class, SendRoutingInfoForGprsResponseImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(AlertServiceCentreResponseImpl.class, AlertServiceCentreResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MoForwardShortMessageResponseImpl.class, MoForwardShortMessageResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(MtForwardShortMessageResponseImpl.class, MtForwardShortMessageResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ReadyForSMResponseImpl.class, ReadyForSMResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ReportSMDeliveryStatusResponseImplV1.class, ReportSMDeliveryStatusResponseImplV1.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ReportSMDeliveryStatusResponseImplV1.class, ReportSMDeliveryStatusResponseImplV3.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(SendRoutingInfoForSMResponseImpl.class, SendRoutingInfoForSMResponseImpl.class);
        	
        	tcapProvider.getParser().registerAlternativeClassMapping(ActivateSSResponseImpl.class, ActivateSSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(DeactivateSSResponseImpl.class, DeactivateSSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(EraseSSResponseImpl.class, EraseSSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(GetPasswordResponseImpl.class, GetPasswordResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(InterrogateSSResponseImpl.class, InterrogateSSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(ProcessUnstructuredSSResponseImpl.class, ProcessUnstructuredSSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RegisterPasswordResponseImpl.class, RegisterPasswordResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(RegisterSSResponseImpl.class, RegisterSSResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(UnstructuredSSNotifyResponseImpl.class, UnstructuredSSNotifyResponseImpl.class);
        	tcapProvider.getParser().registerAlternativeClassMapping(UnstructuredSSResponseImpl.class, UnstructuredSSResponseImpl.class);        	
        } catch(Exception ex) {
        	//already registered
        }
    }

    public TCAPProvider getTCAPProvider() {
        return this.tcapProvider;
    }

    public MAPStackConfigurationManagement getMapCfg() {
		return mapCfg;
	}

	public MAPServiceMobility getMAPServiceMobility() {
        return this.mapServiceMobility;
    }

    public MAPServiceCallHandling getMAPServiceCallHandling() {
        return this.mapServiceCallHandling;
    }

    public MAPServiceOam getMAPServiceOam() {
        return this.mapServiceOam;
    }

    public MAPServicePdpContextActivation getMAPServicePdpContextActivation() {
        return this.mapServicePdpContextActivation;
    }

    public MAPServiceSupplementary getMAPServiceSupplementary() {
        return this.mapServiceSupplementary;
    }

    public MAPServiceSms getMAPServiceSms() {
        return this.mapServiceSms;
    }

    public MAPServiceLsm getMAPServiceLsm() {
        return this.mapServiceLsm;
    }

    public void addMAPDialogListener(UUID key,MAPDialogListener mapDialogListener) {
        this.dialogListeners.put(key,mapDialogListener);
    }

    public MAPParameterFactory getMAPParameterFactory() {
        return MAPParameterFactory;
    }

    public MAPSmsTpduParameterFactory getMAPSmsTpduParameterFactory() {
        return mapSmsTpduParameterFactory;
    }

    public MAPErrorMessageFactory getMAPErrorMessageFactory() {
        return this.mapErrorMessageFactory;
    }

    public void removeMAPDialogListener(UUID key) {
        this.dialogListeners.remove(key);
    }

    public MAPDialog getMAPDialog(Long dialogId) {
            return this.dialogs.get(dialogId);        
    }

    public void start() {
        this.tcapProvider.addTCListener(this);
    }

    public void stop() {
        this.tcapProvider.removeTCListener(this);

        this.dialogs.clear();
    }

    /**
     * protected common methods
     */

    protected void addDialog(MAPDialogImpl dialog) {
            this.dialogs.put(dialog.getLocalDialogId(), dialog);        
    }

    protected MAPDialogImpl removeDialog(Long dialogId) {
            return this.dialogs.remove(dialogId);        
    }

    public void onTCBegin(TCBeginIndication tcBeginIndication) {

        ApplicationContextName acn = tcBeginIndication.getApplicationContextName();
        List<BaseComponent> comps = tcBeginIndication.getComponents();

        // ETS 300 974 Section 12.1.3
        // On receipt of a TC-BEGIN indication primitive, the MAP PM shall:
        //
        // - if no application-context-name is included in the primitive and if
        // the "Components present" indicator indicates "no components", issue a
        // TC-U-ABORT request primitive (note 2). The local MAP-User is not
        // informed;
        if (acn == null && comps == null) {
            loger.error(String
                    .format("Received TCBeginIndication=%s, both ApplicationContextName and Component[] are null. Send TC-U-ABORT to peer and not notifying the User",
                            tcBeginIndication));

            try {
                this.fireTCAbortV1(tcBeginIndication.getDialog(), false);
            } catch (MAPException e) {
                loger.error("Error while firing TC-U-ABORT. ", e);
            }
            return;
        }

        MAPApplicationContext mapAppCtx = null;
        MAPServiceBase perfSer = null;
        if (acn == null) {
            // ApplicationContext is absent but components are absent - MAP
            // Version 1

            // - if no application-context-name is included in the primitive and
            // if presence of components is indicated, wait for the first
            // TC-INVOKE primitive, and derive a version 1
            // application-context-name from the operation code according to
            // table 12.1/1 (note 1);

            // a) if no application-context-name can be derived (i.e. the
            // operation code does not exist in MAP V1 specifications), the MAP
            // PM shall issue a TC-U-ABORT request primitive (note 2). The local
            // MAP-User is not informed.

            // Extracting Invoke and operationCode
            Invoke invoke = null;
            int operationCode = -1;
            for (BaseComponent c : comps) {
                if (c instanceof Invoke) {
                    invoke = (Invoke)c;
                    break;
                }
            }
            if (invoke != null) {
                OperationCode oc = invoke.getOperationCode();
                if (oc != null && oc.getOperationType() == OperationCodeType.Local) {
                    operationCode = (int) (long) oc.getLocalOperationCode();
                }
            }
            if (operationCode != -1) {
                // Selecting the MAP service that can perform the operation, getting
                // ApplicationContext
                for (MAPServiceBase ser : this.mapServices) {
                    MAPApplicationContext ac = ((MAPServiceBaseImpl) ser).getMAPv1ApplicationContext(operationCode);
                    if (ac != null) {
                        perfSer = ser;
                        mapAppCtx = ac;
                        break;
                    }
                }
            }

            if (mapAppCtx == null) {
                // Invoke not found or has bad operationCode or operationCode is not supported
                try {
                    this.fireTCAbortV1(tcBeginIndication.getDialog(), false);
                    return;
                } catch (MAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }
            }
        } else {
            // ApplicationContext is present - MAP Version 2 or higher
            if (MAPApplicationContext.getProtocolVersion(acn.getOid()) < 2) {
                // if a version 1 application-context-name is included, the MAP
                // PM shall issue a TC-U-ABORT
                // request primitive with abort-reason "User-specific" and
                // user-information "MAP-ProviderAbortInfo"
                // indicating "abnormalDialogue". The local MAP-user shall not
                // be informed.
                loger.error("Bad version of ApplicationContext if ApplicationContext exists. Must be 2 or greater");
                try {
                    this.fireTCAbortProvider(tcBeginIndication.getDialog(), MAPProviderAbortReason.abnormalDialogue, null,
                            false);
                } catch (MAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }
                return;
            }

            mapAppCtx = MAPApplicationContext.getInstance(acn.getOid());

            // Check if ApplicationContext is recognizable for the implemented
            // services
            // If no - TC-U-ABORT - ACN-Not-Supported

            if (mapAppCtx == null) {
                StringBuffer s = new StringBuffer();
                s.append("Unrecognizable ApplicationContextName is received: ");
                for (Long l : acn.getOid()) {
                    s.append(l).append(", ");
                }

                loger.error(s.toString());
                try {
                    this.fireTCAbortACNNotSupported(tcBeginIndication.getDialog(), null, null, false);
                } catch (MAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }

                return;
            }
        }

        AddressString destReference = null;
        AddressString origReference = null;
        MAPExtensionContainer extensionContainer = null;
        boolean eriStyle = false;
        AddressString eriMsisdn = null;
        AddressString eriVlrNo = null;

        UserInformation userInfo = tcBeginIndication.getUserInformation();
        if (userInfo == null) {
            // if no User-information is present it is checked whether
            // presence of User Information in the
            // TC-BEGIN indication primitive is required for the received
            // application-context-name. If User
            // Information is required but not present, a TC-U-ABORT request
            // primitive with abort-reason
            // "User-specific" and user-information "MAP-ProviderAbortInfo"
            // indicating "abnormalDialogue"
            // shall be issued. The local MAP-user shall not be informed.

            // We demand User-information mandatory now for ACNs
            // that demand destination/origination reference presence
            // They are: callCompletionContext(8), networkFunctionalSsContext(18)
            // and networkUnstructuredSsContext(19)
            // TODO: I am not absolutely sure that it is correct
            if (mapAppCtx.getApplicationContextName() == MAPApplicationContextName.callCompletionContext
                    || mapAppCtx.getApplicationContextName() == MAPApplicationContextName.networkFunctionalSsContext
                    || mapAppCtx.getApplicationContextName() == MAPApplicationContextName.networkUnstructuredSsContext) {
                loger.error("When parsing TC-BEGIN: userInfo is mandatory for ACN==" + mapAppCtx.getApplicationContextName()
                        + " but not found");
                try {
                    this.fireTCAbortProvider(tcBeginIndication.getDialog(), MAPProviderAbortReason.abnormalDialogue, null,
                            false);
                } catch (MAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }
                return;
            }

        } else {
            // if an application-context-name different from version 1 is
            // included in the primitive and if User-
            // information is present, the User-information must constitute
            // a syntactically correct MAP-OPEN
            // dialogue PDU. Otherwise a TC-U-ABORT request primitive with
            // abort-reason "User-specific" and
            // user-information "MAP-ProviderAbortInfo" indicating
            // "abnormalDialogue" shall be issued and the
            // local MAP-user shall not be informed.

            if (!userInfo.isIDObjectIdentifier()) {
                loger.error("When parsing TC-BEGIN: userInfo.isOid() check failed");
                try {
                    this.fireTCAbortProvider(tcBeginIndication.getDialog(), MAPProviderAbortReason.invalidPDU, null, false);
                } catch (MAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }
                return;
            }

            List<Long> oid = userInfo.getObjectIdentifier();

            MAPDialogueAS mapDialAs = MAPDialogueAS.getInstance(oid);

            if (mapDialAs == null) {
                loger.error("When parsing TC-BEGIN: Expected MAPDialogueAS.MAP_DialogueAS but is null");
                try {
                    this.fireTCAbortProvider(tcBeginIndication.getDialog(), MAPProviderAbortReason.invalidPDU, null, false);
                } catch (MAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }
                return;
            }

            if (!userInfo.isValueObject()) {
                loger.error("When parsing TC-BEGIN: userInfo.isAsn() check failed");
                try {
                    this.fireTCAbortProvider(tcBeginIndication.getDialog(), MAPProviderAbortReason.invalidPDU, null, false);
                } catch (MAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }
                return;
            }

            Object userInfoObject=userInfo.getChild();
            if(!(userInfoObject instanceof MAPOpenInfoImpl)) {
            	loger.error("When parsing TC-BEGIN: MAP-OPEN dialog PDU must be received");
                try {
                    this.fireTCAbortProvider(tcBeginIndication.getDialog(), MAPProviderAbortReason.invalidPDU, null, false);
                } catch (MAPException e) {
                    loger.error("Error while firing TC-U-ABORT. ", e);
                }
                return;
            }
            
            MAPOpenInfoImpl mapOpenInfoImpl = (MAPOpenInfoImpl)userInfoObject;
            destReference = mapOpenInfoImpl.getDestReference();
            origReference = mapOpenInfoImpl.getOrigReference();
            extensionContainer = mapOpenInfoImpl.getExtensionContainer();
            eriStyle = mapOpenInfoImpl.getEriStyle();
            eriMsisdn = mapOpenInfoImpl.getEriMsisdn();
            eriVlrNo = mapOpenInfoImpl.getEriVlrNo();
        }

        // Selecting the MAP service that can perform the ApplicationContext
        if (perfSer == null) {
            for (MAPServiceBase ser : this.mapServices) {

                ServingCheckData chkRes = ser.isServingService(mapAppCtx);
                switch (chkRes.getResult()) {
                    case AC_Serving:
                        perfSer = ser;
                        break;

                    case AC_VersionIncorrect:
                        try {
                            this.fireTCAbortACNNotSupported(tcBeginIndication.getDialog(), null,
                                    chkRes.getAlternativeApplicationContext(), false);
                        } catch (MAPException e1) {
                            loger.error("Error while firing TC-U-ABORT. ", e1);
                        }
                        break;
					default:
						break;
                }

                if (perfSer != null)
                    break;
            }
        }

        // No MAPService can accept the received ApplicationContextName
        if (perfSer == null) {
            StringBuffer s = new StringBuffer();
            s.append("Unsupported ApplicationContextName is received: ");
            if (acn != null) {
                for (Long l : acn.getOid()) {
                    s.append(l).append(", ");
                }
            } else {
                s.append("MAP V1");
            }

            loger.error(s.toString());
            try {
                this.fireTCAbortACNNotSupported(tcBeginIndication.getDialog(), null, null, false);
            } catch (MAPException e1) {
                loger.error("Error while firing TC-U-ABORT. ", e1);
            }

            return;
        }

        // MAPService is not activated
        if (!perfSer.isActivated()) {
            StringBuffer s = new StringBuffer();
            s.append("ApplicationContextName of non activated MAPService is received. Will send back TCAP Abort : ");
            if (acn != null) {
                for (Long l : acn.getOid()) {
                    s.append(l).append(", ");
                }
            } else {
                s.append("MAP V1");
            }

            loger.warn(s.toString());

            try {
                this.fireTCAbortACNNotSupported(tcBeginIndication.getDialog(), null, null, false);
            } catch (MAPException e1) {
                loger.error("Error while firing TC-U-ABORT. ", e1);
            }

            return;
        }

        MAPDialogImpl mapDialogImpl = ((MAPServiceBaseImpl) perfSer).createNewDialogIncoming(mapAppCtx,
                tcBeginIndication.getDialog());
        this.addDialog(mapDialogImpl);
        mapDialogImpl.tcapMessageType = MessageType.Begin;
        mapDialogImpl.receivedOrigReference = origReference;
        mapDialogImpl.receivedDestReference = destReference;
        mapDialogImpl.receivedExtensionContainer = extensionContainer;

        mapDialogImpl.setState(MAPDialogState.INITIAL_RECEIVED);

        mapDialogImpl.delayedAreaState = MAPDialogImpl.DelayedAreaState.No;

        if (eriStyle) {
            this.deliverDialogRequestEri(mapDialogImpl, destReference, origReference, eriMsisdn, eriVlrNo);
        } else {
            this.deliverDialogRequest(mapDialogImpl, destReference, origReference, extensionContainer);
        }
        if (mapDialogImpl.getState() == MAPDialogState.EXPUNGED) {
            // The Dialog was aborter or refused
            return;
        }

        // Now let us decode the Components
        if (comps != null) {
            processComponents(mapDialogImpl, comps, tcBeginIndication.getOriginalBuffer());
        }

        this.deliverDialogDelimiter(mapDialogImpl);

        finishComponentProcessingState(mapDialogImpl);
    }

    private void finishComponentProcessingState(MAPDialogImpl mapDialogImpl) {

        if (mapDialogImpl.getState() == MAPDialogState.EXPUNGED)
            return;

        try {
            switch (mapDialogImpl.delayedAreaState) {
                case Continue:
                    mapDialogImpl.send();
                    break;
                case End:
                    mapDialogImpl.close(false);
                    break;
                case PrearrangedEnd:
                    mapDialogImpl.close(true);
                    break;
				default:
					break;
            }
        } catch (MAPException e) {
            loger.error("Error while finishComponentProcessingState, delayedAreaState=" + mapDialogImpl.delayedAreaState, e);
        }

        mapDialogImpl.delayedAreaState = null;
    }

    public void onTCContinue(TCContinueIndication tcContinueIndication) {

        Dialog tcapDialog = tcContinueIndication.getDialog();

        MAPDialogImpl mapDialogImpl = (MAPDialogImpl) this.getMAPDialog(tcapDialog.getLocalDialogId());
       
        if (mapDialogImpl == null) {
            loger.error("MAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            try {
                this.fireTCAbortProvider(tcapDialog, MAPProviderAbortReason.abnormalDialogue, null, false);
            } catch (MAPException e) {
                loger.error("Error while firing TC-U-ABORT. ", e);
            }
            return;
        }

        mapDialogImpl.tcapMessageType = MessageType.Continue;

        // Checking the received ApplicationContextName :
        // On receipt of the first TC-CONTINUE indication primitive for
        // a dialogue, the MAP PM shall check the value of the
        // application-context-name parameter. If this value matches the
        // one used in the MAP-OPEN request primitive, the MAP PM shall
        // issue a MAP-OPEN confirm primitive with the result parameter
        // indicating "accepted", then process the following TC
        // component handling indication primitives as described in
        // clause 12.6, and then waits for a request primitive from its
        // user or an indication primitive from TC, otherwise it shall
        // issue a TC-U-ABORT request primitive with a MAP-providerAbort
        // PDU indicating "abnormal dialogue" and a MAP-P-ABORT
        // indication primitive with the "provider-reason" parameter
        // indicating "abnormal dialogue".
        if (mapDialogImpl.getState() == MAPDialogState.INITIAL_SENT) {
            ApplicationContextName acn = tcContinueIndication.getApplicationContextName();

            if (acn == null) {

                // if MAP V1 - no ACN included
                if (mapDialogImpl.getApplicationContext().getApplicationContextVersion() != MAPApplicationContextVersion.version1) {

                    loger.error(String.format(
                            "Received first TC-CONTINUE for MAPDialog=%s. But no application-context-name included",
                            mapDialogImpl));
                    try {
                        this.fireTCAbortProvider(tcapDialog, MAPProviderAbortReason.abnormalDialogue, null,
                                mapDialogImpl.getReturnMessageOnError());

                    } catch (MAPException e) {
                        loger.error("Error while firing TC-U-ABORT. ", e);
                    }

                    this.deliverDialogProviderAbort(mapDialogImpl, MAPAbortProviderReason.AbnormalMAPDialogueLocal,
                            MAPAbortSource.MAPProblem, null);
                    mapDialogImpl.setState(MAPDialogState.EXPUNGED);

                    return;
                }
            } else {

                MAPApplicationContext mapAcn = MAPApplicationContext.getInstance(acn.getOid());
                if (mapAcn == null || !mapAcn.equals(mapDialogImpl.getApplicationContext())) {

                    loger.error(String.format("Received first TC-CONTINUE. MAPDialog=%s. But MAPApplicationContext=%s",
                            mapDialogImpl, mapAcn));
                    try {
                        this.fireTCAbortProvider(tcapDialog, MAPProviderAbortReason.abnormalDialogue, null,
                                mapDialogImpl.getReturnMessageOnError());
                    } catch (MAPException e) {
                        loger.error("Error while firing TC-U-ABORT. ", e);
                    }

                    this.deliverDialogProviderAbort(mapDialogImpl, MAPAbortProviderReason.AbnormalMAPDialogueLocal,
                            MAPAbortSource.MAPProblem, null);
                    mapDialogImpl.setState(MAPDialogState.EXPUNGED);

                    return;
                }
            }

            MAPExtensionContainer extensionContainer = null;

            // Parse MapAcceptInfo if it exists - we ignore all errors here
            UserInformation userInfo = tcContinueIndication.getUserInformation();
            if (userInfo != null) {
                MAPAcceptInfoImpl mapAcceptInfoImpl = new MAPAcceptInfoImpl();

                if (userInfo.isIDObjectIdentifier()) {
                    List<Long> oid = userInfo.getObjectIdentifier();
                    MAPDialogueAS mapDialAs = MAPDialogueAS.getInstance(oid);

                    if (mapDialAs != null && userInfo.isValueObject()) {
                    	Object child=userInfo.getChild();
                    	if(child instanceof MAPAcceptInfoImpl) {
                    		mapAcceptInfoImpl=(MAPAcceptInfoImpl)child;
                            extensionContainer = mapAcceptInfoImpl.getExtensionContainer();
                    	}
                    }
                }
            }

            mapDialogImpl.delayedAreaState = MAPDialogImpl.DelayedAreaState.No;

            // Fire MAPAcceptInfo
            mapDialogImpl.setState(MAPDialogState.ACTIVE);
            this.deliverDialogAccept(mapDialogImpl, extensionContainer);

            if (mapDialogImpl.getState() == MAPDialogState.EXPUNGED) {
                // The Dialog was aborter
                finishComponentProcessingState(mapDialogImpl);
                return;
            }
        } else {
            mapDialogImpl.delayedAreaState = MAPDialogImpl.DelayedAreaState.No;
        }

        // Now let us decode the Components
        if (mapDialogImpl.getState() == MAPDialogState.INITIAL_SENT
                || mapDialogImpl.getState() == MAPDialogState.ACTIVE) {
            List<BaseComponent> comps = tcContinueIndication.getComponents();
            if (comps != null) {
                processComponents(mapDialogImpl, comps, tcContinueIndication.getOriginalBuffer());
            }
        } else {
            // This should never happen
            loger.error(String.format("Received TC-CONTINUE. MAPDialog=%s. But state is neither InitialSent or Active",
                    mapDialogImpl));
        }

        this.deliverDialogDelimiter(mapDialogImpl);

        finishComponentProcessingState(mapDialogImpl);
    }

    public void onTCEnd(TCEndIndication tcEndIndication) {

        Dialog tcapDialog = tcEndIndication.getDialog();

        MAPDialogImpl mapDialogImpl = (MAPDialogImpl) this.getMAPDialog(tcapDialog.getLocalDialogId());
        
        if (mapDialogImpl == null) {
            loger.error("MAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }

        mapDialogImpl.tcapMessageType = MessageType.End;

        if (mapDialogImpl.getState() == MAPDialogState.INITIAL_SENT) {
            // On receipt of a TC-END indication primitive in the
            // dialogue
            // initiated state, the MAP PM shall check the value of the
            // application-context-name parameter. If this value does
            // not
            // match
            // the one used in the MAPOPEN request primitive, the MAP PM
            // shall
            // discard any following component handling primitive and
            // shall
            // issue a MAP-P-ABORT indication primitive with the
            // "provider-reason" parameter indicating
            // "abnormal dialogue".
            ApplicationContextName acn = tcEndIndication.getApplicationContextName();

            if (acn == null) {

                // if MAP V1 - no ACN included
                if (mapDialogImpl.getApplicationContext().getApplicationContextVersion() != MAPApplicationContextVersion.version1) {

                    // for MAP version >= 2 - accepts only if only ERROR
                    // & REJECT components are present
                    boolean notOnlyErrorReject = false;
                    for (BaseComponent c : tcEndIndication.getComponents()) {
                        if (!(c instanceof ReturnError) && !(c instanceof Reject)) {
                            notOnlyErrorReject = true;
                            break;
                        }
                    }
                    if (notOnlyErrorReject) {
                        loger.error(String.format(
                                "Received first TC-END for MAPDialog=%s. But no application-context-name included",
                                mapDialogImpl));

                        this.deliverDialogProviderAbort(mapDialogImpl, MAPAbortProviderReason.AbnormalMAPDialogueLocal,
                                MAPAbortSource.MAPProblem, null);
                        mapDialogImpl.setState(MAPDialogState.EXPUNGED);

                        return;
                    }
                }
            } else {

                MAPApplicationContext mapAcn = MAPApplicationContext.getInstance(acn.getOid());

                if (mapAcn == null || !mapAcn.equals(mapDialogImpl.getApplicationContext())) {
                    loger.error(String.format("Received first TC-END. MAPDialog=%s. But MAPApplicationContext=%s",
                            mapDialogImpl, mapAcn));

                    this.deliverDialogProviderAbort(mapDialogImpl, MAPAbortProviderReason.AbnormalMAPDialogueLocal,
                            MAPAbortSource.MAPProblem, null);
                    mapDialogImpl.setState(MAPDialogState.EXPUNGED);

                    return;
                }
            }

            // Otherwise it shall issue a MAP-OPEN confirm primitive with the result
            // parameter set to "accepted" and process the following TC component
            // handling indication primitives as described in clause 12.6;

            // Fire MAPAcceptInfo
            mapDialogImpl.setState(MAPDialogState.ACTIVE);

            MAPExtensionContainer extensionContainer = null;
            // Parse MapAcceptInfo or MapCloseInfo if it exists - we
            // ignore all errors here
            UserInformation userInfo = tcEndIndication.getUserInformation();
            if (userInfo != null) {

                if (userInfo.isIDObjectIdentifier()) {
                    List<Long> oid = userInfo.getObjectIdentifier();
                    MAPDialogueAS mapDialAs = MAPDialogueAS.getInstance(oid);

                    if (mapDialAs != null && userInfo.isValueObject()) {
                    	Object child=userInfo.getChild();
                    	if(child instanceof MAPAcceptInfoImpl) {
                    		MAPAcceptInfoImpl mapAcceptInfoImpl = (MAPAcceptInfoImpl)child;                           
                            extensionContainer = mapAcceptInfoImpl.getExtensionContainer();
                    	} else if(child instanceof MAPCloseInfoImpl) {
                    		MAPCloseInfoImpl mapCloseInfoImpl = (MAPCloseInfoImpl)child;
                            extensionContainer = mapCloseInfoImpl.getExtensionContainer();
                    	}
                    }
                }
            }

            this.deliverDialogAccept(mapDialogImpl, extensionContainer);
            if (mapDialogImpl.getState() == MAPDialogState.EXPUNGED) {
                // The Dialog was aborter
                return;
            }
        }

        // Now let us decode the Components
        List<BaseComponent> comps = tcEndIndication.getComponents();
        if (comps != null) {
            processComponents(mapDialogImpl, comps, tcEndIndication.getOriginalBuffer());
        }

        this.deliverDialogClose(mapDialogImpl);

        mapDialogImpl.setState(MAPDialogState.EXPUNGED);
    }

    public void onTCUni(TCUniIndication arg0) {
        // MAP do not use TCUni - we just ignore them
    }

    public void onInvokeTimeout(Invoke invoke) {

        MAPDialogImpl mapDialogImpl = (MAPDialogImpl) this.getMAPDialog(invoke.getDialog().getLocalDialogId());

        if (mapDialogImpl != null) {
        	if (mapDialogImpl.getState() != MAPDialogState.EXPUNGED) {

                // Getting the MAP Service that serves the MAP Dialog
                MAPServiceBaseImpl perfSer = (MAPServiceBaseImpl) mapDialogImpl.getService();

                // We do not send invokeTimeout for Class 4 invokes
                if (invoke.getInvokeClass() == InvokeClass.Class4)
                    return;

                perfSer.deliverInvokeTimeout(mapDialogImpl, invoke);
            }
        }
    }

    public void onDialogTimeout(Dialog tcapDialog) {

        MAPDialogImpl mapDialogImpl = (MAPDialogImpl) this.getMAPDialog(tcapDialog.getLocalDialogId());

        if (mapDialogImpl != null) {
        	if (mapDialogImpl.getState() != MAPDialogState.EXPUNGED) {
                this.deliverDialogTimeout(mapDialogImpl);
            }
        }
    }

    public void onDialogReleased(Dialog tcapDialog) {

        MAPDialogImpl mapDialogImpl = this.removeDialog(tcapDialog.getLocalDialogId());

        if (mapDialogImpl != null) {
        	this.deliverDialogRelease(mapDialogImpl);
        }
    }

    public void onTCPAbort(TCPAbortIndication tcPAbortIndication) {
        Dialog tcapDialog = tcPAbortIndication.getDialog();

        MAPDialogImpl mapDialogImpl = (MAPDialogImpl) this.getMAPDialog(tcapDialog.getLocalDialogId());
        
        if (mapDialogImpl == null) {
            loger.error("MAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }

        mapDialogImpl.tcapMessageType = MessageType.Abort;

        PAbortCauseType pAbortCause = tcPAbortIndication.getPAbortCause();
        MAPAbortProviderReason abortProviderReason = MAPAbortProviderReason.ProviderMalfunction;
        MAPAbortSource abortSource = MAPAbortSource.TCProblem;

        // Table 16.1/1: Mapping of P-Abort cause in TC-P-ABORT indication
        // on to provider-reason in MAP-P-ABORT indication
        // TC P-Abort cause MAP provider-reason
        // unrecognized message type provider malfunction
        // unrecognized transaction Id supporting dialogue released
        // badlyFormattedTransactionPortion provider malfunction
        // incorrectTransactionPortion provider malfunction (note)
        // resourceLimitation resource limitation
        // abnormalDialogue provider malfunction
        // noCommonDialoguePortion version incompatibility
        // NOTE: Or version incompatibility in the dialogue initiated phase.

        switch (pAbortCause) {
            case UnrecognizedMessageType:
            case BadlyFormattedTxPortion:
            case AbnormalDialogue:
            case NoReasonGiven:
                abortProviderReason = MAPAbortProviderReason.ProviderMalfunction;
                break;
            case UnrecognizedTxID:
                abortProviderReason = MAPAbortProviderReason.SupportingDialogueTransactionReleased;
                break;
            case IncorrectTxPortion:
                if (mapDialogImpl.getState() == MAPDialogState.INITIAL_SENT) {
                    abortProviderReason = MAPAbortProviderReason.VersionIncompatibility;
                } else
                    abortProviderReason = MAPAbortProviderReason.ProviderMalfunction;
                break;
            case ResourceLimitation:
                abortProviderReason = MAPAbortProviderReason.ResourceLimitation;
                break;
            case NoCommonDialoguePortion:
                abortProviderReason = MAPAbortProviderReason.VersionIncompatibilityTcap;
                break;
        }

        if (abortProviderReason == MAPAbortProviderReason.VersionIncompatibility)
            // On receipt of a TC-P-ABORT indication primitive in the
            // "Dialogue Initiated" state with a P-abort parameter
            // indicating "Incorrect Transaction Portion", the MAP PM shall
            // issue a MAP-OPEN confirm primitive with
            // the result parameter indicating "Dialogue Refused" and the
            // refuse reason parameter indicating "Potential Version Incompatibility"."
            this.deliverDialogReject(mapDialogImpl, MAPRefuseReason.PotentialVersionIncompatibility, null, null);
        else if (abortProviderReason == MAPAbortProviderReason.VersionIncompatibilityTcap)
            this.deliverDialogReject(mapDialogImpl, MAPRefuseReason.PotentialVersionIncompatibilityTcap, null, null);
        else
            this.deliverDialogProviderAbort(mapDialogImpl, abortProviderReason, abortSource, null);

        mapDialogImpl.setState(MAPDialogState.EXPUNGED);
    }

    private enum ParsePduResult {
        NoUserInfo, BadUserInfo, MapRefuse, MapUserAbort, MapProviderAbort;
    }

    public void onTCUserAbort(TCUserAbortIndication tcUserAbortIndication) {
        Dialog tcapDialog = tcUserAbortIndication.getDialog();

        MAPDialogImpl mapDialogImpl = (MAPDialogImpl) this.getMAPDialog(tcapDialog.getLocalDialogId());
        if (mapDialogImpl == null) {
            loger.error("MAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }

        mapDialogImpl.tcapMessageType = MessageType.Abort;

        // Trying to parse an userInfo APDU if it exists
        UserInformation userInfo = tcUserAbortIndication.getUserInformation();
        ParsePduResult parsePduResult = ParsePduResult.NoUserInfo;
        MAPRefuseReason mapRefuseReason = MAPRefuseReason.NoReasonGiven;
        MAPUserAbortChoice mapUserAbortChoice = null;
        MAPProviderAbortReason mapProviderAbortReason = null;
        MAPAbortProviderReason abortProviderReason = MAPAbortProviderReason.AbnormalMAPDialogueFromPeer;
        MAPExtensionContainer extensionContainer = null;

        if (userInfo != null) {
            // Checking userInfo.Oid==MAP_DialogueAS
            if (!userInfo.isIDObjectIdentifier()) {
                loger.error("When parsing TCUserAbortIndication indication: userInfo.isOid() check failed");
                parsePduResult = ParsePduResult.BadUserInfo;
            } else {

                List<Long> oid = userInfo.getObjectIdentifier();
                MAPDialogueAS mapDialAs = MAPDialogueAS.getInstance(oid);
                if (mapDialAs == null) {
                    loger.error("When parsing TCUserAbortIndication indication: userInfo.getOidValue() must be userInfoMAPDialogueAS.MAP_DialogueAS");
                    parsePduResult = ParsePduResult.BadUserInfo;
                } else if (!userInfo.isValueObject()) {

                    loger.error("When parsing TCUserAbortIndication indication: userInfo.isAsn() check failed");
                    parsePduResult = ParsePduResult.BadUserInfo;
                } else {
                	Object child=userInfo.getChild();
                	if(child instanceof MAPRefuseInfoImpl) {
                		// On receipt of a TC-U-ABORT indication primitive in the
                        // "Dialogue Initiated" state with an abort-reason
                        // parameter indicating "User Specific" and a MAP-Refuse PDU
                        // included as user information, the MAP PM
                        // shall issue a MAP-OPEN confirm primitive with the result
                        // set to refused and the refuse reason set as
                        // received in the MAP Refuse PDU.
                        MAPRefuseInfoImpl mapRefuseInfoImpl = (MAPRefuseInfoImpl)child;
                        
                        switch (mapRefuseInfoImpl.getReason()) {
                            case invalidOriginatingReference:
                                mapRefuseReason = MAPRefuseReason.InvalidOriginatingReference;
                                break;
                            case invalidDestinationReference:
                                mapRefuseReason = MAPRefuseReason.InvalidDestinationReference;
                                break;
							default:
								break;
                        }

                        extensionContainer = mapRefuseInfoImpl.getExtensionContainer();
                        parsePduResult = ParsePduResult.MapRefuse;
                	} else if(child instanceof MAPUserAbortInfoImpl) {
                		MAPUserAbortInfoImpl mapUserAbortInfoImpl = (MAPUserAbortInfoImpl)child;
                        
                        mapUserAbortChoice = mapUserAbortInfoImpl.getUserAbortChoise();
                        extensionContainer = mapUserAbortInfoImpl.getExtensionContainer();
                        parsePduResult = ParsePduResult.MapUserAbort;
                	} else if(child instanceof MAPProviderAbortInfoImpl) {
                		MAPProviderAbortInfoImpl mapProviderAbortInfoImpl = (MAPProviderAbortInfoImpl)child;
                        
                        mapProviderAbortReason = mapProviderAbortInfoImpl.getMAPProviderAbortReason();
                        switch (mapProviderAbortReason) {
                            case abnormalDialogue:
                                abortProviderReason = MAPAbortProviderReason.AbnormalMAPDialogueFromPeer;
                                break;
                            case invalidPDU:
                                abortProviderReason = MAPAbortProviderReason.InvalidPDU;
                                break;
							default:
								break;
                        }

                        extensionContainer = mapProviderAbortInfoImpl.getExtensionContainer();
                        parsePduResult = ParsePduResult.MapProviderAbort;
                	} else {
                		loger.error("When parsing TCUserAbortIndication indication: userInfogetEncodeType().Tag must be either MAP_REFUSE_INFO_TAG or MAP_USER_ABORT_INFO_TAG or MAP_PROVIDER_ABORT_INFO_TAG");
                        parsePduResult = ParsePduResult.BadUserInfo;                        
                	}                		                    
                }
            }
        }

        // special cases: AareApdu + ApplicationContextNotSupported or NoCommonDialogPortion
        if (tcUserAbortIndication.IsAareApdu()) {
            ResultSourceDiagnostic resultSourceDiagnostic = tcUserAbortIndication.getResultSourceDiagnostic();
            if (resultSourceDiagnostic != null) {
                // ACN_Not_Supported
            	DialogServiceUserType dsut=null;
            	try {
            		dsut=resultSourceDiagnostic.getDialogServiceUserType();
            	} catch(ParseException ex) {
            		
            	}
            	
            	DialogServiceProviderType dspt=null;
            	try {
            		dspt=resultSourceDiagnostic.getDialogServiceProviderType();
            	} catch(ParseException ex) {
            		
            	}
            	
                if (dsut!=null && dsut == DialogServiceUserType.AcnNotSupported) {
                    if (mapDialogImpl.getState() == MAPDialogState.INITIAL_SENT) {
                        this.deliverDialogReject(mapDialogImpl, MAPRefuseReason.ApplicationContextNotSupported,
                                tcUserAbortIndication.getApplicationContextName(), extensionContainer);

                        mapDialogImpl.setState(MAPDialogState.EXPUNGED);
                        return;
                    } else
                        parsePduResult = ParsePduResult.BadUserInfo;

                } else if (dspt!=null && dspt == DialogServiceProviderType.NoCommonDialogPortion) {
                    if (mapDialogImpl.getState() == MAPDialogState.INITIAL_SENT) {
                        // NoCommonDialogPortion
                        this.deliverDialogReject(mapDialogImpl, MAPRefuseReason.PotentialVersionIncompatibilityTcap, null, null);

                        mapDialogImpl.setState(MAPDialogState.EXPUNGED);
                        return;
                    } else
                        parsePduResult = ParsePduResult.BadUserInfo;

                }
            }
        }

        switch (parsePduResult) {
            case NoUserInfo:
                // Neither ABRT nor AARE APDU presents

                // On receipt of a TC-U-ABORT indication primitive in the
                // "Dialogue Initiated" state with an abort-reason
                // parameter indicating "User Specific" and without user
                // information, the MAP PM shall issue a MAP-OPEN
                // confirm primitive with the result parameter indicating
                // "Dialogue Refused" and the refuse-reason
                // parameter indicating "Potential Version Incompatibility".

                if (mapDialogImpl.getState() == MAPDialogState.INITIAL_SENT)
                    this.deliverDialogReject(mapDialogImpl, MAPRefuseReason.NoReasonGiven, null, null);
                else
                    this.deliverDialogProviderAbort(mapDialogImpl, MAPAbortProviderReason.AbnormalMAPDialogueLocal,
                            MAPAbortSource.MAPProblem, null);
                break;

            case BadUserInfo:
                this.deliverDialogProviderAbort(mapDialogImpl, MAPAbortProviderReason.AbnormalMAPDialogueLocal,
                        MAPAbortSource.MAPProblem, null);
                break;

            case MapRefuse:
                if (mapDialogImpl.getState() == MAPDialogState.INITIAL_SENT) {
                    // On receipt of a TC-U-ABORT indication primitive in the
                    // "Dialogue Initiated" state with an abort-reason
                    // parameter indicating "User Specific" and a MAP-Refuse PDU
                    // included as user information, the MAP PM
                    // shall issue a MAP-OPEN confirm primitive with the result
                    // set to refused and the refuse reason set as
                    // received in the MAP Refuse PDU.
                    this.deliverDialogReject(mapDialogImpl, mapRefuseReason, null, extensionContainer);
                } else {
                    // MAPRefuseInfo in a wrong Dialog state
                    this.deliverDialogProviderAbort(mapDialogImpl, MAPAbortProviderReason.AbnormalMAPDialogueLocal,
                            MAPAbortSource.MAPProblem, null);
                }
                break;

            case MapUserAbort:
                this.deliverDialogUserAbort(mapDialogImpl, mapUserAbortChoice, extensionContainer);
                break;

            case MapProviderAbort:
                this.deliverDialogProviderAbort(mapDialogImpl, abortProviderReason, MAPAbortSource.MAPProblem,
                        extensionContainer);
                break;
        }

        mapDialogImpl.setState(MAPDialogState.EXPUNGED);
    }

    /**
     * private service methods
     */
    private void processComponents(MAPDialogImpl mapDialogImpl, List<BaseComponent> components, ByteBuf buffer) {

        // Now let us decode the Components
        for (BaseComponent c : components) {

            doProcessComponent(mapDialogImpl, c, buffer);
        }
    }

    private void doProcessComponent(MAPDialogImpl mapDialogImpl, BaseComponent c, ByteBuf buffer) {

        // Getting the MAP Service that serves the MAP Dialog
        MAPServiceBaseImpl perfSer = (MAPServiceBaseImpl) mapDialogImpl.getService();        
        try {
            ComponentType compType = ComponentType.Invoke;
            if(c instanceof Invoke)
            	compType=ComponentType.Invoke;
            else if(c instanceof ReturnError)
            	compType=ComponentType.ReturnError;
            else if(c instanceof Reject)
            	compType=ComponentType.Reject;
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

                    if (linkedId != null) {
                        // linkedId exists Checking if the linkedId exists
                        linkedInvoke = comp.getLinkedInvoke();

                        long[] lstInv = null;
                        if(linkedInvoke.getOperationCode().getOperationType()==OperationCodeType.Local)
                        	lstInv=perfSer.getLinkedOperationList((linkedInvoke.getOperationCode()).getLocalOperationCode());
                        if (lstInv == null) {
                            ProblemImpl problem = new ProblemImpl();
                            problem.setInvokeProblemType(InvokeProblemType.LinkedResponseUnexpected);
                            mapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(mapDialogImpl, invokeId, problem, true);

                            return;
                        }

                        boolean found = false;
                        if (lstInv != null) {
                        	if(comp.getOperationCode().getOperationType()==OperationCodeType.Local) {
	                            for (long l : lstInv) {
	                                if (l == (comp.getOperationCode()).getLocalOperationCode()) {
	                                    found = true;
	                                    break;
	                                }
	                            }
                        	}
                        }
                        if (!found) {
                            ProblemImpl problem = new ProblemImpl();
                            problem.setInvokeProblemType(InvokeProblemType.UnexpectedLinkedOperation);
                            mapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(mapDialogImpl, invokeId, problem, true);

                            return;
                        }
                    }
                }
                    break;

                case ReturnResult: {
                    ReturnResult comp = (ReturnResult)c;
                    oc = comp.getOperationCode();
                    parameter = comp.getParameter();
                }
                    break;

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
                        errorCode = (comp.getErrorCode()).getLocalErrorCode();
                    if (errorCode < MAPErrorCode.minimalCodeValue || errorCode > MAPErrorCode.maximumCodeValue) {
                        // Not Local error code and not MAP error code received
                        ProblemImpl problem = new ProblemImpl();
                        problem.setReturnErrorProblemType(ReturnErrorProblemType.UnrecognizedError);
                        mapDialogImpl.sendRejectComponent(invokeId, problem);
                        perfSer.deliverRejectComponent(mapDialogImpl, invokeId, problem, true);

                        return;
                    }

                    MAPErrorMessage msgErr = this.mapErrorMessageFactory.createMessageFromErrorCode(errorCode,Long.valueOf(mapDialogImpl.getApplicationContext().getApplicationContextVersion().getVersion()));
                    Object p = comp.getParameter();
                    if (p != null && p instanceof MAPErrorMessage) {
                        msgErr=(MAPErrorMessage)p;
                    }
                    else if(p != null) {
                    	ProblemImpl problem = new ProblemImpl();
                         problem.setReturnErrorProblemType(ReturnErrorProblemType.MistypedParameter);
                         mapDialogImpl.sendRejectComponent(invokeId, problem);
                         perfSer.deliverRejectComponent(mapDialogImpl, invokeId, problem, true);
                         return;
                    }
                    
                    perfSer.deliverErrorComponent(mapDialogImpl, comp.getInvokeId(), msgErr);                    
                    return;
                }

                case Reject: {
                    Reject rej = (Reject)c;
                    perfSer.deliverRejectComponent(mapDialogImpl, rej.getInvokeId(), rej.getProblem(), rej.isLocalOriginated());

                    return;
                }

                default:
                    return;
            }

            try {

            	//null for Forward Check SS Indication
            	if(parameter!=null && !(parameter instanceof MAPMessage)) {
            		throw new MAPParsingComponentException("MAPServiceHandling: unknown incoming operation code: " + oc.getLocalOperationCode(),
                            MAPParsingComponentExceptionReason.MistypedParameter);
            	}
            	
            	MAPMessage realMessage=(MAPMessage)parameter;
            	if(realMessage!=null) {
            		realMessage.setOriginalBuffer(buffer);
	            	realMessage.setInvokeId(invokeId);
	            	realMessage.setMAPDialog(mapDialogImpl);
	            	realMessage.setReturnResultNotLast(compType == ComponentType.ReturnResult);
            	}
            	
                perfSer.processComponent(compType, oc, realMessage, mapDialogImpl, invokeId, linkedId);

            } catch (MAPParsingComponentException e) {

                loger.error(
                        "MAPParsingComponentException when parsing components: " + e.getReason().toString() + " - "
                                + e.getMessage(), e);

                switch (e.getReason()) {
                    case UnrecognizedOperation:
                        // Component does not supported - send TC-U-REJECT
                        if (compType == ComponentType.Invoke) {
                            ProblemImpl problem = new ProblemImpl();
                            problem.setInvokeProblemType(InvokeProblemType.UnrecognizedOperation);
                            mapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(mapDialogImpl, invokeId, problem, true);
                        } else {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setReturnResultProblemType(ReturnResultProblemType.MistypedParameter);
                            mapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(mapDialogImpl, invokeId, problem, true);
                        }
                        break;

                    case MistypedParameter:
                        // Failed when parsing the component - send TC-U-REJECT
                        if (compType == ComponentType.Invoke) {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setInvokeProblemType(InvokeProblemType.MistypedParameter);
                            mapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(mapDialogImpl, invokeId, problem, true);
                        } else {
                        	ProblemImpl problem = new ProblemImpl();
                            problem.setReturnResultProblemType(ReturnResultProblemType.MistypedParameter);
                            mapDialogImpl.sendRejectComponent(invokeId, problem);
                            perfSer.deliverRejectComponent(mapDialogImpl, invokeId, problem, true);
                        }
                        break;
                }
            }
        } catch (MAPException e) {
            loger.error("Error processing a Component: " + e.getMessage() + "\nComponent" + c, e);
        }
    }

    public void onTCNotice(TCNoticeIndication ind) {

        Dialog tcapDialog = ind.getDialog();
        if (tcapDialog == null) {
            // no existent Dialog for TC-NOTICE
            return;
        }

        MAPDialogImpl mapDialogImpl = (MAPDialogImpl) this.getMAPDialog(tcapDialog.getLocalDialogId());

        if (mapDialogImpl == null) {
            loger.error("MAP Dialog not found for Dialog Id " + tcapDialog.getLocalDialogId());
            return;
        }

        if (mapDialogImpl.getState() == MAPDialogState.INITIAL_SENT) {
            // If a TC-NOTICE indication primitive is received before the
            // dialogue has been confirmed (i.e. no backward message is
            // received by the dialogue initiator node), the MAP PM shall
            // issue a MAP-OP EN Cnf primitive with the result parameter
            // indicating Refused and a refuse reason Remote node not
            // reachable�.
            this.deliverDialogReject(mapDialogImpl, MAPRefuseReason.RemoteNodeNotReachable, null, null);
            mapDialogImpl.setState(MAPDialogState.EXPUNGED);
        } else if (mapDialogImpl.getState() == MAPDialogState.ACTIVE) {
            // If a TC-NOTICE indication primitive is received after the
            // dialogue has been confirmed, the MAP PM shall issue a
            // MAP-NOTICE indication to the user, with a problem diagnostic
            // indicating "message cannot be delivered to the peer".
            this.deliverDialogNotice(mapDialogImpl, MAPNoticeProblemDiagnostic.MessageCannotBeDeliveredToThePeer);
        }
    }

    private void deliverDialogDelimiter(MAPDialog mapDialog) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogDelimiter(mapDialog);
        }
    }

    private void deliverDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            MAPExtensionContainer extensionContainer) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogRequest(mapDialog, destReference, origReference, extensionContainer);
        }
    }

    private void deliverDialogRequestEri(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
            AddressString eriMsisdn, AddressString eriVlrNo) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogRequestEricsson(mapDialog, destReference, origReference, eriMsisdn, eriVlrNo);
        }
    }

    private void deliverDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogAccept(mapDialog, extensionContainer);
        }
    }

    private void deliverDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
            ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);
        }
    }

    private void deliverDialogClose(MAPDialog mapDialog) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogClose(mapDialog);
        }
    }

    private void deliverDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
            MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogProviderAbort(mapDialog, abortProviderReason, abortSource, extensionContainer);
        }
    }

    private void deliverDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason,
            MAPExtensionContainer extensionContainer) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogUserAbort(mapDialog, userReason, extensionContainer);
        }
    }

    protected void deliverDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogNotice(mapDialog, noticeProblemDiagnostic);
        }
    }

    protected void deliverDialogRelease(MAPDialog mapDialog) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogRelease(mapDialog);
        }
    }

    protected void deliverDialogTimeout(MAPDialog mapDialog) {
    	Iterator<MAPDialogListener> iterator=this.dialogListeners.values().iterator();
        while(iterator.hasNext()) {
        	iterator.next().onDialogTimeout(mapDialog);
        }
    }

    protected void fireTCBegin(Dialog tcapDialog, ApplicationContextName acn, AddressString destReference,
            AddressString origReference, MAPExtensionContainer mapExtensionContainer, boolean isEriStyle, AddressString eriMsisdn,
            AddressString vlrNoEri, boolean returnMessageOnError) throws MAPException {

        TCBeginRequest tcBeginReq = encodeTCBegin(tcapDialog, acn, destReference, origReference, mapExtensionContainer,
                isEriStyle, eriMsisdn, vlrNoEri);
        if (returnMessageOnError)
            tcBeginReq.setReturnMessageOnError(true);

        try {
            tcapDialog.send(tcBeginReq);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }

    }

    protected TCBeginRequest encodeTCBegin(Dialog tcapDialog, ApplicationContextName acn, AddressString destReference,
            AddressString origReference, MAPExtensionContainer mapExtensionContainer, boolean eriStyle, AddressString eriMsisdn,
            AddressString eriVlrNo) throws MAPException {

        TCBeginRequest tcBeginReq = this.getTCAPProvider().getDialogPrimitiveFactory().createBegin(tcapDialog);

        // we do not set ApplicationContextName if MAP Version 1
        if (MAPApplicationContext.getProtocolVersion(acn.getOid()) > 1)
            tcBeginReq.setApplicationContextName(acn);

        if ((destReference != null || origReference != null || mapExtensionContainer != null || eriStyle)
                && MAPApplicationContext.getProtocolVersion(acn.getOid()) > 1) {
            MAPOpenInfoImpl mapOpn = new MAPOpenInfoImpl();
            mapOpn.setDestReference(destReference);
            mapOpn.setOrigReference(origReference);
            mapOpn.setExtensionContainer(mapExtensionContainer);
            mapOpn.setEriMsisdn(eriMsisdn);
            mapOpn.setEriVlrNo(eriVlrNo);

            UserInformation userInformation = TcapFactory.createUserInformation();
            userInformation.setIdentifier(MAPDialogueAS.MAP_DialogueAS.getOID());
            userInformation.setChildAsObject(mapOpn);
            tcBeginReq.setUserInformation(userInformation);
        }
        return tcBeginReq;
    }

    protected void fireTCContinue(Dialog tcapDialog, Boolean sendMapAcceptInfo, ApplicationContextName acn,
            MAPExtensionContainer mapExtensionContainer, boolean returnMessageOnError) throws MAPException {

        TCContinueRequest tcContinueReq = encodeTCContinue(tcapDialog, sendMapAcceptInfo, acn, mapExtensionContainer);
        if (returnMessageOnError)
            tcContinueReq.setReturnMessageOnError(true);

        try {
            tcapDialog.send(tcContinueReq);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    protected TCContinueRequest encodeTCContinue(Dialog tcapDialog, Boolean sendMapAcceptInfo, ApplicationContextName acn,
    		MAPExtensionContainer mapExtensionContainer) throws MAPException {
        TCContinueRequest tcContinueReq = this.getTCAPProvider().getDialogPrimitiveFactory().createContinue(tcapDialog);

        // we do not set ApplicationContextName if MAP Version 1
        if (acn != null && MAPApplicationContext.getProtocolVersion(acn.getOid()) > 1)
            tcContinueReq.setApplicationContextName(acn);

        if (sendMapAcceptInfo && mapExtensionContainer != null && MAPApplicationContext.getProtocolVersion(acn.getOid()) > 1) {

            MAPAcceptInfoImpl mapAccept = new MAPAcceptInfoImpl();
            mapAccept.setExtensionContainer(mapExtensionContainer);

            UserInformation userInformation = TcapFactory.createUserInformation();
            userInformation.setIdentifier(MAPDialogueAS.MAP_DialogueAS.getOID());
            userInformation.setChildAsObject(mapAccept);
            tcContinueReq.setUserInformation(userInformation);
        }
        return tcContinueReq;
    }

    protected void fireTCEnd(Dialog tcapDialog, Boolean sendMapCloseInfo, boolean prearrangedEnd, ApplicationContextName acn,
            MAPExtensionContainer mapExtensionContainer, boolean returnMessageOnError) throws MAPException {

    	TCEndRequest endRequest = encodeTCEnd(tcapDialog, sendMapCloseInfo, prearrangedEnd, acn, mapExtensionContainer);
        if (returnMessageOnError)
            endRequest.setReturnMessageOnError(true);

        try {
            tcapDialog.send(endRequest);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    protected TCEndRequest encodeTCEnd(Dialog tcapDialog, Boolean sendMapCloseInfo, boolean prearrangedEnd,
            ApplicationContextName acn, MAPExtensionContainer mapExtensionContainer) throws MAPException {
        TCEndRequest endRequest = this.getTCAPProvider().getDialogPrimitiveFactory().createEnd(tcapDialog);

        if (!prearrangedEnd) {
            endRequest.setTermination(TerminationType.Basic);
        } else {
            endRequest.setTermination(TerminationType.PreArranged);
        }

        // we do not set ApplicationContextName if MAP Version 1
        if (acn != null && MAPApplicationContext.getProtocolVersion(acn.getOid()) > 1)
            endRequest.setApplicationContextName(acn);

        if (sendMapCloseInfo && mapExtensionContainer != null && MAPApplicationContext.getProtocolVersion(acn.getOid()) > 1) {
            MAPAcceptInfoImpl mapAccept = new MAPAcceptInfoImpl();
            mapAccept.setExtensionContainer(mapExtensionContainer);

            UserInformation userInformation = TcapFactory.createUserInformation();
            userInformation.setIdentifier(MAPDialogueAS.MAP_DialogueAS.getOID());
            
            userInformation.setChildAsObject(mapAccept);
            endRequest.setUserInformation(userInformation);
        }
        return endRequest;
    }

    /**
     * Issue TC-U-ABORT with the "abort reason" = "application-content-name-not-supported"
     *
     * @param tcapDialog
     * @param mapExtensionContainer
     * @param alternativeApplicationContext
     * @throws MAPException
     */
    private void fireTCAbortACNNotSupported(Dialog tcapDialog, MAPExtensionContainer mapExtensionContainer,
            ApplicationContextName alternativeApplicationContext, boolean returnMessageOnError) throws MAPException {

        if (tcapDialog.getApplicationContextName() == null) // MAP V1
            this.fireTCAbortV1(tcapDialog, returnMessageOnError);

        TCUserAbortRequest tcUserAbort = this.getTCAPProvider().getDialogPrimitiveFactory().createUAbort(tcapDialog);

        MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
        mapRefuseInfoImpl.setReason(Reason.noReasonGiven);

        UserInformation userInformation = TcapFactory.createUserInformation();
        userInformation.setIdentifier(MAPDialogueAS.MAP_DialogueAS.getOID());       
        userInformation.setChildAsObject(mapRefuseInfoImpl);
        
        if (returnMessageOnError)
            tcUserAbort.setReturnMessageOnError(true);

        if (alternativeApplicationContext != null)
            tcUserAbort.setApplicationContextName(alternativeApplicationContext);
        else
            tcUserAbort.setApplicationContextName(tcapDialog.getApplicationContextName());
        tcUserAbort.setDialogServiceUserType(DialogServiceUserType.AcnNotSupported);
        tcUserAbort.setUserInformation(userInformation);

        try {
            tcapDialog.send(tcUserAbort);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    /**
     * Issue TC-U-ABORT with the "abort reason" = "dialogue-refused"
     *
     * @param tcapDialog
     * @param reason
     * @param mapExtensionContainer
     * @throws MAPException
     */
    protected void fireTCAbortRefused(Dialog tcapDialog, Reason reason, MAPExtensionContainer mapExtensionContainer,
            boolean returnMessageOnError) throws MAPException {

        if (tcapDialog.getApplicationContextName() == null) // MAP V1
            this.fireTCAbortV1(tcapDialog, returnMessageOnError);

        TCUserAbortRequest tcUserAbort = this.getTCAPProvider().getDialogPrimitiveFactory().createUAbort(tcapDialog);

        MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
        mapRefuseInfoImpl.setReason(reason);
        mapRefuseInfoImpl.setExtensionContainer(mapExtensionContainer);

        // ApplicationContextName aacn = new ApplicationContextNameImpl();
        // aacn.setOid(new long[] { 3, 4, 5 } );
        // mapRefuseInfoImpl.setAlternativeAcn(aacn);

        UserInformation userInformation = TcapFactory.createUserInformation();
        userInformation.setIdentifier(MAPDialogueAS.MAP_DialogueAS.getOID());
        userInformation.setChildAsObject(mapRefuseInfoImpl);
        
        tcUserAbort.setApplicationContextName(tcapDialog.getApplicationContextName());
        tcUserAbort.setDialogServiceUserType(DialogServiceUserType.NoReasonGive);
        tcUserAbort.setUserInformation(userInformation);
        if (returnMessageOnError)
            tcUserAbort.setReturnMessageOnError(true);

        try {
            tcapDialog.send(tcUserAbort);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    /**
     * Issue TC-U-ABORT with the ABRT apdu with MAPUserAbortInfo userInformation
     *
     * @param tcapDialog
     * @param mapUserAbortChoice
     * @param mapExtensionContainer
     * @throws MAPException
     */
    protected void fireTCAbortUser(Dialog tcapDialog, MAPUserAbortInfoImpl mapUserAbortInfoImpl, boolean returnMessageOnError) throws MAPException {

        if (tcapDialog.getApplicationContextName() == null) // MAP V1
            this.fireTCAbortV1(tcapDialog, returnMessageOnError);

        TCUserAbortRequest tcUserAbort = this.getTCAPProvider().getDialogPrimitiveFactory().createUAbort(tcapDialog);

        UserInformation userInformation = TcapFactory.createUserInformation();
        userInformation.setIdentifier(MAPDialogueAS.MAP_DialogueAS.getOID());
        userInformation.setChildAsObject(mapUserAbortInfoImpl);
        
        if (returnMessageOnError)
            tcUserAbort.setReturnMessageOnError(true);

        tcUserAbort.setUserInformation(userInformation);

        try {
            tcapDialog.send(tcUserAbort);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }

    }

    /**
     * Issue TC-U-ABORT with the ABRT apdu with MAPProviderAbortInfo userInformation
     *
     * @param tcapDialog
     * @param mapProviderAbortReason
     * @param mapExtensionContainer
     * @throws MAPException
     */
    protected void fireTCAbortProvider(Dialog tcapDialog, MAPProviderAbortReason mapProviderAbortReason,
    		MAPExtensionContainer mapExtensionContainer, boolean returnMessageOnError) throws MAPException {

        if (tcapDialog.getApplicationContextName() == null) // MAP V1
            this.fireTCAbortV1(tcapDialog, returnMessageOnError);

        TCUserAbortRequest tcUserAbort = this.getTCAPProvider().getDialogPrimitiveFactory().createUAbort(tcapDialog);

        MAPProviderAbortInfoImpl mapProviderAbortInfo = new MAPProviderAbortInfoImpl();
        mapProviderAbortInfo.setMAPProviderAbortReason(mapProviderAbortReason);
        mapProviderAbortInfo.setExtensionContainer(mapExtensionContainer);

        UserInformation userInformation = TcapFactory.createUserInformation();
        userInformation.setIdentifier(MAPDialogueAS.MAP_DialogueAS.getOID());
        userInformation.setChildAsObject(mapProviderAbortInfo);
        if (returnMessageOnError)
            tcUserAbort.setReturnMessageOnError(true);

        tcUserAbort.setUserInformation(userInformation);

        try {
            tcapDialog.send(tcUserAbort);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    /**
     * Issue TC-U-ABORT without any apdu - for MAP V1
     *
     * @param tcapDialog
     * @param returnMessageOnError
     * @throws MAPException
     */
    protected void fireTCAbortV1(Dialog tcapDialog, boolean returnMessageOnError) throws MAPException {

        TCUserAbortRequest tcUserAbort = this.getTCAPProvider().getDialogPrimitiveFactory().createUAbort(tcapDialog);
        if (returnMessageOnError)
            tcUserAbort.setReturnMessageOnError(true);

        try {
            tcapDialog.send(tcUserAbort);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    @Override
    public int getCurrentDialogsCount() {
        return this.tcapProvider.getCurrentDialogsCount();
    }
}