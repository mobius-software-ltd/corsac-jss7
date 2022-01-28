/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ProtocolIdentifier;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ProtocolIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.TCAPDialogueLevel;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class ProtocolIndicatorImpl extends ASNOctetString implements ProtocolIndicator {
	public ProtocolIndicatorImpl() {
    }

	public ProtocolIndicatorImpl(ProtocolIdentifier protocolIdentifier,TCAPDialogueLevel tcapDialogueLevel) {
		super(translate(protocolIdentifier, tcapDialogueLevel));
	}
	
    public static ByteBuf translate(ProtocolIdentifier protocolIdentifier,TCAPDialogueLevel tcapDialogueLevel) {
    	if(protocolIdentifier!=null || tcapDialogueLevel!=null) {
    		ByteBuf value=Unpooled.buffer(2);
    		if(protocolIdentifier!=null)
    			value.writeByte((byte)protocolIdentifier.getCode());
    		else
    			value.writeByte(0);
    		
    		if(tcapDialogueLevel!=null)
    			value.writeByte((byte)tcapDialogueLevel.getCode());
    		else
    			value.writeByte(0);
    		
    		return value; 	
    	}
    	
    	return null;
    }

    public ProtocolIdentifier getProtocolIdentifier() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 2)
            return null;

        return ProtocolIdentifier.getInstance(data.readByte() & 0x0F);
    }

    public TCAPDialogueLevel getTCAPDialogueLevel() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 2)
            return null;

        data.readByte();
        return TCAPDialogueLevel.getInstance(data.readByte() & 0x0F);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ProtocolIndicator [");
        
        ProtocolIdentifier protocolIdentifier=getProtocolIdentifier();
        if (protocolIdentifier != null) {
            sb.append("protocolIdentifier=");
            sb.append(protocolIdentifier);            
        }
        
        TCAPDialogueLevel tcapDialogueLevel=getTCAPDialogueLevel();
        if (tcapDialogueLevel != null) {
            sb.append("tcapDialogueLevel=");
            sb.append(tcapDialogueLevel);            
        }
        
        sb.append("]");

        return sb.toString();
    }
}
