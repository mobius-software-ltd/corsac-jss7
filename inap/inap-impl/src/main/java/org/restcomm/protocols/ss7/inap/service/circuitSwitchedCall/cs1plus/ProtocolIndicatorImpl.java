/*
 * Mobius Software LTD
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
		super("ProtocolIndicator",2,2,false);
    }

	public ProtocolIndicatorImpl(ProtocolIdentifier protocolIdentifier,TCAPDialogueLevel tcapDialogueLevel) {
		super(translate(protocolIdentifier, tcapDialogueLevel),"ProtocolIndicator",2,2,false);
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
