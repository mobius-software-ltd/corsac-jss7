/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;

/**
 *
moveLeg {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT MoveLegArg {bound}
   RETURN RESULT TRUE
   ERRORS {
     missingParameter | systemFailure | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter | unknownLegID
   }
   CODE opcode-moveLeg
}
-- Direction: gsmSCF -> gsmSSF, Timer: Tml
-- This operation is used by the gsmSCF to move a leg from one call segment to another call segment
-- within the same call segment association.

MoveLegArg {PARAMETERS-BOUND : bound} ::= SEQUENCE{
  legIDToMove  [0] LegID,
  extensions   [2] Extensions {bound} OPTIONAL,
  ...
}
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface MoveLegRequest extends CircuitSwitchedCallMessage {

    LegID getLegIDToMove();

    CAPINAPExtensions getExtensions();
}
