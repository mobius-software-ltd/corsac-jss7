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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VariablePartDateImpl extends ASNOctetString {
	public VariablePartDateImpl() {
    }

    public VariablePartDateImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public VariablePartDateImpl(int year, int month, int day) {
        ByteBuf buffer=Unpooled.buffer(4);
        buffer.writeByte((byte) this.encodeByte(year / 100));
        buffer.writeByte((byte) this.encodeByte(year % 100));
        buffer.writeByte(this.encodeByte(month));
        buffer.writeByte(this.encodeByte(day));
        setValue(buffer);
    }

    public byte[] getData() {
    	ByteBuf buffer=getValue();
    	if(buffer==null)
    		return null;
    	
    	byte[] data=new byte[buffer.readableBytes()];
    	buffer.readBytes(data);
        return data;
    }

    public int getYear() {
    	ByteBuf value=this.getValue();
        if (value == null || value.readableBytes() != 4)
            return 0;

        return this.decodeByte(value.readByte()) * 100 + this.decodeByte(value.readByte());
    }

    public int getMonth() {
    	ByteBuf value=this.getValue();
        if (value == null || value.readableBytes() != 4)
            return 0;

        value.skipBytes(2);
        return this.decodeByte(value.readByte());
    }

    public int getDay() {
    	ByteBuf value=this.getValue();
        if (value == null || value.readableBytes() != 4)
            return 0;

        value.skipBytes(3);
        return this.decodeByte(value.readByte());
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
