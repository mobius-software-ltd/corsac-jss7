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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Class with some utility methods.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public final class Utils {
    public static long decodeTransactionId(ByteBuf data, boolean swapBytes) {

        if (swapBytes) {
        	return Unpooled.wrappedBuffer(data).readUnsignedInt();
        } else {
        	ByteBuf longRep = Unpooled.buffer(8);
        	longRep.writeInt(0);
        	longRep.writeInt(Unpooled.wrappedBuffer(data).readIntLE());
            return longRep.readLong();
        }
    }

    public static ByteBuf encodeTransactionId(long txId, boolean swapBytes) {
        // txId may only be up to 4 bytes, that is 0xFF FF FF FF
    	ByteBuf data = Unpooled.buffer(4);
        // long ll = txId.longValue();
        // data[3] = (byte) ll;
        // data[2] = (byte) (ll>> 8);
        // data[1] = (byte) (ll>>16);
        // data[0] = (byte) (ll >> 24);
        if (swapBytes) {
        	data.writeByte((byte) (txId >> 24));
        	data.writeByte((byte) (txId >> 16));
        	data.writeByte((byte) (txId >> 8));
        	data.writeByte((byte) txId);            
        } else {
        	data.writeByte((byte) txId);
        	data.writeByte((byte) (txId >> 8));
        	data.writeByte((byte) (txId >> 16));
        	data.writeByte((byte) (txId >> 24));
        }

        return data;
    }
}