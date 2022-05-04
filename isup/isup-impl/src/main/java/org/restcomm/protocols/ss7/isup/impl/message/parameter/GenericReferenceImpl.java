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
import org.restcomm.protocols.ss7.isup.message.parameter.GenericReference;

/**
 * Start time:13:04:01 2009-07-17<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class GenericReferenceImpl extends AbstractISUPParameter implements GenericReference {
	/**
     *
     */
    public GenericReferenceImpl() {
        //FIXME: XXX
    }

    /**
     * @throws ParameterException
     * @throws ParameterException
     *
     */
    public GenericReferenceImpl(ByteBuf b) throws ParameterException {
        decode(b);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.isup.parameters.ISUPParameter#getCode()
     */
    public int getCode() {
        // TODO Auto-generated method stub
        return _PARAMETER_CODE;
    }

    public void decode(ByteBuf b) throws ParameterException {        
    }

    public void encode(ByteBuf b) throws ParameterException {        
    }
}