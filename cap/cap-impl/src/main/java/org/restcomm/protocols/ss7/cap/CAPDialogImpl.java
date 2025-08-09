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

package org.restcomm.protocols.ss7.cap;

import java.io.Externalizable;

import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPMessage;
import org.restcomm.protocols.ss7.cap.api.CAPServiceBase;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPDialogState;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorCode;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageParameterless;
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
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public abstract class CAPDialogImpl implements CAPDialog {
	private static final long serialVersionUID = 1L;

	protected Dialog tcapDialog = null;
	protected CAPProviderImpl capProviderImpl = null;
	protected CAPServiceBase capService = null;

	// Application Context of this Dialog
	protected CAPApplicationContext appCntx;

	protected CAPGprsReferenceNumber gprsReferenceNumber = null;
	protected CAPGprsReferenceNumber receivedGprsReferenceNumber;

	protected CAPDialogState state = CAPDialogState.IDLE;

	// protected boolean normalDialogShutDown = false;

	// private Set<Long> incomingInvokeList = new HashSet<Long>();

	boolean returnMessageOnError = false;
	protected MessageType tcapMessageType;
	protected DelayedAreaState delayedAreaState;
	private CAPStackConfigurationManagement capStackConfigurationManagement;

	protected CAPDialogImpl(CAPApplicationContext appCntx, Dialog tcapDialog, CAPProviderImpl capProviderImpl,
			CAPServiceBase capService) {
		this.appCntx = appCntx;
		this.tcapDialog = tcapDialog;
		this.capProviderImpl = capProviderImpl;
		this.capService = capService;
		this.capStackConfigurationManagement = new CAPStackConfigurationManagement();
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
	public CAPServiceBase getService() {
		return this.capService;
	}

	public Dialog getTcapDialog() {
		return tcapDialog;
	}

	@Override
	public void release() {
		// this.setNormalDialogShutDown();
		this.setState(CAPDialogState.EXPUNGED);

		if (this.tcapDialog != null)
			this.tcapDialog.release();
	}

	/**
	 * Setting that the CAP Dialog is normally shutting down - to prevent performing
	 * onDialogReleased()
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
	public CAPDialogState getState() {
		return state;
	}

	protected void setState(CAPDialogState newState) {
		if (this.state == CAPDialogState.EXPUNGED)
			return;

		this.state = newState;
		setUserObject(getUserObject());
	}

	@Override
	public void setGprsReferenceNumber(CAPGprsReferenceNumber gprsReferenceNumber) {
		this.gprsReferenceNumber = gprsReferenceNumber;
	}

	@Override
	public CAPGprsReferenceNumber getGprsReferenceNumber() {
		return this.gprsReferenceNumber;
	}

	@Override
	public CAPGprsReferenceNumber getReceivedGprsReferenceNumber() {
		return receivedGprsReferenceNumber;
	}

	@Override
	public void send(TaskCallback<Exception> callback) {

		switch (this.tcapDialog.getState()) {
		case Idle:
			ApplicationContextName acn = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createApplicationContextName(this.appCntx.getOID());

			this.setState(CAPDialogState.INITIAL_SENT);

			this.capProviderImpl.fireTCBegin(this.getTcapDialog(), acn, this.gprsReferenceNumber,
					this.getReturnMessageOnError(), callback);
			this.gprsReferenceNumber = null;
			break;

		case Active:
			// Its Active send TC-CONTINUE

			this.capProviderImpl.fireTCContinue(this.getTcapDialog(), null, null, this.getReturnMessageOnError(),
					callback);
			break;

		case InitialReceived:
			// Its first Reply to TC-Begin

			ApplicationContextName acn1 = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createApplicationContextName(this.appCntx.getOID());

			CAPDialogState oldState = this.getState();
			this.setState(CAPDialogState.ACTIVE);

			this.capProviderImpl.fireTCContinue(this.getTcapDialog(), acn1, this.gprsReferenceNumber,
					this.getReturnMessageOnError(), new TaskCallback<Exception>() {
						@Override
						public void onSuccess() {
							CAPDialogImpl.this.gprsReferenceNumber = null;
							callback.onSuccess();
						}

						@Override
						public void onError(Exception exception) {
							CAPDialogImpl.this.state = oldState;
							setUserObject(getUserObject());
						}
					});

			break;

		case InitialSent: // we have sent TC-BEGIN already, need to wait
			callback.onError(
					new CAPException("Awaiting TC-BEGIN response, can not send another dialog initiating primitive!"));
			break;
		case Expunged: // dialog has been terminated on TC level, cant send
			callback.onError(new CAPException("Dialog has been terminated, can not send primitives!"));
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
				this.delayedAreaState = CAPDialogImpl.DelayedAreaState.Continue;
				callback.onSuccess();
				break;
			default:
				callback.onSuccess();
				break;
			}
	}

	@Override
	public void close(boolean prearrangedEnd, TaskCallback<Exception> callback) {

		switch (this.tcapDialog.getState()) {
		case InitialReceived:
			ApplicationContextName acn = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
					.createApplicationContextName(this.appCntx.getOID());

			CAPDialogState oldState = getState();
			this.setState(CAPDialogState.EXPUNGED);
			if (prearrangedEnd) {
				// we do not send any data in a prearrangedEnd case
				if (this.tcapDialog != null)
					this.tcapDialog.release();
			} else
				this.capProviderImpl.fireTCEnd(this.getTcapDialog(), prearrangedEnd, acn, this.gprsReferenceNumber,
						this.getReturnMessageOnError(), new TaskCallback<Exception>() {
							@Override
							public void onSuccess() {
								callback.onSuccess();
							}

							@Override
							public void onError(Exception ex) {
								CAPDialogImpl.this.state = oldState;
								setUserObject(getUserObject());

								callback.onError(ex);
							}
						});
			this.gprsReferenceNumber = null;

			break;

		case Active:
			oldState = getState();
			this.setState(CAPDialogState.EXPUNGED);
			if (prearrangedEnd) {
				// we do not send any data in a prearrangedEnd case
				if (this.tcapDialog != null)
					this.tcapDialog.release();
			} else
				this.capProviderImpl.fireTCEnd(this.getTcapDialog(), prearrangedEnd, null, null,
						this.getReturnMessageOnError(), new TaskCallback<Exception>() {
							@Override
							public void onSuccess() {
								callback.onSuccess();
							}

							@Override
							public void onError(Exception ex) {
								CAPDialogImpl.this.state = oldState;
								setUserObject(getUserObject());

								callback.onError(ex);
							}
						});

			break;

		case Idle:
			callback.onError(new CAPException(
					"Awaiting TC-BEGIN to be sent, can not send another dialog initiating primitive!"));
			break;
		case InitialSent: // we have sent TC-BEGIN already, need to wait
			if (prearrangedEnd) {
				// we do not send any data in a prearrangedEnd case
				if (this.tcapDialog != null)
					this.tcapDialog.release();
				this.setState(CAPDialogState.EXPUNGED);
				callback.onSuccess();
				return;
			} else
				callback.onError(new CAPException(
						"Awaiting TC-BEGIN response, can not send another dialog initiating primitive!"));
			break;
		case Expunged: // dialog has been terminated on TC level, cant send
			callback.onError(new CAPException("Dialog has been terminated, can not send primitives!"));
			break;
		}
	}

	@Override
	public void closeDelayed(boolean prearrangedEnd, TaskCallback<Exception> callback) {

		if (this.delayedAreaState == null)
			this.close(prearrangedEnd, callback);
		else if (prearrangedEnd)
			switch (this.delayedAreaState) {
			case No:
			case Continue:
			case End:
				this.delayedAreaState = CAPDialogImpl.DelayedAreaState.PrearrangedEnd;
				callback.onSuccess();
				break;
			default:
				callback.onSuccess();
				break;
			}
		else
			switch (this.delayedAreaState) {
			case No:
			case Continue:
				this.delayedAreaState = CAPDialogImpl.DelayedAreaState.End;
				callback.onSuccess();
				break;
			default:
				callback.onSuccess();
				break;
			}
	}

	@Override
	public void abort(CAPUserAbortReason abortReason, TaskCallback<Exception> callback) {

		// Dialog is not started or has expunged - we need not send
		// TC-U-ABORT,
		// only Dialog removing
		if (this.getState() == CAPDialogState.EXPUNGED || this.getState() == CAPDialogState.IDLE) {
			this.setState(CAPDialogState.EXPUNGED);
			callback.onSuccess();
			return;
		}

		CAPDialogState oldState = getState();
		this.setState(CAPDialogState.EXPUNGED);

		// this.setNormalDialogShutDown();
		this.capProviderImpl.fireTCAbort(this.getTcapDialog(), CAPGeneralAbortReason.UserSpecific, abortReason,
				this.getReturnMessageOnError(), new TaskCallback<Exception>() {
					@Override
					public void onSuccess() {
						callback.onSuccess();
					}

					@Override
					public void onError(Exception ex) {
						CAPDialogImpl.this.state = oldState;
						setUserObject(getUserObject());

						callback.onError(ex);
					}
				});
	}

	@Override
	public void processInvokeWithoutAnswer(Integer invokeId) {
		this.tcapDialog.processInvokeWithoutAnswer(invokeId);
	}

	@Override
	public Integer sendDataComponent(Integer invokeId, Integer linkedId, InvokeClass invokeClass, Long customTimeout,
			Integer operationCode, CAPMessage param, Boolean isRequest, Boolean isLastResponse) throws CAPException {
		try {
			if (param != null)
				capProviderImpl.getCAPStack().newMessageSent(param.getMessageType().name(), getNetworkId());

			if (operationCode != null && (param != null || isRequest))
				return this.tcapDialog.sendData(invokeId, linkedId, invokeClass, customTimeout,
						TcapFactory.createLocalOperationCode(operationCode), param, isRequest, isLastResponse);
			else
				return this.tcapDialog.sendData(invokeId, linkedId, invokeClass, customTimeout, null, param, isRequest,
						isLastResponse);
		} catch (TCAPSendException | TCAPException e) {
			throw new CAPException(e.getMessage(), e);
		}
	}

	@Override
	public void sendErrorComponent(Integer invokeId, CAPErrorMessage mem) throws CAPException {
		try {
			if (mem != null)
				capProviderImpl.getCAPStack().newErrorSent(CAPErrorCode.translate(mem.getErrorCode()), getNetworkId());

			if (mem instanceof CAPErrorMessageParameterless)
				this.tcapDialog.sendError(invokeId, TcapFactory.createLocalErrorCode(mem.getErrorCode()), null);
			else
				this.tcapDialog.sendError(invokeId, TcapFactory.createLocalErrorCode(mem.getErrorCode()), mem);

		} catch (TCAPSendException e) {
			throw new CAPException(e.getMessage(), e);
		}
	}

	@Override
	public void sendRejectComponent(Integer invokeId, Problem problem) throws CAPException {
		try {
			this.tcapDialog.sendReject(invokeId, problem);

		} catch (TCAPSendException e) {
			throw new CAPException(e.getMessage(), e);
		}
	}

	@Override
	public void resetInvokeTimer(Integer invokeId) throws CAPException {

		try {
			this.getTcapDialog().resetTimer(invokeId);
		} catch (TCAPException e) {
			throw new CAPException("TCAPException occure: " + e.getMessage(), e);
		}
	}

	@Override
	public boolean cancelInvocation(Integer invokeId) throws CAPException {
		try {
			return this.getTcapDialog().cancelInvocation(invokeId);
		} catch (TCAPException e) {
			throw new CAPException("TCAPException occure: " + e.getMessage(), e);
		}
	}

	@Override
	public Externalizable getUserObject() {
		Externalizable tcapObject = this.tcapDialog.getUserObject();
		if (tcapObject == null)
			return null;
		else if (!(tcapObject instanceof CAPUserObject))
			return tcapObject;

		return ((CAPUserObject) this.tcapDialog.getUserObject()).getRealObject();
	}

	@Override
	public void setUserObject(Externalizable userObject) {
		this.tcapDialog.setUserObject(new CAPUserObject(state, returnMessageOnError, appCntx, userObject));
	}

	@Override
	public CAPApplicationContext getApplicationContext() {
		return appCntx;
	}

	@Override
	public int getMaxUserDataLength() {
		return this.getTcapDialog().getMaxUserDataLength();
	}

	@Override
	public int getMessageUserDataLengthOnSend() throws CAPException {

		try {
			switch (this.tcapDialog.getState()) {
			case Idle:
				ApplicationContextName acn = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
						.createApplicationContextName(this.appCntx.getOID());

				TCBeginRequest tb = this.capProviderImpl.encodeTCBegin(this.getTcapDialog(), acn,
						this.gprsReferenceNumber);
				return tcapDialog.getDataLength(tb);

			case Active:
				// Its Active send TC-CONTINUE

				TCContinueRequest tc = this.capProviderImpl.encodeTCContinue(this.getTcapDialog(), null, null);
				return tcapDialog.getDataLength(tc);

			case InitialReceived:
				// Its first Reply to TC-Begin

				ApplicationContextName acn1 = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
						.createApplicationContextName(this.appCntx.getOID());

				tc = this.capProviderImpl.encodeTCContinue(this.getTcapDialog(), acn1, this.gprsReferenceNumber);
				return tcapDialog.getDataLength(tc);
			default:
				break;
			}
		} catch (TCAPSendException e) {
			throw new CAPException("TCAPSendException when getMessageUserDataLengthOnSend", e);
		}

		throw new CAPException("Bad TCAP Dialog state: " + this.tcapDialog.getState());
	}

	@Override
	public int getMessageUserDataLengthOnClose(boolean prearrangedEnd) throws CAPException {
		if (prearrangedEnd)
			// we do not send any data in prearrangedEnd dialog termination
			return 0;

		try {
			switch (this.tcapDialog.getState()) {
			case InitialReceived:
				ApplicationContextName acn = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
						.createApplicationContextName(this.appCntx.getOID());

				TCEndRequest te = this.capProviderImpl.encodeTCEnd(this.getTcapDialog(), prearrangedEnd, acn,
						this.gprsReferenceNumber);
				return tcapDialog.getDataLength(te);

			case Active:
				te = this.capProviderImpl.encodeTCEnd(this.getTcapDialog(), prearrangedEnd, null, null);
				return tcapDialog.getDataLength(te);
			default:
				break;
			}
		} catch (TCAPSendException e) {
			throw new CAPException("TCAPSendException when getMessageUserDataLengthOnSend", e);
		}

		throw new CAPException("Bad TCAP Dialog state: " + this.tcapDialog.getState());
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("CAPDialog: LocalDialogId=").append(this.getLocalDialogId()).append(" RemoteDialogId=")
				.append(this.getRemoteDialogId()).append(" CAPDialogState=").append(this.getState())
				.append(" CAPApplicationContext=").append(this.appCntx).append(" TCAPDialogState=")
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
		return capStackConfigurationManagement.getTimerCircuitSwitchedCallControlShort();
	}

	@Override
	public int getTimerCircuitSwitchedCallControlMedium() {
		return capStackConfigurationManagement.getTimerCircuitSwitchedCallControlMedium();
	}

	@Override
	public int getTimerCircuitSwitchedCallControlLong() {
		return capStackConfigurationManagement.getTimerCircuitSwitchedCallControlLong();
	}

	@Override
	public int getTimerSmsShort() {
		return capStackConfigurationManagement.getTimerSmsShort();
	}

	@Override
	public int getTimerGprsShort() {
		return capStackConfigurationManagement.getTimerGprsShort();
	}
}
