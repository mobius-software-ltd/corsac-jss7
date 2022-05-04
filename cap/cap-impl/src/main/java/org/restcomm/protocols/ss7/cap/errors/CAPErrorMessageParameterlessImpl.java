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
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageParameterless;

/**
 * The CAP ReturnError message without any parameters
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPErrorMessageParameterlessImpl extends CAPErrorMessageImpl implements CAPErrorMessageParameterless {
	public CAPErrorMessageParameterlessImpl(Integer errorCode) {
        super(errorCode);
    }

    public CAPErrorMessageParameterlessImpl() {
        super(0);
    }

    @Override
    public boolean isEmParameterless() {
        return true;
    }

    @Override
    public CAPErrorMessageParameterless getEmParameterless() {
        return this;
    }

    @Override
    public String toString() {
        return "CAPErrorMessageParameterless [errorCode=" + errorCode + ":" + capErrorCodeName() + "]";
    }

    private String capErrorCodeName() {
        if (errorCode == null)
            return "N/A";
        switch (errorCode.intValue()) {
            case CAPErrorCode.canceled:
                return "canceled";
            case CAPErrorCode.cancelFailed:
                return "cancelFailed";
            case CAPErrorCode.eTCFailed:
                return "eTCFailed";
            case CAPErrorCode.improperCallerResponse:
                return "improperCallerResponse";
            case CAPErrorCode.missingCustomerRecord:
                return "missingCustomerRecord";
            case CAPErrorCode.missingParameter:
                return "missingParameter";
            case CAPErrorCode.parameterOutOfRange:
                return "parameterOutOfRange";
            case CAPErrorCode.requestedInfoError:
                return "requestedInfoError";
            case CAPErrorCode.systemFailure:
                return "systemFailure";
            case CAPErrorCode.taskRefused:
                return "taskRefused";
            case CAPErrorCode.unavailableResource:
                return "unavailableResource";
            case CAPErrorCode.unexpectedComponentSequence:
                return "unexpectedComponentSequence";
            case CAPErrorCode.unexpectedDataValue:
                return "unexpectedDataValue";
            case CAPErrorCode.unexpectedParameter:
                return "unexpectedParameter";
            case CAPErrorCode.unknownCSID:
                return "unknownCSID";
            case CAPErrorCode.unknownLegID:
                return "unknownLegID";
            case CAPErrorCode.unknownPDPID:
                return "unknownPDPID";
            default:
                return errorCode.toString();
        }
    }
}
