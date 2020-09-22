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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class UUDataImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private UUIndicatorImpl uuIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private UUIImpl uuI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull uusCFInteraction;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;

    public UUDataImpl() {
    }

    public UUDataImpl(UUIndicatorImpl uuIndicator, UUIImpl uuI, boolean uusCFInteraction, MAPExtensionContainerImpl extensionContainer) {
    	this.uuIndicator = uuIndicator;
        this.uuI = uuI;
        
        if(uusCFInteraction)
        	this.uusCFInteraction = new ASNNull();
        
        this.extensionContainer = extensionContainer;
    }

    public UUIndicatorImpl getUUIndicator() {
        return uuIndicator;
    }

    public UUIImpl getUUI() {
        return uuI;
    }

    public boolean getUusCFInteraction() {
        return uusCFInteraction!=null;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("UUData [");

        if (this.uuIndicator != null) {
            sb.append("uuIndicator=");
            sb.append(uuIndicator);
            sb.append(", ");
        }
        if (this.uuI != null) {
            sb.append("uuI=");
            sb.append(uuI);
            sb.append(", ");
        }
        if (this.uusCFInteraction!=null) {
            sb.append("uusCFInteraction");
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
