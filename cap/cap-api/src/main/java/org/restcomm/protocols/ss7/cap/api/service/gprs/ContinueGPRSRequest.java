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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

/**
 *
 continueGPRS OPERATION ::= { ARGUMENT ContinueGPRSArg RETURN RESULT FALSE ERRORS {missingParameter | unknownPDPID | -
 * Direction: gsmSCF -> gprsSSF, Timer: Tcueg -- This operation is used to request the gprsSSF to proceed with processing at the
 * DP at -- which it previously suspended processing to await gsmSCF instructions (i.e., proceed to -- the next point in
 * processing in the Attach/Detach state model or PDP Context -- state model) substituting new data from the gsmSCF.
 *
 * ContinueGPRSArg ::= SEQUENCE { pDPID [0] PDPID OPTIONAL, ... }
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ContinueGPRSRequest extends GprsMessage {

    PDPID getPDPID();

}