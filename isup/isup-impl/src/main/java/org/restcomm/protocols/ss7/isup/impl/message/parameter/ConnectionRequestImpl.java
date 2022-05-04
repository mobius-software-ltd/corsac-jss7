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

/**
 * Start time:17:59:38 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * @author yulianoifa
 * 
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.ConnectionRequest;

/**
 * Start time:17:59:38 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ConnectionRequestImpl extends AbstractISUPParameter implements ConnectionRequest {
	private int localReference;
    // should we use here SignalingPointCode class? XXx
    private int signalingPointCode;
    private boolean protocolClassSet = false;
    private int protocolClass;
    private boolean creditSet = false;
    private int credit;

    public ConnectionRequestImpl(ByteBuf b) throws ParameterException {
        decode(b);
    }

    public ConnectionRequestImpl() {
        super();

    }

    public ConnectionRequestImpl(int localReference, int signalingPointCode, int protocolClass, int credit) {
        super();
        this.localReference = localReference;
        this.signalingPointCode = signalingPointCode;
        this.protocolClass = protocolClass;
        this.credit = credit;
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null) {
            throw new ParameterException("buffer must not be null");
        }

        // if (_PROTOCOL_VERSION == 1 && b.length != 7) {
        // throw new ParameterException("For protocol version 1 length of this parameter must be 7 octets");
        // }

        if (b.readableBytes() != 5 && b.readableBytes() != 7) {
            throw new ParameterException("buffer length must be 5 or 7");
        }

        // FIXME: This is not mentioned, is it inverted as usually or not ?
        this.localReference |= b.readByte();
        this.localReference |= b.readByte() << 8;
        this.localReference |= b.readByte() << 16;

        this.signalingPointCode = b.readByte();
        this.signalingPointCode |= (b.readByte() & 0x3F) << 8;

        if (b.readableBytes()>0) {
            this.creditSet = true;
            this.protocolClassSet = true;
            this.protocolClass = b.readByte();
            this.credit = b.readByte();
        }
    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	buffer.writeByte((byte) this.localReference);
    	buffer.writeByte((byte) (this.localReference >> 8));
    	buffer.writeByte((byte) (this.localReference >> 16));

    	buffer.writeByte((byte) this.signalingPointCode);
    	buffer.writeByte((byte) ((this.signalingPointCode >> 8) & 0x3F));
        
        if (this.creditSet || this.protocolClassSet) {
        	buffer.writeByte((byte) this.protocolClass);
        	buffer.writeByte((byte) this.credit);
        }
    }

    public int getLocalReference() {
        return localReference;
    }

    public void setLocalReference(int localReference) {
        this.localReference = localReference;
    }

    public int getSignalingPointCode() {
        return signalingPointCode;
    }

    public void setSignalingPointCode(int signalingPointCode) {
        this.signalingPointCode = signalingPointCode;
    }

    public int getProtocolClass() {

        return protocolClass;
    }

    public void setProtocolClass(int protocolClass) {
        this.protocolClassSet = true;
        this.protocolClass = protocolClass;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.creditSet = true;
        this.credit = credit;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}