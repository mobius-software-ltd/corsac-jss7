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
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyCategory;

import io.netty.buffer.ByteBuf;

/**
 * Start time:13:31:04 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CallingPartyCategoryImpl extends AbstractISUPParameter implements CallingPartyCategory {
	private byte callingPartyCategory = 0;

    public CallingPartyCategoryImpl(byte callingPartyCategory) {
        super();
        this.callingPartyCategory = callingPartyCategory;
    }

    public CallingPartyCategoryImpl() {
        super();

    }

    public CallingPartyCategoryImpl(ByteBuf representation) throws ParameterException {
        super();
        this.decode(representation);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("buffer must not be null or have different size than 1");
        }
        this.callingPartyCategory = b.readByte();
    }

    public void encode(ByteBuf buffer) {
    	buffer.writeByte(this.callingPartyCategory);
    }

    public byte getCallingPartyCategory() {
        return callingPartyCategory;
    }

    public void setCallingPartyCategory(byte callingPartyCategory) {
        this.callingPartyCategory = callingPartyCategory;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString() {
        return "CallingPartyCategory [callingPartyCategory=" + callingPartyCategory + "]";
    }
}
