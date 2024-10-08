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

package org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 CAI-GSM0224 ::= SEQUENCE { e1 [0] INTEGER (0..8191) OPTIONAL, e2 [1] INTEGER (0..8191) OPTIONAL, e3 [2] INTEGER (0..8191)
 * OPTIONAL, e4 [3] INTEGER (0..8191) OPTIONAL, e5 [4] INTEGER (0..8191) OPTIONAL, e6 [5] INTEGER (0..8191) OPTIONAL, e7 [6]
 * INTEGER (0..8191) OPTIONAL } -- Indicates Charge Advice Information to the Mobile Station. For information regarding --
 * parameter usage, refer to 3GPP TS 22.024 [2].
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface CAI_GSM0224 {

    Integer getE1();

    Integer getE2();

    Integer getE3();

    Integer getE4();

    Integer getE5();

    Integer getE6();

    Integer getE7();
}