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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;

/**
 *
<code>
<p>
CancelStatusReportRequest ::= OPERATION
ARGUMENT CancelStatusReportRequestArg
ERRORS {
	CancelFailed,
	MissingParameter,
	TaskRefused
}
-- Direction: SCF −> SSF, Timer: Tcsr
-- This operation cancels the following processes: RequestFirstStatusMatchReport and
-- RequestEveryStatusChangeReport.

CancelStatusReportRequestArg ::= SEQUENCE {
	resourceID [0] ResourceID OPTIONAL,
	extensions [1] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface CancelStatusReportRequest extends CircuitSwitchedCallMessage {
	ResourceID getResourceID();
	
	CAPINAPExtensions getExtensions();						       
}