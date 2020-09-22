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

import org.restcomm.protocols.ss7.map.api.errors.ASNCallBaringCauseImpl;
import org.restcomm.protocols.ss7.map.api.errors.CallBarringCause;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageCallBarred;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageCallBarredImpl extends MAPErrorMessageImpl implements MAPErrorMessageCallBarred {
	private long mapProtocolVersion;
    private ASNCallBaringCauseImpl callBarringCause;
    private MAPExtensionContainerImpl extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNNull unauthorisedMessageOriginator;

    public MAPErrorMessageCallBarredImpl(long mapProtocolVersion, CallBarringCause callBarringCause,
    		MAPExtensionContainerImpl extensionContainer, Boolean unauthorisedMessageOriginator) {
        super((long) MAPErrorCode.callBarred);

        this.mapProtocolVersion = mapProtocolVersion;
        if(callBarringCause!=null) {
        	this.callBarringCause = new ASNCallBaringCauseImpl();
        	this.callBarringCause.setType(callBarringCause);
        }
        this.extensionContainer = extensionContainer;
        if(unauthorisedMessageOriginator!=null && unauthorisedMessageOriginator)
        	this.unauthorisedMessageOriginator = new ASNNull();
    }

    public MAPErrorMessageCallBarredImpl() {
        super((long) MAPErrorCode.callBarred);
    }

    public boolean isEmCallBarred() {
        return true;
    }

    public MAPErrorMessageCallBarred getEmCallBarred() {
        return this;
    }

    public CallBarringCause getCallBarringCause() {
    	if(this.callBarringCause==null)
    		return null;
    	
        return this.callBarringCause.getType();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public Boolean getUnauthorisedMessageOriginator() {    	
        return this.unauthorisedMessageOriginator!=null;
    }

    public long getMapProtocolVersion() {
        return this.mapProtocolVersion;
    }

    public void setCallBarringCause(CallBarringCause callBarringCause) {
    	if(callBarringCause==null)
    		this.callBarringCause=null;
    	else {
    		this.callBarringCause = new ASNCallBaringCauseImpl();
    		this.callBarringCause.setType(callBarringCause);
    	}
    }

    public void setExtensionContainer(MAPExtensionContainerImpl extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setUnauthorisedMessageOriginator(Boolean unauthorisedMessageOriginator) {
    	if(unauthorisedMessageOriginator!=null && unauthorisedMessageOriginator)
    		this.unauthorisedMessageOriginator = new ASNNull();
    	else
    		this.unauthorisedMessageOriginator=null;
    }

    public void setMapProtocolVersion(long mapProtocolVersion) {
        this.mapProtocolVersion = mapProtocolVersion;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageCallBarred [");
        if (this.callBarringCause != null)
            sb.append("callBarringCause=" + this.callBarringCause.toString());
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        if (this.unauthorisedMessageOriginator != null)
            sb.append(", unauthorisedMessageOriginator=true");
        sb.append("]");

        return sb.toString();
    }
}
