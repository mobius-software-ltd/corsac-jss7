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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.errors.AdditionalRoamingNotAllowedCause;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageRoamingNotAllowed;
import org.restcomm.protocols.ss7.map.api.errors.RoamingNotAllowedCause;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 * @author yulianoifa
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageRoamingNotAllowedImpl extends MAPErrorMessageImpl implements MAPErrorMessageRoamingNotAllowed {
	private ASNRoamingNotAllowedCauseImpl roamingNotAllowedCause;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNAdditionalRoamingNotAllowedCauseImpl additionalRoamingNotAllowedCause;

    public MAPErrorMessageRoamingNotAllowedImpl(RoamingNotAllowedCause roamingNotAllowedCause,
    		MAPExtensionContainer extensionContainer, AdditionalRoamingNotAllowedCause additionalRoamingNotAllowedCause) {
        super(MAPErrorCode.roamingNotAllowed);

        this.roamingNotAllowedCause =new ASNRoamingNotAllowedCauseImpl(roamingNotAllowedCause);
        this.extensionContainer = extensionContainer;
        
        if(additionalRoamingNotAllowedCause!=null)
        	this.additionalRoamingNotAllowedCause =new ASNAdditionalRoamingNotAllowedCauseImpl(additionalRoamingNotAllowedCause);        
    }

    public MAPErrorMessageRoamingNotAllowedImpl() {
        super(MAPErrorCode.roamingNotAllowed);
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
    public MAPExtensionContainer getExtensionContainer() {
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
    	else
    		roamingNotAllowedCause = new ASNRoamingNotAllowedCauseImpl(val);     	
    }

    @Override
    public void setExtensionContainer(MAPExtensionContainer val) {
        extensionContainer = val;
    }

    @Override
    public void setAdditionalRoamingNotAllowedCause(AdditionalRoamingNotAllowedCause val) {
    	if(val==null)
    		additionalRoamingNotAllowedCause=null;
    	else
    		additionalRoamingNotAllowedCause = new ASNAdditionalRoamingNotAllowedCauseImpl(val);    		
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
