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

package org.restcomm.protocols.ss7.inap.errors;

import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorCode;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageParameterless;

/**
 * The INAP ReturnError message without any parameters
 *
 * @author yulian.oifa
 *
 */
public class INAPErrorMessageParameterlessImpl extends INAPErrorMessageImpl implements INAPErrorMessageParameterless {
	public INAPErrorMessageParameterlessImpl(Long errorCode) {
        super(errorCode);
    }

    public INAPErrorMessageParameterlessImpl() {
        super(0L);
    }

    @Override
    public boolean isEmParameterless() {
        return true;
    }

    @Override
    public INAPErrorMessageParameterless getEmParameterless() {
        return this;
    }

    @Override
    public String toString() {
        return "INAPErrorMessageParameterless [errorCode=" + errorCode + ":" + inapErrorCodeName() + "]";
    }

    private String inapErrorCodeName() {
        if (errorCode == null)
            return "N/A";
        switch (errorCode.intValue()) {
            case INAPErrorCode.canceled:
                return "canceled";
            case INAPErrorCode.cancelFailed:
                return "cancelFailed";
            case INAPErrorCode.eTCFailed:
                return "eTCFailed";
            case INAPErrorCode.improperCallerResponse:
                return "improperCallerResponse";
            case INAPErrorCode.missingCustomerRecord:
                return "missingCustomerRecord";
            case INAPErrorCode.missingParameter:
                return "missingParameter";
            case INAPErrorCode.parameterOutOfRange:
                return "parameterOutOfRange";
            case INAPErrorCode.requestedInfoError:
                return "requestedInfoError";
            case INAPErrorCode.systemFailure:
                return "systemFailure";
            case INAPErrorCode.taskRefused:
                return "taskRefused";
            case INAPErrorCode.unavailableResource:
                return "unavailableResource";
            case INAPErrorCode.unexpectedComponentSequence:
                return "unexpectedComponentSequence";
            case INAPErrorCode.unexpectedDataValue:
                return "unexpectedDataValue";
            case INAPErrorCode.unexpectedParameter:
                return "unexpectedParameter";
            case INAPErrorCode.unknownLegID:
                return "unknownLegID";
            case INAPErrorCode.unknownResource:
                return "unknownResource";
            default:
                return errorCode.toString();
        }
    }
}
