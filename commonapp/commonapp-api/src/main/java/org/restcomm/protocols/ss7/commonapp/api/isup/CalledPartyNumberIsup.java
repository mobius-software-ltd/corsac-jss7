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

package org.restcomm.protocols.ss7.commonapp.api.isup;

import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

/**
 *
<code>
ISUP CalledPartyNumber wrapper
CalledPartyNumber {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minCalledPartyNumberLength .. bound.&maxCalledPartyNumberLength))
-- Indicates the Called Party Number. Refer to ETS EN 300 356-1 [23] for encoding.
-- A CalledPartyNumber may contain national-specific values of the Nature Of Address
-- indicator. The filling-in of the national-specific Nature Of Address indicator
-- values shall be done in accordance with the national ISUP of the gsmSSF country, e.g.
-- ANSI T1.113-1995 [92].
-- In terms of ETS EN 300 356-1 [23], the Destination Address Field is not present if the
-- destination address length is set to zero. This is the case e.g. when the ANSI
-- ISUP Nature Of Address indicator indicates no number present, operator requested
-- (1110100) or no number present, cut-through call to carrier (1110101).
-- See also see 3GPP TS 23.078 [7].
minCalledPartyNumberLength ::= 2
maxCalledPartyNumberLength ::= 18
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface CalledPartyNumberIsup {

    CalledPartyNumber getCalledPartyNumber() throws ASNParsingException;
}