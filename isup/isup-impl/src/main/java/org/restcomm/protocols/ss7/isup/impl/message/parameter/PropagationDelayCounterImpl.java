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
import org.restcomm.protocols.ss7.isup.message.parameter.PropagationDelayCounter;

/**
 * Start time:14:20:15 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class PropagationDelayCounterImpl extends AbstractISUPParameter implements PropagationDelayCounter {
	private int propagationDelay;

    public PropagationDelayCounterImpl() {
        super();

    }

    public PropagationDelayCounterImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public PropagationDelayCounterImpl(int propagationDelay) {
        super();
        this.propagationDelay = propagationDelay;
    }

    public void decode(ByteBuf b) throws ParameterException {
        // This one is other way around, as Eduardo might say.
        if (b == null || b.readableBytes() != 2) {
            throw new ParameterException("buffer must  not be null and length must be 2");
        }

        this.propagationDelay = b.readByte() << 8;
        this.propagationDelay |= b.readByte();        
    }

    public void encode(ByteBuf buffer) throws ParameterException {

        buffer.writeByte((byte) (this.propagationDelay >> 8));
        buffer.writeByte((byte) this.propagationDelay);        
    }

    public int getPropagationDelay() {
        return propagationDelay;
    }

    public void setPropagationDelay(int propagationDelay) {
        this.propagationDelay = propagationDelay;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("PropagationDelayCounter [");

        sb.append("propagationDelay=");
        sb.append(propagationDelay);

        sb.append("]");
        return sb.toString();
    }
}
