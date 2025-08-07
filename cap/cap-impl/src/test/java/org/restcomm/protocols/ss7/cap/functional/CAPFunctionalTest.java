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

package org.restcomm.protocols.ss7.cap.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.cap.CAPStackImpl;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.CAPProvider;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.cap.api.errors.UnavailableNetworkResource;
import org.restcomm.protocols.ss7.cap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswer;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CAPDialogGprs;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ConnectGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ContinueGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EventReportGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.FurnishChargingInformationGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ReleaseGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ResetTimerGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.SendChargingInformationGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPDialogSms;
import org.restcomm.protocols.ss7.cap.api.service.sms.ConnectSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ContinueSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.EventReportSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.FurnishChargingInformationSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.InitialDPSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ReleaseSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.RequestReportSMSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ResetTimerSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.RPCause;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.restcomm.protocols.ss7.cap.functional.listeners.Client;
import org.restcomm.protocols.ss7.cap.functional.listeners.EventType;
import org.restcomm.protocols.ss7.cap.functional.listeners.Server;
import org.restcomm.protocols.ss7.cap.functional.wrappers.CAPStackImplWrapper;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCallImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AOCSubsequentImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.AOCGPRSImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ChargingResultImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ElapsedTimeImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.FCIBCCCAMELSequence1GprsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.FreeFormatDataGprsImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSEventSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAI_GSM0224;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedDigits;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.FCIBCCCAMELSequence1;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeDurationChargingResult;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeInformation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Tone;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingLevel;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CAI_GSM0224Impl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.FreeFormatDataImpl;
import org.restcomm.protocols.ss7.commonapp.gap.BasicGapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CalledAddressAndServiceImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapIndicatorsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.NAINumber;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEvent;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventFactory;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventUtils;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

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
public class CAPFunctionalTest extends SccpHarness {
	private CAPStackImpl stack1;
	private CAPStackImpl stack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;

	@Override
	protected int getSSN() {
		return 146;
	}

	@Override
	protected int getSSN2() {
		return 146;
	}

	@Before
	public void beforeEach() throws Exception {
		super.sccpStack1Name = "CAPFunctionalTestSccpStack1";
		super.sccpStack2Name = "CAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 146);
		peer2Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 146);

		stack1 = new CAPStackImplWrapper(super.sccpProvider1, 146, workerPool);
		stack2 = new CAPStackImplWrapper(super.sccpProvider2, 146, workerPool);

		stack1.start();
		stack2.start();

