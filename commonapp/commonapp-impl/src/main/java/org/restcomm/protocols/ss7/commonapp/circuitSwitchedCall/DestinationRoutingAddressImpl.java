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

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DestinationRoutingAddressImpl implements DestinationRoutingAddress {
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,index = -1,defaultImplementation = CalledPartyNumberIsupImpl.class)
	public List<CalledPartyNumberIsup> calledPartyNumber;

    public DestinationRoutingAddressImpl() {
    }

    public DestinationRoutingAddressImpl(List<CalledPartyNumberIsup> calledPartyNumber) {
        this.calledPartyNumber = calledPartyNumber;
    }

    public List<CalledPartyNumberIsup> getCalledPartyNumber() {
        return calledPartyNumber;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("calledPartyNumber [");
        if (this.calledPartyNumber != null) {
            sb.append("calledPartyNumber=[");
            for (CalledPartyNumberIsup cpn : this.calledPartyNumber) {
                sb.append(cpn.toString());
                sb.append(", ");
            }
            sb.append("]");
        }
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(calledPartyNumber==null)
			throw new ASNParsingComponentException("called party number should be set for destination routing address", ASNParsingComponentExceptionReason.MistypedParameter);		    		
		
		if(calledPartyNumber.size()==0)
			throw new ASNParsingComponentException("called party number should be set for destination routing address", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(calledPartyNumber.size()!=1)
			throw new ASNParsingComponentException("called party number should have 1 item for destination routing address", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}