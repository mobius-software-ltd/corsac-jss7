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

import io.netty.buffer.ByteBuf;

/**
 *
<code>
FurnishChargingInformation ::= OPERATION
ARGUMENT FurnishChargingInformationArg
ERRORS {
	MissingParameter,
	TaskRefused,
	UnexpectedComponentSequence,	
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF -> SSF, Timer: Tfci
-- This operation is used to request the SSF to generate, register a call record or to include some
-- information in the default call record. The registered call record is intended for off-line charging
-- of the call. The charging scenarios supported by this operation are: 2.2, 2.3 and 2.4 (refer to Annex B
-- where these are defined).

-- This parameter indicates an extension of an argument data type. Its contents is network operator
-- specific.
FCIBillingChargingCharacteristics ::= OCTET STRING (SIZE (minFCIBillingChargingLength ..
 maxFCIBillingChargingLength))
</code>
 *
 * @author yulian.oifa
 *
 */ 
public interface FurnishChargingInformationRequest extends CircuitSwitchedCallMessage {
	ByteBuf getFCIBillingChargingCharacteristics();
}