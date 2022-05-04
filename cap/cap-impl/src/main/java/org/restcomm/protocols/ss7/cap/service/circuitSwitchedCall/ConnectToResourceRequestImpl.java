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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ResourceAddressImpl;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ConnectToResourceRequestImpl extends CircuitSwitchedCallMessageImpl implements ConnectToResourceRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise
    private ResourceAddressImpl resourceAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = true,index = -1,defaultImplementation = ServiceInteractionIndicatorsTwoImpl.class)
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1)
    private ASNInteger callSegmentID;

    public ConnectToResourceRequestImpl() {
    }

    public ConnectToResourceRequestImpl(CalledPartyNumberIsup resourceAddress_IPRoutingAddress, boolean resourceAddress_Null,
            CAPINAPExtensions extensions, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) {
    	
    	if(resourceAddress_IPRoutingAddress!=null)
    		this.resourceAddress = new ResourceAddressImpl(resourceAddress_IPRoutingAddress);	
    	else
    		this.resourceAddress = new ResourceAddressImpl(resourceAddress_Null);
        
        this.extensions = extensions;
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
        
        if(callSegmentID!=null)
        	this.callSegmentID = new ASNInteger(callSegmentID,"CallSegmentID",0,127,false);        	
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.connectToResource_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.connectToResource;
    }

    @Override
    public CalledPartyNumberIsup getResourceAddress_IPRoutingAddress() {
    	if(resourceAddress==null)
    		return null;
    	
        return resourceAddress.getIPRoutingAddress();
    }

    @Override
    public boolean getResourceAddress_Null() {
    	if(resourceAddress==null)
    		return false;
    	
        return resourceAddress.getIsNull();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    @Override
    public Integer getCallSegmentID() {
    	if(callSegmentID==null || callSegmentID.getValue()==null)
    		return null;
    	
        return callSegmentID.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ConnectToResourceIndication [");
        this.addInvokeIdInfo(sb);

        if (this.resourceAddress != null) {
            sb.append(", resourceAddress=");
            sb.append(resourceAddress.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.serviceInteractionIndicatorsTwo != null) {
            sb.append(", serviceInteractionIndicatorsTwo=");
            sb.append(serviceInteractionIndicatorsTwo.toString());
        }
        if (this.callSegmentID != null && this.callSegmentID.getValue()!=null) {
            sb.append(", callSegmentID=");
            sb.append(callSegmentID.getValue());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(resourceAddress==null)
			throw new ASNParsingComponentException("resource address should be set for connect to resource request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
