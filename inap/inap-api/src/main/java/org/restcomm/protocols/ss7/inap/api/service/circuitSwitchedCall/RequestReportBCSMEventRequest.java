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

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
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

--- From Q.1218 CS1
RequestReportBCSMEventArg ::= SEQUENCE {
	bcsmEvents [0] SEQUENCE SIZE(1..numOfBCSMEvents) OF BCSMEvent,
	bcsmEventCorrelationID [1] CorrelationID OPTIONAL,
	extensions [2] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
-- Indicates the BCSM related events for notification.
-- For correlationID OPTIONAL denotes network operator optional
</code>
 *
 * @author yulian.oifa
 *
 */
public interface RequestReportBCSMEventRequest extends CircuitSwitchedCallMessage {

    List<BCSMEvent> getBCSMEventList();

    DigitsIsup getBCSMEventCorrelationID();
    	
    CAPINAPExtensions getExtensions();

}
