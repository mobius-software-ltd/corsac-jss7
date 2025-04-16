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

package org.restcomm.protocols.ss7.isup.impl;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.restcomm.protocols.ss7.isup.ISUPEvent;
import org.restcomm.protocols.ss7.isup.ISUPTimeoutEvent;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.AbstractISUPMessage;
import org.restcomm.protocols.ss7.isup.message.AddressCompleteMessage;
import org.restcomm.protocols.ss7.isup.message.BlockingAckMessage;
import org.restcomm.protocols.ss7.isup.message.BlockingMessage;
import org.restcomm.protocols.ss7.isup.message.CircuitGroupBlockingMessage;
import org.restcomm.protocols.ss7.isup.message.CircuitGroupQueryMessage;
import org.restcomm.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.restcomm.protocols.ss7.isup.message.CircuitGroupResetAckMessage;
import org.restcomm.protocols.ss7.isup.message.CircuitGroupResetMessage;
import org.restcomm.protocols.ss7.isup.message.CircuitGroupUnblockingAckMessage;
import org.restcomm.protocols.ss7.isup.message.CircuitGroupUnblockingMessage;
import org.restcomm.protocols.ss7.isup.message.ConnectMessage;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.isup.message.InformationMessage;
import org.restcomm.protocols.ss7.isup.message.InformationRequestMessage;
import org.restcomm.protocols.ss7.isup.message.InitialAddressMessage;
import org.restcomm.protocols.ss7.isup.message.ReleaseCompleteMessage;
import org.restcomm.protocols.ss7.isup.message.ReleaseMessage;
import org.restcomm.protocols.ss7.isup.message.ResetCircuitMessage;
import org.restcomm.protocols.ss7.isup.message.SubsequentAddressMessage;
import org.restcomm.protocols.ss7.isup.message.UnblockingAckMessage;
import org.restcomm.protocols.ss7.isup.message.UnblockingMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartBaseImpl;

import com.mobius.software.common.dal.timers.PeriodicQueuedTasks;
import com.mobius.software.common.dal.timers.Timer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author baranowb
 * @author amit bhayani
 * @author yulianoifa
 *
 */
class Circuit {
	private final int cic;
	private final int dpc;
	private final ISUPProviderImpl provider;

	private PeriodicQueuedTasks<Timer> queuedTasks;

	/**
	 * @param cic
	 */
	public Circuit(int cic, int dpc, ISUPProviderImpl provider, PeriodicQueuedTasks<Timer> queuedTasks) {
		this.queuedTasks = queuedTasks;

		this.cic = cic;
		this.dpc = dpc;
		this.provider = provider;

		// create all timers and deactivate them
		t1 = new TimerT1();
		t5 = new TimerT5();
		t7 = new TimerT7();
		t12 = new TimerT12();
		t13 = new TimerT13();
		t14 = new TimerT14();
		t15 = new TimerT15();
		t16 = new TimerT16();
		t17 = new TimerT17();
		t18 = new TimerT18();
		t19 = new TimerT19();
		t20 = new TimerT20();
		t21 = new TimerT21();
		t22 = new TimerT22();
		t23 = new TimerT23();
		t28 = new TimerT28();
		t33 = new TimerT33();
		onStop();
	}

	/**
	 * @return the cic
	 */
	public int getCic() {
		return cic;
	}

	/**
	 * @return the dpc
	 */
	public int getDpc() {
		return dpc;
	}

