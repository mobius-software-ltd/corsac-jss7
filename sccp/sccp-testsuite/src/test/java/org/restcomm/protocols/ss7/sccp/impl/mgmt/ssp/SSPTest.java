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

package org.restcomm.protocols.ss7.sccp.impl.mgmt.ssp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.RemoteSubSystemImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.restcomm.protocols.ss7.sccp.impl.User;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.Mtp3PrimitiveMessage;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.Mtp3PrimitiveMessageType;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.SccpMgmtMessage;
import org.restcomm.protocols.ss7.sccp.impl.mgmt.SccpMgmtMessageType;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * Test condition when SSN is not available in one stack aka prohibited
 *
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public class SSPTest extends SccpHarness {
	private SccpAddress a1, a2;

	@Before
	public void setUpClass() throws Exception {
		this.sccpStack1Name = "sspTestSccpStack1";
		this.sccpStack2Name = "sspTestSccpStack2";
	}

	@Override
	protected void createStack1() {
		sccpStack1 = createStack(sccpStack1Name);
		sccpProvider1 = sccpStack1.getSccpProvider();
	}

	@Override
	protected void createStack2() {
		sccpStack2 = createStack(sccpStack2Name);
		sccpProvider2 = sccpStack2.getSccpProvider();
	}

	@Override
	protected SccpStackImpl createStack(String name) {
		SccpStackImpl stack = new SccpStackImplProxy(name);
		return stack;
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	@After
	public void tearDown() {
		super.tearDown();
	}

	/**
	 * Test of configure method, of class SccpStackImpl.
	 */
	@Test
	public void testRemoteRoutingBasedOnSsn() throws Exception {
		a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
				getStack1PC(), 8);
		a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
				getStack2PC(), 8);

		User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
		User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

		sccpStack1.setSstTimerDuration_Min(5000);
		sccpStack1.setSstTimerDuration_IncreaseFactor(1);

		u1.register();
		// u2.register();
		// this will cause: u1 stack will receive SSP, u2 stack will get SST and
		// message.

		super.sentMessages.set(0);

		TaskCallback<Exception> callback = super.getTaskCallback(2);
		u1.send(callback);
		u2.send(callback);

		super.sendSemaphore.acquire();

		Thread.sleep(PROCESSING_TIMEOUT);

		assertTrue(u1.getMessages().size() == 1);
		assertTrue(u1.check());
		assertTrue(u2.getMessages().size() == 0);

		// now lets check functional.mgmt part

		SccpStackImplProxy stack = (SccpStackImplProxy) sccpStack1;

		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1);
		SccpMgmtMessage rmsg1_ssp = stack.getManagementProxy().getMgmtMessages().get(0);
		SccpMgmtMessage emsg1_ssp = new SccpMgmtMessage(0, SccpMgmtMessageType.SSP.getType(), getSSN(), 2, 0);
		assertEquals(rmsg1_ssp, emsg1_ssp);

		// check if there is no SST
		stack = (SccpStackImplProxy) sccpStack2;

		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0);

		Thread.sleep(6000);

		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1);
		SccpMgmtMessage rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
		SccpMgmtMessage emsg2_sst = new SccpMgmtMessage(0, SccpMgmtMessageType.SST.getType(), getSSN(), 2, 0);
		assertEquals(rmsg2_sst, emsg2_sst);

		assertTrue(rmsg2_sst.getTstamp() >= rmsg1_ssp.getTstamp());

		// register;
		u2.register();
		Thread.sleep(5000);
		stack = (SccpStackImplProxy) sccpStack1;
		// double check first message.
		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 2);
		rmsg1_ssp = stack.getManagementProxy().getMgmtMessages().get(0);
		emsg1_ssp = new SccpMgmtMessage(0, SccpMgmtMessageType.SSP.getType(), getSSN(), 2, 0);
		assertEquals(rmsg1_ssp, emsg1_ssp);

		// now second message MUST be SSA here
		SccpMgmtMessage rmsg1_ssa = stack.getManagementProxy().getMgmtMessages().get(1);
		SccpMgmtMessage emsg1_ssa = new SccpMgmtMessage(1, SccpMgmtMessageType.SSA.getType(), getSSN(), 2, 0);

		assertEquals(rmsg1_ssa, emsg1_ssa);

		// now lets check other one
		// check if there is no SST
		stack = (SccpStackImplProxy) sccpStack2;

		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 2);
		rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
		emsg2_sst = new SccpMgmtMessage(0, SccpMgmtMessageType.SST.getType(), getSSN(), 2, 0);
		assertEquals(rmsg2_sst, emsg2_sst);

		rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(1);
		emsg2_sst = new SccpMgmtMessage(1, SccpMgmtMessageType.SST.getType(), getSSN(), 2, 0);
		assertEquals(rmsg2_sst, emsg2_sst);
		assertTrue(rmsg2_sst.getTstamp() >= rmsg1_ssp.getTstamp());

		// now lets wait and check if there is nothing more
		Thread.sleep(5000);
		stack = (SccpStackImplProxy) sccpStack1;
		// double check first message.
		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 2);

		stack = (SccpStackImplProxy) sccpStack2;

		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 2);

		// try to send;

		super.sentMessages.set(0);
		u1.send(super.getTaskCallback(1));
		super.sendSemaphore.acquire();

		Thread.sleep(PROCESSING_TIMEOUT);

		assertTrue(u1.getMessages().size() == 1);
		assertTrue(u1.check());
		assertTrue(u2.getMessages().size() == 1);

		// TODO: should we check flags in MgmtProxies.
	}

	/**
	 * At first the SSN is not available and henvce U1 should receive SSP. After
	 * that MTP3Pause recevied for peer(u2, pc2). The resume and all should work
	 * again
	 */
	@Test
	public void testRemoteRoutingBasedOnSsn1() throws Exception {
		a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
				getStack1PC(), 8);
		a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
				getStack2PC(), 8);

		User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
		User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

		sccpStack1.setSstTimerDuration_Min(5000);
		sccpStack1.setSstTimerDuration_IncreaseFactor(1);

		u1.register();
		// u2.register();
		// this will cause: u1 stack will receive SSP, u2 stack will get SST and
		// message.

		super.sentMessages.set(0);
		TaskCallback<Exception> callback = super.getTaskCallback(2);
		u1.send(callback);
		u2.send(callback);
		super.sendSemaphore.acquire();

		Thread.sleep(PROCESSING_TIMEOUT);

		assertTrue(u1.getMessages().size() == 1);
		assertTrue(u1.check());
		assertTrue(u2.getMessages().size() == 0);

		// now lets check mgmt part

		SccpStackImplProxy stack = (SccpStackImplProxy) sccpStack1;

		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1);
		SccpMgmtMessage rmsg1_ssp = stack.getManagementProxy().getMgmtMessages().get(0);
		SccpMgmtMessage emsg1_ssp = new SccpMgmtMessage(0, SccpMgmtMessageType.SSP.getType(), getSSN(), 2, 0);
		assertEquals(rmsg1_ssp, emsg1_ssp);

		// check if there is no SST
		stack = (SccpStackImplProxy) sccpStack2;

		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 0);

		Thread.sleep(6000);

		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1);
		SccpMgmtMessage rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
		SccpMgmtMessage emsg2_sst = new SccpMgmtMessage(0, SccpMgmtMessageType.SST.getType(), getSSN(), 2, 0);
		assertEquals(rmsg2_sst, emsg2_sst);

		assertTrue(rmsg2_sst.getTstamp() >= rmsg1_ssp.getTstamp());

		// super.data1.add(createPausePrimitive(getStack2PC()));

		this.sentMessages.set(0);
		this.mtp3UserPart1.sendPauseMessageToLocalUser(getStack2PC(), super.getTaskCallback(1));
		this.sendSemaphore.acquire();

		// register;
		u2.register();
		Thread.sleep(5000);
		stack = (SccpStackImplProxy) sccpStack1;
		// double check first message.
		assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 1);
		assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 1);
		rmsg1_ssp = stack.getManagementProxy().getMgmtMessages().get(0);
		emsg1_ssp = new SccpMgmtMessage(0, SccpMgmtMessageType.SSP.getType(), getSSN(), 2, 0);
		assertEquals(rmsg1_ssp, emsg1_ssp);

		// //now second message MUST be SSA here
		// SccpMgmtMessage rmsg1_ssa =
		// stack.getManagementProxy().getMgmtMessages().get(1);
		// SccpMgmtMessage emsg1_ssa = new
		// SccpMgmtMessage(1,SccpMgmtMessageType.SSA.getType(), getSSN(), 2, 0);
		//
		// assertEquals( rmsg1_ssa,emsg1_ssa,"Failed to match management message in
		// U1");

		// //now lets check other one
		// //check if there is no SST
		// stack = (SccpStackImplProxy) sccpStack2;
		//
		// assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, it
		// should not!","U2 received Mtp3 Primitve);
		// assertTrue(stack.getManagementProxy().getMgmtMessages().size() == 2, it
		// should !","U2 did not receive Management
		// message);
		// rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(0);
		// emsg2_sst = new SccpMgmtMessage(0,SccpMgmtMessageType.SST.getType(),
		// getSSN(), 2, 0);
		// assertEquals( rmsg2_sst,emsg2_sst,"Failed to match management message in
		// U2");
		//
		// rmsg2_sst = stack.getManagementProxy().getMgmtMessages().get(1);
		// emsg2_sst = new SccpMgmtMessage(1,SccpMgmtMessageType.SST.getType(),
		// getSSN(), 2, 0);
		// assertEquals( rmsg2_sst,emsg2_sst,"Failed to match management message in
		// U2");
		// assertTrue(rmsg2_sst.getTstamp()>=rmsg1_ssp.getTstamp(), SST received before
		// SSP.","Out of sync messages);
		//
		// //now lets wait and check if there is nothing more
		// Thread.currentThread().sleep(12000);
		// stack = (SccpStackImplProxy) sccpStack1;
		// //double check first message.
		// assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, it
		// should not!","U1 received Mtp3 Primitve);
		// assertTrue(stack.getManagementProxy().getMgmtMessages().size() ==
		// 2,"U1 received more mgmt messages than it should !");
		//
		// stack = (SccpStackImplProxy) sccpStack2;
		//
		// assertTrue(stack.getManagementProxy().getMtp3Messages().size() == 0, it
		// should not!","U2 received Mtp3 Primitve);
		// assertTrue(stack.getManagementProxy().getMgmtMessages().size() ==
		// 2,"U2 received more mgmt messages than it should!");
		//
		// //try to send;
		//
		// u1.send();
		//
		// Thread.currentThread().sleep(1000);
		//
		// assertTrue( u1.getMessages().size() == 1, it should!","U1 did not receiv
		// message);
		// assertTrue( u1.check(),"Inproper message not received!");
		// assertTrue( u2.getMessages().size() == 1, it should!","U2 did not receiv
		// message);

		// TODO: should we check flags in MgmtProxies.

	}

	/**
	 * Test of configure method, of class SccpStackImpl.
	 */
	@Test
	public void RecdMsgForProhibitedSsnTest() throws Exception {
		a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
				getStack1PC(), 8);
		a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
				getStack2PC(), 8);

		User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());

		sccpStack1.setSstTimerDuration_Min(5000);
		sccpStack1.setSstTimerDuration_IncreaseFactor(1);

		u1.register();
		// u2.register();
		// this will cause: u1 stack will receive SSP, u2 stack will get SST and
		// message.
		Thread.sleep(100);

		RemoteSubSystemImpl rss = (RemoteSubSystemImpl) sccpStack1.getSccpResource().getRemoteSsn(1);
		super.sentMessages.set(0);
		u1.send(super.getTaskCallback(1));
		super.sendSemaphore.acquire();

		Thread.sleep(PROCESSING_TIMEOUT);

		assertEquals(((SccpStackImplProxy) sccpStack1).getManagementProxy().getMgmtMessages().size(), 1);
		rss.setRemoteSsnProhibited(false);
		super.sentMessages.set(0);
		u1.send(super.getTaskCallback(1));
		super.sendSemaphore.acquire();
		// we do not send SSP during a second after sending
		assertEquals(((SccpStackImplProxy) sccpStack1).getManagementProxy().getMgmtMessages().size(), 1);

		Thread.sleep(2000);
		rss.setRemoteSsnProhibited(false);
		super.sentMessages.set(0);
		u1.send(super.getTaskCallback(1));
		super.sendSemaphore.acquire();

		Thread.sleep(PROCESSING_TIMEOUT);

		assertEquals(((SccpStackImplProxy) sccpStack1).getManagementProxy().getMgmtMessages().size(), 2);
	}

	/**
	 * Test of configure method, of class SccpStackImpl.
	 */
	@Test
	public void ConsernedSpcTest() throws Exception {
		a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
				getStack1PC(), 8);
		a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
				getStack2PC(), 8);

		User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());

		sccpStack1.setSstTimerDuration_Min(5000);
		sccpStack1.setSstTimerDuration_IncreaseFactor(1);

		sccpStack1.getSccpResource().addConcernedSpc(1, getStack2PC());

		Thread.sleep(100);

		assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().size(), 0);

		u1.register();
		Thread.sleep(PROCESSING_TIMEOUT);

		assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().size(), 1);
		assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().get(0).getType(),
				SccpMgmtMessageType.SSA);

		u1.deregister();
		Thread.sleep(PROCESSING_TIMEOUT);

		assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().size(), 2);
		assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().get(1).getType(),
				SccpMgmtMessageType.SSP);

		// Now test when the MTP3Pause's and then Resume's, SSA should be sent

		u1.register();
		Thread.sleep(PROCESSING_TIMEOUT);

		assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().size(), 3);
		assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().get(2).getType(),
				SccpMgmtMessageType.SSA);

		// Pause Stack2PC
		this.sentMessages.set(0);
		this.mtp3UserPart1.sendPauseMessageToLocalUser(getStack2PC(), this.getTaskCallback(1));
		this.sendSemaphore.acquire();

		Thread.sleep(PROCESSING_TIMEOUT);

		assertTrue(((SccpStackImplProxy) sccpStack1).getManagementProxy().getMtp3Messages().size() == 1);
		Mtp3PrimitiveMessage rmtpPause = ((SccpStackImplProxy) sccpStack1).getManagementProxy().getMtp3Messages()
				.get(0);
		Mtp3PrimitiveMessage emtpPause = new Mtp3PrimitiveMessage(0, Mtp3PrimitiveMessageType.MTP3_PAUSE,
				getStack2PC());
		assertEquals(rmtpPause, emtpPause);

		// Resume Stack2PC
		this.sentMessages.set(0);
		this.mtp3UserPart1.sendResumeMessageToLocalUser(getStack2PC(), this.getTaskCallback(1));
		this.sendSemaphore.acquire();

		Thread.sleep(PROCESSING_TIMEOUT);

		assertTrue(((SccpStackImplProxy) sccpStack1).getManagementProxy().getMtp3Messages().size() == 2);
		rmtpPause = ((SccpStackImplProxy) sccpStack1).getManagementProxy().getMtp3Messages().get(1);
		emtpPause = new Mtp3PrimitiveMessage(1, Mtp3PrimitiveMessageType.MTP3_RESUME, getStack2PC());
		assertEquals(rmtpPause, emtpPause);

		// And stack2 should receive SSA
		assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().size(), 4);
		assertEquals(((SccpStackImplProxy) sccpStack2).getManagementProxy().getMgmtMessages().get(3).getType(),
				SccpMgmtMessageType.SSA);

	}

	protected static byte[] createPausePrimitive(int pc) throws Exception {
		byte[] b = new byte[] { 0, (byte) (Mtp3PrimitiveMessageType.MTP3_PAUSE.getType() & 0x00FF),
				(byte) (pc >> 24 & 0xFF), (byte) (pc >> 16 & 0xFF), (byte) (pc >> 8 & 0xFF), (byte) (pc & 0xFF) };
		return b;
	}

}
