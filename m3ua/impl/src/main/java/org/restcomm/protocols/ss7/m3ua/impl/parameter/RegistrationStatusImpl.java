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

import org.restcomm.protocols.ss7.m3ua.parameter.Parameter;
import org.restcomm.protocols.ss7.m3ua.parameter.RegistrationStatus;

/**
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class RegistrationStatusImpl extends ParameterImpl implements RegistrationStatus {

    private int status;

    public RegistrationStatusImpl(ByteBuf data) {
        this.status = 0;
        this.status |= data.readByte() & 0xFF;
        this.status <<= 8;
        this.status |= data.readByte() & 0xFF;
        this.status <<= 8;
        this.status |= data.readByte() & 0xFF;
        this.status <<= 8;
        this.status |= data.readByte() & 0xFF;

        this.tag = Parameter.Registration_Status;
    }

    public RegistrationStatusImpl(int status) {
        this.tag = Parameter.Registration_Status;
        this.status = status;
    }

    @Override
    protected ByteBuf getValue() {
        ByteBuf data = Unpooled.buffer(4);
        data.writeByte((byte) (status >>> 24));
        data.writeByte((byte) (status >>> 16));
        data.writeByte((byte) (status >>> 8));
        data.writeByte((byte) (status));

        return data;
    }

    public int getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return String.format("RegistrationStatus status=%d", status);
    }

}
