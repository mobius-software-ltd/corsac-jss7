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

package org.restcomm.protocols.ss7.isup.impl.stack.timers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.restcomm.protocols.ss7.isup.ISUPEvent;
import org.restcomm.protocols.ss7.isup.ISUPListener;
import org.restcomm.protocols.ss7.isup.ISUPProvider;
import org.restcomm.protocols.ss7.isup.ISUPStack;
import org.restcomm.protocols.ss7.isup.ISUPTimeoutEvent;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.CircuitManagerImpl;
import org.restcomm.protocols.ss7.isup.impl.ISUPStackImpl;
import org.restcomm.protocols.ss7.isup.impl.message.AbstractISUPMessage;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartBaseImpl;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.common.dal.timers.WorkerPool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public abstract class EventTestHarness /* extends TestCase */ implements ISUPListener {
	private WorkerPool workerPool;
	protected ISUPStack stack;
	protected ISUPProvider provider;

	protected TimerTestMtp3UserPart userPart;

	// events received by by this listener
	protected List<EventReceived> localEventsReceived;
	// events sent to remote ISUP peer.
	protected List<EventReceived> remoteEventsReceived;

	protected static final int dpc = 1;
	protected static final int localSpc = 2;
	protected static final int ni = 2;

	protected UUID listenerUUID;

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	public void setUp() throws Exception {
		listenerUUID = UUID.randomUUID();
		workerPool = new WorkerPool();
		workerPool.start(4);

		userPart = new TimerTestMtp3UserPart(workerPool);
		userPart.start();

		this.stack = new ISUPStackImpl(localSpc, ni, workerPool.getPeriodicQueue());
		this.provider = this.stack.getIsupProvider();
		this.provider.addListener(listenerUUID, this);
		this.stack.setMtp3UserPart(this.userPart);
		CircuitManagerImpl cm = new CircuitManagerImpl();
		cm.addCircuit(1, dpc);
		this.stack.setCircuitManager(cm);
		this.stack.start();
		localEventsReceived = new ArrayList<EventTestHarness.EventReceived>();
		remoteEventsReceived = new ArrayList<EventTestHarness.EventReceived>();
	}

	public void tearDown() throws Exception {
		// this is done in tests
		// this.stack.stop();

		this.workerPool.stop();
	}

	protected void compareEvents(List<EventReceived> expectedLocalEvents,
			List<EventReceived> expectedRemoteEventsReceived) {

		if (expectedLocalEvents.size() != this.localEventsReceived.size())
			fail("Size of local events: " + this.localEventsReceived.size() + ", does not equal expected events: "
					+ expectedLocalEvents.size() + "\n" + doStringCompare(localEventsReceived, expectedLocalEvents));

		if (expectedRemoteEventsReceived.size() != this.remoteEventsReceived.size())
			fail("Size of remote events: " + this.remoteEventsReceived.size() + ", does not equal expected events: "
					+ expectedRemoteEventsReceived.size() + "\n"
					+ doStringCompare(remoteEventsReceived, expectedRemoteEventsReceived));

		for (int index = 0; index < expectedLocalEvents.size(); index++)
			assertEquals(localEventsReceived.get(index), expectedLocalEvents.get(index));

		for (int index = 0; index < expectedLocalEvents.size(); index++)
			assertEquals(remoteEventsReceived.get(index), expectedRemoteEventsReceived.get(index));
	}

	protected String doStringCompare(List<EventReceived> lst1, List<EventReceived> lst2) {
		StringBuilder sb = new StringBuilder();
		int size1 = lst1.size();
		int size2 = lst2.size();
		int count = size1;
		if (count < size2)
			count = size2;

		for (int index = 0; count > index; index++) {
			String s1 = size1 > index ? lst1.get(index).toString() : "NOP";
			String s2 = size2 > index ? lst2.get(index).toString() : "NOP";
			sb.append(s1).append(" - ").append(s2).append("\n");
		}
		return sb.toString();
	}

	@Override
	public void onEvent(ISUPEvent event) {
		this.localEventsReceived.add(new MessageEventReceived(System.currentTimeMillis(), event));

	}

	@Override
	public void onTimeout(ISUPTimeoutEvent event) {
		this.localEventsReceived.add(new TimeoutEventReceived(System.currentTimeMillis(), event));

	}

	// method implemented by test, to answer stack.
	protected void doAnswer() {
		ISUPMessage answer = getAnswer();
		int opc = 1;
		int dpc = 2;
		int si = Mtp3UserPartBaseImpl._SI_SERVICE_ISUP;
		int ni = 2;
		int sls = 3;
		// int ssi = ni << 2;

		try {
			ByteBuf message = Unpooled.buffer(255);
			((AbstractISUPMessage) answer).encode(message);
			// bout.write(message);
			// byte[] msg = bout.toByteArray();

			// this.userPart.toRead.add(msg);
			Mtp3TransferPrimitiveFactory factory = stack.getMtp3UserPart().getMtp3TransferPrimitiveFactory();
			Mtp3TransferPrimitive mtpMsg = factory.createMtp3TransferPrimitive(si, ni, 0, opc, dpc, sls, message);
			this.userPart.sendTransferMessageToLocalUser(mtpMsg, mtpMsg.getSls());

		} catch (Exception e) {

			e.printStackTrace();
			fail("Failed on receive message: " + e);
		}
	}

	protected void doWait(long t) throws InterruptedException {
		Thread.sleep(t);
	}

	/**
	 * return orignial request
	 *
	 * @return
	 */
	protected abstract ISUPMessage getRequest();

	/**
	 * return answer to be sent.
	 *
	 * @return
	 */
	protected abstract ISUPMessage getAnswer();

	protected class EventReceived {
		private long tstamp;

		/**
		 * @param tstamp
		 */
		public EventReceived(long tstamp) {
			this.tstamp = tstamp;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EventReceived other = (EventReceived) obj;
			
			int threshold = 200;
			if (tstamp != other.tstamp)
				// we have some tolerance
				if ((other.tstamp - threshold) <= tstamp || (other.tstamp + threshold) >= tstamp)
					return true;
				else
					return false;
			return true;
		}
	}

	protected class MessageEventReceived extends EventReceived {
		private ISUPEvent event;

		/**
		 * @param tstamp
		 */
		public MessageEventReceived(long tstamp, ISUPEvent event) {
			super(tstamp);
			this.event = event;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			MessageEventReceived other = (MessageEventReceived) obj;
			if (event == null) {
				if (other.event != null)
					return false;
				// FIXME: use event equal?
			} else if (event.getMessage().getMessageType().getCode() != other.event.getMessage().getMessageType()
					.getCode())
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "MessageEventReceived [event=" + event + ", tstamp= " + super.tstamp + "]";
		}
	}

	protected class TimeoutEventReceived extends EventReceived {
		private ISUPTimeoutEvent event;

		public TimeoutEventReceived(long tstamp, ISUPTimeoutEvent event) {
			super(tstamp);

			this.event = event;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			TimeoutEventReceived other = (TimeoutEventReceived) obj;
			
			if (event == null) {
				if (other.event != null)
					return false;
				// FIXME: use event equal?
			} else if (event.getMessage().getMessageType().getCode() != other.event.getMessage().getMessageType()
					.getCode())
				return false;
			else if (event.getTimerId() != other.event.getTimerId())
				return false;

			return true;
		}

		@Override
		public String toString() {
			return "TimeoutEventReceived [event=" + event + ", tstamp= " + super.tstamp + "]";
		}

	}

	private class TimerTestMtp3UserPart extends Mtp3UserPartBaseImpl {

		public TimerTestMtp3UserPart(WorkerPool workerPool) {
			super(null, workerPool);
		}

		public void sendTransferMessageToLocalUser(Mtp3TransferPrimitive msg, int seqControl) {
			super.sendTransferMessageToLocalUser(msg, seqControl, dummyCallback);
		}

		@Override
		public void sendMessage(Mtp3TransferPrimitive mtpMsg, TaskCallback<Exception> callback) {

			// here we have to parse ISUPMsg and store in receivedRemote
			long tstamp = System.currentTimeMillis();
			ByteBuf payload = mtpMsg.getData();
			if (payload.readableBytes() < 3)
				throw new IllegalArgumentException("byte[] must have atleast three octets");

			payload.markReaderIndex();
			payload.skipBytes(2);
			int commandCode = payload.readByte();
			payload.resetReaderIndex();

			AbstractISUPMessage msg = (AbstractISUPMessage) provider.getMessageFactory().createCommand(commandCode);
			try {
				msg.decode(payload, provider.getMessageFactory(), provider.getParameterFactory());
				MessageEventReceived event = new MessageEventReceived(tstamp, new ISUPEvent(provider, msg, dpc));
				remoteEventsReceived.add(event);
				callback.onSuccess();
			} catch (ParameterException e) {
				e.printStackTrace();
				callback.onError(new RuntimeException());
				fail("Failed on message write: " + e);
			}
		}
	}

}
