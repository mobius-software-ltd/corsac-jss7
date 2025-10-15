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

package org.restcomm.protocols.ss7.tcapAnsi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Return;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCConversationMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCResponseMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.OperationState;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.TRPseudoState;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortRequest;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcapAnsi.asn.Utils;

import com.mobius.software.common.dal.timers.RunnableTimer;
import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.common.dal.timers.WorkerPool;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;

import io.netty.buffer.ByteBuf;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class DialogImpl implements Dialog {

	private static final long serialVersionUID = 1L;

	// timeout of remove task after TC_END
	// private static final int _REMOVE_TIMEOUT = 30000;

	private static final Logger logger = LogManager.getLogger(DialogImpl.class);

	private Object userObject;

	// values for timer timeouts
	// private long removeTaskTimeout = _REMOVE_TIMEOUT;
	private long idleTaskTimeout;

	// sent/received acn, holds last acn/ui.
	private ApplicationContext lastACN;
	private UserInformation lastUI; // optional

	private Long localTransactionIdObject;
	private long localTransactionId;
	private Long remoteTransactionIdObject;

	private ProtocolVersion protocolVersion;
	private SccpAddress localAddress;
	private SccpAddress remoteAddress;
	private int localSsn;

	private AtomicReference<IdleTimer> idleTimer = new AtomicReference<IdleTimer>();

	private AtomicBoolean idleTimerActionTaken = new AtomicBoolean(false);
	private AtomicBoolean idleTimerInvoked = new AtomicBoolean(false);
	private AtomicReference<TRPseudoState> state = new AtomicReference<TRPseudoState>(TRPseudoState.Idle);
	private boolean structured = true;
	// invokde ID space :)
	private static final boolean _INVOKEID_TAKEN = true;
	private static final boolean _INVOKEID_FREE = false;
	private static final int _INVOKE_TABLE_SHIFT = 128;

	private boolean[] invokeIDTable = new boolean[256];
	private int freeCount = invokeIDTable.length;
	private int lastInvokeIdIndex = _INVOKE_TABLE_SHIFT - 1;

	// only originating side keeps FSM, see: Q.771 - 3.1.5
	protected Invoke[] operationsSent = new Invoke[invokeIDTable.length];
	protected Invoke[] operationsSentA = new Invoke[invokeIDTable.length];
	private ConcurrentHashMap<Long, Long> incomingInvokeList = new ConcurrentHashMap<Long, Long>();
	private WorkerPool workerPool;

	// scheduled components list
	private List<Component> scheduledComponentList = new ArrayList<Component>();
	private TCAPProviderImpl provider;

	private int seqControl;

	// If the Dialogue Portion is sent in TCBegin message, the first received
	// Continue message should have the Dialogue Portion too
	private boolean dpSentInBegin = false;

	private int networkId;
	private boolean isSwapTcapIdBytes;

	private ASNParser dialogParser;

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	private static int getIndexFromInvokeId(Long l) {
		int tmp = l.intValue();
		return tmp + _INVOKE_TABLE_SHIFT;
	}

	private static Long getInvokeIdFromIndex(int index) {
		int tmp = index - _INVOKE_TABLE_SHIFT;
		return new Long(tmp);
	}

	/**
	 * Creating a Dialog for normal mode
	 *
	 * @param localAddress
	 * @param remoteAddress
	 * @param origTransactionId
	 * @param structured
	 * @param queuedTasks
	 * @param provider
	 * @param seqControl
	 * @param previewMode
	 */
	protected DialogImpl(SccpAddress localAddress, SccpAddress remoteAddress, Long origTransactionId,
			boolean structured, WorkerPool workerPool, TCAPProviderImpl provider, int seqControl,
			ASNParser dialogParser) {
		super();

		this.localAddress = localAddress;
		this.remoteAddress = remoteAddress;
		if (origTransactionId != null) {
			this.localTransactionIdObject = origTransactionId;
			this.localTransactionId = origTransactionId;
		}
		this.workerPool = workerPool;
		this.provider = provider;
		this.structured = structured;

		this.seqControl = seqControl;

		TCAPStack stack = this.provider.getStack();
		this.idleTaskTimeout = stack.getDialogIdleTimeout();
		this.isSwapTcapIdBytes = stack.getSwapTcapIdBytes();

		this.dialogParser = dialogParser;

		// start
		startIdleTimer();
	}

	@Override
	public void release() {
		for (int i = 0; i < this.operationsSent.length; i++) {
			Invoke invokeImpl = this.operationsSent[i];
			if (invokeImpl != null)
				invokeImpl.setState(OperationState.Idle);
			// TODO whether to call operationTimedOut or not is still not clear
			// operationTimedOut(invokeImpl);
		}

		this.setState(TRPseudoState.Expunged);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#getDialogId()
	 */
	@Override
	public Long getLocalDialogId() {

		return localTransactionIdObject;
	}

	/**
	 *
	 */
	@Override
	public Long getRemoteDialogId() {
		return this.remoteTransactionIdObject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#getNewInvokeId()
	 */
	@Override
	public Long getNewInvokeId() throws TCAPException {
		if (this.freeCount == 0)
			throw new TCAPException("No free invokeId");

		int tryCnt = 0;
		while (true) {
			if (++this.lastInvokeIdIndex >= this.invokeIDTable.length)
				this.lastInvokeIdIndex = 0;
			if (this.invokeIDTable[this.lastInvokeIdIndex] == _INVOKEID_FREE) {
				freeCount--;
				this.invokeIDTable[this.lastInvokeIdIndex] = _INVOKEID_TAKEN;
				return getInvokeIdFromIndex(this.lastInvokeIdIndex);
			}
			if (++tryCnt >= 256)
				throw new TCAPException("No free invokeId");
		}
	}

	@Override
	public int getNetworkId() {
		return networkId;
	}

	@Override
	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#cancelInvocation
	 * (java.lang.Long)
	 */
	@Override
	public boolean cancelInvocation(Long invokeId) throws TCAPException {
		int index = getIndexFromInvokeId(invokeId);
		if (index < 0 || index >= this.operationsSent.length)
			throw new TCAPException("Wrong invoke id passed.");

		// lookup through send buffer.
		for (index = 0; index < this.scheduledComponentList.size(); index++) {
			Component cr = this.scheduledComponentList.get(index);
			Long correlationID = null;

			if (cr.getType() == ComponentType.InvokeNotLast || cr.getType() == ComponentType.InvokeLast)
				correlationID = cr.getCorrelationId();

			if (correlationID != null && correlationID.equals(invokeId)) {
				this.scheduledComponentList.remove(index);
				((Invoke) cr).stopTimer();
				((Invoke) cr).setState(OperationState.Idle);
				return true;
			}
		}

		return false;
	}

	private void freeInvokeId(Long l) {
		int index = getIndexFromInvokeId(l);
		if (this.invokeIDTable[index] == _INVOKEID_TAKEN)
			this.freeCount++;
		this.invokeIDTable[index] = _INVOKEID_FREE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#getRemoteAddress()
	 */
	@Override
	public SccpAddress getRemoteAddress() {

		return this.remoteAddress;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#getLocalAddress()
	 */
	@Override
	public SccpAddress getLocalAddress() {

		return this.localAddress;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#getSsn()
	 */
	@Override
	public int getLocalSsn() {
		return localSsn;
	}

	public void setLocalSsn(int newSsn) {
		localSsn = newSsn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#isEstabilished()
	 */
	@Override
	public boolean isEstabilished() {
		return this.state.get() == TRPseudoState.Active;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#isStructured()
	 */
	@Override
	public boolean isStructured() {

		return this.structured;
	}

	@Override
	public void keepAlive() {
		if (this.idleTimerInvoked.get())
			this.idleTimerActionTaken.set(true);
	}

	/**
	 * @return the acn
	 */
	@Override
	public ApplicationContext getApplicationContextName() {
		return lastACN;
	}

	/**
	 * @return the ui
	 */
	@Override
	public UserInformation getUserInformation() {
		return lastUI;
	}

	/**
	 * Adding the new incoming invokeId into incomingInvokeList list
	 *
	 * @param invokeId
	 * @return false: failure - this invokeId already present in the list
	 */
	private boolean addIncomingInvokeId(Long invokeId) {
		if (invokeId == null)
			return false;

		Long oldValue = this.incomingInvokeList.putIfAbsent(invokeId, invokeId);
		return (oldValue == null);
	}

	private void removeIncomingInvokeId(Long invokeId) {
		if (invokeId != null)
			this.incomingInvokeList.remove(invokeId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.restcomm
	 * .protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest)
	 */
	@Override
	public void send(TCQueryRequest event, TaskCallback<Exception> callback) {

		if (this.state.get() != TRPseudoState.Idle) {
			callback.onError(new TCAPSendException("Can not send Begin in this state: " + this.state));
			return;
		}

		if (!this.isStructured()) {
			callback.onError(new TCAPSendException("Unstructured dialogs do not use Begin"));
			return;
		}

		this.idleTimerActionTaken.set(true);
		restartIdleTimer();
		TCQueryMessage tcbm = TcapFactory.createTCQueryMessage(event.getDialogTermitationPermission());

		// build DP
		tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionId, this.isSwapTcapIdBytes));

		if (event.getApplicationContext() != null || event.getConfidentiality() != null
				|| event.getSecurityContext() != null || event.getUserInformation() != null) {
			this.dpSentInBegin = true;
			this.lastACN = event.getApplicationContext();
			if (event.getUserInformation() != null)
				this.lastUI = event.getUserInformation();

			DialogPortion dp = TcapFactory.createDialogPortion();
			dp.setProtocolVersion(TcapFactory.createProtocolVersionFull());
			dp.setApplicationContext(event.getApplicationContext());
			dp.setUserInformation(event.getUserInformation());

			if (event.getSecurityContext() != null)
				dp.setSecurityContext(event.getSecurityContext());

			dp.setConfidentiality(event.getConfidentiality());

			tcbm.setDialogPortion(dp);
		}

		// now comps
		if (this.scheduledComponentList.size() > 0) {
			List<Component> componentsToSend = new ArrayList<Component>(this.scheduledComponentList.size());
			this.prepareComponents(componentsToSend);
			ComponentPortion cPortion = TcapFactory.createComponentPortion();
			cPortion.setComponents(componentsToSend);
			tcbm.setComponent(cPortion);
		}

		ByteBuf buffer;
		try {
			buffer = dialogParser.encode(tcbm);
		} catch (ASNException e) {
			callback.onError(new TCAPSendException("Failed to send TC-Query message: " + e));
			return;
		}
		provider.getStack().newMessageSent(tcbm.getName(), buffer.readableBytes(), this.getNetworkId());
		this.setState(TRPseudoState.InitialSent);

		this.provider.send(this, buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
				this.seqControl, this.getNetworkId(), this.localSsn, callback);
		this.scheduledComponentList.clear();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.restcomm
	 * .protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest)
	 */
	@Override
	public void send(TCConversationRequest event, TaskCallback<Exception> callback) {
		if (!this.isStructured()) {
			callback.onError(new TCAPSendException("Unstructured dialogs do not use Continue"));
			return;
		}

		if (this.state.get() == TRPseudoState.InitialReceived) {
			this.idleTimerActionTaken.set(true);
			restartIdleTimer();
			TCConversationMessage tcbm = TcapFactory
					.createTCConversationMessage(event.getDialogTermitationPermission());
			tcbm.setOriginatingTransactionId(
					Utils.encodeTransactionId(this.localTransactionId, this.isSwapTcapIdBytes));
			tcbm.setDestinationTransactionId(
					Utils.encodeTransactionId(this.remoteTransactionIdObject, this.isSwapTcapIdBytes));
			// local address may change, lets check it;
			if (event.getOriginatingAddress() != null && !event.getOriginatingAddress().equals(this.localAddress))
				this.localAddress = event.getOriginatingAddress();

			if (event.getApplicationContext() != null || event.getConfidentiality() != null
					|| event.getSecurityContext() != null || event.getUserInformation() != null) {
				// set dialog portion
				DialogPortion dp = TcapFactory.createDialogPortion();
				dp.setProtocolVersion(TcapFactory.createProtocolVersionFull());
				dp.setApplicationContext(event.getApplicationContext());
				dp.setUserInformation(event.getUserInformation());
				dp.setSecurityContext(event.getSecurityContext());
				dp.setConfidentiality(event.getConfidentiality());

				tcbm.setDialogPortion(dp);
			}
			if (this.scheduledComponentList.size() > 0) {
				List<Component> componentsToSend = new ArrayList<Component>(this.scheduledComponentList.size());
				this.prepareComponents(componentsToSend);
				ComponentPortion cPortion = TcapFactory.createComponentPortion();
				cPortion.setComponents(componentsToSend);
				tcbm.setComponent(cPortion);

			}

			ByteBuf buffer;
			try {
				buffer = dialogParser.encode(tcbm);
			} catch (ASNException e) {
				callback.onError(new TCAPSendException("Failed to send TC-Continue message: " + e.getMessage(), e));
				return;
			}

			provider.getStack().newMessageSent(tcbm.getName(), buffer.readableBytes(), this.getNetworkId());
			this.provider.send(this, buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
					this.seqControl, this.getNetworkId(), this.localSsn, callback);
			this.setState(TRPseudoState.Active);
			this.scheduledComponentList.clear();

		} else if (state.get() == TRPseudoState.Active) {
			this.idleTimerActionTaken.set(true);
			restartIdleTimer();
			// in this we ignore acn and passed args(except qos)
			TCConversationMessage tcbm = TcapFactory
					.createTCConversationMessage(event.getDialogTermitationPermission());
			tcbm.setOriginatingTransactionId(
					Utils.encodeTransactionId(this.localTransactionId, this.isSwapTcapIdBytes));
			tcbm.setDestinationTransactionId(
					Utils.encodeTransactionId(this.remoteTransactionIdObject, this.isSwapTcapIdBytes));
			if (this.scheduledComponentList.size() > 0) {
				List<Component> componentsToSend = new ArrayList<Component>(this.scheduledComponentList.size());
				this.prepareComponents(componentsToSend);
				ComponentPortion cPortion = TcapFactory.createComponentPortion();
				cPortion.setComponents(componentsToSend);
				tcbm.setComponent(cPortion);

			}

			ByteBuf buffer;
			try {
				buffer = dialogParser.encode(tcbm);
			} catch (ASNException e) {
				callback.onError(new TCAPSendException("Failed to send TC-Continue message: " + e.getMessage(), e));
				return;
			}

			provider.getStack().newMessageSent(tcbm.getName(), buffer.readableBytes(), this.getNetworkId());
			this.provider.send(this, buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
					this.seqControl, this.getNetworkId(), this.localSsn, callback);
			this.scheduledComponentList.clear();
		} else
			callback.onError(new TCAPSendException("Wrong state: " + this.state));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.restcomm
	 * .protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest)
	 */
	@Override
	public void send(TCResponseRequest event, TaskCallback<Exception> callback) {

		if (!this.isStructured()) {
			callback.onError(new TCAPSendException("Unstructured dialogs do not use Response"));
			return;
		}

		TCResponseMessage tcbm = null;

		if (state.get() == TRPseudoState.InitialReceived) {
			// TC-END request primitive issued in response to a TC-BEGIN
			// indication primitive
			this.idleTimerActionTaken.set(true);
			stopIdleTimer();
			tcbm = TcapFactory.createTCResponseMessage();
			tcbm.setDestinationTransactionId(
					Utils.encodeTransactionId(this.remoteTransactionIdObject, this.isSwapTcapIdBytes));
			// local address may change, lets check it;
			if (event.getOriginatingAddress() != null && !event.getOriginatingAddress().equals(this.localAddress))
				this.localAddress = event.getOriginatingAddress();

			if (event.getApplicationContext() != null || event.getConfidentiality() != null
					|| event.getSecurityContext() != null || event.getUserInformation() != null) {
				// set dialog portion
				DialogPortion dp = TcapFactory.createDialogPortion();
				dp.setProtocolVersion(TcapFactory.createProtocolVersionFull());
				dp.setApplicationContext(event.getApplicationContext());
				dp.setUserInformation(event.getUserInformation());
				dp.setSecurityContext(event.getSecurityContext());
				dp.setConfidentiality(event.getConfidentiality());

				tcbm.setDialogPortion(dp);
			}
			if (this.scheduledComponentList.size() > 0) {
				List<Component> componentsToSend = new ArrayList<Component>(this.scheduledComponentList.size());
				this.prepareComponents(componentsToSend);
				ComponentPortion cPortion = TcapFactory.createComponentPortion();
				cPortion.setComponents(componentsToSend);
				tcbm.setComponent(cPortion);
			}

		} else if (state.get() == TRPseudoState.Active) {
			restartIdleTimer();
			tcbm = TcapFactory.createTCResponseMessage();

			tcbm.setDestinationTransactionId(
					Utils.encodeTransactionId(this.remoteTransactionIdObject, this.isSwapTcapIdBytes));
			if (this.scheduledComponentList.size() > 0) {
				List<Component> componentsToSend = new ArrayList<Component>(this.scheduledComponentList.size());
				this.prepareComponents(componentsToSend);
				ComponentPortion cPortion = TcapFactory.createComponentPortion();
				cPortion.setComponents(componentsToSend);
				tcbm.setComponent(cPortion);
			}

		} else {
			callback.onError(new TCAPSendException(String.format("State is not %s or %s: it is %s",
					TRPseudoState.Active, TRPseudoState.InitialReceived, this.state)));
			return;
		}

		ByteBuf buffer;
		try {
			buffer = dialogParser.encode(tcbm);
		} catch (ASNException e) {
			release();
			callback.onError(new TCAPSendException("Failed to send TC-Response message: " + e.getMessage(), e));
			return;
		}
		provider.getStack().newMessageSent(tcbm.getName(), buffer.readableBytes(), this.getNetworkId());
		this.provider.send(this, buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
				this.seqControl, this.getNetworkId(), this.localSsn, callback);

		this.scheduledComponentList.clear();
		release();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#sendUni()
	 */
	@Override
	public void send(TCUniRequest event, TaskCallback<Exception> callback) {
		if (this.isStructured()) {
			callback.onError(new TCAPSendException("Structured dialogs do not use Uni"));
			return;
		}

		TCUniMessage msg = TcapFactory.createTCUniMessage();

		if (event.getApplicationContext() != null || event.getConfidentiality() != null
				|| event.getSecurityContext() != null || event.getUserInformation() != null) {
			// set dialog portion
			DialogPortion dp = TcapFactory.createDialogPortion();
			dp.setProtocolVersion(TcapFactory.createProtocolVersionFull());
			dp.setApplicationContext(event.getApplicationContext());
			dp.setUserInformation(event.getUserInformation());
			dp.setSecurityContext(event.getSecurityContext());
			dp.setConfidentiality(event.getConfidentiality());

			msg.setDialogPortion(dp);
		}

		if (this.scheduledComponentList.size() > 0) {
			List<Component> componentsToSend = new ArrayList<Component>(this.scheduledComponentList.size());
			this.prepareComponents(componentsToSend);
			ComponentPortion cPortion = TcapFactory.createComponentPortion();
			cPortion.setComponents(componentsToSend);
			msg.setComponent(cPortion);

		}

		ByteBuf buffer;
		try {
			buffer = dialogParser.encode(msg);
		} catch (ASNException e) {
			release();
			callback.onError(new TCAPSendException("Failed to send TC-Uni message: " + e.getMessage(), e));
			return;
		}
		provider.getStack().newMessageSent(msg.getName(), buffer.readableBytes(), this.getNetworkId());
		this.provider.send(this, buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
				this.seqControl, this.getNetworkId(), this.localSsn, callback);
		this.scheduledComponentList.clear();
		release();
	}

	@Override
	public void send(TCUserAbortRequest event, TaskCallback<Exception> callback) {
		// is abort allowed in "Active" state ?
		if (!isStructured()) {
			callback.onError(new TCAPSendException("Unstructured dialog can not be aborted!"));
			return;
		}

		if (this.state.get() == TRPseudoState.InitialReceived || this.state.get() == TRPseudoState.Active) {

			TCAbortMessage msg = TcapFactory.createTCAbortMessage();
			msg.setDestinationTransactionId(
					Utils.encodeTransactionId(this.remoteTransactionIdObject, this.isSwapTcapIdBytes));

			if (event.getApplicationContext() != null || event.getConfidentiality() != null
					|| event.getSecurityContext() != null || event.getUserInformation() != null) {
				// set dialog portion
				DialogPortion dp = TcapFactory.createDialogPortion();
				dp.setProtocolVersion(TcapFactory.createProtocolVersionFull());
				dp.setApplicationContext(event.getApplicationContext());
				dp.setUserInformation(event.getUserInformation());
				dp.setSecurityContext(event.getSecurityContext());
				dp.setConfidentiality(event.getConfidentiality());

				msg.setDialogPortion(dp);
			}
			msg.setUserAbortInformation(event.getUserAbortInformation());

			if (state.get() == TRPseudoState.InitialReceived)
				// local address may change, lets check it
				if (event.getOriginatingAddress() != null && !event.getOriginatingAddress().equals(this.localAddress))
					this.localAddress = event.getOriginatingAddress();

			ByteBuf buffer;
			try {
				buffer = dialogParser.encode(msg);
				if (msg.getPAbortCause() != null)
					provider.getStack().newAbortSent(msg.getPAbortCause().name(), this.getNetworkId());
				else
					provider.getStack().newAbortSent("User", this.getNetworkId());

				provider.getStack().newMessageSent(msg.getName(), buffer.readableBytes(), this.getNetworkId());
				this.provider.send(this, buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
						this.seqControl, this.getNetworkId(), this.localSsn, callback);

				this.scheduledComponentList.clear();
				release();
			} catch (ASNException | ParseException e) {
				release();
				callback.onError(new TCAPSendException("Failed to send TC-U-Abort message: " + e.getMessage(), e));
				return;
			}
		} else if (this.state.get() == TRPseudoState.InitialSent)
			release();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#sendComponent(org
	 * .restcomm.protocols.ss7.tcap.api.tc.component.ComponentRequest)
	 */
	@Override
	public void sendComponent(Component componentRequest) throws TCAPSendException {
		if (componentRequest.getType() == ComponentType.InvokeLast
				|| componentRequest.getType() == ComponentType.InvokeNotLast) {
			// check if its taken!
			int invokeIndex = getIndexFromInvokeId(((Invoke) componentRequest).getInvokeId());
			if (this.operationsSent[invokeIndex] != null)
				throw new TCAPSendException("There is already operation with such invoke id!");

			((Invoke) componentRequest).setState(OperationState.Pending);
			((Invoke) componentRequest).setDialog(this);

			// if the Invoke timeout value has not be reset by TCAP-User
			// for this invocation we are setting it to t((Invoke)componentRequest)he the
			// TCAP stack
			// default value
			if (((Invoke) componentRequest).getTimeout() == TCAPStackImpl._EMPTY_INVOKE_TIMEOUT)
				((Invoke) componentRequest).setTimeout(this.provider.getStack().getInvokeTimeout());
		} else if (componentRequest.getType() != ComponentType.ReturnResultNotLast
				&& componentRequest.getType() != ComponentType.ReturnResultLast)
			// we are sending a response and removing invokeId from
			// incomingInvokeList
			this.removeIncomingInvokeId(componentRequest.getCorrelationId());
		this.scheduledComponentList.add(componentRequest);
	}

	@Override
	public void processInvokeWithoutAnswer(Long invokeId) {
		this.removeIncomingInvokeId(invokeId);
	}

	private void prepareComponents(List<Component> res) {

		int index = 0;
		while (this.scheduledComponentList.size() > index) {
			Component cr = this.scheduledComponentList.get(index);

			if (cr.getType() != null && cr.getType().name() != null)
				provider.getStack().newComponentSent(cr.getType().name(), this.getNetworkId());

			if (cr.getType() == ComponentType.InvokeNotLast || cr.getType() == ComponentType.InvokeLast) {
				Invoke in = (Invoke) cr;
				// FIXME: check not null?
				this.operationsSent[getIndexFromInvokeId(in.getInvokeId())] = in;
				in.setState(OperationState.Sent);
			} else if (cr.getType() == ComponentType.Reject) {
				Reject reject = (Reject) cr;
				try {
					if (reject.getProblem() != null)
						provider.getStack().newRejectSent(reject.getProblem().name(), this.getNetworkId());
				} catch (ParseException ex) {

				}
			}

			res.add(cr);
			index++;
		}
	}

	@Override
	public int getMaxUserDataLength() {

		return this.provider.getMaxUserDataLength(remoteAddress, localAddress, this.networkId);
	}

	@Override
	public int getDataLength(TCQueryRequest event) throws TCAPSendException {

		TCQueryMessage tcbm = TcapFactory.createTCQueryMessage(event.getDialogTermitationPermission());
		tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionId, this.isSwapTcapIdBytes));

		if (event.getApplicationContext() != null || event.getConfidentiality() != null
				|| event.getSecurityContext() != null || event.getUserInformation() != null) {
			DialogPortion dp = TcapFactory.createDialogPortion();
			dp.setProtocolVersion(TcapFactory.createProtocolVersionFull());
			dp.setApplicationContext(event.getApplicationContext());
			dp.setUserInformation(event.getUserInformation());
			dp.setSecurityContext(event.getSecurityContext());
			dp.setConfidentiality(event.getConfidentiality());

			tcbm.setDialogPortion(dp);
		}

		// now comps
		if (this.scheduledComponentList.size() > 0) {
			List<Component> componentsToSend = new ArrayList<Component>(this.scheduledComponentList.size());
			componentsToSend.addAll(this.scheduledComponentList);
			ComponentPortion cPortion = TcapFactory.createComponentPortion();
			cPortion.setComponents(componentsToSend);
			tcbm.setComponent(cPortion);
		}

		try {
			ByteBuf buffer = dialogParser.encode(tcbm);
			return buffer.readableBytes();
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Failed to encode message while length testing: ", e);
			throw new TCAPSendException("Error encoding TCBeginRequest", e);
		}
	}

	@Override
	public int getDataLength(TCConversationRequest event) throws TCAPSendException {
		TCConversationMessage tcbm = TcapFactory.createTCConversationMessage(event.getDialogTermitationPermission());
		tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionId, this.isSwapTcapIdBytes));
		tcbm.setDestinationTransactionId(
				Utils.encodeTransactionId(this.remoteTransactionIdObject, this.isSwapTcapIdBytes));

		if (event.getApplicationContext() != null || event.getConfidentiality() != null
				|| event.getSecurityContext() != null || event.getUserInformation() != null) {
			// set dialog portion
			DialogPortion dp = TcapFactory.createDialogPortion();
			dp.setProtocolVersion(TcapFactory.createProtocolVersionFull());
			dp.setApplicationContext(event.getApplicationContext());
			dp.setUserInformation(event.getUserInformation());
			dp.setSecurityContext(event.getSecurityContext());
			dp.setConfidentiality(event.getConfidentiality());

			tcbm.setDialogPortion(dp);
		}

		if (this.scheduledComponentList.size() > 0) {
			List<Component> componentsToSend = new ArrayList<Component>(this.scheduledComponentList.size());
			componentsToSend.addAll(this.scheduledComponentList);
			ComponentPortion cPortion = TcapFactory.createComponentPortion();
			cPortion.setComponents(componentsToSend);
			tcbm.setComponent(cPortion);
		}

		try {
			ByteBuf buffer = dialogParser.encode(tcbm);
			return buffer.readableBytes();
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Failed to encode message while length testing: ", e);
			throw new TCAPSendException("Error encoding TCContinueRequest", e);
		}
	}

	@Override
	public int getDataLength(TCResponseRequest event) throws TCAPSendException {

		// TC-END request primitive issued in response to a TC-BEGIN
		// indication primitive
		TCResponseMessage tcbm = TcapFactory.createTCResponseMessage();
		tcbm.setDestinationTransactionId(
				Utils.encodeTransactionId(this.remoteTransactionIdObject, this.isSwapTcapIdBytes));

		if (this.scheduledComponentList.size() > 0) {
			List<Component> componentsToSend = new ArrayList<Component>(this.scheduledComponentList.size());
			componentsToSend.addAll(this.scheduledComponentList);
			ComponentPortion cPortion = TcapFactory.createComponentPortion();
			cPortion.setComponents(componentsToSend);
			tcbm.setComponent(cPortion);
		}

		if (state.get() == TRPseudoState.InitialReceived)
			if (event.getApplicationContext() != null || event.getConfidentiality() != null
					|| event.getSecurityContext() != null || event.getUserInformation() != null) {
				// set dialog portion
				DialogPortion dp = TcapFactory.createDialogPortion();
				dp.setProtocolVersion(TcapFactory.createProtocolVersionFull());
				dp.setApplicationContext(event.getApplicationContext());
				dp.setUserInformation(event.getUserInformation());
				dp.setSecurityContext(event.getSecurityContext());
				dp.setConfidentiality(event.getConfidentiality());

				tcbm.setDialogPortion(dp);
			}

		try {
			ByteBuf buffer = dialogParser.encode(tcbm);
			return buffer.readableBytes();
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Failed to encode message while length testing: ", e);
			throw new TCAPSendException("Error encoding TCEndRequest", e);
		}
	}

	@Override
	public int getDataLength(TCUniRequest event) throws TCAPSendException {

		TCUniMessage msg = TcapFactory.createTCUniMessage();

		if (event.getApplicationContext() != null || event.getConfidentiality() != null
				|| event.getSecurityContext() != null || event.getUserInformation() != null) {
			// set dialog portion
			DialogPortion dp = TcapFactory.createDialogPortion();
			dp.setProtocolVersion(TcapFactory.createProtocolVersionFull());
			dp.setApplicationContext(event.getApplicationContext());
			dp.setUserInformation(event.getUserInformation());
			dp.setSecurityContext(event.getSecurityContext());
			dp.setConfidentiality(event.getConfidentiality());

			msg.setDialogPortion(dp);
		}

		if (this.scheduledComponentList.size() > 0) {
			List<Component> componentsToSend = new ArrayList<Component>(this.scheduledComponentList.size());
			componentsToSend.addAll(this.scheduledComponentList);
			ComponentPortion cPortion = TcapFactory.createComponentPortion();
			cPortion.setComponents(componentsToSend);
			msg.setComponent(cPortion);

		}

		try {
			ByteBuf buffer = dialogParser.encode(msg);
			return buffer.readableBytes();
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Failed to encode message while length testing: ", e);
			throw new TCAPSendException("Error encoding TCUniRequest", e);
		}
	}

	// /////////////////
	// LOCAL METHODS //
	// /////////////////

	@Override
	public ProtocolVersion getProtocolVersion() {
		return protocolVersion;
	}

	@Override
	public void setProtocolVersion(ProtocolVersion protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	/**
	 * @param remoteTransactionId the remoteTransactionId to set
	 */
	void setRemoteTransactionId(ByteBuf remoteTransactionId) {
		if (remoteTransactionId != null)
			this.remoteTransactionIdObject = Utils.decodeTransactionId(remoteTransactionId, this.isSwapTcapIdBytes);
	}

	/**
	 * @param localAddress the localAddress to set
	 */
	@Override
	public void setLocalAddress(SccpAddress localAddress) {
		this.localAddress = localAddress;
	}

	/**
	 * @param remoteAddress the remoteAddress to set
	 */
	@Override
	public void setRemoteAddress(SccpAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	void processUni(TCUniMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {

		// TCUniIndicationImpl tcUniIndication = null;
		try {
			this.setRemoteAddress(remoteAddress);
			this.setLocalAddress(localAddress);

			// no dialog portion!
			// convert to indications
			TCUniIndication tcUniIndication = this.provider.getDialogPrimitiveFactory().createUniIndication(this);

			tcUniIndication.setDestinationAddress(localAddress);
			tcUniIndication.setOriginatingAddress(remoteAddress);
			// now comps
			ComponentPortion comps = msg.getComponent();
			tcUniIndication.setComponents(comps);

			if (msg.getDialogPortion() != null) {
				DialogPortion dp = msg.getDialogPortion();
				this.lastACN = dp.getApplicationContext();
				this.lastUI = dp.getUserInformation();
				tcUniIndication.setApplicationContext(this.lastACN);
				tcUniIndication.setUserInformation(this.lastUI);
				tcUniIndication.setConfidentiality(msg.getDialogPortion().getConfidentiality());
				tcUniIndication.setSecurityContext(msg.getDialogPortion().getSecurityContext());
			}

			// lets deliver to provider, this MUST not throw anything
			this.provider.deliver(this, tcUniIndication);

		} finally {
			this.release();
		}
	}

	protected void processQuery(TCQueryMessage msg, SccpAddress localAddress, SccpAddress remoteAddress,
			boolean dialogTermitationPermission) {

		TCQueryIndication tcBeginIndication = null;
		// this is invoked ONLY for server.
		if (state.get() != TRPseudoState.Idle) {
			// should we terminate dialog here?
			if (logger.isErrorEnabled())
				logger.error("Received Begin primitive, but state is not: " + TRPseudoState.Idle + ". Dialog: " + this);
			this.sendAbnormalDialog();
			return;
		}
		restartIdleTimer();

		// lets setup
		this.setRemoteAddress(remoteAddress);
		this.setLocalAddress(localAddress);
		this.setRemoteTransactionId(msg.getOriginatingTransactionId());
		// convert to indications
		tcBeginIndication = this.provider.getDialogPrimitiveFactory().createQueryIndication(this,
				dialogTermitationPermission);

		tcBeginIndication.setDestinationAddress(localAddress);
		tcBeginIndication.setOriginatingAddress(remoteAddress);

		// if APDU and context data present, lets store it
		DialogPortion dialogPortion = msg.getDialogPortion();

		if (dialogPortion != null) {
			this.lastACN = dialogPortion.getApplicationContext();
			this.lastUI = dialogPortion.getUserInformation();
			tcBeginIndication.setApplicationContext(this.lastACN);
			tcBeginIndication.setUserInformation(this.lastUI);
			tcBeginIndication.setConfidentiality(msg.getDialogPortion().getConfidentiality());
			tcBeginIndication.setSecurityContext(msg.getDialogPortion().getSecurityContext());
		}

		ComponentPortion cPortion = TcapFactory.createComponentPortion();

		if (msg.getComponent() != null)
			cPortion.setComponents(processOperationsState(msg.getComponent().getComponents()));

		tcBeginIndication.setComponents(cPortion);
		// change state - before we deliver
		this.setState(TRPseudoState.InitialReceived);
		// lets deliver to provider
		this.provider.deliver(this, tcBeginIndication);
	}

	protected void processConversation(TCConversationMessage msg, SccpAddress localAddress, SccpAddress remoteAddressm,
			boolean dialogTermitationPermission) {

		TCConversationIndication tcContinueIndication = null;
		if (state.get() == TRPseudoState.InitialSent) {
			restartIdleTimer();
			tcContinueIndication = this.provider.getDialogPrimitiveFactory().createConversationIndication(this,
					dialogTermitationPermission);
			// in continue remote address MAY change be changed, so lets
			// update!
			this.setRemoteAddress(remoteAddress);
			this.setRemoteTransactionId(msg.getOriginatingTransactionId());
			tcContinueIndication.setOriginatingAddress(remoteAddress);

			// here we will receive DialogResponse APDU - if request was
			// present!
			DialogPortion dialogPortion = msg.getDialogPortion();
			if (dialogPortion != null) {
				this.lastACN = dialogPortion.getApplicationContext();
				this.lastUI = dialogPortion.getUserInformation();
				tcContinueIndication.setApplicationContext(this.lastACN);
				tcContinueIndication.setUserInformation(this.lastUI);
				tcContinueIndication.setConfidentiality(msg.getDialogPortion().getConfidentiality());
				tcContinueIndication.setSecurityContext(msg.getDialogPortion().getSecurityContext());
			} else if (this.dpSentInBegin) {
				sendAbnormalDialog();
				return;

			}
			tcContinueIndication.setOriginatingAddress(remoteAddress);
			// now comps
			ComponentPortion cPortion = TcapFactory.createComponentPortion();
			cPortion.setComponents(processOperationsState(msg.getComponent().getComponents()));
			tcContinueIndication.setComponents(cPortion);
			// change state
			this.setState(TRPseudoState.Active);

			// lets deliver to provider
			this.provider.deliver(this, tcContinueIndication);

		} else if (state.get() == TRPseudoState.Active) {
			restartIdleTimer();
			// XXX: here NO APDU will be present, hence, no ACN/UI change
			tcContinueIndication = this.provider.getDialogPrimitiveFactory().createConversationIndication(this,
					dialogTermitationPermission);

			tcContinueIndication.setOriginatingAddress(remoteAddress);

			// now comps
			ComponentPortion cPortion = TcapFactory.createComponentPortion();

			if (msg.getComponent() != null)
				cPortion.setComponents(processOperationsState(msg.getComponent().getComponents()));

			tcContinueIndication.setComponents(cPortion);

			// lets deliver to provider
			this.provider.deliver(this, tcContinueIndication);

		} else {
			if (logger.isErrorEnabled())
				logger.error(
						"Received Continue primitive, but state is not proper: " + this.state + ", Dialog: " + this);
			this.sendAbnormalDialog();
			return;
		}
	}

	protected void processResponse(TCResponseMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {
		TCResponseIndication tcResponseIndication = null;
		try {
			restartIdleTimer();
			tcResponseIndication = this.provider.getDialogPrimitiveFactory().createResponseIndication(this);

			if (state.get() == TRPseudoState.InitialSent) {
				// in end remote address MAY change be changed, so lets
				// update!
				this.setRemoteAddress(remoteAddress);
				tcResponseIndication.setOriginatingAddress(remoteAddress);
			}

			DialogPortion dialogPortion = msg.getDialogPortion();
			if (dialogPortion != null) {
				this.lastACN = dialogPortion.getApplicationContext();
				this.lastUI = dialogPortion.getUserInformation();
				tcResponseIndication.setApplicationContext(this.lastACN);
				tcResponseIndication.setUserInformation(this.lastUI);
				tcResponseIndication.setConfidentiality(msg.getDialogPortion().getConfidentiality());
				tcResponseIndication.setSecurityContext(msg.getDialogPortion().getSecurityContext());
			}
			// now comps
			ComponentPortion cPortion = TcapFactory.createComponentPortion();

			if (msg.getComponent() != null)
				cPortion.setComponents(processOperationsState(msg.getComponent().getComponents()));

			tcResponseIndication.setComponents(cPortion);

			this.provider.deliver(this, tcResponseIndication);
		} finally {
			release();
		}
	}

	protected void processAbort(TCAbortMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) {
		try {

			PAbortCause type = null;
			try {
				type = msg.getPAbortCause();
			} catch (ParseException ex) {

			}

			if (type != null) {

				// its TC-P-Abort
				TCPAbortIndication tcAbortIndication = this.provider.getDialogPrimitiveFactory()
						.createPAbortIndication(this);
				tcAbortIndication.setPAbortCause(type);

				this.provider.deliver(this, tcAbortIndication);

			} else {
				// its TC-U-Abort
				TCUserAbortIndication tcUAbortIndication = this.provider.getDialogPrimitiveFactory()
						.createUAbortIndication(this);
				DialogPortion dp = msg.getDialogPortion();
				if (dp != null) {
					tcUAbortIndication.setApplicationContextName(dp.getApplicationContext());
					tcUAbortIndication.setUserInformation(dp.getUserInformation());
					tcUAbortIndication.setSecurityContext(dp.getSecurityContext());
					tcUAbortIndication.setConfidentiality(dp.getConfidentiality());
				}
				tcUAbortIndication.setUserAbortInformation(msg.getUserAbortInformation());

				if (state.get() == TRPseudoState.InitialSent) {
					// in userAbort remote address MAY change be changed, so lets
					// update!
					this.setRemoteAddress(remoteAddress);
					tcUAbortIndication.setOriginatingAddress(remoteAddress);
				}

				this.provider.deliver(this, tcUAbortIndication);
			}
		} finally {
			release();
		}
	}

	protected void sendAbnormalDialog() {

		TCPAbortIndication tcAbortIndication = null;
		try {
			if (this.remoteTransactionIdObject == null)
				// no remoteTransactionId - we can not send back TC-ABORT
				return;

			// sending to the remote side
			if (this.getProtocolVersion() != null)
				this.provider.sendProviderAbort(PAbortCause.InconsistentDialoguePortion,
						Utils.encodeTransactionId(this.remoteTransactionIdObject, this.isSwapTcapIdBytes),
						remoteAddress, localAddress, seqControl, this.getNetworkId(), dummyCallback);
			else
				this.provider.sendRejectAsProviderAbort(PAbortCause.InconsistentDialoguePortion,
						Utils.encodeTransactionId(this.remoteTransactionIdObject, this.isSwapTcapIdBytes),
						remoteAddress, localAddress, seqControl, this.getNetworkId(), dummyCallback);

			// sending to the local side
			tcAbortIndication = this.provider.getDialogPrimitiveFactory().createPAbortIndication(this);
			tcAbortIndication.setPAbortCause(PAbortCause.InconsistentDialoguePortion);

			this.provider.deliver(this, tcAbortIndication);
		} finally {
			this.release();
		}
	}

	@Override
	public Invoke getInvoke(Long correlationId) {
		Invoke invoke = null;
		int index = 0;
		if (correlationId != null) {
			index = getIndexFromInvokeId(correlationId);
			invoke = this.operationsSent[index];
		}

		return invoke;
	}

	protected List<Component> processOperationsState(List<Component> components) {
		if (components == null)
			return null;

		List<Component> resultingIndications = new ArrayList<Component>();
		for (Component ci : components) {
			Long invokeId;
			invokeId = ci.getCorrelationId();
			Invoke invoke = null;
			int index = 0;
			if (invokeId != null) {
				index = getIndexFromInvokeId(invokeId);
				invoke = this.operationsSent[index];
			}

			provider.getStack().newComponentReceived(ci.getType().name(), this.getNetworkId());
			switch (ci.getType()) {

			case InvokeNotLast:
			case InvokeLast:
				if (invokeId != null && invoke == null) {
					logger.error(String.format("Rx : %s but no sent Invoke for correlationId exists", ci));
					this.addReject(resultingIndications, ((Invoke) ci).getInvokeId(),
							RejectProblem.invokeUnrecognisedCorrelationId);
				} else {
					if (invoke != null)
						((Invoke) ci).setCorrelationInvoke(invoke);

					if (!this.addIncomingInvokeId(((Invoke) ci).getInvokeId())) {
						logger.error(String.format("Rx : %s but there is already Invoke with this invokeId", ci));
						this.addReject(resultingIndications, ((Invoke) ci).getInvokeId(),
								RejectProblem.invokeDuplicateInvocation);
					} else
						resultingIndications.add(ci);
				}
				break;
			case ReturnResultNotLast:

				if (invoke == null) {
					logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));
					this.addReject(resultingIndications, ci.getCorrelationId(),
							RejectProblem.returnResultUnrecognisedCorrelationId);
				} else if (invoke.getInvokeClass() != InvokeClass.Class1
						&& invoke.getInvokeClass() != InvokeClass.Class3) {
					logger.error(String.format("Rx : %s but Invoke class is not 1 or 3", ci));
					this.addReject(resultingIndications, ci.getCorrelationId(),
							RejectProblem.returnResultUnexpectedReturnResult);
				} else {
					resultingIndications.add(ci);
					Return rri = (Return) ci;
					if (rri.getOperationCode() == null)
						rri.setOperationCode(invoke.getOperationCode());
				}
				break;

			case ReturnResultLast:

				if (invoke == null) {
					logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));
					this.addReject(resultingIndications, ci.getCorrelationId(),
							RejectProblem.returnResultUnrecognisedCorrelationId);
				} else if (invoke.getInvokeClass() != InvokeClass.Class1
						&& invoke.getInvokeClass() != InvokeClass.Class3) {
					logger.error(String.format("Rx : %s but Invoke class is not 1 or 3", ci));
					this.addReject(resultingIndications, ci.getCorrelationId(),
							RejectProblem.returnResultUnexpectedReturnResult);
				} else {
					invoke.onReturnResultLast();
					if (invoke.isSuccessReported())
						resultingIndications.add(ci);
					Return rri = ((Return) ci);
					if (rri.getOperationCode() == null)
						rri.setOperationCode(invoke.getOperationCode());
				}
				break;

			case ReturnError:
				if (invoke == null) {
					logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));
					this.addReject(resultingIndications, ci.getCorrelationId(),
							RejectProblem.returnErrorUnrecognisedCorrelationId);
				} else if (invoke.getInvokeClass() != InvokeClass.Class1
						&& invoke.getInvokeClass() != InvokeClass.Class2) {
					logger.error(String.format("Rx : %s but Invoke class is not 1 or 2", ci));
					this.addReject(resultingIndications, ci.getCorrelationId(),
							RejectProblem.returnErrorUnexpectedError);
				} else {
					invoke.onError();
					if (invoke.isErrorReported())
						resultingIndications.add(ci);
				}
				break;

			case Reject:
				Reject rej = (Reject) ci;
				RejectProblem problem = null;
				try {
					problem = rej.getProblem();
				} catch (ParseException ex) {

				}

				if (problem != null)
					provider.getStack().newRejectReceived(problem.name(), this.getNetworkId());

				if (invoke != null)
					if (rej != null && !rej.isLocalOriginated()
							&& (problem == RejectProblem.invokeDuplicateInvocation
									|| problem == RejectProblem.invokeIncorrectParameter
									|| problem == RejectProblem.invokeUnrecognisedCorrelationId
									|| problem == RejectProblem.invokeUnrecognisedOperation))
						invoke.onReject();
				if (rej.isLocalOriginated() && this.isStructured())
					try {
						// this is a local originated Reject - we are rejecting
						// an incoming component
						// we need to send a Reject also to a peer
						this.sendComponent(ci);
					} catch (TCAPSendException e) {
						logger.error("TCAPSendException when sending Reject component : Dialog: " + this, e);
					}
				resultingIndications.add(ci);
				break;

			default:
				resultingIndications.add(ci);
				break;
			}

		}
		return resultingIndications;
	}

	private void addReject(List<Component> resultingIndications, Long invokeId, RejectProblem p) {
		try {
			Reject rej = TcapFactory.createComponentReject();
			rej.setLocalOriginated(true);
			if (invokeId != null)
				rej.setCorrelationId(invokeId);
			rej.setProblem(p);

			resultingIndications.add(rej);

			if (this.isStructured())
				this.sendComponent(rej);
		} catch (TCAPSendException e) {
			logger.error(String.format("Error sending Reject component", e));
		}
	}

	protected void setState(TRPseudoState newState) {
		if (this.state.get() == TRPseudoState.Expunged)
			return;

		TRPseudoState oldState = this.state.getAndSet(newState);
		if (oldState == TRPseudoState.Expunged)
			this.state.set(TRPseudoState.Expunged);

		if (oldState != TRPseudoState.Expunged && newState == TRPseudoState.Expunged) {
			stopIdleTimer();
			provider.release(this);
		}
	}

	private void startIdleTimer() {
		if (!this.structured)
			return;

		IdleTimer newTimer = new IdleTimer(System.currentTimeMillis() + this.idleTaskTimeout);
		if (!this.idleTimer.compareAndSet(null, newTimer))
			throw new IllegalStateException("Idle timer is not null");

		if (provider.affinityEnabled)
			this.workerPool.addTimer(newTimer);
		else
			this.workerPool.getPeriodicQueue().store(newTimer.getRealTimestamp(), newTimer);
	}

	private void stopIdleTimer() {
		if (!this.structured)
			return;

		IdleTimer oldTimer = this.idleTimer.getAndSet(null);
		if (oldTimer != null)
			oldTimer.stop();
	}

	private void restartIdleTimer() {
		stopIdleTimer();
		startIdleTimer();
	}

	private class IdleTimer extends RunnableTimer {
		private DialogImpl d = DialogImpl.this;

		public IdleTimer(Long startTime) {
			super(null, startTime, localTransactionIdObject.toString(), "TcapAnsiIdleTimer");
		}

		@Override
		public void execute() {
			if (super.startTime == Long.MAX_VALUE || d == null)
				return;

			idleTimer.set(null);

			d.idleTimerActionTaken.set(false);
			d.idleTimerInvoked.set(true);
			provider.timeout(d);
			if (d.idleTimerActionTaken.get()) {
				if (idleTimer.get() == null)
					startIdleTimer();
			} else if (remoteTransactionIdObject != null && !getState().equals(TRPseudoState.Expunged))
				sendAbnormalDialog();
			else
				release();

			d.idleTimerInvoked.set(false);
		}

		@Override
		public void stop() {
			super.stop();
			d = null;
		}
	}

	// ////////////////////
	// IND like methods //
	// ///////////////////
	@Override
	public void operationEnded(Invoke tcInvokeRequestImpl) {
		// this op died cause of timeout, TC-L-CANCEL!
		int index = getIndexFromInvokeId(tcInvokeRequestImpl.getInvokeId());
		freeInvokeId(tcInvokeRequestImpl.getInvokeId());
		this.operationsSent[index] = null;
		// lets call listener
		// This is done actually with COmponentIndication ....
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#operationEnded(
	 * org.restcomm.protocols.ss7.tcap.tc.component.TCInvokeRequestImpl)
	 */
	@Override
	public void operationTimedOut(Invoke invoke) {
		// this op died cause of timeout, TC-L-CANCEL!
		int index = getIndexFromInvokeId(invoke.getInvokeId());
		freeInvokeId(invoke.getInvokeId());
		this.operationsSent[index] = null;
		// lets call listener
		this.provider.operationTimedOut(invoke, networkId);
	}

	// TC-TIMER-RESET
	@Override
	public void resetTimer(Long invokeId) throws TCAPException {
		int index = getIndexFromInvokeId(invokeId);
		Invoke invoke = operationsSent[index];
		if (invoke == null)
			throw new TCAPException("No operation with this ID");
		invoke.startTimer();
	}

	@Override
	public TRPseudoState getState() {
		return this.state.get();
	}

	@Override
	public Object getUserObject() {
		return this.userObject;
	}

	@Override
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return super.toString() + ": Local[" + this.getLocalDialogId() + "] Remote[" + this.getRemoteDialogId()
				+ "], LocalAddress[" + localAddress + "] RemoteAddress[" + this.remoteAddress + "]";
	}
}
