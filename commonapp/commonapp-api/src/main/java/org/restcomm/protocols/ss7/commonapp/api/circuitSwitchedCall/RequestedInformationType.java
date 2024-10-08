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
 RequestedInformationType ::= ENUMERATED { callAttemptElapsedTime (0), callStopTime (1), callConnectedElapsedTime (2),
 * releaseCause (30) }
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public enum RequestedInformationType {

    callAttemptElapsedTime(0), callStopTime(1), callConnectedElapsedTime(2), releaseCause(30);

    private int code;

    private RequestedInformationType(int code) {
        this.code = code;
    }

    public static RequestedInformationType getInstance(int code) {
        switch (code) {
            case 0:
                return RequestedInformationType.callAttemptElapsedTime;
            case 1:
                return RequestedInformationType.callStopTime;
            case 2:
                return RequestedInformationType.callConnectedElapsedTime;
            case 30:
                return RequestedInformationType.releaseCause;
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
