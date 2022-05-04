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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionWithArgumentRequest;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Povilas Jurna
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true, lengthIndefinite = false)
public class DisconnectForwardConnectionWithArgumentRequestImpl extends CircuitSwitchedCallMessageImpl implements
        DisconnectForwardConnectionWithArgumentRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger callSegmentID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public DisconnectForwardConnectionWithArgumentRequestImpl() {
    }

    public DisconnectForwardConnectionWithArgumentRequestImpl(Integer callSegmentId, CAPINAPExtensions extensions) {
    	if(callSegmentId!=null)
    		this.callSegmentID = new ASNInteger(callSegmentId,"CallSegmentID",0,127,false);
    		
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
    	if(callSegmentID==null)
    		return null;
    	
        return callSegmentID.getIntValue();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

}
