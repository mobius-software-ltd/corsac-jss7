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

import org.restcomm.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
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
        this.value = data;

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
        this.encode();
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
