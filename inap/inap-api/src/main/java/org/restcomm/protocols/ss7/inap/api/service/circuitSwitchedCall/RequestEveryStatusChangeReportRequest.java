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

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;

/**
 *
<code>
<p>
RequestEveryStatusChangeReport ::= OPERATION
ARGUMENT RequestEveryStatusChangeReportArg
RESULT
ERRORS {
	MissingParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedParameter,
	UnknownResource
}
-- Direction: SCF −> SSF, Timer: Tres
-- This operation is used to request the SSF to report every change of busy/idle status of a physical
-- termination resource.

RequestEveryStatusChangeReportArg ::= SEQUENCE {
	resourceID [0] ResourceID,
	correlationID [1] CorrelationID OPTIONAL,
	monitorDuration [2] Duration OPTIONAL,
	extensions [3] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
-- For correlationID OPTIONAL denotes network operator optional.
-- monitorDuration is required if outside the context of a call. It is not expected if we are in the context
-- of a call, because in that case the end of the call implicitly means the end of the monitoring.
</code>
 *
 * @author yulian.oifa
 *
 */
public interface RequestEveryStatusChangeReportRequest extends CircuitSwitchedCallMessage {
	ResourceID getResourceID();
	
	DigitsIsup getCorrelationID();
	
	Integer getDuration();
	
	CAPINAPExtensions getExtensions();						       
}