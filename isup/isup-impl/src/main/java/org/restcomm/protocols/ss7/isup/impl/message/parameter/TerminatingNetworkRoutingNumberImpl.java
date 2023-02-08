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
import org.restcomm.protocols.ss7.isup.message.parameter.TerminatingNetworkRoutingNumber;

/**
 * Start time:15:16:34 2009-04-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class TerminatingNetworkRoutingNumberImpl extends AbstractNumber implements TerminatingNetworkRoutingNumber {
	// FIXME: shoudl we add max octets ?
    private int tnrnLengthIndicator;
    private int numberingPlanIndicator;
    private int natureOfAddressIndicator;

    public TerminatingNetworkRoutingNumberImpl() {
        super();

    }

    public TerminatingNetworkRoutingNumberImpl(ByteBuf representation) throws ParameterException {
        super(representation);
    }

    public TerminatingNetworkRoutingNumberImpl(int numberingPlanIndicator) {
        super();
        this.setNumberingPlanIndicator(numberingPlanIndicator);
        this.tnrnLengthIndicator = 0;
    }

    public TerminatingNetworkRoutingNumberImpl(int numberingPlanIndicator, int natureOfAddressIndicator) {
        super();
        this.setNumberingPlanIndicator(numberingPlanIndicator);
        this.setNatureOfAddressIndicator(natureOfAddressIndicator);
        this.tnrnLengthIndicator = 1;
    }

    public TerminatingNetworkRoutingNumberImpl(String address, int numberingPlanIndicator, int natureOfAddressIndicator) {
        super();
        this.setNumberingPlanIndicator(numberingPlanIndicator);
        this.setNatureOfAddressIndicator(natureOfAddressIndicator);
        this.setAddress(address);
    }

    public void decode(ByteBuf b) throws ParameterException {
        super.decode(b);
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        super.encode(buffer);
    }

    public void decodeHeader(ByteBuf buffer) throws ParameterException {
        if (buffer.readableBytes() == 0) {
            throw new ParameterException("No more data to read.");
        }
        int b = buffer.readByte() & 0xff;

        this.oddFlag = (b & 0x80) >> 7;
        this.tnrnLengthIndicator = b & 0x0F;
        this.numberingPlanIndicator = (b >> 4) & 0x07;
    }

    public void encodeHeader(ByteBuf buffer) {
        int b = 0;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;
        b |= this.tnrnLengthIndicator & 0x0F;
        b |= (this.numberingPlanIndicator & 0x07) << 4;
        buffer.writeByte(b);
    }

    public void decodeBody(ByteBuf buffer) throws ParameterException {
        if (this.tnrnLengthIndicator > 0) {
            if (buffer.readableBytes() == 0) {
                throw new ParameterException("No more data to read.");
            }
            this.setNatureOfAddressIndicator(buffer.readByte());            
        }
    }

    public void encodeBody(ByteBuf buffer) {
        if (this.tnrnLengthIndicator > 0) {
        	buffer.writeByte(this.natureOfAddressIndicator);            
        }
    }

    public void decodeDigits(ByteBuf buffer) throws ParameterException {
        if (this.tnrnLengthIndicator - 1 > 0) {
            if (buffer.readableBytes() == 0) {
                throw new ParameterException("No more data to read.");
            }
            super.decodeDigits(buffer, this.tnrnLengthIndicator - 1);
        }
    }

    public void encodeDigits(ByteBuf buffer) {
        if (this.tnrnLengthIndicator - 1 > 0)
            super.encodeDigits(buffer);        
    }

    public void setAddress(String address) {
        // TODO Auto-generated method stub
        super.setAddress(address);
        int l = super.address.length();
        // +1 for NAI
        this.tnrnLengthIndicator = l / 2 + l % 2 + 1;
        if (tnrnLengthIndicator > 9) {
            throw new IllegalArgumentException("Maximum octets for this parameter in digits part is 8.");
            // FIXME: add check for digit (max 7 ?)
        }

        if (this.tnrnLengthIndicator == 9 && !isOddFlag()) {
            // we allow only odd! digits count in this case
            throw new IllegalArgumentException("To many digits. Maximum number of digits is 15 for tnr length of 9.");
        }
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator & 0x07;
    }

    public int getNatureOfAddressIndicator() {
        return natureOfAddressIndicator;
    }

    public void setNatureOfAddressIndicator(int natureOfAddressIndicator) {
        this.natureOfAddressIndicator = natureOfAddressIndicator & 0x7F;
    }

    public int getTnrnLengthIndicator() {
        return tnrnLengthIndicator;
    }

    public int getCode() {
        return _PARAMETER_CODE;
    }
}
