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

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatusImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.PasswordImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;

/**
 *
<code>
ModificationRequestFor-CB-Info ::= SEQUENCE {
  ss-Code             [0] SS-Code,
  basicService        [1] Ext-BasicServiceCode OPTIONAL,
  ss-Status           [2] Ext-SS-Status OPTIONAL,
  password            [3] Password OPTIONAL,
  wrongPasswordAttemptsCounter   [4] WrongPasswordAttemptsCounter OPTIONAL,
  modifyNotificationToCSE        [5] ModificationInstruction OPTIONAL,
  extensionContainer             [6] ExtensionContainer OPTIONAL,
  ...
}

WrongPasswordAttemptsCounter ::= INTEGER (0..4)
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ModificationRequestForCBInfo extends Serializable {

	SSCodeImpl getSsCode();

    ExtBasicServiceCodeImpl getBasicService();

    ExtSSStatusImpl getSsStatus();

    PasswordImpl getPassword();

    Integer getWrongPasswordAttemptsCounter();

    ModificationInstruction getModifyNotificationToCSE();

    MAPExtensionContainerImpl getExtensionContainer();

}
