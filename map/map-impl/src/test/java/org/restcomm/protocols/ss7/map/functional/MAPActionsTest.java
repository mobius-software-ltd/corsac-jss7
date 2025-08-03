package org.restcomm.protocols.ss7.map.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.restcomm.protocols.ss7.map.api.dialog.Reason;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.dialog.MAPUserAbortChoiseImpl;
import org.restcomm.protocols.ss7.map.functional.listeners.Client;
import org.restcomm.protocols.ss7.map.functional.listeners.Server;
import org.restcomm.protocols.ss7.map.functional.listeners.events.EventType;
import org.restcomm.protocols.ss7.map.functional.wrappers.MAPProviderImplWrapper;
import org.restcomm.protocols.ss7.map.functional.wrappers.MAPServiceSupplementaryImplWrapper;
import org.restcomm.protocols.ss7.map.functional.wrappers.MAPStackImplWrapper;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEvent;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventFactory;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventUtils;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;

/**
 * Test for MAP Dialog normal and abnormal actions
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@SuppressWarnings("unchecked")
public class MAPActionsTest extends SccpHarness {
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

		stack1 = new MAPStackImplWrapper(sccpProvider1, getSSN(), workerPool);
		stack2 = new MAPStackImplWrapper(sccpProvider2, getSSN(), workerPool);

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
}
