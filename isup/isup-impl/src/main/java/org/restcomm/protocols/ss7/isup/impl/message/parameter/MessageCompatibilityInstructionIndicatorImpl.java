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

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInstructionIndicator;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class MessageCompatibilityInstructionIndicatorImpl implements MessageCompatibilityInstructionIndicator {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private boolean transitAtIntermediateExchangeIndicator;
    private boolean releaseCallindicator;
    private boolean sendNotificationIndicator;
    private boolean discardMessageIndicator;
    private boolean passOnNotPossibleIndicator;
    private int bandInterworkingIndicator;

    public MessageCompatibilityInstructionIndicatorImpl(byte b) throws ParameterException {
        super();
        decode(b);
    }

    public MessageCompatibilityInstructionIndicatorImpl() {
        super();

    }

    @Override
    public boolean isTransitAtIntermediateExchangeIndicator() {
        return this.transitAtIntermediateExchangeIndicator;
    }

    @Override
    public void setTransitAtIntermediateExchangeIndicator(boolean transitAtIntermediateExchangeIndicator) {
        this.transitAtIntermediateExchangeIndicator = transitAtIntermediateExchangeIndicator;
    }

    @Override
    public boolean isReleaseCallIndicator() {
        return this.releaseCallindicator;
    }

    @Override
    public void setReleaseCallIndicator(boolean releaseCallindicator) {
        this.releaseCallindicator = releaseCallindicator;
    }

    @Override
    public boolean isSendNotificationIndicator() {
        return this.sendNotificationIndicator;
    }

    @Override
    public void setSendNotificationIndicator(boolean sendNotificationIndicator) {
        this.sendNotificationIndicator = sendNotificationIndicator;
    }

    @Override
    public boolean isDiscardMessageIndicator() {
        return this.discardMessageIndicator;
    }

    @Override
    public void setDiscardMessageIndicator(boolean discardMessageIndicator) {
        this.discardMessageIndicator = discardMessageIndicator;
    }

    @Override
    public boolean isPassOnNotPossibleIndicator() {
        return this.passOnNotPossibleIndicator;
    }

    @Override
    public void setPassOnNotPossibleIndicator(boolean passOnNotPossibleIndicator) {
        this.passOnNotPossibleIndicator = passOnNotPossibleIndicator;
    }

    @Override
    public int getBandInterworkingIndicator() {
        return this.bandInterworkingIndicator;
    }

    @Override
    public void setBandInterworkingIndicator(int bandInterworkingIndicator) {
        this.bandInterworkingIndicator = bandInterworkingIndicator & 0x03;
    }

    public void decode(byte v) throws ParameterException {
        this.transitAtIntermediateExchangeIndicator = (v & 0x01) == _TURN_ON;
        this.releaseCallindicator = ((v >> 1) & 0x01) == _TURN_ON;
        this.sendNotificationIndicator = ((v >> 2) & 0x01) == _TURN_ON;
        this.discardMessageIndicator = ((v >> 3) & 0x01) == _TURN_ON;
        this.passOnNotPossibleIndicator = ((v >> 4) & 0x01) == _TURN_ON;
        this.bandInterworkingIndicator = ((v >> 5) & 0x03);        
    }

    public byte encode() throws ParameterException {
        byte b = 0;
        b |= (this.transitAtIntermediateExchangeIndicator ? _TURN_ON : _TURN_OFF);
        b |= (this.releaseCallindicator ? _TURN_ON : _TURN_OFF) << 1;
        b |= (this.sendNotificationIndicator ? _TURN_ON : _TURN_OFF) << 2;
        b |= (this.discardMessageIndicator ? _TURN_ON : _TURN_OFF) << 3;
        b |= (this.passOnNotPossibleIndicator ? _TURN_ON : _TURN_OFF) << 4;
        b |= this.bandInterworkingIndicator << 5;
        return b;
    }
}