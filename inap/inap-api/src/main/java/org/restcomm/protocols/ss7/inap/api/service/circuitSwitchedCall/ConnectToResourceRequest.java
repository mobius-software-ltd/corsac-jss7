/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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