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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CallResultCS1;

import io.netty.buffer.ByteBuf;

/**
ApplyChargingReport ::= OPERATION
	ARGUMENT ApplyChargingReportArg
	ERRORS {
		MissingParameter,
		UnexpectedComponentSequence,
		UnexpectedParameter,
		UnexpectedDataValue,
		ParameterOutOfRange,
		SystemFailure,
		TaskRefused
}
-- Direction: SSF -> SCF, Timer: Tacr -- This operation is used by the SSF to report to the SCF the occurrence of a specific charging event as
-- requested by the SCF using the ApplyCharging operation.

ApplyChargingReportArg ::= CallResult

CallResult ::= OCTET STRING (SIZE (minCallResultLength ..maxCallResultLength))
</code>
 *
 * @author yulian.oifa
 *
 */
public interface ApplyChargingReportRequest extends CircuitSwitchedCallMessage {

	ByteBuf getCallResult();
    
    CallResultCS1 getCallResultCS1();

}
