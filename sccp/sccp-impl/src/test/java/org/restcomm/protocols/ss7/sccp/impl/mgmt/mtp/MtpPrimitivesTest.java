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

package org.restcomm.protocols.ss7.sccp.impl.mgmt.mtp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusCause;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.restcomm.protocols.ss7.sccp.impl.User;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.Mtp3CongestionType;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.Mtp3PrimitiveMessage;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.Mtp3PrimitiveMessageType;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.Mtp3StatusType;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.Mtp3UnavailabiltyCauseType;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.SccpMgmtMessage;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.SccpMgmtMessageType;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Test condition when SSN is not available in one stack aka prohibited
 *
 * @author baranowb
 * @author yulianoifa
 */
public class MtpPrimitivesTest extends SccpHarness {

    private SccpAddress a1, a2;

    public MtpPrimitivesTest() {
    }

    @Before
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "MtpPrimitivesTestSccpStack1";
        this.sccpStack2Name = "MtpPrimitivesTestSccpStack2";
    }

    @After
    public void tearDownClass() throws Exception {
    }

    protected void createStack1() {
        sccpStack1 = createStack(sccpStack1Name);
        sccpProvider1 = sccpStack1.getSccpProvider();
    }

    protected void createStack2() {
        sccpStack2 = createStack(sccpStack2Name);
        sccpProvider2 = sccpStack2.getSccpProvider();
    }

    @Override
    protected SccpStackImpl createStack(String name) {
        SccpStackImpl stack = new SccpStackImplProxy(name);
        return stack;
    }

