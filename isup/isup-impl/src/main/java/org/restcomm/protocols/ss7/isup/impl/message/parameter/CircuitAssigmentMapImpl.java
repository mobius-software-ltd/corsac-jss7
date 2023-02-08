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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitAssigmentMap;

/**
 * Start time:12:20:07 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CircuitAssigmentMapImpl extends AbstractISUPParameter implements CircuitAssigmentMap {
	private static final int _CIRCUIT_ENABLED = 0x01;

    private int mapType = 0;

    private int mapFormat = 0;

    public CircuitAssigmentMapImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public CircuitAssigmentMapImpl(int mapType, int mapFormat) {
        super();
        this.mapType = mapType;
        this.mapFormat = mapFormat;
    }

    public CircuitAssigmentMapImpl() {
        super();

    }

    public void decode(ByteBuf buffer) throws ParameterException {
        if (buffer == null || buffer.readableBytes() != 5) {
            throw new ParameterException("buffer must  not be null and length must  be 5");
        }

        this.mapType = buffer.readByte() & 0x3F;
        this.mapFormat = buffer.readByte();
        this.mapFormat |= buffer.readByte() << 8;
        this.mapFormat |= buffer.readByte() << 16;
        this.mapFormat |= (buffer.readByte() & 0x7F) << 24;
    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	buffer.writeByte((byte) (this.mapType & 0x3F));
    	buffer.writeByte((byte) this.mapFormat);
    	buffer.writeByte((byte) (this.mapFormat >> 8));
    	buffer.writeByte((byte) (this.mapFormat >> 16));
    	buffer.writeByte((byte) ((this.mapFormat >> 24) & 0x7F));        
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public int getMapFormat() {
        return mapFormat;
    }

    public void setMapFormat(int mapFormat) {
        this.mapFormat = mapFormat;
    }

    /**
     * Enables circuit
     *
     * @param circuitNumber - index of circuit - must be number <1,31>
     * @throws IllegalArgumentException - when number is not in range
     */
    public void enableCircuit(int circuitNumber) throws IllegalArgumentException {
        if (circuitNumber < 1 || circuitNumber > 31) {
            throw new IllegalArgumentException("Cicruit number is out of range[" + circuitNumber + "] <1,31>");
        }

        this.mapFormat |= _CIRCUIT_ENABLED << (circuitNumber - 1);
    }

    /**
     * Disables circuit
     *
     * @param circuitNumber - index of circuit - must be number <1,31>
     * @throws IllegalArgumentException - when number is not in range
     */
    public void disableCircuit(int circuitNumber) throws IllegalArgumentException {
        if (circuitNumber < 1 || circuitNumber > 31) {
            throw new IllegalArgumentException("Cicruit number is out of range[" + circuitNumber + "] <1,31>");
        }
        this.mapFormat &= 0xFFFFFFFE << (circuitNumber - 1);
    }

    public boolean isCircuitEnabled(int circuitNumber) throws IllegalArgumentException {
        if (circuitNumber < 1 || circuitNumber > 31) {
            throw new IllegalArgumentException("Cicruit number is out of range[" + circuitNumber + "] <1,31>");
        }

        return ((this.mapFormat >> (circuitNumber - 1)) & 0x01) == _CIRCUIT_ENABLED;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
