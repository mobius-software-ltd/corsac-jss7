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

package org.restcomm.protocols.ss7.m3ua.impl.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 *
 * @author amit bhayani
 *
 */
public class RoutingContextImpl extends ParameterImpl implements RoutingContext {

    private long[] rcs = null;
    private ByteBuf value;

    public RoutingContextImpl() {
        this.tag = Parameter.Routing_Context;
    }

    protected RoutingContextImpl(ByteBuf value) {
        this.tag = Parameter.Routing_Context;

        int arrSize = 0;
        rcs = new long[(value.readableBytes() / 4)];

        while (value.readableBytes()>0) {
            rcs[arrSize] = 0;
            rcs[arrSize] |= value.readByte() & 0xFF;
            rcs[arrSize] <<= 8;
            rcs[arrSize] |= value.readByte() & 0xFF;
            rcs[arrSize] <<= 8;
            rcs[arrSize] |= value.readByte() & 0xFF;
            rcs[arrSize] <<= 8;
            rcs[arrSize++] |= value.readByte() & 0xFF;
        }

        this.value = value;
    }

    protected RoutingContextImpl(long[] routingcontexts) {
        this.tag = Parameter.Routing_Context;
        rcs = routingcontexts;
        encode();
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = Unpooled.buffer((rcs.length * 4));
        for(int arrSize = 0;arrSize<rcs.length;arrSize++) {        	
            value.writeByte((byte) (rcs[arrSize] >>> 24));
            value.writeByte((byte) (rcs[arrSize] >>> 16));
            value.writeByte((byte) (rcs[arrSize] >>> 8));
            value.writeByte((byte) (rcs[arrSize]));
        }
    }

    public long[] getRoutingContexts() {
        return this.rcs;
    }

    @Override
    protected ByteBuf getValue() {
        return Unpooled.wrappedBuffer(value);
    }

    @Override
    public String toString() {
        return String.format("RoutingContext rc=%s", Arrays.toString(rcs));
    }
}
