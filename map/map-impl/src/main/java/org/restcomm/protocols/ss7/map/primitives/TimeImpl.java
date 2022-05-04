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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.restcomm.protocols.ss7.map.api.primitives.Time;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class TimeImpl extends ASNOctetString implements Time {
	private static final long msbZero = 2085978496000L;

    private static final long msbOne = -2208988800000L;

    public TimeImpl() {
    	super("Time",4,4,false);
    }

    public TimeImpl(int year, int month, int day, int hour, int minute, int second) {
    	super(translate(year, month, day, hour, minute, second),"Time",4,4,false);
    }
    
    public static ByteBuf translate(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(year, month - 1, day, hour, minute, second);
        long ntpTime = getNtpTime(cal.getTimeInMillis());
        ByteBuf buf=Unpooled.buffer(4);
        buf.writeBytes(longToBytes(ntpTime));
        return buf;
    }

    public int getYear() {
        long time = bytesToLong(getValue());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.YEAR);
    }

    public int getMonth() {
        long time = bytesToLong(getValue());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        long time = bytesToLong(getValue());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.DATE);
    }

    public int getHour() {
        long time = bytesToLong(getValue());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        long time = bytesToLong(getValue());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.MINUTE);
    }

    public int getSecond() {
        long time = bytesToLong(getValue());
        time = getTime(time);
        Date d = new Date(time);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(d);
        return cal.get(Calendar.SECOND);
    }

    private static long getNtpTime(long time) {
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

    public static long getTime(long ntpTime) {
        long msb = ntpTime & 0x80000000L;
        if (msb == 0) {
            return msbZero + (ntpTime * 1000);
        } else {
            return msbOne + (ntpTime * 1000);
        }
    }

    public static ByteBuf longToBytes(long x) {
        ByteBuf buffer = Unpooled.buffer(8);
        buffer.writeLong(x);
        buffer.skipBytes(4);
        return buffer.readSlice(4);
    }

    public static long bytesToLong(ByteBuf value) {
        return value.readUnsignedInt();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Time");
        sb.append(" [");
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
