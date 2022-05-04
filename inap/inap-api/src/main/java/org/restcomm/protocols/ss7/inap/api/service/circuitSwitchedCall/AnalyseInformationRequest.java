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
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;

/**
 *
<code>
<p>
AnalyseInformation ::= OPERATION
ARGUMENT AnalyseInformationArg
ERRORS {
	MissingParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF −> SSF, Timer: Tai
-- This operation is used to request the SSF to perform the originating basic call processing actions
-- to analyse destination information that is either collected from a calling party or provided by the SCF
-- (e.g. for number translation). This includes actions to validate the information according to an office
-- or customized dialling plan, and if valid, to determine call termination information, to include the called
-- party address, the type of call (e.g. intra-network or inter-network), and carrier (if inter-network).
-- If the called party is not served by the SSF, the SSF also determines a route index based on the called
-- party address and class of service, where the route index points to a list of outgoing trunk groups.

AnalyseInformationArg ::= SEQUENCE {
	destinationRoutingAddress [0] DestinationRoutingAddress,
	alertingPattern [1] AlertingPattern OPTIONAL,
	iSDNAccessRelatedInformation [2] ISDNAccessRelatedInformation OPTIONAL,
	originalCalledPartyID [3] OriginalCalledPartyID OPTIONAL,
	extensions [4] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	callingPartyNumber [5] CallingPartyNumber OPTIONAL,
	callingPartysCategory [6] CallingPartysCategory OPTIONAL,
	calledPartyNumber [7] CalledPartyNumber OPTIONAL,
	chargeNumber [8] ChargeNumber OPTIONAL,
	travellingClassMark [9] TravellingClassMark OPTIONAL,
	carrier [10] Carrier OPTIONAL
-- ...
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface AnalyseInformationRequest extends CircuitSwitchedCallMessage {
	DestinationRoutingAddress getDestinationRoutingAddress();
	
	AlertingPattern getAlertingPattern();
	
	ISDNAccessRelatedInformation getISDNAccessRelatedInformation();
	
	OriginalCalledNumberIsup getOriginalCalledPartyID();
	
	CAPINAPExtensions getExtensions();
	
	CallingPartyNumberIsup getCallingPartyNumber();
	
	CallingPartysCategoryIsup getCallingPartysCategory();
	
	CalledPartyNumberIsup getCalledPartyNumber();

	LocationNumberIsup getChargeNumber();

	LocationNumberIsup getTravellingClassMark();
    
	Carrier getCarrier();					        
}