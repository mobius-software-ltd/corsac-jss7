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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EMLPPInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author daniel bichara
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class EMLPPInfoImpl implements EMLPPInfo {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=0)
	private ASNInteger maximumentitledPriority = null;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=1)
	private ASNInteger defaultPriority = null;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer = null;

    public EMLPPInfoImpl() {
    }

    /**
     *
     */
    public EMLPPInfoImpl(int maximumentitledPriority, int defaultPriority, MAPExtensionContainer extensionContainer) {
        this.maximumentitledPriority = new ASNInteger(maximumentitledPriority,"MaximumentitledPriority",0,15,false);
        this.defaultPriority = new ASNInteger(defaultPriority,"DefaultPriority",0,15,false);
        this.extensionContainer = extensionContainer;
    }

    public int getMaximumentitledPriority() {
    	if(this.maximumentitledPriority==null || this.maximumentitledPriority.getValue()==null)
    		return 0;
    	
        return this.maximumentitledPriority.getIntValue();
    }

    public int getDefaultPriority() {
    	if(this.defaultPriority==null || this.defaultPriority.getValue()==null)
    		return 0;
    	
        return this.defaultPriority.getIntValue();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EMLPPInfo [");

        if(this.maximumentitledPriority!=null) {
	        sb.append("maximumentitledPriority=");
	        sb.append(this.maximumentitledPriority.getValue());
	        sb.append(", ");
        }
        
        if(this.defaultPriority!=null) {
	        sb.append("defaultPriority=");
	        sb.append(this.defaultPriority.getValue());
	        sb.append(", ");
        }
        
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(maximumentitledPriority==null)
			throw new ASNParsingComponentException("maximum entitled priority should be set for emlpp info", ASNParsingComponentExceptionReason.MistypedParameter);

		if(defaultPriority==null)
			throw new ASNParsingComponentException("default priority should be set for emlpp info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
