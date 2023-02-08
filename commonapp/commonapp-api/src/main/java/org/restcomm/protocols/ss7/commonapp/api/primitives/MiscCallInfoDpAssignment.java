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

package org.restcomm.protocols.ss7.commonapp.api.primitives;

/**
 *
<code>
MiscCallInfo ::= SEQUENCE {
  dpAssignment [1] ENUMERATED {individualLine(0), groupBased(1), officeBased(2)
} OPTIONAL
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public enum MiscCallInfoDpAssignment {
    individualLine(0), groupBased(1), officeBased(2);

    private int code;

    private MiscCallInfoDpAssignment(int code) {
        this.code = code;
    }

    public static MiscCallInfoDpAssignment getInstance(int code) {
        switch (code) {
            case 0:
                return MiscCallInfoDpAssignment.individualLine;
            case 1:
                return MiscCallInfoDpAssignment.groupBased;
            case 2:
                return MiscCallInfoDpAssignment.officeBased;
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
