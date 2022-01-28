/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

/**
 * Start time:17:31:22 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.ClosedUserGroupInterlockCode;

/**
 * Start time:17:31:22 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ClosedUserGroupInterlockCodeImpl extends AbstractISUPParameter implements ClosedUserGroupInterlockCode {
	// XXX: this parameter is weird, it does not follow general convention of
    // parameters :/
    private ByteBuf niDigits = null;
    private int binaryCode = 0;

    public ClosedUserGroupInterlockCodeImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public ClosedUserGroupInterlockCodeImpl() {
        super();

    }

    /**
     *
     * @param niDigits - arrays of NetworkIdentiti digits, it must be of length 4, if only 2 digits are required - last two must
     *        be empty (zero, default int value)
     * @param binaryCode
     */
    public ClosedUserGroupInterlockCodeImpl(ByteBuf niDigits, int binaryCode) {
        super();

        // FIXME: add check for range ?
        this.setNiDigits(niDigits);
        this.binaryCode = binaryCode;
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 4) {
            throw new ParameterException("buffer must not be null and must have length of 4");
        }
        int v = 0;
        this.niDigits = Unpooled.buffer(4);

        for (int i = 0; i < 2; i++) {
            v = 0;
            v = b.readByte();
            this.niDigits.writeByte((byte) ((v >> 4) & 0x0F));
            this.niDigits.writeByte((byte) (v & 0x0F));
        }

        this.binaryCode = (b.readByte() << 8) & 0xFF00;
        this.binaryCode |= b.readByte();
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        int v = 0;
        ByteBuf data=getNiDigits();
        for (int i = 0; i < 2; i++) {
            v = 0;

            v |= (data.readByte() & 0x0F) << 4;
            v |= (data.readByte() & 0x0F);

            buffer.writeByte((byte) v);
        }
        buffer.writeByte((byte) (this.binaryCode >> 8));
        buffer.writeByte((byte) this.binaryCode);
    }

    public ByteBuf getNiDigits() {
        return niDigits;
    }

    public void setNiDigits(ByteBuf niDigits) {
        if (niDigits == null || niDigits.readableBytes() != 4) {
            throw new IllegalArgumentException();
        }

        this.niDigits = niDigits;
    }

    public int getBinaryCode() {
        return binaryCode;
    }

    public void setBinaryCode(int binaryCode) {
        this.binaryCode = binaryCode;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
