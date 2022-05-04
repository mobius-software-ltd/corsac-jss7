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
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeCallIdentifier;

/**
 * Start time:09:06:33 2009-04-06<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class ReturnToInvokingExchangeCallIdentifierImpl extends AbstractInformationImpl implements ReturnToInvokingExchangeCallIdentifier {
	private int callIdentity = 0;
    // Should we use here SignalingPointCode class?
    private int signalingPointCode = 0;
    public ReturnToInvokingExchangeCallIdentifierImpl() {
        super(InformationType.ReturnToInvokingExchangeCallIdentifier);
        //this is always 0x02
        super.tag = 0x02;
    }
    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 5) {
            throw new ParameterException("buffer must not be null or have length of 5");
        }

        this.callIdentity = ((b.readByte() & 0xFF) << 16) | ((b.readByte() & 0xFF) << 8) | (b.readByte() & 0xFF);
        this.signalingPointCode = ((b.readByte() & 0xFF) | ((b.readByte() & 0x3F) << 8));

    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	buffer.writeByte((byte) (this.callIdentity >> 16));
    	buffer.writeByte((byte) (this.callIdentity >> 8));
    	buffer.writeByte((byte) this.callIdentity);

    	buffer.writeByte((byte) this.signalingPointCode);
    	buffer.writeByte((byte) ((this.signalingPointCode >> 8) & 0x3F));
    }

    public int getCallIdentity() {
        return callIdentity;
    }

    public void setCallIdentity(int callIdentity) {
        this.callIdentity = callIdentity & 0xFFFFFF;
    }

    public int getSignalingPointCode() {
        return signalingPointCode;
    }

    public void setSignalingPointCode(int signalingPointCode) {
        this.signalingPointCode = signalingPointCode;
    }
	@Override
	public int getLength() {
		return 5;
	}
}
