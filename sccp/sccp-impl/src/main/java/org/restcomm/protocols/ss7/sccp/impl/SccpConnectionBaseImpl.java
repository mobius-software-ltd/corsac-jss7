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
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CR_RECEIVED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.DISCONNECT_INITIATED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.ESTABLISHED_SEND_WINDOW_EXHAUSTED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.NEW;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_PROPAGATED_VIA_COUPLED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_RECEIVED;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_RECEIVED_WILL_PROPAGATE;
import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.RSR_SENT;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.SccpConnectionState;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCrefMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnErrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRlcMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRlsdMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRscMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnRsrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCause;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import com.mobius.software.common.dal.timers.TaskCallback;

import io.netty.buffer.ByteBuf;

/**
 * 
 * @author yulianoifa
 *
 */
public abstract class SccpConnectionBaseImpl {

	protected final Logger logger;
	protected SccpStackImpl stack;
	protected SccpRoutingControl sccpRoutingControl;
	protected Integer remoteSsn;
	protected Integer remoteDpc;
	protected boolean lastMoreDataSent;

	private AtomicReference<SccpConnectionState> state = new AtomicReference<SccpConnectionState>();
	private int sls;
	private int localSsn;
	private ProtocolClass protocolClass;
	private LocalReference localReference;
	private LocalReference remoteReference;

	protected TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	public SccpConnectionBaseImpl(int sls, int localSsn, LocalReference localReference, ProtocolClass protocol,
			SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
		this.stack = stack;
		this.sccpRoutingControl = sccpRoutingControl;
		this.sls = sls;
		this.localSsn = localSsn;
		this.protocolClass = protocol;
		this.localReference = localReference;
		this.state.set(NEW);
		
		this.logger = LogManager
				.getLogger(SccpConnectionBaseImpl.class.getCanonicalName() + "-" + localReference + "-" + stack.name);
	}

	protected void receiveMessage(SccpConnMessage message) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug(String.format("Rx : SCCP message %s", message.toString()));

