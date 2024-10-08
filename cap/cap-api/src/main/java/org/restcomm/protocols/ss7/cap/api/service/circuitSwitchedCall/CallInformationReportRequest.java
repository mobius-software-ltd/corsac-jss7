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

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

/**
 *
 callInformationReport {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT CallInformationReportArg {bound} RETURN RESULT
 * FALSE ALWAYS RESPONDS FALSE CODE opcode-callInformationReport} -- Direction: gsmSSF -> gsmSCF, Timer: T cirp -- This
 * operation is used to send specific call information for a single call party to the gsmSCF as -- requested by the gsmSCF in a
 * previous CallInformationRequest.
 *
 * CallInformationReportArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { requestedInformationList [0] RequestedInformationList
 * {bound}, extensions [2] Extensions {bound} OPTIONAL, legID [3] ReceivingSideID DEFAULT receivingSideID:leg2, ... }
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CallInformationReportRequest extends CircuitSwitchedCallMessage {

    List<RequestedInformation> getRequestedInformationList();

    CAPINAPExtensions getExtensions();

    LegType getLegID();
}