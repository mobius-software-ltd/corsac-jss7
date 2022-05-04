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

import java.io.IOException;

import org.restcomm.protocols.ss7.isup.ParameterException;

import io.netty.buffer.ByteBuf;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public interface Encodable {
    /**
     * Decodes this element from passed buffer array. This array must contain only element data. however in case of constructor
     * elements it may contain more information elements that consist of tag, length and contents elements, this has to be
     * handled by specific implementation of this method.
     *
     * @param b - array containing body of parameter.
     * @return
     */
    void decode(ByteBuf b) throws ParameterException;

    /**
     * Encodes elements into buffer.It contains body, tag and length should be added by enclosing element. ( See B.4/Q.763 - page
     * 119)
     *
     * @throws IOException
     */
    void encode(ByteBuf b) throws ParameterException;
}