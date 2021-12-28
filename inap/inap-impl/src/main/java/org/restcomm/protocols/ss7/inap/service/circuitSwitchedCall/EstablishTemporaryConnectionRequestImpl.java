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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ScfIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.RouteListImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceInteractionIndicatorsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
  * @author yulian.oifa
*
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EstablishTemporaryConnectionRequestImpl extends CircuitSwitchedCallMessageImpl implements
        EstablishTemporaryConnectionRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = true,index = -1)
    private SendingLegIDWrapperImpl privateLegID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup assistingSSPIPRoutingAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup correlationID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private LegIDWrapperImpl legID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1,defaultImplementation = ScfIDImpl.class)
    private ScfID scfID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1,defaultImplementation = CarrierImpl.class)
    private Carrier carrier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false,index = -1,defaultImplementation = ServiceInteractionIndicatorsImpl.class)
    private ServiceInteractionIndicators serviceInteractionIndicators;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 2,constructed = false,index = -1,defaultImplementation = RouteListImpl.class)
    private RouteList routeList;
    
    public EstablishTemporaryConnectionRequestImpl() {        
    }

    public EstablishTemporaryConnectionRequestImpl(LegType legType,DigitsIsup assistingSSPIPRoutingAddress, DigitsIsup correlationID,
    		ScfID scfID, CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicators serviceInteractionIndicators,RouteList routeList) {
        this(assistingSSPIPRoutingAddress, correlationID, null, scfID, extensions, carrier, serviceInteractionIndicators);
        if(legType!=null)
        	this.privateLegID=new SendingLegIDWrapperImpl(new SendingLegIDImpl(legType));
        
        this.routeList = routeList;
    }

    public EstablishTemporaryConnectionRequestImpl(DigitsIsup assistingSSPIPRoutingAddress, DigitsIsup correlationID, LegID legID,
    		ScfID scfID, CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicators serviceInteractionIndicators) {
        this.assistingSSPIPRoutingAddress = assistingSSPIPRoutingAddress;
        this.correlationID = correlationID;
        if(legID!=null)
        	this.legID=new LegIDWrapperImpl(legID);
        
        this.scfID = scfID;
        this.extensions = extensions;
        this.carrier = carrier;
        this.serviceInteractionIndicators = serviceInteractionIndicators;        
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.establishTemporaryConnection_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.establishTemporaryConnection;
    }

    public LegType getPrivateLegID() {
    	if(privateLegID==null || privateLegID.getSendingLegID()==null)
    		return null;
    	
		return privateLegID.getSendingLegID().getSendingSideID();
	}

	@Override
    public DigitsIsup getAssistingSSPIPRoutingAddress() {
    	if(assistingSSPIPRoutingAddress!=null)
    		assistingSSPIPRoutingAddress.setIsGenericNumber();
    	
        return assistingSSPIPRoutingAddress;
    }

    @Override
    public DigitsIsup getCorrelationID() {
    	if(correlationID!=null)
    		correlationID.setIsGenericDigits();
    	
        return correlationID;
    }

    public LegID getLegID() {
		if(legID==null)
			return null;
		
		return legID.getLegID();
	}

    @Override
    public ScfID getScfID() {
        return scfID;
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public Carrier getCarrier() {
        return carrier;
    }
    
    public ServiceInteractionIndicators getServiceInteractionIndicators() {
		return serviceInteractionIndicators;
	}

	public RouteList getRouteList() {
		return routeList;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EstablishTemporaryConnectionIndication [");
        this.addInvokeIdInfo(sb);

        if (this.privateLegID != null && this.privateLegID.getSendingLegID()!=null && this.privateLegID.getSendingLegID().getSendingSideID()!=null) {
            sb.append(", legID=");
            sb.append(this.privateLegID.getSendingLegID().getSendingSideID());
        }
        if (this.assistingSSPIPRoutingAddress != null) {
            sb.append(", assistingSSPIPRoutingAddress=");
            sb.append(this.assistingSSPIPRoutingAddress.toString());
        }
        if (this.correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID.toString());
        }
        if (this.legID != null && this.legID.getLegID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getLegID());
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
        if (this.serviceInteractionIndicators != null) {
            sb.append(", serviceInteractionIndicators=");
            sb.append(serviceInteractionIndicators.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
