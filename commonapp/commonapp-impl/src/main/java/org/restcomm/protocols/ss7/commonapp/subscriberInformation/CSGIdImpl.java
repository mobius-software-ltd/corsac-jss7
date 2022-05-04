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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CSGId;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
<code>
CSG-Id ::= BIT STRING (SIZE (27))
-- coded according to 3GPP TS 23.003 [17].
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CSGIdImpl extends ASNBitString implements CSGId {
	public CSGIdImpl() {
		super("CSGId",26,26,false);
	}
}