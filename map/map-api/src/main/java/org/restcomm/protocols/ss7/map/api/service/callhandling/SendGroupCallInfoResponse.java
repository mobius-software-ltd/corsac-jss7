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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.ASCICallReference;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalSubscriptions;

import io.netty.buffer.ByteBuf;

/**
 *
 SendGroupCallInfoRes ::= SEQUENCE { anchorMSC-Address [0] ISDN-AddressString OPTIONAL, asciCallReference [1]
 * ASCI-CallReference OPTIONAL, imsi [2] IMSI OPTIONAL, additionalInfo [3] AdditionalInfo OPTIONAL, additionalSubscriptions [4]
 * AdditionalSubscriptions OPTIONAL, kc [5] Kc OPTIONAL, extensionContainer [6] ExtensionContainer OPTIONAL, ... }
 *
 * Kc ::= OCTET STRING (SIZE (8))
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface SendGroupCallInfoResponse extends CallHandlingMessage {

     ISDNAddressString getAnchorMscAddress();

     ASCICallReference getAsciCallReference();

     IMSI getImsi();

     AdditionalInfo getAdditionalInfo();

     AdditionalSubscriptions getAdditionalSubscriptions();

     ByteBuf getKc();

     MAPExtensionContainer getExtensionContainer();
}
