/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import org.restcomm.protocols.ss7.inap.api.errors.CancelProblem;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorCode;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessage;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageCancelFailed;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageFactory;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageImproperCallerResponseCS1Plus;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageOctetString;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageParameterless;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageRequestedInfoError;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageTaskRefused;
import org.restcomm.protocols.ss7.inap.api.errors.ImproperCallerResponseParameter;
import org.restcomm.protocols.ss7.inap.api.errors.RequestedInfoErrorParameter;
import org.restcomm.protocols.ss7.inap.api.errors.TaskRefusedParameter;
import org.restcomm.protocols.ss7.inap.api.errors.UnavailableNetworkResource;

/**
 * The factory of INAP ReturnError messages
 *
 * @author yulian.oifa
 *
 */
public class INAPErrorMessageFactoryImpl implements INAPErrorMessageFactory {

    @Override
    public INAPErrorMessage createMessageFromErrorCode(Long errorCode,Boolean isCS1Plus) {
    	if(isCS1Plus==null || !isCS1Plus) {
	        int ec = (int) (long) errorCode;
	        switch (ec) {
	            case INAPErrorCode.cancelFailed:
	                INAPErrorMessageCancelFailedImpl emCancelFailed = new INAPErrorMessageCancelFailedImpl();
	                return emCancelFailed;
	            case INAPErrorCode.requestedInfoError:
	                INAPErrorMessageRequestedInfoErrorImpl emRequestedInfoError = new INAPErrorMessageRequestedInfoErrorImpl();
	                return emRequestedInfoError;
	            case INAPErrorCode.systemFailure:
	                INAPErrorMessageSystemFailureImpl emSystemFailure = new INAPErrorMessageSystemFailureImpl();
	                return emSystemFailure;
	            case INAPErrorCode.taskRefused:
	                INAPErrorMessageTaskRefusedImpl emTaskRefused = new INAPErrorMessageTaskRefusedImpl();
	                return emTaskRefused;
	            default:
	                return new INAPErrorMessageParameterlessImpl(errorCode);
	        }
    	}
    	else {
    		int ec = (int) (long) errorCode;
 	        switch (ec) {
	 	        case INAPErrorCode.cancelFailed:
	                INAPErrorMessageCancelFailedImpl emCancelFailed = new INAPErrorMessageCancelFailedImpl();
	                return emCancelFailed;
	 	        case INAPErrorCode.systemFailure:
	                INAPErrorMessageSystemFailureImpl emSystemFailure = new INAPErrorMessageSystemFailureImpl();
	                return emSystemFailure;
	            //overalps with executionError
	 	        //case INAPErrorCode.improperCallerResponse:
	 	        //	INAPErrorMessageImproperCallerResponseCS1PlusImpl emImproperCallerResponse = new INAPErrorMessageImproperCallerResponseCS1PlusImpl();
	 	        //	return emImproperCallerResponse;
	 	        case INAPErrorCode.congestion:
	 	        case INAPErrorCode.errorInParameterValue:
	 	        case INAPErrorCode.executionError:
	 	        case INAPErrorCode.illegalCombinationOfParameters:
	 	        case INAPErrorCode.infoNotAvailable:
	 	        case INAPErrorCode.invalidDataItemID:
	 	        case INAPErrorCode.notAuthorized:
	 	        case INAPErrorCode.parameterMissing:
	 	        case INAPErrorCode.otherError:
	 	        	INAPErrorMessageOctetStringImpl emOctetString = new INAPErrorMessageOctetStringImpl();
	 	        	return emOctetString;	
	 	       default:
	                return new INAPErrorMessageParameterlessImpl(errorCode);
 	        }
    	}
    }

    @Override
    public INAPErrorMessageParameterless createINAPErrorMessageParameterless(Long errorCode) {
        return new INAPErrorMessageParameterlessImpl(errorCode);
    }

    @Override
    public INAPErrorMessageCancelFailed createINAPErrorMessageCancelFailed(CancelProblem cancelProblem) {
        return new INAPErrorMessageCancelFailedImpl(cancelProblem);
    }

    @Override
    public INAPErrorMessageRequestedInfoError createINAPErrorMessageRequestedInfoError(
            RequestedInfoErrorParameter requestedInfoErrorParameter) {
        return new INAPErrorMessageRequestedInfoErrorImpl(requestedInfoErrorParameter);
    }

    @Override
    public INAPErrorMessageSystemFailure createINAPErrorMessageSystemFailure(UnavailableNetworkResource unavailableNetworkResource) {
        return new INAPErrorMessageSystemFailureImpl(unavailableNetworkResource);
    }

    @Override
    public INAPErrorMessageTaskRefused createINAPErrorMessageTaskRefused(TaskRefusedParameter taskRefusedParameter) {
        return new INAPErrorMessageTaskRefusedImpl(taskRefusedParameter);
    }

    @Override
    public INAPErrorMessageImproperCallerResponseCS1Plus createINAPErrorMessageImproperCallerResponseCS1Plus(ImproperCallerResponseParameter improperCallerResponseParameter) {
        return new INAPErrorMessageImproperCallerResponseCS1PlusImpl(improperCallerResponseParameter);
    }

	@Override
	public INAPErrorMessageOctetString createINAPErrorMessageOctetString(Long errorCode, byte[] parameter) {
		INAPErrorMessageOctetStringImpl result = new INAPErrorMessageOctetStringImpl(errorCode);
		if(parameter!=null)
			result.setValue(parameter);
		
		return result;
	}
}