/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.commonapp.isup;

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CalledPartyNumberIsupImpl extends ASNOctetString implements CalledPartyNumberIsup {
	public CalledPartyNumberIsupImpl() {
		super("CalledPartyNumberIsup",2,18,false);
    }

    public CalledPartyNumberIsupImpl(CalledPartyNumber calledPartyNumber) throws ASNParsingException {
        super(translate(calledPartyNumber),"CalledPartyNumberIsup",2,18,false);
    }

    public static ByteBuf translate(CalledPartyNumber calledPartyNumber) throws ASNParsingException {
        if (calledPartyNumber == null)
            throw new ASNParsingException("The calledPartyNumber parameter must not be null");
        try {
        	ByteBuf buf=Unpooled.buffer();
        	((CalledPartyNumberImpl) calledPartyNumber).encode(buf);
        	return buf;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when encoding calledPartyNumber: " + e.getMessage(), e);
        }
    }

    public CalledPartyNumber getCalledPartyNumber() throws ASNParsingException {
        if (this.getValue() == null)
            throw new ASNParsingException("The data has not been filled");

        try {
            CalledPartyNumberImpl ln = new CalledPartyNumberImpl();
            ln.decode(this.getValue());
            return ln;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when decoding CalledPartyNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CalledPartyNumberCap [");

        if (getValue() != null) {
            try {
                CalledPartyNumber cpn = this.getCalledPartyNumber();
                sb.append(", ");
                sb.append(cpn.toString());
            } catch (ASNParsingException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
