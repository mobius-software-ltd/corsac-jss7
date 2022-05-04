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

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;

/**
 *
<code>
<p>
SelectFacility ::= OPERATION
ARGUMENT SelectFacilityArg
ERRORS {
	MissingParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF –> SSF, Timer: Tsf
-- This operation is used to request the SSF to perform the terminating basic call processing
-- actions to select the terminating line if it is idle, or selects an idle line from a multi-line hunt
-- group, or selects an idle trunk from a trunk group, as appropriate. If no idle line or trunk is
-- available, the SSF determines that the terminating facility is busy

SelectFacilityArg ::= SEQUENCE {
	alertingPattern [0] AlertingPattern OPTIONAL,
	destinationNumberRoutingAddress [1] CalledPartyNumber OPTIONAL,
	iSDNAccessRelatedInformation [2] ISDNAccessRelatedInformation OPTIONAL,
	calledFacilityGroup [3] FacilityGroup OPTIONAL,
	calledFacilityGroupMember [4] FacilityGroupMember OPTIONAL,
	originalCalledPartyID [5] OriginalCalledPartyID OPTIONAL,
	extensions [6] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
-- OPTIONAL parameters are only provided if modifications desired to basic call processing values.
</code>
 *
 * @author yulian.oifa
 *
 */
public interface SelectFacilityRequest extends CircuitSwitchedCallMessage {
	AlertingPattern getAlertingPattern();
	
	CalledPartyNumberIsup getDestinationNumberRoutingAddress();
	
	ISDNAccessRelatedInformation getISDNAccessRelatedInformation();
	
	FacilityGroup getCalledFacilityGroup();
	
	Integer getCalledFacilityGroupMember();
	
	OriginalCalledNumberIsup getOriginalCalledPartyID();
	
	CAPINAPExtensions getExtensions();						       
}