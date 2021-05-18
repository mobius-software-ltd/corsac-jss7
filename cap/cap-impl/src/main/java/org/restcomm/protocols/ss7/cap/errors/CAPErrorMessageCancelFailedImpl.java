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
import org.restcomm.protocols.ss7.cap.api.errors.CAPErrorMessageCancelFailed;
import org.restcomm.protocols.ss7.cap.api.errors.CancelProblem;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPErrorMessageCancelFailedImpl extends EnumeratedСAPErrorMessage1Impl implements CAPErrorMessageCancelFailed {
	protected CAPErrorMessageCancelFailedImpl(CancelProblem cancelProblem) {
        super((long) CAPErrorCode.cancelFailed);
        
        if(cancelProblem!=null)
        	setValue(Long.valueOf(cancelProblem.getCode()));        
    }

    public CAPErrorMessageCancelFailedImpl() {
        super((long) CAPErrorCode.cancelFailed);
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
    	Long value=getValue();
    	if(value==null)
    		return null;
    	
    	return CancelProblem.getInstance(value.intValue());
    }

    public void setCancelProblem(CancelProblem cancelProblem) {
    	if(cancelProblem!=null)
        	setValue(Long.valueOf(cancelProblem.getCode()));     
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
