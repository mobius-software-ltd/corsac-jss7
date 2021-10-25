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

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.isup.GenericNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.GenericNumberCapWrapperImpl;
import org.restcomm.protocols.ss7.cap.api.isup.LocationNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.OriginalCalledNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.RedirectingPartyIDCapImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.restcomm.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.restcomm.protocols.ss7.inap.api.primitives.LegID;
import org.restcomm.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.restcomm.protocols.ss7.inap.isup.RedirectionInformationInapImpl;
import org.restcomm.protocols.ss7.inap.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.primitives.LegIDWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlockImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author tamas gyorgyey
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ConnectRequestImpl extends CircuitSwitchedCallMessageImpl implements ConnectRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private DestinationRoutingAddressImpl destinationRoutingAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private AlertingPatternCapImpl alertingPattern;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1)
    private OriginalCalledNumberCapImpl originalCalledPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false,index = -1)
    private CarrierImpl carrier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 28,constructed = false,index = -1, defaultImplementation = CallingPartysCategoryInapImpl.class)
    private CallingPartysCategoryInap callingPartysCategory;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29,constructed = false,index = -1)
    private RedirectingPartyIDCapImpl redirectingPartyID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false,index = -1, defaultImplementation = RedirectionInformationInapImpl.class)
    private RedirectionInformationInap redirectionInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = true,index = -1)
    private GenericNumberCapWrapperImpl genericNumbers;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 15,constructed = true,index = -1)
    private ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 19,constructed = false,index = -1)
    private LocationNumberCapImpl chargeNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 21,constructed = true,index = -1)
    private LegIDWrapperImpl legToBeConnected;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 31,constructed = false,index = -1)
    private CUGInterlockImpl cugInterlock;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 32,constructed = false,index = -1)
    private ASNNull cugOutgoingAccess;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 55,constructed = false,index = -1)
    private ASNNull suppressionOfAnnouncement;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 56,constructed = false,index = -1)
    private ASNNull ocsIApplicable;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 57,constructed = false,index = -1)
    private NAOliInfoImpl naoliInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 58,constructed = false,index = -1)
    private ASNNull borInterrogationRequested;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 59,constructed = false,index = -1)
    private ASNNull suppressNCSI;

    public ConnectRequestImpl() {
    }

    public ConnectRequestImpl(DestinationRoutingAddressImpl destinationRoutingAddress, AlertingPatternCapImpl alertingPattern,
            OriginalCalledNumberCapImpl originalCalledPartyID, CAPExtensionsImpl extensions, CarrierImpl carrier,
            CallingPartysCategoryInap callingPartysCategory, RedirectingPartyIDCapImpl redirectingPartyID,
            RedirectionInformationInap redirectionInformation, List<GenericNumberCapImpl> genericNumbers,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, LocationNumberCapImpl chargeNumber,
            LegID legToBeConnected, CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfoImpl naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) {
        this.destinationRoutingAddress = destinationRoutingAddress;
        this.alertingPattern = alertingPattern;
        this.originalCalledPartyID = originalCalledPartyID;
        this.extensions = extensions;
        this.carrier = carrier;
        this.callingPartysCategory = callingPartysCategory;
        this.redirectingPartyID = redirectingPartyID;
        this.redirectionInformation = redirectionInformation;
        
        if(genericNumbers!=null)
        	this.genericNumbers = new GenericNumberCapWrapperImpl(genericNumbers);
        
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
    public DestinationRoutingAddressImpl getDestinationRoutingAddress() {
        return destinationRoutingAddress;
    }

    @Override
    public AlertingPatternCapImpl getAlertingPattern() {
        return alertingPattern;
    }

    @Override
    public OriginalCalledNumberCapImpl getOriginalCalledPartyID() {
        return originalCalledPartyID;
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
    public CallingPartysCategoryInap getCallingPartysCategory() {
        return callingPartysCategory;
    }

    @Override
    public RedirectingPartyIDCapImpl getRedirectingPartyID() {
        return redirectingPartyID;
    }

    @Override
    public RedirectionInformationInap getRedirectionInformation() {
        return redirectionInformation;
    }

    @Override
    public List<GenericNumberCapImpl> getGenericNumbers() {
    	if(genericNumbers==null)
    		return null;
    	
        return genericNumbers.getGenericNumberCap();
    }

    @Override
    public ServiceInteractionIndicatorsTwoImpl getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    @Override
    public LocationNumberCapImpl getChargeNumber() {
        return chargeNumber;
    }

    @Override
    public LegIDImpl getLegToBeConnected() {
    	if(legToBeConnected==null)
    		return null;
    	
        return legToBeConnected.getLegID();
    }

    @Override
    public CUGInterlockImpl getCUGInterlock() {
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
    public NAOliInfoImpl getNAOliInfo() {
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
        if (this.alertingPattern != null) {
            sb.append(", alertingPattern=");
            sb.append(alertingPattern.toString());
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
            for (GenericNumberCapImpl gnc : this.genericNumbers.getGenericNumberCap()) {
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
}