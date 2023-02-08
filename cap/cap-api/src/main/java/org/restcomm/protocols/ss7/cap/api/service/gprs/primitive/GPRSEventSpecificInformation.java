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

package org.restcomm.protocols.ss7.cap.api.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.EsiGprs.DetachSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.DisconnectSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PdpContextChangeOfPositionSpecificInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 GPRSEventSpecificInformation {PARAMETERS-BOUND : bound} ::= CHOICE { attachChangeOfPositionSpecificInformation [0] SEQUENCE {
 * locationInformationGPRS [0] LocationInformationGPRS OPTIONAL, ... }, pdp-ContextchangeOfPositionSpecificInformation [1]
 * SEQUENCE { accessPointName [0] AccessPointName {bound} OPTIONAL, chargingID [1] GPRSChargingID OPTIONAL,
 * locationInformationGPRS [2] LocationInformationGPRS OPTIONAL, endUserAddress [3] EndUserAddress {bound} OPTIONAL,
 * qualityOfService [4] QualityOfService OPTIONAL, timeAndTimeZone [5] TimeAndTimezone {bound} OPTIONAL, ..., gGSNAddress [6]
 * GSN-Address OPTIONAL }, detachSpecificInformation [2] SEQUENCE { initiatingEntity [0] InitiatingEntity OPTIONAL, ...,
 * routeingAreaUpdate [1] NULL OPTIONAL }, disconnectSpecificInformation [3] SEQUENCE { initiatingEntity [0] InitiatingEntity
 * OPTIONAL, ..., routeingAreaUpdate [1] NULL OPTIONAL }, pDPContextEstablishmentSpecificInformation [4] SEQUENCE {
 * accessPointName [0] AccessPointName {bound} OPTIONAL, endUserAddress [1] EndUserAddress {bound} OPTIONAL, qualityOfService
 * [2] QualityOfService OPTIONAL, locationInformationGPRS [3] LocationInformationGPRS OPTIONAL, timeAndTimeZone [4]
 * TimeAndTimezone {bound} OPTIONAL, pDPInitiationType [5] PDPInitiationType OPTIONAL, ..., secondaryPDP-context [6] NULL
 * OPTIONAL }, pDPContextEstablishmentAcknowledgementSpecificInformation [5] SEQUENCE { accessPointName [0] AccessPointName
 * {bound} OPTIONAL, chargingID [1] GPRSChargingID OPTIONAL, endUserAddress [2] EndUserAddress {bound} OPTIONAL,
 * qualityOfService [3] QualityOfService OPTIONAL, locationInformationGPRS [4] LocationInformationGPRS OPTIONAL, timeAndTimeZone
 * [5] TimeAndTimezone {bound} OPTIONAL, ..., gGSNAddress [6] GSN-Address OPTIONAL } }
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag=16,constructed = true,lengthIndefinite = false)
public interface GPRSEventSpecificInformation {

    LocationInformationGPRS getLocationInformationGPRS();

    PdpContextChangeOfPositionSpecificInformation getPdpContextChangeOfPositionSpecificInformation();

    DetachSpecificInformation getDetachSpecificInformation();

    DisconnectSpecificInformation getDisconnectSpecificInformation();

    PDPContextEstablishmentSpecificInformation getPDPContextEstablishmentSpecificInformation();

    PDPContextEstablishmentAcknowledgementSpecificInformation getPDPContextEstablishmentAcknowledgementSpecificInformation();

}