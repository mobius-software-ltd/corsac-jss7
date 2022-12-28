/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import java.io.Externalizable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.component.OperationState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.AbortSourceType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.DialogAPDU;
import org.restcomm.protocols.ss7.tcap.asn.DialogAPDUType;
import org.restcomm.protocols.ss7.tcap.asn.DialogAbortAPDU;
import org.restcomm.protocols.ss7.tcap.asn.DialogPortion;
import org.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDU;
import org.restcomm.protocols.ss7.tcap.asn.DialogResponseAPDU;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.restcomm.protocols.ss7.tcap.asn.ParseException;
import org.restcomm.protocols.ss7.tcap.asn.Result;
import org.restcomm.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.restcomm.protocols.ss7.tcap.asn.ResultType;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.asn.Utils;
import org.restcomm.protocols.ss7.tcap.asn.comp.BaseComponent;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeWrapper;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
import org.restcomm.protocols.ss7.tcap.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUniMessage;
import org.restcomm.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

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
    //private static final int _REMOVE_TIMEOUT = 30000;

    private static final Logger logger = LogManager.getLogger(DialogImpl.class);

    private Externalizable userObject;

    // values for timer timeouts
    //private long removeTaskTimeout = _REMOVE_TIMEOUT;
    private long idleTaskTimeout;

    // sent/received acn, holds last acn/ui.
    private ApplicationContextName lastACN;
    private UserInformation lastUI; // optional

    private Long localTransactionIdObject;
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
    protected ConcurrentHashMap<Integer,InvokeWrapper> sentOperations = new ConcurrentHashMap<Integer,InvokeWrapper>();
    protected InvokeWrapper[] scheduledOperations = new InvokeWrapper[invokeIDTable.length];
    
    private ConcurrentHashMap<Integer,Integer> incomingInvokeList = new ConcurrentHashMap<Integer,Integer>();
    private ScheduledExecutorService executor;

    // scheduled components list
    private List<BaseComponent> scheduledComponentList = new ArrayList<BaseComponent>();
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
    
    private static int getIndexFromInvokeId(Integer l) {
        int tmp = l.intValue();
        return tmp + _INVOKE_TABLE_SHIFT;
    }

    private static Integer getInvokeIdFromIndex(int index) {
        int tmp = index - _INVOKE_TABLE_SHIFT;
        return tmp;
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
        if (origTransactionId != null)
            this.localTransactionIdObject = origTransactionId;
        
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
    	Collection<InvokeWrapper> allInvokes = getAllInvokes();
    	for (InvokeWrapper invoke:allInvokes) {
            if (invoke != null) {
            	invoke.setState(OperationState.Idle);
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
        return this.remoteTransactionIdObject;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#getNewInvokeId()
     */
    public Integer getNewInvokeId() throws TCAPException {
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
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#cancelInvocation (java.lang.Integer)
     */
    public boolean cancelInvocation(Integer invokeId) throws TCAPException {
        int index = getIndexFromInvokeId(invokeId);
        if (index < 0 || index >= this.invokeIDTable.length) {
            throw new TCAPException("Wrong invoke id passed.");
        }

        // lookup through send buffer.
        for (index = 0; index < this.scheduledComponentList.size(); index++) {
        	BaseComponent cr = this.scheduledComponentList.get(index);        	
            if ((cr instanceof Invoke) && cr.getInvokeId().equals(invokeId)) {
                // lucky
                // TCInvokeRequestImpl invoke = (TCInvokeRequestImpl) cr;
                // there is no notification on cancel?
                this.scheduledComponentList.remove(index);
                int invokeIndex = DialogImpl.getIndexFromInvokeId(cr.getInvokeId());
            	InvokeWrapper pendingWrapper = this.scheduledOperations[invokeIndex];
                if(pendingWrapper!=null) {
                	pendingWrapper.stopTimer();
                	pendingWrapper.setState(OperationState.Idle);
                	this.scheduledOperations[invokeIndex]=null;
                }
                return true;
            }
        }

        return false;
    }

    private void freeInvokeId(Integer l) {
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
    public ApplicationContextName getApplicationContextName() {
        return lastACN;
    }

    /**
     * @return the ui
     */
    public UserInformation getUserInformation() {
        return lastUI;
    }

    /**
     * Adding the new incoming invokeId into incomingInvokeList list
     *
     * @param invokeId
     * @return false: failure - this invokeId already present in the list
     */
    private boolean addIncomingInvokeId(Integer invokeId) {
    	Integer oldValue=this.incomingInvokeList.putIfAbsent(invokeId, invokeId);
    	return oldValue==null;
    }

    private void removeIncomingInvokeId(Integer invokeId) {
    	if(invokeId!=null)
    		this.incomingInvokeList.remove(invokeId);        
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.restcomm
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
        TCBeginMessage tcbm = TcapFactory.createTCBeginMessage();

        // build DP

        if (event.getApplicationContextName() != null) {
            this.dpSentInBegin = true;
            DialogPortion dp = TcapFactory.createDialogPortion();
            dp.setUnidirectional(false);
            DialogRequestAPDU apdu = TcapFactory.createDialogAPDURequest();
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
        tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionIdObject, isSwapTcapIdBytes));
        if (this.scheduledComponentList.size() > 0) {
        	List<BaseComponent> componentsToSend = new ArrayList<BaseComponent>(this.scheduledComponentList.size());
        	this.prepareComponents(componentsToSend);
            tcbm.setComponents(componentsToSend);
        }

        try {
        	ByteBuf buffer=dialogParser.encode(tcbm);
            this.setState(TRPseudoState.InitialSent);
            provider.getStack().newMessageSent(tcbm.getName(),buffer.readableBytes(), this.networkId);
            this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
                    this.seqControl, this.networkId, this.localSsn, this.remotePc);
            this.scheduledComponentList.clear();
        } catch (Throwable e) {
            // FIXME: remove freshly added invokes to free invoke ID??
            // TODO: should we release this dialog because TC-BEGIN sending has been failed
            if (logger.isErrorEnabled()) {
                logger.error("Failed to send message: ", e);
            }
            throw new TCAPSendException("Failed to send TC-Begin message: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.restcomm
     * .protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest)
     */
    public void send(TCContinueRequest event) throws TCAPSendException {
        if (!this.isStructured()) {
            throw new TCAPSendException("Unstructured dialogs do not use Continue");
        }
        
        if (this.state.get() == TRPseudoState.InitialReceived) {
            this.idleTimerActionTaken.set(true);
            restartIdleTimer();
            TCContinueMessage tcbm = TcapFactory.createTCContinueMessage();

            if (event.getApplicationContextName() != null) {

                // set dialog portion
                DialogPortion dp = TcapFactory.createDialogPortion();
                dp.setUnidirectional(false);
                DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
                apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
                dp.setDialogAPDU(apdu);
                apdu.setApplicationContextName(event.getApplicationContextName());
                if (event.getUserInformation() != null) {
                    apdu.setUserInformation(event.getUserInformation());
                }
                // WHERE THE HELL THIS COMES FROM!!!!
                // WHEN REJECTED IS USED !!!!!
                Result res = TcapFactory.createResult();
                res.setResultType(ResultType.Accepted);
                ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
                rsd.setDialogServiceUserType(DialogServiceUserType.Null);
                apdu.setResultSourceDiagnostic(rsd);
                apdu.setResult(res);
                tcbm.setDialogPortion(dp);

            }

            tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionIdObject, isSwapTcapIdBytes));
            tcbm.setDestinationTransactionId(Utils.encodeTransactionId(this.remoteTransactionIdObject, isSwapTcapIdBytes));
            if (this.scheduledComponentList.size() > 0) {
                List<BaseComponent> componentsToSend = new ArrayList<BaseComponent>(this.scheduledComponentList.size());
                this.prepareComponents(componentsToSend);
                tcbm.setComponents(componentsToSend);
            }
            
            // local address may change, lets check it;
            if (event.getOriginatingAddress() != null && !event.getOriginatingAddress().equals(this.localAddress)) {
                this.localAddress = event.getOriginatingAddress();
            }
            try {
            	ByteBuf buffer=dialogParser.encode(tcbm);                
            	provider.getStack().newMessageSent(tcbm.getName(),buffer.readableBytes(), this.networkId);
                this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress,
                        this.localAddress, this.seqControl, this.networkId, this.localSsn, this.remotePc);
                this.setState(TRPseudoState.Active);
                this.scheduledComponentList.clear();
            } catch (Exception e) {
                // FIXME: remove freshly added invokes to free invoke ID??
                if (logger.isErrorEnabled()) {
                    logger.error("Failed to send message: ", e);
                }
                throw new TCAPSendException("Failed to send TC-Continue message: " + e.getMessage(), e);
            }

        } else if (state.get() == TRPseudoState.Active) {
            this.idleTimerActionTaken.set(true);
            restartIdleTimer();
            // in this we ignore acn and passed args(except qos)
            TCContinueMessage tcbm = TcapFactory.createTCContinueMessage();

            tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionIdObject, isSwapTcapIdBytes));
            tcbm.setDestinationTransactionId(Utils.encodeTransactionId(this.remoteTransactionIdObject, isSwapTcapIdBytes));
            if (this.scheduledComponentList.size() > 0) {
                List<BaseComponent> componentsToSend = new ArrayList<BaseComponent>(this.scheduledComponentList.size());
                this.prepareComponents(componentsToSend);
                tcbm.setComponents(componentsToSend);
            }

            try {
            	ByteBuf buffer=dialogParser.encode(tcbm);
            	provider.getStack().newMessageSent(tcbm.getName(),buffer.readableBytes(), this.networkId);
                this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress,
                        this.localAddress, this.seqControl, this.networkId, this.localSsn, this.remotePc);
                this.scheduledComponentList.clear();
            } catch (Exception e) {
                // FIXME: remove freshly added invokes to free invoke ID??
                if (logger.isErrorEnabled()) {
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
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.restcomm
     * .protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest)
     */
    public void send(TCEndRequest event) throws TCAPSendException {
       if (!this.isStructured()) {
            throw new TCAPSendException("Unstructured dialogs do not use End");
        }

        TCEndMessage tcbm = null;
        if (state.get() == TRPseudoState.InitialReceived) {
            // TC-END request primitive issued in response to a TC-BEGIN
            // indication primitive
            this.idleTimerActionTaken.set(true);
            stopIdleTimer();

            if (event.getTerminationType() != TerminationType.Basic)
                // we do not send TC-END in PreArranged closing case
                return;

            tcbm = TcapFactory.createTCEndMessage();
            tcbm.setDestinationTransactionId(Utils.encodeTransactionId(this.remoteTransactionIdObject, isSwapTcapIdBytes));

            if (this.scheduledComponentList.size() > 0) {
                List<BaseComponent> componentsToSend = new ArrayList<BaseComponent>(this.scheduledComponentList.size());
                componentsToSend.addAll(this.scheduledComponentList);
                this.scheduledComponentList.clear();
                this.prepareComponents(componentsToSend);
                tcbm.setComponents(componentsToSend);
            }

            ApplicationContextName acn = event.getApplicationContextName();
            if (acn != null) { // acn & DialogPortion is absent in TCAP V1

                // set dialog portion
                DialogPortion dp = TcapFactory.createDialogPortion();
                dp.setUnidirectional(false);
                DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
                apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
                dp.setDialogAPDU(apdu);

                apdu.setApplicationContextName(event.getApplicationContextName());
                if (event.getUserInformation() != null) {
                    apdu.setUserInformation(event.getUserInformation());
                }

                // WHERE THE HELL THIS COMES FROM!!!!
                // WHEN REJECTED IS USED !!!!!
                Result res = TcapFactory.createResult();
                res.setResultType(ResultType.Accepted);
                ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
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

            tcbm = TcapFactory.createTCEndMessage();

            tcbm.setDestinationTransactionId(Utils.encodeTransactionId(this.remoteTransactionIdObject, isSwapTcapIdBytes));
            if (this.scheduledComponentList.size() > 0) {
                List<BaseComponent> componentsToSend = new ArrayList<BaseComponent>(this.scheduledComponentList.size());
                this.prepareComponents(componentsToSend);
                tcbm.setComponents(componentsToSend);
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
            provider.getStack().newMessageSent(tcbm.getName(),buffer.readableBytes(), this.networkId);
            this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
                    this.seqControl, this.networkId, this.localSsn, this.remotePc);

            this.scheduledComponentList.clear();
        } catch (Exception e) {
            // FIXME: remove freshly added invokes to free invoke ID??
            if (logger.isErrorEnabled()) {
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

        TCUniMessage msg = TcapFactory.createTCUniMessage();

        if (event.getApplicationContextName() != null) {
            DialogPortion dp = TcapFactory.createDialogPortion();
            DialogRequestAPDU apdu = TcapFactory.createDialogAPDURequest();
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
            List<BaseComponent> componentsToSend = new ArrayList<BaseComponent>(this.scheduledComponentList.size());
            componentsToSend.addAll(this.scheduledComponentList);
            this.scheduledComponentList.clear();
            this.prepareComponents(componentsToSend);
            msg.setComponents(componentsToSend);
        }

        try {
            ByteBuf buffer=dialogParser.encode(msg);
            provider.getStack().newMessageSent(msg.getName(),buffer.readableBytes(), this.networkId);
            this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress, this.localAddress,
                    this.seqControl, this.networkId, this.localSsn, this.remotePc);
            this.scheduledComponentList.clear();
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
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
            DialogPortion dp = null;
            if (event.getUserInformation() != null || event.getDialogServiceUserType() != null) {
                // User information can be absent in TCAP V1

                dp = TcapFactory.createDialogPortion();
                dp.setUnidirectional(false);

                if (event.getDialogServiceUserType() != null) {
                    // ITU T Q.774 Read Dialogue end on page 12 and 3.2.2
                    // Abnormal
                    // procedures on page 13 and 14
                    DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
                    apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
                    apdu.setApplicationContextName(event.getApplicationContextName());
                    apdu.setUserInformation(event.getUserInformation());

                    Result res = TcapFactory.createResult();
                    res.setResultType(ResultType.RejectedPermanent);
                    ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
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
                    DialogAbortAPDU dapdu = TcapFactory.createDialogAPDUAbort();

                    dapdu.setAbortSource(AbortSourceType.User);
                    dapdu.setUserInformation(event.getUserInformation());
                    dp.setDialogAPDU(dapdu);
                }
            }

            TCAbortMessage msg = TcapFactory.createTCAbortMessage();
            msg.setDestinationTransactionId(Utils.encodeTransactionId(this.remoteTransactionIdObject, isSwapTcapIdBytes));
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
                provider.getStack().newMessageSent(msg.getName(),buffer.readableBytes(), this.networkId);
                if(msg.getPAbortCause()!=null)
                	provider.getStack().newAbortSent(msg.getPAbortCause().name(), this.networkId);
                else
                	provider.getStack().newAbortSent("User", this.networkId);
                
                this.provider.send(buffer, event.getReturnMessageOnError(), this.remoteAddress,
                        this.localAddress, this.seqControl, this.networkId, this.localSsn, this.remotePc);

                this.scheduledComponentList.clear();
            } catch (Exception e) {
                // FIXME: remove freshly added invokes to free invoke ID??
                if (logger.isErrorEnabled()) {
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

    public int getDataLength(TCBeginRequest event) throws TCAPSendException {

        TCBeginMessage tcbm = TcapFactory.createTCBeginMessage();

        if (event.getApplicationContextName() != null) {
            DialogPortion dp = TcapFactory.createDialogPortion();
            dp.setUnidirectional(false);
            DialogRequestAPDU apdu = TcapFactory.createDialogAPDURequest();
            apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
            dp.setDialogAPDU(apdu);
            apdu.setApplicationContextName(event.getApplicationContextName());
            if (event.getUserInformation() != null) {
                apdu.setUserInformation(event.getUserInformation());
            }
            tcbm.setDialogPortion(dp);
        }

        // now comps
        tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionIdObject, isSwapTcapIdBytes));
        if (this.scheduledComponentList.size() > 0) {
        	ArrayList<BaseComponent> componentsToSend = new ArrayList<BaseComponent>(this.scheduledComponentList.size());
        	
        	for (int index = 0; index < this.scheduledComponentList.size(); index++) {
                componentsToSend.add(this.scheduledComponentList.get(index));
            }
        	
            tcbm.setComponents(componentsToSend);
        }

        try {
	        ByteBuf buffer=dialogParser.encode(tcbm);
	        return buffer.readableBytes();
        }catch (Throwable e) {
            // FIXME: remove freshly added invokes to free invoke ID??
            // TODO: should we release this dialog because TC-BEGIN sending has been failed
            if (logger.isErrorEnabled()) {
                logger.error("Failed to send message: ", e);
            }
            throw new TCAPSendException("Failed to send TC-Begin message: " + e.getMessage(), e);
        }
    }

    public int getDataLength(TCContinueRequest event) throws TCAPSendException {

        TCContinueMessage tcbm = TcapFactory.createTCContinueMessage();

        if (event.getApplicationContextName() != null) {

            // set dialog portion
            DialogPortion dp = TcapFactory.createDialogPortion();
            dp.setUnidirectional(false);
            DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
            apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
            dp.setDialogAPDU(apdu);
            apdu.setApplicationContextName(event.getApplicationContextName());
            if (event.getUserInformation() != null) {
                apdu.setUserInformation(event.getUserInformation());
            }
            // WHERE THE HELL THIS COMES FROM!!!!
            // WHEN REJECTED IS USED !!!!!
            Result res = TcapFactory.createResult();
            res.setResultType(ResultType.Accepted);
            ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
            rsd.setDialogServiceUserType(DialogServiceUserType.Null);
            apdu.setResultSourceDiagnostic(rsd);
            apdu.setResult(res);
            tcbm.setDialogPortion(dp);

        }

        tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(this.localTransactionIdObject, isSwapTcapIdBytes));
        tcbm.setDestinationTransactionId(Utils.encodeTransactionId(this.remoteTransactionIdObject, isSwapTcapIdBytes));
        if (this.scheduledComponentList.size() > 0) {
        	ArrayList<BaseComponent> componentsToSend = new ArrayList<BaseComponent>(this.scheduledComponentList.size());
        	
        	for (int index = 0; index < this.scheduledComponentList.size(); index++) {
                componentsToSend.add(this.scheduledComponentList.get(index));
            }
        	tcbm.setComponents(componentsToSend);
        }

        try {
	        ByteBuf buffer=dialogParser.encode(tcbm);
	        return buffer.readableBytes();
        }catch (Throwable e) {
            // FIXME: remove freshly added invokes to free invoke ID??
            // TODO: should we release this dialog because TC-BEGIN sending has been failed
            if (logger.isErrorEnabled()) {
                logger.error("Failed to send message: ", e);
            }
            throw new TCAPSendException("Failed to send TC-Begin message: " + e.getMessage(), e);
        }
    }

    public int getDataLength(TCEndRequest event) throws TCAPSendException {

        // TC-END request primitive issued in response to a TC-BEGIN
        // indication primitive
        TCEndMessage tcbm = TcapFactory.createTCEndMessage();
        tcbm.setDestinationTransactionId(Utils.encodeTransactionId(this.remoteTransactionIdObject, isSwapTcapIdBytes));

        if (this.scheduledComponentList.size() > 0) {
        	ArrayList<BaseComponent> componentsToSend = new ArrayList<BaseComponent>(this.scheduledComponentList.size());
        	
        	for (int index = 0; index < this.scheduledComponentList.size(); index++) {
                componentsToSend.add(this.scheduledComponentList.get(index));
            }
        	tcbm.setComponents(componentsToSend);
        }

        if (state.get() == TRPseudoState.InitialReceived) {
            ApplicationContextName acn = event.getApplicationContextName();
            if (acn != null) { // acn & DialogPortion is absent in TCAP V1

                // set dialog portion
                DialogPortion dp = TcapFactory.createDialogPortion();
                dp.setUnidirectional(false);
                DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
                apdu.setDoNotSendProtocolVersion(doNotSendProtocolVersion());
                dp.setDialogAPDU(apdu);

                apdu.setApplicationContextName(event.getApplicationContextName());
                if (event.getUserInformation() != null) {
                    apdu.setUserInformation(event.getUserInformation());
                }

                // WHERE THE HELL THIS COMES FROM!!!!
                // WHEN REJECTED IS USED !!!!!
                Result res = TcapFactory.createResult();
                res.setResultType(ResultType.Accepted);
                ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
                rsd.setDialogServiceUserType(DialogServiceUserType.Null);
                apdu.setResultSourceDiagnostic(rsd);
                apdu.setResult(res);
                tcbm.setDialogPortion(dp);
            }
        }

        try {
	        ByteBuf buffer=dialogParser.encode(tcbm);
	        return buffer.readableBytes();
        }catch (Throwable e) {
            // FIXME: remove freshly added invokes to free invoke ID??
            // TODO: should we release this dialog because TC-BEGIN sending has been failed
            if (logger.isErrorEnabled()) {
                logger.error("Failed to send message: ", e);
            }
            throw new TCAPSendException("Failed to send TC-Begin message: " + e.getMessage(), e);
        }
    }

    public int getDataLength(TCUniRequest event) throws TCAPSendException {

        TCUniMessage msg = TcapFactory.createTCUniMessage();

        if (event.getApplicationContextName() != null) {
            DialogPortion dp = TcapFactory.createDialogPortion();
            DialogRequestAPDU apdu = TcapFactory.createDialogAPDURequest();
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
        	ArrayList<BaseComponent> componentsToSend = new ArrayList<BaseComponent>(this.scheduledComponentList.size());
        	
        	for (int index = 0; index < this.scheduledComponentList.size(); index++) {
                componentsToSend.add(this.scheduledComponentList.get(index));
            }
        	msg.setComponents(componentsToSend);

        }

        try {
	        ByteBuf buffer=dialogParser.encode(msg);
	        return buffer.readableBytes();
        }catch (Throwable e) {
            // FIXME: remove freshly added invokes to free invoke ID??
            // TODO: should we release this dialog because TC-BEGIN sending has been failed
            if (logger.isErrorEnabled()) {
                logger.error("Failed to send message: ", e);
            }
            throw new TCAPSendException("Failed to send TC-Begin message: " + e.getMessage(), e);
        }
    }
    
    protected Integer sendComponent(BaseComponent component) throws TCAPSendException, TCAPException {
    	if(component instanceof Invoke) {
    		Invoke invoke=(Invoke)component;
        	if(invoke.getInvokeId()==null)
        		invoke.setInvokeId(getNewInvokeId());		
    	}
    	
		this.scheduledComponentList.add(component);
		
		// check if its taken!
        int invokeIndex = DialogImpl.getIndexFromInvokeId(component.getInvokeId());
        if (invokeExists(invokeIndex)) {
            throw new TCAPSendException("There is already operation with such invoke id!");
        }

        if(component instanceof Invoke) {
        	Invoke invoke=(Invoke)component;
        	InvokeWrapper wrapper=new InvokeWrapper(invoke.getOperationCode(), this, component.getInvokeId(), provider, null);
        	wrapper.setState(OperationState.Pending);        	
        	this.scheduledOperations[invokeIndex]=wrapper;
        	
	        // if the Invoke timeout value has not be reset by TCAP-User
	        // for this invocation we are setting it to the the TCAP stack
	        // default value
	        if (wrapper.getTimeout() == TCAPStackImpl._EMPTY_INVOKE_TIMEOUT)
	        	wrapper.setTimeout(this.provider.getStack().getInvokeTimeout());
        }
        
        return component.getInvokeId();
    }
    
    public Integer sendData(Integer invokeId,Integer linkedId,InvokeClass invokeClass,Long customTimeout,OperationCode operationCode,Object param,Boolean isRequest,Boolean isLastResponse) throws TCAPSendException, TCAPException {
    	if(isRequest!=null && isRequest) {
    		Invoke invoke;
    		if(invokeId==null)
    			invokeId=getNewInvokeId();
    		
    		InvokeWrapper invokeWrapper=new InvokeWrapper(operationCode, this, invokeId, provider, invokeClass);
    		if(customTimeout!=null)
    			invokeWrapper.setTimeout(customTimeout);
    		
    		invoke=TcapFactory.createComponentInvoke();
    		if(operationCode!=null && operationCode.getLocalOperationCode()!=null)
    			invoke.setOperationCode(operationCode.getLocalOperationCode());
    		else if(operationCode!=null && operationCode.getGlobalOperationCode()!=null)
    			invoke.setOperationCode(operationCode.getGlobalOperationCode());
    		
    		if(param!=null)
    			invoke.setParameter(param);
    		
    		invoke.setInvokeId(invokeId);
    		
    		if(linkedId!=null)
    			invoke.setLinkedId(linkedId);
    		
    		this.scheduledComponentList.add(invoke);
    		
    		// check if its taken!
            int invokeIndex = DialogImpl.getIndexFromInvokeId(invoke.getInvokeId());
            if (invokeExists(invokeIndex)) {
                throw new TCAPSendException("There is already operation with such invoke id!");
            }

            invokeWrapper.setState(OperationState.Pending);
            this.scheduledOperations[invokeIndex]=invokeWrapper;
        	
            // if the Invoke timeout value has not be reset by TCAP-User
            // for this invocation we are setting it to the the TCAP stack
            // default value
            if (invokeWrapper.getTimeout() == TCAPStackImpl._EMPTY_INVOKE_TIMEOUT)
            	invokeWrapper.setTimeout(this.provider.getStack().getInvokeTimeout());
    	} else if(isLastResponse!=null && isLastResponse) {
    		ReturnResultLast returnResultLast=TcapFactory.createComponentReturnResultLast();
    		returnResultLast.setInvokeId(invokeId);
    		
    		if(operationCode!=null && operationCode.getLocalOperationCode()!=null)
    			returnResultLast.setOperationCode(operationCode.getLocalOperationCode());
    		else if(operationCode!=null && operationCode.getGlobalOperationCode()!=null)
    			returnResultLast.setOperationCode(operationCode.getGlobalOperationCode());
    		
    		if(param!=null)
    			returnResultLast.setParameter(param);
    		this.scheduledComponentList.add(returnResultLast);
    		
    		if(returnResultLast.getInvokeId()!=null)
        		this.removeIncomingInvokeId(returnResultLast.getInvokeId());
    	} else {
    		ReturnResult returnResult=TcapFactory.createComponentReturnResult();
    		returnResult.setInvokeId(invokeId);
    		
    		if(operationCode!=null && operationCode.getLocalOperationCode()!=null)
    			returnResult.setOperationCode(operationCode.getLocalOperationCode());
    		else if(operationCode!=null && operationCode.getGlobalOperationCode()!=null)
    			returnResult.setOperationCode(operationCode.getGlobalOperationCode());
    		
    		if(param!=null)
    			returnResult.setParameter(param);    		
    		this.scheduledComponentList.add(returnResult);
    	}
    	
        return invokeId;
    }
    
    public void sendReject(Integer invokeId,Problem problem) throws TCAPSendException {
    	Reject reject=TcapFactory.createComponentReject();
    	reject.setInvokeId(invokeId);
    	
    	try {
	    	if(problem!=null && problem.getGeneralProblemType()!=null)
	    		reject.setProblem(problem.getGeneralProblemType());
	    	else if(problem!=null && problem.getInvokeProblemType()!=null)
	    		reject.setProblem(problem.getInvokeProblemType());
	    	else if(problem!=null && problem.getReturnErrorProblemType()!=null)
	    		reject.setProblem(problem.getReturnErrorProblemType());
	    	else if(problem!=null && problem.getReturnResultProblemType()!=null)
	    		reject.setProblem(problem.getReturnResultProblemType());
    	}
    	catch(ParseException ex) {
    		throw new TCAPSendException("problem can not be parsed");
    	}
    	
    	reject.setLocalOriginated(true);
    	
    	if(reject.getInvokeId()!=null)
    		this.removeIncomingInvokeId(reject.getInvokeId());            
            
        this.scheduledComponentList.add(reject);
    }
    
    public void sendError(Integer invokeId,ErrorCode errorCode,Object param) throws TCAPSendException {
    	ReturnError error=TcapFactory.createComponentReturnError();
    	
    	if(errorCode!=null && errorCode.getLocalErrorCode()!=null)
    		error.setErrorCode(errorCode.getLocalErrorCode());
    	else if(errorCode!=null && errorCode.getGlobalErrorCode()!=null)
    		error.setErrorCode(errorCode.getGlobalErrorCode());
    	
    	error.setInvokeId(invokeId);
    	error.setParameter(param);
    	
    	if(error.getInvokeId()!=null)
    		this.removeIncomingInvokeId(error.getInvokeId());            
            
        this.scheduledComponentList.add(error);
    }

    public void processInvokeWithoutAnswer(Integer invokeId) {
        this.removeIncomingInvokeId(invokeId);
    }

    private void prepareComponents(List<BaseComponent> res) {
    	int index = 0;
        while (this.scheduledComponentList.size() > index) {
        	BaseComponent cr = this.scheduledComponentList.get(index);
        	ComponentType ct=ComponentType.Invoke;
            if(cr instanceof ReturnError)
            	ct=ComponentType.ReturnError;
            else if(cr instanceof ReturnResult)
            	ct=ComponentType.ReturnResult;
            else if(cr instanceof ReturnResultLast)
            	ct=ComponentType.ReturnResultLast;
            else if(cr instanceof Reject)
            	ct=ComponentType.Reject;
            
        	provider.getStack().newComponentSent(ct.name(), this.networkId);
            
            if (cr instanceof Invoke) {
            	int invokeIndex = DialogImpl.getIndexFromInvokeId(cr.getInvokeId());
            	InvokeWrapper pendingWrapper = this.scheduledOperations[invokeIndex];
                    	    
            	if(pendingWrapper!=null) {
            		setInvokeByIndex(getIndexFromInvokeId(cr.getInvokeId()), pendingWrapper);            		
            		pendingWrapper.setState(OperationState.Sent);
            		this.scheduledOperations[invokeIndex]=null;
            	}
            }
            else if(cr instanceof Reject) {
            	Reject reject=(Reject)cr;
            	if(reject.getProblem()!=null) {
            		try {
	            		if(reject.getProblem().getGeneralProblemType()!=null)
	            			provider.getStack().newRejectSent(reject.getProblem().getGeneralProblemType().name(), this.networkId);
	            		else if(reject.getProblem().getInvokeProblemType()!=null)
	            			provider.getStack().newRejectSent(reject.getProblem().getInvokeProblemType().name(), this.networkId);
	            		else if(reject.getProblem().getReturnErrorProblemType()!=null)
	            			provider.getStack().newRejectSent(reject.getProblem().getReturnErrorProblemType().name(), this.networkId);
	            		else if(reject.getProblem().getReturnResultProblemType()!=null)
	            			provider.getStack().newRejectSent(reject.getProblem().getReturnResultProblemType().name(), this.networkId);
            		}
            		catch(ParseException ex) {
            			
            		}
            	}
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
    void setRemoteTransactionId(Long remoteTransactionId) {
        this.remoteTransactionIdObject = remoteTransactionId;        
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

    void processUni(TCUniMessage msg, SccpAddress localAddress, SccpAddress remoteAddress, ByteBuf data) {
        try {
            this.setRemoteAddress(remoteAddress);
            this.setLocalAddress(localAddress);

            // no dialog portion!
            // convert to indications
            TCUniIndication tcUniIndication = ((DialogPrimitiveFactoryImpl) this.provider
                    .getDialogPrimitiveFactory()).createUniIndication(this, data);

            tcUniIndication.setDestinationAddress(localAddress);
            tcUniIndication.setOriginatingAddress(remoteAddress);
            // now comps
            List<BaseComponent> comps = msg.getComponents();
            tcUniIndication.setComponents(comps);

            if (msg.getDialogPortion() != null) {
                // it should be dialog req?
                DialogPortion dp = msg.getDialogPortion();
                DialogRequestAPDU apdu = (DialogRequestAPDU) dp.getDialogAPDU();
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

    protected void processBegin(TCBeginMessage msg, SccpAddress localAddress, SccpAddress remoteAddress, ByteBuf data) {

        TCBeginIndication tcBeginIndication = null;
        // this is invoked ONLY for server.
        if (state.get() != TRPseudoState.Idle) {
            // should we terminate dialog here?
            if (logger.isErrorEnabled()) {
                logger.error("Received Begin primitive, but state is not: " + TRPseudoState.Idle + ". Dialog: " + this);
            }
            this.sendAbnormalDialog();
            return;
        }
        restartIdleTimer();

        // lets setup
        this.setRemoteAddress(remoteAddress);
        this.setLocalAddress(localAddress);
        this.setRemoteTransactionId(Utils.decodeTransactionId(msg.getOriginatingTransactionId(), isSwapTcapIdBytes));
        // convert to indications
        tcBeginIndication = ((DialogPrimitiveFactoryImpl) this.provider.getDialogPrimitiveFactory())
                .createBeginIndication(this, data);

        tcBeginIndication.setDestinationAddress(localAddress);
        tcBeginIndication.setOriginatingAddress(remoteAddress);

        // if APDU and context data present, lets store it
        DialogPortion dialogPortion = msg.getDialogPortion();

        if (dialogPortion != null && dialogPortion.getDialogAPDU()!=null) {
            // this should not be null....
            DialogAPDU apdu = dialogPortion.getDialogAPDU();
            if (apdu.getType() != DialogAPDUType.Request) {
                if (logger.isErrorEnabled()) {
                    logger.error("Received non-Request APDU: " + apdu.getType() + ". Dialog: " + this);
                }
                this.sendAbnormalDialog();
                return;
            }
            DialogRequestAPDU requestAPDU = (DialogRequestAPDU) apdu;
            this.lastACN = requestAPDU.getApplicationContextName();
            this.lastUI = requestAPDU.getUserInformation();
            tcBeginIndication.setApplicationContextName(this.lastACN);
            tcBeginIndication.setUserInformation(this.lastUI);
        }
        
        tcBeginIndication.setComponents(processOperationsState(msg.getComponents()));
        // change state - before we deliver
        this.setState(TRPseudoState.InitialReceived);

        // lets deliver to provider
        this.provider.deliver(this, tcBeginIndication);
    }

    protected void processContinue(TCContinueMessage msg, SccpAddress localAddress, SccpAddress remoteAddress, ByteBuf data) {

        TCContinueIndication tcContinueIndication = null;
        if (state.get() == TRPseudoState.InitialSent) {
            restartIdleTimer();
            tcContinueIndication = ((DialogPrimitiveFactoryImpl) this.provider
                    .getDialogPrimitiveFactory()).createContinueIndication(this, data);

            // in continue remote address MAY change be changed, so lets
            // update!
            this.setRemoteAddress(remoteAddress);
            this.setRemoteTransactionId(Utils.decodeTransactionId(msg.getOriginatingTransactionId(), isSwapTcapIdBytes));
            tcContinueIndication.setOriginatingAddress(remoteAddress);

            // here we will receive DialogResponse APDU - if request was
            // present!
            DialogPortion dialogPortion = msg.getDialogPortion();
            if (dialogPortion != null) {
                // this should not be null....
                DialogAPDU apdu = dialogPortion.getDialogAPDU();
                if (apdu.getType() != DialogAPDUType.Response) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Received non-Response APDU: " + apdu.getType() + ". Dialog: " + this);
                    }
                    this.sendAbnormalDialog();
                    return;
                }
                DialogResponseAPDU responseAPDU = (DialogResponseAPDU) apdu;
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
            tcContinueIndication.setComponents(processOperationsState(msg.getComponents()));
            // change state
            this.setState(TRPseudoState.Active);

            // lets deliver to provider
            this.provider.deliver(this, tcContinueIndication);

        } else if (state.get() == TRPseudoState.Active) {
            restartIdleTimer();
            // XXX: here NO APDU will be present, hence, no ACN/UI change
            tcContinueIndication = ((DialogPrimitiveFactoryImpl) this.provider
                    .getDialogPrimitiveFactory()).createContinueIndication(this, data);

            tcContinueIndication.setOriginatingAddress(remoteAddress);

            // now comps
            tcContinueIndication.setComponents(processOperationsState(msg.getComponents()));

            // lets deliver to provider
            this.provider.deliver(this, tcContinueIndication);

        } else {
            if (logger.isErrorEnabled()) {
                logger.error(
                        "Received Continue primitive, but state is not proper: " + this.state + ", Dialog: " + this);
            }
            this.sendAbnormalDialog();
            return;
        }
    }

    protected void processEnd(TCEndMessage msg, SccpAddress localAddress, SccpAddress remoteAddress, ByteBuf data) {
        TCEndIndication tcEndIndication = null;
        try {
        	restartIdleTimer();
            tcEndIndication = ((DialogPrimitiveFactoryImpl) this.provider
                    .getDialogPrimitiveFactory()).createEndIndication(this, data);

            if (state.get() == TRPseudoState.InitialSent) {
                // in end remote address MAY change be changed, so lets
                // update!
                this.setRemoteAddress(remoteAddress);
                tcEndIndication.setOriginatingAddress(remoteAddress);
            }

            DialogPortion dialogPortion = msg.getDialogPortion();
            if (dialogPortion != null) {
                DialogAPDU apdu = dialogPortion.getDialogAPDU();
                if (apdu.getType() != DialogAPDUType.Response) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Received non-Response APDU: " + apdu.getType() + ". Dialog: " + this);
                    }
                    // we do not send "this.sendAbnormalDialog()"
                    // because no sense to send an answer to TC-END
                    return;
                }
                DialogResponseAPDU responseAPDU = (DialogResponseAPDU) apdu;
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
            tcEndIndication.setComponents(processOperationsState(msg.getComponents()));

            this.provider.deliver(this, tcEndIndication);
        } finally {
            release();
        }
    }

    protected void processAbort(TCAbortMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2, ByteBuf data) {
    	try {
    		Boolean IsAareApdu = false;
            Boolean IsAbrtApdu = false;
            ApplicationContextName acn = null;
            ResultSourceDiagnostic resultSourceDiagnostic = null;
            AbortSourceType abrtSrc = null;
            UserInformation userInfo = null;
            DialogPortion dp = msg.getDialogPortion();
            if (dp != null) {
                DialogAPDU apdu = dp.getDialogAPDU();
                if (apdu != null && apdu.getType() == DialogAPDUType.Abort) {
                    IsAbrtApdu = true;
                    DialogAbortAPDU abortApdu = (DialogAbortAPDU) apdu;
                    try {
                    	abrtSrc = abortApdu.getAbortSource();
                    }
                    catch(ParseException ex) {
                    	
                    }
                    
                    userInfo = abortApdu.getUserInformation();
                }
                if (apdu != null && apdu.getType() == DialogAPDUType.Response) {
                    IsAareApdu = true;
                    DialogResponseAPDU resptApdu = (DialogResponseAPDU) apdu;
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
            	if ((abrtSrc != null && abrtSrc == AbortSourceType.Provider)) {
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
                TCPAbortIndication tcAbortIndication = ((DialogPrimitiveFactoryImpl) this.provider
                        .getDialogPrimitiveFactory()).createPAbortIndication(this, data);
                tcAbortIndication.setPAbortCause(type);

                this.provider.deliver(this, tcAbortIndication);

            } else {
                // its TC-U-Abort
                TCUserAbortIndication tcUAbortIndication = ((DialogPrimitiveFactoryImpl) this.provider
                        .getDialogPrimitiveFactory()).createUAbortIndication(this, data);
                if (IsAareApdu)
                    tcUAbortIndication.setAareApdu();
                if (IsAbrtApdu)
                    tcUAbortIndication.setAbrtApdu();
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
        TCPAbortIndication tcAbortIndication = null;
        try {
            if (this.remoteTransactionIdObject == null) {
                // no remoteTransactionId - we can not send back TC-ABORT
                return;
            }

            // sending to the remote side
            DialogPortion dp = TcapFactory.createDialogPortion();
            dp.setUnidirectional(false);

            DialogAbortAPDU dapdu = TcapFactory.createDialogAPDUAbort();

            dapdu.setAbortSource(AbortSourceType.Provider);
            dp.setDialogAPDU(dapdu);

            TCAbortMessage msg = TcapFactory.createTCAbortMessage();
            msg.setDestinationTransactionId(Utils.encodeTransactionId(this.remoteTransactionIdObject, isSwapTcapIdBytes));
            msg.setDialogPortion(dp);

            try {
                ByteBuf buffer=provider.encodeAbortMessage(msg);               
                provider.getStack().newMessageSent(msg.getName(),buffer.readableBytes(), this.networkId);
                provider.getStack().newAbortSent(PAbortCauseType.AbnormalDialogue.name(), this.networkId);
                this.provider.send(buffer, false, this.remoteAddress, this.localAddress, this.seqControl,
                        this.networkId, this.localSsn, this.remotePc);
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Failed to send message: ", e);
                }
            }

            // sending to the local side
            tcAbortIndication = ((DialogPrimitiveFactoryImpl) this.provider
                    .getDialogPrimitiveFactory()).createPAbortIndication(this, null);
            tcAbortIndication.setPAbortCause(PAbortCauseType.AbnormalDialogue);
            // tcAbortIndication.setLocalProviderOriginated(true);

            this.provider.deliver(this, tcAbortIndication);
        } finally {
            this.release();
            // this.scheduledComponentList.clear();
        }
    }

    public OperationCode getOperationCodeFromInvoke(Integer invokeId) {
    	InvokeWrapper invoke = null;
        if (invokeId != null) {
        	int index = getIndexFromInvokeId(invokeId);
            invoke = getInvokeByIndex(index);            
        }
        
        if(invoke!=null)
        	return invoke.getOperationCode();
        
        return null;
    }
    
    protected List<BaseComponent> processOperationsState(List<BaseComponent> components) {
        if(components==null) {
        	return null;
        }

        List<BaseComponent> resultingIndications = new ArrayList<BaseComponent>();
        for (BaseComponent ci : components) {
        	Integer invokeId;
            if (ci instanceof Invoke)
                invokeId = ((Invoke)ci).getLinkedId();
            else
                invokeId = ci.getInvokeId();
            
            InvokeWrapper wrapper = null;
            int index = 0;
            if (invokeId != null) {
                index = getIndexFromInvokeId(invokeId);
                wrapper = getInvokeByIndex(index);                
            }

            ComponentType ct=ComponentType.Invoke;
            if(ci instanceof ReturnError)
            	ct=ComponentType.ReturnError;
            else if(ci instanceof ReturnResult)
            	ct=ComponentType.ReturnResult;
            else if(ci instanceof ReturnResultLast)
            	ct=ComponentType.ReturnResultLast;
            else if(ci instanceof Reject)
            	ct=ComponentType.Reject;
            
            provider.getStack().newComponentReceived(ct.name(), this.networkId);
            switch (ct) {

                case Invoke:
                	if (invokeId != null && wrapper == null) {
                        logger.error(String.format("Rx : %s but no sent Invoke for linkedId exists", ci));

                        Problem p = TcapFactory.createProblem();
                        p.setInvokeProblemType(InvokeProblemType.UnrechognizedLinkedID);
                        this.addReject(resultingIndications, ((Invoke)ci).getInvokeId(), p);
                    } else {
                        if (wrapper != null) {
                        	((InvokeImpl)ci).setLinkedOperationCode(wrapper.getOperationCode());
                        }

                        if (!this.addIncomingInvokeId(((Invoke)ci).getInvokeId())) {
                            logger.error(String.format("Rx : %s but there is already Invoke with this invokeId", ci));

                            Problem p = TcapFactory.createProblem();
                            p.setInvokeProblemType(InvokeProblemType.DuplicateInvokeID);
                            this.addReject(resultingIndications, ((Invoke)ci).getInvokeId(), p);
                        } else {
                            resultingIndications.add(ci);
                        }
                    }
                    break;

                case ReturnResult:
                	if (wrapper == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        Problem p = TcapFactory.createProblem();
                        p.setReturnResultProblemType(ReturnResultProblemType.UnrecognizedInvokeID);
                        this.addReject(resultingIndications, ((ReturnResult)ci).getInvokeId(), p);
                    } else if (wrapper.getInvokeClass() != InvokeClass.Class1 && wrapper.getInvokeClass() != InvokeClass.Class3) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 3", ci));

                        Problem p = TcapFactory.createProblem();
                        p.setReturnResultProblemType(ReturnResultProblemType.ReturnResultUnexpected);
                        this.addReject(resultingIndications, ((ReturnResult)ci).getInvokeId(), p);
                    } else {
                        resultingIndications.add(ci);
                        ReturnResult rri = ((ReturnResult)ci);
                        if (rri.getOperationCode() == null) {
                        	if(wrapper.getOperationCode()!=null && wrapper.getOperationCode().getLocalOperationCode()!=null)
                        		rri.setOperationCode(wrapper.getOperationCode().getLocalOperationCode());
                        	else if(wrapper.getOperationCode()!=null && wrapper.getOperationCode().getGlobalOperationCode()!=null)
                        		rri.setOperationCode(wrapper.getOperationCode().getGlobalOperationCode());
                        }
                    }
                    break;

                case ReturnResultLast:

                    if (wrapper == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        Problem p = TcapFactory.createProblem();
                        p.setReturnResultProblemType(ReturnResultProblemType.UnrecognizedInvokeID);
                        this.addReject(resultingIndications, ((ReturnResultLast)ci).getInvokeId(), p);
                    } else if (wrapper.getInvokeClass() != InvokeClass.Class1 && wrapper.getInvokeClass() != InvokeClass.Class3) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 3", ci));

                        Problem p = TcapFactory.createProblem();
                        p.setReturnResultProblemType(ReturnResultProblemType.ReturnResultUnexpected);
                        this.addReject(resultingIndications, ((ReturnResultLast)ci).getInvokeId(), p);
                    } else {
                    	wrapper.onReturnResultLast();
                        if (wrapper.isSuccessReported()) {
                            resultingIndications.add(ci);
                        }
                        ReturnResultLast rri = ((ReturnResultLast)ci);
                        if(wrapper.getOperationCode()!=null && wrapper.getOperationCode().getLocalOperationCode()!=null)
                    		rri.setOperationCode(wrapper.getOperationCode().getLocalOperationCode());
                    	else if(wrapper.getOperationCode()!=null && wrapper.getOperationCode().getGlobalOperationCode()!=null)
                    		rri.setOperationCode(wrapper.getOperationCode().getGlobalOperationCode());                        
                    }
                    break;

                case ReturnError:
                    if (wrapper == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        Problem p = TcapFactory.createProblem();
                        p.setReturnErrorProblemType(ReturnErrorProblemType.UnrecognizedInvokeID);
                        this.addReject(resultingIndications, ((ReturnError)ci).getInvokeId(), p);
                    } else if (wrapper.getInvokeClass() != InvokeClass.Class1 && wrapper.getInvokeClass() != InvokeClass.Class2) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 2", ci));

                        Problem p = TcapFactory.createProblem();
                        p.setReturnErrorProblemType(ReturnErrorProblemType.ReturnErrorUnexpected);
                        this.addReject(resultingIndications, ((ReturnError)ci).getInvokeId(), p);
                    } else {
                    	wrapper.onError();
                        if (wrapper.isErrorReported()) {
                            resultingIndications.add(ci);
                        }
                    }
                    break;

                case Reject:
                    Reject rej = ((Reject)ci);
                    if (wrapper != null) {
                        // If the Reject Problem is the InvokeProblemType we
                        // should move the invoke to the idle state
                        Problem problem = rej.getProblem();
                        InvokeProblemType ipt=null;
                        try {
                        	ipt=problem.getInvokeProblemType();
                        }
                        catch(ParseException ex) {
                        	
                        }
                        
                        if (!rej.isLocalOriginated() && ipt != null)
                        	wrapper.onReject();
                    }
                    if (rej.isLocalOriginated() && this.isStructured()) {
                        try {
                            // this is a local originated Reject - we are rejecting an incoming component
                            // we need to send a Reject also to a peer
                            this.sendReject(rej.getInvokeId(), rej.getProblem());
                        } catch (TCAPSendException e) {
                            logger.error("TCAPSendException when sending Reject component : Dialog: " + this, e);
                        }
                    }
                    resultingIndications.add(ci);
                    
                    if(rej.getProblem()!=null) {
                		try {
    	            		if(rej.getProblem().getGeneralProblemType()!=null)
    	            			provider.getStack().newRejectReceived(rej.getProblem().getGeneralProblemType().name(), this.networkId);
    	            		else if(rej.getProblem().getInvokeProblemType()!=null)
    	            			provider.getStack().newRejectReceived(rej.getProblem().getInvokeProblemType().name(), this.networkId);
    	            		else if(rej.getProblem().getReturnErrorProblemType()!=null)
    	            			provider.getStack().newRejectReceived(rej.getProblem().getReturnErrorProblemType().name(), this.networkId);
    	            		else if(rej.getProblem().getReturnResultProblemType()!=null)
    	            			provider.getStack().newRejectReceived(rej.getProblem().getReturnResultProblemType().name(), this.networkId);
                		}
                		catch(ParseException ex) {
                			
                		}
                	}
                    
                    break;

                default:
                    resultingIndications.add(ci);
                    break;
            }
        }

        return resultingIndications;
    }

    private void addReject(List<BaseComponent> resultingIndications, Integer invokeId, Problem p) {
        try {
        	Reject reject=TcapFactory.createComponentReject();
        	reject.setLocalOriginated(true);
        	reject.setInvokeId(invokeId);
        	
        	try {
	        	if(p!=null && p.getGeneralProblemType()!=null)
	        		reject.setProblem(p.getGeneralProblemType());
	        	else if(p!=null && p.getInvokeProblemType()!=null)
	        		reject.setProblem(p.getInvokeProblemType());
	        	else if(p!=null && p.getReturnErrorProblemType()!=null)
	        		reject.setProblem(p.getReturnErrorProblemType());
	        	else if(p!=null && p.getReturnResultProblemType()!=null)
	        		reject.setProblem(p.getReturnResultProblemType());
        	}
        	catch(ParseException ex) {
        		//should not happen
        	}
        	
        	resultingIndications.add(reject);

            if (this.isStructured())
                this.sendReject(invokeId, p);
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

    protected void startIdleTimer() {
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

    protected void stopIdleTimer() {
        if (!this.structured)
            return;

        Future<?> original=this.idleTimerFuture.getAndSet(null);
        if (original != null) {
        	original.cancel(false);                
        }
    }

    protected void restartIdleTimer() {
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
                if (remoteTransactionIdObject != null && !getState().equals(TRPseudoState.Expunged))
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
    public void operationEnded(int invokeId) {
    	// this op died cause of timeout, TC-L-CANCEL!
        int index = getIndexFromInvokeId(invokeId);
        freeInvokeId(invokeId);
        setInvokeByIndex(index, null);
        // lets call listener
        // This is done actually with COmponentIndication ....
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog#operationEnded(
     * org.restcomm.protocols.ss7.tcap.tc.component.TCInvokeRequestImpl)
     */
    public void operationTimedOut(InvokeClass invokeClass, int invokeId) {
        // this op died cause of timeout, TC-L-CANCEL!
    	int index = getIndexFromInvokeId(invokeId);
    
        freeInvokeId(invokeId);
        setInvokeByIndex(index, null);
        // lets call listener
        this.provider.operationTimedOut(this, invokeId, invokeClass, this.networkId);
    }

    // TC-TIMER-RESET
    public void resetTimer(Integer invokeId) throws TCAPException {
    	int index = getIndexFromInvokeId(invokeId);
        InvokeWrapper wrapper = getInvokeByIndex(index);
        if (wrapper == null) {
            throw new TCAPException("No operation with this ID");
        }
        wrapper.startTimer();
    }

    protected Boolean invokeExists(int index) {
    	return this.sentOperations.containsKey(index);
    }
    
    protected Collection<InvokeWrapper> getAllInvokes() {
    	return this.sentOperations.values();
    }
    
    protected InvokeWrapper getInvokeByIndex(int index) {
    	return this.sentOperations.get(index);
    }
    
    protected void setInvokeByIndex(int index, InvokeWrapper invoke) {
    	if(invoke!=null)
    		this.sentOperations.put(index,invoke);
    	else
    		this.sentOperations.remove(index);
    }
    
    public TRPseudoState getState() {
        return this.state.get();
    }

    public Externalizable getUserObject() {
        return this.userObject;
    }

    public void setUserObject(Externalizable userObject) {
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