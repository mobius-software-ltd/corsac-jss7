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

import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.Status;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class StatusImpl extends ParameterImpl implements Status {

    private int type;
    private int info;
    private ByteBuf value;

    public StatusImpl(int type, int info) {
        this.type = type;
        this.info = info;
        this.tag = Parameter.Status;
        encode();
    }

    public StatusImpl(ByteBuf data) {
        this.tag = Parameter.Status;

        this.type = 0;
        this.type |= data.readByte() & 0xFF;
        this.type <<= 8;
        this.type |= data.readByte() & 0xFF;

        this.info = 0;
        this.info |= data.readByte() & 0xFF;
        this.info <<= 8;
        this.info |= data.readByte() & 0xFF;
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = Unpooled.buffer(4);
        // encode routing context
        value.writeByte((byte) (type >> 8));
        value.writeByte((byte) (type));
        value.writeByte((byte) (info >> 8));
        value.writeByte((byte) (info));
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(value);
    }

    public int getInfo() {
        return this.info;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return String.format("Status type=%d info=%d", type, info);
    }

}
