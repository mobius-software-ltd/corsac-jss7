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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
 AccessRestrictionData ::= BIT STRING {
   utranNotAllowed (0),
   geranNotAllowed (1),
   ganNotAllowed (2),
   i-hspa-evolutionNotAllowed (3),
   e-utranNotAllowed (4),
   ho-toNon3GPP-AccessNotAllowed (5)
} (SIZE (2..8))
-- exception handling:
-- access restriction data related to an access type not supported by a node
-- shall be ignored
-- bits 6 to 7 shall be ignored if received and not understood
</code>
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,lengthIndefinite=false)
public interface AccessRestrictionData {

    boolean getUtranNotAllowed();

    boolean getGeranNotAllowed();

    boolean getGanNotAllowed();

    boolean getIHspaEvolutionNotAllowed();

    boolean getEUtranNotAllowed();

    boolean getHoToNon3GPPAccessNotAllowed();

}
