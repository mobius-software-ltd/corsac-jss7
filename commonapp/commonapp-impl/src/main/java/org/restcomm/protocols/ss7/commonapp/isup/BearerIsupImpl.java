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
import org.restcomm.protocols.ss7.commonapp.api.isup.BearerIsup;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserServiceInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformation;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
public class BearerIsupImpl extends ASNOctetString implements BearerIsup {
	public BearerIsupImpl() {
    }

    public BearerIsupImpl(UserServiceInformation userServiceInformation) throws APPException {
        super(translate(userServiceInformation));
    }

    public static ByteBuf translate(UserServiceInformation userServiceInformation) throws APPException {
        if (userServiceInformation == null)
            throw new APPException("The userServiceInformation parameter must not be null");
        try {
        	ByteBuf buffer=Unpooled.buffer();
        	((UserServiceInformationImpl) userServiceInformation).encode(buffer);
            return buffer;
        } catch (ParameterException e) {
            throw new APPException("ParameterException when encoding userServiceInformation: " + e.getMessage(), e);
        }
    }

    public UserServiceInformation getUserServiceInformation() throws APPException {
        if (this.getValue() == null)
            throw new APPException("The data has not been filled");

        try {
            UserServiceInformationImpl ln = new UserServiceInformationImpl();
            ln.decode(this.getValue());
            return ln;
        } catch (ParameterException e) {
            throw new APPException("ParameterException when decoding UserServiceInformation: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BearerCap [");

        if (getValue() != null) {
            try {
                UserServiceInformation usi = this.getUserServiceInformation();
                sb.append(", ");
                sb.append(usi.toString());
            } catch (APPException e) {
            }
        }

        sb.append("]");

        return sb.toString();
    }
}
