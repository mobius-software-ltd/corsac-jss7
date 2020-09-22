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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.map.api.errors.ASNUnauthorizedLCSClientDiagnosticImpl;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageUnauthorizedLCSClient;
import org.restcomm.protocols.ss7.map.api.errors.UnauthorizedLCSClientDiagnostic;
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
public class MAPErrorMessageUnauthorizedLCSClientImpl extends MAPErrorMessageImpl implements
        MAPErrorMessageUnauthorizedLCSClient {
 	private ASNUnauthorizedLCSClientDiagnosticImpl unauthorizedLCSClientDiagnostic;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private MAPExtensionContainerImpl extensionContainer;

    public MAPErrorMessageUnauthorizedLCSClientImpl(UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic,
            MAPExtensionContainerImpl extensionContainer) {
        super((long) MAPErrorCode.unauthorizedLCSClient);

        this.unauthorizedLCSClientDiagnostic = new ASNUnauthorizedLCSClientDiagnosticImpl();
        this.unauthorizedLCSClientDiagnostic.setType(unauthorizedLCSClientDiagnostic);
        this.extensionContainer = extensionContainer;
    }

    public MAPErrorMessageUnauthorizedLCSClientImpl() {
        super((long) MAPErrorCode.unauthorizedLCSClient);
    }

    public boolean isEmUnauthorizedLCSClient() {
        return true;
    }

    public MAPErrorMessageUnauthorizedLCSClient getEmUnauthorizedLCSClient() {
        return this;
    }

    public UnauthorizedLCSClientDiagnostic getUnauthorizedLCSClientDiagnostic() {
    	if(this.unauthorizedLCSClientDiagnostic==null)
    		return null;
    	
        return this.unauthorizedLCSClientDiagnostic.getType();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public void setUnauthorizedLCSClientDiagnostic(UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic) {
        this.unauthorizedLCSClientDiagnostic = new ASNUnauthorizedLCSClientDiagnosticImpl();
        this.unauthorizedLCSClientDiagnostic.setType(unauthorizedLCSClientDiagnostic);
    }

    public void setExtensionContainer(MAPExtensionContainerImpl extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageUnauthorizedLCSClient [");
        if (this.unauthorizedLCSClientDiagnostic != null)
            sb.append("unauthorizedLCSClientDiagnostic=" + this.unauthorizedLCSClientDiagnostic.toString());
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        sb.append("]");

        return sb.toString();
    }
}
