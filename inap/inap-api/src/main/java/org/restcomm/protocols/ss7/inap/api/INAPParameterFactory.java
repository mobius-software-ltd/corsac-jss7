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

package org.restcomm.protocols.ss7.inap.api;

import org.restcomm.protocols.ss7.inap.api.isup.CallingPartysCategoryInapImpl;
import org.restcomm.protocols.ss7.inap.api.isup.HighLayerCompatibilityInapImpl;
import org.restcomm.protocols.ss7.inap.api.isup.RedirectionInformationInapImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoDpAssignment;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface INAPParameterFactory {

    CallingPartysCategoryInapImpl createCallingPartysCategoryInap(CallingPartyCategory callingPartyCategory)
            throws INAPException;

    HighLayerCompatibilityInapImpl createHighLayerCompatibilityInap(UserTeleserviceInformation highLayerCompatibility)
            throws INAPException;

    RedirectionInformationInapImpl createRedirectionInformationInap(RedirectionInformation redirectionInformation)
            throws INAPException;

    SendingLegIDImpl createSendingLegID(LegType legID);

    ReceivingLegIDImpl createReceivingLegID(LegType legID);

    LegIDImpl createLegID(ReceivingLegIDImpl receivingLeg,SendingLegIDImpl sendingLeg);
    
    MiscCallInfoImpl createMiscCallInfo(MiscCallInfoMessageType messageType, MiscCallInfoDpAssignment dpAssignment);

}
