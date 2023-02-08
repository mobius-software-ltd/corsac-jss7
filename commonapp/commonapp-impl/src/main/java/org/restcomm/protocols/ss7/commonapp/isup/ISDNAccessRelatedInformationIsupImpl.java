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

import org.restcomm.protocols.ss7.commonapp.api.isup.ISDNAccessRelatedInformationIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.accessTransport.AccessTransportImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

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
public class ISDNAccessRelatedInformationIsupImpl extends ASNOctetString implements ISDNAccessRelatedInformationIsup {
	public ISDNAccessRelatedInformationIsupImpl() {
		super("ISDNAccessRelatedInformationIsup",null,null,false);
    }

    public ISDNAccessRelatedInformationIsupImpl(LocationNumber locationNumber) throws ASNParsingException {
        super(translate(locationNumber),"ISDNAccessRelatedInformationIsup",3,11,false);
    }

    public static ByteBuf translate(LocationNumber locationNumber) throws ASNParsingException {
        if (locationNumber == null)
            throw new ASNParsingException("The locationNumber parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((LocationNumberImpl) locationNumber).encode(buffer);
            return buffer;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when encoding locationNumber: " + e.getMessage(), e);
        }
    }

    public AccessTransport getAccessTransport() throws ASNParsingException {
        if (this.getValue() == null)
            throw new ASNParsingException("The data has not been filled");

        try {
        	AccessTransportImpl ln = new AccessTransportImpl();
            ln.decode(this.getValue());
            return ln;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when decoding LocationNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ISDNAccessRelatedInformationIsup [");

        if (getValue() != null) {
            sb.append("data=[");
            sb.append(printDataArr());
            sb.append("]");
            try {
                AccessTransport ln = this.getAccessTransport();
                sb.append(", ");
                sb.append(ln.toString());
            } catch (ASNParsingException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
