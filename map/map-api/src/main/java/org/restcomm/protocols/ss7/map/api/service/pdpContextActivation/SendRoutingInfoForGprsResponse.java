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

package org.restcomm.protocols.ss7.map.api.service.pdpContextActivation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

/**
 *
<code>
SendRoutingInfoForGprsRes ::= SEQUENCE {
  sgsn-Address              [0] GSN-Address,
  ggsn-Address              [1] GSN-Address OPTIONAL,
  mobileNotReachableReason  [2] AbsentSubscriberDiagnosticSM OPTIONAL,
  extensionContainer        [3] ExtensionContainer OPTIONAL,
  ...
}

AbsentSubscriberDiagnosticSM ::= INTEGER (0..255)
-- AbsentSubscriberDiagnosticSM values are defined in 3GPP TS 23.040
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface SendRoutingInfoForGprsResponse extends PdpContextActivationMessage {
	GSNAddress getSgsnAddress();

	GSNAddress getGgsnAddress();

    Integer getMobileNotReachableReason();

    MAPExtensionContainer getExtensionContainer();
}