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
import org.restcomm.protocols.ss7.isup.message.parameter.NetworkRoutingNumber;

/**
 * Start time:18:44:18 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class NetworkRoutingNumberImpl extends AbstractNumber implements NetworkRoutingNumber {
	private int numberingPlanIndicator;
    private int natureOfAddressIndicator;

    public NetworkRoutingNumberImpl(String address) {
        super(address);

    }

    public NetworkRoutingNumberImpl(String address, int numberingPlanIndicator, int natureOfAddressIndicator) {
        super(address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.natureOfAddressIndicator = natureOfAddressIndicator;
    }

    public NetworkRoutingNumberImpl() {
        super();

    }

    public NetworkRoutingNumberImpl(ByteBuf representation) throws ParameterException {
        super(representation);
    }

    public void decodeBody(ByteBuf buffer) throws IllegalArgumentException {
    }

    public void encodeBody(ByteBuf buffer) {
    }

    public void decodeHeader(ByteBuf buffer) throws IllegalArgumentException {
        int b = buffer.readByte() & 0xff;
        this.oddFlag = (b & 0x80) >> 7;
        this.numberingPlanIndicator = (b & 0x70) >> 4;
        this.natureOfAddressIndicator = b & 0x0F;
    }

    public void encodeHeader(ByteBuf buffer) {
        int b = 0;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;

        b |= (this.numberingPlanIndicator & 0x07) << 4;
        b |= this.natureOfAddressIndicator & 0x0F;
        buffer.writeByte(b);
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getNatureOfAddressIndicator() {
        return natureOfAddressIndicator;
    }

    public void setNatureOfAddressIndicator(int natureOfAddressIndicator) {
        this.natureOfAddressIndicator = natureOfAddressIndicator;
    }

    public int getCode() {
        return _PARAMETER_CODE;
    }
}