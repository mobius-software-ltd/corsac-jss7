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

package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class PlmnIdImpl extends ASNOctetString implements PlmnId {
	
	public PlmnIdImpl() {
		super("PlmnId",3,3,false);
    }

	public PlmnIdImpl(int mcc, int mnc) {
		super(translate(mcc, mnc),"PlmnId",3,3,false);
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
