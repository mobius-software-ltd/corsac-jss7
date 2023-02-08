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

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;

/**
 *
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public class ReturnCauseImpl extends AbstractParameter implements ReturnCause {
	private static final long serialVersionUID = 1L;

	private ReturnCauseValue value;
    private int digValue;

    public ReturnCauseImpl() {
        value = ReturnCauseValue.UNQALIFIED;
    }

    public ReturnCauseImpl(ReturnCauseValue value) {
        this.value = value;
        if (value != null)
            this.digValue = value.getValue();
    }

    public ReturnCauseImpl(int digValue) {
        this.digValue = digValue;
        value = ReturnCauseValue.getInstance(digValue);
    }

    public ReturnCauseValue getValue() {
        return value;
    }

    public int getDigitalValue() {
        return digValue;
    }

    @Override
    public void decode(ByteBuf buffer, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        this.digValue = buffer.readByte() & 0xff;

        this.value = ReturnCauseValue.getInstance(this.digValue);
    }

    @Override
    public void encode(ByteBuf buffer, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	buffer.writeByte((byte) this.digValue);
    }

    public String toString() {
        if (this.value != null)
            return new StringBuffer().append("ReturnCause [").append("value=").append(value).append("]").toString();
        else {
            return new StringBuffer().append("ReturnCause [").append("digValue=").append(digValue).append("]").toString();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + digValue;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReturnCauseImpl other = (ReturnCauseImpl) obj;
        if (digValue != other.digValue)
            return false;
        return true;
    }
}
