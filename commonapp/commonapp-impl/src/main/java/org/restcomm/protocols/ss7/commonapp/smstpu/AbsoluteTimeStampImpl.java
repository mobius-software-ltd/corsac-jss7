/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.commonapp.smstpu;

import org.restcomm.protocols.ss7.commonapp.api.smstpdu.AbsoluteTimeStamp;

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class AbsoluteTimeStampImpl implements AbsoluteTimeStamp {
	private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int timeZone;

    private AbsoluteTimeStampImpl() {
    }

    public AbsoluteTimeStampImpl(int year, int month, int day, int hour, int minute, int second, int timeZone) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.timeZone = timeZone;
    }

    public static AbsoluteTimeStampImpl createMessage(ByteBuf data) throws ASNParsingException {

        if (data == null)
            throw new ASNParsingException("Error creating ServiceCentreTimeStamp: buffer must not be null");

        if (data.readableBytes() < 7)
            throw new ASNParsingException("Error creating ServiceCentreTimeStamp: not enouph data in the buffer");

        
        AbsoluteTimeStampImpl res = new AbsoluteTimeStampImpl();
        res.year = constractDigitVal(data.readByte());
        res.month = constractDigitVal(data.readByte());
        res.day = constractDigitVal(data.readByte());
        res.hour = constractDigitVal(data.readByte());
        res.minute = constractDigitVal(data.readByte());
        res.second = constractDigitVal(data.readByte());
        
        byte currByte=data.readByte();
        res.timeZone = constractDigitVal((byte) (currByte & 0xF7));
        if ((currByte & 0x08) != 0)
            res.timeZone = -res.timeZone;
        
        return res;
    }

    private static int constractDigitVal(byte bt) {
        return (bt & 0xF) * 10 + ((bt & 0xF0) >>> 4);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getTimeZone() {
        return timeZone;
    }

    private static int constractEncodesVal(int val) {
        int i1 = val % 10;
        int i2 = val / 10;
        return (i1 << 4) | i2;
    }

    public void encodeData(ByteBuf stm) throws ASNParsingException {
    	stm.writeByte(constractEncodesVal(this.year));
        stm.writeByte(constractEncodesVal(this.month));
        stm.writeByte(constractEncodesVal(this.day));
        stm.writeByte(constractEncodesVal(this.hour));
        stm.writeByte(constractEncodesVal(this.minute));
        stm.writeByte(constractEncodesVal(this.second));
        if (this.timeZone >= 0)
            stm.writeByte(constractEncodesVal(this.timeZone));
        else
            stm.writeByte(constractEncodesVal((-this.timeZone)) | 0x08);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("AbsoluteTimeStamp [");

        int yr;
        if (this.year > 90)
            yr = 1900 + this.year;
        else
            yr = 2000 + this.year;
        sb.append(this.month);
        sb.append("/");
        sb.append(this.day);
        sb.append("/");
        sb.append(yr);

        sb.append(" ");
        sb.append(this.hour);
        sb.append(":");
        sb.append(this.minute);
        sb.append(":");
        sb.append(this.second);

        sb.append(" GMT");
        if (this.timeZone >= 0)
            sb.append("+");
        sb.append(this.timeZone / 4);
        sb.append(":");
        sb.append((this.timeZone % 4) * 15);

        sb.append("]");

        return sb.toString();
    }
}
