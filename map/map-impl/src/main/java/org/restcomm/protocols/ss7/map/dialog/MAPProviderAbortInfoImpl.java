/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.dialog.ASNMAPProviderAbortReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPProviderAbortReason;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 MAP-ProviderAbortInfo ::= SEQUENCE { map-ProviderAbortReason MAP-ProviderAbortReason, ..., extensionContainer
 * ExtensionContainer OPTIONAL -- extensionContainer must not be used in version 2 }
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x05,constructed=true,lengthIndefinite=false)
public class MAPProviderAbortInfoImpl {
	private ASNMAPProviderAbortReason mapProviderAbortReason = null;
    private MAPExtensionContainerImpl extensionContainer;

    public MAPProviderAbortInfoImpl() {
    }

    public MAPProviderAbortReason getMAPProviderAbortReason() {
    	if(mapProviderAbortReason==null)
    		return null;
    	
        return this.mapProviderAbortReason.getType();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    public void setMAPProviderAbortReason(MAPProviderAbortReason mapProvAbrtReas) {
        this.mapProviderAbortReason = new ASNMAPProviderAbortReason();
        this.mapProviderAbortReason.setType(mapProvAbrtReas);
    }

    public void setExtensionContainer(MAPExtensionContainerImpl extensionContainer) {
        this.extensionContainer = extensionContainer;
    }
}
