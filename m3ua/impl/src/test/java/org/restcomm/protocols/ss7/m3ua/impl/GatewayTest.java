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

package org.restcomm.protocols.ss7.m3ua.impl;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.api.IpChannelType;
import org.restcomm.protocols.api.Management;
import org.restcomm.protocols.sctp.SctpManagementImpl;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartListener;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.common.dal.timers.WorkerPool;
import com.mobius.software.telco.protocols.ss7.common.UUIDGenerator;
import com.sun.nio.sctp.SctpChannel;

import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@SuppressWarnings("restriction")
public class GatewayTest {
	private static final Logger logger = LogManager.getLogger(GatewayTest.class);

	private static final String SERVER_NAME = "testserver";
	private static final String SERVER_HOST = "127.0.0.1";
	private static final int SERVER_PORT = 2365;

	private static final String SERVER_ASSOCIATION_NAME = "serverAsscoiation";
	private static final String CLIENT_ASSOCIATION_NAME = "clientAsscoiation";

	private static final String CLIENT_HOST = "127.0.0.1";
	private static final int CLIENT_PORT = 2366;

	private static final int CONNECT_DELLAY = 5 * 1000;

	private static final ParameterFactoryImpl paramFactory = new ParameterFactoryImpl();

	private WorkerPool workerPool;

	private Management sctpManagement = null;
	private M3UAManagementImpl m3uaMgmt = null;

	private AsImpl remAs;
	private AspImpl remAsp;

	private AsImpl localAs;
	private AspImpl localAsp;

	private Server server;
	private Client client;

	private Mtp3UserPartListenerImpl mtp3UserPartListener = null;

	@Before
	public void setUp() throws Exception {
		this.workerPool = new WorkerPool();
		this.workerPool.start(4);

		this.mtp3UserPartListener = new Mtp3UserPartListenerImpl();

		this.client = new Client();
		this.server = new Server();

		this.sctpManagement = new SctpManagementImpl("GatewayTest", 4, 4, 4);
		this.sctpManagement.start();
		this.sctpManagement.setConnectDelay(CONNECT_DELLAY);
		this.sctpManagement.removeAllResourses();

		UUIDGenerator uuidGenerator = new UUIDGenerator(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });

