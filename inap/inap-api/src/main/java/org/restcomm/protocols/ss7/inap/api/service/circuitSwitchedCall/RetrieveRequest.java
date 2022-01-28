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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ApplicationID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemID;

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

RetrieveArg ::= SET {
	operationID [01] OCTET STRING ('0100'H),
	applicationID [02] ApplicationID,
	dataItemID [03] DataItemID
‐‐ ...
}
</code>
*
 * @author yulian.oifa
 *
 */
public interface RetrieveRequest extends CircuitSwitchedCallMessage {

    ByteBuf getOperationID();

    ApplicationID getApplicationID();

    DataItemID getDataItemID();
}
