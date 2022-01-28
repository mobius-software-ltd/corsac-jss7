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

package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


/**
 *
 * @author sergey vetyutnev
 *
 */
public class PlmnIdImpl extends ASNOctetString2 implements PlmnId {
	
	public PlmnIdImpl() {
    }

	public PlmnIdImpl(int mcc, int mnc) {
		super(translate(mcc, mnc));
	}
	
    public static ByteBuf translate(int mcc, int mnc) {
        int a1 = mcc / 100;
        int tv = mcc % 100;
        int a2 = tv / 10;
        int a3 = tv % 10;

        int b1 = mnc / 100;
        tv = mnc % 100;
        int b2 = tv / 10;
        int b3 = tv % 10;

        ByteBuf data = Unpooled.buffer(3);
        data.writeByte((byte) ((a2 << 4) + a1));
        if (b1 == 0) {
        	data.writeByte((byte) (0xF0 + a3));
        	data.writeByte((byte) ((b3 << 4) + b2));
        } else {
        	data.writeByte((byte) ((b3 << 4) + a3));
        	data.writeByte((byte) ((b2 << 4) + b1));
        }
        
        return data;
    }

    public int getMcc() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 3)
            return 0;

        byte firstByte=data.readByte();
        int a1 = firstByte & 0x0F;
        int a2 = (firstByte & 0xF0) >> 4;
        int a3 = data.readByte() & 0x0F;

        return a1 * 100 + a2 * 10 + a3;
    }

    public int getMnc() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 3)
            return 0;

        data.skipBytes(1);
        int a3 = (data.readByte() & 0xF0) >> 4;
        byte thirdByte=data.readByte();
        int a1 = thirdByte & 0x0F;
        int a2 = (thirdByte & 0xF0) >> 4;
        
        if (a3 == 15)
            return a1 * 10 + a2;
        else
            return a1 * 100 + a2 * 10 + a3;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PLMN ID");
        sb.append(" [");

        sb.append("MCC=[");
        sb.append(this.getMcc());
        sb.append("]");

        sb.append(", MNC=[");
        sb.append(this.getMnc());
        sb.append("]");

        sb.append("]");

        return sb.toString();
    }
}
