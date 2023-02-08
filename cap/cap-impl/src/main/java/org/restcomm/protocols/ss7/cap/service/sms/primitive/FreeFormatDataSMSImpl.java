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

package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMS;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author Lasith Waruna Perera
 * @author alerant appngin
 * @author yulianoifa
 *
 */
public class FreeFormatDataSMSImpl extends ASNOctetString implements FreeFormatDataSMS {
	public FreeFormatDataSMSImpl() {
		super("FreeFormatDataSMS",1,160,false);
    }

    public FreeFormatDataSMSImpl(ByteBuf value) {
    	super(value,"FreeFormatDataSMS",1,160,false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FreeFormatDataSMSImpl [");

        if (this.getValue() != null) {
            sb.append("data=");
            sb.append(printDataArr());
        }
        
        sb.append("]");

        return sb.toString();
    }
}
