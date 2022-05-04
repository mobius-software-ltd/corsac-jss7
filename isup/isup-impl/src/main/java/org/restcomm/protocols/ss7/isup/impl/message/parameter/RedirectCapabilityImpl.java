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
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectCapability;

/**
 * Start time:09:11:07 2009-04-06<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class RedirectCapabilityImpl extends AbstractISUPParameter implements RedirectCapability {
	private ByteBuf capabilities;

    public RedirectCapabilityImpl() {
        super();

    }

    public RedirectCapabilityImpl(ByteBuf capabilities) throws ParameterException {
        super();
        decode(capabilities);
    }

    public void decode(ByteBuf b) throws ParameterException {
        try {
            this.setCapabilities(b);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
    }

    public void encode(ByteBuf b) throws ParameterException {
    	ByteBuf curr=getCapabilities();
        while(curr.readableBytes()>1) {
            b.writeByte((byte) (curr.readByte() & 0x07));
        }

        if(curr.readableBytes()>0)
        	b.writeByte((byte) (curr.readByte() | (0x01 << 7)));        
    }

    public ByteBuf getCapabilities() {
    	if(capabilities==null)
        	return null;
        
        return Unpooled.wrappedBuffer(capabilities);
    }

    public void setCapabilities(ByteBuf capabilities) {
        if (capabilities == null || capabilities.readableBytes() == 0) {
            throw new IllegalArgumentException("buffer must not be null and length must be greater than 0");
        }
        this.capabilities = capabilities;
    }

    public int getCapability(byte b) {
        return b & 0x7F;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
