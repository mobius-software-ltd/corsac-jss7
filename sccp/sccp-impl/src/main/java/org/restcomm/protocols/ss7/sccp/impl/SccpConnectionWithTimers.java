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

package org.restcomm.protocols.ss7.sccp.impl;

import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CLOSED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CONNECTION_INITIATED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.DISCONNECT_INITIATED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_SENT;

import java.util.concurrent.atomic.AtomicBoolean;

import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpConnectionState;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRscMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SequenceNumberImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SequencingSegmentingImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCauseValue;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.common.dal.timers.Timer;

import io.netty.buffer.Unpooled;

/**
 * 
 * @author yulianoifa
 *
 */
abstract class SccpConnectionWithTimers extends SccpConnectionWithTransmitQueueImpl {
	private ConnEstProcess connEstProcess;
	private IasInactivitySendProcess iasInactivitySendProcess;
	private IarInactivityReceiveProcess iarInactivityReceiveProcess;
	private RelProcess relProcess;
	private RepeatRelProcess repeatRelProcess;
	private IntProcess intProcess;
	private GuardProcess guardProcess;
	private ResetProcess resetProcess;

	public SccpConnectionWithTimers(int sls, int localSsn, LocalReference localReference, ProtocolClass protocol,
			SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
		super(sls, localSsn, localReference, protocol, stack, sccpRoutingControl);
		connEstProcess = new ConnEstProcess();
		iasInactivitySendProcess = new IasInactivitySendProcess();
		iarInactivityReceiveProcess = new IarInactivityReceiveProcess();
		relProcess = new RelProcess();
		repeatRelProcess = new RepeatRelProcess();
		intProcess = new IntProcess();
		guardProcess = new GuardProcess();
		resetProcess = new ResetProcess();
	}

	protected void stopTimers() {
		connEstProcess.stop();
		iasInactivitySendProcess.stop();
		iarInactivityReceiveProcess.stop();
		relProcess.stop();
		repeatRelProcess.stop();
		intProcess.stop();
		guardProcess.stop();
		resetProcess.stop();
	}

	@Override
	protected void receiveMessage(SccpConnMessage message) throws Exception {
		iarInactivityReceiveProcess.resetTimer();

		if (message instanceof SccpConnCcMessageImpl)
			connEstProcess.stop();
		else if (message instanceof SccpConnRscMessageImpl)
			resetProcess.stop();

		super.receiveMessage(message);
	}

	@Override
	public void sendMessage(SccpConnMessage message, TaskCallback<Exception> callback) {
		if (stack.state != SccpStackImpl.State.RUNNING) {
			String errorMessage = "Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING";

			logger.error(errorMessage);
			callback.onError(new IllegalStateException(errorMessage));
			return;
		}
		iasInactivitySendProcess.resetTimer();
		super.sendMessage(message, callback);
	}

	@Override
	public void setState(SccpConnectionState state) {
		super.setState(state);

		if (state == RSR_SENT)
			resetProcess.start();
		else if (state == DISCONNECT_INITIATED) {
			relProcess.start();
			iasInactivitySendProcess.stop();
			iarInactivityReceiveProcess.stop();

		} else if (state == CONNECTION_INITIATED)
			connEstProcess.start();
	}

	protected class ConnEstProcess extends BaseProcess {
		{
			delay = stack.getConnEstTimerDelay();
		}

