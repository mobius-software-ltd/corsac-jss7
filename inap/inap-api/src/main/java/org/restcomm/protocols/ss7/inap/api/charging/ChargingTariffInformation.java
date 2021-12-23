/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.inap.api.charging;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
ChargingTariffInformation ::= SEQUENCE {
  chargingControlIndicators [00] ChargingControlIndicators,
  chargingTariff            [01] CHOICE {
    tariffCurrency  [00] TariffCurrency,
    tariffPulse     [01] TariffPulse
  },
  extensions                [02] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
  originationIdentification [03] ChargingReferenceIdentification,
  destinationIdentification [04] ChargingReferenceIdentification OPTIONAL,
  currency                  [05] Currency
}
--This message is used
-- to transfer explicit tariff data to the originating subscriber exchange and the charge  registration exchange during call
-- set-up and also in the active phase of a call.
-- The destinationIdentification is not available in an initial ChargingTariffInformation, in all subsequent ones it is included, see
-- "Handling of Identifiers".
</code>
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface ChargingTariffInformation {

    ChargingControlIndicators getChargingControlIndicators();

    ChargingTariff getChargingTariff();

    CAPINAPExtensions getExtensions();

    ChargingReferenceIdentification getOriginationIdentification();

    ChargingReferenceIdentification getDestinationIdentification();

    Currency getCurrency();

}
