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

package org.restcomm.protocols.ss7.map.api.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.SignalInfo;
import org.restcomm.protocols.ss7.map.api.smstpdu.SmsDeliverReportTpdu;


/**
 *
The MAP ReturnError message: MessageSMDeliveryFailure with parameters

sm-DeliveryFailure ERROR ::= {
  PARAMETER SM-DeliveryFailureCause
  CODE local:32
}

MAP V 2-3
SM-DeliveryFailureCause ::= SEQUENCE {
  sm-EnumeratedDeliveryFailureCause   SM-EnumeratedDeliveryFailureCause,
  diagnosticInfo                      SignalInfo OPTIONAL,
  extensionContainer                  ExtensionContainer OPTIONAL,
  ...
}

MAP V1
sm-EnumeratedDeliveryFailureCause     SM-EnumeratedDeliveryFailureCause

 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface MAPErrorMessageSMDeliveryFailure extends MAPErrorMessage {

    SMEnumeratedDeliveryFailureCause getSMEnumeratedDeliveryFailureCause();

    SignalInfo getSignalInfo();

    MAPExtensionContainer getExtensionContainer();

    void setSMEnumeratedDeliveryFailureCause(SMEnumeratedDeliveryFailureCause sMEnumeratedDeliveryFailureCause);

    void setSignalInfo(SignalInfo signalInfo);

    void setExtensionContainer(MAPExtensionContainer extensionContainer);

    SmsDeliverReportTpdu getSmsDeliverReportTpdu() throws MAPException;

    void setSmsDeliverReportTpdu(SmsDeliverReportTpdu tpdu) throws MAPException;

}
