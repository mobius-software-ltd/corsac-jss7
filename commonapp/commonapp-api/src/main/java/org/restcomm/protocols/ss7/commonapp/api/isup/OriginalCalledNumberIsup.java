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

import org.restcomm.protocols.ss7.isup.message.parameter.OriginalCalledNumber;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

/**
 *
ISUP OriginalCalledNumber wrapper
OriginalCalledPartyID {PARAMETERS-BOUND : bound} ::= OCTET STRING (
  SIZE( bound.&minOriginalCalledPartyIDLength .. bound.&maxOriginalCalledPartyIDLength))
-- Indicates the original called number. Refer to ETSI EN 300 356-1 [23] Original Called Number
-- for encoding.
minOriginalCalledPartyIDLength ::= 2
maxOriginalCalledPartyIDLength ::= 10
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface OriginalCalledNumberIsup {

    OriginalCalledNumber getOriginalCalledNumber() throws ASNParsingException;

}