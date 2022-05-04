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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardGVNSIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGCallIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGInterLockCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericDigitsSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericNumbersSet;
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

--- From CS1+ Spec
ConnectArg ::= SEQUENCE {
	legToBeCreated [PRIVATE 01] SendingSideID DEFAULT sendingSideID 2,
	‐‐ the valid values for SendingSideID are (2..30)
	bearerCapabilities [PRIVATE 02] BearerCapability OPTIONAL,
	cUGCallIndicator [PRIVATE 03] CUGCallIndicator OPTIONAL,
	cUGInterLockCode [PRIVATE 04] CUGInterLockCode OPTIONAL,
	forwardCallIndicators [PRIVATE 05] ForwardCallIndicators OPTIONAL,
	genericDigitsSet [PRIVATE 06] GenericDigitsSet OPTIONAL,
	genericNumberSet [PRIVATE 07] GenericNumberSet OPTIONAL,
	highLayerCompatibility [PRIVATE 08] HighLayerCompatibility OPTIONAL,
	forwardGVNSIndicator [PRIVATE 09] ForwardGVNSIndicator OPTIONAL,
	destinationRoutingAddress [00] DestinationRoutingAddress OPTIONAL,
	alertingPattern [01] AlertingPattern OPTIONAL,
	correlationID [02] GenericDigits OPTIONAL,
	cutAndPaste [03] CutAndPaste OPTIONAL,
	originalCalledPartyID [06] Number OPTIONAL,
	routeList [07] RouteList OPTIONAL,
	sCFID [08] GenericNumber OPTIONAL,
	extensions [10] SEQUENCE SIZE (1..7) OF ExtensionField2 OPTIONAL,
	serviceInteractionIndicators [26] CONServiceInteractionIndicators OPTIONAL,
	callingPartyNumber [27] Number OPTIONAL,
	callingPartysCategory [28] CallingPartysCategory OPTIONAL,
	redirectingPartyID [29] Number OPTIONAL,
	redirectionInformation [30] RedirectionInformation OPTIONAL
‐‐ ...
}
</code>
*
 * @author yulian.oifa
 *
 */
public interface ConnectRequest extends CircuitSwitchedCallMessage {
	LegType getLegToBeCreated();
    
    BearerCapability getBearerCapabilities();
    
    CUGCallIndicator getCUGCallIndicator();
    
    CUGInterLockCode getCUGInterLockCode();
    
    ForwardCallIndicatorsIsup getForwardCallIndicators();
    
    GenericDigitsSet getGenericDigitsSet();
    
    GenericNumbersSet getGenericNumberSet();
    
    HighLayerCompatibilityIsup getHighLayerCompatibility();
    
    ForwardGVNSIsup getForwardGVNSIndicator();
    
    DestinationRoutingAddress getDestinationRoutingAddress();

    AlertingPattern getAlertingPattern();

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
