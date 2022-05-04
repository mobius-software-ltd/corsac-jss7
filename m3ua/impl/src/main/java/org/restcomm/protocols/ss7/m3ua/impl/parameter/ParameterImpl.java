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

package org.restcomm.protocols.ss7.m3ua.impl.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 * @author amit bhayani
 * @author kulikov
 * @author yulianoifa
 */
public abstract class ParameterImpl implements Parameter {

    protected volatile short tag;
    protected volatile short length;

    public short getTag() {
        return tag;
    }

    protected abstract ByteBuf getValue();

    public void write(ByteBuf buf) {
        // obtain encoded value
        ByteBuf value = getValue();

        // encode tag
        buf.writeByte((byte) (tag >> 8));
        buf.writeByte((byte) (tag));

        // encode length including value, tag and length field itself
        length = (short) (value.readableBytes() + 4);
        if(length==4)
        	throw new RuntimeException("value is empty!!!" + this.getClass().getCanonicalName());
        
        buf.writeByte((byte) (length >> 8));
        buf.writeByte((byte) (length));

        // encode value
        buf.writeBytes(value);

        /*
         * The total length of a parameter (including Tag, Parameter Length, and Value fields) MUST be a multiple of 4 octets.
         * If the length of the parameter is not a multiple of 4 octets, the sender pads the Parameter at the end (i.e., after
         * the Parameter Value field) with all zero octets. The length of the padding is NOT included in the parameter length
         * field. A sender MUST NOT pad with more than 3 octets. The receiver MUST ignore the padding octets.
         */
        int remainder = (4 - length % 4);
        if (remainder < 4) {
            while (remainder > 0) {
                buf.writeByte((byte) 0x00);
                remainder--;
            }
        }
    }

}
