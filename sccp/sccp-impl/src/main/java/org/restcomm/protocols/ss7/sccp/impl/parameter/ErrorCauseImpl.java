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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCause;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

import io.netty.buffer.ByteBuf;

public class ErrorCauseImpl extends AbstractParameter  implements ErrorCause {
	private static final long serialVersionUID = 1L;

	private ErrorCauseValue value;
    private int digValue;

    public ErrorCauseImpl() {
        value = ErrorCauseValue.UNQUALIFIED;
        this.digValue = value.getValue();
    }

    public ErrorCauseImpl(ErrorCauseValue value) {
        this.value = value;
        if (value != null)
            this.digValue = value.getValue();
    }

    public ErrorCauseImpl(int digValue) {
        this.digValue = digValue;
        value = ErrorCauseValue.getInstance(digValue);
    }

    public ErrorCauseValue getValue() {
        return value;
    }

    public int getDigitalValue() {
        return digValue;
    }

    @Override
    public void decode(ByteBuf b, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        if (b.readableBytes() < 1) {
            throw new ParseException();
        }
        this.digValue = b.readByte();
        this.value = ErrorCauseValue.getInstance(digValue);
    }

    @Override
    public void encode(ByteBuf b, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        b.writeByte((byte)this.digValue);
    }

    public String toString() {
        if (this.value != null)
            return new StringBuffer().append("ErrorCause [").append("value=").append(value).append("]").toString();
        else {
            return new StringBuffer().append("ErrorCause [").append("digValue=").append(digValue).append("]").toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorCauseImpl that = (ErrorCauseImpl) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return digValue;
    }
}
