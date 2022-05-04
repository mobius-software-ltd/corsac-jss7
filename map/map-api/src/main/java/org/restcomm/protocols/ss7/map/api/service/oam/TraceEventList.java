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

package org.restcomm.protocols.ss7.map.api.service.oam;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
TraceEventList ::= SEQUENCE {
  msc-s-List      [0] MSC-S-EventList OPTIONAL,
  mgw-List        [1] MGW-EventList OPTIONAL,
  sgsn-List       [2] SGSN-EventList OPTIONAL,
  ggsn-List       [3] GGSN-EventList OPTIONAL,
  bmsc-List       [4] BMSC-EventList OPTIONAL,
  ...,
  mme-List        [5] MME-EventList OPTIONAL,
  sgw-List        [6] SGW-EventList OPTIONAL,
  pgw-List        [7] PGW-EventList OPTIONAL
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface TraceEventList {

    MSCSEventList getMscSList();

    MGWEventList getMgwList();

    SGSNEventList getSgsnList();

    GGSNEventList getGgsnList();

    BMSCEventList getBmscList();

    MMEEventList getMmeList();

    SGWEventList getSgwList();

    PGWEventList getPgwList();

}