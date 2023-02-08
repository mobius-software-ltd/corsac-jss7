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

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;

/**
 *
 eventReportSMS {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT EventReportSMSArg {bound}
   RETURN RESULT FALSE
   ALWAYS RESPONDS FALSE
   CODE opcode-eventReportSMS
 }
 -- Direction: gsmSSF or gprsSSF -> gsmSCF, Timer: Terbsms
 -- This operation is used to notify the gsmSCF of a Short Message related event (FSM events
 -- such as submission, delivery or failure) previously requested by the gsmSCF in a
 -- RequestReportSMSEvent operation.

 EventReportSMSArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
   eventTypeSMS                [0] EventTypeSMS,
   eventSpecificInformationSMS [1] EventSpecificInformationSMS OPTIONAL,
   miscCallInfo                [2] MiscCallInfo DEFAULT {messageType request},
   extensions                  [10] Extensions {bound} OPTIONAL,
   ...
}
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface EventReportSMSRequest extends SmsMessage {

    EventTypeSMS getEventTypeSMS();

    EventSpecificInformationSMS getEventSpecificInformationSMS();

    MiscCallInfo getMiscCallInfo();

    CAPINAPExtensions getExtensions();

}