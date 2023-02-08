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

package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 * MAP V3: UpdateLocationRes ::= SEQUENCE { hlr-Number ISDN-AddressStringImpl, extensionContainer ExtensionContainer OPTIONAL, ...,
 * add-Capability NULL OPTIONAL, pagingArea-Capability [0]NULL OPTIONAL }
 *
 * MAP V2: UpdateLocationRes ::= CHOICE { hlr-Number ISDN-AddressStringImpl, -- hlr-Number must not be used in version greater 1
 * extensibleUpdateLocationRes ExtensibleUpdateLocationRes} -- extensibleUpdateLocationRes must not be used in version 1
 *
 * ExtensibleUpdateLocationRes ::= SEQUENCE { hlr-Number ISDN-AddressStringImpl, ...}
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface UpdateLocationResponse extends MobilityMessage {

    ISDNAddressString getHlrNumber();

    MAPExtensionContainer getExtensionContainer();

    boolean getAddCapability();

    boolean getPagingAreaCapability();

}
