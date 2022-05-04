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
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageCancelFailed;
import org.restcomm.protocols.ss7.cap.api.errors.CancelProblem;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPErrorMessageCancelFailedImpl extends EnumeratedСAPErrorMessage1Impl implements CAPErrorMessageCancelFailed {
	protected CAPErrorMessageCancelFailedImpl(CancelProblem cancelProblem) {
        super(CAPErrorCode.cancelFailed,"CAPErrorMessageCancelFailed",0,2);
        
        if(cancelProblem!=null)
        	setValue(cancelProblem.getCode());        
    }

    public CAPErrorMessageCancelFailedImpl() {
        super(CAPErrorCode.cancelFailed,"CAPErrorMessageCancelFailed",0,2);
    }

    @Override
    public boolean isEmCancelFailed() {
        return true;
    }

    @Override
    public CAPErrorMessageCancelFailed getEmCancelFailed() {
        return this;
    }

    @Override
    public CancelProblem getCancelProblem() {
    	Integer value=getValue();
    	if(value==null)
    		return null;
    	
    	return CancelProblem.getInstance(value);
    }

    public void setCancelProblem(CancelProblem cancelProblem) {
    	if(cancelProblem!=null)
        	setValue(cancelProblem.getCode());
    	else
    		setValue(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CAPErrorMessageCancelFailed [");
        CancelProblem cancelProblem=getCancelProblem();
        if (cancelProblem != null) {
            sb.append("cancelProblem=");
            sb.append(cancelProblem);
            sb.append(",");
        }
        sb.append("]");

        return sb.toString();
    }
}
