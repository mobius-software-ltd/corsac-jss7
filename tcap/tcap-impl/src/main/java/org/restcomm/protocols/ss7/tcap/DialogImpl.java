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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.component.OperationState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.ASNAbortSource;
import org.restcomm.protocols.ss7.tcap.asn.AbortSourceType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextNameImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogAPDU;
import org.restcomm.protocols.ss7.tcap.asn.DialogAPDUType;
import org.restcomm.protocols.ss7.tcap.asn.DialogAbortAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogPortionImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogResponseAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.ResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnosticImpl;
import org.restcomm.protocols.ss7.tcap.asn.ResultType;
import org.restcomm.protocols.ss7.tcap.asn.TCAbortMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCBeginMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCContinueMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCEndMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TCUniMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.UserInformationImpl;
import org.restcomm.protocols.ss7.tcap.asn.Utils;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentPortionImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ProblemImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCBeginIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCContinueIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCEndIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCPAbortIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCUniIndicationImpl;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.TCUserAbortIndicationImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class DialogImpl implements Dialog {
	private static final long serialVersionUID = 1L;

	// timeout of remove task after TC_END
    //private static final int _REMOVE_TIMEOUT = 30000;

    private static final Logger logger = Logger.getLogger(DialogImpl.class);

    private Object userObject;

    // values for timer timeouts
    //private long removeTaskTimeout = _REMOVE_TIMEOUT;
    private long idleTaskTimeout;

    // sent/received acn, holds last acn/ui.
    private ApplicationContextNameImpl lastACN;
    private UserInformationImpl lastUI; // optional

    private Long localTransactionIdObject;
    private long localTransactionId;
    private byte[] remoteTransactionId;
    private Long remoteTransactionIdObject;

    private SccpAddress localAddress;
    private SccpAddress remoteAddress;
    private int localSsn;
    private int remotePc = -1;

    private AtomicReference<Future<?>> idleTimerFuture=new AtomicReference<Future<?>>();
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
    protected InvokeImpl[] operationsSent = new InvokeImpl[invokeIDTable.length];
    protected InvokeImpl[] operationsSentA = new InvokeImpl[invokeIDTable.length];
    private ConcurrentHashMap<Long,Long> incomingInvokeList = new ConcurrentHashMap<Long,Long>();
    private ScheduledExecutorService executor;

    // scheduled components list
    private List<ComponentImpl> scheduledComponentList = new ArrayList<ComponentImpl>();
    private TCAPProviderImpl provider;

    private int seqControl;

    // If the Dialogue Portion is sent in TCBegin message, the first received
    // Continue message should have the Dialogue Portion too
    private boolean dpSentInBegin = false;

    private long startDialogTime;
    private int networkId;

    private Boolean doNotSendProtocolVersion = null;

    private boolean isSwapTcapIdBytes;

    private ASNParser dialogParser;
    
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
     * @param executor
     * @param provider
     * @param seqControl
     */
    protected DialogImpl(SccpAddress localAddress, SccpAddress remoteAddress, Long origTransactionId, boolean structured,
            ScheduledExecutorService executor, TCAPProviderImpl provider, int seqControl,ASNParser dialogParser) {
        super();
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
        if (origTransactionId != null) {
            this.localTransactionIdObject = origTransactionId;
            this.localTransactionId = origTransactionId;
        }
        this.executor = executor;
        this.provider = provider;
        this.structured = structured;

        this.seqControl = seqControl;
        
        TCAPStack stack = this.provider.getStack();
        this.idleTaskTimeout = stack.getDialogIdleTimeout();
        this.isSwapTcapIdBytes = stack.getSwapTcapIdBytes();

        startDialogTime = System.currentTimeMillis();

        this.dialogParser=dialogParser;
        // start
        startIdleTimer();
    }

    @Override
    public Boolean isDoNotSendProtcolVersion() {
        return doNotSendProtocolVersion;
    }

    @Override
    public void setDoNotSendProtocolVersion(Boolean doNotSendProtocolVersion) {
        this.doNotSendProtocolVersion = doNotSendProtocolVersion;
    }

    /**
     * Compute convergent option value as combination from dialog level value
     * and global value specified at stack level.
     *
     * @return
     */
    private boolean doNotSendProtocolVersion() {
        return doNotSendProtocolVersion != null ?
                doNotSendProtocolVersion  :
                provider.getStack().getDoNotSendProtocolVersion();
    }

    public void release() {
    	for (int i = 0; i < this.operationsSent.length; i++) {
            InvokeImpl invokeImpl = this.operationsSent[i];
            if (invokeImpl != null) {
                invokeImpl.setState(OperationState.Idle);
                // TODO whether to call operationTimedOut or not is still not clear
                // operationTimedOut(invokeImpl);
            }
        }
    	
        this.setState(TRPseudoState.Expunged);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#getDialogId()
     */
    public Long getLocalDialogId() {

        return localTransactionIdObject;
    }

    /**
     *
     */
    public Long getRemoteDialogId() {
        if (this.remoteTransactionId != null && this.remoteTransactionIdObject == null) {
            this.remoteTransactionIdObject = Utils.decodeTransactionId(this.remoteTransactionId, isSwapTcapIdBytes);
        }

        return this.remoteTransactionIdObject;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#getNewInvokeId()
     */
    public Long getNewInvokeId() throws TCAPException {
    	if (this.freeCount == 0) {
            throw new TCAPException("No free invokeId");
        }

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

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#cancelInvocation (java.lang.Long)
     */
    public boolean cancelInvocation(Long invokeId) throws TCAPException {
        int index = getIndexFromInvokeId(invokeId);
        if (index < 0 || index >= this.operationsSent.length) {
            throw new TCAPException("Wrong invoke id passed.");
        }

        // lookup through send buffer.
        for (index = 0; index < this.scheduledComponentList.size(); index++) {
        	ComponentImpl cr = this.scheduledComponentList.get(index);
            if (cr.getType() == ComponentType.Invoke && cr.getInvoke().getInvokeId().equals(invokeId)) {
                // lucky
                // TCInvokeRequestImpl invoke = (TCInvokeRequestImpl) cr;
                // there is no notification on cancel?
                this.scheduledComponentList.remove(index);
                cr.getInvoke().stopTimer();
                cr.getInvoke().setState(OperationState.Idle);
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
    public SccpAddress getRemoteAddress() {

        return this.remoteAddress;
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
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#getLocalAddress()
     */
    public SccpAddress getLocalAddress() {

        return this.localAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#isEstabilished()
     */
    public boolean isEstabilished() {    	
        return (this.state.get()==TRPseudoState.Active);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#isStructured()
     */
    public boolean isStructured() {

        return this.structured;
    }

    public void keepAlive() {
        if (this.idleTimerInvoked.get()) {
            this.idleTimerActionTaken.set(true);
        }
    }

    /**
     * @return the acn
     */
    public ApplicationContextNameImpl getApplicationContextName() {
        return lastACN;
    }

    /**
     * @return the ui
     */
    public UserInformationImpl getUserInformation() {
        return lastUI;
    }

    /**
     * Adding the new incoming invokeId into incomingInvokeList list
     *
     * @param invokeId
     * @return false: failure - this invokeId already present in the list
     */
    private boolean addIncomingInvokeId(Long invokeId) {
    	Long oldValue=this.incomingInvokeList.putIfAbsent(invokeId, invokeId);
    	return oldValue==null;
    }

    private void removeIncomingInvokeId(Long invokeId) {
    	if(invokeId!=null)
    		this.incomingInvokeList.remove(invokeId);        
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
     * .protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest)
     */
    public void send(TCBeginRequest event) throws TCAPSendException {
        if (this.state.get() != TRPseudoState.Idle) {
            throw new TCAPSendException("Can not send Begin in this state: " + this.state);
        }

        if (!this.isStructured()) {
            throw new TCAPSendException("Unstructured dialogs do not use Begin");
        }
        
        this.idleTimerActionTaken.set(true);
        restartIdleTimer();
        TCBeginMessageImpl tcbm = (TCBeginMessageImpl) TcapFactory.createTCBeginMessage();

        // build DP

        if (event.getApplicationContextName() != null) {
            this.dpSentInBegin = true;
            DialogPortionImpl dp = TcapFactory.createDialogPortion();
            dp.setUnidirectional(false);
            DialogRequestAPDUImpl apdu = TcapFactory.createDialogAPDURequest();
            apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
            dp.setDialogAPDU(apdu);
            apdu.setApplicationContextName(event.getApplicationContextName());
            this.lastACN = event.getApplicationContextName();
            if (event.getUserInformation() != null) {
                apdu.setUserInformation(event.getUserInformation());
                this.lastUI = event.getUserInformation();
            }
            tcbm.setDialogPortion(dp);
        }

        // now comps
        tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionId, isSwapTcapIdBytes));
        if (this.scheduledComponentList.size() > 0) {
        	ArrayList<ComponentImpl> componentsToSend = new ArrayList<ComponentImpl>(this.scheduledComponentList.size());
        	this.prepareComponents(componentsToSend);
            
            ComponentPortionImpl cPortion=new ComponentPortionImpl();
            cPortion.setComponents(componentsToSend);
            tcbm.setComponent(cPortion);
        }

        try {
        	ByteBuf buffer=dialogParser.encode(tcbm);
            this.setState(TRPseudoState.InitialSent);                
            this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
                    this.seqControl, this.networkId, this.localSsn, this.remotePc);
            this.scheduledComponentList.clear();
        } catch (Throwable e) {
            // FIXME: remove freshly added invokes to free invoke ID??
            // TODO: should we release this dialog because TC-BEGIN sending has been failed
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
            throw new TCAPSendException("Failed to send TC-Begin message: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
     * .protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest)
     */
    public void send(TCContinueRequest event) throws TCAPSendException {
        if (!this.isStructured()) {
            throw new TCAPSendException("Unstructured dialogs do not use Continue");
        }
        
        if (this.state.get() == TRPseudoState.InitialReceived) {
            this.idleTimerActionTaken.set(true);
            restartIdleTimer();
            TCContinueMessageImpl tcbm = (TCContinueMessageImpl) TcapFactory.createTCContinueMessage();

            if (event.getApplicationContextName() != null) {

                // set dialog portion
                DialogPortionImpl dp = TcapFactory.createDialogPortion();
                dp.setUnidirectional(false);
                DialogResponseAPDUImpl apdu = TcapFactory.createDialogAPDUResponse();
                apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
                dp.setDialogAPDU(apdu);
                apdu.setApplicationContextName(event.getApplicationContextName());
                if (event.getUserInformation() != null) {
                    apdu.setUserInformation(event.getUserInformation());
                }
                // WHERE THE HELL THIS COMES FROM!!!!
                // WHEN REJECTED IS USED !!!!!
                ResultImpl res = TcapFactory.createResult();
                res.setResultType(ResultType.Accepted);
                ResultSourceDiagnosticImpl rsd = TcapFactory.createResultSourceDiagnostic();
                rsd.setDialogServiceUserType(DialogServiceUserType.Null);
                apdu.setResultSourceDiagnostic(rsd);
                apdu.setResult(res);
                tcbm.setDialogPortion(dp);

            }

            tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionId, isSwapTcapIdBytes));
            tcbm.setDestinationTransactionId(this.remoteTransactionId);
            if (this.scheduledComponentList.size() > 0) {
                List<ComponentImpl> componentsToSend = new ArrayList<ComponentImpl>(this.scheduledComponentList.size());
                this.prepareComponents(componentsToSend);
                ComponentPortionImpl cPortion=new ComponentPortionImpl();
                cPortion.setComponents(componentsToSend);
                tcbm.setComponent(cPortion);
            }
            
            // local address may change, lets check it;
            if (event.getOriginatingAddress() != null && !event.getOriginatingAddress().equals(this.localAddress)) {
                this.localAddress = event.getOriginatingAddress();
            }
            try {
            	ByteBuf buffer=dialogParser.encode(tcbm);                
                this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress,
                        this.localAddress, this.seqControl, this.networkId, this.localSsn, this.remotePc);
                this.setState(TRPseudoState.Active);
                this.scheduledComponentList.clear();
            } catch (Exception e) {
                // FIXME: remove freshly added invokes to free invoke ID??
                if (logger.isEnabledFor(Level.ERROR)) {
                    logger.error("Failed to send message: ", e);
                }
                throw new TCAPSendException("Failed to send TC-Continue message: " + e.getMessage(), e);
            }

        } else if (state.get() == TRPseudoState.Active) {
            this.idleTimerActionTaken.set(true);
            restartIdleTimer();
            // in this we ignore acn and passed args(except qos)
            TCContinueMessageImpl tcbm = (TCContinueMessageImpl) TcapFactory.createTCContinueMessage();

            tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionId, isSwapTcapIdBytes));
            tcbm.setDestinationTransactionId(this.remoteTransactionId);
            if (this.scheduledComponentList.size() > 0) {
                List<ComponentImpl> componentsToSend = new ArrayList<ComponentImpl>(this.scheduledComponentList.size());
                this.prepareComponents(componentsToSend);
                ComponentPortionImpl cPortion=new ComponentPortionImpl();
                cPortion.setComponents(componentsToSend);
                tcbm.setComponent(cPortion);

            }

            try {
            	ByteBuf buffer=dialogParser.encode(tcbm);                
                this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress,
                        this.localAddress, this.seqControl, this.networkId, this.localSsn, this.remotePc);
                this.scheduledComponentList.clear();
            } catch (Exception e) {
                // FIXME: remove freshly added invokes to free invoke ID??
                if (logger.isEnabledFor(Level.ERROR)) {
                    logger.error("Failed to send message: ", e);
                }
                throw new TCAPSendException("Failed to send TC-Continue message: " + e.getMessage(), e);
            }
        } else {
            throw new TCAPSendException("Wrong state: " + this.state.get());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
     * .protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest)
     */
    public void send(TCEndRequest event) throws TCAPSendException {
       if (!this.isStructured()) {
            throw new TCAPSendException("Unstructured dialogs do not use End");
        }

        TCEndMessageImpl tcbm = null;

        if (state.get() == TRPseudoState.InitialReceived) {
            // TC-END request primitive issued in response to a TC-BEGIN
            // indication primitive
            this.idleTimerActionTaken.set(true);
            stopIdleTimer();

            if (event.getTerminationType() != TerminationType.Basic)
                // we do not send TC-END in PreArranged closing case
                return;

            tcbm = (TCEndMessageImpl) TcapFactory.createTCEndMessage();
            tcbm.setDestinationTransactionId(this.remoteTransactionId);

            if (this.scheduledComponentList.size() > 0) {
                List<ComponentImpl> componentsToSend = new ArrayList<ComponentImpl>(this.scheduledComponentList.size());
                componentsToSend.addAll(this.scheduledComponentList);
                this.scheduledComponentList.clear();
                this.prepareComponents(componentsToSend);
                ComponentPortionImpl cPortion=new ComponentPortionImpl();
                cPortion.setComponents(componentsToSend);
                tcbm.setComponent(cPortion);
            }

            ApplicationContextNameImpl acn = event.getApplicationContextName();
            if (acn != null) { // acn & DialogPortion is absent in TCAP V1

                // set dialog portion
                DialogPortionImpl dp = TcapFactory.createDialogPortion();
                dp.setUnidirectional(false);
                DialogResponseAPDUImpl apdu = TcapFactory.createDialogAPDUResponse();
                apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
                dp.setDialogAPDU(apdu);

                apdu.setApplicationContextName(event.getApplicationContextName());
                if (event.getUserInformation() != null) {
                    apdu.setUserInformation(event.getUserInformation());
                }

                // WHERE THE HELL THIS COMES FROM!!!!
                // WHEN REJECTED IS USED !!!!!
                ResultImpl res = TcapFactory.createResult();
                res.setResultType(ResultType.Accepted);
                ResultSourceDiagnosticImpl rsd = TcapFactory.createResultSourceDiagnostic();
                rsd.setDialogServiceUserType(DialogServiceUserType.Null);
                apdu.setResultSourceDiagnostic(rsd);
                apdu.setResult(res);
                tcbm.setDialogPortion(dp);
            }
            // local address may change, lets check it
            if (event.getOriginatingAddress() != null && !event.getOriginatingAddress().equals(this.localAddress)) {
                this.localAddress = event.getOriginatingAddress();
            }

        } else if (state.get() == TRPseudoState.Active) {
            restartIdleTimer();

            if (event.getTerminationType() != TerminationType.Basic)
                // we do not send TC-END in PreArranged closing case
                return;

            tcbm = (TCEndMessageImpl) TcapFactory.createTCEndMessage();

            tcbm.setDestinationTransactionId(this.remoteTransactionId);
            if (this.scheduledComponentList.size() > 0) {
                List<ComponentImpl> componentsToSend = new ArrayList<ComponentImpl>(this.scheduledComponentList.size());
                this.prepareComponents(componentsToSend);
                ComponentPortionImpl cPortion=new ComponentPortionImpl();
                cPortion.setComponents(componentsToSend);
                tcbm.setComponent(cPortion);
            }

            // ITU - T Q774 Section 3.2.2.1 Dialogue Control

            // when a dialogue portion is received inopportunely (e.g. a
            // dialogue APDU is received during the active state of a
            // transaction).

            // Don't set the Application Context or Dialogue Portion in
            // Active state

        } else {
            throw new TCAPSendException(String.format("State is not %s or %s: it is %s", TRPseudoState.Active,
                    TRPseudoState.InitialReceived, this.state.get()));
        }

        try {
            ByteBuf buffer=dialogParser.encode(tcbm);
            this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
                    this.seqControl, this.networkId, this.localSsn, this.remotePc);

            this.scheduledComponentList.clear();
        } catch (Exception e) {
            // FIXME: remove freshly added invokes to free invoke ID??
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
            throw new TCAPSendException("Failed to send TC-End message: " + e.getMessage(), e);
        } finally {
            // FIXME: is this proper place - should we not release in case
            // of error ?
            release();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#sendUni()
     */
    public void send(TCUniRequest event) throws TCAPSendException {
        if (this.isStructured()) {
            throw new TCAPSendException("Structured dialogs do not use Uni");
        }

        TCUniMessageImpl msg = (TCUniMessageImpl) TcapFactory.createTCUniMessage();

        if (event.getApplicationContextName() != null) {
            DialogPortionImpl dp = TcapFactory.createDialogPortion();
            DialogRequestAPDUImpl apdu = TcapFactory.createDialogAPDURequest();
            apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
            apdu.setApplicationContextName(event.getApplicationContextName());
            if (event.getUserInformation() != null) {
                apdu.setUserInformation(event.getUserInformation());
            }
            dp.setUnidirectional(true);
            dp.setDialogAPDU(apdu);
            msg.setDialogPortion(dp);

        }

        if (this.scheduledComponentList.size() > 0) {
            List<ComponentImpl> componentsToSend = new ArrayList<ComponentImpl>(this.scheduledComponentList.size());
            componentsToSend.addAll(this.scheduledComponentList);
            this.scheduledComponentList.clear();
            this.prepareComponents(componentsToSend);
            ComponentPortionImpl cPortion=new ComponentPortionImpl();
            cPortion.setComponents(componentsToSend);
            msg.setComponent(cPortion);
        }

        try {
            ByteBuf buffer=dialogParser.encode(msg);
            this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
                    this.seqControl, this.networkId, this.localSsn, this.remotePc);
            this.scheduledComponentList.clear();
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to send message: ", e);
            }
            throw new TCAPSendException("Failed to send TC-Uni message: " + e.getMessage(), e);
        } finally {
            release();
        }
    }

    public void send(TCUserAbortRequest event) throws TCAPSendException {
        // is abort allowed in "Active" state ?
        if (!isStructured()) {
            throw new TCAPSendException("Unstructured dialog can not be aborted!");
        }

        if (this.state.get() == TRPseudoState.InitialReceived || this.state.get() == TRPseudoState.Active) {
            // allowed
            DialogPortionImpl dp = null;
            if (event.getUserInformation() != null || event.getDialogServiceUserType() != null) {
                // User information can be absent in TCAP V1

                dp = TcapFactory.createDialogPortion();
                dp.setUnidirectional(false);

                if (event.getDialogServiceUserType() != null) {
                    // ITU T Q.774 Read Dialogue end on page 12 and 3.2.2
                    // Abnormal
                    // procedures on page 13 and 14
                    DialogResponseAPDUImpl apdu = TcapFactory.createDialogAPDUResponse();
                    apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
                    apdu.setApplicationContextName(event.getApplicationContextName());
                    apdu.setUserInformation(event.getUserInformation());

                    ResultImpl res = TcapFactory.createResult();
                    res.setResultType(ResultType.RejectedPermanent);
                    ResultSourceDiagnosticImpl rsd = TcapFactory.createResultSourceDiagnostic();
                    rsd.setDialogServiceUserType(event.getDialogServiceUserType());
                    apdu.setResultSourceDiagnostic(rsd);
                    apdu.setResult(res);
                    dp.setDialogAPDU(apdu);
                } else {
                    // When a BEGIN message has been received (i.e. the
                    // dialogue
                    // is
                    // in the "Initiation Received" state) containing a
                    // Dialogue
                    // Request (AARQ) APDU, the TC-User can abort for any
                    // user
                    // defined reason. In such a situation, the TC-User
                    // issues a
                    // TC-U-ABORT request primitive with the Abort Reason
                    // parameter
                    // absent or with set to any value other than either
                    // "application-context-name-not-supported" or
                    // dialogue-refused". In such a case, a Dialogue Abort (ABRT) APDU is generated with abort-source coded
                    // as "dialogue-service-user",
                    // and supplied as the User Data parameter of the
                    // TR-U-ABORT
                    // request primitive. User information (if any) provided
                    // in
                    // the
                    // TC-U-ABORT request primitive is coded in the
                    // user-information
                    // field of the ABRT APDU.
                    DialogAbortAPDUImpl dapdu = TcapFactory.createDialogAPDUAbort();

                    ASNAbortSource as = (ASNAbortSource)TcapFactory.createAbortSource();
                    as.setAbortSourceType(AbortSourceType.User);
                    dapdu.setAbortSource(as);
                    dapdu.setUserInformation(event.getUserInformation());
                    dp.setDialogAPDU(dapdu);
                }
            }

            TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
            msg.setDestinationTransactionId(this.remoteTransactionId);
            msg.setDialogPortion(dp);

            if (state.get() == TRPseudoState.InitialReceived) {
                // local address may change, lets check it
                if (event.getOriginatingAddress() != null && !event.getOriginatingAddress().equals(this.localAddress)) {
                    this.localAddress = event.getOriginatingAddress();
                }
            }

            // no components
            try {
                ByteBuf buffer=dialogParser.encode(msg);
                this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress,
                        this.localAddress, this.seqControl, this.networkId, this.localSsn, this.remotePc);

                this.scheduledComponentList.clear();
            } catch (Exception e) {
                // FIXME: remove freshly added invokes to free invoke ID??
                if (logger.isEnabledFor(Level.ERROR)) {
                    e.printStackTrace();
                    logger.error("Failed to send message: ", e);
                }
                throw new TCAPSendException("Failed to send TC-U-Abort message: " + e.getMessage(), e);
            } finally {
                release();
            }
        } else if (this.state.get() == TRPseudoState.InitialSent) {
            release();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#sendComponent(org
     * .mobicents.protocols.ss7.tcap.api.tc.component.ComponentRequest)
     */
    public void sendComponent(ComponentImpl componentRequest) throws TCAPSendException {
        if (componentRequest.getType() == ComponentType.Invoke) {
            InvokeImpl invoke = componentRequest.getInvoke();

            // check if its taken!
            int invokeIndex = DialogImpl.getIndexFromInvokeId(invoke.getInvokeId());
            if (this.operationsSent[invokeIndex] != null) {
                throw new TCAPSendException("There is already operation with such invoke id!");
            }

            invoke.setState(OperationState.Pending);
            invoke.setDialog(this);

            // if the Invoke timeout value has not be reset by TCAP-User
            // for this invocation we are setting it to the the TCAP stack
            // default value
            if (invoke.getTimeout() == TCAPStackImpl._EMPTY_INVOKE_TIMEOUT)
                invoke.setTimeout(this.provider.getStack().getInvokeTimeout());
        } else {
            if (componentRequest.getType() != ComponentType.ReturnResult) {
                // we are sending a response and removing invokeId from
                // incomingInvokeList
            	if(componentRequest.getExistingComponent()!=null && componentRequest.getExistingComponent().getInvokeId()!=null)
            		this.removeIncomingInvokeId(componentRequest.getExistingComponent().getInvokeId());
            }
        }
        this.scheduledComponentList.add(componentRequest);
    }

    public void processInvokeWithoutAnswer(Long invokeId) {
        this.removeIncomingInvokeId(invokeId);
    }

    private void prepareComponents(List<ComponentImpl> res) {
    	int index = 0;
        while (this.scheduledComponentList.size() > index) {
        	ComponentImpl cr = this.scheduledComponentList.get(index);
            if (cr.getType() == ComponentType.Invoke) {
                InvokeImpl in = cr.getInvoke();
                // FIXME: check not null?
                this.operationsSent[getIndexFromInvokeId(in.getInvokeId())] = in;
                in.setState(OperationState.Sent);
            }

            res.add(cr);
            index++;
        }
    }

    public int getMaxUserDataLength() {

        return this.provider.getMaxUserDataLength(remoteAddress, localAddress, this.networkId);
    }

    // /////////////////
    // LOCAL METHODS //
    // /////////////////

    /**
     * @param remoteTransactionId the remoteTransactionId to set
     */
    void setRemoteTransactionId(byte[] remoteTransactionId) {
        this.remoteTransactionId = remoteTransactionId;
    }

    /**
     * @param localAddress the localAddress to set
     */
    public void setLocalAddress(SccpAddress localAddress) {
        this.localAddress = localAddress;
    }

    /**
     * @param remoteAddress the remoteAddress to set
     */
    public void setRemoteAddress(SccpAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    void processUni(TCUniMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {
        try {
            this.setRemoteAddress(remoteAddress);
            this.setLocalAddress(localAddress);

            // no dialog portion!
            // convert to indications
            TCUniIndicationImpl tcUniIndication = (TCUniIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                    .getDialogPrimitiveFactory()).createUniIndication(this);

            tcUniIndication.setDestinationAddress(localAddress);
            tcUniIndication.setOriginatingAddress(remoteAddress);
            // now comps
            List<ComponentImpl> comps = msg.getComponent().getComponents();
            tcUniIndication.setComponents(comps);

            if (msg.getDialogPortion() != null) {
                // it should be dialog req?
                DialogPortionImpl dp = msg.getDialogPortion();
                DialogRequestAPDUImpl apdu = (DialogRequestAPDUImpl) dp.getDialogAPDU();
                this.lastACN = apdu.getApplicationContextName();
                this.lastUI = apdu.getUserInformation();
                tcUniIndication.setApplicationContextName(this.lastACN);
                tcUniIndication.setUserInformation(this.lastUI);
            }

            // lets deliver to provider, this MUST not throw anything
            this.provider.deliver(this, tcUniIndication);

        } finally {
            this.release();
        }
    }

    protected void processBegin(TCBeginMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {

        TCBeginIndicationImpl tcBeginIndication = null;
        // this is invoked ONLY for server.
        if (state.get() != TRPseudoState.Idle) {
            // should we terminate dialog here?
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Received Begin primitive, but state is not: " + TRPseudoState.Idle + ". Dialog: " + this);
            }
            this.sendAbnormalDialog();
            return;
        }
        restartIdleTimer();

        // lets setup
        this.setRemoteAddress(remoteAddress);
        this.setLocalAddress(localAddress);
        this.setRemoteTransactionId(msg.getOriginatingTransactionId());
        // convert to indications
        tcBeginIndication = (TCBeginIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider.getDialogPrimitiveFactory())
                .createBeginIndication(this);

        tcBeginIndication.setDestinationAddress(localAddress);
        tcBeginIndication.setOriginatingAddress(remoteAddress);

        // if APDU and context data present, lets store it
        DialogPortionImpl dialogPortion = msg.getDialogPortion();

        if (dialogPortion != null && dialogPortion.getDialogAPDU()!=null) {
            // this should not be null....
            DialogAPDU apdu = dialogPortion.getDialogAPDU();
            if (apdu.getType() != DialogAPDUType.Request) {
                if (logger.isEnabledFor(Level.ERROR)) {
                    logger.error("Received non-Request APDU: " + apdu.getType() + ". Dialog: " + this);
                }
                this.sendAbnormalDialog();
                return;
            }
            DialogRequestAPDUImpl requestAPDU = (DialogRequestAPDUImpl) apdu;
            this.lastACN = requestAPDU.getApplicationContextName();
            this.lastUI = requestAPDU.getUserInformation();
            tcBeginIndication.setApplicationContextName(this.lastACN);
            tcBeginIndication.setUserInformation(this.lastUI);
        }
        
        tcBeginIndication.setComponents(processOperationsState(msg.getComponent()));
     // change state - before we deliver
        this.setState(TRPseudoState.InitialReceived);

        // lets deliver to provider
        this.provider.deliver(this, tcBeginIndication);
    }

    protected void processContinue(TCContinueMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {

        TCContinueIndicationImpl tcContinueIndication = null;
        if (state.get() == TRPseudoState.InitialSent) {
            restartIdleTimer();
            tcContinueIndication = (TCContinueIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                    .getDialogPrimitiveFactory()).createContinueIndication(this);

            // in continue remote address MAY change be changed, so lets
            // update!
            this.setRemoteAddress(remoteAddress);
            this.setRemoteTransactionId(msg.getOriginatingTransactionId());
            tcContinueIndication.setOriginatingAddress(remoteAddress);

            // here we will receive DialogResponse APDU - if request was
            // present!
            DialogPortionImpl dialogPortion = msg.getDialogPortion();
            if (dialogPortion != null) {
                // this should not be null....
                DialogAPDU apdu = dialogPortion.getDialogAPDU();
                if (apdu.getType() != DialogAPDUType.Response) {
                    if (logger.isEnabledFor(Level.ERROR)) {
                        logger.error("Received non-Response APDU: " + apdu.getType() + ". Dialog: " + this);
                    }
                    this.sendAbnormalDialog();
                    return;
                }
                DialogResponseAPDUImpl responseAPDU = (DialogResponseAPDUImpl) apdu;
                // this will be present if APDU is present.
                if (!responseAPDU.getApplicationContextName().equals(this.lastACN)) {
                    this.lastACN = responseAPDU.getApplicationContextName();
                }
                if (responseAPDU.getUserInformation() != null) {
                    this.lastUI = responseAPDU.getUserInformation();
                }
                tcContinueIndication.setApplicationContextName(responseAPDU.getApplicationContextName());
                tcContinueIndication.setUserInformation(responseAPDU.getUserInformation());
            } else if (this.dpSentInBegin) {
                // ITU - T Q.774 3.2.2 : Abnormal procedure page 13

                // when a dialogue portion is missing when its presence
                // is
                // mandatory (e.g. an AARQ APDU was sent in a Begin
                // message,
                // but
                // no AARE APDU was received in the first backward
                // Continue
                // message) or when a dialogue portion is received
                // inopportunely
                // (e.g. a dialogue APDU is received during the active
                // state
                // of
                // a transaction). At the side where the abnormality is
                // detected, a TC-P-ABORT indication primitive is issued
                // to
                // the
                // local TC-user with the "P-Abort" parameter in the
                // primitive
                // set to "abnormal dialogue". At the same time, a
                // TR-U-ABORT
                // request primitive is issued to the transaction
                // sub-layer
                // with
                // an ABRT APDU as user data. The abort-source field of
                // the
                // ABRT
                // APDU is set to "dialogue-service-provider" and the
                // user
                // information field is absent.

                sendAbnormalDialog();
                return;

            }
            tcContinueIndication.setOriginatingAddress(remoteAddress);
            // now comps
            tcContinueIndication.setComponents(processOperationsState(msg.getComponent()));
            // change state
            this.setState(TRPseudoState.Active);

            // lets deliver to provider
            this.provider.deliver(this, tcContinueIndication);

        } else if (state.get() == TRPseudoState.Active) {
            restartIdleTimer();
            // XXX: here NO APDU will be present, hence, no ACN/UI change
            tcContinueIndication = (TCContinueIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                    .getDialogPrimitiveFactory()).createContinueIndication(this);

            tcContinueIndication.setOriginatingAddress(remoteAddress);

            // now comps
            tcContinueIndication.setComponents(processOperationsState(msg.getComponent()));

            // lets deliver to provider
            this.provider.deliver(this, tcContinueIndication);

        } else {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(
                        "Received Continue primitive, but state is not proper: " + this.state + ", Dialog: " + this);
            }
            this.sendAbnormalDialog();
            return;
        }
    }

    protected void processEnd(TCEndMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {
        TCEndIndicationImpl tcEndIndication = null;
        try {
        	restartIdleTimer();
            tcEndIndication = (TCEndIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                    .getDialogPrimitiveFactory()).createEndIndication(this);

            if (state.get() == TRPseudoState.InitialSent) {
                // in end remote address MAY change be changed, so lets
                // update!
                this.setRemoteAddress(remoteAddress);
                tcEndIndication.setOriginatingAddress(remoteAddress);
            }

            DialogPortionImpl dialogPortion = msg.getDialogPortion();
            if (dialogPortion != null) {
                DialogAPDU apdu = dialogPortion.getDialogAPDU();
                if (apdu.getType() != DialogAPDUType.Response) {
                    if (logger.isEnabledFor(Level.ERROR)) {
                        logger.error("Received non-Response APDU: " + apdu.getType() + ". Dialog: " + this);
                    }
                    // we do not send "this.sendAbnormalDialog()"
                    // because no sense to send an answer to TC-END
                    return;
                }
                DialogResponseAPDUImpl responseAPDU = (DialogResponseAPDUImpl) apdu;
                // this will be present if APDU is present.
                if (!responseAPDU.getApplicationContextName().equals(this.lastACN)) {
                    this.lastACN = responseAPDU.getApplicationContextName();
                }
                if (responseAPDU.getUserInformation() != null) {
                    this.lastUI = responseAPDU.getUserInformation();
                }
                tcEndIndication.setApplicationContextName(responseAPDU.getApplicationContextName());
                tcEndIndication.setUserInformation(responseAPDU.getUserInformation());

            }
            // now comps
            tcEndIndication.setComponents(processOperationsState(msg.getComponent()));

            this.provider.deliver(this, tcEndIndication);
        } finally {
            release();
        }
    }

    protected void processAbort(TCAbortMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) {
    	try {
            Boolean IsAareApdu = false;
            Boolean IsAbrtApdu = false;
            ApplicationContextNameImpl acn = null;
            ResultSourceDiagnosticImpl resultSourceDiagnostic = null;
            ASNAbortSource abrtSrc = null;
            UserInformationImpl userInfo = null;
            DialogPortionImpl dp = msg.getDialogPortion();
            if (dp != null) {
                DialogAPDU apdu = dp.getDialogAPDU();
                if (apdu != null && apdu.getType() == DialogAPDUType.Abort) {
                    IsAbrtApdu = true;
                    DialogAbortAPDUImpl abortApdu = (DialogAbortAPDUImpl) apdu;
                    abrtSrc = abortApdu.getAbortSource();
                    userInfo = abortApdu.getUserInformation();
                }
                if (apdu != null && apdu.getType() == DialogAPDUType.Response) {
                    IsAareApdu = true;
                    DialogResponseAPDUImpl resptApdu = (DialogResponseAPDUImpl) apdu;
                    acn = resptApdu.getApplicationContextName();
                    resptApdu.getResult();
                    resultSourceDiagnostic = resptApdu.getResultSourceDiagnostic();
                    userInfo = resptApdu.getUserInformation();
                }
            }

            PAbortCauseType type = null;
            try {
            	type=msg.getPAbortCause();
            }
            catch(ParseException ex) {
            	
            }
            
            if (type == null) {
            	AbortSourceType asType=null;  
            	if(abrtSrc!=null) {
	            	try {
	            		asType=abrtSrc.getAbortSourceType();
	            	}
	            	catch(ParseException ex) {
	            		
	            	}
            	}
            	
                if ((asType != null && asType == AbortSourceType.Provider)) {
                    type = PAbortCauseType.AbnormalDialogue;
                }
                
                DialogServiceProviderType dspType=null;
                if(resultSourceDiagnostic!=null) {
                	try {
                		dspType=resultSourceDiagnostic.getDialogServiceProviderType();
                	}
                	catch(ParseException ex) {
                		
                	}
                }
                
                if (dspType != null) {
                    if (dspType == DialogServiceProviderType.NoCommonDialogPortion)
                        type = PAbortCauseType.NoCommonDialoguePortion;
                    else
                        type = PAbortCauseType.NoReasonGiven;
                }
            }

            if (type != null) {

                // its TC-P-Abort
                TCPAbortIndicationImpl tcAbortIndication = (TCPAbortIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                        .getDialogPrimitiveFactory()).createPAbortIndication(this);
                tcAbortIndication.setPAbortCause(type);

                this.provider.deliver(this, tcAbortIndication);

            } else {
                // its TC-U-Abort
                TCUserAbortIndicationImpl tcUAbortIndication = (TCUserAbortIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                        .getDialogPrimitiveFactory()).createUAbortIndication(this);
                if (IsAareApdu)
                    tcUAbortIndication.SetAareApdu();
                if (IsAbrtApdu)
                    tcUAbortIndication.SetAbrtApdu();
                tcUAbortIndication.setUserInformation(userInfo);
                tcUAbortIndication.setAbortSource(abrtSrc);
                tcUAbortIndication.setApplicationContextName(acn);
                tcUAbortIndication.setResultSourceDiagnostic(resultSourceDiagnostic);

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
        TCPAbortIndicationImpl tcAbortIndication = null;
        try {
            if (this.remoteTransactionId == null) {
                // no remoteTransactionId - we can not send back TC-ABORT
                return;
            }

            // sending to the remote side
            DialogPortionImpl dp = TcapFactory.createDialogPortion();
            dp.setUnidirectional(false);

            DialogAbortAPDUImpl dapdu = TcapFactory.createDialogAPDUAbort();

            ASNAbortSource as = TcapFactory.createAbortSource();
            as.setAbortSourceType(AbortSourceType.Provider);

            dapdu.setAbortSource((ASNAbortSource)as);
            dp.setDialogAPDU(dapdu);

            TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
            msg.setDestinationTransactionId(this.remoteTransactionId);
            msg.setDialogPortion(dp);

            try {
                ByteBuf buffer=provider.encodeAbortMessage(msg);               
                this.provider.send(buffer, false, this.remoteAddress, this.localAddress, this.seqControl,
                        this.networkId, this.localSsn, this.remotePc);
            } catch (Exception e) {
                if (logger.isEnabledFor(Level.ERROR)) {
                    logger.error("Failed to send message: ", e);
                }
            }

            // sending to the local side
            tcAbortIndication = (TCPAbortIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                    .getDialogPrimitiveFactory()).createPAbortIndication(this);
            tcAbortIndication.setPAbortCause(PAbortCauseType.AbnormalDialogue);
            // tcAbortIndication.setLocalProviderOriginated(true);

            this.provider.deliver(this, tcAbortIndication);
        } finally {
            this.release();
            // this.scheduledComponentList.clear();
        }
    }

    protected List<ComponentImpl> processOperationsState(ComponentPortionImpl componentsPortion) {
        if(componentsPortion==null) {
        	return null;
        }
        
        List<ComponentImpl> components=componentsPortion.getComponents();
    	if (components == null) {
            return null;
        }

        List<ComponentImpl> resultingIndications = new ArrayList<ComponentImpl>();
        for (ComponentImpl ci : components) {
            Long invokeId;
            if (ci.getType() == ComponentType.Invoke)
                invokeId = ci.getInvoke().getLinkedId();
            else
                invokeId = ci.getExistingComponent().getInvokeId();
            
            InvokeImpl invoke = null;
            int index = 0;
            if (invokeId != null) {
                index = getIndexFromInvokeId(invokeId);
                invoke = this.operationsSent[index];                
            }

            switch (ci.getType()) {

                case Invoke:
                    if (invokeId != null && invoke == null) {
                        logger.error(String.format("Rx : %s but no sent Invoke for linkedId exists", ci));

                        ProblemImpl p = new ProblemImpl();
                        p.setInvokeProblemType(InvokeProblemType.UnrechognizedLinkedID);
                        this.addReject(resultingIndications, ci.getInvoke().getInvokeId(), p);
                    } else {
                        if (invoke != null) {
                        	ci.getInvoke().setLinkedInvoke(invoke);
                        }

                        if (!this.addIncomingInvokeId(ci.getInvoke().getInvokeId())) {
                            logger.error(String.format("Rx : %s but there is already Invoke with this invokeId", ci));

                            ProblemImpl p = new ProblemImpl();
                            p.setInvokeProblemType(InvokeProblemType.DuplicateInvokeID);
                            this.addReject(resultingIndications, ci.getInvoke().getInvokeId(), p);
                        } else {
                            resultingIndications.add(ci);
                        }
                    }
                    break;

                case ReturnResult:

                    if (invoke == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        ProblemImpl p = new ProblemImpl();
                        p.setReturnResultProblemType(ReturnResultProblemType.UnrecognizedInvokeID);
                        this.addReject(resultingIndications, ci.getReturnResult().getInvokeId(), p);
                    } else if (invoke.getInvokeClass() != InvokeClass.Class1 && invoke.getInvokeClass() != InvokeClass.Class3) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 3", ci));

                        ProblemImpl p = new ProblemImpl();
                        p.setReturnResultProblemType(ReturnResultProblemType.ReturnResultUnexpected);
                        this.addReject(resultingIndications, ci.getReturnResult().getInvokeId(), p);
                    } else {
                        resultingIndications.add(ci);
                        ReturnResultImpl rri = (ReturnResultImpl) ci.getReturnResult();
                        if (rri.getOperationCode() == null)
                            rri.setOperationCode(invoke.getOperationCode());
                    }
                    break;

                case ReturnResultLast:

                    if (invoke == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        ProblemImpl p = new ProblemImpl();
                        p.setReturnResultProblemType(ReturnResultProblemType.UnrecognizedInvokeID);
                        this.addReject(resultingIndications, ci.getExistingComponent().getInvokeId(), p);
                    } else if (invoke.getInvokeClass() != InvokeClass.Class1 && invoke.getInvokeClass() != InvokeClass.Class3) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 3", ci));

                        ProblemImpl p = new ProblemImpl();
                        p.setReturnResultProblemType(ReturnResultProblemType.ReturnResultUnexpected);
                        this.addReject(resultingIndications, ci.getExistingComponent().getInvokeId(), p);
                    } else {
                        invoke.onReturnResultLast();
                        if (invoke.isSuccessReported()) {
                            resultingIndications.add(ci);
                        }
                        ReturnResultLastImpl rri = (ReturnResultLastImpl) ci.getReturnResultLast();
                        if (rri.getOperationCode() == null)
                            rri.setOperationCode(invoke.getOperationCode());
                    }
                    break;

                case ReturnError:
                    if (invoke == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        ProblemImpl p = new ProblemImpl();
                        p.setReturnErrorProblemType(ReturnErrorProblemType.UnrecognizedInvokeID);
                        this.addReject(resultingIndications, ci.getExistingComponent().getInvokeId(), p);
                    } else if (invoke.getInvokeClass() != InvokeClass.Class1 && invoke.getInvokeClass() != InvokeClass.Class2) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 2", ci));

                        ProblemImpl p = new ProblemImpl();
                        p.setReturnErrorProblemType(ReturnErrorProblemType.ReturnErrorUnexpected);
                        this.addReject(resultingIndications, ci.getExistingComponent().getInvokeId(), p);
                    } else {
                        invoke.onError();
                        if (invoke.isErrorReported()) {
                            resultingIndications.add(ci);
                        }
                    }
                    break;

                case Reject:
                    RejectImpl rej = ci.getReject();
                    if (invoke != null) {
                        // If the Reject Problem is the InvokeProblemType we
                        // should move the invoke to the idle state
                        ProblemImpl problem = rej.getProblem();
                        InvokeProblemType ipt=null;
                        try {
                        	ipt=problem.getInvokeProblemType();
                        }
                        catch(ParseException ex) {
                        	
                        }
                        
                        if (!rej.isLocalOriginated() && ipt != null)
                            invoke.onReject();
                    }
                    if (rej.isLocalOriginated() && this.isStructured()) {
                        try {
                            // this is a local originated Reject - we are rejecting an incoming component
                            // we need to send a Reject also to a peer
                            this.sendComponent(ci);
                        } catch (TCAPSendException e) {
                            logger.error("TCAPSendException when sending Reject component : Dialog: " + this, e);
                        }
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

    private void addReject(List<ComponentImpl> resultingIndications, Long invokeId, ProblemImpl p) {
        try {
        	ComponentImpl component=TcapFactory.createComponentReject();
        	component.getReject().setLocalOriginated(true);
        	component.getReject().setInvokeId(invokeId);
        	component.getReject().setProblem(p);
            resultingIndications.add(component);

            if (this.isStructured())
                this.sendComponent(component);
        } catch (TCAPSendException e) {
            logger.error(String.format("Error sending Reject component", e));
        }
    }

    protected void setState(TRPseudoState newState) {
    	if(this.state.get()==TRPseudoState.Expunged)
    		return;
    	
    	if(this.state.getAndSet(newState)==TRPseudoState.Expunged)
    		this.state.set(TRPseudoState.Expunged);
    	
        if (newState == TRPseudoState.Expunged) {
            stopIdleTimer();
            provider.release(this);
        }
    }

    private void startIdleTimer() {
        if (!this.structured)
            return;
        
        IdleTimerTask t = new IdleTimerTask();
        t.d = this;
        FutureTask<Void> dummyTask=new FutureTask<Void>(t,null);
        
        if (!this.idleTimerFuture.compareAndSet(null, dummyTask)) {
            throw new IllegalStateException();
        }

        this.idleTimerFuture.set(this.executor.schedule(t, this.idleTaskTimeout, TimeUnit.MILLISECONDS));
    }

    private void stopIdleTimer() {
        if (!this.structured)
            return;

        Future<?> original=this.idleTimerFuture.getAndSet(null);
        if (original != null) {
        	original.cancel(false);                
        }
    }

    private void restartIdleTimer() {
        stopIdleTimer();
        startIdleTimer();
    }

    private class IdleTimerTask implements Runnable {
        DialogImpl d;

        public void run() {
        	d.idleTimerFuture.set(null);

        	d.idleTimerActionTaken.set(false);
            d.idleTimerInvoked.set(true);
            provider.timeout(d);
            // send abort
            if (d.idleTimerActionTaken.get()) {
                startIdleTimer();
            } else {
                if (remoteTransactionId != null)
                    sendAbnormalDialog();
                else
                    release();
            }
            
            d.idleTimerInvoked.set(false);            
        }
    }

    // ////////////////////
    // IND like methods //
    // ///////////////////
    public void operationEnded(InvokeImpl tcInvokeRequestImpl) {
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
    public void operationTimedOut(InvokeImpl invoke) {
        // this op died cause of timeout, TC-L-CANCEL!
    	int index = getIndexFromInvokeId(invoke.getInvokeId());
        freeInvokeId(invoke.getInvokeId());
        this.operationsSent[index] = null;
        // lets call listener
        this.provider.operationTimedOut(invoke);
    }

    // TC-TIMER-RESET
    public void resetTimer(Long invokeId) throws TCAPException {
    	int index = getIndexFromInvokeId(invokeId);
        InvokeImpl invoke = operationsSent[index];
        if (invoke == null) {
            throw new TCAPException("No operation with this ID");
        }
        invoke.startTimer();
    }

    public TRPseudoState getState() {
        return this.state.get();
    }

    public Object getUserObject() {
        return this.userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public long getIdleTaskTimeout() {
        return this.idleTaskTimeout;
    }

    public void setIdleTaskTimeout(long idleTaskTimeoutMs) {
        this.idleTaskTimeout = idleTaskTimeoutMs;
    }

    public int getRemotePc() {
        return remotePc;
    }

    public void setRemotePc(int remotePc) {
        this.remotePc = remotePc;
    }

    public long getStartTimeDialog() {
        return this.startDialogTime;
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