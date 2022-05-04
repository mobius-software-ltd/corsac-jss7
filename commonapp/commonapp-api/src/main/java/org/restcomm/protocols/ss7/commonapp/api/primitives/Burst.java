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

package org.restcomm.protocols.ss7.commonapp.api.primitives;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
Burst ::= SEQUENCE {
  numberOfBursts   [0] INTEGER (1..3) DEFAULT 1,
  -- 3GPP Release 7 32 3GPP TS 29.078 7.0.0 (2005-06)
  burstInterval    [1] INTEGER (1..1200) DEFAULT 2,
  numberOfTonesInBurst [2] INTEGER (1..3) DEFAULT 3,
  toneDuration     [3] INTEGER (1..20) DEFAULT 2,
  toneInterval     [4] INTEGER (1..20) DEFAULT 2,
  ...
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface Burst {

    Integer getNumberOfBursts();

    Integer getBurstInterval();

    Integer getNumberOfTonesInBurst();

    Integer getToneDuration();

    Integer getToneInterval();
}