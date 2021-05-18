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
public class VariablePartTimeImpl extends ASNOctetString {
	public VariablePartTimeImpl() {
    }

    public VariablePartTimeImpl(byte[] data) {
    	if(data!=null)
    		setValue(Unpooled.wrappedBuffer(data));        
    }

    public VariablePartTimeImpl(int hour, int minute) {
        setParameters(hour, minute);
    }

    protected void setParameters(int hour, int minute) {
    	ByteBuf buffer=Unpooled.buffer(2);
        
        buffer.writeByte((byte) this.encodeByte(hour));
        buffer.writeByte((byte) this.encodeByte(minute));
        
        setValue(buffer);
    }

    public byte[] getData() {
    	ByteBuf value=getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public int getHour() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() != 2)
            return 0;

        return this.decodeByte(value.readByte());
    }

    public int getMinute() {
    	ByteBuf value=getValue();
        if (value == null || value.readableBytes() != 2)
            return 0;

        value.skipBytes(1);
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

        ByteBuf value=getValue();
        if (value != null && value.readableBytes() == 2) {
            sb.append("hour=");
            sb.append(this.getHour());
            sb.append(", minute=");
            sb.append(this.getMinute());
        }

        sb.append("]");

        return sb.toString();
    }
}
