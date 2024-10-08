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
package org.restcomm.protocols.ss7.cap.api.service.gprs;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

/**
 *
 connectGPRS {PARAMETERS-BOUND: bound} OPERATION::= { ARGUMENT ConnectGPRSArg {bound} RETURN RESULT FALSE ERRORS
 * {missingParameter | parameterOutOfRange | unknownPDPID | systemFailure | taskRefused | unexpectedComponentSequence |
 * unexpectedDataValue | unexpectedParameter} CODE opcode-connectGPRS} -- Direction: gsmSCF -> gprsSSF, Timer: Tcong -- This
 * operation is used to modify the Access Point Name used when establishing a PDP Context.
 *
 * ConnectGPRSArg {PARAMETERS-BOUND: bound}::= SEQUENCE { accessPointName [0] AccessPointName {bound}, pdpID [1] PDPID OPTIONAL,
 * ... }
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ConnectGPRSRequest extends GprsMessage {

    AccessPointName getAccessPointName();

    PDPID getPDPID();

}