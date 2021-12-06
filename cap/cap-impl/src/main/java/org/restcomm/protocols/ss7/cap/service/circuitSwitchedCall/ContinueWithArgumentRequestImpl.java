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
import org.restcomm.protocols.ss7.cap.api.isup.GenericNumberCap;
import org.restcomm.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCap;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ContinueWithArgumentArgExtension;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfo;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.cap.isup.GenericNumberCapWrapperImpl;
import org.restcomm.protocols.ss7.cap.isup.LocationNumberCapImpl;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ContinueWithArgumentArgExtensionImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.restcomm.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.CUGInterlockImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Povilas Jurna
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ContinueWithArgumentRequestImpl extends CircuitSwitchedCallMessageImpl implements
        ContinueWithArgumentRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = AlertingPatternCapImpl.class)
    private AlertingPatternCap alertingPattern;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1, defaultImplementation = CAPExtensionsImpl.class)
    private CAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = true,index = -1, defaultImplementation = ServiceInteractionIndicatorsTwoImpl.class)
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = false,index = -1,defaultImplementation = CallingPartysCategoryInapImpl.class)
    private CallingPartysCategoryInap callingPartysCategory;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 16,constructed = true,index = -1)
    private GenericNumberCapWrapperImpl genericNumbers;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 17,constructed = false,index = -1,defaultImplementation = CUGInterlockImpl.class)
    private CUGInterlock cugInterlock;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 18,constructed = false,index = -1)
    private ASNNull cugOutgoingAccess;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1,defaultImplementation = LocationNumberCapImpl.class)
    private LocationNumberCap chargeNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = false,index = -1, defaultImplementation = CarrierImpl.class)
    private Carrier carrier;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 55,constructed = false,index = -1)
    private ASNNull suppressionOfAnnouncement;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 56,constructed = false,index = -1,defaultImplementation = NAOliInfoImpl.class)
    private NAOliInfo naOliInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 57,constructed = false,index = -1)
    private ASNNull borInterrogationRequested;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 58,constructed = false,index = -1)
    private ASNNull suppressOCsi;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 59,constructed = true,index = -1, defaultImplementation = ContinueWithArgumentArgExtensionImpl.class)
    private ContinueWithArgumentArgExtension continueWithArgumentArgExtension;

    public ContinueWithArgumentRequestImpl() {
    }

    public ContinueWithArgumentRequestImpl(AlertingPatternCap alertingPattern, CAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryInap callingPartysCategory, List<GenericNumberCap> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, LocationNumberCap chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo, boolean borInterrogationRequested,
            boolean suppressOCsi, ContinueWithArgumentArgExtension continueWithArgumentArgExtension) {
        super();
        this.alertingPattern = alertingPattern;
        this.extensions = extensions;
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
        this.callingPartysCategory = callingPartysCategory;
        
        if(genericNumbers!=null)
        	this.genericNumbers = new GenericNumberCapWrapperImpl(genericNumbers);
        this.cugInterlock = cugInterlock;
        
        if(cugOutgoingAccess)
        	this.cugOutgoingAccess = new ASNNull();
        this.chargeNumber = chargeNumber;
        this.carrier = carrier;
        
        if(suppressionOfAnnouncement)
        	this.suppressionOfAnnouncement = new ASNNull();
        
        this.naOliInfo = naOliInfo;
        
        if(borInterrogationRequested)
        	this.borInterrogationRequested = new ASNNull();
        
        if(suppressOCsi)
        	this.suppressOCsi = new ASNNull();
        
        this.continueWithArgumentArgExtension = continueWithArgumentArgExtension;
    }

    public CAPMessageType getMessageType() {
        return CAPMessageType.continueWithArgument_Request;
    }

    public int getOperationCode() {
        return CAPOperationCode.continueWithArgument;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ContinueWithArgumentRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (alertingPattern != null) {
            sb.append(", alertingPattern=");
            sb.append(alertingPattern);
        }
        if (extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions);
        }
        if (serviceInteractionIndicatorsTwo != null) {
            sb.append(", serviceInteractionIndicatorsTwo=");
            sb.append(serviceInteractionIndicatorsTwo);
        }
        if (callingPartysCategory != null) {
            sb.append(", callingPartysCategory=");
            sb.append(callingPartysCategory);
        }
        if (genericNumbers != null && genericNumbers.getGenericNumberCap()!=null) {
            sb.append(", genericNumbers=[");
            boolean isFirst = true;
            for (GenericNumberCap gnc : this.genericNumbers.getGenericNumberCap()) {
                if (isFirst)
                    isFirst = false;
                else
                    sb.append(", ");
                sb.append(gnc.toString());
            }
            sb.append("]");
        }
        if (cugInterlock != null) {
            sb.append(", cugInterlock=");
            sb.append(cugInterlock);
        }
        if (cugOutgoingAccess!=null) {
            sb.append(", cugOutgoingAccess=");
            sb.append(cugOutgoingAccess);
        }
        if (chargeNumber != null) {
            sb.append(", chargeNumber=");
            sb.append(chargeNumber);
        }
        if (carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier);
        }
        if (suppressionOfAnnouncement!=null) {
            sb.append(", suppressionOfAnnouncement=");
            sb.append(suppressionOfAnnouncement);
        }
        if (naOliInfo != null) {
            sb.append(", naOliInfo=");
            sb.append(naOliInfo);
        }
        if (borInterrogationRequested!=null) {
            sb.append(", borInterrogationRequested=");
            sb.append(borInterrogationRequested);
        }
        if (suppressOCsi!=null) {
            sb.append(", suppressOCsi=");
            sb.append(suppressOCsi);
        }
        if (continueWithArgumentArgExtension != null) {
            sb.append(", continueWithArgumentArgExtension=");
            sb.append(continueWithArgumentArgExtension);
        }

        sb.append("]");

        return sb.toString();
    }

    @Override
    public AlertingPatternCap getAlertingPattern() {
        return this.alertingPattern;
    }

    @Override
    public CAPExtensions getExtensions() {
        return this.extensions;
    }

    @Override
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return this.serviceInteractionIndicatorsTwo;
    }

    @Override
    public CallingPartysCategoryInap getCallingPartysCategory() {
        return this.callingPartysCategory;
    }

    @Override
    public List<GenericNumberCap> getGenericNumbers() {
    	if(this.genericNumbers==null)
    		return null;
    	
        return this.genericNumbers.getGenericNumberCap();
    }

    @Override
    public CUGInterlock getCugInterlock() {
        return this.cugInterlock;
    }

    @Override
    public boolean getCugOutgoingAccess() {
        return this.cugOutgoingAccess!=null;
    }

    @Override
    public LocationNumberCap getChargeNumber() {
        return this.chargeNumber;
    }

    @Override
    public Carrier getCarrier() {
        return this.carrier;
    }

    @Override
    public boolean getSuppressionOfAnnouncement() {
        return this.suppressionOfAnnouncement!=null;
    }

    @Override
    public NAOliInfo getNaOliInfo() {
        return this.naOliInfo;
    }

    @Override
    public boolean getBorInterrogationRequested() {
        return this.borInterrogationRequested!=null;
    }

    @Override
    public boolean getSuppressOCsi() {
        return this.suppressOCsi!=null;
    }

    @Override
    public ContinueWithArgumentArgExtension getContinueWithArgumentArgExtension() {
        return this.continueWithArgumentArgExtension;
    }

}
