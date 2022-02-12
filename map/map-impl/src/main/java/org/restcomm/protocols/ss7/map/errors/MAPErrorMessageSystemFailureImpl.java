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

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.errors.AdditionalNetworkResource;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;
import org.restcomm.protocols.ss7.map.primitives.ASNNetworkResourceImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class MAPErrorMessageSystemFailureImpl extends MAPErrorMessageImpl implements MAPErrorMessageSystemFailure {
	private ASNNetworkResourceImpl networkResource;
	
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNAdditionalNetworkResourceImpl additionalNetworkResource;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public MAPErrorMessageSystemFailureImpl(NetworkResource networkResource, AdditionalNetworkResource additionalNetworkResource, MAPExtensionContainer extensionContainer) {
        super(MAPErrorCode.systemFailure);

        if(networkResource!=null)
        	this.networkResource=new ASNNetworkResourceImpl(networkResource);
        	
        if(additionalNetworkResource!=null)
        	this.additionalNetworkResource = new ASNAdditionalNetworkResourceImpl(additionalNetworkResource);
        	
        this.extensionContainer = extensionContainer;
    }

    public MAPErrorMessageSystemFailureImpl() {
        super(MAPErrorCode.systemFailure);
    }

    public boolean isEmSystemFailure() {
        return true;
    }

    public MAPErrorMessageSystemFailure getEmSystemFailure() {
        return this;
    }

    public AdditionalNetworkResource getAdditionalNetworkResource() {
    	if(this.additionalNetworkResource==null)
    		return null;
    	
        return this.additionalNetworkResource.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public void setAdditionalNetworkResource(AdditionalNetworkResource additionalNetworkResource) {
    	if(additionalNetworkResource==null)
    		this.additionalNetworkResource=null;
    	else
    		this.additionalNetworkResource=new ASNAdditionalNetworkResourceImpl(additionalNetworkResource);    		
    }

	@Override
	public NetworkResource getNetworkResource() {
		if(this.networkResource==null)
			return null;
		
		return this.networkResource.getType();
	}

	@Override
	public void setNetworkResource(NetworkResource networkResource) {
		if(networkResource==null)
			this.networkResource=null;
		else 
			this.networkResource=new ASNNetworkResourceImpl(networkResource);				
	}

    public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageSystemFailure [");
        if (this.networkResource != null)
            sb.append("networkResource=" + this.networkResource.toString());
        if (this.extensionContainer != null)
            sb.append(", extensionContainer=" + this.extensionContainer.toString());
        if (this.additionalNetworkResource != null)
            sb.append(", additionalNetworkResource=" + this.additionalNetworkResource.toString());
        sb.append("]");

        return sb.toString();
    }
}
