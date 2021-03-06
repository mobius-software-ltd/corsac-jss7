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
    public static long decodeTransactionId(byte[] data, boolean swapBytes) {
    	byte[] longRep = new byte[8];

        if (swapBytes) {
            // copy data so longRep = {0,0,0,...,data};
            System.arraycopy(data, 0, longRep, longRep.length - data.length, data.length);
        } else {
            longRep[4] = data[3];
            longRep[5] = data[2];
            longRep[6] = data[1];
            longRep[7] = data[0];
        }
        ByteBuf bb = Unpooled.wrappedBuffer(longRep);
        return bb.readLong();

    }

    public static byte[] encodeTransactionId(long txId, boolean swapBytes) {
        // txId may only be up to 4 bytes, that is 0xFF FF FF FF
        byte[] data = new byte[4];
        // long ll = txId.longValue();
        // data[3] = (byte) ll;
        // data[2] = (byte) (ll>> 8);
        // data[1] = (byte) (ll>>16);
        // data[0] = (byte) (ll >> 24);
        if (swapBytes) {
            data[3] = (byte) txId;
            data[2] = (byte) (txId >> 8);
            data[1] = (byte) (txId >> 16);
            data[0] = (byte) (txId >> 24);
        } else {
            data[0] = (byte) txId;
            data[1] = (byte) (txId >> 8);
            data[2] = (byte) (txId >> 16);
            data[3] = (byte) (txId >> 24);
        }

        return data;
    }
}