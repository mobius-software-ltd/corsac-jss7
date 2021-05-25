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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.isup.CallingPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.DigitsImpl;
import org.restcomm.protocols.ss7.cap.api.isup.LocationNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.OriginalCalledNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.ScfIDImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EstablishTemporaryConnectionRequestImpl extends CircuitSwitchedCallMessageImpl implements
        EstablishTemporaryConnectionRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private DigitsImpl assistingSSPIPRoutingAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private DigitsImpl correlationID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ScfIDImpl scfID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1)
    private CarrierImpl carrier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1)
    private ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = true,index = -1)
    private ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1)
    private ASNInteger callSegmentID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1)
    private NAOliInfoImpl naOliInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = false,index = -1)
    private LocationNumberCapImpl chargeNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = false,index = -1)
    private OriginalCalledNumberCapImpl originalCalledPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 53,constructed = false,index = -1)
    private CallingPartyNumberCapImpl callingPartyNumber;

    public EstablishTemporaryConnectionRequestImpl() {        
    }

    public EstablishTemporaryConnectionRequestImpl(DigitsImpl assistingSSPIPRoutingAddress, DigitsImpl correlationID, ScfIDImpl scfID,
            CAPExtensionsImpl extensions, CarrierImpl carrier, ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
            Integer callSegmentID, NAOliInfoImpl naOliInfo, LocationNumberCapImpl chargeNumber,
            OriginalCalledNumberCapImpl originalCalledPartyID, CallingPartyNumberCapImpl callingPartyNumber) {
        this.assistingSSPIPRoutingAddress = assistingSSPIPRoutingAddress;
        this.correlationID = correlationID;
        this.scfID = scfID;
        this.extensions = extensions;
        this.carrier = carrier;
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
        
        if(callSegmentID!=null) {
        	this.callSegmentID = new ASNInteger();
        	this.callSegmentID.setValue(callSegmentID.longValue());
        }
        
        this.naOliInfo = naOliInfo;
        this.chargeNumber = chargeNumber;
        this.originalCalledPartyID = originalCalledPartyID;
        this.callingPartyNumber = callingPartyNumber;
    }
    
    public EstablishTemporaryConnectionRequestImpl(DigitsImpl assistingSSPIPRoutingAddress, DigitsImpl correlationID, ScfIDImpl scfID,
            CAPExtensionsImpl extensions, CarrierImpl carrier, ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
            NAOliInfoImpl naOliInfo, LocationNumberCapImpl chargeNumber,
            OriginalCalledNumberCapImpl originalCalledPartyID, CallingPartyNumberCapImpl callingPartyNumber) {
        this.assistingSSPIPRoutingAddress = assistingSSPIPRoutingAddress;
        this.correlationID = correlationID;
        this.scfID = scfID;
        this.extensions = extensions;
        this.carrier = carrier;
        this.serviceInteractionIndicatorsTwo2 = serviceInteractionIndicatorsTwo;
        this.naOliInfo = naOliInfo;
        this.chargeNumber = chargeNumber;
        this.originalCalledPartyID = originalCalledPartyID;
        this.callingPartyNumber = callingPartyNumber;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.establishTemporaryConnection_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.establishTemporaryConnection;
    }

    @Override
    public DigitsImpl getAssistingSSPIPRoutingAddress() {
    	if(assistingSSPIPRoutingAddress!=null)
    		assistingSSPIPRoutingAddress.setIsGenericNumber();
    	
        return assistingSSPIPRoutingAddress;
    }

    @Override
    public DigitsImpl getCorrelationID() {
    	if(correlationID!=null)
    		correlationID.setIsGenericDigits();
    	
        return correlationID;
    }

    @Override
    public ScfIDImpl getScfID() {
        return scfID;
    }

    @Override
    public CAPExtensionsImpl getExtensions() {
        return extensions;
    }

    @Override
    public CarrierImpl getCarrier() {
        return carrier;
    }

    @Override
    public ServiceInteractionIndicatorsTwoImpl getServiceInteractionIndicatorsTwo() {
    	if(serviceInteractionIndicatorsTwo!=null)
    		return serviceInteractionIndicatorsTwo;
    	
    	return serviceInteractionIndicatorsTwo2;
    }

    @Override
    public Integer getCallSegmentID() {
    	if(callSegmentID==null || callSegmentID.getValue()==null)
    		return null;
    	
        return callSegmentID.getValue().intValue();
    }

    @Override
    public NAOliInfoImpl getNAOliInfo() {
        return naOliInfo;
    }

    @Override
    public LocationNumberCapImpl getChargeNumber() {
        return chargeNumber;
    }

    @Override
    public OriginalCalledNumberCapImpl getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    @Override
    public CallingPartyNumberCapImpl getCallingPartyNumber() {
        return callingPartyNumber;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EstablishTemporaryConnectionIndication [");
        this.addInvokeIdInfo(sb);

        if (this.assistingSSPIPRoutingAddress != null) {
            sb.append(", assistingSSPIPRoutingAddress=");
            sb.append(this.assistingSSPIPRoutingAddress.toString());
        }
        if (this.correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID.toString());
        }
        if (this.scfID != null) {
            sb.append(", scfID=");
            sb.append(scfID.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }
        if (this.serviceInteractionIndicatorsTwo != null) {
            sb.append(", serviceInteractionIndicatorsTwo=");
            sb.append(serviceInteractionIndicatorsTwo.toString());
        }
        if (this.callSegmentID != null && this.callSegmentID.getValue()!=null) {
            sb.append(", callSegmentID=");
            sb.append(callSegmentID.getValue());
        }
        if (this.naOliInfo != null) {
            sb.append(", naOliInfo=");
            sb.append(naOliInfo.toString());
        }
        if (this.chargeNumber != null) {
            sb.append(", chargeNumber=");
            sb.append(chargeNumber.toString());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
