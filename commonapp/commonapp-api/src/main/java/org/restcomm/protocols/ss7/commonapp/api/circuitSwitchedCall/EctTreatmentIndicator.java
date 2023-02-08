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
ectTreatmentIndicator [52] OCTET STRING (SIZE(1)) OPTIONAL,
-- applicable to InitialDP, Connect and ContinueWithArgument
-- acceptEctRequest 'xxxx xx01'B
-- rejectEctRequest 'xxxx xx10'B
-- if absent from Connect or ContinueWithArgument,
-- then CAMEL service does not affect explicit call transfer treatment
</code>
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public enum EctTreatmentIndicator {
    acceptEctRequest(1), rejectEctRequest(2);

    private int code;

    private EctTreatmentIndicator(int code) {
        this.code = code;
    }

    public static EctTreatmentIndicator getInstance(int code) {
        switch (code & 0x03) {
            case 1:
                return EctTreatmentIndicator.acceptEctRequest;
            case 2:
                return EctTreatmentIndicator.rejectEctRequest;
            default:
                return null;
        }
    }

    public int getCode() {
        return this.code;
    }
}
