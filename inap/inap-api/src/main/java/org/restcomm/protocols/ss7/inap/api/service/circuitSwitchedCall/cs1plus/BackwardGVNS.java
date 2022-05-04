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
InstructionIndicator ::= ENUMERATED {
	noInformation(128),
	dedicatedTerminatingAccess(129),
	switchedTerminatingAccess(130),
	spare(131)
}
</code>
 *
 *
 * @author yulian.oifa
 *
 */
public enum BackwardGVNS {
	noInformation(128),
	dedicatedTerminatingAccess(129),
	switchedTerminatingAccess(130),
	spare(131);

    private int code;

    private BackwardGVNS(int code) {
        this.code = code;
    }

    public static BackwardGVNS getInstance(int code) {
        switch (code) {
            case 128:
                return BackwardGVNS.noInformation;
            case 129:
                return BackwardGVNS.dedicatedTerminatingAccess;  
            case 130:
                return BackwardGVNS.switchedTerminatingAccess;
            case 131:
                return BackwardGVNS.spare;  
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
