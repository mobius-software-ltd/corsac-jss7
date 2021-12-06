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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TBusySpecificInfo;
import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.restcomm.protocols.ss7.cap.api.isup.CauseCap;
import org.restcomm.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.isup.CauseCapImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TBusySpecificInfoImpl implements TBusySpecificInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = CauseCapImpl.class)
    private CauseCap busyCause;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1)
    private ASNNull callForwarded;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = false,index = -1)
    private ASNNull routeNotPermitted;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = false,index = -1, defaultImplementation = CalledPartyNumberCapImpl.class)
    private CalledPartyNumberCap forwardingDestinationNumber;

    public TBusySpecificInfoImpl() {
    }

    public TBusySpecificInfoImpl(CauseCap busyCause, boolean callForwarded, boolean routeNotPermitted,
            CalledPartyNumberCap forwardingDestinationNumber) {
        this.busyCause = busyCause;
        if(callForwarded)
        	this.callForwarded = new ASNNull();
        
        if(routeNotPermitted)
        	this.routeNotPermitted = new ASNNull();
        
        this.forwardingDestinationNumber = forwardingDestinationNumber;
    }

    public CauseCap getBusyCause() {
        return busyCause;
    }

    public boolean getCallForwarded() {
        return callForwarded!=null;
    }

    public boolean getRouteNotPermitted() {
        return routeNotPermitted!=null;
    }

    public CalledPartyNumberCap getForwardingDestinationNumber() {
        return forwardingDestinationNumber;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TBusySpecificInfo [");

        if (this.busyCause != null) {
            sb.append("busyCause= [");
            sb.append(busyCause);
            sb.append("]");
        }
        if (this.callForwarded!=null) {
            sb.append(", callForwarded");
        }
        if (this.routeNotPermitted!=null) {
            sb.append(", routeNotPermitted");
        }
        if (this.forwardingDestinationNumber != null) {
            sb.append(", forwardingDestinationNumber= [");
            sb.append(forwardingDestinationNumber);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
