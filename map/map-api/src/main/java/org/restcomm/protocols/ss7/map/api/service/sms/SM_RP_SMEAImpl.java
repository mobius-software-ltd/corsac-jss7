/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.map.api.service.sms;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressFieldImpl;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SM_RP_SMEAImpl extends ASNOctetString {
	public SM_RP_SMEAImpl() {
    }

    public SM_RP_SMEAImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public SM_RP_SMEAImpl(AddressFieldImpl addressField) throws MAPException {
        if (addressField == null) {
            throw new MAPException("addressField field must not be equal null");
        }

        ByteBuf buffer=Unpooled.buffer();
        addressField.encodeData(buffer);
        setValue(buffer);
    }

    public AddressFieldImpl getAddressField() throws MAPException {
        AddressFieldImpl res = AddressFieldImpl.createMessage(getValue());
        return res;
    }
}