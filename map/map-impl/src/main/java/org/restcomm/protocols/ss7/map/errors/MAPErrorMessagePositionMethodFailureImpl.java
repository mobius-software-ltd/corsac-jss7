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

import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessagePositionMethodFailure;
import org.restcomm.protocols.ss7.map.api.errors.PositionMethodFailureDiagnostic;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessagePositionMethodFailureImpl extends MAPErrorMessageImpl implements
        MAPErrorMessagePositionMethodFailure {
	
	private ASNPositionMethodFailureDiagnosticImpl positionMethodFailureDiagnostic;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public MAPErrorMessagePositionMethodFailureImpl(PositionMethodFailureDiagnostic positionMethodFailureDiagnostic,
    		MAPExtensionContainer extensionContainer) {
        super((long) MAPErrorCode.positionMethodFailure);

        this.positionMethodFailureDiagnostic = new ASNPositionMethodFailureDiagnosticImpl();
        this.positionMethodFailureDiagnostic.setType(positionMethodFailureDiagnostic);
        this.extensionContainer = extensionContainer;
    }

    public MAPErrorMessagePositionMethodFailureImpl() {
        super((long) MAPErrorCode.positionMethodFailure);
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
    	else {
    		this.positionMethodFailureDiagnostic = new ASNPositionMethodFailureDiagnosticImpl();
    		this.positionMethodFailureDiagnostic.setType(positionMethodFailureDiagnostic);
    	}
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
