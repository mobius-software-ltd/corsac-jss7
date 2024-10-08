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

package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.dialog.MAPProviderAbortReason;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 MAP-ProviderAbortInfo ::= SEQUENCE { map-ProviderAbortReason MAP-ProviderAbortReason, ..., extensionContainer
 * ExtensionContainer OPTIONAL -- extensionContainer must not be used in version 2 }
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x05,constructed=true,lengthIndefinite=false)
public class MAPProviderAbortInfoImpl {
	private ASNMAPProviderAbortReason mapProviderAbortReason = null;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;

    public MAPProviderAbortInfoImpl() {
    }

    public MAPProviderAbortReason getMAPProviderAbortReason() {
    	if(mapProviderAbortReason==null)
    		return null;
    	
        return this.mapProviderAbortReason.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setMAPProviderAbortReason(MAPProviderAbortReason mapProvAbrtReas) {
        this.mapProviderAbortReason = new ASNMAPProviderAbortReason(mapProvAbrtReas);        
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }
}
