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
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;

import io.netty.buffer.ByteBuf;

/**
 *
 *
EventNotificationCharging ::= OPERATION
ARGUMENT
EventNotificationChargingArg
-- Direction: SSF -> SCF, Timer: Tenc -- This operation is used by the SSF to report to the SCF the occurrence of a specific charging event
-- type as previously requested by the SCF in a RequestNotificationChargingEvent operation.
-- The operation supports the capabilities to cope with the interactions concerning charging (refer
-- to Annex B, Clause B.5).


EventNotificationChargingArg ::= SEQUENCE {
	eventTypeCharging [0] EventTypeCharging,
	eventSpecificInformationCharging [1] EventSpecificInformationCharging OPTIONAL,
	legID [2] LegID OPTIONAL,
	extensions [3] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	monitorMode [30] MonitorMode DEFAULT notifyAndContinue
-- ...
}

EventTypeCharging ::= OCTET STRING (SIZE (minEventTypeChargingLength ..
 maxEventTypeChargingLength))
 
EventSpecificInformationCharging ::= OCTET STRING (SIZE(minEventSpecificInformationChargingLength
 ..maxEventSpecificInformationChargingLength))
-- defined by network operator. Indicates the charging related information specific to the event.
 * @author yulian.oifa
 *
 */
public interface EventNotificationChargingRequest extends CircuitSwitchedCallMessage {

    ByteBuf getEventTypeCharging();

    ByteBuf getEventSpecificInformationCharging();
    
    LegID getLegID();
    
    CAPINAPExtensions getExtensions();

    MonitorMode getMonitorMode();
}