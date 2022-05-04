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
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotCapability;

/**
 * Start time:15:52:32 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class PivotCapabilityImpl extends AbstractISUPParameter implements PivotCapability {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private ByteBuf pivotCapabilities;

    public PivotCapabilityImpl() {
        super();

    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	ByteBuf curr=getPivotCapabilities();
        while(curr.readableBytes()>1) {
        	buffer.writeByte(curr.readByte() & 0x7F);            
        }

        if(curr.readableBytes()>0)
        	buffer.writeByte(curr.readByte() | (0x01 << 7));        
    }

    public void decode(ByteBuf b) throws ParameterException {
        setPivotCapabilities(b);
    }

    public ByteBuf getPivotCapabilities() {
    	if(pivotCapabilities==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(pivotCapabilities);
    }

    public void setPivotCapabilities(ByteBuf pivotCapabilities) {
        if (pivotCapabilities == null || pivotCapabilities.readableBytes() == 0) {
            throw new IllegalArgumentException("buffer must not be null and length must be greater than 0");
        }

        this.pivotCapabilities = pivotCapabilities;
    }

    public byte createPivotCapabilityByte(boolean itriNotAllowed, int pivotPossibility) {
        int b = (itriNotAllowed ? _TURN_ON : _TURN_OFF) << 6;
        b |= pivotPossibility & 0x07;

        return (byte) b;
    }

    public boolean getITRINotAllowed(byte b) {
        return ((b >> 6) & 0x01) == _TURN_ON;
    }

    public int getPivotCapability(byte b) {
        return b & 0x07;
    }

    public int getCode() {
        return _PARAMETER_CODE;
    }
}