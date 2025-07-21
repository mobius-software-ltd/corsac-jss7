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

package org.restcomm.protocols.ss7.tcap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.InvokeTestASN;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.ASNInvokeParameterImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;
import org.restcomm.protocols.ss7.tcap.listeners.EventTestHarness;
import org.restcomm.protocols.ss7.tcap.listeners.EventType;
import org.restcomm.protocols.ss7.tcap.listeners.TestEvent;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * Test for component processing
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TCAPComponentsTest extends SccpHarness {
	private static final long MINI_WAIT_TIME = 1000;
	private static final int DIALOG_TIMEOUT = Integer.MAX_VALUE;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private ClientComponent client;
	private ServerComponent server;

	@Before
	public void beforeEach() throws Exception {
		this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		this.tcapStack1 = new TCAPStackImpl("TCAPComponentsTest", this.sccpProvider1, 8, workerPool);
		this.tcapStack2 = new TCAPStackImpl("TCAPComponentsTest", this.sccpProvider2, 8, workerPool);

		this.tcapStack1.start();
		this.tcapStack2.start();

		// default invoke timeouts
		this.tcapStack1.setInvokeTimeout(MINI_WAIT_TIME);
		this.tcapStack2.setInvokeTimeout(MINI_WAIT_TIME);
		// default dialog timeouts
		this.tcapStack1.setDialogIdleTimeout(DIALOG_TIMEOUT);
		this.tcapStack2.setDialogIdleTimeout(DIALOG_TIMEOUT);
	}

	@After
	public void afterEach() {
		this.tcapStack1.stop();
		this.tcapStack2.stop();

		super.tearDown();
	}

	/**
	 * Sending BadlyStructuredComponent Component
	 *
	 * <pre>
	 * TC-BEGIN + Invoke with BadlyStructuredComponent + Invoke 
	 * TC-END + Reject (mistypedComponent)
	 * </pre>
	 */
	@Test
	public void BadlyStructuredComponentTest() throws Exception {
		this.tcapStack1.getProvider().getParser().registerAlternativeClassMapping(ASNInvokeParameterImpl.class,
				InvokeTestASN.class);
		this.tcapStack2.getProvider().getParser().registerAlternativeClassMapping(ASNInvokeParameterImpl.class,
				InvokeTestASN.class);

		this.client = new ClientComponent(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address) {
			@Override
			public void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback) {
				super.onTCEnd(ind, callback);

				assertEquals(ind.getComponents().size(), 1);
				BaseComponent c = ind.getComponents().get(0);
				assertTrue(c instanceof Reject);
				Reject r = (Reject) c;
				assertEquals(r.getInvokeId(), new Integer(1));
				try {
					assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.MistypedComponent);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(r.isLocalOriginated());
			}
		};

		this.server = new ServerComponent(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address) {
			@Override
			public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
				super.onTCBegin(ind, callback);

				assertEquals(ind.getComponents().size(), 2);
				BaseComponent c = ind.getComponents().get(0);

				assertTrue(c instanceof Reject);
				Reject r = (Reject) c;
				try {
					assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.MistypedComponent);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertTrue(r.isLocalOriginated());

				try {
					this.sendEnd(TerminationType.Basic);
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 1");
					e.printStackTrace();
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int cnt = 0;
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Begin, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
		clientExpectedEvents.add(te);

		cnt = 0;
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
		serverExpectedEvents.add(te);

		client.startClientDialog();

		BaseComponent badComp = new BadInvokeImpl();
		badComp.setInvokeId(1);
		((DialogImpl) client.dialog).sendComponent(badComp);

		client.addNewInvoke(2, 10000L);
		client.sendBegin();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Sending unrecognizedComponent
	 *
	 * <pre>
	 * TC-BEGIN + bad component (with component type != Invoke,ReturnResult,...) + Invoke
	 * TC-END + Reject (unrecognizedComponent)
	 * </pre>
	 */
	@Test
	public void UnrecognizedComponentTest() throws Exception {
		this.client = new ClientComponent(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address) {

			@Override
			public void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback) {
				super.onTCEnd(ind, callback);

				assertEquals(ind.getComponents().size(), 1);
				BaseComponent c = ind.getComponents().get(0);
				assertTrue(c instanceof Reject);
				Reject r = (Reject) c;
				assertNull(r.getInvokeId());
				try {
					assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.UnrecognizedComponent);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
				assertFalse(r.isLocalOriginated());
			}
		};

		this.server = new ServerComponent(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address) {
			@Override
			public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
				super.onTCBegin(ind, callback);

				assertEquals(ind.getComponents().size(), 2);
				BaseComponent c = ind.getComponents().get(0);
				assertTrue(c instanceof Reject);
				Reject r = (Reject) c;
				assertNull(r.getInvokeId());
				try {
					assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.UnrecognizedComponent);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertTrue(r.isLocalOriginated());

				try {
					this.sendEnd(TerminationType.Basic);
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 1");
					e.printStackTrace();
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int cnt = 0;
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Begin, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
		clientExpectedEvents.add(te);

		cnt = 0;
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
		serverExpectedEvents.add(te);

		client.startClientDialog();

		BaseComponent badComp = new BadComponentImpl();
		((DialogImpl) client.dialog).sendComponent(badComp);

		client.addNewInvoke(1, 10000L);
		client.sendBegin();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Sending MistypedComponent Component
	 *
	 * <pre>
	 * TC-BEGIN + Invoke with an extra bad component + Invoke TC-END + Reject
	 * (mistypedComponent)
	 * </pre>
	 */
	@Test
	public void MistypedComponentTest() throws Exception {

		this.client = new ClientComponent(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address) {

			@Override
			public void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback) {
				super.onTCEnd(ind, callback);

				assertEquals(ind.getComponents().size(), 1);
				BaseComponent c = ind.getComponents().get(0);
				assertTrue(c instanceof Reject);
				Reject r = (Reject) c;
				assertEquals((long) r.getInvokeId(), 1);
				try {
					assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.MistypedComponent);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(r.isLocalOriginated());
			}
		};

		this.server = new ServerComponent(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
				super.onTCBegin(ind, callback);

				assertEquals(ind.getComponents().size(), 2);
				BaseComponent c = ind.getComponents().get(0);
				assertTrue(c instanceof Reject);
				Reject r = (Reject) c;
				assertEquals((long) r.getInvokeId(), 1);
				try {
					assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.MistypedComponent);
				} catch (ParseException e) {
					assertEquals(1, 2);
				}

				assertTrue(r.isLocalOriginated());

				try {
					this.sendEnd(TerminationType.Basic);
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 1");
					e.printStackTrace();
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int cnt = 0;
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Begin, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
		clientExpectedEvents.add(te);

		cnt = 0;
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
		serverExpectedEvents.add(te);

		client.startClientDialog();

		BaseComponent badComp = new MistypedInvokeImpl(100);
		badComp.setInvokeId(1);
		((DialogImpl) client.dialog).sendComponent(badComp);

		client.addNewInvoke(2, 10000L);
		client.sendBegin();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Testing diplicateInvokeId case.
	 * <p>
	 * All Invokes are with a little invokeTimeout(removed before an answer from a
	 * Server).
	 * 
	 * <pre>
	 * TC-BEGIN + Invoke (invokeId=1)
	 * TC-CONTINUE + ReturnResult (invokeId=1)
	 * TC-CONTINUE + Reject(unrecognizedInvokeId) + Invoke (invokeId=1)
	 * TC-CONTINUE + Reject (duplicateInvokeId)
	 * TC-CONTINUE + Invoke (invokeId=2)
	 * TC-CONTINUE + ReturnResultLast (invokeId=1) + ReturnError (invokeId=2)
	 * TC-CONTINUE + Invoke (invokeId=1, for this message we will invoke processWithoutAnswer()) + Invoke (invokeId==2)
	 * TC-CONTINUE TC-CONTINUE + Invoke (invokeId=1) + Invoke (invokeId=2) * TC-END + Reject (duplicateInvokeId for invokeId=2)
	 * </pre>
	 */
	@Test
	public void DuplicateInvokeIdTest() throws Exception {
		final long beginInvokeTimeout = MINI_WAIT_TIME / 2;
		final long continueInvokeTimeout = MINI_WAIT_TIME / 2;

		this.client = new ClientComponent(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address) {

			@Override
			public void onTCContinue(TCContinueIndication ind, TaskCallback<Exception> callback) {
				super.onTCContinue(ind, callback);

				step++;

				try {
					switch (step) {
					case 1:
						assertEquals(ind.getComponents().size(), 1);
						BaseComponent c = ind.getComponents().get(0);

						assertTrue(c instanceof Reject);
						Reject r = (Reject) c;
						assertEquals((long) r.getInvokeId(), 1);
						assertEquals(r.getProblem().getReturnResultProblemType(),
								ReturnResultProblemType.UnrecognizedInvokeID);
						assertTrue(r.isLocalOriginated());

						this.addNewInvoke(1, continueInvokeTimeout);
						this.sendContinue();
						break;
					case 2:
						assertEquals(ind.getComponents().size(), 1);
						c = ind.getComponents().get(0);
						assertTrue(c instanceof Reject);
						r = (Reject) c;
						assertEquals((long) r.getInvokeId(), 1);
						assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
						assertFalse(r.isLocalOriginated());

						this.addNewInvoke(2, continueInvokeTimeout);
						this.sendContinue();
						break;

					case 3:
						assertEquals(ind.getComponents().size(), 2);
						c = ind.getComponents().get(0);
						assertTrue(c instanceof Reject);
						r = (Reject) c;
						assertEquals((long) r.getInvokeId(), 1);
						assertEquals(r.getProblem().getReturnResultProblemType(),
								ReturnResultProblemType.UnrecognizedInvokeID);
						assertTrue(r.isLocalOriginated());

						c = ind.getComponents().get(1);
						assertTrue(c instanceof Reject);
						r = (Reject) c;
						assertEquals((long) r.getInvokeId(), 2);
						assertEquals(r.getProblem().getReturnErrorProblemType(),
								ReturnErrorProblemType.UnrecognizedInvokeID);
						assertTrue(r.isLocalOriginated());

						this.addNewInvoke(1, continueInvokeTimeout);
						this.addNewInvoke(2, continueInvokeTimeout);
						this.sendContinue();
						break;

					case 4:
						this.addNewInvoke(1, 10000L);
						this.addNewInvoke(2, 10000L);
						this.sendContinue();
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					fail("Exception when sendComponent / send message 2");
				}
			}

			@Override
			public void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback) {
				super.onTCEnd(ind, callback);

				try {
					assertEquals(ind.getComponents().size(), 1);
					BaseComponent c = ind.getComponents().get(0);
					assertTrue(c instanceof Reject);
					Reject r = (Reject) c;
					assertEquals((long) r.getInvokeId(), 2);
					assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
					assertFalse(r.isLocalOriginated());
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 3");
					e.printStackTrace();
				}
			}
		};

		this.server = new ServerComponent(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address) {
			@Override
			public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
				super.onTCBegin(ind, callback);

				// waiting for Invoke timeout at a client side
				EventTestHarness.waitFor(beginInvokeTimeout * 2);

				try {
					this.addNewReturnResult(1);

					this.sendContinue();
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 1");
					e.printStackTrace();
				}
			}

			@Override
			public void onTCContinue(TCContinueIndication ind, TaskCallback<Exception> callback) {
				super.onTCContinue(ind, callback);

				// waiting for Invoke timeout at a client side
				EventTestHarness.waitFor(continueInvokeTimeout * 2);

				step++;

				try {
					switch (step) {
					case 1:
						assertEquals(ind.getComponents().size(), 2);

						BaseComponent c = ind.getComponents().get(0);
						assertTrue(c instanceof Reject);
						Reject r = (Reject) c;
						assertEquals((long) r.getInvokeId(), 1);
						assertEquals(r.getProblem().getReturnResultProblemType(),
								ReturnResultProblemType.UnrecognizedInvokeID);
						assertFalse(r.isLocalOriginated());

						c = ind.getComponents().get(1);
						assertTrue(c instanceof Reject);
						r = (Reject) c;
						assertEquals((long) r.getInvokeId(), 1);
						assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
						assertTrue(r.isLocalOriginated());

						this.sendContinue();
						break;

					case 2:
						this.addNewReturnResultLast(1);
						this.addNewReturnError(2);
						this.sendContinue();
						break;

					case 3:
						this.dialog.processInvokeWithoutAnswer(1);

						this.sendContinue();
						break;

					case 4:
						assertEquals(ind.getComponents().size(), 2);

						c = ind.getComponents().get(1);
						assertTrue(c instanceof Reject);
						r = (Reject) c;
						assertEquals((long) r.getInvokeId(), 2);
						assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
						assertTrue(r.isLocalOriginated());

						this.sendEnd(TerminationType.Basic);
						break;
					}
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 2");
					e.printStackTrace();
				}
			}

		};

		long stamp = System.currentTimeMillis();
		int cnt = 0;
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Begin, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, cnt++, stamp + MINI_WAIT_TIME * 5);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 5);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp + MINI_WAIT_TIME * 5);
		clientExpectedEvents.add(te);

		cnt = 0;
		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.ReturnResult, null, cnt++, stamp + MINI_WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.ReturnResultLast, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.ReturnError, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp + MINI_WAIT_TIME * 5);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp + MINI_WAIT_TIME * 5);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.addNewInvoke(1, beginInvokeTimeout);
		client.sendBegin();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	public class ClientComponent extends EventTestHarness {

		protected int step = 0;

		public ClientComponent(final TCAPStack stack, final ParameterFactory parameterFactory,
				final SccpAddress thisAddress, final SccpAddress remoteAddress) {
			super(stack, parameterFactory, thisAddress, remoteAddress, LogManager.getLogger(ClientComponent.class));

			super.listenerName = "Client";
		}

		public DialogImpl getCurDialog() {
			return (DialogImpl) this.dialog;
		}

		@Override
		public void onTCContinue(TCContinueIndication ind, TaskCallback<Exception> callback) {
			super.onTCContinue(ind, callback);

			procComponents(ind.getComponents());
		}

		@Override
		public void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback) {
			super.onTCEnd(ind, callback);

			procComponents(ind.getComponents());
		}

		private void procComponents(List<BaseComponent> compp) {
			if (compp != null)
				for (BaseComponent c : compp) {
					EventType et = null;
					if (c instanceof Invoke)
						et = EventType.Invoke;
					if (c instanceof ReturnResult)
						et = EventType.ReturnResult;
					if (c instanceof ReturnResultLast)
						et = EventType.ReturnResultLast;
					if (c instanceof ReturnError)
						et = EventType.ReturnError;
					if (c instanceof Reject)
						et = EventType.Reject;

					super.handleReceived(et, c);
				}
		}

		public void addNewInvoke(Integer invokeId, Long timeout) throws Exception {
			OperationCode oc = TcapFactory.createLocalOperationCode(10);

			// Parameter p1 = TcapFactory.createParameter();
			// p1.setTagClass(Tag.CLASS_UNIVERSAL);
			// p1.setTag(Tag.STRING_OCTET);
			// p1.setData(new byte[]{0x0F});
			//
			// Parameter p2 = TcapFactory.createParameter();
			// p2.setTagClass(Tag.CLASS_UNIVERSAL);
			// p2.setTag(Tag.STRING_OCTET);
			// p2.setData(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6,
			// 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19,
			// 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5,
			// 0x72, (byte) 0xb9, 0x11 });
			//
			// Parameter pm = TcapFactory.createParameter();
			// pm.setTagClass(Tag.CLASS_UNIVERSAL);
			// pm.setTag(Tag.SEQUENCE);
			// pm.setParameters(new Parameter[]{p1, p2});
			// invoke.setParameter(pm);

			this.handleSent(EventType.Invoke, null);
			this.dialog.sendData(invokeId, null, null, timeout, oc, null, true, false);
		}
	}

	public class ServerComponent extends EventTestHarness {
		protected int step = 0;

		/**
		 * @param stack
		 * @param thisAddress
		 * @param remoteAddress
		 */
		public ServerComponent(final TCAPStack stack, final ParameterFactory parameterFactory,
				final SccpAddress thisAddress, final SccpAddress remoteAddress) {
			super(stack, parameterFactory, thisAddress, remoteAddress, LogManager.getLogger(ServerComponent.class));

			super.listenerName = "Server";
		}

		public void addNewReturnResult(Integer invokeId) throws Exception {

			OperationCode oc = TcapFactory.createLocalOperationCode(10);

			this.handleSent(EventType.ReturnResult, null);

			this.dialog.sendData(invokeId, null, null, null, oc, null, false, false);
		}

		public void addNewReturnResultLast(Integer invokeId) throws Exception {

			OperationCode oc = TcapFactory.createLocalOperationCode(10);

			this.handleSent(EventType.ReturnResultLast, null);

			this.dialog.sendData(invokeId, null, null, null, oc, null, false, true);
		}

		public void addNewReturnError(Integer invokeId) throws Exception {

			ErrorCode ec = TcapFactory.createLocalErrorCode(10);

			this.handleSent(EventType.ReturnError, null);

			this.dialog.sendError(invokeId, ec, null);
		}

		@Override
		public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
			super.onTCBegin(ind, callback);

			procComponents(ind.getComponents());
		}

		@Override
		public void onTCContinue(TCContinueIndication ind, TaskCallback<Exception> callback) {
			super.onTCContinue(ind, callback);

			procComponents(ind.getComponents());
		}

		private void procComponents(List<BaseComponent> compp) {
			if (compp != null)
				for (BaseComponent c : compp) {
					EventType et = null;
					if (c instanceof Invoke)
						et = EventType.Invoke;
					if (c instanceof ReturnResult)
						et = EventType.ReturnResult;
					if (c instanceof ReturnResultLast)
						et = EventType.ReturnResultLast;
					if (c instanceof ReturnError)
						et = EventType.ReturnError;
					if (c instanceof Reject)
						et = EventType.Reject;

					super.handleReceived(et, c);
				}
		}
	}
}