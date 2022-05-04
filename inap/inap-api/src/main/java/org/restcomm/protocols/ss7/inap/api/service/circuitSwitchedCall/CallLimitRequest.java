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

import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapTreatment;
import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.LimitIndicators;

/**
<code>
CallLimit ::= OPERATION
ARGUMENT CallLimitArg
‐‐ Direction SCF ‐> SSF, Timer: Tcl
‐‐ Activates the 'window' traffic management
‐‐ mechanism in SSF

CallLimitArg ::= SEQUENCE {
	startTime [00] DateAndTime OPTIONAL,
	limitCriteria [01] LimitCriteria,
	limitIndicators [02] LimitIndicators,
	limitTreatment [03] LimitTreatment OPTIONAL
‐‐ ...
}
</code>
*
 * @author yulian.oifa
 *
 */
public interface CallLimitRequest extends CircuitSwitchedCallMessage {

	DateAndTime getStartTime();

	GapCriteria getLimitCriteria();

	LimitIndicators getLimitIndicators();
    
	GapTreatment getLimitTreatment();
}
