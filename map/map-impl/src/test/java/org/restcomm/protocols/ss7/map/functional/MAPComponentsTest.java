package org.restcomm.protocols.ss7.map.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSMDeliveryFailure;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.map.api.errors.SMEnumeratedDeliveryFailureCause;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.functional.listeners.Client;
import org.restcomm.protocols.ss7.map.functional.listeners.Server;
import org.restcomm.protocols.ss7.map.functional.listeners.events.EventType;
import org.restcomm.protocols.ss7.map.functional.wrappers.MAPStackImplWrapper;
import org.restcomm.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEvent;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventFactory;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventUtils;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.Unpooled;

/**
 * Test for MAP Component processing
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public class MAPComponentsTest extends SccpHarness {
	private static final int _LITTLE_DELAY = 100;

	private MAPStackImpl stack1;
	private MAPStackImpl stack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;

	@Before
	public void beforeEach() throws Exception {
		this.sccpStack1Name = "MAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "MAPFunctionalTestSccpStack2";

		super.setUp();

		int ssn = getSSN();
		peer1Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), ssn);
		peer2Address = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), ssn);

		stack1 = new MAPStackImplWrapper(this.sccpProvider1, getSSN(), workerPool);
		stack2 = new MAPStackImplWrapper(this.sccpProvider2, getSSN(), workerPool);

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
		Client client = new Client(stack1, peer1Address, peer2Address) {
			int responseReceived = 0;

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					mapDialog.send(dummyCallback);
				} catch (MAPException e) {
					this.error("Error while sending response", e);
					fail("Error while trying to send empty response");
				}
			}

			@Override
			public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResponse) {
				super.onProcessUnstructuredSSResponse(procUnstrResponse);
				String ussdString;
				try {
					ussdString = procUnstrResponse.getUSSDString().getString(null);
					this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
					responseReceived++;
					assertEquals(ussdString, "Your balance is 500");
				} catch (MAPException e) {
					this.error("Error while trying to parse ussdString", e);
					fail("Error while trying to parse ussdString");
				}

			}

			@Override
			public void onDialogRelease(MAPDialog mapDialog) {
				super.onDialogRelease(mapDialog);
				assertEquals(this.responseReceived, 2);
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			private int processUnstructuredSSRequestInvokeId = 0;
			private int dialogStep;

			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				String ussdString;
				try {
					ussdString = procUnstrReqInd.getUSSDString().getString(null);
					// AddressStringImpl msisdn = procUnstrReqInd.getMSISDNAddressString();
					this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
					assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
				} catch (MAPException e1) {
					this.error("Error while trying to parse ussdString", e1);
					fail("Error while trying to parse ussdString");
				}

				MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
				processUnstructuredSSRequestInvokeId = procUnstrReqInd.getInvokeId();
				try {
					CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
					USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory()
							.createUSSDString("Your balance is 500", ussdDataCodingScheme, null);

					ProcessUnstructuredSSResponseImpl req = new ProcessUnstructuredSSResponseImpl(ussdDataCodingScheme,
							ussdStrObj);
					mapDialog.sendDataComponent(processUnstructuredSSRequestInvokeId, null, null, null,
							MAPOperationCode.processUnstructuredSS_Request, req, false, false);
				} catch (MAPException e) {
					this.error("Error while trying to send ProcessUnstructuredSSResponse", e);
					fail("Error while trying to send ProcessUnstructuredSSResponse");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				this.dialogStep++;
				try {
					if (this.dialogStep == 1) {
						super.handleSent(EventType.ProcessUnstructuredSSResponseIndication, null);
						mapDialog.send(dummyCallback);
					} else {
						super.handleSent(EventType.ProcessUnstructuredSSResponseIndication, null);
						USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory()
								.createUSSDString("Your balance is 500");
						CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(0x0f);
						((MAPDialogSupplementary) mapDialog).addProcessUnstructuredSSResponse(
								this.processUnstructuredSSRequestInvokeId, ussdDataCodingScheme, ussdStrObj);

						mapDialog.close(false, dummyCallback);
					}
				} catch (MAPException e) {
					this.error("Error while trying to send Response", e);
					fail("Error while trying to send ProcessUnstructuredSSResponse");
				}
			}
		};

		client.actionA();
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
		// Action_Component_E

		Client client = new Client(stack1, peer1Address, peer2Address) {

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

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				try {
					String ussdString = procUnstrReqInd.getUSSDString().getString(null);
					// AddressStringImpl msisdn = procUnstrReqInd.getMSISDNAddressString();
					this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
					assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
					MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();

					Problem problem = this.mapProvider.getMAPParameterFactory()
							.createProblemInvoke(InvokeProblemType.ResourceLimitation);

					mapDialog.sendRejectComponent(procUnstrReqInd.getInvokeId(), problem);
				} catch (MAPException e) {
					this.error("Error while trying to send Duplicate InvokeId Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					super.handleSent(EventType.ErrorComponent, null);
					mapDialog.close(false, dummyCallback);
				} catch (MAPException e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to send Duplicate InvokeId Component");
				}
			}

		};

		client.actionA();
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

		Client client = new Client(stack1, peer1Address, peer2Address) {

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

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				try {
					String ussdString = procUnstrReqInd.getUSSDString().getString(null);
					// AddressStringImpl msisdn = procUnstrReqInd.getMSISDNAddressString();
					this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
					assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
					MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
					MAPErrorMessage msg = this.mapErrorMessageFactory.createMAPErrorMessageSMDeliveryFailure(
							SMEnumeratedDeliveryFailureCause.scCongestion, null, null);

					mapDialog.sendErrorComponent(procUnstrReqInd.getInvokeId(), msg);
				} catch (MAPException e) {
					this.error("Error while trying to add Error Component", e);
					fail("Error while trying to add Error Component");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					super.handleSent(EventType.ErrorComponent, null);
					mapDialog.close(true, dummyCallback);
				} catch (MAPException e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to send Error Component");
				}
			}

		};

		client.actionA();
		client.clientDialog.close(true, dummyCallback);
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
	 * TC-END + Reject (invokeProblem-ResourceLimitation) without invokeId! 
	 * - this Reject is Invoked by MAP-user
	 * </pre>
	 */
	@Test
	public void testComponentGeneralProblemTypeComponent() throws Exception {
		// Action_Component_G

		Client client = new Client(stack1, peer1Address, peer2Address) {

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

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				try {
					String ussdString = procUnstrReqInd.getUSSDString().getString(null);
					// AddressStringImpl msisdn = procUnstrReqInd.getMSISDNAddressString();
					this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
					assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
					MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();

					Problem problem = this.mapProvider.getMAPParameterFactory()
							.createProblemInvoke(InvokeProblemType.ResourceLimitation);

					mapDialog.sendRejectComponent(null, problem);
				} catch (MAPException e) {
					this.error("Error while trying to add Duplicate InvokeId Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					super.handleSent(EventType.RejectComponent, null);
					mapDialog.close(false, dummyCallback);
				} catch (MAPException e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

		};

		client.actionA();
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

		Client client = new Client(stack1, peer1Address, peer2Address) {

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

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				try {
					String ussdString = procUnstrReqInd.getUSSDString().getString(null);
					// AddressStringImpl msisdn = procUnstrReqInd.getMSISDNAddressString();
					this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
					assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
					// MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
				} catch (MAPException e) {
					this.error("Error while trying to add Duplicate InvokeId Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

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

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					mapDialog.close(false, dummyCallback);
				} catch (MAPException e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

		};

		client.sendUnrecognizedOperation();
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

		Client client = new Client(stack1, peer1Address, peer2Address) {

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

		Server server = new Server(this.stack2, peer2Address, peer1Address) {
			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);
				try {
					String ussdString = procUnstrReqInd.getUSSDString().getString(null);
					// AddressStringImpl msisdn = procUnstrReqInd.getMSISDNAddressString();
					this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
					assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
					// MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
				} catch (MAPException e) {
					this.error("Error while trying to add Duplicate InvokeId Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

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

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					mapDialog.close(false, dummyCallback);
				} catch (MAPException e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

		};

		client.sendMystypedParameter();
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

		Client client = new Client(stack1, peer1Address, peer2Address) {

			private int stepRej = 0;

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

				stepRej++;

				try {
					if (stepRej == 1) {
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

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					mapDialog.close(false, dummyCallback);
				} catch (MAPException e) {
					this.error("Error while sending the TC-CONTINUE", e);
					fail("Error while sending the TC-CONTINUE");
				}
			}
		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			private int step = 0;
			private int stepRej = 0;
			private int invokeId1;

			@Override
			public void onSendRoutingInfoForSMRequest(SendRoutingInfoForSMRequest ind) {
				super.onSendRoutingInfoForSMRequest(ind);
				invokeId1 = ind.getInvokeId();
			}

			@Override
			public void onReportSMDeliveryStatusRequest(ReportSMDeliveryStatusRequest ind) {
				super.onReportSMDeliveryStatusRequest(ind);
			}

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

				stepRej++;

				assertEquals((long) invokeId, invokeId1);
				try {
					if (stepRej == 1) {
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

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					step++;
					MAPDialogSms clientDialogSms = (MAPDialogSms) mapDialog;

					switch (step) {
					case 1:

						ISDNAddressString msisdn = this.mapParameterFactory.createISDNAddressString(
								AddressNature.international_number, NumberingPlan.ISDN, "11223344");
						// AddressString serviceCentreAddress =
						// this.mapParameterFactory.createAddressString(AddressNature.international_number,
						// NumberingPlan.ISDN, "1122334455");
						IMSI imsi = this.mapParameterFactory.createIMSI("777222");
						LocationInfoWithLMSI locationInfoWithLMSI = this.mapParameterFactory
								.createLocationInfoWithLMSI(msisdn, null, null, false, null);
						clientDialogSms.addSendRoutingInfoForSMResponse(invokeId1, imsi, locationInfoWithLMSI, null,
								null, null);

						super.handleSent(EventType.SendRoutingInfoForSMRespIndication, null);

						imsi = this.mapParameterFactory.createIMSI("777222222");
						clientDialogSms.addSendRoutingInfoForSMResponse(invokeId1, imsi, locationInfoWithLMSI, null,
								null, null);

						super.handleSent(EventType.SendRoutingInfoForSMRespIndication, null);

						MAPErrorMessage mapErrorMessage = this.mapErrorMessageFactory
								.createMAPErrorMessageSystemFailure(NetworkResource.hlr, null, null);
						clientDialogSms.sendErrorComponent(invokeId1, mapErrorMessage);

						super.handleSent(EventType.ErrorComponent, null);

						mapDialog.send(dummyCallback);
						break;
					}

				} catch (MAPException e) {
					this.error("Error while sending TC-CONTINUE or TC-END", e);
					fail("Error while sending TC-CONTINUE or TC-END");
				}
			}
		};

		client.send_sendRoutingInfoForSMRequest_reportSMDeliveryStatusRequest();

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

		Client client = new Client(stack1, peer1Address, peer2Address) {

			private int rejectStep;

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

				rejectStep++;

				try {
					switch (rejectStep) {
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

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);

				try {
					mapDialog.close(false, dummyCallback);
				} catch (Exception e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

		};

		Server server = new Server(this.stack2, peer2Address, peer1Address) {

			private int step;
			private int invokeId1;
			private int invokeId2;
			private int invokeId3;
			private int rejectStep;

			@Override
			public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
				super.onProcessUnstructuredSSRequest(procUnstrReqInd);

				try {
					String ussdString = procUnstrReqInd.getUSSDString().getString(null);
					// AddressString msisdn = procUnstrReqInd.getMSISDNAddressString();
					this.debug("Received ProcessUnstructuredSSRequest " + ussdString);
					assertEquals(MAPFunctionalTest.USSD_STRING, ussdString);
					// MAPDialogSupplementary mapDialog = procUnstrReqInd.getMAPDialog();
				} catch (MAPException e) {
					this.error("Error while trying to add Duplicate InvokeId Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}

				step++;
				switch (step) {
				case 1:
					invokeId1 = procUnstrReqInd.getInvokeId();
					break;
				case 2:
					invokeId2 = procUnstrReqInd.getInvokeId();
					break;
				case 3:
					invokeId3 = procUnstrReqInd.getInvokeId();
					break;
				}
			}

			@Override
			public void onRejectComponent(MAPDialog mapDialog, Integer invokeId, Problem problem,
					boolean isLocalOriginated) {
				super.onRejectComponent(mapDialog, invokeId, problem, isLocalOriginated);

				rejectStep++;

				try {
					switch (rejectStep) {
					case 1:
						assertEquals(problem.getReturnResultProblemType(), ReturnResultProblemType.MistypedParameter);
						assertFalse(isLocalOriginated);
						assertEquals((long) invokeId, invokeId1);
						break;
					case 2:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.MistypedParameter);
						assertFalse(isLocalOriginated);
						assertEquals((long) invokeId, invokeId2);
						break;
					case 3:
						assertEquals(problem.getReturnErrorProblemType(), ReturnErrorProblemType.UnrecognizedError);
						assertFalse(isLocalOriginated);
						assertEquals((long) invokeId, invokeId3);
						break;
					}
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
			}

			@Override
			public void onDialogDelimiter(MAPDialog mapDialog) {
				super.onDialogDelimiter(mapDialog);
				try {
					ASNOctetString octetString = new ASNOctetString(
							Unpooled.wrappedBuffer(new byte[] { 1, 1, 1, 1, 1 }), null, null, null, false);
					((MAPDialogImpl) mapDialog).getTcapDialog().sendData(invokeId1, null, null, null,
							TcapFactory.createLocalOperationCode(MAPOperationCode.processUnstructuredSS_Request),
							octetString, false, true);

					octetString = new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 1, 1, 1, 1, 1 }), null, null,
							null, false);
					((MAPDialogImpl) mapDialog).getTcapDialog().sendError(invokeId2,
							TcapFactory.createLocalErrorCode(MAPErrorCode.systemFailure), octetString);

					((MAPDialogImpl) mapDialog).getTcapDialog().sendError(invokeId3,
							TcapFactory.createLocalErrorCode(1000), null);

					super.handleSent(EventType.ErrorComponent, null);
					super.handleSent(EventType.ErrorComponent, null);

					mapDialog.send(dummyCallback);
				} catch (Exception e) {
					this.error("Error while trying to send Error Component", e);
					fail("Error while trying to add Duplicate InvokeId Component");
				}
			}

		};

		client.actionAAA();
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

}
