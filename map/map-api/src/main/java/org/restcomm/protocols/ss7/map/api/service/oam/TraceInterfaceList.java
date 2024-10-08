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
TraceInterfaceList ::= SEQUENCE {
  msc-s-List      [0] MSC-S-InterfaceList OPTIONAL,
  mgw-List        [1] MGW-InterfaceList OPTIONAL,
  sgsn-List       [2] SGSN-InterfaceList OPTIONAL,
  ggsn-List       [3] GGSN-InterfaceList OPTIONAL,
  rnc-List        [4] RNC-InterfaceList OPTIONAL,
  bmsc-List       [5] BMSC-InterfaceList OPTIONAL,
  ...,
  mme-List        [6] MME-InterfaceLis	t OPTIONAL,
  sgw-List        [7] SGW-InterfaceList OPTIONAL,
  pgw-List        [8] PGW-InterfaceList OPTIONAL,
  eNB-List        [9] ENB-InterfaceList OPTIONAL
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface TraceInterfaceList {

    MSCSInterfaceList getMscSList();

    MGWInterfaceList getMgwList();

    SGSNInterfaceList getSgsnList();

    GGSNInterfaceList getGgsnList();

    RNCInterfaceList getRncList();

    BMSCInterfaceList getBmscList();

    MMEInterfaceList getMmeList();

    SGWInterfaceList getSgwList();

    PGWInterfaceList getPgwList();

    ENBInterfaceList getEnbList();

}
