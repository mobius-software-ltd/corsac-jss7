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

import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

/**
 *
 ReleaseCallPartyConnection ::= OPERATION
ARGUMENT ReleaseCallPartyConnectionArg
RESULT
ERRORS {
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter,
	UnknownLegID
}
‐‐ Direction SCF ‐> SSF, Timer: Trcp
‐‐ Based on Q1218 Appendix I.

ReleaseCallPartyConnectionArg ::= SEQUENCE {
	legToBeReleased [00] SendingSideID,
	releaseCause [02] Cause OPTIONAL
‐‐ ...
}

Q.1218 Flavour
	ReleaseCallPartyConnection ::= OPERATION
ARGUMENT
	ReleaseCallPartyConnectionArg
RESULT
	CallPartyHandlingResultsArg
ERRORS {
	DataAlreadyExists,
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}

ReleaseCallPartyConnectionArg ::= SEQUENCE {
	legToBeReleased [0] LegID,
	callID [1] CallID OPTIONAL,
	releaseCause [2] Cause OPTIONAL
}
-- OPTIONAL denotes network operator specific use. Common Data Types
CallID ::= INTEGER
-- Indicates an identifier to reference an instance of a Call accessible to the SCF. Refer to
-- 4.2.2.1/Q.1214 for a description of Call Segment.

CallPartyHandlingResultsArg ::= SEQUENCE OF LegInformation
 *
 * @author yulian.oifa
 *
 */
public interface ReleaseCallPartyConnectionRequest extends CircuitSwitchedCallMessage {

	LegType getLegToBeReleased();
	
	Integer getCallID();
	
    CauseIsup getReleaseCause();
}