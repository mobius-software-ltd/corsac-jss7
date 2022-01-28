/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:12:50:23 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CallDiversionTreatmentIndicators;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Start time:12:50:23 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CallDiversionTreatmentIndicatorsImpl extends AbstractISUPParameter implements CallDiversionTreatmentIndicators {
	private ByteBuf callDivertedIndicators = null;

    public CallDiversionTreatmentIndicatorsImpl() {
        super();

    }

    public CallDiversionTreatmentIndicatorsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() == 0) {
            throw new ParameterException("buffer must  not be null and length must  be greater than 0");
        }
        this.callDivertedIndicators = Unpooled.wrappedBuffer(b);
    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	ByteBuf curr=getCallDivertedIndicators();
    	while(curr.readableBytes()>1) {
    		buffer.writeByte((byte) (curr.readByte() & 0x03));
        }

    	if(curr.readableBytes()>0)
    		buffer.writeByte((byte) (curr.readByte() | (0x01 << 7)));        
    }

    public ByteBuf getCallDivertedIndicators() {
    	if(callDivertedIndicators==null)
    		return null;
    	
    	return Unpooled.wrappedBuffer(callDivertedIndicators);
    }

    public void setCallDivertedIndicators(ByteBuf callDivertedIndicators) {
        this.callDivertedIndicators = callDivertedIndicators;
    }

    public int getDiversionIndicator(byte b) {
        return b & 0x03;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
