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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CallingPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.DigitsImpl;
import org.restcomm.protocols.ss7.cap.api.isup.LocationNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.OriginalCalledNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.RedirectingPartyIDCapImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.restcomm.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.restcomm.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGIndexImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;

/**
 *
<code>
initialDP {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT InitialDPArg {bound}
   RETURN RESULT FALSE
   ERRORS {
     missingCustomerRecord | missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence |
     unexpectedDataValue | unexpectedParameter} CODE opcode-initialDP
   }
   -- Direction: gsmSSF -> gsmSCF, Timer: Tidp
   -- This operation is used after a TDP to indicate request for service.

InitialDPArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  serviceKey                     [0] ServiceKey, (= Integer4)
  calledPartyNumber              [2] CalledPartyNumber {bound} OPTIONAL,
  callingPartyNumber             [3] CallingPartyNumber {bound} OPTIONAL,
  callingPartysCategory          [5] CallingPartysCategory OPTIONAL,
  cGEncountered                  [7] CGEncountered OPTIONAL,
  iPSSPCapabilities              [8] IPSSPCapabilities {bound} OPTIONAL, (OCTET STRING (1..4))
  locationNumber                 [10] LocationNumber {bound} OPTIONAL,
  originalCalledPartyID          [12] OriginalCalledPartyID {bound} OPTIONAL,
  extensions                     [15] Extensions {bound} OPTIONAL,
  highLayerCompatibility         [23] HighLayerCompatibility OPTIONAL,
  additionalCallingPartyNumber   [25] AdditionalCallingPartyNumber {bound} OPTIONAL,
  bearerCapability               [27] BearerCapability {bound} OPTIONAL,
  eventTypeBCSM                  [28] EventTypeBCSM OPTIONAL,
  redirectingPartyID             [29] RedirectingPartyID {bound} OPTIONAL,
  redirectionInformation         [30] RedirectionInformation OPTIONAL,
  cause                          [17] Cause {bound} OPTIONAL,
  serviceInteractionIndicatorsTwo [32] ServiceInteractionIndicatorsTwo OPTIONAL,
  carrier                        [37] Carrier {bound} OPTIONAL,
  cug-Index                      [45] CUG-Index OPTIONAL,
  cug-Interlock                  [46] CUG-Interlock OPTIONAL,
  cug-OutgoingAccess             [47] NULL OPTIONAL,
  iMSI                           [50] IMSI OPTIONAL,
  subscriberState                [51] SubscriberState OPTIONAL,
  locationInformation            [52] LocationInformation OPTIONAL,
  ext-basicServiceCode           [53] Ext-BasicServiceCode OPTIONAL,
  callReferenceNumber            [54] CallReferenceNumber OPTIONAL,
  mscAddress                     [55] ISDN-AddressString OPTIONAL,
  calledPartyBCDNumber           [56] CalledPartyBCDNumber {bound} OPTIONAL,
  timeAndTimezone                [57] TimeAndTimezone {bound} OPTIONAL,
  callForwardingSS-Pending       [58] NULL OPTIONAL,
  initialDPArgExtension          [59] InitialDPArgExtension {bound} OPTIONAL,
  ... }
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InitialDPRequest extends CircuitSwitchedCallMessage {

    int getServiceKey();

    CalledPartyNumberCapImpl getCalledPartyNumber();

    CallingPartyNumberCapImpl getCallingPartyNumber();

    CallingPartysCategoryInap getCallingPartysCategory();

    CGEncountered getCGEncountered();

    IPSSPCapabilitiesImpl getIPSSPCapabilities();

    LocationNumberCapImpl getLocationNumber();

    OriginalCalledNumberCapImpl getOriginalCalledPartyID();

    CAPExtensionsImpl getExtensions();

    HighLayerCompatibilityInap getHighLayerCompatibility();

    /**
     * Use Digits.getGenericNumber() for AdditionalCallingPartyNumber
     *
     * @return
     */
    DigitsImpl getAdditionalCallingPartyNumber();

    BearerCapabilityImpl getBearerCapability();

    EventTypeBCSM getEventTypeBCSM();

    RedirectingPartyIDCapImpl getRedirectingPartyID();

    RedirectionInformationInap getRedirectionInformation();

    CauseCapImpl getCause();

    ServiceInteractionIndicatorsTwoImpl getServiceInteractionIndicatorsTwo();

    CarrierImpl getCarrier();

    CUGIndexImpl getCugIndex();

    CUGInterlockImpl getCugInterlock();

    boolean getCugOutgoingAccess();

    IMSIImpl getIMSI();

    SubscriberStateImpl getSubscriberState();

    LocationInformationImpl getLocationInformation();

    ExtBasicServiceCodeImpl getExtBasicServiceCode();

    CallReferenceNumberImpl getCallReferenceNumber();

    ISDNAddressStringImpl getMscAddress();

    CalledPartyBCDNumberImpl getCalledPartyBCDNumber();

    TimeAndTimezoneImpl getTimeAndTimezone();

    boolean getCallForwardingSSPending();

    InitialDPArgExtensionImpl getInitialDPArgExtension();
}
