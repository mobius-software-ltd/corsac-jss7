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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ZoneCodeImpl extends ASNOctetString2 implements ZoneCode {
	public ZoneCodeImpl() {
    }

	public ZoneCodeImpl(int value) {
		super(translate(value));
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