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

package org.restcomm.protocols.ss7.cap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPProvider;
import org.restcomm.protocols.ss7.cap.api.CAPStack;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorCode;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPStackImpl implements CAPStack {

    protected TCAPStack tcapStack = null;

    protected CAPProviderImpl capProvider = null;

    private State state = State.IDLE;

    private final String name;

    private ConcurrentHashMap<String, AtomicLong> messagesSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> messagesReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> errorsSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> errorsReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> dialogsSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> dialogsReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> messagesSentByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> messagesReceivedByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> errorsSentByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> errorsReceivedByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> dialogsSentByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    private ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>> dialogsReceivedByTypeAndNetwork=new ConcurrentHashMap<Integer,ConcurrentHashMap<String, AtomicLong>>();
    
    public static List<String> allMessageTypes=new ArrayList<String>();
    
    static
    {
    	for(CAPMessageType messageType:CAPMessageType.values())
    		allMessageTypes.add(messageType.name());
        
    	allMessageTypes.add("unknown");    	
    }
    
    public CAPStackImpl(String name, SccpProvider sccpPprovider, int ssn,int threads) {
        this.name = name;
        this.tcapStack = new TCAPStackImpl(name, sccpPprovider, ssn, threads);
        TCAPProvider tcapProvider = tcapStack.getProvider();
        capProvider = new CAPProviderImpl(name, this, tcapProvider);

        this.state = State.CONFIGURED; 
        initCounters();
    }

    public CAPStackImpl(String name, TCAPProvider tcapProvider) {
        this.name = name;
        capProvider = new CAPProviderImpl(name, this, tcapProvider);
        this.tcapStack = tcapProvider.getStack();
        this.state = State.CONFIGURED; 
        initCounters();
    }
    
    private void initCounters() {
    	for(String currName:CAPServiceBaseImpl.getNames()) {
    		dialogsSentByType.put(currName,new AtomicLong(0));
    		dialogsReceivedByType.put(currName,new AtomicLong(0));
        } 
    	
    	for(CAPMessageType currType:CAPMessageType.values()) {
    		messagesSentByType.put(currType.name(),new AtomicLong(0));
    		messagesReceivedByType.put(currType.name(),new AtomicLong(0));
        }  
    	
    	messagesSentByType.put("unknown",new AtomicLong(0));
		messagesReceivedByType.put("unknown",new AtomicLong(0));
		
		for(String currName:CAPErrorCode.getAllNames()) {
    		errorsSentByType.put(currName,new AtomicLong(0));
    		errorsReceivedByType.put(currName,new AtomicLong(0));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CAPProvider getCAPProvider() {
        return this.capProvider;
    }

    @Override
    public void start() throws Exception {
        if (state != State.CONFIGURED) {
            throw new IllegalStateException("Stack has not been configured or is already running!");
        }
        if (tcapStack != null) {
            // this is null in junits!
            this.tcapStack.start();
        }
        this.capProvider.start();

        this.state = State.RUNNING;

    }

    @Override
    public void stop() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Stack is not running!");
        }
        this.capProvider.stop();
        if (tcapStack != null) {
            this.tcapStack.stop();
        }

        this.state = State.CONFIGURED;
    }

    @Override
    public TCAPStack getTCAPStack() {
        return this.tcapStack;
    }

    private enum State {
        IDLE, CONFIGURED, RUNNING;
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
	public Map<String, Long> getErrorsSentByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=errorsSentByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getErrorsReceivedByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=errorsReceivedByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getDialogsSentByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=dialogsSentByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getDialogsReceivedByType() {
		Map<String,Long> result=new HashMap<String, Long>();
		Iterator<Entry<String, AtomicLong>> iterator=dialogsReceivedByType.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, AtomicLong> currEntry=iterator.next();
			result.put(currEntry.getKey(), currEntry.getValue().get());
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getMessagesSentByTypeAndNetwork(int networkID) {
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
	public Map<String, Long> getMessagesReceivedByTypeAndNetwork(int networkID) {
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
	public Map<String, Long> getErrorsSentByTypeAndNetwork(int networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> errorsSentByType = errorsSentByTypeAndNetwork.get(networkID);
		if(errorsSentByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=errorsSentByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getErrorsReceivedByTypeAndNetwork(int networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> errorsReceivedByType = errorsReceivedByTypeAndNetwork.get(networkID);
		if(errorsReceivedByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=errorsReceivedByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getDialogsSentByTypeAndNetwork(int networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> dialogsSentByType = dialogsSentByTypeAndNetwork.get(networkID);
		if(dialogsSentByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=dialogsSentByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Long> getDialogsReceivedByTypeAndNetwork(int networkID) {
		Map<String,Long> result=new HashMap<String, Long>();
		Map<String,AtomicLong> dialogsReceivedByType = dialogsReceivedByTypeAndNetwork.get(networkID);
		if(dialogsReceivedByType!=null) {
			Iterator<Entry<String, AtomicLong>> iterator=dialogsReceivedByType.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, AtomicLong> currEntry=iterator.next();
				result.put(currEntry.getKey(), currEntry.getValue().get());
			}
		}
		
		return result;
	}
	
	public void newMessageReceived(String messageType,int networkID) {
		messagesSentByType.get(messageType).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> messagesSentByType =messagesSentByTypeAndNetwork.get(networkID);
		if(messagesSentByType==null) {
			messagesSentByType=new ConcurrentHashMap<String,AtomicLong>();
			for(CAPMessageType currType:CAPMessageType.values())
				messagesSentByType.put(currType.name(),new AtomicLong(0));
	        
			messagesSentByType.put("unknown",new AtomicLong(0));
			
			ConcurrentHashMap<String,AtomicLong> oldValue=messagesSentByTypeAndNetwork.putIfAbsent(networkID, messagesSentByType);
			if(oldValue!=null)
				messagesSentByType=oldValue;
		}
		
		messagesSentByType.get(messageType).incrementAndGet();
	}
	
	public void newMessageSent(String messageType,int networkID) {
		messagesReceivedByType.get(messageType).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> messagesReceivedByType =messagesReceivedByTypeAndNetwork.get(networkID);
		if(messagesReceivedByType==null) {
			messagesReceivedByType=new ConcurrentHashMap<String,AtomicLong>();
			for(CAPMessageType currType:CAPMessageType.values())
	    		messagesReceivedByType.put(currType.name(),new AtomicLong(0));
	        
	    	messagesReceivedByType.put("unknown",new AtomicLong(0));
			
			ConcurrentHashMap<String,AtomicLong> oldValue=messagesReceivedByTypeAndNetwork.putIfAbsent(networkID, messagesReceivedByType);
			if(oldValue!=null)
				messagesReceivedByType=oldValue;
		}
		
		messagesReceivedByType.get(messageType).incrementAndGet();
	}
	
	public void newErrorReceived(String errorType,int networkID) {
		errorsSentByType.get(errorType).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> errorsSentByType =errorsSentByTypeAndNetwork.get(networkID);
		if(errorsSentByType==null) {
			errorsSentByType=new ConcurrentHashMap<String,AtomicLong>();
			for(String currName:CAPErrorCode.getAllNames())
				errorsSentByType.put(currName,new AtomicLong(0));
	        
			ConcurrentHashMap<String,AtomicLong> oldValue=errorsSentByTypeAndNetwork.putIfAbsent(networkID, errorsSentByType);
			if(oldValue!=null)
				errorsSentByType=oldValue;
		}
		
		errorsSentByType.get(errorType).incrementAndGet();
	}
	
	public void newErrorSent(String errorType,int networkID) {
		errorsReceivedByType.get(errorType).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> errorsReceivedByType =errorsReceivedByTypeAndNetwork.get(networkID);
		if(errorsReceivedByType==null) {
			errorsReceivedByType=new ConcurrentHashMap<String,AtomicLong>();
			for(String currName:CAPErrorCode.getAllNames())
	    		errorsReceivedByType.put(currName,new AtomicLong(0));
	        
			ConcurrentHashMap<String,AtomicLong> oldValue=errorsReceivedByTypeAndNetwork.putIfAbsent(networkID, errorsReceivedByType);
			if(oldValue!=null)
				errorsReceivedByType=oldValue;
		}
		
		errorsReceivedByType.get(errorType).incrementAndGet();
	}
	
	public void newDialogReceived(String dialogType,int networkID) {
		dialogsSentByType.get(dialogType).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> dialogsSentByType =dialogsSentByTypeAndNetwork.get(networkID);
		if(dialogsSentByType==null) {
			dialogsSentByType=new ConcurrentHashMap<String,AtomicLong>();
			for(String currName:CAPServiceBaseImpl.getNames())
				dialogsSentByType.put(currName,new AtomicLong(0));
	        
			ConcurrentHashMap<String,AtomicLong> oldValue=dialogsSentByTypeAndNetwork.putIfAbsent(networkID, dialogsSentByType);
			if(oldValue!=null)
				dialogsSentByType=oldValue;
		}
		
		dialogsSentByType.get(dialogType).incrementAndGet();
	}
	
	public void newDialogSent(String dialogType,int networkID) {
		dialogsReceivedByType.get(dialogType).incrementAndGet();
		
		ConcurrentHashMap<String,AtomicLong> dialogsReceivedByType =dialogsReceivedByTypeAndNetwork.get(networkID);
		if(dialogsReceivedByType==null) {
			dialogsReceivedByType=new ConcurrentHashMap<String,AtomicLong>();
			for(String currName:CAPServiceBaseImpl.getNames())
	    		dialogsReceivedByType.put(currName,new AtomicLong(0));
	        
			ConcurrentHashMap<String,AtomicLong> oldValue=dialogsReceivedByTypeAndNetwork.putIfAbsent(networkID, dialogsReceivedByType);
			if(oldValue!=null)
				dialogsReceivedByType=oldValue;
		}
		
		dialogsReceivedByType.get(dialogType).incrementAndGet();		
	}
}