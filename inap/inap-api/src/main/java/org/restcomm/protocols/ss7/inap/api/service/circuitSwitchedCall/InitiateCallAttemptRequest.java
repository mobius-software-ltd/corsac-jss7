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
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardGVNSIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGCallIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGInterLockCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericDigitsSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericNumbersSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;

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
	serviceInteractionIndicators [29] ServiceInteractionIndicators OPTIONAL,
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

--- from CS1+ Spec
InitiateCallAttemptArg ::= SEQUENCE {
	originalCalledPartyID [PRIVATE 01] Number OPTIONAL,
	legToBeCreated [PRIVATE 02] SendingSideID DEFAULT sendingSideID 1,
	callingPartysCategory [PRIVATE 03] CallingPartysCategory OPTIONAL,
	redirectingPartyID [PRIVATE 04] Number OPTIONAL,
	redirectionInformation [PRIVATE 05] RedirectionInformation OPTIONAL,
	bearerCapabilities [PRIVATE 06] BearerCapability OPTIONAL,
	cUGCallIndicator [PRIVATE 07] CUGCallIndicator OPTIONAL,
	cUGInterLockCode [PRIVATE 08] CUGInterLockCode OPTIONAL,
	forwardCallIndicators [PRIVATE 09] ForwardCallIndicators OPTIONAL,
	genericDigitsSet [PRIVATE 10] GenericDigitsSet OPTIONAL,
	genericNumberSet [PRIVATE 11] GenericNumberSet OPTIONAL,
	highLayerCompatibility [PRIVATE 12] HighLayerCompatibility OPTIONAL,
	forwardGVNSIndicator [PRIVATE 13] ForwardGVNSIndicator OPTIONAL,
	destinationRoutingAddress [00] DestinationRoutingAddress,
	alertingPattern [01] AlertingPattern OPTIONAL,
	extensions [04] SEQUENCE SIZE (1..7) OF ExtensionField1 OPTIONAL,
	serviceInteractionIndicators [29] ICAServiceInteractionIndicators OPTIONAL,
	callingPartyNumber [30] Number OPTIONAL,
	‐‐ ...
	routeList [PRIVATE 14] RouteList OPTIONAL
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface InitiateCallAttemptRequest extends CircuitSwitchedCallMessage {

	OriginalCalledNumberIsup getOriginalCalledPartyID();

	LegType getLegToBeCreated();
	
	CallingPartysCategoryIsup getCallingPartysCategory();

	RedirectingPartyIDIsup getRedirectingPartyID();

	RedirectionInformationIsup getRedirectionInformation();
	
	BearerCapability getBearerCapability();

	CUGCallIndicator getCUGCallIndicator();
    
    CUGInterLockCode getCUGInterLockCode();
    
    ForwardCallIndicators getForwardCallIndicators();
    
    GenericDigitsSet getGenericDigitsSet();
    
    GenericNumbersSet getGenericNumberSet();
    
    HighLayerCompatibilityIsup getHighLayerCompatibility();

    ForwardGVNSIsup getForwardGVNSIndicator();        	
    
    DestinationRoutingAddress getDestinationRoutingAddress();

    AlertingPattern getAlertingPattern();
    
    ISDNAccessRelatedInformation getISDNAccessRelatedInformation();
    
    LocationNumberIsup getTravellingClassMark();
    
    CAPINAPExtensions getExtensions();

    ServiceInteractionIndicators getServiceInteractionIndicators();
    
    CallingPartyNumberIsup getCallingPartyNumber();
    
    RouteList getRouteList();

}