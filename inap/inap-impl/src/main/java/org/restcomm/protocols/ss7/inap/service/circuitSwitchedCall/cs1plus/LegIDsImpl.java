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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import java.util.List;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ExistingLegs;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.LegIDs;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class LegIDsImpl implements LegIDs {
	
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,index = -1, defaultImplementation = ExistingLegsImpl.class)
	private List<ExistingLegs> existingLegs;

    public LegIDsImpl() {
    }

    public LegIDsImpl(List<ExistingLegs> existingLegs) {
    	this.existingLegs=existingLegs;
    }

    public List<ExistingLegs> getExistingLegs() {
    	return existingLegs;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("LegIDs [");
        
        List<ExistingLegs> items=getExistingLegs();
        if (items != null && items.size()!=0) {
            sb.append("existingLegs=");
            boolean isFirst=false;
            for(ExistingLegs curr:items) {
            	if(!isFirst)
            		sb.append(",");
            	
            	sb.append(curr);
            	isFirst=false;
            }         
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(existingLegs==null || existingLegs.size()==0)
			throw new ASNParsingComponentException("existing legs list should be set for leg IDs", ASNParsingComponentExceptionReason.MistypedParameter);

		if(existingLegs.size()>20)
			throw new ASNParsingComponentException("existing legs list size should be betwen 1 and 30 for leg IDs", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}