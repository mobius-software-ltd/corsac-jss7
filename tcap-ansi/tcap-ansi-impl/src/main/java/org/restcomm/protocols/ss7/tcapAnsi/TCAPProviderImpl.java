/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.tcapAnsi;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
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
import org.restcomm.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcapAnsi.api.DialogPrimitiveFactory;
import org.restcomm.protocols.ss7.tcapAnsi.api.MessageType;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCListener;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCConversationMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCResponseMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.TRPseudoState;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.DraftParsedMessage;
import org.restcomm.protocols.ss7.tcapAnsi.asn.InvokeImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TCAbortMessageImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TCNoticeIndicationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TCResponseMessageImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TCUnidentifiedMessage;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TransactionID;
import org.restcomm.protocols.ss7.tcapAnsi.asn.Utils;
import org.restcomm.protocols.ss7.tcapAnsi.tc.component.ComponentPrimitiveFactoryImpl;
import org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events.DraftParsedMessageImpl;
import org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events.TCConversationIndicationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events.TCPAbortIndicationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events.TCQueryIndicationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events.TCResponseIndicationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events.TCUniIndicationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.tc.dialog.events.TCUserAbortIndicationImpl;

/**
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class TCAPProviderImpl implements TCAPProvider, SccpListener {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(TCAPProviderImpl.class); // listenres

    private transient List<TCListener> tcListeners = new CopyOnWriteArrayList<TCListener>();
    protected transient ScheduledExecutorService service;
    // boundry for Uni directional dialogs :), tx id is always encoded
    // on 4 octets, so this is its max value
    // private static final long _4_OCTETS_LONG_FILL = 4294967295l;
    private transient ComponentPrimitiveFactory componentPrimitiveFactory;
    private transient DialogPrimitiveFactory dialogPrimitiveFactory;
    private transient SccpProvider sccpProvider;

    private transient MessageFactory messageFactory;
    
    private transient TCAPStackImpl stack; // originating TX id ~=Dialog, its direct
    // mapping, but not described
    // explicitly...

//    private transient FastMap<Long, DialogImpl> dialogs = new FastMap<Long, DialogImpl>();
    private transient ConcurrentHashMap<Long, DialogImpl> dialogs = new ConcurrentHashMap<Long, DialogImpl>();
    
//    protected transient FastMap<PrevewDialogDataKey, PreviewDialogData> dialogPreviewList = new FastMap<PrevewDialogDataKey, PreviewDialogData>();
    protected transient ConcurrentHashMap<PreviewDialogDataKey, PreviewDialogData> dialogPreviewList = new ConcurrentHashMap<PreviewDialogDataKey, PreviewDialogData>();

    private AtomicInteger seqControl = new AtomicInteger(1);
    private int ssn;
    private AtomicLong curDialogId = new AtomicLong(0);

    protected TCAPProviderImpl(SccpProvider sccpProvider, TCAPStackImpl stack, int ssn,ScheduledExecutorService service) {
        super();
        this.sccpProvider = sccpProvider;
        this.ssn = ssn;
        messageFactory = sccpProvider.getMessageFactory();
        this.stack = stack;

        this.componentPrimitiveFactory = new ComponentPrimitiveFactoryImpl(this);
        this.dialogPrimitiveFactory = new DialogPrimitiveFactoryImpl(this.componentPrimitiveFactory);
        this.service=service;
    }

    public boolean getPreviewMode() {
        return this.stack.getPreviewMode();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPStack#addTCListener(org.mobicents .protocols.ss7.tcap.api.TCListener)
     */

    public void addTCListener(TCListener lst) {
        if (this.tcListeners.contains(lst)) {
        } else {
            this.tcListeners.add(lst);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPStack#removeTCListener(org.mobicents .protocols.ss7.tcap.api.TCListener)
     */
    public void removeTCListener(TCListener lst) {
        this.tcListeners.remove(lst);

    }

    private boolean checkAvailableTxId(Long id) {
        if (!this.dialogs.containsKey(id))
            return true;
        else
            return false;
    }

    private Long getAvailableTxId() throws TCAPException {
        while (true) {
        	this.curDialogId.compareAndSet(this.stack.getDialogIdRangeEnd(), this.stack.getDialogIdRangeStart()-1);

        	Long id = this.curDialogId.incrementAndGet();
            if (checkAvailableTxId(id))
                return id;
        }
    }

    protected void resetDialogIdValueAfterRangeChange() {
    	if(this.curDialogId.get()<this.stack.getDialogIdRangeStart())
    		this.curDialogId.set(this.stack.getDialogIdRangeStart());
    	
    	if(this.curDialogId.get()>this.stack.getDialogIdRangeEnd())
    		this.curDialogId.set(this.stack.getDialogIdRangeEnd()-1);
    	
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

        if (this.stack.getSlsRangeType() == SlsRangeType.Odd) {
            if (res % 2 == 0)
                res++;
        } else if (this.stack.getSlsRangeType() == SlsRangeType.Even) {
            if (res %2 != 0)
                res++;
        }
        res = res & 0xFF;

        return res;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.protocols.ss7.tcap.api.TCAPProvider# getComopnentPrimitiveFactory()
     */
    public ComponentPrimitiveFactory getComponentPrimitiveFactory() {

        return this.componentPrimitiveFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPProvider#getDialogPrimitiveFactory ()
     */
    public DialogPrimitiveFactory getDialogPrimitiveFactory() {

        return this.dialogPrimitiveFactory;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPProvider#getNewDialog(org.mobicents
     * .protocols.ss7.sccp.parameter.SccpAddress, org.restcomm.protocols.ss7.sccp.parameter.SccpAddress)
     */
    public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
        DialogImpl res = getNewDialog(localAddress, remoteAddress, getNextSeqControl(), null);
        this.setSsnToDialog(res, localAddress.getSubsystemNumber());
        return res;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPProvider#getNewDialog(org.mobicents
     * .protocols.ss7.sccp.parameter.SccpAddress, org.restcomm.protocols.ss7.sccp.parameter.SccpAddress, Long id)
     */
    public Dialog getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, Long id) throws TCAPException {
        DialogImpl res = getNewDialog(localAddress, remoteAddress, getNextSeqControl(), id);
        this.setSsnToDialog(res, localAddress.getSubsystemNumber());
        return res;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPProvider#getNewUnstructuredDialog
     * (org.restcomm.protocols.ss7.sccp.parameter.SccpAddress, org.restcomm.protocols.ss7.sccp.parameter.SccpAddress)
     */
    public Dialog getNewUnstructuredDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
        DialogImpl res = _getDialog(localAddress, remoteAddress, false, getNextSeqControl(), null);
        this.setSsnToDialog(res, localAddress.getSubsystemNumber());
        return res;
    }

    private DialogImpl getNewDialog(SccpAddress localAddress, SccpAddress remoteAddress, int seqControl, Long id) throws TCAPException {
        return _getDialog(localAddress, remoteAddress, true, seqControl, id);
    }

    private DialogImpl _getDialog(SccpAddress localAddress, SccpAddress remoteAddress, boolean structured, int seqControl, Long id)
            throws TCAPException {

        if (this.stack.getPreviewMode()) {
            throw new TCAPException("Can not create a Dialog in a PreviewMode");
        }

        if (localAddress == null) {
            throw new NullPointerException("LocalAddress must not be null");
        }

        if (id == null) {
            id = this.getAvailableTxId();
        } else {
            if (!checkAvailableTxId(id)) {
                throw new TCAPException("Suggested local TransactionId is already present in system: " + id);
            }
        }
        if (structured) {
            DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this.service, this, seqControl,
                    this.stack.getPreviewMode());

            this.dialogs.put(id, di);
            
            return di;
        } else {
            DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this.service, this, seqControl,
                    this.stack.getPreviewMode());
            
            return di;
        }

        // }

    }

    private void setSsnToDialog(DialogImpl di, int ssn) {
        if (ssn != this.ssn) {
            if (ssn <= 0 || !this.stack.isExtraSsnPresent(ssn))
                ssn = this.ssn;
        }
        di.setLocalSsn(ssn);
    }

    @Override
    public int getCurrentDialogsCount() {
        return this.dialogs.size();
    }

    public void send(byte[] data, boolean returnMessageOnError, SccpAddress destinationAddress, SccpAddress originatingAddress,
            int seqControl, int networkId, int localSsn) throws IOException {
        if (this.stack.getPreviewMode())
            return;

        SccpDataMessage msg = messageFactory.createDataMessageClass1(destinationAddress, originatingAddress, data, seqControl,
                localSsn, returnMessageOnError, null, null);
        msg.setNetworkId(networkId);
        sccpProvider.send(msg);
    }

    public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int networkId) {
        return this.sccpProvider.getMaxUserDataLength(calledPartyAddress, callingPartyAddress, networkId);
    }

    public void deliver(DialogImpl dialogImpl, TCQueryIndicationImpl msg) {

        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCQuery(msg);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCConversationIndicationImpl tcContinueIndication) {

        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCConversation(tcContinueIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCResponseIndicationImpl tcEndIndication) {

        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCResponse(tcEndIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }
    }

    public void deliver(DialogImpl dialogImpl, TCPAbortIndicationImpl tcAbortIndication) {

        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCPAbort(tcAbortIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCUserAbortIndicationImpl tcAbortIndication) {

        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCUserAbort(tcAbortIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCUniIndicationImpl tcUniIndication) {

        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCUni(tcUniIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }
    }

    public void deliver(DialogImpl dialogImpl, TCNoticeIndicationImpl tcNoticeIndication) {
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCNotice(tcNoticeIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }
    }

    public void release(DialogImpl d) {
        Long did = d.getLocalDialogId();

        if (!d.getPreviewMode()) {
        	this.dialogs.remove(did);
            this.doRelease(d);
        }
    }

    private void doRelease(DialogImpl d) {

        try {
            for (TCListener lst : this.tcListeners) {
                lst.onDialogReleased(d);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering dialog release.", e);
            }
        }
    }

    /**
     * @param d
     */
    public void timeout(DialogImpl d) {

        try {
            for (TCListener lst : this.tcListeners) {
                lst.onDialogTimeout(d);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering dialog release.", e);
            }
        }
    }

    public TCAPStackImpl getStack() {
        return this.stack;
    }

    // ///////////////////////////////////////////
    // Some methods invoked by operation FSM //
    // //////////////////////////////////////////
    public Future<?> createOperationTimer(Runnable operationTimerTask, long invokeTimeout) {

        return this.service.schedule(operationTimerTask, invokeTimeout, TimeUnit.MILLISECONDS);
    }

    public void operationTimedOut(InvokeImpl tcInvokeRequestImpl) {
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onInvokeTimeout(tcInvokeRequestImpl);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering Begin.", e);
            }
        }
    }

    void start() {
        logger.info("Starting TCAP Provider");
        this.sccpProvider.registerSccpListener(ssn, this);
        logger.info("Registered SCCP listener with ssn " + ssn);

        if(this.stack.getExtraSsns()!=null) {
	        Iterator<Integer> extraSsns = this.stack.getExtraSsns().iterator();
	        while (extraSsns.hasNext()) {
	        	int extraSsn = extraSsns.next();
                this.sccpProvider.deregisterSccpListener(extraSsn);
            }
        }
    }

    void stop() {
        this.sccpProvider.deregisterSccpListener(ssn);

        if(this.stack.getExtraSsns()!=null) {
	        Iterator<Integer> extraSsns = this.stack.getExtraSsns().iterator();
	        if (extraSsns != null) {
	            while(extraSsns.hasNext()) {
	            	int extraSsn = extraSsns.next();
                    this.sccpProvider.registerSccpListener(extraSsn, this);
                    logger.info("Registered SCCP listener with extra ssn " + extraSsn);
	            }
	        }
        }

        this.dialogs.clear();
        this.dialogPreviewList.clear();
    }

    protected void sendProviderAbort(PAbortCause pAbortCause, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl, int networkId) {
        if (this.stack.getPreviewMode())
            return;

        TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
        msg.setDestinationTransactionId(remoteTransactionId);
        msg.setPAbortCause(pAbortCause);

        AsnOutputStream aos = new AsnOutputStream();
        try {
            msg.encode(aos);
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl, networkId, localAddress.getSubsystemNumber());
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
        }
    }

    protected void sendRejectAsProviderAbort(PAbortCause pAbortCause, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl, int networkId) {
        if (this.stack.getPreviewMode())
            return;

        RejectProblem rp = RejectProblem.getFromPAbortCause(pAbortCause);
        if (rp == null)
            rp = RejectProblem.transactionBadlyStructuredTransPortion;

        TCResponseMessageImpl msg = (TCResponseMessageImpl) TcapFactory.createTCResponseMessage();
        msg.setDestinationTransactionId(remoteTransactionId);
        Component[] cc = new Component[1];
        Reject r = TcapFactory.createComponentReject();
        r.setProblem(rp);
        cc[0] = r;
        msg.setComponent(cc);

        AsnOutputStream aos = new AsnOutputStream();
        try {
            msg.encode(aos);
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl, networkId, localAddress.getSubsystemNumber());
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
        }
    }

    public void onMessage(SccpDataMessage message) {

        try {
            byte[] data = message.getData();
            SccpAddress localAddress = message.getCalledPartyAddress();
            SccpAddress remoteAddress = message.getCallingPartyAddress();

            // asnData - it should pass
            AsnInputStream ais = new AsnInputStream(data);

            // this should have TC message tag :)
            int tag = ais.readTag();

            if (ais.getTagClass() != Tag.CLASS_PRIVATE) {
                unrecognizedPackageType(message, localAddress, remoteAddress, ais, tag, message.getNetworkId());
                return;
            }

            switch (tag) {
                case TCConversationMessage._TAG_CONVERSATION_WITH_PERM:
                case TCConversationMessage._TAG_CONVERSATION_WITHOUT_PERM:
                    TCConversationMessage tcm = null;
                    try {
                        tcm = TcapFactory.createTCConversationMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCConversationMessage: " + e.toString(), e);

                        // parsing OriginatingTransactionId
                        ais = new AsnInputStream(data);
                        tag = ais.readTag();
                        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
                        tcUnidentified.decode(ais);
                        if (tcUnidentified.getOriginatingTransactionId() != null) {
                            boolean isDP = false;
                            if (tcUnidentified.isDialogPortionExists()) {
                                isDP = true;
                            } else {
                                Dialog ddi = null;
                                if (tcUnidentified.getDestinationTransactionId() != null) {
                                    long dialogId = Utils.decodeTransactionId(tcUnidentified.getDestinationTransactionId(), this.stack.getSwapTcapIdBytes());
                                    ddi = this.dialogs.get(dialogId);
                                }
                                if (ddi != null && ddi.getProtocolVersion() != null)
                                    isDP = true;
                            }
                            if (isDP) {
                                if (e.getPAbortCauseType() != null) {
                                    this.sendProviderAbort(e.getPAbortCauseType(),
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                } else {
                                    this.sendProviderAbort(PAbortCause.BadlyStructuredDialoguePortion,
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                }
                            } else {
                                if (e.getPAbortCauseType() != null) {
                                    this.sendRejectAsProviderAbort(e.getPAbortCauseType(),
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                } else {
                                    this.sendRejectAsProviderAbort(PAbortCause.BadlyStructuredTransactionPortion,
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                }
                            }
                        }
                        return;
                    }

                    long dialogId = Utils.decodeTransactionId(tcm.getDestinationTransactionId(), this.stack.getSwapTcapIdBytes());
                    DialogImpl di;
                    if (this.stack.getPreviewMode()) {
                        PreviewDialogDataKey ky1 = new PreviewDialogDataKey(message.getIncomingDpc(), (message
                                .getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCalledPartyAddress().getSubsystemNumber(),
                                dialogId);
                        long dId = Utils.decodeTransactionId(tcm.getOriginatingTransactionId(), this.stack.getSwapTcapIdBytes());
                        PreviewDialogDataKey ky2 = new PreviewDialogDataKey(message.getIncomingOpc(), (message
                                .getCallingPartyAddress().getGlobalTitle() != null ? message.getCallingPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCallingPartyAddress().getSubsystemNumber(),
                                dId);
                        di = (DialogImpl) this.getPreviewDialog(ky1, ky2, localAddress, remoteAddress, 0);
                        setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                    } else {
                        di = this.dialogs.get(dialogId);
                    }
                    if (di == null) {
                        logger.warn("TC-Conversation: No dialog/transaction for id: " + dialogId);
                        if (tcm.getDialogPortion() != null) {
                            this.sendProviderAbort(PAbortCause.UnassignedRespondingTransactionID,
                                    tcm.getOriginatingTransactionId(), remoteAddress, localAddress, message.getSls(),
                                    message.getNetworkId());
                        } else {
                            this.sendRejectAsProviderAbort(PAbortCause.UnassignedRespondingTransactionID,
                                    tcm.getOriginatingTransactionId(), remoteAddress, localAddress, message.getSls(),
                                    message.getNetworkId());
                        }
                    } else {
                        di.processConversation(tcm, localAddress, remoteAddress,
                                tag == TCConversationMessage._TAG_CONVERSATION_WITH_PERM);
                    }

                    break;

                case TCQueryMessage._TAG_QUERY_WITH_PERM:
                case TCQueryMessage._TAG_QUERY_WITHOUT_PERM:
                    TCQueryMessage tcb = null;
                    try {
                        tcb = TcapFactory.createTCQueryMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCQueryMessage: " + e.toString(), e);

                        // parsing OriginatingTransactionId
                        ais = new AsnInputStream(data);
                        tag = ais.readTag();
                        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
                        tcUnidentified.decode(ais);
                        if (tcUnidentified.getOriginatingTransactionId() != null) {
                            if (tcUnidentified.isDialogPortionExists()) {
                                if (e.getPAbortCauseType() != null) {
                                    this.sendProviderAbort(e.getPAbortCauseType(),
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                } else {
                                    this.sendProviderAbort(PAbortCause.BadlyStructuredDialoguePortion,
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                }
                            } else {
                                if (e.getPAbortCauseType() != null) {
                                    this.sendRejectAsProviderAbort(e.getPAbortCauseType(),
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                } else {
                                    this.sendRejectAsProviderAbort(PAbortCause.BadlyStructuredTransactionPortion,
                                            tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                            message.getSls(), message.getNetworkId());
                                }
                            }
                        }
                        return;
                    }

                    di = null;
                    try {
                        if (this.stack.getPreviewMode()) {
                            long dId = Utils.decodeTransactionId(tcb.getOriginatingTransactionId(), this.stack.getSwapTcapIdBytes());
                            PreviewDialogDataKey ky = new PreviewDialogDataKey(message.getIncomingOpc(), (message
                                    .getCallingPartyAddress().getGlobalTitle() != null ? message.getCallingPartyAddress()
                                    .getGlobalTitle().getDigits() : null), message.getCallingPartyAddress()
                                    .getSubsystemNumber(), dId);
                            di = (DialogImpl) this.createPreviewDialog(ky, localAddress, remoteAddress, 0);
                            setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                        } else {
                            di = (DialogImpl) this.getNewDialog(localAddress, remoteAddress, message.getSls(), null);
                            setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                        }

                    } catch (TCAPException e) {
                        if (tcb.getDialogPortion() != null) {
                            this.sendProviderAbort(PAbortCause.ResourceUnavailable, tcb.getOriginatingTransactionId(),
                                    remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                        } else {
                            this.sendRejectAsProviderAbort(PAbortCause.ResourceUnavailable, tcb.getOriginatingTransactionId(),
                                    remoteAddress, localAddress, message.getSls(), message.getNetworkId());
                        }
                        logger.error("Can not add a new dialog when receiving TCBeginMessage: " + e.getMessage(), e);
                        return;
                    }

                    if (tcb.getDialogPortion() != null) {
                        if (tcb.getDialogPortion().getProtocolVersion() != null) {
                            di.setProtocolVersion(tcb.getDialogPortion().getProtocolVersion());
                        } else {
                            ProtocolVersion pv = TcapFactory.createProtocolVersionEmpty();
                            di.setProtocolVersion(pv);
                        }
                    }

                    di.setNetworkId(message.getNetworkId());
                    di.processQuery(tcb, localAddress, remoteAddress, tag == TCQueryMessage._TAG_QUERY_WITH_PERM);

                    if (this.stack.getPreviewMode()) {
                        di.getPrevewDialogData().setLastACN(di.getApplicationContextName());
                        di.getPrevewDialogData().setOperationsSentB(di.operationsSent);
                        di.getPrevewDialogData().setOperationsSentA(di.operationsSentA);
                    }

                    break;

                case TCResponseMessage._TAG_RESPONSE:
                    TCResponseMessage teb = null;
                    try {
                        teb = TcapFactory.createTCResponseMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCResponseMessage: " + e.toString(), e);
                        return;
                    }

                    dialogId = Utils.decodeTransactionId(teb.getDestinationTransactionId(), this.stack.getSwapTcapIdBytes());
                    if (this.stack.getPreviewMode()) {
                        PreviewDialogDataKey ky = new PreviewDialogDataKey(message.getIncomingDpc(), (message
                                .getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCalledPartyAddress().getSubsystemNumber(),
                                dialogId);
                        di = (DialogImpl) this.getPreviewDialog(ky, null, localAddress, remoteAddress, 0);
                        setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                    } else {
                        di = this.dialogs.get(dialogId);
                    }
                    if (di == null) {
                        logger.warn("TC-Response: No dialog/transaction for id: " + dialogId);
                    } else {
                        di.processResponse(teb, localAddress, remoteAddress);

                        if (this.stack.getPreviewMode()) {
                            this.removePreviewDialog(di);
                        }
                    }
                    break;

                case TCAbortMessage._TAG_ABORT:
                    TCAbortMessage tub = null;
                    try {
                        tub = TcapFactory.createTCAbortMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCAbortMessage: " + e.toString(), e);
                        return;
                    }

                    dialogId = Utils.decodeTransactionId(tub.getDestinationTransactionId(), this.stack.getSwapTcapIdBytes());
                    if (this.stack.getPreviewMode()) {
                        long dId = Utils.decodeTransactionId(tub.getDestinationTransactionId(), this.stack.getSwapTcapIdBytes());
                        PreviewDialogDataKey ky = new PreviewDialogDataKey(message.getIncomingDpc(), (message
                                .getCalledPartyAddress().getGlobalTitle() != null ? message.getCalledPartyAddress()
                                .getGlobalTitle().getDigits() : null), message.getCalledPartyAddress().getSubsystemNumber(),
                                dId);
                        di = (DialogImpl) this.getPreviewDialog(ky, null, localAddress, remoteAddress, 0);
                        setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                    } else {
                        di = this.dialogs.get(dialogId);
                    }
                    if (di == null) {
                        logger.warn("TC-ABORT: No dialog/transaction for id: " + dialogId);
                    } else {
                        di.processAbort(tub, localAddress, remoteAddress);

                        if (this.stack.getPreviewMode()) {
                            this.removePreviewDialog(di);
                        }
                    }
                    break;

                case TCUniMessage._TAG_UNI:
                    TCUniMessage tcuni;
                    try {
                        tcuni = TcapFactory.createTCUniMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCUniMessage: " + e.toString(), e);
                        return;
                    }

                    DialogImpl uniDialog = (DialogImpl) this.getNewUnstructuredDialog(localAddress, remoteAddress);
                    setSsnToDialog(uniDialog, message.getCalledPartyAddress().getSubsystemNumber());
                    uniDialog.processUni(tcuni, localAddress, remoteAddress);
                    break;

                default:
                    unrecognizedPackageType(message, localAddress, remoteAddress, ais, tag, message.getNetworkId());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("Error while decoding Rx SccpMessage=%s", message), e);
        }
    }

    private void unrecognizedPackageType(SccpDataMessage message, SccpAddress localAddress, SccpAddress remoteAddress, AsnInputStream ais, int tag,
            int networkId) {
        if (this.stack.getPreviewMode()) {
            return;
        }

        logger.error(String.format("Rx unidentified messageType=%s. SccpMessage=%s", tag, message));
        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
        try {
            tcUnidentified.decode(ais);
        } catch (ParseException e) {
            // we do nothing
        }

        if (tcUnidentified.getOriginatingTransactionId() != null) {
            byte[] otid = tcUnidentified.getOriginatingTransactionId();

            if (tcUnidentified.getDestinationTransactionId() != null) {
                Utils.decodeTransactionId(tcUnidentified.getDestinationTransactionId(), this.stack.getSwapTcapIdBytes());
                this.sendProviderAbort(PAbortCause.UnrecognizedPackageType, otid, remoteAddress, localAddress, message.getSls(), networkId);
            } else {
                this.sendProviderAbort(PAbortCause.UnrecognizedPackageType, otid, remoteAddress, localAddress, message.getSls(), networkId);
            }
        } else {
            this.sendProviderAbort(PAbortCause.UnrecognizedPackageType, new byte[4], remoteAddress, localAddress, message.getSls(), networkId);
        }
    }

    public void onNotice(SccpNoticeMessage msg) {

        if (this.stack.getPreviewMode()) {
            return;
        }

        DialogImpl dialog = null;

        try {
            byte[] data = msg.getData();
            AsnInputStream ais = new AsnInputStream(data);

            // this should have TC message tag :)
            ais.readTag();

            TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
            tcUnidentified.decode(ais);

            if (tcUnidentified.getOriginatingTransactionId() != null) {
                long otid = Utils.decodeTransactionId(tcUnidentified.getOriginatingTransactionId(), this.stack.getSwapTcapIdBytes());
                dialog = this.dialogs.get(otid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("Error while decoding Rx SccpNoticeMessage=%s", msg), e);
        }

        TCNoticeIndicationImpl ind = new TCNoticeIndicationImpl();
        ind.setRemoteAddress(msg.getCallingPartyAddress());
        ind.setLocalAddress(msg.getCalledPartyAddress());
        ind.setDialog(dialog);
        ind.setReportCause(msg.getReturnCause().getValue());

        if (dialog != null) {
        	this.deliver(dialog, ind);

            if (dialog.getState() != TRPseudoState.Active) {
                dialog.release();
            }
        } else {
            this.deliver(dialog, ind);
        }
    }

    protected Dialog createPreviewDialog(PreviewDialogDataKey ky, SccpAddress localAddress, SccpAddress remoteAddress,
            int seqControl) throws TCAPException {
        Long dialogId = this.getAvailableTxIdPreview();
        PreviewDialogData pdd = new PreviewDialogData(this, dialogId);
        pdd.setPrevewDialogDataKey1(ky);
        PreviewDialogData pddx = this.dialogPreviewList.putIfAbsent(ky, pdd);
        if (pddx != null) {
            this.removePreviewDialog(pddx);
            throw new TCAPException("Dialog with trId=" + ky.origTxId + " is already exists - we ignore it and drops curent dialog");
        }

        DialogImpl di = new DialogImpl(localAddress, remoteAddress, seqControl, this.service, this, pdd, false);

        pdd.startIdleTimer();

        return di;
    }

    protected Long getAvailableTxIdPreview() throws TCAPException {
        while (true) {
//            Long id;
//            if (!currentDialogId.compareAndSet(this.stack.getDialogIdRangeEnd(), this.stack.getDialogIdRangeStart() + 1)) {
//                id = currentDialogId.getAndIncrement();
//            } else {
//                id = this.stack.getDialogIdRangeStart();
//            }
//            return id;

        	this.curDialogId.compareAndSet(this.stack.getDialogIdRangeStart()-1, this.stack.getDialogIdRangeStart());
        	this.curDialogId.incrementAndGet();
        	this.curDialogId.compareAndSet(this.stack.getDialogIdRangeEnd()+1, this.stack.getDialogIdRangeStart());

        	Long id = this.curDialogId.get();
            return id;
        }
    }

    protected Dialog getPreviewDialog(PreviewDialogDataKey ky1, PreviewDialogDataKey ky2, SccpAddress localAddress,
            SccpAddress remoteAddress, int seqControl) {

        PreviewDialogData pdd = this.dialogPreviewList.get(ky1);
        DialogImpl di = null;
        boolean sideB = false;
        if (pdd != null) {
            sideB = pdd.getPrevewDialogDataKey1().equals(ky1);
            di = new DialogImpl(localAddress, remoteAddress, seqControl, this.service, this, pdd, sideB);
        } else {
            if (ky2 != null)
                pdd = this.dialogPreviewList.get(ky2);
            if (pdd != null) {
                sideB = pdd.getPrevewDialogDataKey1().equals(ky1);
                di = new DialogImpl(localAddress, remoteAddress, seqControl, this.service, this, pdd, sideB);
            } else {
                return null;
            }
        }

        pdd.restartIdleTimer();

        if (pdd.getPrevewDialogDataKey2() == null && ky2 != null) {
            if (pdd.getPrevewDialogDataKey1().equals(ky1))
                pdd.setPrevewDialogDataKey2(ky2);
            else
                pdd.setPrevewDialogDataKey2(ky1);
            this.dialogPreviewList.put(pdd.getPrevewDialogDataKey2(), pdd);
        }

        return di;

        // }

    }

    protected void removePreviewDialog(DialogImpl di) {

        PreviewDialogData pdd = this.dialogPreviewList.get(di.prevewDialogData.getPrevewDialogDataKey1());
        if (pdd == null && di.prevewDialogData.getPrevewDialogDataKey2() != null) {
            pdd = this.dialogPreviewList.get(di.prevewDialogData.getPrevewDialogDataKey2());
        }

        if (pdd != null)
            removePreviewDialog(pdd);

        this.doRelease(di);
    }

    protected void removePreviewDialog(PreviewDialogData pdd) {

        this.dialogPreviewList.remove(pdd.getPrevewDialogDataKey1());
        if (pdd.getPrevewDialogDataKey2() != null) {
            this.dialogPreviewList.remove(pdd.getPrevewDialogDataKey2());
        }

        pdd.stopIdleTimer();

        // TODO ??? : create Dialog and invoke "this.doRelease(di);"
    }
    @Override
    public void onCoordResponse(int ssn, int multiplicityIndicator) {
        // TODO Auto-generated method stub

    }

    @Override
    public DraftParsedMessage parseMessageDraft(byte[] data) {
        try {
            DraftParsedMessageImpl res = new DraftParsedMessageImpl();

            AsnInputStream ais = new AsnInputStream(data);

            int tag = ais.readTag();

            if (ais.getTagClass() != Tag.CLASS_PRIVATE) {
                res.setParsingErrorReason("Message tag class must be CLASS_PRIVATE");
                ais.close();
                return res;
            }

            switch (tag) {
                case TCConversationMessage._TAG_CONVERSATION_WITH_PERM:
                case TCConversationMessage._TAG_CONVERSATION_WITHOUT_PERM:
                    AsnInputStream localAis = ais.readSequenceStream();

                    // transaction portion
                    TransactionID tid = TcapFactory.readTransactionID(localAis);
                    if (tid.getFirstElem() == null || tid.getSecondElem() == null) {
                        res.setParsingErrorReason("TCTCConversationMessage decoding error: a message does not contain both both origination and resination transactionId");
                        return res;
                    }
                    byte[] originatingTransactionId = tid.getFirstElem();
                    res.setOriginationDialogId(Utils.decodeTransactionId(originatingTransactionId, this.stack.getSwapTcapIdBytes()));

                    byte[] destinationTransactionId = tid.getSecondElem();
                    res.setDestinationDialogId(Utils.decodeTransactionId(destinationTransactionId, this.stack.getSwapTcapIdBytes()));

                    if (tag == TCConversationMessage._TAG_CONVERSATION_WITH_PERM)
                        res.setMessageType(MessageType.ConversationWithPerm);
                    else
                        res.setMessageType(MessageType.ConversationWithoutPerm);
                    break;

                case TCQueryMessage._TAG_QUERY_WITH_PERM:
                case TCQueryMessage._TAG_QUERY_WITHOUT_PERM:
                    localAis = ais.readSequenceStream();

                    // transaction portion
                    tid = TcapFactory.readTransactionID(localAis);
                    if (tid.getFirstElem() == null || tid.getSecondElem() != null) {
                        res.setParsingErrorReason("TCQueryMessage decoding error: transactionId must contain only one transactionId");
                        return res;
                    }
                    originatingTransactionId = tid.getFirstElem();
                    res.setOriginationDialogId(Utils.decodeTransactionId(originatingTransactionId, this.stack.getSwapTcapIdBytes()));

                    if (tag == TCQueryMessage._TAG_QUERY_WITH_PERM)
                        res.setMessageType(MessageType.QueryWithPerm);
                    else
                        res.setMessageType(MessageType.QueryWithoutPerm);
                    break;

                case TCResponseMessage._TAG_RESPONSE:
                    localAis = ais.readSequenceStream();

                    // transaction portion
                    tid = TcapFactory.readTransactionID(localAis);
                    if (tid.getFirstElem() == null || tid.getSecondElem() != null) {
                        res.setParsingErrorReason("TCResponseMessage decoding error: transactionId must contain only one transactionId");
                        return res;
                    }
                    destinationTransactionId = tid.getFirstElem();
                    res.setDestinationDialogId(Utils.decodeTransactionId(destinationTransactionId, this.stack.getSwapTcapIdBytes()));

                    res.setMessageType(MessageType.Response);
                    break;

                case TCAbortMessage._TAG_ABORT:
                    localAis = ais.readSequenceStream();

                    // transaction portion
                    tid = TcapFactory.readTransactionID(localAis);
                    if (tid.getFirstElem() == null || tid.getSecondElem() != null) {
                        res.setParsingErrorReason("TCAbortMessage decoding error: transactionId must contain only one transactionId");
                        return res;
                    }
                    destinationTransactionId = tid.getFirstElem();
                    res.setDestinationDialogId(Utils.decodeTransactionId(destinationTransactionId, this.stack.getSwapTcapIdBytes()));

                    res.setMessageType(MessageType.Abort);
                    break;

                case TCUniMessage._TAG_UNI:
                    res.setMessageType(MessageType.Unidirectional);
                    break;

                default:
                    res.setParsingErrorReason("Unrecognized message tag");
                    break;
            }

            ais.close();
            return res;
        }
        catch (Exception e) {
            DraftParsedMessageImpl res = new DraftParsedMessageImpl();
            res.setParsingErrorReason("Exception when message parsing: " + e.getMessage());
            return res;
        }
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
    public void onConnectIndication(SccpConnection conn, SccpAddress calledAddress, SccpAddress callingAddress, ProtocolClass clazz, Credit credit, byte[] data, Importance importance) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConnectConfirm(SccpConnection conn, byte[] data) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, ReleaseCause reason, byte[] data) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDisconnectIndication(SccpConnection conn, RefusalCause reason, byte[] data) {
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
    public void onData(SccpConnection conn, byte[] data) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDisconnectConfirm(SccpConnection conn) {
        // TODO Auto-generated method stub
    }
}
