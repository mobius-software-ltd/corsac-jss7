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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class VLRCapabilityImpl implements VLRCapability {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = SupportedCamelPhasesImpl.class)
    private SupportedCamelPhases supportedCamelPhases;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull solsaSupportIndicator;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNISTSupportIndicatorImpl istSupportIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private SuperChargerInfoWrapperImpl superChargerSupportedInServingNetworkEntity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNNull longFtnSupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = SupportedLCSCapabilitySetsImpl.class)
    private SupportedLCSCapabilitySets supportedLCSCapabilitySets;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = OfferedCamel4CSIsImpl.class)
    private OfferedCamel4CSIs offeredCamel4CSIs;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1, defaultImplementation = SupportedRATTypesImpl.class)
    private SupportedRATTypes supportedRATTypesIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNNull longGroupIDSupported;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private ASNNull mtRoamingForwardingSupported;

    public VLRCapabilityImpl() {
    }

    public VLRCapabilityImpl(SupportedCamelPhases supportedCamelPhases, MAPExtensionContainer extensionContainer,
            boolean solsaSupportIndicator, ISTSupportIndicator istSupportIndicator,
            SuperChargerInfo superChargerSupportedInServingNetworkEntity, boolean longFtnSupported,
            SupportedLCSCapabilitySets supportedLCSCapabilitySets, OfferedCamel4CSIs offeredCamel4CSIs,
            SupportedRATTypes supportedRATTypesIndicator, boolean longGroupIDSupported, boolean mtRoamingForwardingSupported) {
        this.supportedCamelPhases = supportedCamelPhases;
        this.extensionContainer = extensionContainer;
        
        if(solsaSupportIndicator)
        	this.solsaSupportIndicator = new ASNNull();
        
        if(istSupportIndicator!=null)
        	this.istSupportIndicator = new ASNISTSupportIndicatorImpl(istSupportIndicator);
        	
        if(superChargerSupportedInServingNetworkEntity!=null)
        	this.superChargerSupportedInServingNetworkEntity = new SuperChargerInfoWrapperImpl(superChargerSupportedInServingNetworkEntity);
        
        if(longFtnSupported)
        	this.longFtnSupported = new ASNNull();
        
        this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
        this.offeredCamel4CSIs = offeredCamel4CSIs;
        this.supportedRATTypesIndicator = supportedRATTypesIndicator;
        
        if(longGroupIDSupported)
        	this.longGroupIDSupported = new ASNNull();
        
        if(mtRoamingForwardingSupported)
        	this.mtRoamingForwardingSupported = new ASNNull();
    }

    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public boolean getSolsaSupportIndicator() {
        return solsaSupportIndicator!=null;
    }

    public ISTSupportIndicator getIstSupportIndicator() {
    	if(istSupportIndicator==null)
    		return null;
    	
        return istSupportIndicator.getType();
    }

    public SuperChargerInfo getSuperChargerSupportedInServingNetworkEntity() {
    	if(superChargerSupportedInServingNetworkEntity==null)
    		return null;
    	
        return superChargerSupportedInServingNetworkEntity.getSuperChargerInfo();
    }

    public boolean getLongFtnSupported() {
        return longFtnSupported!=null;
    }

    public SupportedLCSCapabilitySets getSupportedLCSCapabilitySets() {
        return supportedLCSCapabilitySets;
    }

    public OfferedCamel4CSIs getOfferedCamel4CSIs() {
        return offeredCamel4CSIs;
    }

    public SupportedRATTypes getSupportedRATTypesIndicator() {    	
        return supportedRATTypesIndicator;
    }

    public boolean getLongGroupIDSupported() {
        return longGroupIDSupported!=null;
    }

    public boolean getMtRoamingForwardingSupported() {
        return mtRoamingForwardingSupported!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VlrCapability [");

        if (this.supportedCamelPhases != null) {
            sb.append("supportedCamelPhases=");
            sb.append(this.supportedCamelPhases.toString());
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }
        if (this.solsaSupportIndicator!=null) {
            sb.append("solsaSupportIndicator, ");
        }
        if (this.istSupportIndicator != null) {
            sb.append("istSupportIndicator=");
            sb.append(this.istSupportIndicator.getType());
            sb.append(", ");
        }
        if (this.superChargerSupportedInServingNetworkEntity != null && this.superChargerSupportedInServingNetworkEntity.getSuperChargerInfo()!=null) {
            sb.append("superChargerSupportedInServingNetworkEntity=");
            sb.append(this.superChargerSupportedInServingNetworkEntity.getSuperChargerInfo());
            sb.append(", ");
        }
        if (this.longFtnSupported!=null) {
            sb.append("longFtnSupported, ");
        }
        if (this.supportedLCSCapabilitySets != null) {
            sb.append("supportedLCSCapabilitySets=");
            sb.append(this.supportedLCSCapabilitySets.toString());
            sb.append(", ");
        }
        if (this.offeredCamel4CSIs != null) {
            sb.append("offeredCamel4CSIs=");
            sb.append(this.offeredCamel4CSIs.toString());
            sb.append(", ");
        }
        if (this.supportedRATTypesIndicator != null) {
            sb.append("supportedRATTypesIndicator=");
            sb.append(this.supportedRATTypesIndicator.toString());
            sb.append(", ");
        }
        if (this.longGroupIDSupported!=null) {
            sb.append("longGroupIDSupported, ");
        }
        if (this.mtRoamingForwardingSupported!=null) {
            sb.append("mtRoamingForwardingSupported, ");
        }

        sb.append("]");

        return sb.toString();
    }
}