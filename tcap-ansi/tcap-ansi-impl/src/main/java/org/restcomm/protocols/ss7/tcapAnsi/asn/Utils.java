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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Class with some utility methods.
 *
 * @author baranowb
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