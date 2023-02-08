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

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.errors.CallBarringCause;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 */
public class MAPErrorMessageCallBarred1Impl extends EnumeratedMAPErrorMessage1Impl implements MAPErrorMessageCallBarred {
	public MAPErrorMessageCallBarred1Impl(CallBarringCause callBarringCause) {
        super(MAPErrorCode.callBarred,"CallBarred",0,1);

        if(callBarringCause!=null)
        	setValue(callBarringCause.getCode());
    }

    public MAPErrorMessageCallBarred1Impl() {
        super(MAPErrorCode.callBarred,"CallBarred",0,1);
    }

    public boolean isEmCallBarred() {
        return true;
    }

    public MAPErrorMessageCallBarred getEmCallBarred() {
        return this;
    }

    @Override
    public CallBarringCause getCallBarringCause() {
    	Integer value=getValue();
    	if(value==null)
    		return null;
    	
    	return CallBarringCause.getInstance(value.intValue());
    }

    @Override
    public void setCallBarringCause(CallBarringCause val) {
    	if(val!=null)
    		setValue(val.getCode());
    	else
    		setValue(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageSystemFailure [");
        CallBarringCause callBarringCause = getCallBarringCause();
        if (callBarringCause != null)
            sb.append("callBarringCause=" + callBarringCause.toString());
        sb.append("]");

        return sb.toString();
    }

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return null;
	}

	@Override
	public Boolean getUnauthorisedMessageOriginator() {
		return null;
	}

	@Override
	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {				
	}

	@Override
	public void setUnauthorisedMessageOriginator(Boolean unauthorisedMessageOriginator) {		
	}
}
