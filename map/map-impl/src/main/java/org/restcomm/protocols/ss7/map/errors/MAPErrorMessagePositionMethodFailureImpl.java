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

package org.restcomm.protocols.ss7.map.errors;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessagePositionMethodFailure;
import org.restcomm.protocols.ss7.map.api.errors.PositionMethodFailureDiagnostic;

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
public class MAPErrorMessagePositionMethodFailureImpl extends MAPErrorMessageImpl implements
        MAPErrorMessagePositionMethodFailure {
	
	private ASNPositionMethodFailureDiagnosticImpl positionMethodFailureDiagnostic;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public MAPErrorMessagePositionMethodFailureImpl(PositionMethodFailureDiagnostic positionMethodFailureDiagnostic,
    		MAPExtensionContainer extensionContainer) {
        super(MAPErrorCode.positionMethodFailure);

        this.positionMethodFailureDiagnostic = new ASNPositionMethodFailureDiagnosticImpl(positionMethodFailureDiagnostic);
        this.extensionContainer = extensionContainer;
    }

    public MAPErrorMessagePositionMethodFailureImpl() {
        super(MAPErrorCode.positionMethodFailure);
    }

    public boolean isEmPositionMethodFailure() {
        return true;
    }

    public MAPErrorMessagePositionMethodFailure getEmPositionMethodFailure() {
        return this;
    }

    public PositionMethodFailureDiagnostic getPositionMethodFailureDiagnostic() {
    	if(this.positionMethodFailureDiagnostic==null)
    		return null;
    	
        return this.positionMethodFailureDiagnostic.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public void setPositionMethodFailureDiagnostic(PositionMethodFailureDiagnostic positionMethodFailureDiagnostic) {
    	if(positionMethodFailureDiagnostic==null)
    		this.positionMethodFailureDiagnostic=null;
    	else
    		this.positionMethodFailureDiagnostic = new ASNPositionMethodFailureDiagnosticImpl(positionMethodFailureDiagnostic);    	
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessagePositionMethodFailure [");
        if (this.positionMethodFailureDiagnostic != null)
            sb.append("positionMethodFailureDiagnostic=" + this.positionMethodFailureDiagnostic.toString());
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        sb.append("]");

        return sb.toString();
    }
}
