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

package org.restcomm.protocols.ss7.tcap.api;

/**
 * Message type tag - it holds whole representation of tag - along with universal and other bits set.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public enum MessageType {

    Unidirectional(0x61), Begin(0x62), End(0x64), Continue(0x65), Abort(0x67), Unknown(-1);

    private int value = -1;

    private MessageType(int value) {
        this.value = value;
    }

    public static MessageType getValue(int t) {
        switch (t) {
            case 0x61:
                return Unidirectional;
            case 0x62:
                return Begin;
            case 0x65:
                return Continue;
            case 0x64:
                return End;
            case 0x67:
                return Abort;
            default:
                return Unknown;
        }
    }

    public int getValue() {
        return this.value;
    }
}