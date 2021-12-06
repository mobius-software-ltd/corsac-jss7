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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePartPrice;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VariablePartPriceImpl extends ASNOctetString implements VariablePartPrice {
	public VariablePartPriceImpl() {
    }

    public VariablePartPriceImpl(byte[] data) {
        setValue(Unpooled.wrappedBuffer(data));
    }

    public VariablePartPriceImpl(double price) {
        setPrice(price);
    }

    protected void setPrice(double price) {
    	ByteBuf buffer=Unpooled.buffer(4);
        long val = (long) (price * 100);
        if (val < 0)
            val = -val;
        buffer.writeByte((byte) this.encodeByte((int) (val / 1000000 - (val / 100000000) * 100)));
        buffer.writeByte((byte) this.encodeByte((int) (val / 10000 - (val / 1000000) * 100)));
        buffer.writeByte((byte) this.encodeByte((int) (val / 100 - (val / 10000) * 100)));
        buffer.writeByte((byte) this.encodeByte((int) (val - (val / 100) * 100)));
        setValue(buffer);
    }

    public VariablePartPriceImpl(int integerPart, int hundredthPart) {
        setPriceIntegerHundredthPart(integerPart, hundredthPart);
    }

    protected void setPriceIntegerHundredthPart(int integerPart, int hundredthPart) {
    	ByteBuf buffer=Unpooled.buffer(4);
        long val = ((long) integerPart * 100 + hundredthPart);
        if (val < 0)
            val = -val;
        
        buffer.writeByte((byte) this.encodeByte((int) (val / 1000000 - (val / 100000000) * 100)));
        buffer.writeByte((byte) this.encodeByte((int) (val / 10000 - (val / 1000000) * 100)));
        buffer.writeByte((byte) this.encodeByte((int) (val / 100 - (val / 10000) * 100)));
        buffer.writeByte((byte) this.encodeByte((int) (val - (val / 100) * 100)));
        setValue(buffer);
    }

    public byte[] getData() {
    	ByteBuf value=this.getValue();
    	if(value==null)
    		return null;
    	
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
        return data;
    }

    public double getPrice() {
    	ByteBuf value=this.getValue();
        if (value == null || value.readableBytes() != 4)
        	return Double.NaN;

        double res = this.decodeByte(value.readByte()) * 10000 + this.decodeByte(value.readByte()) * 100 + this.decodeByte(value.readByte())
                + (double) this.decodeByte(value.readByte()) / 100.0;
        return res;
    }

    public int getPriceIntegerPart() {
    	ByteBuf value=this.getValue();
        if (value == null || value.readableBytes() != 4)
            return 0;

        int res = this.decodeByte(value.readByte()) * 10000 + this.decodeByte(value.readByte()) * 100 + this.decodeByte(value.readByte());
        return res;
    }

    public int getPriceHundredthPart() {
    	ByteBuf value=this.getValue();
        if (value == null || value.readableBytes() != 4)
            return 0;

        value.skipBytes(3);
        int res = this.decodeByte(value.readByte());
        return res;
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
        sb.append("VariablePartPrice [");

        double val = this.getPrice();
        if (!Double.isNaN(val)) {
            sb.append("price=");
            sb.append(val);
            sb.append(", integerPart=");
            sb.append(this.getPriceIntegerPart());
            sb.append(", hundredthPart=");
            sb.append(this.getPriceHundredthPart());
        }

        sb.append("]");

        return sb.toString();
    }
}
