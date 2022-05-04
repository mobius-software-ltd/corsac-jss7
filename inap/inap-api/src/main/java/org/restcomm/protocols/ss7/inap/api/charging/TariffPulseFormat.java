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

package org.restcomm.protocols.ss7.inap.api.charging;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
TariffPulseFormat ::= SEQUENCE {
  communicationChargeSequencePulse [00] SEQUENCE SIZE(minCommunicationTariffNum.. maxCommunicationTariffNum)
    OF CommunicationChargePulse OPTIONAL ,
  tariffControlIndicators          [01] BIT STRING { non-cyclicTariff (0) } (SIZE(minTariffIndicatorsLen..maxTariffIndicatorsLen)) ,
  callAttemptChargePulse           [02] PulseUnits OPTIONAL ,
  callSetupChargePulse             [03] PulseUnits OPTIONAL
}
-- The communication charges are meter-pulse units, which are to be applied per charge unit time interval.
-- The call attempt pulse units are to be charged only for unsuccessful calls.
-- The call set-up pulse units are to be charged once at start of charging.
</code>
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface TariffPulseFormat {

    List<CommunicationChargePulse> getCommunicationChargeSequencePulse();

    TariffControlIndicators getTariffControlIndicators();

    PulseUnits getCallAttemptChargePulse();

    PulseUnits getCallSetupChargePulse();

}
