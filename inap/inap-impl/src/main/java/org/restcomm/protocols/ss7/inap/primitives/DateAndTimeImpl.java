/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.inap.primitives;

import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class DateAndTimeImpl extends ASNOctetString implements DateAndTime {
	public DateAndTimeImpl() {
    }

    public DateAndTimeImpl(int year, int month, int day, int hour, int minute, int second) {
        byte[] data = new byte[6];
        data[0] = (byte) encodeByte(year % 100);
        data[1] = (byte) encodeByte(month);
        data[2] = (byte) encodeByte(day);
        data[3] = (byte) encodeByte(hour);
        data[4] = (byte) encodeByte(minute);
        data[5] = (byte) encodeByte(second);        
        setValue(Unpooled.wrappedBuffer(data));
    }

    public int getYear() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        return (int) this.decodeByte(data.readByte());
    }

    public int getMonth() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        data.skipBytes(1);
        return this.decodeByte((int) data.readByte());
    }

    public int getDay() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        data.skipBytes(2);
        return this.decodeByte((int) data.readByte());
    }

    public int getHour() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        data.skipBytes(3);
        return this.decodeByte((int) data.readByte());
    }

    public int getMinute() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        data.skipBytes(4);
        return this.decodeByte((int) data.readByte());
    }

    public int getSecond() {
    	ByteBuf data=getValue();
        if (data == null || data.readableBytes() != 6)
            return 0;

        data.skipBytes(5);
        return this.decodeByte((int) data.readByte());
    }

    private int decodeByte(int bt) {
        return (bt & 0x0F) * 10 + ((bt & 0xF0) >> 4);
    }

    private int encodeByte(int val) {
        return (val / 10) | (val % 10) << 4;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DateAndTime [");
        if (getValue() != null) {
            sb.append("year=");
            sb.append(this.getYear());
            sb.append(", month=");
            sb.append(this.getMonth());
            sb.append(", day=");
            sb.append(this.getDay());
            sb.append(", hour=");
            sb.append(this.getHour());
            sb.append(", minite=");
            sb.append(this.getMinute());
            sb.append(", second=");
            sb.append(this.getSecond());
        }
        sb.append("]");

        return sb.toString();
    }
}
