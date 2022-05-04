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

package org.restcomm.protocols.ss7.map.api.service.supplementary;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

/**
 *
 MAP V3:
 *
 * ss-InvocationNotification OPERATION ::= { --Timer m ARGUMENT SS-InvocationNotificationArg RESULT SS-InvocationNotificationRes
 * -- optional ERRORS { dataMissing | unexpectedDataValue | unknownSubscriber} CODE local:72 }
 *
 * SS-InvocationNotificationArg ::= SEQUENCE { imsi [0] IMSI, msisdn [1] ISDN-AddressString, ss-Event [2] SS-Code, -- The
 * following SS-Code values are allowed : -- ect SS-Code ::= '00110001'B -- multiPTY SS-Code ::= '01010001'B -- cd SS-Code ::=
 * '00100100'B -- ccbs SS-Code ::= '01000100'B ss-EventSpecification [3] SS-EventSpecification OPTIONAL, extensionContainer [4]
 * ExtensionContainer OPTIONAL, ..., b-subscriberNumber [5] ISDN-AddressString OPTIONAL, ccbs-RequestState [6] CCBS-RequestState
 * OPTIONAL }
 *
 * SS-EventSpecification ::= SEQUENCE SIZE (1..2) OF AddressString
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface SSInvocationNotificationRequest extends SupplementaryMessage {

    IMSI getImsi();

    ISDNAddressString getMsisdn();

    SSCode getSsEvent();

    List<AddressString> getSsEventSpecification();

    MAPExtensionContainer getExtensionContainer();

    ISDNAddressString getBSubscriberNumber();

    CCBSRequestState getCcbsRequestState();

}