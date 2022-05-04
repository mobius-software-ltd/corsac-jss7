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

package org.restcomm.protocols.ss7.map.api.service.mobility.handover;

import java.io.Serializable;

import io.netty.buffer.ByteBuf;

/**
 *
 PermittedEncryptionAlgorithms ::= OCTET STRING (SIZE (1..8)) -- Octets contain a complete PermittedEncryptionAlgorithms data
 * type -- as defined in 3GPP TS 25.413, encoded according to the encoding scheme -- mandated by 3GPP TS 25.413 -- Padding bits
 * are included, if needed, in the least significant bits of the -- last octet of the octet string.
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface PermittedEncryptionAlgorithms extends Serializable {

	ByteBuf getValue();
}
