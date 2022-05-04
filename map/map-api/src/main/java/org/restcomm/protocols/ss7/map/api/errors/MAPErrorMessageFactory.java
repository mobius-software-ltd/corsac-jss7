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

package org.restcomm.protocols.ss7.map.api.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.api.primitives.SignalInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;

/**
 * The factory of MAP ReturnError messages
 *
 * @author sergey vetyutnev
 * @author yulianoifa
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
    MAPErrorMessage createMessageFromErrorCode(Integer errorCode,Long mapVersion);

    MAPErrorMessageParameterless createMAPErrorMessageParameterless(Integer errorCode);

    MAPErrorMessageExtensionContainer createMAPErrorMessageExtensionContainer(Integer errorCode,
    		MAPExtensionContainer extensionContainer);

    MAPErrorMessageSMDeliveryFailure createMAPErrorMessageSMDeliveryFailure(SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause);

    MAPErrorMessageSMDeliveryFailure createMAPErrorMessageSMDeliveryFailure(SMEnumeratedDeliveryFailureCause smEnumeratedDeliveryFailureCause, SignalInfo signalInfo,
            MAPExtensionContainer extensionContainer);

    MAPErrorMessageFacilityNotSup createMAPErrorMessageFacilityNotSup(MAPExtensionContainer extensionContainer,
            Boolean shapeOfLocationEstimateNotSupported, Boolean neededLcsCapabilityNotSupportedInServingNode);

    MAPErrorMessageSystemFailure createMAPErrorMessageSystemFailure(NetworkResource networkResource);

    MAPErrorMessageSystemFailure createMAPErrorMessageSystemFailure(NetworkResource networkResource,
            AdditionalNetworkResource additionalNetworkResource, MAPExtensionContainer extensionContainer);

    MAPErrorMessageUnknownSubscriber createMAPErrorMessageUnknownSubscriber(MAPExtensionContainer extensionContainer,
            UnknownSubscriberDiagnostic unknownSubscriberDiagnostic);

    MAPErrorMessageAbsentSubscriberSM createMAPErrorMessageAbsentSubscriberSM(
            AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM, MAPExtensionContainer extensionContainer,
            AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM);

    MAPErrorMessageAbsentSubscriber createMAPErrorMessageAbsentSubscriber(Boolean mwdSet);

    MAPErrorMessageSubscriberBusyForMtSms createMAPErrorMessageSubscriberBusyForMtSms(
    		MAPExtensionContainer extensionContainer, Boolean gprsConnectionSuspended);

    MAPErrorMessageCallBarred createMAPErrorMessageCallBarred(CallBarringCause callBarringCause);

    MAPErrorMessageCallBarred createMAPErrorMessageCallBarred(CallBarringCause callBarringCause,
    		MAPExtensionContainer extensionContainer, Boolean unauthorisedMessageOriginator);

    MAPErrorMessageAbsentSubscriber createMAPErrorMessageAbsentSubscriber(MAPExtensionContainer extensionContainer,
            AbsentSubscriberReason absentSubscriberReason);

    MAPErrorMessageUnauthorizedLCSClient createMAPErrorMessageUnauthorizedLCSClient(
            UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic, MAPExtensionContainer extensionContainer);

    MAPErrorMessagePositionMethodFailure createMAPErrorMessagePositionMethodFailure(
            PositionMethodFailureDiagnostic positionMethodFailureDiagnostic, MAPExtensionContainer extensionContainer);

    MAPErrorMessageBusySubscriber createMAPErrorMessageBusySubscriber(MAPExtensionContainer extensionContainer,
            boolean ccbsPossible, boolean ccbsBusy);

    MAPErrorMessageCUGReject createMAPErrorMessageCUGReject(CUGRejectCause cugRejectCause,
    		MAPExtensionContainer extensionContainer);

    MAPErrorMessageRoamingNotAllowed createMAPErrorMessageRoamingNotAllowed(
            RoamingNotAllowedCause roamingNotAllowedCause, MAPExtensionContainer extensionContainer,
            AdditionalRoamingNotAllowedCause additionalRoamingNotAllowedCause);

    MAPErrorMessageSsErrorStatus createMAPErrorMessageSsErrorStatus(int data);

    MAPErrorMessageSsErrorStatus createMAPErrorMessageSsErrorStatus(boolean qBit, boolean pBit, boolean rBit,
            boolean aBit);

    MAPErrorMessageSsIncompatibility createMAPErrorMessageSsIncompatibility(SSCode ssCode,
            BasicServiceCode basicService, SSStatus ssStatus);

    MAPErrorMessagePwRegistrationFailure createMAPErrorMessagePwRegistrationFailure(
            PWRegistrationFailureCause pwRegistrationFailureCause);
}