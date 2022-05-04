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
import org.restcomm.protocols.ss7.isup.message.parameter.BackwardGVNS;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Start time:13:15:04 2009-04-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class BackwardGVNSImpl extends AbstractISUPParameter implements BackwardGVNS {
	private ByteBuf backwardGVNS = null;

    public BackwardGVNSImpl(ByteBuf backwardGVNS) throws ParameterException {
        super();
        decode(backwardGVNS);
    }

    public BackwardGVNSImpl() {
        super();

    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() == 0) {
            throw new ParameterException("buffer must  not be null and length must  be greater than 0");
        }
        this.backwardGVNS = Unpooled.wrappedBuffer(b);
    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	ByteBuf curr=getBackwardGVNS();
    	while(curr.readableBytes()>1) {
    		buffer.writeByte((byte) (curr.readByte() & 0x7F));
        }

    	if(curr.readableBytes()>0)
    		buffer.writeByte((byte) (curr.readByte() & (1 << 7)));
    }

    public ByteBuf getBackwardGVNS() {
    	if(this.backwardGVNS==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(backwardGVNS);
    }

    public void setBackwardGVNS(ByteBuf backwardGVNS) {
        if (backwardGVNS == null || backwardGVNS.readableBytes() == 0) {
            throw new IllegalArgumentException("ByteBuf must  not be null and length must  be greater than 0");
        }
        this.backwardGVNS = backwardGVNS;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
