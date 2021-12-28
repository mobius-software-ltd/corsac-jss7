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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.BCSMEventWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class RequestReportBCSMEventRequestImpl extends CircuitSwitchedCallMessageImpl implements RequestReportBCSMEventRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private BCSMEventWrapperImpl bcsmEventList;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup bcsmEventCorrelationID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public RequestReportBCSMEventRequestImpl() {
    }

    public RequestReportBCSMEventRequestImpl(List<BCSMEvent> bcsmEventList, DigitsIsup bcsmEventCorrelationID, CAPINAPExtensions extensions) {
    	if(bcsmEventList!=null)
    		this.bcsmEventList = new BCSMEventWrapperImpl(bcsmEventList);
    	
    	this.bcsmEventCorrelationID=bcsmEventCorrelationID;
    	this.extensions = extensions;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.requestReportBCSMEvent_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.requestReportBCSMEvent;
    }

    @Override
    public List<BCSMEvent> getBCSMEventList() {
    	if(bcsmEventList==null)
    		return null;
    	
        return bcsmEventList.getBCSMEvents();
    }

    @Override
    public DigitsIsup getBCSMEventCorrelationID() {
    	if(bcsmEventCorrelationID!=null)
    		bcsmEventCorrelationID.setIsGenericNumber();
    	
		return bcsmEventCorrelationID;
	}
    
    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("RequestReportBCSMEventRequest [");
        this.addInvokeIdInfo(sb);

        if (this.bcsmEventList != null && this.bcsmEventList.getBCSMEvents()!=null) {
            sb.append(", bcsmEventList=[");
            boolean firstItem = true;
            for (BCSMEvent be : this.bcsmEventList.getBCSMEvents()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }
        if (this.bcsmEventCorrelationID != null) {
            sb.append(", bcsmEventCorrelationID=");
            sb.append(bcsmEventCorrelationID.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}