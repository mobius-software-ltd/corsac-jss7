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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.restcomm.protocols.ss7.map.api.smstpdu.AddressField;
import org.restcomm.protocols.ss7.map.smstpdu.AddressFieldImpl;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SM_RP_SMEAImpl extends ASNOctetString implements SM_RP_SMEA {
	public SM_RP_SMEAImpl() {
    }

	public SM_RP_SMEAImpl(AddressField addressField) throws MAPException {
		super(translate(addressField));
	}
	
    public static ByteBuf translate(AddressField addressField) throws MAPException {
        if (addressField == null) {
            throw new MAPException("addressField field must not be equal null");
        }

        ByteBuf buffer=Unpooled.buffer();
        addressField.encodeData(buffer);
        return buffer;
    }

    public AddressField getAddressField() throws MAPException {
        AddressFieldImpl res = AddressFieldImpl.createMessage(getValue());
        return res;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SM_RP_SMEAImpl");
        sb.append(" [");
        try {
	        AddressField addressField=getAddressField();
	        if (addressField!=null) {
	            sb.append("addressField=");
	            sb.append(addressField);            
	        }
        }
        catch(MAPException ex) {
        	
        }
        
        sb.append("]");
        return sb.toString();
    }
}