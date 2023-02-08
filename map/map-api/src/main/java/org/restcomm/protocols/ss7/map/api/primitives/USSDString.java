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

package org.restcomm.protocols.ss7.map.api.primitives;

import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.map.api.MAPException;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;

/**
 * USSD-String ::= OCTET STRING (SIZE (1..maxUSSD-StringLength)) -- The structure of the contents of the USSD-String is
 * dependent -- on the USSD-DataCodingScheme as described in TS 3GPP TS 23.038 [25].
 *
 * maxUSSD-StringLength INTEGER ::= 160
 *
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface USSDString {

    /**
     * Get the ByteBuf that represents encoded USSD String
     *
     * @return
     */
	ByteBuf getValue();

    /**
     * Get the decoded USSD String
     *
     * @return
     */
    String getString(Charset gsm8Charset) throws MAPException;

}