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

import java.util.Arrays;

import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
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
