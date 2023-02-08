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

import org.restcomm.protocols.ss7.m3ua.parameter.CorrelationId;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class CorrelationIdImpl extends ParameterImpl implements CorrelationId {

    private long corrId;
    private ByteBuf value;

    protected CorrelationIdImpl(long corrId) {
        this.corrId = corrId;        
        this.tag = Parameter.Correlation_ID;
        encode();
    }

    protected CorrelationIdImpl(ByteBuf data) {
        this.corrId = 0;
        this.corrId |= data.readByte() & 0xFF;
        this.corrId <<= 8;
        this.corrId |= data.readByte() & 0xFF;
        this.corrId <<= 8;
        this.corrId |= data.readByte() & 0xFF;
        this.corrId <<= 8;
        this.corrId |= data.readByte() & 0xFF;
        this.tag = Parameter.Correlation_ID;
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = Unpooled.buffer(4);
        // encode routing context
        value.writeByte((byte) (corrId >> 24));
        value.writeByte((byte) (corrId >> 16));
        value.writeByte((byte) (corrId >> 8));
        value.writeByte((byte) (corrId));
    }

    public long getCorrelationId() {
        return this.corrId;
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(value);
    }

    @Override
    public String toString() {
        return String.format("CorrelationId id=%d", corrId);
    }

}
