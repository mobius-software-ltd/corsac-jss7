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

package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

/**
 *
 MAP V3: noteMM-Event OPERATION ::= { --Timer m ARGUMENT NoteMM-EventArg RESULT NoteMM-EventRes ERRORS { dataMissing |
 * unexpectedDataValue | unknownSubscriber | mm-EventNotSupported} CODE local:89 }
 *
 * NoteMM-EventArg::= SEQUENCE { serviceKey ServiceKey, eventMet [0] MM-Code, imsi [1] IMSI, msisdn [2] ISDN-AddressStringImpl,
 * locationInformation [3] LocationInformation OPTIONAL, supportedCAMELPhases [5] SupportedCamelPhases OPTIONAL,
 * extensionContainer [6] ExtensionContainer OPTIONAL, ..., locationInformationGPRS [7] LocationInformationGPRS OPTIONAL,
 * offeredCamel4Functionalities [8] OfferedCamel4Functionalities OPTIONAL }
 *
 * ServiceKey ::= INTEGER (0..2147483647)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface NoteMMEventRequest extends MobilityMessage {

    long getServiceKey();

    MMCodeImpl getMMCode();

    IMSIImpl getImsi();

    ISDNAddressStringImpl getMsisdn();

    LocationInformationImpl getLocationInformation();

    SupportedCamelPhasesImpl getSupportedCamelPhases();

    MAPExtensionContainerImpl getExtensionContainer();

    LocationInformationGPRSImpl getLocationInformationGPRS();

    OfferedCamel4FunctionalitiesImpl getOfferedCamel4Functionalities();

}
