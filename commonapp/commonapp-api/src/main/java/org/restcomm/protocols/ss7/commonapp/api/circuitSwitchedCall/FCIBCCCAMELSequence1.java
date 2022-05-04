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

package org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
fCIBCCCAMELsequence1 [0] SEQUENCE {
  freeFormatData       [0] OCTET STRING (SIZE( bound.&minFCIBillingChargingDataLength .. bound.&maxFCIBillingChargingDataLength)),
  partyToCharge        [1] SendingSideID DEFAULT sendingSideID: leg1,
  appendFreeFormatData [2] AppendFreeFormatData DEFAULT overwrite,
  ...
}
minFCIBillingChargingDataLength ::= 1
maxFCIBillingChargingDataLength ::= 160
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface FCIBCCCAMELSequence1 {

    FreeFormatData getFreeFormatData();

    LegType getPartyToCharge();

    AppendFreeFormatData getAppendFreeFormatData();

}