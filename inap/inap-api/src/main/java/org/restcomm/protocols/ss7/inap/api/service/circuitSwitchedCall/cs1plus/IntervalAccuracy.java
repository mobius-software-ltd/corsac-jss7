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
IntervalAccuracy [05] ENUMERATED {
	tenMilliSeconds (01),
	oneHundredMilliSeconds (02),
	seconds (03)
}
</code>
 *
 *
 * @author yulian.oifa
 *
 */
public enum IntervalAccuracy {
	tenMilliSeconds(1),
	oneHundredMilliSeconds(2),
	seconds(3);

    private int code;

    private IntervalAccuracy(int code) {
        this.code = code;
    }

    public static IntervalAccuracy getInstance(int code) {
        switch (code) {
            case 1:
                return IntervalAccuracy.tenMilliSeconds;
            case 2:
                return IntervalAccuracy.oneHundredMilliSeconds;  
            case 3:
                return IntervalAccuracy.seconds;            
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
