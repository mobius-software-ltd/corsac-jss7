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

package org.restcomm.protocols.ss7.tcap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for component processing
 *
 * @author sergey vetyutnev
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

    @BeforeClass
    public void setUpClass() {
        this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
        this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";
        System.out.println("setUpClass");
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        System.out.println("tearDownClass");
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("setUp");
        super.setUp();

        peer1Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        peer2Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

        this.tcapStack1 = new TCAPStackImpl("TCAPComponentsTest", this.sccpProvider1, 8, 4);
        this.tcapStack2 = new TCAPStackImpl("TCAPComponentsTest", this.sccpProvider2, 8, 4);

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
    @AfterMethod
    public void tearDown() {
        this.tcapStack1.stop();
        this.tcapStack2.stop();
        super.tearDown();

    }

    /**
     * Testing diplicateInvokeId case All Invokes are with a little invokeTimeout(removed before an answer from a Server) !!!
     *
     * TC-BEGIN + Invoke (invokeId==1) TC-CONTINUE + ReturnResult (invokeId==1) TC-CONTINUE + Reject(unrecognizedInvokeId) +
     * Invoke (invokeId==1) TC-CONTINUE + Reject (duplicateInvokeId) TC-CONTINUE + Invoke (invokeId==2) TC-CONTINUE +
     * ReturnResultLast (invokeId==1) + ReturnError (invokeId==2) TC-CONTINUE + Invoke (invokeId==1, for this message we will
     * invoke processWithoutAnswer()) + Invoke (invokeId==2) TC-CONTINUE TC-CONTINUE + Invoke (invokeId==1) + Invoke
     * (invokeId==2) * TC-END + Reject (duplicateInvokeId for invokeId==2)
     */
    @Test(groups = { "functional.flow" })
    public void DuplicateInvokeIdTest() throws Exception {

        this.client = new ClientComponent(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address) {

            @Override
            public void onTCContinue(TCContinueIndication ind) {
                super.onTCContinue(ind);

                step++;

                try {
                    switch (step) {
                        case 1:
                            assertEquals(ind.getComponents().size(), 1);
                            BaseComponent c = ind.getComponents().get(0);
                            assertTrue(c instanceof Reject);
                            Reject r = (Reject)c;
                            assertEquals((long) r.getInvokeId(), 1);
                            assertEquals(r.getProblem().getReturnResultProblemType(),
                                    ReturnResultProblemType.UnrecognizedInvokeID);
                            assertTrue(r.isLocalOriginated());

                            this.addNewInvoke(1, MINI_WAIT_TIME/2);
                            this.sendContinue();
                            break;

                        case 2:
                            assertEquals(ind.getComponents().size(), 1);
                            c = ind.getComponents().get(0);
                            assertTrue(c instanceof Reject);
                            r = (Reject)c;
                            assertEquals((long) r.getInvokeId(), 1);
                            assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
                            assertFalse(r.isLocalOriginated());

                            this.addNewInvoke(2, MINI_WAIT_TIME/2);
                            this.sendContinue();
                            break;

                        case 3:
                            assertEquals(ind.getComponents().size(), 2);
                            c = ind.getComponents().get(0);
                            assertTrue(c instanceof Reject);
                            r = (Reject)c;
                            assertEquals((long) r.getInvokeId(), 1);
                            assertEquals(r.getProblem().getReturnResultProblemType(),
                                    ReturnResultProblemType.UnrecognizedInvokeID);
                            assertTrue(r.isLocalOriginated());

                            c = ind.getComponents().get(1);
                            assertTrue(c instanceof Reject);
                            r = (Reject)c;
                            assertEquals((long) r.getInvokeId(), 2);
                            assertEquals(r.getProblem().getReturnErrorProblemType(),
                                    ReturnErrorProblemType.UnrecognizedInvokeID);
                            assertTrue(r.isLocalOriginated());

                            this.addNewInvoke(1, MINI_WAIT_TIME/2);
                            this.addNewInvoke(2, MINI_WAIT_TIME/2);
                            this.sendContinue();
                            break;

                        case 4:
                            this.addNewInvoke(1, 10000L);
                            this.addNewInvoke(2, 10000L);
                            this.sendContinue();
                            break;
                    }
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 2", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onTCEnd(TCEndIndication ind) {
                super.onTCEnd(ind);

                try {
                    assertEquals(ind.getComponents().size(), 1);
                    BaseComponent c = ind.getComponents().get(0);
                    assertTrue(c instanceof Reject);
                    Reject r = (Reject)c;
                    assertEquals((long) r.getInvokeId(), 2);
                    assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
                    assertFalse(r.isLocalOriginated());
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 3", e);
                    e.printStackTrace();
                }
            }
        };

        this.server = new ServerComponent(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

            @Override
            public void onTCBegin(TCBeginIndication ind) {
                super.onTCBegin(ind);

                // waiting for Invoke timeout at a client side
                EventTestHarness.waitFor(MINI_WAIT_TIME);
                
                try {

                    this.addNewReturnResult(1);
                    this.sendContinue();
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 1", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onTCContinue(TCContinueIndication ind) {
                super.onTCContinue(ind);

                // waiting for Invoke timeout at a client side
                EventTestHarness.waitFor(MINI_WAIT_TIME);

                step++;

                try {
                    switch (step) {
                        case 1:
                            assertEquals(ind.getComponents().size(), 2);

                            BaseComponent c = ind.getComponents().get(0);
                            assertTrue(c instanceof Reject);
                            Reject r = (Reject)c;
                            assertEquals((long) r.getInvokeId(), 1);
                            assertEquals(r.getProblem().getReturnResultProblemType(),
                                    ReturnResultProblemType.UnrecognizedInvokeID);
                            assertFalse(r.isLocalOriginated());

                            c = ind.getComponents().get(1);
                            assertTrue(c instanceof Reject);
                            r = (Reject)c;
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
                            r = (Reject)c;
                            assertEquals((long) r.getInvokeId(), 2);
                            assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
                            assertTrue(r.isLocalOriginated());

                            this.sendEnd(TerminationType.Basic);
                            break;
                    }
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 2", e);
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
        client.addNewInvoke(1, MINI_WAIT_TIME/2);
        client.sendBegin();

        EventTestHarness.waitFor(WAIT_TIME * 2);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    } 

    public class ClientComponent extends EventTestHarness {

        protected int step = 0;
        
        public ClientComponent(final TCAPStack stack, final ParameterFactory parameterFactory, final SccpAddress thisAddress, final SccpAddress remoteAddress) {
            super(stack, parameterFactory, thisAddress, remoteAddress);

        }

        public DialogImpl getCurDialog() {
            return (DialogImpl) this.dialog;
        }

        @Override
        public void onTCContinue(TCContinueIndication ind) {
            super.onTCContinue(ind);

            procComponents(ind.getComponents());
        }

        @Override
        public void onTCEnd(TCEndIndication ind) {
            super.onTCEnd(ind);

            procComponents(ind.getComponents());
        }

        private void procComponents(List<BaseComponent> compp) {
            if (compp != null) {
                for (BaseComponent c : compp) {
                    EventType et = null;
                    if (c instanceof Invoke) {
                        et = EventType.Invoke;
                    }
                    if (c instanceof ReturnResult) {
                        et = EventType.ReturnResult;
                    }
                    if (c instanceof ReturnResultLast) {
                        et = EventType.ReturnResultLast;
                    }
                    if (c instanceof ReturnError) {
                        et = EventType.ReturnError;
                    }
                    if (c instanceof Reject) {
                        et = EventType.Reject;
                    }

                    TestEvent te = TestEvent.createReceivedEvent(et, c, sequence++);
                    this.observerdEvents.add(te);
                }
            }
        }

        public void addNewInvoke(Integer invokeId, Long timout) throws Exception {
            OperationCode oc = TcapFactory.createLocalOperationCode(10);
            
            // Parameter p1 = TcapFactory.createParameter();
            // p1.setTagClass(Tag.CLASS_UNIVERSAL);
            // p1.setTag(Tag.STRING_OCTET);
            // p1.setData(new byte[]{0x0F});
            //
            // Parameter p2 = TcapFactory.createParameter();
            // p2.setTagClass(Tag.CLASS_UNIVERSAL);
            // p2.setTag(Tag.STRING_OCTET);
            // p2.setData(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19,
            // 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5,
            // 0x72, (byte) 0xb9, 0x11 });
            //
            // Parameter pm = TcapFactory.createParameter();
            // pm.setTagClass(Tag.CLASS_UNIVERSAL);
            // pm.setTag(Tag.SEQUENCE);
            // pm.setParameters(new Parameter[]{p1, p2});
            // invoke.setParameter(pm);

            TestEvent te = TestEvent.createSentEvent(EventType.Invoke, null, sequence++);
            this.observerdEvents.add(te);

            this.dialog.sendData(invokeId, null, null, timout, oc, null, true, false);
        }
    }

    public class ServerComponent extends EventTestHarness {

        protected int step = 0;

        /**
         * @param stack
         * @param thisAddress
         * @param remoteAddress
         */
        public ServerComponent(final TCAPStack stack, final ParameterFactory parameterFactory, final SccpAddress thisAddress, final SccpAddress remoteAddress) {
            super(stack, parameterFactory, thisAddress, remoteAddress);
            // TODO Auto-generated constructor stub
        }

        public void addNewReturnResult(Integer invokeId) throws Exception {

            OperationCode oc = TcapFactory.createLocalOperationCode(10);
            
            TestEvent te = TestEvent.createSentEvent(EventType.ReturnResult, null, sequence++);
            this.observerdEvents.add(te);

            this.dialog.sendData(invokeId, null, null, null, oc, null, false, false);
        }

        public void addNewReturnResultLast(Integer invokeId) throws Exception {

            OperationCode oc = TcapFactory.createLocalOperationCode(10);
            
            TestEvent te = TestEvent.createSentEvent(EventType.ReturnResultLast, null, sequence++);
            this.observerdEvents.add(te);

            this.dialog.sendData(invokeId, null, null, null, oc, null, false, true);
        }

        public void addNewReturnError(Integer invokeId) throws Exception {

            ErrorCode ec = TcapFactory.createLocalErrorCode(10);
            
            TestEvent te = TestEvent.createSentEvent(EventType.ReturnError, null, sequence++);
            this.observerdEvents.add(te);

            this.dialog.sendError(invokeId, ec, null);
        }

        @Override
        public void onTCBegin(TCBeginIndication ind) {
            super.onTCBegin(ind);

            procComponents(ind.getComponents());
        }

        @Override
        public void onTCContinue(TCContinueIndication ind) {
            super.onTCContinue(ind);

            procComponents(ind.getComponents());
        }

        private void procComponents(List<BaseComponent> compp) {
            if (compp != null) {
                for (BaseComponent c : compp) {
                    EventType et = null;
                    if (c instanceof Invoke) {
                        et = EventType.Invoke;
                    }
                    if (c instanceof ReturnResult) {
                        et = EventType.ReturnResult;
                    }
                    if (c instanceof ReturnResultLast) {
                        et = EventType.ReturnResultLast;
                    }
                    if (c instanceof ReturnError) {
                        et = EventType.ReturnError;
                    }
                    if (c instanceof Reject) {
                        et = EventType.Reject;
                    }

                    TestEvent te = TestEvent.createReceivedEvent(et, c, sequence++);
                    this.observerdEvents.add(te);
                }
            }
        }
    }
}