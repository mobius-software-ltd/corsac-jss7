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
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionWithArgumentRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Povilas Jurna
 *
 */
public class DisconnectForwardConnectionWithArgumentRequestImpl extends CircuitSwitchedCallMessageImpl implements
        DisconnectForwardConnectionWithArgumentRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger callSegmentID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;

    public DisconnectForwardConnectionWithArgumentRequestImpl() {
    }

    public DisconnectForwardConnectionWithArgumentRequestImpl(Integer callSegmentId, CAPExtensionsImpl extensions) {
    	if(callSegmentId!=null) {
    		this.callSegmentID = new ASNInteger();
    		this.callSegmentID.setValue(callSegmentId.longValue());
    	}
    	
        this.extensions = extensions;
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.disconnectForwardConnectionWithArgument_Request;
    }

    public int getOperationCode() {
        return CAPOperationCode.dFCWithArgument;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DisconnectForwardConnectionWithArgumentRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.callSegmentID != null) {
            sb.append(", callSegmentId=");
            sb.append(callSegmentID.toString());
            sb.append(", ");
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public Integer getCallSegmentID() {
    	if(callSegmentID==null || callSegmentID.getValue()==null)
    		return null;
    	
        return callSegmentID.getValue().intValue();
    }

    @Override
    public CAPExtensionsImpl getExtensions() {
        return extensions;
    }

}
