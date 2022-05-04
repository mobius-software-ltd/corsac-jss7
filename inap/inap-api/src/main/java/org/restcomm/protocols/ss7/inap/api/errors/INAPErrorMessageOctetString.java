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

package org.restcomm.protocols.ss7.inap.api.errors;

import io.netty.buffer.ByteBuf;

/**
 *
 Currently used solely for CS1+
 
 Congestion ::= ERROR
	PARAMETER
		operationReturnID [01] OCTET STRING ('010001'H)
 ErrorInParameterValue ::= ERROR
	PARAMETER
		operationReturnID [01] OCTET STRING ('010001'H)
 ExecutionError ::= ERROR
	PARAMETER
		operationReturnID [01] OCTET STRING ('010001'H)
 IllegalCombinationOfParameters ::= ERROR
	PARAMETER
		operationReturnID [01] OCTET STRING ('010001'H)
 InfoNotAvailable ::= ERROR
	PARAMETER
		operationReturnID [01] OCTET STRING ('010001'H)
 InvalidDataItemID ::= ERROR
	PARAMETER
		operationReturnID [01] OCTET STRING ('010001'H)
 NotAuthorized ::= ERROR
	PARAMETER
		operationReturnID [01] OCTET STRING ('010001'H)
 ParameterMissing ::= ERROR
	PARAMETER
		operationReturnID [01] OCTET STRING ('010001'H)
 OtherError ::= ERROR
	PARAMETER
		operationReturnID [01] OCTET STRING ('010001'H)
 *
 * @author yulian.oifa
 *
 */
public interface INAPErrorMessageOctetString extends INAPErrorMessage {
   ByteBuf getValue();
}