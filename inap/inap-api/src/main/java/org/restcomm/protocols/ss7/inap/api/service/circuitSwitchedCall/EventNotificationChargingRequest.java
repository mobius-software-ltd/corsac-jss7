/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;

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

    byte[] getEventTypeCharging();

    byte[] getEventSpecificInformationCharging();
    
    LegID getLegID();
    
    CAPINAPExtensions getExtensions();

    MonitorMode getMonitorMode();
}