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

import java.util.List;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.LegInformation;

/**
 *
<code>
<p>
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
</code>
 *
 * @author yulian.oifa
 *
 */
public interface ReleaseCallPartyConnectionResponse extends CircuitSwitchedCallMessage {
	List<LegInformation> getLegInformation();						       
}