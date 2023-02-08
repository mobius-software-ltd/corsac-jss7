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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
ChargingCharacteristics ::= OCTET STRING (SIZE (2))
-- Octets are coded according to 3GPP TS 32.215.
hargingCharacteristics ::= OCTET STRING (SIZE(2))
--
-- Bit 0-3: Profile Index
-- Bit 4-15: For Behavior
Profile index bits (byte 0, bits 3-0 - P3-P0):
the P3 (N) flag in the Charging Characteristics indicates normal charging,
the P2 (P) flag indicates prepaid charging,
the P1 (F) flag indicates flat rate charging and
the P0 (H) flag indicates charging by hot billing.
</code>
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface ChargingCharacteristics {

    boolean isNormalCharging();

    boolean isPrepaidCharging();

    boolean isFlatRateChargingCharging();

    boolean isChargingByHotBillingCharging();

}