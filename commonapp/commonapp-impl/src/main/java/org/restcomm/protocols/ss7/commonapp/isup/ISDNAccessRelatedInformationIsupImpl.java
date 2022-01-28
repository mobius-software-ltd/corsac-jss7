/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.isup;

import org.restcomm.protocols.ss7.commonapp.api.APPException;
import org.restcomm.protocols.ss7.commonapp.api.isup.ISDNAccessRelatedInformationIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.accessTransport.AccessTransportImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class ISDNAccessRelatedInformationIsupImpl extends ASNOctetString2 implements ISDNAccessRelatedInformationIsup {
	public ISDNAccessRelatedInformationIsupImpl() {
    }

    public ISDNAccessRelatedInformationIsupImpl(LocationNumber locationNumber) throws APPException {
        super(translate(locationNumber));
    }

    public static ByteBuf translate(LocationNumber locationNumber) throws APPException {
        if (locationNumber == null)
            throw new APPException("The locationNumber parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((LocationNumberImpl) locationNumber).encode(buffer);
            return buffer;
        } catch (ParameterException e) {
            throw new APPException("ParameterException when encoding locationNumber: " + e.getMessage(), e);
        }
    }

    public AccessTransport getAccessTransport() throws APPException {
        if (this.getValue() == null)
            throw new APPException("The data has not been filled");

        try {
        	AccessTransportImpl ln = new AccessTransportImpl();
            ln.decode(this.getValue());
            return ln;
        } catch (ParameterException e) {
            throw new APPException("ParameterException when decoding LocationNumber: " + e.getMessage(), e);
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
            } catch (APPException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
