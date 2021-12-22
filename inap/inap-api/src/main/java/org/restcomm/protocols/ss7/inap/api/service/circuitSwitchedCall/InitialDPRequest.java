/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGIndex;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGInterlock;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.inap.api.primitives.TerminalType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.IPAvailable;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;

/**
 *
<code>
InitialDP ::= OPERATION
ARGUMENT InitialDPArg
ERRORS {
	MissingCustomerRecord,
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SSF -> SCF, Timer: Tidp -- This operation is used after a TDP to indicate request for service.

InitialDPArg ::= SEQUENCE {
	serviceKey [0] ServiceKey,
	calledPartyNumber [2] CalledPartyNumber OPTIONAL,
	callingPartyNumber [3] CallingPartyNumber OPTIONAL,
	callingPartysCategory [5] CallingPartysCategory OPTIONAL,
	cGEncountered [7] CGEncountered OPTIONAL,
	iPSSPCapabilities [8] IPSSPCapabilities OPTIONAL,
	iPAvailable [9] IPAvailable OPTIONAL,
	locationNumber [10] LocationNumber OPTIONAL,
	originalCalledPartyID [12] OriginalCalledPartyID OPTIONAL,
	extensions [15] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	highLayerCompatibility [23] HighLayerCompatibility OPTIONAL,
	serviceInteractionIndicators [24] ServiceInteractionIndicatorsOPTIONAL,
	additionalCallingPartyNumber [25] AdditionalCallingPartyNumberOPTIONAL,
	forwardCallIndicators [26] ForwardCallIndicators OPTIONAL,
	bearerCapability [27] BearerCapability OPTIONAL,
	eventTypeBCSM [28] EventTypeBCSM OPTIONAL,
	redirectingPartyID [29] RedirectingPartyID OPTIONAL,
	redirectionInformation [30] RedirectionInformation OPTIONAL
-- ...
}

-- OPTIONAL for iPSSPCapabilities, iPAvailable, cGEncountered denotes network operator specific use.
-- OPTIONAL for callingPartyNumber, and callingPartysCategory refer to Clause 7 for the trigger detection
-- point processing rules to specify when these parameters are included in the message.
-- The following parameters shall be recognized by the SCF upon reception of InitialDP:
-- dialledDigits [1] CalledPartyNumber OPTIONAL,
-- callingPartyBusinessGroupID [4] CallingPartyBusinessGroupID OPTIONAL,
-- callingPartySubaddress [6] CallingPartySubaddress OPTIONAL,
-- miscCallInfo [11] MiscCallInfo OPTIONAL,
-- serviceProfileIdentifier [13] ServiceProfileIdentifier OPTIONAL,
-- terminalType [14] TerminalType OPTIONAL
-- These parameters shall be ignored by the SCF and not lead to any error procedures.
-- These parameters shall not be sent by a SSF following this ETS.
-- For details on the coding of these parameters refer to ITU-T Recommendation Q.1218 [12].
</code>
 *
 * @author yulian.oifa
 *
 */
public interface InitialDPRequest extends CircuitSwitchedCallMessage {

    int getServiceKey();

    CallingPartyNumberIsup getDialledDigits();
    
    CalledPartyNumberIsup getCalledPartyNumber();

    CallingPartyNumberIsup getCallingPartyNumber();

    CallingPartyBusinessGroupID getCallingPartyBusinessGroupID();
    
    CallingPartysCategoryIsup getCallingPartysCategory();

    byte[] getCallingPartySubaddress();
    
    CGEncountered getCGEncountered();

    IPSSPCapabilities getIPSSPCapabilities();

    IPAvailable getIPAvailable();
    
    LocationNumberIsup getLocationNumber();

    MiscCallInfo getMiscCallInfo();
    
    OriginalCalledNumberIsup getOriginalCalledPartyID();

    byte[] getServiceProfileIdentifier();
    
    TerminalType getTerminalType();
    
    CAPINAPExtensions getExtensions();

    HighLayerCompatibilityIsup getHighLayerCompatibility();

    ServiceInteractionIndicators getServiceInteractionIndicators();
    /**
     * Use Digits.getGenericNumber() for AdditionalCallingPartyNumber
     *
     * @return
     */
    DigitsIsup getAdditionalCallingPartyNumber();

    ForwardCallIndicators getForwardCallIndicators();
    
    BearerCapability getBearerCapability();

    EventTypeBCSM getEventTypeBCSM();

    RedirectingPartyIDIsup getRedirectingPartyID();

    RedirectionInformationIsup getRedirectionInformation();

    CauseIsup getCause();

    ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo();

    Carrier getCarrier();

    CUGIndex getCugIndex();

    CUGInterlock getCugInterlock();

    boolean getCugOutgoingAccess();

    IMSI getIMSI();

    SubscriberState getSubscriberState();

    LocationInformation getLocationInformation();

    ExtBasicServiceCode getExtBasicServiceCode();

    CallReferenceNumber getCallReferenceNumber();

    ISDNAddressString getMscAddress();

    CalledPartyBCDNumber getCalledPartyBCDNumber();

    TimeAndTimezone getTimeAndTimezone();

    boolean getCallForwardingSSPending();

    InitialDPArgExtension getInitialDPArgExtension();
}
