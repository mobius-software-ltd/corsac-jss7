/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;

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
	extensions [5] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField
OPTIONAL
-- ...
}
</code>
 * 
 * @author yulian.oifa
 *
 */
public interface EventReportBCSMRequest extends CircuitSwitchedCallMessage {

    EventTypeBCSM getEventTypeBCSM();

    EventSpecificInformationBCSM getEventSpecificInformationBCSM();

    LegType getLegID();

    MiscCallInfo getMiscCallInfo();

    CAPINAPExtensions getExtensions();
}