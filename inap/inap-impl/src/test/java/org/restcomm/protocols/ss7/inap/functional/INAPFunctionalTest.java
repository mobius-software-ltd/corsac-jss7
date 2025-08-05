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

package org.restcomm.protocols.ss7.inap.functional;

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
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedDigits;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Tone;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.gap.BasicGapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.CalledAddressAndServiceImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.gap.GapIndicatorsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.INAPStackImpl;
import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.INAPParameterFactory;
import org.restcomm.protocols.ss7.inap.api.INAPProvider;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPGeneralAbortReason;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPUserAbortReason;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessage;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.inap.api.errors.UnavailableNetworkResource;
import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallGapRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CancelRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CollectInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ConnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ContinueRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.AchBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.inap.functional.listeners.Client;
import org.restcomm.protocols.ss7.inap.functional.listeners.EventType;
import org.restcomm.protocols.ss7.inap.functional.listeners.Server;
import org.restcomm.protocols.ss7.inap.functional.wrappers.INAPStackImplWrapper;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.INAPDialogCircuitSwitchedCallImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ChargingInformationImpl;
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

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author yulianoifa
 *
 */
public class INAPFunctionalTest extends SccpHarness {
	private INAPStackImpl stack1;
	private INAPStackImpl stack2;
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
		super.sccpStack1Name = "INAPFunctionalTestSccpStack1";
		super.sccpStack2Name = "INAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 146);
		peer2Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 146);

		stack1 = new INAPStackImplWrapper(super.sccpProvider1, 146, workerPool);
		stack2 = new INAPStackImplWrapper(super.sccpProvider2, 146, workerPool);

		stack1.start();
		stack2.start();

		client = new Client(stack1, peer1Address, peer2Address);
		server = new Server(this.stack2, peer2Address, peer1Address);
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
		stack2.getProvider().removeINAPDialogListener(server.listenerID);

		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				server.awaitSent(EventType.ErrorComponent);
			}
		};

		// 1. TC-BEGIN + InitialDPRequest
		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);

		client.awaitSent(EventType.InitialDpRequest);
		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			assertTrue(Client.checkTestInitialDp(ind));

			INAPErrorMessage capErrorMessage = server.inapErrorMessageFactory
					.createINAPErrorMessageSystemFailure(UnavailableNetworkResource.endUserFailure);

			ind.getINAPDialog().sendErrorComponent(ind.getInvokeId(), capErrorMessage);
			server.handleSent(EventType.ErrorComponent, null);
		}

		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			inapDialog.close(false, dummyCallback);
		}

		// 2. TC-END + Error message SystemFailure
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ErrorComponent);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ErrorComponent);
			@SuppressWarnings("unchecked")
			List<Object> args = (List<Object>) event.getEvent();

			INAPErrorMessage capErrorMessage = (INAPErrorMessage) args.get(1);
			assertTrue(capErrorMessage.isEmSystemFailure());

			INAPErrorMessageSystemFailure em = capErrorMessage.getEmSystemFailure();
			assertEquals(UnavailableNetworkResource.endUserFailure, em.getUnavailableNetworkResource());
		}

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
		serverExpected.addSent(EventType.ErrorComponent);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Circuit switch call simple messageflow 1
	 * ACN=Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
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
	 * TC-CONTINUE + ActivityTestRequest
	 * TC-CONTINUE + EventReportBCSMRequest (ODisconnect)
	 * TC-END (empty)
	 * </pre>
	 */
	@Test
	public void testCircuitCall1() throws Exception {
		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogTimeout(INAPDialog inapDialog) {
				super.onDialogTimeout(inapDialog);
				server.awaitSent(EventType.ActivityTestRequest);
			}
		};

		final long invokeTimeout = 1500;
		final long clientDialogTimeout = 60000;
		final long serverDialogTimeout = 1500;

		// setting timeouts
		client.inapStack.getTCAPStack().setDialogIdleTimeout(clientDialogTimeout);
		client.suppressInvokeTimeout();

		server.inapStack.getTCAPStack().setInvokeTimeout(invokeTimeout);
		server.inapStack.getTCAPStack().setDialogIdleTimeout(serverDialogTimeout);

		// 1. TC-BEGIN + InitialDPRequest
		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);
		client.awaitSent(EventType.InitialDpRequest);

		INAPDialog inapDialog = null;

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
			inapDialog = (INAPDialog) event.getEvent();
		}

		// 2. TC-CONTINUE + RequestReportBCSMEventRequest
		INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
		{
			RequestReportBCSMEventRequest rrc = server.getRequestReportBCSMEventRequest();
			dlg.addRequestReportBCSMEventRequest(rrc.getBCSMEventList(), null, rrc.getExtensions());
			server.handleSent(EventType.RequestReportBCSMEventRequest, null);
			dlg.send(dummyCallback);
		}

		server.awaitSent(EventType.RequestReportBCSMEventRequest);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RequestReportBCSMEventRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.RequestReportBCSMEventRequest);
			RequestReportBCSMEventRequest ind = (RequestReportBCSMEventRequest) event.getEvent();

			client.checkRequestReportBCSMEventRequest(ind);
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 3. TC-CONTINUE + FurnishChargingInformationRequest
		{
			byte[] freeFormatData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
			dlg.addFurnishChargingInformationRequest(Unpooled.wrappedBuffer(freeFormatData));
			dlg.send(dummyCallback);
			server.handleSent(EventType.FurnishChargingInformationRequest, null);
		}

		server.awaitSent(EventType.FurnishChargingInformationRequest);

		client.awaitReceived(EventType.FurnishChargingInformationRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.FurnishChargingInformationRequest);
			FurnishChargingInformationRequest ind = (FurnishChargingInformationRequest) event.getEvent();

			byte[] freeFormatData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
			assertNotNull(ind.getFCIBillingChargingCharacteristics());
			assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(freeFormatData),
					ind.getFCIBillingChargingCharacteristics()));
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 4. TC-CONTINUE + ApplyChargingRequest + ConnectRequest
		// Boolean tone, CAPExtensionsImpl extensions, Long tariffSwitchInterval
		{
			AchBillingChargingCharacteristicsCS1 aChBillingChargingCharacteristics = server.inapParameterFactory
					.getAchBillingChargingCharacteristicsCS1(null, null);
			dlg.addApplyChargingRequest(aChBillingChargingCharacteristics, false, new LegIDImpl(LegType.leg1, null),
					null);
			server.handleSent(EventType.ApplyChargingRequest, null);

			List<CalledPartyNumberIsup> calledPartyNumber = new ArrayList<CalledPartyNumberIsup>();
			CalledPartyNumber cpn = server.isupParameterFactory.createCalledPartyNumber();
			cpn.setAddress("5599999988");
			cpn.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
			cpn.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
			cpn.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_ALLOWED);
			CalledPartyNumberIsup cpnc = server.inapParameterFactory.createCalledPartyNumber(cpn);
			calledPartyNumber.add(cpnc);
			DestinationRoutingAddress destinationRoutingAddress = server.inapParameterFactory
					.createDestinationRoutingAddress(calledPartyNumber);
			dlg.addConnectRequest(destinationRoutingAddress, null, null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null);
			server.handleSent(EventType.ConnectRequest, null);
			dlg.send(dummyCallback);
		}

		server.awaitSent(EventType.ApplyChargingRequest);
		server.awaitSent(EventType.ConnectRequest);

		client.awaitReceived(EventType.ApplyChargingRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ApplyChargingRequest);
			ApplyChargingRequest ind = (ApplyChargingRequest) event.getEvent();

			assertNotNull(ind.getPartyToCharge());
			assertNotNull(ind.getPartyToCharge().getReceivingSideID());
			assertEquals(ind.getPartyToCharge().getReceivingSideID(), LegType.leg1);
			assertNull(ind.getExtensions());
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.ConnectRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ConnectRequest);
			ConnectRequest ind = (ConnectRequest) event.getEvent();

			assertEquals(ind.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
			CalledPartyNumber cpn = ind.getDestinationRoutingAddress().getCalledPartyNumber().get(0)
					.getCalledPartyNumber();
			assertTrue(cpn.getAddress().equals("5599999988"));
			assertEquals(cpn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
			assertEquals(cpn.getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);
			assertEquals(cpn.getInternalNetworkNumberIndicator(), CalledPartyNumber._INN_ROUTING_ALLOWED);

			assertNull(ind.getAlertingPattern());
			assertNull(ind.getCallingPartysCategory());
			assertNull(ind.getOriginalCalledPartyID());
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 5. TC-CONTINUE + ContinueRequest
		{
			dlg.addContinueRequest(LegType.leg1);
			server.handleSent(EventType.ContinueRequest, null);
			dlg.send(dummyCallback);
		}

		server.awaitSent(EventType.ContinueRequest);

		client.awaitReceived(EventType.ContinueRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ContinueRequest);
			ContinueRequest ind = (ContinueRequest) event.getEvent();

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 6. TC-CONTINUE + SendChargingInformationRequest
		{
			SCIBillingChargingCharacteristicsCS1 sciBillingChargingCharacteristics = server.inapParameterFactory
					.getSCIBillingChargingCharacteristicsCS1(new ChargingInformationImpl(true, null, 1, false));
			dlg.addSendChargingInformationRequest(sciBillingChargingCharacteristics, LegType.leg2, null);
			server.handleSent(EventType.SendChargingInformationRequest, null);
			dlg.send(dummyCallback);
		}

		server.awaitSent(EventType.SendChargingInformationRequest);

		client.awaitReceived(EventType.SendChargingInformationRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.SendChargingInformationRequest);
			SendChargingInformationRequest ind = (SendChargingInformationRequest) event.getEvent();

			SCIBillingChargingCharacteristicsCS1 characteristics = (SCIBillingChargingCharacteristicsCS1) ind
					.getSCIBillingChargingCharacteristics();

			assertNotNull(characteristics);
			assertNotNull(characteristics.getChargingInformation());
			assertTrue(characteristics.getChargingInformation().getOrderStartOfCharging());
			assertFalse(characteristics.getChargingInformation().getCreateDefaultBillingRecord());
			assertNull(characteristics.getChargingInformation().getChargeMessage());
			assertEquals(characteristics.getChargingInformation().getPulseBurst(), new Integer(1));
			assertEquals(ind.getPartyToCharge(), LegType.leg2);
			assertNull(ind.getExtensions());

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}

		// 7. TC-CONTINUE + EventReportBCSMRequest (OAnswer)
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEventWithSkip(EventType.DialogDelimiter, 4);
			inapDialog = (INAPDialog) event.getEvent();

			dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
			INAPParameterFactory paramFactory = client.inapParameterFactory;

			AnswerSpecificInfo oAnswerSpecificInfo = paramFactory.createAnswerSpecificInfo(null, null, null);
			MiscCallInfo miscCallInfo = paramFactory.createMiscCallInfo(MiscCallInfoMessageType.notification, null);
			EventSpecificInformationBCSM eventSpecificInformationBCSM = paramFactory
					.createEventSpecificInformationBCSM(oAnswerSpecificInfo, false);
			dlg.addEventReportBCSMRequest(EventTypeBCSM.oAnswer, null, eventSpecificInformationBCSM,
					new LegIDImpl(LegType.leg2, null), miscCallInfo, null);
			client.handleSent(EventType.EventReportBCSMRequest, null);
			dlg.send(dummyCallback);
		}

		client.awaitSent(EventType.EventReportBCSMRequest);

		server.awaitReceived(EventType.EventReportBCSMRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.EventReportBCSMRequest);
			EventReportBCSMRequest ind = (EventReportBCSMRequest) event.getEvent();

			assertEquals(ind.getEventTypeBCSM(), EventTypeBCSM.oAnswer);
			assertNotNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo());
			assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getBackwardCallIndicators());
			assertNull(ind.getEventSpecificInformationBCSM().getOAnswerSpecificInfo().getBackwardGVNSIndicator());
			assertEquals(ind.getLegID().getReceivingSideID(), LegType.leg2);
			assertNull(ind.getExtensions());

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);

		// 8. TC-CONTINUE + ApplyChargingReportRequest <waiting till DialogTimeout>
		{
			dlg.addApplyChargingRequest(null, null, new LegIDImpl(LegType.leg1, null), null);
			client.handleSent(EventType.ApplyChargingReportRequest, null);
			dlg.send(dummyCallback);
		}
		client.awaitSent(EventType.ApplyChargingReportRequest);

		server.awaitReceived(EventType.ApplyChargingRequest);
		server.awaitReceived(EventType.DialogDelimiter);

		TestEventUtils.updateStamp();

		server.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(serverDialogTimeout);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogTimeout);
			inapDialog = (INAPDialog) event.getEvent();

			inapDialog.keepAlive();

			dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
			dlg.addActivityTestRequest(500);
			dlg.send(dummyCallback);

			server.handleSent(EventType.ActivityTestRequest, null);
		}

		// 9. TC-CONTINUE + ActivityTestRequest
		// server awaiting for sending of ActivityTestRequest is in listener above
		client.awaitReceived(EventType.ActivityTestRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ActivityTestRequest);
			ActivityTestRequest ind = (ActivityTestRequest) event.getEvent();

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			inapDialog = (INAPDialog) event.getEvent();

			dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

			dlg.addActivityTestRequest();
			client.handleSent(EventType.ActivityTestRequest, null);
			dlg.send(dummyCallback);
		}

		// 10. TC-CONTINUE + ActivityTestRequest
		client.awaitSent(EventType.ActivityTestRequest);
		server.awaitReceived(EventType.ActivityTestRequest);
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
			assertNotNull(ind.getLegID());
			assertNotNull(ind.getLegID().getReceivingSideID());
			assertEquals(ind.getLegID().getReceivingSideID(), LegType.leg1);
			assertEquals(ind.getMiscCallInfo().getMessageType(), MiscCallInfoMessageType.notification);
			assertNull(ind.getMiscCallInfo().getDpAssignment());
			assertNull(ind.getExtensions());

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			inapDialog = (INAPDialog) event.getEvent();

			dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
			dlg.close(false, dummyCallback);
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
		clientExpected.addSent(EventType.ActivityTestRequest);
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
		serverExpected.addReceived(EventType.ApplyChargingRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addSent(EventType.ActivityTestRequest);
		serverExpected.addReceived(EventType.ActivityTestRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.EventReportBCSMRequest);
		serverExpected.addReceived(EventType.DialogDelimiter);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Circuit switch call play announcement and disconnect ACN =
	 * Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
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

		Client client = new Client(stack1, peer1Address, peer2Address) {
			private int dialogStep;

			@Override
			public void onRequestReportBCSMEventRequest(RequestReportBCSMEventRequest ind) {
				super.onRequestReportBCSMEventRequest(ind);

				this.checkRequestReportBCSMEventRequest(ind);
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onConnectToResourceRequest(ConnectToResourceRequest ind) {
				super.onConnectToResourceRequest(ind);

				try {
					CalledPartyNumber cpn = ind.getResourceAddress().getIPRoutingAddress().getCalledPartyNumber();
					assertTrue(cpn.getAddress().equals("111222333"));
					assertEquals(cpn.getInternalNetworkNumberIndicator(), CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
					assertEquals(cpn.getNatureOfAddressIndicator(), NAINumber._NAI_INTERNATIONAL_NUMBER);
					assertEquals(cpn.getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);
				} catch (ASNParsingException e) {
					this.error("Error while checking ConnectToResourceRequest", e);
				}
				assertFalse(ind.getResourceAddressNull());
				assertNull(ind.getExtensions());
				assertNull(ind.getServiceInteractionIndicators());
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			private int playAnnounsmentInvokeId;

			@Override
			public void onPlayAnnouncementRequest(PlayAnnouncementRequest ind) {
				super.onPlayAnnouncementRequest(ind);

				assertEquals(ind.getInformationToSend().getTone().getToneID(), 10);
				assertEquals((int) ind.getInformationToSend().getTone().getDuration(), 100);
				assertTrue(ind.getDisconnectFromIPForbidden());
				assertTrue(ind.getRequestAnnouncementCompleteNotification());
				assertNull(ind.getExtensions());

				playAnnounsmentInvokeId = ind.getInvokeId();

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDisconnectForwardConnectionRequest(DisconnectForwardConnectionRequest ind) {
				super.onDisconnectForwardConnectionRequest(ind);

				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onReleaseCallRequest(ReleaseCallRequest ind) {
				super.onReleaseCallRequest(ind);

				CauseIndicators ci;
				try {
					ci = ind.getCause().getCauseIndicators();
					assertEquals(ci.getCauseValue(), CauseIndicators._CV_SEND_SPECIAL_TONE);
					assertEquals(ci.getCodingStandard(), CauseIndicators._CODING_STANDARD_ITUT);
					assertNull(ci.getDiagnostics());
					assertEquals(ci.getLocation(), CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);
				} catch (ASNParsingException e) {
					this.error("Error while checking ReleaseCallRequest", e);
				}
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after PlayAnnouncementRequest
						dlg.addSpecializedResourceReportRequest(playAnnounsmentInvokeId);
						super.handleSent(EventType.SpecializedResourceReportRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			private int dialogStep = 0;

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				assertTrue(Client.checkTestInitialDp(ind));

				dialogStep = 1;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onSpecializedResourceReportRequest(SpecializedResourceReportRequest ind) {
				super.onSpecializedResourceReportRequest(ind);

				dialogStep = 2;
				ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
			}

			@Override
			public void onDialogDelimiter(INAPDialog inapDialog) {
				super.onDialogDelimiter(inapDialog);

				INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

				try {
					switch (dialogStep) {
					case 1: // after InitialDp
						RequestReportBCSMEventRequest rrc = this.getRequestReportBCSMEventRequest();
						dlg.addRequestReportBCSMEventRequest(rrc.getBCSMEventList(), null, rrc.getExtensions());
						super.handleSent(EventType.RequestReportBCSMEventRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						CalledPartyNumber calledPartyNumber = this.isupParameterFactory.createCalledPartyNumber();
						calledPartyNumber.setAddress("111222333");
						calledPartyNumber.setInternalNetworkNumberIndicator(CalledPartyNumber._INN_ROUTING_NOT_ALLOWED);
						calledPartyNumber.setNatureOfAddresIndicator(NAINumber._NAI_INTERNATIONAL_NUMBER);
						calledPartyNumber.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
						CalledPartyNumberIsup resourceAddress_IPRoutingAddress = this.inapParameterFactory
								.createCalledPartyNumber(calledPartyNumber);
						dlg.addConnectToResourceRequest(resourceAddress_IPRoutingAddress, null, null);
						super.handleSent(EventType.ConnectToResourceRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						Tone tone = this.inapParameterFactory.createTone(10, 100);
						InformationToSend informationToSend = this.inapParameterFactory.createInformationToSend(tone);

						dlg.addPlayAnnouncementRequest(informationToSend, true, true, null);
						super.handleSent(EventType.PlayAnnouncementRequest, null);
						dlg.send(dummyCallback);

						dialogStep = 0;

						break;

					case 2: // after SpecializedResourceReportRequest
						dlg.addDisconnectForwardConnectionRequest(LegType.leg1);
						super.handleSent(EventType.DisconnectForwardConnectionRequest, null);
						dlg.send(dummyCallback);

						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) {

						}

						CauseIndicators causeIndicators = this.isupParameterFactory.createCauseIndicators();
						causeIndicators.setCauseValue(CauseIndicators._CV_SEND_SPECIAL_TONE);
						causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_ITUT);
						causeIndicators.setDiagnostics(null);
						causeIndicators.setLocation(CauseIndicators._LOCATION_INTERNATIONAL_NETWORK);
						CauseIsup cause = this.inapParameterFactory.createCause(causeIndicators);
						dlg.addReleaseCallRequest(cause);
						super.handleSent(EventType.ReleaseCallRequest, null);
						dlg.close(false, dummyCallback);

						dialogStep = 0;

						break;
					}
				} catch (INAPException e) {
					this.error("Error while trying to close() Dialog", e);
				}
			}
		};

		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);

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
	 *
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

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		INAPDialogCircuitSwitchedCall serverDlg = null;
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			serverDlg = (INAPDialogCircuitSwitchedCall) inapDialog;
		}

		// 2. TC-CONTINUE + ResetTimerRequest
		{
			serverDlg.addResetTimerRequest(TimerID.tssf, 1001, null);
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
			assertNull(ind.getExtensions());
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 3. TC-CONTINUE + PromptAndCollectUserInformationRequest
		{
			CollectedDigits collectedDigits = server.inapParameterFactory.createCollectedDigits(1, 11, null, null, null,
					null, null, null, null, null, null);
			CollectedInfo collectedInfo = server.inapParameterFactory.createCollectedInfo(collectedDigits);
			serverDlg.addPromptAndCollectUserInformationRequest(collectedInfo, true, null, null);

			server.handleSent(EventType.PromptAndCollectUserInformationRequest, null);
			serverDlg.send(dummyCallback);
		}

		INAPDialogCircuitSwitchedCall clientDlg = null;
		int promptAndCollectUserInformationInvokeId = Integer.MIN_VALUE;

		server.awaitSent(EventType.PromptAndCollectUserInformationRequest);
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
			assertFalse(ind.getRequestAnnouncementStartedNotification());
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			clientDlg = (INAPDialogCircuitSwitchedCall) inapDialog;
		}

		// 4. TC-CONTINUE + SpecializedResourceReportRequest
		{
			clientDlg.addSpecializedResourceReportRequest(promptAndCollectUserInformationInvokeId, false, true);
			client.handleSent(EventType.SpecializedResourceReportRequest, null);
			clientDlg.send(dummyCallback);
		}
		client.awaitSent(EventType.SpecializedResourceReportRequest);

		server.awaitReceived(EventType.SpecializedResourceReportRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.SpecializedResourceReportRequest);
			SpecializedResourceReportRequest ind = (SpecializedResourceReportRequest) event.getEvent();

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
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

			DigitsIsup digitsResponse = client.inapParameterFactory.createDigits_GenericNumber(genericNumber);
			clientDlg.addPromptAndCollectUserInformationResponse(promptAndCollectUserInformationInvokeId,
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
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			serverDlg = (INAPDialogCircuitSwitchedCall) inapDialog;
		}

		// 6. TC-CONTINUE + CancelRequest
		{
			serverDlg.addCancelRequest();
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
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);

		// 7. TC-END + CancelRequest
		{
			serverDlg.addCancelRequest(new Integer(10));
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
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
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
	 * ScfSsf test ACN = Core_INAP_CS1_SSP_to_SCP_AC
	 * 
	 * <pre>
	 * TC-BEGIN + establishTemporaryConnection + callInformationRequest + collectInformationRequest 
	 * TC-END + callInformationReport
	 * </pre>
	 */
	@Test
	public void testScfSsf() throws Exception {
		// 1. TC-BEGIN + ...
		client.sendEstablishTemporaryConnectionRequest_CallInformationRequest();
		client.awaitSent(EventType.EstablishTemporaryConnectionRequest);
		client.awaitSent(EventType.CallInformationRequest);
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

			assertNull(ind.getCarrier());
			assertNull(ind.getCorrelationID());
			assertNull(ind.getExtensions());
			assertNull(ind.getScfID());

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.CallInformationRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CallInformationRequest);
			CallInformationRequest ind = (CallInformationRequest) event.getEvent();

			List<RequestedInformationType> al = ind.getRequestedInformationTypeList();
			assertEquals(al.size(), 1);
			assertEquals(al.get(0), RequestedInformationType.callStopTime);
			assertNull(ind.getExtensions());
			assertNull(ind.getLegID());

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.CollectInformationRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.CollectInformationRequest);
			CollectInformationRequest ind = (CollectInformationRequest) event.getEvent();

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
			INAPParameterFactory paramFactory = server.inapParameterFactory;

			List<RequestedInformation> requestedInformationList = new ArrayList<RequestedInformation>();
			DateAndTime dt = paramFactory.createDateAndTime(2012, 11, 30, 23, 50, 40);
			RequestedInformation ri = paramFactory.createRequestedInformation_CallStopTime(dt);

			requestedInformationList.add(ri);
			dlg.addCallInformationReportRequest(requestedInformationList, null, null);

			// sending callInformationReport
			server.handleSent(EventType.CallInformationReportRequest, null);
			dlg.close(false, dummyCallback);
		}

		client.awaitReceived(EventType.DialogAccept);

		// 2. TC-END + callInformationReport
		server.awaitSent(EventType.CallInformationReportRequest);

		client.awaitReceived(EventType.CallInformationReportRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.CallInformationReportRequest);
			CallInformationReportRequest ind = (CallInformationReportRequest) event.getEvent();

			List<RequestedInformation> al = ind.getRequestedInformationList();
			assertEquals(al.size(), 1);
			DateAndTime dt = al.get(0).getCallStopTimeValue();
			assertEquals(dt.getYear(), 12);
			assertEquals(dt.getMonth(), 11);
			assertEquals(dt.getDay(), 30);
			assertEquals(dt.getHour(), 23);
			assertEquals(dt.getMinute(), 50);
			assertEquals(dt.getSecond(), 40);
			assertNull(ind.getExtensions());
			assertNull(ind.getLegID());
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogClose);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.EstablishTemporaryConnectionRequest);
		clientExpected.addSent(EventType.CallInformationRequest);
		clientExpected.addSent(EventType.CollectInformationRequest);
		clientExpected.addReceived(EventType.DialogAccept);
		clientExpected.addReceived(EventType.CallInformationReportRequest);
		clientExpected.addReceived(EventType.DialogClose);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.DialogRequest);
		serverExpected.addReceived(EventType.EstablishTemporaryConnectionRequest);
		serverExpected.addReceived(EventType.CallInformationRequest);
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
	 * TC-CONTINUE + ResetTimerRequest
	 * reject ResetTimerRequest 
	 * DialogUserAbort: AbortReason=missing_reference
	 * </pre>
	 */
	@Test
	public void testAbnormal() throws Exception {
		final AtomicInteger clientResetTimerInvokeId = new AtomicInteger();

		client = new Client(stack1, peer1Address, peer2Address) {
			@Override
			public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);

				assertEquals(clientResetTimerInvokeId.get(), (long) invokeId);
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
			public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason,
					INAPUserAbortReason userReason) {
				super.onDialogUserAbort(inapDialog, generalReason, userReason);

				assertNull(generalReason);
				assertNull(userReason);
			}
		};

		final int invokeTimeout = 1000;

		// 1. TC-BEGIN + ActivityTestRequest
		client.sendActivityTestRequest(invokeTimeout);
		client.awaitSent(EventType.ActivityTestRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.ActivityTestRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ActivityTestRequest);
			ActivityTestRequest ind = (ActivityTestRequest) event.getEvent();

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			// sending TC-CONTINUE
			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
			dlg.send(dummyCallback);
		}

		// 2. TC-CONTINUE <no ActivityTestResponse> resetInvokeTimer() before
		// InvokeTimeout
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.DialogDelimiter);

		TestEventUtils.updateStamp();

		// 3. InvokeTimeout
		client.awaitReceived(EventType.InvokeTimeout);
		TestEventUtils.assertPassed(invokeTimeout);

		INAPDialogCircuitSwitchedCall clientDlg = null;
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.InvokeTimeout);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			clientDlg = (INAPDialogCircuitSwitchedCall) inapDialog;
		}

		// 4. TC-CONTINUE + CancelRequest + cancelInvocation()
		{
			int invId = clientDlg.addCancelRequest();
			client.handleSent(EventType.CancelRequest, null);
			clientDlg.cancelInvocation(invId);
			clientDlg.send(dummyCallback);
		}
		client.awaitSent(EventType.CancelRequest);
		server.awaitReceived(EventType.DialogDelimiter);

		// 5. TC-CONTINUE + ResetTimerRequest
		{
			clientResetTimerInvokeId.set(clientDlg.addResetTimerRequest(TimerID.tssf, 2222, null));
			client.handleSent(EventType.ResetTimerRequest, null);
			clientDlg.send(dummyCallback);
		}
		final AtomicInteger serverResetTimerInvokeId = new AtomicInteger();

		client.awaitSent(EventType.ResetTimerRequest);
		server.awaitReceived(EventType.ResetTimerRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.ResetTimerRequest);
			ResetTimerRequest ind = (ResetTimerRequest) event.getEvent();

			serverResetTimerInvokeId.set(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

			ProblemImpl problem = new ProblemImpl();
			problem.setInvokeProblemType(InvokeProblemType.MistypedParameter);

			dlg.sendRejectComponent(serverResetTimerInvokeId.get(), problem);
			server.handleSent(EventType.RejectComponent, null);

			dlg.send(dummyCallback);
		}

		// 6. reject ResetTimerRequest
		server.awaitSent(EventType.RejectComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

			// sending abort
			client.handleSent(EventType.DialogUserAbort, null);
			dlg.abort(INAPUserAbortReason.missing_reference, dummyCallback);
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
		final long invokeTimeout = 2000;

		final long clientDialogTimeout = 2000; // client timeout first
		final long serverDialogTimeout = 3000;

		// setting timeouts
		client.inapStack.getTCAPStack().setInvokeTimeout(invokeTimeout);
		client.inapStack.getTCAPStack().setDialogIdleTimeout(clientDialogTimeout);
		client.suppressInvokeTimeout();

		server.inapStack.getTCAPStack().setInvokeTimeout(invokeTimeout);
		server.inapStack.getTCAPStack().setDialogIdleTimeout(serverDialogTimeout);
		server.suppressInvokeTimeout();

		// 1. TC-BEGIN + InitialDPRequest
		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			assertTrue(Client.checkTestInitialDp(ind));
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
			dlg.send(dummyCallback);
		}

		// 2. TC-CONTINUE empty (no answer - DialogTimeout at both sides)
		client.awaitReceived(EventType.DialogAccept);
		TestEventUtils.updateStamp();

		client.awaitReceived(EventType.DialogTimeout); // waiting for dialog timeout
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
			public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason,
					INAPUserAbortReason userReason) {
				super.onDialogUserAbort(inapDialog, generalReason, userReason);

				assertEquals(generalReason, INAPGeneralAbortReason.ACNNotSupported);
				assertNull(userReason);
				assertEquals(inapDialog.getTCAPMessageType(), MessageType.Abort);
			}
		};

		// deactivating server
		server.inapProvider.getINAPServiceCircuitSwitchedCall().deactivate();

		// 1. TC-BEGIN + InitialDPRequest (Server service is down -> ACN not supported)
		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);
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
			public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason generalReason,
					INAPUserAbortReason userReason) {
				super.onDialogUserAbort(inapDialog, generalReason, userReason);

				assertNull(generalReason);
				assertNull(userReason);
				assertEquals(inapDialog.getTCAPMessageType(), MessageType.Abort);
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
	 * TC-CONTINUE ProviderAbort
	 * </pre>
	 */
	@Test
	public void testProviderAbort() throws Exception {
		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onDialogProviderAbort(INAPDialog inapDialog, PAbortCauseType abortCause) {
				super.onDialogProviderAbort(inapDialog, abortCause);

				assertEquals(abortCause, PAbortCauseType.UnrecognizedTxID);
			}
		};

		// 1. TC-BEGIN + InitialDP
		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		server.awaitReceived(EventType.DialogDelimiter);

		// 2. relaseDialog
		client.releaseDialog();

		// 3. TC-CONTINUE ProviderAbort
		server.sendAccept();
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
	 * <pre>
	 * TC-BEGIN + broken referensedNumber
	 * TC-ABORT
	 * </pre>
	 */
	// @Test
	public void testMessageUserDataLength() throws Exception {
		// Client client = new Client(stack1, peer1Address, peer2Address) {
		// @Override
		// public void onDialogUserAbort(INAPDialog inapDialog, INAPGeneralAbortReason
		// generalReason,
		// INAPUserAbortReason userReason) {
		// super.onDialogUserAbort(inapDialog, generalReason, userReason);
		// assertEquals(inapDialog.getTCAPMessageType(), MessageType.Abort);
		// }
		// };
		//
		// long stamp = System.currentTimeMillis();
		// int count = 0;
		// Client side events
		// List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		// TestEvent te = TestEvent.createReceivedEvent(EventType.DialogUserAbort, null,
		// count++, (stamp));
		// clientExpectedEvents.add(te);
		//
		// te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, count++,
		// (stamp));
		// clientExpectedEvents.add(te);
		//
		// count = 0;
		// Server side events
		//
		// client.testMessageUserDataLength();
		//
		// client.awaitReceived(EventType.DialogRelease);server.awaitReceived(EventType.DialogRelease);
		//
		// client.compareEvents(clientExpectedEvents);
		// server.compareEvents(serverExpectedEvents);
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
			public void onDialogAccept(INAPDialog inapDialog) {
				super.onDialogAccept(inapDialog);
				assertEquals(inapDialog.getTCAPMessageType(), MessageType.Continue);
			}

			@Override
			public void onContinueRequest(ContinueRequest ind) {
				super.onContinueRequest(ind);
				client.awaitSent(EventType.CancelRequest);
			};
		};

		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);
				server.awaitSent(EventType.ContinueRequest);
			}
		};

		// 1. TC-BEGIN + referensedNumber + initialDPRequest + initialDPRequest
		client.sendInitialDp2();
		client.awaitSent(EventType.InitialDpRequest);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			INAPDialogCircuitSwitchedCall d = ind.getINAPDialog();
			assertTrue(Client.checkTestInitialDp(ind));
			assertEquals(d.getTCAPMessageType(), MessageType.Begin);

			d.addContinueRequest(LegType.leg1);
			d.sendDelayed(dummyCallback);
			server.handleSent(EventType.ContinueRequest, null);
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		// waiting for server send Continue is implemented in listener above
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			INAPDialogCircuitSwitchedCall d = ind.getINAPDialog();
			assertTrue(Client.checkTestInitialDp(ind));
			assertEquals(d.getTCAPMessageType(), MessageType.Begin);

			d.addContinueRequest(LegType.leg1);
			d.sendDelayed(dummyCallback);
			server.handleSent(EventType.ContinueRequest, null);
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		// waiting for server send Continue is implemented in listener above
		server.awaitReceived(EventType.DialogDelimiter);

		// 2. TC-CONTINUE + sendDelayed(ContinueRequest) + sendDelayed(ContinueRequest)
		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.ContinueRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ContinueRequest);
			ContinueRequest ind = (ContinueRequest) event.getEvent();

			INAPDialogCircuitSwitchedCall d = ind.getINAPDialog();
			assertEquals(d.getTCAPMessageType(), MessageType.Continue);

			d.addCancelRequest();
			d.closeDelayed(false, dummyCallback);
			client.handleSent(EventType.CancelRequest, null);
		}
		// waiting for server send Cancel is implemented in listener above
		client.awaitReceived(EventType.ContinueRequest);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.ContinueRequest);
			ContinueRequest ind = (ContinueRequest) event.getEvent();

			INAPDialogCircuitSwitchedCall d = ind.getINAPDialog();
			assertEquals(d.getTCAPMessageType(), MessageType.Continue);

			d.addCancelRequest();
			d.sendDelayed(dummyCallback);
			client.handleSent(EventType.CancelRequest, null);
		}
		// waiting for server send Cancel is implemented in listener above
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
			public void onDialogAccept(INAPDialog inapDialog) {
				super.onDialogAccept(inapDialog);
				assertEquals(inapDialog.getTCAPMessageType(), MessageType.End);
			}
		};

		server = new Server(stack2, peer2Address, peer1Address) {
			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);
				server.awaitSent(EventType.ContinueRequest);
			}

			@Override
			public void onDialogRequest(INAPDialog inapDialog) {
				super.onDialogRequest(inapDialog);
				assertEquals(inapDialog.getTCAPMessageType(), MessageType.Begin);
			}
		};

		// 1. TC-BEGIN + initialDPRequest + initialDPRequest
		client.sendInitialDp3();
		client.awaitSent(EventType.InitialDpRequest);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			INAPDialogCircuitSwitchedCall d = ind.getINAPDialog();

			d.addContinueRequest(LegType.leg1);
			d.sendDelayed(dummyCallback);

			server.handleSent(EventType.ContinueRequest, null);
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		// waiting for server send Continue is implemented in listener above
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			INAPDialogCircuitSwitchedCall d = ind.getINAPDialog();

			d.addContinueRequest(LegType.leg1);
			d.closeDelayed(true, dummyCallback);

			server.handleSent(EventType.ContinueRequest, null);
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		// waiting for server send Continue is implemented in listener above
		server.awaitReceived(EventType.DialogDelimiter);

		// 2. TC-END + Prearranged + [ContinueRequest + ContinueRequest]
		client.clientCscDialog.close(true, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.InitialDpRequest);
		clientExpected.addSent(EventType.InitialDpRequest);
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
			private AtomicInteger dialogStep = new AtomicInteger(0);

			@Override
			public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);

				dialogStep.incrementAndGet();

				try {
					switch (dialogStep.get()) {
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

		final List<Integer> invokeIds = new ArrayList<Integer>();
		final List<Integer> outInvokeIds = new ArrayList<Integer>();

		server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);

				try {
					if (invokeId == outInvokeIds.get(0)) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.LinkedResponseUnexpected);
						assertFalse(isLocalOriginated);
					} else if (invokeId == outInvokeIds.get(1)) {
						assertEquals(problem.getType(), ProblemType.Invoke);
						assertEquals(problem.getInvokeProblemType(), InvokeProblemType.UnrechognizedLinkedID);
						assertFalse(isLocalOriginated);
					} else if (invokeId == outInvokeIds.get(2)) {
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

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			invokeIds.add(ind.getInvokeId());
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.PlayAnnouncementRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.PlayAnnouncementRequest);
			PlayAnnouncementRequest ind = (PlayAnnouncementRequest) event.getEvent();

			invokeIds.add(ind.getInvokeId());
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			INAPDialogCircuitSwitchedCallImpl dlg = (INAPDialogCircuitSwitchedCallImpl) inapDialog;

			int invokeId1 = invokeIds.get(0);
			int invokeId2 = invokeIds.get(1);

			outInvokeIds.add(dlg.addSpecializedResourceReportRequest(invokeId1));
			outInvokeIds.add(dlg.addSpecializedResourceReportRequest(50));
			outInvokeIds.add(dlg.sendDataComponent(null, invokeId2, null, 2000L, INAPOperationCode.continueCode, null,
					true, false));

			dlg.addSpecializedResourceReportRequest(invokeId2);
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
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
			dlg.close(false, dummyCallback);
		}

		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.RejectComponent);
		server.awaitReceived(EventType.RejectComponent);

		// 3. TC-END

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
	 * TC-END + Reject (ReturnResultUnexpected) + Reject (ReturnErrorUnexpected) + Reject
	 * (ReturnResultUnexpected) + Reject (ReturnErrorUnexpected)
	 * </pre>
	 */
	@Test
	public void testUnexpectedResultError() throws Exception {
		client = new Client(stack1, peer1Address, peer2Address) {
			private AtomicInteger rejectStep = new AtomicInteger(0);

			@Override
			public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);

				rejectStep.incrementAndGet();

				try {
					switch (rejectStep.get()) {
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

		server = new Server(this.stack2, peer2Address, peer1Address) {
			private AtomicInteger dialogStep = new AtomicInteger(0);
			private AtomicInteger rejectStep = new AtomicInteger(0);

			@Override
			public void onInitialDPRequest(InitialDPRequest ind) {
				super.onInitialDPRequest(ind);

				dialogStep.incrementAndGet();

				switch (dialogStep.get()) {
				case 1:
					invokeIds.add(ind.getInvokeId());
					break;
				case 2:
					invokeIds.add(ind.getInvokeId());
					break;
				}
			}

			@Override
			public void onPromptAndCollectUserInformationRequest(PromptAndCollectUserInformationRequest ind) {
				super.onPromptAndCollectUserInformationRequest(ind);

				dialogStep.incrementAndGet();

				switch (dialogStep.get()) {
				case 3:
					invokeIds.add(ind.getInvokeId());
					break;
				case 4:
					invokeIds.add(ind.getInvokeId());
					break;
				}
			}

			@Override
			public void onActivityTestRequest(ActivityTestRequest ind) {
				super.onActivityTestRequest(ind);

				dialogStep.incrementAndGet();

				switch (dialogStep.get()) {
				case 5:
					break;
				case 6:
					invokeIds.add(ind.getInvokeId());
					break;
				}
			}

			@Override
			public void onReleaseCallRequest(ReleaseCallRequest ind) {
				super.onReleaseCallRequest(ind);

				dialogStep.incrementAndGet();

				switch (dialogStep.get()) {
				case 7:
					invokeIds.add(ind.getInvokeId());
					break;
				case 8:
					invokeIds.add(ind.getInvokeId());
					break;
				}
			}

			@Override
			public void onRejectComponent(INAPDialog inapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(inapDialog, invokeId, problem, isLocalOriginated);

				rejectStep.incrementAndGet();

				try {
					switch (rejectStep.get()) {
					case 1:
						assertEquals(invokeId, invokeIds.get(0));
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 2:
						assertEquals(invokeId, invokeIds.get(4));
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 3:
						assertEquals(invokeId, invokeIds.get(5));
						assertEquals(problem.getReturnResultProblemType(),
								ReturnResultProblemType.ReturnResultUnexpected);
						assertFalse(isLocalOriginated);
						break;
					case 4:
						assertEquals(invokeId, invokeIds.get(6));
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.ReturnErrorUnexpected);
						assertFalse(isLocalOriginated);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}
		};

		// 1. TC-BEGIN + ...
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
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			INAPDialogCircuitSwitchedCallImpl dlg = (INAPDialogCircuitSwitchedCallImpl) inapDialog;

			dlg.sendDataComponent(invokeIds.get(0), null, null, null, INAPOperationCode.initialDP, null, false, true);

			INAPErrorMessage mem = server.inapErrorMessageFactory
					.createINAPErrorMessageSystemFailure(UnavailableNetworkResource.endUserFailure);
			dlg.sendErrorComponent(invokeIds.get(1), mem);
			server.handleSent(EventType.ErrorComponent, null);

			GenericNumber genericNumber = server.isupParameterFactory.createGenericNumber();
			genericNumber.setAddress("444422220000");
			genericNumber.setAddressRepresentationRestrictedIndicator(GenericNumber._APRI_ALLOWED);
			genericNumber.setNatureOfAddresIndicator(NAINumber._NAI_SUBSCRIBER_NUMBER);
			genericNumber.setNumberingPlanIndicator(GenericNumber._NPI_DATA);
			genericNumber.setNumberQualifierIndicator(GenericNumber._NQIA_CALLING_PARTY_NUMBER);
			genericNumber.setScreeningIndicator(GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
			DigitsIsup digitsResponse = server.inapParameterFactory.createDigits_GenericNumber(genericNumber);
			dlg.addPromptAndCollectUserInformationResponse(invokeIds.get(2), digitsResponse);

			server.handleSent(EventType.PromptAndCollectUserInformationResponse, null);
			mem = server.inapErrorMessageFactory
					.createINAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
			dlg.sendErrorComponent(invokeIds.get(3), mem);
			server.handleSent(EventType.ErrorComponent, null);

			mem = server.inapErrorMessageFactory
					.createINAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
			dlg.sendErrorComponent(invokeIds.get(4), mem);
			server.handleSent(EventType.ErrorComponent, null);

			dlg.sendDataComponent(invokeIds.get(5), null, null, null, INAPOperationCode.releaseCall, null, false, true);

			mem = server.inapErrorMessageFactory
					.createINAPErrorMessageSystemFailure(UnavailableNetworkResource.resourceStatusFailure);
			dlg.sendErrorComponent(invokeIds.get(6), mem);
			server.handleSent(EventType.ErrorComponent, null);

			dlg.send(dummyCallback);
		}

		// 2. TC-CONTINUE + ...
		server.awaitSent(EventType.ErrorComponent);
		server.awaitSent(EventType.PromptAndCollectUserInformationResponse);
		server.awaitSent(EventType.ErrorComponent);
		server.awaitSent(EventType.ErrorComponent);
		server.awaitSent(EventType.ErrorComponent);

		client.awaitReceived(EventType.DialogAccept);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.ErrorComponent);
		client.awaitReceived(EventType.PromptAndCollectUserInformationResponse);
		client.awaitReceived(EventType.ErrorComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.RejectComponent);
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			// closing dialog
			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
			dlg.close(false, dummyCallback);
		}

		// 3. TC-END + ...
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
			public void onDialogProviderAbort(INAPDialog capDialog, PAbortCauseType abortCause) {
				super.onDialogProviderAbort(capDialog, abortCause);

				assertEquals(abortCause, PAbortCauseType.UnrecognizedMessageType);
			}
		};

		// 1. TC-Message + bad UnrecognizedMessageType
		// sending a dummy message to a bad address for a dialog starting
		client.sendDummyMessage();

		// sending a badly formatted message
		SccpDataMessage message = sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address, peer1Address,
				Unpooled.wrappedBuffer(getMessageBadTag()), 0, 0, false, null, null);

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
			public void onDialogNotice(INAPDialog inapDialog, INAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
				super.onDialogNotice(inapDialog, noticeProblemDiagnostic);

				assertEquals(noticeProblemDiagnostic, INAPNoticeProblemDiagnostic.MessageCannotBeDeliveredToThePeer);
			}
		};

		// 1. TC-BEGIN + (bad sccp address + setReturnMessageOnError)
		client.actionB();
		client.awaitReceived(EventType.DialogNotice);

		// 2. TC-NOTICE
		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addReceived(EventType.DialogNotice);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	public static byte[] getMessageBadTag() {
		return new byte[] { 106, 6, 72, 1, 1, 73, 1, 1 };
	}

	/**
	 * ACN = Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
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
		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			assertTrue(Client.checkTestInitialDp(ind));
			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;

			// sending continue witj argument
			dlg.addContinueWithArgumentRequest(null, null);
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

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		client.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			// closing dialog
			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
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
	 * ACN = Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
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
		client.sendInitialDp(INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B);
		client.awaitSent(EventType.InitialDpRequest);

		server.awaitReceived(EventType.DialogRequest);
		server.awaitReceived(EventType.InitialDpRequest);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.InitialDpRequest);
			InitialDPRequest ind = (InitialDPRequest) event.getEvent();

			ind.getINAPDialog().processInvokeWithoutAnswer(ind.getInvokeId());
		}
		server.awaitReceived(EventType.DialogDelimiter);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.DialogDelimiter);
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
			INAPProvider inapProvider = server.inapProvider;

			GenericNumber genericNumber = inapProvider.getISUPParameterFactory().createGenericNumber();
			genericNumber.setAddress("501090500");
			DigitsIsup digits = inapProvider.getINAPParameterFactory().createDigits_GenericNumber(genericNumber);

			CalledAddressAndServiceImpl calledAddressAndService = new CalledAddressAndServiceImpl(digits, 100);
			BasicGapCriteriaImpl basicGapCriteria = new BasicGapCriteriaImpl(calledAddressAndService);
			GapCriteriaImpl gapCriteria = new GapCriteriaImpl(basicGapCriteria);
			GapIndicatorsImpl gapIndicators = new GapIndicatorsImpl(60, -1);

			dlg.addCallGapRequest(gapCriteria, gapIndicators, null, null, null);

			// sending callGap
			server.handleSent(EventType.CallGapRequest, null);
			dlg.send(dummyCallback);
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
			INAPDialog inapDialog = (INAPDialog) event.getEvent();

			// closing dialog -> step 3
			INAPDialogCircuitSwitchedCall dlg = (INAPDialogCircuitSwitchedCall) inapDialog;
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
