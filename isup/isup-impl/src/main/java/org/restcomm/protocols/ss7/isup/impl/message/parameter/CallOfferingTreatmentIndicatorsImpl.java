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
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CallOfferingTreatmentIndicators;

/**
 * Start time:13:33:13 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CallOfferingTreatmentIndicatorsImpl extends AbstractISUPParameter implements CallOfferingTreatmentIndicators {
	private ByteBuf callOfferingTreatmentIndicators = null;

    public CallOfferingTreatmentIndicatorsImpl() {
        super();

    }

    public CallOfferingTreatmentIndicatorsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() == 0) {
            throw new ParameterException("buffer must not be null and length must be greater than 0");
        }
        setCallOfferingTreatmentIndicators(b);        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	ByteBuf curr=getCallOfferingTreatmentIndicators();
    	while(curr.readableBytes()>1) {
    		buffer.writeByte((byte) ((curr.readByte() & 0x03) | 0x80));
        }

    	if(curr.readableBytes()>0)
    		buffer.writeByte((byte) ((curr.readByte() & 0x7F)));
    }

    public ByteBuf getCallOfferingTreatmentIndicators() {
    	if(callOfferingTreatmentIndicators==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(callOfferingTreatmentIndicators);
    }

    public void setCallOfferingTreatmentIndicators(ByteBuf callOfferingTreatmentIndicators) {
        if (callOfferingTreatmentIndicators == null || callOfferingTreatmentIndicators.readableBytes() == 0) {
            throw new IllegalArgumentException("buffer must not be null and length must be greater than 0");
        }

        this.callOfferingTreatmentIndicators = callOfferingTreatmentIndicators;
    }

    public int getCallOfferingIndicator(byte b) {
        return b & 0x03;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}