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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceAddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;

/**
 *
<code>
ConnectToResource ::= OPERATION
ARGUMENT ConnectToResourceArg
ERRORS {
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF -> SSF, Timer: Tctr
-- This operation is used to connect a call from the SSP to the PE containing the SRF.

ConnectToResourceArg ::= SEQUENCE {
	resourceAddress CHOICE {
		ipRoutingAddress [0] IPRoutingAddress,
		none [3] NULL
	},
	extensions [4] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	serviceInteractionIndicators [30] ServiceInteractionIndicators OPTIONAL
-- ...
}

--- From Q.1218 CS1
ConnectToResourceArg ::= SEQUENCE {
	resourceAddress CHOICE {
		ipRoutingAddress [0] IPRoutingAddress,
		legID [1] LegID,
		both [2] SEQUENCE {
			ipRoutingAddress [0] IPRoutingAddress,
 			legID [1] LegID
 		},
		none [3] NULL
	},
	extensions [4] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	serviceInteractionIndicators [30] ServiceInteractionIndicators OPTIONAL
-- ...
}
</code>
*
 * @author yulian.oifa
 *
 */
public interface ConnectToResourceRequest extends CircuitSwitchedCallMessage {

	ResourceAddress getResourceAddress();

    boolean getResourceAddressNull();

    CAPINAPExtensions getExtensions();

    ServiceInteractionIndicators getServiceInteractionIndicators();
}