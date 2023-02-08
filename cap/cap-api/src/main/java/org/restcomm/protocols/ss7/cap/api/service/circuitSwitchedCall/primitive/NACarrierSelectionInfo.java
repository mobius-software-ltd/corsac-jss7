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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

/**
 *
<code>
NACarrierSelectionInfo ::= OCTET STRING (SIZE (1))
-- NA carrier selection information octet carries the same values as ANSI
-- ISUP T1.113 [12]: '00'H – not indicated or not explicitly provided
-- '01'H – subscribed not dialled
-- '02'H – subscribed and dialled
-- '03'H – subscribed with dialling undetermined
-- '04'H – dialled CIC not subscribed
</code>
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public enum NACarrierSelectionInfo {

    notIndication(0),subscribedNotDialed(1), subscribedAndDialed(2), subscribedWithDialingUndetermined(3), dialedCICNotSubscribed(4);

    private int code;

    private NACarrierSelectionInfo(int code) {
        this.code = code;
    }

    public static NACarrierSelectionInfo getInstance(int code) {
        switch (code & 0x07) {
        	case 0:
        		return NACarrierSelectionInfo.notIndication;
            case 1:
                return NACarrierSelectionInfo.subscribedNotDialed;
            case 2:
                return NACarrierSelectionInfo.subscribedAndDialed;
            case 3:
            	return NACarrierSelectionInfo.subscribedWithDialingUndetermined;
            case 4:
            	return NACarrierSelectionInfo.dialedCICNotSubscribed;
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }

}
