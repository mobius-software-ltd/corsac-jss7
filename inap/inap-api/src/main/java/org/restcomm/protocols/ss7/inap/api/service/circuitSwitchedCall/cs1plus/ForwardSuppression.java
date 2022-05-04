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
BackwardSuppression ::= ENUMERATED {
	additionalCalledNumber(0),
	additionalCallingPartyNumber(1),
	additionalOriginalCalledNumber(2),
	additionalRedirectingNumber(3),
	additionalOriginalCalledINNumber(4),
	privateNetworkTravellingClassMark(5),
	businessCommunicationGroupID(6),
	callingPartySubaddress(7),
	calledPartySubaddress(8),
	locationNumber(9)
}
</code>
 *
 *
 * @author yulian.oifa
 *
 */
public enum ForwardSuppression {
	additionalCalledNumber(0),
	additionalCallingPartyNumber(1),
	additionalOriginalCalledNumber(2),
	additionalRedirectingNumber(3),
	additionalOriginalCalledINNumber(4),
	privateNetworkTravellingClassMark(5),
	businessCommunicationGroupID(6),
	callingPartySubaddress(7),
	calledPartySubaddress(8),
	locationNumber(9);

    private int code;

    private ForwardSuppression(int code) {
        this.code = code;
    }

    public static ForwardSuppression getInstance(int code) {
        switch (code) {
            case 0:
                return ForwardSuppression.additionalCalledNumber;
            case 1:
                return ForwardSuppression.additionalCallingPartyNumber;            
            case 2:
                return ForwardSuppression.additionalOriginalCalledNumber;
            case 3:
                return ForwardSuppression.additionalRedirectingNumber;
            case 4:
                return ForwardSuppression.additionalOriginalCalledINNumber;
            case 5:
                return ForwardSuppression.privateNetworkTravellingClassMark;            
            case 6:
                return ForwardSuppression.businessCommunicationGroupID;
            case 7:
                return ForwardSuppression.callingPartySubaddress;
            case 8:
                return ForwardSuppression.calledPartySubaddress;
            case 9:
                return ForwardSuppression.locationNumber;
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
