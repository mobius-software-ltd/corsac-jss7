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

package org.restcomm.protocols.ss7.inap;

import java.io.Externalizable;

import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPMessage;
import org.restcomm.protocols.ss7.inap.api.INAPServiceBase;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPDialogState;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPGeneralAbortReason;
import org.restcomm.protocols.ss7.inap.api.dialog.INAPUserAbortReason;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorCode;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessage;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageParameterless;
import org.restcomm.protocols.ss7.inap.dialog.INAPUserObject;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 *
 * @author yulian.oifa
 */
public abstract class INAPDialogImpl implements INAPDialog {
	private static final long serialVersionUID = 1L;

	protected Dialog tcapDialog = null;
	protected INAPProviderImpl inapProviderImpl = null;
	protected INAPServiceBase inapService = null;

	// Application Context of this Dialog
	protected INAPApplicationContext appCntx;

	protected INAPDialogState state = INAPDialogState.IDLE;

	// protected boolean normalDialogShutDown = false;

	// private Set<Long> incomingInvokeList = new HashSet<Long>();

	boolean returnMessageOnError = false;
	protected MessageType tcapMessageType;
	protected DelayedAreaState delayedAreaState;
	private INAPStackConfigurationManagement inapStackConfigurationManagement;

	protected INAPDialogImpl(INAPApplicationContext appCntx, Dialog tcapDialog, INAPProviderImpl inapProviderImpl,
			INAPServiceBase inapService) {
		this.appCntx = appCntx;
		this.tcapDialog = tcapDialog;
		this.inapProviderImpl = inapProviderImpl;
		this.inapService = inapService;
		this.inapStackConfigurationManagement = new INAPStackConfigurationManagement();
		setUserObject(getUserObject());
	}

	@Override
	public SccpAddress getLocalAddress() {
		return this.tcapDialog.getLocalAddress();
	}

	@Override
	public void setLocalAddress(SccpAddress localAddress) {
		this.tcapDialog.setLocalAddress(localAddress);
	}

	@Override
	public SccpAddress getRemoteAddress() {
		return this.tcapDialog.getRemoteAddress();
	}

	@Override
	public void setRemoteAddress(SccpAddress remoteAddress) {
		this.tcapDialog.setRemoteAddress(remoteAddress);
	}

	@Override
	public void setReturnMessageOnError(boolean val) {
		returnMessageOnError = val;
		setUserObject(getUserObject());
	}

	@Override
	public boolean getReturnMessageOnError() {
		return returnMessageOnError;
	}

	@Override
	public MessageType getTCAPMessageType() {
		return tcapMessageType;
	}

	@Override
	public int getNetworkId() {
		return this.tcapDialog.getNetworkId();
	}

	@Override
	public void setNetworkId(int networkId) {
		this.tcapDialog.setNetworkId(networkId);
	}

	@Override
	public void keepAlive() {
		this.tcapDialog.keepAlive();
	}

	@Override
	public Long getLocalDialogId() {
		return tcapDialog.getLocalDialogId();
	}

	@Override
	public Long getRemoteDialogId() {
		return tcapDialog.getRemoteDialogId();
	}

	@Override
	public INAPServiceBase getService() {
		return this.inapService;
	}

	public Dialog getTcapDialog() {
		return tcapDialog;
	}

	@Override
	public void release() {
		// this.setNormalDialogShutDown();
		this.setState(INAPDialogState.EXPUNGED);

		if (this.tcapDialog != null)
			this.tcapDialog.release();
	}

	/**
	 * Setting that the INAP Dialog is normally shutting down - to prevent
	 * performing onDialogReleased()
	 */
	// protected void setNormalDialogShutDown() {
	// this.normalDialogShutDown = true;
	// }
	//
	// protected Boolean getNormalDialogShutDown() {
	// return this.normalDialogShutDown;
	// }

	/**
	 * Adding the new incoming invokeId into incomingInvokeList list
	 *
	 * @param invokeId
	 * @return false: failure - this invokeId already present in the list
	 */
	// public boolean addIncomingInvokeId(Long invokeId) {
	// if (this.incomingInvokeList.contains(invokeId))
	// return false;
	// else {
	// this.incomingInvokeList.add(invokeId);
	// return true;
	// }
	// }
	//
	// public void removeIncomingInvokeId(Long invokeId) {
	// this.incomingInvokeList.remove(invokeId);
	// }
	//
	// public Boolean checkIncomingInvokeIdExists(Long invokeId) {
	// return this.incomingInvokeList.contains(invokeId);
	// }

