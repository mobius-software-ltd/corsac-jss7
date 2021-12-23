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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceStatus;

/**
 *
<code>
<p>
RequestFirstStatusMatchReport ::= OPERATION
ARGUMENT RequestFirstStatusMatchReportArg
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
-- Direction: SCF –> SSF, Timer: Trfs
-- This operation is used to request the SSF to report the first change busy/idle to the specified status of
-- a physical termination resource.

RequestFirstStatusMatchReportArg ::= SEQUENCE {
	resourceID [0] ResourceID OPTIONAL,
	resourceStatus [1] ResourceStatus OPTIONAL,
	correlationID [2] CorrelationID OPTIONAL,
	monitorDuration [3] Duration OPTIONAL,
	extensions [4] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	bearerCapability [5] BearerCapability OPTIONAL
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
public interface RequestFirstStatusMatchReportRequest extends CircuitSwitchedCallMessage {
	ResourceID getResourceID();
	
	ResourceStatus getResourceStatus();
	
	DigitsIsup getCorrelationID();
	
	Integer getDuration();
	
	CAPINAPExtensions getExtensions();	
	
	BearerCapability getBearerCapability();
}