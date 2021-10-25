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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegRequest;
import org.restcomm.protocols.ss7.inap.api.primitives.LegID;
import org.restcomm.protocols.ss7.inap.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.primitives.LegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Povilas Jurna
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class DisconnectLegRequestImpl extends CircuitSwitchedCallMessageImpl implements DisconnectLegRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private LegIDWrapperImpl legToBeReleased;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private CauseCapImpl releaseCause;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;

    public DisconnectLegRequestImpl() {
    }

    public DisconnectLegRequestImpl(LegID legToBeReleased, CauseCapImpl releaseCause, CAPExtensionsImpl extensions) {
    	
    	if(legToBeReleased!=null)
    		this.legToBeReleased = new LegIDWrapperImpl(legToBeReleased);
    	
        this.releaseCause = releaseCause;
        this.extensions = extensions;
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.disconnectLeg_Request;
    }

    public int getOperationCode() {
        return CAPOperationCode.disconnectLeg;
    }

    public CAPExtensionsImpl getExtensions() {
        return extensions;
    }

    public LegIDImpl getLegToBeReleased() {
    	if(legToBeReleased==null)
    		return null;
    	
        return legToBeReleased.getLegID();
    }

    public CauseCapImpl getReleaseCause() {
        return releaseCause;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DisconnectlegRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.legToBeReleased != null) {
            sb.append(", legToBeReleased=");
            sb.append(legToBeReleased.toString());
        }
        if (this.releaseCause != null) {
            sb.append(", releaseCause=");
            sb.append(releaseCause.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        sb.append("]");

        return sb.toString();
    }
}
