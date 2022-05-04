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
ONoAnswer ::= OPERATION
ARGUMENT ONoAnswerArg
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
-- Direction: SSF −> SCF, Timer: Tona
-- This operation is used for indication from the terminating half BCSM that the terminating party does not
-- answer within a specified time period (DP 6 – O_No_Answer). For additional information on this
-- operation, refer to 4.2.2.2.1-4/Q.1214.

ONoAnswerArg ::= SEQUENCE {
	dpSpecificCommonParameters [0] DpSpecificCommonParameters,
	callingPartyBusinessGroupID [1] CallingPartyBusinessGroupID OPTIONAL,
	callingPartySubaddress [2] CallingPartySubaddress OPTIONAL,
	callingFacilityGroup [3] FacilityGroup OPTIONAL,
	callingFacilityGroupMember [4] FacilityGroupMember OPTIONAL,
	originalCalledPartyID [5] OriginalCalledPartyID OPTIONAL,
	prefix [6] Digits OPTIONAL,
	redirectingPartyID [7] RedirectingPartyID OPTIONAL,
	redirectionInformation [8] RedirectionInformation OPTIONAL,
	routeList [9] RouteList OPTIONAL,
	travellingClassMark [10] TravellingClassMark OPTIONAL,
	extensions [11] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	carrier [12] Carrier OPTIONAL
-- ...
}
-- For the OPTIONAL parameters, refer to clause 3 for the trigger detection point processing rules
-- to specify when these parameters are included in the message.
</code>
 *
 * @author yulian.oifa
 *
 */
public interface ONoAnswerRequest extends CircuitSwitchedCallMessage {
	DpSpecificCommonParameters getDpSpecificCommonParameters();
	
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