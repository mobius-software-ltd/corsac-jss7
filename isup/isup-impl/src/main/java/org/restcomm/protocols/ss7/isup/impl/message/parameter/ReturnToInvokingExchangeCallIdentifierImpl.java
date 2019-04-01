/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:09:06:33 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.InformationType;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeCallIdentifier;

/**
 * Start time:09:06:33 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
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
            throw new ParameterException("byte[] must not be null or have length of 5");
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
