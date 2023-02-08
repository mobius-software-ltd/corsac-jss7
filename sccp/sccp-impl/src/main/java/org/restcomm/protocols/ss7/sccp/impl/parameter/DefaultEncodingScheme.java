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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.EncodingScheme;
import org.restcomm.protocols.ss7.sccp.parameter.EncodingSchemeType;

/**
 * Default impl which supports simple encoding.
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public class DefaultEncodingScheme implements EncodingScheme {
    public static final EncodingScheme INSTANCE = new DefaultEncodingScheme();
    public static final int SCHEMA_CODE = 0;

    public DefaultEncodingScheme() {
    }

    @Override
    public EncodingSchemeType getType() {
        return EncodingSchemeType.UNKNOWN;
    }

    @Override
    public byte getSchemeCode() {
        return SCHEMA_CODE;
    }

    protected boolean isOdd() {
        //Trick for single codebase
        return false;
    }

    @Override
    public void encode(String digits, ByteBuf buffer) throws ParseException {
    	boolean odd;
        if (getSchemeCode() != 0) {
            odd = isOdd();
        } else {
            odd = digits.length() % 2 != 0;
        }

        int b = 0;

        int count = odd ? digits.length() - 1 : digits.length();

        for (int i = 0; i < count - 1; i += 2) {
            String ds1 = digits.substring(i, i + 1);
            String ds2 = digits.substring(i + 1, i + 2);

            int d1 = Integer.parseInt(ds1, 16);
            int d2 = Integer.parseInt(ds2, 16);

            b = (byte) (d2 << 4 | d1);
            buffer.writeByte(b);
        }

        // if number is odd append last digit with filler
        if (odd) {
            String ds1 = digits.substring(count, count + 1);
            int d = Integer.parseInt(ds1, 16);

            b = (byte) (d & 0x0f);
            buffer.writeByte(b);
        }
    }

    @Override
    public String decode(ByteBuf buffer) throws ParseException {
    	int b;
        StringBuilder digits = new StringBuilder();

        while (buffer.readableBytes() > 0) {
            b = buffer.readByte() & 0xff;

            digits.append(Integer.toHexString(b & 0x0f));
            digits.append(Integer.toHexString((b & 0xf0) >> 4));
        }

        if (isOdd()) {
            digits.deleteCharAt(digits.length() - 1);
        }

        return digits.toString().toLowerCase();
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = getSchemeCode();
        result = prime * result;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        if(this.getSchemeCode() == ((EncodingScheme)obj).getSchemeCode())
            return true;
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"[type=" + getType() + ", code=" + getSchemeCode() + "]";
    }
}
