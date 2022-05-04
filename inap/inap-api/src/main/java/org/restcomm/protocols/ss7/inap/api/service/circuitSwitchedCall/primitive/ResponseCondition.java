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
ResponseCondition ::= ENUMERATED {
intermediateResponse(0),
lastResponse(1)
-- additional values are for further study.
}
-- ResponseCondition is used to identify the reason why ServiceFilteringResponse operation is sent.
-- intermediateresponse identifies that service filtering is running and the interval time is expired and
-- a call is received, or that service filtering is running and the threshold value is reached.
-- lastResponse identifies that the duration time is expired and service filtering has been finished or
-- that the stop time is met and service filtering has been finished.
</code>
 *
 *
 * @author yulian.oifa
 *
 */
public enum ResponseCondition {
	intermediateResponse(0),
	lastResponse(1);

    private int code;

    private ResponseCondition(int code) {
        this.code = code;
    }

    public static ResponseCondition getInstance(int code) {
        switch (code) {
            case 0:
                return ResponseCondition.intermediateResponse;
            case 1:
                return ResponseCondition.lastResponse; 
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