	@Override
	public INAPDialogState getState() {
		return state;
	}

	protected void setState(INAPDialogState newState) {
		if (this.state == INAPDialogState.EXPUNGED)
			return;

		this.state = newState;
		setUserObject(getUserObject());
	}

	@Override
	public void send(TaskCallback<Exception> callback) {

		switch (this.tcapDialog.getState()) {
		case Idle:
			ApplicationContextName acn = this.inapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createApplicationContextName(this.appCntx.getOID());

			this.setState(INAPDialogState.INITIAL_SENT);

			this.inapProviderImpl.fireTCBegin(this.getTcapDialog(), acn, this.getReturnMessageOnError(), callback);
			break;
			
		case Active:
			// Its Active send TC-CONTINUE

			this.inapProviderImpl.fireTCContinue(this.getTcapDialog(), null, this.getReturnMessageOnError(), callback);
			break;

		case InitialReceived:
			// Its first Reply to TC-Begin

			ApplicationContextName acn1 = this.inapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createApplicationContextName(this.appCntx.getOID());

			INAPDialogState oldState = getState();
			this.setState(INAPDialogState.ACTIVE);
			try {
				this.inapProviderImpl.fireTCContinue(this.getTcapDialog(), acn1, this.getReturnMessageOnError(),
						callback);
			} catch (Exception ex) {
				this.state = oldState;
				setUserObject(getUserObject());
			}
			break;

		case InitialSent: // we have sent TC-BEGIN already, need to wait
			callback.onError(
					new INAPException("Awaiting TC-BEGIN response, can not send another dialog initiating primitive!"));
			break;
		case Expunged: // dialog has been terminated on TC level, cant send
			callback.onError(new INAPException("Dialog has been terminated, can not send primitives!"));
			break;
		}
	}

	@Override
	public void sendDelayed(TaskCallback<Exception> callback) {

		if (this.delayedAreaState == null)
			this.send(callback);
		else
			switch (this.delayedAreaState) {
			case No:
				this.delayedAreaState = INAPDialogImpl.DelayedAreaState.Continue;
				break;
			default:
				break;
			}
	}

	@Override
	public void close(boolean prearrangedEnd, TaskCallback<Exception> callback) {

		switch (this.tcapDialog.getState()) {
		case InitialReceived:
			ApplicationContextName acn = this.inapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createApplicationContextName(this.appCntx.getOID());

			INAPDialogState oldState = getState();
			this.setState(INAPDialogState.EXPUNGED);
			if (prearrangedEnd) {
				// we do not send any data in a prearrangedEnd case
				if (this.tcapDialog != null)
					this.tcapDialog.release();
			} else
				try {
					this.inapProviderImpl.fireTCEnd(this.getTcapDialog(), prearrangedEnd, acn,
							this.getReturnMessageOnError(), callback);
				} catch (Exception ex) {
					this.state = oldState;
					setUserObject(getUserObject());
				}

			break;

		case Active:
			oldState = getState();
			this.setState(INAPDialogState.EXPUNGED);
			if (prearrangedEnd) {
				// we do not send any data in a prearrangedEnd case
				if (this.tcapDialog != null)
					this.tcapDialog.release();
			} else
				try {
					this.inapProviderImpl.fireTCEnd(this.getTcapDialog(), prearrangedEnd, null,
							this.getReturnMessageOnError(), callback);
				} catch (Exception ex) {
					this.state = oldState;
					setUserObject(getUserObject());
				}

			break;

		case Idle:
			callback.onError(new INAPException(
					"Awaiting TC-BEGIN to be sent, can not send another dialog initiating primitive!"));
			return;
		case InitialSent: // we have sent TC-BEGIN already, need to wait
			if (prearrangedEnd) {
				// we do not send any data in a prearrangedEnd case
				if (this.tcapDialog != null)
					this.tcapDialog.release();
				this.setState(INAPDialogState.EXPUNGED);
				return;
			} else {
				callback.onError(new INAPException(
						"Awaiting TC-BEGIN response, can not send another dialog initiating primitive!"));
				return;
			}
		case Expunged: // dialog has been terminated on TC level, cant send
			callback.onError(new INAPException("Dialog has been terminated, can not send primitives!"));
			return;
		}
	}

