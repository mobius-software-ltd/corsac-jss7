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

package org.restcomm.protocols.ss7.commonapp.api.primitives;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 NAEA-CIC ::= OCTET STRING (SIZE (3)) -- The internal structure is defined by the Carrier Identification -- parameter in ANSI
 * T1.113.3. Carrier codes between 000 and 999 may -- be encoded as 3 digits using 000 to 999 or as 4 digits using -- 0000 to
 * 0999. Carrier codes between 1000 and 9999 are encoded -- using 4 digits. -- using 4 digits.
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface NAEACIC {

    String getCarrierCode();

    NetworkIdentificationPlanValue getNetworkIdentificationPlanValue();

    NetworkIdentificationTypeValue getNetworkIdentificationTypeValue();

}
