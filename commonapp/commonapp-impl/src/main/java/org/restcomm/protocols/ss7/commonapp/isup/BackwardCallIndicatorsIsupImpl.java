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

import org.restcomm.protocols.ss7.commonapp.api.isup.BackwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.BackwardCallIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.BackwardCallIndicators;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class BackwardCallIndicatorsIsupImpl extends ASNOctetString implements BackwardCallIndicatorsIsup {
	public BackwardCallIndicatorsIsupImpl() {
		super("BackwardCallIndicatorsIsup",2,2,false);
    }

    public BackwardCallIndicatorsIsupImpl(BackwardCallIndicators BackwardCallIndicators) throws ASNParsingException {
        super(translate(BackwardCallIndicators),"BackwardCallIndicatorsIsup",2,2,false);
    }

    private static ByteBuf translate(BackwardCallIndicators BackwardCallIndicators) throws ASNParsingException {
        if (BackwardCallIndicators == null)
            throw new ASNParsingException("The BackwardCallIndicators parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((BackwardCallIndicatorsImpl) BackwardCallIndicators).encode(buffer);
        	return buffer;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when encoding originalCalledNumber: " + e.getMessage(), e);
        }
    }

    public BackwardCallIndicators getBackwardCallIndicators() throws ASNParsingException {
        if (this.getValue() == null)
            throw new ASNParsingException("The data has not been filled");

        try {
        	BackwardCallIndicatorsImpl ocn = new BackwardCallIndicatorsImpl();
            ocn.decode(this.getValue());
            return ocn;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when decoding OriginalCalledNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BackwardCallIndicatorsIsup [");

        if (getValue() != null) {
            try {
                BackwardCallIndicators fci = this.getBackwardCallIndicators();
                sb.append(", ");
                sb.append(fci.toString());
            } catch (ASNParsingException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
