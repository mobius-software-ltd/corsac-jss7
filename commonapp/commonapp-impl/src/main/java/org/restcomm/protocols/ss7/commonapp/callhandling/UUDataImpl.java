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

package org.restcomm.protocols.ss7.commonapp.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.callhandling.UUData;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.UUI;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.UUIndicator;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class UUDataImpl implements UUData {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = UUIndicatorImpl.class)
    private UUIndicator uuIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = UUIImpl.class)
    private UUI uuI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull uusCFInteraction;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public UUDataImpl() {
    }

    public UUDataImpl(UUIndicator uuIndicator, UUI uuI, boolean uusCFInteraction, MAPExtensionContainer extensionContainer) {
    	this.uuIndicator = uuIndicator;
        this.uuI = uuI;
        
        if(uusCFInteraction)
        	this.uusCFInteraction = new ASNNull();
        
        this.extensionContainer = extensionContainer;
    }

    public UUIndicator getUUIndicator() {
        return uuIndicator;
    }

    public UUI getUUI() {
        return uuI;
    }

    public boolean getUusCFInteraction() {
        return uusCFInteraction!=null;
    }

    public MAPExtensionContainer getExtensionContainer() {
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