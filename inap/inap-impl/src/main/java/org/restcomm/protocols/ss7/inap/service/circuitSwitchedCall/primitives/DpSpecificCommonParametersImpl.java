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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ISDNAccessRelatedInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ASNCGEncountered;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.ISDNAccessRelatedInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.TerminalType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.IPAvailable;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceAddressInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceProfileIdentifier;
import org.restcomm.protocols.ss7.inap.primitives.ASNTerminalType;

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
public class DpSpecificCommonParametersImpl implements DpSpecificCommonParameters {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = ServiceAddressInformationImpl.class)
    private ServiceAddressInformation serviceAddressInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1)
    private BearerCapabilityWrapperImpl bearerCapability;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1, defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup calledPartyNumber;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1, defaultImplementation = CallingPartyNumberIsupImpl.class)
    private CallingPartyNumberIsup callingPartyNumber;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false, index=-1, defaultImplementation = CallingPartysCategoryIsupImpl.class)
    private CallingPartysCategoryIsup callingPartysCategory;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false, index=-1,defaultImplementation = IPSSPCapabilitiesImpl.class)
    private IPSSPCapabilities ipsspCapabilities;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false, index=-1,defaultImplementation = IPAvailableImpl.class)
    private IPAvailable ipAvailable;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false, index=-1, defaultImplementation = ISDNAccessRelatedInformationIsupImpl.class)
    private ISDNAccessRelatedInformationIsup isdnAccessRelatedInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false, index=-1)
    private ASNCGEncountered cgEncountered;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = false, index=-1, defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup locationNumber;
        
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = false, index=-1, defaultImplementation = ServiceProfileIdentifierImpl.class)
    private ServiceProfileIdentifier serviceProfileIdentifier;
        
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 11,constructed = false, index=-1)
    private ASNTerminalType terminalType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 12,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 13,constructed = false, index=-1, defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup chargeNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 14,constructed = false, index=-1, defaultImplementation = LocationNumberIsupImpl.class)
    private LocationNumberIsup servingAreaID;
    
    public DpSpecificCommonParametersImpl() {
    }

    public DpSpecificCommonParametersImpl(ServiceAddressInformation serviceAddressInformation,BearerCapability bearerCapability,
    		CalledPartyNumberIsup calledPartyNumber,CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
    		IPSSPCapabilities ipsspCapabilities,IPAvailable ipAvailable,ISDNAccessRelatedInformationIsup isdnAccessRelatedInformation,
    		CGEncountered cgEncountered,LocationNumberIsup locationNumber,ServiceProfileIdentifier serviceProfileIdentifier,
    		TerminalType terminalType,CAPINAPExtensions extensions,LocationNumberIsup chargeNumber,LocationNumberIsup servingAreaID) {
    	
    	this.serviceAddressInformation=serviceAddressInformation;
    	
    	if(bearerCapability!=null)
    		this.bearerCapability=new BearerCapabilityWrapperImpl(bearerCapability);
    	
    	this.calledPartyNumber=calledPartyNumber;
    	this.callingPartyNumber=callingPartyNumber;
    	this.callingPartysCategory=callingPartysCategory;
    	this.ipsspCapabilities=ipsspCapabilities;
    	this.ipAvailable=ipAvailable;
    	this.isdnAccessRelatedInformation=isdnAccessRelatedInformation;
    	
    	if(cgEncountered!=null)
    		this.cgEncountered=new ASNCGEncountered(cgEncountered);
    		
    	this.locationNumber=locationNumber;
    	this.serviceProfileIdentifier=serviceProfileIdentifier;
    	
    	if(terminalType!=null)
    		this.terminalType=new ASNTerminalType(terminalType);
    		
    	this.extensions=extensions;
    	this.chargeNumber=chargeNumber;
    	this.servingAreaID=servingAreaID;
    }

    public ServiceAddressInformation getServiceAddressInformation() {
    	return serviceAddressInformation;
    }

    public BearerCapability getBearerCapability() {
    	if(bearerCapability==null)
    		return null;
    	
    	return bearerCapability.getBearerCapability();
    }

    public CalledPartyNumberIsup getCalledPartyNumber() {
    	return calledPartyNumber;
    }

    public CallingPartyNumberIsup getCallingPartyNumber() {
    	return callingPartyNumber;
    }

    public CallingPartysCategoryIsup getCallingPartysCategory() {
    	return callingPartysCategory;
    }

    public IPSSPCapabilities getIPSSPCapabilities() {
    	return ipsspCapabilities;
    }

    public IPAvailable getIPAvailable() {
    	return ipAvailable;
    }

    public ISDNAccessRelatedInformationIsup getISDNAccessRelatedInformation() {
    	return isdnAccessRelatedInformation;
    }

    public CGEncountered getCGEncountered() {
    	if(cgEncountered==null)
    		return null;
    	
    	return cgEncountered.getType();
    }

    public LocationNumberIsup getLocationNumber() {
    	return locationNumber;
    }

    public ServiceProfileIdentifier getServiceProfileIdentifier() {
    	return serviceProfileIdentifier;
    }

    public TerminalType getTerminalType() {
    	if(terminalType==null)
    		return null;
    	
    	return terminalType.getType();
    }

    public CAPINAPExtensions getExtensions() {
    	return extensions;
    }

    public LocationNumberIsup getChargeNumber() {
    	return chargeNumber;
    }

    public LocationNumberIsup getServingAreaID() {
    	return servingAreaID;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DpSpecificCommonParameters [");

        if (this.serviceAddressInformation != null) {
            sb.append(", serviceAddressInformation=");
            sb.append(this.serviceAddressInformation);
        }

        if (this.bearerCapability != null || this.bearerCapability.getBearerCapability()!=null) {
            sb.append(", bearerCapability=");
            sb.append(this.bearerCapability.getBearerCapability());
        }
           
        if (this.calledPartyNumber != null) {
            sb.append(", calledPartyNumber=");
            sb.append(this.calledPartyNumber);
        }
        
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(this.callingPartyNumber);
        }
        
        if (this.callingPartysCategory != null) {
            sb.append(", callingPartysCategory=");
            sb.append(this.callingPartysCategory);
        }
        
        if (this.ipsspCapabilities != null) {
            sb.append(", ipsspCapabilities=");
            sb.append(this.ipsspCapabilities);
        }
        
        if (this.ipAvailable != null) {
            sb.append(", ipAvailable=");
            sb.append(this.ipAvailable);
        }
        
        if (this.isdnAccessRelatedInformation != null) {
            sb.append(", isdnAccessRelatedInformation=");
            sb.append(this.isdnAccessRelatedInformation);
        }

        if (this.cgEncountered != null || this.cgEncountered.getType()!=null) {
            sb.append(", cgEncountered=");
            sb.append(this.cgEncountered.getType());
        }
        
        if (this.locationNumber != null) {
            sb.append(", locationNumber=");
            sb.append(this.locationNumber);
        }
        
        if (this.serviceProfileIdentifier != null) {
            sb.append(", serviceProfileIdentifier=");
            sb.append(this.serviceProfileIdentifier);
        }

        if (this.terminalType != null || this.terminalType.getType()!=null) {
            sb.append(", terminalType=");
            sb.append(this.terminalType.getType());
        }
        
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(this.extensions);
        }
        
        if (this.chargeNumber != null) {
            sb.append(", chargeNumber=");
            sb.append(this.chargeNumber);
        }
        
        if (this.servingAreaID != null) {
            sb.append(", servingAreaID=");
            sb.append(this.servingAreaID);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(serviceAddressInformation==null)
			throw new ASNParsingComponentException("service address information should be set for dp specific common parameters", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}