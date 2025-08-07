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

package org.restcomm.protocols.ss7.map.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBearerServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtTeleserviceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.MAPStack;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.restcomm.protocols.ss7.map.api.dialog.Reason;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.primitives.NAEAPreferredCI;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.callhandling.AllowedServices;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CCBSIndicators;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.restcomm.protocols.ss7.map.api.service.callhandling.IstCommandRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.IstCommandResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.restcomm.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.restcomm.protocols.ss7.map.api.service.lsm.ExtGeographicalInformation;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.FailureCause;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.TripletList;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ADDInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UESRVCCCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UsedRATType;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Category;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SubscriberStatus;
import org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeRequest_Oam;
import org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeResponse_Oam;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.restcomm.protocols.ss7.map.api.service.oam.SendImsiRequest;
import org.restcomm.protocols.ss7.map.api.service.oam.SendImsiResponse;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPDialogPdpContextActivation;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsRequest;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertReason;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.MWStatus;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GenericServiceInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GuidanceInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSData;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.restcomm.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPUserAbortChoiseImpl;
import org.restcomm.protocols.ss7.map.functional.listeners.Client;
import org.restcomm.protocols.ss7.map.functional.listeners.Server;
import org.restcomm.protocols.ss7.map.functional.listeners.events.EventType;
import org.restcomm.protocols.ss7.map.functional.wrappers.MAPProviderImplWrapper;
import org.restcomm.protocols.ss7.map.functional.wrappers.MAPServiceSupplementaryImplWrapper;
import org.restcomm.protocols.ss7.map.functional.wrappers.MAPStackImplWrapper;
import org.restcomm.protocols.ss7.map.service.callhandling.RoutingInfoImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.SendRoutingInformationRequestImplV3;
import org.restcomm.protocols.ss7.map.service.callhandling.SendRoutingInformationResponseImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.TripletListTest;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.RestoreDataRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.imei.UESBIIuAImpl;
import org.restcomm.protocols.ss7.map.service.mobility.imei.UESBIIuBImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateGprsLocationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.CAMELSubscriptionInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTDPDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ODBGeneralDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.restcomm.protocols.ss7.map.service.oam.SendImsiRequestImpl;
import org.restcomm.protocols.ss7.map.service.sms.SmsSignalInfoImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.RegisterSSRequestImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsSubmitTpduImpl;
import org.restcomm.protocols.ss7.map.smstpdu.SmsTpduImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEvent;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventFactory;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventUtils;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

import com.mobius.software.common.dal.timers.WorkerPool;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@SuppressWarnings("unchecked")
public class MAPFunctionalTest extends SccpHarness {
	public static final String USSD_STRING = "*133#";
	public static final String USSD_MENU = "Select 1)Wallpaper 2)Ringtone 3)Games";
	public static final String USSD_RESPONSE = "1";
	public static final String USSD_FINAL_RESPONSE = "Thank you";

	private MAPStackImpl stack1;
	private MAPStackImpl stack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;

