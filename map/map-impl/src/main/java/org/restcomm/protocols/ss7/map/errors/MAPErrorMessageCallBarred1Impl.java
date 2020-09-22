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

import org.restcomm.protocols.ss7.map.api.errors.CallBarringCause;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MAPErrorMessageCallBarred1Impl extends EnumeratedMAPErrorMessage1Impl implements
MAPErrorMessageCallBarred {
	private CallBarringCause callBarringCause;
	private long mapProtocolVersion = 2;
    
    public MAPErrorMessageCallBarred1Impl(CallBarringCause callBarringCause) {
        super((long) MAPErrorCode.callBarred);

        this.callBarringCause = callBarringCause;
        if(this.callBarringCause!=null)
        	setValue(Long.valueOf(this.callBarringCause.getCode()));
    }

    public MAPErrorMessageCallBarred1Impl() {
        super((long) MAPErrorCode.callBarred);
    }

    public boolean isEmCallBarred() {
        return true;
    }

    public MAPErrorMessageCallBarred getEmCallBarred() {
        return this;
    }

    @Override
    public CallBarringCause getCallBarringCause() {
    	return callBarringCause;
    }

    @Override
    public void setCallBarringCause(CallBarringCause val) {
    	this.callBarringCause=val;
    	if(val!=null)
    		setValue(Long.valueOf(val.getCode()));
    	else
    		setValue(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageSystemFailure [");

        if (this.callBarringCause != null)
            sb.append("callBarringCause=" + this.callBarringCause.toString());
        sb.append("]");

        return sb.toString();
    }

	@Override
	public long getMapProtocolVersion() {
		return mapProtocolVersion;
	}

	@Override
	public MAPExtensionContainerImpl getExtensionContainer() {
		return null;
	}

	@Override
	public Boolean getUnauthorisedMessageOriginator() {
		return null;
	}

	@Override
	public void setExtensionContainer(MAPExtensionContainerImpl extensionContainer) {				
	}

	@Override
	public void setUnauthorisedMessageOriginator(Boolean unauthorisedMessageOriginator) {		
	}

	@Override
	public void setMapProtocolVersion(long mapProtocolVersion) {
		this.mapProtocolVersion=mapProtocolVersion;
	}
}
