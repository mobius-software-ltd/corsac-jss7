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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingOptions;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * ForwardingData ::= SEQUENCE { forwardedToNumber [5] ISDN-AddressString OPTIONAL, forwardedToSubaddress [4]
 * ISDN-SubaddressString OPTIONAL, forwardingOptions [6] ForwardingOptions OPTIONAL, extensionContainer [7] ExtensionContainer
 * OPTIONAL, ..., longForwardedToNumber [8] FTN-AddressString OPTIONAL}
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface ForwardingData {
     ISDNAddressString getForwardedToNumber();

     ISDNSubaddressString getForwardedToSubaddress(); // TODO: ISDNSubaddressString

     ForwardingOptions getForwardingOptions();

     MAPExtensionContainer getExtensionContainer();

     FTNAddressString getLongForwardedToNumber();
}