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

import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageCancelFailed;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageParameterless;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageRequestedInfoError;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageTaskRefused;

/**
 * Base class of CAP ReturnError messages
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public abstract class CAPErrorMessageImpl implements CAPErrorMessage {
    protected Integer errorCode;

    protected CAPErrorMessageImpl(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public CAPErrorMessageImpl() {
    }

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public boolean isEmParameterless() {
        return false;
    }

    @Override
    public boolean isEmCancelFailed() {
        return false;
    }

    @Override
    public boolean isEmRequestedInfoError() {
        return false;
    }

    @Override
    public boolean isEmSystemFailure() {
        return false;
    }

    @Override
    public boolean isEmTaskRefused() {
        return false;
    }

    @Override
    public CAPErrorMessageParameterless getEmParameterless() {
        return null;
    }

    @Override
    public CAPErrorMessageCancelFailed getEmCancelFailed() {
        return null;
    }

    @Override
    public CAPErrorMessageRequestedInfoError getEmRequestedInfoError() {
        return null;
    }

    @Override
    public CAPErrorMessageSystemFailure getEmSystemFailure() {
        return null;
    }

    @Override
    public CAPErrorMessageTaskRefused getEmTaskRefused() {
        return null;
    }
}
