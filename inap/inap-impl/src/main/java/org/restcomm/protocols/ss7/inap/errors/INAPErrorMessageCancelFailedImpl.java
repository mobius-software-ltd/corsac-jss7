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

import org.restcomm.protocols.ss7.inap.api.errors.CancelProblem;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorCode;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageCancelFailed;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPErrorMessageCancelFailedImpl extends EnumeratedINAPErrorMessage1Impl implements INAPErrorMessageCancelFailed {
	protected INAPErrorMessageCancelFailedImpl(CancelProblem cancelProblem) {
        super(INAPErrorCode.cancelFailed,"CancelFailed",0,2);
        
        if(cancelProblem!=null)
        	setValue(cancelProblem.getCode());        
    }

    public INAPErrorMessageCancelFailedImpl() {
        super(INAPErrorCode.cancelFailed,"CancelFailed",0,2);
    }

    @Override
    public boolean isEmCancelFailed() {
        return true;
    }

    @Override
    public INAPErrorMessageCancelFailed getEmCancelFailed() {
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
        sb.append("INAPErrorMessageCancelFailed [");
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
