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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCause;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

import io.netty.buffer.ByteBuf;
/**
 * 
 * @author yulianoifa
 *
 */
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
