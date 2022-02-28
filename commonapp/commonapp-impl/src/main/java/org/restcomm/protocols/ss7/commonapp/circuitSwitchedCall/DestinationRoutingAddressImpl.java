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