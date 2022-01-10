/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.AChBillingChargingCharacteristics;

/**
 * <p>
 * This is the instruction from the gsmSCF to the gsmSSF to start or continue monitoring the call duration
 * </p>
 * <p>
 * See also {@link ApplyChargingReportRequest}
 * </p>
 *
<code>
ApplyCharging ::= OPERATION
	ARGUMENT ApplyChargingArg
	ERRORS {
		MissingParameter,
		UnexpectedComponentSequence,
		UnexpectedParameter,
		UnexpectedDataValue,
		ParameterOutOfRange,
		SystemFailure,
		TaskRefused
}
-- Direction: SCF -> SSF, Timer: Tac -- This operation is used for interacting from the SCF with the SSF charging mechanisms. The
-- ApplyChargingReport operation provides the feedback from the SSF to the SCF.

ApplyChargingArg ::= SEQUENCE {
	aChBillingChargingCharacteristics[0] AChBillingChargingCharacteristics,
	sendCalculationToSCPIndication [1] BOOLEAN DEFAULT FALSE,
	partyToCharge [2] LegID OPTIONAL,
	extensions [3] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface ApplyChargingRequest extends CircuitSwitchedCallMessage {

    AChBillingChargingCharacteristics getAChBillingChargingCharacteristics();

    Boolean getSendCalculationToSCPIndication();
    
    LegID getPartyToCharge();

    CAPINAPExtensions getExtensions();    
}
