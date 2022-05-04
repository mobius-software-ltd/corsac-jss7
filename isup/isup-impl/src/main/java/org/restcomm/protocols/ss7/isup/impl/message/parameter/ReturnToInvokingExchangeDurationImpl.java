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
import org.restcomm.protocols.ss7.isup.message.parameter.InformationType;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeDuration;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class ReturnToInvokingExchangeDurationImpl extends AbstractInformationImpl implements ReturnToInvokingExchangeDuration {
	private int duration;

    public ReturnToInvokingExchangeDurationImpl() {
        super(InformationType.ReturnToInvokingExchangeDuration);
        super.tag = 0x01;
    }

    @Override
    public void setDuration(int seconds) {
        this.duration = seconds & 0XFFFF;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public void encode(ByteBuf buffer) throws ParameterException {
    	buffer.writeByte((byte) this.duration);
        if(this.duration > 0xFF){
        	buffer.writeByte((byte) ((this.duration >> 8) & 0xFF));
        }
    }

    @Override
    public void decode(ByteBuf b) throws ParameterException {
        if(b.readableBytes() != 1 && b.readableBytes()!=2){
            throw new ParameterException("Wrong numbder of bytes: "+b.readableBytes());
        }
        this.duration = (b.readByte() & 0xFF);
        if(b.readableBytes() == 1){
             this.duration |= ((b.readByte() & 0xFF) << 8);
        }
    }

	@Override
	public int getLength() {
		if(this.duration > 0xFF)
			return 2;
		
		return 1;
	}
}
