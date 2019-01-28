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

package org.restcomm.protocols.ss7.tcap;

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
import org.restcomm.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcap.api.DialogPrimitiveFactory;
import org.restcomm.protocols.ss7.tcap.api.MessageType;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCListener;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.DraftParsedMessage;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogResponseAPDU;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.restcomm.protocols.ss7.tcap.asn.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.Result;
import org.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.restcomm.protocols.ss7.tcap.asn.ResultType;
import org.restcomm.protocols.ss7.tcap.asn.TCAbortMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCNoticeIndicationImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCUnidentifiedMessage;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.Utils;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcap.tc.component.ComponentPrimitiveFactoryImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.DraftParsedMessageImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCBeginIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCContinueIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCEndIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCPAbortIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCUniIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCUserAbortIndicationImpl;

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
    
    private transient ComponentPrimitiveFactory componentPrimitiveFactory;
    private transient DialogPrimitiveFactory dialogPrimitiveFactory;
    private transient SccpProvider sccpProvider;

    private transient MessageFactory messageFactory;
    
    private transient TCAPStackImpl stack; // originating TX id ~=Dialog, its direct

    private transient ConcurrentHashMap<Long, DialogImpl> dialogs = new ConcurrentHashMap <Long, DialogImpl>();
    
//    protected transient FastMap<PrevewDialogDataKey, PrevewDialogData> dialogPreviewList = new FastMap<PrevewDialogDataKey, PrevewDialogData>();
    protected transient ConcurrentHashMap<PreviewDialogDataKey, PreviewDialogData> dialogPreviewList = new ConcurrentHashMap<PreviewDialogDataKey, PreviewDialogData>();
    
    private AtomicInteger seqControl = new AtomicInteger(1);
    private int ssn;
    private AtomicLong curDialogId = new AtomicLong(0);

    private transient ConcurrentHashMap<String, Integer> lstUserPartCongestionLevel = new ConcurrentHashMap<String, Integer>();

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

    // get next Seq Control value available

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
            int seqControl, int networkId, int localSsn, int remotePc) throws IOException {
        if (this.stack.getPreviewMode())
            return;

        SccpDataMessage msg = messageFactory.createDataMessageClass1(destinationAddress, originatingAddress, data, seqControl,
                localSsn, returnMessageOnError, null, null);
        msg.setNetworkId(networkId);
        msg.setOutgoingDpc(remotePc);
        sccpProvider.send(msg);
    }

    public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId) {
        return this.sccpProvider.getMaxUserDataLength(calledPartyAddress, callingPartyAddress, msgNetworkId);
    }

    public void deliver(DialogImpl dialogImpl, TCBeginIndicationImpl msg) {
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCBegin(msg);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCContinueIndicationImpl tcContinueIndication) {
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCContinue(tcContinueIndication);
            }
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received exception while delivering data to transport layer.", e);
            }
        }

    }

    public void deliver(DialogImpl dialogImpl, TCEndIndicationImpl tcEndIndication) {
        try {
            for (TCListener lst : this.tcListeners) {
                lst.onTCEnd(tcEndIndication);
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

    @Override
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
	        if (extraSsns != null) {
	            while(extraSsns.hasNext()) {
	            	int extraSsn = extraSsns.next();
                    this.sccpProvider.registerSccpListener(extraSsn, this);
                    logger.info("Registered SCCP listener with extra ssn " + extraSsn);
	            }
	        }
        }
        
        // congestion caring
        lstUserPartCongestionLevel.clear();
    }

    void stop() {
        this.sccpProvider.deregisterSccpListener(ssn);

        if(this.stack.getExtraSsns()!=null) {
	        Iterator<Integer> extraSsns = this.stack.getExtraSsns().iterator();
	        while (extraSsns.hasNext()) {
	        	int extraSsn = extraSsns.next();
                this.sccpProvider.deregisterSccpListener(extraSsn);
            }
        }
        
        this.dialogs.clear();
        this.dialogPreviewList.clear();
    }

    protected void sendProviderAbort(PAbortCauseType pAbortCause, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl, int networkId, int remotePc) {
        if (this.stack.getPreviewMode())
            return;

        TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
        msg.setDestinationTransactionId(remoteTransactionId);
        msg.setPAbortCause(pAbortCause);

        AsnOutputStream aos = new AsnOutputStream();
        try {
            msg.encode(aos);
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl, networkId, localAddress.getSubsystemNumber(), remotePc);
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
        }
    }

    protected void sendProviderAbort(DialogServiceProviderType pt, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl, ApplicationContextName acn, int networkId, int remotePc) {
        if (this.stack.getPreviewMode())
            return;

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

        TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
        msg.setDestinationTransactionId(remoteTransactionId);
        msg.setDialogPortion(dp);

        AsnOutputStream aos = new AsnOutputStream();
        try {
            msg.encode(aos);
            this.send(aos.toByteArray(), false, remoteAddress, localAddress, seqControl, networkId, localAddress.getSubsystemNumber(), remotePc);
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

            // FIXME: Qs state that OtxID and DtxID consittute to dialog id.....

            // asnData - it should pass
            AsnInputStream ais = new AsnInputStream(data);

            // this should have TC message tag :)
            int tag = ais.readTag();

            if (ais.getTagClass() != Tag.CLASS_APPLICATION) {
                unrecognizedPackageType(message, localAddress, remoteAddress, ais, tag, message.getNetworkId());
                return;
            }

            switch (tag) {
            // continue first, usually we will get more of those. small perf
            // boost
                case TCContinueMessage._TAG:
                    TCContinueMessage tcm = null;
                    try {
                        tcm = TcapFactory.createTCContinueMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCContinueMessage: " + e.toString(), e);

                        // parsing OriginatingTransactionId
                        ais = new AsnInputStream(data);
                        tag = ais.readTag();
                        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
                        tcUnidentified.decode(ais);
                        if (tcUnidentified.getOriginatingTransactionId() != null) {
                            if (e.getPAbortCauseType() != null) {
                                this.sendProviderAbort(e.getPAbortCauseType(), tcUnidentified.getOriginatingTransactionId(),
                                        remoteAddress, localAddress, message.getSls(), message.getNetworkId(), message.getIncomingOpc());
                            } else {
                                this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion,
                                        tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                        message.getSls(), message.getNetworkId(), message.getIncomingOpc());
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
                        logger.warn("TC-CONTINUE: No dialog/transaction for id: " + dialogId);
                        this.sendProviderAbort(PAbortCauseType.UnrecognizedTxID, tcm.getOriginatingTransactionId(),
                                remoteAddress, localAddress, message.getSls(), message.getNetworkId(), message.getIncomingOpc());
                    } else {
                        di.processContinue(tcm, localAddress, remoteAddress);
                    }

                    break;

                case TCBeginMessage._TAG:
                    TCBeginMessage tcb = null;
                    try {
                        tcb = TcapFactory.createTCBeginMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCBeginMessage: " + e.toString(), e);

                        // parsing OriginatingTransactionId
                        ais = new AsnInputStream(data);
                        tag = ais.readTag();
                        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
                        tcUnidentified.decode(ais);
                        if (tcUnidentified.getOriginatingTransactionId() != null) {
                            if (e.getPAbortCauseType() != null) {
                                this.sendProviderAbort(e.getPAbortCauseType(), tcUnidentified.getOriginatingTransactionId(),
                                        remoteAddress, localAddress, message.getSls(), message.getNetworkId(), message.getIncomingOpc());
                            } else {
                                this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion,
                                        tcUnidentified.getOriginatingTransactionId(), remoteAddress, localAddress,
                                        message.getSls(), message.getNetworkId(), message.getIncomingOpc());
                            }
                        }
                        return;
                    }
                    if (tcb.getDialogPortion() != null && tcb.getDialogPortion().getDialogAPDU() != null
                            && tcb.getDialogPortion().getDialogAPDU() instanceof DialogRequestAPDUImpl) {
                        DialogRequestAPDUImpl dlg = (DialogRequestAPDUImpl) tcb.getDialogPortion().getDialogAPDU();
                        if (dlg.getProtocolVersion() != null && !dlg.getProtocolVersion().isSupportedVersion()) {
                            logger.error("Unsupported protocol version of  has been received when parsing TCBeginMessage");
                            this.sendProviderAbort(DialogServiceProviderType.NoCommonDialogPortion,
                                    tcb.getOriginatingTransactionId(), remoteAddress, localAddress, message.getSls(),
                                    dlg.getApplicationContextName(), message.getNetworkId(), message.getIncomingOpc());
                            return;
                        }
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
                            int remotePc = message.getIncomingOpc();
                            di = (DialogImpl) this.getNewDialog(localAddress, remoteAddress, message.getSls(), null);
                            di.setRemotePc(remotePc);
                            setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
                        }

                    } catch (TCAPException e) {
                        this.sendProviderAbort(PAbortCauseType.ResourceLimitation, tcb.getOriginatingTransactionId(),
                                remoteAddress, localAddress, message.getSls(), message.getNetworkId(), message.getIncomingOpc());
                        logger.error("Can not add a new dialog when receiving TCBeginMessage: " + e.getMessage(), e);
                        return;
                    }

                    di.setNetworkId(message.getNetworkId());
                    di.processBegin(tcb, localAddress, remoteAddress);

                    if (this.stack.getPreviewMode()) {
                        di.getPrevewDialogData().setLastACN(di.getApplicationContextName());
                        di.getPrevewDialogData().setOperationsSentB(di.operationsSent);
                        di.getPrevewDialogData().setOperationsSentA(di.operationsSentA);
                    }

                    break;

                case TCEndMessage._TAG:
                    TCEndMessage teb = null;
                    try {
                        teb = TcapFactory.createTCEndMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCEndMessage: " + e.toString(), e);
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
                        logger.warn("TC-END: No dialog/transaction for id: " + dialogId);
                    } else {
                        di.processEnd(teb, localAddress, remoteAddress);

                        if (this.stack.getPreviewMode()) {
                            this.removePreviewDialog(di);
                        }
                    }
                    break;

                case TCAbortMessage._TAG:
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

                case TCUniMessage._TAG:
                    TCUniMessage tcuni;
                    try {
                        tcuni = TcapFactory.createTCUniMessage(ais);
                    } catch (ParseException e) {
                        logger.error("ParseException when parsing TCUniMessage: " + e.toString(), e);
                        return;
                    }

                    int remotePc = message.getIncomingOpc();
                    DialogImpl uniDialog = (DialogImpl) this.getNewUnstructuredDialog(localAddress, remoteAddress);
                    uniDialog.setRemotePc(remotePc);
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
            int networkId) throws ParseException {
        if (this.stack.getPreviewMode()) {
            return;
        }

        logger.error(String.format("Rx unidentified tag=%s, tagClass=. SccpMessage=%s", tag, ais.getTagClass(), message));
        TCUnidentifiedMessage tcUnidentified = new TCUnidentifiedMessage();
        tcUnidentified.decode(ais);

        if (tcUnidentified.getOriginatingTransactionId() != null) {
            byte[] otid = tcUnidentified.getOriginatingTransactionId();
            this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, otid, remoteAddress, localAddress, message.getSls(), networkId, message.getIncomingOpc());            
        } else {
            this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, new byte[0], remoteAddress, localAddress, message.getSls(), networkId, message.getIncomingOpc());
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

    protected  Dialog createPreviewDialog(PreviewDialogDataKey ky, SccpAddress localAddress, SccpAddress remoteAddress,
            int seqControl) throws TCAPException {
        Long dialogId = this.getAvailableTxIdPreview();
        PreviewDialogData pdd = new PreviewDialogData(this, dialogId);
        pdd.setPrevewDialogDataKey1(ky);
        PreviewDialogData pddx = this.dialogPreviewList.putIfAbsent(ky, pdd);
        if (pddx != null) {
            this.removePreviewDialog(pddx);
            throw new TCAPException("Dialog with trId=" + ky.origTxId
                    + " is already exists - we ignore it and drops curent dialog");
        }

        DialogImpl di = new DialogImpl(localAddress, remoteAddress, seqControl, this.service, this, pdd, false);

        pdd.startIdleTimer();

        return di;
    }

    protected Long getAvailableTxIdPreview() throws TCAPException {
        while (true) {
            // Long id;
            // if (!currentDialogId.compareAndSet(this.stack.getDialogIdRangeEnd(), this.stack.getDialogIdRangeStart() + 1)) {
            // id = currentDialogId.getAndIncrement();
            // } else {
            // id = this.stack.getDialogIdRangeStart();
            // }
            // return id;

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
    public DraftParsedMessage parseMessageDraft(byte[] data) {
        try {
            DraftParsedMessageImpl res = new DraftParsedMessageImpl();

            AsnInputStream ais = new AsnInputStream(data);

            int tag = ais.readTag();

            if (ais.getTagClass() != Tag.CLASS_APPLICATION) {
                res.setParsingErrorReason("Message tag class must be CLASS_APPLICATION");
                ais.close();
                return res;
            }

            switch (tag) {
                case TCContinueMessage._TAG:
                    AsnInputStream localAis = ais.readSequenceStream();

                    tag = localAis.readTag();
                    if (tag != TCContinueMessage._TAG_OTX || localAis.getTagClass() != Tag.CLASS_APPLICATION) {
                        res.setParsingErrorReason("originatingTransactionId tag/tagClass is bad for TC-CONTINUE message");
                        return res;
                    }
                    byte[] originatingTransactionId = localAis.readOctetString();
                    res.setOriginationDialogId(Utils.decodeTransactionId(originatingTransactionId, this.stack.getSwapTcapIdBytes()));

                    tag = localAis.readTag();
                    if (tag != TCContinueMessage._TAG_DTX || localAis.getTagClass() != Tag.CLASS_APPLICATION) {
                        res.setParsingErrorReason("destinationTransactionId tag/tagClass is bad for TC-CONTINUE message");
                        return res;
                    }
                    byte[] destinationTransactionId = localAis.readOctetString();
                    res.setDestinationDialogId(Utils.decodeTransactionId(destinationTransactionId, this.stack.getSwapTcapIdBytes()));

                    res.setMessageType(MessageType.Continue);
                    break;

                case TCBeginMessage._TAG:
                    localAis = ais.readSequenceStream();

                    tag = localAis.readTag();
                    if (tag != TCBeginMessage._TAG_OTX || localAis.getTagClass() != Tag.CLASS_APPLICATION) {
                        res.setParsingErrorReason("originatingTransactionId tag/tagClass is bad for TC-BEGIN message");
                        return res;
                    }
                    originatingTransactionId = localAis.readOctetString();
                    res.setOriginationDialogId(Utils.decodeTransactionId(originatingTransactionId, this.stack.getSwapTcapIdBytes()));

                    res.setMessageType(MessageType.Begin);
                    break;

                case TCEndMessage._TAG:
                    localAis = ais.readSequenceStream();

                    tag = localAis.readTag();
                    if (tag != TCEndMessage._TAG_DTX || localAis.getTagClass() != Tag.CLASS_APPLICATION) {
                        res.setParsingErrorReason("destinationTransactionId tag/tagClass is bad for TC-END message");
                        return res;
                    }
                    destinationTransactionId = localAis.readOctetString();
                    res.setDestinationDialogId(Utils.decodeTransactionId(destinationTransactionId, this.stack.getSwapTcapIdBytes()));

                    res.setMessageType(MessageType.End);
                    break;

                case TCAbortMessage._TAG:
                    localAis = ais.readSequenceStream();

                    tag = localAis.readTag();
                    if (tag != TCAbortMessage._TAG_DTX || localAis.getTagClass() != Tag.CLASS_APPLICATION) {
                        res.setParsingErrorReason("destinationTransactionId tag/tagClass is bad for TC-ABORT message");
                        return res;
                    }
                    destinationTransactionId = localAis.readOctetString();
                    res.setDestinationDialogId(Utils.decodeTransactionId(destinationTransactionId, this.stack.getSwapTcapIdBytes()));

                    res.setMessageType(MessageType.Abort);
                    break;

                case TCUniMessage._TAG:
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
