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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
MG-CSI ::= SEQUENCE {
  mobilityTriggers    MobilityTriggers,
  serviceKey          ServiceKey,
  gsmSCF-Address      [0] ISDN-AddressString,
  extensionContainer  [1] ExtensionContainer OPTIONAL,
  notificationToCSE   [2] NULL OPTIONAL,
  csi-Active          [3] NULL OPTIONAL,
  ...
}
-- notificationToCSE and csi-Active shall not be present when MG-CSI is sent to SGSN.
-- They may only be included in ATSI/ATM ack/NSDC message.
ServiceKey ::= INTEGER (0..2147483647)
MobilityTriggers ::= SEQUENCE SIZE (1..10) OF MM-Code
</code>
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface MGCSI {

    List<MMCode> getMobilityTriggers();

    int getServiceKey();

    ISDNAddressString getGsmSCFAddress();

    MAPExtensionContainer getExtensionContainer();

    boolean getNotificationToCSE();

    boolean getCsiActive();

}