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

import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledPartyIDIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.OriginalCalledNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginalCalledNumber;

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
public class OriginalCalledPartyIDIsupImpl extends ASNOctetString implements OriginalCalledPartyIDIsup {
	public OriginalCalledPartyIDIsupImpl() {
		super("OriginalCalledPartyIDIsup",1,5,false);
    }

    public OriginalCalledPartyIDIsupImpl(OriginalCalledNumber originalCalledNumber) throws ASNParsingException {
    	super(translate(originalCalledNumber),"OriginalCalledPartyIDIsup",1,5,false);
    }

    public static ByteBuf translate(OriginalCalledNumber originalCalledNumber) throws ASNParsingException {
        if (originalCalledNumber == null)
            throw new ASNParsingException("The redirectingNumber parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((OriginalCalledNumberImpl) originalCalledNumber).encode(buffer);
        	return buffer;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when encoding redirectingNumber: " + e.getMessage(), e);
        }
    }

    public OriginalCalledNumber getOriginalCalledNumber() throws ASNParsingException {
        if (this.getValue() == null)
            throw new ASNParsingException("The data has not been filled");

        try {
        	OriginalCalledNumberImpl ocn = new OriginalCalledNumberImpl();
            ocn.decode(this.getValue());
            return ocn;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when decoding RedirectingNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OriginalCalledPartyIDIsup [");

        if (getValue() != null) {
            try {
            	OriginalCalledNumber rn = this.getOriginalCalledNumber();
                sb.append(", ");
                sb.append(rn.toString());
            } catch (ASNParsingException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
