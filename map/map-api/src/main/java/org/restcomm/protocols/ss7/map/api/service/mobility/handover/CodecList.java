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

package org.restcomm.protocols.ss7.map.api.service.mobility.handover;

import java.io.Serializable;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

/**
 *
 CodecList ::= SEQUENCE { codec1 [1] Codec, codec2 [2] Codec OPTIONAL, codec3 [3] Codec OPTIONAL, codec4 [4] Codec OPTIONAL,
 * codec5 [5] Codec OPTIONAL, codec6 [6] Codec OPTIONAL, codec7 [7] Codec OPTIONAL, codec8 [8] Codec OPTIONAL,
 * extensionContainer [9] ExtensionContainer OPTIONAL, ...} -- Codecs are sent in priority order where codec1 has highest
 * priority
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CodecList extends Serializable {

    Codec getCodec1();

    Codec getCodec2();

    Codec getCodec3();

    Codec getCodec4();

    Codec getCodec5();

    Codec getCodec6();

    Codec getCodec7();

    Codec getCodec8();

    MAPExtensionContainer getExtensionContainer();
}