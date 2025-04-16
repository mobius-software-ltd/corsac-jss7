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

package org.restcomm.protocols.ss7.tcapAnsi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Return;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.ASNInvokeSetParameterImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.InvokeNotLastImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.WrappedComponentImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

import io.netty.buffer.Unpooled;

/**
 * Test for component processing
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TCAPComponentsTest extends SccpHarness {

	public static final long MINI_WAIT_TIME = 100;
	public static final long WAIT_TIME = 500;
	private static final int _DIALOG_TIMEOUT = 5000000;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private ClientComponent client;
	private ServerComponent server;

	public TCAPComponentsTest() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

		System.out.println("setUp");
		super.setUp();

		peer1Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1,
				8);
		peer2Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2,
				8);

		this.tcapStack1 = new TCAPStackImpl("TCAPComponentsTest_1", this.sccpProvider1, 8,
				workerPool.getPeriodicQueue());
		this.tcapStack2 = new TCAPStackImpl("TCAPComponentsTest_2", this.sccpProvider2, 8,
				workerPool.getPeriodicQueue());

		this.tcapStack1.start();
		this.tcapStack2.start();

		this.tcapStack1.setInvokeTimeout(0);
		this.tcapStack2.setInvokeTimeout(0);
		this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT);
		this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT);
		// create test classes

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	@After
	public void tearDown() {
		this.tcapStack1.stop();
		this.tcapStack2.stop();
		super.tearDown();

	}

	/**
	 * Testing diplicateInvokeId case All Invokes are with a little
	 * invokeTimeout(removed before an answer from a Server) !!!
	 *
	 * TC-BEGIN + InvokeNotLast (invokeId==1, little invokeTimeout) TC-CONTINUE +
	 * ReturnResult (correlationId==1 -> Reject because of invokeTimeout)
	 * TC-CONTINUE + Reject(unrecognizedInvokeId) + InvokeNotLast (invokeId==1)
	 * TC-CONTINUE + Reject (duplicateInvokeId) TC-CONTINUE + InvokeNotLast
	 * (invokeId==2) TC-CONTINUE + ReturnResultLast (correlationId==1) + ReturnError
	 * (correlationId==2) TC-CONTINUE + InvokeNotLast (invokeId==1, for this message
	 * we will invoke processWithoutAnswer()) + InvokeNotLast (invokeId==2)
	 * TC-CONTINUE TC-CONTINUE + InvokeLast (invokeId==1) + InvokeLast (invokeId==2)
	 * TC-END + Reject (duplicateInvokeId for invokeId==2)
	 */
	@Test
	public void DuplicateInvokeIdTest() throws Exception {

		this.client = new ClientComponent(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address) {

			@Override
			public void onTCConversation(TCConversationIndication ind) {
				super.onTCConversation(ind);

				step++;

				try {
					switch (step) {
					case 1:
						assertEquals(ind.getComponents().getComponents().size(), 1);
						Component c = ind.getComponents().getComponents().get(0);
						assertEquals(c.getType(), ComponentType.Reject);
						Reject r = (Reject) c;
						assertEquals((long) r.getCorrelationId(), 1);
						assertEquals(r.getProblem(), RejectProblem.returnResultUnrecognisedCorrelationId);
						assertTrue(r.isLocalOriginated());

						this.addNewInvoke(1L, 5L, false);
						this.sendContinue(false);
						break;

					case 2:
						assertEquals(ind.getComponents().getComponents().size(), 1);
						c = ind.getComponents().getComponents().get(0);
						assertEquals(c.getType(), ComponentType.Reject);
						r = (Reject) c;
						assertEquals((long) r.getCorrelationId(), 1);
						assertEquals(r.getProblem(), RejectProblem.invokeDuplicateInvocation);
						assertFalse(r.isLocalOriginated());

						this.addNewInvoke(2L, 5L, false);
						this.sendContinue(false);
						break;

					case 3:
						assertEquals(ind.getComponents().getComponents().size(), 2);
						c = ind.getComponents().getComponents().get(0);
						assertEquals(c.getType(), ComponentType.Reject);
						r = (Reject) c;
						assertEquals((long) r.getCorrelationId(), 1);
						assertEquals(r.getProblem(), RejectProblem.returnResultUnrecognisedCorrelationId);
						assertTrue(r.isLocalOriginated());

						c = ind.getComponents().getComponents().get(1);
						assertEquals(c.getType(), ComponentType.Reject);
						r = (Reject) c;
						assertEquals((long) r.getCorrelationId(), 2);
						assertEquals(r.getProblem(), RejectProblem.returnErrorUnrecognisedCorrelationId);
						assertTrue(r.isLocalOriginated());

						this.addNewInvoke(1L, 5L, false);
						this.addNewInvoke(2L, 5L, false);
						this.sendContinue(false);
						break;

					case 4:
						this.addNewInvoke(1L, 10000L, true);
						this.addNewInvoke(2L, 10000L, true);
						this.sendContinue(false);
						break;
					}
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 2");
					e.printStackTrace();
				}
			}

			@Override
			public void onTCResponse(TCResponseIndication ind) {
				super.onTCResponse(ind);

				try {
					assertEquals(ind.getComponents().getComponents().size(), 1);
					Component c = ind.getComponents().getComponents().get(0);
					assertEquals(c.getType(), ComponentType.Reject);
					Reject r = (Reject) c;
					assertEquals((long) r.getCorrelationId(), 2);
					assertEquals(r.getProblem(), RejectProblem.invokeDuplicateInvocation);
					assertFalse(r.isLocalOriginated());
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 3");
					e.printStackTrace();
				}
			}
		};

		this.server = new ServerComponent(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onTCQuery(TCQueryIndication ind) {
				super.onTCQuery(ind);

				// waiting for Invoke timeout at a client side
				EventTestHarness.waitFor(MINI_WAIT_TIME);

				try {

					this.addNewReturnResult(1L);
					this.sendContinue(false);
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 1");
					e.printStackTrace();
				}
			}

			@Override
			public void onTCConversation(TCConversationIndication ind) {
				super.onTCConversation(ind);

				// waiting for Invoke timeout at a client side
				EventTestHarness.waitFor(MINI_WAIT_TIME + 20);

				step++;

				try {
					switch (step) {
					case 1:
						assertEquals(ind.getComponents().getComponents().size(), 2);

						Component c = ind.getComponents().getComponents().get(0);
						assertEquals(c.getType(), ComponentType.Reject);
						Reject r = (Reject) c;
						assertEquals((long) r.getCorrelationId(), 1);
						assertEquals(r.getProblem(), RejectProblem.returnResultUnrecognisedCorrelationId);
						assertFalse(r.isLocalOriginated());

						c = ind.getComponents().getComponents().get(1);
						assertEquals(c.getType(), ComponentType.Reject);
						r = (Reject) c;
						assertEquals((long) r.getCorrelationId(), 1);
						assertEquals(r.getProblem(), RejectProblem.invokeDuplicateInvocation);
						assertTrue(r.isLocalOriginated());

						this.sendContinue(false);
						break;

					case 2:
						this.addNewReturnResultLast(1L);
						this.addNewReturnError(2L);
						this.sendContinue(false);
						break;

					case 3:
						this.dialog.processInvokeWithoutAnswer(1L);
						this.addNewReject();

						this.sendContinue(false);
						break;

					case 4:
						assertEquals(ind.getComponents().getComponents().size(), 2);

						c = ind.getComponents().getComponents().get(1);
						assertEquals(c.getType(), ComponentType.Reject);
						r = (Reject) c;
						assertEquals((long) r.getCorrelationId(), 2);
						assertEquals(r.getProblem(), RejectProblem.invokeDuplicateInvocation);
						assertTrue(r.isLocalOriginated());

						this.sendEnd(false);
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
		TestEvent te = TestEvent.createSentEvent(EventType.InvokeNotLast, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Begin, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.InvokeNotLast, null, cnt++, stamp + MINI_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.InvokeNotLast, null, cnt++, stamp + MINI_WAIT_TIME * 2);
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
		te = TestEvent.createSentEvent(EventType.InvokeNotLast, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.InvokeNotLast, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.InvokeLast, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.InvokeLast, null, cnt++, stamp + MINI_WAIT_TIME * 4);
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
		te = TestEvent.createReceivedEvent(EventType.InvokeNotLast, null, cnt++, stamp);
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
		te = TestEvent.createReceivedEvent(EventType.InvokeNotLast, null, cnt++, stamp + MINI_WAIT_TIME * 2);
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
		te = TestEvent.createReceivedEvent(EventType.InvokeNotLast, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeNotLast, null, cnt++, stamp + MINI_WAIT_TIME * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeLast, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 4);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp + MINI_WAIT_TIME * 5);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp + MINI_WAIT_TIME * 5);
		serverExpectedEvents.add(te);

		// !!!! ....................
//        this.saveTrafficInFile();
		// !!!! ....................

		client.startClientDialog();
		client.addNewInvoke(1L, 100L, false);
		client.sendBegin();

		EventTestHarness.waitFor(WAIT_TIME * 2);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Sending unrecognizedComponent
	 *
	 * TC-BEGIN + bad component (with component type != Invoke,ReturnResult,...) +
	 * Invoke TC-END + Reject (unrecognizedComponent)
	 */
	@Test
	public void UnrecognizedComponentTest() throws Exception {

		this.client = new ClientComponent(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address) {

			@Override
			public void onTCResponse(TCResponseIndication ind) {
				super.onTCResponse(ind);

				assertEquals(ind.getComponents().getComponents().size(), 1);
				Component c = ind.getComponents().getComponents().get(0);
				assertEquals(c.getType(), ComponentType.Reject);
				Reject r = (Reject) c;
				assertNull(r.getCorrelationId());
				try {
					assertEquals(r.getProblem(), RejectProblem.generalUnrecognisedComponentType);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
				assertFalse(r.isLocalOriginated());
			}
		};

		this.server = new ServerComponent(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onTCQuery(TCQueryIndication ind) {
				super.onTCQuery(ind);

				assertEquals(ind.getComponents().getComponents().size(), 2);
				Component c = ind.getComponents().getComponents().get(0);
				assertEquals(c.getType(), ComponentType.Reject);
				Reject r = (Reject) c;
				assertNull(r.getCorrelationId());
				try {
					assertEquals(r.getProblem(), RejectProblem.generalUnrecognisedComponentType);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
				assertTrue(r.isLocalOriginated());
				c = ind.getComponents().getComponents().get(1);
				assertEquals(c.getType(), ComponentType.InvokeNotLast);

				try {
					this.sendEnd(false);
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 1");
					e.printStackTrace();
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int cnt = 0;
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InvokeNotLast, null, cnt++, stamp);
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
		te = TestEvent.createReceivedEvent(EventType.InvokeNotLast, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
		serverExpectedEvents.add(te);

		client.startClientDialog();

		Component badComp = new BadComponentUnrecognizedComponent();
		client.dialog.sendComponent(badComp);

		client.addNewInvoke(1L, 10000L, false);
		client.sendBegin();

		EventTestHarness.waitFor(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Sending MistypedComponent Component
	 *
	 * TC-BEGIN + Invoke with an extra bad component + Invoke TC-END + Reject
	 * (mistypedComponent)
	 */
	@Test
	public void MistypedComponentTest() throws Exception {

		this.client = new ClientComponent(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address) {

			@Override
			public void onTCResponse(TCResponseIndication ind) {
				super.onTCResponse(ind);

				assertEquals(ind.getComponents().getComponents().size(), 1);
				Component c = ind.getComponents().getComponents().get(0);
				assertEquals(c.getType(), ComponentType.Reject);
				Reject r = (Reject) c;
				assertEquals((long) r.getCorrelationId(), 1);
				try {
					assertEquals(r.getProblem(), RejectProblem.generalIncorrectComponentPortion);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(r.isLocalOriginated());
			}
		};

		this.server = new ServerComponent(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onTCQuery(TCQueryIndication ind) {
				super.onTCQuery(ind);

				assertEquals(ind.getComponents().getComponents().size(), 2);
				Component c = ind.getComponents().getComponents().get(0);
				assertEquals(c.getType(), ComponentType.Reject);
				Reject r = (Reject) c;
				assertEquals((long) r.getCorrelationId(), 1);
				try {
					assertEquals(r.getProblem(), RejectProblem.generalIncorrectComponentPortion);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertTrue(r.isLocalOriginated());

				try {
					this.sendEnd(false);
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 1");
					e.printStackTrace();
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int cnt = 0;
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InvokeNotLast, null, cnt++, stamp);
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
		te = TestEvent.createReceivedEvent(EventType.InvokeNotLast, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
		serverExpectedEvents.add(te);

		client.startClientDialog();

		Component badComp = new BadComponentMistypedComponent();
		badComp.setCorrelationId(1L);
		client.dialog.sendComponent(badComp);

		client.addNewInvoke(2L, 10000L, false);
		client.sendBegin();

		EventTestHarness.waitFor(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	/**
	 * Sending BadlyStructuredComponent Component
	 *
	 * TC-BEGIN + Invoke with BadlyStructuredComponent + Invoke TC-END + Reject
	 * (mistypedComponent)
	 */
	@Test
	public void BadlyStructuredComponentTest() throws Exception {

		this.client = new ClientComponent(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address) {

			@Override
			public void onTCResponse(TCResponseIndication ind) {
				super.onTCResponse(ind);

				assertEquals(ind.getComponents().getComponents().size(), 1);
				Component c = ind.getComponents().getComponents().get(0);
				assertEquals(c.getType(), ComponentType.Reject);
				Reject r = (Reject) c;
				assertNull(r.getCorrelationId());
				try {
					assertEquals(r.getProblem(), RejectProblem.generalIncorrectComponentPortion);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}

				assertFalse(r.isLocalOriginated());
			}
		};

		this.server = new ServerComponent(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onTCQuery(TCQueryIndication ind) {
				super.onTCQuery(ind);

				assertEquals(ind.getComponents().getComponents().size(), 2);
				Component c = ind.getComponents().getComponents().get(0);
				assertEquals(c.getType(), ComponentType.Reject);
				Reject r = (Reject) c;
				assertNull(r.getCorrelationId());
				try {
					assertEquals(r.getProblem(), RejectProblem.generalIncorrectComponentPortion);
				} catch (ParseException ex) {
					assertEquals(1, 2);
				}
				assertTrue(r.isLocalOriginated());

				try {
					this.sendEnd(false);
				} catch (Exception e) {
					fail("Exception when sendComponent / send message 1");
					e.printStackTrace();
				}
			}
		};

		long stamp = System.currentTimeMillis();
		int cnt = 0;
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.InvokeNotLast, null, cnt++, stamp);
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
		te = TestEvent.createReceivedEvent(EventType.InvokeNotLast, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
		serverExpectedEvents.add(te);

		client.startClientDialog();

		Component badComp = new BadComponentBadlyStructuredComponent();
		badComp.setCorrelationId(1L);
		client.dialog.sendComponent(badComp);

		client.addNewInvoke(2L, 10000L, false);
		client.sendBegin();

		EventTestHarness.waitFor(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	public class ClientComponent extends EventTestHarness {

		protected int step = 0;

		public ClientComponent(final TCAPStack stack, final ParameterFactory parameterFactory,
				final SccpAddress thisAddress, final SccpAddress remoteAddress) {
			super(stack, parameterFactory, thisAddress, remoteAddress);
			ASNParser parser = stack.getProvider().getParser();
			try {
				parser.registerAlternativeClassMapping(ASNInvokeSetParameterImpl.class, ComponentTestASN.class);
			} catch (Exception ex) {
				// already registered;
			}
		}

		public DialogImpl getCurDialog() {
			return (DialogImpl) this.dialog;
		}

		@Override
		public void onTCConversation(TCConversationIndication ind) {
			super.onTCConversation(ind);

			procComponents(ind.getComponents().getComponents());
		}

		@Override
		public void onTCResponse(TCResponseIndication ind) {
			super.onTCResponse(ind);

			procComponents(ind.getComponents().getComponents());
		}

		private void procComponents(List<Component> compp) {
			if (compp != null)
				for (Component c : compp) {
					EventType et = null;
					if (c.getType() == ComponentType.InvokeNotLast)
						et = EventType.InvokeNotLast;
					if (c.getType() == ComponentType.InvokeLast)
						et = EventType.InvokeLast;
					if (c.getType() == ComponentType.ReturnResultNotLast)
						et = EventType.ReturnResult;
					if (c.getType() == ComponentType.ReturnResultLast)
						et = EventType.ReturnResultLast;
					if (c.getType() == ComponentType.ReturnError)
						et = EventType.ReturnError;
					if (c.getType() == ComponentType.Reject)
						et = EventType.Reject;

					TestEvent te = TestEvent.createReceivedEvent(et, c, sequence++);
					this.observerdEvents.add(te);
				}
		}

		public void addNewInvoke(Long invokeId, Long timout, boolean last) throws Exception {

			Invoke invoke;
			if (last)
				invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestLast();
			else
				invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestNotLast();

			invoke.setInvokeId(invokeId);

			OperationCode oc = TcapFactory.createPrivateOperationCode(2357);
//            oc.setNationalOperationCode(10L);
			invoke.setOperationCode(oc);

			ComponentTestASN p = new ComponentTestASN(Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4, 5 }));
			invoke.setSetParameter(p);
			invoke.setTimeout(timout);

			TestEvent te;
			if (last)
				te = TestEvent.createSentEvent(EventType.InvokeLast, null, sequence++);
			else
				te = TestEvent.createSentEvent(EventType.InvokeNotLast, null, sequence++);
			this.observerdEvents.add(te);

			this.dialog.sendComponent(invoke);
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
			super(stack, parameterFactory, thisAddress, remoteAddress);
			// TODO Auto-generated constructor stub
			ASNParser parser = stack.getProvider().getParser();
			try {
				parser.registerAlternativeClassMapping(ASNInvokeSetParameterImpl.class, ComponentTestASN.class);
			} catch (Exception ex) {
				// already registered;
			}
		}

		public void addNewInvoke(Long invokeId, Long timout) throws Exception {

			Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequestNotLast();
			invoke.setInvokeId(invokeId);

			OperationCode oc = TcapFactory.createNationalOperationCode(10);
			invoke.setOperationCode(oc);

			invoke.setTimeout(timout);

			TestEvent te = TestEvent.createSentEvent(EventType.InvokeNotLast, null, sequence++);
			this.observerdEvents.add(te);
			this.dialog.sendComponent(invoke);
		}

		public void addNewReject() throws Exception {

			Reject rej = this.tcapProvider.getComponentPrimitiveFactory().createTCRejectRequest();
			rej.setProblem(RejectProblem.returnErrorUnexpectedError);

			TestEvent te = TestEvent.createSentEvent(EventType.Reject, null, sequence++);
			this.observerdEvents.add(te);
			this.dialog.sendComponent(rej);
		}

		public void addNewReturnResult(Long invokeId) throws Exception {

			Return rr = this.tcapProvider.getComponentPrimitiveFactory().createTCResultNotLastRequest();
			rr.setCorrelationId(invokeId);

//            oc.setNationalOperationCode(10L);
//            rr.setOperationCode(oc);

			TestEvent te = TestEvent.createSentEvent(EventType.ReturnResult, null, sequence++);
			this.observerdEvents.add(te);
			this.dialog.sendComponent(rr);
		}

		public void addNewReturnResultLast(Long invokeId) throws Exception {

			Return rr = this.tcapProvider.getComponentPrimitiveFactory().createTCResultLastRequest();
			rr.setCorrelationId(invokeId);

//            OperationCode oc = TcapFactory.createOperationCode();
//
//            oc.setNationalOperationCode(10L);
//            rr.setOperationCode(oc);

			TestEvent te = TestEvent.createSentEvent(EventType.ReturnResultLast, null, sequence++);
			this.observerdEvents.add(te);
			this.dialog.sendComponent(rr);
		}

		public void addNewReturnError(Long invokeId) throws Exception {

			ReturnError err = this.tcapProvider.getComponentPrimitiveFactory().createTCReturnErrorRequest();
			err.setCorrelationId(invokeId);

			ErrorCode ec = TcapFactory.createPrivateErrorCode(1);
//            ec.setNationalErrorCode(10L);
			err.setErrorCode(ec);

			TestEvent te = TestEvent.createSentEvent(EventType.ReturnError, null, sequence++);
			this.observerdEvents.add(te);
			this.dialog.sendComponent(err);
		}

		@Override
		public void onTCQuery(TCQueryIndication ind) {
			super.onTCQuery(ind);

			procComponents(ind.getComponents().getComponents());
		}

		@Override
		public void onTCConversation(TCConversationIndication ind) {
			super.onTCConversation(ind);

			procComponents(ind.getComponents().getComponents());
		}

		private void procComponents(List<Component> compp) {
			if (compp != null)
				for (Component c : compp) {
					EventType et = null;
					if (c.getType() == ComponentType.InvokeNotLast)
						et = EventType.InvokeNotLast;
					if (c.getType() == ComponentType.InvokeLast)
						et = EventType.InvokeLast;
					if (c.getType() == ComponentType.ReturnResultNotLast)
						et = EventType.ReturnResult;
					if (c.getType() == ComponentType.ReturnResultLast)
						et = EventType.ReturnResultLast;
					if (c.getType() == ComponentType.ReturnError)
						et = EventType.ReturnError;
					if (c.getType() == ComponentType.Reject)
						et = EventType.Reject;

					TestEvent te = TestEvent.createReceivedEvent(et, c, sequence++);
					this.observerdEvents.add(te);
				}
		}
	}

	/**
	 * A bad component with UnrecognizedComponent (unrecognized component tag)
	 *
	 */
	class BadComponentUnrecognizedComponentWrapper extends WrappedComponentImpl {
		BadComponentUnrecognizedComponent badComponent;

		@Override
		public Component getExistingComponent() {
			return badComponent;
		}
	}

	@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 20, constructed = true, lengthIndefinite = false)
	class BadComponentUnrecognizedComponent extends InvokeNotLastImpl {
		public BadComponentUnrecognizedComponent() {
			this.setInvokeId(1l);
		}

		@Override
		public void setCorrelationId(Long i) {
			// TODO Auto-generated method stub

		}

		@Override
		public Long getCorrelationId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ComponentType getType() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/**
	 * A bad component with MistypedComponent
	 *
	 */
	class BadComponentMistypedComponent extends InvokeLastImpl {
		@ASNProperty(asnClass = ASNClass.PRIVATE, tag = 30, constructed = false, index = -1)
		private ASNInteger unexpectedParam = new ASNInteger((Long) null, "UnexpectedParam", null, null, false);

		public BadComponentMistypedComponent() {
			this.setInvokeId(1l);
			OperationCode oc = TcapFactory.createNationalOperationCode(20);
			this.setOperationCode(oc);
		}
	}

	/**
	 * A bad component with BadlyStructuredComponent
	 *
	 */
	class BadComponentBadlyStructuredComponent extends InvokeLastImpl {

		@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 5, constructed = false, index = -1)
		private ASNInteger nationalOperationCode = new ASNInteger(18, null, null, null, null);

		public BadComponentBadlyStructuredComponent() {
			OperationCode oc = TcapFactory.createNationalOperationCode(20);
			this.setOperationCode(oc);
		}

		@Override
		public Long getInvokeId() {
			return 1L;
		}
	}
}