/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.map.api.primitives.ASCICallReference;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalSubscriptionsImpl;

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
 *
 */
public interface SendGroupCallInfoResponse extends CallHandlingMessage {

     ISDNAddressStringImpl getAnchorMscAddress();

     ASCICallReference getAsciCallReference();

     IMSIImpl getImsi();

     AdditionalInfoImpl getAdditionalInfo();

     AdditionalSubscriptionsImpl getAdditionalSubscriptions();

     byte[] getKc();

     MAPExtensionContainerImpl getExtensionContainer();

}
