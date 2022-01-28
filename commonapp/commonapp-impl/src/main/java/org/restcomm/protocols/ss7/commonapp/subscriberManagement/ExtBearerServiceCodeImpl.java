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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.BearerServiceCodeValue;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBearerServiceCode;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtBearerServiceCodeImpl extends ASNOctetString implements ExtBearerServiceCode {
	public ExtBearerServiceCodeImpl() {  
    }

    public ExtBearerServiceCodeImpl(BearerServiceCodeValue value) {
        super(translate(value));
    }

    private static ByteBuf translate(BearerServiceCodeValue value) {
        if (value != null) {
        	ByteBuf buffer=Unpooled.buffer();
        	buffer.writeByte(value.getCode());
            return buffer;
        }
        else
        	return Unpooled.EMPTY_BUFFER;
    }

    public BearerServiceCodeValue getBearerServiceCodeValue() {
    	ByteBuf buffer=getValue();
        if (buffer == null || buffer.readableBytes() < 1)
            return null;
        else
            return BearerServiceCodeValue.getInstance(buffer.readByte());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtBearerServiceCode [");

        sb.append("Value=");
        sb.append(this.getBearerServiceCodeValue());

        sb.append("]");

        return sb.toString();
    }
}
