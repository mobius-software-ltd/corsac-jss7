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

package org.restcomm.protocols.ss7.inap.charging;

import org.restcomm.protocols.ss7.inap.api.charging.ChargeUnitTimeInterval;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class ChargeUnitTimeIntervalImpl extends ASNOctetString implements ChargeUnitTimeInterval {
	public ChargeUnitTimeIntervalImpl() {
		super("ChargeUnitTimeInterval",2,2,false);
    }

	public ChargeUnitTimeIntervalImpl(Integer value) {
		super(translate(value),"ChargeUnitTimeInterval",2,2,false);
	}
	
    public static ByteBuf translate(Integer value) {
    	if(value!=null) {
    		ByteBuf buffer=Unpooled.buffer(2);
    		buffer.writeShort(value);
    		return buffer;
    	}
    	else
    		return Unpooled.EMPTY_BUFFER;
    }

    public Integer getData() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 2)
            return null;

        return data.readUnsignedShort();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChargeUnitTimeInterval [");
        Integer data=getData();
        if (data != null) {
            sb.append("data=");
            sb.append(data);            
        }
        sb.append("]");

        return sb.toString();
    }
}
