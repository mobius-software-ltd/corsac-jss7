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

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

/**
*
DisconnectForwardConnection ::= OPERATION
ERRORS {
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence
}
-- Direction: SCF -> SSF, Timer: Tdfc
-- This operation is used to disconnect a forward temporary connection or a connection to a resource.

DisconnectForwardConnectionArg ::= [PRIVATE 01] SEQUENCE {
	legID [00] SendingSideID
‐‐ ...
}
‐‐ Argument is optional.
‐‐ No argument indicates CP.
*
* @author yulian.oifa
*
*/
public interface DisconnectForwardConnectionCS1PlusRequest extends DisconnectForwardConnectionRequest {
	LegType getLegID();
}