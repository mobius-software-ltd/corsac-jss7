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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus;

/**
 *
<code>
protocolIdentifier ::= ENUMERATED {
	inap_cs1(0),
	ericson_cs1_plus(1)
}
</code>
 *
 *
 * @author yulian.oifa
 *
 */
public enum ProtocolIdentifier {
	inap_cs1(0),
	ericson_cs1_plus(1);

    private int code;

    private ProtocolIdentifier(int code) {
        this.code = code;
    }

    public static ProtocolIdentifier getInstance(int code) {
        switch (code) {
            case 0:
                return ProtocolIdentifier.inap_cs1;
            case 1:
                return ProtocolIdentifier.ericson_cs1_plus;                        
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
