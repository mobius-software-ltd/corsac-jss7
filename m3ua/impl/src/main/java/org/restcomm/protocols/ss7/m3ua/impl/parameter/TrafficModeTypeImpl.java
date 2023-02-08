/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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
        encode();
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
