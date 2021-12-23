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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
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

/**
 *
<code>
CollectedInformation ::= OPERATION
ARGUMENT CollectedInformationArg
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
-- Direction: SSF −> SCF, Timer: Tcdi
-- This operation is used to indicate availability of complete initial information package/dialling string from
-- originating party. (This event may have already occurred in the case of en bloc signalling, in which case
-- the waiting duration in this PIC is zero.) (DP 2 – Collected_Info). For additional information on this
-- operation and its use with open numbering plans, refer to 4.2.2.2.1-2/Q.1214.

CollectedInformationArg ::= SEQUENCE {
	dpSpecificCommonParameters [0] DpSpecificCommonParameters,
	dialledDigits [1] CalledPartyNumber OPTIONAL,
	callingPartyBusinessGroupID [2] CallingPartyBusinessGroupID OPTIONAL,
	callingPartySubaddress [3] CallingPartySubaddress OPTIONAL,
	callingFacilityGroup [4] FacilityGroup OPTIONAL,
	callingFacilityGroupMember [5] FacilityGroupMember OPTIONAL,
	originalCalledPartyID [6] OriginalCalledPartyID OPTIONAL,
	prefix [7] Digits OPTIONAL,
	redirectingPartyID [8] RedirectingPartyID OPTIONAL,
	redirectionInformation [9] RedirectionInformation OPTIONAL,
	travellingClassMark [10] TravellingClassMark OPTIONAL,
	extensions [11] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	featureCode [12] FeatureCode OPTIONAL,
	accessCode [13] AccessCode OPTIONAL,
	carrier [14] Carrier OPTIONAL
-- ...
}
-- For the OPTIONAL parameters, refer to clause 3 for the trigger detection point processing rules
-- to specify when these parameters are included in the message
</code>
 *
 * @author yulian.oifa
 *
 */
public interface CollectedInformationRequest extends CircuitSwitchedCallMessage {
DpSpecificCommonParameters getDpSpecificCommonParameters();
	
	CalledPartyNumberIsup getDialedDigits();
	
	CallingPartyBusinessGroupID getCallingPartyBusinessGroupID();
	
	CallingPartySubaddress getCallingPartySubaddress();
	
	FacilityGroup getCallingFacilityGroup();
	
	Integer getCallingFacilityGroupMember();
	
	OriginalCalledNumberIsup getOriginalCalledPartyID();
	
	DigitsIsup getPrefix();
	
    RedirectingPartyIDIsup getRedirectingPartyID();
	
    RedirectionInformationIsup getRedirectionInformation();
    
    LocationNumberIsup getTravellingClassMark();
    
	CAPINAPExtensions getExtensions();
	
	LocationNumberIsup getFeatureCode();
	
	LocationNumberIsup getAccessCode();
	
	Carrier getCarrier();
}
