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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapIndicators;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapTreatment;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

/**
 *
<code>
callGap {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT CallGapArg {bound}
  RETURN RESULT FALSE
  ALWAYS RESPONDS FALSE
  CODE opcode-callGap
}
-- Direction: gsmSCF -> gsmSSF, Timer: T cg
-- This operation is used to request the gsmSSF to reduce the rate at which specific service
-- requests are sent to the gsmSCF.

CallGapArg {PARAMETERS-BOUND : bound}::= SEQUENCE {
  gapCriteria    [0] GapCriteria {bound},
  gapIndicators  [1] GapIndicators,
  controlType    [2] ControlType OPTIONAL
  gapTreatment   [3] GapTreatment {bound} OPTIONAL
  extensions     [4] Extensions {bound} OPTIONAL,
  ...
}
-- OPTIONAL denotes network operator optional. If gapTreatment is not present, then the gsmSSF will
-- use a default treatment depending on network operator implementation.
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CallGapRequest extends CircuitSwitchedCallMessage {

    GapCriteria getGapCriteria();

    GapIndicators getGapIndicators();

    ControlType getControlType();

    GapTreatment getGapTreatment();

    CAPINAPExtensions getExtensions();

}