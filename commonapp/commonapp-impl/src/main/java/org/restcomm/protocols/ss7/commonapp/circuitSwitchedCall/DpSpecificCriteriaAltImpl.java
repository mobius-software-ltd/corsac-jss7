/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ChangeOfLocation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DpSpecificCriteriaAlt;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DpSpecificCriteriaAltImpl implements DpSpecificCriteriaAlt {
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0, constructed = true,index = -1)
    private ChangeOfLocationListWrapperImpl changeOfPositionControlInfo;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1, constructed = false,index = -1)
    private ASNInteger numberOfDigits;

    public DpSpecificCriteriaAltImpl() {        
    }

    public DpSpecificCriteriaAltImpl(List<ChangeOfLocation> changeOfPositionControlInfo, Integer numberOfDigits) {
        if(changeOfPositionControlInfo!=null) {
        	this.changeOfPositionControlInfo = new ChangeOfLocationListWrapperImpl(changeOfPositionControlInfo);
        }
        
        if(numberOfDigits!=null) {
        	this.numberOfDigits = new ASNInteger();
        	this.numberOfDigits.setValue(numberOfDigits.longValue());
        }
    }

    public List<ChangeOfLocation> getChangeOfPositionControlInfo() {
        if(changeOfPositionControlInfo==null)
        	return null;
        
    	return changeOfPositionControlInfo.getChangeOfLocationList();
    }

    public Integer getNumberOfDigits() {
    	if(numberOfDigits==null)
    		return null;
    	
        return numberOfDigits.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DpSpecificCriteriaAlt [");

        if (this.changeOfPositionControlInfo != null && this.changeOfPositionControlInfo.getChangeOfLocationList() != null) {
            sb.append("changeOfPositionControlInfo=[");
            boolean firstItem = true;
            for (ChangeOfLocation be : this.changeOfPositionControlInfo.getChangeOfLocationList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }
        if (this.numberOfDigits != null) {
            sb.append("numberOfDigits=");
            sb.append(numberOfDigits);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
