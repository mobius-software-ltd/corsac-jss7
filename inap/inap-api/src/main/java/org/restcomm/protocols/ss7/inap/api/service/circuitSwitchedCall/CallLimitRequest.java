/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
