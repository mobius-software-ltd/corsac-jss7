/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

package org.restcomm.protocols.ss7.m3ua.impl.fsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.common.dal.timers.WorkerPool;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class FSMTest {
	private WorkerPool workerPool = new WorkerPool("M3UA");

	private volatile boolean timedOut = false;
    private volatile boolean stateEntered = false;
    private volatile boolean stateExited = false;
    private volatile boolean transitionHandlerCalled = false;
    private volatile int timeOutCount = 0;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {

        timedOut = false;
        stateEntered = false;
        stateExited = false;
        transitionHandlerCalled = false;

		workerPool.start(64);
	}

    @After
    public void tearDown() throws Exception {    	
		workerPool.stop();
    }

    @Test
    public void testOnExit() throws Exception {
		FSM fsm = new FSM("test", workerPool.getPeriodicQueue());

        fsm.createState("STATE1").setOnExit(new AsState1Exit(fsm));
        fsm.createState("STATE2");

        fsm.setStart("STATE1");
        fsm.setEnd("STATE2");

        fsm.createTransition("GoToSTATE2", "STATE1", "STATE2");

		workerPool.getPeriodicQueue().store(fsm.getRealTimestamp(), fsm);

        fsm.signal("GoToSTATE2");

        assertTrue(stateExited);
        assertEquals("STATE2", fsm.getState().getName());
    }

    @Test
    public void testTransitionHandler() throws Exception {
		FSM fsm = new FSM("test", workerPool.getPeriodicQueue());

        fsm.createState("STATE1");
        fsm.createState("STATE2");

        fsm.setStart("STATE1");
        fsm.setEnd("STATE2");

        fsm.createTransition("GoToSTATE2", "STATE1", "STATE2").setHandler(new State1ToState2Transition());

		workerPool.getPeriodicQueue().store(fsm.getRealTimestamp(), fsm);

        fsm.signal("GoToSTATE2");

        assertTrue(transitionHandlerCalled);
        assertEquals("STATE2", fsm.getState().getName());
    }

    /**
     * In this test we set TransitionHandler to cancel the transition and yet original timeout is to be respected
     *
     * @throws Exception
     */
    @Test
    public void testNoTransitionHandler() throws Exception {
		FSM fsm = new FSM("test", workerPool.getPeriodicQueue());

        fsm.createState("STATE1");
        fsm.createState("STATE2");

        fsm.setStart("STATE1");
        fsm.setEnd("STATE2");

        // Transition shouldn't happen
        fsm.createTransition("GoToSTATE2", "STATE1", "STATE2").setHandler(new State1ToState2NoTransition());

		workerPool.getPeriodicQueue().store(fsm.getRealTimestamp(), fsm);

        fsm.signal("GoToSTATE2");

        assertTrue(transitionHandlerCalled);
        assertEquals("STATE1", fsm.getState().getName());               
    }

    @Test
    public void testOnEnter() throws Exception {
		FSM fsm = new FSM("test", workerPool.getPeriodicQueue());

        fsm.createState("STATE1");
        fsm.createState("STATE2").setOnEnter(new AsState2Enter(fsm));

        fsm.setStart("STATE1");
        fsm.setEnd("STATE2");

        fsm.createTransition("GoToSTATE2", "STATE1", "STATE2");

		workerPool.getPeriodicQueue().store(fsm.getRealTimestamp(), fsm);

        fsm.signal("GoToSTATE2");

        assertTrue(stateEntered);
        assertEquals("STATE2", fsm.getState().getName());
    }

    @Test
    public void testTimeout() throws Exception {
		FSM fsm = new FSM("test", workerPool.getPeriodicQueue());

        fsm.createState("STATE1");
        fsm.createState("STATE2").setOnTimeOut(new AsState2Timeout(fsm), 2000);

        fsm.setStart("STATE1");
        fsm.setEnd("STATE2");

        fsm.createTransition("GoToSTATE2", "STATE1", "STATE2");

		workerPool.getPeriodicQueue().store(fsm.getRealTimestamp(), fsm);

        fsm.signal("GoToSTATE2");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertTrue(timedOut);
        assertEquals("STATE2", fsm.getState().getName());

    }

    @Test
    public void testTimeoutNoTransition() throws Exception {
		FSM fsm = new FSM("test", workerPool.getPeriodicQueue());

        fsm.createState("STATE1");
        fsm.createState("STATE2").setOnTimeOut(new AsState2Timeout(fsm), 2000);
        fsm.createState("STATE3");

        fsm.setStart("STATE1");
        fsm.setEnd("STATE2");

        fsm.createTransition("GoToSTATE2", "STATE1", "STATE2");
        fsm.createTransition("GoToSTATE3", "STATE2", "STATE3").setHandler(new NoTransition());

		workerPool.getPeriodicQueue().store(fsm.getRealTimestamp(), fsm);

        fsm.signal("GoToSTATE2");
        assertEquals("STATE2", fsm.getState().getName());
        fsm.signal("GoToSTATE3");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertTrue(timedOut);
        assertEquals("STATE2", fsm.getState().getName());

    }

    @Test
    public void testTimeoutTransition() throws Exception {
		FSM fsm = new FSM("test", workerPool.getPeriodicQueue());

        fsm.createState("STATE1");
        fsm.createState("STATE2");
        fsm.createState("STATE3");

        fsm.setStart("STATE1");
        fsm.setEnd("STATE3");

        fsm.createTransition("GoToSTATE2", "STATE1", "STATE2");
        fsm.createTimeoutTransition("STATE2", "STATE2", 1000l).setHandler(new State2TimeoutTransition());

		workerPool.getPeriodicQueue().store(fsm.getRealTimestamp(), fsm);

        fsm.signal("GoToSTATE2");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertTrue((2 <= timeOutCount) && (timeOutCount <= 3));
        assertEquals("STATE2", fsm.getState().getName());

    }

    class AsState1Exit implements FSMStateEventHandler {

        public AsState1Exit(FSM fsm) {
            //this.fsm = fsm;
        }

        @Override
		public void onEvent(FSMState state) {
            stateExited = true;
        }
    }

    class AsState2Timeout implements FSMStateEventHandler {

        public AsState2Timeout(FSM fsm) {
            //this.fsm = fsm;
        }

        @Override
		public void onEvent(FSMState state) {
            timedOut = true;
        }
    }

    class AsState2Enter implements FSMStateEventHandler {

        public AsState2Enter(FSM fsm) {
            //this.fsm = fsm;
        }

        @Override
		public void onEvent(FSMState state) {
            stateEntered = true;
        }
    }

    class State1ToState2Transition implements TransitionHandler {

        @Override
        public boolean process(FSMState state) {
            transitionHandlerCalled = true;
            return true;
        }

    }

    class State2TimeoutTransition implements TransitionHandler {

        @Override
        public boolean process(FSMState state) {
            timeOutCount++;
            return true;
        }

    }

    class State1ToState2NoTransition implements TransitionHandler {

        @Override
        public boolean process(FSMState state) {
            transitionHandlerCalled = true;
            return false;
        }

    }

    class NoTransition implements TransitionHandler {

        @Override
        public boolean process(FSMState state) {
            return false;
        }

    }
}
