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
 * @author yulianoifa
 *
 */
public class SM_RP_SMEAImpl extends ASNOctetString implements SM_RP_SMEA {
	public SM_RP_SMEAImpl() {
		super("SM_RP_SMEA",1,12,false);
    }

	public SM_RP_SMEAImpl(AddressField addressField) throws MAPException {
		super(translate(addressField),"SM_RP_SMEA",1,12,false);
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