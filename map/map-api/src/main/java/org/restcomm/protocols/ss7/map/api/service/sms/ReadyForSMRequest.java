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

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

/**
 *
<code>
MAP V2-3:

MAP V3: readyForSM OPERATION ::= {
  --Timer m
  ARGUMENT ReadyForSM-Arg
  RESULT ReadyForSM-Res
  -- optional
  ERRORS { dataMissing | unexpectedDataValue | facilityNotSupported | unknownSubscriber }
  CODE local:66
}

MAP V2:
ReadyForSM ::= OPERATION
--Timer m
ARGUMENT readyForSM-Arg
ReadyForSM-Arg
RESULT
ERRORS { DataMissing, UnexpectedDataValue, FacilityNotSupported, UnknownSubscriber }


MAP V3:

ReadyForSM-Arg ::= SEQUENCE {
  imsi                   [0] IMSI,
  alertReason            AlertReason,
  alertReasonIndicator   NULL OPTIONAL,
  -- alertReasonIndicator is set only when the alertReason
  -- sent to HLR is for GPRS
  extensionContainer     ExtensionContainer OPTIONAL,
  ...,
  additionalAlertReasonIndicator [1] NULL OPTIONAL
  -- additionalAlertReasonIndicator is set only when the alertReason
  -- sent to HLR is for IP-SM-GW
}

MAP V2:

ReadyForSM-Arg ::= SEQUENCE {
  imsi         [0] IMSI,
  alertReason  AlertReason,
  ...
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ReadyForSMRequest extends SmsMessage {
	IMSI getImsi();

    AlertReason getAlertReason();

    boolean getAlertReasonIndicator();

    MAPExtensionContainer getExtensionContainer();

    boolean getAdditionalAlertReasonIndicator();
}
