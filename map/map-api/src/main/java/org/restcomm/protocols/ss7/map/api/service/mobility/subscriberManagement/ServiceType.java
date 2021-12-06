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

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
ServiceType ::= SEQUENCE {
  serviceTypeIdentity   LCSServiceTypeID,
  gmlc-Restriction      [0] GMLC-Restriction OPTIONAL,
  notificationToMSUser  [1] NotificationToMSUser OPTIONAL,
  -- If notificationToMSUser is not received, the default value according to
  -- 3GPP TS 23.271 shall be assumed.
  extensionContainer    [2] ExtensionContainer OPTIONAL,
  ...
}
LCSServiceTypeID ::= INTEGER (0..127)
-- the integer values 0-63 are reserved for Standard LCS service types
-- the integer values 64-127 are reserved for Non Standard LCS service types
</code>
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface ServiceType {

    int getServiceTypeIdentity();

    GMLCRestriction getGMLCRestriction();

    NotificationToMSUser getNotificationToMSUser();

    MAPExtensionContainer getExtensionContainer();
}