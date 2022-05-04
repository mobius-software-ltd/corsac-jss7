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

/**
<code>
TMidCall ::= OPERATION
ARGUMENT MidCallArg
ERRORS {
	MissingCustomerRecord,
	MissingParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SSF –> SCF, Timer: Ttmc
-- This operation is used to indicate that a feature request is received from the terminating party (e.g. hook
-- flash, ISDN feature activation Q.931 HOLD or RETrieve message). (DP 16 – T_Mid_Call.)
-- For additional information on this operation, refer to 4.2.2.2.2-10/Q.1214.

MidCallArg ::= SEQUENCE {
	dpSpecificCommonParameters [0] DpSpecificCommonParameters,
	calledPartyBusinessGroupID [1] CalledPartyBusinessGroupID OPTIONAL,
	calledPartySubaddress [2] CalledPartySubaddress OPTIONAL,
	callingPartyBusinessGroupID [3] CallingPartyBusinessGroupID OPTIONAL,
	callingPartySubaddress [4] CallingPartySubaddress OPTIONAL,
	featureRequestIndicator [5] FeatureRequestIndicator OPTIONAL,
	extensions [6] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	carrier [7] Carrier OPTIONAL
-- ...
}
-- For the OPTIONAL parameters, refer to clause 3 for the trigger detection point processing rules
-- to specify when these parameters are included in the message.
}
</code>
 * 
 * @author yulian.oifa
 *
 */
public interface TMidCallRequest extends MidCallRequest {
}