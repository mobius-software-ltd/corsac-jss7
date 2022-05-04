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

package org.restcomm.protocols.ss7.sccp.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.MaxConnectionCountReached;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SccpManagementEventListener;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpStack;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpNoticeMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.sccp.message.MessageFactory;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author Oleg Kulikov
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SccpProviderImpl implements SccpProvider, Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(SccpProviderImpl.class);

    private transient SccpStackImpl stack;
    protected ConcurrentHashMap<Integer, SccpListener> ssnToListener = new ConcurrentHashMap<Integer, SccpListener>();
    protected ConcurrentHashMap<UUID,SccpManagementEventListener> managementEventListeners = new ConcurrentHashMap<UUID,SccpManagementEventListener>();

    private MessageFactoryImpl messageFactory;
    private ParameterFactoryImpl parameterFactory;

    SccpProviderImpl(SccpStackImpl stack) {
        this.stack = stack;
        this.messageFactory = stack.messageFactory;
        this.parameterFactory = new ParameterFactoryImpl();
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public ParameterFactory getParameterFactory() {
        return parameterFactory;
    }

    public void registerSccpListener(int ssn, SccpListener listener) {
    	SccpListener existingListener = ssnToListener.get(ssn);
        if (existingListener != null) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("Registering SccpListener=%s for already existing SccpListnere=%s for SSN=%d",
                        listener, existingListener, ssn));
            }
        }
        
        ssnToListener.put(ssn, listener);
        this.stack.broadcastChangedSsnState(ssn, true);
    }

    public void deregisterSccpListener(int ssn) {
    	SccpListener existingListener = ssnToListener.remove(ssn);
        if (existingListener == null) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("No existing SccpListnere=%s for SSN=%d", existingListener, ssn));
            }
        }
        this.stack.broadcastChangedSsnState(ssn, false);
    }

    public void registerManagementEventListener(UUID key,SccpManagementEventListener listener) {
    	if (this.managementEventListeners.containsKey(key))
    		return;
    	
    	this.managementEventListeners.put(key, listener);
    }

    public void deregisterManagementEventListener(UUID key) {
    	this.managementEventListeners.remove(key);
    }

    protected SccpListener getSccpListener(int ssn) {
        return ssnToListener.get(ssn);
    }

    protected ConcurrentHashMap<Integer, SccpListener> getAllSccpListeners() {
        return ssnToListener;
    }

    public SccpConnection newConnection(int localSsn, ProtocolClass protocol) throws MaxConnectionCountReached {
        return stack.newConnection(localSsn, protocol);
    }

    @Override
    public ConcurrentHashMap<Integer, SccpConnection> getConnections() {
        return stack.connections;
    }

    @Override
    public void send(SccpDataMessage message) throws IOException {
        try{
            SccpDataMessageImpl msg = ((SccpDataMessageImpl) message);
            stack.send(msg);
        }catch(Exception e){
            logger.error(e);
            throw new IOException(e);
        }
    }

    @Override
    public void send(SccpNoticeMessage message) throws IOException {
        try{
            SccpNoticeMessageImpl msg = ((SccpNoticeMessageImpl) message);
            stack.send(msg);
        }catch(Exception e){
            throw new IOException(e);
        }
    }

    public int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress, int msgNetworkId) {
        return this.stack.getMaxUserDataLength(calledPartyAddress, callingPartyAddress, msgNetworkId);
    }

    @Override
    public void coordRequest(int ssn) {
        // TODO Auto-generated method stub

    }

    @Override
    public SccpStack getSccpStack() {
        return this.stack;
    }
}