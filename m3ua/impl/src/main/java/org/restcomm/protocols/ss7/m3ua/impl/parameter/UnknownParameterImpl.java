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

package org.restcomm.protocols.ss7.m3ua.impl.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author kulikov
 * @author yulianoifa
 */
public class UnknownParameterImpl extends ParameterImpl {

    private ByteBuf value;

    protected UnknownParameterImpl(int tag, int length, ByteBuf value) {
        this.tag = (short) tag;
        this.length = (short) length;
        this.value = value;
    }

    @Override
    protected ByteBuf getValue() {
        return Unpooled.wrappedBuffer(value);
    }

    @Override
    public String toString() {
        return String.format("Unknown parameter: tag=%d, length=%d", tag, length);
    }

}
