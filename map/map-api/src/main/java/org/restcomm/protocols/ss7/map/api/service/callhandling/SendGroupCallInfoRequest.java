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

import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellIdImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.TMSIImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LongGroupIdImpl;

/**
 *
 MAP V3:
 *
 * sendGroupCallInfo OPERATION ::= { --Timer m ARGUMENT SendGroupCallInfoArg RESULT SendGroupCallInfoRes ERRORS { systemFailure
 * | ongoingGroupCall | unexpectedDataValue | dataMissing | teleserviceNotProvisioned | unknownSubscriber} CODE local:84 }
 *
 * SendGroupCallInfoArg ::= SEQUENCE { requestedInfo RequestedInfo, groupId Long-GroupId, teleservice Ext-TeleserviceCode,
 * cellId [0] GlobalCellId OPTIONAL, imsi [1] IMSI OPTIONAL, tmsi [2] TMSI OPTIONAL, additionalInfo [3] AdditionalInfo OPTIONAL,
 * talkerPriority [4] TalkerPriority OPTIONAL, cksn [5] Cksn OPTIONAL, extensionContainer [6] ExtensionContainer OPTIONAL, ... }
 *
 * Cksn ::= OCTET STRING (SIZE (1)) -- The internal structure is defined in 3GPP TS 24.008
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendGroupCallInfoRequest extends CallHandlingMessage {

     RequestedInfoImpl getRequestedInfo();

     LongGroupIdImpl getGroupId();

     ExtTeleserviceCodeImpl getTeleservice();

     GlobalCellIdImpl getCellId();

     IMSIImpl getImsi();

     TMSIImpl getTmsi();

     AdditionalInfoImpl getAdditionalInfo();

     TalkerPriority getTalkerPriority();

     byte[] getCksn();

     MAPExtensionContainerImpl getExtensionContainer();

}
