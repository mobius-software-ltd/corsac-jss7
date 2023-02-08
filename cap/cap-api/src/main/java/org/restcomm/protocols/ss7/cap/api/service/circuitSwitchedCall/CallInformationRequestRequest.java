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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

/**
 *
 callInformationRequest {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT CallInformationRequestArg {bound} RETURN RESULT
 * FALSE ERRORS {missingParameter | parameterOutOfRange | requestedInfoError | systemFailure | taskRefused |
 * unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter | unknownLegID} CODE opcode-callInformationRequest}
 * -- Direction: gsmSCF -> gsmSSF, Timer: Tcirq -- This operation is used to request the gsmSSF to record specific information
 * about a single -- call party and report it to the gsmSCF (with a CallInformationReport operation).
 *
 * CallInformationRequestArg {PARAMETERS-BOUND : bound}::= SEQUENCE { requestedInformationTypeList [0]
 * RequestedInformationTypeList, extensions [2] Extensions {bound} OPTIONAL, legID [3] SendingSideID DEFAULT sendingSideID:leg2,
 * ... } -- OPTIONAL denotes network operator optional.
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CallInformationRequestRequest extends CircuitSwitchedCallMessage {

    List<RequestedInformationType> getRequestedInformationTypeList();

    CAPINAPExtensions getExtensions();

    LegType getLegID();

}