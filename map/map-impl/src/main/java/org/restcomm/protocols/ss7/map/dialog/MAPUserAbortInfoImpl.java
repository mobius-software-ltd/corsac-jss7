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

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.restcomm.protocols.ss7.map.api.dialog.ResourceUnavailableReason;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * MAP-UserAbortInfo ::= SEQUENCE { map-UserAbortChoice MAP-UserAbortChoice, ... extensionContainer ExtensionContainer OPTIONAL
 * }
 *
 * MAP-UserAbortChoice ::= CHOICE { userSpecificReason [0] NULL, userResourceLimitation [1] NULL, resourceUnavailable [2]
 * ResourceUnavailableReason, applicationProcedureCancellation [3] ProcedureCancellationReason}
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0x04,constructed=true,lengthIndefinite=false)
public class MAPUserAbortInfoImpl implements MAPUserAbortChoice {
	@ASNChoise
	private MAPUserAbortChoiseImpl userAbortChoise;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }
    
    public MAPUserAbortChoice getUserAbortChoise() {
        return userAbortChoise;
    }

    public void setUserAbortChoise(MAPUserAbortChoice userAbortChoise) {
    	if(userAbortChoise instanceof MAPUserAbortChoiseImpl)
    		this.userAbortChoise = (MAPUserAbortChoiseImpl)userAbortChoise;
    	else {
    		if(this.userAbortChoise==null)
    			this.userAbortChoise=new MAPUserAbortChoiseImpl();
    		
    		if(userAbortChoise.isUserResourceLimitation())
    			this.userAbortChoise.setUserResourceLimitation();
    		else if(userAbortChoise.isProcedureCancellationReason())
    			this.userAbortChoise.setProcedureCancellationReason(userAbortChoise.getProcedureCancellationReason());
    		else if(userAbortChoise.isResourceUnavailableReason())
    			this.userAbortChoise.setResourceUnavailableReason(userAbortChoise.getResourceUnavailableReason());
    		else if(userAbortChoise.isUserSpecificReason())
    			this.userAbortChoise.setUserSpecificReason();    			
    	}
    }

	@Override
	public void setUserSpecificReason() {
		if(this.userAbortChoise==null)
			this.userAbortChoise = new MAPUserAbortChoiseImpl();
		
		this.userAbortChoise.setUserSpecificReason();
	}

	@Override
	public void setUserResourceLimitation() {
		if(this.userAbortChoise==null)
			this.userAbortChoise = new MAPUserAbortChoiseImpl();
		
		this.userAbortChoise.setUserResourceLimitation();
	}

	@Override
	public void setResourceUnavailableReason(ResourceUnavailableReason resUnaReas) {
		if(this.userAbortChoise==null)
			this.userAbortChoise = new MAPUserAbortChoiseImpl();
		
		this.userAbortChoise.setResourceUnavailableReason(resUnaReas);
	}

	@Override
	public void setProcedureCancellationReason(ProcedureCancellationReason procCanReasn) {
		if(this.userAbortChoise==null)
			this.userAbortChoise = new MAPUserAbortChoiseImpl();
		
		this.userAbortChoise.setProcedureCancellationReason(procCanReasn);
	}

	@Override
	public ProcedureCancellationReason getProcedureCancellationReason() {
		if(this.userAbortChoise==null)
			return null;
		
		return this.userAbortChoise.getProcedureCancellationReason();
	}

	@Override
	public ResourceUnavailableReason getResourceUnavailableReason() {
		if(this.userAbortChoise==null)
			return null;
		
		return this.userAbortChoise.getResourceUnavailableReason();
	}

	@Override
	public boolean isUserSpecificReason() {
		if(this.userAbortChoise==null)
			return false;
		
		return this.userAbortChoise.isUserSpecificReason();
	}

	@Override
	public boolean isUserResourceLimitation() {
		if(this.userAbortChoise==null)
			return false;
		
		return this.userAbortChoise.isUserResourceLimitation();
	}

	@Override
	public boolean isResourceUnavailableReason() {
		if(this.userAbortChoise==null)
			return false;
		
		return this.userAbortChoise.isResourceUnavailableReason();
	}

	@Override
	public boolean isProcedureCancellationReason() {
		if(this.userAbortChoise==null)
			return false;
		
		return this.userAbortChoise.isProcedureCancellationReason();
	}
}