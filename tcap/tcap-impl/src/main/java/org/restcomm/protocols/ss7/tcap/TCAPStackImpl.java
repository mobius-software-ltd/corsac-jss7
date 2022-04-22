/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpStack;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;

/**
 * @author amit bhayani
 * @author baranowb
 *
 */
public class TCAPStackImpl implements TCAPStack {

    private final Logger logger;

    protected static final String TCAP_MANAGEMENT_PERSIST_DIR_KEY = "tcapmanagement.persist.dir";
    protected static final String USER_DIR_KEY = "user.dir";
    protected static final String PERSIST_FILE_NAME = "management.xml";
    
    // TCAP state data, it is used ONLY on client side
    protected TCAPProviderImpl tcapProvider;
    protected ScheduledExecutorService service;
    
    private SccpProvider sccpProvider;
    
    private final String name;

    protected String persistDir = null;

    private volatile boolean started = false;

    private long dialogTimeout = _DIALOG_TIMEOUT;
    private long invokeTimeout = _INVOKE_TIMEOUT;
    
    // TODO: make this configurable
    private long dialogIdRangeStart = 1;
    private long dialogIdRangeEnd = Integer.MAX_VALUE;
    private ConcurrentHashMap<Integer,Integer> extraSsns = new ConcurrentHashMap<Integer,Integer>();
    private boolean doNotSendProtocolVersion = false;
    
    private boolean isSwapTcapIdBytes = true;  // for now configurable only via XML file

    private int ssn = -1;

    // SLS value
    private SlsRangeType slsRange = SlsRangeType.All;

    public TCAPStackImpl(String name,int threads) {
        super();
        this.name = name;
        service=Executors.newScheduledThreadPool(threads);
        this.logger = LogManager.getLogger(TCAPStackImpl.class.getCanonicalName() + "-" + this.name);
    }

