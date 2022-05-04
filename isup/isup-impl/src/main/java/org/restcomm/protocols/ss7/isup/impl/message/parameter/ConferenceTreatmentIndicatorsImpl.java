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
 * Start time:13:42:38 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * @author yulianoifa
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.ConferenceTreatmentIndicators;

/**
 * Start time:13:42:38 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ConferenceTreatmentIndicatorsImpl extends AbstractISUPParameter implements ConferenceTreatmentIndicators {
	private ByteBuf conferenceAcceptance = null;

    public ConferenceTreatmentIndicatorsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public ConferenceTreatmentIndicatorsImpl() {
        super();

    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() == 0) {
            throw new ParameterException("buffer must not be null and length must be greater than 0");
        }
        setConferenceAcceptance(b);        
    }

    public void encode(ByteBuf b) throws ParameterException {
    	ByteBuf curr=getConferenceAcceptance();
    	while(curr.readableBytes()>1) {
            b.writeByte(curr.readByte() & 0x03);
        }

    	if(curr.readableBytes()>0)
    		b.writeByte(curr.readByte()  | (0x01 << 7));        
    }

    public ByteBuf getConferenceAcceptance() {
        return conferenceAcceptance;
    }

    public void setConferenceAcceptance(ByteBuf conferenceAcceptance) {
        if (conferenceAcceptance == null || conferenceAcceptance.readableBytes() == 0) {
            throw new IllegalArgumentException("ByteBuf must not be null and length must be greater than 0");
        }

        this.conferenceAcceptance = conferenceAcceptance;
    }

    public int getConferenceTreatmentIndicator(byte b) {
        return b & 0x03;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
