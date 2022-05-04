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

package org.restcomm.protocols.ss7.cap.api.service.sms;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;

/**
 *
 connectSMS {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT ConnectSMSArg {bound}
   RETURN RESULT FALSE
   ERRORS {missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence | unexpectedDataValue |
     unexpectedParameter}
   CODE opcode-connectSMS}
 -- Direction: gsmSCF -> gsmSSF or gprsSSF, Timer: Tconsms
 -- This operation is used to request the smsSSF to perform the SMS processing
 -- actions to route or forward a short message to a specified destination.

 ConnectSMSArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
   callingPartysNumber         [0] SMS-AddressString OPTIONAL,
   destinationSubscriberNumber [1] CalledPartyBCDNumber {bound} OPTIONAL,
   sMSCAddress                 [2] ISDN-AddressString OPTIONAL,
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
public interface ConnectSMSRequest extends SmsMessage {

    SMSAddressString getCallingPartysNumber();

    CalledPartyBCDNumber getDestinationSubscriberNumber();

    ISDNAddressString getSMSCAddress();

    CAPINAPExtensions getExtensions();
}