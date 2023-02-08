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

import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.ForwardCallIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;

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
public class ForwardCallIndicatorsIsupImpl extends ASNOctetString implements ForwardCallIndicatorsIsup {
	public ForwardCallIndicatorsIsupImpl() {
		super("ForwardCallIndicatorsIsup",2,2,false);
    }

    public ForwardCallIndicatorsIsupImpl(ForwardCallIndicators forwardCallIndicators) throws ASNParsingException {
        super(translate(forwardCallIndicators),"ForwardCallIndicatorsIsup",2,2,false);
    }

    public static ByteBuf translate(ForwardCallIndicators forwardCallIndicators) throws ASNParsingException {
        if (forwardCallIndicators == null)
            throw new ASNParsingException("The forwardCallIndicators parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((ForwardCallIndicatorsImpl) forwardCallIndicators).encode(buffer);
        	return buffer;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when encoding originalCalledNumber: " + e.getMessage(), e);
        }
    }

    public ForwardCallIndicators getForwardCallIndicators() throws ASNParsingException {
        if (this.getValue() == null)
            throw new ASNParsingException("The data has not been filled");

        try {
        	ForwardCallIndicatorsImpl ocn = new ForwardCallIndicatorsImpl();
            ocn.decode(this.getValue());
            return ocn;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when decoding OriginalCalledNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ForwardCallIndicatorsIsup [");

        if (getValue() != null) {
            sb.append("data=[");
            sb.append(printDataArr());
            sb.append("]");
            try {
                ForwardCallIndicators fci = this.getForwardCallIndicators();
                sb.append(", ");
                sb.append(fci.toString());
            } catch (ASNParsingException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
