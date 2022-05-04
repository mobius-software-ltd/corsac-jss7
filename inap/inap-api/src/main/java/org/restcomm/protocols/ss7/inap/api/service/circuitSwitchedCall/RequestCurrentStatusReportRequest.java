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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;

/**
 *
<code>
<p>
RequestCurrentStatusReport ::= OPERATION
ARGUMENT RequestCurrentStatusReportArg
RESULT RequestCurrentStatusReportResultArg
ERRORS {
	MissingParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedParameter,
	UnknownResource	
}
-- Direction: SCF −> SSF, Timer: Trcs
-- This operation is used to request the SSF to report immediately the busy/idle status of a physical
-- termination resource.

RequestCurrentStatusReportArg ::= ResourceID
</code>
 *
 * @author yulian.oifa
 *
 */
public interface RequestCurrentStatusReportRequest extends CircuitSwitchedCallMessage {
	ResourceID getResourceID();					       
}