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

import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;

/**
 *
<code>
<p>
TermAttemptAuthorized ::= OPERATION
ARGUMENT TermAttemptAuthorizedArg
ERRORS {
	MissingCustomerRecord,
	MissingParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SSF –> SCF, Timer: Ttaa
-- This operation is used for indication of incoming call received from originating half BCSM and authority
-- to route call to a specified terminating resource (or group) verified. (DP 12 – Termination_Authorized.)
-- For additional information on this operation, refer to 4.2.2.2.2-7/Q.1214.

TermAttemptAuthorizedArg ::= SEQUENCE {
	dpSpecificCommonParameters [0] DpSpecificCommonParameters,
	calledPartyBusinessGroupID [1] CalledPartyBusinessGroupID OPTIONAL,
	calledPartySubaddress [2] CalledPartySubaddress OPTIONAL,
	callingPartyBusinessGroupID [3] CallingPartyBusinessGroupID OPTIONAL,
	originalCalledPartyID [4] OriginalCalledPartyID OPTIONAL,
	redirectingPartyID [5] RedirectingPartyID OPTIONAL,
	redirectionInformation [6] RedirectionInformation OPTIONAL,
	routeList [7] RouteList OPTIONAL,
	travellingClassMark [8] TravellingClassMark OPTIONAL,
	extensions [9] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface TermAttemptAuthorizedRequest extends CircuitSwitchedCallMessage {
	DpSpecificCommonParameters getDpSpecificCommonParameters();
	
	CalledPartyBusinessGroupID getCalledPartyBusinessGroupID();
	
	CalledPartySubaddress getCalledPartySubaddress();
	
	CallingPartyBusinessGroupID getCallingPartyBusinessGroupID();
	
	OriginalCalledNumberIsup getOriginalCalledPartyID();
	
	RedirectingPartyIDIsup getRedirectingPartyID();
	
    RedirectionInformationIsup getRedirectionInformation();
    
    RouteList getRouteList();
    
    LocationNumberIsup getTravellingClassMark();
    
	CAPINAPExtensions getExtensions();
}