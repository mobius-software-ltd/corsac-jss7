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

import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardGVNSIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.ForwardGVNSImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardGVNS;

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
public class ForwardGVNSIsupImpl extends ASNOctetString implements ForwardGVNSIsup {
	public ForwardGVNSIsupImpl() {
		super("ForwardGVNSIsup",null,null,false);
    }

    public ForwardGVNSIsupImpl(ForwardGVNS forwardGVNS) throws ASNParsingException {
        super(translate(forwardGVNS),"ForwardGVNSIsup",null,null,false);
    }

    public static ByteBuf translate(ForwardGVNS forwardGVNS) throws ASNParsingException {
        if (forwardGVNS == null)
            throw new ASNParsingException("The forwardGVNS parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((ForwardGVNSImpl) forwardGVNS).encode(buffer);
            return buffer;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when encoding originalCalledNumber: " + e.getMessage(), e);
        }
    }

    public ForwardGVNS getForwardGVNS() throws ASNParsingException {
        if (this.getValue() == null)
            throw new ASNParsingException("The data has not been filled");

        try {
        	ForwardGVNSImpl ocn = new ForwardGVNSImpl();
            ocn.decode(this.getValue());
            return ocn;
        } catch (ParameterException e) {
            throw new ASNParsingException("ParameterException when decoding OriginalCalledNumber: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ForwardGVNSIsup [");

        if (getValue() != null) {
            sb.append("data=[");
            sb.append(printDataArr());
            sb.append("]");
            try {
                ForwardGVNS fg = this.getForwardGVNS();
                sb.append(", ");
                sb.append(fg.toString());
            } catch (ASNParsingException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
