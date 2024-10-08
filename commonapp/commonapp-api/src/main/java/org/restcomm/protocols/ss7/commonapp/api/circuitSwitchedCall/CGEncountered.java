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

package org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall;

/**
 *
<code>
CGEncountered ::= ENUMERATED {
  noCGencountered (0),
  manualCGencountered (1),
  scpOverload (2)
}
-- Indicates the type of automatic call gapping encountered, if any.
</code>
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public enum CGEncountered {
    noCGencountered(0), manualCGencountered(1), scpOverload(2);

    private int code;

    private CGEncountered(int code) {
        this.code = code;
    }

    public static CGEncountered getInstance(int code) {
        switch (code) {
            case 0:
                return CGEncountered.noCGencountered;
            case 1:
                return CGEncountered.manualCGencountered;
            case 2:
                return CGEncountered.scpOverload;
            default:
                return null;
        }
    }

    public int getCode() {
        return code;
    }
}
