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

package org.restcomm.protocols.ss7.map.api.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;

/**
 *
 MAP V1-2:
 *
 * MAP V2: alertServiceCentre OPERATION ::= { --Timer s ARGUMENT AlertServiceCentreArg RETURN RESULT TRUE ERRORS { systemFailure
 * | dataMissing | unexpectedDataValue} CODE local:64 }
 *
 * MAP V1: alertServiceCentreWithoutResult ::= OPERATION --Timer s ARGUMENT alertServiceCentreArg AlertServiceCentreArg
 *
 *
 * MAP V1-2: AlertServiceCentreArg ::= SEQUENCE { msisdn ISDN-AddressStringImpl, serviceCentreAddress AddressStringImpl, ...}
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface AlertServiceCentreRequest extends SmsMessage {

    ISDNAddressString getMsisdn();

    AddressString getServiceCentreAddress();
}