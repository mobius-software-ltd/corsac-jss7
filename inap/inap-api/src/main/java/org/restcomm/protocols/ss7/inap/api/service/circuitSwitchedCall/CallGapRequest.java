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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapIndicators;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapTreatment;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;

/**
 *
<code>
CallGap ::= OPERATION
ARGUMENT CallGapArg
-- Direction: SCF -> SSF, Timer: Tcg -- This operation is used to request the SSF to reduce the rate at which specific service requests
-- are sent to the SCF.

CallGapArg ::= SEQUENCE {
	gapCriteria [0] GapCriteria,
	gapIndicators [1] GapIndicators,
	controlType [2] ControlType OPTIONAL,
	gapTreatment [3] GapTreatment OPTIONAL,
	extensions [4] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}

-- From CS1+ Spec
CallGapArg ::= SEQUENCE {
	startTime [PRIVATE 01] DateAndTime OPTIONAL,
	gapCriteria [00] GapCriteria,
	gapIndicators [01] GapIndicators,
	controlType [02] ControlType OPTIONAL,
	gapTreatment [03] GapTreatment OPTIONAL,
	extensions [04] SEQUENCE SIZE (1..7) OF ExtensionField1 OPTIONAL
‐‐ ...
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface CallGapRequest extends CircuitSwitchedCallMessage {

	DateAndTime getStartTime();
	
    GapCriteria getGapCriteria();

    GapIndicators getGapIndicators();

    ControlType getControlType();

    GapTreatment getGapTreatment();

    CAPINAPExtensions getExtensions();

}