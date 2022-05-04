/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AlertingPatternWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ScfIDImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SelectRouteRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CallingPartyBusinessGroupIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.RouteListImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class SelectRouteRequestImpl extends CircuitSwitchedCallMessageImpl implements SelectRouteRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1,defaultImplementation = CalledPartyNumberIsupImpl.class)
	private CalledPartyNumberIsup destinationNumberRoutingAddress;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
	private AlertingPatternWrapperImpl alertingPattern;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
	private DigitsIsup correlationID;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1,defaultImplementation = CallingPartyBusinessGroupIDImpl.class)
	private ISDNAccessRelatedInformation isdnAccessRelatedInformation;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1, defaultImplementation = OriginalCalledNumberIsupImpl.class)
	private OriginalCalledNumberIsup originalCalledPartyID;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1,defaultImplementation = RouteListImpl.class)
    private RouteList routeList;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1,defaultImplementation = ScfIDImpl.class)
    private ScfID scfID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1, defaultImplementation = LocationNumberIsupImpl.class)
	private LocationNumberIsup travellingClassMark;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
	private CAPINAPExtensions extensions;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1, defaultImplementation = CarrierImpl.class)
	private Carrier carrier;

    public SelectRouteRequestImpl() {
    }

    public SelectRouteRequestImpl(CalledPartyNumberIsup destinationNumberRoutingAddress,AlertingPattern alertingPattern,
    		DigitsIsup correlationID, ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		OriginalCalledNumberIsup originalCalledPartyID,RouteList routeList,ScfID scfID,
    		LocationNumberIsup travellingClassMark,CAPINAPExtensions extensions,Carrier carrier) {
    	this.destinationNumberRoutingAddress = destinationNumberRoutingAddress;
        if(alertingPattern!=null)
    		this.alertingPattern = new AlertingPatternWrapperImpl(alertingPattern);
    	
        this.correlationID=correlationID;
        this.isdnAccessRelatedInformation = isdnAccessRelatedInformation;
        this.originalCalledPartyID=originalCalledPartyID;
        this.routeList=routeList;
        this.scfID=scfID;
        this.travellingClassMark=travellingClassMark;
        this.extensions = extensions;
        this.carrier=carrier;        
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.selectFacility_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.selectRoute;
    }

    @Override
    public CalledPartyNumberIsup getDestinationNumberRoutingAddress() {
		return destinationNumberRoutingAddress;
	}

    @Override
    public AlertingPattern getAlertingPattern() {
    	if(alertingPattern==null)
    		return null;
    	
		return alertingPattern.getAlertingPattern();
	}

    @Override
    public DigitsIsup getCorrelationID() {
    	if(correlationID!=null)
    		correlationID.setIsGenericNumber();
    	
        return correlationID;
    }

    @Override
    public ISDNAccessRelatedInformation getISDNAccessRelatedInformation() {
		return isdnAccessRelatedInformation;
	}

    @Override
    public OriginalCalledNumberIsup getOriginalCalledPartyID() {
		return originalCalledPartyID;
	}

	@Override
    public RouteList getRouteList() {
		return routeList;
	}

	@Override
    public ScfID getScfID() {
		return scfID;
	}

    @Override
    public LocationNumberIsup getTravellingClassMark() {
		return travellingClassMark;
	}

    @Override
    public CAPINAPExtensions getExtensions() {
		return extensions;
	}

    @Override
    public Carrier getCarrier() {
		return carrier;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SelectRouteRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.destinationNumberRoutingAddress != null) {
            sb.append(", destinationNumberRoutingAddress=");
            sb.append(destinationNumberRoutingAddress.toString());
        }
        if (this.alertingPattern != null && this.alertingPattern.getAlertingPattern()!=null) {
            sb.append(", alertingPattern=");
            sb.append(alertingPattern.toString());
        }
        if (this.correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID.toString());
        }
        if (this.isdnAccessRelatedInformation != null) {
            sb.append(", isdnAccessRelatedInformation=");
            sb.append(isdnAccessRelatedInformation.toString());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.routeList != null) {
            sb.append(", routeList=");
            sb.append(routeList);
        }
        if (this.scfID != null) {
            sb.append(", scfID=");
            sb.append(scfID.toString());
        }
        if (this.travellingClassMark != null) {
            sb.append(", travellingClassMark=");
            sb.append(travellingClassMark.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(destinationNumberRoutingAddress==null)
			throw new ASNParsingComponentException("destination routing address should be set for route select failure request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
