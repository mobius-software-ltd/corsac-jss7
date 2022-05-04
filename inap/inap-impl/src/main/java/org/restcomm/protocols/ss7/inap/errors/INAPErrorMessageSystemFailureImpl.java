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
        super(INAPErrorCode.systemFailure,"SystemFailure",0,4);

        if(unavailableNetworkResource!=null)
        	setValue(unavailableNetworkResource.getCode());        
    }

    public INAPErrorMessageSystemFailureImpl() {
        super(INAPErrorCode.systemFailure,"SystemFailure",0,4);
    }

    public boolean isEmSystemFailure() {
        return true;
    }

    public INAPErrorMessageSystemFailure getEmSystemFailure() {
        return this;
    }

    @Override
    public UnavailableNetworkResource getUnavailableNetworkResource() {
    	Integer value=getValue();
    	if(value==null)
    		return null;
    	
    	return UnavailableNetworkResource.getInstance(value);
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
