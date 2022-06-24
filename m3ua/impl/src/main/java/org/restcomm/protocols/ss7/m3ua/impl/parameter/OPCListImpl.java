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

import java.util.Arrays;

import org.restcomm.protocols.ss7.m3ua.parameter.OPCList;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class OPCListImpl extends ParameterImpl implements OPCList {

    private ByteBuf value;
    private int[] pointCodes;
    private short[] masks;

    public OPCListImpl() {
        this.tag = Parameter.Originating_Point_Code_List;
    }

    protected OPCListImpl(ByteBuf value) {
        this.tag = Parameter.Originating_Point_Code_List;

        int arrSize = 0;
        pointCodes = new int[(value.readableBytes() / 4)];
        masks = new short[(value.readableBytes() / 4)];

        while (value.readableBytes()>0) {
            masks[arrSize] = value.readByte();

            pointCodes[arrSize] = 0;
            pointCodes[arrSize] |= value.readByte() & 0xFF;
            pointCodes[arrSize] <<= 8;
            pointCodes[arrSize] |= value.readByte() & 0xFF;
            pointCodes[arrSize] <<= 8;
            pointCodes[arrSize++] |= value.readByte() & 0xFF;
        }
    }

    protected OPCListImpl(int[] pointCodes, short[] masks) {
        this.tag = Parameter.Originating_Point_Code_List;
        this.pointCodes = pointCodes;
        this.masks = masks;
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;

        this.value = Unpooled.buffer((pointCodes.length * 4));

        // encode routing context
        for(int arrSize = 0;arrSize<masks.length; arrSize++) {
            value.writeByte((byte) (masks[arrSize]));

            value.writeByte((byte) (pointCodes[arrSize] >>> 16));
            value.writeByte((byte) (pointCodes[arrSize] >>> 8));
            value.writeByte((byte) (pointCodes[arrSize]));
        }
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(this.value);
    }

    public short[] getMasks() {
        return this.masks;
    }

    public int[] getPointCodes() {
        return this.pointCodes;
    }

    @Override
    public String toString() {
        return String.format("OPCList pointCode=%s mask=%s", Arrays.toString(this.pointCodes), Arrays.toString(this.masks));
    }
}
