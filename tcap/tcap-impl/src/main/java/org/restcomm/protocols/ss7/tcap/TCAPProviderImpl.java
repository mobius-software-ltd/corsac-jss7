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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.DraftParsedMessage;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogPortionImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogResponseAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.ResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnosticImpl;
import org.restcomm.protocols.ss7.tcap.asn.ResultType;
import org.restcomm.protocols.ss7.tcap.asn.TCAbortMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCBeginMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCContinueMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCEndMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCNoticeIndicationImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCUniMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCUnifiedMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.Utils;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.tc.component.ComponentPrimitiveFactoryImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.DraftParsedMessageImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCBeginIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCContinueIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCEndIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCPAbortIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCUniIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCUserAbortIndicationImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

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
    
    private AtomicInteger seqControl = new AtomicInteger(1);
    private int ssn;
    private AtomicLong curDialogId = new AtomicLong(0);

    private transient ConcurrentHashMap<String, Integer> lstUserPartCongestionLevel = new ConcurrentHashMap<String, Integer>();

    private ASNParser messageParser=new ASNParser(true);
    
    protected TCAPProviderImpl(SccpProvider sccpProvider, TCAPStackImpl stack, int ssn,ScheduledExecutorService service) {
        super();
        this.sccpProvider = sccpProvider;
        this.ssn = ssn;
        messageFactory = sccpProvider.getMessageFactory();
        this.stack = stack;

        this.componentPrimitiveFactory = new ComponentPrimitiveFactoryImpl(this);
        this.dialogPrimitiveFactory = new DialogPrimitiveFactoryImpl(this.componentPrimitiveFactory);
        this.service=service;
        
        messageParser.loadClass(TCAbortMessageImpl.class);
        messageParser.loadClass(TCBeginMessageImpl.class);
        messageParser.loadClass(TCEndMessageImpl.class);
        messageParser.loadClass(TCContinueMessageImpl.class);
        messageParser.loadClass(TCUniMessageImpl.class);
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
            DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this.service, this, seqControl,messageParser);

            this.dialogs.put(id, di);
            return di;
        } else {
            DialogImpl di = new DialogImpl(localAddress, remoteAddress, id, structured, this.service, this, seqControl,messageParser);
            
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

    public void send(ByteBuf data, boolean returnMessageOnError, SccpAddress destinationAddress, SccpAddress originatingAddress,
            int seqControl, int networkId, int localSsn, int remotePc) throws IOException {
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

        this.dialogs.remove(did);
        this.doRelease(d);
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
    }

    protected ByteBuf encodeAbortMessage(TCAbortMessageImpl msg) throws ASNException {
    	return messageParser.encode(msg);
    }
    
    protected void sendProviderAbort(PAbortCauseType pAbortCause, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl, int networkId, int remotePc) {
        
    	TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
        msg.setDestinationTransactionId(remoteTransactionId);
        msg.setPAbortCause(pAbortCause);

        try {
        	System.out.println(msg.toString());
        	ByteBuf buffer=messageParser.encode(msg);
            this.send(buffer, false, remoteAddress, localAddress, seqControl, networkId, localAddress.getSubsystemNumber(), remotePc);
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
        }
    }

    protected void sendProviderAbort(DialogServiceProviderType pt, byte[] remoteTransactionId, SccpAddress remoteAddress,
            SccpAddress localAddress, int seqControl, ApplicationContextNameImpl acn, int networkId, int remotePc) {
        
    	DialogPortionImpl dp = TcapFactory.createDialogPortion();
        dp.setUnidirectional(false);

        DialogResponseAPDUImpl apdu = TcapFactory.createDialogAPDUResponse();
        apdu.setDoNotSendProtocolVersion(this.getStack().getDoNotSendProtocolVersion());

        ResultImpl res = TcapFactory.createResult();
        res.setResultType(ResultType.RejectedPermanent);
        ResultSourceDiagnosticImpl rsd = TcapFactory.createResultSourceDiagnostic();
        rsd.setDialogServiceProviderType(pt);
        apdu.setResultSourceDiagnostic(rsd);
        apdu.setResult(res);
        apdu.setApplicationContextName(acn);
        dp.setDialogAPDU(apdu);

        TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
        msg.setDestinationTransactionId(remoteTransactionId);
        msg.setDialogPortion(dp);

        try {
        	ByteBuf buffer=messageParser.encode(msg);
            this.send(buffer, false, remoteAddress, localAddress, seqControl, networkId, localAddress.getSubsystemNumber(), remotePc);
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
        }
    }
    
    public void onMessage(SccpDataMessage message) {

        try {
        	ByteBuf data = message.getData();
            SccpAddress localAddress = message.getCalledPartyAddress();
            SccpAddress remoteAddress = message.getCallingPartyAddress();

            ASNDecodeResult output=null;
            try {
            	output=messageParser.decode(Unpooled.wrappedBuffer(data));
            }
            catch(ASNException ex) {
                logger.error("ParseException when parsing TCMessage: " + ex.toString(), ex);
                this.sendProviderAbort(PAbortCauseType.BadlyFormattedTxPortion,new byte[0], remoteAddress, localAddress,message.getSls(), message.getNetworkId(), message.getIncomingOpc());                
                return;           	
            }
            
            if(output.getResult() instanceof TCUnifiedMessageImpl) {
            	TCUnifiedMessageImpl realMessage=(TCUnifiedMessageImpl)output.getResult();
            	if(!output.getHadErrors() && realMessage.validate()) {
	            	if(realMessage instanceof TCContinueMessageImpl) {
	            		TCContinueMessageImpl tcm=(TCContinueMessageImpl)realMessage;
	            		long dialogId = Utils.decodeTransactionId(tcm.getDestinationTransactionId(), this.stack.getSwapTcapIdBytes());
	                    DialogImpl di = this.dialogs.get(dialogId);
	                    
	                    if (di == null) {
	                        logger.warn("TC-CONTINUE: No dialog/transaction for id: " + dialogId);
	                        this.sendProviderAbort(PAbortCauseType.UnrecognizedTxID, tcm.getOriginatingTransactionId(),
	                                remoteAddress, localAddress, message.getSls(), message.getNetworkId(), message.getIncomingOpc());
	                    } else {
	                        di.processContinue(tcm, localAddress, remoteAddress);
	                    }
	            	} else if(realMessage instanceof TCBeginMessageImpl) {
	            		TCBeginMessageImpl tcb=(TCBeginMessageImpl)realMessage;
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
	
	                    DialogImpl di = null;
	                    try {
	                    	int remotePc = message.getIncomingOpc();
                            di = (DialogImpl) this.getNewDialog(localAddress, remoteAddress, message.getSls(), null);
                            di.setRemotePc(remotePc);
                            setSsnToDialog(di, message.getCalledPartyAddress().getSubsystemNumber());
	                    } catch (TCAPException e) {
	                        this.sendProviderAbort(PAbortCauseType.ResourceLimitation, tcb.getOriginatingTransactionId(),
	                                remoteAddress, localAddress, message.getSls(), message.getNetworkId(), message.getIncomingOpc());
	                        logger.error("Can not add a new dialog when receiving TCBeginMessage: " + e.getMessage(), e);
	                        return;
	                    }
	
	                    di.setNetworkId(message.getNetworkId());
	                    di.processBegin(tcb, localAddress, remoteAddress);
	            	}
	            	else if(realMessage instanceof TCEndMessageImpl) {
	            		TCEndMessageImpl teb=(TCEndMessageImpl)realMessage;
	            		long dialogId = Utils.decodeTransactionId(teb.getDestinationTransactionId(), this.stack.getSwapTcapIdBytes());
	                    DialogImpl di = this.dialogs.get(dialogId);
	                    if (di == null) {
	                        logger.warn("TC-END: No dialog/transaction for id: " + dialogId);
	                    } else {
	                        di.processEnd(teb, localAddress, remoteAddress);
	                    }		
	            	}
	            	else if(realMessage instanceof TCAbortMessageImpl) {
	            		TCAbortMessageImpl tub=(TCAbortMessageImpl)realMessage;
	            		Long dialogId = Utils.decodeTransactionId(tub.getDestinationTransactionId(), this.stack.getSwapTcapIdBytes());
	            		DialogImpl di = this.dialogs.get(dialogId);
	                    
	                    if (di == null) {
	                        logger.warn("TC-ABORT: No dialog/transaction for id: " + dialogId);
	                    } else {
	                        di.processAbort(tub, localAddress, remoteAddress);
	                    }		
	            	}
	            	else if(realMessage instanceof TCUniMessageImpl) {
	            		TCUniMessageImpl tcuni=(TCUniMessageImpl)realMessage;
	            		int remotePc = message.getIncomingOpc();
	                    DialogImpl uniDialog = (DialogImpl) this.getNewUnstructuredDialog(localAddress, remoteAddress);
	                    uniDialog.setRemotePc(remotePc);
	                    setSsnToDialog(uniDialog, message.getCalledPartyAddress().getSubsystemNumber());
	                    uniDialog.processUni(tcuni, localAddress, remoteAddress);	
	            	} else {
	            		unrecognizedPackageType(message, realMessage.getOriginatingTransactionId(), localAddress, remoteAddress, message.getNetworkId());                    
	            	}
            	}
            	else
            		unrecognizedPackageType(message,  realMessage.getOriginatingTransactionId(), localAddress, remoteAddress, message.getNetworkId());
            }
            else {
            	unrecognizedPackageType(message, null, localAddress, remoteAddress, message.getNetworkId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("Error while decoding Rx SccpMessage=%s", message), e);
        }
    }

    private void unrecognizedPackageType(SccpDataMessage message,byte[] transactionID, SccpAddress localAddress, SccpAddress remoteAddress,int networkId) throws ParseException {
        byte[] realTransactionID=transactionID;
        if(realTransactionID==null)
        	realTransactionID=new byte[0];
        
        logger.error(String.format("Rx unidentified.SccpMessage=%s", message));
        this.sendProviderAbort(PAbortCauseType.UnrecognizedMessageType, realTransactionID, remoteAddress, localAddress, message.getSls(), networkId, message.getIncomingOpc());        
    }

    public void onNotice(SccpNoticeMessage msg) {

        DialogImpl dialog = null;

        try {
            ByteBuf data = msg.getData();
            ASNDecodeResult output=messageParser.decode(Unpooled.wrappedBuffer(data));
            
            if(output.getHadErrors()) {
            	logger.error(String.format("Error while decoding Rx SccpNoticeMessage=%s", msg));
            }
            else if(output.getResult() instanceof TCUnifiedMessageImpl) {
	            TCUnifiedMessageImpl tcUnidentified = (TCUnifiedMessageImpl)output.getResult();
	            if (tcUnidentified.getOriginatingTransactionId() != null) {
	                long otid = Utils.decodeTransactionId(tcUnidentified.getOriginatingTransactionId(), this.stack.getSwapTcapIdBytes());
	                dialog = this.dialogs.get(otid);
	            }
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

    @Override
    public DraftParsedMessage parseMessageDraft(ByteBuf data) {
        try {
            DraftParsedMessageImpl res = new DraftParsedMessageImpl();
            ASNDecodeResult output=messageParser.decode(data);
            if (!(output.getResult() instanceof TCUnifiedMessageImpl)) {
                res.setParsingErrorReason("Invalid message found");
                return res;
            }

            res.setMessage((TCUnifiedMessageImpl)output.getResult());
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
    public void onConnectIndication(SccpConnection conn, SccpAddress calledAddress, SccpAddress callingAddress, ProtocolClass clazz, Credit credit, ByteBuf data, Importance importance) throws Exception {
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
}
