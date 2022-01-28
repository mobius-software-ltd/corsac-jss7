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
 * Start time:16:56:29 2009-07-17<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 *
 */
package org.restcomm.protocols.ss7.isup.util;

import io.netty.buffer.ByteBuf;

/**
 * Start time:16:56:29 2009-07-17<br>
 * Project: restcomm-isup-stack<br>
 * Small class with some utility methods to work on raw without stack.
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ISUPUtility {

    public static String toHex(ByteBuf b) {

        String out = "";

        for (int index = 0; index < b.readableBytes(); index++) {

            out += "b[" + index + "][" + Integer.toHexString(b.readByte()) + "]\n";

            // out+="\n";
        }

        return out;

    }
}
