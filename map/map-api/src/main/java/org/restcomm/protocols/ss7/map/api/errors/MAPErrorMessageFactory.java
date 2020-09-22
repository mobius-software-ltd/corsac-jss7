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

package org.restcomm.protocols.ss7.map.api.errors;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatusImpl;

/**
 * The factory of MAP ReturnError messages
 *
 * @author sergey vetyutnev
 *
 */
public interface MAPErrorMessageFactory {

    /**
     * Generate the empty message depends of the error code (for incoming messages)
     *
     * @param errorCode
     * @param mapVersion
     * @return
     */
    MAPErrorMessage createMessageFromErrorCode(Long errorCode,Long mapVersion);

    MAPErrorMessageParameterless createMAPErrorMessageParameterless(Long errorCode);

    MAPErrorMessageExtensionContainer createMAPErrorMessageExtensionContainer(Long errorCode,
    		MAPExtensionContainerImpl extensionContainer);

    MAPErrorMessageSMDeliveryFailure createMAPErrorMessageSMDeliveryFailure(long mapProtocolVersion,
            SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause, byte[] signalInfo,
            MAPExtensionContainerImpl extensionContainer);

    MAPErrorMessageFacilityNotSup createMAPErrorMessageFacilityNotSup(MAPExtensionContainerImpl extensionContainer,
            Boolean shapeOfLocationEstimateNotSupported, Boolean neededLcsCapabilityNotSupportedInServingNode);

    MAPErrorMessageSystemFailure createMAPErrorMessageSystemFailure(long mapVersion, NetworkResource networkResource,
            AdditionalNetworkResource additionalNetworkResource, MAPExtensionContainerImpl extensionContainer);

    MAPErrorMessageUnknownSubscriber createMAPErrorMessageUnknownSubscriber(MAPExtensionContainerImpl extensionContainer,
            UnknownSubscriberDiagnostic unknownSubscriberDiagnostic);

    MAPErrorMessageAbsentSubscriberSM createMAPErrorMessageAbsentSubscriberSM(
            AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM, MAPExtensionContainerImpl extensionContainer,
            AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM);

    MAPErrorMessageAbsentSubscriber createMAPErrorMessageAbsentSubscriber(Boolean mwdSet);

    MAPErrorMessageSubscriberBusyForMtSms createMAPErrorMessageSubscriberBusyForMtSms(
    		MAPExtensionContainerImpl extensionContainer, Boolean gprsConnectionSuspended);

    MAPErrorMessageCallBarred createMAPErrorMessageCallBarred(Long mapVersion, CallBarringCause callBarringCause,
    		MAPExtensionContainerImpl extensionContainer, Boolean unauthorisedMessageOriginator);

    MAPErrorMessageAbsentSubscriber createMAPErrorMessageAbsentSubscriber(MAPExtensionContainerImpl extensionContainer,
            AbsentSubscriberReason absentSubscriberReason);

    MAPErrorMessageUnauthorizedLCSClient createMAPErrorMessageUnauthorizedLCSClient(
            UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic, MAPExtensionContainerImpl extensionContainer);

    MAPErrorMessagePositionMethodFailure createMAPErrorMessagePositionMethodFailure(
            PositionMethodFailureDiagnostic positionMethodFailureDiagnostic, MAPExtensionContainerImpl extensionContainer);

    MAPErrorMessageBusySubscriber createMAPErrorMessageBusySubscriber(MAPExtensionContainerImpl extensionContainer,
            boolean ccbsPossible, boolean ccbsBusy);

    MAPErrorMessageCUGReject createMAPErrorMessageCUGReject(CUGRejectCause cugRejectCause,
    		MAPExtensionContainerImpl extensionContainer);

    MAPErrorMessageRoamingNotAllowed createMAPErrorMessageRoamingNotAllowed(
            RoamingNotAllowedCause roamingNotAllowedCause, MAPExtensionContainerImpl extensionContainer,
            AdditionalRoamingNotAllowedCause additionalRoamingNotAllowedCause);

    MAPErrorMessageSsErrorStatus createMAPErrorMessageSsErrorStatus(int data);

    MAPErrorMessageSsErrorStatus createMAPErrorMessageSsErrorStatus(boolean qBit, boolean pBit, boolean rBit,
            boolean aBit);

    MAPErrorMessageSsIncompatibility createMAPErrorMessageSsIncompatibility(SSCodeImpl ssCode,
            BasicServiceCodeImpl basicService, SSStatusImpl ssStatus);

    MAPErrorMessagePwRegistrationFailure createMAPErrorMessagePwRegistrationFailure(
            PWRegistrationFailureCause pwRegistrationFailureCause);

}
