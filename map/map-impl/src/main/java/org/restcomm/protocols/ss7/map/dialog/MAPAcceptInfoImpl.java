/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * map-accept [1] IMPLICIT SEQUENCE { ... , extensionContainer SEQUENCE { privateExtensionList [0] IMPLICIT SEQUENCE SIZE (1 ..
 * SEQUENCE { extId MAP-EXTENSION .&extensionId ( { , ...} ) , extType MAP-EXTENSION .&ExtensionType ( { , ...} { @extId } )
 * OPTIONAL} OPTIONAL, pcs-Extensions [1] IMPLICIT SEQUENCE { ... } OPTIONAL, ... } OPTIONAL},
 *
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x01,constructed=false,lengthIndefinite=false)
public class MAPAcceptInfoImpl {
	private MAPExtensionContainerImpl extensionContainer;

	public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(MAPExtensionContainerImpl extensionContainer) {
        this.extensionContainer = extensionContainer;
    }
}