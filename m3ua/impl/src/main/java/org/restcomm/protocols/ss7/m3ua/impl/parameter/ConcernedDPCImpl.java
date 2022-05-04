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

import org.restcomm.protocols.ss7.m3ua.parameter.ConcernedDPC;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class ConcernedDPCImpl extends ParameterImpl implements ConcernedDPC {

    private int pointCode;

    protected ConcernedDPCImpl(int pointCode) {
        this.pointCode = pointCode;
        this.tag = Parameter.Concerned_Destination;
    }

    protected ConcernedDPCImpl(ByteBuf data) {
        // data[0] is reserved
    	data.skipBytes(1);
    	
        this.pointCode = 0;
        this.pointCode |= data.readByte() & 0xFF;
        this.pointCode <<= 8;
        this.pointCode |= data.readByte() & 0xFF;
        this.pointCode <<= 8;
        this.pointCode |= data.readByte() & 0xFF;
        this.tag = Parameter.Concerned_Destination;
    }

    @Override
    protected ByteBuf getValue() {
        ByteBuf data = Unpooled.buffer(4);
        // reserved
        data.writeByte(0);

        // DPC
        data.writeByte((byte) (pointCode >>> 16));
        data.writeByte((byte) (pointCode >>> 8));
        data.writeByte((byte) (pointCode));

        return data;
    }

    @Override
    public String toString() {
        return String.format("ConcernedDPC dpc=%d", pointCode);
    }

    public int getPointCode() {
        return this.pointCode;
    }

}
