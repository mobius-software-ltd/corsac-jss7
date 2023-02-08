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

import org.restcomm.protocols.ss7.m3ua.parameter.ConcernedDPC;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class ConcernedDPCImpl extends ParameterImpl implements ConcernedDPC {

    private int pointCode;
    private ByteBuf value;
    
    protected ConcernedDPCImpl(int pointCode) {
        this.pointCode = pointCode;
        this.tag = Parameter.Concerned_Destination;
        encode();
    }

    protected ConcernedDPCImpl(ByteBuf data) {
        // data[0] is reserved
    	data.skipBytes(1);
    	
        this.pointCode = 0;
        this.pointCode |= data.readByte() & 0xFF;
        this.pointCode <<= 8;
        this.pointCode |= data.readByte() & 0xFF;
        this.pointCode <<= 8;
        this.pointCode |= data.readByte() & 0xFF;
        this.tag = Parameter.Concerned_Destination;
    }

    protected void encode() {
    	this.value = Unpooled.buffer(4);
        // reserved
        value.writeByte(0);

        // DPC
        value.writeByte((byte) (pointCode >>> 16));
        value.writeByte((byte) (pointCode >>> 8));
        value.writeByte((byte) (pointCode));
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(this.value);
    }

    @Override
    public String toString() {
        return String.format("ConcernedDPC dpc=%d", pointCode);
    }

    public int getPointCode() {
        return this.pointCode;
    }

}
