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
import org.restcomm.protocols.ss7.isup.message.parameter.GVNSUserGroup;

/**
 * Start time:13:58:48 2009-04-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class GVNSUserGroupImpl extends AbstractNumber implements GVNSUserGroup {
	// FIXME: shoudl we add max octets ?
    private int gugLengthIndicator;

    public GVNSUserGroupImpl() {

    }

    public GVNSUserGroupImpl(ByteBuf representation) throws ParameterException {
        super(representation);
    }

    public GVNSUserGroupImpl(String address) {
        super(address);

    }

    public void decode(ByteBuf b) throws ParameterException {
        super.decode(b);
    }

    public void encode(ByteBuf b) throws ParameterException {
        super.encode(b);
    }

    public void decodeHeader(ByteBuf buffer) throws IllegalArgumentException {
        int b = buffer.readByte() & 0xff;

        this.oddFlag = (b & 0x80) >> 7;
        this.gugLengthIndicator = b & 0x0F;        
    }

    public void encodeHeader(ByteBuf buffer) {
        int b = 0;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;
        b |= this.gugLengthIndicator & 0x0F;
        buffer.writeByte(b);        
    }

    public void decodeBody(ByteBuf buffer) throws IllegalArgumentException {        
    }

    public void encodeBody(ByteBuf buffer) {        
    }

    public int getGugLengthIndicator() {
        return gugLengthIndicator;
    }

    public void decodeDigits(ByteBuf buffer) throws IllegalArgumentException, ParameterException {
        super.decodeDigits(buffer, this.gugLengthIndicator);
    }

    public void setAddress(String address) {
        // TODO Auto-generated method stub
        super.setAddress(address);
        int l = super.address.length();
        this.gugLengthIndicator = l / 2 + l % 2;
        if (gugLengthIndicator > 8) {
            throw new IllegalArgumentException("Maximum octets for this parameter in digits part is 8.");
            // FIXME: add check for digit (max 7 ?)
        }
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
