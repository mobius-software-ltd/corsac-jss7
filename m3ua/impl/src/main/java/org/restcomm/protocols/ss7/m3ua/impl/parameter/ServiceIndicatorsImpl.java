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

import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.ServiceIndicators;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class ServiceIndicatorsImpl extends ParameterImpl implements ServiceIndicators {

    private short[] indicators;
    private ByteBuf value = null;

    public ServiceIndicatorsImpl() {
        this.tag = Parameter.Service_Indicators;
    }

    protected ServiceIndicatorsImpl(short[] inds) {
        this.tag = Parameter.Service_Indicators;
        this.indicators = inds;
        encode();
    }

    protected ServiceIndicatorsImpl(ByteBuf value) {
        this.tag = Parameter.Service_Indicators;
        this.indicators = new short[value.readableBytes()];
        int index=0;
        while(value.readableBytes()>0) {
            this.indicators[index++] = value.readByte();
        }
    }

    private void encode() {
        // create byte array taking into account data, point codes and
        // indicators;
        this.value = Unpooled.buffer(indicators.length);
        // encode routing context
        for(short currIndicator:indicators) {
            value.writeByte((byte) currIndicator);
        }
    }

    @Override
    protected ByteBuf getValue() {
    	if(value==null)
    		encode();
    	
        return Unpooled.wrappedBuffer(this.value);
    }

    public short[] getIndicators() {
        return this.indicators;
    }

    @Override
    public String toString() {
        return String.format("ServiceIndicators ids=%s", Arrays.toString(this.indicators));
    }
}
