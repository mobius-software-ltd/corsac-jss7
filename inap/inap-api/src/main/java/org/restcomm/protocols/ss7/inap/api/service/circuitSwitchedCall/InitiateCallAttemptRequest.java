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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;

/**
 *
<code>
InitiateCallAttempt ::= OPERATION
ARGUMENT InitiateCallAttemptArg
ERRORS {
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF -> SSF, Timer: Tica
-- This operation is used to request the SSF to create a new call to one call party using
-- address information provided by the SCF.

InitiateCallAttemptArg ::= SEQUENCE {
	destinationRoutingAddress [0] DestinationRoutingAddress,
	alertingPattern [1] AlertingPattern OPTIONAL,
	extensions [4] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	serviceInteractionIndicators [29] ServiceInteractionIndicatorsOPTIONAL,
	callingPartyNumber [30] CallingPartyNumber OPTIONAL
-- ...
}

--- From Q.1218 CS1
InitiateCallAttemptArg ::= SEQUENCE {
	destinationRoutingAddress [0] DestinationRoutingAddress,
	alertingPattern [1] AlertingPattern OPTIONAL,
	iSDNAccessRelatedInformation [2] ISDNAccessRelatedInformation OPTIONAL,
	travellingClassMark [3] TravellingClassMark OPTIONAL,
	extensions [4] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
	serviceInteractionIndicators [29] ServiceInteractionIndicators OPTIONAL,
	callingPartyNumber [30] CallingPartyNumber OPTIONAL
-- ...
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface InitiateCallAttemptRequest extends CircuitSwitchedCallMessage {

    DestinationRoutingAddress getDestinationRoutingAddress();

    AlertingPatternWrapper getAlertingPattern();
    
    ISDNAccessRelatedInformation getISDNAccessRelatedInformation();
    
    LocationNumberIsup getTravellingClassMark();
    
    CAPINAPExtensions getExtensions();

    ServiceInteractionIndicators getServiceInteractionIndicators();
    
    CallingPartyNumberIsup getCallingPartyNumber();

}