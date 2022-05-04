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

package org.restcomm.protocols.ss7.map.api.service.supplementary;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
ForwardingFeature ::= SEQUENCE {
  basicService             BasicServiceCode OPTIONAL,
  ss-Status                [4] SS-Status OPTIONAL,
  forwardedToNumber        [5] ISDN-AddressString OPTIONAL,
  forwardedToSubaddress    [8] ISDN-SubaddressString OPTIONAL,
  forwardingOptions        [6] ForwardingOptions OPTIONAL,
  noReplyConditionTime     [7] NoReplyConditionTime OPTIONAL,
  ...,
  longForwardedToNumber    [9] FTN-AddressString OPTIONAL
}
NoReplyConditionTime ::= INTEGER (5..30)
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface ForwardingFeature {

    BasicServiceCode getBasicService();

    SSStatus getSsStatus();

    ISDNAddressString getForwardedToNumber();

    ISDNAddressString getForwardedToSubaddress();

    ForwardingOptions getForwardingOptions();

    Integer getNoReplyConditionTime();

    FTNAddressString getLongForwardedToNumber();

}