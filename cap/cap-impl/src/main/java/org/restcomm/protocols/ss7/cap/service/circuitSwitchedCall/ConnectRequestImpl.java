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

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.NAOliInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.GenericNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGInterlock;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AlertingPatternWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.NAOliInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.GenericNumberIsupWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectionInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.CUGInterlockImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author tamas gyorgyey
 * @author yulianoifa
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ConnectRequestImpl extends CircuitSwitchedCallMessageImpl implements ConnectRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = DestinationRoutingAddressImpl.class)
    private DestinationRoutingAddress destinationRoutingAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = AlertingPatternWrapperImpl.class)
    private AlertingPatternWrapper alertingPattern;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1,defaultImplementation = OriginalCalledNumberIsupImpl.class)
    private OriginalCalledNumberIsup originalCalledPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false,index = -1,defaultImplementation = CarrierImpl.class)
    private Carrier carrier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 28,constructed = false,index = -1, defaultImplementation = CallingPartysCategoryIsupImpl.class)
    private CallingPartysCategoryIsup callingPartysCategory;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29,constructed = false,index = -1,defaultImplementation = RedirectingPartyIDIsupImpl.class)
    private RedirectingPartyIDIsup redirectingPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false,index = -1, defaultImplementation = RedirectionInformationIsupImpl.class)
    private RedirectionInformationIsup redirectionInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = true,index = -1)
    private GenericNumberIsupWrapperImpl genericNumbers;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = true,index = -1, defaultImplementation = ServiceInteractionIndicatorsTwoImpl.class)
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 19,constructed = false,index = -1, defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup chargeNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 21,constructed = true,index = -1)
    private LegIDWrapperImpl legToBeConnected;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 31,constructed = false,index = -1,defaultImplementation = CUGInterlockImpl.class)
    private CUGInterlock cugInterlock;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 32,constructed = false,index = -1)
    private ASNNull cugOutgoingAccess;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 55,constructed = false,index = -1)
    private ASNNull suppressionOfAnnouncement;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 56,constructed = false,index = -1)
    private ASNNull ocsIApplicable;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 57,constructed = false,index = -1,defaultImplementation = NAOliInfoImpl.class)
    private NAOliInfo naoliInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 58,constructed = false,index = -1)
    private ASNNull borInterrogationRequested;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 59,constructed = false,index = -1)
    private ASNNull suppressNCSI;

    public ConnectRequestImpl() {
    }

    public ConnectRequestImpl(DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern,
            OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions, Carrier carrier,
            CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, List<GenericNumberIsup> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberIsup chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) {
        this.destinationRoutingAddress = destinationRoutingAddress;
        
        if(alertingPattern!=null)
        	this.alertingPattern = new AlertingPatternWrapperImpl(alertingPattern);
        
        this.originalCalledPartyID = originalCalledPartyID;
        this.extensions = extensions;
        this.carrier = carrier;
        this.callingPartysCategory = callingPartysCategory;
        this.redirectingPartyID = redirectingPartyID;
        this.redirectionInformation = redirectionInformation;
        
        if(genericNumbers!=null)
        	this.genericNumbers = new GenericNumberIsupWrapperImpl(genericNumbers);
        
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
        this.chargeNumber = chargeNumber;
        
        if(legToBeConnected!=null)
        	this.legToBeConnected = new LegIDWrapperImpl(legToBeConnected);
        
        this.cugInterlock = cugInterlock;
        
        if(cugOutgoingAccess)
        	this.cugOutgoingAccess = new ASNNull();
        
        if(suppressionOfAnnouncement)
        	this.suppressionOfAnnouncement = new ASNNull();
        
        if(ocsIApplicable)
        	this.ocsIApplicable = new ASNNull();
        
        this.naoliInfo = naoliInfo;
        
        if(borInterrogationRequested)
        	this.borInterrogationRequested = new ASNNull();
        
        if(suppressNCSI)
        	this.suppressNCSI = new ASNNull();
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.connect_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.connect;
    }

    @Override
    public DestinationRoutingAddress getDestinationRoutingAddress() {
        return destinationRoutingAddress;
    }

    @Override
    public AlertingPattern getAlertingPattern() {
    	if(alertingPattern==null)
    		return null;
    	
        return alertingPattern.getAlertingPattern();
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
    public Carrier getCarrier() {
        return carrier;
    }

    @Override
    public CallingPartysCategoryIsup getCallingPartysCategory() {
        return callingPartysCategory;
    }

    @Override
    public RedirectingPartyIDIsup getRedirectingPartyID() {
        return redirectingPartyID;
    }

    @Override
    public RedirectionInformationIsup getRedirectionInformation() {
        return redirectionInformation;
    }

    @Override
    public List<GenericNumberIsup> getGenericNumbers() {
    	if(genericNumbers==null)
    		return null;
    	
        return genericNumbers.getGenericNumberCap();
    }

    @Override
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    @Override
    public LocationNumberIsup getChargeNumber() {
        return chargeNumber;
    }

    @Override
    public LegID getLegToBeConnected() {
    	if(legToBeConnected==null)
    		return null;
    	
        return legToBeConnected.getLegID();
    }

    @Override
    public CUGInterlock getCUGInterlock() {
        return cugInterlock;
    }

    @Override
    public boolean getCugOutgoingAccess() {
        return cugOutgoingAccess!=null;
    }

    @Override
    public boolean getSuppressionOfAnnouncement() {
        return suppressionOfAnnouncement!=null;
    }

    @Override
    public boolean getOCSIApplicable() {
        return ocsIApplicable!=null;
    }

    @Override
    public NAOliInfo getNAOliInfo() {
        return naoliInfo;
    }

    @Override
    public boolean getBorInterrogationRequested() {
        return borInterrogationRequested!=null;
    }

    @Override
    public boolean getSuppressNCSI() {
        return suppressNCSI!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ConnectRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.destinationRoutingAddress != null) {
            sb.append(", destinationRoutingAddress=");
            sb.append(destinationRoutingAddress.toString());
        }
        if (this.alertingPattern != null && this.alertingPattern.getAlertingPattern()!=null) {
            sb.append(", alertingPattern=");
            sb.append(alertingPattern.getAlertingPattern());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }
        if (this.callingPartysCategory != null) {
            sb.append(", callingPartysCategory=");
            sb.append(callingPartysCategory.toString());
        }
        if (this.redirectingPartyID != null) {
            sb.append(", redirectingPartyID=");
            sb.append(redirectingPartyID.toString());
        }
        if (this.redirectionInformation != null) {
            sb.append(", redirectionInformation=");
            sb.append(redirectionInformation.toString());
        }
        if (this.genericNumbers != null && this.genericNumbers.getGenericNumberCap()!=null) {
            sb.append(", genericNumbers=[");
            boolean isFirst = true;
            for (GenericNumberIsup gnc : this.genericNumbers.getGenericNumberCap()) {
                if (isFirst)
                    isFirst = false;
                else
                    sb.append(", ");
                sb.append(gnc.toString());
            }
            sb.append("]");
        }
        if (this.serviceInteractionIndicatorsTwo != null) {
            sb.append(", serviceInteractionIndicatorsTwo=");
            sb.append(serviceInteractionIndicatorsTwo.toString());
        }
        if (this.chargeNumber != null) {
            sb.append(", chargeNumber=");
            sb.append(chargeNumber.toString());
        }
        if (this.legToBeConnected != null && this.legToBeConnected.getLegID()!=null) {
            sb.append(", legToBeConnected=");
            sb.append(legToBeConnected.getLegID());
        }
        if (this.cugInterlock != null) {
            sb.append(", cugInterlock=");
            sb.append(cugInterlock.toString());
        }
        if (this.cugOutgoingAccess!=null) {
            sb.append(", cugOutgoingAccess");
        }
        if (suppressionOfAnnouncement!=null) {
            sb.append(", suppressionOfAnnouncement");
        }
        if (ocsIApplicable!=null) {
            sb.append(", ocsIApplicable");
        }
        if (this.naoliInfo != null) {
            sb.append(", naoliInfo=");
            sb.append(naoliInfo.toString());
        }
        if (this.borInterrogationRequested!=null) {
            sb.append(", borInterrogationRequested");
        }
        if (this.suppressNCSI!=null) {
            sb.append(", suppressNCSI");
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(destinationRoutingAddress==null)
			throw new ASNParsingComponentException("destination routing address should be set for connect request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}