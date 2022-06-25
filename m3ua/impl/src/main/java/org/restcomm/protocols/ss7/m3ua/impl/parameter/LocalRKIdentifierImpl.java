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

import org.restcomm.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class LocalRKIdentifierImpl extends ParameterImpl implements LocalRKIdentifier {

    private ByteBuf value;
    private long id;

    public LocalRKIdentifierImpl() {
        this.tag = Parameter.Local_Routing_Key_Identifier;
    }

    protected LocalRKIdentifierImpl(ByteBuf data) {
        this.tag = Parameter.Local_Routing_Key_Identifier;
        
        this.id = 0;
        this.id |= data.readByte() & 0xFF;
        this.id <<= 8;
        this.id |= data.readByte() & 0xFF;
        this.id <<= 8;
        this.id |= data.readByte() & 0xFF;
        this.id <<= 8;
        this.id |= data.readByte() & 0xFF;
    }

    protected LocalRKIdentifierImpl(long id) {
        this.tag = Parameter.Local_Routing_Key_Identifier;
        this.id = id;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = Unpooled.buffer(4);
        // encode routing context
        value.writeByte((byte) (this.id >> 24));
        value.writeByte((byte) (this.id >> 16));
        value.writeByte((byte) (this.id >> 8));
        value.writeByte((byte) (this.id));        
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(this.value);
    }

    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.format("LocalRKIdentifier id=%d", id);
    }
}
