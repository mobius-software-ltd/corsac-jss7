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

package org.restcomm.protocols.ss7.cap.errors;

import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorCode;
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageRequestedInfoError;
import org.restcomm.protocols.ss7.cap.api.errors.RequestedInfoErrorParameter;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPErrorMessageRequestedInfoErrorImpl extends EnumeratedСAPErrorMessage1Impl implements CAPErrorMessageRequestedInfoError {
	protected CAPErrorMessageRequestedInfoErrorImpl(RequestedInfoErrorParameter requestedInfoErrorParameter) {
        super(CAPErrorCode.requestedInfoError,"CAPErrorMessageRequestedInfoError",1,2);

        if(requestedInfoErrorParameter!=null)
        	setValue(requestedInfoErrorParameter.getCode());        
    }

    public CAPErrorMessageRequestedInfoErrorImpl() {
        super(CAPErrorCode.requestedInfoError,"CAPErrorMessageRequestedInfoError",1,2);
    }

    public boolean isEmRequestedInfoError() {
        return true;
    }

    public CAPErrorMessageRequestedInfoError getEmRequestedInfoError() {
        return this;
    }

    public RequestedInfoErrorParameter getRequestedInfoErrorParameter() {
    	Integer value=getValue();
    	if(value==null)
    		return null;
    	
    	return RequestedInfoErrorParameter.getInstance(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CAPErrorMessageRequestedInfoError [");
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
