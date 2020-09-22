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
package org.restcomm.protocols.ss7.map.api.primitives;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class TimeImpl extends ASNOctetString {
	private static final long msbZero = 2085978496000L;

    private static final long msbOne = -2208988800000L;

    public TimeImpl() {
    }

    public TimeImpl(byte[] data) {
    	setValue(Unpooled.wrappedBuffer(data));
    }

    public TimeImpl(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(year, month - 1, day, hour, minute, second);
        long ntpTime = getNtpTime(cal.getTimeInMillis());
        ByteBuf buf=Unpooled.buffer(4);
        buf.writeBytes(longToBytes(ntpTime));
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public int getYear() {
        long time = bytesToLong(getData());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.YEAR);
    }

    public int getMonth() {
        long time = bytesToLong(getData());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        long time = bytesToLong(getData());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.DATE);
    }

    public int getHour() {
        long time = bytesToLong(getData());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        long time = bytesToLong(getData());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.MINUTE);
    }

    public int getSecond() {
        long time = bytesToLong(getData());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.SECOND);
    }

    private long getNtpTime(long time) {
        boolean isMSBSet = time < msbZero;
        long timeWithMills;
        if (isMSBSet) {
            timeWithMills = time - msbOne;
        } else {
            timeWithMills = time - msbZero;
        }
        long seconds = timeWithMills / 1000;
        if (isMSBSet) {
            seconds |= 0x80000000L;
        }
        return seconds;
    }

    public long getTime(long ntpTime) {
        long msb = ntpTime & 0x80000000L;
        if (msb == 0) {
            return msbZero + (ntpTime * 1000);
        } else {
            return msbOne + (ntpTime * 1000);
        }
    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        byte[] eightbytes = new byte[8];
        System.arraycopy(bytes, 0, eightbytes, 4, 4);
        buffer.put(eightbytes);
        buffer.flip();
        return buffer.getLong();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Time");
        sb.append(" [");
        if (getData() != null) {
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
