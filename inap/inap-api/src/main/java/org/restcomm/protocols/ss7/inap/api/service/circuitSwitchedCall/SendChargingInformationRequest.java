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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;

/**
 *
 *SendChargingInformation ::= OPERATION
ARGUMENT SendChargingInformationArg
ERRORS {
	MissingParameter,
	UnexpectedComponentSequence,
	UnexpectedParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnknownLegID
}
-- Direction: SCF -> SSF, Timer: Tsci
-- This operation is used to instruct the SSF on the charging information to be sent by the SSF.
-- The charging information can either be sent back by means of signalling or internal if the SSF is
-- located in the local exchange. In the local exchange this information may be used to update the
-- charge meter or to create a standard call record. The charging scenario supported by this operation is
-- scenario 3.2 (refer to Annex B where these are defined).

SendChargingInformationArg ::= SEQUENCE {
	sCIBillingChargingCharacteristics[0] SCIBillingChargingCharacteristics,
	legID [1] LegID,
	extensions [2] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
 * @author yulian.oifa
 *
 */
public interface SendChargingInformationRequest extends CircuitSwitchedCallMessage {

    SCIBillingChargingCharacteristics getSCIBillingChargingCharacteristics();

    LegType getPartyToCharge();

    CAPINAPExtensions getExtensions();

}