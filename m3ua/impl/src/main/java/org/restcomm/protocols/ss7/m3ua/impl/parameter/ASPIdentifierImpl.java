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

package org.restcomm.protocols.ss7.m3ua.impl.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
/**
 * 
 * @author yulianoifa
 *
 */
public class ASPIdentifierImpl extends ParameterImpl implements ASPIdentifier {

    private long aspID = 0;
    private ByteBuf value;
    
    protected ASPIdentifierImpl(ByteBuf value) {
    	this.tag = Parameter.ASP_Identifier;

        this.value = Unpooled.wrappedBuffer(value);
        
        this.aspID = 0;
        this.aspID |= value.readByte() & 0xFF;
        this.aspID <<= 8;
        this.aspID |= value.readByte() & 0xFF;
        this.aspID <<= 8;
        this.aspID |= value.readByte() & 0xFF;
        this.aspID <<= 8;
        this.aspID |= value.readByte() & 0xFF;        
    }

    protected ASPIdentifierImpl(long id) {
    	this.tag = Parameter.ASP_Identifier;
        aspID = id;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = Unpooled.buffer(4);

        // encode asp identifier
        value.writeByte((byte) (aspID >> 24));
        value.writeByte((byte) (aspID >> 16));
        value.writeByte((byte) (aspID >> 8));
        value.writeByte((byte) (aspID));
    }

    public long getAspId() {
        return aspID;
    }

    @Override
    protected ByteBuf getValue() {
    	return Unpooled.wrappedBuffer(value);
    }

    @Override
    public String toString() {
        return String.format("ASPIdentifier id=%d", aspID);
    }
}
