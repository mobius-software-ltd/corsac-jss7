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

import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 * @author amit bhayani
 * @author kulikov
 * @author yulianoifa
 */
public class NetworkAppearanceImpl extends ParameterImpl implements NetworkAppearance {

    private long na;
    private ByteBuf value;

    public NetworkAppearanceImpl() {
        this.tag = Parameter.Network_Appearance;
    }

    protected NetworkAppearanceImpl(long value) {
        this.na = value;
        this.tag = Parameter.Network_Appearance;
    }

    protected NetworkAppearanceImpl(ByteBuf data) {
        this.na = 0;
        this.na |= data.readByte() & 0xFF;
        this.na <<= 8;
        this.na |= data.readByte() & 0xFF;
        this.na <<= 8;
        this.na |= data.readByte() & 0xFF;
        this.na <<= 8;
        this.na |= data.readByte() & 0xFF;
        this.tag = Parameter.Network_Appearance;
    }

    public long getNetApp() {
        return na;
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(value);
    }
    
    private void encode() {
    	this.value = Unpooled.buffer(4);
        // encode routing context
        value.writeByte((byte) (na >> 24));
        value.writeByte((byte) (na >> 16));
        value.writeByte((byte) (na >> 8));
        value.writeByte((byte) (na));
    }

    @Override
    public String toString() {
        return String.format("NetworkAppearance value=%d", value);
    }
}
