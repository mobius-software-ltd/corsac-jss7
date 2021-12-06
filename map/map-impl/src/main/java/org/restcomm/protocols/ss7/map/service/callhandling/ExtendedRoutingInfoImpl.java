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

package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.RoutingInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/*
 *
 * @author cristian veliscu
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtendedRoutingInfoImpl implements ExtendedRoutingInfo {
	@ASNChoise
    private RoutingInfoImpl routingInfo = null;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=true,index=-1, defaultImplementation = CamelRoutingInfoImpl.class)
	private CamelRoutingInfo camelRoutingInfo = null;

    public ExtendedRoutingInfoImpl() {
    }

    public ExtendedRoutingInfoImpl(RoutingInfo routingInfo) {
    	if(routingInfo instanceof RoutingInfoImpl)
    		this.routingInfo=(RoutingInfoImpl)routingInfo;
    	else if(routingInfo!=null) {
    		if(routingInfo.getForwardingData()!=null)
    			this.routingInfo = new RoutingInfoImpl(routingInfo.getForwardingData());
    		else
    			this.routingInfo = new RoutingInfoImpl(routingInfo.getRoamingNumber());
    	}
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
}