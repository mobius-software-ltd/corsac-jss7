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

package org.restcomm.protocols.ss7.map.smstpdu;

import org.restcomm.protocols.ss7.commonapp.api.datacoding.NationalLanguageIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.Gsm7NationalLanguageIdentifier;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public abstract class Gsm7NationalLanguageIdentifierImpl implements Gsm7NationalLanguageIdentifier {
	private static final long serialVersionUID = 1L;
	
	private NationalLanguageIdentifier nationalLanguageCode;

    public Gsm7NationalLanguageIdentifierImpl(NationalLanguageIdentifier nationalLanguageCode) {
        this.nationalLanguageCode = nationalLanguageCode;
    }

    public Gsm7NationalLanguageIdentifierImpl(ByteBuf encodedInformationElementData) {
        if (encodedInformationElementData != null && encodedInformationElementData.readableBytes() > 0)
            this.nationalLanguageCode = NationalLanguageIdentifier.getInstance(encodedInformationElementData.readByte() & 0xFF);
    }

    public NationalLanguageIdentifier getNationalLanguageIdentifier() {
        return nationalLanguageCode;
    }

    public ByteBuf getEncodedInformationElementData() {
    	ByteBuf buf=Unpooled.buffer(1);
    	buf.writeByte((byte) nationalLanguageCode.getCode());
        return buf;
    }
}