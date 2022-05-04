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

package org.restcomm.protocols.ss7.commonapp.isup;

import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;

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
public class CallingPartyNumberIsupImpl extends ASNOctetString implements CallingPartyNumberIsup {
	public CallingPartyNumberIsupImpl() {
		super("CallingPartyNumberIsup",2,10,false);
    }

    public CallingPartyNumberIsupImpl(CallingPartyNumber callingPartyNumber) throws ASNParsingException {
        super(translate(callingPartyNumber),"CallingPartyNumberIsup",2,10,false);
    }

    public static ByteBuf translate(CallingPartyNumber callingPartyNumber) throws ASNParsingException {
        if (callingPartyNumber == null)
            throw new ASNParsingException("The callingPartyNumber parameter must not be null");
        try {
        	ByteBuf buf=Unpooled.buffer();
        	((CallingPartyNumberImpl) callingPartyNumber).encode(buf);
        	return buf;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when encoding callingPartyNumber: " + e.getMessage(), e);
        }
    }

    public CallingPartyNumber getCallingPartyNumber() throws ASNParsingException {
        if (this.getValue() == null)
            throw new ASNParsingException("The data has not been filled");

        try {
            CallingPartyNumberImpl ln = new CallingPartyNumberImpl();
            ln.decode(this.getValue());
            return ln;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when decoding CallingPartyNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallingPartyNumberCap [");

        if (getValue() != null) {
            sb.append("data=[");
            sb.append(printDataArr());
            sb.append("]");
            try {
                CallingPartyNumber cpn = this.getCallingPartyNumber();
                sb.append(", ");
                sb.append(cpn.toString());
            } catch (ASNParsingException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}