    public TCAPStackImpl(String name, SccpProvider sccpProvider, int ssn,int threads) {
        this(name,threads);

        this.sccpProvider = sccpProvider;
        this.tcapProvider = new TCAPProviderImpl(sccpProvider, this, ssn, service);

        this.ssn = ssn;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSubSystemNumber(){
        return this.ssn;
    }

    public void start() throws Exception {
        logger.info("Starting ..." + tcapProvider);

        if (this.dialogTimeout < 0) {
            throw new IllegalArgumentException("DialogIdleTimeout value must be greater or equal to zero.");
        }

        if (this.dialogTimeout < this.invokeTimeout) {
            throw new IllegalArgumentException("DialogIdleTimeout value must be greater or equal to invoke timeout.");
        }

        if (this.invokeTimeout < 0) {
            throw new IllegalArgumentException("InvokeTimeout value must be greater or equal to zero.");
        }

        tcapProvider.start();
        this.started = true;
    }

    private void checkDialogIdRangeValues(long rangeStart, long rangeEnd) {
        if (rangeStart >= rangeEnd)
            throw new IllegalArgumentException("Range start value cannot be equal/greater than Range end value");
        if (rangeStart < 1)
            throw new IllegalArgumentException("Range start value must be greater or equal 1");
        if (rangeEnd > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Range end value must be less or equal " + Integer.MAX_VALUE);
        if ((rangeEnd - rangeStart) < 10000)
            throw new IllegalArgumentException("Range \"end - start\" must has at least 10000 possible dialogs");        
    }

    public void stop() {
        this.tcapProvider.stop();
        service.shutdownNow();
        this.started = false;
    }

    /**
     * @return the started
     */
    public boolean isStarted() {
        return this.started;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPStack#getProvider()
     */
    public TCAPProvider getProvider() {

        return tcapProvider;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPStack#setDialogIdleTimeout(long)
     */
    public void setDialogIdleTimeout(long v) throws Exception {
        if (!this.started)
            throw new Exception("DialogIdleTimeout parameter can be updated only when TCAP stack is running");

        if (v < 0) {
            throw new IllegalArgumentException("DialogIdleTimeout value must be greater or equal to zero.");
        }
        if (v < this.invokeTimeout) {
            throw new IllegalArgumentException("DialogIdleTimeout value must be greater or equal to invoke timeout.");
        }

        this.dialogTimeout = v;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPStack#getDialogIdleTimeout()
     */
    public long getDialogIdleTimeout() {
        return this.dialogTimeout;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPStack#setInvokeTimeout(long)
     */
    public void setInvokeTimeout(long v) throws Exception {
        if (!this.started)
            throw new Exception("InvokeTimeout parameter can be updated only when TCAP stack is running");

        if (v < 0) {
            throw new IllegalArgumentException("InvokeTimeout value must be greater or equal to zero.");
        }
        if (v > this.dialogTimeout) {
            throw new IllegalArgumentException("InvokeTimeout value must be smaller or equal to dialog timeout.");
        }

        this.invokeTimeout = v;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.tcap.api.TCAPStack#getInvokeTimeout()
     */
    public long getInvokeTimeout() {
        return this.invokeTimeout;
    }

    public void setDialogIdRangeStart(long val) throws Exception {
        if (!this.started)
            throw new Exception("DialogIdRangeStart parameter can be updated only when TCAP stack is running");

        this.checkDialogIdRangeValues(val, this.getDialogIdRangeEnd());
        dialogIdRangeStart = val;
        tcapProvider.resetDialogIdValueAfterRangeChange();
    }

    public void setDialogIdRangeEnd(long val) throws Exception {
        if (!this.started)
            throw new Exception("DialogIdRangeEnd parameter can be updated only when TCAP stack is running");

        this.checkDialogIdRangeValues(this.getDialogIdRangeStart(), val);
        dialogIdRangeEnd = val;
        tcapProvider.resetDialogIdValueAfterRangeChange();
    }

    public long getDialogIdRangeStart() {
        return dialogIdRangeStart;
    }

    public long getDialogIdRangeEnd() {
        return dialogIdRangeEnd;
    }

    public void setExtraSsns(List<Integer> extraSsnsNew) throws Exception {
        if (this.started)
            throw new Exception("ExtraSsns parameter can be updated only when TCAP stack is NOT running");

        if (extraSsnsNew != null) {
        	ConcurrentHashMap<Integer,Integer> extraSsnsTemp = new ConcurrentHashMap<Integer,Integer>();
        	for(Integer ssn:extraSsnsNew)
        		extraSsnsTemp.put(ssn, ssn);
        	
        	this.extraSsns = extraSsnsTemp;            
        }
    }

    public Collection<Integer> getExtraSsns() {
        return extraSsns.values();
    }

    public boolean isExtraSsnPresent(int ssn) {
        if (this.ssn == ssn)
            return true;
        if (extraSsns != null) {
            if (extraSsns.containsKey(ssn))
                return true;
        }
        return false;
    }

    @Override
    public String getSubSystemNumberList() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.ssn);
        if (extraSsns != null) {
        	Iterator<Integer> iterator=extraSsns.values().iterator();
            while(iterator.hasNext()) {
                sb.append(", ");
                sb.append(iterator.next());
            }
        }

        return sb.toString();
    }

    public void setSlsRange(String val) throws Exception  {

        if (val.equals(SlsRangeType.All.toString()))  {
            this.slsRange = SlsRangeType.All;
        } else if (val.equals(SlsRangeType.Odd.toString())) {
            this.slsRange = SlsRangeType.Odd;
        } else if (val.equals(SlsRangeType.Even.toString())) {
            this.slsRange = SlsRangeType.Even;
        } else {
            throw new Exception("SlsRange value is invalid");
        }
    }

    public String getSlsRange() {
        return this.slsRange.toString();
    }

    public SlsRangeType getSlsRangeType () {
        return this.slsRange;
    }


    @Override
    public void setDoNotSendProtocolVersion(boolean val) throws Exception {
        if (!this.started)
            throw new Exception("DoNotSendProtocolVersion parameter can be updated only when TCAP stack is running");

        doNotSendProtocolVersion = val;
    }

    @Override
    public boolean getDoNotSendProtocolVersion() {
        return doNotSendProtocolVersion;
    }

    @Override
    public boolean getSwapTcapIdBytes() {
        return isSwapTcapIdBytes;
    }

    @Override
    public void setSwapTcapIdBytes(boolean isSwapTcapIdBytes) {
        this.isSwapTcapIdBytes = isSwapTcapIdBytes;
    }

    @Override
    public SccpStack getSccpStack() {
        return this.sccpProvider.getSccpStack();
    }
}
