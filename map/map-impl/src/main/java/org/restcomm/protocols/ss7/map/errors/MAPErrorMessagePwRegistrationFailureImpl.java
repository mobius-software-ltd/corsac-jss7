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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessagePwRegistrationFailure;
import org.restcomm.protocols.ss7.map.api.errors.PWRegistrationFailureCause;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MAPErrorMessagePwRegistrationFailureImpl extends EnumeratedMAPErrorMessage1Impl implements
        MAPErrorMessagePwRegistrationFailure {
	
    public MAPErrorMessagePwRegistrationFailureImpl(PWRegistrationFailureCause pwRegistrationFailureCause) {
        super((long) MAPErrorCode.pwRegistrationFailure);

        if(pwRegistrationFailureCause!=null)
        	setValue(Long.valueOf(pwRegistrationFailureCause.getCode()));
    }

    public MAPErrorMessagePwRegistrationFailureImpl() {
        super((long) MAPErrorCode.pwRegistrationFailure);
    }

    public boolean isEmPwRegistrationFailure() {
        return true;
    }

    public MAPErrorMessagePwRegistrationFailure getEmPwRegistrationFailure() {
        return this;
    }

    @Override
    public PWRegistrationFailureCause getPWRegistrationFailureCause() {
    	Long value=getValue();
    	if(value==null)
    		return null;
    	
    	return PWRegistrationFailureCause.getInstance(value.intValue());
    }

    @Override
    public void setPWRegistrationFailureCause(PWRegistrationFailureCause val) {
    	if(val!=null)
    		setValue(Long.valueOf(val.getCode()));
    	else
    		setValue(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessagePwRegistrationFailure [");
        PWRegistrationFailureCause pwRegistrationFailureCause=getPWRegistrationFailureCause();
        if (pwRegistrationFailureCause != null)
            sb.append("pwRegistrationFailureCause=" + pwRegistrationFailureCause.toString());
        sb.append("]");

        return sb.toString();
    }
}