		if (message instanceof SccpConnCrMessageImpl) {
			SccpConnCrMessageImpl cr = (SccpConnCrMessageImpl) message;
			remoteReference = cr.getSourceLocalReferenceNumber();
			if (cr.getCallingPartyAddress() != null && cr.getCallingPartyAddress().getSignalingPointCode() != 0)
				remoteDpc = cr.getCallingPartyAddress().getSignalingPointCode();
			else if (cr.getIncomingOpc() != -1)
				remoteDpc = cr.getIncomingOpc();
			else
				// when both users are on the same stack
				remoteDpc = cr.getCalledPartyAddress().getSignalingPointCode();
			setState(CR_RECEIVED);

		} else if (message instanceof SccpConnCcMessageImpl) {
			SccpConnCcMessageImpl cc = (SccpConnCcMessageImpl) message;
			remoteReference = cc.getSourceLocalReferenceNumber();
			if (cc.getIncomingDpc() != -1)
				remoteDpc = cc.getIncomingOpc();
			setState(SccpConnectionState.ESTABLISHED);

		} else if (message instanceof SccpConnRscMessageImpl)
			setState(SccpConnectionState.ESTABLISHED);
		else if (message instanceof SccpConnRsrMessageImpl)
			confirmReset();
		else if (message instanceof SccpConnRlsdMessageImpl)
			confirmRelease();
	}

	protected void confirmRelease() throws Exception {
		SccpConnRlcMessageImpl rlc = new SccpConnRlcMessageImpl(sls, localSsn);
		rlc.setSourceLocalReferenceNumber(localReference);
		rlc.setDestinationLocalReferenceNumber(remoteReference);
		rlc.setOutgoingDpc(remoteDpc);
		sendMessage(rlc, dummyCallback);
	}

	protected void confirmReset() throws Exception {
		setState(RSR_RECEIVED);

		SccpConnRscMessageImpl rsc = new SccpConnRscMessageImpl(sls, localSsn);
		rsc.setDestinationLocalReferenceNumber(remoteReference);
		rsc.setSourceLocalReferenceNumber(localReference);
		sendMessage(rsc, dummyCallback);

		setState(SccpConnectionState.ESTABLISHED);
	}

	public void sendErr(ErrorCause cause, TaskCallback<Exception> callback) throws Exception {
		SccpConnErrMessageImpl err = new SccpConnErrMessageImpl(sls, localSsn);
		err.setDestinationLocalReferenceNumber(remoteReference);
		err.setSourceLocalReferenceNumber(localReference);
		err.setErrorCause(cause);

		sendMessage(err, callback);
	}

	public void sendMessage(SccpConnMessage message, TaskCallback<Exception> callback) {
		if (message instanceof SccpConnSegmentableMessageImpl)
			prepareMessageForSending((SccpConnSegmentableMessageImpl) message);
		if (logger.isDebugEnabled())
			logger.debug(String.format("Tx : SCCP Message=%s", message.toString()));
		
		this.sccpRoutingControl.routeMssgFromSccpUserConn(message, callback);
	}

	public void setState(SccpConnectionState state) {
		SccpConnectionState oldState;
		do {
			oldState = this.state.get();
			if (!(oldState == NEW && state == CONNECTION_INITIATED || oldState == NEW && state == CR_RECEIVED
					|| oldState == NEW && state == CLOSED || oldState == CR_RECEIVED && state == ESTABLISHED
					|| oldState == CR_RECEIVED && state == CLOSED
					|| oldState == CONNECTION_INITIATED && state == ESTABLISHED
					|| oldState == CONNECTION_INITIATED && state == CLOSED
					|| oldState == ESTABLISHED && state == ESTABLISHED || oldState == ESTABLISHED && state == CLOSED
					|| oldState == ESTABLISHED && state == RSR_SENT || oldState == ESTABLISHED && state == RSR_RECEIVED
					|| oldState == ESTABLISHED && state == ESTABLISHED_SEND_WINDOW_EXHAUSTED
					|| oldState == ESTABLISHED && state == RSR_RECEIVED_WILL_PROPAGATE
					|| oldState == ESTABLISHED && state == DISCONNECT_INITIATED
					|| oldState == DISCONNECT_INITIATED && state == DISCONNECT_INITIATED // repeated RLSD
					|| oldState == DISCONNECT_INITIATED && state == CLOSED
					|| oldState == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == ESTABLISHED
					|| oldState == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == ESTABLISHED_SEND_WINDOW_EXHAUSTED
					|| oldState == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == RSR_RECEIVED
					|| oldState == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == CLOSED
					|| oldState == ESTABLISHED_SEND_WINDOW_EXHAUSTED && state == DISCONNECT_INITIATED
					|| oldState == RSR_SENT && state == ESTABLISHED || oldState == RSR_SENT && state == CLOSED
					|| oldState == RSR_RECEIVED && state == ESTABLISHED
					|| oldState == RSR_RECEIVED_WILL_PROPAGATE && state == RSR_PROPAGATED_VIA_COUPLED
					|| oldState == RSR_PROPAGATED_VIA_COUPLED && state == ESTABLISHED
					|| oldState == RSR_RECEIVED && state == CLOSED
					// when error happens during message routing connection becomes immediately
					// closed
					|| oldState == CLOSED && state == CLOSED)) {
				logger.error(String.format("state change error: from %s to %s", oldState, state));
				throw new IllegalStateException(String.format("state change error: from %s to %s", oldState, state));
			}
		} while (!this.state.compareAndSet(oldState, state));
	}

	protected void checkLocalListener(TaskCallback<Exception> callback) {
		if (stack.sccpProvider.getSccpListener(getLocalSsn()) == null) {
			String errorMessage = String.format("Attempting to establish connection but the SSN %d is not available", getLocalSsn());
			
			logger.error(errorMessage);
			callback.onError(new IOException(errorMessage));
		}
	}

	public void establish(SccpConnCrMessage message, TaskCallback<Exception> callback) {
		this.checkLocalListener(callback);
		
		try {
			message.setSourceLocalReferenceNumber(this.getLocalReference());

			if (message.getCalledPartyAddress() == null) {
				String errorMessage = "Message to send must have filled CalledPartyAddress field";
				
				logger.error(errorMessage);
				callback.onError(new IOException(errorMessage));
			}
			setState(CONNECTION_INITIATED);
			remoteSsn = message.getCalledPartyAddress().getSubsystemNumber();
			if (message.getCalledPartyAddress().getAddressIndicator().isPCPresent())
				remoteDpc = (message.getCalledPartyAddress().getSignalingPointCode());

			if (logger.isDebugEnabled())
				logger.debug(
						String.format("Establishing connection to DPC=%d, SSN=%d", getRemoteDpc(), getRemoteSsn()));

			this.sendMessage(message, callback);

		} catch (Exception e) {
			logger.error(e);
			callback.onError(e);
		}
	}

	public void reset(ResetCause reason, TaskCallback<Exception> callback) throws Exception {
		if (reason.getValue().isError())
			logger.warn(String.format("Resetting connection to DPC=%d, SSN=%d, DLR=%s due to %s", getRemoteDpc(),
					getRemoteSsn(), getRemoteReference(), reason));
		else if (logger.isDebugEnabled())
			logger.debug(String.format("Resetting connection to DPC=%d, SSN=%d, DLR=%s due to %s", getRemoteDpc(),
					getRemoteSsn(), getRemoteReference(), reason));
		SccpConnRsrMessageImpl rsr = new SccpConnRsrMessageImpl(getSls(), getLocalSsn());
		rsr.setSourceLocalReferenceNumber(getLocalReference());
		rsr.setDestinationLocalReferenceNumber(getRemoteReference());
		rsr.setResetCause(reason);
		setState(RSR_SENT);
		sendMessage(rsr, callback);
	}

	public void resetSection(ResetCause reason, TaskCallback<Exception> callback) throws Exception {
		reset(reason, callback);
	}

	public void disconnect(ReleaseCause reason, ByteBuf data, TaskCallback<Exception> callback) {
		if (reason.getValue().isError())
			logger.warn(String.format("Disconnecting connection to DPC=%d, SSN=%d, DLR=%s due to %s", getRemoteDpc(),
					getRemoteSsn(), getRemoteReference(), reason));
		else if (logger.isDebugEnabled())
			logger.debug(String.format("Disconnecting connection to DPC=%d, SSN=%d, DLR=%s due to %s", getRemoteDpc(),
					getRemoteSsn(), getRemoteReference(), reason));

		SccpConnRlsdMessageImpl rlsd = new SccpConnRlsdMessageImpl(getSls(), getLocalSsn());
		rlsd.setDestinationLocalReferenceNumber(getRemoteReference());
		rlsd.setReleaseCause(reason);
		rlsd.setSourceLocalReferenceNumber(getLocalReference());
		rlsd.setUserData(data);
		SccpConnectionState prevState = state.get();
		try {
			setState(DISCONNECT_INITIATED);
			this.sendMessage(rlsd, callback);
		} catch (Exception e) {
			state.set(prevState);
			throw e;
		}
	}

	public void refuse(RefusalCause reason, ByteBuf data, TaskCallback<Exception> callback) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug(String.format("Refusing connection from DPC=%d, SSN=%d, DLR=%s due to %s", getRemoteDpc(),
					getRemoteSsn(), getRemoteReference(), reason));
		SccpConnCrefMessageImpl cref = new SccpConnCrefMessageImpl(getSls(), getLocalSsn());
		cref.setDestinationLocalReferenceNumber(getRemoteReference());
		cref.setSourceLocalReferenceNumber(getLocalReference());
		cref.setRefusalCause(reason);
		cref.setUserData(data);
		sendMessage(cref, callback);
		stack.removeConnection(getLocalReference());
	}

	public void confirm(SccpAddress respondingAddress, Credit credit, ByteBuf data, TaskCallback<Exception> callback) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug(String.format("Confirming connection from DPC=%d, SSN=%d, DLR=%s", getRemoteDpc(),
					getRemoteSsn(), getRemoteReference()));
		if (getState() != CR_RECEIVED) {
			logger.error(String.format("Trying to confirm connection in non-compatible state %s", getState()));
			throw new IllegalStateException(
					String.format("Trying to confirm connection in non-compatible state %s", getState()));
		}
		SccpConnCcMessageImpl message = new SccpConnCcMessageImpl(getSls(), getLocalSsn());
		message.setSourceLocalReferenceNumber(getLocalReference());
		message.setDestinationLocalReferenceNumber(getRemoteReference());
		message.setProtocolClass(getProtocolClass());
		message.setCalledPartyAddress(respondingAddress);
		message.setUserData(data);
		message.setCredit(credit);

		sendMessage(message, callback);

		setState(SccpConnectionState.ESTABLISHED);
	}

	public Integer getRemoteDpc() {
		return remoteDpc;
	}

	public Integer getRemoteSsn() {
		// could be unknown i. e. null
		return remoteSsn;
	}

	public void setRemoteSsn(Integer val) {
		remoteSsn = val;
	}

	public SccpListener getListener() {
		return stack.sccpProvider.getSccpListener(localSsn);
	}

	public int getSls() {
		return sls;
	}

	public int getLocalSsn() {
		return localSsn;
	}

	public LocalReference getLocalReference() {
		return localReference;
	}

	public LocalReference getRemoteReference() {
		return remoteReference;
	}

	public SccpConnectionState getState() {
		return state.get();
	}

	public ProtocolClass getProtocolClass() {
		return protocolClass;
	}

	public boolean isAvailable() {
		SccpConnectionState currState = state.get();
		return currState == ESTABLISHED || currState == ESTABLISHED_SEND_WINDOW_EXHAUSTED;
	}

	protected boolean isCanSendData() {
		return state.get() == ESTABLISHED;
	}

	public Credit getSendCredit() {
		throw new IllegalArgumentException(
				"sendCredit is supported only by flow control connection-oriented protocol class");
	}

	public Credit getReceiveCredit() {
		throw new IllegalArgumentException(
				"receiveCredit is supported only by flow control connection-oriented protocol class");
	}

	public abstract void prepareMessageForSending(SccpConnSegmentableMessageImpl message);

	protected abstract void prepareMessageForSending(SccpConnItMessageImpl message);

	protected abstract void callListenerOnData(ByteBuf data);
}
