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
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageRequestedInfoError;
import org.restcomm.protocols.ss7.inap.api.errors.RequestedInfoErrorParameter;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPErrorMessageRequestedInfoErrorImpl extends EnumeratedINAPErrorMessage1Impl implements INAPErrorMessageRequestedInfoError {
	protected INAPErrorMessageRequestedInfoErrorImpl(RequestedInfoErrorParameter requestedInfoErrorParameter) {
        super((long) INAPErrorCode.requestedInfoError);

        if(requestedInfoErrorParameter!=null)
        	setValue(Long.valueOf(requestedInfoErrorParameter.getCode()));        
    }

    public INAPErrorMessageRequestedInfoErrorImpl() {
        super((long) INAPErrorCode.requestedInfoError);
    }

    public boolean isEmRequestedInfoError() {
        return true;
    }

    public INAPErrorMessageRequestedInfoError getEmRequestedInfoError() {
        return this;
    }

    public RequestedInfoErrorParameter getRequestedInfoErrorParameter() {
    	Long value=getValue();
    	if(value==null)
    		return null;
    	
    	return RequestedInfoErrorParameter.getInstance(value.intValue());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INAPErrorMessageRequestedInfoError [");
        RequestedInfoErrorParameter requestedInfoErrorParameter=getRequestedInfoErrorParameter();
        if (requestedInfoErrorParameter != null) {
            sb.append("requestedInfoErrorParameter=");
            sb.append(requestedInfoErrorParameter);
            sb.append(",");
        }
        sb.append("]");

        return sb.toString();
    }
}
