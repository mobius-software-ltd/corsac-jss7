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

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
<code>
MAP V3:

provideSubscriberInfo OPERATION ::= {
  --Timer m
  ARGUMENT ProvideSubscriberInfoArg
  RESULT ProvideSubscriberInfoRes
  ERRORS { dataMissing | unexpectedDataValue}
  CODE local:70
}

ProvideSubscriberInfoArg ::= SEQUENCE {
  imsi                [0] IMSI,
  lmsi                [1] LMSI OPTIONAL,
  requestedInfo       [2] RequestedInfo,
  extensionContainer  [3] ExtensionContainer OPTIONAL,
  ...,
  callPriority        [4] EMLPP-Priority OPTIONAL
}
</code>
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ProvideSubscriberInfoRequest extends MobilityMessage {

	IMSI getImsi();

	LMSI getLmsi();

	RequestedInfo getRequestedInfo();

    MAPExtensionContainer getExtensionContainer();

    EMLPPPriority getCallPriority();

}
