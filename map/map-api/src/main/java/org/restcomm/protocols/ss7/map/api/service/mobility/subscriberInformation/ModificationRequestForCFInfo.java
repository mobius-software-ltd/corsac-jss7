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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import java.io.Serializable;

import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;

/**
 *
<code>
ModificationRequestFor-CF-Info ::= SEQUENCE {
  ss-Code                  [0] SS-Code,
  basicService             [1] Ext-BasicServiceCode OPTIONAL,
  ss-Status                [2] Ext-SS-Status OPTIONAL,
  forwardedToNumber        [3] AddressString OPTIONAL,
  forwardedToSubaddress    [4] ISDN-SubaddressString OPTIONAL,
  noReplyConditionTime     [5] Ext-NoRepCondTime OPTIONAL,
  modifyNotificationToCSE  [6] ModificationInstruction OPTIONAL,
  extensionContainer       [7] ExtensionContainer OPTIONAL,
  ...
}

Ext-NoRepCondTime ::= INTEGER (1..100)
-- Only values 5-30 are used.
-- Values in the ranges 1-4 and 31-100 are reserved for future use
-- If received:
-- values 1-4 shall be mapped on to value 5
-- values 31-100 shall be mapped on to value 30
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ModificationRequestForCFInfo extends Serializable {

	SSCodeImpl getSsCode();

    ExtBasicServiceCodeImpl getBasicService();

    ExtSSStatusImpl getSsStatus();

    AddressStringImpl getForwardedToNumber();

    ISDNSubaddressStringImpl getForwardedToSubaddress();

    Integer getNoReplyConditionTime();

    ModificationInstruction getModifyNotificationToCSE();

    MAPExtensionContainerImpl getExtensionContainer();

}
