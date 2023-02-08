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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.RemoteOperationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.RemoteOperations;
import org.restcomm.protocols.ss7.isup.message.parameter.RemoteOperationsPortionImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;

/**
 * Start time:17:24:08 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class RemoteOperationsImpl extends AbstractISUPParameter implements RemoteOperations {
	private RemoteOperationsPortionImpl remoteOperations = new RemoteOperationsPortionImpl();
    private byte protocol = RemoteOperations.PROTOCOL_REMOTE_OPERATIONS;

    private static ASNParser parser=new ASNParser();
    
    static {
    	parser.loadClass(RemoteOperationsPortionImpl.class);
    }
    // FIXME: XXX
    // Q.763 3.48, requires a lot of hacks
    public RemoteOperationsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public RemoteOperationsImpl() {
        super();

    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b.readableBytes() < 1) {
            throw new ParameterException();
        }
        
        byte curr=b.readByte();
        this.protocol = (byte) (curr & 0x1F);
        if ((curr & 0x80) > 0) {
            if (b.readableBytes() > 1) {
                throw new ParameterException();
            }
            return;
        }

        try {
        	Object output=parser.decode(Unpooled.wrappedBuffer(b)); 
        	if(!(output instanceof RemoteOperationsPortionImpl))
        		throw new ParameterException("Invalid asn content found");
        } catch (ASNException e) {
            throw new ParameterException(e);
        }
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        if (remoteOperations.getComponents()==null || remoteOperations.getComponents().size() == 0) {
        	buffer.writeByte(0x80);
        	buffer.writeByte(this.protocol);            
        } else {
        	try {
        		parser.encode(remoteOperations);
        	}
        	catch(ASNException ex) {
        		throw new ParameterException(ex.getMessage());
        	}
        }
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public void setProtocol(byte protocol) {
        this.protocol = protocol;
    }

    @Override
    public byte getProtocol() {
        return this.protocol;
    }

    @Override
    public void setOperations(List<RemoteOperationImpl> operations) {
    	if(this.remoteOperations==null)
    		this.remoteOperations=new RemoteOperationsPortionImpl();
    	
    	this.remoteOperations.setComponents(operations);        
    }

    @Override
    public List<RemoteOperationImpl> getOperations() {
    	if(this.remoteOperations==null)
    		return null;
    	
        return this.remoteOperations.getComponents();
    }
}