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

package org.restcomm.protocols.ss7.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.MAPStack;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class MAPStackImpl implements MAPStack {

    protected TCAPStack tcapStack = null;

    protected MAPProviderImpl mapProvider = null;

    private State state = State.IDLE;

    private final String name;
    private final MAPStackConfigurationManagement mapCfg;

    private ConcurrentHashMap<String, AtomicLong> messagesSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> messagesReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> errorsSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> errorsReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> dialogsSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> dialogsReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    
    public MAPStackImpl(String name, SccpProvider sccpPprovider, int ssn) {
        this.name = name;
        this.tcapStack = new TCAPStackImpl(name, sccpPprovider, ssn, 4);
        TCAPProvider tcapProvider = tcapStack.getProvider();
        this.mapCfg = new MAPStackConfigurationManagement();
        mapProvider = new MAPProviderImpl(name, this, tcapProvider,this.mapCfg);
        this.state = State.CONFIGURED;
        initCounters();
    }

    public MAPStackImpl(String name, TCAPProvider tcapProvider) {
        this.name = name;
        this.mapCfg = new MAPStackConfigurationManagement();
        mapProvider = new MAPProviderImpl(name, this, tcapProvider,this.mapCfg);
        this.tcapStack = tcapProvider.getStack();
        this.state = State.CONFIGURED;   
        initCounters();
    }
    
    private void initCounters() {
    	for(String currName:MAPServiceBaseImpl.getNames()) {
    		dialogsSentByType.put(currName,new AtomicLong(0));
    		dialogsReceivedByType.put(currName,new AtomicLong(0));
        } 
    	
    	for(MAPMessageType currType:MAPMessageType.values()) {
    		messagesSentByType.put(currType.name(),new AtomicLong(0));
    		messagesReceivedByType.put(currType.name(),new AtomicLong(0));
        }  
    	
    	messagesSentByType.put("unknown",new AtomicLong(0));
		messagesReceivedByType.put("unknown",new AtomicLong(0));
		
		for(String currName:MAPErrorCode.getAllNames()) {
    		errorsSentByType.put(currName,new AtomicLong(0));
    		errorsReceivedByType.put(currName,new AtomicLong(0));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MAPProvider getMAPProvider() {
        return this.mapProvider;
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
        this.mapProvider.start();

        this.state = State.RUNNING;

    }

    @Override
    public void stop() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Stack is not running!");
        }
        this.mapProvider.stop();
        if (tcapStack != null) {
            this.tcapStack.stop();
        }

        this.state = State.CONFIGURED;
    }

    // // ///////////////
    // // CONF METHOD //
    // // ///////////////
    // /**
    // * @throws ConfigurationException
    // *
    // */
    // public void configure(Properties props) throws ConfigurationException {
    // if (state != State.IDLE) {
    // throw new
    // IllegalStateException("Stack already been configured or is already running!");
    // }
    // tcapStack.configure(props);
    // TCAPProvider tcapProvider = tcapStack.getProvider();
    // mapProvider = new MAPProviderImpl(tcapProvider);
    // this.state = State.CONFIGURED;
    // }

    private enum State {
        IDLE, CONFIGURED, RUNNING;
    }

    @Override
    public TCAPStack getTCAPStack() {
        return this.tcapStack;
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
	
	public void newMessageReceived(String messageType) {
		messagesSentByType.get(messageType).incrementAndGet();
	}
	
	public void newMessageSent(String messageType) {
		messagesReceivedByType.get(messageType).incrementAndGet();
	}
	
	public void newErrorReceived(String errorType) {
		errorsSentByType.get(errorType).incrementAndGet();
	}
	
	public void newErrorSent(String errorType) {
		errorsReceivedByType.get(errorType).incrementAndGet();
	}
	
	public void newDialogReceived(String dialogType) {
		dialogsSentByType.get(dialogType).incrementAndGet();
	}
	
	public void newDialogSent(String dialogType) {
		dialogsReceivedByType.get(dialogType).incrementAndGet();
	}
}