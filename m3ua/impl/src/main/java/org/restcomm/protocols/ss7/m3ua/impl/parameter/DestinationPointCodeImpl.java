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
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class DestinationPointCodeImpl extends ParameterImpl implements DestinationPointCode {

    private int destPC = 0;
    private short mask = 0;
    private ByteBuf value;

    public DestinationPointCodeImpl() {
        this.tag = Parameter.Destination_Point_Code;
    }

    protected DestinationPointCodeImpl(ByteBuf value) {
        this.tag = Parameter.Destination_Point_Code;
        this.mask = value.readByte();

        destPC = 0;
        destPC |= value.readByte() & 0xFF;
        destPC <<= 8;
        destPC |= value.readByte() & 0xFF;
        destPC <<= 8;
        destPC |= value.readByte() & 0xFF;
    }

    protected DestinationPointCodeImpl(int pc, short mask) {
        this.tag = Parameter.Destination_Point_Code;
        this.destPC = pc;
        this.mask = mask;
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = Unpooled.buffer(4);
        // encode point code with mask
        value.writeByte((byte) this.mask);// Mask

        value.writeByte((byte) (destPC >> 16));
        value.writeByte((byte) (destPC >> 8));
        value.writeByte((byte) (destPC));
    }

    public int getPointCode() {
        return destPC;
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(value);
    }

    public short getMask() {
        return this.mask;
    }

    @Override
    public String toString() {
        return String.format("DestinationPointCode dpc=%d mask=%d", destPC, mask);
    }
}
