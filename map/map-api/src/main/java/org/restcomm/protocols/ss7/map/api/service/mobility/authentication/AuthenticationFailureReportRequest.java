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

package org.restcomm.protocols.ss7.map.api.service.mobility.authentication;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

import io.netty.buffer.ByteBuf;

/**
 *
<code>
MAP V3:
authenticationFailureReport OPERATION ::= {
  --Timer m
  ARGUMENT AuthenticationFailureReportArg
  RESULT AuthenticationFailureReportRes
  -- optional
  ERRORS { systemFailure | unexpectedDataValue | unknownSubscriber }
  CODE local:15
}

AuthenticationFailureReportArg ::= SEQUENCE {
  imsi                IMSI,
  failureCause        FailureCause,
  extensionContainer  ExtensionContainer OPTIONAL,
  ... ,
  re-attempt          BOOLEAN OPTIONAL,
  accessType          AccessType OPTIONAL,
  rand                RAND OPTIONAL,
  vlr-Number          [0] ISDN-AddressStringImpl OPTIONAL,
  sgsn-Number         [1] ISDN-AddressStringImpl OPTIONAL
}

RAND ::= OCTET STRING (SIZE (16))
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface AuthenticationFailureReportRequest extends MobilityMessage {

	IMSI getImsi();

    FailureCause getFailureCause();

    MAPExtensionContainer getExtensionContainer();

    Boolean getReAttempt();

    AccessType getAccessType();

    ByteBuf getRand();

    ISDNAddressString getVlrNumber();

    ISDNAddressString getSgsnNumber();
}
