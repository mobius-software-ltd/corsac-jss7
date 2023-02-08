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

package org.restcomm.protocols.ss7.cap.api.service.gprs.primitive;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
SGSNCapabilities ::= OCTET STRING (SIZE (1))
-- Indicates the SGSN capabilities. The coding of the parameter is as follows:
-- Bit Value Meaning
-- 0 0 AoC not supported by SGSN
--   1 AoC supported by SGSN
-- 1 - This bit is reserved in CAP V.3
-- 2 - This bit is reserved in CAP V.3
-- 3 - This bit is reserved in CAP V.3
-- 4 - This bit is reserved in CAP V.3
-- 5 - This bit is reserved in CAP V.3
-- 6 - This bit is reserved in CAP V.3
-- 7 - This bit is reserved in CAP V.3
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface SGSNCapabilities {
    int getData();

    boolean getAoCSupportedBySGSN();
}