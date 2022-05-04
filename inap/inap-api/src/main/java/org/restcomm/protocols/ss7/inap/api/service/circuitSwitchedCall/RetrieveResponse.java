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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemInformation;

import io.netty.buffer.ByteBuf;

/**
<code>
Retrieve ::= OPERATION
ARGUMENT RetrieveArg
RESULT RetrieveResultArg
ERRORS {
	Congestion,
	ErrorInParameterValue,
	ExecutionError,
	InfoNotAvailable,
	InvalidDataItemID,
	NotAuthorized,
	ParameterMissing,
	OtherError
}
‐‐ Direction SCF ‐> SDF, Timer: Tret

RetrieveResultArg ::= SEQUENCE {
	operationreturnID [01] OCTET STRING ('010001'H),
	dataItemInformation [02] DataItemInformation
‐‐ ...
}
</code>
*
 * @author yulian.oifa
 *
 */
public interface RetrieveResponse extends CircuitSwitchedCallMessage {

    ByteBuf getOperationReturnID();
    
    DataItemInformation getDataItemInformation();
}