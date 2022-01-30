/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
import org.restcomm.protocols.ss7.map.api.errors.AdditionalNetworkResource;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorCode;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.map.api.primitives.NetworkResource;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MAPErrorMessageSytemFailure1Impl extends EnumeratedMAPErrorMessage1Impl implements
MAPErrorMessageSystemFailure {
	public MAPErrorMessageSytemFailure1Impl(NetworkResource networkResource) {
        super((long) MAPErrorCode.systemFailure);

        if(networkResource!=null)
        	setValue(Long.valueOf(networkResource.getCode()));
    }

    public MAPErrorMessageSytemFailure1Impl() {
        super((long) MAPErrorCode.systemFailure);
    }

    public boolean isEmSystemFailure() {
        return true;
    }

    public MAPErrorMessageSystemFailure getEmSystemFailure() {
        return this;
    }

    @Override
    public NetworkResource getNetworkResource() {
    	Long value=getValue();
    	if(value==null)
    		return null;
    	
    	return NetworkResource.getInstance(value.intValue());
    }

    @Override
    public void setNetworkResource(NetworkResource val) {
    	if(val!=null)
    		setValue(Long.valueOf(val.getCode()));
    	else
    		setValue(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("MAPErrorMessageSystemFailure [");

        NetworkResource networkResource=getNetworkResource();
        if (networkResource != null)
            sb.append("networkResource=" + networkResource.toString());
        
        sb.append("]");

        return sb.toString();
    }

	@Override
	public AdditionalNetworkResource getAdditionalNetworkResource() {
		return null;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return null;
	}

	@Override
	public void setAdditionalNetworkResource(AdditionalNetworkResource additionalNetworkResource) {
		
	}

	@Override
	public void setExtensionContainer(MAPExtensionContainer extensionContainer) {
		
	}
}
