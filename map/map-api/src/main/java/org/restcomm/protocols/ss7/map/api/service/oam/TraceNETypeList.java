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

package org.restcomm.protocols.ss7.map.api.service.oam;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
TraceNE-TypeList ::= BIT STRING {
  msc-s (0),
  mgw (1),
  sgsn (2),
  ggsn (3),
  rnc (4),
  bm-sc (5),
  mme (6),
  sgw (7),
  pgw (8),
  eNB (9)
} (SIZE (6..16))
-- Other bits than listed above shall be discarded.
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,lengthIndefinite=false)
public interface TraceNETypeList {

    boolean getMscS();

    boolean getMgw();

    boolean getSgsn();

    boolean getGgsn();

    boolean getRnc();

    boolean getBmSc();

    boolean getMme();

    boolean getSgw();

    boolean getPgw();

    boolean getEnb();

}