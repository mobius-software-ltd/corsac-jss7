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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.LegOrCallSegment;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Povilas Jurna
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class LegOrCallSegmentImpl implements LegOrCallSegment {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger callSegmentID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = LegIDImpl.class)
    private LegID legID;

    public LegOrCallSegmentImpl() {
    }

    public LegOrCallSegmentImpl(Integer callSegmentID) {
    	if(callSegmentID!=null)
    		this.callSegmentID = new ASNInteger(callSegmentID);    		
    }

    public LegOrCallSegmentImpl(LegID legID) {
        this.legID = legID;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("LegOrCallSegment [");

        if (this.callSegmentID != null) {
            sb.append("callSegmentID=");
            sb.append(callSegmentID);
            sb.append(", ");
        }
        if (this.legID != null) {
            sb.append("legID=");
            sb.append(legID.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

    public Integer getCallSegmentID() {
    	if(callSegmentID==null)
    		return null;
    	
        return callSegmentID.getIntValue();
    }

    public LegID getLegID() {
        return legID;
    }
}
