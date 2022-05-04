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

import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;

/**
 *
<code>
<p>
TBusy ::= OPERATION
ARGUMENT TBusyArg
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
-- Direction: SSF –> SCF, Timer: Ttb
-- This operation is used to indicate all resources in group busy (DP 13 – TBusy).
-- For additional information on this operation, refer to 4.2.2.2.2-8/Q.1214.

TBusyArg ::= SEQUENCE {
	dpSpecificCommonParameters [0] DpSpecificCommonParameters,
	busyCause [1] Cause OPTIONAL,
	calledPartyBusinessGroupID [2] CalledPartyBusinessGroupID OPTIONAL,
	calledPartySubaddress [3] CalledPartySubaddress PTIONAL,
	originalCalledPartyID [4] OriginalCalledPartyID OPTIONAL,
	redirectingPartyID [5] RedirectingPartyID OPTIONAL,
	redirectionInformation [6] RedirectionInformation OPTIONAL,
	routeList [7] RouteList OPTIONAL,
	travellingClassMark [8] TravellingClassMark OPTIONAL,
	extensions [9] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
-- For the OPTIONAL parameters, refer to clause 3 for the trigger detection point processing rules
-- to specify when these parameters are included in the message.
</code>
 *
 * @author yulian.oifa
 *
 */
public interface TBusyRequest extends CircuitSwitchedCallMessage {
	DpSpecificCommonParameters getDpSpecificCommonParameters();
	
	CauseIsup getBusyCause();
	
	CalledPartyBusinessGroupID getCalledPartyBusinessGroupID();
	
	CalledPartySubaddress getCalledPartySubaddress();
	
	OriginalCalledNumberIsup getOriginalCalledPartyID();
	
	RedirectingPartyIDIsup getRedirectingPartyID();
	
    RedirectionInformationIsup getRedirectionInformation();
    
    RouteList getRouteList();
    
    LocationNumberIsup getTravellingClassMark();
    
	CAPINAPExtensions getExtensions();
}