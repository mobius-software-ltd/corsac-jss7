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

package org.restcomm.protocols.ss7.tcap.asn;

import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
/**
 * 
 * @author yulianoifa
 *
 */
public enum DialogServiceUserType {

    Null(0), NoReasonGive(1), AcnNotSupported(2);

    private int type = -1;

    DialogServiceUserType(int t) {
        this.type = t;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    public static DialogServiceUserType getFromInt(int t) throws ParseException {
        if (t == 0) {
            return Null;
        } else if (t == 1) {
            return NoReasonGive;
        } else if (t == 2) {
            return AcnNotSupported;
        }

        throw new ParseException(PAbortCauseType.IncorrectTxPortion, null, "Wrong value of type: " + t);
    }

}
