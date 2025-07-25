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
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;
import org.restcomm.protocols.ss7.tcap.asn.messages.BadComponentImpl;
import org.restcomm.protocols.ss7.tcap.asn.messages.BadInvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.messages.MistypedInvokeImpl;
import org.restcomm.protocols.ss7.tcap.listeners.TCAPTestHarness;
import org.restcomm.protocols.ss7.tcap.listeners.events.EventType;
import org.restcomm.protocols.ss7.tcap.listeners.events.TestEvent;
import org.restcomm.protocols.ss7.tcap.listeners.events.TestEventFactory;
import org.restcomm.protocols.ss7.tcap.utils.EventTestUtils;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 * Test for component processing
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TCAPComponentsTest extends SccpHarness {
	private static final long INVOKE_TIMEOUT = 1000;
	private static final long DIALOG_TIMEOUT = Integer.MAX_VALUE;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private ClientComponent client;
	private ServerComponent server;

	@Before
	public void beforeEach() throws Exception {
		super.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		super.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		tcapStack1 = new TCAPStackImpl("TCAPComponentsTest", this.sccpProvider1, 8, workerPool);
		tcapStack2 = new TCAPStackImpl("TCAPComponentsTest", this.sccpProvider2, 8, workerPool);

		tcapStack1.start();
		tcapStack2.start();

		// default invoke timeouts
		tcapStack1.setInvokeTimeout(INVOKE_TIMEOUT);
		tcapStack2.setInvokeTimeout(INVOKE_TIMEOUT);

		// default dialog timeouts
		tcapStack1.setDialogIdleTimeout(DIALOG_TIMEOUT);
		tcapStack2.setDialogIdleTimeout(DIALOG_TIMEOUT);

		client = new ClientComponent(tcapStack1, parameterFactory, peer1Address, peer2Address);
		server = new ServerComponent(tcapStack2, parameterFactory, peer2Address, peer1Address);
	}

	@After
	public void afterEach() {
		if (tcapStack1 != null) {
			tcapStack1.stop();
			tcapStack1 = null;
		}

		if (tcapStack2 != null) {
			tcapStack2.stop();
			tcapStack2 = null;
		}

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
		ASNParser parser1 = tcapStack1.getProvider().getParser();
		parser1.registerAlternativeClassMapping(ASNInvokeParameterImpl.class, InvokeTestASN.class);

		ASNParser parser2 = tcapStack2.getProvider().getParser();
		parser2.registerAlternativeClassMapping(ASNInvokeParameterImpl.class, InvokeTestASN.class);

		client.startClientDialog();

		BaseComponent badComp = new BadInvokeImpl();
		badComp.setInvokeId(1);
		client.getCurrentDialog().sendComponent(badComp);

		// 1. TC-BEGIN + Invoke with BadlyStructuredComponent + Invoke
		client.addNewInvoke(2, 10000L);
		client.sendBegin();

		client.awaitSent(EventType.Invoke, EventType.Begin);
		server.awaitReceived(EventType.Begin, EventType.Reject, EventType.Invoke);
		{
			TestEvent event = server.getNextEvent(EventType.Begin);
			TCBeginIndication ind = (TCBeginIndication) event.getEvent();

			assertEquals(2, ind.getComponents().size());
			BaseComponent c = ind.getComponents().get(0);

			assertTrue(c instanceof Reject);
			Reject r = (Reject) c;
			assertEquals(GeneralProblemType.MistypedComponent, r.getProblem().getGeneralProblemType());

			assertTrue(r.isLocalOriginated());
		}

		// 2. TC-END + Reject (mistypedComponent)
		server.sendEnd(TerminationType.Basic);

		server.awaitSent(EventType.End);
		client.awaitReceived(EventType.End, EventType.Reject);
		{
			TestEvent event = client.getNextEvent(EventType.End);
			TCEndIndication ind = (TCEndIndication) event.getEvent();

			assertEquals(1, ind.getComponents().size());
			BaseComponent c = ind.getComponents().get(0);
			assertTrue(c instanceof Reject);
			Reject r = (Reject) c;
			assertEquals((int) r.getInvokeId(), 1);
			assertEquals(GeneralProblemType.MistypedComponent, r.getProblem().getGeneralProblemType());

			assertFalse(r.isLocalOriginated());
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Invoke);
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.End);
		clientExpected.addReceived(EventType.Reject);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addReceived(EventType.Reject);
		serverExpected.addReceived(EventType.Invoke);
		serverExpected.addSent(EventType.End);
		serverExpected.addReceived(EventType.DialogRelease);

		EventTestUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		EventTestUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
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
		client.startClientDialog();

		// 1. TC-BEGIN + bad component + Invoke
		BaseComponent badComp = new BadComponentImpl();
		client.getCurrentDialog().sendComponent(badComp);

		client.addNewInvoke(1, 10000L);
		client.sendBegin();

		client.awaitSent(EventType.Invoke, EventType.Begin);
		server.awaitReceived(EventType.Begin, EventType.Reject, EventType.Invoke);
		{
			TestEvent event = server.getNextEvent(EventType.Begin);
			TCBeginIndication ind = (TCBeginIndication) event.getEvent();

			assertEquals(2, ind.getComponents().size());
			BaseComponent c = ind.getComponents().get(0);
			assertTrue(c instanceof Reject);
			Reject r = (Reject) c;
			assertNull(r.getInvokeId());
			assertEquals(GeneralProblemType.UnrecognizedComponent, r.getProblem().getGeneralProblemType());

			assertTrue(r.isLocalOriginated());
		}

		// 2. TC-END + Reject (unrecognizedComponent)
		server.sendEnd(TerminationType.Basic);
		server.awaitSent(EventType.End);
		client.awaitReceived(EventType.End, EventType.Reject);
		{
			TestEvent event = client.getNextEvent(EventType.End);
			TCEndIndication ind = (TCEndIndication) event.getEvent();

			assertEquals(1, ind.getComponents().size());
			BaseComponent c = ind.getComponents().get(0);
			assertTrue(c instanceof Reject);
			Reject r = (Reject) c;
			assertNull(r.getInvokeId());
			assertEquals(GeneralProblemType.UnrecognizedComponent, r.getProblem().getGeneralProblemType());

			assertFalse(r.isLocalOriginated());
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Invoke);
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.End);
		clientExpected.addReceived(EventType.Reject);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addReceived(EventType.Reject);
		serverExpected.addReceived(EventType.Invoke);
		serverExpected.addSent(EventType.End);
		serverExpected.addReceived(EventType.DialogRelease);

		EventTestUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		EventTestUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Sending MistypedComponent Component
	 *
	 * <pre>
	 * TC-BEGIN + Invoke with an extra bad component + Invoke 
	 * TC-END + Reject (mistypedComponent)
	 * </pre>
	 */
	@Test
	public void MistypedComponentTest() throws Exception {
		client.startClientDialog();

		// 1. TC-BEGIN + Invoke with an extra bad component + Invoke
		BaseComponent badComp = new MistypedInvokeImpl(100);
		badComp.setInvokeId(1);
		client.getCurrentDialog().sendComponent(badComp);

		client.addNewInvoke(2, 10000L);
		client.sendBegin();

		client.awaitSent(EventType.Invoke, EventType.Begin);
		server.awaitReceived(EventType.Begin, EventType.Reject, EventType.Invoke);
		{
			TestEvent event = server.getNextEvent(EventType.Begin);
			TCBeginIndication ind = (TCBeginIndication) event.getEvent();

			assertEquals(2, ind.getComponents().size());
			BaseComponent c = ind.getComponents().get(0);
			assertTrue(c instanceof Reject);
			Reject r = (Reject) c;
			assertEquals(1, (long) r.getInvokeId());
			assertEquals(GeneralProblemType.MistypedComponent, r.getProblem().getGeneralProblemType());

			assertTrue(r.isLocalOriginated());
		}

		// 2. TC-END + Reject (mistypedComponent)
		server.sendEnd(TerminationType.Basic);
		server.awaitSent(EventType.End);
		client.awaitReceived(EventType.End, EventType.Reject);
		{
			TestEvent event = client.getNextEvent(EventType.End);
			TCEndIndication ind = (TCEndIndication) event.getEvent();

			assertEquals(1, ind.getComponents().size());
			BaseComponent c = ind.getComponents().get(0);
			assertTrue(c instanceof Reject);
			Reject r = (Reject) c;
			assertEquals(1, (long) r.getInvokeId());
			assertEquals(GeneralProblemType.MistypedComponent, r.getProblem().getGeneralProblemType());

			assertFalse(r.isLocalOriginated());
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Invoke);
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.End);
		clientExpected.addReceived(EventType.Reject);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addReceived(EventType.Reject);
		serverExpected.addReceived(EventType.Invoke);
		serverExpected.addSent(EventType.End);
		serverExpected.addReceived(EventType.DialogRelease);

		EventTestUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		EventTestUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
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
	 * TC-CONTINUE 
	 * TC-CONTINUE + Invoke (invokeId=1) + Invoke (invokeId=2)
	 * TC-END + Reject (duplicateInvokeId for invokeId=2)
	 * </pre>
	 */
	@Test
	public void DuplicateInvokeIdTest() throws Exception {
		// 1. TC-BEGIN + Invoke (invokeId=1)
		client.startClientDialog();
		client.addNewInvoke(1, INVOKE_TIMEOUT);
		client.sendBegin();

		EventTestUtils.updateStamp();

		client.awaitSent(EventType.Invoke, EventType.Begin);
		server.awaitReceived(EventType.Begin, EventType.Invoke);

		client.awaitReceived(EventType.InvokeTimeout); // server does not responds
		EventTestUtils.assertPassed(INVOKE_TIMEOUT);

		// 2. TC-CONTINUE + ReturnResult (invokeId=1)
		server.addNewReturnResult(1);
		server.sendContinue();

		server.awaitSent(EventType.ReturnResult, EventType.Continue);
		client.awaitReceived(EventType.Continue, EventType.Reject);
		{
			TestEvent event = client.getNextEvent(EventType.Continue);
			TCContinueIndication ind = (TCContinueIndication) event.getEvent();

			assertEquals(ind.getComponents().size(), 1);
			BaseComponent c = ind.getComponents().get(0);

			assertTrue(c instanceof Reject);
			Reject r = (Reject) c;
			assertEquals((long) r.getInvokeId(), 1);
			assertEquals(r.getProblem().getReturnResultProblemType(), ReturnResultProblemType.UnrecognizedInvokeID);
			assertTrue(r.isLocalOriginated());
		}

		// 3. TC-CONTINUE + Reject(unrecognizedInvokeId) + Invoke (invokeId=1)
		client.addNewInvoke(1, INVOKE_TIMEOUT);
		client.sendContinue();
		EventTestUtils.updateStamp();

		client.awaitSent(EventType.Invoke, EventType.Continue);
		server.awaitReceived(EventType.Continue, EventType.Reject, EventType.Reject);
		{
			TestEvent event = server.getNextEvent(EventType.Continue);
			TCContinueIndication ind = (TCContinueIndication) event.getEvent();

			assertEquals(ind.getComponents().size(), 2);

			BaseComponent c = ind.getComponents().get(0);
			assertTrue(c instanceof Reject);
			Reject r = (Reject) c;
			assertEquals((long) r.getInvokeId(), 1);
			assertEquals(r.getProblem().getReturnResultProblemType(), ReturnResultProblemType.UnrecognizedInvokeID);
			assertFalse(r.isLocalOriginated());

			c = ind.getComponents().get(1);
			assertTrue(c instanceof Reject);
			r = (Reject) c;
			assertEquals((long) r.getInvokeId(), 1);
			assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
			assertTrue(r.isLocalOriginated());
		}

		client.awaitReceived(EventType.InvokeTimeout); // server does not responds
		EventTestUtils.assertPassed(INVOKE_TIMEOUT);

		// 4. TC-CONTINUE + Reject (duplicateInvokeId)
		server.sendContinue();

		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue, EventType.Reject);

		// 5. TC-CONTINUE + Invoke (invokeId=2)
		client.addNewInvoke(2, INVOKE_TIMEOUT);
		client.sendContinue();

		client.awaitSent(EventType.Invoke, EventType.Continue);
		server.awaitReceived(EventType.Continue, EventType.Invoke);
		EventTestUtils.updateStamp();

		client.awaitReceived(EventType.InvokeTimeout); // server does not responds
		EventTestUtils.assertPassed(INVOKE_TIMEOUT);

		// 6. TC-CONTINUE + ReturnResultLast (invokeId=1) + ReturnError (invokeId=2)

		server.addNewReturnResultLast(1);
		server.addNewReturnError(2);
		server.sendContinue();

		server.awaitSent(EventType.ReturnResultLast, EventType.ReturnError, EventType.Continue);
		client.awaitReceived(EventType.Continue, EventType.Reject, EventType.Reject);

		// 7. TC-CONTINUE + Invoke (invokeId=1) + Invoke (invokeId==2)
		client.addNewInvoke(1, INVOKE_TIMEOUT);
		client.addNewInvoke(2, INVOKE_TIMEOUT);
		client.sendContinue();
		EventTestUtils.updateStamp();

		client.awaitSent(EventType.Invoke, EventType.Invoke, EventType.Continue);
		server.awaitReceived(EventType.Continue);
		server.awaitReceived(EventType.Reject, EventType.Reject, EventType.Invoke, EventType.Invoke);

		client.awaitReceived(EventType.InvokeTimeout, EventType.InvokeTimeout);
		EventTestUtils.assertPassed(INVOKE_TIMEOUT);

		// 8. TC-CONTINUE
		server.dialog.processInvokeWithoutAnswer(1);
		server.sendContinue();

		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);

		// 9. TC-CONTINUE + Invoke (invokeId=1) + Invoke (invokeId=2)
		client.addNewInvoke(1, 10000L);
		client.addNewInvoke(2, 10000L);
		client.sendContinue();

		client.awaitSent(EventType.Invoke, EventType.Invoke, EventType.Continue);
		server.awaitReceived(EventType.Continue, EventType.Invoke, EventType.Reject);
		{
			TestEvent event = server.getNextEventWithSkip(EventType.Continue, 2);
			TCContinueIndication ind = (TCContinueIndication) event.getEvent();
			assertEquals(ind.getComponents().size(), 2);

			BaseComponent c = ind.getComponents().get(1);
			assertTrue(c instanceof Reject);
			Reject r = (Reject) c;
			assertEquals((long) r.getInvokeId(), 2);
			assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
			assertTrue(r.isLocalOriginated());
		}

		// 10. TC-END + Reject (duplicateInvokeId for invokeId=2)
		server.sendEnd(TerminationType.Basic);

		server.awaitSent(EventType.End);
		client.awaitReceived(EventType.End, EventType.Reject);
		{
			TestEvent event = client.getNextEvent(EventType.End);
			TCEndIndication ind = (TCEndIndication) event.getEvent();

			assertEquals(ind.getComponents().size(), 1);
			BaseComponent c = ind.getComponents().get(0);
			assertTrue(c instanceof Reject);
			Reject r = (Reject) c;
			assertEquals((long) r.getInvokeId(), 2);
			assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
			assertFalse(r.isLocalOriginated());
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Invoke);
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.InvokeTimeout);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.Reject);
		clientExpected.addSent(EventType.Invoke);
		clientExpected.addSent(EventType.Continue);
		clientExpected.addReceived(EventType.InvokeTimeout);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.Reject);
		clientExpected.addSent(EventType.Invoke);
		clientExpected.addSent(EventType.Continue);
		clientExpected.addReceived(EventType.InvokeTimeout);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.Reject);
		clientExpected.addReceived(EventType.Reject);
		clientExpected.addSent(EventType.Invoke);
		clientExpected.addSent(EventType.Invoke);
		clientExpected.addSent(EventType.Continue);
		clientExpected.addReceived(EventType.InvokeTimeout);
		clientExpected.addReceived(EventType.InvokeTimeout);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addSent(EventType.Invoke);
		clientExpected.addSent(EventType.Invoke);
		clientExpected.addSent(EventType.Continue);
		clientExpected.addReceived(EventType.End);
		clientExpected.addReceived(EventType.Reject);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addReceived(EventType.Invoke);
		serverExpected.addSent(EventType.ReturnResult);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.Continue);
		serverExpected.addReceived(EventType.Reject);
		serverExpected.addReceived(EventType.Reject);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.Continue);
		serverExpected.addReceived(EventType.Invoke);
		serverExpected.addSent(EventType.ReturnResultLast);
		serverExpected.addSent(EventType.ReturnError);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.Continue);
		serverExpected.addReceived(EventType.Reject);
		serverExpected.addReceived(EventType.Reject);
		serverExpected.addReceived(EventType.Invoke);
		serverExpected.addReceived(EventType.Invoke);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.Continue);
		serverExpected.addReceived(EventType.Invoke);
		serverExpected.addReceived(EventType.Reject);
		serverExpected.addSent(EventType.End);
		serverExpected.addReceived(EventType.DialogRelease);

		EventTestUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		EventTestUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	public class ClientComponent extends TCAPTestHarness {
		public ClientComponent(final TCAPStack stack, final ParameterFactory parameterFactory,
				final SccpAddress thisAddress, final SccpAddress remoteAddress) {
			super(stack, parameterFactory, thisAddress, remoteAddress, LogManager.getLogger(ClientComponent.class));

			super.listenerName = "Client";
		}

		@Override
		public DialogImpl getCurrentDialog() {
			return (DialogImpl) super.dialog;
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

			super.dialog.sendData(invokeId, null, null, timeout, oc, null, true, false);
			super.handleSent(EventType.Invoke, null);
		}
	}

	public class ServerComponent extends TCAPTestHarness {
		protected int step = 0;

		public ServerComponent(final TCAPStack stack, final ParameterFactory parameterFactory,
				final SccpAddress thisAddress, final SccpAddress remoteAddress) {
			super(stack, parameterFactory, thisAddress, remoteAddress, LogManager.getLogger(ServerComponent.class));

			super.listenerName = "Server";
		}

		public void addNewReturnResult(Integer invokeId) throws Exception {
			OperationCode oc = TcapFactory.createLocalOperationCode(10);
			super.dialog.sendData(invokeId, null, null, null, oc, null, false, false);

			super.handleSent(EventType.ReturnResult, null);
		}

		public void addNewReturnResultLast(Integer invokeId) throws Exception {
			OperationCode oc = TcapFactory.createLocalOperationCode(10);
			super.dialog.sendData(invokeId, null, null, null, oc, null, false, true);

			super.handleSent(EventType.ReturnResultLast, null);
		}

		public void addNewReturnError(Integer invokeId) throws Exception {
			ErrorCode ec = TcapFactory.createLocalErrorCode(10);
			super.dialog.sendError(invokeId, ec, null);

			super.handleSent(EventType.ReturnError, null);
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