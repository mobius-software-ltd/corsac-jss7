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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class RequestedInformationTypeWrapperImpl {
	private List<ASNRequestedInformationTypeImpl> requestedInformationTypes;

    public RequestedInformationTypeWrapperImpl() {
    }

    public RequestedInformationTypeWrapperImpl(List<RequestedInformationType> requestedInformationTypes) {
    	if(requestedInformationTypes!=null) {
    		this.requestedInformationTypes=new ArrayList<ASNRequestedInformationTypeImpl>();
    		for(RequestedInformationType currType:requestedInformationTypes) {
    			ASNRequestedInformationTypeImpl currValue=new ASNRequestedInformationTypeImpl(currType);
    			this.requestedInformationTypes.add(currValue);
    		}
    	}    	
    }

    public List<RequestedInformationType> getRequestedInformationTypes() {
    	List<RequestedInformationType> result=new ArrayList<RequestedInformationType>();
    	if(requestedInformationTypes!=null) {
    		for(ASNRequestedInformationTypeImpl curr:requestedInformationTypes)
    			result.add(curr.getType());
    	}
    	
        return result;
    }
}
