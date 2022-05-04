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
 * Start time:11:21:05 2009-03-31<br>
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
import org.restcomm.protocols.ss7.isup.message.parameter.EndOfOptionalParameters;

/**
 * Start time:11:21:05 2009-03-31<br>
 * Project: restcomm-isup-stack<br>
 * This class represent element that encodes end of parameters
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EndOfOptionalParametersImpl extends AbstractISUPParameter implements EndOfOptionalParameters {
	public EndOfOptionalParametersImpl() {
        super();

    }

    public EndOfOptionalParametersImpl(ByteBuf b) {
        super();

    }

    /**
     * heeh, value is zero actually :D
     */
    public static final int _PARAMETER_CODE = 0;

    public void decode(ByteBuf b) throws ParameterException {
    }

    public void encode(ByteBuf b) throws ParameterException {
        // TODO Auto-generated method stub
        b.writeByte(0);
    }

    public int getCode() {
        return _PARAMETER_CODE;
    }
}