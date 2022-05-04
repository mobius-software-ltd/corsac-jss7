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
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBuf;

/**
 *
<code>
CellGlobalIdOrServiceAreaIdFixedLength ::= OCTET STRING (SIZE (7))
-- Refers to Cell Global Identification or Service Are Identification
-- defined in 3GPP TS 23.003.
-- The internal structure is defined as follows:
-- octet 1 bits 4321 Mobile Country Code 1st digit
--         bits 8765 Mobile Country Code 2nd digit
-- octet 2 bits 4321 Mobile Country Code 3rd digit
--         bits 8765 Mobile Network Code 3rd digit
-- or filler (1111) for 2 digit MNCs
-- octet 3 bits 4321 Mobile Network Code 1st digit
--         bits 8765 Mobile Network Code 2nd digit
-- octets 4 and 5 Location Area Code according to 3GPP TS 24.008
-- octets 6 and 7 Cell Identity (CI) value or
-- Service Area Code (SAC) value
-- according to 3GPP TS 23.003
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface CellGlobalIdOrServiceAreaIdFixedLength {

	ByteBuf getValue();
	
    int getMCC() throws ASNParsingException;

    int getMNC() throws ASNParsingException;

    int getLac() throws ASNParsingException;

    /**
     * Cell Identity (CI) value or Service Area Code (SAC) value
     *
     * @return
     */
    int getCellIdOrServiceAreaCode() throws ASNParsingException;
}