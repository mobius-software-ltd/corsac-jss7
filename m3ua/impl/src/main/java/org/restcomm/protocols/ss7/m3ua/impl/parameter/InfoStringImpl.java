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

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.m3ua.parameter.InfoString;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class InfoStringImpl extends ParameterImpl implements InfoString {

    private String string;

    protected InfoStringImpl(ByteBuf value) {
        this.string = value.toString(Charset.forName("uS-ASCII"));
    }

    protected InfoStringImpl(String string) {
        this.tag = Parameter.INFO_String;
        this.string = string;
    }

    public String getString() {
        return this.string;
    }

    @Override
    protected ByteBuf getValue() {
        return Unpooled.wrappedBuffer(this.string.getBytes());
    }

    @Override
    public String toString() {
        return String.format("InfoString : string = %s ", this.string);
    }

}
