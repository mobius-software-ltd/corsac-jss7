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
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageRequestedInfoError;
import org.restcomm.protocols.ss7.cap.api.errors.RequestedInfoErrorParameter;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
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
