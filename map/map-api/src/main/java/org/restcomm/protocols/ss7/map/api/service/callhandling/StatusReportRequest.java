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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

/**
 *
 MAP V3:
 *
 * statusReport OPERATION ::= { --Timer m ARGUMENT StatusReportArg RESULT StatusReportRes -- optional ERRORS { unknownSubscriber
 * | systemFailure | unexpectedDataValue | dataMissing} CODE local:74 }
 *
 * StatusReportArg ::= SEQUENCE{ imsi [0] IMSI, eventReportData [1] EventReportData OPTIONAL, callReportdata [2] CallReportData
 * OPTIONAL, extensionContainer [3] ExtensionContainer OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface StatusReportRequest extends CallHandlingMessage {

    IMSI getImsi();

    EventReportData getEventReportData();

    CallReportData getCallReportData();

    MAPExtensionContainer getExtensionContainer();
}