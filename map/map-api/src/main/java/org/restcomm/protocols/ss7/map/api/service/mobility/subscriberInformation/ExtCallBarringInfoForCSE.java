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

import java.io.Serializable;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;

/**
 *
<code>
Ext-CallBarringInfoFor-CSE ::= SEQUENCE {
  ss-Code                       [0] SS-Code,
  callBarringFeatureList        [1] Ext-CallBarFeatureList,
  password                      [2] Password OPTIONAL,
  wrongPasswordAttemptsCounter  [3] WrongPasswordAttemptsCounter OPTIONAL,
  notificationToCSE             [4] NULL OPTIONAL,
  extensionContainer            [5] ExtensionContainer OPTIONAL,
  ...
}

Ext-CallBarFeatureList ::= SEQUENCE SIZE (1..32) OF Ext-CallBarringFeature

WrongPasswordAttemptsCounter ::= INTEGER (0..4)
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ExtCallBarringInfoForCSE extends Serializable {

	SSCode getSsCode();

    List<ExtCallBarringFeature> getCallBarringFeatureList();

    Password getPassword();

    Integer getWrongPasswordAttemptsCounter();

    boolean getNotificationToCSE();

    MAPExtensionContainer getExtensionContainer();

}