	@Before
	public void beforeEach() throws Exception {
		super.sccpStack1Name = "MAPFunctionalTestSccpStack1";
		super.sccpStack2Name = "MAPFunctionalTestSccpStack2";

		super.workerPool = new WorkerPool();
		workerPool.start(2);

		super.setUp();

		int ssn = getSSN();
		peer1Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), ssn);
		peer2Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), ssn);

		stack1 = new MAPStackImplWrapper(super.sccpProvider1, ssn, workerPool);
		stack2 = new MAPStackImplWrapper(super.sccpProvider2, ssn, workerPool);

		stack1.start();
		stack2.start();

		client = new Client(stack1, peer1Address, peer2Address);
		server = new Server(stack2, peer2Address, peer1Address);
	}

	@After
	public void afterEach() {
		if (this.stack1 != null) {
			this.stack1.stop();
			this.stack1 = null;
		}

		if (this.stack2 != null) {
			this.stack2.stop();
			this.stack2 = null;
		}

		super.tearDown();
	}

	/**
	 * Tests for MAP Dialog normal and abnormal actions
	 *
	 */

	/**
	 * Complex TC Dialog
	 *
	 * <pre>
	 * TC-BEGIN + ExtensionContainer + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + ExtensionContainer + addUnstructuredSSRequest
	 * TC-CONTINUE + addUnstructuredSSResponse 
	 * TC-END + addProcessUnstructuredSSResponse
	 * </pre>
	 */
	@Test
	public void testComplexTCWithDialog() throws Exception {
		// 1. TC-BEGIN + ExtensionContainer + addProcessUnstructuredSSRequest
		client.actionA();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		int processUnstructuredSSRequestInvokeId = Integer.MIN_VALUE;

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
			MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
			processUnstructuredSSRequestInvokeId = procUnstrReqInd.getInvokeId();

			USSDString ussdStringObj = server.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);
			mapDialog.addUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdStringObj, null, null);
		}

		// 2. TC-CONTINUE + ExtensionContainer + addUnstructuredSSRequest
		server.handleSent(EventType.UnstructuredSSRequestIndication, null);
		server.getCurrentDialog().setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
		server.getCurrentDialog().send(dummyCallback);

		client.awaitReceived(EventType.DialogAccept);
		server.awaitSent(EventType.UnstructuredSSRequestIndication);

		client.awaitReceived(EventType.UnstructuredSSRequestIndication);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.UnstructuredSSRequestIndication);
			UnstructuredSSRequest unstrReqInd = (UnstructuredSSRequest) event.getEvent();

			String ussdString = unstrReqInd.getUSSDString().getString(null);

			assertEquals(MAPFunctionalTest.USSD_MENU, ussdString);

			MAPDialogSupplementary mapDialog = unstrReqInd.getMAPDialog();
			Integer invokeId = unstrReqInd.getInvokeId();

			USSDString ussdStringObj = client.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_RESPONSE);
			mapDialog.addUnstructuredSSResponse(invokeId, new CBSDataCodingSchemeImpl(0x0f), ussdStringObj);
		}

		client.handleSent(EventType.UnstructuredSSResponseIndication, null);
		client.getCurrentDialog().send(dummyCallback);

		// 3. TC-CONTINUE + addUnstructuredSSResponse
		client.awaitSent(EventType.UnstructuredSSResponseIndication);

		server.awaitReceived(EventType.UnstructuredSSResponseIndication);
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.UnstructuredSSResponseIndication);
			UnstructuredSSResponse unstrResInd = (UnstructuredSSResponse) event.getEvent();

			String ussdString = unstrResInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_RESPONSE, ussdString);
		}

		MAPDialogSupplementary mapDialog = (MAPDialogSupplementary) server.getCurrentDialog();

		USSDString ussdStringObj = server.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_FINAL_RESPONSE);
		mapDialog.addProcessUnstructuredSSResponse(processUnstructuredSSRequestInvokeId,
				new CBSDataCodingSchemeImpl(0x0f), ussdStringObj);

		server.handleSent(EventType.ProcessUnstructuredSSResponseIndication, null);
		server.getCurrentDialog().close(false, dummyCallback);

		// 4. TC-END + addProcessUnstructuredSSResponse
		server.awaitSent(EventType.ProcessUnstructuredSSResponseIndication);
		client.awaitReceived(EventType.ProcessUnstructuredSSResponseIndication);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.UnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.UnstructuredSSResponseIndication);
		clientExpected.addReceived(EventType.ProcessUnstructuredSSResponseIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.UnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.UnstructuredSSResponseIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ProcessUnstructuredSSResponseIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Ending Dialog in the middle of conversation by "close(true)" - without
	 * sending components
	 *
	 * <pre>
	 * TC-BEGIN + ExtensionContainer + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + ExtensionContainer + addUnstructuredSSRequest
	 * prearranged TC-END
	 * </pre>
	 */
	@Test
	public void testDialogEndAtTheMiddleConversation() throws Exception {
		// 1. TC-BEGIN + ExtensionContainer + addProcessUnstructuredSSRequest
		client.actionA();

		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
		}

		// 2. TC-CONTINUE + ExtensionContainer + addUnstructuredSSRequest
		{
			MAPDialogSupplementary mapDialog = (MAPDialogSupplementary) server.getCurrentDialog();

			USSDString ussdStringObj = server.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);
			mapDialog.addUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdStringObj, null, null);

			server.handleSent(EventType.UnstructuredSSRequestIndication, null);
			mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
			mapDialog.send(dummyCallback);

			mapDialog.close(true, dummyCallback);
		}
		client.awaitReceived(EventType.DialogAccept);

		// 3. prearranged TC-END
		server.awaitSent(EventType.UnstructuredSSRequestIndication);
		client.awaitReceived(EventType.UnstructuredSSRequestIndication);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.UnstructuredSSRequestIndication);
			UnstructuredSSRequest unstrReqInd = (UnstructuredSSRequest) event.getEvent();

			String ussdString = unstrReqInd.getUSSDString().getString(null);

			assertEquals(MAPFunctionalTest.USSD_MENU, ussdString);

			MAPDialogSupplementary mapDialog = unstrReqInd.getMAPDialog();
			Integer invokeId = unstrReqInd.getInvokeId();

			USSDString ussdStringObj = client.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_RESPONSE);
			mapDialog.addUnstructuredSSResponse(invokeId, new CBSDataCodingSchemeImpl(0x0f), ussdStringObj);
		}

		client.handleSent(EventType.UnstructuredSSResponseIndication, null);
		client.getCurrentDialog().close(true, dummyCallback);

		client.awaitSent(EventType.UnstructuredSSResponseIndication);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.UnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.UnstructuredSSResponseIndication);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.UnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Server reject a Dialog with InvalidDestinationReference reason
	 *
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * refuse() -> TC-ABORT + MapRefuseInfo + ExtensionContainer
	 * </pre>
	 */
	@Test
	public void testDialogRefuse() throws Exception {
		// 1. TC-BEGIN + addProcessUnstructuredSSRequest
		client.actionA();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
		}

		// 2. refuse() -> TC-ABORT + MapRefuseInfo + ExtensionContainer
		MAPDialog serverDialog = server.getCurrentDialog();
		serverDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
		server.handleSent(EventType.DialogUserAbort, null);
		serverDialog.refuse(Reason.invalidDestinationReference, dummyCallback);

		server.awaitSent(EventType.DialogUserAbort);
		client.awaitReceived(EventType.DialogReject);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogReject);
			List<Object> args = (List<Object>) event.getEvent();

			MAPDialog rejectDialog = (MAPDialog) args.get(0);
			MAPRefuseReason refuseReason = (MAPRefuseReason) args.get(1);
			MAPExtensionContainer extensionContainer = (MAPExtensionContainer) args.get(3);

			assertEquals(refuseReason, MAPRefuseReason.InvalidDestinationReference);
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

			assertEquals(rejectDialog.getTCAPMessageType(), MessageType.Abort);
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogReject);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.DialogUserAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Server reject a Dialog because of ApplicationContextName does not supported
	 * (Bad ACN is simulated)
	 *
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-ABORT(Reason=ACN_Not_Supprted) + alternativeApplicationContextName
	 * </pre>
	 */
	@Test
	public void testInvalidApplicationContext() throws Exception {
		MAPServiceSupplementaryImplWrapper serviceWrapper2 = ((MAPServiceSupplementaryImplWrapper) stack2.getProvider()
				.getMAPServiceSupplementary());
		serviceWrapper2.setTestMode(1);

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest
		client.actionA();

		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		// 2. TC-ABORT(Reason=ACN_Not_Supprted) + alternativeApplicationContextName
		client.awaitReceived(EventType.DialogReject);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogReject);
			List<Object> args = (List<Object>) event.getEvent();

			MAPDialog rejectDialog = (MAPDialog) args.get(0);
			MAPRefuseReason refuseReason = (MAPRefuseReason) args.get(1);
			ApplicationContextName alternativeApplicationContext = (ApplicationContextName) args.get(2);

			assertEquals(refuseReason, MAPRefuseReason.ApplicationContextNotSupported);
			assertNotNull(alternativeApplicationContext);
			Long[] oids = new Long[alternativeApplicationContext.getOid().size()];
			oids = alternativeApplicationContext.getOid().toArray(oids);
			assertTrue(Arrays.equals(oids, new Long[] { 1L, 2L, 3L }));
			assertEquals(rejectDialog.getTCAPMessageType(), MessageType.Abort);
		}

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogReject);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * User-Abort as a response to TC-CONTINUE by a Client
	 *
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + addUnstructuredSSRequest
	 * TC-ABORT(MAP-UserAbortInfo) + ExtensionContainer
	 * </pre>
	 */
	@Test
	public void testDialogUserAbort() throws Exception {
		server.stop();

		server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);

				server.awaitSent(EventType.UnstructuredSSRequestIndication);
			}
		};

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest
		client.actionA();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);

			MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
			USSDString ussdStringObj = server.mapParameterFactory.createUSSDString(MAPFunctionalTest.USSD_MENU);
			mapDialog.addUnstructuredSSRequest(new CBSDataCodingSchemeImpl(0x0f), ussdStringObj, null, null);

			server.handleSent(EventType.UnstructuredSSRequestIndication, null);
			mapDialog.send(dummyCallback);
		}

		// 2. TC-CONTINUE + addUnstructuredSSRequest
		client.awaitReceived(EventType.UnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.UnstructuredSSRequestIndication);
			UnstructuredSSRequest unstrReqInd = (UnstructuredSSRequest) event.getEvent();

			String ussdString = unstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_MENU, ussdString);
		}

		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			mapDialog.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
			MAPUserAbortChoiseImpl choice = new MAPUserAbortChoiseImpl();
			choice.setProcedureCancellationReason(ProcedureCancellationReason.handoverCancellation);

			client.handleSent(EventType.DialogUserAbort, null);
			mapDialog.abort(choice, dummyCallback);
		}

		// 3. TC-ABORT(MAP-UserAbortInfo) + ExtensionContainer
		client.awaitSent(EventType.DialogUserAbort);

		server.awaitReceived(EventType.DialogDelimiter);
		server.awaitReceived(EventType.DialogUserAbort);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogUserAbort);
			List<Object> args = (List<Object>) event.getEvent();

			MAPDialog mapDialog = (MAPDialog) args.get(0);
			MAPUserAbortChoice userReason = (MAPUserAbortChoice) args.get(1);
			MAPExtensionContainer extensionContainer = (MAPExtensionContainer) args.get(2);

			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
			assertTrue(userReason.isProcedureCancellationReason());
			assertEquals(ProcedureCancellationReason.handoverCancellation, userReason.getProcedureCancellationReason());
			assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.UnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.DialogUserAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addSent(EventType.UnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogUserAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Simulating a ProviderAbort from a Server (InvalidPDU)
	 *
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-ABORT(MAP-ProviderAbortInfo)
	 * </pre>
	 */
	@Test
	public void testReceivedDialogAbortInfo() throws Exception {
		((MAPProviderImplWrapper) this.stack2.getProvider()).setTestMode(1);

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest
		client.actionA();

		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		// 2. TC-ABORT(MAP-ProviderAbortInfo)
		client.awaitReceived(EventType.DialogProviderAbort);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogProviderAbort);
			List<Object> args = (List<Object>) event.getEvent();

			MAPDialog mapDialog = (MAPDialog) args.get(0);
			MAPAbortProviderReason abortProviderReason = (MAPAbortProviderReason) args.get(1);
			MAPExtensionContainer extensionContainer = (MAPExtensionContainer) args.get(3);

			assertEquals(abortProviderReason, MAPAbortProviderReason.InvalidPDU);
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
			assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
		}

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogProviderAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Ericsson-style OpenInfo Dialog
	 *
	 * <pre>
	 * TC-BEGIN + Ericsson-style MAP-OpenInfo + addProcessUnstructuredSSRequest
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testEricssonDialog() throws Exception {
		// removing existing listener
		server.stop();

		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference,
					AddressString origReference, AddressString eriImsi, AddressString eriVlrNo) {
				super.onDialogRequestEricsson(mapDialog, destReference, origReference, eriImsi, eriVlrNo);

				assertNotNull(eriImsi);
				assertEquals(eriImsi.getAddress(), "12345");

				assertNotNull(eriVlrNo);
				assertEquals(eriVlrNo.getAddress(), "556677");

				assertNotNull(destReference);
				assertEquals(destReference.getAddress(), "888777");

				assertNotNull(origReference);
				assertEquals(origReference.getAddress(), "1115550000");
			}
		};

		// 1. TC-BEGIN + Ericsson-style MAP-OpenInfo + addProcessUnstructuredSSRequest
		client.actionEricssonDialog();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogEricssonRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			// sending close
			server.handleSent(EventType.DialogClose, null);
			mapDialog.close(false, dummyCallback);
		}

		client.awaitReceived(EventType.DialogAccept);

		// 2. TC-END
		server.awaitSent(EventType.DialogClose);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogEricssonRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.DialogClose);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Rejecting a dialog because of service is inactive
	 *
	 * <pre>
	 * TC-BEGIN + alertServiceCentre V2
	 * TC-ABORT + DialogReject + ACNNotSupported
	 * </pre>
	 */
	@Test
	public void testRejectServiceIsNotActive() throws Exception {
		// removing existing listener
		client.stop();

		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
					ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
				super.onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);

				assertEquals(refuseReason, MAPRefuseReason.ApplicationContextNotSupported);
			}
		};

		server.mapProvider.getMAPServiceSms().deactivate();

		// 1. TC-BEGIN + alertServiceCentre V2
		client.sendAlertServiceCentreRequestV2();
		client.awaitSent(EventType.AlertServiceCentreIndication);

		// 2. TC-ABORT + DialogReject+ACNNotSupported
		client.awaitReceived(EventType.DialogReject);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.AlertServiceCentreIndication);
		clientExpected.addReceived(EventType.DialogReject);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Rejecting a dialog because of service is inactive - MAP V1
	 *
	 * <pre>
	 * TC-BEGIN + alertServiceCentre V1
	 * TC-ABORT + DialogReject + ACNNotSupported
	 * </pre>
	 */
	@Test
	public void testRejectServiceIsNotActiveV1() throws Exception {
		server.mapProvider.getMAPServiceSms().deactivate();

		// 1. TC-BEGIN + alertServiceCentre V1
		client.sendAlertServiceCentreRequestV1();
		client.awaitSent(EventType.AlertServiceCentreIndication);

		// 2. TC-ABORT + DialogReject + ACNNotSupported
		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.AlertServiceCentreIndication);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Tests for MAP Component processing
	 * 
	 */

	/**
	 * Sending ReturnError (MAPErrorMessageSystemFailure) component from the Server
	 * as a response to ProcessUnstructuredSSRequest
	 *
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + ReturnError(systemFailure)
	 * </pre>
	 */
	@Test
	public void testComponentErrorMessageSystemFailure() throws Exception {
		client.stop();

		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onErrorComponent(MAPDialog mapDialog, Integer invokeId, MAPErrorMessage mapErrorMessage) {
				super.onErrorComponent(mapDialog, invokeId, mapErrorMessage);
				assertTrue(mapErrorMessage.isEmSystemFailure());

				MAPErrorMessageSystemFailure mes = mapErrorMessage.getEmSystemFailure();
				assertNotNull(mes);
				assertTrue(mes.getAdditionalNetworkResource() == null);
				assertTrue(mes.getNetworkResource() == null);
			}
		};

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest
		client.actionA();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);

			MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
			MAPErrorMessage msg = server.mapErrorMessageFactory.createMAPErrorMessageSystemFailure(null);
			mapDialog.sendErrorComponent(procUnstrReqInd.getInvokeId(), msg);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ErrorComponent, null);
			mapDialog.close(false, dummyCallback);
		}

		client.awaitReceived(EventType.DialogAccept);

		// 2. TC-END + ReturnError(systemFailure)
		server.awaitSent(EventType.ErrorComponent);
		client.awaitReceived(EventType.ErrorComponent);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ErrorComponent);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Sending ReturnError (SM-DeliveryFailure + SM-DeliveryFailureCause) component
	 * from the Server as a response to ProcessUnstructuredSSRequest
	 *
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + ReturnError(SM-DeliveryFailure + SM-DeliveryFailureCause)
	 * </pre>
	 */
	@Test
	public void testComponentErrorMessageSMDeliveryFailure() throws Exception {
		client.stop();

		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onErrorComponent(MAPDialog mapDialog, Integer invokeId, MAPErrorMessage mapErrorMessage) {
				super.onErrorComponent(mapDialog, invokeId, mapErrorMessage);
				assertTrue(mapErrorMessage.isEmSMDeliveryFailure());

				MAPErrorMessageSMDeliveryFailure mes = mapErrorMessage.getEmSMDeliveryFailure();
				assertNotNull(mes);
				assertEquals(mes.getSMEnumeratedDeliveryFailureCause(), SMEnumeratedDeliveryFailureCause.scCongestion);
				assertTrue(mes.getSignalInfo() == null);
			}
		};

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest
		client.actionA();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);

			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
			MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
			MAPErrorMessage msg = server.mapErrorMessageFactory
					.createMAPErrorMessageSMDeliveryFailure(SMEnumeratedDeliveryFailureCause.scCongestion, null, null);
			mapDialog.sendErrorComponent(procUnstrReqInd.getInvokeId(), msg);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ErrorComponent, null);
			mapDialog.close(false, dummyCallback);
		}

		client.awaitReceived(EventType.DialogAccept);

		// 2. TC-END + ReturnError(SM-DeliveryFailure + SM-DeliveryFailureCause)
		server.awaitSent(EventType.ErrorComponent);
		client.awaitReceived(EventType.ErrorComponent);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ErrorComponent);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Responses as ReturnResult (this case is simulated) and ReturnResultLast
	 *
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-CONTINUE + ReturnResult (addProcessUnstructuredSSResponse)
	 * TC-CONTINUE 
	 * TC-END + ReturnResultLast (addProcessUnstructuredSSResponse)
	 * </pre>
	 */
	@Test
	public void testComponentD() throws Exception {
		// 1. TC-BEGIN + addProcessUnstructuredSSRequest
		client.actionA();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		final String basicUssdString = "Your balance is 500";
		int processUnstructuredSSRequestInvokeId = Integer.MIN_VALUE;

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);

			MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
			processUnstructuredSSRequestInvokeId = procUnstrReqInd.getInvokeId();

			CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
			USSDString ussdStrObj = server.mapProvider.getMAPParameterFactory().createUSSDString(basicUssdString,
					ussdDataCodingScheme, null);

			ProcessUnstructuredSSResponseImpl req = new ProcessUnstructuredSSResponseImpl(ussdDataCodingScheme,
					ussdStrObj);
			mapDialog.sendDataComponent(processUnstructuredSSRequestInvokeId, null, null, null,
					MAPOperationCode.processUnstructuredSS_Request, req, false, false);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			// sending ProcessUnstructuredSSResponse
			server.handleSent(EventType.ProcessUnstructuredSSResponseIndication, null);
			mapDialog.send(dummyCallback);
		}

		// 2. TC-CONTINUE + ReturnResult (addProcessUnstructuredSSResponse)
		server.awaitSent(EventType.ProcessUnstructuredSSResponseIndication);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ProcessUnstructuredSSResponseIndication);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ProcessUnstructuredSSResponseIndication);
			ProcessUnstructuredSSResponse procUnstrResponse = (ProcessUnstructuredSSResponse) event.getEvent();

			String ussdString = procUnstrResponse.getUSSDString().getString(null);
			assertEquals(ussdString, basicUssdString);
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			mapDialog.send(dummyCallback);
		}

		// 3. TC-CONTINUE
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ProcessUnstructuredSSResponseIndication, null);
			USSDString ussdStrObj = server.mapProvider.getMAPParameterFactory().createUSSDString("Your balance is 500");
			CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
			((MAPDialogSupplementary) mapDialog).addProcessUnstructuredSSResponse(processUnstructuredSSRequestInvokeId,
					ussdDataCodingScheme, ussdStrObj);

			mapDialog.close(false, dummyCallback);
		}

		// 4. TC-END + ReturnResultLast (addProcessUnstructuredSSResponse)
		server.awaitSent(EventType.ProcessUnstructuredSSResponseIndication);
		client.awaitReceived(EventType.ProcessUnstructuredSSResponseIndication);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ProcessUnstructuredSSResponseIndication);
			ProcessUnstructuredSSResponse procUnstrResponse = (ProcessUnstructuredSSResponse) event.getEvent();

			String ussdString = procUnstrResponse.getUSSDString().getString(null);
			assertEquals(ussdString, basicUssdString);
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ProcessUnstructuredSSResponseIndication);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.ProcessUnstructuredSSResponseIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ProcessUnstructuredSSResponseIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ProcessUnstructuredSSResponseIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Responses as Reject (DuplicateInvokeID) component from the Server as a
	 * response to ProcessUnstructuredSSRequest
	 *
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest
	 * TC-END + Reject (ResourceLimitation)
	 * </pre>
	 * 
	 * - manually sent Reject
	 */
	@Test
	public void testComponentDuplicateInvokeID() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

				try {
					InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
					assertNotNull(invokeProblemType);
					assertEquals(invokeProblemType, InvokeProblemType.ResourceLimitation);
					assertTrue(problem.getGeneralProblemType() == null);
					assertTrue(problem.getReturnErrorProblemType() == null);
					assertTrue(problem.getReturnResultProblemType() == null);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(isLocalOriginated);
				assertEquals((long) invokeId, 0);
			}
		};

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest
		client.actionA();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
			MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();

			Problem problem = server.mapProvider.getMAPParameterFactory()
					.createProblemInvoke(InvokeProblemType.ResourceLimitation);

			mapDialog.sendRejectComponent(procUnstrReqInd.getInvokeId(), problem);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ErrorComponent, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + Reject (ResourceLimitation)
		server.awaitSent(EventType.ErrorComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogAccept);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Responses as ReturnError component from the Server as a response to
	 * ProcessUnstructuredSSRequest but the error received because of "close(true)"
	 *
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest 
	 * no TC-END + ReturnError(systemFailure) (prearranged end)
	 * </pre>
	 */
	@Test
	public void testComponentErrorCloseTrue() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onErrorComponent(MAPDialog mapDialog, Integer invokeId, MAPErrorMessage mapErrorMessage) {
				super.onErrorComponent(mapDialog, invokeId, mapErrorMessage);
				assertTrue(mapErrorMessage.isEmSMDeliveryFailure());

				MAPErrorMessageSMDeliveryFailure mes = mapErrorMessage.getEmSMDeliveryFailure();
				assertNotNull(mes);
				assertEquals(mes.getSMEnumeratedDeliveryFailureCause(), SMEnumeratedDeliveryFailureCause.scCongestion);
				assertTrue(mes.getSignalInfo() == null);
			}
		};

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest
		client.actionA();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
			MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
			MAPErrorMessage msg = server.mapErrorMessageFactory
					.createMAPErrorMessageSMDeliveryFailure(SMEnumeratedDeliveryFailureCause.scCongestion, null, null);

			mapDialog.sendErrorComponent(procUnstrReqInd.getInvokeId(), msg);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ErrorComponent, null);
			mapDialog.close(true, dummyCallback);
		}

		// 2. no TC-END + ReturnError(systemFailure) (prearranged end)
		client.clientDialog.close(true, dummyCallback);

		server.awaitSent(EventType.ErrorComponent);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Responses as Reject (ResourceLimitation without invokeId!) component from the
	 * Server as a response to ProcessUnstructuredSSRequest
	 * 
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest 
	 * TC-END + Reject (invokeProblem-ResourceLimitation) without invokeId! - this Reject is Invoked by MAP-user
	 * </pre>
	 */
	@Test
	public void testComponentGeneralProblemTypeComponent() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
				try {
					InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
					assertNotNull(invokeProblemType);
					assertEquals(invokeProblemType, InvokeProblemType.ResourceLimitation);
					assertTrue(problem.getGeneralProblemType() == null);
					assertTrue(problem.getReturnErrorProblemType() == null);
					assertTrue(problem.getReturnResultProblemType() == null);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(isLocalOriginated);
				assertNull(invokeId);
			}
		};

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest
		client.actionA();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
			MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();

			Problem problem = server.mapProvider.getMAPParameterFactory()
					.createProblemInvoke(InvokeProblemType.ResourceLimitation);

			mapDialog.sendRejectComponent(null, problem);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.RejectComponent, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + Reject (invokeProblem-ResourceLimitation) without invokeId!
		server.awaitSent(EventType.RejectComponent);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.RejectComponent);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Rejecting an Invoke with a bad OperationCode==1000
	 *
	 * <pre>
	 * TC-BEGIN + Invoke(bad opCode==1000)
	 * TC-END + Reject (generalProblem-UnrecognizedOperation) without invokeId!
	 * </pre>
	 */
	@Test
	public void testInvokeUnrecognizedOperation() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
				try {
					InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
					assertEquals(invokeProblemType, InvokeProblemType.UnrecognizedOperation);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(isLocalOriginated);
				assertEquals((long) invokeId, 10L);
			}
		};

		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
				try {
					InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
					assertEquals(invokeProblemType, InvokeProblemType.UnrecognizedOperation);
				} catch (ParseException ex) {
					ex.printStackTrace();
				}

				assertTrue(isLocalOriginated);
				assertEquals((long) invokeId, 10L);
			}
		};

		// 1. TC-BEGIN + Invoke(bad opCode==1000)
		client.sendUnrecognizedOperation();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + Reject (generalProblem-UnrecognizedOperation) without invokeId!
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.RejectComponent);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Rejecting an Invoke with a bad Parameter (decoding error)
	 *
	 * <pre>
	 * TC-BEGIN + Invoke(bad opCode==1000)
	 * TC-END + Reject (generalProblem-MistypedParameter) without invokeId!
	 * </pre>
	 */
	@Test
	public void testInvokeMistypedParameter() throws Exception {
		client.stop();
		server.stop();

		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
				try {
					InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
					assertEquals(invokeProblemType, InvokeProblemType.MistypedParameter);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(isLocalOriginated);
				assertEquals((long) invokeId, 10L);
			}
		};

		server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);
				try {
					InvokeProblemType invokeProblemType = problem.getInvokeProblemType();
					assertEquals(invokeProblemType, InvokeProblemType.MistypedParameter);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertTrue(isLocalOriginated);
				assertEquals((long) invokeId, 10L);
			}
		};

		// 1. TC-BEGIN + Invoke(bad opCode==1000)
		client.sendMystypedParameter();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest procUnstrReqInd = (ProcessUnstructuredSSRequest) event.getEvent();

			String ussdString = procUnstrReqInd.getUSSDString().getString(null);
			assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + Reject (generalProblem-MistypedParameter) without invokeId!
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.RejectComponent);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + sendRoutingInfoForSMRequest + reportSMDeliveryStatusRequest
	 * TC-CONTINUE sendRoutingInfoForSMResponse + sendRoutingInfoForSMResponse for the same InvokeId + SystemFailureError for the same InvokeId
	 * TC-END + Reject(ReturnResultProblemType.UnrecognizedInvokeID) + Reject(ReturnErrorProblemType.UnrecognizedInvokeID)
	 * </pre>
	 */
	@Test
	public void testUnrecognizedInvokeID() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			private AtomicInteger stepRej = new AtomicInteger();

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

				stepRej.incrementAndGet();

				try {
					if (stepRej.get() == 1) {
						assertEquals(problem.getType(), ProblemType.ReturnResult);
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.UnrecognizedInvokeID);
					} else {
						assertEquals(problem.getType(), ProblemType.ReturnError);
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.UnrecognizedInvokeID);
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertTrue(isLocalOriginated);
			}
		};

		AtomicInteger serverInvokeId = new AtomicInteger();
		server = new Server(this.stack2, peer2Address, peer1Address) {
			private AtomicInteger stepRej = new AtomicInteger();

			@Override
			public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest ind) {
				super.onSendRoutingInfoForSMRequest(ind);
				serverInvokeId.set(ind.getInvokeId());
			}

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

				stepRej.incrementAndGet();

				assertEquals((long) invokeId, serverInvokeId.get());
				try {
					if (stepRej.get() == 1) {
						assertEquals(problem.getType(), ProblemType.ReturnResult);
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.UnrecognizedInvokeID);
					} else {
						assertEquals(problem.getType(), ProblemType.ReturnError);
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.UnrecognizedInvokeID);
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(isLocalOriginated);
			}
		};

		// 1. TC-BEGIN + sendRoutingInfoForSMRequest + reportSMDeliveryStatusRequest
		client.send_sendRoutingInfoForSMRequest_reportSMDeliveryStatusRequest();
		client.awaitSent(EventType.SendRoutingInfoForSMIndication);
		client.awaitSent(EventType.ReportSMDeliveryStatusIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendRoutingInfoForSMIndication);
		server.awaitReceived(EventType.ReportSMDeliveryStatusIndication);
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			MAPDialogSms clientDialogSms = (MAPDialogSms) mapDialog;
			MAPParameterFactory paramFactory = server.mapParameterFactory;

			ISDNAddressString msisdn = paramFactory.createISDNAddressString(AddressNature.international_number,
					NumberingPlan.ISDN, "11223344");

			IMSI imsi = paramFactory.createIMSI("777222");
			LocationInfoWithLMSI locationInfoWithLMSI = paramFactory.createLocationInfoWithLMSI(msisdn, null, null,
					false, null);
			clientDialogSms.addSendRoutingInfoForSMResponse(serverInvokeId.get(), imsi, locationInfoWithLMSI, null,
					null, null);

			server.handleSent(EventType.SendRoutingInfoForSMRespIndication, null);

			imsi = paramFactory.createIMSI("777222222");
			clientDialogSms.addSendRoutingInfoForSMResponse(serverInvokeId.get(), imsi, locationInfoWithLMSI, null,
					null, null);

			server.handleSent(EventType.SendRoutingInfoForSMRespIndication, null);

			MAPErrorMessage mapErrorMessage = server.mapErrorMessageFactory
					.createMAPErrorMessageSystemFailure(NetworkResource.hlr, null, null);
			clientDialogSms.sendErrorComponent(serverInvokeId.get(), mapErrorMessage);

			server.handleSent(EventType.ErrorComponent, null);

			mapDialog.send(dummyCallback);
		}

		// 2. TC-CONTINUE + ...
		server.awaitSent(EventType.SendRoutingInfoForSMRespIndication);
		server.awaitSent(EventType.SendRoutingInfoForSMRespIndication);
		server.awaitSent(EventType.ErrorComponent);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendRoutingInfoForSMRespIndication);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			mapDialog.close(false, dummyCallback);
		}

		// 3. TC-END + ...
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendRoutingInfoForSMIndication);
		clientExpected.addSent(EventType.ReportSMDeliveryStatusIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendRoutingInfoForSMRespIndication);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendRoutingInfoForSMIndication);
		serverExpected.addReceived(EventType.ReportSMDeliveryStatusIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendRoutingInfoForSMRespIndication);
		serverExpected.addSent(EventType.SendRoutingInfoForSMRespIndication);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addReceived(EventType.RejectComponent);
		serverExpected.addReceived(EventType.RejectComponent);
		serverExpected.addReceived(EventType.DialogClose);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Rejecting:
	 * <p>
	 * - an ReturtResult with a bad Parameter (decoding error)
	 * ReturtResultProblem.MistypedParameter
	 * <p>
	 * - an ReturtError with a bad Parameter (decoding error)
	 * ReturtErrorProblem.MistypedParameter
	 * <p>
	 * - an ReturtError with a bad code ReturtErrorProblem.UnrecognizedError
	 *
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest + addProcessUnstructuredSSRequest + addProcessUnstructuredSSRequest 
	 * TC-CONTINUE + ReturnResultLast with a bad Parameter + ReturnError with a bad Parameter + ReturnError with a bad errorCode (=1000)
	 * TC-END + Reject (ReturnResultProblem.MistypedParameter) + Reject (ReturnErrorProblem.MistypedParameter) + Reject (ReturnErrorProblem.UnrecognizedError)
	 * </pre>
	 */
	@Test
	public void testResultErrorMistypedParameter() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			private AtomicInteger rejectStep = new AtomicInteger();

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

				try {
					switch (rejectStep.incrementAndGet()) {
					case 1:
						assertEquals(problem.getReturnResultProblemType(), ReturnResultProblemType.MistypedParameter);
						assertTrue(isLocalOriginated);
						assertEquals((long) invokeId, 0L);
						break;
					case 2:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.MistypedParameter);
						assertTrue(isLocalOriginated);
						assertEquals((long) invokeId, 1L);
						break;
					case 3:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.UnrecognizedError);
						assertTrue(isLocalOriginated);
						assertEquals((long) invokeId, 2L);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}
		};

		final int[] serverInvokeIds = new int[3];
		server = new Server(stack2, peer2Address, peer1Address) {
			private AtomicInteger step = new AtomicInteger();
			private AtomicInteger rejectStep = new AtomicInteger();

			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);

				try {
					String ussdString = procUnstrReqInd.getUSSDString().getString(null);
					assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
				} catch (MAPException e) {
					fail("Error while trying to add Duplicate InvokeId Component");
				}

				switch (step.incrementAndGet()) {
				case 1:
					serverInvokeIds[0] = procUnstrReqInd.getInvokeId();
					break;
				case 2:
					serverInvokeIds[1] = procUnstrReqInd.getInvokeId();
					break;
				case 3:
					serverInvokeIds[2] = procUnstrReqInd.getInvokeId();
					break;
				}
			}

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

				try {
					switch (rejectStep.incrementAndGet()) {
					case 1:
						assertEquals(problem.getReturnResultProblemType(), ReturnResultProblemType.MistypedParameter);
						assertFalse(isLocalOriginated);
						assertEquals((long) invokeId, serverInvokeIds[0]);
						break;
					case 2:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.MistypedParameter);
						assertFalse(isLocalOriginated);
						assertEquals((long) invokeId, serverInvokeIds[1]);
						break;
					case 3:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.UnrecognizedError);
						assertFalse(isLocalOriginated);
						assertEquals((long) invokeId, serverInvokeIds[2]);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}
		};

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest +
		// addProcessUnstructuredSSRequest + addProcessUnstructuredSSRequest
		client.actionAAA();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			ASNOctetString octetString = new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 1, 1, 1, 1, 1 }), null,
					null, null, false);
			((MAPDialogImpl) mapDialog).getTcapDialog().sendData(serverInvokeIds[0], null, null, null,
					TcapFactory.createLocalOperationCode(MAPOperationCode.processUnstructuredSS_Request), octetString,
					false, true);

			octetString = new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 1, 1, 1, 1, 1 }), null, null, null,
					false);
			((MAPDialogImpl) mapDialog).getTcapDialog().sendError(serverInvokeIds[1],
					TcapFactory.createLocalErrorCode(MAPErrorCode.systemFailure), octetString);

			((MAPDialogImpl) mapDialog).getTcapDialog().sendError(serverInvokeIds[2],
					TcapFactory.createLocalErrorCode(1000), null);

			server.handleSent(EventType.ErrorComponent, null);
			server.handleSent(EventType.ErrorComponent, null);

			mapDialog.send(dummyCallback);
		}

		// 2. TC-CONTINUE + ReturnResultLast + ReturnError + ReturnError
		server.awaitSent(EventType.ErrorComponent);
		server.awaitSent(EventType.ErrorComponent);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			mapDialog.close(false, dummyCallback);
		}

		// 3. TC-END + Reject + Reject + Reject
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addReceived(EventType.RejectComponent);
		serverExpected.addReceived(EventType.RejectComponent);
		serverExpected.addReceived(EventType.RejectComponent);
		serverExpected.addReceived(EventType.DialogClose);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest (releasing Dialog at a client side) 
	 * TC-CONTINUE addProcessUnstructuredSSResponse
	 * TC-ABORT (UnrecognizedTxID)
	 * </pre>
	 */
	@Test
	public void testSupportingDialogueTransactionReleased() throws Exception {
		server.stop();

		server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
					MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
				super.onDialogProviderAbort(mapDialog, abortProviderReason, abortSource, extensionContainer);

				assertEquals(abortProviderReason, MAPAbortProviderReason.SupportingDialogueTransactionReleased);
				assertEquals(abortSource, MAPAbortSource.TCProblem);
			}
		};

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest (releasing Dialog at a client
		// side)
		client.actionA();
		client.clientDialog.release();

		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		int invokeId = Integer.MIN_VALUE;

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProcessUnstructuredSSRequestIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProcessUnstructuredSSRequestIndication);
			ProcessUnstructuredSSRequest unstrResInd = (ProcessUnstructuredSSRequest) event.getEvent();

			invokeId = unstrResInd.getInvokeId();
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			USSDString ussdStringObj = server.mapParameterFactory
					.createUSSDString(MAPFunctionalTest.USSD_FINAL_RESPONSE);
			MAPDialogSupplementary mapDialogSupp = (MAPDialogSupplementary) mapDialog;
			mapDialogSupp.addProcessUnstructuredSSResponse(invokeId, new CBSDataCodingSchemeImpl(0x0f), ussdStringObj);

			server.handleSent(EventType.ProcessUnstructuredSSResponseIndication, null);
			mapDialog.send(dummyCallback);
		}

		// 2. TC-CONTINUE + addProcessUnstructuredSSResponse
		server.awaitSent(EventType.ProcessUnstructuredSSResponseIndication);

		// 3. TC-ABORT (UnrecognizedTxID)
		// asserts performed in listener above
		server.awaitReceived(EventType.DialogProviderAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProcessUnstructuredSSRequestIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ProcessUnstructuredSSResponseIndication);
		serverExpected.addReceived(EventType.DialogProviderAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + addProcessUnstructuredSSRequest (bad sccp address + setReturnMessageOnError) 
	 * TC-NOTICE
	 * </pre>
	 */
	@Test
	public void testTcNotice() throws Exception {
		client.stop();

		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
					ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
				super.onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);

				assertEquals(refuseReason, MAPRefuseReason.RemoteNodeNotReachable);
			}
		};

		// 1. TC-BEGIN + addProcessUnstructuredSSRequest (bad sccp address +
		// setReturnMessageOnError)
		client.actionB();
		client.awaitSent(EventType.ProcessUnstructuredSSRequestIndication);

		// 2. TC-NOTICE
		client.awaitReceived(EventType.DialogReject);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProcessUnstructuredSSRequestIndication);
		clientExpected.addReceived(EventType.DialogReject);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Tests for MAP V1 Dialogs
	 * 
	 */

	/**
	 * Action_V1_A
	 * 
	 * <pre>
	 * TC-BEGIN + INVOKE(opCode=47)
	 * TC-END+RRL(opCode=47) (47=reportSM-DeliveryStatus)
	 * </pre>
	 */
	@Test
	public void testV1ReportSMDeliveryStatus() throws Exception {
		// 1. TC-BEGIN + INVOKE(opCode=47)
		client.sendReportSMDeliveryStatusV1();
		client.awaitSent(EventType.ReportSMDeliveryStatusIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ReportSMDeliveryStatusIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ReportSMDeliveryStatusIndication);
			ReportSMDeliveryStatusRequest reportSMDeliveryStatusInd = (ReportSMDeliveryStatusRequest) event.getEvent();

			MAPDialogSms d = reportSMDeliveryStatusInd.getMAPDialog();

			ISDNAddressString msisdn = reportSMDeliveryStatusInd.getMsisdn();
			AddressString sca = reportSMDeliveryStatusInd.getServiceCentreAddress();
			SMDeliveryOutcome sMDeliveryOutcome = reportSMDeliveryStatusInd.getSMDeliveryOutcome();
			Integer absentSubscriberDiagnosticSM = reportSMDeliveryStatusInd.getAbsentSubscriberDiagnosticSM();
			MAPExtensionContainer extensionContainer = reportSMDeliveryStatusInd.getExtensionContainer();
			Boolean gprsSupportIndicator = reportSMDeliveryStatusInd.getGprsSupportIndicator();
			Boolean deliveryOutcomeIndicator = reportSMDeliveryStatusInd.getDeliveryOutcomeIndicator();
			SMDeliveryOutcome additionalSMDeliveryOutcome = reportSMDeliveryStatusInd.getAdditionalSMDeliveryOutcome();
			Integer additionalAbsentSubscriberDiagnosticSM = reportSMDeliveryStatusInd
					.getAdditionalAbsentSubscriberDiagnosticSM();

			assertNotNull(msisdn);
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(msisdn.getAddress(), "111222333");
			assertNotNull(sca);
			assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
			assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
			assertEquals(sca.getAddress(), "999000");
			assertNull(sMDeliveryOutcome);
			assertNull(absentSubscriberDiagnosticSM);
			assertFalse(gprsSupportIndicator);
			assertFalse(deliveryOutcomeIndicator);
			assertNull(additionalSMDeliveryOutcome);
			assertNull(additionalAbsentSubscriberDiagnosticSM);
			assertNull(extensionContainer);

			d.addReportSMDeliveryStatusResponse(reportSMDeliveryStatusInd.getInvokeId(), null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			// sending request and closing dialog
			server.handleSent(EventType.ReportSMDeliveryStatusRespIndication, null);
			mapDialog.close(false, dummyCallback);
		}

		client.awaitReceived(EventType.DialogAccept);

		// 2. TC-END+RRL(opCode=47) (47=reportSM-DeliveryStatus)
		server.awaitSent(EventType.ReportSMDeliveryStatusRespIndication);
		client.awaitReceived(EventType.ReportSMDeliveryStatusRespIndication);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ReportSMDeliveryStatusIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ReportSMDeliveryStatusRespIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ReportSMDeliveryStatusIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ReportSMDeliveryStatusRespIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Action_V1_B
	 * 
	 * <pre>
	 * TC-BEGIN + INVOKE(opCode=49) -> release()
	 * </pre>
	 */
	@Test
	public void testV1AlertServiceCentreRequest() throws Exception {
		// 1. TC-BEGIN + INVOKE(opCode=49)
		client.sendAlertServiceCentreRequestV1();
		client.awaitSent(EventType.AlertServiceCentreIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.AlertServiceCentreIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.AlertServiceCentreIndication);
			AlertServiceCentreRequest alertServiceCentreInd = (AlertServiceCentreRequest) event.getEvent();

			MAPDialogSms d = alertServiceCentreInd.getMAPDialog();

			ISDNAddressString msisdn = alertServiceCentreInd.getMsisdn();
			AddressString serviceCentreAddress = alertServiceCentreInd.getServiceCentreAddress();

			assertNotNull(msisdn);
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(msisdn.getAddress(), "111222333");
			assertNotNull(serviceCentreAddress);
			assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.subscriber_number);
			assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.national);
			assertEquals(serviceCentreAddress.getAddress(), "0011");

			if (d.getApplicationContext().getApplicationContextVersion() == MAPApplicationContextVersion.version1)
				d.processInvokeWithoutAnswer(alertServiceCentreInd.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			// release()
			mapDialog.release();
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.AlertServiceCentreIndication);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.AlertServiceCentreIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Action_V1_C
	 * 
	 * <pre>
	 * TC-BEGIN (empty - no components) -> TC-ABORT V1
	 * </pre>
	 */
	@Test
	public void testV1AlertServiceCentreRequestReject() throws Exception {
		client.stop();

		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
					ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
				super.onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);

				assertNotNull(refuseReason);
				assertEquals(refuseReason, MAPRefuseReason.NoReasonGiven);
				assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
			}
		};

		// 1. TC-BEGIN (empty - no components)
		client.sendEmptyV1Request();

		// 2. TC-ABORT V1
		client.awaitReceived(EventType.DialogReject);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addReceived(EventType.DialogReject);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Action_V1_D
	 * 
	 * <pre>
	 * TC-BEGIN (unsupported opCode) -> TC-ABORT V1
	 * </pre>
	 */
	@Test
	public void testV1AlertServiceCentreRequestReject2() throws Exception {
		client.stop();

		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason,
					ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
				super.onDialogReject(mapDialog, refuseReason, alternativeApplicationContext, extensionContainer);

				assertNotNull(refuseReason);
				assertEquals(refuseReason, MAPRefuseReason.NoReasonGiven);
				assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
			}
		};

		// 1. TC-BEGIN (unsupported opCode)
		client.sendV1BadOperationCode();
		client.awaitSent(EventType.AlertServiceCentreIndication);

		// 2. TC-ABORT V1
		client.awaitReceived(EventType.DialogReject);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.AlertServiceCentreIndication);
		clientExpected.addReceived(EventType.DialogReject);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Action_V1_E
	 * 
	 * <pre>
	 * TC-BEGIN + INVOKE (opCode=46)
	 * TC-CONTINUE (empty)
	 * TC-ABORT (UserReason)(-> Abort V1)
	 * </pre>
	 */
	@Test
	public void testV1ForwardShortMessageRequest() throws Exception {
		server.stop();

		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason,
					MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
				super.onDialogProviderAbort(mapDialog, abortProviderReason, abortSource, extensionContainer);

				assertEquals(abortProviderReason, MAPAbortProviderReason.AbnormalMAPDialogueLocal);
				assertEquals(mapDialog.getTCAPMessageType(), MessageType.Abort);
			}
		};

		// 1. TC-BEGIN + INVOKE (opCode=46)
		client.sendForwardShortMessageRequestV1();
		client.awaitSent(EventType.ForwardShortMessageIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ForwardShortMessageIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ForwardShortMessageIndication);
			ForwardShortMessageRequest forwSmInd = (ForwardShortMessageRequest) event.getEvent();

			SM_RP_DA sm_RP_DA = forwSmInd.getSM_RP_DA();
			SM_RP_OA sm_RP_OA = forwSmInd.getSM_RP_OA();
			SmsSignalInfo sm_RP_UI = forwSmInd.getSM_RP_UI();

			assertNotNull(sm_RP_DA);
			assertNotNull(sm_RP_DA.getIMSI());
			assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
			assertNotNull(sm_RP_OA);
			assertNotNull(sm_RP_OA.getMsisdn());
			assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
			assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
			assertNotNull(sm_RP_UI);
			ByteBuf translatedValue = Unpooled.buffer();

			sm_RP_UI.decodeTpdu(false).encodeData(translatedValue);

			assertTrue(ByteBufUtil.equals(translatedValue,
					Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16,
							17, 33, 23, 5, 0, 3, -21, 2, 1, -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10,
							-123, 0 })));
			assertFalse(forwSmInd.getMoreMessagesToSend());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			mapDialog.send(dummyCallback);
		}

		// 2. TC-CONTINUE (empty)
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			MAPUserAbortChoiseImpl choice = new MAPUserAbortChoiseImpl();
			choice.setProcedureCancellationReason(ProcedureCancellationReason.handoverCancellation);
			client.handleSent(EventType.DialogUserAbort, null);
			mapDialog.abort(choice, dummyCallback);
		}

		// 3. TC-ABORT (UserReason)(-> Abort V1)
		client.awaitSent(EventType.DialogUserAbort);
		server.awaitReceived(EventType.DialogProviderAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ForwardShortMessageIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.DialogUserAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ForwardShortMessageIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogProviderAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Below are test from testSmsService
	 */

	/**
	 * <pre>
	 * TC-BEGIN + AlertServiceCentreRequest
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testV2AlertServiceCentreRequest() throws Exception {
		// 1. TC-BEGIN + AlertServiceCentreRequest
		client.sendAlertServiceCentreRequestV2();
		client.awaitSent(EventType.AlertServiceCentreIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.AlertServiceCentreIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.AlertServiceCentreIndication);
			AlertServiceCentreRequest alertServiceCentreInd = (AlertServiceCentreRequest) event.getEvent();

			MAPDialogSms d = alertServiceCentreInd.getMAPDialog();

			ISDNAddressString msisdn = alertServiceCentreInd.getMsisdn();
			AddressString serviceCentreAddress = alertServiceCentreInd.getServiceCentreAddress();

			assertNotNull(msisdn);
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(msisdn.getAddress(), "111222333");
			assertNotNull(serviceCentreAddress);
			assertEquals(serviceCentreAddress.getAddressNature(), AddressNature.subscriber_number);
			assertEquals(serviceCentreAddress.getNumberingPlan(), NumberingPlan.national);
			assertEquals(serviceCentreAddress.getAddress(), "0011");

			d.addAlertServiceCentreResponse(alertServiceCentreInd.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.AlertServiceCentreRespIndication, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END
		server.awaitSent(EventType.AlertServiceCentreRespIndication);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.AlertServiceCentreRespIndication);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.AlertServiceCentreIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.AlertServiceCentreRespIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.AlertServiceCentreIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.AlertServiceCentreRespIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + ForwardSMRequest_V2
	 * TC-END + ForwardSMResponse_V2
	 * </pre>
	 */
	@Test
	public void testV2ForwardShortMessageRequest() throws Exception {
		// 1. TC-BEGIN + ForwardSMRequest_V2
		client.sendForwardShortMessageRequestV2();
		client.awaitSent(EventType.ForwardShortMessageIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ForwardShortMessageIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ForwardShortMessageIndication);
			ForwardShortMessageRequest forwSmInd = (ForwardShortMessageRequest) event.getEvent();

			MAPDialogSms d = forwSmInd.getMAPDialog();

			SM_RP_DA sm_RP_DA = forwSmInd.getSM_RP_DA();
			SM_RP_OA sm_RP_OA = forwSmInd.getSM_RP_OA();
			SmsSignalInfo sm_RP_UI = forwSmInd.getSM_RP_UI();

			assertNotNull(sm_RP_DA);
			assertNotNull(sm_RP_DA.getIMSI());
			assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
			assertNotNull(sm_RP_OA);
			assertNotNull(sm_RP_OA.getMsisdn());
			assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
			assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
			assertNotNull(sm_RP_UI);

			ByteBuf translatedValue = Unpooled.buffer();
			sm_RP_UI.decodeTpdu(false).encodeData(translatedValue);

			assertTrue(ByteBufUtil.equals(translatedValue,
					Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16,
							17, 33, 23, 5, 0, 3, -21, 2, 1, -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10,
							-123, 0 })));
			assertTrue(forwSmInd.getMoreMessagesToSend());
			d.addForwardShortMessageResponse(forwSmInd.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ForwardShortMessageRespIndication, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + ForwardSMResponse_V2
		server.awaitSent(EventType.ForwardShortMessageRespIndication);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ForwardShortMessageRespIndication);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ForwardShortMessageIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ForwardShortMessageRespIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ForwardShortMessageIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ForwardShortMessageRespIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + MoForwardSMRequest
	 * TC-END + MoForwardSMResponse
	 * </pre>
	 */
	@Test
	public void testMoForwardShortMessageRequest() throws Exception {
		// 1. TC-BEGIN + MoForwardSMRequest
		client.sendMoForwardShortMessageRequest();
		client.awaitSent(EventType.MoForwardShortMessageIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.MoForwardShortMessageIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.MoForwardShortMessageIndication);
			MoForwardShortMessageRequest moForwSmInd = (MoForwardShortMessageRequest) event.getEvent();

			MAPDialogSms d = moForwSmInd.getMAPDialog();

			SM_RP_DA sm_RP_DA = moForwSmInd.getSM_RP_DA();
			SM_RP_OA sm_RP_OA = moForwSmInd.getSM_RP_OA();
			SmsSignalInfo sm_RP_UI = moForwSmInd.getSM_RP_UI();
			MAPExtensionContainer extensionContainer = moForwSmInd.getExtensionContainer();
			IMSI imsi2 = moForwSmInd.getIMSI();

			assertNotNull(sm_RP_DA);
			assertNotNull(sm_RP_DA.getIMSI());
			assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
			assertNotNull(sm_RP_OA);
			assertNotNull(sm_RP_OA.getMsisdn());
			assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
			assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
			assertNotNull(sm_RP_UI);

			SmsSubmitTpduImpl tpdu = (SmsSubmitTpduImpl) sm_RP_UI.decodeTpdu(true);
			tpdu.getUserData().decode();
			assertFalse(tpdu.getRejectDuplicates());
			assertTrue(tpdu.getReplyPathExists());
			assertFalse(tpdu.getStatusReportRequest());
			assertEquals(tpdu.getMessageReference(), 55);
			assertEquals(tpdu.getDestinationAddress().getTypeOfNumber(), TypeOfNumber.InternationalNumber);
			assertEquals(tpdu.getDestinationAddress().getNumberingPlanIdentification(),
					NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
			assertTrue(tpdu.getDestinationAddress().getAddressValue().equals("700007"));
			assertEquals(tpdu.getProtocolIdentifier().getCode(), 0);
			assertEquals((int) tpdu.getValidityPeriod().getRelativeFormatValue(), 100);
			assertEquals(tpdu.getUserData().getDataCodingScheme().getCode(), 0);
			assertTrue(tpdu.getUserData().getDecodedMessage().equals("Hello, world !!!"));

			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
			assertNotNull(imsi2);
			assertEquals(imsi2.getData(), "25007123456789");

			SmsSignalInfoImpl sm_RP_UI2 = new SmsSignalInfoImpl(SmsTpduImpl.createInstance(Unpooled.wrappedBuffer(
					new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3,
							-21, 2, 1, -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 }),
					false, null), null);

			d.addMoForwardShortMessageResponse(moForwSmInd.getInvokeId(), sm_RP_UI2,
					MAPExtensionContainerTest.GetTestExtensionContainer());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.MoForwardShortMessageRespIndication, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + MoForwardSMResponse
		server.awaitSent(EventType.MoForwardShortMessageRespIndication);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.MoForwardShortMessageRespIndication);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.MoForwardShortMessageRespIndication);
			MoForwardShortMessageResponse moForwSmRespInd = (MoForwardShortMessageResponse) event.getEvent();

			SmsSignalInfo sm_RP_UI = moForwSmRespInd.getSM_RP_UI();
			MAPExtensionContainer extensionContainer = moForwSmRespInd.getExtensionContainer();

			assertNotNull(sm_RP_UI);
			ByteBuf translatedValue = Unpooled.buffer();
			sm_RP_UI.decodeTpdu(false).encodeData(translatedValue);

			assertTrue(ByteBufUtil.equals(translatedValue,
					Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16,
							17, 33, 23, 5, 0, 3, -21, 2, 1, -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10,
							-123, 0 })));
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.MoForwardShortMessageIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.MoForwardShortMessageRespIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.MoForwardShortMessageIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.MoForwardShortMessageRespIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + MtForwardSMRequest
	 * TC-END + MtForwardSMResponse
	 * </pre>
	 */
	@Test
	public void testMtForwardShortMessageRequest() throws Exception {
		// 1. TC-BEGIN + MtForwardSMRequest
		client.sendMtForwardShortMessageRequest();
		client.awaitSent(EventType.MtForwardShortMessageIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.MtForwardShortMessageIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.MtForwardShortMessageIndication);
			MtForwardShortMessageRequest mtForwSmInd = (MtForwardShortMessageRequest) event.getEvent();

			MAPDialogSms d = mtForwSmInd.getMAPDialog();

			SM_RP_DA sm_RP_DA = mtForwSmInd.getSM_RP_DA();
			SM_RP_OA sm_RP_OA = mtForwSmInd.getSM_RP_OA();
			SmsSignalInfo sm_RP_UI = mtForwSmInd.getSM_RP_UI();
			MAPExtensionContainer extensionContainer = mtForwSmInd.getExtensionContainer();
			Boolean moreMessagesToSend = mtForwSmInd.getMoreMessagesToSend();

			assertNotNull(sm_RP_DA);
			assertNotNull(sm_RP_DA.getLMSI());
			assertTrue(ByteBufUtil.equals(sm_RP_DA.getLMSI().getValue(),
					Unpooled.wrappedBuffer(new byte[] { 49, 48, 47, 46 })));
			assertNotNull(sm_RP_OA);
			assertNotNull(sm_RP_OA.getServiceCentreAddressOA());
			assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
			assertEquals(sm_RP_OA.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(sm_RP_OA.getServiceCentreAddressOA().getAddress(), "111222333");
			assertNotNull(sm_RP_UI);

			ByteBuf translatedValue = Unpooled.buffer();
			sm_RP_UI.decodeTpdu(false).encodeData(translatedValue);

			assertTrue(ByteBufUtil.equals(translatedValue,
					Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16,
							17, 33, 23, 5, 0, 3, -21, 2, 1, -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10,
							-123, 0 })));
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
			assertTrue(moreMessagesToSend);

			SmsSignalInfoImpl sm_RP_UI2 = new SmsSignalInfoImpl(SmsTpduImpl.createInstance(Unpooled.wrappedBuffer(
					new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3,
							-21, 2, 1, -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 }),
					false, null), null);

			d.addMtForwardShortMessageResponse(mtForwSmInd.getInvokeId(), sm_RP_UI2,
					MAPExtensionContainerTest.GetTestExtensionContainer());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.MtForwardShortMessageRespIndication, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + MtForwardSMResponse
		server.awaitSent(EventType.MtForwardShortMessageRespIndication);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.MtForwardShortMessageRespIndication);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.MtForwardShortMessageRespIndication);
			MtForwardShortMessageResponse mtForwSmRespInd = (MtForwardShortMessageResponse) event.getEvent();

			SmsSignalInfo sm_RP_UI = mtForwSmRespInd.getSM_RP_UI();
			MAPExtensionContainer extensionContainer = mtForwSmRespInd.getExtensionContainer();

			assertNotNull(sm_RP_UI);
			ByteBuf translatedValue = Unpooled.buffer();
			sm_RP_UI.decodeTpdu(false).encodeData(translatedValue);

			assertTrue(ByteBufUtil.equals(translatedValue,
					Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16,
							17, 33, 23, 5, 0, 3, -21, 2, 1, -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10,
							-123, 0 })));
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.MtForwardShortMessageIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.MtForwardShortMessageRespIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.MtForwardShortMessageIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.MtForwardShortMessageRespIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * MAP V3
	 * 
	 * <pre>
	 * TC-BEGIN + ReportSMDeliveryStatusRequest 
	 * TC-END + ReportSMDeliveryStatusResponse
	 * </pre>
	 */
	@Test
	public void testReportSMDeliveryStatusRequestV3() throws Exception {
		// 1. TC-BEGIN + ReportSMDeliveryStatusRequest
		client.sendReportSMDeliveryStatus3();
		client.awaitSent(EventType.ReportSMDeliveryStatusIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ReportSMDeliveryStatusIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ReportSMDeliveryStatusIndication);
			ReportSMDeliveryStatusRequest request = (ReportSMDeliveryStatusRequest) event.getEvent();
			MAPDialogSms d = request.getMAPDialog();

			ISDNAddressString msisdn = request.getMsisdn();
			AddressString sca = request.getServiceCentreAddress();
			SMDeliveryOutcome sMDeliveryOutcome = request.getSMDeliveryOutcome();
			Integer absentSubscriberDiagnosticSM = request.getAbsentSubscriberDiagnosticSM();
			MAPExtensionContainer extensionContainer = request.getExtensionContainer();
			Boolean gprsSupportIndicator = request.getGprsSupportIndicator();
			Boolean deliveryOutcomeIndicator = request.getDeliveryOutcomeIndicator();
			SMDeliveryOutcome additionalSMDeliveryOutcome = request.getAdditionalSMDeliveryOutcome();
			Integer additionalAbsentSubscriberDiagnosticSM = request.getAdditionalAbsentSubscriberDiagnosticSM();

			assertNotNull(msisdn);
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(msisdn.getAddress(), "111222333");
			assertNotNull(sca);
			assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
			assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
			assertEquals(sca.getAddress(), "999000");
			assertEquals(sMDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
			assertNotNull(absentSubscriberDiagnosticSM);
			assertEquals((int) absentSubscriberDiagnosticSM, 555);
			assertTrue(gprsSupportIndicator);
			assertTrue(deliveryOutcomeIndicator);
			assertEquals(additionalSMDeliveryOutcome, SMDeliveryOutcome.successfulTransfer);
			assertNotNull(additionalAbsentSubscriberDiagnosticSM);
			assertEquals((int) additionalAbsentSubscriberDiagnosticSM, 444);
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

			ISDNAddressString storedMSISDN = server.mapParameterFactory.createISDNAddressString(
					AddressNature.network_specific_number, NumberingPlan.national, "111000111");

			d.addReportSMDeliveryStatusResponse(request.getInvokeId(), storedMSISDN,
					MAPExtensionContainerTest.GetTestExtensionContainer());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ReportSMDeliveryStatusRespIndication, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + ReportSMDeliveryStatusResponse
		server.awaitSent(EventType.ReportSMDeliveryStatusRespIndication);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ReportSMDeliveryStatusRespIndication);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ReportSMDeliveryStatusRespIndication);
			ReportSMDeliveryStatusResponse response = (ReportSMDeliveryStatusResponse) event.getEvent();

			ISDNAddressString storedMSISDN = response.getStoredMSISDN();
			MAPExtensionContainer extensionContainer = response.getExtensionContainer();

			assertNotNull(storedMSISDN);
			assertEquals(storedMSISDN.getAddressNature(), AddressNature.network_specific_number);
			assertEquals(storedMSISDN.getNumberingPlan(), NumberingPlan.national);
			assertEquals(storedMSISDN.getAddress(), "111000111");
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
		}

		client.awaitReceived(EventType.DialogClose);
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ReportSMDeliveryStatusIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ReportSMDeliveryStatusRespIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ReportSMDeliveryStatusIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ReportSMDeliveryStatusRespIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * MAP V2
	 * 
	 * <pre>
	 * TC-BEGIN + ReportSMDeliveryStatusRequest
	 * TC-END + ReportSMDeliveryStatusResponse
	 * </pre>
	 */
	@Test
	public void testReportSMDeliveryStatusRequestV2() throws Exception {
		// 1. TC-BEGIN + ReportSMDeliveryStatusRequest
		client.sendReportSMDeliveryStatus2();
		client.awaitSent(EventType.ReportSMDeliveryStatusIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ReportSMDeliveryStatusIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ReportSMDeliveryStatusIndication);
			ReportSMDeliveryStatusRequest request = (ReportSMDeliveryStatusRequest) event.getEvent();
			MAPDialogSms d = request.getMAPDialog();

			ISDNAddressString msisdn = request.getMsisdn();
			AddressString sca = request.getServiceCentreAddress();
			SMDeliveryOutcome sMDeliveryOutcome = request.getSMDeliveryOutcome();
			Integer absentSubscriberDiagnosticSM = request.getAbsentSubscriberDiagnosticSM();
			MAPExtensionContainer extensionContainer = request.getExtensionContainer();
			boolean gprsSupportIndicator = request.getGprsSupportIndicator();
			boolean deliveryOutcomeIndicator = request.getDeliveryOutcomeIndicator();
			SMDeliveryOutcome additionalSMDeliveryOutcome = request.getAdditionalSMDeliveryOutcome();
			Integer additionalAbsentSubscriberDiagnosticSM = request.getAdditionalAbsentSubscriberDiagnosticSM();

			assertNotNull(msisdn);
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(msisdn.getAddress(), "111222333");
			assertNotNull(sca);
			assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
			assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
			assertEquals(sca.getAddress(), "999000");
			assertEquals(sMDeliveryOutcome, SMDeliveryOutcome.absentSubscriber);
			assertNull(absentSubscriberDiagnosticSM);
			assertFalse(gprsSupportIndicator);
			assertFalse(deliveryOutcomeIndicator);
			assertNull(additionalSMDeliveryOutcome);
			assertNull(additionalAbsentSubscriberDiagnosticSM);
			assertNull(extensionContainer);

			ISDNAddressString storedMSISDN = server.mapParameterFactory.createISDNAddressString(
					AddressNature.network_specific_number, NumberingPlan.national, "111000111");

			d.addReportSMDeliveryStatusResponse(request.getInvokeId(), storedMSISDN);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ReportSMDeliveryStatusRespIndication, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + ReportSMDeliveryStatusResponse
		server.awaitSent(EventType.ReportSMDeliveryStatusRespIndication);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ReportSMDeliveryStatusRespIndication);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ReportSMDeliveryStatusRespIndication);
			ReportSMDeliveryStatusResponse response = (ReportSMDeliveryStatusResponse) event.getEvent();

			ISDNAddressString storedMSISDN = response.getStoredMSISDN();
			MAPExtensionContainer extensionContainer = response.getExtensionContainer();

			assertNotNull(storedMSISDN);
			assertEquals(storedMSISDN.getAddressNature(), AddressNature.network_specific_number);
			assertEquals(storedMSISDN.getNumberingPlan(), NumberingPlan.national);
			assertEquals(storedMSISDN.getAddress(), "111000111");
			assertNull(extensionContainer);
		}

		client.awaitReceived(EventType.DialogClose);
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ReportSMDeliveryStatusIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ReportSMDeliveryStatusRespIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ReportSMDeliveryStatusIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ReportSMDeliveryStatusRespIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + SendRoutingInfoForSMRequest 
	 * TC-END + SendRoutingInfoForSMResponse + InformServiceCentreRequest
	 * </pre>
	 */
	@Test
	public void testSendRoutingInfoForSM() throws Exception {
		// 1. TC-BEGIN + SendRoutingInfoForSMRequest
		client.sendSendRoutingInfoForSM();
		client.awaitSent(EventType.SendRoutingInfoForSMIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendRoutingInfoForSMIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendRoutingInfoForSMIndication);
			SendRoutingInfoForSMRequest request = (SendRoutingInfoForSMRequest) event.getEvent();
			MAPDialogSms d = request.getMAPDialog();

			ISDNAddressString msisdn = request.getMsisdn();
			Boolean sm_RP_PRI = request.getSm_RP_PRI();
			AddressString sca = request.getServiceCentreAddress();
			MAPExtensionContainer extensionContainer = request.getExtensionContainer();
			Boolean gprsSupportIndicator = request.getGprsSupportIndicator();
			SM_RP_MTI sM_RP_MTI = request.getSM_RP_MTI();
			SM_RP_SMEA sM_RP_SMEA = request.getSM_RP_SMEA();

			assertNotNull(msisdn);
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(msisdn.getAddress(), "111222333");
			assertFalse(sm_RP_PRI);
			assertNotNull(sca);
			assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
			assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
			assertEquals(sca.getAddress(), "999000");
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
			assertTrue(gprsSupportIndicator);
			assertEquals(sM_RP_MTI, SM_RP_MTI.SMS_Status_Report);

			try {
				AddressField af = sM_RP_SMEA.getAddressField();
				assertEquals(af.getTypeOfNumber(), TypeOfNumber.InternationalNumber);
				assertEquals(af.getNumberingPlanIdentification(),
						NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
				assertEquals(af.getAddressValue(), "72223884321");
			} catch (MAPException ex) {
				assertTrue(false);
			}

			IMSI imsi = server.mapParameterFactory.createIMSI("25099777000");
			ISDNAddressString networkNodeNumber = server.mapParameterFactory.createISDNAddressString(
					AddressNature.network_specific_number, NumberingPlan.national, "111000111");
			LMSI lmsi = server.mapParameterFactory.createLMSI(Unpooled.wrappedBuffer(new byte[] { 75, 74, 73, 72 }));
			ISDNAddressString sgsnAdditionalNumber = server.mapParameterFactory
					.createISDNAddressString(AddressNature.subscriber_number, NumberingPlan.private_plan, "000111000");
			AdditionalNumber additionalNumber = server.mapParameterFactory
					.createAdditionalNumberSgsnNumber(sgsnAdditionalNumber);
			LocationInfoWithLMSI locationInfoWithLMSI = server.mapParameterFactory.createLocationInfoWithLMSI(
					networkNodeNumber, lmsi, MAPExtensionContainerTest.GetTestExtensionContainer(), true,
					additionalNumber);

			ISDNAddressString storedMSISDN = server.mapParameterFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			MWStatus mwStatus = server.mapParameterFactory.createMWStatus(false, true, false, true);
			Integer absentSubscriberDiagnosticSM = 555;
			Integer additionalAbsentSubscriberDiagnosticSM = 444;

			d.addSendRoutingInfoForSMResponse(request.getInvokeId(), imsi, locationInfoWithLMSI, null, null, null);
			d.addInformServiceCentreRequest(storedMSISDN, mwStatus, null, absentSubscriberDiagnosticSM,
					additionalAbsentSubscriberDiagnosticSM);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SendRoutingInfoForSMRespIndication, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + SendRoutingInfoForSMResponse + InformServiceCentreRequest
		server.awaitSent(EventType.SendRoutingInfoForSMRespIndication);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendRoutingInfoForSMRespIndication);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendRoutingInfoForSMRespIndication);
			SendRoutingInfoForSMResponse response = (SendRoutingInfoForSMResponse) event.getEvent();

			IMSI imsi = response.getIMSI();
			MAPExtensionContainer extensionContainer = response.getExtensionContainer();
			LocationInfoWithLMSI locationInfoWithLMSI = response.getLocationInfoWithLMSI();
			ISDNAddressString networkNodeNumber = locationInfoWithLMSI.getNetworkNodeNumber();
			LMSI lmsi = locationInfoWithLMSI.getLMSI();
			MAPExtensionContainer extensionContainer2 = locationInfoWithLMSI.getExtensionContainer();
			AdditionalNumber additionalNumber = locationInfoWithLMSI.getAdditionalNumber();

			assertNotNull(imsi);
			assertEquals(imsi.getData(), "25099777000");
			assertNotNull(networkNodeNumber);
			assertEquals(networkNodeNumber.getAddressNature(), AddressNature.network_specific_number);
			assertEquals(networkNodeNumber.getNumberingPlan(), NumberingPlan.national);
			assertEquals(networkNodeNumber.getAddress(), "111000111");
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer2));
			assertTrue(locationInfoWithLMSI.getGprsNodeIndicator());
			assertNotNull(lmsi);
			assertTrue(ByteBufUtil.equals(lmsi.getValue(), Unpooled.wrappedBuffer(new byte[] { 75, 74, 73, 72 })));
			assertNotNull(additionalNumber);
			assertEquals(additionalNumber.getSGSNNumber().getAddressNature(), AddressNature.subscriber_number);
			assertEquals(additionalNumber.getSGSNNumber().getNumberingPlan(), NumberingPlan.private_plan);
			assertEquals(additionalNumber.getSGSNNumber().getAddress(), "000111000");
			assertNull(extensionContainer);
		}

		client.awaitReceived(EventType.InformServiceCentreIndication);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.InformServiceCentreIndication);
			InformServiceCentreRequest request = (InformServiceCentreRequest) event.getEvent();

			assertNull(request.getExtensionContainer());
			assertTrue(request.getStoredMSISDN().getAddress().equals("111222333"));
			assertFalse(request.getMwStatus().getScAddressNotIncluded());
			assertTrue(request.getMwStatus().getMnrfSet());
			assertFalse(request.getMwStatus().getMcefSet());
			assertTrue(request.getMwStatus().getMnrgSet());
			assertEquals((int) request.getAbsentSubscriberDiagnosticSM(), 555);
			assertEquals((int) request.getAdditionalAbsentSubscriberDiagnosticSM(), 444);

			request.getMAPDialog().processInvokeWithoutAnswer(request.getInvokeId());
		}

		client.awaitReceived(EventType.DialogClose);
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendRoutingInfoForSMIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendRoutingInfoForSMRespIndication);
		clientExpected.addReceived(EventType.InformServiceCentreIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendRoutingInfoForSMIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendRoutingInfoForSMRespIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * testMsgLength test
	 */

	/**
	 * Sending a short SMS message (20 bytes) This message is fit to the TC-BEGIN
	 * message with Dialog portion
	 *
	 * <pre>
	 * TC-BEGIN + MtForward(Short SMS) -> 
	 * TC-END+MtForward(Response)
	 * </pre>
	 */
	@Test
	public void testAction_TestMsgLength_A() throws Exception {
		client.stop();
		server.stop();

		final int dataLength = 20;

		Client_TestMsgLength client = new Client_TestMsgLength(stack1, peer1Address, peer2Address, 20);
		Server_TestMsgLength server = new Server_TestMsgLength(stack2, peer2Address, peer1Address);

		// 1. TC-BEGIN + MtForward(Short SMS)
		client.sendMoForwardShortMessageRequest_WithLengthChecking(dataLength);
		client.awaitSent(EventType.MoForwardShortMessageIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.MoForwardShortMessageIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.MoForwardShortMessageIndication);
			MoForwardShortMessageRequest moForwSmInd = (MoForwardShortMessageRequest) event.getEvent();

			MAPDialogSms d = moForwSmInd.getMAPDialog();

			SmsSignalInfoImpl sm_RP_UI2 = new SmsSignalInfoImpl(SmsTpduImpl.createInstance(Unpooled.wrappedBuffer(
					new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3,
							-21, 2, 1, -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 }),
					false, null), null);

			d.addMoForwardShortMessageResponse(moForwSmInd.getInvokeId(), sm_RP_UI2,
					MAPExtensionContainerTest.GetTestExtensionContainer());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.MoForwardShortMessageRespIndication, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END+MtForward(Response)
		server.awaitSent(EventType.MoForwardShortMessageRespIndication);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.MoForwardShortMessageIndication);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.MoForwardShortMessageRespIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.MoForwardShortMessageIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.MoForwardShortMessageRespIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Sending a long SMS message (170 bytes) This message is not fit to the
	 * TC-BEGIN message with Dialog portion In the TC-BEGIN message only Dialog
	 * portion is sent, MtForward message is sent in the second (TC-CONTINUE)
	 * message
	 *
	 * <pre>
	 * TC-BEGIN -> 
	 * TC-CONTINUE -> 
	 * TC-CONTINUE + MtForward(Long SMS) ->
	 * TC-END + MtForward(Response)
	 * </pre>
	 */
	@Test
	public void testAction_TestMsgLength_B() throws Exception {
		client.stop();
		server.stop();

		final int dataLength = 170;

		Client_TestMsgLength client = new Client_TestMsgLength(stack1, peer1Address, peer2Address, dataLength);
		Server_TestMsgLength server = new Server_TestMsgLength(stack2, peer2Address, peer1Address);

		// 1. TC-BEGIN ->
		client.sendMoForwardShortMessageRequest_WithLengthChecking(dataLength);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			mapDialog.send(dummyCallback);
		}

		// 2. TC-CONTINUE ->
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			client.sendMoForwardShortMessageRequest_WithLengthChecking_2(dataLength, (MAPDialogSms) mapDialog);
		}

		// 3. TC-CONTINUE + MtForward(Long SMS) ->
		client.awaitSent(EventType.MoForwardShortMessageIndication);

		server.awaitReceived(EventType.MoForwardShortMessageIndication);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.MoForwardShortMessageIndication);
			MoForwardShortMessageRequest moForwSmInd = (MoForwardShortMessageRequest) event.getEvent();

			SmsSignalInfoImpl sm_RP_UI2 = new SmsSignalInfoImpl(SmsTpduImpl.createInstance(Unpooled.wrappedBuffer(
					new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16, 17, 33, 23, 5, 0, 3,
							-21, 2, 1, -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10, -123, 0 }),
					false, null), null);

			MAPDialogSms d = moForwSmInd.getMAPDialog();

			d.addMoForwardShortMessageResponse(moForwSmInd.getInvokeId(), sm_RP_UI2,
					MAPExtensionContainerTest.GetTestExtensionContainer());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.MoForwardShortMessageRespIndication, null);
			mapDialog.close(false, dummyCallback);
		}

		// 4. TC-END + MtForward(Response)
		server.awaitSent(EventType.MoForwardShortMessageRespIndication);

		client.awaitReceived(EventType.MoForwardShortMessageRespIndication);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.MoForwardShortMessageIndication);
		clientExpected.addReceived(EventType.MoForwardShortMessageRespIndication);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.MoForwardShortMessageIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.MoForwardShortMessageRespIndication);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	private class Client_TestMsgLength extends Client {
		public Client_TestMsgLength(MAPStack mapStack, SccpAddress thisAddress, SccpAddress remoteAddress,
				int dataLength) {
			super(mapStack, thisAddress, remoteAddress);
		}

		public void sendMoForwardShortMessageRequest_WithLengthChecking(int dataLength) throws Exception {
			super.mapProvider.getMAPServiceSms().acivate();

			MAPApplicationContext appCnt = null;
			appCnt = MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
					MAPApplicationContextVersion.version3);
			AddressString orgiReference = this.mapParameterFactory
					.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
			AddressString destReference = this.mapParameterFactory.createAddressString(
					AddressNature.international_number, NumberingPlan.land_mobile, "204208300008002");

			clientDialogSms = super.mapProvider.getMAPServiceSms().createNewDialog(appCnt, super.thisAddress,
					orgiReference, super.remoteAddress, destReference, 0);
			clientDialogSms.setExtentionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

			sendMoForwardShortMessageRequest_WithLengthChecking_2(dataLength, clientDialogSms);
		}

		protected void sendMoForwardShortMessageRequest_WithLengthChecking_2(int dataLength, MAPDialogSms dlg)
				throws MAPException {
			SmsSignalInfoImpl sm_RP_UI;
			byte[] data = new byte[dataLength];
			Arrays.fill(data, (byte) 11);
			sm_RP_UI = new SmsSignalInfoImpl(Unpooled.wrappedBuffer(data));
			IMSI imsi1 = this.mapParameterFactory.createIMSI("250991357999");
			SM_RP_DA sm_RP_DA = this.mapParameterFactory.createSM_RP_DA(imsi1);
			ISDNAddressString msisdn1 = this.mapParameterFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
			SM_RP_OA sm_RP_OA = this.mapParameterFactory.createSM_RP_OA_Msisdn(msisdn1);
			IMSI imsi2 = this.mapParameterFactory.createIMSI("25007123456789");

			Integer invokeId = dlg.addMoForwardShortMessageRequest(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, imsi2);

			int maxMsgLen = dlg.getMaxUserDataLength();
			int curMsgLen = dlg.getMessageUserDataLengthOnSend();

			if (curMsgLen > maxMsgLen)
				dlg.cancelInvocation(invokeId);
			else
				super.handleSent(EventType.MoForwardShortMessageIndication, null);

			dlg.send(dummyCallback);
		}

		@Override
		public void onMoForwardShortMessageResponse(MoForwardShortMessageResponse moForwSmRespInd) {
			super.onMoForwardShortMessageResponse(moForwSmRespInd);

			SmsSignalInfo sm_RP_UI = moForwSmRespInd.getSM_RP_UI();
			MAPExtensionContainer extensionContainer = moForwSmRespInd.getExtensionContainer();

			ByteBuf translatedValue = Unpooled.buffer();
			try {
				sm_RP_UI.decodeTpdu(false).encodeData(translatedValue);
			} catch (Exception ex) {
				assertFalse(true);
			}
			assertTrue(ByteBufUtil.equals(translatedValue,
					Unpooled.wrappedBuffer(new byte[] { -28, 10, -111, 33, 67, 101, -121, 9, 0, 0, 112, 80, 81, 81, 16,
							17, 33, 23, 5, 0, 3, -21, 2, 1, -112, 101, 54, -5, -51, 2, -35, -33, 114, 54, 25, 20, 10,
							-123, 0 })));
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
		}
	};

	private class Server_TestMsgLength extends Server {
		Server_TestMsgLength(MAPStack mapStack, SccpAddress thisAddress, SccpAddress remoteAddress) {
			super(mapStack, thisAddress, remoteAddress);
		}

		@Override
		public void onMoForwardShortMessageRequest(MoForwardShortMessageRequest moForwSmInd) {
			super.onMoForwardShortMessageRequest(moForwSmInd);

			SM_RP_DA sm_RP_DA = moForwSmInd.getSM_RP_DA();
			SM_RP_OA sm_RP_OA = moForwSmInd.getSM_RP_OA();
			SmsSignalInfo sm_RP_UI = moForwSmInd.getSM_RP_UI();
			// MAPExtensionContainer extensionContainer =
			// moForwSmInd.getExtensionContainer();
			IMSI imsi2 = moForwSmInd.getIMSI();

			assertNotNull(sm_RP_DA);
			assertNotNull(sm_RP_DA.getIMSI());
			assertEquals(sm_RP_DA.getIMSI().getData(), "250991357999");
			assertNotNull(sm_RP_OA);
			assertNotNull(sm_RP_OA.getMsisdn());
			assertEquals(sm_RP_OA.getMsisdn().getAddressNature(), AddressNature.international_number);
			assertEquals(sm_RP_OA.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(sm_RP_OA.getMsisdn().getAddress(), "111222333");
			assertNotNull(sm_RP_UI);

			// assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
			assertNotNull(imsi2);
			assertEquals(imsi2.getData(), "25007123456789");
		}
	};

	/**
	 * <pre>
	 * TC-BEGIN + sendAuthenticationInfoRequest_V3
	 * TC-END + sendAuthenticationInfoResponse_V3
	 * </pre>
	 */
	@Test
	public void testSendAuthenticationInfo_V3() throws Exception {
		// 1. TC-BEGIN + sendAuthenticationInfoRequest_V3
		client.sendSendAuthenticationInfo_V3();
		client.awaitSent(EventType.SendAuthenticationInfo_V3);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendAuthenticationInfo_V3);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendAuthenticationInfo_V3);
			SendAuthenticationInfoRequest request = (SendAuthenticationInfoRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			IMSI imsi = request.getImsi();

			assertTrue(imsi.getData().equals("4567890"));
			assertEquals(request.getNumberOfRequestedVectors(), 3);
			assertTrue(request.getSegmentationProhibited());
			assertTrue(request.getImmediateResponsePreferred());
			assertNull(request.getReSynchronisationInfo());
			assertNull(request.getExtensionContainer());
			assertEquals(request.getRequestingNodeType(), RequestingNodeType.sgsn);
			assertNull(request.getRequestingPlmnId());
			assertEquals((int) request.getNumberOfRequestedAdditionalVectors(), 5);
			assertFalse(request.getAdditionalVectorsAreForEPS());

			MAPParameterFactory mapParameterFactory = server.mapParameterFactory;

			ArrayList<AuthenticationTriplet> authenticationTriplets = new ArrayList<AuthenticationTriplet>();
			AuthenticationTriplet at = mapParameterFactory.createAuthenticationTriplet(
					Unpooled.wrappedBuffer(TripletListTest.getRandData()),
					Unpooled.wrappedBuffer(TripletListTest.getSresData()),
					Unpooled.wrappedBuffer(TripletListTest.getKcData()));
			authenticationTriplets.add(at);
			TripletList tripletList = mapParameterFactory.createTripletList(authenticationTriplets);
			AuthenticationSetList asl = mapParameterFactory.createAuthenticationSetListV3(tripletList);

			d.addSendAuthenticationInfoResponse(request.getInvokeId(), asl, null, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SendAuthenticationInfoResp_V3, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + sendAuthenticationInfoResponse_V3
		server.awaitSent(EventType.SendAuthenticationInfoResp_V3);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendAuthenticationInfoResp_V3);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendAuthenticationInfoResp_V3);
			SendAuthenticationInfoResponse response = (SendAuthenticationInfoResponse) event.getEvent();

			AuthenticationSetList asl = response.getAuthenticationSetList();
			AuthenticationTriplet at = asl.getTripletList().getAuthenticationTriplets().get(0);

			assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
			assertTrue(ByteBufUtil.equals(at.getRand(), Unpooled.wrappedBuffer(TripletListTest.getRandData())));
			assertTrue(ByteBufUtil.equals(at.getSres(), Unpooled.wrappedBuffer(TripletListTest.getSresData())));
			assertTrue(ByteBufUtil.equals(at.getKc(), Unpooled.wrappedBuffer(TripletListTest.getKcData())));
			assertNull(asl.getQuintupletList());
			assertNull(response.getEpsAuthenticationSetList());
			assertNull(response.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendAuthenticationInfo_V3);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendAuthenticationInfoResp_V3);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendAuthenticationInfo_V3);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendAuthenticationInfoResp_V3);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + sendAuthenticationInfoRequest_V2
	 * TC-END + sendAuthenticationInfoResponse_V2
	 * </pre>
	 */
	@Test
	public void testSendAuthenticationInfo_V2() throws Exception {
		// 1. TC-BEGIN + sendAuthenticationInfoRequest_V2
		client.sendSendAuthenticationInfo_V2();
		client.awaitSent(EventType.SendAuthenticationInfo_V2);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendAuthenticationInfo_V2);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendAuthenticationInfo_V2);
			SendAuthenticationInfoRequest request = (SendAuthenticationInfoRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			IMSI imsi = request.getImsi();

			assertTrue(imsi.getData().equals("456789000"));
			assertEquals(request.getNumberOfRequestedVectors(), 0);
			assertFalse(request.getSegmentationProhibited());
			assertFalse(request.getImmediateResponsePreferred());
			assertNull(request.getReSynchronisationInfo());
			assertNull(request.getExtensionContainer());
			assertNull(request.getRequestingNodeType());
			assertNull(request.getRequestingPlmnId());
			assertNull(request.getNumberOfRequestedAdditionalVectors());
			assertFalse(request.getAdditionalVectorsAreForEPS());

			MAPParameterFactory mapParameterFactory = server.mapParameterFactory;

			ArrayList<AuthenticationTriplet> authenticationTriplets = new ArrayList<AuthenticationTriplet>();
			AuthenticationTriplet at = mapParameterFactory.createAuthenticationTriplet(
					Unpooled.wrappedBuffer(TripletListTest.getRandData()),
					Unpooled.wrappedBuffer(TripletListTest.getSresData()),
					Unpooled.wrappedBuffer(TripletListTest.getKcData()));
			authenticationTriplets.add(at);
			TripletList tripletList = mapParameterFactory.createTripletList(authenticationTriplets);
			AuthenticationSetList asl = mapParameterFactory.createAuthenticationSetList(tripletList);

			d.addSendAuthenticationInfoResponse(request.getInvokeId(), asl);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SendAuthenticationInfoResp_V2, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + sendAuthenticationInfoResponse_V2
		server.awaitSent(EventType.SendAuthenticationInfoResp_V2);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendAuthenticationInfoResp_V2);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendAuthenticationInfoResp_V2);
			SendAuthenticationInfoResponse response = (SendAuthenticationInfoResponse) event.getEvent();

			AuthenticationSetList asl = response.getAuthenticationSetList();
			AuthenticationTriplet at = asl.getTripletList().getAuthenticationTriplets().get(0);

			assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
			assertTrue(ByteBufUtil.equals(at.getRand(), Unpooled.wrappedBuffer(TripletListTest.getRandData())));
			assertTrue(ByteBufUtil.equals(at.getSres(), Unpooled.wrappedBuffer(TripletListTest.getSresData())));
			assertTrue(ByteBufUtil.equals(at.getKc(), Unpooled.wrappedBuffer(TripletListTest.getKcData())));
			assertNull(asl.getQuintupletList());
			assertNull(response.getEpsAuthenticationSetList());
			assertNull(response.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendAuthenticationInfo_V2);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendAuthenticationInfoResp_V2);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendAuthenticationInfo_V2);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendAuthenticationInfoResp_V2);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + updateLocation
	 * TC-END + updateLocationResponse
	 * </pre>
	 */
	@Test
	public void testUpdateLocation() throws Exception {
		// 1. TC-BEGIN + updateLocationRequest
		client.sendUpdateLocation();
		client.awaitSent(EventType.UpdateLocation);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.UpdateLocation);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.UpdateLocation);
			UpdateLocationRequest request = (UpdateLocationRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			IMSI imsi = request.getImsi();
			ISDNAddressString mscNumber = request.getMscNumber();
			ISDNAddressString vlrNumber = request.getVlrNumber();
			LMSI lmsi = request.getLmsi();
			ADDInfo addInfo = request.getADDInfo();

			assertTrue(imsi.getData().equals("45670000"));
			assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertTrue(mscNumber.getAddress().equals("8222333444"));
			assertNull(request.getRoamingNumber());
			assertEquals(vlrNumber.getAddressNature(), AddressNature.network_specific_number);
			assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertTrue(vlrNumber.getAddress().equals("700000111"));
			assertTrue(ByteBufUtil.equals(lmsi.getValue(), Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4 })));
			assertNull(request.getExtensionContainer());
			assertNull(request.getVlrCapability());
			assertTrue(request.getInformPreviousNetworkEntity());
			assertFalse(request.getCsLCSNotSupportedByUE());
			assertNull(request.getVGmlcAddress());
			assertTrue(addInfo.getImeisv().getIMEI().equals("987654321098765"));
			assertNull(request.getPagingArea());
			assertFalse(request.getSkipSubscriberDataUpdate());
			assertTrue(request.getRestorationIndicator());

			ISDNAddressString hlrNumber = server.mapParameterFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "765765765");

			d.addUpdateLocationResponse(request.getInvokeId(), hlrNumber, null, true, false);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.UpdateLocationResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + updateLocationResponse
		server.awaitSent(EventType.UpdateLocationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.UpdateLocationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.UpdateLocationResp);
			UpdateLocationResponse response = (UpdateLocationResponse) event.getEvent();

			ISDNAddressString hlrNumber = response.getHlrNumber();

			assertEquals(hlrNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(hlrNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertTrue(hlrNumber.getAddress().equals("765765765"));
			assertNull(response.getExtensionContainer());
			assertTrue(response.getAddCapability());
			assertFalse(response.getPagingAreaCapability());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.UpdateLocation);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.UpdateLocationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.UpdateLocation);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.UpdateLocationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + anyTimeInterrogationRequest
	 * TC-END + anyTimeInterrogationResponse
	 * </pre>
	 */
	@Test
	public void testAnyTimeInterrogation() throws Exception {
		// 1. TC-BEGIN + anyTimeInterrogationRequest
		client.sendAnyTimeInterrogation();
		client.awaitSent(EventType.AnyTimeInterrogation);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.AnyTimeInterrogation);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.AnyTimeInterrogation);
			AnyTimeInterrogationRequest request = (AnyTimeInterrogationRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			SubscriberIdentity subscriberIdentity = request.getSubscriberIdentity();
			assertTrue(subscriberIdentity.getIMSI().getData().equals("33334444"));
			RequestedInfo requestedInfo = request.getRequestedInfo();
			assertTrue(requestedInfo.getLocationInformation());
			assertTrue(requestedInfo.getSubscriberState());
			assertFalse(requestedInfo.getCurrentLocation());
			assertNull(requestedInfo.getRequestedDomain());
			assertFalse(requestedInfo.getImei());
			assertFalse(requestedInfo.getMsClassmark());
			ISDNAddressString gsmSCFAddress = request.getGsmSCFAddress();
			assertTrue(gsmSCFAddress.getAddress().equals("11112222"));
			assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
			assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);

			MAPParameterFactory mapParameterFactory = server.mapParameterFactory;

			SubscriberState ss = mapParameterFactory.createSubscriberState(SubscriberStateChoice.camelBusy, null);
			SubscriberInfo si = mapParameterFactory.createSubscriberInfo(null, ss, null, null, null, null, null, null,
					null);

			d.addAnyTimeInterrogationResponse(request.getInvokeId(), si, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.AnyTimeInterrogationResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + anyTimeInterrogationResponse
		server.awaitSent(EventType.AnyTimeInterrogationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.AnyTimeInterrogationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.AnyTimeInterrogationResp);
			AnyTimeInterrogationResponse response = (AnyTimeInterrogationResponse) event.getEvent();

			SubscriberInfo si = response.getSubscriberInfo();
			SubscriberState ss = si.getSubscriberState();
			assertEquals(ss.getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
			assertNull(ss.getNotReachableReason());
			assertNull(si.getLocationInformation());
			assertNull(si.getExtensionContainer());
			assertNull(si.getGPRSMSClass());
			assertNull(si.getIMEI());
			assertNull(si.getLocationInformationGPRS());
			assertNull(si.getMNPInfoRes());
			assertNull(si.getMSClassmark2());
			assertNull(si.getPSSubscriberState());
			assertNull(response.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.AnyTimeInterrogation);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.AnyTimeInterrogationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.AnyTimeInterrogation);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.AnyTimeInterrogationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + anyTimeSubscriptionInterrogationRequest
	 * TC-END + anyTimeSubscriptionInterrogationResponse
	 * </pre>
	 */
	@Test
	public void testAnyTimeSubscriptionInterrogation() throws Exception {
		// 1. TC-BEGIN + anyTimeSubscriptionInterrogationRequest
		client.sendAnyTimeSubscriptionInterrogation();
		client.awaitSent(EventType.AnyTimeSubscriptionInterrogation);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.AnyTimeSubscriptionInterrogation);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.AnyTimeSubscriptionInterrogation);
			AnyTimeSubscriptionInterrogationRequest request = (AnyTimeSubscriptionInterrogationRequest) event
					.getEvent();

			SubscriberIdentity subscriberIdentity = request.getSubscriberIdentity();
			assertEquals(subscriberIdentity.getMSISDN().getAddress(), "111222333");

			ISDNAddressString gsmSCFAddressReq = request.getGsmScfAddress();
			assertEquals(gsmSCFAddressReq.getAddress(), "1234567890");

			RequestedSubscriptionInfo requestedSubscriptionInfo = request.getRequestedSubscriptionInfo();
			assertNull(requestedSubscriptionInfo.getRequestedSSInfo());
			assertFalse(requestedSubscriptionInfo.getOdb());
			assertEquals(requestedSubscriptionInfo.getRequestedCAMELSubscriptionInfo(),
					RequestedCAMELSubscriptionInfo.oCSI);
			assertTrue(requestedSubscriptionInfo.getSupportedVlrCamelPhases());
			assertFalse(requestedSubscriptionInfo.getSupportedSgsnCamelPhases());
			assertNull(requestedSubscriptionInfo.getExtensionContainer());
			assertEquals(requestedSubscriptionInfo.getAdditionalRequestedCamelSubscriptionInfo(),
					AdditionalRequestedCAMELSubscriptionInfo.mtSmsCSI);
			assertFalse(requestedSubscriptionInfo.getMsisdnBsList());
			assertTrue(requestedSubscriptionInfo.getCsgSubscriptionDataRequested());
			assertFalse(requestedSubscriptionInfo.getCwInfo());
			assertFalse(requestedSubscriptionInfo.getClipInfo());
			assertFalse(requestedSubscriptionInfo.getClirInfo());
			assertFalse(requestedSubscriptionInfo.getHoldInfo());
			assertFalse(requestedSubscriptionInfo.getEctInfo());

			ISDNAddressString gsmSCFAddress = server.mapProvider.getMAPParameterFactory()
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "123456789");
			final OBcsmCamelTDPData data = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.collectedInfo, 3,
					gsmSCFAddress, DefaultCallHandling.continueCall, null);

			ArrayList<OBcsmCamelTDPData> dataList = new ArrayList<OBcsmCamelTDPData>();
			dataList.add(data);
			OCSI ocsi = new OCSIImpl(dataList, null, null, false, true);
			CAMELSubscriptionInfo camelSubscriptionInfo = new CAMELSubscriptionInfoImpl(ocsi, null, null, null, null,
					null, null, false, false, null, null, null, null, null, null, null, null, null, null, null, null,
					null, null);
			SupportedCamelPhases supportedCamelPhasesVlr = new SupportedCamelPhasesImpl(true, true, true, true);
			OfferedCamel4CSIsImpl offeredCamel4CSIsVlr = new OfferedCamel4CSIsImpl(true, false, false, false, false,
					false, false);

			MAPDialogMobility d = request.getMAPDialog();
			d.addAnyTimeSubscriptionInterrogationResponse(request.getInvokeId(), null, null, null,
					camelSubscriptionInfo, supportedCamelPhasesVlr, null, null, offeredCamel4CSIsVlr, null, null, null,
					null, null, null, null, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.AnyTimeSubscriptionInterrogationRes, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + anyTimeSubscriptionInterrogationResponse
		server.awaitSent(EventType.AnyTimeSubscriptionInterrogationRes);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.AnyTimeSubscriptionInterrogationRes);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.AnyTimeSubscriptionInterrogationRes);
			AnyTimeSubscriptionInterrogationResponse response = (AnyTimeSubscriptionInterrogationResponse) event
					.getEvent();

			OCSI ocsi = response.getCamelSubscriptionInfo().getOCsi();
			assertNotNull(ocsi.getOBcsmCamelTDPDataList());
			assertEquals(ocsi.getOBcsmCamelTDPDataList().size(), 1);

			OBcsmCamelTDPData tdpData = ocsi.getOBcsmCamelTDPDataList().get(0);
			assertEquals(tdpData.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
			assertEquals(tdpData.getServiceKey(), 3);
			assertEquals(tdpData.getDefaultCallHandling(), DefaultCallHandling.continueCall);

			ISDNAddressString gsmSCFAddress = tdpData.getGsmSCFAddress();
			assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
			assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(gsmSCFAddress.getAddress(), "123456789");

			SupportedCamelPhases supportedCamelPhasesVlr = response.getsupportedVlrCamelPhases();
			assertTrue(supportedCamelPhasesVlr.getPhase1Supported());
			assertTrue(supportedCamelPhasesVlr.getPhase2Supported());
			assertTrue(supportedCamelPhasesVlr.getPhase3Supported());
			assertTrue(supportedCamelPhasesVlr.getPhase4Supported());

			OfferedCamel4CSIs offeredCamel4CSIsVlr = response.getOfferedCamel4CSIsInVlr();
			assertTrue(offeredCamel4CSIsVlr.getOCsi());
			assertFalse(offeredCamel4CSIsVlr.getDCsi());
			assertFalse(offeredCamel4CSIsVlr.getVtCsi());
			assertFalse(offeredCamel4CSIsVlr.getTCsi());
			assertFalse(offeredCamel4CSIsVlr.getMtSmsCsi());
			assertFalse(offeredCamel4CSIsVlr.getMgCsi());
			assertFalse(offeredCamel4CSIsVlr.getPsiEnhancements());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.AnyTimeSubscriptionInterrogation);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.AnyTimeSubscriptionInterrogationRes);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.AnyTimeSubscriptionInterrogation);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.AnyTimeSubscriptionInterrogationRes);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + provideSubscriberInfoRequest
	 * TC-END + provideSubscriberInfoResponse
	 * </pre>
	 */
	@Test
	public void testProvideSubscriberInfo() throws Exception {
		// 1. TC-BEGIN + provideSubscriberInfoRequest
		client.sendProvideSubscriberInfo();
		client.awaitSent(EventType.ProvideSubscriberInfo);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProvideSubscriberInfo);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProvideSubscriberInfo);
			ProvideSubscriberInfoRequest request = (ProvideSubscriberInfoRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			assertEquals(request.getImsi().getData(), "33334444");
			RequestedInfo requestedInfo = request.getRequestedInfo();
			assertTrue(requestedInfo.getLocationInformation());
			assertTrue(requestedInfo.getSubscriberState());
			assertFalse(requestedInfo.getCurrentLocation());
			assertNull(requestedInfo.getRequestedDomain());
			assertFalse(requestedInfo.getImei());
			assertFalse(requestedInfo.getMsClassmark());

			MAPParameterFactory paramFactory = server.mapParameterFactory;

			GeographicalInformation geographicalInformation = paramFactory.createGeographicalInformation(30, 60, 10);
			LocationInformation locationInformation = paramFactory.createLocationInformation(10,
					geographicalInformation, null, null, null, null, null, null, null, false, false, null, null);
			SubscriberState ss = paramFactory.createSubscriberState(SubscriberStateChoice.camelBusy, null);
			SubscriberInfo si = paramFactory.createSubscriberInfo(locationInformation, ss, null, null, null, null, null,
					null, null);

			d.addProvideSubscriberInfoResponse(request.getInvokeId(), si, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ProvideSubscriberInfoResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + provideSubscriberInfoResponse
		server.awaitSent(EventType.ProvideSubscriberInfoResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ProvideSubscriberInfoResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ProvideSubscriberInfoResp);
			ProvideSubscriberInfoResponse response = (ProvideSubscriberInfoResponse) event.getEvent();

			SubscriberInfo si = response.getSubscriberInfo();
			SubscriberState ss = si.getSubscriberState();
			assertEquals(ss.getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
			assertNull(ss.getNotReachableReason());
			assertNull(si.getExtensionContainer());
			assertNull(si.getGPRSMSClass());
			assertNull(si.getIMEI());
			assertNull(si.getLocationInformationGPRS());
			assertNull(si.getMNPInfoRes());
			assertNull(si.getMSClassmark2());
			assertNull(si.getPSSubscriberState());
			assertNull(response.getExtensionContainer());

			LocationInformation locationInformation = si.getLocationInformation();
			assertEquals((int) locationInformation.getAgeOfLocationInformation(), 10);
			assertTrue(Math.abs(locationInformation.getGeographicalInformation().getLatitude() - 30) < 0.01);
			assertTrue(Math.abs(locationInformation.getGeographicalInformation().getLongitude() - 60) < 0.01);
			assertTrue(Math.abs(locationInformation.getGeographicalInformation().getUncertainty() - 10) < 1);
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProvideSubscriberInfo);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ProvideSubscriberInfoResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProvideSubscriberInfo);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ProvideSubscriberInfoResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + provideSubscriberLocationRequest
	 * TC-END + provideSubscriberLocationResponse
	 * </pre>
	 */
	@Test
	public void testProvideSubscriberLocation() throws Exception {
		// 1. TC-BEGIN + provideSubscriberLocationRequest
		client.sendProvideSubscriberLocation();
		client.awaitSent(EventType.ProvideSubscriberLocation);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProvideSubscriberLocation);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProvideSubscriberLocation);
			ProvideSubscriberLocationRequest request = (ProvideSubscriberLocationRequest) event.getEvent();
			MAPDialogLsm d = request.getMAPDialog();

			assertEquals(request.getLocationType().getLocationEstimateType(),
					LocationEstimateType.cancelDeferredLocation);
			assertEquals(request.getMlcNumber().getAddress(), "11112222");

			ExtGeographicalInformation locationEstimate = server.mapParameterFactory
					.createExtGeographicalInformation_EllipsoidPoint(-31, -53);
			d.addProvideSubscriberLocationResponse(request.getInvokeId(), locationEstimate, null, null, 6, null, null,
					false, null, false, null, null, false, null, null, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ProvideSubscriberLocationResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + provideSubscriberLocationResponse
		server.awaitSent(EventType.ProvideSubscriberLocationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ProvideSubscriberLocationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ProvideSubscriberLocationResp);
			ProvideSubscriberLocationResponse response = (ProvideSubscriberLocationResponse) event.getEvent();

			// assertTrue(ByteBufUtil.equals(response.getLocationEstimate().getData(), new
			// byte[] { 50 }));
			assertEquals((int) response.getAgeOfLocationEstimate(), 6);

			assertTrue(response.getLocationEstimate().getLatitude() - (-31) < 0.001);
			assertTrue(response.getLocationEstimate().getLongitude() - (-53) < 0.001);
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProvideSubscriberLocation);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ProvideSubscriberLocationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProvideSubscriberLocation);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ProvideSubscriberLocationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + subscriberLocationReportRequest
	 * TC-END + subscriberLocationReportResponse
	 * </pre>
	 */
	@Test
	public void testSubscriberLocationReport() throws Exception {
		// 1. TC-BEGIN + SubscriberLocationReportRequest
		client.sendSubscriberLocationReport();
		client.awaitSent(EventType.SubscriberLocationReport);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SubscriberLocationReport);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SubscriberLocationReport);
			SubscriberLocationReportRequest request = (SubscriberLocationReportRequest) event.getEvent();
			MAPDialogLsm d = request.getMAPDialog();

			assertEquals(request.getLCSEvent(), LCSEvent.emergencyCallOrigination);
			assertEquals(request.getLCSClientID().getLCSClientType(), LCSClientType.plmnOperatorServices);
			assertEquals(request.getLCSLocationInfo().getNetworkNodeNumber().getAddress(), "11113333");

			ISDNAddressString naEsrd = server.mapParameterFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "11114444");

			d.addSubscriberLocationReportResponse(request.getInvokeId(), naEsrd, null, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SubscriberLocationReportResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + SubscriberLocationReportResponse
		server.awaitSent(EventType.SubscriberLocationReportResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SubscriberLocationReportResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SubscriberLocationReportResp);
			SubscriberLocationReportResponse response = (SubscriberLocationReportResponse) event.getEvent();

			assertEquals(response.getNaESRD().getAddress(), "11114444");
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SubscriberLocationReport);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SubscriberLocationReportResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SubscriberLocationReport);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SubscriberLocationReportResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + sendRoutingInforForLCSRequest 
	 * TC-END + sendRoutingInforForLCSResponse
	 * </pre>
	 */
	@Test
	public void testSendRoutingInforForLCS() throws Exception {
		// 1. TC-BEGIN + SendRoutingInfoForLCSRequest
		client.sendSendRoutingInforForLCS();
		client.awaitSent(EventType.SendRoutingInfoForLCS);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendRoutingInfoForLCS);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendRoutingInfoForLCS);
			SendRoutingInfoForLCSRequest request = (SendRoutingInfoForLCSRequest) event.getEvent();
			MAPDialogLsm d = request.getMAPDialog();

			assertEquals(request.getMLCNumber().getAddress(), "11112222");
			assertEquals(request.getTargetMS().getIMSI().getData(), "5555544444");

			MAPParameterFactory mapParameterFactory = server.mapParameterFactory;

			IMSI imsi = mapParameterFactory.createIMSI("6666644444");
			SubscriberIdentity targetMS = mapParameterFactory.createSubscriberIdentity(imsi);
			ISDNAddressString networkNodeNumber = mapParameterFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "11114444");
			LCSLocationInfo lcsLocationInfo = mapParameterFactory.createLCSLocationInfo(networkNodeNumber, null, null,
					false, null, null, null, null, null);

			d.addSendRoutingInfoForLCSResponse(request.getInvokeId(), targetMS, lcsLocationInfo, null, null, null, null,
					null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SendRoutingInfoForLCSResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + SendRoutingInfoForLCSResponse
		server.awaitSent(EventType.SendRoutingInfoForLCSResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendRoutingInfoForLCSResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendRoutingInfoForLCSResp);
			SendRoutingInfoForLCSResponse response = (SendRoutingInfoForLCSResponse) event.getEvent();

			assertEquals(response.getTargetMS().getIMSI().getData(), "6666644444");
			assertEquals(response.getLCSLocationInfo().getNetworkNodeNumber().getAddress(), "11114444");
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendRoutingInfoForLCS);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendRoutingInfoForLCSResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendRoutingInfoForLCS);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendRoutingInfoForLCSResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + checkImeiRequest 
	 * TC-END + checkImeiResponse
	 * </pre>
	 */
	@Test
	public void testCheckImei() throws Exception {
		// 1. TC-BEGIN + CheckImeiRequest
		client.sendCheckImei();
		client.awaitSent(EventType.CheckImei);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.CheckImei);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CheckImei);
			CheckImeiRequest request = (CheckImeiRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			assertEquals(request.getIMEI().getIMEI(), "111111112222222");
			assertTrue(request.getRequestedEquipmentInfo().getEquipmentStatus());
			assertFalse(request.getRequestedEquipmentInfo().getBmuef());
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(request.getExtensionContainer()));

			UESBIIuAImpl uesbiIuA = new UESBIIuAImpl();
			uesbiIuA.setBit(0);

			UESBIIuBImpl uesbiIuB = new UESBIIuBImpl();
			UESBIIu bmuef = server.mapParameterFactory.createUESBIIu(uesbiIuA, uesbiIuB);

			MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
			d.addCheckImeiResponse(request.getInvokeId(), EquipmentStatus.blackListed, bmuef, extensionContainer);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.CheckImeiResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + CheckImeiResponse
		server.awaitSent(EventType.CheckImeiResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.CheckImeiResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CheckImeiResp);
			CheckImeiResponse response = (CheckImeiResponse) event.getEvent();

			assertEquals(response.getEquipmentStatus(), EquipmentStatus.blackListed);
			assertTrue(response.getBmuef().getUESBI_IuA().isBitSet(0));
			assertFalse(response.getBmuef().getUESBI_IuB().isBitSet(0));
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(response.getExtensionContainer()));
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.CheckImei);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.CheckImeiResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.CheckImei);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.CheckImeiResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + checkImeiRequest_V2 
	 * TC-END + checkImeiResponse_V2
	 * </pre>
	 */
	@Test
	public void testCheckImei_V2() throws Exception {
		// 1. TC-BEGIN + CheckImeiRequest
		client.sendCheckImei_V2();
		client.awaitSent(EventType.CheckImei);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.CheckImei);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CheckImei);
			CheckImeiRequest request = (CheckImeiRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			assertEquals(request.getIMEI().getIMEI(), "333333334444444");
			assertNull(request.getRequestedEquipmentInfo());
			assertNull(request.getExtensionContainer());

			d.addCheckImeiResponse(request.getInvokeId(), EquipmentStatus.blackListed);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.CheckImeiResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + CheckImeiResponse
		server.awaitSent(EventType.CheckImeiResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.CheckImeiResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CheckImeiResp);
			CheckImeiResponse response = (CheckImeiResponse) event.getEvent();

			assertEquals(response.getEquipmentStatus(), EquipmentStatus.blackListed);
			assertNull(response.getBmuef());
			assertNull(response.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.CheckImei);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.CheckImeiResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.CheckImei);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.CheckImeiResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + checkImeiRequest_V2_Huawei_extention_test 
	 * TC-END + checkImeiResponse_V2
	 * </pre>
	 */
	@Test
	public void testCheckImei_Huawei_V2() throws Exception {
		// 1. TC-BEGIN + CheckImeiRequest
		client.sendCheckImei_Huawei_V2();
		client.awaitSent(EventType.CheckImei);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.CheckImei);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CheckImei);
			CheckImeiRequest request = (CheckImeiRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			assertEquals(request.getIMEI().getIMEI(), "333333334444444");
			assertNull(request.getRequestedEquipmentInfo());
			assertNull(request.getExtensionContainer());
			CheckImeiRequestImplV1 impl = (CheckImeiRequestImplV1) request;
			assertEquals(impl.getIMSI().getData(), "999999998888888");

			d.addCheckImeiResponse(request.getInvokeId(), EquipmentStatus.blackListed);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.CheckImeiResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + CheckImeiResponse
		server.awaitSent(EventType.CheckImeiResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.CheckImeiResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CheckImeiResp);
			CheckImeiResponse response = (CheckImeiResponse) event.getEvent();

			assertEquals(response.getEquipmentStatus(), EquipmentStatus.blackListed);
			assertNull(response.getBmuef());
			assertNull(response.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.CheckImei);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.CheckImeiResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.CheckImei);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.CheckImeiResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Some not real test for testing:
	 * <p>
	 * - sendDelayed() / closeDelayed()
	 * <p>
	 * - getTCAPMessageType()
	 * <p>
	 * - saving origReferense, destReference, extContainer in MAPDialog
	 * 
	 * <pre>
	 * TC-BEGIN + extContainer + checkImeiRequest + checkImeiRequest
	 * TC-CONTINUE + sendDelayed(checkImeiResponse) + sendDelayed(checkImeiResponse)
	 * TC-END + closeDelayed(checkImeiResponse) + sendDelayed(checkImeiResponse)
	 * </pre>
	 */
	@Test
	public void testDelayedSendClose() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onCheckImeiResponse(CheckImeiResponse ind) {
				super.onCheckImeiResponse(ind);

				assertTrue(ind.getEquipmentStatus().equals(EquipmentStatus.blackListed));
				assertNull(ind.getBmuef());
				assertNull(ind.getExtensionContainer());

				MAPDialogMobility d = ind.getMAPDialog();
				assertEquals(d.getTCAPMessageType(), MessageType.Continue);

				client.awaitSent(EventType.CheckImei);
			};
		};

		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
					MAPExtensionContainer extensionContainer) {
				super.onDialogRequest(mapDialog, destReference, origReference, extensionContainer);

				assertEquals(origReference.getAddressNature(), AddressNature.international_number);
				assertEquals(origReference.getNumberingPlan(), NumberingPlan.ISDN);
				assertTrue(origReference.getAddress().equals("11335577"));

				assertEquals(destReference.getAddressNature(), AddressNature.international_number);
				assertEquals(destReference.getNumberingPlan(), NumberingPlan.ISDN);
				assertTrue(destReference.getAddress().equals("22446688"));

				assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

				assertEquals(mapDialog.getTCAPMessageType(), MessageType.Begin);
			}

			@Override
			public void onCheckImeiRequest(CheckImeiRequest ind) {
				super.onCheckImeiRequest(ind);

				MAPDialogMobility d = ind.getMAPDialog();

				assertTrue(ind.getIMEI().getIMEI().equals("333333334444444"));
				assertNull(ind.getRequestedEquipmentInfo());
				assertNull(ind.getExtensionContainer());

				AddressString origReference = d.getReceivedOrigReference();
				if (origReference != null)
					server.awaitSent(EventType.CheckImeiResp);
			}
		};

		// 1. TC-BEGIN + extContainer + checkImeiRequest + checkImeiRequest
		client.sendCheckImei_ForDelayedTest();
		client.awaitSent(EventType.CheckImei);
		client.awaitSent(EventType.CheckImei);

		server.awaitReceived(EventType.DialogRequest);

		server.awaitReceived(EventType.CheckImei);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CheckImei);
			CheckImeiRequest ind = (CheckImeiRequest) event.getEvent();

			MAPDialogMobility d = ind.getMAPDialog();
			assertEquals(d.getTCAPMessageType(), MessageType.Begin);

			assertEquals(d.getReceivedOrigReference().getAddressNature(), AddressNature.international_number);
			assertEquals(d.getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
			assertTrue(d.getReceivedOrigReference().getAddress().equals("11335577"));

			assertEquals(d.getReceivedDestReference().getAddressNature(), AddressNature.international_number);
			assertEquals(d.getReceivedDestReference().getNumberingPlan(), NumberingPlan.ISDN);
			assertTrue(d.getReceivedDestReference().getAddress().equals("22446688"));

			d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed);
			d.sendDelayed(dummyCallback);
			server.handleSent(EventType.CheckImeiResp, null);

			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(d.getReceivedExtensionContainer()));
		}
		// server's awaiting of sending CheckImeiResp is implemented in listener
		server.awaitReceived(EventType.CheckImei);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CheckImei);
			CheckImeiRequest ind = (CheckImeiRequest) event.getEvent();

			MAPDialogMobility d = ind.getMAPDialog();
			assertEquals(d.getTCAPMessageType(), MessageType.Begin);

			assertEquals(d.getReceivedOrigReference().getAddressNature(), AddressNature.international_number);
			assertEquals(d.getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
			assertTrue(d.getReceivedOrigReference().getAddress().equals("11335577"));

			assertEquals(d.getReceivedDestReference().getAddressNature(), AddressNature.international_number);
			assertEquals(d.getReceivedDestReference().getNumberingPlan(), NumberingPlan.ISDN);
			assertTrue(d.getReceivedDestReference().getAddress().equals("22446688"));

			d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed);
			d.sendDelayed(dummyCallback);
			server.handleSent(EventType.CheckImeiResp, null);

			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(d.getReceivedExtensionContainer()));
		}
		// server's awaiting of sending CheckImeiResp is implemented in listener

		server.awaitReceived(EventType.DialogDelimiter);

		// 2. TC-CONTINUE + sendDelayed + sendDelayed
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.CheckImeiResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CheckImeiResp);
			CheckImeiResponse ind = (CheckImeiResponse) event.getEvent();

			MAPDialogMobility d = ind.getMAPDialog();
			IMEI imei = client.mapParameterFactory.createIMEI("333333334444444");
			d.addCheckImeiRequest(imei);
			d.closeDelayed(false, dummyCallback);
			client.handleSent(EventType.CheckImei, null);
		}
		// client's awaiting of sending CheckImei is implemented in listener above
		client.awaitReceived(EventType.CheckImeiResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CheckImeiResp);
			CheckImeiResponse ind = (CheckImeiResponse) event.getEvent();

			MAPDialogMobility d = ind.getMAPDialog();
			IMEI imei = client.mapParameterFactory.createIMEI("333333334444444");
			d.addCheckImeiRequest(imei);
			d.sendDelayed(dummyCallback);
			client.handleSent(EventType.CheckImei, null);
		}
		// client's awaiting of sending CheckImei is implemented in listener above
		client.awaitReceived(EventType.DialogDelimiter);

		// 3. TC-END + closeDelayed(checkImeiResponse) + sendDelayed(checkImeiResponse)
		server.awaitReceived(EventType.CheckImei);
		server.awaitReceived(EventType.CheckImei);
		server.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.CheckImei);
		clientExpected.addSent(EventType.CheckImei);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.CheckImeiResp);
		clientExpected.addSent(EventType.CheckImei);
		clientExpected.addReceived(EventType.CheckImeiResp);
		clientExpected.addSent(EventType.CheckImei);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.CheckImei);
		serverExpected.addSent(EventType.CheckImeiResp);
		serverExpected.addReceived(EventType.CheckImei);
		serverExpected.addSent(EventType.CheckImeiResp);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.CheckImei);
		serverExpected.addReceived(EventType.CheckImei);
		serverExpected.addReceived(EventType.DialogClose);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Some not real test for testing:
	 * <p>
	 * - closeDelayed(true)
	 * <p>
	 * - getTCAPMessageType()
	 * 
	 * <pre>
	 * TC-BEGIN + checkImeiRequest + checkImeiRequest 
	 * no TC-END (Prearranged) + [checkImeiResponse + checkImeiResponse]
	 * </pre>
	 */
	@Test
	public void testDelayedClosePrearranged() throws Exception {
		server.stop();

		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference,
					MAPExtensionContainer extensionContainer) {
				super.onDialogRequest(mapDialog, destReference, origReference, extensionContainer);

				assertNull(origReference);
				assertNull(destReference);
				assertNull(extensionContainer);

				assertEquals(mapDialog.getTCAPMessageType(), MessageType.Begin);
			}

			@Override
			public void onCheckImeiRequest(CheckImeiRequest ind) {
				super.onCheckImeiRequest(ind);

				MAPDialogMobility d = ind.getMAPDialog();

				assertTrue(ind.getIMEI().getIMEI().equals("333333334444444"));
				assertNull(ind.getRequestedEquipmentInfo());
				assertNull(ind.getExtensionContainer());

				assertNull(d.getReceivedOrigReference());
				assertNull(d.getReceivedDestReference());
				assertNull(d.getReceivedExtensionContainer());

				assertEquals(d.getTCAPMessageType(), MessageType.Begin);

				server.awaitSent(EventType.CheckImeiResp);
			}
		};

		// 1. TC-BEGIN + checkImeiRequest + checkImeiRequest
		client.sendCheckImei_ForDelayedTest2();
		client.clientDialogMobility.close(true, dummyCallback);

		client.awaitSent(EventType.CheckImei);
		client.awaitSent(EventType.CheckImei);

		// 2. no TC-END (Prearranged) + [checkImeiResponse + checkImeiResponse]
		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.CheckImei);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CheckImei);
			CheckImeiRequest ind = (CheckImeiRequest) event.getEvent();

			MAPDialogMobility d = ind.getMAPDialog();
			d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed);
			d.sendDelayed(dummyCallback);

			server.handleSent(EventType.CheckImeiResp, null);
		}
		// awaiting for server's CheckImeiResp is implemented in listener above
		server.awaitReceived(EventType.CheckImei);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CheckImei);
			CheckImeiRequest ind = (CheckImeiRequest) event.getEvent();

			MAPDialogMobility d = ind.getMAPDialog();
			d.addCheckImeiResponse(ind.getInvokeId(), EquipmentStatus.blackListed);
			d.closeDelayed(true, dummyCallback);

			server.handleSent(EventType.CheckImeiResp, null);
		}
		// awaiting for server's CheckImeiResp is implemented in listener above
		server.awaitReceived(EventType.DialogDelimiter);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.CheckImei);
		clientExpected.addSent(EventType.CheckImei);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.CheckImei);
		serverExpected.addSent(EventType.CheckImeiResp);
		serverExpected.addReceived(EventType.CheckImei);
		serverExpected.addSent(EventType.CheckImeiResp);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + cancelLocation MAV V3 
	 * TC-END + cancleLocationResponse
	 * </pre>
	 */
	@Test
	public void testCancelLocation() throws Exception {
		// 1. TC-BEGIN + CancelLocationRequest
		client.sendCancelLocation();
		client.awaitSent(EventType.CancelLocation);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.CancelLocation);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CancelLocation);
			CancelLocationRequest request = (CancelLocationRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			IMSI imsi = request.getImsi();
			IMSIWithLMSI imsiWithLmsi = request.getImsiWithLmsi();
			CancellationType cancellationType = request.getCancellationType();
			MAPExtensionContainer extensionContainer = request.getExtensionContainer();
			TypeOfUpdate typeOfUpdate = request.getTypeOfUpdate();
			boolean mtrfSupportedAndAuthorized = request.getMtrfSupportedAndAuthorized();
			boolean mtrfSupportedAndNotAuthorized = request.getMtrfSupportedAndNotAuthorized();
			ISDNAddressString newMSCNumber = request.getNewMSCNumber();
			ISDNAddressString newVLRNumber = request.getNewVLRNumber();
			LMSI newLmsi = request.getNewLmsi();

			assertEquals(imsi.getData(), "1111122222");
			assertNull(imsiWithLmsi);
			assertEquals(cancellationType.getCode(), 1);
			assertNotNull(extensionContainer);
			MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer);
			assertEquals(typeOfUpdate.getCode(), 0);
			assertFalse(mtrfSupportedAndAuthorized);
			assertFalse(mtrfSupportedAndNotAuthorized);
			assertEquals(newMSCNumber.getAddress(), "22228");
			assertEquals(newMSCNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(newMSCNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(newVLRNumber.getAddress(), "22229");
			assertEquals(newVLRNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(newVLRNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertTrue(ByteBufUtil.equals(newLmsi.getValue(), Unpooled.wrappedBuffer(new byte[] { 0, 3, 98, 39 })));

			d.addCancelLocationResponse(request.getInvokeId(), extensionContainer);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.CancelLocationResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + CancelLocationResponse
		server.awaitSent(EventType.CancelLocationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.CancelLocationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CancelLocationResp);
			CancelLocationResponse response = (CancelLocationResponse) event.getEvent();

			MAPExtensionContainerTest.CheckTestExtensionContainer(response.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.CancelLocation);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.CancelLocationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.CancelLocation);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.CancelLocationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + cancelLocationRequest MAV V2 
	 * TC-END + cancleLocationResponse
	 * </pre>
	 */
	@Test
	public void testCancelLocation_V2() throws Exception {
		// 1. TC-BEGIN + CancelLocationRequest
		client.sendCancelLocation_V2();
		client.awaitSent(EventType.CancelLocation);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.CancelLocation);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CancelLocation);
			CancelLocationRequest request = (CancelLocationRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			IMSI imsi = request.getImsi();
			IMSIWithLMSI imsiWithLmsi = request.getImsiWithLmsi();
			CancellationType cancellationType = request.getCancellationType();
			MAPExtensionContainer extensionContainer = request.getExtensionContainer();
			TypeOfUpdate typeOfUpdate = request.getTypeOfUpdate();
			boolean mtrfSupportedAndAuthorized = request.getMtrfSupportedAndAuthorized();
			boolean mtrfSupportedAndNotAuthorized = request.getMtrfSupportedAndNotAuthorized();
			ISDNAddressString newMSCNumber = request.getNewMSCNumber();
			ISDNAddressString newVLRNumber = request.getNewVLRNumber();
			LMSI newLmsi = request.getNewLmsi();

			assertEquals(imsi.getData(), "1111122222");
			assertNull(imsiWithLmsi);
			assertNull(cancellationType);
			assertNull(extensionContainer);
			assertNull(typeOfUpdate);
			assertFalse(mtrfSupportedAndAuthorized);
			assertFalse(mtrfSupportedAndNotAuthorized);
			assertNull(newMSCNumber);
			assertNull(newVLRNumber);
			assertNull(newLmsi);

			d.addCancelLocationResponse(request.getInvokeId(), null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.CancelLocationResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + CancelLocationResponse
		server.awaitSent(EventType.CancelLocationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.CancelLocationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CancelLocationResp);
			CancelLocationResponse response = (CancelLocationResponse) event.getEvent();

			assertNull(response.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.CancelLocation);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.CancelLocationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.CancelLocation);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.CancelLocationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + provideRoamingNumber V3
	 * TC-END + provideRoamingNumberResponse
	 * </pre>
	 */
	@Test
	public void testProvideRoamingNumber() throws Exception {
		// 1. TC-BEGIN + ProvideRoamingNumberRequest
		client.sendProvideRoamingNumber();
		client.awaitSent(EventType.ProvideRoamingNumber);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProvideRoamingNumber);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProvideRoamingNumber);
			ProvideRoamingNumberRequest request = (ProvideRoamingNumberRequest) event.getEvent();
			MAPDialogCallHandling d = request.getMAPDialog();

			IMSI imsi = request.getImsi();
			ISDNAddressString mscNumber = request.getMscNumber();
			ISDNAddressString msisdn = request.getMsisdn();
			LMSI lmsi = request.getLmsi();
			ExternalSignalInfo gsmBearerCapability = request.getGsmBearerCapability();
			ExternalSignalInfo networkSignalInfo = request.getNetworkSignalInfo();
			boolean suppressionOfAnnouncement = request.getSuppressionOfAnnouncement();
			ISDNAddressString gmscAddress = request.getGmscAddress();
			// CallReferenceNumber callReferenceNumber = request.getCallReferenceNumber();
			// boolean orInterrogation = request.getOrInterrogation();
			MAPExtensionContainer extensionContainer = request.getExtensionContainer();
			// AlertingPattern alertingPattern = request.getAlertingPattern();
			// boolean ccbsCall = request.getCcbsCall();
			// SupportedCamelPhases supportedCamelPhasesInInterrogatingNode =
			// request.getSupportedCamelPhasesInInterrogatingNode();
			// ExtExternalSignalInfo additionalSignalInfo =
			// request.getAdditionalSignalInfo();
			// boolean orNotSupportedInGMSC = request.getOrNotSupportedInGMSC();
			// boolean prePagingSupported = request.getPrePagingSupported();
			// boolean longFTNSupported = request.getLongFTNSupported();
			// boolean suppressVtCsi = request.getSuppressVtCsi();
			// OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode =
			// request.getOfferedCamel4CSIsInInterrogatingNode();
			// boolean mtRoamingRetrySupported = request.getMtRoamingRetrySupported();
			// PagingArea pagingArea = request.getPagingArea();
			// EMLPPPriority callPriority = request.getCallPriority();
			// boolean mtrfIndicator = request.getMtrfIndicator();
			// ISDNAddressString oldMSCNumber = request.getOldMSCNumber();

			assertNotNull(imsi);
			assertEquals(imsi.getData(), "011220200198227");
			assertNotNull(mscNumber);
			assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(mscNumber.getAddress(), "22228");
			assertNotNull(msisdn);
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(msisdn.getAddress(), "22227");
			assertNotNull(lmsi);
			assertNotNull(gsmBearerCapability);
			assertNotNull(networkSignalInfo);
			assertFalse(suppressionOfAnnouncement);
			assertNotNull(gmscAddress);
			assertEquals(gmscAddress.getAddressNature(), AddressNature.international_number);
			assertEquals(gmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(gmscAddress.getAddress(), "22226");
			MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer);

			ISDNAddressString roamingNumber = new ISDNAddressStringImpl(AddressNature.international_number,
					NumberingPlan.ISDN, "49883700292");
			boolean releaseResourcesSupported = false;
			ISDNAddressStringImpl vmscAddress = new ISDNAddressStringImpl(AddressNature.international_number,
					NumberingPlan.ISDN, "29113123311");

			d.addProvideRoamingNumberResponse(request.getInvokeId(), roamingNumber, extensionContainer,
					releaseResourcesSupported, vmscAddress);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ProvideRoamingNumberResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + ProvideRoamingNumberResponse
		server.awaitSent(EventType.ProvideRoamingNumberResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ProvideRoamingNumberResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ProvideRoamingNumberResp);
			ProvideRoamingNumberResponse response = (ProvideRoamingNumberResponse) event.getEvent();

			ISDNAddressString roamingNumber = response.getRoamingNumber();
			MAPExtensionContainer extensionContainer = response.getExtensionContainer();
			// boolean releaseResourcesSupported = response.getReleaseResourcesSupported();
			// ISDNAddressString vmscAddress = response.getVmscAddress();

			assertNotNull(roamingNumber);
			assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(roamingNumber.getAddress(), "49883700292");
			MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer);
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProvideRoamingNumber);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ProvideRoamingNumberResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProvideRoamingNumber);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ProvideRoamingNumberResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + provideRoamingNumberRequest V2
	 * TC-END + provideRoamingNumberResponse
	 * </pre>
	 */
	@Test
	public void testProvideRoamingNumber_V2() throws Exception {
		// 1. TC-BEGIN + ProvideRoamingNumberRequest
		client.sendProvideRoamingNumber_V2();
		client.awaitSent(EventType.ProvideRoamingNumber);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ProvideRoamingNumber);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ProvideRoamingNumber);
			ProvideRoamingNumberRequest request = (ProvideRoamingNumberRequest) event.getEvent();
			MAPDialogCallHandling d = request.getMAPDialog();

			IMSI imsi = request.getImsi();
			ISDNAddressString mscNumber = request.getMscNumber();
			ISDNAddressString msisdn = request.getMsisdn();
			LMSI lmsi = request.getLmsi();
			// ExternalSignalInfo gsmBearerCapability = request.getGsmBearerCapability();
			// ExternalSignalInfo networkSignalInfo = request.getNetworkSignalInfo();
			boolean suppressionOfAnnouncement = request.getSuppressionOfAnnouncement();
			ISDNAddressString gmscAddress = request.getGmscAddress();
			// CallReferenceNumber callReferenceNumber = request.getCallReferenceNumber();
			// boolean orInterrogation = request.getOrInterrogation();
			MAPExtensionContainer extensionContainer = request.getExtensionContainer();
			// AlertingPattern alertingPattern = request.getAlertingPattern();
			// boolean ccbsCall = request.getCcbsCall();
			// SupportedCamelPhases supportedCamelPhasesInInterrogatingNode =
			// request.getSupportedCamelPhasesInInterrogatingNode();
			// ExtExternalSignalInfo additionalSignalInfo =
			// request.getAdditionalSignalInfo();
			// boolean orNotSupportedInGMSC = request.getOrNotSupportedInGMSC();
			// boolean prePagingSupported = request.getPrePagingSupported();
			// boolean longFTNSupported = request.getLongFTNSupported();
			// boolean suppressVtCsi = request.getSuppressVtCsi();
			// OfferedCamel4CSIs offeredCamel4CSIsInInterrogatingNode =
			// request.getOfferedCamel4CSIsInInterrogatingNode();
			// boolean mtRoamingRetrySupported = request.getMtRoamingRetrySupported();
			// PagingArea pagingArea = request.getPagingArea();
			// EMLPPPriority callPriority = request.getCallPriority();
			// boolean mtrfIndicator = request.getMtrfIndicator();
			// ISDNAddressString oldMSCNumber = request.getOldMSCNumber();

			assertNotNull(imsi);
			assertEquals(imsi.getData(), "011220200198227");
			assertNotNull(mscNumber);
			assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(mscNumber.getAddress(), "22228");
			assertNull(msisdn);
			assertNull(lmsi);
			assertFalse(suppressionOfAnnouncement);
			assertNull(gmscAddress);
			assertNull(extensionContainer);

			ISDNAddressString roamingNumber = new ISDNAddressStringImpl(AddressNature.international_number,
					NumberingPlan.ISDN, "49883700292");

			d.addProvideRoamingNumberResponse(request.getInvokeId(), roamingNumber);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ProvideRoamingNumberResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + ProvideRoamingNumberResponse
		server.awaitSent(EventType.ProvideRoamingNumberResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ProvideRoamingNumberResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ProvideRoamingNumberResp);
			ProvideRoamingNumberResponse response = (ProvideRoamingNumberResponse) event.getEvent();

			ISDNAddressString roamingNumber = response.getRoamingNumber();
			MAPExtensionContainer extensionContainer = response.getExtensionContainer();
			boolean releaseResourcesSupported = response.getReleaseResourcesSupported();
			ISDNAddressString vmscAddress = response.getVmscAddress();

			assertNotNull(roamingNumber);
			assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(roamingNumber.getAddress(), "49883700292");
			assertFalse(releaseResourcesSupported);
			assertNull(extensionContainer);
			assertNull(vmscAddress);
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ProvideRoamingNumber);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ProvideRoamingNumberResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ProvideRoamingNumber);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ProvideRoamingNumberResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + istCommandRequest
	 * TC-END + istCommandResponse
	 * </pre>
	 */
	@Test
	public void testIstCommand() throws Exception {
		// 1. TC-BEGIN + IstCommandRequest
		client.sendIstCommand();
		client.awaitSent(EventType.IstCommand);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.IstCommand);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.IstCommand);
			IstCommandRequest request = (IstCommandRequest) event.getEvent();
			MAPDialogCallHandling d = request.getMAPDialog();

			IMSI imsi = request.getImsi();
			MAPExtensionContainer extensionContainer = request.getExtensionContainer();

			assertNotNull(imsi);
			assertEquals(imsi.getData(), "011220200198227");
			assertNotNull(extensionContainer);

			d.addIstCommandResponse(request.getInvokeId(), extensionContainer);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.IstCommandResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + IstCommandResponse
		server.awaitSent(EventType.IstCommandResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.IstCommandResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.IstCommandResp);
			IstCommandResponse response = (IstCommandResponse) event.getEvent();

			MAPExtensionContainer extensionContainer = response.getExtensionContainer();
			assertNotNull(extensionContainer);
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.IstCommand);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.IstCommandResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.IstCommand);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.IstCommandResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + InsertSubscriberDataRequest MAV V3
	 * TC-END + InsertSubscriberDataRequestResponse
	 * </pre>
	 */
	@Test
	public void testInsertSubscriberData_V3() throws Exception {
		// 1. TC-BEGIN + InsertSubscriberDataRequest
		client.sendInsertSubscriberData_V3();
		client.awaitSent(EventType.InsertSubscriberData);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InsertSubscriberData);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InsertSubscriberData);
			InsertSubscriberDataRequest request = (InsertSubscriberDataRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			assertNull(request.getProvisionedSS());
			assertNull(request.getODBData());
			assertTrue(request.getRoamingRestrictionDueToUnsupportedFeature());
			assertNull(request.getRegionalSubscriptionData());
			assertNull(request.getVbsSubscriptionData());
			assertNull(request.getVgcsSubscriptionData());
			assertNull(request.getVlrCamelSubscriptionInfo());
			assertNull(request.getNAEAPreferredCI());
			assertNull(request.getGPRSSubscriptionData());
			assertTrue(request.getRoamingRestrictedInSgsnDueToUnsupportedFeature());
			assertNull(request.getNetworkAccessMode());
			assertNull(request.getLSAInformation());
			assertTrue(request.getLmuIndicator());
			assertNull(request.getLCSInformation());
			assertNull(request.getIstAlertTimer());
			assertNull(request.getSuperChargerSupportedInHLR());
			assertNull(request.getMcSsInfo());
			assertNull(request.getCSAllocationRetentionPriority());
			assertNull(request.getSgsnCamelSubscriptionInfo());
			assertNull(request.getChargingCharacteristics());
			assertNull(request.getAccessRestrictionData());
			assertNull(request.getIcsIndicator());
			assertNull(request.getEpsSubscriptionData());
			assertNull(request.getCsgSubscriptionDataList());
			assertTrue(request.getUeReachabilityRequestIndicator());

			assertNull(request.getMmeName());
			assertNull(request.getSubscribedPeriodicRAUTAUtimer());
			assertTrue(request.getVplmnLIPAAllowed());
			assertNull(request.getMdtUserConsent());
			assertNull(request.getSubscribedPeriodicLAUtimer());

			IMSI imsi = request.getImsi();
			assertEquals(imsi.getData(), "1111122222");

			ISDNAddressString msisdn = request.getMsisdn();
			assertEquals(msisdn.getAddress(), "22234");
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

			Category category = request.getCategory();
			assertEquals(category.getData(), 5);

			SubscriberStatus subscriberStatus = request.getSubscriberStatus();
			assertEquals(subscriberStatus, SubscriberStatus.operatorDeterminedBarring);

			List<ExtBearerServiceCode> bearerServiceList = request.getBearerServiceList();
			assertNotNull(bearerServiceList);
			assertEquals(bearerServiceList.size(), 1);
			ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
			assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

			List<ExtTeleserviceCode> teleserviceList = request.getTeleserviceList();
			assertNotNull(teleserviceList);
			assertEquals(teleserviceList.size(), 1);
			ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
			assertEquals(extTeleserviceCode.getTeleserviceCodeValue(),
					TeleserviceCodeValue.allSpeechTransmissionServices);

			MAPExtensionContainer extensionContainer = request.getExtensionContainer();
			ISDNAddressString sgsnNumber = request.getSgsnNumber();
			assertNotNull(sgsnNumber);
			assertEquals(sgsnNumber.getAddress(), "22228");
			assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);

			ArrayList<SSCode> ssList = null;
			ODBGeneralData odbGeneralData = null;
			RegionalSubscriptionResponse regionalSubscriptionResponse = null;
			SupportedCamelPhases supportedCamelPhases = null;
			OfferedCamel4CSIs offeredCamel4CSIs = null;
			SupportedFeatures supportedFeatures = null;

			d.addInsertSubscriberDataResponse(request.getInvokeId(), teleserviceList, bearerServiceList, ssList,
					odbGeneralData, regionalSubscriptionResponse, supportedCamelPhases, extensionContainer,
					offeredCamel4CSIs, supportedFeatures);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.InsertSubscriberDataResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + InsertSubscriberDataResponse
		server.awaitSent(EventType.InsertSubscriberDataResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.InsertSubscriberDataResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.InsertSubscriberDataResp);
			InsertSubscriberDataResponse response = (InsertSubscriberDataResponse) event.getEvent();

			List<ExtTeleserviceCode> teleserviceList = response.getTeleserviceList();
			assertNotNull(teleserviceList);
			assertEquals(teleserviceList.size(), 1);
			ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
			assertEquals(extTeleserviceCode.getTeleserviceCodeValue(),
					TeleserviceCodeValue.allSpeechTransmissionServices);

			List<ExtBearerServiceCode> bearerServiceList = response.getBearerServiceList();
			assertNotNull(bearerServiceList);
			assertEquals(bearerServiceList.size(), 1);
			ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
			assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InsertSubscriberData);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.InsertSubscriberDataResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InsertSubscriberData);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.InsertSubscriberDataResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + InsertSubscriberDataRequest MAV V2
	 * TC-END + InsertSubscriberDataRequestResponse
	 * </pre>
	 */
	@Test
	public void testInsertSubscriberData_V2() throws Exception {
		// 1. TC-BEGIN + InsertSubscriberDataRequest
		client.sendInsertSubscriberData_V2();
		client.awaitSent(EventType.InsertSubscriberData);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InsertSubscriberData);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InsertSubscriberData);
			InsertSubscriberDataRequest request = (InsertSubscriberDataRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			assertNull(request.getProvisionedSS());
			assertNull(request.getODBData());
			assertTrue(request.getRoamingRestrictionDueToUnsupportedFeature());
			assertNull(request.getRegionalSubscriptionData());
			assertNull(request.getVbsSubscriptionData());
			assertNull(request.getVgcsSubscriptionData());
			assertNull(request.getVlrCamelSubscriptionInfo());
			assertNull(request.getNAEAPreferredCI());
			assertNull(request.getGPRSSubscriptionData());
			assertFalse(request.getRoamingRestrictedInSgsnDueToUnsupportedFeature());
			assertNull(request.getNetworkAccessMode());
			assertNull(request.getLSAInformation());
			assertFalse(request.getLmuIndicator());
			assertNull(request.getLCSInformation());
			assertNull(request.getIstAlertTimer());
			assertNull(request.getSuperChargerSupportedInHLR());
			assertNull(request.getMcSsInfo());
			assertNull(request.getCSAllocationRetentionPriority());
			assertNull(request.getSgsnCamelSubscriptionInfo());
			assertNull(request.getChargingCharacteristics());
			assertNull(request.getAccessRestrictionData());
			assertNull(request.getIcsIndicator());
			assertNull(request.getEpsSubscriptionData());
			assertNull(request.getCsgSubscriptionDataList());
			assertFalse(request.getUeReachabilityRequestIndicator());

			assertNull(request.getMmeName());
			assertNull(request.getSubscribedPeriodicRAUTAUtimer());
			assertFalse(request.getVplmnLIPAAllowed());
			assertNull(request.getMdtUserConsent());
			assertNull(request.getSubscribedPeriodicLAUtimer());

			IMSI imsi = request.getImsi();
			assertEquals(imsi.getData(), "1111122222");

			ISDNAddressString msisdn = request.getMsisdn();
			assertEquals(msisdn.getAddress(), "22234");
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

			Category category = request.getCategory();
			assertEquals(category.getData(), 5);

			SubscriberStatus subscriberStatus = request.getSubscriberStatus();
			assertEquals(subscriberStatus, SubscriberStatus.operatorDeterminedBarring);

			List<ExtBearerServiceCode> bearerServiceList = request.getBearerServiceList();
			assertNotNull(bearerServiceList);
			assertEquals(bearerServiceList.size(), 1);
			ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
			assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);

			List<ExtTeleserviceCode> teleserviceList = request.getTeleserviceList();
			assertNotNull(teleserviceList);
			assertEquals(teleserviceList.size(), 1);
			ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
			assertEquals(extTeleserviceCode.getTeleserviceCodeValue(),
					TeleserviceCodeValue.allSpeechTransmissionServices);
			assertNull(request.getExtensionContainer());

			ArrayList<SSCode> ssList = null;
			ODBGeneralDataImpl odbGeneralData = null;
			RegionalSubscriptionResponse regionalSubscriptionResponse = null;

			d.addInsertSubscriberDataResponse(request.getInvokeId(), teleserviceList, bearerServiceList, ssList,
					odbGeneralData, regionalSubscriptionResponse);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.InsertSubscriberDataResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + InsertSubscriberDataResponse
		server.awaitSent(EventType.InsertSubscriberDataResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.InsertSubscriberDataResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.InsertSubscriberDataResp);
			InsertSubscriberDataResponse response = (InsertSubscriberDataResponse) event.getEvent();

			List<ExtTeleserviceCode> teleserviceList = response.getTeleserviceList();
			assertNotNull(teleserviceList);
			assertEquals(teleserviceList.size(), 1);
			ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
			assertEquals(extTeleserviceCode.getTeleserviceCodeValue(),
					TeleserviceCodeValue.allSpeechTransmissionServices);

			List<ExtBearerServiceCode> bearerServiceList = response.getBearerServiceList();
			assertNotNull(bearerServiceList);
			assertEquals(bearerServiceList.size(), 1);
			ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
			assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.padAccessCA_9600bps);
			MAPExtensionContainerTest.CheckTestExtensionContainer(response.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InsertSubscriberData);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.InsertSubscriberDataResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InsertSubscriberData);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.InsertSubscriberDataResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + DeleteSubscriberDataRequest MAV V3
	 * TC-END + DeleteSubscriberDataRequestResponse
	 * </pre>
	 */
	@Test
	public void testDeleteSubscriberData_V3() throws Exception {
		// 1. TC-BEGIN + DeleteSubscriberDataRequest
		client.sendDeleteSubscriberData_V3();
		client.awaitSent(EventType.DeleteSubscriberData);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.DeleteSubscriberData);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DeleteSubscriberData);
			DeleteSubscriberDataRequest request = (DeleteSubscriberDataRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			assertEquals(request.getImsi().getData(), "1111122222");
			assertEquals(request.getBasicServiceList().size(), 2);
			assertEquals(request.getBasicServiceList().get(0).getExtBearerService().getBearerServiceCodeValue(),
					BearerServiceCodeValue.padAccessCA_9600bps);
			assertEquals(request.getBasicServiceList().get(1).getExtBearerService().getBearerServiceCodeValue(),
					BearerServiceCodeValue.padAccessCA_9600bps);
			assertEquals(request.getSsList().size(), 2);
			assertEquals(request.getSsList().get(0).getSupplementaryCodeValue(),
					SupplementaryCodeValue.allForwardingSS);
			assertEquals(request.getSsList().get(1).getSupplementaryCodeValue(),
					SupplementaryCodeValue.allLineIdentificationSS);
			assertFalse(request.getRoamingRestrictionDueToUnsupportedFeature());
			assertNull(request.getRegionalSubscriptionIdentifier());

			d.addDeleteSubscriberDataResponse(request.getInvokeId(), RegionalSubscriptionResponse.tooManyZoneCodes,
					null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.DeleteSubscriberDataResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + DeleteSubscriberDataResponse
		server.awaitSent(EventType.DeleteSubscriberDataResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.DeleteSubscriberDataResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DeleteSubscriberDataResp);
			DeleteSubscriberDataResponse response = (DeleteSubscriberDataResponse) event.getEvent();

			assertEquals(response.getRegionalSubscriptionResponse(), RegionalSubscriptionResponse.tooManyZoneCodes);
			assertNull(response.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.DeleteSubscriberData);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.DeleteSubscriberDataResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.DeleteSubscriberData);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.DeleteSubscriberDataResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + DeleteSubscriberDataRequest MAV V2
	 * TC-END + DeleteSubscriberDataRequestResponse
	 * </pre>
	 */
	@Test
	public void testDeleteSubscriberData_V2() throws Exception {
		// 1. TC-BEGIN + DeleteSubscriberDataRequest
		client.sendDeleteSubscriberData_V2();
		client.awaitSent(EventType.DeleteSubscriberData);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.DeleteSubscriberData);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DeleteSubscriberData);
			DeleteSubscriberDataRequest request = (DeleteSubscriberDataRequest) event.getEvent();
			MAPDialogMobility d = request.getMAPDialog();

			assertEquals(request.getImsi().getData(), "1111122222");
			assertNull(request.getBasicServiceList());
			assertNull(request.getSsList());
			assertTrue(request.getRoamingRestrictionDueToUnsupportedFeature());
			assertEquals(request.getRegionalSubscriptionIdentifier().getIntValue(), 10);

			d.addDeleteSubscriberDataResponse(request.getInvokeId(), null, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.DeleteSubscriberDataResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + DeleteSubscriberDataResponse
		server.awaitSent(EventType.DeleteSubscriberDataResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.DeleteSubscriberDataResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DeleteSubscriberDataResp);
			DeleteSubscriberDataResponse response = (DeleteSubscriberDataResponse) event.getEvent();

			assertNull(response.getRegionalSubscriptionResponse());
			assertNull(response.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.DeleteSubscriberData);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.DeleteSubscriberDataResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.DeleteSubscriberData);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.DeleteSubscriberDataResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + SendRoutingInformation MAV V3
	 * TC-END + SendRoutingInformationResponse
	 * </pre>
	 */
	@Test
	public void testSendRoutingInformation_V3() throws Exception {
		// 1. TC-BEGIN + SendRoutingInformationRequest
		client.sendSendRoutingInformation_V3();
		client.awaitSent(EventType.SendRoutingInformation);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendRoutingInformation);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendRoutingInformation);
			SendRoutingInformationRequest request = (SendRoutingInformationRequest) event.getEvent();
			MAPDialogCallHandling d = request.getMAPDialog();

			ISDNAddressString msisdn = request.getMsisdn();
			InterrogationType type = request.getInterogationType();
			ISDNAddressString gmsc = request.getGmscOrGsmSCFAddress();

			assertNotNull(msisdn);
			assertNotNull(type);
			assertNotNull(gmsc);
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(msisdn.getAddress(), "29113123311");
			assertEquals(gmsc.getAddressNature(), AddressNature.international_number);
			assertEquals(gmsc.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(gmsc.getAddress(), "49883700292");
			assertEquals(type, InterrogationType.forwarding);

			MAPParameterFactory mapParameterFactory = server.mapParameterFactory;

			IMSI imsi = mapParameterFactory.createIMSI("011220200198227");
			ISDNAddressString roamingNumber = mapParameterFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "79273605819");
			RoutingInfo routingInfo = mapParameterFactory.createRoutingInfo(roamingNumber);
			ExtendedRoutingInfo extRoutingInfo = mapParameterFactory.createExtendedRoutingInfo(routingInfo);

			CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = mapParameterFactory
					.createCellGlobalIdOrServiceAreaIdFixedLength(250, 1, 1111, 222);
			CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = mapParameterFactory
					.createCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdFixedLength);
			LocationInformationGPRS locationInformationGPRS = mapParameterFactory.createLocationInformationGPRS(
					cellGlobalIdOrServiceAreaIdOrLAI, null, null, null, null, null, false, null, false, null);
			SubscriberInfo subscriberInfo = mapParameterFactory.createSubscriberInfo(null, null, null,
					locationInformationGPRS, null, null, null, null, null);

			d.addSendRoutingInformationResponse(request.getInvokeId(), imsi, extRoutingInfo, null, false,
					subscriberInfo, null, null, false, null, null, null, null, null, null, null, null, null, null, null,
					null, null, null, false, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SendRoutingInformationResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + SendRoutingInformationResponse
		server.awaitSent(EventType.SendRoutingInformationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendRoutingInformationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendRoutingInformationResp);
			SendRoutingInformationResponse response = (SendRoutingInformationResponse) event.getEvent();

			IMSI imsi = response.getIMSI();
			ExtendedRoutingInfo extRoutingInfo = response.getExtendedRoutingInfo();
			RoutingInfo routingInfo = extRoutingInfo.getRoutingInfo();
			ISDNAddressString roamingNumber = routingInfo.getRoamingNumber();

			assertNotNull(imsi);
			assertEquals(imsi.getData(), "011220200198227");
			assertNotNull(roamingNumber);
			assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(roamingNumber.getAddress(), "79273605819");
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendRoutingInformation);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendRoutingInformationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendRoutingInformation);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendRoutingInformationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * MAV V3
	 * 
	 * <pre>
	 * TC-BEGIN + SendRoutingInformation
	 * TC-CONTINUE + SendRoutingInformationResponse-NonLast
	 * TC-CONTINUE
	 * TC-END + SendRoutingInformationResponse-Last
	 * </pre>
	 */
	@Test
	public void testSendRoutingInformation_V3_NonLast() throws Exception {
		// 1. TC-BEGIN + SendRoutingInformation
		client.sendSendRoutingInformation_V3();
		client.awaitSent(EventType.SendRoutingInformation);

		int invokeId;

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendRoutingInformation);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendRoutingInformation);
			SendRoutingInformationRequest request = (SendRoutingInformationRequest) event.getEvent();

			SendRoutingInformationRequestImplV3 ind = (SendRoutingInformationRequestImplV3) request;
			invokeId = ind.getInvokeId();

			ISDNAddressString msisdn = ind.getMsisdn();
			InterrogationType type = ind.getInterogationType();
			ISDNAddressString gmsc = ind.getGmscOrGsmSCFAddress();

			assertNotNull(msisdn);
			assertNotNull(type);
			assertNotNull(gmsc);
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
			assertTrue(msisdn.getAddress().equals("29113123311"));
			assertEquals(gmsc.getAddressNature(), AddressNature.international_number);
			assertEquals(gmsc.getNumberingPlan(), NumberingPlan.ISDN);
			assertTrue(gmsc.getAddress().equals("49883700292"));
			assertEquals(type, InterrogationType.forwarding);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			MAPDialogCallHandling d = (MAPDialogCallHandling) mapDialog;
			server.handleSent(EventType.SendRoutingInformationResp, null);

			MAPParameterFactory paramFactory = server.mapParameterFactory;
			IMSI imsi = paramFactory.createIMSI("011220200198227");

			ISDNAddressString roamingNumber = paramFactory.createISDNAddressString(AddressNature.international_number,
					NumberingPlan.ISDN, "79273605819");
			RoutingInfo routingInfo = paramFactory.createRoutingInfo(roamingNumber);
			ExtendedRoutingInfo extRoutingInfo = paramFactory.createExtendedRoutingInfo(routingInfo);

			CUGCheckInfo cugCheckInfo = null;
			boolean cugSubscriptionFlag = false;
			CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = paramFactory
					.createCellGlobalIdOrServiceAreaIdFixedLength(250, 1, 1111, 222);
			CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI = paramFactory
					.createCellGlobalIdOrServiceAreaIdOrLAI(cellGlobalIdOrServiceAreaIdFixedLength);
			LocationInformationGPRS locationInformationGPRS = paramFactory.createLocationInformationGPRS(
					cellGlobalIdOrServiceAreaIdOrLAI, null, null, null, null, null, false, null, false, null);
			SubscriberInfo subscriberInfo = paramFactory.createSubscriberInfo(null, null, null, locationInformationGPRS,
					null, null, null, null, null);
			ArrayList<SSCode> ssList = null;
			ExtBasicServiceCode basicService = null;
			boolean forwardingInterrogationRequired = false;
			ISDNAddressString vmscAddress = null;
			MAPExtensionContainer extensionContainer = null;
			NAEAPreferredCI naeaPreferredCI = null;
			CCBSIndicators ccbsIndicators = null;
			NumberPortabilityStatus nrPortabilityStatus = null;
			Integer istAlertTimer = null;
			SupportedCamelPhases supportedCamelPhases = null;
			OfferedCamel4CSIs offeredCamel4CSIs = null;
			RoutingInfo routingInfo2 = null;
			ArrayList<SSCode> ssList2 = null;
			ExtBasicServiceCode basicService2 = null;
			AllowedServices allowedServices = null;
			UnavailabilityCause unavailabilityCause = null;
			boolean releaseResourcesSupported = false;
			ExternalSignalInfo gsmBearerCapability = null;

			d.addSendRoutingInformationResponse_NonLast(invokeId, imsi, extRoutingInfo, cugCheckInfo,
					cugSubscriptionFlag, subscriberInfo, ssList, basicService, forwardingInterrogationRequired,
					vmscAddress, extensionContainer, naeaPreferredCI, ccbsIndicators, null, nrPortabilityStatus,
					istAlertTimer, supportedCamelPhases, offeredCamel4CSIs, routingInfo2, ssList2, basicService2,
					allowedServices, unavailabilityCause, releaseResourcesSupported, gsmBearerCapability);
			d.send(dummyCallback);
		}

		// 2. TC-CONTINUE + SendRoutingInformationResponse-NonLast
		server.awaitSent(EventType.SendRoutingInformationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendRoutingInformationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendRoutingInformationResp);
			SendRoutingInformationResponse response = (SendRoutingInformationResponse) event.getEvent();

			SendRoutingInformationResponseImplV3 ind = (SendRoutingInformationResponseImplV3) response;
			IMSI imsi = ind.getIMSI();
			ExtendedRoutingInfo extRoutingInfo = ind.getExtendedRoutingInfo();
			RoutingInfo routingInfo = extRoutingInfo.getRoutingInfo();
			ISDNAddressString roamingNumber = routingInfo.getRoamingNumber();

			assertNotNull(imsi);
			assertEquals(imsi.getData(), "011220200198227");
			assertNotNull(roamingNumber);
			assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
			assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(roamingNumber.getAddress(), "79273605819");

			assertNull(ind.getVmscAddress());

			assertTrue(ind.isReturnResultNotLast());
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			mapDialog.send(dummyCallback);
		}

		// 3. TC-CONTINUE
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			MAPDialogCallHandling d = (MAPDialogCallHandling) mapDialog;
			server.handleSent(EventType.SendRoutingInformationResp, null);

			ISDNAddressString vmscAddress = server.mapParameterFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "22233300");
			d.addSendRoutingInformationResponse(invokeId, null, null, null, false, null, null, null, false, vmscAddress,
					null, null, null, null, null, null, null, null, null, null, null, null, null, false, null);

			d.close(false, dummyCallback);
		}

		// 4. TC-END + SendRoutingInformationResponse-Last
		server.awaitSent(EventType.SendRoutingInformationResp);

		client.awaitReceived(EventType.SendRoutingInformationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendRoutingInformationResp);
			SendRoutingInformationResponse response = (SendRoutingInformationResponse) event.getEvent();

			SendRoutingInformationResponseImplV3 ind = (SendRoutingInformationResponseImplV3) response;
			ISDNAddressString isdn = ind.getVmscAddress();
			assertEquals(isdn.getAddress(), "22233300");
			assertFalse(ind.isReturnResultNotLast());

			assertNull(ind.getIMSI());

			assertFalse(ind.isReturnResultNotLast());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendRoutingInformation);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendRoutingInformationResp);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.SendRoutingInformationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendRoutingInformation);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendRoutingInformationResp);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendRoutingInformationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + SendRoutingInformation MAV V2
	 * TC-END + SendRoutingInformationResponse
	 * </pre>
	 */
	@Test
	public void testSendRoutingInformation_V2() throws Exception {
		// 1. TC-BEGIN + SendRoutingInformationRequest
		client.sendSendRoutingInformation_V2();
		client.awaitSent(EventType.SendRoutingInformation);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendRoutingInformation);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendRoutingInformation);
			SendRoutingInformationRequest request = (SendRoutingInformationRequest) event.getEvent();
			MAPDialogCallHandling d = request.getMAPDialog();

			ISDNAddressString msisdn = request.getMsisdn();
			assertNotNull(msisdn);
			assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
			assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(msisdn.getAddress(), "29113123311");

			MAPParameterFactory mapParameterFactory = server.mapParameterFactory;

			IMSI imsi = mapParameterFactory.createIMSI("011220200198227");
			ISDNAddressString roamingNumber = mapParameterFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "79273605819");
			RoutingInfoImpl routingInfo = new RoutingInfoImpl(roamingNumber);

			d.addSendRoutingInformationResponse(request.getInvokeId(), imsi, null, routingInfo);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SendRoutingInformationResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + SendRoutingInformationResponse
		server.awaitSent(EventType.SendRoutingInformationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendRoutingInformationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendRoutingInformationResp);
			SendRoutingInformationResponse response = (SendRoutingInformationResponse) event.getEvent();

			IMSI imsi = response.getIMSI();
			assertNull(response.getExtendedRoutingInfo());
			assertNotNull(imsi);
			assertEquals(imsi.getData(), "011220200198227");
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendRoutingInformation);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendRoutingInformationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendRoutingInformation);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendRoutingInformationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + sendIdentificationRequest MAV V2 
	 * TC-END + sendIdentificationResponse
	 * </pre>
	 */
	@Test
	public void testSendIdentification_V2() throws Exception {
		// 1. TC-BEGIN + SendIdentificationRequest
		client.sendSendIdentification_V2();
		client.awaitSent(EventType.SendIdentification);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendIdentification);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendIdentification);
			SendIdentificationRequest request = (SendIdentificationRequest) event.getEvent();

			MAPDialogMobility d = request.getMAPDialog();

			assertTrue(ByteBufUtil.equals(request.getTmsi().getValue(),
					Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4 })));

			IMSIImpl imsi = new IMSIImpl("011220200198227");
			d.addSendIdentificationResponse(request.getInvokeId(), imsi, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SendIdentificationResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + SendIdentificationResponse
		server.awaitSent(EventType.SendIdentificationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendIdentificationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendIdentificationResp);
			SendIdentificationResponse ind = (SendIdentificationResponse) event.getEvent();

			assertEquals(ind.getImsi().getData(), "011220200198227");
			assertNull(ind.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendIdentification);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendIdentificationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendIdentification);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendIdentificationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + sendIdentificationRequest MAV V3 
	 * TC-END + sendIdentificationResponse
	 * </pre>
	 */
	@Test
	public void testSendIdentification_V3() throws Exception {
		// 1. TC-BEGIN + SendIdentificationRequest
		client.sendSendIdentification_V3();
		client.awaitSent(EventType.SendIdentification);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendIdentification);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendIdentification);
			SendIdentificationRequest request = (SendIdentificationRequest) event.getEvent();

			MAPDialogMobility d = request.getMAPDialog();

			assertTrue(ByteBufUtil.equals(request.getTmsi().getValue(),
					Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4 })));

			IMSIImpl imsi = new IMSIImpl("011220200198227");
			d.addSendIdentificationResponse(request.getInvokeId(), imsi, null, null, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SendIdentificationResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + SendIdentificationResponse
		server.awaitSent(EventType.SendIdentificationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendIdentificationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendIdentificationResp);
			SendIdentificationResponse ind = (SendIdentificationResponse) event.getEvent();

			assertEquals(ind.getImsi().getData(), "011220200198227");
			assertNull(ind.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendIdentification);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendIdentificationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendIdentification);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendIdentificationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + UpdateGprsLocationRequest MAP V3 
	 * TC-END + UpdateGprsLocationResponse
	 * </pre>
	 */
	@Test
	public void testUpdateGprsLocation_V3() throws Exception {
		// 1. TC-BEGIN + UpdateGprsLocationRequest
		client.sendUpdateGprsLocation_V3();
		client.awaitSent(EventType.UpdateGprsLocation);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.UpdateGprsLocation);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.UpdateGprsLocation);
			UpdateGprsLocationRequest request = (UpdateGprsLocationRequest) event.getEvent();

			MAPDialogMobility d = ((UpdateGprsLocationRequestImpl) request).getMAPDialog();

			assertEquals("111222", request.getImsi().getData());
			assertEquals("22228", request.getSgsnNumber().getAddress());
			assertEquals(AddressNature.international_number, request.getSgsnNumber().getAddressNature());
			assertEquals(NumberingPlan.ISDN, request.getSgsnNumber().getNumberingPlan());
			assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(request.getExtensionContainer()));
			assertTrue(request.getSGSNCapability().getSolsaSupportIndicator());
			assertTrue(request.getInformPreviousNetworkEntity());
			assertTrue(request.getPsLCSNotSupportedByUE());
			assertEquals("12341234", request.getADDInfo().getImeisv().getIMEI());
			assertTrue(request.getEPSInfo().getIsrInformation().getCancelSGSN());
			assertTrue(request.getServingNodeTypeIndicator());
			assertTrue(request.getSkipSubscriberDataUpdate());
			assertEquals(UsedRATType.gan, request.getUsedRATType());
			assertTrue(request.getGprsSubscriptionDataNotNeeded());
			assertTrue(request.getNodeTypeIndicator());
			assertTrue(request.getAreaRestricted());
			assertTrue(request.getUeReachableIndicator());
			assertTrue(request.getEpsSubscriptionDataNotNeeded());
			assertEquals(UESRVCCCapability.ueSrvccSupported, request.getUESRVCCCapability());

			ISDNAddressStringImpl hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number,
					NumberingPlan.ISDN, "22228");
			d.addUpdateGprsLocationResponse(((UpdateGprsLocationRequestImpl) request).getInvokeId(), hlrNumber, null,
					true, true);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.UpdateGprsLocationResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + UpdateGprsLocationResponse
		server.awaitSent(EventType.UpdateGprsLocationResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.UpdateGprsLocationResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.UpdateGprsLocationResp);
			UpdateGprsLocationResponse response = (UpdateGprsLocationResponse) event.getEvent();

			assertEquals("22228", response.getHlrNumber().getAddress());
			assertEquals(AddressNature.international_number, response.getHlrNumber().getAddressNature());
			assertEquals(NumberingPlan.ISDN, response.getHlrNumber().getNumberingPlan());
			assertTrue(response.getAddCapability());
			assertTrue(response.getSgsnMmeSeparationSupported());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.UpdateGprsLocation);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.UpdateGprsLocationResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.UpdateGprsLocation);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.UpdateGprsLocationResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + PurgeMSRequest MAP V3 
	 * TC-END + PurgeMSResponse
	 * </pre>
	 */
	@Test
	public void testPurgeMSRequest_V3() throws Exception {
		// 1. TC-BEGIN + PurgeMSRequest
		client.sendPurgeMS_V3();
		client.awaitSent(EventType.PurgeMS);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.PurgeMS);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.PurgeMS);
			PurgeMSRequest request = (PurgeMSRequest) event.getEvent();

			MAPDialogMobility d = ((PurgeMSRequestImplV3) request).getMAPDialog();

			assertEquals("111222", request.getImsi().getData());
			assertEquals("22228", request.getSgsnNumber().getAddress());

			d.addPurgeMSResponse(((PurgeMSRequestImplV3) request).getInvokeId(), true, true, null, true);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.PurgeMSResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + PurgeMSResponse
		server.awaitSent(EventType.PurgeMSResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.PurgeMSResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.PurgeMSResp);
			PurgeMSResponse ind = (PurgeMSResponse) event.getEvent();

			assertTrue(ind.getFreezeMTMSI());
			assertTrue(ind.getFreezePTMSI());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.PurgeMS);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.PurgeMSResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.PurgeMS);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.PurgeMSResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + PurgeMSRequest MAP V2 
	 * TC-END + PurgeMSResponse
	 * </pre>
	 */
	@Test
	public void testPurgeMSRequest_V2() throws Exception {
		// 1. TC-BEGIN + PurgeMSRequest
		client.sendPurgeMS_V2();
		client.awaitSent(EventType.PurgeMS);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.PurgeMS);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.PurgeMS);
			PurgeMSRequest request = (PurgeMSRequest) event.getEvent();

			MAPDialogMobility d = ((PurgeMSRequestImplV1) request).getMAPDialog();

			assertEquals(request.getImsi().getData(), "111222");
			assertEquals(request.getVlrNumber().getAddress(), "22228");

			d.addPurgeMSResponse(((PurgeMSRequestImplV1) request).getInvokeId(), false, false, null, false);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.PurgeMSResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + PurgeMSResponse
		server.awaitSent(EventType.PurgeMSResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.PurgeMSResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.PurgeMSResp);
			PurgeMSResponse ind = (PurgeMSResponse) event.getEvent();

			assertFalse(ind.getFreezeMTMSI());
			assertFalse(ind.getFreezePTMSI());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.PurgeMS);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.PurgeMSResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.PurgeMS);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.PurgeMSResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + reset MAP V1
	 * </pre>
	 */
	@Test
	public void testResetRequest_V1() throws Exception {
		// 1. TC-BEGIN + ResetRequest
		client.sendReset_V1();
		client.awaitSent(EventType.Reset);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.Reset);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.Reset);
			ResetRequest request = (ResetRequest) event.getEvent();

			assertEquals(request.getNetworkResource(), NetworkResource.hlr);
			assertEquals(request.getHlrNumber().getAddress(), "22220000");
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();
			mapDialog.release();
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Reset);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.Reset);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + forwardCheckSSIndicationRequest MAP V3
	 * </pre>
	 */
	@Test
	public void testForwardCheckSSIndicationRequest_V3() throws Exception {
		// 1. TC-BEGIN + forwardCheckSSIndicationRequest
		client.sendForwardCheckSSIndicationRequest_V3();
		client.awaitSent(EventType.ForwardCheckSSIndication);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ForwardCheckSSIndication);
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();
			mapDialog.release();
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ForwardCheckSSIndication);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ForwardCheckSSIndication);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + RestoreDataRequest 
	 * TC-END + RestoreDataResponse
	 * </pre>
	 */
	@Test
	public void testRestoreDataRequest() throws Exception {
		// 1. TC-BEGIN + RestoreDataRequest
		client.sendRestoreData();
		client.awaitSent(EventType.RestoreData);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.RestoreData);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.RestoreData);
			RestoreDataRequest request = (RestoreDataRequest) event.getEvent();

			MAPDialogMobility d = ((RestoreDataRequestImpl) request).getMAPDialog();

			assertEquals(request.getImsi().getData(), "00000222229999");

			ISDNAddressString hlrNumber = server.mapParameterFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "9992222");
			d.addRestoreDataResponse(((RestoreDataRequestImpl) request).getInvokeId(), hlrNumber, false, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.RestoreDataResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + RestoreDataResponse
		server.awaitSent(EventType.RestoreDataResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RestoreDataResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.RestoreDataResp);
			RestoreDataResponse ind = (RestoreDataResponse) event.getEvent();

			assertEquals(ind.getHlrNumber().getAddress(), "9992222");
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.RestoreData);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RestoreDataResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.RestoreData);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.RestoreDataResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + SendImsiRequest 
	 * TC-END + SendImsiResponse
	 * </pre>
	 */
	@Test
	public void testSendImsiRequest() throws Exception {
		// 1. TC-BEGIN + SendImsiRequest
		client.sendSendImsi();
		client.awaitSent(EventType.SendImsi);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendImsi);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendImsi);
			SendImsiRequest request = (SendImsiRequest) event.getEvent();

			MAPDialogOam d = ((SendImsiRequestImpl) request).getOamMAPDialog();

			assertEquals(request.getMsisdn().getAddress(), "9992222");

			IMSI imsi = server.mapParameterFactory.createIMSI("88888999991111");
			d.addSendImsiResponse(((SendImsiRequestImpl) request).getInvokeId(), imsi);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SendImsiResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + SendImsiResponse
		server.awaitSent(EventType.SendImsiResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendImsiResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendImsiResp);
			SendImsiResponse ind = (SendImsiResponse) event.getEvent();

			assertEquals(ind.getImsi().getData(), "88888999991111");
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendImsi);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendImsiResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendImsi);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendImsiResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + RegisterSSRequest
	 * TC-END + RegisterSSResponse with parameter
	 * </pre>
	 */
	@Test
	public void testRegisterSSRequest() throws Exception {
		// 1. TC-BEGIN + RegisterSSRequest
		client.sendRegisterSS();
		client.awaitSent(EventType.RegisterSS);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.RegisterSS);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.RegisterSS);
			RegisterSSRequest request = (RegisterSSRequest) event.getEvent();

			MAPDialogSupplementary d = ((RegisterSSRequestImpl) request).getMAPDialog();

			assertEquals(request.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
			assertEquals(request.getBasicService().getBearerService().getBearerServiceCodeValue(),
					BearerServiceCodeValue.padAccessCA_9600bps);

			assertEquals(request.getMAPDialog().getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(request.getMAPDialog().getReceivedOrigReference().getAddress(), "31628968300");
			assertEquals(request.getMAPDialog().getReceivedDestReference().getNumberingPlan(),
					NumberingPlan.land_mobile);
			assertEquals(request.getMAPDialog().getReceivedDestReference().getAddress(), "204208300008002");

			SSCode ssCode = server.mapParameterFactory.createSSCode(SupplementaryCodeValue.cfu);
			SSStatus ssStatus = server.mapParameterFactory.createSSStatus(false, false, false, true);
			SSData ssData = server.mapParameterFactory.createSSData(ssCode, ssStatus, null, null, null, null);
			SSInfo ssInfo = server.mapParameterFactory.createSSInfo(ssData);
			d.addRegisterSSResponse(request.getInvokeId(), ssInfo);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.RegisterSSResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + RegisterSSResponse with parameter
		server.awaitSent(EventType.RegisterSSResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RegisterSSResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.RegisterSSResp);
			RegisterSSResponse ind = (RegisterSSResponse) event.getEvent();

			SSData ssData = ind.getSsInfo().getSsData();
			assertEquals(ssData.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
			assertTrue(ssData.getSsStatus().getABit());
			assertFalse(ssData.getSsStatus().getQBit());
			assertFalse(ssData.getSsStatus().getPBit());
			assertFalse(ssData.getSsStatus().getRBit());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.RegisterSS);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RegisterSSResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.RegisterSS);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.RegisterSSResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + EraseSSRequest
	 * TC-END + EraseSSResponse without parameter
	 * </pre>
	 */
	@Test
	public void testEraseSSRequest() throws Exception {
		// 1. TC-BEGIN + EraseSSRequest
		client.sendEraseSS();
		client.awaitSent(EventType.EraseSS);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.EraseSS);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.EraseSS);
			EraseSSRequest request = (EraseSSRequest) event.getEvent();

			MAPDialogSupplementary d = request.getMAPDialog();

			assertEquals(request.getMAPDialog().getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(request.getMAPDialog().getReceivedOrigReference().getAddress(), "31628968300");
			assertEquals(request.getMAPDialog().getReceivedDestReference().getNumberingPlan(),
					NumberingPlan.land_mobile);
			assertEquals(request.getMAPDialog().getReceivedDestReference().getAddress(), "204208300008002");

			assertEquals(request.getSsForBSCode().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);

			d.addEraseSSResponse(request.getInvokeId(), null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.EraseSSResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + EraseSSResponse without parameter
		server.awaitSent(EventType.EraseSSResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.EraseSSResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.EraseSSResp);
			EraseSSResponse ind = (EraseSSResponse) event.getEvent();

			assertNull(ind.getSsInfo());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.EraseSS);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.EraseSSResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.EraseSS);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.EraseSSResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + ActivateSSRequest
	 * TC-END + ActivateSSResponse with parameter
	 * </pre>
	 */
	@Test
	public void testActivateSSRequest() throws Exception {
		// 1. TC-BEGIN + ActivateSSRequest
		client.sendActivateSS();
		client.awaitSent(EventType.ActivateSS);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ActivateSS);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ActivateSS);
			ActivateSSRequest request = (ActivateSSRequest) event.getEvent();

			MAPDialogSupplementary d = request.getMAPDialog();

			assertEquals(request.getMAPDialog().getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(request.getMAPDialog().getReceivedOrigReference().getAddress(), "31628968300");
			assertEquals(request.getMAPDialog().getReceivedDestReference().getNumberingPlan(),
					NumberingPlan.land_mobile);
			assertEquals(request.getMAPDialog().getReceivedDestReference().getAddress(), "204208300008002");

			assertEquals(request.getSsForBSCode().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);

			MAPParameterFactory paramFactory = server.mapParameterFactory;

			SSCode ssCode = paramFactory.createSSCode(SupplementaryCodeValue.cfu);
			SSStatus ssStatus = paramFactory.createSSStatus(false, false, false, true);
			SSData ssData = paramFactory.createSSData(ssCode, ssStatus, null, null, null, null);
			SSInfo ssInfo = paramFactory.createSSInfo(ssData);
			d.addActivateSSResponse(request.getInvokeId(), ssInfo);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ActivateSSResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + ActivateSSResponse with parameter
		server.awaitSent(EventType.ActivateSSResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ActivateSSResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ActivateSSResp);
			ActivateSSResponse ind = (ActivateSSResponse) event.getEvent();

			SSData ssData = ind.getSsInfo().getSsData();
			assertEquals(ssData.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
			assertTrue(ssData.getSsStatus().getABit());
			assertFalse(ssData.getSsStatus().getQBit());
			assertFalse(ssData.getSsStatus().getPBit());
			assertFalse(ssData.getSsStatus().getRBit());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ActivateSS);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ActivateSSResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ActivateSS);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ActivateSSResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + DeactivateSSRequest
	 * TC-END + DeactivateSSResponse without parameter
	 * </pre>
	 */
	@Test
	public void testDeactivateSSRequest() throws Exception {
		// 1. TC-BEGIN + DeactivateSSRequest
		client.sendDeactivateSS();
		client.awaitSent(EventType.DeactivateSS);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.DeactivateSS);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DeactivateSS);
			DeactivateSSRequest request = (DeactivateSSRequest) event.getEvent();

			MAPDialogSupplementary d = request.getMAPDialog();

			assertEquals(request.getMAPDialog().getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(request.getMAPDialog().getReceivedOrigReference().getAddress(), "31628968300");
			assertEquals(request.getMAPDialog().getReceivedDestReference().getNumberingPlan(),
					NumberingPlan.land_mobile);
			assertEquals(request.getMAPDialog().getReceivedDestReference().getAddress(), "204208300008002");

			assertEquals(request.getSsForBSCode().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);

			d.addDeactivateSSResponse(request.getInvokeId(), null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.DeactivateSSResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + DeactivateSSResponse without parameter
		server.awaitSent(EventType.DeactivateSSResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.DeactivateSSResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DeactivateSSResp);
			DeactivateSSResponse ind = (DeactivateSSResponse) event.getEvent();

			assertNull(ind.getSsInfo());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.DeactivateSS);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.DeactivateSSResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.DeactivateSS);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.DeactivateSSResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + InterrogateSSRequest
	 * TC-END + InterrogateSSResponse
	 * </pre>
	 */
	@Test
	public void testInterrogateSSRequest() throws Exception {
		// 1. TC-BEGIN + InterrogateSSRequest
		client.sendInterrogateSS();
		client.awaitSent(EventType.InterrogateSS);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InterrogateSS);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InterrogateSS);
			InterrogateSSRequest request = (InterrogateSSRequest) event.getEvent();

			MAPDialogSupplementary d = request.getMAPDialog();

			assertEquals(request.getMAPDialog().getReceivedOrigReference().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(request.getMAPDialog().getReceivedOrigReference().getAddress(), "31628968300");
			assertEquals(request.getMAPDialog().getReceivedDestReference().getNumberingPlan(),
					NumberingPlan.land_mobile);
			assertEquals(request.getMAPDialog().getReceivedDestReference().getAddress(), "204208300008002");

			assertEquals(request.getSsForBSCode().getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);

			SSStatus ssStatus = server.mapParameterFactory.createSSStatus(false, true, false, false);
			GenericServiceInfo genericServiceInfo = server.mapParameterFactory.createGenericServiceInfo(ssStatus, null,
					null, null, null, null, null, null);
			d.addInterrogateSSResponse_GenericServiceInfo(request.getInvokeId(), genericServiceInfo);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.InterrogateSSResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + InterrogateSSResponse
		server.awaitSent(EventType.InterrogateSSResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.InterrogateSSResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.InterrogateSSResp);
			InterrogateSSResponse ind = (InterrogateSSResponse) event.getEvent();

			assertTrue(ind.getGenericServiceInfo().getSsStatus().getPBit());
			assertFalse(ind.getGenericServiceInfo().getSsStatus().getABit());
			assertFalse(ind.getGenericServiceInfo().getSsStatus().getQBit());
			assertFalse(ind.getGenericServiceInfo().getSsStatus().getRBit());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InterrogateSS);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.InterrogateSSResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InterrogateSS);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.InterrogateSSResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + ReadyForSMRequest
	 * TC-END + ReadyForSMResponse
	 * </pre>
	 */
	@Test
	public void testReadyForSMRequest() throws Exception {
		// 1. TC-BEGIN + ReadyForSMRequest
		client.sendReadyForSM();
		client.awaitSent(EventType.ReadyForSM);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ReadyForSM);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ReadyForSM);
			ReadyForSMRequest request = (ReadyForSMRequest) event.getEvent();

			MAPDialogSms d = request.getMAPDialog();

			assertEquals(request.getImsi().getData(), "88888777773333");
			assertEquals(request.getAlertReason(), AlertReason.memoryAvailable);

			d.addReadyForSMResponse(request.getInvokeId(), null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ReadyForSMResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + ReadyForSMResponse
		server.awaitSent(EventType.ReadyForSMResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ReadyForSMResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ReadyForSMResp);
			ReadyForSMResponse ind = (ReadyForSMResponse) event.getEvent();

			assertNull(ind.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ReadyForSM);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ReadyForSMResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ReadyForSM);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ReadyForSMResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC - BEGIN + noteSubscriberPresent
	 * </pre>
	 */
	@Test
	public void testNoteSubscriberPresentRequest() throws Exception {
		// 1. TC-BEGIN + noteSubscriberPresent
		client.sendNoteSubscriberPresent();
		client.awaitSent(EventType.NoteSubscriberPresent);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.NoteSubscriberPresent);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.NoteSubscriberPresent);
			NoteSubscriberPresentRequest request = (NoteSubscriberPresentRequest) event.getEvent();

			assertEquals(request.getIMSI().getData(), "88888777773333");
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			mapDialog.release();
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.NoteSubscriberPresent);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.NoteSubscriberPresent);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + SendRoutingInfoForGprsRequest
	 * TC-END + SendRoutingInfoForGprsResponse
	 * </pre>
	 */
	@Test
	public void testSendRoutingInfoForGprsRequest() throws Exception {
		// 1. TC-BEGIN + SendRoutingInfoForGprsRequest
		client.sendSendRoutingInfoForGprsRequest();
		client.awaitSent(EventType.SendRoutingInfoForGprs);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.SendRoutingInfoForGprs);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SendRoutingInfoForGprs);
			SendRoutingInfoForGprsRequest request = (SendRoutingInfoForGprsRequest) event.getEvent();

			MAPDialogPdpContextActivation d = request.getMAPDialog();

			byte[] addressData = new byte[] { (byte) 192, (byte) 168, 4, 22 };
			assertEquals(request.getImsi().getData(), "88888777773333");
			assertEquals(request.getGgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
			assertTrue(ByteBufUtil.equals(request.getGgsnAddress().getGSNAddressData(),
					Unpooled.wrappedBuffer(addressData)));
			assertEquals(request.getGgsnNumber().getAddress(), "31628838002");

			GSNAddress sgsnAddress = server.mapParameterFactory.createGSNAddress(GSNAddressAddressType.IPv4,
					Unpooled.wrappedBuffer(addressData));
			d.addSendRoutingInfoForGprsResponse(request.getInvokeId(), sgsnAddress, null, null, null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.SendRoutingInfoForGprsResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + SendRoutingInfoForGprsResponse
		server.awaitSent(EventType.SendRoutingInfoForGprsResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.SendRoutingInfoForGprsResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendRoutingInfoForGprsResp);
			SendRoutingInfoForGprsResponse ind = (SendRoutingInfoForGprsResponse) event.getEvent();

			byte[] addressData = new byte[] { (byte) 192, (byte) 168, 4, 22 };
			assertEquals(ind.getSgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
			assertTrue(
					ByteBufUtil.equals(ind.getSgsnAddress().getGSNAddressData(), Unpooled.wrappedBuffer(addressData)));
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.SendRoutingInfoForGprs);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.SendRoutingInfoForGprsResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.SendRoutingInfoForGprs);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SendRoutingInfoForGprsResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + ActivateTraceModeRequest (OAM ACN)
	 * TC-END + ActivateTraceModeResponse (empty)
	 * </pre>
	 */
	@Test
	public void testActivateTraceModeRequest_Oam() throws Exception {
		// 1. TC-BEGIN + ActivateTraceModeRequest (OAM ACN)
		client.sendActivateTraceModeRequest_Oam();
		client.awaitSent(EventType.ActivateTraceMode);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ActivateTraceMode);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ActivateTraceMode);
			ActivateTraceModeRequest_Oam request = (ActivateTraceModeRequest_Oam) event.getEvent();

			MAPDialogOam d = request.getOamMAPDialog();
			assertEquals(request.getImsi().getData(), "88888777773333");

			byte[] traceReferenceData = new byte[] { 19 };
			assertTrue(ByteBufUtil.equals(request.getTraceReference().getValue(),
					Unpooled.wrappedBuffer(traceReferenceData)));
			assertEquals(request.getTraceType().getData(), 21);

			d.addActivateTraceModeResponse(request.getInvokeId(), null, false);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ActivateTraceModeResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + ActivateTraceModeResponse (empty)
		server.awaitSent(EventType.ActivateTraceModeResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ActivateTraceModeResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ActivateTraceModeResp);
			ActivateTraceModeResponse_Oam ind = (ActivateTraceModeResponse_Oam) event.getEvent();

			assertNull(ind.getExtensionContainer());
			assertFalse(ind.getTraceSupportIndicator());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ActivateTraceMode);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ActivateTraceModeResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ActivateTraceMode);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ActivateTraceModeResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + ActivateTraceModeRequest (Mobility ACN)
	 * TC-END + ActivateTraceModeResponse (with primitive)
	 * </pre>
	 */
	@Test
	public void testActivateTraceModeRequest_Mobility() throws Exception {
		// 1. TC-BEGIN + ActivateTraceModeRequest (Mobility ACN)
		client.sendActivateTraceModeRequest_Mobility();
		client.awaitSent(EventType.ActivateTraceMode);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ActivateTraceMode);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ActivateTraceMode);
			ActivateTraceModeRequest_Mobility request = (ActivateTraceModeRequest_Mobility) event.getEvent();

			MAPDialogMobility d = request.getMAPDialog();
			assertEquals(request.getImsi().getData(), "88888777773333");

			byte[] traceReferenceData = new byte[] { 19 };
			assertTrue(ByteBufUtil.equals(request.getTraceReference().getValue(),
					Unpooled.wrappedBuffer(traceReferenceData)));
			assertEquals(request.getTraceType().getData(), 21);

			d.addActivateTraceModeResponse(request.getInvokeId(), null, true);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.ActivateTraceModeResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + ActivateTraceModeResponse (with primitive)
		server.awaitSent(EventType.ActivateTraceModeResp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ActivateTraceModeResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ActivateTraceModeResp);
			ActivateTraceModeResponse_Mobility ind = (ActivateTraceModeResponse_Mobility) event.getEvent();

			assertNull(ind.getExtensionContainer());
			assertTrue(ind.getTraceSupportIndicator());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ActivateTraceMode);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ActivateTraceModeResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ActivateTraceMode);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ActivateTraceModeResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + RegisterPasswordRequest
	 * TC-CONTINUE + GetPasswordRequest
	 * TC-CONTINUE + GetPasswordResponse
	 * TC-END + RegisterPasswordResponse
	 * </pre>
	 */
	@Test
	public void testRegisterPassword_GetPassword() throws Exception {
		// 1. TC-BEGIN + RegisterPasswordRequest
		client.sendRegisterPassword();
		client.awaitSent(EventType.RegisterPassword);

		int registerPasswordInvokeId = Integer.MIN_VALUE;

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.RegisterPassword);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.RegisterPassword);
			RegisterPasswordRequest request = (RegisterPasswordRequest) event.getEvent();

			MAPDialogSupplementary d = request.getMAPDialog();
			assertEquals(request.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allCondForwardingSS);

			registerPasswordInvokeId = request.getInvokeId();
			d.addGetPasswordRequest(registerPasswordInvokeId, GuidanceInfo.enterNewPW);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.GetPassword, null);
			mapDialog.send(dummyCallback);
		}

		// 2. TC-CONTINUE + GetPasswordRequest
		server.awaitSent(EventType.GetPassword);

		int getPasswordInvokeId = Integer.MIN_VALUE;

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.GetPassword);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.GetPassword);
			GetPasswordRequest ind = (GetPasswordRequest) event.getEvent();

			getPasswordInvokeId = ind.getInvokeId();
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			Password password = client.mapParameterFactory.createPassword("9876");
			((MAPDialogSupplementary) mapDialog).addGetPasswordResponse(getPasswordInvokeId, password);
			client.handleSent(EventType.GetPasswordResp, null);

			mapDialog.send(dummyCallback);
		}

		// 3. TC-CONTINUE + GetPasswordResponse
		client.awaitSent(EventType.GetPasswordResp);

		server.awaitReceived(EventType.GetPasswordResp);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.GetPasswordResp);
			GetPasswordResponse request = (GetPasswordResponse) event.getEvent();

			MAPDialogSupplementary d = request.getMAPDialog();

			assertEquals(request.getPassword().getData(), "9876");

			Password password = server.mapParameterFactory.createPassword("5555");
			d.addRegisterPasswordResponse(registerPasswordInvokeId, password);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.RegisterPasswordResp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 4. TC-END + RegisterPasswordResponse
		server.awaitSent(EventType.RegisterPasswordResp);

		client.awaitReceived(EventType.RegisterPasswordResp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.RegisterPasswordResp);
			RegisterPasswordResponse ind = (RegisterPasswordResponse) event.getEvent();

			assertEquals(ind.getPassword().getData(), "5555");
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.RegisterPassword);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.GetPassword);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.GetPasswordResp);
		clientExpected.addReceived(EventType.RegisterPasswordResp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.RegisterPassword);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.GetPassword);
		serverExpected.addReceived(EventType.GetPasswordResp);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.RegisterPasswordResp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + AuthenticationFailureReportRequest
	 * TC-END + AuthenticationFailureReportResponse (without parameter)
	 * </pre>
	 */
	@Test
	public void testAuthenticationFailureReport() throws Exception {
		// 1. TC-BEGIN + AuthenticationFailureReportRequest
		client.sendAuthenticationFailureReport();
		client.awaitSent(EventType.AuthenticationFailureReport);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.AuthenticationFailureReport);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.AuthenticationFailureReport);
			AuthenticationFailureReportRequest ind = (AuthenticationFailureReportRequest) event.getEvent();

			MAPDialogMobility d = ind.getMAPDialog();

			assertEquals(ind.getImsi().getData(), "88888777773333");
			assertEquals(ind.getFailureCause(), FailureCause.wrongNetworkSignature);
			assertNull(ind.getExtensionContainer());

			d.addAuthenticationFailureReportResponse(ind.getInvokeId(), null);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			MAPDialog mapDialog = (MAPDialog) event.getEvent();

			server.handleSent(EventType.AuthenticationFailureReport_Resp, null);
			mapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + AuthenticationFailureReportResponse (without parameter)
		server.awaitSent(EventType.AuthenticationFailureReport_Resp);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.AuthenticationFailureReport_Resp);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.AuthenticationFailureReport_Resp);
			AuthenticationFailureReportResponse ind = (AuthenticationFailureReportResponse) event.getEvent();

			assertNull(ind.getExtensionContainer());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.AuthenticationFailureReport);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.AuthenticationFailureReport_Resp);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.AuthenticationFailureReport);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.AuthenticationFailureReport_Resp);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}
}
