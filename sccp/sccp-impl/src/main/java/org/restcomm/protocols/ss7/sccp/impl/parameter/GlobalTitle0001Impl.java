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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.indicator.GlobalTitleIndicator;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle0001;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * @author baranowb
 * @author yulianoifa
 */
public class GlobalTitle0001Impl extends AbstractGlobalTitle implements GlobalTitle0001 {
	private static final long serialVersionUID = 1L;

	private NatureOfAddress natureOfAddress;

    public GlobalTitle0001Impl() {
    }

    /**
     * @param natureOfAddress
     */
    public GlobalTitle0001Impl(final String digits, final NatureOfAddress natureOfAddress) {
        super();
        if (natureOfAddress == null) {
            throw new IllegalArgumentException();
        }
        if (digits == null) {
            throw new IllegalArgumentException();
        }
        this.natureOfAddress = natureOfAddress;
        super.digits = digits;
        super.encodingScheme = super.digits.length() % 2 == 1 ? BCDOddEncodingScheme.INSTANCE : BCDEvenEncodingScheme.INSTANCE;
    }

    @Override
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY;
    }

    @Override
    public NatureOfAddress getNatureOfAddress() {
        return this.natureOfAddress;
    }

    @Override
    public void decode(ByteBuf buffer, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	int b = buffer.readByte() & 0xff;
        this.natureOfAddress = NatureOfAddress.valueOf(b & 0x7f);
        if((b & 0x80) >0){
            super.encodingScheme = BCDOddEncodingScheme.INSTANCE;
        } else {
            super.encodingScheme = BCDEvenEncodingScheme.INSTANCE;
        }
        super.digits = this.encodingScheme.decode(buffer);
    }

    @Override
    public void encode(ByteBuf buffer, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	if (this.natureOfAddress == null) {
            throw new IllegalStateException();
        }

        boolean odd = (super.digits.length() % 2) != 0;
        // encoding first byte
        int b = 0x00;
        if (odd) {
            b = b | (byte) 0x80;
        }
        // adding nature of address indicator
        b = b | (byte) this.natureOfAddress.getValue();
        // write first byte
        buffer.writeByte((byte) b);

        // encode digits
        if(super.digits == null){
            throw new IllegalStateException();
        }
        this.encodingScheme.encode(digits, buffer);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((natureOfAddress == null) ? 0 : natureOfAddress.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GlobalTitle0001Impl other = (GlobalTitle0001Impl) obj;
        if (natureOfAddress != other.natureOfAddress)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GlobalTitle0001Impl [digits=" + digits + ", natureOfAddress=" + natureOfAddress + ", encodingScheme="
                + encodingScheme + "]";
    }
}
