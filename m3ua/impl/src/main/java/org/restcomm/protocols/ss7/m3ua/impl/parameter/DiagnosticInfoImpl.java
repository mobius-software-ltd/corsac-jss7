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

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.m3ua.parameter.DiagnosticInfo;
import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class DiagnosticInfoImpl extends ParameterImpl implements DiagnosticInfo {

    private String info;

    public DiagnosticInfoImpl(String info) {
        this.info = info;
        this.tag = Parameter.Diagnostic_Information;
    }

    public DiagnosticInfoImpl(ByteBuf value) {
        this.tag = Parameter.Diagnostic_Information;
        this.info = value.toString(Charset.forName("US-ASCII"));
    }

    @Override
    protected ByteBuf getValue() {
        return Unpooled.wrappedBuffer(this.info.getBytes());
    }

    public String getInfo() {
        return this.info;
    }

}
