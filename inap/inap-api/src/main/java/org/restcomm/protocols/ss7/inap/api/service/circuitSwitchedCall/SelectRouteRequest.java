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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;

/**
 *
<code>
<p>
SelectRoute ::= OPERATION
ARGUMENT SelectRouteArg
ERRORS {
	MissingParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF > SSF, Timer: Tsr
-- This operation is used to request the SSF to perform the originating basic call processing actions to
-- determine routing information and select a route for a call, based either on call information available
-- to the SSF, or on call information provided by the SCF (e.g. for alternate routing), to include the
-- called party address, type of call, carrier, route index, and one or more alternate route indices.
-- Based on the routing information, the SSF attempts to select a primary route for the call, and if the
-- route is busy, attempts to select an alternate route. The SSF may fail to select a route for the call
-- if all routes are busy.

SelectRouteArg ::= SEQUENCE {
	destinationRoutingAddress [0] DestinationRoutingAddress,
	alertingPattern [1] AlertingPattern OPTIONAL,
	correlationID [2] CorrelationID OPTIONAL,
	iSDNAccessRelatedInformation [3] ISDNAccessRelatedInformation OPTIONAL,
	originalCalledPartyID [4] OriginalCalledPartyID OPTIONAL,
	routeList [5] RouteList OPTIONAL,
	scfID [6] ScfID OPTIONAL,
	travellingClassMark [7] TravellingClassMark OPTIONAL,
	extensions [8] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	carrier [9] Carrier OPTIONAL
-- ...
}
-- OPTIONAL parameters are only provided if modifications desired to basic call processing values.
</code>
 *
 * @author yulian.oifa
 *
 */
public interface SelectRouteRequest extends CircuitSwitchedCallMessage {
	CalledPartyNumberIsup getDestinationNumberRoutingAddress();
	
	AlertingPattern getAlertingPattern();
	
	DigitsIsup getCorrelationID();
	
	ISDNAccessRelatedInformation getISDNAccessRelatedInformation();
	
	OriginalCalledNumberIsup getOriginalCalledPartyID();
	
	RouteList getRouteList();
	
	ScfID getScfID();
	
	LocationNumberIsup getTravellingClassMark();
    
	CAPINAPExtensions getExtensions();
	
	Carrier getCarrier();					        
}