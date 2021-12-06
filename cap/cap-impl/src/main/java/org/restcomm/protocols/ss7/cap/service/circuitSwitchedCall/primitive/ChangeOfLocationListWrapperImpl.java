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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocation;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocation.Boolean_Option;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocation.CellGlobalIdOrServiceAreaIdFixedLength_Option;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ChangeOfLocationListWrapperImpl {
	@ASNChoise
	private List<ChangeOfLocationImpl> changeOfLocationList;

    public ChangeOfLocationListWrapperImpl() {
    }

    public ChangeOfLocationListWrapperImpl(List<ChangeOfLocation> changeOfLocationList) {
    	if(changeOfLocationList!=null) {
    		this.changeOfLocationList=new ArrayList<ChangeOfLocationImpl>();
    		for(ChangeOfLocation curr:changeOfLocationList) {
    			if(curr instanceof ChangeOfLocationImpl) 
    				this.changeOfLocationList.add((ChangeOfLocationImpl)curr);
    			else if(curr.getCellGlobalId()!=null)
    				this.changeOfLocationList.add(new ChangeOfLocationImpl(curr.getCellGlobalId(), CellGlobalIdOrServiceAreaIdFixedLength_Option.cellGlobalId));
    			else if(curr.getServiceAreaId()!=null)
    				this.changeOfLocationList.add(new ChangeOfLocationImpl(curr.getServiceAreaId(), CellGlobalIdOrServiceAreaIdFixedLength_Option.serviceAreaId));
    			else if(curr.getChangeOfLocationAlt()!=null)
    				this.changeOfLocationList.add(new ChangeOfLocationImpl(curr.getChangeOfLocationAlt()));
    			else if(curr.getLocationAreaId()!=null)
    				this.changeOfLocationList.add(new ChangeOfLocationImpl(curr.getLocationAreaId()));
    			else if(curr.isInterMSCHandOver())
    				this.changeOfLocationList.add(new ChangeOfLocationImpl(Boolean_Option.interMSCHandOver));
    			else if(curr.isInterPLMNHandOver())
    				this.changeOfLocationList.add(new ChangeOfLocationImpl(Boolean_Option.interPLMNHandOver));
    			else if(curr.isInterSystemHandOver())
    				this.changeOfLocationList.add(new ChangeOfLocationImpl(Boolean_Option.interSystemHandOver));
    		}
    	}
    }

    public List<ChangeOfLocation> getChangeOfLocationList() {
    	if(changeOfLocationList==null)
    		return null;
    	
    	List<ChangeOfLocation> result=new ArrayList<ChangeOfLocation>();
    	for(ChangeOfLocationImpl curr:changeOfLocationList)
    		result.add(curr);
    	
    	return result;
    }
}