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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
GPRS-CamelTDPData ::= SEQUENCE {
  gprs-TriggerDetectionPoint [0] GPRS-TriggerDetectionPoint,
  serviceKey                 [1] ServiceKey,
  gsmSCF-Address             [2] ISDN-AddressString,
  defaultSessionHandling     [3] DefaultGPRS-Handling,
  extensionContainer         [4] ExtensionContainer OPTIONAL,
  ...
}
ServiceKey ::= INTEGER (0..2147483647)
</code>
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface GPRSCamelTDPData {

    GPRSTriggerDetectionPoint getGPRSTriggerDetectionPoint();

    int getServiceKey();

    ISDNAddressString getGsmSCFAddress();

    DefaultGPRSHandling getDefaultSessionHandling();

    MAPExtensionContainer getExtensionContainer();

}