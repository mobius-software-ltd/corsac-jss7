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

package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class RoutingInfoImpl implements RoutingInfo {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString roamingNumber = null;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = ForwardingDataImpl.class)
	private ForwardingData forwardingData = null;

    public RoutingInfoImpl() {
    }

    public RoutingInfoImpl(ISDNAddressString roamingNumber) {
        this.roamingNumber = roamingNumber;
    }

    public RoutingInfoImpl(ForwardingData forwardingData) {
        this.forwardingData = forwardingData;
    }

    public ISDNAddressString getRoamingNumber() {
        return this.roamingNumber;
    }

    public ForwardingData getForwardingData() {
        return this.forwardingData;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RoutingInfo [");

        if (this.roamingNumber != null) {
            sb.append(this.roamingNumber.toString());
        } else if (this.forwardingData != null) {
            sb.append(this.forwardingData.toString());
        }

        sb.append("]");
        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(roamingNumber==null && forwardingData==null)
			throw new ASNParsingComponentException("either roaming number or forwarding data should be set for routing info", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}