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

package org.restcomm.protocols.ss7.cap.errors;

import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorCode;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageCancelFailed;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageParameterless;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageRequestedInfoError;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageTaskRefused;
import org.restcomm.protocols.ss7.cap.api.errors.CancelProblem;
import org.restcomm.protocols.ss7.cap.api.errors.RequestedInfoErrorParameter;
import org.restcomm.protocols.ss7.cap.api.errors.TaskRefusedParameter;
import org.restcomm.protocols.ss7.cap.api.errors.UnavailableNetworkResource;

/**
 * The factory of CAP ReturnError messages
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPErrorMessageFactoryImpl implements CAPErrorMessageFactory {

    @Override
    public CAPErrorMessage createMessageFromErrorCode(Integer errorCode) {
        int ec = (int) (long) errorCode;
        switch (ec) {
            case CAPErrorCode.cancelFailed:
                CAPErrorMessageCancelFailedImpl emCancelFailed = new CAPErrorMessageCancelFailedImpl();
                return emCancelFailed;
            case CAPErrorCode.requestedInfoError:
                CAPErrorMessageRequestedInfoErrorImpl emRequestedInfoError = new CAPErrorMessageRequestedInfoErrorImpl();
                return emRequestedInfoError;
            case CAPErrorCode.systemFailure:
                CAPErrorMessageSystemFailureImpl emSystemFailure = new CAPErrorMessageSystemFailureImpl();
                return emSystemFailure;
            case CAPErrorCode.taskRefused:
                CAPErrorMessageTaskRefusedImpl emTaskRefused = new CAPErrorMessageTaskRefusedImpl();
                return emTaskRefused;
            default:
                return new CAPErrorMessageParameterlessImpl(errorCode);
        }
    }

    @Override
    public CAPErrorMessageParameterless createCAPErrorMessageParameterless(Integer errorCode) {
        return new CAPErrorMessageParameterlessImpl(errorCode);
    }

    @Override
    public CAPErrorMessageCancelFailed createCAPErrorMessageCancelFailed(CancelProblem cancelProblem) {
        return new CAPErrorMessageCancelFailedImpl(cancelProblem);
    }

    @Override
    public CAPErrorMessageRequestedInfoError createCAPErrorMessageRequestedInfoError(
            RequestedInfoErrorParameter requestedInfoErrorParameter) {
        return new CAPErrorMessageRequestedInfoErrorImpl(requestedInfoErrorParameter);
    }

    @Override
    public CAPErrorMessageSystemFailure createCAPErrorMessageSystemFailure(UnavailableNetworkResource unavailableNetworkResource) {
        return new CAPErrorMessageSystemFailureImpl(unavailableNetworkResource);
    }

    @Override
    public CAPErrorMessageTaskRefused createCAPErrorMessageTaskRefused(TaskRefusedParameter taskRefusedParameter) {
        return new CAPErrorMessageTaskRefusedImpl(taskRefusedParameter);
    }
}
