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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;

/**
 *
<code>
PromptAndCollectUserInformation ::= OPERATION
ARGUMENT PromptAndCollectUserInformationArg
RESULT ReceivedInformationArg
ERRORS {
	Cancelled,
	ImproperCallerResponse,
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnavailableResource,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF -> SRF, Timer: Tpc -- This operation is used to interact with a user to collect information.

ReceivedInformationArg ::= CHOICE {
	digitsResponse [0] Digits
}

--- From Q.1218 CS1
ReceivedInformationArg ::= CHOICE {
	digitsResponse [0] Digits,
	iA5Response [1] IA5String
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface PromptAndCollectUserInformationResponse extends CircuitSwitchedCallMessage {

    DigitsIsup getDigitsResponse();
    
    String getIA5Response();
}