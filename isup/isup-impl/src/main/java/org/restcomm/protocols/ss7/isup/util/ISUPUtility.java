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

package org.restcomm.protocols.ss7.isup.util;

import io.netty.buffer.ByteBuf;

/**
 * Start time:16:56:29 2009-07-17<br>
 * Project: restcomm-isup-stack<br>
 * Small class with some utility methods to work on raw without stack.
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
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
