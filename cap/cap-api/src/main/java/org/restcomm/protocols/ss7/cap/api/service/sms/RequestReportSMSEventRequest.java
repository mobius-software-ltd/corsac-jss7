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

package org.restcomm.protocols.ss7.cap.api.service.sms;


import java.util.List;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

/**
 *
 requestReportSMSEvent {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT RequestReportSMSEventArg {bound}
   RETURN RESULT FALSE
   ERRORS {missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence |
     unexpectedDataValue | unexpectedParameter}
   CODE opcode-requestReportSMSEvent}
   -- Direction: gsmSCF -> gsmSSF or gprsSSF, Timer: Trrbsms
   -- This operation is used to request the gsmSSF or gprsSSF to monitor for a
   -- Short Message related event (FSM events such as submission, delivery or failure)
   -- and to send a notification to the gsmSCF when the event is detected.

RequestReportSMSEventArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  sMSEvents  [0] SEQUENCE SIZE (1..10) OF SMSEvent,
  extensions [10] Extensions {bound} OPTIONAL, ...
}
-- Indicates the Short Message related events(s) for notification.
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface RequestReportSMSEventRequest extends SmsMessage {

    List<SMSEvent> getSMSEvents();

    CAPINAPExtensions getExtensions();

}