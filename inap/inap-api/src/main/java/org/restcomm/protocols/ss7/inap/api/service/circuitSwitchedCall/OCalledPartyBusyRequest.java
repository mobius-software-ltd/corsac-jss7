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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;

/**
 *
<code>
<p>
OCalledPartyBusy ::= OPERATION
ARGUMENT OCalledPartyBusyArg
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

-- Direction: SSF −> SCF, Timer: Tob
-- This operation is used for Indication from the terminating half BCSM that the terminating party is busy
-- (DP 5 – O_Called_Party_Busy). For additional information on this operation, refer to 4.2.2.2.1-4/Q.1214.

OCalledPartyBusyArg ::= SEQUENCE {
	dpSpecificCommonParameters [0] DpSpecificCommonParameters,
	busyCause [1] Cause OPTIONAL,
	callingPartyBusinessGroupID [2] CallingPartyBusinessGroupID OPTIONAL,
	callingPartySubaddress [3] CallingPartySubaddress OPTIONAL,
	callingFacilityGroup [4] FacilityGroup OPTIONAL,
	callingFacilityGroupMember [5] FacilityGroupMember OPTIONAL,
	originalCalledPartyID [6] OriginalCalledPartyID OPTIONAL,
	prefix [7] Digits OPTIONAL,
	redirectingPartyID [8] RedirectingPartyID OPTIONAL,
	redirectionInformation [9] RedirectionInformation OPTIONAL,
	routeList [10] RouteList OPTIONAL,
	travellingClassMark [11] TravellingClassMark OPTIONAL,
	extensions [12] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	carrier [13] Carrier OPTIONAL
-- ...
}
-- For the OPTIONAL parameters, refer to clause 3 for the trigger detection point processing rules
-- to specify when these parameters are included in the message.
</code>
 *
 * @author yulian.oifa
 *
 */
public interface OCalledPartyBusyRequest extends CircuitSwitchedCallMessage {
	DpSpecificCommonParameters getDpSpecificCommonParameters();
	
	CauseIsup getBusyCause();
	
	CallingPartyBusinessGroupID getCallingPartyBusinessGroupID();
	
	CallingPartySubaddress getCallingPartySubaddress();
	
	FacilityGroup getCallingFacilityGroup();
	
	Integer getCallingFacilityGroupMember();
	
	OriginalCalledNumberIsup getOriginalCalledPartyID();
	
	DigitsIsup getPrefix();
	
    RedirectingPartyIDIsup getRedirectingPartyID();
	
    RedirectionInformationIsup getRedirectionInformation();
    
    RouteList getRouteList();
    
    LocationNumberIsup getTravellingClassMark();
    
	CAPINAPExtensions getExtensions();
	
	Carrier getCarrier();
}