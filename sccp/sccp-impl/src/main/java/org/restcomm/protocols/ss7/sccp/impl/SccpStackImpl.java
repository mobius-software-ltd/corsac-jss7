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

import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.calculateLudtFieldsLengthWithoutData;
import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.calculateUdtFieldsLengthWithoutData;
import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.calculateXudtFieldsLengthWithoutData;
import static org.restcomm.protocols.ss7.sccp.impl.message.MessageUtil.calculateXudtFieldsLengthWithoutData2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusCause;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPart;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartBaseImpl;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartListener;
import org.restcomm.protocols.ss7.sccp.LongMessageRule;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.MaxConnectionCountReached;
import org.restcomm.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.restcomm.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.Rule;
import org.restcomm.protocols.ss7.sccp.SccpCongestionControlAlgo;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpConnectionState;
import org.restcomm.protocols.ss7.sccp.SccpManagementEventListener;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpResource;
import org.restcomm.protocols.ss7.sccp.SccpStack;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpAddressedMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpDataNoticeTemplateMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpSegmentableMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.restcomm.protocols.ss7.sccp.impl.router.RouterImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpMessage;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 *
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SccpStackImpl implements SccpStack, Mtp3UserPartListener {
    protected final Logger logger;

    /**
     * Interval in milliseconds in which new coming for an affected PC MTP-STATUS messages will be logged
     */
    private static final int STATUS_MSG_LOGGING_INTERVAL_MILLISEC_CONG = 10000;
    private static final int STATUS_MSG_LOGGING_INTERVAL_MILLISEC_UNAVAIL = 100;

     // If the XUDT message data length greater this value, segmentation is
    // needed
    protected int zMarginXudtMessage = 240;

    // sccp segmented message reassembling timeout
    protected int connEstTimerDelay = 15000;
    protected int iasTimerDelay = 7500 * 60;
    protected int iarTimerDelay = 16000 * 60;
    protected int relTimerDelay = 15000;
    protected int repeatRelTimerDelay = 15000;
    protected int intTimerDelay = 30000;
    protected int guardTimerDelay = 24000 * 60;
    protected int resetTimerDelay = 15000;
    protected int reassemblyTimerDelay = 15000;

    // Max available Sccp message data for all messages
    protected int maxDataMessage = 2560;
    // period logging warning in msec
    private int periodOfLogging = 60000;
    // remove PC from calledPartyAddress when sending to MTP3
    private boolean removeSpc = true;
    // respect point-code for outgoing messages
    //https://github.com/RestComm/jss7/issues/13
    private boolean respectPc = false;
    // can relay when relay with coupling condition is met
    private boolean canRelay = false;
    // min (starting) SST sending interval (millisec)
    protected int sstTimerDuration_Min = 10000;
    // max (after increasing) SST sending interval (millisec)
    protected int sstTimerDuration_Max = 600000;
    // multiplicator of SST sending interval (next interval will be greater the
    // current by sstTimerDuration_IncreaseFactor)
    // TODO: make it configurable
    protected double sstTimerDuration_IncreaseFactor = 1.5;
    // Which SCCP protocol version stack processes (ITU / ANSI)
    private SccpProtocolVersion sccpProtocolVersion = SccpProtocolVersion.ITU;

    // SCCP congestion control - the count of levels for restriction level - RLM
    protected int congControl_N = 8;
    // SCCP congestion control - the count of sublevels for restriction level - RSLM
    protected int congControl_M = 4;
    // Timer Ta value - started at next MTP-STATUS(cong) primitive coming; during this timer no more MTP-STATUS(cong) are
    // accepted
    protected int congControl_TIMER_A = 400; // 200
    // Timer Td value - started after last MTP-STATUS(cong) primitive coming; after end of this timer (without new coming
    // MTP-STATUS(cong)) RSLM will be reduced
    protected int congControl_TIMER_D = 2000; // 2000
    // sccp congestion control
    // international: international algorithm - only one level is provided by MTP3 level (in MTP-STATUS primitive). Each
    // MTP-STATUS increases N / M levels
    // levelDepended: MTP3 level (MTP-STATUS primitive) provides 3 levels of a congestion (1-3) and SCCP congestion will
    // increase to the
    // next level after MTP-STATUS next level increase (MTP-STATUS 1 to N up to 3, MTP-STATUS 2 to N up to 5, MTP-STATUS 3
    // to N up to 7)
    protected SccpCongestionControlAlgo congControl_Algo = SccpCongestionControlAlgo.international;
    // if true outgoing SCCP messages will be blocked (depending on message type, UDP messages from level N=6)
    protected boolean congControl_blockingOutgoungSccpMessages = false;

    protected int connectionHandlingThreadCount = 4;
    
    protected volatile State state = State.IDLE;

    // provider ref, this can be real provider or pipe, for tests.
    protected SccpProviderImpl sccpProvider;

    protected RouterImpl router;
    protected SccpResourceImpl sccpResource;

    protected MessageFactoryImpl messageFactory;

    protected SccpManagement sccpManagement;
    protected SccpRoutingControl sccpRoutingControl;

    protected ConcurrentHashMap<Integer, SccpConnection> connections = new ConcurrentHashMap<Integer, SccpConnection>();

    protected int referenceNumberCounterMax = 0xffffff;

    protected ConcurrentHashMap<Integer, Mtp3UserPart> mtp3UserParts = new ConcurrentHashMap<Integer, Mtp3UserPart>();
    protected ConcurrentHashMap<MessageReassemblyProcess, SccpSegmentableMessageImpl> reassemplyCache = new ConcurrentHashMap<MessageReassemblyProcess, SccpSegmentableMessageImpl>();

    protected ScheduledExecutorService connectionExecutors;
    
    public static final int slsFilter = 0x0f;
    protected int[] slsTable = null;

    // protected int localSpc;
    // protected int ni = 2;

    protected final String name;

    protected boolean rspProhibitedByDefault;
    protected boolean useCopy = false;
    
    private volatile AtomicInteger segmentationLocalRef = new AtomicInteger(0);
    private volatile AtomicInteger slsCounter = new AtomicInteger(0);
    private volatile AtomicBoolean selectorCounter = new AtomicBoolean(false);
    protected volatile AtomicInteger referenceNumberCounter = new AtomicInteger(0);

    private ConcurrentHashMap<Integer, Date> lastCongNotice = new ConcurrentHashMap<Integer, Date>();
    private ConcurrentHashMap<Integer, Date> lastUserPartUnavailNotice = new ConcurrentHashMap<Integer, Date>();

    private ConcurrentHashMap<String, AtomicLong> messagesSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> messagesReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> bytesSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> bytesReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<Integer, ConcurrentHashMap<String, AtomicLong>> messagesSentByTypeAndNetwork=new ConcurrentHashMap<Integer, ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer, ConcurrentHashMap<String, AtomicLong>> messagesReceivedByTypeAndNetwork=new ConcurrentHashMap<Integer, ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer, ConcurrentHashMap<String, AtomicLong>> bytesSentByTypeAndNetwork=new ConcurrentHashMap<Integer, ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer, ConcurrentHashMap<String, AtomicLong>> bytesReceivedByTypeAndNetwork=new ConcurrentHashMap<Integer, ConcurrentHashMap<String, AtomicLong>>();
        
    public static List<String> allMessageTypes=Arrays.asList(new String[] {SccpMessageImpl.MESSAGE_NAME_OTHER,
    		SccpMessageImpl.MESSAGE_NAME_CR, SccpMessageImpl.MESSAGE_NAME_CC, SccpMessageImpl.MESSAGE_NAME_CREF,
    		SccpMessageImpl.MESSAGE_NAME_RLSD, SccpMessageImpl.MESSAGE_NAME_RLC, SccpMessageImpl.MESSAGE_NAME_DT1,
    		SccpMessageImpl.MESSAGE_NAME_DT2, SccpMessageImpl.MESSAGE_NAME_AK, SccpMessageImpl.MESSAGE_NAME_RSR,
    		SccpMessageImpl.MESSAGE_NAME_RSC, SccpMessageImpl.MESSAGE_NAME_ERR, SccpMessageImpl.MESSAGE_NAME_IT,
    		SccpMessageImpl.MESSAGE_NAME_UDT, SccpMessageImpl.MESSAGE_NAME_UDTS, SccpMessageImpl.MESSAGE_NAME_XUDT,
    		SccpMessageImpl.MESSAGE_NAME_XUDTS, SccpMessageImpl.MESSAGE_NAME_LUDT,SccpMessageImpl.MESSAGE_NAME_LUDTS });
    
    public SccpStackImpl(String name,Boolean useBuffersCopy) {
    	this(name);
    	if(useBuffersCopy!=null)
    		this.useCopy = useBuffersCopy;
    }
    /*
     * For non-connection oriented protocol class usage
     */
    public SccpStackImpl(String name) {    
        this.name = name;
        this.logger = LogManager.getLogger(SccpStackImpl.class.getCanonicalName() + "-" + this.name);

        this.messageFactory = new MessageFactoryImpl(this);
        this.sccpProvider = new SccpProviderImpl(this);

        this.state = State.CONFIGURED;
        
        for(String currType:allMessageTypes) {
        	messagesSentByType.put(currType, new AtomicLong(0L));
        	messagesReceivedByType.put(currType, new AtomicLong(0L));
        	bytesSentByType.put(currType, new AtomicLong(0L));
        	bytesReceivedByType.put(currType, new AtomicLong(0L));
        }
    }

    public String getName() {
        return this.name;
    }

    public void setRspProhibitedByDefault(boolean rspProhibitedByDefault) {
        this.rspProhibitedByDefault = rspProhibitedByDefault;
    }

    public boolean isRspProhibitedByDefault() {
        return rspProhibitedByDefault;
    }

    public SccpProvider getSccpProvider() {
        return sccpProvider;
    }

    public Map<Integer, Mtp3UserPart> getMtp3UserParts() {
        return mtp3UserParts;
    }

    public void setMtp3UserParts(ConcurrentHashMap<Integer, Mtp3UserPart> mtp3UserPartsTemp) {
        if (mtp3UserPartsTemp != null) {
        	this.mtp3UserParts=mtp3UserPartsTemp;
        }
    }

    public Mtp3UserPart getMtp3UserPart(int id) {
        return mtp3UserParts.get(id);
    }

    public void setMtp3UserPart(int id, Mtp3UserPart mtp3UserPart) {
        if (mtp3UserPart == null) {
            this.removeMtp3UserPart(id);
        } else {
        	this.mtp3UserParts.put(id, mtp3UserPart);
        }
    }

    public void removeMtp3UserPart(int id) {
    	this.mtp3UserParts.remove(id);
    }

    public void setRemoveSpc(boolean removeSpc) throws Exception {
        if (!this.isStarted())
            throw new Exception("RemoveSpc parameter can be updated only when SCCP stack is running");

        this.removeSpc = removeSpc;
    }

    public void setRespectPc(boolean respectPc) throws Exception {
        if (!this.isStarted())
            throw new Exception("RespectPc parameter can be updated only when SCCP stack is running");

        this.respectPc = respectPc;
    }

    public void setCanRelay(boolean canRelay) throws Exception {
        if (!this.isStarted())
            throw new Exception("CanRelay parameter can be updated only when SCCP stack is running");

        this.canRelay = canRelay;
    }

    public void setSccpProtocolVersion(SccpProtocolVersion sccpProtocolVersion) throws Exception {
        if (!this.isStarted())
            throw new Exception("SccpProtocolVersion parameter can be updated only when SCCP stack is running");

        if (sccpProtocolVersion != null)
            this.sccpProtocolVersion = sccpProtocolVersion;
    }

    public int getConnectionHandlingThreadsCount() {
        return this.connectionHandlingThreadCount;
    }

    public void setConnectionHandlingThreadsCount(int deliveryMessageThreadCount) throws Exception {
        if (this.isStarted())
            throw new Exception("DeliveryMessageThreadCount parameter can be updated only when SCCP stack is NOT running");

        if (deliveryMessageThreadCount > 0 && deliveryMessageThreadCount <= 100)
            this.connectionHandlingThreadCount = deliveryMessageThreadCount;
    }

    public void setSstTimerDuration_Min(int sstTimerDuration_Min) throws Exception {
        if (!this.isStarted())
            throw new Exception("SstTimerDuration_Min parameter can be updated only when SCCP stack is running");

        // 5-10 seconds
        if (sstTimerDuration_Min < 5000)
            sstTimerDuration_Min = 5000;
        if (sstTimerDuration_Min > 10000)
            sstTimerDuration_Min = 10000;
        this.sstTimerDuration_Min = sstTimerDuration_Min;
    }

    public void setSstTimerDuration_Max(int sstTimerDuration_Max) throws Exception {
        if (!this.isStarted())
            throw new Exception("SstTimerDuration_Max parameter can be updated only when SCCP stack is running");

        // 10-20 minutes
        if (sstTimerDuration_Max < 600000)
            sstTimerDuration_Max = 600000;
        if (sstTimerDuration_Max > 1200000)
            sstTimerDuration_Max = 1200000;
        this.sstTimerDuration_Max = sstTimerDuration_Max;
    }

    public void setSstTimerDuration_IncreaseFactor(double sstTimerDuration_IncreaseFactor) throws Exception {
        if (!this.isStarted())
            throw new Exception("SstTimerDuration_IncreaseFactor parameter can be updated only when SCCP stack is running");

        // acceptable factor from 1 to 4
        if (sstTimerDuration_IncreaseFactor < 1)
            sstTimerDuration_IncreaseFactor = 1;
        if (sstTimerDuration_IncreaseFactor > 4)
            sstTimerDuration_IncreaseFactor = 4;
        this.sstTimerDuration_IncreaseFactor = sstTimerDuration_IncreaseFactor;
    }

    public boolean isRemoveSpc() {
        return this.removeSpc;
    }

    public boolean isRespectPc() {
        return this.respectPc;
    }

    public boolean isCanRelay() {
        return this.canRelay;
    }

    public SccpProtocolVersion getSccpProtocolVersion() {
        return this.sccpProtocolVersion;
    }

    public int getSstTimerDuration_Min() {
        return this.sstTimerDuration_Min;
    }

    public int getSstTimerDuration_Max() {
        return this.sstTimerDuration_Max;
    }

    public double getSstTimerDuration_IncreaseFactor() {
        return this.sstTimerDuration_IncreaseFactor;
    }

    public int getZMarginXudtMessage() {
        return zMarginXudtMessage;
    }

    public void setZMarginXudtMessage(int zMarginXudtMessage) throws Exception {
        if (!this.isStarted())
            throw new Exception("ZMarginXudtMessage parameter can be updated only when SCCP stack is running");

        // value from 160 to 255 bytes
        if (zMarginXudtMessage < 160)
            zMarginXudtMessage = 160;
        if (zMarginXudtMessage > 255)
            zMarginXudtMessage = 255;
        this.zMarginXudtMessage = zMarginXudtMessage;
    }

    public int getMaxDataMessage() {
        return maxDataMessage;
    }

    public void setMaxDataMessage(int maxDataMessage) throws Exception {
        if (!this.isStarted())
            throw new Exception("MaxDataMessage parameter can be updated only when SCCP stack is running");

        // from 2560 to 3952 bytes
        if (maxDataMessage < 2560)
            maxDataMessage = 2560;
        if (maxDataMessage > 3952)
            maxDataMessage = 3952;
        this.maxDataMessage = maxDataMessage;
    }

    public int getConnEstTimerDelay() {
        return this.connEstTimerDelay;
    }

    public void setConnEstTimerDelay(int connEstTimerDelay) throws Exception {
        if (!this.isStarted())
            throw new Exception("ConnEstTimerDelay parameter can be updated only when SCCP stack is running");

        // from 1 to 2 minutes
        if (connEstTimerDelay < 60000)
            connEstTimerDelay = 60000;
        if (connEstTimerDelay > 120000)
            connEstTimerDelay = 120000;
        this.connEstTimerDelay = connEstTimerDelay;
    }

    public int getIasTimerDelay() {
        return this.iasTimerDelay;
    }

    public void setIasTimerDelay(int iasTimerDelay) throws Exception {
        if (!this.isStarted())
            throw new Exception("IasTimerDelay parameter can be updated only when SCCP stack is running");

        // from 5 to 10 minutes
        if (iasTimerDelay < 300000)
            iasTimerDelay = 300000;
        if (iasTimerDelay > 600000)
            iasTimerDelay = 600000;
        this.iasTimerDelay = iasTimerDelay;
    }

    public int getIarTimerDelay() {
        return this.iarTimerDelay;
    }

    public void setIarTimerDelay(int iarTimerDelay) throws Exception {
        this.iarTimerDelay = iarTimerDelay;

        if (!this.isStarted())
            throw new Exception("IarTimerDelay parameter can be updated only when SCCP stack is running");

        // from 11 to 21 minutes
        if (iarTimerDelay < 660000)
            iarTimerDelay = 660000;
        if (iarTimerDelay > 1260000)
            iarTimerDelay = 1260000;
        this.iarTimerDelay = iarTimerDelay;
    }

    public int getRelTimerDelay() {
        return this.relTimerDelay;
    }

    public void setRelTimerDelay(int relTimerDelay) throws Exception {
        if (!this.isStarted())
            throw new Exception("RelTimerDelay parameter can be updated only when SCCP stack is running");

        // from 10 to 20 seconds
        if (relTimerDelay < 10000)
            relTimerDelay = 10000;
        if (relTimerDelay > 20000)
            relTimerDelay = 20000;
        this.relTimerDelay = relTimerDelay;
    }

    public int getRepeatRelTimerDelay() {
        return this.repeatRelTimerDelay;
    }

    public void setRepeatRelTimerDelay(int repeatRelTimerDelay) throws Exception {
        if (!this.isStarted())
            throw new Exception("RepeatRelTimerDelay parameter can be updated only when SCCP stack is running");

        // from 10 to 20 seconds
        if (repeatRelTimerDelay < 10000)
            repeatRelTimerDelay = 10000;
        if (repeatRelTimerDelay > 20000)
            repeatRelTimerDelay = 20000;
        this.repeatRelTimerDelay = repeatRelTimerDelay;
    }

    public int getIntTimerDelay() {
        return this.intTimerDelay;
    }

    public void setIntTimerDelay(int intTimerDelay) throws Exception {
        if (!this.isStarted())
            throw new Exception("IntTimerDelay parameter can be updated only when SCCP stack is running");

        // extending to 1 minute
        if (intTimerDelay < 0)
            intTimerDelay = 0;
        if (intTimerDelay > 60000)
            intTimerDelay = 60000;
        this.intTimerDelay = intTimerDelay;
    }

    public int getGuardTimerDelay() {
        return this.guardTimerDelay;
    }

    public void setGuardTimerDelay(int guardTimerDelay) throws Exception {
        if (!this.isStarted())
            throw new Exception("GuardTimerDelay parameter can be updated only when SCCP stack is running");

        // from 23 to 25 minutes
        if (guardTimerDelay < 1380000)
            guardTimerDelay = 1380000;
        if (guardTimerDelay > 1500000)
            guardTimerDelay = 1500000;
        this.guardTimerDelay = guardTimerDelay;
    }

    public int getResetTimerDelay() {
        return this.resetTimerDelay;
    }

    public void setResetTimerDelay(int resetTimerDelay) throws Exception {
        if (!this.isStarted())
            throw new Exception("ResetTimerDelay parameter can be updated only when SCCP stack is running");

        // from 10 to 20 seconds
        if (resetTimerDelay < 10000)
            resetTimerDelay = 10000;
        if (resetTimerDelay > 20000)
            resetTimerDelay = 20000;
        this.resetTimerDelay = resetTimerDelay;
    }

    public int getPeriodOfLogging() {
        return periodOfLogging;
    }

    public void setPeriodOfLogging(int periodOfLogging) throws Exception {
        if (!this.isStarted())
            throw new Exception("periodOfLogging parameter can be updated only when SCCP stack is running");

        this.periodOfLogging = periodOfLogging;
    }

    public int getReassemblyTimerDelay() {
        return this.reassemblyTimerDelay;
    }

    public void setReassemblyTimerDelay(int reassemblyTimerDelay) throws Exception {
        if (!this.isStarted())
            throw new Exception("ReassemblyTimerDelay parameter can be updated only when SCCP stack is running");

        // from 10 to 20 seconds
        if (reassemblyTimerDelay < 10000)
            reassemblyTimerDelay = 10000;
        if (reassemblyTimerDelay > 20000)
            reassemblyTimerDelay = 20000;
        this.reassemblyTimerDelay = reassemblyTimerDelay;
    }

    public int newSegmentationLocalRef() {
        return segmentationLocalRef.incrementAndGet();
    }

    public int newSls() {
    	this.slsCounter.incrementAndGet();
    	this.slsCounter.compareAndSet(256, 0);
        return this.slsCounter.get();
    }

    public boolean newSelector() {
    	if(!this.selectorCounter.compareAndSet(false, true))
    		this.selectorCounter.set(true);
        
    	return this.selectorCounter.get();
    }

    protected void createSLSTable(int maxSls, int minimumBoundThread) {
        int stream = 0;
        for (int i = 0; i < maxSls; i++) {
            if (stream >= minimumBoundThread) {
                stream = 0;
            }
            slsTable[i] = stream++;
        }
    }

    public void start() throws IllegalStateException {
    	start(new SccpRoutingControl(sccpProvider, this));
    }
    
    public void start(SccpRoutingControl routingControl) throws IllegalStateException {
        logger.info("Starting ...");

        // FIXME: make this configurable
        // FIXME: move creation to constructor ?
        this.sccpManagement = new SccpManagement(name, sccpProvider, this);
        this.sccpRoutingControl = routingControl;
        
        this.sccpManagement.setSccpRoutingControl(sccpRoutingControl);
        this.sccpRoutingControl.setSccpManagement(sccpManagement);
        
        this.router = new RouterImpl(this.name, this);
        this.router.start();

        this.sccpResource = new SccpResourceImpl(this.name, this.rspProhibitedByDefault);
        this.sccpResource.start();

        logger.info("Starting routing engine...");
        this.sccpRoutingControl.start();
        logger.info("Starting management ...");
        this.sccpManagement.start();
        logger.info("Starting MSU handler...");

        this.connectionExecutors = Executors.newScheduledThreadPool(connectionHandlingThreadCount, new DefaultThreadFactory("SccpTransit-DeliveryExecutor"));
        
        // initiating of SCCP delivery executors
        // TODO: we do it for ITU standard, may be we may configure it for other standard's (different SLS count) maxSls and
        // slsFilter values initiating
        int maxSls = 16;
        this.slsTable = new int[maxSls];
        this.createSLSTable(maxSls, this.connectionHandlingThreadCount);
        
        Iterator<Mtp3UserPart> mtp3Iterator=this.mtp3UserParts.values().iterator();
        while(mtp3Iterator.hasNext()) {
            Mtp3UserPart mup = mtp3Iterator.next();
            mup.addMtp3UserPartListener(this);
        }

        Iterator<SccpManagementEventListener> iterator=this.sccpProvider.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            try {
            	iterator.next().onServiceStarted();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServiceStarted", ee);
            }
        }

        this.state = State.RUNNING;
    }

    public void stop() {
        logger.info("Stopping ...");
        // stateLock.lock();
        // try
        // {
        this.state = State.IDLE;
        // executor = null;
        //
        // layer3exec = null;

        Iterator<SccpManagementEventListener> iterator=this.sccpProvider.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            try {
            	iterator.next().onServiceStopped();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServiceStopped", ee);
            }
        }

        Iterator<Mtp3UserPart> mtp3Iterator=this.mtp3UserParts.values().iterator();
        while(mtp3Iterator.hasNext()) {
            Mtp3UserPart mup = mtp3Iterator.next();
            mup.removeMtp3UserPartListener(this);
        }

        logger.info("Stopping management...");
        this.sccpManagement.stop();
        logger.info("Stopping routing engine...");
        this.sccpRoutingControl.stop();
        logger.info("Stopping MSU handler...");

        this.sccpResource.stop();

        this.router.stop();

        reassemplyCache.clear();
        
        // }finally
        // {
        // stateLock.unlock();
        // }
    }

    public boolean isStarted() {
        return this.state == State.RUNNING;
    }

    public Router getRouter() {
        return this.router;
    }

    public SccpResource getSccpResource() {
        return sccpResource;
    }

    public SccpConnectionImpl getConnection(LocalReference number) {
        if (number == null) {
            logger.error("Reference number can't be null");
            throw new IllegalArgumentException("Reference number can't be null");
        }
        return (SccpConnectionImpl)connections.get(number.getValue());
    }

    public int getConnectionsNumber() {
        return connections.size();
    }

    protected enum State {
        IDLE, CONFIGURED, RUNNING;
    }

    public SccpConnectionImpl newConnection(int localSsn, ProtocolClass protocol) throws MaxConnectionCountReached {
        SccpConnectionImpl conn;
        Integer refNumber = newReferenceNumber();

        if (protocol.getProtocolClass() == 2) {
            conn = new SccpConnectionImpl(localSsn, new LocalReferenceImpl(refNumber), protocol, this, sccpRoutingControl);
        } else if (protocol.getProtocolClass() == 3) {
            conn = new SccpConnectionWithFlowControlImpl(localSsn, new LocalReferenceImpl(refNumber), protocol, this, sccpRoutingControl);
        } else {
            logger.error(String.format("Unsupported connection class %d", protocol.getProtocolClass()));
            throw new IllegalArgumentException();
        }

        connections.put(refNumber, conn);
        return conn;
    }

    public void removeConnection(LocalReference ref) {
        SccpConnectionImpl conn = (SccpConnectionImpl)connections.remove(ref.getValue());
        if (conn != null) {
            conn.stopTimers();
            conn.setState(SccpConnectionState.CLOSED);
        }
    }

    protected int newReferenceNumber() throws MaxConnectionCountReached {
    	referenceNumberCounter.incrementAndGet();
    	referenceNumberCounter.compareAndSet(referenceNumberCounterMax, 0);
    	
        // 0 and reference numbers values which are > 0
        if (connections.size() >= referenceNumberCounterMax + 1) {
            logger.error(String.format("Can't open more connections than %d", referenceNumberCounterMax + 1));
            throw new MaxConnectionCountReached(String.format("Can't open more connections than %d", referenceNumberCounterMax + 1));
        }

        while (connections.containsKey(referenceNumberCounter.get())) {
            referenceNumberCounter.incrementAndGet();
        }
        return referenceNumberCounter.get();
    }

    protected void send(SccpDataNoticeTemplateMessageImpl message) throws Exception {

        if (this.state != State.RUNNING) {
            logger.error("Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING");
            return;
        }

        if (message.getCalledPartyAddress() == null || message.getCallingPartyAddress() == null || message.getData() == null
                || message.getData().readableBytes() == 0) {
            logger.error("Message to send must has filled CalledPartyAddress, CallingPartyAddress and data fields");
            throw new IOException("Message to send must has filled CalledPartyAddress, CallingPartyAddress and data fields");
        }

        try {
            this.sccpRoutingControl.routeMssgFromSccpUser(message);
        } catch (Exception e) {
            // log here Exceptions from MTP3 level
            logger.error("IOException when sending the message to MTP3 level: " + e.getMessage(), e);
            throw e;
        }
    }

    protected int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId) {

        GlobalTitle gt = calledPartyAddress.getGlobalTitle();
        int dpc = calledPartyAddress.getSignalingPointCode();
        int ssn = calledPartyAddress.getSubsystemNumber();

        if (calledPartyAddress.getAddressIndicator().isPCPresent()) {
            if (this.router.spcIsLocal(dpc)) {
                if (ssn > 0) {
                    // local destination - unlimited length
                    return this.getMaxDataMessage();
                } else if (gt != null) {
                    return getMaxUserDataLengthForGT(calledPartyAddress, callingPartyAddress, msgNetworkId);
                } else {
                    return 0;
                }
            } else {
                return getMaxUserDataLengthForDpc(dpc, calledPartyAddress, callingPartyAddress);
            }
        } else {
            if (gt != null) {
                return getMaxUserDataLengthForGT(calledPartyAddress, callingPartyAddress, msgNetworkId);
            } else {
                return 0;
            }
        }
    }

    private int getMaxUserDataLengthForDpc(int dpc, SccpAddress calledPartyAddress, SccpAddress callingPartyAddress) {

        LongMessageRule lmr = this.router.findLongMessageRule(dpc);
        LongMessageRuleType lmrt = LongMessageRuleType.LONG_MESSAGE_FORBBIDEN;
        if (lmr != null)
            lmrt = lmr.getLongMessageRuleType();
        Mtp3ServiceAccessPoint sap = this.router.findMtp3ServiceAccessPoint(dpc, 0);
        if (sap == null) {
            return 0;
        }
        Mtp3UserPart mup = this.getMtp3UserPart(sap.getMtp3Id());
        if (mup == null) {
            return 0;
        }

        try {
            int fieldsLen = 0;
            ByteBuf cdp=Unpooled.buffer();
            ByteBuf cnp=Unpooled.buffer();
            ((SccpAddressImpl) calledPartyAddress).encode(cdp, isRemoveSpc(), this.getSccpProtocolVersion());
            ((SccpAddressImpl) callingPartyAddress).encode(cnp, isRemoveSpc(), this.getSccpProtocolVersion());
            switch (lmrt) {
                case LONG_MESSAGE_FORBBIDEN:
                    fieldsLen = calculateUdtFieldsLengthWithoutData(cdp.readableBytes(), cnp.readableBytes());
                    break;
                case LUDT_ENABLED:
                case LUDT_ENABLED_WITH_SEGMENTATION:
                    fieldsLen = calculateLudtFieldsLengthWithoutData(cdp.readableBytes(), cnp.readableBytes(), true, true);
                    break;
                case XUDT_ENABLED:
                    fieldsLen = calculateXudtFieldsLengthWithoutData(cdp.readableBytes(), cnp.readableBytes(), true, true);
                    int fieldsLen2 = calculateXudtFieldsLengthWithoutData2(cdp.readableBytes(), cnp.readableBytes());
                    if (fieldsLen > fieldsLen2)
                        fieldsLen = fieldsLen2;
                    break;
            }

            int availLen = mup.getMaxUserDataLength(dpc) - fieldsLen;
            if ((lmrt == LongMessageRuleType.LONG_MESSAGE_FORBBIDEN || lmrt == LongMessageRuleType.XUDT_ENABLED)
                    && availLen > 255)
                availLen = 255;
            if (lmrt == LongMessageRuleType.XUDT_ENABLED)
                availLen *= 16;
            if (availLen > this.getMaxDataMessage())
                availLen = this.getMaxDataMessage();
            return availLen;

        } catch (Exception e) {
            // this can not occur
            // dont be so sure!
            e.printStackTrace();
            return 0;
        }
    }

    private int getMaxUserDataLengthForGT(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId) {

        Rule rule = this.router.findRule(calledPartyAddress, callingPartyAddress, false, msgNetworkId);
        if (rule == null) {
            return 0;
        }
        SccpAddress translationAddressPri = this.router.getRoutingAddress(rule.getPrimaryAddressId());
        if (translationAddressPri == null) {
            return 0;
        }

        return getMaxUserDataLengthForDpc(translationAddressPri.getSignalingPointCode(), calledPartyAddress,
                callingPartyAddress);
    }

    protected void broadcastChangedSsnState(int affectedSsn, boolean inService) {
        this.sccpManagement.broadcastChangedSsnState(affectedSsn, inService);
    }

    public void removeAllResourses() {

        if (this.state != State.RUNNING) {
            return;
        }

        this.router.removeAllResourses();
        this.sccpResource.removeAllResourses();

        Iterator<SccpManagementEventListener> iterator=this.sccpProvider.managementEventListeners.values().iterator();
        while(iterator.hasNext()) {
            try {
            	iterator.next().onRemoveAllResources();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onRemoveAllResources", ee);
            }
        }
    }

    public void onMtp3PauseMessage(Mtp3PausePrimitive msg) {

        logger.warn(String.format("Rx : %s", msg));

        if (this.state != State.RUNNING) {
            logger.error("Cannot consume MTP3 PASUE message as SCCP stack is not RUNNING");
            return;
        }

        sccpManagement.handleMtp3Pause(msg.getAffectedDpc());
    }

    public void onMtp3ResumeMessage(Mtp3ResumePrimitive msg) {
        logger.warn(String.format("Rx : %s", msg));

        if (this.state != State.RUNNING) {
            logger.error("Cannot consume MTP3 RESUME message as SCCP stack is not RUNNING");
            return;
        }

        sccpManagement.handleMtp3Resume(msg.getAffectedDpc());
    }

    public void onMtp3StatusMessage(Mtp3StatusPrimitive msg) {
        int affectedDpc = msg.getAffectedDpc();

        // we are making of announcing of MTP-STATUS only each 10 seconds
        Date lastNotice;
        if (msg.getCause() == Mtp3StatusCause.SignallingNetworkCongested) {
            lastNotice = lastCongNotice.get(affectedDpc);
            if (lastNotice == null) {
                lastCongNotice.put(affectedDpc, new Date());
                logger.warn(String.format("Rx : %s  (one message in %d seconds)", msg, STATUS_MSG_LOGGING_INTERVAL_MILLISEC_UNAVAIL));
            } else {
                if (System.currentTimeMillis() - lastNotice.getTime() > STATUS_MSG_LOGGING_INTERVAL_MILLISEC_CONG) {
                    lastNotice.setTime(System.currentTimeMillis());
                    logger.warn(String.format("Rx : %s (one message in %d seconds)", msg, STATUS_MSG_LOGGING_INTERVAL_MILLISEC_UNAVAIL));
                }
            }
        } else {
            lastNotice = lastUserPartUnavailNotice.get(affectedDpc);
            if (lastNotice == null) {
                lastUserPartUnavailNotice.put(affectedDpc, new Date());
                logger.warn(String.format("Rx : %s (one message in %d seconds)", msg, STATUS_MSG_LOGGING_INTERVAL_MILLISEC_UNAVAIL));
            } else {
                if (System.currentTimeMillis() - lastNotice.getTime() > STATUS_MSG_LOGGING_INTERVAL_MILLISEC_UNAVAIL) {
                    lastNotice.setTime(System.currentTimeMillis());
                    logger.warn(String.format("Rx : %s (one message in %d seconds)", msg, STATUS_MSG_LOGGING_INTERVAL_MILLISEC_UNAVAIL));
                }
            }
        }

        if (this.state != State.RUNNING) {
            logger.error("Cannot consume MTP3 STATUS message as SCCP stack is not RUNNING");
            return;
        }

        sccpManagement.handleMtp3Status(msg.getCause(), affectedDpc, msg.getCongestionLevel());
    }

    @Override
    public void onMtp3EndCongestionMessage(Mtp3EndCongestionPrimitive msg) {
        int affectedDpc = msg.getAffectedDpc();

        logger.warn(String.format("Rx : %s", msg));

        sccpManagement.handleMtp3EndCongestion(affectedDpc);
    }

    public void onMtp3TransferMessage(Mtp3TransferPrimitive mtp3Msg) {

        if (this.state != State.RUNNING) {
            logger.error("Received MTP3TransferPrimitive from lower layer but SCCP stack is not RUNNING");
            return;
        }

        SccpMessageImpl msg = null;
        int dpc = mtp3Msg.getDpc();
        int opc = mtp3Msg.getOpc();

        try {
        	// decoding of a message
            ByteBuf data=mtp3Msg.getData();
            int bytes=data.readableBytes();
            int mt = data.readUnsignedByte();
            msg = ((MessageFactoryImpl) sccpProvider.getMessageFactory()).createMessage(mt, mtp3Msg.getOpc(), mtp3Msg.getDpc(), mtp3Msg.getSls(), data,
                    this.sccpProtocolVersion, 0);
            
            messagesReceivedByType.get(SccpMessageImpl.getName(msg.getType())).incrementAndGet();
        	bytesReceivedByType.get(SccpMessageImpl.getName(msg.getType())).addAndGet(bytes);
        	
            // checking if incoming dpc is local
            if (!this.router.spcIsLocal(dpc)) {

                // incoming dpc is not local - trying to find the target SAP and
                // send a message to MTP3 (MTP transit function)
                int sls = mtp3Msg.getSls();

                RemoteSignalingPointCode remoteSpc = this.getSccpResource().getRemoteSpcByPC(dpc);
                if (remoteSpc == null) {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String.format("Incoming Mtp3 Message for nonlocal dpc=%d. But RemoteSpc is not found", dpc));
                    }
                    return;
                }
                if (remoteSpc.isRemoteSpcProhibited()) {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String
                                .format("Incoming Mtp3 Message for nonlocal dpc=%d. But RemoteSpc is Prohibited", dpc));
                    }
                    // TODO: ***** SSP should we send SSP message to a peer ?
                    return;
                }
                if (remoteSpc.getCurrentRestrictionLevel() > 1) {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String
                                .format("Incoming Mtp3 Message for nonlocal dpc=%d. But RemoteSpc is Congested", dpc));
                    }
                    // TODO: ***** SSC should we send SSC message to a peer ?
                    return;
                }
                Mtp3ServiceAccessPoint sap2 = this.router.findMtp3ServiceAccessPoint(dpc, sls);
                if (sap2 == null) {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String.format("Incoming Mtp3 Message for nonlocal dpc=%d / sls=%d. But SAP is not found",
                                dpc, sls));
                    }
                    return;
                }
                Mtp3UserPart mup = this.getMtp3UserPart(sap2.getMtp3Id());
                if (mup == null) {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String.format(
                                "Incoming Mtp3 Message for nonlocal dpc=%d / sls=%d. no matching Mtp3UserPart found", dpc, sls));
                    }
                    return;
                }

                mup.sendMessage(mtp3Msg);
                return;
            }

            // process only SCCP messages
            if (mtp3Msg.getSi() != Mtp3UserPartBaseImpl._SI_SERVICE_SCCP) {
                logger.warn(String
                        .format("Received Mtp3TransferPrimitive from lower layer with Service Indicator=%d which is not SCCP. Dropping this message",
                                mtp3Msg.getSi()));
                return;
            }

            // finding sap and networkId for a message
            dpc = mtp3Msg.getDpc();
            opc = mtp3Msg.getOpc();
            String localGtDigits = null;
            if (msg instanceof SccpAddressedMessageImpl) {
                SccpAddressedMessageImpl msgAddr = (SccpAddressedMessageImpl) msg;
                SccpAddress addr = msgAddr.getCalledPartyAddress();
                if (addr != null) {
                    GlobalTitle gt = addr.getGlobalTitle();
                    if (gt != null) {
                        localGtDigits = gt.getDigits();
                    }
                }
            }
            Mtp3ServiceAccessPoint sap = this.router.findMtp3ServiceAccessPointForIncMes(dpc, opc, localGtDigits);
            int networkId = 0;
            if (sap == null) {
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format("Incoming Mtp3 Message for local address for localPC=%d, remotePC=%d, sls=%d. But SAP is not found for localPC", dpc, opc, mtp3Msg.getSls()));
                }
            } else {
                networkId = sap.getNetworkId();
            }
            msg.setNetworkId(networkId);
            Integer networkID=msg.getNetworkId();
         	ConcurrentHashMap<String, AtomicLong> messagesReceivedByTypeAndNetwork=this.messagesReceivedByTypeAndNetwork.get(networkID);
         	if(messagesReceivedByTypeAndNetwork==null) {
         		messagesReceivedByTypeAndNetwork=new ConcurrentHashMap<String, AtomicLong>();
         		for(String currType:allMessageTypes)
                	messagesReceivedByTypeAndNetwork.put(currType, new AtomicLong(0L));            	
                
         		ConcurrentHashMap<String, AtomicLong> oldValue=this.messagesReceivedByTypeAndNetwork.putIfAbsent(networkID, messagesReceivedByTypeAndNetwork);
         		if(oldValue!=null)
         			messagesReceivedByTypeAndNetwork=oldValue;     			
         	}
         	
         	ConcurrentHashMap<String, AtomicLong> bytesReceivedByTypeAndNetwork=this.bytesReceivedByTypeAndNetwork.get(networkID);
         	if(bytesReceivedByTypeAndNetwork==null) {
         		bytesReceivedByTypeAndNetwork=new ConcurrentHashMap<String, AtomicLong>();
         		for(String currType:allMessageTypes)
         			bytesReceivedByTypeAndNetwork.put(currType, new AtomicLong(0L));            	
                
         		ConcurrentHashMap<String, AtomicLong> oldValue=this.bytesReceivedByTypeAndNetwork.putIfAbsent(networkID, bytesReceivedByTypeAndNetwork);
         		if(oldValue!=null)
         			bytesReceivedByTypeAndNetwork=oldValue;     			
         	}
         	
         	messagesReceivedByTypeAndNetwork.get(SccpMessageImpl.getName(msg.getType())).incrementAndGet();
        	bytesReceivedByTypeAndNetwork.get(SccpMessageImpl.getName(msg.getType())).addAndGet(bytes);
        	
        	if (logger.isDebugEnabled()) {
                logger.debug(String.format("Rx : SCCP message from MTP %s", msg));
            }

            // when segmented messages - make a reassembly operation
            if (msg instanceof SccpSegmentableMessageImpl) {
            	SccpSegmentableMessageImpl sgmMsg = (SccpSegmentableMessageImpl) msg;
                SegmentationImpl segm = (SegmentationImpl) sgmMsg.getSegmentation();
                if (segm != null) {
                    // segmentation info is present - segmentation is possible
                    if (segm.isFirstSegIndication() && segm.getRemainingSegments() == 0) {                    	
                        // the single segment - no reassembly is needed
                    	//not need to change the ref count here
                        sgmMsg.setReceivedSingleSegment();
                    } else {
                        
                        // multiple segments - reassembly is needed
                        if (segm.isFirstSegIndication()) {
                            // first segment
                        	//incrementing the count for current segment
                        	ReferenceCountUtil.retain(sgmMsg.getData());
                            sgmMsg.setReceivedFirstSegment();
                            MessageReassemblyProcess msp = new MessageReassemblyProcess(segm.getSegmentationLocalRef(),
                                    sgmMsg.getCallingPartyAddress());
                            this.reassemplyCache.put(msp, sgmMsg);
                            sgmMsg.setMessageReassemblyProcess(msp);
                            msp.startTimer();
                            return;
                        } else {

                            // nonfirst segment
                            MessageReassemblyProcess msp = new MessageReassemblyProcess(segm.getSegmentationLocalRef(),
                                    sgmMsg.getCallingPartyAddress());
                            SccpSegmentableMessageImpl sgmMsgFst = null;
                            sgmMsgFst = this.reassemplyCache.get(msp);
                            
                            if (sgmMsgFst == null) {
                                // previous segments cache is not found -
                                // discard a segment                            	
                                if (logger.isWarnEnabled()) {
                                    logger.warn(String
                                            .format("Reassembly function failure: received a non first segment without the first segement having recieved. SccpMessageSegment=%s",
                                                    msg));
                                }
                                return;
                            }
                            if (sgmMsgFst.getRemainingSegments() - 1 != segm.getRemainingSegments()) {
                                // segments bad order
                            	 this.reassemplyCache.remove(msp);
                            	 //need to release buffers stored till now
                            	 MessageReassemblyProcess mspMain = sgmMsgFst.getMessageReassemblyProcess();
                                 if (mspMain != null)
                                     mspMain.stopTimer();
                                 
                                if (logger.isWarnEnabled()) {
                                    logger.warn(String
                                            .format("Reassembly function failure: when receiving a next segment message order is missing. SccpMessageSegment=%s",
                                                    msg));
                                }
                                this.sccpRoutingControl.sendSccpError(sgmMsgFst, ReturnCauseValue.CANNOT_REASEMBLE, null);
                                return;
                            }

                            //incrementing the count for current segment
                        	ReferenceCountUtil.retain(sgmMsg.getData());                            
                            if (sgmMsgFst.getRemainingSegments() == 1) {
                                // last segment
                            	MessageReassemblyProcess mspMain = sgmMsgFst.getMessageReassemblyProcess();
                                if (mspMain != null)
                                    mspMain.stopTimer();
                                this.reassemplyCache.remove(msp);
                                
                                if (sgmMsgFst.getRemainingSegments() != 1)
                                    return;

                                sgmMsgFst.setReceivedNextSegment(sgmMsg);
                                msg = sgmMsgFst;
                            } else {
                                // not last segment
                                sgmMsgFst.setReceivedNextSegment(sgmMsg);
                                return;
                            }
                        }
                    }
                }
            }

            if(useCopy && msg instanceof SccpSegmentableMessageImpl)
            	((SccpSegmentableMessageImpl)msg).copyData();
            	
            if (msg instanceof SccpAddressedMessageImpl) {
                // CR or connectionless messages
                SccpAddressedMessageImpl msgAddr = (SccpAddressedMessageImpl) msg;

                // adding OPC into CallingPartyAddress if it is absent there and
                // "RouteOnSsn"
                SccpAddress addr = msgAddr.getCallingPartyAddress();
                if (addr != null
                        && addr.getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
                    if (!addr.getAddressIndicator().isPCPresent()) {
                        msgAddr.setCallingPartyAddress(new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, msgAddr.getIncomingOpc(), addr
                                .getSubsystemNumber()));
                    }
                }

                sccpRoutingControl.routeMssgFromMtp(msgAddr);

            } else if (msg instanceof SccpConnMessage) {
                // non-addressed message processing (these are connected-oriented messages in the connected phase)
                sccpRoutingControl.routeMssgFromMtpConn((SccpConnMessage) msg);

            } else {
                logger.warn(String
                        .format("Rx SCCP message which is not instance of SccpAddressedMessage or SccpSegmentableMessage" +
                                " and doesn't implement SccpConnMessage. Will be dropped. Message=", msg));
            }
        } catch (Exception e) {
            logger.error("IOException while handling SCCP message: " + e.getMessage(), e);
        }
    }

    public class MessageReassemblyProcess implements Runnable {
        private int segmentationLocalRef;
        private SccpAddress callingPartyAddress;

        private Future<?> timer;

        public MessageReassemblyProcess(int segmentationLocalRef, SccpAddress callingPartyAddress) {
            this.segmentationLocalRef = segmentationLocalRef;
            this.callingPartyAddress = callingPartyAddress;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof MessageReassemblyProcess))
                return false;
            MessageReassemblyProcess x = (MessageReassemblyProcess) obj;
            if (this.segmentationLocalRef != x.segmentationLocalRef)
                return false;

            if (this.callingPartyAddress == null || x.callingPartyAddress == null)
                return false;

            return this.callingPartyAddress.equals(x.callingPartyAddress);
        }

        @Override
        public int hashCode() {
            return this.segmentationLocalRef;
        }

        public void startTimer() {
            this.timer = connectionExecutors.schedule(this, reassemblyTimerDelay, TimeUnit.MILLISECONDS);
        }

        public void stopTimer() {
            if (this.timer != null) {
                this.timer.cancel(false);
                this.timer = null;
            }
        }

        public void run() {
            SccpSegmentableMessageImpl msg = null;
            msg = reassemplyCache.remove(this);
            if (msg == null)
                return;

            msg.cancelSegmentation();

            try {
                sccpRoutingControl.sendSccpError(msg, ReturnCauseValue.CANNOT_REASEMBLE, null);
            } catch (Exception e) {
                logger.warn("IOException when sending an error message", e);
            }
        }
    }
    
    public void sendMessageToMTP(SccpMessage message,Mtp3UserPart mup,Mtp3TransferPrimitive mtp3Message) throws IOException {
    	messagesSentByType.get(SccpMessageImpl.getName(message.getType())).incrementAndGet();
     	bytesSentByType.get(SccpMessageImpl.getName(message.getType())).addAndGet(mtp3Message.getData().readableBytes());
     	
     	Integer networkID=message.getNetworkId();
     	ConcurrentHashMap<String, AtomicLong> messagesSentByTypeAndNetwork=this.messagesSentByTypeAndNetwork.get(networkID);
     	if(messagesSentByTypeAndNetwork==null) {
     		messagesSentByTypeAndNetwork=new ConcurrentHashMap<String, AtomicLong>();
     		for(String currType:allMessageTypes)
            	messagesSentByTypeAndNetwork.put(currType, new AtomicLong(0L));            	
            
     		ConcurrentHashMap<String, AtomicLong> oldValue=this.messagesSentByTypeAndNetwork.putIfAbsent(networkID, messagesSentByTypeAndNetwork);
     		if(oldValue!=null)
     			messagesSentByTypeAndNetwork=oldValue;     			
     	}
     	
     	ConcurrentHashMap<String, AtomicLong> bytesSentByTypeAndNetwork=this.bytesSentByTypeAndNetwork.get(networkID);
     	if(bytesSentByTypeAndNetwork==null) {
     		bytesSentByTypeAndNetwork=new ConcurrentHashMap<String, AtomicLong>();
     		for(String currType:allMessageTypes)
            	bytesSentByTypeAndNetwork.put(currType, new AtomicLong(0L));            	
            
     		ConcurrentHashMap<String, AtomicLong> oldValue=this.bytesSentByTypeAndNetwork.putIfAbsent(networkID, bytesSentByTypeAndNetwork);
     		if(oldValue!=null)
     			bytesSentByTypeAndNetwork=oldValue;     			
     	}
     	
     	messagesSentByTypeAndNetwork.get(SccpMessageImpl.getName(message.getType())).incrementAndGet();
     	bytesSentByTypeAndNetwork.get(SccpMessageImpl.getName(message.getType())).addAndGet(mtp3Message.getData().readableBytes());
     	mup.sendMessage(mtp3Message);
    }

	@Override
	public Map<String, Long> getMessagesSentByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=messagesSentByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getMessagesReceivedByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=messagesReceivedByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getBytesSentByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=bytesSentByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getBytesReceivedByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=bytesReceivedByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Long getDataMessagesSent() {
		return messagesSentByType.get(SccpMessageImpl.MESSAGE_NAME_DT1).get() + messagesSentByType.get(SccpMessageImpl.MESSAGE_NAME_DT2).get() + messagesSentByType.get(SccpMessageImpl.MESSAGE_NAME_UDT).get() + messagesSentByType.get(SccpMessageImpl.MESSAGE_NAME_LUDT).get() + messagesSentByType.get(SccpMessageImpl.MESSAGE_NAME_XUDT).get();
	}

	@Override
	public Long getDataMessagesReceived() {
		return messagesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_DT1).get() + messagesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_DT2).get() + messagesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_UDT).get() + messagesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_LUDT).get() + messagesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_XUDT).get();
	}

	@Override
	public Long getDataBytesSent() {
		return bytesSentByType.get(SccpMessageImpl.MESSAGE_NAME_DT1).get() + bytesSentByType.get(SccpMessageImpl.MESSAGE_NAME_DT2).get() + bytesSentByType.get(SccpMessageImpl.MESSAGE_NAME_UDT).get() + bytesSentByType.get(SccpMessageImpl.MESSAGE_NAME_LUDT).get() + bytesSentByType.get(SccpMessageImpl.MESSAGE_NAME_XUDT).get();
	}

	@Override
	public Long getDataBytesReceived() {
		return bytesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_DT1).get() + bytesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_DT2).get() + bytesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_UDT).get() + bytesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_LUDT).get() + bytesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_XUDT).get();
	}

	@Override
	public Map<String, Long> getMessagesSentByTypeAndNetworkID(int networkID) {
		ConcurrentHashMap<String, AtomicLong> messagesSentByType=messagesSentByTypeAndNetwork.get(networkID);
		Map<String,Long> result=new HashMap<String, Long>();
		
		if(messagesSentByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=messagesSentByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getMessagesReceivedByTypeAndNetworkID(int networkID) {
		ConcurrentHashMap<String, AtomicLong> messagesReceivedByType=messagesReceivedByTypeAndNetwork.get(networkID);
		Map<String,Long> result=new HashMap<String, Long>();
		
		if(messagesReceivedByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=messagesReceivedByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getBytesSentByTypeAndNetworkID(int networkID) {
		ConcurrentHashMap<String, AtomicLong> bytesSentByType=bytesSentByTypeAndNetwork.get(networkID);
		Map<String,Long> result=new HashMap<String, Long>();
		
		if(bytesSentByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=bytesSentByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getBytesReceivedByTypeAndNetworkID(int networkID) {
		ConcurrentHashMap<String, AtomicLong> bytesReceivedByType=bytesReceivedByTypeAndNetwork.get(networkID);
		Map<String,Long> result=new HashMap<String, Long>();
		
		if(bytesReceivedByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=bytesReceivedByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Long getDataMessagesSentByTypeAndNetworkID(int networkID) {
		ConcurrentHashMap<String, AtomicLong> messagesSentByType=messagesSentByTypeAndNetwork.get(networkID);
		if(messagesSentByType==null)
			return 0L;
		
		return messagesSentByType.get(SccpMessageImpl.MESSAGE_NAME_DT1).get() + messagesSentByType.get(SccpMessageImpl.MESSAGE_NAME_DT2).get() + messagesSentByType.get(SccpMessageImpl.MESSAGE_NAME_UDT).get() + messagesSentByType.get(SccpMessageImpl.MESSAGE_NAME_LUDT).get() + messagesSentByType.get(SccpMessageImpl.MESSAGE_NAME_XUDT).get();
	}

	@Override
	public Long getDataMessagesReceivedByTypeAndNetworkID(int networkID) {
		ConcurrentHashMap<String, AtomicLong> messagesReceivedByType=messagesReceivedByTypeAndNetwork.get(networkID);
		if(messagesReceivedByType==null)
			return 0L;
		
		return messagesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_DT1).get() + messagesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_DT2).get() + messagesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_UDT).get() + messagesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_LUDT).get() + messagesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_XUDT).get();
	}

	@Override
	public Long getDataBytesSentByTypeAndNetworkID(int networkID) {
		ConcurrentHashMap<String, AtomicLong> bytesSentByType=bytesSentByTypeAndNetwork.get(networkID);
		if(bytesSentByType==null)
			return 0L;
		
		return bytesSentByType.get(SccpMessageImpl.MESSAGE_NAME_DT1).get() + bytesSentByType.get(SccpMessageImpl.MESSAGE_NAME_DT2).get() + bytesSentByType.get(SccpMessageImpl.MESSAGE_NAME_UDT).get() + bytesSentByType.get(SccpMessageImpl.MESSAGE_NAME_LUDT).get() + bytesSentByType.get(SccpMessageImpl.MESSAGE_NAME_XUDT).get();
	}

	@Override
	public Long getDataBytesReceivedByTypeAndNetworkID(int networkID) {
		ConcurrentHashMap<String, AtomicLong> bytesReceivedByType=bytesReceivedByTypeAndNetwork.get(networkID);
		if(bytesReceivedByType==null)
			return 0L;
		
		return bytesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_DT1).get() + bytesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_DT2).get() + bytesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_UDT).get() + bytesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_LUDT).get() + bytesReceivedByType.get(SccpMessageImpl.MESSAGE_NAME_XUDT).get();
	}
}