	@Override
	public void closeDelayed(boolean prearrangedEnd, TaskCallback<Exception> callback) {

		if (this.delayedAreaState == null)
			this.close(prearrangedEnd, callback);
		else if (prearrangedEnd) {
			switch (this.delayedAreaState) {
			case No:
			case Continue:
			case End:
				this.delayedAreaState = INAPDialogImpl.DelayedAreaState.PrearrangedEnd;
				break;
			default:
				break;
			}
			callback.onSuccess();
		} else {
			switch (this.delayedAreaState) {
			case No:
			case Continue:
				this.delayedAreaState = INAPDialogImpl.DelayedAreaState.End;
				break;
			default:
				break;
			}
			callback.onSuccess();
		}
	}

	@Override
	public void abort(INAPUserAbortReason abortReason, TaskCallback<Exception> callback) {

		// Dialog is not started or has expunged - we need not send
		// TC-U-ABORT,
		// only Dialog removing
		if (this.getState() == INAPDialogState.EXPUNGED || this.getState() == INAPDialogState.IDLE) {
			this.setState(INAPDialogState.EXPUNGED);
			callback.onSuccess();
			return;
		}

		INAPDialogState oldState = getState();
		this.setState(INAPDialogState.EXPUNGED);

		TaskCallback<Exception> proxyCallback = new TaskCallback<Exception>() {
			@Override
			public void onSuccess() {
				callback.onSuccess();

			}

			@Override
			public void onError(Exception exception) {
				state = oldState;
				setUserObject(getUserObject());
				callback.onError(exception);
			}
		};

		// this.setNormalDialogShutDown();
		this.inapProviderImpl.fireTCAbort(this.getTcapDialog(), INAPGeneralAbortReason.UserSpecific, abortReason,
				this.getReturnMessageOnError(), proxyCallback);
	}

	@Override
	public void processInvokeWithoutAnswer(Integer invokeId) {
		this.tcapDialog.processInvokeWithoutAnswer(invokeId);
	}

	@Override
	public Integer sendDataComponent(Integer invokeId, Integer linkedId, InvokeClass invokeClass, Long customTimeout,
			Integer operationCode, INAPMessage param, Boolean isRequest, Boolean isLastResponse) throws INAPException {
		try {
			if (param != null)
				inapProviderImpl.getStack().newMessageSent(param.getMessageType().name(),
						this.tcapDialog.getNetworkId());

			if (operationCode != null && (param != null || isRequest))
				return this.tcapDialog.sendData(invokeId, linkedId, invokeClass, customTimeout,
						TcapFactory.createLocalOperationCode(operationCode), param, isRequest, isLastResponse);
			else
				return this.tcapDialog.sendData(invokeId, linkedId, invokeClass, customTimeout, null, param, isRequest,
						isLastResponse);
		} catch (TCAPSendException | TCAPException e) {
			throw new INAPException(e.getMessage(), e);
		}
	}

	@Override
	public void sendErrorComponent(Integer invokeId, INAPErrorMessage mem) throws INAPException {
		try {
			if (mem != null)
				inapProviderImpl.getStack().newErrorSent(INAPErrorCode.translate(mem, mem.getErrorCode()),
						this.tcapDialog.getNetworkId());

			if (mem instanceof INAPErrorMessageParameterless)
				this.tcapDialog.sendError(invokeId, TcapFactory.createLocalErrorCode(mem.getErrorCode()), null);
			else
				this.tcapDialog.sendError(invokeId, TcapFactory.createLocalErrorCode(mem.getErrorCode()), mem);

		} catch (TCAPSendException e) {
			throw new INAPException(e.getMessage(), e);
		}
	}

	@Override
	public void sendRejectComponent(Integer invokeId, Problem problem) throws INAPException {
		try {
			this.tcapDialog.sendReject(invokeId, problem);

		} catch (TCAPSendException e) {
			throw new INAPException(e.getMessage(), e);
		}
	}