		@Override
		public void run() {
			try {
				if (getState() == CLOSED)
					return;

				disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), Unpooled.buffer(), dummyCallback);

			} catch (Exception e) {
				logger.error(e);
			}
		}

		@Override
		public String printTaskDetails() {
			return "Task name: SccpConnEstProcess";
		}
	}

	protected class IasInactivitySendProcess extends BaseProcess {
		{
			delay = stack.getIasTimerDelay();
		}

		@Override
		public void run() {
			try {
				if (getState() == CLOSED || getState() == CONNECTION_INITIATED)
					return;

				SccpConnItMessageImpl it = new SccpConnItMessageImpl(getSls(), getLocalSsn());
				it.setProtocolClass(getProtocolClass());
				it.setSourceLocalReferenceNumber(getLocalReference());
				it.setDestinationLocalReferenceNumber(getRemoteReference());

				// could be overwritten during preparing
				it.setCredit(new CreditImpl(0));
				it.setSequencingSegmenting(new SequencingSegmentingImpl(new SequenceNumberImpl(0, false),
						new SequenceNumberImpl(0, false), lastMoreDataSent));
				prepareMessageForSending(it);
				sendMessage(it, dummyCallback);

			} catch (Exception e) {
				logger.error(e);
			}
		}

		@Override
		public String printTaskDetails() {
			return "Task name: SccpIasInactivitySendProcess";
		}
	}

	protected class IarInactivityReceiveProcess extends BaseProcess {
		{
			delay = stack.getIarTimerDelay();
		}

		@Override
		public void run() {
			try {
				if (getState() == CLOSED)
					return;

				disconnect(new ReleaseCauseImpl(ReleaseCauseValue.EXPIRATION_OF_RECEIVE_INACTIVITY_TIMER),
						Unpooled.buffer(), dummyCallback);

			} catch (Exception e) {
				logger.error(e);
			}
		}

		@Override
		public String printTaskDetails() {
			return "Task name: SccpIarInactivityReceiveProcess";
		}
	}

	protected class RelProcess extends BaseProcess {
		{
			delay = stack.getRelTimerDelay();
		}

		@Override
		public void start() {
			if (this.isStarted())
				return; // ignore if already started
			super.start();
		}

		@Override
		public void run() {
			try {
				if (getState() == CLOSED)
					return;

				disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), Unpooled.buffer(), dummyCallback);
				intProcess.start();
				repeatRelProcess.start();

			} catch (Exception e) {
				logger.error(e);
			}
		}

		@Override
		public String printTaskDetails() {
			return "Task name: SccpRelProcess";
		}
	}

	protected class RepeatRelProcess extends BaseProcess {
		{
			delay = stack.getRepeatRelTimerDelay();
		}

		@Override
		public void start() {
			if (this.isStarted())
				return; // ignore if already started
			super.start();
		}

		@Override
		public void run() {
			try {
				if (getState() == CLOSED)
					return;

				disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), Unpooled.buffer(), dummyCallback);
				repeatRelProcess.start();

			} catch (Exception e) {
				logger.error(e);
			}
		}

		@Override
		public String printTaskDetails() {
			return "Task name: SccpRepeatRelProcess";
		}
	}

	protected class IntProcess extends BaseProcess {
		{
			delay = stack.getIntTimerDelay();
		}

		@Override
		public void start() {
			if (this.isStarted())
				return; // ignore if already started
			super.start();
		}

		@Override
		public void run() {
			if (getState() == CLOSED)
				return;

			repeatRelProcess.stop();

			SccpListener listener = getListener();
			if (listener != null)
				listener.onDisconnectIndication((SccpConnection) SccpConnectionWithTimers.this,
						new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), Unpooled.buffer());
			stack.removeConnection(getLocalReference());
		}

		@Override
		public String printTaskDetails() {
			return "Task name: SccpIntProcess";
		}
	}

	protected class GuardProcess extends BaseProcess {
		{
			delay = stack.getGuardTimerDelay();
		}

		@Override
		public void run() {
			if (getState() == CLOSED)
				return;
		}

		@Override
		public String printTaskDetails() {
			return "Task name: SccpGuardProcess";
		}
	}

	protected class ResetProcess extends BaseProcess {
		{
			delay = stack.getResetTimerDelay();
		}

		@Override
		public void run() {
			try {
				if (getState() == CLOSED)
					return;

				disconnect(new ReleaseCauseImpl(ReleaseCauseValue.SCCP_FAILURE), Unpooled.buffer(), dummyCallback);
				stack.removeConnection(getLocalReference());

			} catch (Exception e) {
				logger.error(e);
			}
		}

		@Override
		public String printTaskDetails() {
			return "Task name: SccpResetProcess";
		}
	}

	private abstract class BaseProcess implements Timer {
		protected long delay = stack.getConnEstTimerDelay();

		private long startTime = System.currentTimeMillis();
		private AtomicBoolean isStarted = new AtomicBoolean(false);

		public void start() {
			this.startTime = System.currentTimeMillis();

			if (this.isStarted.get())
				logger.error(new IllegalStateException(String.format("Already started %s timer", getClass())));

			stack.workerPool.getPeriodicQueue().store(this.getRealTimestamp(), this);
			this.isStarted.set(true);
		}

		public void resetTimer() {
			this.stop();
			this.start();
		}

		public boolean isStarted() {
			return this.isStarted.get();
		}

		@Override
		public void execute() {
			if (this.startTime == Long.MAX_VALUE)
				return;

			this.run();
		}

		public abstract void run();

		@Override
		public long getStartTime() {
			return this.startTime;
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + this.delay;
		}

		@Override
		public void stop() {
			this.startTime = Long.MAX_VALUE;
			this.isStarted.set(false);
		}
	}
}
