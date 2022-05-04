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

package org.restcomm.protocols.ss7.map.api.service.pdpContextActivation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

/**
 *
 MAP V3:
 *
 * failureReport OPERATION ::= { --Timer m ARGUMENT FailureReportArg RESULT FailureReportRes -- optional ERRORS { systemFailure
 * | dataMissing | unexpectedDataValue | unknownSubscriber} CODE local:25 }
 *
 * FailureReportArg ::= SEQUENCE { imsi [0] IMSI, ggsn-Number [1] ISDN-AddressString , ggsn-Address [2] GSN-Address OPTIONAL,
 * extensionContainer [3] ExtensionContainer OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface FailureReportRequest extends PdpContextActivationMessage {

    IMSI getImsi();

    ISDNAddressString getGgsnNumber();

    GSNAddress getGgsnAddress();

    MAPExtensionContainer getExtensionContainer();

}
