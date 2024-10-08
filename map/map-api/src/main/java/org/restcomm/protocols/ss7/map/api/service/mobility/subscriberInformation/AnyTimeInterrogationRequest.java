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

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
<code>
MAP V3:

anyTimeInterrogation OPERATION ::= {
  --Timer m
  ARGUMENT AnyTimeInterrogationArg
  RESULT AnyTimeInterrogationRes ERRORS { systemFailure | ati-NotAllowed | dataMissing | unexpectedDataValue | unknownSubscriber }
  CODE local:71
}


AnyTimeInterrogationArg ::= SEQUENCE {
  subscriberIdentity  [0] SubscriberIdentity,
  requestedInfo       [1] RequestedInfo,
  gsmSCF-Address      [3] ISDN-AddressStringImpl,
  extensionContainer  [2] ExtensionContainer OPTIONAL,
  ...
}
</code>
 *
 * @author abhayani
 * @author yulianoifa
 *
 */
public interface AnyTimeInterrogationRequest extends MobilityMessage {

	SubscriberIdentity getSubscriberIdentity();

	RequestedInfo getRequestedInfo();

    ISDNAddressString getGsmSCFAddress();

    MAPExtensionContainer getExtensionContainer();
}
