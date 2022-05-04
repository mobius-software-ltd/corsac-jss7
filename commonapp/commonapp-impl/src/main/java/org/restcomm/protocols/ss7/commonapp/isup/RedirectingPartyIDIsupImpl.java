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

import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.RedirectingNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectingNumber;

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
public class RedirectingPartyIDIsupImpl extends ASNOctetString implements RedirectingPartyIDIsup {
	public RedirectingPartyIDIsupImpl() {
		super("RedirectingPartyIDIsup",2,10,false);
    }

    public RedirectingPartyIDIsupImpl(RedirectingNumber redirectingNumber) throws ASNParsingException {
        super(translate(redirectingNumber),"RedirectingPartyIDIsup",2,10,false);
    }

    public static ByteBuf translate(RedirectingNumber redirectingNumber) throws ASNParsingException {
        if (redirectingNumber == null)
            throw new ASNParsingException("The redirectingNumber parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((RedirectingNumberImpl) redirectingNumber).encode(buffer);
            return buffer;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when encoding redirectingNumber: " + e.getMessage(), e);
        }
    }

    public RedirectingNumber getRedirectingNumber() throws ASNParsingException {
        if (this.getValue() == null)
            throw new ASNParsingException("The data has not been filled");

        try {
            RedirectingNumberImpl ocn = new RedirectingNumberImpl();
            ocn.decode(this.getValue());
            return ocn;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when decoding RedirectingNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RedirectingPartyIDCap [");

        if (getValue() != null) {
            try {
                RedirectingNumber rn = this.getRedirectingNumber();
                sb.append(", ");
                sb.append(rn.toString());
            } catch (ASNParsingException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}