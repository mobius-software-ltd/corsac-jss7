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

package org.restcomm.protocols.ss7.isup.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.isup.CircuitManager;
import org.restcomm.protocols.ss7.isup.ISUPMessageFactory;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.ISUPProvider;
import org.restcomm.protocols.ss7.isup.ISUPStack;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.AbstractISUPMessage;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageName;
import org.restcomm.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPart;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartBaseImpl;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartListener;

import io.netty.buffer.ByteBuf;

/**
 * Start time:12:14:57 2009-09-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class ISUPStackImpl implements ISUPStack, Mtp3UserPartListener {

    private Logger logger = LogManager.getLogger(ISUPStackImpl.class);

    private State state = State.IDLE;

    private Mtp3UserPart mtp3UserPart = null;
    private CircuitManager circuitManager = null;
    private ISUPProviderImpl provider;
    // local vars
    private ISUPMessageFactory messageFactory;
    private ISUPParameterFactory parameterFactory;

    private ScheduledExecutorService scheduledExecutorService;
    
    private ConcurrentHashMap<String, AtomicLong> messagesSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> messagesReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> bytesSentByType=new ConcurrentHashMap<String, AtomicLong>();
    private ConcurrentHashMap<String, AtomicLong> bytesReceivedByType=new ConcurrentHashMap<String, AtomicLong>();
    
    public ISUPStackImpl(final int localSpc, final int ni, final boolean automaticTimerMessages,int localThreads) {
        super();
        this.provider = new ISUPProviderImpl(this, ni, localSpc, automaticTimerMessages);
        this.parameterFactory = this.provider.getParameterFactory();
        this.messageFactory = this.provider.getMessageFactory();

        this.state = State.CONFIGURED;
        this.scheduledExecutorService=Executors.newScheduledThreadPool(localThreads);
        
        for(MessageName currType:MessageName.values()) {
        	messagesSentByType.put(currType.name(), new AtomicLong(0L));
        	messagesReceivedByType.put(currType.name(), new AtomicLong(0L));
        	bytesSentByType.put(currType.name(), new AtomicLong(0L));
        	bytesReceivedByType.put(currType.name(), new AtomicLong(0L));
        }
    }

    public ISUPStackImpl(int localSpc, int ni,int localThreads) {
        this(localSpc, ni, true, localThreads);
    }

    public ISUPProvider getIsupProvider() {
        return provider;
    }

    public void start() throws IllegalStateException {
        if (state != State.CONFIGURED) {
            throw new IllegalStateException("Stack has not been configured or is already running!");
        }
        if (state == State.RUNNING) {
            // throw new StartFailedException("Can not start stack again!");
            throw new IllegalStateException("Can not start stack again!");
        }
        if (this.mtp3UserPart == null) {
            throw new IllegalStateException("No Mtp3UserPart present!");
        }

        if (this.circuitManager == null) {
            throw new IllegalStateException("No CircuitManager present!");
        }
        // this.executor = Executors.newFixedThreadPool(1);
        // this.layer3exec = Executors.newFixedThreadPool(1);
        this.provider.start();
        
        this.mtp3UserPart.addMtp3UserPartListener(this);

        this.state = State.RUNNING;

    }

    public void stop() {
        if (state != State.RUNNING) {
            throw new IllegalStateException("Stack is not running!");
        }
        // if(state == State.CONFIGURED)
        // {
        // throw new IllegalStateException("Can not stop stack again!");
        // }

        this.mtp3UserPart.removeMtp3UserPartListener(this);

        this.scheduledExecutorService.shutdown();
        // this.executor.shutdown();
        // this.layer3exec.shutdown();
        this.provider.stop();
        this.state = State.CONFIGURED;

    }

    // ///////////////
    // CONF METHOD //
    // ///////////////
    /**
     *
     */

    public Mtp3UserPart getMtp3UserPart() {
        return mtp3UserPart;
    }

    public void setMtp3UserPart(Mtp3UserPart mtp3UserPart) {
        this.mtp3UserPart = mtp3UserPart;
    }

    public void setCircuitManager(CircuitManager mgr) {
        this.circuitManager = mgr;

    }

    public CircuitManager getCircuitManager() {
        return this.circuitManager;
    }

    public ScheduledExecutorService getExecutorService() {
    	return this.scheduledExecutorService;
    }
    // ---------------- private methods and class defs

    /**
     * @param message
     */
    void send(ISUPMessage message,Mtp3TransferPrimitive mtp3Message) throws IOException {

        if (this.state != State.RUNNING)
            return;

        // here we have encoded msg, nothing more, need to add MTP3 label.
        // txDataQueue.add(message);
        messagesSentByType.get(message.getMessageType().getMessageName().name()).incrementAndGet();
     	bytesSentByType.get(message.getMessageType().getMessageName().name()).addAndGet(mtp3Message.getData().readableBytes());
     	try {        	
            this.mtp3UserPart.sendMessage(mtp3Message);
        } catch (IOException e) {
            // log here Exceptions from MTP3 level
            logger.error("IOException when sending the message to MTP3 level: " + e.getMessage(), e);
            e.printStackTrace();
            throw e;
        }
    }

    private enum State {
        IDLE, CONFIGURED, RUNNING;
    }

    @Override
    public void onMtp3PauseMessage(Mtp3PausePrimitive arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMtp3ResumeMessage(Mtp3ResumePrimitive arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMtp3StatusMessage(Mtp3StatusPrimitive arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMtp3EndCongestionMessage(Mtp3EndCongestionPrimitive msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMtp3TransferMessage(Mtp3TransferPrimitive mtpMsg) {

        if (this.state != State.RUNNING)
            return;

        // process only ISUP messages
        if (mtpMsg.getSi() != Mtp3UserPartBaseImpl._SI_SERVICE_ISUP)
            return;

        // 2(CIC) + 1(CODE)
        ByteBuf payload = mtpMsg.getData();
        int bytes=payload.readableBytes();
        if(payload.readableBytes()<3)
        	throw new IllegalArgumentException("buffer must have atleast three readable octets");
        
        payload.markReaderIndex();
        payload.skipBytes(2);
        int commandCode = payload.readByte();
        payload.resetReaderIndex();
        AbstractISUPMessage msg = (AbstractISUPMessage) messageFactory.createCommand(commandCode);
        try {
            msg.decode(payload,messageFactory, parameterFactory);
        } catch (ParameterException e) {
            logger.error("Error decoding of incoming Mtp3TransferPrimitive" + e.getMessage(), e);
            e.printStackTrace();
        }
        msg.setSls(mtpMsg.getSls()); // store SLS...
        // should take here OPC or DPC????? since come in different direction looks like opc
        provider.receive(msg, mtpMsg.getOpc());
        
        messagesReceivedByType.get(msg.getMessageType().getMessageName().name()).incrementAndGet();
    	bytesReceivedByType.get(msg.getMessageType().getMessageName().name()).addAndGet(bytes);    	
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
}
