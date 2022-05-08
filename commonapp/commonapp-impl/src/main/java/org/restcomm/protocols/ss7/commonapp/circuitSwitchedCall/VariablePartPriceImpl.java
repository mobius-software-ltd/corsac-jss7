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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePartPrice;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class VariablePartPriceImpl extends ASNOctetString implements VariablePartPrice {
	public VariablePartPriceImpl() {
		super("VariablePartPrice",4,4,false);
    }

    public VariablePartPriceImpl(double price) {
        super(translate(price),"VariablePartPrice",4,4,false);
    }

    protected static ByteBuf translate(double price) {
    	ByteBuf buffer=Unpooled.buffer(4);
        long val = (long) (price * 100);
        if (val < 0)
            val = -val;
        buffer.writeByte((byte) encodeByte((int) (val / 1000000 - (val / 100000000) * 100)));
        buffer.writeByte((byte) encodeByte((int) (val / 10000 - (val / 1000000) * 100)));
        buffer.writeByte((byte) encodeByte((int) (val / 100 - (val / 10000) * 100)));
        buffer.writeByte((byte) encodeByte((int) (val - (val / 100) * 100)));
        return buffer;
    }

    public VariablePartPriceImpl(int integerPart, int hundredthPart) {
        super(translate(integerPart, hundredthPart),"VariablePartPrice",4,4,false);
    }

    protected static ByteBuf translate(int integerPart, int hundredthPart) {
    	ByteBuf buffer=Unpooled.buffer(4);
        long val = ((long) integerPart * 100 + hundredthPart);
        if (val < 0)
            val = -val;
        
        buffer.writeByte((byte) encodeByte((int) (val / 1000000 - (val / 100000000) * 100)));
        buffer.writeByte((byte) encodeByte((int) (val / 10000 - (val / 1000000) * 100)));
        buffer.writeByte((byte) encodeByte((int) (val / 100 - (val / 10000) * 100)));
        buffer.writeByte((byte) encodeByte((int) (val - (val / 100) * 100)));
        return buffer;
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

    private static int encodeByte(int val) {
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