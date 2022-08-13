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

import java.util.ArrayList;
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
import org.restcomm.protocols.ss7.sccp.SccpStack;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.asn.TCUnifiedMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;

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
    
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> messagesSentByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> messagesReceivedByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> componentsSentByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> componentsReceivedByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> rejectsSentByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> rejectsReceivedByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> abortsSentByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> abortsReceivedByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,AtomicLong> incomingDialogsProcessedByNetwork=new ConcurrentHashMap<Integer,AtomicLong>();
    private ConcurrentHashMap<Integer,AtomicLong> outgoingDialogsProcessedByNetwork=new ConcurrentHashMap<Integer,AtomicLong>();
    private ConcurrentHashMap<Integer,AtomicLong> dialogTimeoutProcessedByNetwork=new ConcurrentHashMap<Integer,AtomicLong>();
    private ConcurrentHashMap<Integer,AtomicLong> invokeTimeoutProcessedByNetwork=new ConcurrentHashMap<Integer,AtomicLong>();
    private ConcurrentHashMap<Integer,AtomicLong> bytesSentByNetwork=new ConcurrentHashMap<Integer,AtomicLong>();
    private ConcurrentHashMap<Integer,AtomicLong> bytesReceivedByNetwork=new ConcurrentHashMap<Integer,AtomicLong>();
    
    public static List<String> allAbortCauses=new ArrayList<String>();
    public static List<String> allProblemTypes=new ArrayList<String>();
    
    static
    {
    	for(PAbortCauseType abortType:PAbortCauseType.values())
    		allAbortCauses.add(abortType.name());
        
    	allAbortCauses.add("User");
    	
    	for(GeneralProblemType generalProblem:GeneralProblemType.values())
    		allProblemTypes.add(generalProblem.name());
        
        for(InvokeProblemType invokeProblem:InvokeProblemType.values())
        	allProblemTypes.add(invokeProblem.name());
        
        for(ReturnErrorProblemType returnErrorProblem:ReturnErrorProblemType.values())
        	allProblemTypes.add(returnErrorProblem.name());
        
        for(ReturnResultProblemType returnResultProblem:ReturnResultProblemType.values())
        	allProblemTypes.add(returnResultProblem.name());        
    }
    
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
        
        for(GeneralProblemType generalProblem:GeneralProblemType.values()) {
        	rejectsSentByType.put(generalProblem.name(),new AtomicLong(0));
        	rejectsReceivedByType.put(generalProblem.name(),new AtomicLong(0));
        }
        
        for(InvokeProblemType invokeProblem:InvokeProblemType.values()) {
        	rejectsSentByType.put(invokeProblem.name(),new AtomicLong(0));
        	rejectsReceivedByType.put(invokeProblem.name(),new AtomicLong(0));
        }
        
        for(ReturnErrorProblemType returnErrorProblem:ReturnErrorProblemType.values()) {
        	rejectsSentByType.put(returnErrorProblem.name(),new AtomicLong(0));
        	rejectsReceivedByType.put(returnErrorProblem.name(),new AtomicLong(0));
        }
        
        for(ReturnResultProblemType returnResultProblem:ReturnResultProblemType.values()) {
        	rejectsSentByType.put(returnResultProblem.name(),new AtomicLong(0));
        	rejectsReceivedByType.put(returnResultProblem.name(),new AtomicLong(0));
        }
        
        for(PAbortCauseType abortType:PAbortCauseType.values()) {
        	abortsReceivedByType.put(abortType.name(),new AtomicLong(0));
        	abortsSentByType.put(abortType.name(),new AtomicLong(0));
        }
        
        abortsReceivedByType.put("User",new AtomicLong(0));
    	abortsSentByType.put("User",new AtomicLong(0));
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
	
	@Override
	public Map<String, Long> getComponentsSentByTypeAndNetwork(Integer networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> componentsSentByType = componentsSentByTypeAndNetwork.get(networkID);
		if(componentsSentByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=componentsSentByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;		
	}

	@Override
	public Map<String, Long> getComponentsReceivedByTypeAndNetwork(Integer networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> componentsReceivedByType = componentsReceivedByTypeAndNetwork.get(networkID);
		if(componentsReceivedByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=componentsReceivedByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;		
	}

	@Override
	public Map<String, Long> getMessagesSentByTypeAndNetwork(Integer networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> messagesSentByType = messagesSentByTypeAndNetwork.get(networkID);
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
	public Map<String, Long> getMessagesReceivedByTypeAndNetwork(Integer networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> messagesReceivedByType = messagesReceivedByTypeAndNetwork.get(networkID);
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
	public Map<String, Long> getRejectsSentByTypeAndNetwork(Integer networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> rejectsSentByType = rejectsSentByTypeAndNetwork.get(networkID);
		if(rejectsSentByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=rejectsSentByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getRejectsReceivedByTypeAndNetwork(Integer networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> rejectsReceivedByType = rejectsReceivedByTypeAndNetwork.get(networkID);
		if(rejectsReceivedByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=rejectsReceivedByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getAbortsSentByTypeAndNetwork(Integer networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> abortsSentByType = abortsSentByTypeAndNetwork.get(networkID);
		if(abortsSentByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=abortsSentByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getAbortsReceivedByTypeAndNetwork(Integer networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> abortsReceivedByType = abortsReceivedByTypeAndNetwork.get(networkID);
		if(abortsReceivedByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=abortsReceivedByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Long getIncomingDialogsProcessedByNetwork(Integer networkID) {
		AtomicLong result = incomingDialogsProcessedByNetwork.get(networkID);
		if(result==null)
			return 0L;
		
		return result.get();
	}

	@Override
	public Long getOutgoingDialogsProcessedByNetwork(Integer networkID) {
		AtomicLong result = outgoingDialogsProcessedByNetwork.get(networkID);
		if(result==null)
			return 0L;
		
		return result.get();
	}

	@Override
	public Long getBytesSentByNetwork(Integer networkID) {
		AtomicLong result = bytesSentByNetwork.get(networkID);
		if(result==null)
			return 0L;
		
		return result.get();
	}

	@Override
	public Long getBytesReceivedByNetwork(Integer networkID) {
		AtomicLong result = bytesReceivedByNetwork.get(networkID);
		if(result==null)
			return 0L;
		
		return result.get();
	}

	@Override
	public Long getDialogTimeoutProcessedByNetwork(Integer networkID) {
		AtomicLong result = dialogTimeoutProcessedByNetwork.get(networkID);
		if(result==null)
			return 0L;
		
		return result.get();
	}

	@Override
	public Long getInvokeTimeoutProcessedByNetwork(Integer networkID) {
		AtomicLong result = invokeTimeoutProcessedByNetwork.get(networkID);
		if(result==null)
			return 0L;
		
		return result.get();
	}
	
	protected void newIncomingDialogProcessed(int networkID) {
		incomingDialogsProcessed.incrementAndGet();
		
		AtomicLong incomingDialogsProcessed = incomingDialogsProcessedByNetwork.get(networkID);
		if(incomingDialogsProcessed==null) {
			incomingDialogsProcessed=new AtomicLong();
			AtomicLong oldValue=incomingDialogsProcessedByNetwork.putIfAbsent(networkID, incomingDialogsProcessed);
			if(oldValue!=null)
				incomingDialogsProcessed=oldValue;
		}
		
		incomingDialogsProcessed.incrementAndGet();
	}
	
	protected void newOutgoingDialogProcessed(int networkID) {
		outgoingDialogsProcessed.incrementAndGet();
		
		AtomicLong outgoingDialogsProcessed = outgoingDialogsProcessedByNetwork.get(networkID);
		if(outgoingDialogsProcessed==null) {
			outgoingDialogsProcessed=new AtomicLong();
			AtomicLong oldValue=outgoingDialogsProcessedByNetwork.putIfAbsent(networkID, outgoingDialogsProcessed);
			if(oldValue!=null)
				invokeTimeoutProcessed=oldValue;
		}
		
		outgoingDialogsProcessed.incrementAndGet();
	}
	
	protected void newComponentSent(String componentName,int networkID) {
		componentsSentByType.get(componentName).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> componentsSentByType =componentsSentByTypeAndNetwork.get(networkID);
		if(componentsSentByType==null) {
			componentsSentByType=new ConcurrentHashMap<String,AtomicLong>();
			for(ComponentType currComponentType:ComponentType.values())
				componentsSentByType.put(currComponentType.name(),new AtomicLong(0));
	        
			ConcurrentHashMap<String,AtomicLong> oldValue=componentsSentByTypeAndNetwork.putIfAbsent(networkID, componentsSentByType);
			if(oldValue!=null)
				componentsSentByType=oldValue;
		}
		
		componentsSentByType.get(componentName).incrementAndGet();	
	}
	
	protected void newComponentReceived(String componentName,int networkID) {
		componentsReceivedByType.get(componentName).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> componentsReceivedByType =componentsReceivedByTypeAndNetwork.get(networkID);
		if(componentsReceivedByType==null) {
			componentsReceivedByType=new ConcurrentHashMap<String,AtomicLong>();
			for(ComponentType currComponentType:ComponentType.values())
				componentsReceivedByType.put(currComponentType.name(),new AtomicLong(0));
	        
			ConcurrentHashMap<String,AtomicLong> oldValue=componentsReceivedByTypeAndNetwork.putIfAbsent(networkID, componentsReceivedByType);
			if(oldValue!=null)
				componentsReceivedByType=oldValue;
		}
		
		componentsReceivedByType.get(componentName).incrementAndGet();	
	}
	
	protected void newRejectSent(String rejectReason,int networkID) {
		rejectsSentByType.get(rejectReason).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> rejectsSentByType =rejectsSentByTypeAndNetwork.get(networkID);
		if(rejectsSentByType==null) {
			rejectsSentByType=new ConcurrentHashMap<String,AtomicLong>();
			for(GeneralProblemType generalProblem:GeneralProblemType.values())
	        	rejectsSentByType.put(generalProblem.name(),new AtomicLong(0));
	        
	        for(InvokeProblemType invokeProblem:InvokeProblemType.values())
	        	rejectsSentByType.put(invokeProblem.name(),new AtomicLong(0));
	        
	        for(ReturnErrorProblemType returnErrorProblem:ReturnErrorProblemType.values())
	        	rejectsSentByType.put(returnErrorProblem.name(),new AtomicLong(0));
	        
	        for(ReturnResultProblemType returnResultProblem:ReturnResultProblemType.values())
	        	rejectsSentByType.put(returnResultProblem.name(),new AtomicLong(0));
	        
			ConcurrentHashMap<String,AtomicLong> oldValue=rejectsSentByTypeAndNetwork.putIfAbsent(networkID, rejectsSentByType);
			if(oldValue!=null)
				rejectsSentByType=oldValue;
		}
		
		rejectsSentByType.get(rejectReason).incrementAndGet();	
	}
	
	protected void newRejectReceived(String rejectReason,int networkID) {
		rejectsReceivedByType.get(rejectReason).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> rejectsReceivedByType =rejectsReceivedByTypeAndNetwork.get(networkID);
		if(rejectsReceivedByType==null) {
			rejectsReceivedByType=new ConcurrentHashMap<String,AtomicLong>();
			for(GeneralProblemType generalProblem:GeneralProblemType.values())
	        	rejectsReceivedByType.put(generalProblem.name(),new AtomicLong(0));
	        
	        for(InvokeProblemType invokeProblem:InvokeProblemType.values())
	        	rejectsReceivedByType.put(invokeProblem.name(),new AtomicLong(0));
	        
	        for(ReturnErrorProblemType returnErrorProblem:ReturnErrorProblemType.values())
	        	rejectsReceivedByType.put(returnErrorProblem.name(),new AtomicLong(0));
	        
	        for(ReturnResultProblemType returnResultProblem:ReturnResultProblemType.values())
	        	rejectsReceivedByType.put(returnResultProblem.name(),new AtomicLong(0));
	        
			ConcurrentHashMap<String,AtomicLong> oldValue=rejectsReceivedByTypeAndNetwork.putIfAbsent(networkID, rejectsReceivedByType);
			if(oldValue!=null)
				rejectsReceivedByType=oldValue;
		}
		
		rejectsReceivedByType.get(rejectReason).incrementAndGet();	
	}
	
	protected void newAbortSent(String abortCause,int networkID) {
		abortsSentByType.get(abortCause).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> abortsSentByType =abortsSentByTypeAndNetwork.get(networkID);
		if(abortsSentByType==null) {
			abortsSentByType=new ConcurrentHashMap<String,AtomicLong>();
			for(PAbortCauseType abortType:PAbortCauseType.values())
	        	abortsSentByType.put(abortType.name(),new AtomicLong(0));
	        
	        abortsSentByType.put("User",new AtomicLong(0));
	    	
			ConcurrentHashMap<String,AtomicLong> oldValue=abortsSentByTypeAndNetwork.putIfAbsent(networkID, abortsSentByType);
			if(oldValue!=null)
				abortsSentByType=oldValue;
		}
		
		abortsSentByType.get(abortCause).incrementAndGet();
	}
	
	protected void newAbortReceived(String abortCause,Integer networkID) {
		abortsReceivedByType.get(abortCause).incrementAndGet();
		
		if(networkID!=null) {
			ConcurrentHashMap<String,AtomicLong> abortsReceivedByType =abortsReceivedByTypeAndNetwork.get(networkID);
			if(abortsReceivedByType==null) {
				abortsReceivedByType=new ConcurrentHashMap<String,AtomicLong>();
				for(PAbortCauseType abortType:PAbortCauseType.values())
		        	abortsReceivedByType.put(abortType.name(),new AtomicLong(0));
		        
		        abortsReceivedByType.put("User",new AtomicLong(0));
		    	
				ConcurrentHashMap<String,AtomicLong> oldValue=abortsReceivedByTypeAndNetwork.putIfAbsent(networkID, abortsReceivedByType);
				if(oldValue!=null)
					abortsReceivedByType=oldValue;
			}
			
			abortsReceivedByType.get(abortCause).incrementAndGet();
		}
	}
	
	protected void newMessageSent(String messageType,int bytes,int networkID) {
		messagesSentByType.get(messageType).incrementAndGet();
		bytesSent.addAndGet(bytes);
		
		ConcurrentHashMap<String,AtomicLong> messagesSentByType =messagesSentByTypeAndNetwork.get(networkID);
		if(messagesSentByType==null) {
			messagesSentByType=new ConcurrentHashMap<String,AtomicLong>();
			for(String currName:TCUnifiedMessageImpl.getAllNames())
	        	messagesSentByType.put(currName,new AtomicLong(0));
	        
			ConcurrentHashMap<String,AtomicLong> oldValue=messagesSentByTypeAndNetwork.putIfAbsent(networkID, messagesSentByType);
			if(oldValue!=null)
				messagesSentByType=oldValue;
		}
		
		messagesSentByType.get(messageType).incrementAndGet();		
		
		AtomicLong bytesSent = bytesSentByNetwork.get(networkID);
		if(bytesSent==null) {
			bytesSent=new AtomicLong();
			AtomicLong oldValue=bytesSentByNetwork.putIfAbsent(networkID, bytesSent);
			if(oldValue!=null)
				bytesSent=oldValue;
		}
		
		bytesSent.addAndGet(bytes);		
	}
	
	protected void newMessageReceived(String messageType,int bytes,int networkID) {
		messagesReceivedByType.get(messageType).incrementAndGet();
		bytesReceived.addAndGet(bytes);
		
		ConcurrentHashMap<String,AtomicLong> messagesReceivedByType =messagesReceivedByTypeAndNetwork.get(networkID);
		if(messagesReceivedByType==null) {
			messagesReceivedByType=new ConcurrentHashMap<String,AtomicLong>();
			for(String currName:TCUnifiedMessageImpl.getAllNames())
				messagesReceivedByType.put(currName,new AtomicLong(0));
	        
			ConcurrentHashMap<String,AtomicLong> oldValue=messagesReceivedByTypeAndNetwork.putIfAbsent(networkID, messagesReceivedByType);
			if(oldValue!=null)
				messagesReceivedByType=oldValue;
		}
		
		messagesReceivedByType.get(messageType).incrementAndGet();		
		
		AtomicLong bytesReceived = bytesReceivedByNetwork.get(networkID);
		if(bytesReceived==null) {
			bytesReceived=new AtomicLong();
			AtomicLong oldValue=bytesReceivedByNetwork.putIfAbsent(networkID, bytesReceived);
			if(oldValue!=null)
				bytesReceived=oldValue;
		}
		
		bytesReceived.addAndGet(bytes);		
	}
	
	protected void dialogTimedOut(int networkID) {
		dialogTimeoutProcessed.incrementAndGet();
		
		AtomicLong dialogTimeoutProcessed = dialogTimeoutProcessedByNetwork.get(networkID);
		if(dialogTimeoutProcessed==null) {
			dialogTimeoutProcessed=new AtomicLong();
			AtomicLong oldValue=dialogTimeoutProcessedByNetwork.putIfAbsent(networkID, dialogTimeoutProcessed);
			if(oldValue!=null)
				dialogTimeoutProcessed=oldValue;
		}
		
		dialogTimeoutProcessed.incrementAndGet();
	}
	
	protected void invokeTimedOut(int networkID) {
		invokeTimeoutProcessed.incrementAndGet();
		
		AtomicLong invokeTimeoutProcessed = invokeTimeoutProcessedByNetwork.get(networkID);
		if(invokeTimeoutProcessed==null) {
			invokeTimeoutProcessed=new AtomicLong();
			AtomicLong oldValue=invokeTimeoutProcessedByNetwork.putIfAbsent(networkID, invokeTimeoutProcessed);
			if(oldValue!=null)
				invokeTimeoutProcessed=oldValue;
		}
		
		invokeTimeoutProcessed.incrementAndGet();
	}
}