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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.RemoteSccpStatus;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SignallingPointStatus;
import org.restcomm.protocols.ss7.sccp.message.MessageFactory;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCause;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcap.api.DialogPrimitiveFactory;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCListener;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.DraftParsedMessage;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcap.asn.ASNComponentPortionObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.ASNDialogPortionObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.DialogAPDU;
import org.restcomm.protocols.ss7.tcap.asn.DialogAPDUType;
import org.restcomm.protocols.ss7.tcap.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDU;
import org.restcomm.protocols.ss7.tcap.asn.DialogResponseAPDU;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.Result;
import org.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.restcomm.protocols.ss7.tcap.asn.ResultType;
import org.restcomm.protocols.ss7.tcap.asn.TCNoticeIndicationImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCUnifiedMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCUnknownMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.Utils;
import org.restcomm.protocols.ss7.tcap.asn.comp.DestinationTransactionID;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.Return;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultInnerImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUnifiedMessage;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogAbortAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogResponseAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCAbortMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCBeginMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCContinueMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCEndMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCUniMessageImpl;
import org.restcomm.protocols.ss7.tcap.tc.component.ComponentPrimitiveFactoryImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.DraftParsedMessageImpl;

import com.mobius.software.common.dal.timers.RunnableTask;
import com.mobius.software.common.dal.timers.RunnableTimer;
import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.common.dal.timers.WorkerPool;
import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeHandler;
import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TCAPProviderImpl implements TCAPProvider, SccpListener, ASNDecodeHandler {
	private static final long serialVersionUID = 1L;

	public static final int TCAP_ACN = 1;

	private static final Logger logger = LogManager.getLogger(TCAPProviderImpl.class); // listenres

	private transient List<TCListener> tcListeners = new CopyOnWriteArrayList<TCListener>();

	protected transient WorkerPool workerPool;

	private transient ComponentPrimitiveFactory componentPrimitiveFactory;
	private transient DialogPrimitiveFactory dialogPrimitiveFactory;
	private transient SccpProvider sccpProvider;

	private transient MessageFactory messageFactory;

	private transient TCAPStackImpl stack; // originating TX id ~=Dialog, its direct

	private transient ConcurrentHashMap<Long, DialogImpl> dialogs = new ConcurrentHashMap<Long, DialogImpl>();

	private AtomicInteger seqControl = new AtomicInteger(1);
	private int ssn;
	private AtomicLong curDialogId = new AtomicLong(0);

	private transient ConcurrentHashMap<String, Integer> lstUserPartCongestionLevel = new ConcurrentHashMap<String, Integer>();

	private ASNParser messageParser = new ASNParser(TCUnknownMessageImpl.class, true, false);
	protected boolean affinityEnabled = false;

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
			logger.warn("An error occurred, while processing task asynchronously , " + exception.getMessage(),
					exception);
		}
	};

	protected TCAPProviderImpl(SccpProvider sccpProvider, TCAPStackImpl stack, int ssn, WorkerPool workerPool) {
		super();

		this.sccpProvider = sccpProvider;
		this.ssn = ssn;
		this.messageFactory = sccpProvider.getMessageFactory();
		this.stack = stack;

		this.componentPrimitiveFactory = new ComponentPrimitiveFactoryImpl();
		this.dialogPrimitiveFactory = new DialogPrimitiveFactoryImpl(this.componentPrimitiveFactory);
		this.workerPool = workerPool;

		messageParser.setDecodeHandler(this);
		messageParser.loadClass(TCAbortMessageImpl.class);
		messageParser.loadClass(TCBeginMessageImpl.class);
		messageParser.loadClass(TCEndMessageImpl.class);
		messageParser.loadClass(TCContinueMessageImpl.class);
		messageParser.loadClass(TCUniMessageImpl.class);

		messageParser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogRequestAPDUImpl.class);
		messageParser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogResponseAPDUImpl.class);
		messageParser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogAbortAPDUImpl.class);

		messageParser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, InvokeImpl.class);
		messageParser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultImpl.class);
		messageParser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultLastImpl.class);
		messageParser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, RejectImpl.class);
		messageParser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnErrorImpl.class);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.restcomm.protocols.ss7.tcap.api.TCAPStack#addTCListener(org.restcomm
	 * .protocols.ss7.tcap.api.TCListener)
	 */

	@Override
	public void addTCListener(TCListener lst) {
		if (this.tcListeners.contains(lst)) {
		} else
			this.tcListeners.add(lst);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.restcomm.protocols.ss7.tcap.api.TCAPStack#removeTCListener(org.restcomm
	 * .protocols.ss7.tcap.api.TCListener)
	 */
	@Override
	public void removeTCListener(TCListener lst) {
		this.tcListeners.remove(lst);

	}

	protected boolean checkAvailableTxId(Long id) {
		if (!this.dialogs.containsKey(id))
			return true;
		else
			return false;
	}

	protected Long getAvailableTxId() throws TCAPException {
		while (true) {
			this.curDialogId.compareAndSet(this.stack.getDialogIdRangeEnd(), this.stack.getDialogIdRangeStart() - 1);

			Long id = this.curDialogId.incrementAndGet();
			if (checkAvailableTxId(id))
				return id;
		}
	}

	protected void resetDialogIdValueAfterRangeChange() {
		if (this.curDialogId.get() < this.stack.getDialogIdRangeStart())
			this.curDialogId.set(this.stack.getDialogIdRangeStart());

		if (this.curDialogId.get() > this.stack.getDialogIdRangeEnd())
			this.curDialogId.set(this.stack.getDialogIdRangeEnd() - 1);

		// if (this.currentDialogId.longValue() < this.stack.getDialogIdRangeStart())
		// this.currentDialogId.set(this.stack.getDialogIdRangeStart());
		// if (this.currentDialogId.longValue() >= this.stack.getDialogIdRangeEnd())
		// this.currentDialogId.set(this.stack.getDialogIdRangeEnd() - 1);
	}

	// get next Seq Control value available
	protected int getNextSeqControl() {
		int res = seqControl.getAndIncrement();

		// if (!seqControl.compareAndSet(256, 1)) {
		// return seqControl.getAndIncrement();
		// } else {
		// return 0;
		// }

		// seqControl++;
		// if (seqControl > 255) {
		// seqControl = 0;
		//
		// }
		// return seqControl;

		// get next Seq Control value available

		if (this.stack.getSlsRangeType() == SlsRangeType.Odd) {
			if (res % 2 == 0)
				res++;
		} else if (this.stack.getSlsRangeType() == SlsRangeType.Even)
			if (res % 2 != 0)
				res++;
		res = res & 0xFF;

		return res;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.restcomm.protocols.ss7.tcap.api.TCAPProvider#
	 * getComopnentPrimitiveFactory()
	 */
	@Override
	public ComponentPrimitiveFactory getComponentPrimitiveFactory() {

		return this.componentPrimitiveFactory;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.restcomm.protocols.ss7.tcap.api.TCAPProvider#getDialogPrimitiveFactory ()
	 */
	@Override
	public DialogPrimitiveFactory getDialogPrimitiveFactory() {

		return this.dialogPrimitiveFactory;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.restcomm.protocols.ss7.tcap.api.TCAPProvider#getNewDialog(org.restcomm
	 * .protocols.ss7.sccp.parameter.SccpAddress,
	 * org.restcomm.protocols.ss7.sccp.parameter.SccpAddress)
	 */
	@Override
	public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, int networkId)
			throws TCAPException {
		stack.newOutgoingDialogProcessed(networkId);
		DialogImpl res = getNewDialog(localAddress, remoteAddress, getNextSeqControl(), null);
		this.setSsnToDialog(res, localAddress.getSubsystemNumber());
		res.setNetworkId(networkId);
		return res;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.restcomm.protocols.ss7.tcap.api.TCAPProvider#getNewDialog(org.restcomm
	 * .protocols.ss7.sccp.parameter.SccpAddress,
	 * org.restcomm.protocols.ss7.sccp.parameter.SccpAddress, Long id)
	 */
	@Override
	public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, Long id, int networkId)
			throws TCAPException {
		if (id == null)
			stack.newOutgoingDialogProcessed(networkId);

		DialogImpl res = getNewDialog(localAddress, remoteAddress, getNextSeqControl(), id);
		this.setSsnToDialog(res, localAddress.getSubsystemNumber());
		res.setNetworkId(networkId);
		return res;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.restcomm.protocols.ss7.tcap.api.TCAPProvider#getNewUnstructuredDialog
	 * (org.restcomm.protocols.ss7.sccp.parameter.SccpAddress,
	 * org.restcomm.protocols.ss7.sccp.parameter.SccpAddress)
	 */
	@Override
	public Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress, int networkId)
			throws TCAPException {
		DialogImpl res = _getDialog(localAddress, remoteAddress, false, getNextSeqControl(), null);
		this.setSsnToDialog(res, localAddress.getSubsystemNumber());
		res.setNetworkId(networkId);
		return res;
	}

	protected DialogImpl getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, int seqControl, Long id)
			throws TCAPException {
		return _getDialog(localAddress, remoteAddress, true, seqControl, id);
	}

	protected DialogImpl _getDialog(SccpAddress localAddress, SccpAddress remoteAddress, boolean structured,
			int seqControl, Long id) throws TCAPException {

		if (localAddress == null)
			throw new NullPointerException("LocalAddress must not be null");

		if (id == null)
			id = this.getAvailableTxId();
		else if (!checkAvailableTxId(id))
			throw new TCAPException("Suggested local TransactionId is already present in system: " + id);
		if (structured) {
			DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this.workerPool, this,
					seqControl);

			this.dialogs.put(id, di);
			return di;
		} else {
			DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this.workerPool, this,
					seqControl);

			return di;
		}
	}

	@Override
	public DialogImpl getDialogById(Long id) {
		return this.dialogs.get(id);
	}

	protected void setSsnToDialog(DialogImpl di, int ssn) {
		if (ssn != this.ssn)
			if (ssn <= 0 || !this.stack.isExtraSsnPresent(ssn))
				ssn = this.ssn;
		di.setLocalSsn(ssn);
	}

	@Override
	public int getCurrentDialogsCount() {
		return this.dialogs.size();
	}

	public void send(DialogImpl dialog, ByteBuf data, boolean returnMessageOnError, SccpAddress destinationAddress,
			SccpAddress originatingAddress, int seqControl, int networkId, int localSsn, int remotePc,
			TaskCallback<Exception> callback) {
		SccpDataMessage msg = messageFactory.createDataMessageClass1(destinationAddress, originatingAddress, data,
				seqControl, localSsn, returnMessageOnError, null, null);
		msg.setNetworkId(networkId);
		msg.setOutgoingDpc(remotePc);

		String taskID = String.valueOf(destinationAddress.getSignalingPointCode());
		if (dialog != null && dialog.getLocalDialogId() == null)
			taskID = String.valueOf(dialog.getLocalDialogId());

		data.retain();
		RunnableTask outgoingTask = new RunnableTask(new Runnable() {
			@Override
			public void run() {
				sccpProvider.send(msg, callback);
				data.release();
			}
		}, taskID, "TcapOutgoingMessageTask");

		if (this.affinityEnabled)
			workerPool.addTaskLast(outgoingTask);
		else
			workerPool.getQueue().offerLast(outgoingTask);
	}

	public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId) {
		return this.sccpProvider.getMaxUserDataLength(calledPartyAddress, callingPartyAddress, msgNetworkId);
	}

	public void deliver(DialogImpl dialogImpl, TCBeginIndication msg) {
		try {
			for (TCListener lst : this.tcListeners)
				lst.onTCBegin(msg, dummyCallback);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Received exception while delivering data to transport layer.", e);
		}

	}

	public void deliver(DialogImpl dialogImpl, TCContinueIndication tcContinueIndication) {
		try {
			for (TCListener lst : this.tcListeners)
				lst.onTCContinue(tcContinueIndication, dummyCallback);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Received exception while delivering data to transport layer.", e);
		}

	}

	public void deliver(DialogImpl dialogImpl, TCEndIndication tcEndIndication) {
		try {
			for (TCListener lst : this.tcListeners)
				lst.onTCEnd(tcEndIndication, dummyCallback);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Received exception while delivering data to transport layer.", e);
		}
	}

	public void deliver(DialogImpl dialogImpl, TCPAbortIndication tcAbortIndication) {
		try {
			for (TCListener lst : this.tcListeners)
				lst.onTCPAbort(tcAbortIndication);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Received exception while delivering data to transport layer.", e);
		}

	}

	public void deliver(DialogImpl dialogImpl, TCUserAbortIndication tcAbortIndication) {
		try {
			for (TCListener lst : this.tcListeners)
				lst.onTCUserAbort(tcAbortIndication);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Received exception while delivering data to transport layer.", e);
		}

	}

	public void deliver(DialogImpl dialogImpl, TCUniIndication tcUniIndication) {
		try {
			for (TCListener lst : this.tcListeners)
				lst.onTCUni(tcUniIndication);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Received exception while delivering data to transport layer.", e);
		}
	}

	public void deliver(DialogImpl dialogImpl, TCNoticeIndication tcNoticeIndication) {
		try {
			for (TCListener lst : this.tcListeners)
				lst.onTCNotice(tcNoticeIndication);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Received exception while delivering data to transport layer.", e);
		}
	}

	public void release(DialogImpl d) {
		Long did = d.getLocalDialogId();

		this.dialogs.remove(did);
		this.doRelease(d);
	}

	protected void doRelease(DialogImpl d) {
		try {
			for (TCListener lst : this.tcListeners)
				lst.onDialogReleased(d);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Received exception while delivering dialog release.", e);
		}
	}

	/**
	 * @param d
	 */
	public void timeout(DialogImpl d) {
		stack.dialogTimedOut(d.getNetworkId());
		try {
			for (TCListener lst : this.tcListeners)
				lst.onDialogTimeout(d);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Received exception while delivering dialog release.", e);
		}
	}

	@Override
	public TCAPStackImpl getStack() {
		return this.stack;
	}

	// ///////////////////////////////////////////
	// Some methods invoked by operation FSM //
	// //////////////////////////////////////////
	@Override
	public void storeOperationTimer(RunnableTimer operationTimer) {
		this.workerPool.addTimer(operationTimer);
	}

	public void operationTimedOut(DialogImpl dialog, int invokeId, InvokeClass invokeClass, int networkId) {
		stack.invokeTimedOut(networkId);
		try {
			for (TCListener lst : this.tcListeners)
				lst.onInvokeTimeout(dialog, invokeId, invokeClass);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Received exception while delivering Begin.", e);
		}
	}

	void start() {
		logger.info("Starting TCAP Provider");

		this.sccpProvider.registerSccpListener(ssn, this);
		logger.info("Registered SCCP listener with ssn " + ssn);

		if (this.stack.getExtraSsns() != null) {
			Iterator<Integer> extraSsns = this.stack.getExtraSsns().iterator();
			if (extraSsns != null)
				while (extraSsns.hasNext()) {
					int extraSsn = extraSsns.next();
					this.sccpProvider.registerSccpListener(extraSsn, this);
					logger.info("Registered SCCP listener with extra ssn " + extraSsn);
				}
		}

		// congestion caring
		lstUserPartCongestionLevel.clear();
	}

	public void stop() {
		this.sccpProvider.deregisterSccpListener(ssn);

		if (this.stack.getExtraSsns() != null) {
			Iterator<Integer> extraSsns = this.stack.getExtraSsns().iterator();
			while (extraSsns.hasNext()) {
				int extraSsn = extraSsns.next();
				this.sccpProvider.deregisterSccpListener(extraSsn);
			}
		}

		this.dialogs.clear();
	}

	protected ByteBuf encodeAbortMessage(TCAbortMessage msg) throws ASNException {
		return messageParser.encode(msg);
	}

	protected void sendProviderAbort(PAbortCauseType pAbortCause, ByteBuf remoteTransactionId,
			SccpAddress remoteAddress, SccpAddress localAddress, int seqControl, int networkId, int remotePc,
			TaskCallback<Exception> callback) {

		TCAbortMessage msg = TcapFactory.createTCAbortMessage();
		msg.setDestinationTransactionId(remoteTransactionId);
		msg.setPAbortCause(pAbortCause);

		ByteBuf buffer;
		try {
			buffer = messageParser.encode(msg);
		} catch (ASNException e) {
			if (logger.isErrorEnabled())
				logger.error("Failed to send message: ", e);

			callback.onError(e);
			return;
		}

		if (pAbortCause != null)
			stack.newAbortSent(pAbortCause.name(), networkId);
		else
			stack.newAbortSent("User", networkId);

		stack.newMessageSent(msg.getName(), buffer.readableBytes(), networkId);
		this.send(null, buffer, false, remoteAddress, localAddress, seqControl, networkId,
				localAddress.getSubsystemNumber(), remotePc, callback);
	}

	protected void sendProviderAbort(DialogServiceProviderType pt, ByteBuf remoteTransactionId,
			SccpAddress remoteAddress, SccpAddress localAddress, int seqControl, ApplicationContextName acn,
			int networkId, int remotePc, TaskCallback<Exception> callback) {

		DialogPortion dp = TcapFactory.createDialogPortion();
		dp.setUnidirectional(false);

		DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
		apdu.setDoNotSendProtocolVersion(this.getStack().getDoNotSendProtocolVersion());

		Result res = TcapFactory.createResult();
		res.setResultType(ResultType.RejectedPermanent);
		ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
		rsd.setDialogServiceProviderType(pt);
		apdu.setResultSourceDiagnostic(rsd);
		apdu.setResult(res);
		apdu.setApplicationContextName(acn);
		dp.setDialogAPDU(apdu);

		TCAbortMessage msg = TcapFactory.createTCAbortMessage();
		msg.setDestinationTransactionId(remoteTransactionId);
		msg.setDialogPortion(dp);

		ByteBuf buffer;
		try {
			buffer = messageParser.encode(msg);
		} catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Failed to send message: ", e);

			callback.onError(e);
			return;
		}

		stack.newAbortSent("User", networkId);
		stack.newMessageSent(msg.getName(), buffer.readableBytes(), networkId);
		this.send(null, buffer, false, remoteAddress, localAddress, seqControl, networkId,
				localAddress.getSubsystemNumber(), remotePc, callback);
	}

	@Override
	public void postProcessElement(Object parent, Object element, ConcurrentHashMap<Integer, Object> data) {
		if (element instanceof DestinationTransactionID) {
			long dialogId = Utils.decodeTransactionId(((DestinationTransactionID) element).getValue(),
					this.stack.getSwapTcapIdBytes());
			DialogImpl di = this.getDialogById(dialogId);
			if (di != null) {
				((DestinationTransactionID) element).setDialog(di);
				ApplicationContextName acn = di.getApplicationContextName();
				if (acn != null)
					data.put(TCAP_ACN, acn);
			}
		} else if (element instanceof DialogAPDU) {
			DialogAPDU dialogAPDU = (DialogAPDU) element;
			ApplicationContextName acn = null;
			if (dialogAPDU.getType() == DialogAPDUType.Request)
				acn = ((DialogRequestAPDU) dialogAPDU).getApplicationContextName();
			else if (dialogAPDU.getType() == DialogAPDUType.Response)
				acn = ((DialogResponseAPDU) dialogAPDU).getApplicationContextName();

			if (acn != null)
				data.put(TCAP_ACN, acn);
		}
	}

	@Override
	public void preProcessElement(Object parent, Object element, ConcurrentHashMap<Integer, Object> data) {
		if (element instanceof ReturnResultInnerImpl && parent instanceof Return) {
			OperationCode oc = ((Return) parent).getOperationCode();
			if (oc != null) {
				ReturnResultInnerImpl rri = (ReturnResultInnerImpl) element;
				if (oc != null && oc.getLocalOperationCode() != null)
					rri.setOperationCode(oc.getLocalOperationCode());
				else if (oc != null && oc.getGlobalOperationCode() != null)
					rri.setOperationCode(oc.getGlobalOperationCode());
			}

			ApplicationContextName acn = (ApplicationContextName) data.remove(TCAP_ACN);
			if (acn != null)
				((ReturnResultInnerImpl) element).setACN(acn);
		}
		if (element instanceof InvokeImpl) {
			ApplicationContextName acn = (ApplicationContextName) data.remove(TCAP_ACN);
			if (acn != null)
				((InvokeImpl) element).setACN(acn);
		}
	}

	private void storeProcessTask(Dialog di, TCUnifiedMessage message, SccpAddress localAddress,
			SccpAddress remoteAddress, ByteBuf data) {
		final DialogImpl dialog = (DialogImpl) di;

		data.retain();
		RunnableTask incomingTask = new RunnableTask(new Runnable() {
			@Override
			public void run() {
				if (message instanceof TCBeginMessage)
					dialog.processBegin((TCBeginMessage) message, localAddress, remoteAddress, data);
				else if (message instanceof TCContinueMessage)
					dialog.processContinue((TCContinueMessage) message, localAddress, remoteAddress, data);
				else if (message instanceof TCEndMessage)
					dialog.processEnd((TCEndMessage) message, localAddress, remoteAddress, data);
				else if (message instanceof TCAbortMessage)
					dialog.processAbort((TCAbortMessage) message, localAddress, remoteAddress, data);
				else if (message instanceof TCUniMessage)
					dialog.processUni((TCUniMessage) message, localAddress, remoteAddress, data);

				data.release();
			}
		}, dialog.getLocalDialogId().toString(), "TcapIncomingMessageTask");

		if (this.affinityEnabled)
			this.workerPool.addTaskLast(incomingTask);
		else
			this.workerPool.getQueue().offerLast(incomingTask);
	}

	@Override
	public void onMessage(SccpDataMessage message) {
		try {
			ByteBuf data = message.getData();
			int bytes = data.readableBytes();
			SccpAddress localAddress = message.getCalledPartyAddress();
			SccpAddress remoteAddress = message.getCallingPartyAddress();

			ASNDecodeResult output = null;
			try {
				output = messageParser.decode(Unpooled.wrappedBuffer(data));
			} catch (ASNException ex) {
				logger.error("ParseException when parsing TCMessage: " + ex.toString(), ex);
				this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion, Unpooled.EMPTY_BUFFER, remoteAddress,
						localAddress, message.getSls(), message.getNetworkId(), message.getIncomingOpc(),
						dummyCallback);
				return;
			}

			if (output.getResult() instanceof TCUnifiedMessage) {
				TCUnifiedMessage realMessage = (TCUnifiedMessage) output.getResult();

				stack.newMessageReceived(realMessage.getName(), bytes, message.getNetworkId());
				DialogImpl di = ((TCUnifiedMessageImpl) realMessage).getDialog();

				Boolean shouldProceed = !output.getHadErrors();
				if (shouldProceed)
					if (shouldProceed)
						try {
							ASNParsingComponentException exception = messageParser.validateObject(realMessage);
							if (exception != null)
								shouldProceed = false;
						} catch (ASNException ex) {
							shouldProceed = false;
						}

				if (shouldProceed) {
					if (realMessage instanceof TCContinueMessage) {
						TCContinueMessage tcm = (TCContinueMessage) realMessage;
						long dialogId = Utils.decodeTransactionId(tcm.getDestinationTransactionId(),
								this.stack.getSwapTcapIdBytes());

						if (di == null)
							di = this.getDialogById(dialogId);

						if (di == null) {
							logger.warn("TC-CONTINUE: No dialog/transaction for id: " + dialogId);
							this.sendProviderAbort(PAbortCauseType.UnrecognizedTxID, tcm.getOriginatingTransactionId(),
									remoteAddress, localAddress, message.getSls(), message.getNetworkId(),
									message.getIncomingOpc(), dummyCallback);
						} else
							this.storeProcessTask(di, tcm, localAddress, remoteAddress, data);
					} else if (realMessage instanceof TCBeginMessage) {
						TCBeginMessage tcb = (TCBeginMessage) realMessage;
						if (tcb.getDialogPortion() != null && tcb.getDialogPortion().getDialogAPDU() != null
								&& tcb.getDialogPortion().getDialogAPDU() instanceof DialogRequestAPDU) {
							DialogRequestAPDU dlg = (DialogRequestAPDU) tcb.getDialogPortion().getDialogAPDU();
							if (dlg.getProtocolVersion() != null && !dlg.getProtocolVersion().isSupportedVersion()) {
								logger.error(
										"Unsupported protocol version of  has been received when parsing TCBeginMessage");
								this.sendProviderAbort(DialogServiceProviderType.NoCommonDialogPortion,
										tcb.getOriginatingTransactionId(), remoteAddress, localAddress,
										message.getSls(), dlg.getApplicationContextName(), message.getNetworkId(),
										message.getIncomingOpc(), dummyCallback);
								return;
							}
						}

						try {
							int remotePc = message.getIncomingOpc();
							di = this.getNewDialog(localAddress, remoteAddress, message.getSls(), null);
							di.setRemotePc(remotePc);
							setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
							stack.newIncomingDialogProcessed(message.getNetworkId());
						} catch (TCAPException e) {
							this.sendProviderAbort(PAbortCauseType.ResourceLimitation,
									tcb.getOriginatingTransactionId(), remoteAddress, localAddress, message.getSls(),
									message.getNetworkId(), message.getIncomingOpc(), dummyCallback);
							logger.error("Can not add a new dialog when receiving TCBeginMessage: " + e.getMessage(),
									e);
							return;
						}

						di.setNetworkId(message.getNetworkId());
						this.storeProcessTask(di, tcb, localAddress, remoteAddress, data);
					} else if (realMessage instanceof TCEndMessage) {

						TCEndMessage teb = (TCEndMessage) realMessage;
						long dialogId = Utils.decodeTransactionId(teb.getDestinationTransactionId(),
								this.stack.getSwapTcapIdBytes());
						if (di == null)
							di = this.getDialogById(dialogId);

						if (di == null)
							logger.warn("TC-END: No dialog/transaction for id: " + dialogId);
						else
							di.processEnd(teb, localAddress, remoteAddress, data);
					} else if (realMessage instanceof TCAbortMessage) {
						TCAbortMessage tub = (TCAbortMessage) realMessage;
						Long dialogId = null;
						if (tub.getDestinationTransactionId() != null) {
							dialogId = Utils.decodeTransactionId(tub.getDestinationTransactionId(),
									this.stack.getSwapTcapIdBytes());

							if (di == null)
								di = this.getDialogById(dialogId);
						}

						if (di == null)
							logger.warn("TC-ABORT: No dialog/transaction for id: " + dialogId);
						else {
							if (tub.getPAbortCause() != null)
								stack.newAbortReceived(tub.getPAbortCause().name(), message.getNetworkId());
							else
								stack.newAbortReceived("User", message.getNetworkId());

							this.storeProcessTask(di, tub, localAddress, remoteAddress, data);
						}
					} else if (realMessage instanceof TCUniMessage) {
						TCUniMessage tcuni = (TCUniMessage) realMessage;
						int remotePc = message.getIncomingOpc();
						DialogImpl uniDialog = (DialogImpl) this.getNewUnstructuredDialog(localAddress, remoteAddress,
								message.getNetworkId());
						uniDialog.setRemotePc(remotePc);

						setSsnToDialog(uniDialog, message.getCalledPartyAddress().getSubsystemNumber());
						this.storeProcessTask(uniDialog, tcuni, localAddress, remoteAddress, data);
					} else

						unrecognizedPackageType(message, PAbortCauseType.UnrecognizedMessageType,
								realMessage.getOriginatingTransactionId(), localAddress, remoteAddress,
								message.getNetworkId());
				} else {
					if (realMessage instanceof TCBeginMessage)

					{
						TCBeginMessage tcb = (TCBeginMessage) realMessage;
						if (tcb.getDialogPortion() != null && tcb.getDialogPortion().getDialogAPDU() != null
								&& tcb.getDialogPortion().getDialogAPDU() instanceof DialogRequestAPDU) {
							DialogRequestAPDU dlg = (DialogRequestAPDU) tcb.getDialogPortion().getDialogAPDU();
							if (dlg.getProtocolVersion() != null && !dlg.getProtocolVersion().isSupportedVersion()) {
								logger.error(
										"Unsupported protocol version of  has been received when parsing TCBeginMessage");
								this.sendProviderAbort(DialogServiceProviderType.NoCommonDialogPortion,
										tcb.getOriginatingTransactionId(), remoteAddress, localAddress,
										message.getSls(), dlg.getApplicationContextName(), message.getNetworkId(),
										message.getIncomingOpc(), dummyCallback);
								return;
							}
						}
					}

					if (output.getFirstError() != null && output.getFirstError().getParent() != null
							&& output.getFirstError().getParent().getClass().getPackage()
									.equals(TCUniMessageImpl.class.getPackage()))

						unrecognizedPackageType(message, PAbortCauseType.IncorrectTxPortion,
								realMessage.getOriginatingTransactionId(), localAddress, remoteAddress,
								message.getNetworkId());
					else
						unrecognizedPackageType(message, PAbortCauseType.UnrecognizedMessageType,
								realMessage.getOriginatingTransactionId(), localAddress, remoteAddress,
								message.getNetworkId());
				}
			} else
				unrecognizedPackageType(message, PAbortCauseType.UnrecognizedMessageType, null, localAddress,
						remoteAddress, message.getNetworkId());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("Error while decoding Rx SccpMessage=%s", message), e);
		}
	}

	private void unrecognizedPackageType(SccpDataMessage message, PAbortCauseType abortCausetype, ByteBuf transactionID,
			SccpAddress localAddress, SccpAddress remoteAddress, int networkId) throws ParseException {
		logger.error(String.format("Rx unidentified.SccpMessage=%s", message));
		this.sendProviderAbort(abortCausetype, transactionID, remoteAddress, localAddress, message.getSls(), networkId,
				message.getIncomingOpc(), dummyCallback);
	}

	@Override
	public void onNotice(SccpNoticeMessage msg) {

		DialogImpl dialog = null;

		try {
			ByteBuf data = msg.getData();
			ASNDecodeResult output = messageParser.decode(Unpooled.wrappedBuffer(data));

			if (output.getHadErrors())
				logger.error(String.format("Error while decoding Rx SccpNoticeMessage=%s", msg));
			else if (output.getResult() instanceof TCUnifiedMessage) {
				TCUnifiedMessage tcUnidentified = (TCUnifiedMessage) output.getResult();
				if (tcUnidentified.getOriginatingTransactionId() != null) {
					long otid = Utils.decodeTransactionId(tcUnidentified.getOriginatingTransactionId(),
							this.stack.getSwapTcapIdBytes());
					dialog = this.getDialogById(otid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("Error while decoding Rx SccpNoticeMessage=%s", msg), e);
		}

		TCNoticeIndication ind = new TCNoticeIndicationImpl();
		ind.setRemoteAddress(msg.getCallingPartyAddress());
		ind.setLocalAddress(msg.getCalledPartyAddress());
		ind.setDialog(dialog);
		ind.setReportCause(msg.getReturnCause().getValue());

		if (dialog != null) {
			this.deliver(dialog, ind);

			if (dialog.getState() != TRPseudoState.Active)
				dialog.release();
		} else
			this.deliver(dialog, ind);
	}

	@Override
	public DraftParsedMessage parseMessageDraft(ByteBuf data) {
		try {
			DraftParsedMessageImpl res = new DraftParsedMessageImpl();
			ASNDecodeResult output = messageParser.decode(data);
			if (!(output.getResult() instanceof TCUnifiedMessage)) {
				res.setParsingErrorReason("Invalid message found");
				return res;
			}

			res.setMessage((TCUnifiedMessage) output.getResult());
			return res;
		} catch (Exception e) {
			DraftParsedMessageImpl res = new DraftParsedMessageImpl();
			res.setParsingErrorReason("Exception when message parsing: " + e.getMessage());
			return res;
		}
	}

	@Override
	public void onCoordResponse(int ssn, int multiplicityIndicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onState(int dpc, int ssn, boolean inService, int multiplicityIndicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel,
			RemoteSccpStatus remoteSccpStatus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectIndication(SccpConnection conn, SccpAddress calledAddress, SccpAddress callingAddress,
			ProtocolClass clazz, Credit credit, ByteBuf data, Importance importance) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnectConfirm(SccpConnection conn, ByteBuf data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisconnectIndication(SccpConnection conn, ReleaseCause reason, ByteBuf data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisconnectIndication(SccpConnection conn, RefusalCause reason, ByteBuf data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisconnectIndication(SccpConnection conn, ErrorCause errorCause) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResetIndication(SccpConnection conn, ResetCause reason) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResetConfirm(SccpConnection conn) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onData(SccpConnection conn, ByteBuf data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisconnectConfirm(SccpConnection conn) {
		// TODO Auto-generated method stub
	}

	@Override
	public ASNParser getParser() {
		return messageParser;
	}

	@Override
	public void setAffinity(boolean isEnabled) {
		this.affinityEnabled = isEnabled;
	}
}
