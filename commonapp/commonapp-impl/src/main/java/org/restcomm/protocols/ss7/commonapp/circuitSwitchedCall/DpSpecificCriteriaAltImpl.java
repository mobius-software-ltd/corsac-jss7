/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ChangeOfLocation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DpSpecificCriteriaAlt;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
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
        
        if(numberOfDigits!=null)
        	this.numberOfDigits = new ASNInteger(numberOfDigits,"NumberOfDigits",1,255,false);        	
    }

    public List<ChangeOfLocation> getChangeOfPositionControlInfo() {
        if(changeOfPositionControlInfo==null)
        	return null;
        
    	return changeOfPositionControlInfo.getChangeOfLocationList();
    }

    public Integer getNumberOfDigits() {
    	if(numberOfDigits==null)
    		return null;
    	
        return numberOfDigits.getIntValue();
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(changeOfPositionControlInfo==null || changeOfPositionControlInfo.getChangeOfLocationList()==null)
			throw new ASNParsingComponentException("change of position should be set for dp specific criteria alt", ASNParsingComponentExceptionReason.MistypedParameter);		    		
		
		if(changeOfPositionControlInfo.getChangeOfLocationList().size()==0)
			throw new ASNParsingComponentException("change of position should be set for dp specific criteria alt", ASNParsingComponentExceptionReason.MistypedParameter);		    		
		
		if(changeOfPositionControlInfo.getChangeOfLocationList().size()>10)
			throw new ASNParsingComponentException("change of position should have 1 to 10 items for dp specific criteria alt", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
