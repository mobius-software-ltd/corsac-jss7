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

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSForBSCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
RequestedSubscriptionInfo ::= SEQUENCE {
  requestedSS-Info                 [1] SS-ForBS-Code OPTIONAL,
  odb                              [2] NULL OPTIONAL,
  requestedCAMEL-SubscriptionInfo  [3] RequestedCAMEL-SubscriptionInfo OPTIONAL,
  supportedVLR-CAMEL-Phases        [4] NULL OPTIONAL,
  supportedSGSN-CAMEL-Phases       [5] NULL OPTIONAL,
  extensionContainer               [6] ExtensionContainer OPTIONAL,
  ...,
  additionalRequestedCAMEL-SubscriptionInfo [7] AdditionalRequestedCAMEL-SubscriptionInfo OPTIONAL,
  msisdn-BS-List                            [8] NULL OPTIONAL,
  csg-SubscriptionDataRequested             [9] NULL OPTIONAL,
  cw-Info                                   [10] NULL OPTIONAL,
  clip-Info                                 [11] NULL OPTIONAL,
  clir-Info                                 [12] NULL OPTIONAL,
  hold-Info                                 [13] NULL OPTIONAL,
  ect-Info                                  [14] NULL OPTIONAL
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface RequestedSubscriptionInfo {

    SSForBSCode getRequestedSSInfo();

    boolean getOdb();

    RequestedCAMELSubscriptionInfo getRequestedCAMELSubscriptionInfo();

    boolean getSupportedVlrCamelPhases();

    boolean getSupportedSgsnCamelPhases();

    MAPExtensionContainer getExtensionContainer();

    AdditionalRequestedCAMELSubscriptionInfo getAdditionalRequestedCamelSubscriptionInfo();

    boolean getMsisdnBsList();

    boolean getCsgSubscriptionDataRequested();

    boolean getCwInfo();

    boolean getClipInfo();

    boolean getClirInfo();

    boolean getHoldInfo();

    boolean getEctInfo();

}