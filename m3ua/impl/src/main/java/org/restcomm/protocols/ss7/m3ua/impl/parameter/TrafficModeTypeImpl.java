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

import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class TrafficModeTypeImpl extends ParameterImpl implements TrafficModeType {
    private int mode = 0;
    private ByteBuf value;

    public TrafficModeTypeImpl() {
        this.tag = Parameter.Traffic_Mode_Type;
    }

    protected TrafficModeTypeImpl(ByteBuf data) {
        this.tag = Parameter.Traffic_Mode_Type;       
        this.mode = 0;
        this.mode |= data.readByte() & 0xFF;
        this.mode <<= 8;
        this.mode |= data.readByte() & 0xFF;
        this.mode <<= 8;
        this.mode |= data.readByte() & 0xFF;
        this.mode <<= 8;
        this.mode |= data.readByte() & 0xFF;
    }

    public TrafficModeTypeImpl(int traffmode) {
        this.tag = Parameter.Traffic_Mode_Type;
        mode = traffmode;
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = Unpooled.buffer(4);
        // encode routing context
        value.writeByte((byte) (mode >> 24));
        value.writeByte((byte) (mode >> 16));
        value.writeByte((byte) (mode >> 8));
        value.writeByte((byte) (mode));
    }

    public int getMode() {
        return mode;
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(value);
    }

    @Override
    public String toString() {
        return String.format("TrafficModeType mode=%d value=%s", mode, this.getStringRepresentation());
    }

    public String getStringRepresentation() {
        switch (mode) {
            case 1:
                return "Override";
            case 2:
                return "Loadshare";
            case 3:
                return "Broadcast";
            default:
                return "";
        }
    }

}
