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

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
TariffDuration ::= INTEGER (0..36000)
-- TariffDuration identifies with 0 unlimited duration and else in seconds unit.
-- 0 = unlimited
-- 1 = 1 second
-- 2 = 2 seconds
-- ...
-- 36000 = 10 hours
--
-- The duration indicates for how long time the communication charge component is valid. Expiration of the tariff duration
-- timer leads to the activation of the next communication charge (if present).
-- In the case where there is no next communication charge in the communication charge sequence, the action to be performed
-- is indicated by the tariffControlIndicators.
</code>
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 2,constructed = false,lengthIndefinite = false)
public interface TariffDuration {

    Integer getData();
}
