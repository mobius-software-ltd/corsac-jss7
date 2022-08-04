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

package org.restcomm.protocols.ss7.tcapAnsi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TCUnifiedMessageImpl;

/**
 * @author amit bhayani
 * @author baranowb
 * @author yulianoifa
 *
 */
public class TCAPStackImpl implements TCAPStack {

    private final Logger logger;

    protected static final String TCAP_MANAGEMENT_PERSIST_DIR_KEY = "tcapmanagement.persist.dir";
    protected static final String USER_DIR_KEY = "user.dir";
    protected static final String PERSIST_FILE_NAME = "management.xml";
    // default value of idle timeout and after TC_END remove of task.
    
    // TCAP state data, it is used ONLY on client side
    protected TCAPProviderImpl tcapProvider;
    protected ScheduledExecutorService service;
    
    private final String name;

    protected String persistDir = null;

    private volatile boolean started = false;

    private long dialogTimeout = _DIALOG_TIMEOUT;
    private long invokeTimeout = _INVOKE_TIMEOUT;
    
    // TODO: make this configurable
    private long dialogIdRangeStart = 1;
    private long dialogIdRangeEnd = Integer.MAX_VALUE;
    private ConcurrentHashMap<Integer,Integer> extraSsns = new ConcurrentHashMap<Integer,Integer>();
    
    private boolean isSwapTcapIdBytes = true;  // for now configurable only via XML file

    private int ssn = -1;

    // SLS value
    private SlsRangeType slsRange = SlsRangeType.All;

    private ConcurrentHashMap<String, AtomicLong> messagesSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> messagesReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> componentsSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> componentsReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> rejectsSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> rejectsReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> abortsSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> abortsReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private AtomicLong incomingDialogsProcessed=new AtomicLong(0L);
    private AtomicLong outgoingDialogsProcessed=new AtomicLong(0L);
    private AtomicLong dialogTimeoutProcessed=new AtomicLong(0L);
    private AtomicLong invokeTimeoutProcessed=new AtomicLong(0L);
    private AtomicLong bytesSent=new AtomicLong(0L);
    private AtomicLong bytesReceived=new AtomicLong(0L);
    
    public TCAPStackImpl(String name,int threads) {
        super();
        this.name = name;

        service=Executors.newScheduledThreadPool(threads);
        this.logger = LogManager.getLogger(TCAPStackImpl.class.getCanonicalName() + "-" + this.name);
        
        for(String currName:TCUnifiedMessageImpl.getAllNames()) {
        	messagesSentByType.put(currName,new AtomicLong(0));
        	messagesReceivedByType.put(currName,new AtomicLong(0));
        }        	
        
        for(ComponentType currComponentType:ComponentType.values()) {
        	componentsSentByType.put(currComponentType.name(),new AtomicLong(0));
        	componentsReceivedByType.put(currComponentType.name(),new AtomicLong(0));
        }
        
        for(RejectProblem rejectProblem:RejectProblem.values()) {
        	rejectsSentByType.put(rejectProblem.name(),new AtomicLong(0));
        	rejectsReceivedByType.put(rejectProblem.name(),new AtomicLong(0));
        }
        
        for(PAbortCause abortType:PAbortCause.values()) {
        	abortsReceivedByType.put(abortType.name(),new AtomicLong(0));
        	abortsSentByType.put(abortType.name(),new AtomicLong(0));
        }
        
        abortsReceivedByType.put("User",new AtomicLong(0));
    	abortsSentByType.put("User",new AtomicLong(0));
    }

    public TCAPStackImpl(String name, SccpProvider sccpProvider, int ssn,int threads) {
        this(name,threads);

        this.tcapProvider = new TCAPProviderImpl(sccpProvider, this, ssn,service);
        this.ssn = ssn;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPersistDir() {
        return persistDir;
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

    public void setSlsRange(String val) throws Exception {

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

    @Override
    public boolean getSwapTcapIdBytes() {
        return isSwapTcapIdBytes;
    }

    @Override
    public void setSwapTcapIdBytes(boolean isSwapTcapIdBytes) {
        this.isSwapTcapIdBytes = isSwapTcapIdBytes;
    }

    public SlsRangeType getSlsRangeType() {
        return this.slsRange;
    }

	@Override
	public Map<String, Long> getComponentsSentByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=componentsSentByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getComponentsReceivedByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=componentsReceivedByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
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
	public Map<String, Long> getRejectsSentByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=rejectsSentByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getRejectsReceivedByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=rejectsReceivedByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getAbortsSentByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=abortsSentByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getAbortsReceivedByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=abortsReceivedByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Long getIncomingDialogsProcessed() {
		return incomingDialogsProcessed.get();
	}

	@Override
	public Long getOutgoingDialogsProcessed() {
		return outgoingDialogsProcessed.get();
	}

	@Override
	public Long getBytesSent() {
		return bytesSent.get();
	}

	@Override
	public Long getBytesReceived() {
		return bytesReceived.get();
	}

	@Override
	public Long getDialogTimeoutProcessed() {
		return dialogTimeoutProcessed.get();
	}

	@Override
	public Long getInvokeTimeoutProcessed() {
		return invokeTimeoutProcessed.get();
	}
	
	protected void newIncomingDialogProcessed() {
		incomingDialogsProcessed.incrementAndGet();
	}
	
	protected void newOutgoingDialogProcessed() {
		outgoingDialogsProcessed.incrementAndGet();
	}
	
	protected void newComponentReceived(String componentName) {
		componentsSentByType.get(componentName).incrementAndGet();
	}
	
	protected void newComponentSent(String componentName) {
		componentsReceivedByType.get(componentName).incrementAndGet();
	}
	
	protected void newRejectReceived(String rejectReason) {
		rejectsSentByType.get(rejectReason).incrementAndGet();
	}
	
	protected void newRejectSent(String rejectReason) {
		rejectsReceivedByType.get(rejectReason).incrementAndGet();
	}
	
	protected void newAbortReceived(String abortCause) {
		abortsSentByType.get(abortCause).incrementAndGet();
	}
	
	protected void newAbortSent(String abortCause) {
		abortsReceivedByType.get(abortCause).incrementAndGet();
	}
	
	protected void newMessageReceived(String messageType,int bytes) {
		messagesSentByType.get(messageType).incrementAndGet();
		bytesSent.addAndGet(bytes);
	}
	
	protected void newMessageSent(String messageType,int bytes) {
		messagesReceivedByType.get(messageType).incrementAndGet();
		bytesReceived.addAndGet(bytes);
	}
	
	protected void dialogTimedOut() {
		dialogTimeoutProcessed.incrementAndGet();
	}
	
	protected void invokeTimedOut() {
		invokeTimeoutProcessed.incrementAndGet();
	}
}
