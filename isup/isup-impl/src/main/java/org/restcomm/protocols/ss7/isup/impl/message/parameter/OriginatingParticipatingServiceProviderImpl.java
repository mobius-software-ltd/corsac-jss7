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
import org.restcomm.protocols.ss7.isup.message.parameter.OriginatingParticipatingServiceProvider;

/**
 * Start time:13:58:48 2009-04-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class OriginatingParticipatingServiceProviderImpl extends AbstractNumber implements OriginatingParticipatingServiceProvider {
	
	// FIXME: shoudl we add max octets ?
    private int opspLengthIndicator;

    public OriginatingParticipatingServiceProviderImpl() {
    }

    public OriginatingParticipatingServiceProviderImpl(ByteBuf representation) throws ParameterException {
        super(representation);
    }

    public OriginatingParticipatingServiceProviderImpl(String address) {
        super(address);
    }

    public void decode(ByteBuf b) throws ParameterException {
        super.decode(b);
    }

    public void encode(ByteBuf b) throws ParameterException {
        super.encode(b);
    }

    public void decodeHeader(ByteBuf buffer) throws ParameterException {
        if (buffer.readableBytes() == 0) {
            throw new ParameterException("No more data to read.");
        }
        int b = buffer.readByte() & 0xff;

        this.oddFlag = (b & 0x80) >> 7;
        this.opspLengthIndicator = b & 0x0F;        
    }

    public void encodeHeader(ByteBuf buffer) {
        int b = 0;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;
        b |= this.opspLengthIndicator & 0x0F;
        buffer.writeByte(b);        
    }

    public void decodeBody(ByteBuf buffer) throws ParameterException {        
    }

    public void encodeBody(ByteBuf buffer) {
    }

    public void decodeDigits(ByteBuf buffer) throws ParameterException {
        if (this.opspLengthIndicator > 0) {
            if (buffer.readableBytes() == 0) {
                throw new ParameterException("No more data to read.");
            }
            
            super.decodeDigits(buffer, this.opspLengthIndicator);
        }
    }

    public void encodeDigits(ByteBuf buffer) {
        if (this.opspLengthIndicator > 0) {
            super.encodeDigits(buffer);
        }
    }

    public int getOpspLengthIndicator() {
        return opspLengthIndicator;
    }

    public void setAddress(String address) {
        // TODO Auto-generated method stub
        super.setAddress(address);
        int l = super.address.length();
        this.opspLengthIndicator = l / 2 + l % 2;
        if (opspLengthIndicator > 4) {
            throw new IllegalArgumentException("Maximum octets for this parameter in digits part is 4. Address: " + address);
            // FIXME: add check for digit (max 7 ?)
        }
        if (this.opspLengthIndicator == 4 && !isOddFlag()) {
            throw new IllegalArgumentException("maximum allowed number of digits is 7. Address: " + address);
        }
    }

    public int getCode() {
        return _PARAMETER_CODE;
    }
}