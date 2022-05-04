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

package org.restcomm.protocols.ss7.commonapp.api.isup;

import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

/**
 *
 ISUP CauseIndicators wrapper
 *
 * Cause {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minCauseLength .. bound.&maxCauseLength)) -- Indicates the
 * cause for interface related information. -- Refer to ETSI EN 300 356-1 [23] Cause parameter for encoding. -- For the use of
 * cause and location values refer to ITU-T Recommendation Q.850 [47] -- Shall always include the cause value and shall also
 * include the diagnostics field, -- if available.
 *
 * minCauseLength ::= 2 maxCauseLength ::= 32
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface CauseIsup {

    CauseIndicators getCauseIndicators() throws ASNParsingException;

}