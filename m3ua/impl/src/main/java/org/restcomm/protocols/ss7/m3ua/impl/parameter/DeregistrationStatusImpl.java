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

import org.restcomm.protocols.ss7.m3ua.parameter.DeregistrationStatus;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class DeregistrationStatusImpl extends ParameterImpl implements DeregistrationStatus {

    private int status;
    private ByteBuf value;
    
    public DeregistrationStatusImpl(int status) {
        this.tag = Parameter.Deregistration_Status;
        this.status = status;
        encode();
    }

    public DeregistrationStatusImpl(ByteBuf data) {
        this.tag = Parameter.Deregistration_Status;
        this.status = 0;
        this.status |= data.readByte() & 0xFF;
        this.status <<= 8;
        this.status |= data.readByte() & 0xFF;
        this.status <<= 8;
        this.status |= data.readByte() & 0xFF;
        this.status <<= 8;
        this.status |= data.readByte() & 0xFF;
    }

    protected void encode() {
    	this.value = Unpooled.buffer(4);
        value.writeByte((byte) (status >>> 24));
        value.writeByte((byte) (status >>> 16));
        value.writeByte((byte) (status >>> 8));
        value.writeByte((byte) (status));
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(this.value);
    }

    public int getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return String.format("DeregistrationStatus = %d", status);
    }

}
