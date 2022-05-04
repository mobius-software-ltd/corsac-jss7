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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

/**
 *
<code>
requestReportBCSMEvent {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT RequestReportBCSMEventArg {bound}
  RETURN RESULT FALSE
  ERRORS {missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence |
          unexpectedDataValue | unexpectedParameter | unknownLegID}
  CODE opcode-requestReportBCSMEvent
}
-- Direction: gsmSCF -> gsmSSF, Timer: Trrb
-- This operation is used to request the gsmSSF to monitor for a call-related event
-- (e.g. BCSM events such as O_Busy or O_No_Answer) and to send a notification
-- to the gsmSCF when the event is detected.
-- -- NOTE:
-- Every EDP must be explicitly armed by the gsmSCF via a RequestReportBCSMEvent operation.
-- No implicit arming of EDPs at the gsmSSF after reception of any operation (different
-- from RequestReportBCSMEvent) from the gsmSCF is allowed.

RequestReportBCSMEventArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  bcsmEvents     [0] SEQUENCE SIZE(1..bound.&numOfBCSMEvents) OF BCSMEvent {bound},
  extensions     [2] Extensions {bound} OPTIONAL,
  ...
}
-- Indicates the BCSM related events for notification.

numOfBCSMEvents = 30
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface RequestReportBCSMEventRequest extends CircuitSwitchedCallMessage {

    List<BCSMEvent> getBCSMEventList();

    CAPINAPExtensions getExtensions();

}
