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

package org.restcomm.protocols.ss7.cap.api.service.gprs;

import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfServiceImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilitiesImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentityImpl;

/**
 *
 initialDPGPRS {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT InitialDPGPRSArg {bound} RETURN RESULT FALSE ERRORS
 * {missingCustomerRecord | missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence |
 * unexpectedDataValue | unexpectedParameter} CODE opcode-initialDPGPRS} -- Direction gprsSSF -> gsmSCF,Timer Tidpg -- This
 * operation is used by the gprsSSF when a trigger is detected at a DP in the GPRS state -- machines to request instructions
 * from the gsmSCF
 *
 * InitialDPGPRSArg {PARAMETERS-BOUND : bound}::= SEQUENCE { serviceKey [0] ServiceKey, gPRSEventType [1] GPRSEventType, mSISDN
 * [2] ISDN-AddressString, iMSI [3] IMSI, timeAndTimeZone [4] TimeAndTimezone {bound}, gPRSMSClass [5] GPRSMSClass OPTIONAL,
 * endUserAddress [6] EndUserAddress {bound} OPTIONAL, qualityOfService [7] QualityOfService OPTIONAL, accessPointName [8]
 * AccessPointName{bound} OPTIONAL, routeingAreaIdentity [9] RAIdentity OPTIONAL, chargingID [10] GPRSChargingID OPTIONAL,
 * sGSNCapabilities [11] SGSNCapabilities OPTIONAL, locationInformationGPRS [12] LocationInformationGPRS OPTIONAL,
 * pDPInitiationType [13] PDPInitiationType OPTIONAL, extensions [14] Extensions {bound} OPTIONAL, ..., gGSNAddress [15]
 * GSN-Address OPTIONAL, secondaryPDP-context [16] NULL OPTIONAL, iMEI [17] IMEI OPTIONAL } -- The RouteingAreaIdentity
 * parameter is not used. -- The receiving entity shall ignore RouteingAreaIdentity if received. -- The RouteingAreaIdentity is
 * conveyed in the LocationInformationGPRS parameter.
 *
 * ServiceKey ::= Integer4
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InitialDpGprsRequest extends GprsMessage {

    int getServiceKey();

    GPRSEventType getGPRSEventType();

    ISDNAddressStringImpl getMsisdn();

    IMSIImpl getImsi();

    TimeAndTimezoneImpl getTimeAndTimezone();

    GPRSMSClassImpl getGPRSMSClass();

    EndUserAddressImpl getEndUserAddress();

    QualityOfServiceImpl getQualityOfService();

    AccessPointNameImpl getAccessPointName();

    RAIdentityImpl getRouteingAreaIdentity();

    GPRSChargingIDImpl getChargingID();

    SGSNCapabilitiesImpl getSGSNCapabilities();

    LocationInformationGPRSImpl getLocationInformationGPRS();

    PDPInitiationType getPDPInitiationType();

    CAPExtensionsImpl getExtensions();

    GSNAddressImpl getGSNAddress();

    boolean getSecondaryPDPContext();

    IMEIImpl getImei();
}