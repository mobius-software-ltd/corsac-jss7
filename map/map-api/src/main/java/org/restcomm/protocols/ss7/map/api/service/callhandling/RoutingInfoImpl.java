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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/*
 *
 * @author cristian veliscu
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class RoutingInfoImpl {
	private ISDNAddressStringImpl roamingNumber = null;
    private ForwardingDataImpl forwardingData = null;

    public RoutingInfoImpl() {
    }

    public RoutingInfoImpl(ISDNAddressStringImpl roamingNumber) {
        this.roamingNumber = roamingNumber;
    }

    public RoutingInfoImpl(ForwardingDataImpl forwardingData) {
        this.forwardingData = forwardingData;
    }

    public ISDNAddressStringImpl getRoamingNumber() {
        return this.roamingNumber;
    }

    public ForwardingDataImpl getForwardingData() {
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
}