	/**
	 * @param timerId
	 * @return
	 */
	public boolean cancelTimer(int timerId) {
		switch (timerId) {
		case ISUPTimeoutEvent.T1:
			return cancelT1();
		case ISUPTimeoutEvent.T5:
			return cancelT5();
		case ISUPTimeoutEvent.T7:
			return cancelT7();
		case ISUPTimeoutEvent.T12:
			return cancelT12();
		case ISUPTimeoutEvent.T13:
			return cancelT13();
		case ISUPTimeoutEvent.T14:
			return cancelT14();
		case ISUPTimeoutEvent.T15:
			return cancelT15();
		case ISUPTimeoutEvent.T16:
			return cancelT16();
		case ISUPTimeoutEvent.T17:
			return cancelT17();
		case ISUPTimeoutEvent.T18:
			return cancelT18();
		case ISUPTimeoutEvent.T19:
			return cancelT19();
		case ISUPTimeoutEvent.T20:
			return cancelT20();
		case ISUPTimeoutEvent.T21:
			return cancelT21();
		case ISUPTimeoutEvent.T22:
			return cancelT22();
		case ISUPTimeoutEvent.T23:
			return cancelT23();
		case ISUPTimeoutEvent.T28:
			return cancelT28();
		case ISUPTimeoutEvent.T33:
			return cancelT33();
		default:
			return false;
		}
	}

	/**
	 * @param message
	 */
	public void receive(ISUPMessage message) {
		try {
			// process timers
			switch (message.getMessageType().getCode()) {
			// FIXME: add check for SEG
			case ReleaseCompleteMessage.MESSAGE_CODE:
				// this is tricy BS.... its REL and RSC are answered with RLC
				if (!stopRELTimers())
					stopRSCTimers();
				break;
			case AddressCompleteMessage.MESSAGE_CODE:
			case ConnectMessage.MESSAGE_CODE:
				stoptXAMTimers();
				break;
			case BlockingAckMessage.MESSAGE_CODE:
				stoptBLOTimers();
				break;
			case UnblockingAckMessage.MESSAGE_CODE:
				stoptUBLTimers();
				break;
			case CircuitGroupBlockingMessage.MESSAGE_CODE:
				stoptCGBTimers();
				break;

			case CircuitGroupUnblockingAckMessage.MESSAGE_CODE:
				stoptCGUTimers();
				break;
			case CircuitGroupResetAckMessage.MESSAGE_CODE:
				stoptGRSTimers();
				break;
			case CircuitGroupQueryResponseMessage.MESSAGE_CODE:
				stoptCQMTimers();
				break;
			case InformationMessage.MESSAGE_CODE:
				stoptINRTimers();
				break;
			}

			// deliver
			ISUPEvent event = new ISUPEvent(provider, message, dpc);
			provider.deliver(event);
		} catch (Exception e) {
			// catch exception, so thread dont die.
			e.printStackTrace();
		}
	}

