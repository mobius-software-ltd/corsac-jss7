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
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageUnauthorizedLCSClient;
import org.restcomm.protocols.ss7.map.api.errors.UnauthorizedLCSClientDiagnostic;

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
public class MAPErrorMessageUnauthorizedLCSClientImpl extends MAPErrorMessageImpl implements
        MAPErrorMessageUnauthorizedLCSClient {
 	private ASNUnauthorizedLCSClientDiagnosticImpl unauthorizedLCSClientDiagnostic;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public MAPErrorMessageUnauthorizedLCSClientImpl(UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic,
    		MAPExtensionContainer extensionContainer) {
        super(MAPErrorCode.unauthorizedLCSClient);

        this.unauthorizedLCSClientDiagnostic = new ASNUnauthorizedLCSClientDiagnosticImpl(unauthorizedLCSClientDiagnostic);
        this.extensionContainer = extensionContainer;
    }

    public MAPErrorMessageUnauthorizedLCSClientImpl() {
        super(MAPErrorCode.unauthorizedLCSClient);
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

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public void setUnauthorizedLCSClientDiagnostic(UnauthorizedLCSClientDiagnostic unauthorizedLCSClientDiagnostic) {
        this.unauthorizedLCSClientDiagnostic = new ASNUnauthorizedLCSClientDiagnosticImpl(unauthorizedLCSClientDiagnostic);        
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
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
