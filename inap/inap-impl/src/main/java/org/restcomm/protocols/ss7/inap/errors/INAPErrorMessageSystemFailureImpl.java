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

package org.restcomm.protocols.ss7.inap.errors;

import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorCode;
import org.restcomm.protocols.ss7.inap.api.errors.INAPErrorMessageSystemFailure;
import org.restcomm.protocols.ss7.inap.api.errors.UnavailableNetworkResource;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPErrorMessageSystemFailureImpl extends EnumeratedINAPErrorMessage1Impl implements INAPErrorMessageSystemFailure {
	protected INAPErrorMessageSystemFailureImpl(UnavailableNetworkResource unavailableNetworkResource) {
        super((long) INAPErrorCode.systemFailure);

        if(unavailableNetworkResource!=null)
        	setValue(Long.valueOf(unavailableNetworkResource.getCode()));        
    }

    public INAPErrorMessageSystemFailureImpl() {
        super((long) INAPErrorCode.systemFailure);
    }

    public boolean isEmSystemFailure() {
        return true;
    }

    public INAPErrorMessageSystemFailure getEmSystemFailure() {
        return this;
    }

    @Override
    public UnavailableNetworkResource getUnavailableNetworkResource() {
    	Long value=getValue();
    	if(value==null)
    		return null;
    	
    	return UnavailableNetworkResource.getInstance(value.intValue());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INAPErrorMessageSystemFailure [");
        UnavailableNetworkResource unavailableNetworkResource=getUnavailableNetworkResource();
        if (unavailableNetworkResource != null) {
            sb.append("unavailableNetworkResource=");
            sb.append(unavailableNetworkResource);
            sb.append(",");
        }
        sb.append("]");

        return sb.toString();
    }
}
