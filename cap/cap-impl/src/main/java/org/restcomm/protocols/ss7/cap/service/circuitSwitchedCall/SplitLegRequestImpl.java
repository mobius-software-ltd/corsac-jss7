/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegRequest;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegID;
import org.restcomm.protocols.ss7.inap.primitives.LegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author tamas gyorgyey
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class SplitLegRequestImpl extends CircuitSwitchedCallMessageImpl implements SplitLegRequest {
    private static final long serialVersionUID = 1L;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private LegIDWrapperImpl legToBeSplit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger newCallSegment;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CAPExtensionsImpl.class)
    private CAPExtensions extensions;

    public SplitLegRequestImpl() {
    }

    public SplitLegRequestImpl(LegID legIDToMove, Integer newCallSegment, CAPExtensions extensions) {
    	if(legIDToMove!=null)
    		this.legToBeSplit =  new LegIDWrapperImpl(legIDToMove);
    	
    	if(newCallSegment!=null) {
    		this.newCallSegment = new ASNInteger();
    		this.newCallSegment.setValue(newCallSegment.longValue());
    	}
    	
        this.extensions = extensions;
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.splitLeg_Request;
    }

    public int getOperationCode() {
        return CAPOperationCode.splitLeg;
    }

    public LegID getLegToBeSplit() {
    	if(legToBeSplit==null)
    		return null;
    	
        return legToBeSplit.getLegID();
    }

    public Integer getNewCallSegment() {
    	if(newCallSegment==null || newCallSegment.getValue()==null)
    		return null;
    	
        return newCallSegment.getValue().intValue();
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SplitLegRequest [");

        if (this.legToBeSplit != null && this.legToBeSplit.getLegID()!=null) {
            sb.append("legToBeSplit=");
            sb.append(legToBeSplit.getLegID());
        }
        if (this.newCallSegment != null && this.newCallSegment.getValue()!=null) {
            sb.append(", newCallSegment=");
            sb.append(newCallSegment.getValue());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        sb.append("]");

        return sb.toString();
    }
}
