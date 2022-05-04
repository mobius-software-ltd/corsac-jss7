/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.EsiGprs;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSChargingID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 pdp-ContextchangeOfPositionSpecificInformation [1] SEQUENCE { accessPointName [0] AccessPointName {bound} OPTIONAL,
 * chargingID [1] GPRSChargingID OPTIONAL, locationInformationGPRS [2] LocationInformationGPRS OPTIONAL, endUserAddress [3]
 * EndUserAddress {bound} OPTIONAL, qualityOfService [4] QualityOfService OPTIONAL, timeAndTimeZone [5] TimeAndTimezone {bound}
 * OPTIONAL, ..., gGSNAddress [6] GSN-Address OPTIONAL },
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true, lengthIndefinite = false)
public interface PdpContextChangeOfPositionSpecificInformation {

    AccessPointName getAccessPointName();

    GPRSChargingID getChargingID();

    LocationInformationGPRS getLocationInformationGPRS();

    EndUserAddress getEndUserAddress();

    QualityOfService getQualityOfService();

    TimeAndTimezone getTimeAndTimezone();

    GSNAddress getGSNAddress();

}