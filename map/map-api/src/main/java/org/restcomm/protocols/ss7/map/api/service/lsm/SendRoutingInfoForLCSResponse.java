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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;

/**
 * RoutingInfoForLCS-Res ::= SEQUENCE { targetMS [0] SubscriberIdentity, lcsLocationInfo [1] LCSLocationInfo, extensionContainer
 * [2] ExtensionContainer OPTIONAL, ..., v-gmlc-Address [3] GSN-Address OPTIONAL, h-gmlc-Address [4] GSN-Address OPTIONAL,
 * ppr-Address [5] GSN-Address OPTIONAL, additional-v-gmlc-Address [6] GSN-Address OPTIONAL }
 *
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public interface SendRoutingInfoForLCSResponse extends LsmMessage {

    SubscriberIdentity getTargetMS();

    LCSLocationInfo getLCSLocationInfo();

    MAPExtensionContainer getExtensionContainer();

    GSNAddress getVgmlcAddress();

    GSNAddress getHGmlcAddress();

    GSNAddress getPprAddress();

    GSNAddress getAdditionalVGmlcAddress();

}
