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
ODisconnect ::= OPERATION
ARGUMENT ODisconnectArg
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
-- Direction: SSF −> SCF, Timer: Tod
-- This operation is used for a disconnect indication (e.g. onhook, Q.931 Disconnect message, SS7 Release
-- message) is received from the originating party, or received from the terminating party via the terminating
-- half BCSM. (DP 9 – O_Disconnect). For additional information on this operation, refer to
-- 4.2.2.2.1-5/Q.1214.

ODisconnectArg ::= SEQUENCE {
	dpSpecificCommonParameters [0] DpSpecificCommonParameters,
	callingPartyBusinessGroupID [1] CallingPartyBusinessGroupID OPTIONAL,
	callingPartySubaddress [2] CallingPartySubaddress OPTIONAL,
	callingFacilityGroup [3] FacilityGroup OPTIONAL,
	callingFacilityGroupMember [4] FacilityGroupMember OPTIONAL,
	releaseCause [5] Cause OPTIONAL,
	routeList [6] RouteList OPTIONAL,
	extensions [7] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	carrier [8] Carrier OPTIONAL,
	connectTime [9] Integer4 OPTIONAL
-- ...
}
-- For the OPTIONAL parameters, refer to clause 3 for the trigger detection point processing rules
-- to specify when these parameters are included in the message.
</code>
 *
 * @author yulian.oifa
 *
 */
public interface ODisconnectRequest extends CircuitSwitchedCallMessage {
	DpSpecificCommonParameters getDpSpecificCommonParameters();
	
	CallingPartyBusinessGroupID getCallingPartyBusinessGroupID();
	
	CallingPartySubaddress getCallingPartySubaddress();
	
	FacilityGroup getCallingFacilityGroup();
	
	Integer getCallingFacilityGroupMember();
	
	CauseIsup getReleaseCause();
	
	RouteList getRouteList();
    
	CAPINAPExtensions getExtensions();
	
	Carrier getCarrier();
	
	Integer getConnectTime();
}