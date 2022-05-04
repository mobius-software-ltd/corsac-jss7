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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
AddOnChargingInformation ::= SEQUENCE {
  chargingControlIndicators [00] ChargingControlIndicators ,
  addOncharge [01] CHOICE {
    addOnChargeCurrency [00] CurrencyFactorScale ,
    addOnChargePulse    [01] PulseUnits
  },
  extensions                [02] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
  originationIdentification [03] ChargingReferenceIdentification,
  destinationIdentification [04] ChargingReferenceIdentification OPTIONAL,
  currency                  [05] Currency
}
-- This message is used to add an amount of charge for the call and does not alter the current tariff.
-- The destinationIdentification is not available in an initial AddOnChargingInformation, in all subsequent ones it is included, see
-- "Handling of Identifiers".
-- In the message the
-- add-on charge has either the pulse or currency format.
</code>
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface AddOnChargingInformation {

    ChargingControlIndicators getChargingControlIndicators();

    AddOnCharge getAddOnCharge();

    CAPINAPExtensions getExtensions();

    ChargingReferenceIdentification getOriginationIdentification();

    ChargingReferenceIdentification getDestinationIdentification();

    Currency getCurrency();

}
