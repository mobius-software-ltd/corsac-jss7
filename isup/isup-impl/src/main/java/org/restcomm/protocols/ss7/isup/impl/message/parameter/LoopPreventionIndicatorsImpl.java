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

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.LoopPreventionIndicators;

/**
 * Start time:11:31:36 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class LoopPreventionIndicatorsImpl extends AbstractISUPParameter implements LoopPreventionIndicators {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private boolean response;
    private int responseIndicator;

    public LoopPreventionIndicatorsImpl() {
        super();

    }

    public LoopPreventionIndicatorsImpl(boolean response, int responseIndicator) {
        super();
        this.response = response;
        this.responseIndicator = responseIndicator;
    }

    public LoopPreventionIndicatorsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("buffer must  not be null and length must  be 1");
        }

        byte curr=b.readByte();
        this.response = (curr & 0x01) == _TURN_ON;

        if (response) {
            this.responseIndicator = (curr >> 1) & 0x03;
        }        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        int v = this.response ? _TURN_ON : _TURN_OFF;
        if (this.response) {
            v |= (this.responseIndicator & 0x03) << 1;
        }
        buffer.writeByte((byte) v);
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public int getResponseIndicator() {
        return responseIndicator;
    }

    public void setResponseIndicator(int responseIndicator) {
        this.responseIndicator = responseIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
