/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessagePwRegistrationFailure;
import org.restcomm.protocols.ss7.map.api.errors.PWRegistrationFailureCause;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 */
public class MAPErrorMessagePwRegistrationFailureImpl extends EnumeratedMAPErrorMessage1Impl implements
        MAPErrorMessagePwRegistrationFailure {
	
    public MAPErrorMessagePwRegistrationFailureImpl(PWRegistrationFailureCause pwRegistrationFailureCause) {
        super(MAPErrorCode.pwRegistrationFailure,"PWRegistrationFailure",0,2);

        if(pwRegistrationFailureCause!=null)
        	setValue(pwRegistrationFailureCause.getCode());
    }

    public MAPErrorMessagePwRegistrationFailureImpl() {
        super(MAPErrorCode.pwRegistrationFailure,"PWRegistrationFailure",0,2);
    }

    public boolean isEmPwRegistrationFailure() {
        return true;
    }

    public MAPErrorMessagePwRegistrationFailure getEmPwRegistrationFailure() {
        return this;
    }

    @Override
    public PWRegistrationFailureCause getPWRegistrationFailureCause() {
    	Integer value=getValue();
    	if(value==null)
    		return null;
    	
    	return PWRegistrationFailureCause.getInstance(value.intValue());
    }

    @Override
    public void setPWRegistrationFailureCause(PWRegistrationFailureCause val) {
    	if(val!=null)
    		setValue(val.getCode());
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