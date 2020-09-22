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

package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * <p>
 * When a subscriber registers with an MSC, the MSC uses MAP LU to request the HLR of that subscriber for GSM subscription data.
 * If the VLR supports CAMEL, then the VLR will indicate this in MAP LU; the VLR will indicate each individual CAMEL phase that
 * it supports. The indication of CAMEL support in MAP LU tells the HLR that it is allowed to send CAMEL subscription data to
 * that VLR
 * </p>
 *
 * MAP V1-2-3
 *
 * updateLocation OPERATION ::= { --Timer m ARGUMENT UpdateLocationArg RESULT UpdateLocationRes ERRORS { systemFailure |
 * dataMissing | -- DataMissing must not be used in version 1 unexpectedDataValue | unknownSubscriber | roamingNotAllowed} CODE
 * local:2 }
 *
 * MAP V3: UpdateLocationArg ::= SEQUENCE { imsi IMSI, msc-Number [1] ISDN-AddressStringImpl, vlr-Number ISDN-AddressStringImpl, lmsi
 * [10] LMSI OPTIONAL, extensionContainer ExtensionContainer OPTIONAL, ... , vlr-Capability [6] VLR-Capability OPTIONAL,
 * informPreviousNetworkEntity [11] NULL OPTIONAL, cs-LCS-NotSupportedByUE [12] NULL OPTIONAL, v-gmlc-Address [2] GSN-Address
 * OPTIONAL, add-info [13] ADD-Info OPTIONAL, pagingArea [14] PagingArea OPTIONAL, skipSubscriberDataUpdate [15] NULL OPTIONAL,
 * -- The skipSubscriberDataUpdate parameter in the UpdateLocationArg and the ADD-Info -- structures carry the same semantic.
 * restorationIndicator [16] NULL OPTIONAL }
 *
 * MAP V2: UpdateLocationArg ::= SEQUENCE { imsi IMSI, locationInfo LocationInfo, vlr-Number ISDN-AddressStringImpl, lmsi [10] LMSI
 * OPTIONAL, ...}
 *
 * LocationInfo ::= CHOICE { roamingNumber [0] ISDN-AddressStringImpl, -- roamingNumber must not be used in version greater 1
 * msc-Number [1] ISDN-AddressStringImpl}
 *
 * @author sergey vetyutnev
 *
 */
public interface UpdateLocationRequest extends MobilityMessage {

    IMSIImpl getImsi();

    ISDNAddressStringImpl getMscNumber();

    ISDNAddressStringImpl getRoamingNumber();

    ISDNAddressStringImpl getVlrNumber();

    LMSIImpl getLmsi();

    MAPExtensionContainerImpl getExtensionContainer();

    VLRCapabilityImpl getVlrCapability();

    boolean getInformPreviousNetworkEntity();

    boolean getCsLCSNotSupportedByUE();

    GSNAddressImpl getVGmlcAddress();

    ADDInfoImpl getADDInfo();

    PagingAreaImpl getPagingArea();

    boolean getSkipSubscriberDataUpdate();

    boolean getRestorationIndicator();

    long getMapProtocolVersion();

}
