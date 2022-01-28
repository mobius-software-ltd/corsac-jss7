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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeValue;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PDPTypeImpl extends ASNOctetString2 implements PDPType {
	public static final int _VALUE_ETSI = 0xF0 + 0; // PPP
    public static final int _VALUE_IETF = 0xF0 + 1; // IPv4, IPv6

    public static final int _VALUE_PPP = 1;
    public static final int _VALUE_IPv4 = 33;
    public static final int _VALUE_IPv6 = 87;

    public PDPTypeImpl() {
    }

    public PDPTypeImpl(PDPTypeValue value) {
        super(translate(value));
    }

    protected static ByteBuf translate(PDPTypeValue value) {
        ByteBuf result = Unpooled.buffer(2);

        switch (value) {
        case PPP:
        	result.writeByte((byte) _VALUE_ETSI);
        	result.writeByte(_VALUE_PPP);
            break;
        case IPv4:
        	result.writeByte((byte) _VALUE_IETF)	;
        	result.writeByte((byte) _VALUE_IPv4);
            break;
        case IPv6:
        	result.writeByte((byte) _VALUE_IETF);
        	result.writeByte((byte) _VALUE_IPv6);
            break;
        }
        
        return result;
    }

    public PDPTypeValue getPDPTypeValue() {
    	ByteBuf value=getValue();
        if (value != null && value.readableBytes() == 2) {
        	byte firstByte=value.readByte();
            if ((firstByte & 0x0F) == (_VALUE_ETSI & 0x0F)) {
                if (value.readByte() == _VALUE_PPP)
                    return PDPTypeValue.PPP;
            } else if ((firstByte & 0x0F) == (_VALUE_IETF & 0x0F)) {
            	byte secondByte=value.readByte();
                if (secondByte == _VALUE_IPv4)
                    return PDPTypeValue.IPv4;
                if (secondByte == _VALUE_IPv6)
                    return PDPTypeValue.IPv6;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        PDPTypeValue value = this.getPDPTypeValue();
        if (value != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("PDPType [PDPTypeValue=");
            sb.append(value);
            sb.append("]");

            return sb.toString();
        } else {
            return super.toString();
        }
    }
}
