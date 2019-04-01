/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

import org.restcomm.protocols.ss7.m3ua.parameter.ProtocolData;

/**
 * Implements Protocol Data parameter.
 *
 * @author amit bhayani
 * @author kulikov
 */
public class ProtocolDataImpl extends ParameterImpl implements ProtocolData {

    private int opc;
    private int dpc;
    private int si;
    private int ni;
    private int mp;
    private int sls;
    private ByteBuf data;

    protected ProtocolDataImpl() {
        this.tag = ParameterImpl.Protocol_Data;
    }

    protected ProtocolDataImpl(int opc, int dpc, int si, int ni, int mp, int sls, ByteBuf data) {
        this();
        this.opc = opc;
        this.dpc = dpc;
        this.si = si;
        this.ni = ni;
        this.mp = mp;
        this.sls = sls;
        this.data = data;
        encode();
    }

    /**
     * Creates new parameter with specified value.
     *
     * @param valueData the value of this parameter
     */
    protected ProtocolDataImpl(ByteBuf valueData) {
        this();

        this.opc = ((valueData.readByte() & 0xff) << 24) | ((valueData.readByte() & 0xff) << 16) | ((valueData.readByte() & 0xff) << 8)
                | (valueData.readByte() & 0xff);
        this.dpc = ((valueData.readByte() & 0xff) << 24) | ((valueData.readByte() & 0xff) << 16) | ((valueData.readByte() & 0xff) << 8)
                | (valueData.readByte() & 0xff);

        this.si = valueData.readByte() & 0xff;
        this.ni = valueData.readByte() & 0xff;
        this.mp = valueData.readByte() & 0xff;
        this.sls = valueData.readByte() & 0xff;

        this.data = valueData.slice();
    }

    private ByteBuf encode() {
        // create byte array taking into account data, point codes and
        // indicators;
    	ByteBuf headerValue=Unpooled.buffer(12);
    	
    	// encode originated point codes
    	headerValue.writeByte((byte) (opc >> 24));
    	headerValue.writeByte((byte) (opc >> 16));
    	headerValue.writeByte((byte) (opc >> 8));
    	headerValue.writeByte((byte) (opc));

        // encode destination point code
    	headerValue.writeByte((byte) (dpc >> 24));
    	headerValue.writeByte((byte) (dpc >> 16));
    	headerValue.writeByte((byte) (dpc >> 8));
    	headerValue.writeByte((byte) (dpc));

        // encode indicators
    	headerValue.writeByte((byte) (si));
    	headerValue.writeByte((byte) (ni));
    	headerValue.writeByte((byte) (mp));
    	headerValue.writeByte((byte) (sls));
        
    	ByteBuf value=Unpooled.wrappedBuffer(headerValue,data);
        return value;
    }

    public int getOpc() {
        return opc;
    }

    public int getDpc() {
        return dpc;
    }

    public int getSI() {
        return si;
    }

    public int getNI() {
        return ni;
    }

    public int getMP() {
        return mp;
    }

    public int getSLS() {
        return sls;
    }

    public ByteBuf getData() {
        return Unpooled.wrappedBuffer(data);
    }

    @Override
    protected ByteBuf getValue() {
        return this.encode();
    }

    @Override
    public String toString() {
        return String.format("Protocol opc=%d dpc=%d si=%d ni=%d sls=%d", opc, dpc, si, ni, sls);
    }

}
