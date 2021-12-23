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
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ForwardingCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;

/**
<code>
Connect ::= OPERATION
ARGUMENT ConnectArg
ERRORS {
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}

-- Direction: SCF -> SSF, Timer: Tcon -- This operation is used to request the SSF to perform the call processing actions to route or
-- forward a call to a specified destination. To do so, the SSF may or may not use destination
-- information from the calling party (e.g., dialled digits) and existing call setup information (e.g.,
-- route index to a list of trunk groups), depending on the information provided by the SCF.

ConnectArg ::= SEQUENCE {
	destinationRoutingAddress [0] DestinationRoutingAddress,
	alertingPattern [1] AlertingPattern OPTIONAL,
	correlationID [2] CorrelationID OPTIONAL,
	cutAndPaste [3] CutAndPaste OPTIONAL,
	originalCalledPartyID [6] OriginalCalledPartyID OPTIONAL,
	routeList [7] RouteList OPTIONAL,
	scfID [8] ScfID OPTIONAL,
	extensions [10] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	serviceInteractionIndicators [26] ServiceInteractionIndicators OPTIONAL,
	callingPartyNumber [27] CallingPartyNumber OPTIONAL,
	callingPartysCategory [28] CallingPartysCategory OPTIONAL,
	redirectingPartyID [29] RedirectingPartyID OPTIONAL,
	redirectionInformation [30] RedirectionInformation OPTIONAL
-- ...
}
-- For alerting pattern, OPTIONAL denotes that this parameter only applies if SSF is the
-- terminating local exchange for the subscriber.

CutAndPaste ::= INTEGER (0..22)

--- From Q.1218 CS1
ConnectArg ::= SEQUENCE {
	destinationRoutingAddress [0] DestinationRoutingAddress,
	alertingPattern [1] AlertingPattern OPTIONAL,
	correlationID [2] CorrelationID OPTIONAL,
	cutAndPaste [3] CutAndPaste OPTIONAL,
	forwardingCondition [4] ForwardingCondition OPTIONAL,
	iSDNAccessRelatedInformation [5] ISDNAccessRelatedInformation OPTIONAL,
	originalCalledPartyID [6] OriginalCalledPartyID OPTIONAL,
	routeList [7] RouteList OPTIONAL,
	scfID [8] ScfID OPTIONAL,
	travellingClassMark [9] TravellingClassMark OPTIONAL,
	extensions [10] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	carrier [11] Carrier OPTIONAL,
	serviceInteractionIndicators [26] ServiceInteractionIndicators OPTIONAL,
	callingPartyNumber [27] CallingPartyNumber OPTIONAL,
	callingPartysCategory [28] CallingPartysCategory OPTIONAL,
	redirectingPartyID [29] RedirectingPartyID OPTIONAL,
	redirectionInformation [30] RedirectionInformation OPTIONAL
-- ...
}
-- For alerting pattern, OPTIONAL denotes that this parameter only applies if SSF is the terminating
-- local exchange for the subscriber.
</code>
*
 * @author yulian.oifa
 *
 */
public interface ConnectRequest extends CircuitSwitchedCallMessage {

    DestinationRoutingAddress getDestinationRoutingAddress();

    AlertingPatternWrapper getAlertingPattern();

    DigitsIsup getCorrelationID();
    
    Integer getCutAndPaste();
    
    ForwardingCondition getForwardingCondition();
    
    ISDNAccessRelatedInformation getISDNAccessRelatedInformation();
    
    OriginalCalledNumberIsup getOriginalCalledPartyID();

    RouteList getRouteList();
    
    ScfID getScfID();
    
    LocationNumberIsup getTravellingClassMark();
    
	CAPINAPExtensions getExtensions();

    Carrier getCarrier();

    ServiceInteractionIndicators getServiceInteractionIndicators();
    
    CallingPartyNumberIsup getCallingPartyNumber();
    
    CallingPartysCategoryIsup getCallingPartysCategory();

    RedirectingPartyIDIsup getRedirectingPartyID();

    RedirectionInformationIsup getRedirectionInformation();
}
