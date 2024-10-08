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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

import io.netty.buffer.ByteBuf;
/**
 * 
 * @author yulianoifa
 *
 */
public class LocalReferenceImpl extends AbstractParameter implements LocalReference {
	private static final long serialVersionUID = 1L;

	private int value;

    public LocalReferenceImpl() {
    }

    public LocalReferenceImpl(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void decode(ByteBuf buffer, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        if (buffer.readableBytes() < 3) {
            throw new ParseException();
        }
        this.value =  (int)buffer.readByte() << 16 & 0xFFFFFF;
        this.value |= (int)buffer.readByte() << 8 & 0xFFFF;
        this.value |= (int)buffer.readByte() & 0xFF;

    }

    @Override
    public void encode(ByteBuf buffer, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	buffer.writeByte((byte) (this.value >> 16 & 0xFF));
    	buffer.writeByte((byte) (this.value >> 8 & 0xFF));
    	buffer.writeByte((byte) (this.value      & 0xFF));    	
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalReferenceImpl that = (LocalReferenceImpl) o;

        return value == that.value;

    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return new StringBuffer().append("LocalReference [").append("value=").append(value).append("]").toString();
    }
}
