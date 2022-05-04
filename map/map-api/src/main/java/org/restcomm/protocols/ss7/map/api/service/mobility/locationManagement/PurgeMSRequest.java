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

package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
MAP V2-3:

MAP V3: purgeMS OPERATION ::= {
--Timer m
ARGUMENT PurgeMS-Arg RESULT
  PurgeMS-Res  -- optional
  ERRORS { dataMissing | unexpectedDataValue| unknownSubscriber}
CODE local:67 }

MAP V2: PurgeMS ::= OPERATION
--Timer m
ARGUMENT purgeMS-Arg PurgeMS-Arg
RESULT

MAP V3: PurgeMS-Arg ::= [3] SEQUENCE {
  imsi         IMSI,
  vlr-Number   [0] ISDN-AddressStringImpl OPTIONAL,
  sgsn-Number  [1] ISDN-AddressStringImpl OPTIONAL,
  extensionContainer ExtensionContainer OPTIONAL,
  ...
}

MAP V2: PurgeMS-Arg ::= SEQUENCE {
  imsi         IMSI,
  vlr-Number   ISDN-AddressStringImpl,
  ...
}

 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface PurgeMSRequest extends MobilityMessage {

	IMSI getImsi();

    ISDNAddressString getVlrNumber();

    ISDNAddressString getSgsnNumber();

    MAPExtensionContainer getExtensionContainer();
}