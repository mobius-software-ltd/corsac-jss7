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
