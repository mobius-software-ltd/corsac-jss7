/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

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