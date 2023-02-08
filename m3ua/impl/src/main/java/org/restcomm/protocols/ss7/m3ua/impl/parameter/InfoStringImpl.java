/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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
