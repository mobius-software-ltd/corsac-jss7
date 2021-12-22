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

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

/**
 *
<code>
RequestReportBCSMEvent ::= OPERATION
ARGUMENT RequestReportBCSMEventArg
ERRORS {
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF -> SSF, Timer: Trrb
-- This operation is used to request the SSF to monitor for a call-related event (e.g., BCSM events
-- such as busy or no answer), then send a notification back to the SCF when the event is
-- detected.

RequestReportBCSMEventArg ::= SEQUENCE {
	bcsmEvents [0] SEQUENCE SIZE (1..numOfBCSMEvents) OF BCSMEvent,
	extensions [2] SEQUENCE SIZE (1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface RequestReportBCSMEventRequest extends CircuitSwitchedCallMessage {

    List<BCSMEvent> getBCSMEventList();

    CAPINAPExtensions getExtensions();

}
