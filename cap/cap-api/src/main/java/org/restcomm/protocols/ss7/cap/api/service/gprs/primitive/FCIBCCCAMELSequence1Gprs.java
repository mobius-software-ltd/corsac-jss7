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

import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 fCIBCCCAMELsequence1 [0] SEQUENCE { freeFormatData [0] OCTET STRING (SIZE(1 .. 160)), pDPID [1] PDPID OPTIONAL,
 * appendFreeFormatData [2] AppendFreeFormatData DEFAULT overwrite, ... }
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,lengthIndefinite = false)
public interface FCIBCCCAMELSequence1Gprs {

    FreeFormatDataGprs getFreeFormatData();

    PDPID getPDPID();

    AppendFreeFormatData getAppendFreeFormatData();

}