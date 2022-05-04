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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
<code>
SubscriberInfo ::= SEQUENCE {
  locationInformation            [0] LocationInformation OPTIONAL,
  subscriberState                [1] SubscriberState OPTIONAL,
  extensionContainer             [2] ExtensionContainer OPTIONAL,
  ... ,
  locationInformationGPRS        [3] LocationInformationGPRS OPTIONAL,
  ps-SubscriberState             [4] PS-SubscriberState OPTIONAL,
  imei                           [5] IMEI OPTIONAL,
  ms-Classmark2                  [6] MS-Classmark2 OPTIONAL,
  gprs-MS-Class                  [7] GPRSMSClass OPTIONAL,
  mnpInfoRes                     [8] MNPInfoRes OPTIONAL
}
-- If the HLR receives locationInformation, subscriberState or ms-Classmark2 from an SGSN
-- it shall discard them.
-- If the HLR receives locationInformationGPRS, ps-SubscriberState or gprs-MS-Class from
-- a VLR it shall discard them.
-- If the HLR receives parameters which it has not requested, it shall discard them.
</code>
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface SubscriberInfo {

    LocationInformation getLocationInformation();

    SubscriberState getSubscriberState();

    MAPExtensionContainer getExtensionContainer();

    LocationInformationGPRS getLocationInformationGPRS();

    PSSubscriberState getPSSubscriberState();

    IMEI getIMEI();

    MSClassmark2 getMSClassmark2();

    GPRSMSClass getGPRSMSClass();

    MNPInfoRes getMNPInfoRes();

}