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

import org.restcomm.protocols.ss7.commonapp.api.isup.GenericNumberIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 * @author tamas gyorgyey
 * @author yulianoifa
 */
public class GenericNumberIsupImpl extends ASNOctetString implements GenericNumberIsup {
	public GenericNumberIsupImpl() {
		super("GenericNumberIsup",3,11,false);
    }

    public GenericNumberIsupImpl(GenericNumber genericNumber) throws ASNParsingException {
        super(translate(genericNumber),"GenericNumberIsup",3,11,false);
    }

    public static ByteBuf translate(GenericNumber genericNumber) throws ASNParsingException {
        if (genericNumber == null)
            throw new ASNParsingException("The genericNumber parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((GenericNumberImpl) genericNumber).encode(buffer);
            return buffer;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when encoding genericNumber: " + e.getMessage(), e);
        }
    }

    public GenericNumber getGenericNumber() throws ASNParsingException {
        if (this.getValue() == null)
            throw new ASNParsingException("The data has not been filled");

        try {
            GenericNumberImpl ocn = new GenericNumberImpl();
            ocn.decode(this.getValue());
            return ocn;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when decoding GenericNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GenericNumberCap [");
        
        if (getValue() != null) {
            try {
                GenericNumber gn = this.getGenericNumber();
                sb.append(", ");
                sb.append(gn.toString());
            } catch (ASNParsingException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
