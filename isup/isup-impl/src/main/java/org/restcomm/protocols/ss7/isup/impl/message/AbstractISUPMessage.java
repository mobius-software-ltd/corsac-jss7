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

package org.restcomm.protocols.ss7.isup.impl.message;

import org.restcomm.protocols.ss7.isup.ISUPMessageFactory;
import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;

import io.netty.buffer.ByteBuf;

/**
 * Base class for implementation of ISUP Messages.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public abstract class AbstractISUPMessage implements ISUPMessage {
	/**
     * Decodes this element from passed buffer. This array must contain only element data. however in case of constructor
     * elements it may contain more information elements that consist of tag, length and contents elements, this has to be
     * handled by specific implementation of this method.
     *
     * @param b - array containing body of parameter.
     * @param isupMessageFactory
     * @param parameterFactory - factory which will be used to create specific params.
     * @return
     */
    public abstract void decode(ByteBuf b, ISUPMessageFactory isupMessageFactory, ISUPParameterFactory parameterFactory) throws ParameterException;

    /**
     * Encodes message into buffer. See B.4/Q.763 - page 119)
     *
     * @throws ParameterException
     */
    public abstract void encode(ByteBuf buffer) throws ParameterException;
}
