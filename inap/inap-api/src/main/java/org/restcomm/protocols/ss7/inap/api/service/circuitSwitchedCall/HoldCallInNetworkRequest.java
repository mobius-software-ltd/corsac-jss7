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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.HoldCause;

/**
 *
<code>
HoldCallInNetwork ::= OPERATION
ARGUMENT HoldCallInNetworkArg
ERRORS {
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}

-- Direction: SCF −> SSF, Timer: Thcn
-- This operation is used to provide the capability of queueing a call during the set-up phase (e.g. to provide
-- a call completion to busy, the call would be queued until the destination becomes free).

HoldCallInNetworkArg ::= CHOICE {
	holdcause [0] HoldCause,
	empty [1] NULL
}
-- holdcause is optional and denotes network operator specific use.
</code>
*
 * @author yulian.oifa
 *
 */
public interface HoldCallInNetworkRequest extends CircuitSwitchedCallMessage {

    HoldCause getHoldCause();

    boolean getEmpty();
}