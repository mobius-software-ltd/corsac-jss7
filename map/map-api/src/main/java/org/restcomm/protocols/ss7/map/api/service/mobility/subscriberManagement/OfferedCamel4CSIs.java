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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
OfferedCamel4CSIs ::= BIT STRING {
  o-csi (0),
  d-csi (1),
  vt-csi (2),
  t-csi (3),
  mt-sms-csi (4),
  mg-csi (5),
  psi-enhancements (6)
} (SIZE (7..16))
-- A node supporting Camel phase 4 shall mark in the BIT STRING all Camel4 CSIs
-- it offers.
-- Other values than listed above shall be discarded.
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,lengthIndefinite=false)
public interface OfferedCamel4CSIs {

    boolean getOCsi();

    boolean getDCsi();

    boolean getVtCsi();

    boolean getTCsi();

    boolean getMtSmsCsi();

    boolean getMgCsi();

    boolean getPsiEnhancements();

}