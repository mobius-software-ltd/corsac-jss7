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
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V1-2-3:

MAP V3: cancelLocation OPERATION ::= {
-- Timer m
ARGUMENT CancelLocationArg
RESULT CancelLocationRes
-- optional ERRORS { dataMissing | unexpectedDataValue }
CODE local:3 }

MAP V2: CancelLocation ::= OPERATION
--Timer m
ARGUMENT cancelLocationArg CancelLocationArg
RESULT ERRORS {
  DataMissing,
  -- DataMissing must not be used in version 1
  UnexpectedDataValue,
  UnidentifiedSubscriber
}
-- UnidentifiedSubscriber must not be used in version greater 1


MAP V3: CancelLocationArg ::= [3] SEQUENCE {
  identity                       Identity,
  cancellationType               CancellationType OPTIONAL,
  extensionContainer             ExtensionContainer OPTIONAL,
  ...,
  typeOfUpdate                   [0] TypeOfUpdate OPTIONAL,
  mtrf-SupportedAndAuthorized    [1] NULL OPTIONAL,
  mtrf-SupportedAndNotAuthorized [2] NULL OPTIONAL,
  newMSC-Number                  [3] ISDN-AddressStringImpl OPTIONAL,
  newVLR-Number                  [4] ISDN-AddressStringImpl OPTIONAL,
  new-lmsi                       [5] LMSI OPTIONAL
}
-- mtrf-SupportedAndAuthorized and mtrf-SupportedAndNotAuthorized shall not
-- both be present

MAP V2: CancelLocationArg ::= CHOICE {
  imsi            IMSI,
  imsi-WithLMSI   IMSI-WithLMSI
}

Identity ::= CHOICE {
  imsi            IMSI,
  imsi-WithLMSI   IMSI-WithLMSI
}
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CancelLocationRequest extends MobilityMessage {

	IMSI getImsi();

	IMSIWithLMSI getImsiWithLmsi();

    CancellationType getCancellationType();

    MAPExtensionContainer getExtensionContainer();

    TypeOfUpdate getTypeOfUpdate();

    boolean getMtrfSupportedAndAuthorized();

    boolean getMtrfSupportedAndNotAuthorized();

    ISDNAddressString getNewMSCNumber();

    ISDNAddressString getNewVLRNumber();

    LMSI getNewLmsi();
}