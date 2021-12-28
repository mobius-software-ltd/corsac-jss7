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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AlertingPatternWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AnalyseInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ISDNAccessRelatedInformationImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AnalyzeInformationRequestImpl extends CircuitSwitchedCallMessageImpl implements AnalyseInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = DestinationRoutingAddressImpl.class)
    private DestinationRoutingAddress destinationRoutingAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = AlertingPatternWrapperImpl.class)
    private AlertingPatternWrapper alertingPattern;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = ISDNAccessRelatedInformationImpl.class)
    private ISDNAccessRelatedInformation isdnAccessRelatedInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1,defaultImplementation = OriginalCalledNumberIsupImpl.class)
    private OriginalCalledNumberIsup originalCalledPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1, defaultImplementation = CallingPartyNumberIsupImpl.class)
    private CallingPartyNumberIsup callingPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1, defaultImplementation = CallingPartysCategoryIsupImpl.class)
    private CallingPartysCategoryIsup callingPartysCategory;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1, defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup calledPartyNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1,defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup chargeNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false,index = -1,defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup travellingClassMark;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false,index = -1,defaultImplementation = CarrierImpl.class)
    private Carrier carrier;
    
    public AnalyzeInformationRequestImpl() {
    }
    
    public AnalyzeInformationRequestImpl(DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern,
    		ISDNAccessRelatedInformation isdnAccessRelatedInformation,OriginalCalledNumberIsup originalCalledPartyID,
    		CAPINAPExtensions extensions,CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
    		CalledPartyNumberIsup calledPartyNumber,LocationNumberIsup chargeNumber,LocationNumberIsup travellingClassMark,Carrier carrier) {
        this.destinationRoutingAddress = destinationRoutingAddress;
        
        if(alertingPattern!=null)
        	this.alertingPattern = new AlertingPatternWrapperImpl(alertingPattern);
        
        this.isdnAccessRelatedInformation=isdnAccessRelatedInformation;
        this.originalCalledPartyID = originalCalledPartyID;
        this.extensions = extensions;
        this.callingPartyNumber=callingPartyNumber;
        this.callingPartysCategory=callingPartysCategory;
        this.calledPartyNumber=calledPartyNumber;
        this.chargeNumber=chargeNumber;
        this.travellingClassMark=travellingClassMark;
        this.carrier = carrier;               
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.analyzeInformation_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.analyseInformation;
    }

    public DestinationRoutingAddress getDestinationRoutingAddress() {
		return destinationRoutingAddress;
	}

	public AlertingPattern getAlertingPattern() {
		if(alertingPattern==null)
			return null;
		
		return alertingPattern.getAlertingPattern();
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
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

	@Override
    public CallingPartyNumberIsup getCallingPartyNumber() {
		return callingPartyNumber;
	}

	@Override
    public CallingPartysCategoryIsup getCallingPartysCategory() {
        return callingPartysCategory;
    }
    
	@Override
    public CalledPartyNumberIsup getCalledPartyNumber() {
		return calledPartyNumber;
	}

	@Override
    public LocationNumberIsup getChargeNumber() {
		return chargeNumber;
	}

	@Override
    public LocationNumberIsup getTravellingClassMark() {
		return travellingClassMark;
	}

	@Override
    public Carrier getCarrier() {
        return carrier;
    }
	
   @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AnalyzeInformationRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.destinationRoutingAddress != null) {
            sb.append(", destinationRoutingAddress=");
            sb.append(destinationRoutingAddress.toString());
        }
        if (this.alertingPattern != null) {
            sb.append(", alertingPattern=");
            sb.append(alertingPattern.toString());
        }
        if (this.isdnAccessRelatedInformation != null) {
            sb.append(", isdnAccessRelatedInformation=");
            sb.append(isdnAccessRelatedInformation.toString());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber.toString());
        }
        if (this.callingPartysCategory != null) {
            sb.append(", callingPartysCategory=");
            sb.append(callingPartysCategory.toString());
        }
        if (this.calledPartyNumber != null) {
            sb.append(", calledPartyNumber=");
            sb.append(calledPartyNumber.toString());
        }
        if (this.chargeNumber != null) {
            sb.append(", chargeNumber=");
            sb.append(chargeNumber);
        }
        if (this.travellingClassMark != null) {
            sb.append(", travellingClassMark=");
            sb.append(travellingClassMark.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}