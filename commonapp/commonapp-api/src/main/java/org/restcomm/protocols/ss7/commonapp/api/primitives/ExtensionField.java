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

package org.restcomm.protocols.ss7.commonapp.api.primitives;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;

/**
 *
 ExtensionField ::= SEQUENCE {
 type EXTENSION.&id ({SupportedExtensions}),
 -- shall identify the value of an EXTENSION type
 criticality CriticalityType DEFAULT ignore,
 value [1] EXTENSION.&ExtensionType ({SupportedExtensions}{@type}), ... }
 -- This parameter indicates an extension of an argument data type.
 -- Its content is network operator specific
 *
 * CriticalityType ::= ENUMERATED { ignore (0), abort (1) } Code ::= CHOICE {local INTEGER, global OBJECT IDENTIFIER}
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface ExtensionField {

    Integer getLocalCode();

    List<Long> getGlobalCode();

    CriticalityType getCriticalityType();

    ByteBuf getValue();   
}