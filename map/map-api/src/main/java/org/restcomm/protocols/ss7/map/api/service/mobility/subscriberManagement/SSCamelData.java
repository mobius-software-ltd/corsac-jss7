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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 SS-CamelData ::= SEQUENCE { ss-EventList SS-EventList, gsmSCF-Address ISDN-AddressString, extensionContainer [0]
 * ExtensionContainer OPTIONAL, ...}
 *
 * SS-EventList ::= SEQUENCE SIZE (1..10) OF SS-Code -- Actions for the following SS-Code values are defined in CAMEL Phase 3:
 * -- ect SS-Code ::= '00110001'B -- multiPTY SS-Code ::= '01010001'B -- cd SS-Code ::= '00100100'B -- ccbs SS-Code ::=
 * '01000100'B -- all other SS codes shall be ignored -- When SS-CSI is sent to the VLR, it shall not contain a marking for
 * ccbs. -- If the VLR receives SS-CSI containing a marking for ccbs, the VLR shall discard the -- ccbs marking in SS-CSI.
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface SSCamelData {

    List<SSCode> getSsEventList();

    ISDNAddressString getGsmSCFAddress();

    MAPExtensionContainer getExtensionContainer();
}