		this.m3uaMgmt = new M3UAManagementImpl("GatewayTest", null, uuidGenerator, workerPool);
		this.m3uaMgmt.setTransportManagement(this.sctpManagement);
		this.m3uaMgmt.addMtp3UserPartListener(mtp3UserPartListener);
		this.m3uaMgmt.start();
		this.m3uaMgmt.removeAllResourses();
	}

	@After
	public void tearDown() throws Exception {
		this.sctpManagement.stop();
		this.m3uaMgmt.stop();
		this.workerPool.stop();
	}

	@Test
	public void testSingleAspInAs() throws Exception {
		// 5.1.1. Single ASP in an Application Server ("1+0" sparing),
		logger.debug("Starting server");
		server.start();
		Thread.sleep(100);

		logger.debug("Starting client");
		client.start();

		Thread.sleep(10000);

		// Both AS and ASP should be ACTIVE now
		AspState.getState(remAsp.getPeerFSM().getState().getName());
		assertEquals(AspState.getState(remAsp.getPeerFSM().getState().getName()), AspState.ACTIVE);
		assertEquals(AsState.getState(remAs.getLocalFSM().getState().getName()), AsState.ACTIVE);

		assertEquals(AspState.getState(localAsp.getLocalFSM().getState().getName()), AspState.ACTIVE);
		assertEquals(AsState.getState(localAs.getPeerFSM().getState().getName()), AsState.ACTIVE);

		client.sendPayload();
		server.sendPayload();

		Thread.sleep(100);

		client.stop();
		logger.debug("Stopped Client");
		// Give time to exchnge ASP_DOWN messages
		Thread.sleep(100);

		// The AS is Pending
		assertEquals(AsState.getState(localAs.getPeerFSM().getState().getName()), AsState.PENDING);
		assertEquals(AsState.getState(remAs.getLocalFSM().getState().getName()), AsState.PENDING);

		// Let the AS go in DOWN state
		Thread.sleep(4000);
		logger.debug("Woke from 4000 sleep");

		// The AS is Pending
		assertEquals(AsState.getState(localAs.getPeerFSM().getState().getName()), AsState.DOWN);
		assertEquals(AsState.getState(remAs.getLocalFSM().getState().getName()), AsState.DOWN);

		client.stopClient();
		server.stop();

		Thread.sleep(100);

		// we should receive two MTP3 data
		assertEquals(mtp3UserPartListener.getReceivedData().size(), 2);
	}

	// retunrs true if sctp is supported by this OS and false in not
	public static boolean checkSctpEnabled() {
		try {
			SctpChannel socketChannel = SctpChannel.open();
			socketChannel.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private class Client {
		public void start() throws Exception {
			IpChannelType ipChannelType = IpChannelType.TCP;
			if (checkSctpEnabled())
				ipChannelType = IpChannelType.SCTP;

			// 1. Create SCTP Association
			sctpManagement.addAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_HOST, SERVER_PORT, CLIENT_ASSOCIATION_NAME,
					ipChannelType, null);

			// 2. Create AS
			// m3ua as create rc <rc> <ras-name>
			RoutingContext rc = paramFactory.createRoutingContext(new long[] { 100l });
			TrafficModeType trafficModeType = paramFactory.createTrafficModeType(TrafficModeType.Loadshare);
			localAs = (AsImpl) m3uaMgmt.createAs("client-testas", Functionality.AS, ExchangeType.SE, IPSPType.CLIENT,
					rc, trafficModeType, 1, null);

			// 3. Create ASP
			// m3ua asp create ip <local-ip> port <local-port> remip <remip>
			// remport <remport> <asp-name>
			m3uaMgmt.createAspFactory("client-testasp", CLIENT_ASSOCIATION_NAME, false);

			// 4. Assign ASP to AS
			localAsp = m3uaMgmt.assignAspToAs("client-testas", "client-testasp");

			// 5. Define Route
			m3uaMgmt.addRoute(1408, -1, -1, "client-testas");

			// 6. Start ASP
			m3uaMgmt.startAsp("client-testasp");

		}

		public void stop() throws Exception {
			// 1. stop ASP
			m3uaMgmt.stopAsp("client-testasp");

		}

		public void stopClient() throws Exception {
			// 2.Remove route
			m3uaMgmt.removeRoute(1408, -1, -1, "client-testas");

			// 3. Unassign ASP from AS
			// clientM3UAMgmt.
			m3uaMgmt.unassignAspFromAs("client-testas", "client-testasp");

			// 4. destroy aspFactory
			m3uaMgmt.destroyAspFactory("client-testasp");

			// 5. Destroy As
			m3uaMgmt.destroyAs("client-testas");

			// 6. remove sctp
			sctpManagement.removeAssociation(CLIENT_ASSOCIATION_NAME);
		}

		public void sendPayload() throws Exception {
			Mtp3TransferPrimitiveFactory factory = m3uaMgmt.getMtp3TransferPrimitiveFactory();
			Mtp3TransferPrimitive mtp3TransferPrimitive = factory.createMtp3TransferPrimitive(3, 1, 0, 123, 1408, 1,
					Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4 }));

			Semaphore sendSemaphore = new Semaphore(0);
			m3uaMgmt.sendMessage(mtp3TransferPrimitive, new TaskCallback<Exception>() {
				@Override
				public void onSuccess() {
					sendSemaphore.release();
				}

				@Override
				public void onError(Exception exception) {
					logger.error(exception);
				}
			});

			sendSemaphore.acquire();
		}
	}

	private class Server {
		private void start() throws Exception {
			IpChannelType ipChannelType = IpChannelType.TCP;
			if (checkSctpEnabled())
				ipChannelType = IpChannelType.SCTP;

			// 1. Create SCTP Server
			sctpManagement.addServer(SERVER_NAME, SERVER_HOST, SERVER_PORT, ipChannelType, null);

			// 2. Create SCTP Server Association
			sctpManagement.addServerAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME,
					ipChannelType);

			// 3. Start Server
			sctpManagement.startServer(SERVER_NAME);

			// 4. Create RAS
			// m3ua ras create rc <rc> rk dpc <dpc> opc <opc-list> si <si-list>
			// traffic-mode {broadcast|loadshare|override} <ras-name>
			RoutingContext rc = paramFactory.createRoutingContext(new long[] { 100l });
			TrafficModeType trafficModeType = paramFactory.createTrafficModeType(TrafficModeType.Loadshare);
			remAs = (AsImpl) m3uaMgmt.createAs("server-testas", Functionality.SGW, ExchangeType.SE, IPSPType.CLIENT, rc,
					trafficModeType, 1, null);

			// 5. Create RASP
			// m3ua rasp create <asp-name> <assoc-name>"
			m3uaMgmt.createAspFactory("server-testasp", SERVER_ASSOCIATION_NAME, false);

			// 6. Assign ASP to AS
			remAsp = m3uaMgmt.assignAspToAs("server-testas", "server-testasp");

			// 5. Define Route
			// Define Route
			m3uaMgmt.addRoute(123, -1, -1, "server-testas");

			// 7. Start ASP
			m3uaMgmt.startAsp("server-testasp");
		}

		public void stop() throws Exception {
			m3uaMgmt.stopAsp("server-testasp");

			// 2.Remove route
			m3uaMgmt.removeRoute(123, -1, -1, "server-testas");

			m3uaMgmt.unassignAspFromAs("server-testas", "server-testasp");

			// 4. destroy aspFactory
			m3uaMgmt.destroyAspFactory("server-testasp");

			// 5. Destroy As
			m3uaMgmt.destroyAs("server-testas");

			sctpManagement.removeAssociation(SERVER_ASSOCIATION_NAME);

			sctpManagement.stopServer(SERVER_NAME);
			sctpManagement.removeServer(SERVER_NAME);
		}

		public void sendPayload() throws Exception {
			Mtp3TransferPrimitiveFactory factory = m3uaMgmt.getMtp3TransferPrimitiveFactory();
			Mtp3TransferPrimitive mtp3TransferPrimitive = factory.createMtp3TransferPrimitive(3, 1, 0, 1408, 123, 1,
					Unpooled.wrappedBuffer(new byte[] { 1, 2, 3, 4 }));

			Semaphore sendSemaphore = new Semaphore(0);
			m3uaMgmt.sendMessage(mtp3TransferPrimitive, new TaskCallback<Exception>() {
				@Override
				public void onSuccess() {
					sendSemaphore.release();
				}

				@Override
				public void onError(Exception exception) {
					logger.error(exception);
					sendSemaphore.release();
				}
			});

			sendSemaphore.acquire();
		}
	}

	private class Mtp3UserPartListenerImpl implements Mtp3UserPartListener {
		private ConcurrentLinkedQueue<Mtp3TransferPrimitive> receivedData = new ConcurrentLinkedQueue<Mtp3TransferPrimitive>();

		public ConcurrentLinkedQueue<Mtp3TransferPrimitive> getReceivedData() {
			return receivedData;
		}

		@Override
		public void onMtp3PauseMessage(Mtp3PausePrimitive pauseMessage) {
		}

		@Override
		public void onMtp3ResumeMessage(Mtp3ResumePrimitive resumeMessage) {
		}

		@Override
		public void onMtp3StatusMessage(Mtp3StatusPrimitive statusMessage) {
		}

		@Override
		public void onMtp3TransferMessage(Mtp3TransferPrimitive transferMessage) {
			receivedData.offer(transferMessage);
		}

		@Override
		public void onMtp3EndCongestionMessage(Mtp3EndCongestionPrimitive endCongestionMessage) {
		}
	}
}
