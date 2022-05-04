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
TariffSwitchoverTime ::= OCTET STRING (SIZE(1))
-- This time is the absolute time at which the next tariff has to become active. It is represented in steps of 15 minutes.
-- The coding is the following:
-- 0 : spare
-- 1 : 0 hour 15 minutes
-- 2 : 0 hour 30 minutes
-- 3 : 0 hour 45 minutes
-- 4 : 1 hour 0 minutes
-- ..
-- 96 : 24 hours 0 minutes
-- 97-255 : spare
</code>
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface TariffSwitchoverTime {

    Integer getData();

}
