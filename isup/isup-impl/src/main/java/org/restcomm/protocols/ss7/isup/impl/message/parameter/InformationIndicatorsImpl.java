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
import org.restcomm.protocols.ss7.isup.message.parameter.InformationIndicators;

import io.netty.buffer.ByteBuf;

/**
 * Start time:14:36:25 2009-03-31<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class InformationIndicatorsImpl extends AbstractISUPParameter implements InformationIndicators {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private int callingPartyAddressResponseIndicator;
    private boolean holdProvidedIndicator;
    private boolean callingPartysCategoryResponseIndicator;
    private boolean chargeInformationResponseIndicator;
    private boolean solicitedInformationIndicator;
    // FIXME: should we care about it.
    private int reserved;

    public InformationIndicatorsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public InformationIndicatorsImpl() {
        super();

    }

    public InformationIndicatorsImpl(int callingPartyAddressResponseIndicator, boolean holdProvidedIndicator,
            boolean callingPartysCategoryResponseIndicator, boolean chargeInformationResponseIndicator,
            boolean solicitedInformationIndicator, int reserved) {
        super();
        this.callingPartyAddressResponseIndicator = callingPartyAddressResponseIndicator;
        this.holdProvidedIndicator = holdProvidedIndicator;
        this.callingPartysCategoryResponseIndicator = callingPartysCategoryResponseIndicator;
        this.chargeInformationResponseIndicator = chargeInformationResponseIndicator;
        this.solicitedInformationIndicator = solicitedInformationIndicator;
        this.reserved = reserved;
    }

    public void decode(ByteBuf buffer) throws ParameterException {
        if (buffer == null || buffer.readableBytes() != 2) {
            throw new ParameterException("buffer must  not be null and length must  be 2");
        }

        byte b=buffer.readByte();
        this.callingPartyAddressResponseIndicator = b & 0x03;
        this.holdProvidedIndicator = ((b >> 2) & 0x01) == _TURN_ON;
        this.callingPartysCategoryResponseIndicator = ((b >> 5) & 0x01) == _TURN_ON;
        this.chargeInformationResponseIndicator = ((b >> 6) & 0x01) == _TURN_ON;
        this.solicitedInformationIndicator = ((b >> 7) & 0x01) == _TURN_ON;
        
        b=buffer.readByte();
        this.reserved = (b >> 4) & 0x0F;        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        int b1 = this.callingPartyAddressResponseIndicator & 0x03;
        b1 |= (this.holdProvidedIndicator ? _TURN_ON : _TURN_OFF) << 2;
        b1 |= (this.callingPartysCategoryResponseIndicator ? _TURN_ON : _TURN_OFF) << 5;
        b1 |= (this.chargeInformationResponseIndicator ? _TURN_ON : _TURN_OFF) << 6;
        b1 |= (this.solicitedInformationIndicator ? _TURN_ON : _TURN_OFF) << 7;

        int b2 = (this.reserved & 0x0F) << 4;
        buffer.writeByte(b1);
        buffer.writeByte(b2);        
    }

    public int getCallingPartyAddressResponseIndicator() {
        return callingPartyAddressResponseIndicator;
    }

    public void setCallingPartyAddressResponseIndicator(int callingPartyAddressResponseIndicator) {
        this.callingPartyAddressResponseIndicator = callingPartyAddressResponseIndicator;
    }

    public boolean isHoldProvidedIndicator() {
        return holdProvidedIndicator;
    }

    public void setHoldProvidedIndicator(boolean holdProvidedIndicator) {
        this.holdProvidedIndicator = holdProvidedIndicator;
    }

    public boolean isCallingPartysCategoryResponseIndicator() {
        return callingPartysCategoryResponseIndicator;
    }

    public void setCallingPartysCategoryResponseIndicator(boolean callingPartysCategoryResponseIndicator) {
        this.callingPartysCategoryResponseIndicator = callingPartysCategoryResponseIndicator;
    }

    public boolean isChargeInformationResponseIndicator() {
        return chargeInformationResponseIndicator;
    }

    public void setChargeInformationResponseIndicator(boolean chargeInformationResponseIndicator) {
        this.chargeInformationResponseIndicator = chargeInformationResponseIndicator;
    }

    public boolean isSolicitedInformationIndicator() {
        return solicitedInformationIndicator;
    }

    public void setSolicitedInformationIndicator(boolean solicitedInformationIndicator) {
        this.solicitedInformationIndicator = solicitedInformationIndicator;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getCode() {
        return _PARAMETER_CODE;
    }
}