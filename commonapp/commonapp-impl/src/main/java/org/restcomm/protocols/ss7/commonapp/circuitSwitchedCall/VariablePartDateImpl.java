/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePartDate;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VariablePartDateImpl extends ASNOctetString implements VariablePartDate {
	public VariablePartDateImpl() {
    }

    public VariablePartDateImpl(int year, int month, int day) {
    	super(translate(year, month, day));
    }
    
    private static ByteBuf translate(int year, int month, int day) {
        ByteBuf buffer=Unpooled.buffer(4);
        buffer.writeByte(encodeByte(year / 100));
        buffer.writeByte(encodeByte(year % 100));
        buffer.writeByte(encodeByte(month));
        buffer.writeByte(encodeByte(day));
        return buffer;
    }

    public int getYear() {
    	ByteBuf value=this.getValue();
        if (value == null || value.readableBytes() != 4)
            return 0;

        return decodeByte(value.readByte()) * 100 + decodeByte(value.readByte());
    }

    public int getMonth() {
    	ByteBuf value=this.getValue();
        if (value == null || value.readableBytes() != 4)
            return 0;

        value.skipBytes(2);
        return decodeByte(value.readByte());
    }

    public int getDay() {
    	ByteBuf value=this.getValue();
        if (value == null || value.readableBytes() != 4)
            return 0;

        value.skipBytes(3);
        return decodeByte(value.readByte());
    }

    private static int decodeByte(int bt) {
        return (bt & 0x0F) * 10 + ((bt & 0xF0) >> 4);
    }

    private static int encodeByte(int val) {
        return (val / 10) | (val % 10) << 4;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("VariablePartDate [");

        if (this.getValue() != null && this.getValue().readableBytes() == 4) {
            sb.append("year=");
            sb.append(this.getYear());
            sb.append(", month=");
            sb.append(this.getMonth());
            sb.append(", day=");
            sb.append(this.getDay());
        }

        sb.append("]");

        return sb.toString();
    }
}