//    @Before
//    public void setUp() throws Exception {
//        super.setUp();
//    }
//
//    @After
//    public void tearDown() {
//        super.tearDown();
//    }

    /**
     * Test of configure method, of class SccpStackImpl.
     */
    @Test
    public void testPauseAndResume() throws Exception {
        super.setUp();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        // register, to SSNs are up.
        u1.register();
        u2.register();

        // now, other tests check bidirectional com, it works.
        // now on one side we will inject pause, try to send message, check on other side
        // inject resume, send message and check on other side.

        // super.data1.add(createPausePrimitive(getStack2PC()));
        this.mtp3UserPart1.sendPauseMessageToLocalUser(getStack2PC());

        Thread.sleep(500);
        // now s1 thinks s2 is not available
        assertTrue(u1.getMessages().size() == 0);
        assertTrue(u2.getMessages().size() == 0);

        // lets check stack functional.mgmt messages

        SccpStackImplProxy stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 1);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0);
        Mtp3PrimitiveMessage rmtpPause = stack.getManagementProxy().getMtp3Messages().get(0);
        Mtp3PrimitiveMessage emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_PAUSE, 2);
        assertEquals(rmtpPause, emtpPause);

        // check if there is no SST
        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0);

        // now send msg from s1
        u1.send();

        Thread.sleep(500);

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0);
        assertTrue(u2.getMessages().size() == 0);

        // noooow lets inject mtp3_resume and retry
        // super.data1.add(createResumePrimitive(getStack2PC()));
        this.mtp3UserPart1.sendResumeMessageToLocalUser(getStack2PC());

        Thread.sleep(500);
        stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 2);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0);
        rmtpPause = stack.getManagementProxy().getMtp3Messages().get(0);
        emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_PAUSE, 2);
        assertEquals(rmtpPause, emtpPause);

        Mtp3PrimitiveMessage rmtpResume = stack.getManagementProxy().getMtp3Messages().get(1);
        Mtp3PrimitiveMessage emtpResume = new Mtp3PrimitiveMessage(1, Mtp3PrimitiveMessageType.MTP3_RESUME, 2);
        assertEquals(rmtpResume, emtpResume);

        u1.send();

        Thread.sleep(500);
        stack = (SccpStackImplProxy) sccpStack2;
        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0);
        assertTrue(u2.getMessages().size() == 1);
        assertTrue(u2.check());

        super.tearDown();
    }

    @Test
    public void testStatus_1() throws Exception {
        super.setUp();

        doTestStatus(Mtp3UnavailabiltyCauseType.CAUSE_INACCESSIBLE);

        super.tearDown();
    }

    @Test
    public void testStatus_2() throws Exception {
        super.setUp();

        doTestStatus(Mtp3UnavailabiltyCauseType.CAUSE_UNKNOWN);

        super.tearDown();
    }

    @Test
    public void testStatus_3() throws Exception {
        super.setUp();
        sccpStack1.setSccpProtocolVersion(SccpProtocolVersion.ANSI);
        sccpStack2.setSccpProtocolVersion(SccpProtocolVersion.ANSI);
        super.tearDown();
        super.setUp();

        doTestStatus(Mtp3UnavailabiltyCauseType.CAUSE_INACCESSIBLE);

        sccpStack1.setSccpProtocolVersion(SccpProtocolVersion.ITU);
        sccpStack2.setSccpProtocolVersion(SccpProtocolVersion.ITU);

        super.tearDown();
    }

    protected void doTestStatus(Mtp3UnavailabiltyCauseType type) throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        // register, to SSNs are up.
        u1.register();
        u2.register();

        // now, other tests check bidirectional com, it works.
        // now on one side we will inject pause, try to send message, check on other side
        // inject resume, send message and check on other side.

        // super.data1.add(createStatusPrimitive(getStack2PC(),Mtp3StatusType.RemoteUserUnavailable,Mtp3CongestionType.NULL,Mtp3UnavailabiltyCauseType.CAUSE_UNEQUIPED));
        this.mtp3UserPart1.sendStatusMessageToLocalUser(getStack2PC(),
                Mtp3StatusCause.UserPartUnavailability_UnequippedRemoteUser, 0, 0);

        Thread.sleep(500);
        // now s1 thinks s2 is not available
        assertTrue(u1.getMessages().size() == 0);
        assertTrue(u2.getMessages().size() == 0);

        // lets check stack functional.mgmt messages

        SccpStackImplProxy stack = (SccpStackImplProxy) sccpStack1;
        
        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 1);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0);
        Mtp3PrimitiveMessage rmtpPause = stack.getManagementProxy().getMtp3Messages().get(0);
        Mtp3PrimitiveMessage emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_STATUS, getStack2PC(),
                Mtp3StatusType.RemoteUserUnavailable, Mtp3CongestionType.NULL, Mtp3UnavailabiltyCauseType.CAUSE_UNEQUIPED);
        assertEquals(rmtpPause, emtpPause);

        // check if there is no SST
        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0);

        // now send msg from s1
        u1.send();

        Thread.sleep(500);

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0);
        assertTrue(u2.getMessages().size() == 0);

        // //noooow lets inject another status, this will enable SST/SSA
        // super.data1.add(createStatusPrimitive(getStack2PC(), Mtp3StatusType.RemoteUserUnavailable, Mtp3CongestionType.NULL,
        // type));
        Mtp3StatusCause cs = Mtp3StatusCause.UserPartUnavailability_Unknown;
        switch (type) {
            case CAUSE_INACCESSIBLE:
                cs = Mtp3StatusCause.UserPartUnavailability_InaccessibleRemoteUser;
                break;
            case CAUSE_UNEQUIPED:
                cs = Mtp3StatusCause.UserPartUnavailability_UnequippedRemoteUser;
                break;
			default:
				break;
        }
        this.mtp3UserPart1.sendStatusMessageToLocalUser(getStack2PC(), cs, 0, 0);

        Thread.sleep(15000); // 15000
        stack = (SccpStackImplProxy) sccpStack1;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 2);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1);
        rmtpPause = stack.getManagementProxy().getMtp3Messages().get(0);
        emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_STATUS, getStack2PC(), Mtp3StatusType.RemoteUserUnavailable,
                Mtp3CongestionType.NULL, Mtp3UnavailabiltyCauseType.CAUSE_UNEQUIPED);
        assertEquals(rmtpPause, emtpPause);

        rmtpPause = stack.getManagementProxy().getMtp3Messages().get(1);
        emtpPause = new Mtp3PrimitiveMessage(1, Mtp3PrimitiveMessageType.MTP3_STATUS, getStack2PC(), Mtp3StatusType.RemoteUserUnavailable,
                Mtp3CongestionType.NULL, type);
        assertEquals(rmtpPause, emtpPause);

        // check for SSA - note SSN is set to 1, since its whole peer
        // now second message MUST be SSA here
        SccpMgmtMessage rmsg1_ssa = stack.getManagementProxy().getMgmtMessages().get(0);
        SccpMgmtMessage emsg1_ssa = new SccpMgmtMessage(2, SccpMgmtMessageType.SSA.getType(), 1, getStack2PC(), 0);

        assertEquals(rmsg1_ssa, emsg1_ssa);

        // check other Stack for SST
        // check if there is no SST
        stack = (SccpStackImplProxy) sccpStack2;

        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1);
        SccpMgmtMessage rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
        SccpMgmtMessage emsg2_sst = new SccpMgmtMessage(0, SccpMgmtMessageType.SST.getType(), 1, getStack2PC(), 0);
        assertEquals(rmsg2_sst, emsg2_sst);

        u1.send();

        Thread.sleep(500);
        assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
        assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1);
        assertTrue(u2.getMessages().size() == 1);
        assertTrue(u2.check());
    }

}
