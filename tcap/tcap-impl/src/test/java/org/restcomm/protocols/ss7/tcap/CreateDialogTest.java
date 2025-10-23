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
import static org.junit.Assert.fail;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.sccp.MaxConnectionCountReached;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SccpManagementEventListener;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpStack;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.MessageFactory;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.wrappers.TCAPStackImplWrapper;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.common.dal.timers.WorkerPool;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CreateDialogTest {
	private SccpHarnessPreview sccpProv = new SccpHarnessPreview();
	private TCAPStackImplWrapper tcapStack1;
	private WorkerPool workerPool;

	@Before
	public void setUp() throws Exception {
		workerPool = new WorkerPool("TCAP");
		workerPool.start(4);

		tcapStack1 = new TCAPStackImplWrapper(this.sccpProv, 8, "CreateDialogTest", workerPool);
		tcapStack1.start();
	}

	@After
	public void tearDown() {
		if (tcapStack1 != null) {
			tcapStack1.stop();
			tcapStack1 = null;
		}

		if (workerPool != null) {
			workerPool.stop();
			workerPool = null;
		}
	}

	@Test
	public void createDialogTest() throws Exception {
		SccpAddress localAddress = new SccpAddressImpl();
		SccpAddress remoteAddress = new SccpAddressImpl();

		Dialog dlg1 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress, 0);
		assertEquals((long) dlg1.getLocalDialogId(), 1L);

		try {
			this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress, 1L, 0);
			fail("Must be failure because dialogID==1 is busy");
		} catch (Exception e) {
		}

		Dialog dlg3 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress, 2L, 0);
		assertEquals((long) dlg3.getLocalDialogId(), 2L);

		Dialog dlg4 = this.tcapStack1.getProvider().getNewDialog(localAddress, remoteAddress, 0);
		assertEquals((long) dlg4.getLocalDialogId(), 3L);
	}

	private class SccpHarnessPreview implements SccpProvider {
		private static final long serialVersionUID = 1L;

		@Override
		public void deregisterSccpListener(int arg0) {
		}

		@Override
		public int getMaxUserDataLength(SccpAddress arg0, SccpAddress arg1, int networkId) {
			return 0;
		}

		@Override
		public MessageFactory getMessageFactory() {
			return null;
		}

		@Override
		public ParameterFactory getParameterFactory() {
			return null;
		}

		@Override
		public void registerSccpListener(int arg0, SccpListener listener) {
		}

		@Override
		public void send(SccpDataMessage msg, TaskCallback<Exception> callback) {
			// we check here that no messages go from TCAP previewMode
			fail("No message must go from TCAP previewMode");
		}

		@Override
		public void registerManagementEventListener(UUID key, SccpManagementEventListener listener) {
		}

		@Override
		public void deregisterManagementEventListener(UUID key) {
		}

		@Override
		public void coordRequest(int ssn) {
		}

		@Override
		public SccpConnection newConnection(int localSsn, ProtocolClass protocolClass)
				throws MaxConnectionCountReached {
			return null;
		}

		@Override
		public ConcurrentHashMap<Integer, SccpConnection> getConnections() {
			return null;
		}

		@Override
		public void send(SccpNoticeMessage message, TaskCallback<Exception> callback) {
			callback.onSuccess();
		}

		@Override
		public SccpStack getSccpStack() {
			return null;
		}
	}

}
