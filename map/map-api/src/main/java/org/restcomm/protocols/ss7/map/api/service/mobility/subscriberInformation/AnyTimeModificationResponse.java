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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
<code>
AnyTimeModificationRes ::= SEQUENCE {
  ss-InfoFor-CSE           [0] Ext-SS-InfoFor-CSE OPTIONAL,
  camel-SubscriptionInfo   [1] CAMEL-SubscriptionInfo OPTIONAL,
  extensionContainer       [2] ExtensionContainer OPTIONAL,
  ...,
  odb-Info                 [3] ODB-Info OPTIONAL,
  cw-Data                  [4] CallWaitingData OPTIONAL,
  ch-Data                  [5] CallHoldData OPTIONAL,
  clip-Data                [6] ClipData OPTIONAL,
  clir-Data                [7] ClirData OPTIONAL,
  ect-data                 [8] EctData OPTIONAL,
  serviceCentreAddress     [9] AddressStringImpl OPTIONAL
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface AnyTimeModificationResponse extends MobilityMessage {

    ExtSSInfoForCSE getSsInfoForCSE();

    CAMELSubscriptionInfo getCamelSubscriptionInfo();

    MAPExtensionContainer getExtensionContainer();

    ODBInfo getOdbInfo();

    CallWaitingData getCwData();

    CallHoldData getChData();

    ClipData getClipData();

    ClirData getClirData();

    EctData getEctData();

    AddressString getServiceCentreAddress();

}