		client = new Client(stack1, peer1Address, peer2Address);
		server = new Server(stack2, peer2Address, peer1Address);
	}

	@After
	public void afterEach() {
		if (stack1 != null) {
			stack1.stop();
			stack1 = null;
		}

		if (stack2 != null) {
			stack2.stop();
			stack2 = null;
		}

		super.tearDown();
	}

	/**
	 * InitialDP + Error message SystemFailure ACN=CAP-v1-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest
	 * TC-END + Error message SystemFailure
	 * </pre>
	 */
	@Test
	public void testInitialDp_Error() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onErrorComponent(CAPDialog capDialog, Integer invokeId, CAPErrorMessage capErrorMessage) {
				super.onErrorComponent(capDialog, invokeId, capErrorMessage);

				assertTrue(capErrorMessage.isEmSystemFailure());
				CAPErrorMessageSystemFailure em = capErrorMessage.getEmSystemFailure();
				assertEquals(em.getUnavailableNetworkResource(), UnavailableNetworkResource.endUserFailure);
			}
		};

		// 1. TC-BEGIN + InitialDPRequest
		client.sendInitialDp(CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			assertTrue(Client.checkTestInitialDp(ind));

			server.handleSent(EventType.ErrorComponent, null);
			CAPErrorMessage capErrorMessage = server.capErrorMessageFactory
					.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.endUserFailure);

			ind.getCAPDialog().sendErrorComponent(ind.getInvokeId(), capErrorMessage);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			capDialog.close(false, dummyCallback);
		}

		// 2. TC-END + Error message SystemFailure
		server.awaitSent(EventType.ErrorComponent);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ErrorComponent);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ErrorComponent);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Circuit switch call simple messageflow 1 ACN=CAP-v2-gsmSSF-to-gsmSCF
	 * 
	 * <pre>
	 * TC-BEGIN + InitialDPRequest
	 * TC-CONTINUE + RequestReportBCSMEventRequest
	 * TC-CONTINUE + FurnishChargingInformationRequest
	 * TC-CONTINUE + ApplyChargingRequest + ConnectRequest
	 * TC-CONTINUE + ContinueRequest
	 * TC-CONTINUE + SendChargingInformationRequest
	 * TC-CONTINUE + EventReportBCSMRequest (OAnswer)
	 * TC-CONTINUE + ApplyChargingReportRequest <call... waiting till DialogTimeout>
	 * TC-CONTINUE + ActivityTestRequest
	 * TC-CONTINUE + ActivityTestResponse
	 * TC-CONTINUE + EventReportBCSMRequest (ODisconnect)
	 * TC-END (empty)
	 * </pre>
	 */
	@Test
	public void testCircuitCall1() throws Exception {
		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogTimeout(CAPDialog capDialog) {
				super.onDialogTimeout(capDialog);

				server.awaitSent(EventType.ActivityTestRequest);
			}
		};

		final long invokeTimeout = 1500;
		final long dialogTimeout = 2500;

		// setting dialog timeout little interval to invoke onDialogTimeout on SCF side
		client.capStack.getTCAPStack().setDialogIdleTimeout(60000);
		client.suppressInvokeTimeout();

		server.capStack.getTCAPStack().setInvokeTimeout(invokeTimeout);
		server.capStack.getTCAPStack().setDialogIdleTimeout(dialogTimeout);

		// 1. TC-BEGIN + InitialDPRequest
		client.sendInitialDp(CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF);
		client.awaitSent(EventType.InitialDpRequest);

		CAPDialogCircuitSwitchedCall serverDlg;

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			assertTrue(Client.checkTestInitialDp(ind));
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			serverDlg = (CAPDialogCircuitSwitchedCall) capDialog;
		}

		// 2. TC-CONTINUE + RequestReportBCSMEventRequest
		{
			RequestReportBCSMEventRequest rrc = server.getRequestReportBCSMEventRequest();
			serverDlg.addRequestReportBCSMEventRequest(rrc.getBCSMEventList(), rrc.getExtensions());
			server.handleSent(EventType.RequestReportBCSMEventRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.RequestReportBCSMEventRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RequestReportBCSMEventRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.RequestReportBCSMEventRequest);
			RequestReportBCSMEventRequest ind = (RequestReportBCSMEventRequest) event.getEvent();

			client.checkRequestReportBCSMEventRequest(ind);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 3. TC-CONTINUE + FurnishChargingInformationRequest
		{
			byte[] freeFormatData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
			FreeFormatDataImpl ffd = new FreeFormatDataImpl(Unpooled.wrappedBuffer(freeFormatData));
			FCIBCCCAMELSequence1 FCIBCCCAMELsequence1 = server.capParameterFactory.createFCIBCCCAMELsequence1(ffd,
					LegType.leg1, AppendFreeFormatData.append);
			serverDlg.addFurnishChargingInformationRequest(FCIBCCCAMELsequence1);
			serverDlg.send(dummyCallback);
			server.handleSent(EventType.FurnishChargingInformationRequest, null);
		}
		server.awaitSent(EventType.FurnishChargingInformationRequest);

		client.awaitReceived(EventType.FurnishChargingInformationRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.FurnishChargingInformationRequest);
			FurnishChargingInformationRequest ind = (FurnishChargingInformationRequest) event.getEvent();

			byte[] freeFormatData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
			assertTrue(ByteBufUtil.equals(ind.getFCIBCCCAMELsequence1().getFreeFormatData().getValue(),
					Unpooled.wrappedBuffer(freeFormatData)));
			assertEquals(ind.getFCIBCCCAMELsequence1().getPartyToCharge(), LegType.leg1);
			assertEquals(ind.getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 4. TC-CONTINUE + ApplyChargingRequest + ConnectRequest
		{
			// Boolean tone, CAPExtensionsImpl extensions, Long tariffSwitchInterval
			CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics = server.capParameterFactory
					.createCAMELAChBillingChargingCharacteristics(1000, null, null, null);
			serverDlg.addApplyChargingRequest(aChBillingChargingCharacteristics, LegType.leg1, null, null);
			server.handleSent(EventType.ApplyChargingRequest, null);

			List<CalledPartyNumberIsup> calledPartyNumber = new ArrayList<CalledPartyNumberIsup>();
			CalledPartyNumber cpn = server.isupParameterFactory.createCalledPartyNumber();
			cpn.setAddress("5599999988");
			cpn.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
			cpn.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
			cpn.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
			CalledPartyNumberIsup cpnc = server.capParameterFactory.createCalledPartyNumber(cpn);
			calledPartyNumber.add(cpnc);
			DestinationRoutingAddress destinationRoutingAddress = server.capParameterFactory
					.createDestinationRoutingAddress(calledPartyNumber);
			serverDlg.addConnectRequest(destinationRoutingAddress, null, null, null, null, null, null, null, null, null,
					null, null, null, false, false, false, null, false, false);
			server.handleSent(EventType.ConnectRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.ApplyChargingRequest);
		server.awaitSent(EventType.ConnectRequest);

		client.awaitReceived(EventType.ApplyChargingRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ApplyChargingRequest);
			ApplyChargingRequest ind = (ApplyChargingRequest) event.getEvent();

			assertEquals(ind.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration(), 1000);
			// we are sending V2 which does not have this property
			assertFalse(ind.getAChBillingChargingCharacteristics().getReleaseIfdurationExceeded());
			assertNull(ind.getAChBillingChargingCharacteristics().getTariffSwitchInterval());
			assertEquals(ind.getPartyToCharge(), LegType.leg1);
			assertNull(ind.getExtensions());
			assertNotNull(ind.getAChChargingAddress());
			assertNotNull(ind.getAChChargingAddress().getLegID());
			assertNotNull(ind.getAChChargingAddress().getLegID().getSendingSideID());
			assertNull(ind.getAChChargingAddress().getLegID().getReceivingSideID());
			assertEquals(ind.getAChChargingAddress().getLegID().getSendingSideID(), LegType.leg1);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.ConnectRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ConnectRequest);
			ConnectRequest ind = (ConnectRequest) event.getEvent();

			assertEquals(ind.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
			CalledPartyNumber calledPartyNumber = ind.getDestinationRoutingAddress().getCalledPartyNumber().get(0)
					.getCalledPartyNumber();
			assertTrue(calledPartyNumber.getAddress().equals("5599999988"));
			assertEquals(calledPartyNumber.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
			assertEquals(calledPartyNumber.getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);
			assertEquals(calledPartyNumber.getInternalNetworkNumberIndicator(), CalledPartyNumber._INN_ROUTING_ALLOWED);

			assertNull(ind.getAlertingPattern());
			assertNull(ind.getCallingPartysCategory());
			assertNull(ind.getChargeNumber());
			assertNull(ind.getGenericNumbers());
			assertNull(ind.getLegToBeConnected());
			assertNull(ind.getOriginalCalledPartyID());
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 5. TC-CONTINUE + ContinueRequest
		{
			serverDlg.addContinueRequest();
			server.handleSent(EventType.ContinueRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.ContinueRequest);

		client.awaitReceived(EventType.ContinueRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ContinueRequest);
			ContinueRequest ind = (ContinueRequest) event.getEvent();

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 6. TC-CONTINUE + SendChargingInformationRequest
		{
			CAI_GSM0224 aocInitial = server.capParameterFactory.createCAI_GSM0224(1, 2, 3, null, null, null, null);
			AOCBeforeAnswer aocBeforeAnswer = server.capParameterFactory.createAOCBeforeAnswer(aocInitial, null);
			SCIBillingChargingCharacteristics sciBillingChargingCharacteristics = server.capParameterFactory
					.createSCIBillingChargingCharacteristics(aocBeforeAnswer);
			serverDlg.addSendChargingInformationRequest(sciBillingChargingCharacteristics, LegType.leg2, null);
			server.handleSent(EventType.SendChargingInformationRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.SendChargingInformationRequest);

		client.awaitReceived(EventType.SendChargingInformationRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendChargingInformationRequest);
			SendChargingInformationRequest ind = (SendChargingInformationRequest) event.getEvent();

			CAI_GSM0224 aocInitial = ind.getSCIBillingChargingCharacteristics().getAOCBeforeAnswer().getAOCInitial();
			assertEquals((int) aocInitial.getE1(), 1);
			assertEquals((int) aocInitial.getE2(), 2);
			assertEquals((int) aocInitial.getE3(), 3);
			assertNull(aocInitial.getE4());
			assertNull(aocInitial.getE5());
			assertNull(aocInitial.getE6());
			assertNull(aocInitial.getE7());
			assertNull(ind.getSCIBillingChargingCharacteristics().getAOCBeforeAnswer().getAOCSubsequent());
			assertNull(ind.getSCIBillingChargingCharacteristics().getAOCSubsequent());
			assertNull(ind.getSCIBillingChargingCharacteristics().getAOCExtension());
			assertEquals(ind.getPartyToCharge(), LegType.leg2);
			assertNull(ind.getExtensions());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		CAPDialogCircuitSwitchedCall clientDlg;
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			clientDlg = (CAPDialogCircuitSwitchedCall) capDialog;
		}
		// 7. TC-CONTINUE + EventReportBCSMRequest (OAnswer)
		{
			OAnswerSpecificInfo oAnswerSpecificInfo = client.capParameterFactory.createOAnswerSpecificInfo(null, false,
					false, null, null, null);
			MiscCallInfo miscCallInfo = client.capParameterFactory
					.createMiscCallInfo(MiscCallInfoMessageType.notification, null);
			EventSpecificInformationBCSM eventSpecificInformationBCSM = client.capParameterFactory
					.createEventSpecificInformationBCSM(oAnswerSpecificInfo);
			clientDlg.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, eventSpecificInformationBCSM, LegType.leg2,
					miscCallInfo, null);
			client.handleSent(EventType.EventReportBCSMRequest, null);
			clientDlg.send(dummyCallback);
		}
		client.awaitSent(EventType.EventReportBCSMRequest);

		server.awaitReceived(EventType.EventReportBCSMRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.EventReportBCSMRequest);
			EventReportBCSMRequest ind = (EventReportBCSMRequest) event.getEvent();

			assertEquals(ind.getEventTypeBCSM(), EventTypeBCSM.oAnswer);
			assertNotNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo());
			assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getDestinationAddress());
			assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getChargeIndicator());
			assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getExtBasicServiceCode());
			assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getExtBasicServiceCode2());
			assertFalse(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getForwardedCall());
			assertFalse(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getOrCall());
			assertEquals(ind.getLegID(), LegType.leg2);
			assertNull(ind.getExtensions());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);

		// 8. TC-CONTINUE + ApplyChargingReportRequest <waiting till DialogTimeout>
		{
			TimeInformation timeInformation = client.capParameterFactory.createTimeInformation(2000);
			TimeDurationChargingResult timeDurationChargingResult = client.capParameterFactory
					.createTimeDurationChargingResult(LegType.leg1, timeInformation, true, false, null, null);
			clientDlg.addApplyChargingReportRequest(timeDurationChargingResult);
			client.handleSent(EventType.ApplyChargingReportRequest, null);
			clientDlg.send(dummyCallback);
		}
		client.awaitSent(EventType.ApplyChargingReportRequest);

		server.awaitReceived(EventType.ApplyChargingReportRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ApplyChargingReportRequest);
			ApplyChargingReportRequest ind = (ApplyChargingReportRequest) event.getEvent();

			TimeDurationChargingResult tdr = ind.getTimeDurationChargingResult();
			assertEquals(tdr.getPartyToCharge(), LegType.leg1);
			assertEquals((int) tdr.getTimeInformation().getTimeIfNoTariffSwitch(), 2000);
			assertNotNull(tdr.getAChChargingAddress());
			assertNotNull(tdr.getAChChargingAddress().getLegID());
			assertNotNull(tdr.getAChChargingAddress().getLegID().getReceivingSideID());
			assertEquals(tdr.getAChChargingAddress().getLegID().getReceivingSideID(), LegType.leg1);
			assertFalse(tdr.getCallLegReleasedAtTcpExpiry());
			assertNull(tdr.getExtensions());
			assertTrue(tdr.getLegActive());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		TestEventUtils.updateStamp();

		server.awaitReceived(EventType.DialogTimeout);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogTimeout);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			capDialog.keepAlive();

			serverDlg = (CAPDialogCircuitSwitchedCall) capDialog;
			serverDlg.addActivityTestRequest(500);
			serverDlg.send(dummyCallback);

			server.handleSent(EventType.ActivityTestRequest, null);
		}
		TestEventUtils.assertPassed(dialogTimeout);

		// 9. TC-CONTINUE + ActivityTestRequest
		// server's ActivityTestRequest sending awaiting implemented in listener
		int activityTestInvokeId;

		client.awaitReceived(EventType.ActivityTestRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ActivityTestRequest);
			ActivityTestRequest ind = (ActivityTestRequest) event.getEvent();

			activityTestInvokeId = ind.getInvokeId();
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());

		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			clientDlg = (CAPDialogCircuitSwitchedCall) capDialog;

			clientDlg.addActivityTestResponse(activityTestInvokeId);
			client.handleSent(EventType.ActivityTestResponse, null);
			clientDlg.send(dummyCallback);
		}

		// 10. TC-CONTINUE + ActivityTestResponse
		client.awaitSent(EventType.ActivityTestResponse);

		server.awaitReceived(EventType.ActivityTestResponse);
		server.awaitReceived(EventType.DialogDelimiter);

		// 11. TC-CONTINUE + EventReportBCSMRequest (ODisconnect)
		client.sendEventReportBCSMRequest_1();
		client.awaitSent(EventType.EventReportBCSMRequest);

		server.awaitReceived(EventType.EventReportBCSMRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.EventReportBCSMRequest);
			EventReportBCSMRequest ind = (EventReportBCSMRequest) event.getEvent();

			assertEquals(ind.getEventTypeBCSM(), EventTypeBCSM.oDisconnect);
			assertNotNull(ind.getEventSpecificInformationBCSM().getODisconnectSpecificInfo());
			CauseIndicators ci = ind.getEventSpecificInformationBCSM().getODisconnectSpecificInfo().getReleaseCause()
					.getCauseIndicators();
			assertEquals(ci.getCauseValue(), CauseIndicators._CV_ALL_CLEAR);
			assertEquals(ci.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
			assertEquals(ci.getLocation(), CauseIndicators._LOCATION_USER);
			assertEquals(ind.getLegID(), LegType.leg1);
			assertEquals(ind.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.notification);
			assertNull(ind.getMiscCallInfo().getDpAssignment());
			assertNull(ind.getExtensions());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			serverDlg = (CAPDialogCircuitSwitchedCall) capDialog;
			serverDlg.close(false, dummyCallback);
		}

		// 12. TC-END (empty)
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RequestReportBCSMEventRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.FurnishChargingInformationRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.ApplyChargingRequest);
		clientExpected.addReceived(EventType.ConnectRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.ContinueRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.SendChargingInformationRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.EventReportBCSMRequest);
		clientExpected.addSent(EventType.ApplyChargingReportRequest);
		clientExpected.addReceived(EventType.ActivityTestRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.ActivityTestResponse);
		clientExpected.addSent(EventType.EventReportBCSMRequest);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.RequestReportBCSMEventRequest);
		serverExpected.addSent(EventType.FurnishChargingInformationRequest);
		serverExpected.addSent(EventType.ApplyChargingRequest);
		serverExpected.addSent(EventType.ConnectRequest);
		serverExpected.addSent(EventType.ContinueRequest);
		serverExpected.addSent(EventType.SendChargingInformationRequest);
		serverExpected.addReceived(EventType.EventReportBCSMRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.ApplyChargingReportRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addSent(EventType.ActivityTestRequest);
		serverExpected.addReceived(EventType.ActivityTestResponse);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.EventReportBCSMRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 *
	 * Circuit switch call play announcement and disconnect ACN =
	 * capssf-scfGenericAC V3
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest
	 * TC-CONTINUE + RequestReportBCSMEventRequest
	 * TC-CONTINUE + ConnectToResourceRequest
	 * TC-CONTINUE + PlayAnnouncementRequest
	 * TC-CONTINUE + SpecializedResourceReportRequest
	 * TC-CONTINUE + DisconnectForwardConnectionRequest
	 * TC-END + ReleaseCallRequest
	 * </pre>
	 */
	@Test
	public void testPlayAnnouncment() throws Exception {
		// 1. TC-BEGIN + InitialDPRequest
		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			assertTrue(Client.checkTestInitialDp(ind));
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		CAPDialogCircuitSwitchedCall serverDlg;
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			serverDlg = (CAPDialogCircuitSwitchedCall) capDialog;
		}

		// 2. TC-CONTINUE + RequestReportBCSMEventRequest
		{
			RequestReportBCSMEventRequest rrc = server.getRequestReportBCSMEventRequest();
			serverDlg.addRequestReportBCSMEventRequest(rrc.getBCSMEventList(), rrc.getExtensions());
			server.handleSent(EventType.RequestReportBCSMEventRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.RequestReportBCSMEventRequest);

		client.awaitReceived(EventType.RequestReportBCSMEventRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.RequestReportBCSMEventRequest);
			RequestReportBCSMEventRequest ind = (RequestReportBCSMEventRequest) event.getEvent();

			client.checkRequestReportBCSMEventRequest(ind);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 3. TC-CONTINUE + ConnectToResourceRequest
		{
			CalledPartyNumber calledPartyNumber = server.isupParameterFactory.createCalledPartyNumber();
			calledPartyNumber.setAddress("111222333");
			calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
			calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
			calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
			CalledPartyNumberIsup resourceAddress_IPRoutingAddress = server.capParameterFactory
					.createCalledPartyNumber(calledPartyNumber);
			serverDlg.addConnectToResourceRequest(resourceAddress_IPRoutingAddress, false, null, null, null);
			server.handleSent(EventType.ConnectToResourceRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.ConnectToResourceRequest);

		client.awaitReceived(EventType.ConnectToResourceRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ConnectToResourceRequest);
			ConnectToResourceRequest ind = (ConnectToResourceRequest) event.getEvent();

			CalledPartyNumber cpn = ind.getResourceAddress_IPRoutingAddress().getCalledPartyNumber();
			assertTrue(cpn.getAddress().equals("111222333"));
			assertEquals(cpn.getInternalNetworkNumberIndicator(), CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
			assertEquals(cpn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
			assertEquals(cpn.getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);
			assertFalse(ind.getResourceAddress_Null());
			assertNull(ind.getCallSegmentID());
			assertNull(ind.getExtensions());
			assertNull(ind.getServiceInteractionIndicatorsTwo());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 4. TC-CONTINUE + PlayAnnouncementRequest
		{
			Tone tone = server.capParameterFactory.createTone(10, 100);
			InformationToSend informationToSend = server.capParameterFactory.createInformationToSend(tone);

			serverDlg.addPlayAnnouncementRequest(informationToSend, true, true, null, null,
					server.invokeTimeoutSuppressed);
			server.handleSent(EventType.PlayAnnouncementRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.PlayAnnouncementRequest);

		int playAnnounsmentInvokeId;
		client.awaitReceived(EventType.PlayAnnouncementRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.PlayAnnouncementRequest);
			PlayAnnouncementRequest ind = (PlayAnnouncementRequest) event.getEvent();

			assertEquals(ind.getInformationToSend().getTone().getToneID(), 10);
			assertEquals((int) ind.getInformationToSend().getTone().getDuration(), 100);
			assertTrue(ind.getDisconnectFromIPForbidden());
			assertTrue(ind.getRequestAnnouncementCompleteNotification());
			assertFalse(ind.getRequestAnnouncementStartedNotification());
			assertNull(ind.getCallSegmentID());
			assertNull(ind.getExtensions());

			playAnnounsmentInvokeId = ind.getInvokeId();
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.addSpecializedResourceReportRequest_CapV23(playAnnounsmentInvokeId);
			client.handleSent(EventType.SpecializedResourceReportRequest, null);
			dlg.send(dummyCallback);
		}

		// 5. TC-CONTINUE + SpecializedResourceReportRequest
		client.awaitSent(EventType.SpecializedResourceReportRequest);

		server.awaitReceived(EventType.SpecializedResourceReportRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SpecializedResourceReportRequest);
			SpecializedResourceReportRequest ind = (SpecializedResourceReportRequest) event.getEvent();

			assertFalse(ind.getFirstAnnouncementStarted());
			assertFalse(ind.getAllAnnouncementsComplete());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			serverDlg = (CAPDialogCircuitSwitchedCall) capDialog;
		}

		// 6. TC-CONTINUE + DisconnectForwardConnectionRequest
		{
			serverDlg.addDisconnectForwardConnectionRequest();
			server.handleSent(EventType.DisconnectForwardConnectionRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.DisconnectForwardConnectionRequest);

		client.awaitReceived(EventType.DisconnectForwardConnectionRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DisconnectForwardConnectionRequest);
			DisconnectForwardConnectionRequest ind = (DisconnectForwardConnectionRequest) event.getEvent();

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 7. TC-END + ReleaseCallRequest
		{
			CauseIndicators causeIndicators = server.isupParameterFactory.createCauseIndicators();
			causeIndicators.setCauseValue(CauseIndicators._CV_SEND_SPECIAL_TONE);
			causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
			causeIndicators.setDiagnostics(null);
			causeIndicators.setLocation(CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);
			CauseIsup cause = server.capParameterFactory.createCause(causeIndicators);

			serverDlg.addReleaseCallRequest(cause);
			server.handleSent(EventType.ReleaseCallRequest, null);
			serverDlg.close(false, dummyCallback);
		}
		server.awaitSent(EventType.ReleaseCallRequest);

		client.awaitReceived(EventType.ReleaseCallRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ReleaseCallRequest);
			ReleaseCallRequest ind = (ReleaseCallRequest) event.getEvent();

			CauseIndicators ci = ind.getCause().getCauseIndicators();
			assertEquals(ci.getCauseValue(), CauseIndicators._CV_SEND_SPECIAL_TONE);
			assertEquals(ci.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
			assertNull(ci.getDiagnostics());
			assertEquals(ci.getLocation(), CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RequestReportBCSMEventRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.ConnectToResourceRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.PlayAnnouncementRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.SpecializedResourceReportRequest);
		clientExpected.addReceived(EventType.DisconnectForwardConnectionRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.ReleaseCallRequest);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.RequestReportBCSMEventRequest);
		serverExpected.addSent(EventType.ConnectToResourceRequest);
		serverExpected.addSent(EventType.PlayAnnouncementRequest);
		serverExpected.addReceived(EventType.SpecializedResourceReportRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.DisconnectForwardConnectionRequest);
		serverExpected.addSent(EventType.ReleaseCallRequest);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Assist SSF dialog (V4) ACN = capssf-scfAssistHandoffAC V4
	 * 
	 * <pre>
	 * TC-BEGIN + AssistRequestInstructionsRequest
	 * TC-CONTINUE + ResetTimerRequest
	 * TC-CONTINUE + PromptAndCollectUserInformationRequest
	 * TC-CONTINUE + SpecializedResourceReportRequest
	 * TC-CONTINUE + PromptAndCollectUserInformationResponse
	 * TC-CONTINUE + CancelRequest
	 * TC-END + CancelRequest
	 * </pre>
	 */
	@Test
	public void testAssistSsf() throws Exception {
		// 1. TC-BEGIN + AssistRequestInstructionsRequest
		client.sendAssistRequestInstructionsRequest();
		client.awaitSent(EventType.AssistRequestInstructionsRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.AssistRequestInstructionsRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.AssistRequestInstructionsRequest);
			AssistRequestInstructionsRequest ind = (AssistRequestInstructionsRequest) event.getEvent();

			// assertNull(ind.getCorrelationID().getGenericDigits());
			GenericNumber gn = ind.getCorrelationID().getGenericNumber();
			assertTrue(gn.getAddress().equals("333111222"));
			assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
			assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
			assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_ISDN);
			assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLED_NUMBER);
			assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_NETWORK_PROVIDED);

			IPSSPCapabilities ipc = ind.getIPSSPCapabilities();
			assertTrue(ipc.getIPRoutingAddressSupported());
			assertFalse(ipc.getVoiceBackSupported());
			assertTrue(ipc.getVoiceInformationSupportedViaSpeechRecognition());
			assertFalse(ipc.getVoiceInformationSupportedViaVoiceRecognition());
			assertFalse(ipc.getGenerationOfVoiceAnnouncementsFromTextSupported());
			assertNull(ipc.getExtraData());

			assertNull(ind.getExtensions());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		CAPDialogCircuitSwitchedCall serverDlg;
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			serverDlg = (CAPDialogCircuitSwitchedCall) capDialog;
		}

		// 2. TC-CONTINUE + ResetTimerRequest
		{
			serverDlg.addResetTimerRequest(TimerID.tssf, 1001, null, null);
			server.handleSent(EventType.ResetTimerRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.ResetTimerRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ResetTimerRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ResetTimerRequest);
			ResetTimerRequest ind = (ResetTimerRequest) event.getEvent();

			assertEquals(ind.getTimerID(), TimerID.tssf);
			assertEquals(ind.getTimerValue(), 1001);
			assertNull(ind.getCallSegmentID());
			assertNull(ind.getExtensions());
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 3. TC-CONTINUE + PromptAndCollectUserInformationRequest
		{
			CollectedDigits collectedDigits = server.capParameterFactory.createCollectedDigits(1, 11, null, null, null,
					null, null, null, null, null, null);
			CollectedInfo collectedInfo = server.capParameterFactory.createCollectedInfo(collectedDigits);
			serverDlg.addPromptAndCollectUserInformationRequest(collectedInfo, true, null, null, null, null);
			server.handleSent(EventType.PromptAndCollectUserInformationRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.PromptAndCollectUserInformationRequest);

		int promptAndCollectUserInformationInvokeId;
		client.awaitReceived(EventType.PromptAndCollectUserInformationRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.PromptAndCollectUserInformationRequest);
			PromptAndCollectUserInformationRequest ind = (PromptAndCollectUserInformationRequest) event.getEvent();

			promptAndCollectUserInformationInvokeId = ind.getInvokeId();

			CollectedDigits cd = ind.getCollectedInfo().getCollectedDigits();
			assertEquals((int) cd.getMinimumNbOfDigits(), 1);
			assertEquals(cd.getMaximumNbOfDigits(), 11);
			assertNull(cd.getCancelDigit());
			assertNull(cd.getEndOfReplyDigit());
			assertNull(cd.getFirstDigitTimeOut());
			assertNull(cd.getStartDigit());
			assertTrue(ind.getDisconnectFromIPForbidden());
			assertNull(ind.getInformationToSend());
			assertNull(ind.getExtensions());
			assertNull(ind.getCallSegmentID());
			assertFalse(ind.getRequestAnnouncementStartedNotification());
		}
		CAPDialogCircuitSwitchedCall clientDlg;
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			clientDlg = (CAPDialogCircuitSwitchedCall) capDialog;
		}

		// 4. TC-CONTINUE + SpecializedResourceReportRequest
		{
			clientDlg.addSpecializedResourceReportRequest_CapV4(promptAndCollectUserInformationInvokeId, false, true);
			client.handleSent(EventType.SpecializedResourceReportRequest, null);
			clientDlg.send(dummyCallback);
		}
		client.awaitSent(EventType.SpecializedResourceReportRequest);

		server.awaitReceived(EventType.SpecializedResourceReportRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SpecializedResourceReportRequest);
			SpecializedResourceReportRequest ind = (SpecializedResourceReportRequest) event.getEvent();

			assertFalse(ind.getAllAnnouncementsComplete());
			assertTrue(ind.getFirstAnnouncementStarted());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);

		// 5. TC-CONTINUE + PromptAndCollectUserInformationResponse
		{
			GenericNumber genericNumber = client.isupParameterFactory.createGenericNumber();
			genericNumber.setAddress("444422220000");
			genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
			genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_SUBSCRIBER_NUMBER);
			genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_DATA);
			genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLING_PARTY_NUMBER);
			genericNumber.setScreeningIndicator(GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
			DigitsIsup digitsResponse = client.capParameterFactory.createDigits_GenericNumber(genericNumber);
			clientDlg.addPromptAndCollectUserInformationResponse_DigitsResponse(promptAndCollectUserInformationInvokeId,
					digitsResponse);
			client.handleSent(EventType.PromptAndCollectUserInformationResponse, null);
			clientDlg.send(dummyCallback);
		}
		client.awaitSent(EventType.PromptAndCollectUserInformationResponse);

		server.awaitReceived(EventType.PromptAndCollectUserInformationResponse);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.PromptAndCollectUserInformationResponse);
			PromptAndCollectUserInformationResponse ind = (PromptAndCollectUserInformationResponse) event.getEvent();

			DigitsIsup digits = ind.getDigitsResponse();
			digits.setIsGenericNumber();
			GenericNumber gn = digits.getGenericNumber();
			assertTrue(gn.getAddress().equals("444422220000"));
			assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
			assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_SUBSCRIBER_NUMBER);
			assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_DATA);
			assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLING_PARTY_NUMBER);
			assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			serverDlg = (CAPDialogCircuitSwitchedCall) capDialog;
		}

		// 6. TC-CONTINUE + CancelRequest
		{
			serverDlg.addCancelRequest_AllRequests();
			server.handleSent(EventType.CancelRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.CancelRequest);

		client.awaitReceived(EventType.CancelRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CancelRequest);
			CancelRequest ind = (CancelRequest) event.getEvent();

			assertTrue(ind.getAllRequests());
			assertNull(ind.getInvokeID());
			assertNull(ind.getCallSegmentToCancel());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 7. TC-END + CancelRequest
		{
			serverDlg.addCancelRequest_InvokeId(10);
			server.handleSent(EventType.CancelRequest, null);
			serverDlg.close(false, dummyCallback);
		}
		server.awaitSent(EventType.CancelRequest);

		client.awaitReceived(EventType.CancelRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CancelRequest);
			CancelRequest ind = (CancelRequest) event.getEvent();

			assertFalse(ind.getAllRequests());
			assertEquals((int) ind.getInvokeID(), 10);
			assertNull(ind.getCallSegmentToCancel());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.AssistRequestInstructionsRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ResetTimerRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.PromptAndCollectUserInformationRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.SpecializedResourceReportRequest);
		clientExpected.addSent(EventType.PromptAndCollectUserInformationResponse);
		clientExpected.addReceived(EventType.CancelRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.CancelRequest);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.AssistRequestInstructionsRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ResetTimerRequest);
		serverExpected.addSent(EventType.PromptAndCollectUserInformationRequest);
		serverExpected.addReceived(EventType.SpecializedResourceReportRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.PromptAndCollectUserInformationResponse);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.CancelRequest);
		serverExpected.addSent(EventType.CancelRequest);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * ScfSsf test (V4) ACN = capscf-ssfGenericAC V4
	 * 
	 * <pre>
	 * TC-BEGIN + establishTemporaryConnection + callInformationRequest + collectInformationRequest 
	 * TC-END + callInformationReport
	 * </pre>
	 */
	@Test
	public void testScfSsf() throws Exception {
		// 1. TC-BEGIN + establishTemporaryConnection + callInformationRequest +
		// collectInformationRequest
		client.sendEstablishTemporaryConnectionRequest_CallInformationRequest();
		client.awaitSent(EventType.EstablishTemporaryConnectionRequest);
		client.awaitSent(EventType.CallInformationRequestRequest);
		client.awaitSent(EventType.CollectInformationRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.EstablishTemporaryConnectionRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.EstablishTemporaryConnectionRequest);
			EstablishTemporaryConnectionRequest ind = (EstablishTemporaryConnectionRequest) event.getEvent();

			GenericNumber gn = ind.getAssistingSSPIPRoutingAddress().getGenericNumber();
			assertTrue(gn.getAddress().equals("333111222"));
			assertEquals(gn.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
			assertEquals(gn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
			assertEquals(gn.getNumberingPlanIndicator(), GenericNumber._NPI_ISDN);
			assertEquals(gn.getNumberQualifierIndicator(), GenericNumber._NQIA_CALLED_NUMBER);
			assertEquals(gn.getScreeningIndicator(), GenericNumber._SI_NETWORK_PROVIDED);

			assertNull(ind.getCallingPartyNumber());
			assertNull(ind.getCallSegmentID());
			assertNull(ind.getCarrier());
			assertNull(ind.getChargeNumber());
			assertNull(ind.getCorrelationID());
			assertNull(ind.getExtensions());
			assertNull(ind.getNAOliInfo());
			assertNull(ind.getOriginalCalledPartyID());
			assertNull(ind.getScfID());
			assertNull(ind.getServiceInteractionIndicatorsTwo());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.CallInformationRequestRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CallInformationRequestRequest);
			CallInformationRequestRequest ind = (CallInformationRequestRequest) event.getEvent();

			List<RequestedInformationType> al = ind.getRequestedInformationTypeList();
			assertEquals(al.size(), 1);
			assertEquals(al.get(0), RequestedInformationType.callStopTime);
			assertNull(ind.getExtensions());
			assertNotNull(ind.getLegID());
			assertEquals(ind.getLegID(), LegType.leg2);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.CollectInformationRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CollectInformationRequest);
			CollectInformationRequest ind = (CollectInformationRequest) event.getEvent();

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

			List<RequestedInformation> requestedInformationList = new ArrayList<RequestedInformation>();
			DateAndTime dt = server.capParameterFactory.createDateAndTime(2012, 11, 30, 23, 50, 40);
			RequestedInformation ri = server.capParameterFactory.createRequestedInformation_CallStopTime(dt);
			requestedInformationList.add(ri);
			dlg.addCallInformationReportRequest(requestedInformationList, null, null);
			server.handleSent(EventType.CallInformationReportRequest, null);
			dlg.close(false, dummyCallback);
		}

		// 2. TC-END + callInformationReport
		server.awaitSent(EventType.CallInformationReportRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.CallInformationReportRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CallInformationReportRequest);
			CallInformationReportRequest ind = (CallInformationReportRequest) event.getEvent();

			List<RequestedInformation> al = ind.getRequestedInformationList();
			assertEquals(al.size(), 1);
			DateAndTime dt = al.get(0).getCallStopTimeValue();
			assertEquals(dt.getYear(), 2012);
			assertEquals(dt.getMonth(), 11);
			assertEquals(dt.getDay(), 30);
			assertEquals(dt.getHour(), 23);
			assertEquals(dt.getMinute(), 50);
			assertEquals(dt.getSecond(), 40);
			assertNull(ind.getExtensions());
			assertNotNull(ind.getLegID());
			assertEquals(ind.getLegID(), LegType.leg2);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.EstablishTemporaryConnectionRequest);
		clientExpected.addSent(EventType.CallInformationRequestRequest);
		clientExpected.addSent(EventType.CollectInformationRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.CallInformationReportRequest);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.EstablishTemporaryConnectionRequest);
		serverExpected.addReceived(EventType.CallInformationRequestRequest);
		serverExpected.addReceived(EventType.CollectInformationRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.CallInformationReportRequest);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Abnormal test ACN = CAP-v2-assist-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + ActivityTestRequest
	 * TC-CONTINUE <no ActivityTestResponse> resetInvokeTimer() before InvokeTimeout
	 * InvokeTimeout 
	 * TC-CONTINUE + CancelRequest + cancelInvocation() -> CancelRequest will not go to Server
	 * TC-CONTINUE + ResetTimerRequest reject ResetTimerRequest 
	 * DialogUserAbort: AbortReason=missing_reference
	 * </pre>
	 */
	@Test
	public void testAbnormal() throws Exception {
		final AtomicInteger clientResetTimerRequestInvokeId = new AtomicInteger(0);
		final AtomicInteger serverResetTimerRequestInvokeId = new AtomicInteger(0);

		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(capDialog, invokeId, problem, isLocalOriginated);

				assertEquals(clientResetTimerRequestInvokeId.get(), (long) invokeId);
				try {
					assertEquals(problem.getInvokeProblemType(), InvokeProblemType.MistypedParameter);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(isLocalOriginated);
			}
		};

		server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason,
					CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);

				assertNull(generalReason);
				assertNull(userReason);
			}
		};

		// 1. TC-BEGIN + ActivityTestRequest
		final int activityTestInvokeTimeout = 1000;

		client.sendActivityTestRequest(activityTestInvokeTimeout);
		client.awaitSent(EventType.ActivityTestRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ActivityTestRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ActivityTestRequest);
			ActivityTestRequest ind = (ActivityTestRequest) event.getEvent();

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.send(dummyCallback);
		}

		TestEventUtils.updateStamp();

		// 2. TC-CONTINUE <no ActivityTestResponse> resetInvokeTimer() before
		// InvokeTimeout
		client.awaitReceived(EventType.DialogDelimiter);

		// 3. InvokeTimeout
		CAPDialogCircuitSwitchedCall clientDlg;
		client.awaitReceived(EventType.InvokeTimeout);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.InvokeTimeout);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			clientDlg = (CAPDialogCircuitSwitchedCall) capDialog;
		}
		TestEventUtils.assertPassed(activityTestInvokeTimeout);

		// 4. TC-CONTINUE + CancelRequest + cancelInvocation() -> ...
		{
			int invId = clientDlg.addCancelRequest_AllRequests();
			client.handleSent(EventType.CancelRequest, null);
			clientDlg.cancelInvocation(invId);
			clientDlg.send(dummyCallback);
		}
		client.awaitSent(EventType.CancelRequest);

		server.awaitReceived(EventType.DialogDelimiter);

		// 5. TC-CONTINUE + ResetTimerRequest
		{
			int resetTimerRequestInvokeId = clientDlg.addResetTimerRequest(TimerID.tssf, 2222, null, null);

			clientResetTimerRequestInvokeId.set(resetTimerRequestInvokeId);
			client.handleSent(EventType.ResetTimerRequest, null);
			clientDlg.send(dummyCallback);
		}
		client.awaitSent(EventType.ResetTimerRequest);

		server.awaitReceived(EventType.ResetTimerRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ResetTimerRequest);
			ResetTimerRequest ind = (ResetTimerRequest) event.getEvent();

			serverResetTimerRequestInvokeId.set(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			ProblemImpl problem = new ProblemImpl();
			problem.setInvokeProblemType(InvokeProblemType.MistypedParameter);

			dlg.sendRejectComponent(serverResetTimerRequestInvokeId.get(), problem);
			server.handleSent(EventType.RejectComponent, null);
			dlg.send(dummyCallback);
		}

		// 6. reject ResetTimerRequest
		server.awaitSent(EventType.RejectComponent);

		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			client.handleSent(EventType.DialogUserAbort, null);
			dlg.abort(CAPUserAbortReason.missing_reference, dummyCallback);
		}

		// 7. DialogUserAbort: AbortReason=missing_reference
		client.awaitSent(EventType.DialogUserAbort);

		server.awaitReceived(EventType.DialogUserAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ActivityTestRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.InvokeTimeout);
		clientExpected.addSent(EventType.CancelRequest);
		clientExpected.addSent(EventType.ResetTimerRequest);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.DialogUserAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ActivityTestRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.ResetTimerRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.RejectComponent);
		serverExpected.addReceived(EventType.DialogUserAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * DialogTimeout test ACN=CAP-v3-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest
	 * TC-CONTINUE empty (no answer - DialogTimeout at both sides)
	 * </pre>
	 */
	@Test
	public void testDialogTimeout() throws Exception {
		// setting dialog timeout little interval to invoke onDialogTimeout on SCF side
		final long clientInvokeTimeout = 1500;
		final long clientDialogTimeout = 1500;

		client.capStack.getTCAPStack().setInvokeTimeout(clientInvokeTimeout);
		client.capStack.getTCAPStack().setDialogIdleTimeout(clientDialogTimeout);
		client.suppressInvokeTimeout();

		final long serverInvokeTimeout = 1900;
		final long serverDialogTimeout = 2100;

		server.capStack.getTCAPStack().setInvokeTimeout(serverInvokeTimeout);
		server.capStack.getTCAPStack().setDialogIdleTimeout(serverDialogTimeout);
		server.suppressInvokeTimeout();

		// 1. TC-BEGIN + InitialDPRequest
		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			assertTrue(Client.checkTestInitialDp(ind));
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.send(dummyCallback);
		}
		TestEventUtils.updateStamp();

		// 2. TC-CONTINUE empty (no answer - DialogTimeout at both sides)
		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(clientDialogTimeout);

		client.awaitReceived(EventType.DialogProviderAbort);

		server.awaitReceived(EventType.DialogProviderAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.DialogTimeout);
		clientExpected.addReceived(EventType.DialogProviderAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogProviderAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * ACNNotSuported test ACN=CAP-v3-gsmSSF-to-gsmSCF
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPRequest (Server service is down -> ACN not supported)
	 * TC-ABORT + ACNNotSuported
	 * </pre>
	 */
	@Test
	public void testACNNotSuported() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason,
					CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);

				assertEquals(generalReason, CAPGeneralAbortReason.ACNNotSupported);
				assertNull(userReason);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.Abort);
			}
		};

		// 1. TC-BEGIN + InitialDPRequest (Server service is down -> ACN not supported)
		server.capProvider.getCAPServiceCircuitSwitchedCall().deactivate();
		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);
		client.awaitSent(EventType.InitialDpRequest);

		// 2. TC-ABORT + ACNNotSuported
		client.awaitReceived(EventType.DialogUserAbort);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addReceived(EventType.DialogUserAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Bad data sending at TC-BEGIN test - no ACN
	 *
	 * <pre>
	 * TC-BEGIN + no ACN
	 * TC-ABORT + BadReceivedData
	 * </pre>
	 */
	@Test
	public void testBadDataSendingNoAcn() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason generalReason,
					CAPUserAbortReason userReason) {
				super.onDialogUserAbort(capDialog, generalReason, userReason);

				assertNull(generalReason);
				assertNull(userReason);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.Abort);
			}
		};

		// 1. TC-BEGIN + no ACN
		client.sendBadDataNoAcn();

		// 2. TC-ABORT + BadReceivedData
		client.awaitReceived(EventType.DialogUserAbort);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addReceived(EventType.DialogUserAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * TC-CONTINUE from Server after dialogRelease at Client
	 *
	 * <pre>
	 * TC-BEGIN + InitialDP
	 * relaseDialog 
	 * TC-CONTINUE
	 * ProviderAbort
	 * </pre>
	 */
	@Test
	public void testProviderAbort() throws Exception {
		server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
				super.onDialogProviderAbort(capDialog, abortCause);

				assertEquals(abortCause, PAbortCauseType.UnrecognizedTxID);
			}
		};

		// 1. TC-BEGIN + InitialDP
		client.sendInitialDp(CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		server.awaitReceived(EventType.DialogDelimiter);

		// 2. relaseDialog
		client.releaseDialog();

		// 3. TC-CONTINUE
		server.sendAccept();

		// 4. ProviderAbort
		server.awaitReceived(EventType.DialogProviderAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogProviderAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * referensedNumber
	 *
	 * <pre>
	 * TC-BEGIN + referensedNumber
	 * TC-END + referensedNumber
	 * </pre>
	 */
	@Test
	public void testReferensedNumber() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogAccept(capDialog, capGprsReferenceNumber);

				assertEquals((int) capGprsReferenceNumber.getDestinationReference(), 10005);
				assertEquals((int) capGprsReferenceNumber.getOriginationReference(), 10006);
			}
		};

		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogRequest(capDialog, capGprsReferenceNumber);

				assertEquals((int) capGprsReferenceNumber.getDestinationReference(), 1005);
				assertEquals((int) capGprsReferenceNumber.getOriginationReference(), 1006);
			}
		};

		// 1. TC-BEGIN + referensedNumber
		client.sendReferensedNumber();

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

			CAPGprsReferenceNumber capGprsReferenceNumber = server.capParameterFactory
					.createCAPGprsReferenceNumber(10005, 10006);
			dlg.setGprsReferenceNumber(capGprsReferenceNumber);
			dlg.close(false, dummyCallback);
		}

		// 2. TC-END + referensedNumber
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + broken referensedNumber
	 * TC-ABORT
	 * </pre>
	 */
	@Test
	public void testMessageUserDataLength() throws Exception {
		// client = new Client(stack1, peer1Address, peer2Address) {
		// @Override
		// public void onDialogUserAbort(CAPDialog capDialog, CAPGeneralAbortReason
		// generalReason,
		// CAPUserAbortReason userReason) {
		// super.onDialogUserAbort(capDialog, generalReason, userReason);
		// assertEquals(capDialog.getTCAPMessageType(), MessageType.Abort);
		// }
		// };
		//
		// 1. TC-BEGIN + broken referensedNumber
		// client.testMessageUserDataLength();
		//
		// TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		// clientExpected.addReceived(EventType.DialogUserAbort);
		// clientExpected.addReceived(EventType.DialogRelease);
		//
		// TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		//
		// client.awaitReceived(EventType.DialogRelease);
		// server.awaitReceived(EventType.DialogRelease);
		//
		// TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		// TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
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
	 * TC-BEGIN + referensedNumber + initialDPRequest + initialDPRequest
	 * TC-CONTINUE + sendDelayed(ContinueRequest) + sendDelayed(ContinueRequest)
	 * TC-END + closeDelayed(CancelRequest) + sendDelayed(CancelRequest)
	 * </pre>
	 */
	@Test
	public void testDelayedSendClose() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogAccept(capDialog, capGprsReferenceNumber);

				assertEquals((int) capGprsReferenceNumber.getDestinationReference(), 201);
				assertEquals((int) capGprsReferenceNumber.getOriginationReference(), 202);

				assertEquals(capDialog.getTCAPMessageType(), MessageType.Continue);
			}

			@Override
			public void onContinueRequest(ContinueRequest ind) {
				super.onContinueRequest(ind);

				CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();
				assertEquals(d.getTCAPMessageType(), MessageType.Continue);

				assertEquals((int) d.getReceivedGprsReferenceNumber().getDestinationReference(), 201);
				assertEquals((int) d.getReceivedGprsReferenceNumber().getOriginationReference(), 202);

				client.awaitSent(EventType.CancelRequest);
			};
		};

		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogRequest(capDialog, capGprsReferenceNumber);

				assertEquals((int) capGprsReferenceNumber.getDestinationReference(), 101);
				assertEquals((int) capGprsReferenceNumber.getOriginationReference(), 102);
			}

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();
				assertTrue(Client.checkTestInitialDp(ind));

				assertEquals((int) d.getReceivedGprsReferenceNumber().getDestinationReference(), 101);
				assertEquals((int) d.getReceivedGprsReferenceNumber().getOriginationReference(), 102);

				assertEquals(d.getTCAPMessageType(), MessageType.Begin);
				server.awaitSent(EventType.ContinueRequest);
			}
		};

		// 1. TC-BEGIN + referensedNumber + initialDPRequest + initialDPRequest
		client.sendInitialDp2();
		client.awaitSent(EventType.InitialDpRequest);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogRequest);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPGprsReferenceNumber grn = server.capParameterFactory.createCAPGprsReferenceNumber(201, 202);
			capDialog.setGprsReferenceNumber(grn);
		}
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();
			d.addContinueRequest();
			d.sendDelayed(dummyCallback);
			server.handleSent(EventType.ContinueRequest, null);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		// server's awaiting for sending ContinueRequest is implemented in listener
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();
			d.addContinueRequest();
			d.sendDelayed(dummyCallback);
			server.handleSent(EventType.ContinueRequest, null);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		// server's awaiting for sending ContinueRequest is implemented in listener
		server.awaitReceived(EventType.DialogDelimiter);

		// 2. TC-CONTINUE + sendDelayed(ContinueRequest) + sendDelayed(ContinueRequest)
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ContinueRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ContinueRequest);
			ContinueRequest ind = (ContinueRequest) event.getEvent();

			CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();
			d.addCancelRequest_AllRequests();
			d.closeDelayed(false, dummyCallback);

			client.handleSent(EventType.CancelRequest, null);
		}
		// client's awaiting for sending CancelRequest is implemented in listener above
		client.awaitReceived(EventType.ContinueRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ContinueRequest);
			ContinueRequest ind = (ContinueRequest) event.getEvent();

			CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();
			d.addCancelRequest_AllRequests();
			d.sendDelayed(dummyCallback);

			client.handleSent(EventType.CancelRequest, null);
		}
		// client's awaiting for sending CancelRequest is implemented in listener above
		client.awaitReceived(EventType.DialogDelimiter);

		// 3. TC-END + closeDelayed(CancelRequest) + sendDelayed(CancelRequest)
		server.awaitReceived(EventType.CancelRequest);
		server.awaitReceived(EventType.CancelRequest);
		server.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ContinueRequest);
		clientExpected.addSent(EventType.CancelRequest);
		clientExpected.addReceived(EventType.ContinueRequest);
		clientExpected.addSent(EventType.CancelRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addSent(EventType.ContinueRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addSent(EventType.ContinueRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.CancelRequest);
		serverExpected.addReceived(EventType.CancelRequest);
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
	 * TC-BEGIN + initialDPRequest + initialDPRequest 
	 * TC-END + Prearranged + [ContinueRequest + ContinueRequest]
	 * </pre>
	 */
	@Test
	public void testDelayedClosePrearranged() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogAccept(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogAccept(capDialog, capGprsReferenceNumber);

				assertNull(capGprsReferenceNumber);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.End);
			}
		};

		server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogRequest(CAPDialog capDialog, CAPGprsReferenceNumber capGprsReferenceNumber) {
				super.onDialogRequest(capDialog, capGprsReferenceNumber);

				assertNull(capGprsReferenceNumber);
				assertEquals(capDialog.getTCAPMessageType(), MessageType.Begin);
			}

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				server.awaitSent(EventType.ContinueRequest);
			}
		};

		// 1. TC-BEGIN + initialDPRequest + initialDPRequest
		client.sendInitialDp3();
		client.clientCscDialog.close(true, dummyCallback);

		client.awaitSent(EventType.InitialDpRequest);
		client.awaitSent(EventType.InitialDpRequest);

		// 2. TC-END + Prearranged + [ContinueRequest + ContinueRequest]
		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();

			assertTrue(Client.checkTestInitialDp(ind));
			assertNull(d.getReceivedGprsReferenceNumber());
			assertEquals(d.getTCAPMessageType(), MessageType.Begin);

			d.addContinueRequest();
			d.sendDelayed(dummyCallback);

			server.handleSent(EventType.ContinueRequest, null);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			CAPDialogCircuitSwitchedCall d = ind.getCAPDialog();

			assertTrue(Client.checkTestInitialDp(ind));
			assertNull(d.getReceivedGprsReferenceNumber());
			assertEquals(d.getTCAPMessageType(), MessageType.Begin);

			d.addContinueRequest();
			d.closeDelayed(true, dummyCallback);

			server.handleSent(EventType.ContinueRequest, null);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addSent(EventType.InitialDpRequest);
		// Uncomment the following lines if you want to include them:
		// clientExpected.addReceived(EventType.DialogAccept);
		// clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addSent(EventType.ContinueRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addSent(EventType.ContinueRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Testing for some special error cases:
	 * <p>
	 * - linkedId to an operation that does not support linked operations
	 * <p>
	 * - linkedId to a missed operation
	 *
	 * <pre>
	 * TC-BEGIN + initialDPRequest + playAnnouncement 
	 * TC-CONTINUE + SpecializedResourceReportRequest to initialDPRequest (->
	 * LinkedResponseUnexpected) + SpecializedResourceReportRequest to a missed
	 * operation (linkedId==bad==50 -> UnrechognizedLinkedID) + ContinueRequest to a
	 * playAnnouncement operation (-> UnexpectedLinkedOperation) +
	 * SpecializedResourceReportRequest to a playAnnouncement operation (-> normal
	 * case)
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testBadInvokeLinkedId() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			private AtomicInteger rejectStep = new AtomicInteger(0);

			@Override
			public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(capDialog, invokeId, problem, isLocalOriginated);

				try {
					switch (rejectStep.incrementAndGet()) {
					case 1:
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.LinkedResponseUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 2:
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnrechognizedLinkedID);
						assertTrue(isLocalOriginated);
						break;
					case 3:
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnexpectedLinkedOperation);
						assertTrue(isLocalOriginated);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}
		};

		final AtomicInteger outInvokeId1 = new AtomicInteger(0);
		final AtomicInteger outInvokeId2 = new AtomicInteger(0);
		final AtomicInteger outInvokeId3 = new AtomicInteger(0);

		server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(capDialog, invokeId, problem, isLocalOriginated);

				try {
					if (invokeId == outInvokeId1.get()) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.LinkedResponseUnexpected);
						assertFalse(isLocalOriginated);
					} else if (invokeId == outInvokeId2.get()) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnrechognizedLinkedID);
						assertFalse(isLocalOriginated);
					} else if (invokeId == outInvokeId3.get()) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnexpectedLinkedOperation);
						assertFalse(isLocalOriginated);
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}
		};

		// 1. TC-BEGIN + initialDPRequest + playAnnouncement
		client.sendInitialDp_playAnnouncement();
		client.awaitSent(EventType.InitialDpRequest);
		client.awaitSent(EventType.PlayAnnouncementRequest);

		int invokeId1, invokeId2;

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			invokeId1 = ind.getInvokeId();
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());

		}
		server.awaitReceived(EventType.PlayAnnouncementRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.PlayAnnouncementRequest);
			PlayAnnouncementRequest ind = (PlayAnnouncementRequest) event.getEvent();

			invokeId2 = ind.getInvokeId();
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCallImpl dlg = (CAPDialogCircuitSwitchedCallImpl) capDialog;

			outInvokeId1.set(dlg.addSpecializedResourceReportRequest_CapV23(invokeId1));
			outInvokeId2.set(dlg.addSpecializedResourceReportRequest_CapV23(50));
			outInvokeId3.set(dlg.sendDataComponent(null, invokeId2, null, 2000L, CAPOperationCode.continueCode, null,
					true, false));

			dlg.addSpecializedResourceReportRequest_CapV23(invokeId2);
			server.handleSent(EventType.SpecializedResourceReportRequest, null);
			server.handleSent(EventType.SpecializedResourceReportRequest, null);
			server.handleSent(EventType.ContinueRequest, null);
			server.handleSent(EventType.SpecializedResourceReportRequest, null);

			dlg.send(dummyCallback);
		}

		// 2. TC-CONTINUE + ...
		server.awaitSent(EventType.SpecializedResourceReportRequest);
		server.awaitSent(EventType.SpecializedResourceReportRequest);
		server.awaitSent(EventType.ContinueRequest);
		server.awaitSent(EventType.SpecializedResourceReportRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.SpecializedResourceReportRequest);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.close(false, dummyCallback);
		}

		// 3. TC-END
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addSent(EventType.PlayAnnouncementRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.SpecializedResourceReportRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addReceived(EventType.PlayAnnouncementRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SpecializedResourceReportRequest);
		serverExpected.addSent(EventType.SpecializedResourceReportRequest);
		serverExpected.addSent(EventType.ContinueRequest);
		serverExpected.addSent(EventType.SpecializedResourceReportRequest);
		serverExpected.addReceived(EventType.RejectComponent);
		serverExpected.addReceived(EventType.RejectComponent);
		serverExpected.addReceived(EventType.RejectComponent);
		serverExpected.addReceived(EventType.DialogClose);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * ReturnResultLast & ReturnError for operation classes 1, 2, 3, 4
	 *
	 * <pre>
	 * TC-BEGIN + initialDPRequest (class2, invokeId==1) + initialDPRequest (class2,
	 * invokeId==2) + promptAndCollectUserInformationRequest (class1, invokeId==3) +
	 * promptAndCollectUserInformationRequest (class1, invokeId==4) + +
	 * activityTestRequest (class3, invokeId==5) + activityTestRequest (class3,
	 * invokeId==6) + releaseCallRequest (class4, invokeId==7) + releaseCallRequest
	 * (class4, invokeId==7)
	 *
	 * TC-CONTINUE + ReturnResultLast (initialDP, invokeId==1 ->
	 * ReturnResultUnexpected) + SystemFailureError (initialDP, invokeId==2 -> OK) +
	 * promptAndCollectUserInformationResponse (invokeId==3 -> OK) +
	 * SystemFailureError (promptAndCollectUserInformation, invokeId==4 -> OK) +
	 * activityTestResponse (invokeId==5 -> OK) + SystemFailureError (activityTest,
	 * invokeId==6 -> ReturnErrorUnexpected) + ReturnResultLast (releaseCall,
	 * invokeId==7 -> ReturnResultUnexpected) + SystemFailureError
	 * (releaseCallRequest, invokeId==8 -> ReturnErrorUnexpected)
	 * 
	 * TC-END + Reject
	 * (ReturnResultUnexpected) + Reject (ReturnErrorUnexpected) + Reject
	 * (ReturnResultUnexpected) + Reject (ReturnErrorUnexpected)
	 * </pre>
	 */
	@Test
	public void testUnexpectedResultError() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			private AtomicInteger rejectStep = new AtomicInteger(0);

			@Override
			public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(capDialog, invokeId, problem, isLocalOriginated);

				try {
					switch (rejectStep.incrementAndGet()) {
					case 1:
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 2:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 3:
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertTrue(isLocalOriginated);
						break;
					case 4:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertTrue(isLocalOriginated);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}
		};

		final List<Integer> invokeIds = new ArrayList<Integer>();
		server = new Server(stack2, peer2Address, peer1Address) {
			private AtomicInteger rejectStep = new AtomicInteger(0);

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				invokeIds.add(ind.getInvokeId()); // 1 & 2
			}

			@Override
			public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
				super.onPromptAndCollectUserInformationRequest(ind);

				invokeIds.add(ind.getInvokeId()); // 3 & 4
			}

			@Override
			public void onActivityTestRequest(ActivityTestRequest ind) {
				super.onActivityTestRequest(ind);

				invokeIds.add(ind.getInvokeId()); // 5 & 6
			}

			@Override
			public void onReleaseCallRequest(ReleaseCallRequest ind) {
				super.onReleaseCallRequest(ind);

				invokeIds.add(ind.getInvokeId()); // 7 & 8
			}

			@Override
			public void onRejectComponent(CAPDialog capDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(capDialog, invokeId, problem, isLocalOriginated);

				try {
					switch (rejectStep.incrementAndGet()) {
					case 1:
						assertEquals(invokeId, invokeIds.get(0));
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 2:
						assertEquals(invokeId, invokeIds.get(5));
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 3:
						assertEquals(invokeId, invokeIds.get(6));
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 4:
						assertEquals(invokeId, invokeIds.get(7));
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertFalse(isLocalOriginated);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}
		};

		// 1. TC-BEGIN
		client.sendInvokesForUnexpectedResultError();

		client.awaitSent(EventType.InitialDpRequest);
		client.awaitSent(EventType.InitialDpRequest);
		client.awaitSent(EventType.PromptAndCollectUserInformationRequest);
		client.awaitSent(EventType.PromptAndCollectUserInformationRequest);
		client.awaitSent(EventType.ActivityTestRequest);
		client.awaitSent(EventType.ActivityTestRequest);
		client.awaitSent(EventType.ReleaseCallRequest);
		client.awaitSent(EventType.ReleaseCallRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		server.awaitReceived(EventType.PromptAndCollectUserInformationRequest);
		server.awaitReceived(EventType.PromptAndCollectUserInformationRequest);
		server.awaitReceived(EventType.ActivityTestRequest);
		server.awaitReceived(EventType.ActivityTestRequest);
		server.awaitReceived(EventType.ReleaseCallRequest);
		server.awaitReceived(EventType.ReleaseCallRequest);
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCallImpl dlg = (CAPDialogCircuitSwitchedCallImpl) capDialog;
			dlg.sendDataComponent(invokeIds.get(0), null, null, null, CAPOperationCode.initialDP, null, false, true);

			CAPErrorMessageFactory errorMessageFactory = server.capErrorMessageFactory;

			CAPErrorMessage mem = errorMessageFactory
					.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.endUserFailure);
			dlg.sendErrorComponent(invokeIds.get(1), mem);
			server.handleSent(EventType.ErrorComponent, null);

			GenericNumber genericNumber = server.isupParameterFactory.createGenericNumber();
			genericNumber.setAddress("444422220000");
			genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
			genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_SUBSCRIBER_NUMBER);
			genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_DATA);
			genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLING_PARTY_NUMBER);
			genericNumber.setScreeningIndicator(GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
			DigitsIsup digitsResponse = server.capParameterFactory.createDigits_GenericNumber(genericNumber);
			dlg.addPromptAndCollectUserInformationResponse_DigitsResponse(invokeIds.get(2), digitsResponse);
			server.handleSent(EventType.PromptAndCollectUserInformationResponse, null);

			mem = errorMessageFactory
					.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
			dlg.sendErrorComponent(invokeIds.get(3), mem);
			server.handleSent(EventType.ErrorComponent, null);

			dlg.addActivityTestResponse(invokeIds.get(4));
			server.handleSent(EventType.ActivityTestResponse, null);

			mem = errorMessageFactory
					.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
			dlg.sendErrorComponent(invokeIds.get(5), mem);
			server.handleSent(EventType.ErrorComponent, null);

			dlg.sendDataComponent(invokeIds.get(6), null, null, null, CAPOperationCode.releaseCall, null, false, true);

			mem = errorMessageFactory
					.createCAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
			dlg.sendErrorComponent(invokeIds.get(7), mem);
			server.handleSent(EventType.ErrorComponent, null);

			dlg.send(dummyCallback);
		}

		// 2. TC-CONTINUE
		server.awaitSent(EventType.ErrorComponent);
		server.awaitSent(EventType.PromptAndCollectUserInformationResponse);
		server.awaitSent(EventType.ErrorComponent);
		server.awaitSent(EventType.ActivityTestResponse);
		server.awaitSent(EventType.ErrorComponent);
		server.awaitSent(EventType.ErrorComponent);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.ErrorComponent);
		client.awaitReceived(EventType.PromptAndCollectUserInformationResponse);
		client.awaitReceived(EventType.ErrorComponent);
		client.awaitReceived(EventType.ActivityTestResponse);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.close(false, dummyCallback);
		}

		// 3. TC-END
		server.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addSent(EventType.PromptAndCollectUserInformationRequest);
		clientExpected.addSent(EventType.PromptAndCollectUserInformationRequest);
		clientExpected.addSent(EventType.ActivityTestRequest);
		clientExpected.addSent(EventType.ActivityTestRequest);
		clientExpected.addSent(EventType.ReleaseCallRequest);
		clientExpected.addSent(EventType.ReleaseCallRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.ErrorComponent);
		clientExpected.addReceived(EventType.PromptAndCollectUserInformationResponse);
		clientExpected.addReceived(EventType.ErrorComponent);
		clientExpected.addReceived(EventType.ActivityTestResponse);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.RejectComponent);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addReceived(EventType.PromptAndCollectUserInformationRequest);
		serverExpected.addReceived(EventType.PromptAndCollectUserInformationRequest);
		serverExpected.addReceived(EventType.ActivityTestRequest);
		serverExpected.addReceived(EventType.ActivityTestRequest);
		serverExpected.addReceived(EventType.ReleaseCallRequest);
		serverExpected.addReceived(EventType.ReleaseCallRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addSent(EventType.PromptAndCollectUserInformationResponse);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addSent(EventType.ActivityTestResponse);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addReceived(EventType.RejectComponent);
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
	 * TC-Message + bad UnrecognizedMessageType
	 * TC-ABORT UnrecognizedMessageType
	 * </pre>
	 */
	@Test
	public void testUnrecognizedMessageType() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogProviderAbort(CAPDialog capDialog, PAbortCauseType abortCause) {
				super.onDialogProviderAbort(capDialog, abortCause);

				assertEquals(abortCause, PAbortCauseType.UnrecognizedMessageType);
			}
		};

		// 1. TC-Message + bad UnrecognizedMessageType
		// sending a dummy message to a bad address for a dialog starting
		client.sendDummyMessage();

		// sending a badly formatted message
		final byte[] badTag = new byte[] { 106, 6, 72, 1, 1, 73, 1, 1 };
		SccpDataMessage message = sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address, peer1Address,
				Unpooled.wrappedBuffer(badTag), 0, 0, false, null, null);

		sccpProvider1.send(message, dummyCallback);

		// 2. TC-ABORT UnrecognizedMessageType
		client.awaitReceived(EventType.DialogProviderAbort);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addReceived(EventType.DialogProviderAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * <pre>
	 * TC-BEGIN + (bad sccp address + setReturnMessageOnError)
	 * TC-NOTICE
	 * </pre>
	 */
	@Test
	public void testTcNotice() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onDialogNotice(CAPDialog capDialog, CAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
				super.onDialogNotice(capDialog, noticeProblemDiagnostic);

				assertEquals(noticeProblemDiagnostic, CAPNoticeProblemDiagnostic.MessageCannotBeDeliveredToThePeer);
			}
		};

		// 1. TC-BEGIN + (bad sccp address + setReturnMessageOnError)
		client.actionB();

		// 2. TC-NOTICE
		client.awaitReceived(EventType.DialogNotice);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addReceived(EventType.DialogNotice);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * GPSR messageflow 1 ACN=cap3-gprssf-scf
	 *
	 * <pre>
	 * TC-BEGIN + InitialDPGPRSRequest + originationReference=1001
	 * TC-CONTINUE + requestReportGPRSEventRequest + destinationReference=1001 + originationReference=2001 
	 * TC-CONTINUE + furnishChargingInformationGPRSRequest
	 * TC-CONTINUE + eventReportGPRSRequest
	 * TC-CONTINUE + eventReportGPRSResponse
	 * TC-CONTINUE + resetTimerGPRSRequest
	 * TC-CONTINUE + applyChargingGPRSRequest + connectGPRSRequest 
	 * TC-CONTINUE + applyChargingReportGPRSRequest
	 * TC-END + applyChargingReportGPRSResponse
	 * </pre>
	 */
	@Test
	public void testGPRS1() throws Exception {
		// 1. TC-BEGIN + InitialDPGPRSRequest + originationReference=1001
		client.suppressInvokeTimeout();
		client.sendInitialDpGprs(CAPApplicationContext.CapV3_gprsSSF_gsmSCF);
		client.awaitSent(EventType.InitialDpGprsRequest);

		CAPDialogGprs serverDlg;

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpGprsRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpGprsRequest);
			InitialDpGprsRequest ind = (InitialDpGprsRequest) event.getEvent();

			assertTrue(Client.checkTestInitialDpGprsRequest(ind));
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			serverDlg = (CAPDialogGprs) capDialog;
		}

		// 2. TC-CONTINUE + requestReportGPRSEventRequest + ...
		{
			RequestReportGPRSEventRequest rrc = server.getRequestReportGPRSEventRequest();
			serverDlg.addRequestReportGPRSEventRequest(rrc.getGPRSEvent(), rrc.getPDPID());
			server.handleSent(EventType.RequestReportGPRSEventRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.RequestReportGPRSEventRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RequestReportGPRSEventRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.RequestReportGPRSEventRequest);
			RequestReportGPRSEventRequest ind = (RequestReportGPRSEventRequest) event.getEvent();

			Client.checkRequestReportGPRSEventRequest(ind);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 3. TC-CONTINUE + furnishChargingInformationGPRSRequest
		{
			byte[] bFreeFormatData = new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };

			FreeFormatDataGprsImpl freeFormatData = new FreeFormatDataGprsImpl(Unpooled.wrappedBuffer(bFreeFormatData));
			PDPIDImpl pdpID = new PDPIDImpl(2);
			FCIBCCCAMELSequence1GprsImpl fcIBCCCAMELsequence1 = new FCIBCCCAMELSequence1GprsImpl(freeFormatData, pdpID,
					AppendFreeFormatData.append);

			CAMELFCIGPRSBillingChargingCharacteristicsImpl fciGPRSBillingChargingCharacteristics = new CAMELFCIGPRSBillingChargingCharacteristicsImpl(
					fcIBCCCAMELsequence1);

			serverDlg.addFurnishChargingInformationGPRSRequest(fciGPRSBillingChargingCharacteristics);
			serverDlg.send(dummyCallback);
			server.handleSent(EventType.FurnishChargingInformationGPRSRequest, null);
		}
		server.awaitSent(EventType.FurnishChargingInformationGPRSRequest);

		client.awaitReceived(EventType.FurnishChargingInformationGPRSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.FurnishChargingInformationGPRSRequest);
			FurnishChargingInformationGPRSRequest ind = (FurnishChargingInformationGPRSRequest) event.getEvent();

			byte[] bFreeFormatData = new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };
			assertTrue(ByteBufUtil.equals(ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1()
					.getFreeFormatData().getValue(), Unpooled.wrappedBuffer(bFreeFormatData)));
			assertEquals(ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getPDPID().getId(),
					2);
			assertEquals(
					ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getAppendFreeFormatData(),
					AppendFreeFormatData.append);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

			GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
			MiscCallInfoImpl miscGPRSInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);
			LAIFixedLengthImpl lai = new LAIFixedLengthImpl(250, 1, 4444);

			CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
			RAIdentityImpl ra = new RAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 11, 12, 13, 14, 15, 16 }));

			GeographicalInformationImpl ggi = null;
			ByteBuf geoBuffer = Unpooled.wrappedBuffer(new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 });
			ggi = new GeographicalInformationImpl(
					GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF),
					GeographicalInformationImpl.decodeLatitude(geoBuffer),
					GeographicalInformationImpl.decodeLongitude(geoBuffer),
					GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));

			ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number,
					NumberingPlan.ISDN, "654321");
			LSAIdentityImpl lsa = new LSAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 91, 92, 93 }));

			GeodeticInformationImpl gdi = null;
			ByteBuf geodeticBuffer = Unpooled.wrappedBuffer(new byte[] { 1, 16, 3, 4, 5, 6, 7, 8, 9, 10 });
			gdi = new GeodeticInformationImpl(geodeticBuffer.readByte() & 0x0FF,
					GeographicalInformationImpl.decodeTypeOfShape(geodeticBuffer.readByte() & 0x0FF),
					GeographicalInformationImpl.decodeLatitude(geodeticBuffer),
					GeographicalInformationImpl.decodeLongitude(geodeticBuffer),
					GeographicalInformationImpl.decodeUncertainty(geodeticBuffer.readByte() & 0x0FF),
					geodeticBuffer.readByte() & 0x0FF);

			LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn,
					lsa, null, true, gdi, true, 13);
			GPRSEventSpecificInformationImpl gprsEventSpecificInformation = new GPRSEventSpecificInformationImpl(
					locationInformationGPRS);
			PDPIDImpl pdpID = new PDPIDImpl(1);
			dlg.addEventReportGPRSRequest(gprsEventType, miscGPRSInfo, gprsEventSpecificInformation, pdpID);

			client.handleSent(EventType.EventReportGPRSRequest, null);
			dlg.send(dummyCallback);
		}

		// 4. TC-CONTINUE + eventReportGPRSRequest
		client.awaitSent(EventType.EventReportGPRSRequest);

		int eventReportGPRSResponse;
		server.awaitReceived(EventType.EventReportGPRSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.EventReportGPRSRequest);
			EventReportGPRSRequest ind = (EventReportGPRSRequest) event.getEvent();

			assertEquals(ind.getGPRSEventType(), GPRSEventType.attachChangeOfPosition);
			assertNotNull(ind.getMiscGPRSInfo().getMessageType());
			assertNull(ind.getMiscGPRSInfo().getDpAssignment());
			assertEquals(ind.getMiscGPRSInfo().getMessageType(), MiscCallInfoMessageType.notification);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			eventReportGPRSResponse = ind.getInvokeId();
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			serverDlg = (CAPDialogGprs) capDialog;
		}

		// 5. TC-CONTINUE + eventReportGPRSResponse
		{
			serverDlg.addEventReportGPRSResponse(eventReportGPRSResponse);
			server.handleSent(EventType.EventReportGPRSResponse, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.EventReportGPRSResponse);

		client.awaitReceived(EventType.EventReportGPRSResponse);
		client.awaitReceived(EventType.DialogDelimiter);

		// 6. TC-CONTINUE + resetTimerGPRSRequest
		{
			serverDlg.addResetTimerGPRSRequest(TimerID.tssf, 12);
			server.handleSent(EventType.ResetTimerGPRSRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.ResetTimerGPRSRequest);

		client.awaitReceived(EventType.ResetTimerGPRSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ResetTimerGPRSRequest);
			ResetTimerGPRSRequest ind = (ResetTimerGPRSRequest) event.getEvent();

			assertEquals(ind.getTimerValue(), 12);
			assertEquals(ind.getTimerID(), TimerID.tssf);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 7. TC-CONTINUE + applyChargingGPRSRequest + connectGPRSRequest
		{
			ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(200L);

			Integer tariffSwitchInterval = new Integer(24);
			PDPIDImpl pdpIDApplyCharging = new PDPIDImpl(2);
			serverDlg.addApplyChargingGPRSRequest(chargingCharacteristics, tariffSwitchInterval, pdpIDApplyCharging);
			server.handleSent(EventType.ApplyChargingGPRSRequest, null);

			AccessPointNameImpl accessPointName = new AccessPointNameImpl(
					Unpooled.wrappedBuffer(new byte[] { 52, 20, 30 }));
			PDPIDImpl pdpIDConnectRequest = new PDPIDImpl(2);
			serverDlg.addConnectGPRSRequest(accessPointName, pdpIDConnectRequest);
			server.handleSent(EventType.ConnectGPRSRequest, null);
			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.ApplyChargingGPRSRequest);
		server.awaitSent(EventType.ConnectGPRSRequest);

		client.awaitReceived(EventType.ApplyChargingGPRSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ApplyChargingGPRSRequest);
			ApplyChargingGPRSRequest ind = (ApplyChargingGPRSRequest) event.getEvent();

			assertEquals(ind.getChargingCharacteristics().getMaxTransferredVolume(), 200L);
			assertEquals(ind.getTariffSwitchInterval().intValue(), 24);
			assertEquals(ind.getPDPID().getId(), 2);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.ConnectGPRSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ConnectGPRSRequest);
			ConnectGPRSRequest ind = (ConnectGPRSRequest) event.getEvent();

			assertTrue(ByteBufUtil.equals(ind.getAccessPointName().getValue(),
					Unpooled.wrappedBuffer(new byte[] { 52, 20, 30 })));
			assertEquals(ind.getPDPID().getId(), 2);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

			ElapsedTimeImpl elapsedTime = new ElapsedTimeImpl(new Integer(5320));
			ChargingResultImpl chargingResult = new ChargingResultImpl(elapsedTime);
			boolean active = true;
			dlg.addApplyChargingReportGPRSRequest(chargingResult, null, active, null, null);
			client.handleSent(EventType.ApplyChargingReportGPRSRequest, null);
			dlg.send(dummyCallback);
		}

		// 8. TC-CONTINUE + applyChargingReportGPRSRequest
		client.awaitSent(EventType.ApplyChargingReportGPRSRequest);

		int applyChargingReportGPRSResponse;
		server.awaitReceived(EventType.ApplyChargingReportGPRSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ApplyChargingReportGPRSRequest);
			ApplyChargingReportGPRSRequest ind = (ApplyChargingReportGPRSRequest) event.getEvent();

			assertEquals(ind.getChargingResult().getElapsedTime().getTimeGPRSIfNoTariffSwitch().intValue(), 5320);
			assertNull(ind.getChargingResult().getTransferredVolume());
			assertNull(ind.getQualityOfService());
			assertTrue(ind.getActive());
			assertNull(ind.getPDPID());
			assertNull(ind.getChargingRollOver());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			applyChargingReportGPRSResponse = ind.getInvokeId();
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

			dlg.addApplyChargingReportGPRSResponse(applyChargingReportGPRSResponse);
			server.handleSent(EventType.ApplyChargingReportGPRSResponse, null);
			dlg.close(false, dummyCallback);
		}

		// 9. TC-END + applyChargingReportGPRSResponse
		server.awaitSent(EventType.ApplyChargingReportGPRSResponse);

		client.awaitReceived(EventType.ApplyChargingReportGPRSResponse);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpGprsRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.RequestReportGPRSEventRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.FurnishChargingInformationGPRSRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.EventReportGPRSRequest);
		clientExpected.addReceived(EventType.EventReportGPRSResponse);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.ResetTimerGPRSRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.ApplyChargingGPRSRequest);
		clientExpected.addReceived(EventType.ConnectGPRSRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.ApplyChargingReportGPRSRequest);
		clientExpected.addReceived(EventType.ApplyChargingReportGPRSResponse);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpGprsRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.RequestReportGPRSEventRequest);
		serverExpected.addSent(EventType.FurnishChargingInformationGPRSRequest);
		serverExpected.addReceived(EventType.EventReportGPRSRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.EventReportGPRSResponse);
		serverExpected.addSent(EventType.ResetTimerGPRSRequest);
		serverExpected.addSent(EventType.ApplyChargingGPRSRequest);
		serverExpected.addSent(EventType.ConnectGPRSRequest);
		serverExpected.addReceived(EventType.ApplyChargingReportGPRSRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ApplyChargingReportGPRSResponse);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * GPSR messageflow 2 ACN=cap3-gsmscf-gprsssf
	 *
	 * <pre>
	 * TC-BEGIN + activityTestGPRSSRequest + destinationReference=1001 + originationReference=2001 
	 * TC-CONTINUE + activityTestGPRSSResponse + destinationReference=2001 + originationReference=1001 
	 * TC-CONTINUE + furnishChargingInformationGPRSRequest + continueGPRSRequest 
	 * TC-CONTINUE + eventReportGPRSRequest 
	 * TC-END + eventReportGPRSResponse + cancelGPRSRequest
	 * </pre>
	 */
	@Test
	public void testGPRS2() throws Exception {
		// 1. TC-BEGIN + activityTestGPRSSRequest + destinationReference=1001 + ...
		client.suppressInvokeTimeout();
		client.sendActivityTestGPRSRequest(CAPApplicationContext.CapV3_gsmSCF_gprsSSF);
		client.awaitSent(EventType.ActivityTestGPRSRequest);

		int activityTestGPRSRequest;

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ActivityTestGPRSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ActivityTestGPRSRequest);
			ActivityTestGPRSRequest ind = (ActivityTestGPRSRequest) event.getEvent();

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			activityTestGPRSRequest = ind.getInvokeId();
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogGprs dlg = (CAPDialogGprs) capDialog;
			dlg.addActivityTestGPRSResponse(activityTestGPRSRequest);
			dlg.send(dummyCallback);
			server.handleSent(EventType.ActivityTestGPRSResponse, null);
		}

		// 2. TC-CONTINUE + activityTestGPRSSResponse + destinationReference=2001 + ...
		server.awaitSent(EventType.ActivityTestGPRSResponse);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ActivityTestGPRSResponse);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

			byte[] bFreeFormatData = new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };
			FreeFormatDataGprsImpl freeFormatData = new FreeFormatDataGprsImpl(Unpooled.wrappedBuffer(bFreeFormatData));
			PDPIDImpl pdpID = new PDPIDImpl(2);
			FCIBCCCAMELSequence1GprsImpl fcIBCCCAMELsequence1 = new FCIBCCCAMELSequence1GprsImpl(freeFormatData, pdpID,
					AppendFreeFormatData.append);
			CAMELFCIGPRSBillingChargingCharacteristicsImpl fciGPRSBillingChargingCharacteristics = new CAMELFCIGPRSBillingChargingCharacteristicsImpl(
					fcIBCCCAMELsequence1);

			dlg.addFurnishChargingInformationGPRSRequest(fciGPRSBillingChargingCharacteristics);
			client.handleSent(EventType.FurnishChargingInformationGPRSRequest, null);

			dlg.addContinueGPRSRequest(pdpID);
			client.handleSent(EventType.ContinueGPRSRequest, null);

			dlg.send(dummyCallback);
		}

		// 3. TC-CONTINUE + furnishChargingInformationGPRSRequest + continueGPRSRequest
		client.awaitSent(EventType.FurnishChargingInformationGPRSRequest);
		client.awaitSent(EventType.ContinueGPRSRequest);

		server.awaitReceived(EventType.FurnishChargingInformationGPRSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.FurnishChargingInformationGPRSRequest);
			FurnishChargingInformationGPRSRequest ind = (FurnishChargingInformationGPRSRequest) event.getEvent();

			byte[] bFreeFormatData = new byte[] { 48, 6, -128, 1, 5, -127, 1, 2 };
			assertTrue(ByteBufUtil.equals(ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1()
					.getFreeFormatData().getValue(), Unpooled.wrappedBuffer(bFreeFormatData)));
			assertEquals(ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getPDPID().getId(),
					2);
			assertEquals(
					ind.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getAppendFreeFormatData(),
					AppendFreeFormatData.append);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.ContinueGPRSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ContinueGPRSRequest);
			ContinueGPRSRequest ind = (ContinueGPRSRequest) event.getEvent();

			assertEquals(ind.getPDPID().getId(), 2);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

			GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
			MiscCallInfoImpl miscGPRSInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);
			LAIFixedLengthImpl lai;
			try {
				lai = new LAIFixedLengthImpl(250, 1, 4444);
			} catch (ASNParsingException e) {
				throw new CAPException(e.getMessage(), e);
			}
			CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
			RAIdentityImpl ra = new RAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 11, 12, 13, 14, 15, 16 }));

			GeographicalInformationImpl ggi = null;
			try {
				ByteBuf geoBuffer = Unpooled.wrappedBuffer(new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 });
				ggi = new GeographicalInformationImpl(
						GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF),
						GeographicalInformationImpl.decodeLatitude(geoBuffer),
						GeographicalInformationImpl.decodeLongitude(geoBuffer),
						GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));
			} catch (ASNParsingException e) {
				throw new CAPException(e.getMessage(), e);
			}

			ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number,
					NumberingPlan.ISDN, "654321");
			LSAIdentityImpl lsa = new LSAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 91, 92, 93 }));

			GeodeticInformationImpl gdi = null;
			try {
				ByteBuf geodeticBuffer = Unpooled.wrappedBuffer(new byte[] { 1, 16, 3, 4, 5, 6, 7, 8, 9, 10 });
				gdi = new GeodeticInformationImpl(geodeticBuffer.readByte() & 0x0FF,
						GeographicalInformationImpl.decodeTypeOfShape(geodeticBuffer.readByte() & 0x0FF),
						GeographicalInformationImpl.decodeLatitude(geodeticBuffer),
						GeographicalInformationImpl.decodeLongitude(geodeticBuffer),
						GeographicalInformationImpl.decodeUncertainty(geodeticBuffer.readByte() & 0x0FF),
						geodeticBuffer.readByte() & 0x0FF);
			} catch (ASNParsingException e) {
				throw new CAPException(e.getMessage(), e);
			}

			LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn,
					lsa, null, true, gdi, true, 13);
			GPRSEventSpecificInformationImpl gprsEventSpecificInformation = new GPRSEventSpecificInformationImpl(
					locationInformationGPRS);
			PDPIDImpl pdpID = new PDPIDImpl(1);
			dlg.addEventReportGPRSRequest(gprsEventType, miscGPRSInfo, gprsEventSpecificInformation, pdpID);

			server.handleSent(EventType.EventReportGPRSRequest, null);
			dlg.send(dummyCallback);
		}

		// 4. TC-CONTINUE + eventReportGPRSRequest
		server.awaitSent(EventType.EventReportGPRSRequest);

		int eventReportGPRSResponse;
		client.awaitReceived(EventType.EventReportGPRSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.EventReportGPRSRequest);
			EventReportGPRSRequest ind = (EventReportGPRSRequest) event.getEvent();

			assertEquals(ind.getGPRSEventType(), GPRSEventType.attachChangeOfPosition);
			assertNotNull(ind.getMiscGPRSInfo().getMessageType());
			assertNull(ind.getMiscGPRSInfo().getDpAssignment());
			assertEquals(ind.getMiscGPRSInfo().getMessageType(), MiscCallInfoMessageType.notification);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			eventReportGPRSResponse = ind.getInvokeId();
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogGprs dlg = (CAPDialogGprs) capDialog;

			dlg.addEventReportGPRSResponse(eventReportGPRSResponse);
			client.handleSent(EventType.EventReportGPRSResponse, null);

			PDPIDImpl pdpIDCancelGPRS = new PDPIDImpl(2);
			dlg.addCancelGPRSRequest(pdpIDCancelGPRS);
			client.handleSent(EventType.CancelGPRSRequest, null);
			dlg.close(false, dummyCallback);
		}

		// 5. TC-END + eventReportGPRSResponse + cancelGPRSRequest
		client.awaitSent(EventType.EventReportGPRSResponse);
		client.awaitSent(EventType.CancelGPRSRequest);

		server.awaitReceived(EventType.EventReportGPRSResponse);
		server.awaitReceived(EventType.CancelGPRSRequest);
		server.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ActivityTestGPRSRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ActivityTestGPRSResponse);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.FurnishChargingInformationGPRSRequest);
		clientExpected.addSent(EventType.ContinueGPRSRequest);
		clientExpected.addReceived(EventType.EventReportGPRSRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.EventReportGPRSResponse);
		clientExpected.addSent(EventType.CancelGPRSRequest);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ActivityTestGPRSRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ActivityTestGPRSResponse);
		serverExpected.addReceived(EventType.FurnishChargingInformationGPRSRequest);
		serverExpected.addReceived(EventType.ContinueGPRSRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.EventReportGPRSRequest);
		serverExpected.addReceived(EventType.EventReportGPRSResponse);
		serverExpected.addReceived(EventType.CancelGPRSRequest);
		serverExpected.addReceived(EventType.DialogClose);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * GPSR messageflow 3 ACN=cap3-gprssf-scf
	 *
	 * <pre>
	 * TC-BEGIN + eventReportGPRSRequest + destinationReference=2001 + originationReference=1001 
	 * TC-END + eventReportGPRSResponse + connectGPRSRequest + sendChargingInformationGPRSRequest + destinationReference=1001 + originationReference=2001
	 * </pre>
	 */
	@Test
	public void testGPRS3() throws Exception {
		int eventReportGPRSResponse;

		// 1. TC-BEGIN + eventReportGPRSRequest + destinationReference=2001 + ...
		client.suppressInvokeTimeout();
		client.sendEventReportGPRSRequest(CAPApplicationContext.CapV3_gprsSSF_gsmSCF);
		client.awaitSent(EventType.EventReportGPRSRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.EventReportGPRSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.EventReportGPRSRequest);
			EventReportGPRSRequest ind = (EventReportGPRSRequest) event.getEvent();

			assertEquals(ind.getGPRSEventType(), GPRSEventType.attachChangeOfPosition);
			assertNotNull(ind.getMiscGPRSInfo().getMessageType());
			assertNull(ind.getMiscGPRSInfo().getDpAssignment());
			assertEquals(ind.getMiscGPRSInfo().getMessageType(), MiscCallInfoMessageType.notification);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			eventReportGPRSResponse = ind.getInvokeId();
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogGprs dlg = (CAPDialogGprs) capDialog;
			dlg.addEventReportGPRSResponse(eventReportGPRSResponse);
			server.handleSent(EventType.EventReportGPRSResponse, null);

			AccessPointNameImpl accessPointName = new AccessPointNameImpl(
					Unpooled.wrappedBuffer(new byte[] { 52, 20, 30 }));
			PDPIDImpl pdpIDConnectRequest = new PDPIDImpl(2);
			dlg.addConnectGPRSRequest(accessPointName, pdpIDConnectRequest);
			server.handleSent(EventType.ConnectGPRSRequest, null);

			CAI_GSM0224Impl aocInitial = new CAI_GSM0224Impl(1, 2, 3, 4, 5, 6, 7);
			CAI_GSM0224Impl cai_GSM0224 = new CAI_GSM0224Impl(null, null, null, 4, 5, null, null);
			AOCSubsequentImpl aocSubsequent = new AOCSubsequentImpl(cai_GSM0224, 222);
			AOCGPRSImpl aocGPRS = new AOCGPRSImpl(aocInitial, aocSubsequent);
			PDPIDImpl pdpID = new PDPIDImpl(1);
			CAMELSCIGPRSBillingChargingCharacteristicsImpl sciGPRSBillingChargingCharacteristics = new CAMELSCIGPRSBillingChargingCharacteristicsImpl(
					aocGPRS, pdpID);
			dlg.addSendChargingInformationGPRSRequest(sciGPRSBillingChargingCharacteristics);
			server.handleSent(EventType.SendChargingInformationGPRSRequest, null);

			dlg.close(false, dummyCallback);
		}

		// 2. TC-END + eventReportGPRSResponse + connectGPRSRequest + ...
		server.awaitSent(EventType.EventReportGPRSResponse);
		server.awaitSent(EventType.ConnectGPRSRequest);
		server.awaitSent(EventType.SendChargingInformationGPRSRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ConnectGPRSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ConnectGPRSRequest);
			ConnectGPRSRequest ind = (ConnectGPRSRequest) event.getEvent();

			assertTrue(ByteBufUtil.equals(ind.getAccessPointName().getValue(),
					Unpooled.wrappedBuffer(new byte[] { 52, 20, 30 })));
			assertEquals(ind.getPDPID().getId(), 2);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.SendChargingInformationGPRSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendChargingInformationGPRSRequest);
			SendChargingInformationGPRSRequest ind = (SendChargingInformationGPRSRequest) event.getEvent();

			assertEquals((int) ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCInitial().getE1(), 1);
			assertNull(ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent().getCAI_GSM0224()
					.getE1());
			assertNull(ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent().getCAI_GSM0224()
					.getE2());
			assertNull(ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent().getCAI_GSM0224()
					.getE3());
			assertNull(ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent().getCAI_GSM0224()
					.getE6());
			assertNull(ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent().getCAI_GSM0224()
					.getE7());
			assertEquals((int) ind.getSCIGPRSBillingChargingCharacteristics().getAOCGPRS().getAOCSubsequent()
					.getTariffSwitchInterval(), 222);
			assertEquals(ind.getSCIGPRSBillingChargingCharacteristics().getPDPID().getId(), 1);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.EventReportGPRSRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.EventReportGPRSResponse);
		clientExpected.addReceived(EventType.ConnectGPRSRequest);
		clientExpected.addReceived(EventType.SendChargingInformationGPRSRequest);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.EventReportGPRSRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.EventReportGPRSResponse);
		serverExpected.addSent(EventType.ConnectGPRSRequest);
		serverExpected.addSent(EventType.SendChargingInformationGPRSRequest);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * GPSR messageflow 4 ACN=cap3-gsmscf-gprsssf
	 *
	 * <pre>
	 * TC-BEGIN + releaseGPRSRequest + destinationReference=1001 + originationReference=2001 
	 * TC-END + destinationReference=2001 + originationReference=1001
	 * </pre>
	 */
	@Test
	public void testGPRS4() throws Exception {
		// 1. TC-BEGIN + releaseGPRSRequest + destinationReference=1001 + ...
		client.suppressInvokeTimeout();
		client.sendReleaseGPRSRequest(CAPApplicationContext.CapV3_gsmSCF_gprsSSF);
		client.awaitSent(EventType.ReleaseGPRSRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ReleaseGPRSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ReleaseGPRSRequest);
			ReleaseGPRSRequest ind = (ReleaseGPRSRequest) event.getEvent();

			assertEquals(ind.getGPRSCause().getData(), 5);
			assertEquals(ind.getPDPID().getId(), 2);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogGprs dlg = (CAPDialogGprs) capDialog;
			dlg.close(false, dummyCallback);
		}

		// 2. TC-END + destinationReference=2001 + originationReference=1001
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.ReleaseGPRSRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.ReleaseGPRSRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * SMS test messageflow 1 ACN=CapV3_cap3_sms
	 * 
	 * <pre>
	 * -> initialDPSMSRequest
	 * <- resetTimerSMS + requestReportSMSEventRequest 
	 * -> eventReportSMSRequest 
	 * <- furnishChargingInformationSMS 
	 * <- connectSMS (TC-END)
	 * </pre>
	 */
	@Test
	public void testSMS1() throws Exception {
		final byte[] freeFD = new byte[] { 1, 2, 3, 4, 5 };

		// 1. -> initialDPSMSRequest
		client.suppressInvokeTimeout();
		client.sendInitialDpSmsRequest(CAPApplicationContext.CapV3_cap3_sms);
		client.awaitSent(EventType.InitialDPSMSRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDPSMSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDPSMSRequest);
			InitialDPSMSRequest ind = (InitialDPSMSRequest) event.getEvent();

			assertEquals(ind.getServiceKey(), 15);
			assertEquals(ind.getDestinationSubscriberNumber().getAddress(), "123678");
			assertEquals(ind.getDestinationSubscriberNumber().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(ind.getDestinationSubscriberNumber().getAddressNature(), AddressNature.international_number);
			assertEquals(ind.getCallingPartyNumber().getAddress(), "123999");
			assertEquals(ind.getImsi().getData(), "12345678901234");
			assertEquals(ind.getEventTypeSMS(), EventTypeSMS.smsDeliveryRequested);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogSms dlg = (CAPDialogSms) capDialog;
			dlg.addResetTimerSMSRequest(TimerID.tssf, 3000, null);
			server.handleSent(EventType.ResetTimerSMSRequest, null);

			List<SMSEvent> smsEvents = new ArrayList<SMSEvent>();
			SMSEvent smsEvent = server.capParameterFactory.createSMSEvent(EventTypeSMS.tSmsDelivery,
					MonitorMode.transparent);
			smsEvents.add(smsEvent);
			smsEvent = server.capParameterFactory.createSMSEvent(EventTypeSMS.oSmsFailure,
					MonitorMode.notifyAndContinue);
			smsEvents.add(smsEvent);
			dlg.addRequestReportSMSEventRequest(smsEvents, null);
			server.handleSent(EventType.RequestReportSMSEventRequest, null);

			dlg.send(dummyCallback);
		}

		// 2. <- resetTimerSMS + requestReportSMSEventRequest
		server.awaitSent(EventType.ResetTimerSMSRequest);
		server.awaitSent(EventType.RequestReportSMSEventRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ResetTimerSMSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ResetTimerSMSRequest);
			ResetTimerSMSRequest ind = (ResetTimerSMSRequest) event.getEvent();

			assertEquals(ind.getTimerID(), TimerID.tssf);
			assertEquals(ind.getTimerValue(), 3000);
			assertNull(ind.getExtensions());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.RequestReportSMSEventRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.RequestReportSMSEventRequest);
			RequestReportSMSEventRequest ind = (RequestReportSMSEventRequest) event.getEvent();

			assertEquals(ind.getSMSEvents().size(), 2);
			SMSEvent smsEvent = ind.getSMSEvents().get(0);
			assertEquals(smsEvent.getEventTypeSMS(), EventTypeSMS.tSmsDelivery);
			assertEquals(smsEvent.getMonitorMode(), MonitorMode.transparent);
			assertNull(ind.getExtensions());
			smsEvent = ind.getSMSEvents().get(1);
			assertEquals(smsEvent.getEventTypeSMS(), EventTypeSMS.oSmsFailure);
			assertEquals(smsEvent.getMonitorMode(), MonitorMode.notifyAndContinue);
			assertNull(ind.getExtensions());

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogSms dlg = (CAPDialogSms) capDialog;
			OSmsFailureSpecificInfo oSmsFailureSpecificInfo = client.capParameterFactory
					.createOSmsFailureSpecificInfo(MOSMSCause.releaseFromRadioInterface);
			EventSpecificInformationSMS eventSpecificInformationSMS = client.capParameterFactory
					.createEventSpecificInformationSMS(oSmsFailureSpecificInfo);
			dlg.addEventReportSMSRequest(EventTypeSMS.oSmsFailure, eventSpecificInformationSMS, null, null);

			client.handleSent(EventType.EventReportSMSRequest, null);
			dlg.send(dummyCallback);
		}

		// 3. -> eventReportSMSRequest
		client.awaitSent(EventType.EventReportSMSRequest);

		server.awaitReceived(EventType.EventReportSMSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.EventReportSMSRequest);
			EventReportSMSRequest ind = (EventReportSMSRequest) event.getEvent();

			assertEquals(ind.getEventTypeSMS(), EventTypeSMS.oSmsFailure);
			assertEquals(ind.getEventSpecificInformationSMS().getOSmsFailureSpecificInfo().getFailureCause(),
					MOSMSCause.releaseFromRadioInterface);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		CAPDialogSms serverDlg;
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			serverDlg = (CAPDialogSms) capDialog;
		}

		// 4. <- furnishChargingInformationSMS
		{
			FreeFormatDataSMS freeFormatData = server.capParameterFactory
					.createFreeFormatDataSMS(Unpooled.wrappedBuffer(freeFD));
			FCIBCCCAMELSequence1SMS fciBCCCAMELsequence1 = server.capParameterFactory
					.createFCIBCCCAMELsequence1(freeFormatData, null);
			serverDlg.addFurnishChargingInformationSMSRequest(fciBCCCAMELsequence1);
			server.handleSent(EventType.FurnishChargingInformationSMSRequest, null);

			serverDlg.send(dummyCallback);
		}
		server.awaitSent(EventType.FurnishChargingInformationSMSRequest);

		client.awaitReceived(EventType.FurnishChargingInformationSMSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.FurnishChargingInformationSMSRequest);
			FurnishChargingInformationSMSRequest ind = (FurnishChargingInformationSMSRequest) event.getEvent();

			assertTrue(ByteBufUtil.equals(ind.getFCIBCCCAMELsequence1().getFreeFormatData().getValue(),
					Unpooled.wrappedBuffer(freeFD)));
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 5. <- connectSMS (TC-END)
		{
			SMSAddressString callingPartysNumber = server.capParameterFactory
					.createSMSAddressString(AddressNature.reserved, NumberingPlan.ISDN, "Drosd");
			CalledPartyBCDNumber destinationSubscriberNumber = server.capParameterFactory
					.createCalledPartyBCDNumber(AddressNature.international_number, NumberingPlan.ISDN, "1111144444");
			ISDNAddressString smscAddress = server.capParameterFactory
					.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "1111155555");
			serverDlg.addConnectSMSRequest(callingPartysNumber, destinationSubscriberNumber, smscAddress, null);
			server.handleSent(EventType.ConnectSMSRequest, null);

			serverDlg.close(false, dummyCallback);
		}
		server.awaitSent(EventType.ConnectSMSRequest);

		client.awaitReceived(EventType.ConnectSMSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ConnectSMSRequest);
			ConnectSMSRequest ind = (ConnectSMSRequest) event.getEvent();

			assertEquals(ind.getCallingPartysNumber().getAddressNature(), AddressNature.reserved);
			assertEquals(ind.getCallingPartysNumber().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(ind.getCallingPartysNumber().getAddress(), "Drosd");
			assertEquals(ind.getDestinationSubscriberNumber().getAddress(), "1111144444");
			assertEquals(ind.getSMSCAddress().getAddress(), "1111155555");

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDPSMSRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ResetTimerSMSRequest);
		clientExpected.addReceived(EventType.RequestReportSMSEventRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.EventReportSMSRequest);
		clientExpected.addReceived(EventType.FurnishChargingInformationSMSRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.ConnectSMSRequest);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDPSMSRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ResetTimerSMSRequest);
		serverExpected.addSent(EventType.RequestReportSMSEventRequest);
		serverExpected.addReceived(EventType.EventReportSMSRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.FurnishChargingInformationSMSRequest);
		serverExpected.addSent(EventType.ConnectSMSRequest);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * SMS test messageflow 2 ACN=CapV4_cap4_sms
	 * 
	 * <pre>
	 * -> initialDPSMSRequest
	 * <- continueSMS
	 * </pre>
	 */
	@Test
	public void testSMS2() throws Exception {
		// 1. -> initialDPSMSRequest
		client.suppressInvokeTimeout();
		client.sendInitialDpSmsRequest(CAPApplicationContext.CapV4_cap4_sms);
		client.awaitSent(EventType.InitialDPSMSRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDPSMSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDPSMSRequest);
			InitialDPSMSRequest ind = (InitialDPSMSRequest) event.getEvent();

			assertEquals(ind.getServiceKey(), 15);
			assertEquals(ind.getDestinationSubscriberNumber().getAddress(), "123678");
			assertEquals(ind.getDestinationSubscriberNumber().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(ind.getDestinationSubscriberNumber().getAddressNature(), AddressNature.international_number);
			assertEquals(ind.getCallingPartyNumber().getAddress(), "123999");
			assertEquals(ind.getImsi().getData(), "12345678901234");
			assertEquals(ind.getEventTypeSMS(), EventTypeSMS.smsDeliveryRequested);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogSms dlg = (CAPDialogSms) capDialog;
			dlg.addContinueSMSRequest();
			server.handleSent(EventType.ContinueSMSRequest, null);

			dlg.close(false, dummyCallback);
		}

		// 2. <- continueSMS
		server.awaitSent(EventType.ContinueSMSRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ContinueSMSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ContinueSMSRequest);
			ContinueSMSRequest ind = (ContinueSMSRequest) event.getEvent();

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDPSMSRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ContinueSMSRequest);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDPSMSRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ContinueSMSRequest);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * SMS test messageflow 3 ACN=CapV4_cap4_sms
	 * 
	 * <pre>
	 * -> initialDPSMSRequest
	 * <- releaseSMS
	 * </pre>
	 */
	@Test
	public void testSMS3() throws Exception {
		// 1. -> initialDPSMSRequest
		client.suppressInvokeTimeout();
		client.sendInitialDpSmsRequest(CAPApplicationContext.CapV4_cap4_sms);
		client.awaitSent(EventType.InitialDPSMSRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDPSMSRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDPSMSRequest);
			InitialDPSMSRequest ind = (InitialDPSMSRequest) event.getEvent();

			assertEquals(ind.getServiceKey(), 15);
			assertEquals(ind.getDestinationSubscriberNumber().getAddress(), "123678");
			assertEquals(ind.getDestinationSubscriberNumber().getNumberingPlan(), NumberingPlan.ISDN);
			assertEquals(ind.getDestinationSubscriberNumber().getAddressNature(), AddressNature.international_number);
			assertEquals(ind.getCallingPartyNumber().getAddress(), "123999");
			assertEquals(ind.getImsi().getData(), "12345678901234");
			assertEquals(ind.getEventTypeSMS(), EventTypeSMS.smsDeliveryRequested);

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogSms dlg = (CAPDialogSms) capDialog;

			RPCause rpCause = server.capParameterFactory.createRPCause(8);
			dlg.addReleaseSMSRequest(rpCause);

			server.handleSent(EventType.ReleaseSMSRequest, null);
			dlg.close(false, dummyCallback);
		}

		// 2. <- releaseSMS
		server.awaitSent(EventType.ReleaseSMSRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ReleaseSMSRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ReleaseSMSRequest);
			ReleaseSMSRequest ind = (ReleaseSMSRequest) event.getEvent();

			assertEquals(ind.getRPCause().getData(), 8);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDPSMSRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ReleaseSMSRequest);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDPSMSRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ReleaseSMSRequest);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * ACN = capscf-ssfGenericAC V4
	 *
	 * <pre>
	 * TC-BEGIN + InitiateCallAttemptRequest
	 * TC-CONTINUE + InitiateCallAttemptResponse
	 * TC-CONTINUE + SplitLegRequest 
	 * TC-CONTINUE + SplitLegResponse 
	 * TC-CONTINUE + MoveLegRequest 
	 * TC-CONTINUE + MoveLegResponse
	 * TC-CONTINUE + DisconnectLegRequest
	 * TC-CONTINUE + DisconnectLegResponse
	 * TC-END + DisconnectForwardConnectionWithArgumentRequest
	 * </pre>
	 */
	@Test
	public void testInitiateCallAttempt() throws Exception {
		int invokeIdInitiateCallAttempt;
		int invokeIdSplitLeg;
		int invokeIdMoveLeg;
		int invokeIdDisconnectLeg;

		// 1. TC-BEGIN + InitiateCallAttemptRequest
		client.sendInitiateCallAttemptRequest();
		client.awaitSent(EventType.InitiateCallAttemptRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitiateCallAttemptRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitiateCallAttemptRequest);
			InitiateCallAttemptRequest ind = (InitiateCallAttemptRequest) event.getEvent();

			invokeIdInitiateCallAttempt = ind.getInvokeId();

			assertEquals(ind.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
			assertEquals(ind.getDestinationRoutingAddress().getCalledPartyNumber().get(0).getCalledPartyNumber()
					.getAddress(), "1113330");
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			SupportedCamelPhases supportedCamelPhases = server.capParameterFactory.createSupportedCamelPhases(true,
					true, true, false);
			dlg.addInitiateCallAttemptResponse(invokeIdInitiateCallAttempt, supportedCamelPhases, null, null, false);
			server.handleSent(EventType.InitiateCallAttemptResponse, null);
			dlg.send(dummyCallback);
		}

		// 2. TC-CONTINUE + InitiateCallAttemptResponse
		server.awaitSent(EventType.InitiateCallAttemptResponse);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.InitiateCallAttemptResponse);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.InitiateCallAttemptResponse);
			InitiateCallAttemptResponse ind = (InitiateCallAttemptResponse) event.getEvent();

			assertTrue(ind.getSupportedCamelPhases().getPhase1Supported());
			assertTrue(ind.getSupportedCamelPhases().getPhase2Supported());
			assertTrue(ind.getSupportedCamelPhases().getPhase3Supported());
			assertFalse(ind.getSupportedCamelPhases().getPhase4Supported());
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			LegID logIDToSplit = client.capParameterFactory.createLegID(LegType.leg1, null);
			dlg.addSplitLegRequest(logIDToSplit, 1, null);

			client.handleSent(EventType.SplitLegRequest, null);
			dlg.send(dummyCallback);
		}

		// 3. TC-CONTINUE + SplitLegRequest
		client.awaitSent(EventType.SplitLegRequest);

		server.awaitReceived(EventType.SplitLegRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SplitLegRequest);
			SplitLegRequest ind = (SplitLegRequest) event.getEvent();

			invokeIdSplitLeg = ind.getInvokeId();
			assertEquals(ind.getLegToBeSplit().getReceivingSideID(), LegType.leg1);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.addSplitLegResponse(invokeIdSplitLeg);
			server.handleSent(EventType.SplitLegResponse, null);
			dlg.send(dummyCallback);
		}

		// 4. TC-CONTINUE + SplitLegResponse
		server.awaitSent(EventType.SplitLegResponse);

		client.awaitReceived(EventType.SplitLegResponse);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			LegID logIDToMove = client.capParameterFactory.createLegID(LegType.leg1, null);
			dlg.addMoveLegRequest(logIDToMove, null);

			client.handleSent(EventType.MoveLegRequest, null);
			dlg.send(dummyCallback);
		}

		// 5. TC-CONTINUE + MoveLegRequest
		client.awaitSent(EventType.MoveLegRequest);

		server.awaitReceived(EventType.MoveLegRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.MoveLegRequest);
			MoveLegRequest ind = (MoveLegRequest) event.getEvent();

			invokeIdMoveLeg = ind.getInvokeId();
			assertEquals(ind.getLegIDToMove().getReceivingSideID(), LegType.leg1);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.addMoveLegResponse(invokeIdMoveLeg);
			server.handleSent(EventType.MoveLegResponse, null);
			dlg.send(dummyCallback);
		}

		// 6. TC-CONTINUE + MoveLegResponse
		server.awaitSent(EventType.MoveLegResponse);

		client.awaitReceived(EventType.MoveLegResponse);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			LegID logToBeReleased = client.capParameterFactory.createLegID(LegType.leg2, null);
			CauseIndicators causeIndicators = client.isupParameterFactory.createCauseIndicators();
			causeIndicators.setCauseValue(3);
			CauseIsup causeCap = client.capParameterFactory.createCause(causeIndicators);
			dlg.addDisconnectLegRequest(logToBeReleased, causeCap, null);

			client.handleSent(EventType.DisconnectLegRequest, null);
			dlg.send(dummyCallback);
		}

		// 7. TC-CONTINUE + DisconnectLegRequest
		client.awaitSent(EventType.DisconnectLegRequest);

		server.awaitReceived(EventType.DisconnectLegRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DisconnectLegRequest);
			DisconnectLegRequest ind = (DisconnectLegRequest) event.getEvent();

			invokeIdDisconnectLeg = ind.getInvokeId();
			assertEquals(ind.getLegToBeReleased().getReceivingSideID(), LegType.leg2);
			assertEquals(ind.getReleaseCause().getCauseIndicators().getCauseValue(), 3);
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.addDisconnectLegResponse(invokeIdDisconnectLeg);
			server.handleSent(EventType.DisconnectLegResponse, null);
			dlg.send(dummyCallback);
		}

		// 8. TC-CONTINUE + DisconnectLegResponse
		server.awaitSent(EventType.DisconnectLegResponse);

		client.awaitReceived(EventType.DisconnectLegResponse);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.addDisconnectForwardConnectionWithArgumentRequest(15, null);
			client.handleSent(EventType.DisconnectForwardConnectionWithArgumentRequest, null);
			dlg.close(false, dummyCallback);
		}

		// 9. TC-END + DisconnectForwardConnectionWithArgumentRequest
		client.awaitSent(EventType.DisconnectForwardConnectionWithArgumentRequest);

		server.awaitReceived(EventType.DisconnectForwardConnectionWithArgumentRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DisconnectForwardConnectionWithArgumentRequest);
			DisconnectForwardConnectionWithArgumentRequest ind = (DisconnectForwardConnectionWithArgumentRequest) event
					.getEvent();

			assertEquals((int) ind.getCallSegmentID(), 15);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitiateCallAttemptRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.InitiateCallAttemptResponse);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.SplitLegRequest);
		clientExpected.addReceived(EventType.SplitLegResponse);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.MoveLegRequest);
		clientExpected.addReceived(EventType.MoveLegResponse);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.DisconnectLegRequest);
		clientExpected.addReceived(EventType.DisconnectLegResponse);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addSent(EventType.DisconnectForwardConnectionWithArgumentRequest);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitiateCallAttemptRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.InitiateCallAttemptResponse);
		serverExpected.addReceived(EventType.SplitLegRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.SplitLegResponse);
		serverExpected.addReceived(EventType.MoveLegRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.MoveLegResponse);
		serverExpected.addReceived(EventType.DisconnectLegRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.DisconnectLegResponse);
		serverExpected.addReceived(EventType.DisconnectForwardConnectionWithArgumentRequest);
		serverExpected.addReceived(EventType.DialogClose);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * ACN = capscf-ssfGenericAC V4
	 *
	 * <pre>
	 * TC-BEGIN + InitiateDPRequest
	 * TC-CONTINUE + ContinueWithArgumentRequest
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testContinueWithArgument() throws Exception {
		// 1. TC-BEGIN + InitiateDPRequest
		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			assertTrue(Client.checkTestInitialDp(ind));
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;

			AlertingPatternImpl ap = new AlertingPatternImpl(AlertingLevel.Level1);
			dlg.addContinueWithArgumentRequest(ap, null, null, null, null, null, false, null, null, false, null, false,
					false, null);
			server.handleSent(EventType.ContinueWithArgumentRequest, null);
			dlg.send(dummyCallback);
		}

		// 2. TC-CONTINUE + ContinueWithArgumentRequest
		server.awaitSent(EventType.ContinueWithArgumentRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ContinueWithArgumentRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ContinueWithArgumentRequest);
			ContinueWithArgumentRequest ind = (ContinueWithArgumentRequest) event.getEvent();

			assertEquals(ind.getAlertingPattern().getAlertingLevel(), AlertingLevel.Level1);
			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.close(false, dummyCallback);
		}

		// 3. TC-END
		server.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.ContinueWithArgumentRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.ContinueWithArgumentRequest);
		serverExpected.addReceived(EventType.DialogClose);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * ACN = capscf-ssfGenericAC V4
	 *
	 * <pre>
	 * TC-BEGIN + InitiateDPRequest
	 * TC-CONTINUE + callGap
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testCallGap() throws Exception {
		// 1. TC-BEGIN + InitiateDPRequest
		client.sendInitialDp(CAPApplicationContext.CapV3_gsmSSF_scfGeneric);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			ind.getCAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			CAPProvider capProvider = server.capProvider;

			GenericNumber genericNumber = capProvider.getISUPParameterFactory().createGenericNumber();
			genericNumber.setAddress("501090500");
			DigitsIsup digits = capProvider.getCAPParameterFactory().createDigits_GenericNumber(genericNumber);

			CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits, 100);
			BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
			GapCriteriaImpl gapCriteria = new GapCriteriaImpl(basicGapCriteria);
			GapIndicatorsImpl gapIndicators = new GapIndicatorsImpl(60, -1);

			dlg.addCallGapRequest(gapCriteria, gapIndicators, null, null, null);
			server.handleSent(EventType.CallGapRequest, null);
			dlg.send(dummyCallback);

			// GenericNumber genericNumber =
			// capProvider.getISUPParameterFactory().createGenericNumber();
			// genericNumber.setAddress("501090500");
			// Digits digits =
			// capProvider.getCAPParameterFactory().createDigits_GenericNumber(genericNumber);
			//
			// CalledAddressAndService calledAddressAndService = new
			// CalledAddressAndServiceImpl(digits, 100);
			// GapOnService gapOnService = new GapOnServiceImpl(888);
			// BasicGapCriteria basicGapCriteria = new
			// BasicGapCriteriaImpl(calledAddressAndService);
			// BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(digits);
			// BasicGapCriteria basicGapCriteria = new BasicGapCriteriaImpl(gapOnService);
			// ScfID scfId = new ScfIDImpl(new byte[] { 12, 32, 23, 56 });
			// CompoundCriteria compoundCriteria = new
			// CompoundCriteriaImpl(basicGapCriteria, scfId);
			// GapCriteria gapCriteria = new GapCriteriaImpl(compoundCriteria);
			// GapIndicators gapIndicators = new GapIndicatorsImpl(60, -1);
			//
			// MessageID messageID = new MessageIDImpl(11);
			// InbandInfo inbandInfo = new InbandInfoImpl(messageID, 1, 2, 3);
			// InformationToSend informationToSend = new InformationToSendImpl(inbandInfo);
			// GapTreatment gapTreatment = new GapTreatmentImpl(informationToSend);
			//
			// dlg.addCallGapRequest(gapCriteria, gapIndicators, ControlType.sCPOverloaded,
			// gapTreatment, null);
			// super.handleSent(EventType.CallGapRequest, null);
			// dlg.send(dummyCallback);
		}

		// 2. TC-CONTINUE + callGap
		server.awaitSent(EventType.CallGapRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.CallGapRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CallGapRequest);
			CallGapRequest ind = (CallGapRequest) event.getEvent();

			assertEquals(ind.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService()
					.getCalledAddressNumber().getGenericNumber().getAddress(), "501090500");
			assertEquals(ind.getGapCriteria().getBasicGapCriteria().getCalledAddressAndService().getServiceKey(), 100);
			assertEquals(ind.getGapIndicators().getDuration(), 60);
			assertEquals(ind.getGapIndicators().getGapInterval(), -1);
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			CAPDialog capDialog = (CAPDialog) event.getEvent();

			CAPDialogCircuitSwitchedCall dlg = (CAPDialogCircuitSwitchedCall) capDialog;
			dlg.close(false, dummyCallback);
		}

		// 3. TC-END
		server.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.CallGapRequest);
		clientExpected.addReceived(EventType.DialogDelimiter);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.InitialDpRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addSent(EventType.CallGapRequest);
		serverExpected.addReceived(EventType.DialogClose);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}
}