	/**
	 * @param message
	 * @throws ParameterException
	 * @throws IOException
	 */
	public void send(ISUPMessage message) throws ParameterException, IOException {
		try {
			// FIXME: add SEG creation?
			Mtp3TransferPrimitive msg = decorate(message);
			// process timers
			switch (message.getMessageType().getCode()) {
			case ReleaseMessage.MESSAGE_CODE:
				startRELTimers(msg, (ReleaseMessage) message);
				break;
			case SubsequentAddressMessage.MESSAGE_CODE:
			case InitialAddressMessage.MESSAGE_CODE:
				startXAMTimers(message);
				break;
			case BlockingMessage.MESSAGE_CODE:
				startBLOTimers(msg, (BlockingMessage) message);
				break;
			case UnblockingMessage.MESSAGE_CODE:
				startUBLTimers(msg, (UnblockingMessage) message);
				break;
			case ResetCircuitMessage.MESSAGE_CODE:
				startRSCTimers(msg, (ResetCircuitMessage) message);
				break;
			case CircuitGroupBlockingMessage.MESSAGE_CODE:
				startCGBTimers(msg, (CircuitGroupBlockingMessage) message);
				break;
			case CircuitGroupUnblockingMessage.MESSAGE_CODE:
				startCGUTimers(msg, (CircuitGroupUnblockingMessage) message);
				break;
			case CircuitGroupResetMessage.MESSAGE_CODE:
				startGRSTimers(msg, (CircuitGroupResetMessage) message);
				break;
			case CircuitGroupQueryMessage.MESSAGE_CODE:
				startCQMTimers((CircuitGroupQueryMessage) message);
				break;
			case InformationRequestMessage.MESSAGE_CODE:
				startINRTimers((InformationRequestMessage) message);
				break;
			}
			// send
			provider.send(message, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param message
	 * @return
	 * @throws ParameterException
	 * @throws IOException
	 */
	private Mtp3TransferPrimitive decorate(ISUPMessage message) throws ParameterException, IOException {
		ByteBuf buffer = Unpooled.buffer();
		((AbstractISUPMessage) message).encode(buffer);
		int opc = this.provider.getLocalSpc();
		int dpc = this.dpc;
		int si = Mtp3UserPartBaseImpl._SI_SERVICE_ISUP;
		int ni = this.provider.getNi();
		int sls = message.getSls() & 0x0F; // promote

		Mtp3TransferPrimitiveFactory factory = this.provider.stack.getMtp3UserPart().getMtp3TransferPrimitiveFactory();
		Mtp3TransferPrimitive msg = factory.createMtp3TransferPrimitive(si, ni, 0, opc, dpc, sls, buffer,
				new AtomicBoolean(false));
		return msg;
	}

	// ----------------- timer handlers ----------------------

	// FIXME: check how t3 works....

	private TimerT1 t1;
	private TimerT5 t5;
	private Mtp3TransferPrimitive t1t5encodedREL; // keep encoded value, so we can simply send,
	// without spending CPU on encoding.
	private ReleaseMessage t1t5REL; // keep for timers.
	private TimerT7 t7;
	private ISUPMessage t7AddressMessage; // IAM/SAM

	// FIXME: t8 - receive IAM with contuuity check ind.
	// FIXME: t11

	private TimerT12 t12;
	private TimerT13 t13;
	private Mtp3TransferPrimitive t12t13encodedBLO; // keep encoded value, so we can simply
	// send, without spending CPU on
	// encoding.
	private BlockingMessage t12t13BLO; // keep for timers.

	private TimerT14 t14;
	private TimerT15 t15;
	private Mtp3TransferPrimitive t14t15encodedUBL; // keep encoded value, so we can simply
	// send, without spending CPU on
	// encoding.
	private UnblockingMessage t14t15UBL; // keep for timers.

	private TimerT16 t16;
	private TimerT17 t17;
	private Mtp3TransferPrimitive t16t17encodedRSC; // keep encoded value, so we can simply
	// send, without spending CPU on
	// encoding.
	private ResetCircuitMessage t16t17RSC; // keep for timers.

	private TimerT18 t18;
	private TimerT19 t19;
	private Mtp3TransferPrimitive t18t19encodedCGB; // keep encoded value, so we can simply
	// send, without spending CPU on
	// encoding.
	private CircuitGroupBlockingMessage t18t19CGB; // keep for timers.

	private TimerT20 t20;
	private TimerT21 t21;
	private Mtp3TransferPrimitive t20t21encodedCGU; // keep encoded value, so we can simply
	// send, without spending CPU on
	// encoding.
	private CircuitGroupUnblockingMessage t20t21CGU; // keep for timers.

	private TimerT22 t22;
	private TimerT23 t23;
	private Mtp3TransferPrimitive t22t23encodedGRS; // keep encoded value, so we can simply
	// send, without spending CPU on
	// encoding.
	private CircuitGroupResetMessage t22t23GRS; // keep for timers.

	private TimerT28 t28;
	private CircuitGroupQueryMessage t28CQM;

	private TimerT33 t33;
	private InformationRequestMessage t33INR;

	// FIXME: t34 - check how SEG works

	private void startRELTimers(Mtp3TransferPrimitive encoded, ReleaseMessage rel) {
		// FIXME: add lock ?
		this.t1t5encodedREL = encoded;
		this.t1t5REL = rel;

		if (!this.t5.isActive())
			startT5();

		// it is started always.
		startT1();

	}

	/**
	 * @return
	 */
	private boolean stopRELTimers() {
		if (this.t1.isActive() || this.t5.isActive()) {
			cancelT1();
			cancelT5();
			return true;
		} else
			return false;
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startXAMTimers(ISUPMessage message) {
		this.cancelT7();
		this.t7AddressMessage = message;
		this.startT7();
	}

	/**
	 * @return
	 */
	private void stoptXAMTimers() {
		cancelT7();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startBLOTimers(Mtp3TransferPrimitive encoded, BlockingMessage message) {
		this.t12t13BLO = message;
		this.t12t13encodedBLO = encoded;

		if (!this.t13.isActive())
			startT13();

		// it is started always.
		startT12();
	}

	/**
	 * @return
	 */
	private void stoptBLOTimers() {
		cancelT12();
		cancelT13();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startUBLTimers(Mtp3TransferPrimitive encoded, UnblockingMessage message) {
		this.t14t15UBL = message;
		this.t14t15encodedUBL = encoded;

		if (!this.t15.isActive())
			startT15();

		// it is started always.
		startT14();

	}

	/**
	 * @return
	 */
	private void stoptUBLTimers() {
		cancelT14();
		cancelT15();

	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startRSCTimers(Mtp3TransferPrimitive encoded, ResetCircuitMessage message) {
		this.t16t17RSC = message;
		this.t16t17encodedRSC = encoded;

		if (!this.t17.isActive())
			startT17();
		// it is started always.
		startT16();

	}

	/**
	 * @return
	 */
	private void stopRSCTimers() {
		cancelT16();
		cancelT17();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startINRTimers(InformationRequestMessage message) {
		this.t33INR = message;
		startT33();
	}

	/**
	 * @return
	 */
	private void stoptINRTimers() {
		cancelT33();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startCQMTimers(CircuitGroupQueryMessage message) {
		this.t28CQM = message;

		// it is started always.
		startT28();
		// FIXME: can we send more than one?
	}

	/**
	 * @return
	 */
	private void stoptCQMTimers() {
		cancelT28();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startGRSTimers(Mtp3TransferPrimitive encoded, CircuitGroupResetMessage message) {
		this.t22t23GRS = message;
		this.t22t23encodedGRS = encoded;

		if (!this.t23.isActive())
			startT23();
		// it is started always.
		startT22();

	}

	/**
	 * @return
	 */
	private void stoptGRSTimers() {
		cancelT22();
		cancelT23();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startCGUTimers(Mtp3TransferPrimitive encoded, CircuitGroupUnblockingMessage message) {
		this.t20t21CGU = message;
		this.t20t21encodedCGU = encoded;

		if (!this.t21.isActive())
			startT21();
		// it is started always.
		startT20();

	}

	/**
	 * @return
	 */
	private void stoptCGUTimers() {
		cancelT20();
		cancelT21();
	}

	/**
	 * @param encoded
	 * @param message
	 */
	private void startCGBTimers(Mtp3TransferPrimitive encoded, CircuitGroupBlockingMessage message) {
		this.t18t19CGB = message;
		this.t18t19encodedCGB = encoded;

		if (!this.t19.isActive())
			startT19();
		// it is started always.
		startT18();

	}

	/**
	 * @return
	 */
	private void stoptCGBTimers() {
		cancelT18();
		cancelT19();
	}

	private void startT1() {
		cancelT1();
		this.t1.start();
	}

	private boolean cancelT1() {
		return this.t1.cancel();
	}

	private void startT5() {
		cancelT5();
		this.t5.start();
	}

	private boolean cancelT5() {
		return this.t5.cancel();
	}

	private void startT7() {
		cancelT7();
		this.t7.start();
	}

	private boolean cancelT7() {
		return this.t7.cancel();
	}

	private void startT12() {
		cancelT12();
		this.t12.start();
	}

	private boolean cancelT12() {
		return this.t12.cancel();
	}

	private void startT13() {
		cancelT13();
		this.t13.start();
	}

	private boolean cancelT13() {
		return this.t13.cancel();
	}

	private void startT14() {
		cancelT14();
		this.t14.start();
	}

	private boolean cancelT14() {
		return this.t14.cancel();
	}

	private void startT15() {
		cancelT15();
		this.t15.start();
	}

	private boolean cancelT15() {
		return this.t15.cancel();
	}

	private void startT16() {
		cancelT16();
		this.t16.start();
	}

	private boolean cancelT16() {
		return this.t16.cancel();
	}

	private void startT17() {
		cancelT17();
		this.t17.start();
	}

	private boolean cancelT17() {
		return this.t17.cancel();
	}

	private void startT18() {
		cancelT18();
		this.t18.start();
	}

	private boolean cancelT18() {
		return this.t18.cancel();
	}

	private void startT19() {
		cancelT19();
		this.t19.start();
	}

	private boolean cancelT19() {
		return this.t19.cancel();
	}

	private void startT20() {
		cancelT20();
		this.t20.start();
	}

	private boolean cancelT20() {
		return this.t20.cancel();
	}

	private void startT21() {
		cancelT21();
		this.t21.start();
	}

	private boolean cancelT21() {
		return this.t21.cancel();
	}

	private void startT22() {
		cancelT22();
		this.t22.start();
	}

	private boolean cancelT22() {
		return this.t22.cancel();
	}

	private void startT23() {
		cancelT23();
		this.t23.start();
	}

	private boolean cancelT23() {
		return this.t23.cancel();
	}

	private void startT28() {
		cancelT28();
		this.t28.start();
	}

	private boolean cancelT28() {
		return this.t28.cancel();
	}

	private void startT33() {
		cancelT33();
		this.t33.start();
	}

	private boolean cancelT33() {
		return this.t33.cancel();
	}

	private abstract class CircuitTimer implements Timer {
		protected long startTime = System.currentTimeMillis();
		protected boolean active = false;

		public final void start() {
			this.startTime = System.currentTimeMillis();
			queuedTasks.store(this.getRealTimestamp(), this);

			this.active = true;
		}

		public final Boolean isActive() {
			return (this.startTime != Long.MAX_VALUE && this.active);
		}

		public final Boolean cancel() {
			if (this.startTime != Long.MAX_VALUE && this.active) {
				this.stop();
				return true;
			}

			return false;
		}

		@Override
		public final void execute() {
			if (this.startTime == Long.MAX_VALUE)
				return;

			this.run();
			this.active = false;
		};

		public abstract void run();

		@Override
		public abstract Long getRealTimestamp();

		@Override
		public final long getStartTime() {
			return this.startTime;
		}

		@Override
		public final void stop() {
			this.active = false;
			this.startTime = Long.MAX_VALUE;
		}
	}

	private class TimerT1 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// start T1
				startT1();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						// TODO: CI required ?
						provider.send(t1t5REL, t1t5encodedREL);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t1t5REL, ISUPTimeoutEvent.T1, dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT1Timeout();
		}
	}

	private class TimerT5 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// remove t5, its current timer.
				cancelT1();

				// restart T5
				startT5();

				// send
				if (provider.isAutomaticTimerMessages())
					try {
						final ResetCircuitMessage rcm = provider.getMessageFactory().createRSC(cic);
						// avoid provider method, since we dont want other timer to be
						// setup.
						provider.sendMessage(rcm, dpc);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t1t5REL, ISUPTimeoutEvent.T5, dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT5Timeout();
		}
	}

	private class TimerT7 extends CircuitTimer {
		@Override
		public void run() {

			try {
				// send REL
				if (provider.isAutomaticTimerMessages())
					try {
						final ReleaseMessage rel = provider.getMessageFactory().createREL(cic);
						final CauseIndicators ci = provider.getParameterFactory().createCauseIndicators();
						ci.setCauseValue(CauseIndicators._CV_NORMAL_UNSPECIFIED);
						rel.setCauseIndicators(ci);
						provider.sendMessage(rel, dpc);
					} catch (ParameterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t7AddressMessage,
						ISUPTimeoutEvent.T7, dpc);
				t7AddressMessage = null;
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT7Timeout();
		}
	}

	private class TimerT12 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// start T12
				startT12();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t12t13BLO, t12t13encodedBLO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t12t13BLO, ISUPTimeoutEvent.T12,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT12Timeout();
		}
	}

	private class TimerT13 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// cancel T12
				cancelT12();
				// restart T13
				startT13();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t12t13BLO, t12t13encodedBLO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t12t13BLO, ISUPTimeoutEvent.T13,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT13Timeout();
		}
	}

	private class TimerT14 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// start T14
				startT14();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t14t15UBL, t14t15encodedUBL);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t14t15UBL, ISUPTimeoutEvent.T14,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT14Timeout();
		}
	}

	private class TimerT15 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// cancel T14
				cancelT14();
				// start
				startT15();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t14t15UBL, t14t15encodedUBL);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t14t15UBL, ISUPTimeoutEvent.T15,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT15Timeout();
		}
	}

	private class TimerT16 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// start T14
				startT16();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t16t17RSC, t16t17encodedRSC);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t16t17RSC, ISUPTimeoutEvent.T16,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT16Timeout();
		}
	}

	private class TimerT17 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// cancel T16
				cancelT16();
				// restart T17
				startT17();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t16t17RSC, t16t17encodedRSC);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t16t17RSC, ISUPTimeoutEvent.T17,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT17Timeout();
		}
	}

	private class TimerT18 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// start T18

				startT18();
				// send

				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t18t19CGB, t18t19encodedCGB);
					} catch (Exception e) {
						e.printStackTrace();
					}

				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t18t19CGB, ISUPTimeoutEvent.T18,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT18Timeout();
		}
	}

	private class TimerT19 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// cancel T18
				cancelT18();
				// restart T19
				startT19();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t18t19CGB, t18t19encodedCGB);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t18t19CGB, ISUPTimeoutEvent.T19,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT19Timeout();
		}
	}

	private class TimerT20 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// start T20
				startT20();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t20t21CGU, t20t21encodedCGU);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t20t21CGU, ISUPTimeoutEvent.T20,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT20Timeout();
		}
	}

	private class TimerT21 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// cancel T20
				cancelT20();
				// restart T21
				startT21();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t20t21CGU, t20t21encodedCGU);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t20t21CGU, ISUPTimeoutEvent.T21,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT20Timeout();
		}
	}

	private class TimerT22 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// start T22
				startT22();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t22t23GRS, t22t23encodedGRS);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t22t23GRS, ISUPTimeoutEvent.T22,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT22Timeout();
		}
	}

	private class TimerT23 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// cancel T22
				cancelT22();
				// restart T23
				startT23();
				// send
				if (provider.isAutomaticTimerMessages())
					try {
						provider.send(t22t23GRS, t22t23encodedGRS);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, t22t23GRS, ISUPTimeoutEvent.T23,
						dpc);
				provider.deliver(timeoutEvent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT23Timeout();
		}
	}

	private class TimerT28 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// notify user
				final CircuitGroupQueryMessage msg = t28CQM;
				t28CQM = null;
				ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, msg, ISUPTimeoutEvent.T28, dpc);
				provider.deliver(timeoutEvent);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT28Timeout();
		}
	}

	private class TimerT33 extends CircuitTimer {
		@Override
		public void run() {
			try {
				// send REL
				if (provider.isAutomaticTimerMessages())
					try {
						final ReleaseMessage rel = provider.getMessageFactory().createREL(cic);
						final CauseIndicators ci = provider.getParameterFactory().createCauseIndicators();
						ci.setCauseValue(CauseIndicators._CV_NORMAL_UNSPECIFIED);
						rel.setCauseIndicators(ci);
						provider.sendMessage(rel, dpc);
					} catch (ParameterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				final InformationRequestMessage msg = t33INR;
				t33INR = null;
				// notify user
				final ISUPTimeoutEvent timeoutEvent = new ISUPTimeoutEvent(provider, msg, ISUPTimeoutEvent.T33, dpc);
				provider.deliver(timeoutEvent);
				// FIXME: do this after call, to prevent send of another msg

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Long getRealTimestamp() {
			return this.startTime + provider.getT33Timeout();
		}
	}

	/**
	 *
	 */
	public void onStop() {
		cancelT1();
		cancelT5();
		cancelT12();
		cancelT13();
		cancelT14();
		cancelT15();
		cancelT16();
		cancelT17();
		cancelT18();
		cancelT19();
		cancelT20();
		cancelT21();
		cancelT22();
		cancelT23();
		cancelT28();
		cancelT33();
	}

}
