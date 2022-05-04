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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class ZoneCodeImpl extends ASNOctetString implements ZoneCode {
	public ZoneCodeImpl() {
		super("ZoneCode",2,2,false);
    }

	public ZoneCodeImpl(int value) {
		super(translate(value),"ZoneCode",2,2,false);
	}
	
    public static ByteBuf translate(int value) {
        ByteBuf result = Unpooled.buffer(2);
        result.writeByte((byte) ((value & 0xFF00) >> 8));
        result.writeByte((byte) (value & 0xFF));
        return result;
    }

    public int getIntValue() {
    	ByteBuf value=getValue();
    	if(value==null || value.readableBytes()!=2)
    		return 0;
    	
        int res = ((value.readByte() & 0xFF) << 8) | (value.readByte() & 0xFF);
        return res;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ZoneCodeImpl");
        sb.append(" [");
        Integer value=getIntValue();
        if (value!=null) {
            sb.append("value=");
            sb.append(value);            
        }
        sb.append("]");

        return sb.toString();
    }
}