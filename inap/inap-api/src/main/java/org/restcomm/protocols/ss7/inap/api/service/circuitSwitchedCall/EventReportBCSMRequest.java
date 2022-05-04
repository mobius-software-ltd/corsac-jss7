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

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;

/**
<code>
EventReportBCSM ::= OPERATION
ARGUMENT
EventReportBCSMArg
-- Direction: SSF -> SCF, Timer: Terb
-- This operation is used to notify the SCF of a call-related event (e.g., BCSM events such as
-- busy or no answer) previously requested by the SCF in a RequestReportBCSMEvent operation.

EventReportBCSMArg ::= SEQUENCE {
	eventTypeBCSM [0] EventTypeBCSM,
	eventSpecificInformationBCSM [2] EventSpecificInformationBCSM OPTIONAL,
	legID [3] LegID OPTIONAL,
	miscCallInfo [4] MiscCallInfo DEFAULT {messageType request},
	extensions [5] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}

--- From Q.1218 CS1
EventReportBCSMArg ::= SEQUENCE {
	eventTypeBCSM [0] EventTypeBCSM,
	bcsmEventCorrelationID [1] CorrelationID OPTIONAL,
	eventSpecificInformationBCSM [2] EventSpecificInformationBCSM OPTIONAL,
	legID [3] LegID OPTIONAL,
	miscCallInfo [4] MiscCallInfo DEFAULT
	{messageType request},
	extensions [5] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
</code>
 * 
 * @author yulian.oifa
 *
 */
public interface EventReportBCSMRequest extends CircuitSwitchedCallMessage {

    EventTypeBCSM getEventTypeBCSM();

    DigitsIsup getBCSMEventCorrelationID();
    
    EventSpecificInformationBCSM getEventSpecificInformationBCSM();

    LegID getLegID();

    MiscCallInfo getMiscCallInfo();

    CAPINAPExtensions getExtensions();
}