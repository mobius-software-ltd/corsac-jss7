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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;

/**
 *
 MAP V3:
 *
 * setReportingState OPERATION ::= { --Timer m ARGUMENT SetReportingStateArg RESULT SetReportingStateRes -- optional ERRORS {
 * systemFailure | unidentifiedSubscriber | unexpectedDataValue | dataMissing | resourceLimitation | facilityNotSupported} CODE
 * local:73 }
 *
 * SetReportingStateArg ::= SEQUENCE { imsi [0] IMSI OPTIONAL, lmsi [1] LMSI OPTIONAL, ccbs-Monitoring [2] ReportingState
 * OPTIONAL, extensionContainer [3] ExtensionContainer OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface SetReportingStateRequest extends CallHandlingMessage {

    IMSI getImsi();

    LMSI getLmsi();

    ReportingState getCcbsMonitoring();

    MAPExtensionContainer getExtensionContainer();
}