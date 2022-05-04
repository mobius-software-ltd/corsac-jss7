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

import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author cristian veliscu
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtendedRoutingInfoImpl implements ExtendedRoutingInfo {
	@ASNChoise(defaultImplementation = RoutingInfoImpl.class)
    private RoutingInfo routingInfo = null;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=true,index=-1, defaultImplementation = CamelRoutingInfoImpl.class)
	private CamelRoutingInfo camelRoutingInfo = null;

    public ExtendedRoutingInfoImpl() {
    }

    public ExtendedRoutingInfoImpl(RoutingInfo routingInfo) {
    	this.routingInfo=routingInfo;    	
    }

    public ExtendedRoutingInfoImpl(CamelRoutingInfo camelRoutingInfo) {
        this.camelRoutingInfo = camelRoutingInfo;
    }

    public RoutingInfo getRoutingInfo() {
        return this.routingInfo;
    }

    public CamelRoutingInfo getCamelRoutingInfo() {
        return this.camelRoutingInfo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtendedRoutingInfo [");

        if (this.routingInfo != null) {
            sb.append(this.routingInfo.toString());
        } else if (this.camelRoutingInfo != null) {
            sb.append(this.camelRoutingInfo.toString());
        }

        sb.append("]");
        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(routingInfo==null && camelRoutingInfo==null)
			throw new ASNParsingComponentException("either routing info or camel routing info should be set for extended routing info", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}