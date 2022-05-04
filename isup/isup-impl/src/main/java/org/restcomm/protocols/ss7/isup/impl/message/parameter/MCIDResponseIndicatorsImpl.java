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
import org.restcomm.protocols.ss7.isup.message.parameter.MCIDResponseIndicators;

/**
 * Start time:08:29:07 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class MCIDResponseIndicatorsImpl extends AbstractISUPParameter implements MCIDResponseIndicators {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;
    public static boolean HOLDING_NOT_RROVIDED = false;

    public static boolean HOLDING_PROVIDED = true;

    public static boolean MCID_INCLUDED = true;

    public static boolean MCID_NOT_INCLUDED = false;

    private boolean mcidIncludedIndicator = false;
    private boolean holdingProvidedIndicator = false;

    public MCIDResponseIndicatorsImpl() {
        super();

    }

    public MCIDResponseIndicatorsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public MCIDResponseIndicatorsImpl(boolean mcidIncludedIndicator, boolean holdingProvidedIndicator) {
        super();
        this.mcidIncludedIndicator = mcidIncludedIndicator;
        this.holdingProvidedIndicator = holdingProvidedIndicator;
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("buffer must  not be null and length must  be 1");
        }

        byte curr=b.readByte();
        this.mcidIncludedIndicator = (curr & 0x01) == _TURN_ON;
        this.holdingProvidedIndicator = ((curr >> 1) & 0x01) == _TURN_ON;        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        int b0 = 0;

        b0 |= (this.mcidIncludedIndicator ? _TURN_ON : _TURN_OFF);
        b0 |= ((this.holdingProvidedIndicator ? _TURN_ON : _TURN_OFF)) << 1;

        buffer.writeByte((byte) b0);
    }

    public boolean isMcidIncludedIndicator() {
        return mcidIncludedIndicator;
    }

    public void setMcidIncludedIndicator(boolean mcidIncludedIndicator) {
        this.mcidIncludedIndicator = mcidIncludedIndicator;
    }

    public boolean isHoldingProvidedIndicator() {
        return holdingProvidedIndicator;
    }

    public void setHoldingProvidedIndicator(boolean holdingProvidedIndicator) {
        this.holdingProvidedIndicator = holdingProvidedIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
