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
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCauseValue;

import io.netty.buffer.ByteBuf;
/**
 * 
 * @author yulianoifa
 *
 */
public class ResetCauseImpl extends AbstractParameter  implements ResetCause {
	private static final long serialVersionUID = 1L;

	private ResetCauseValue value;
    private int digValue;

    public ResetCauseImpl() {
        value = ResetCauseValue.UNQUALIFIED;
        this.digValue = value.getValue();
    }

    public ResetCauseImpl(ResetCauseValue value) {
        this.value = value;
        if (value != null)
            this.digValue = value.getValue();
    }

    public ResetCauseImpl(int digValue) {
        this.digValue = digValue;
        value = ResetCauseValue.getInstance(digValue);
    }

    public ResetCauseValue getValue() {
        return value;
    }

    public int getDigitalValue() {
        return digValue;
    }

    @Override
    public void decode(ByteBuf buffer, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        if (buffer.readableBytes() < 1) {
            throw new ParseException();
        }
        this.digValue = buffer.readByte();
        this.value = ResetCauseValue.getInstance(this.digValue);
    }

    @Override
    public void encode(ByteBuf buffer, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	buffer.writeByte((byte)this.digValue);
    }

    public String toString() {
        if (this.value != null)
            return new StringBuffer().append("ResetCause [").append("value=").append(value).append("]").toString();
        else {
            return new StringBuffer().append("ResetCause [").append("digValue=").append(digValue).append("]").toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResetCauseImpl that = (ResetCauseImpl) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return digValue;
    }
}
