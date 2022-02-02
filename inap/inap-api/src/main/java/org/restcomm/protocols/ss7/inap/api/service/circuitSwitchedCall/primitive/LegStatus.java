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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

/**
*
<code>
LegStatus ::= ENUMERATED {
	connected(0),
	unconnected(1),
	pending(2),
	interacting(3) -- user connected to a resource
}
-- Indicates the state of the call party.
</code>

*
* @author yulian.oifa
*
*/
public enum LegStatus {
	connected(0),unconnected(1), pending(2),interacting(3);

    private int code;

    private LegStatus(int code) {
        this.code = code;
    }

    public static LegStatus getInstance(int code) {
        switch (code) {
        	case 0:
        		return LegStatus.connected;
        	case 1:	
                return LegStatus.unconnected;
            case 2:
                return LegStatus.pending;
            case 3:
                return LegStatus.interacting;
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }

}