	@Override
	public void resetInvokeTimer(Integer invokeId) throws INAPException {

		try {
			this.getTcapDialog().resetTimer(invokeId);
		} catch (TCAPException e) {
			throw new INAPException("TCAPException occure: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean cancelInvocation(Integer invokeId) throws INAPException {
		try {
			return this.getTcapDialog().cancelInvocation(invokeId);
		} catch (TCAPException e) {
			throw new INAPException("TCAPException occure: " + e.getMessage(), e);
		}
	}

	@Override
	public Externalizable getUserObject() {
		Externalizable tcapObject = this.tcapDialog.getUserObject();
		if (tcapObject == null)
			return null;
		else if (!(tcapObject instanceof INAPUserObject))
			return tcapObject;

		return ((INAPUserObject) this.tcapDialog.getUserObject()).getRealObject();
	}

	@Override
	public void setUserObject(Externalizable userObject) {
		this.tcapDialog.setUserObject(new INAPUserObject(state, returnMessageOnError, appCntx, userObject));
	}

	@Override
	public INAPApplicationContext getApplicationContext() {
		return appCntx;
	}

	@Override
	public int getMaxUserDataLength() {
		return this.getTcapDialog().getMaxUserDataLength();
	}

	@Override
	public int getMessageUserDataLengthOnSend() throws INAPException {

		try {
			switch (this.tcapDialog.getState()) {
			case Idle:
				ApplicationContextName acn = this.inapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
						.createApplicationContextName(this.appCntx.getOID());

				TCBeginRequest tb = this.inapProviderImpl.encodeTCBegin(this.getTcapDialog(), acn);
				return tcapDialog.getDataLength(tb);

			case Active:
				// Its Active send TC-CONTINUE

				TCContinueRequest tc = this.inapProviderImpl.encodeTCContinue(this.getTcapDialog(), null);
				return tcapDialog.getDataLength(tc);

			case InitialReceived:
				// Its first Reply to TC-Begin

				ApplicationContextName acn1 = this.inapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
						.createApplicationContextName(this.appCntx.getOID());

				tc = this.inapProviderImpl.encodeTCContinue(this.getTcapDialog(), acn1);
				return tcapDialog.getDataLength(tc);
			default:
				break;
			}
		} catch (TCAPSendException e) {
			throw new INAPException("TCAPSendException when getMessageUserDataLengthOnSend", e);
		}

		throw new INAPException("Bad TCAP Dialog state: " + this.tcapDialog.getState());
	}

	@Override
	public int getMessageUserDataLengthOnClose(boolean prearrangedEnd) throws INAPException {
		if (prearrangedEnd)
			// we do not send any data in prearrangedEnd dialog termination
			return 0;

		try {
			switch (this.tcapDialog.getState()) {
			case InitialReceived:
				ApplicationContextName acn = this.inapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
						.createApplicationContextName(this.appCntx.getOID());

				TCEndRequest te = this.inapProviderImpl.encodeTCEnd(this.getTcapDialog(), prearrangedEnd, acn);
				return tcapDialog.getDataLength(te);

			case Active:
				te = this.inapProviderImpl.encodeTCEnd(this.getTcapDialog(), prearrangedEnd, null);
				return tcapDialog.getDataLength(te);
			default:
				break;
			}
		} catch (TCAPSendException e) {
			throw new INAPException("TCAPSendException when getMessageUserDataLengthOnSend", e);
		}

		throw new INAPException("Bad TCAP Dialog state: " + this.tcapDialog.getState());
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("INAPDialog: LocalDialogId=").append(this.getLocalDialogId()).append(" RemoteDialogId=")
				.append(this.getRemoteDialogId()).append(" INAPDialogState=").append(this.getState())
				.append(" INAPApplicationContext=").append(this.appCntx).append(" TCAPDialogState=")
				.append(this.tcapDialog.getState());
		return sb.toString();
	}

	protected enum DelayedAreaState {
		No, Continue, End, PrearrangedEnd;
	}

	@Override
	public long getIdleTaskTimeout() {
		return tcapDialog.getIdleTaskTimeout();
	}

	@Override
	public void setIdleTaskTimeout(long idleTaskTimeoutMs) {
		tcapDialog.setIdleTaskTimeout(idleTaskTimeoutMs);
	}

	@Override
	public int getTimerCircuitSwitchedCallControlShort() {
		return inapStackConfigurationManagement.getTimerCircuitSwitchedCallControlShort();
	}

	@Override
	public int getTimerCircuitSwitchedCallControlMedium() {
		return inapStackConfigurationManagement.getTimerCircuitSwitchedCallControlMedium();
	}

	@Override
	public int getTimerCircuitSwitchedCallControlLong() {
		return inapStackConfigurationManagement.getTimerCircuitSwitchedCallControlLong();
	}
}
