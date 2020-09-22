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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.ASNUnknownSubscriberDiagnosticImpl;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageUnknownSubscriber;
import org.restcomm.protocols.ss7.map.api.errors.UnknownSubscriberDiagnostic;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageUnknownSubscriberImpl extends MAPErrorMessageImpl implements MAPErrorMessageUnknownSubscriber {
 	private MAPExtensionContainerImpl extensionContainer;
 	
    private ASNUnknownSubscriberDiagnosticImpl unknownSubscriberDiagnostic;

    public MAPErrorMessageUnknownSubscriberImpl(MAPExtensionContainerImpl extensionContainer,
            UnknownSubscriberDiagnostic unknownSubscriberDiagnostic) {
        super((long) MAPErrorCode.unknownSubscriber);

        this.extensionContainer = extensionContainer;
        this.unknownSubscriberDiagnostic = new ASNUnknownSubscriberDiagnosticImpl();
        this.unknownSubscriberDiagnostic.setType(unknownSubscriberDiagnostic);
    }

    public MAPErrorMessageUnknownSubscriberImpl() {
        super((long) MAPErrorCode.unknownSubscriber);
    }

    public boolean isEmUnknownSubscriber() {
        return true;
    }

    public MAPErrorMessageUnknownSubscriber getEmUnknownSubscriber() {
        return this;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public UnknownSubscriberDiagnostic getUnknownSubscriberDiagnostic() {
    	if(this.unknownSubscriberDiagnostic==null)
    		return null;
    	
        return this.unknownSubscriberDiagnostic.getType();
    }

    public void setExtensionContainer(MAPExtensionContainerImpl extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setUnknownSubscriberDiagnostic(UnknownSubscriberDiagnostic unknownSubscriberDiagnostic) {
    	if(unknownSubscriberDiagnostic==null)
    		this.unknownSubscriberDiagnostic=null;
    	else {
    		this.unknownSubscriberDiagnostic = new ASNUnknownSubscriberDiagnosticImpl();
    		this.unknownSubscriberDiagnostic.setType(unknownSubscriberDiagnostic);
    	}
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageUnknownSubscriber [");
        if (this.extensionContainer != null)
            sb.append("extensionContainer=" + this.extensionContainer.toString());
        if (this.unknownSubscriberDiagnostic != null)
            sb.append(", unknownSubscriberDiagnostic=" + this.unknownSubscriberDiagnostic.toString());
        sb.append("]");

        return sb.toString();
    }
}
