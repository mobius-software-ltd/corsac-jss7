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

import org.restcomm.protocols.ss7.map.api.errors.ASNAdditionalRoamingNotAllowedCauseImpl;
import org.restcomm.protocols.ss7.map.api.errors.ASNRoamingNotAllowedCauseImpl;
import org.restcomm.protocols.ss7.map.api.errors.AdditionalRoamingNotAllowedCause;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageRoamingNotAllowed;
import org.restcomm.protocols.ss7.map.api.errors.RoamingNotAllowedCause;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageRoamingNotAllowedImpl extends MAPErrorMessageImpl implements MAPErrorMessageRoamingNotAllowed {
	private ASNRoamingNotAllowedCauseImpl roamingNotAllowedCause;
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNAdditionalRoamingNotAllowedCauseImpl additionalRoamingNotAllowedCause;

    public MAPErrorMessageRoamingNotAllowedImpl(RoamingNotAllowedCause roamingNotAllowedCause,
            MAPExtensionContainerImpl extensionContainer, AdditionalRoamingNotAllowedCause additionalRoamingNotAllowedCause) {
        super((long) MAPErrorCode.roamingNotAllowed);

        this.roamingNotAllowedCause =new ASNRoamingNotAllowedCauseImpl();
        this.roamingNotAllowedCause.setType(roamingNotAllowedCause);
        this.extensionContainer = extensionContainer;
        this.additionalRoamingNotAllowedCause =new ASNAdditionalRoamingNotAllowedCauseImpl();
        this.additionalRoamingNotAllowedCause.setType(additionalRoamingNotAllowedCause);
    }

    public MAPErrorMessageRoamingNotAllowedImpl() {
        super((long) MAPErrorCode.roamingNotAllowed);
    }

    public boolean isEmRoamingNotAllowed() {
        return true;
    }

    public MAPErrorMessageRoamingNotAllowed getEmRoamingNotAllowed() {
        return this;
    }

    @Override
    public RoamingNotAllowedCause getRoamingNotAllowedCause() {
    	if(this.roamingNotAllowedCause==null)
    		return null;
    	
        return roamingNotAllowedCause.getType();
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public AdditionalRoamingNotAllowedCause getAdditionalRoamingNotAllowedCause() {
        if(this.additionalRoamingNotAllowedCause==null)
        	return null;
        
    	return additionalRoamingNotAllowedCause.getType();
    }

    @Override
    public void setRoamingNotAllowedCause(RoamingNotAllowedCause val) {
    	if(val==null)
    		roamingNotAllowedCause=null;
    	else {
    		roamingNotAllowedCause = new ASNRoamingNotAllowedCauseImpl();
    		roamingNotAllowedCause.setType(val);
    	}
    }

    @Override
    public void setExtensionContainer(MAPExtensionContainerImpl val) {
        extensionContainer = val;
    }

    @Override
    public void setAdditionalRoamingNotAllowedCause(AdditionalRoamingNotAllowedCause val) {
    	if(val==null)
    		additionalRoamingNotAllowedCause=null;
    	else {
    		additionalRoamingNotAllowedCause = new ASNAdditionalRoamingNotAllowedCauseImpl();
    		additionalRoamingNotAllowedCause.setType(val);
    	}
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("MAPErrorMessageRoamingNotAllowed [");

        if (this.roamingNotAllowedCause != null) {
            sb.append("roamingNotAllowedCause = ");
            sb.append(roamingNotAllowedCause);
        }
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        if (this.additionalRoamingNotAllowedCause != null) {
            sb.append(", additionalRoamingNotAllowedCause = ");
            sb.append(additionalRoamingNotAllowedCause);
        }
        sb.append("]");

        return sb.toString();
    }
}
