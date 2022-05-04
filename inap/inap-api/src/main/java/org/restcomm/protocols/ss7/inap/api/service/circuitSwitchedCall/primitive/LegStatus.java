/*
 * Mobius Software LTD
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
