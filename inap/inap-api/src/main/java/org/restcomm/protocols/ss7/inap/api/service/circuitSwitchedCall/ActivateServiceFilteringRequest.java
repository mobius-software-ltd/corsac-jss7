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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteredCallTreatment;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCharacteristics;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCriteria;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringTimeOut;

/**
 *
<code>
<p>
ActivateServiceFiltering ::= OPERATION
ARGUMENT ActivateServiceFilteringArg
RESULT
ERRORS {
	MissingParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedParameter
}
-- Direction: SCF -> SSF, Timer: Tasf
-- When receiving this operation, the SSF handles calls to destination in a specified manner
-- without sending queries for every detected call. It is used for example for providing televoting
-- or mass calling services. Simple registration functionality (counters) and announcement
-- control may be located at the SSF. The operation initializes the specified counters in the SSF.

ActivateServiceFilteringArg ::= SEQUENCE {
	filteredCallTreatment [0] FilteredCallTreatment,
	filteringCharacteristics [1] FilteringCharacteristics,
	filteringTimeOut [2] FilteringTimeOut,
	filteringCriteria [3] FilteringCriteria,
	startTime [4] DateAndTime OPTIONAL,
	extensions [5] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}

--- from CS1+ Spec
ActivateServiceFilteringArg ::= SEQUENCE {
	filteredCallTreatment [00] FilteredCallTreatment,
	filteringCharacteristics [01] FilteringCharacteristics,
	filteringTimeOut [02] FilteringTimeOut,
	filteringCriteria [03] FilteringCriteria,
	startTime [04] DateAndTime OPTIONAL,
	extensions [05] SEQUENCE SIZE (1..7) OF
	ExtensionField1 OPTIONAL,
	‐‐ ...
	sCFCorrelationInfo [PRIVATE 01] OCTET STRING (SIZE(16)) OPTIONAL
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface ActivateServiceFilteringRequest extends CircuitSwitchedCallMessage {
	FilteredCallTreatment getFilteredCallTreatment();
	
	FilteringCharacteristics getFilteringCharacteristics();
	
	FilteringTimeOut getFilteringTimeOut();
	
	FilteringCriteria getFilteringCriteria();
	
	DateAndTime getStartTime();

    CAPINAPExtensions getExtensions();
    
    byte[] getSCFCorrelationInfo();
}