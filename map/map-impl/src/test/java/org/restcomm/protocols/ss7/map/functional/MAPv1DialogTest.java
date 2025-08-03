package org.restcomm.protocols.ss7.map.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.map.dialog.MAPUserAbortChoiseImpl;
import org.restcomm.protocols.ss7.map.functional.listeners.Client;
import org.restcomm.protocols.ss7.map.functional.listeners.Server;
import org.restcomm.protocols.ss7.map.functional.listeners.events.EventType;
import org.restcomm.protocols.ss7.map.functional.wrappers.MAPStackImplWrapper;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEvent;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventFactory;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventUtils;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * Test for MAP V1 Dialogs
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MAPv1DialogTest extends SccpHarness {
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

		super.setUp();

		int ssn = getSSN();
		peer1Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), ssn);
		peer2Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), ssn);

		stack1 = new MAPStackImplWrapper(super.sccpProvider1, getSSN(), workerPool);
		stack2 = new MAPStackImplWrapper(super.sccpProvider2, getSSN(), workerPool);

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
}
