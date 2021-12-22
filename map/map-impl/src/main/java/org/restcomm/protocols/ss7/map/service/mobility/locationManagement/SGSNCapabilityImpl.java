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
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SGSNCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author Lasith Waruna Perera
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SGSNCapabilityImpl implements SGSNCapability {
	
	private ASNNull solsaSupportIndicator;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private SuperChargerInfoWrapperImpl superChargerSupportedInServingNetworkEntity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNNull gprsEnhancementsSupportIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = SupportedCamelPhasesImpl.class)
    private SupportedCamelPhases supportedCamelPhases;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = SupportedLCSCapabilitySetsImpl.class)
    private SupportedLCSCapabilitySets supportedLCSCapabilitySets;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = OfferedCamel4CSIsImpl.class)
    private OfferedCamel4CSIs offeredCamel4CSIs;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNNull smsCallBarringSupportIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1, defaultImplementation = SupportedRATTypesImpl.class)
    private SupportedRATTypes supportedRATTypesIndicator;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1, defaultImplementation = SupportedFeaturesImpl.class)
    private SupportedFeatures supportedFeatures;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private ASNNull tAdsDataRetrieval;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1)
    private ASNBoolean homogeneousSupportOfIMSVoiceOverPSSessions;

    public SGSNCapabilityImpl() {
    }

    public SGSNCapabilityImpl(boolean solsaSupportIndicator, MAPExtensionContainer extensionContainer,
            SuperChargerInfo superChargerSupportedInServingNetworkEntity, boolean gprsEnhancementsSupportIndicator,
            SupportedCamelPhases supportedCamelPhases, SupportedLCSCapabilitySets supportedLCSCapabilitySets,
            OfferedCamel4CSIs offeredCamel4CSIs, boolean smsCallBarringSupportIndicator,
            SupportedRATTypes supportedRATTypesIndicator, SupportedFeatures supportedFeatures, boolean tAdsDataRetrieval,
            Boolean homogeneousSupportOfIMSVoiceOverPSSessions) {
        
    	if(solsaSupportIndicator)
    		this.solsaSupportIndicator = new ASNNull();
    	
        this.extensionContainer = extensionContainer;
        
        if(superChargerSupportedInServingNetworkEntity!=null)        	
        	this.superChargerSupportedInServingNetworkEntity = new SuperChargerInfoWrapperImpl(superChargerSupportedInServingNetworkEntity);
        
        if(gprsEnhancementsSupportIndicator)
        	this.gprsEnhancementsSupportIndicator = new ASNNull();
        
        this.supportedCamelPhases = supportedCamelPhases;
        this.supportedLCSCapabilitySets = supportedLCSCapabilitySets;
        this.offeredCamel4CSIs = offeredCamel4CSIs;
        
        if(smsCallBarringSupportIndicator)
        	this.smsCallBarringSupportIndicator = new ASNNull();
        
        this.supportedRATTypesIndicator = supportedRATTypesIndicator;
        this.supportedFeatures = supportedFeatures;
        
        if(tAdsDataRetrieval)
        	this.tAdsDataRetrieval = new ASNNull();
        
        if(homogeneousSupportOfIMSVoiceOverPSSessions!=null) {
        	this.homogeneousSupportOfIMSVoiceOverPSSessions = new ASNBoolean();
        	this.homogeneousSupportOfIMSVoiceOverPSSessions.setValue(homogeneousSupportOfIMSVoiceOverPSSessions);
        }
    }

    public boolean getSolsaSupportIndicator() {
        return this.solsaSupportIndicator!=null;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public SuperChargerInfo getSuperChargerSupportedInServingNetworkEntity() {
    	if(superChargerSupportedInServingNetworkEntity==null)
    		return null;
    	
        return this.superChargerSupportedInServingNetworkEntity.getSuperChargerInfo();
    }

    public boolean getGprsEnhancementsSupportIndicator() {
        return this.gprsEnhancementsSupportIndicator!=null;
    }

    public SupportedCamelPhases getSupportedCamelPhases() {
        return this.supportedCamelPhases;
    }

    public SupportedLCSCapabilitySets getSupportedLCSCapabilitySets() {
        return this.supportedLCSCapabilitySets;
    }

    public OfferedCamel4CSIs getOfferedCamel4CSIs() {
        return this.offeredCamel4CSIs;
    }

    public boolean getSmsCallBarringSupportIndicator() {
        return this.smsCallBarringSupportIndicator!=null;
    }

    public SupportedRATTypes getSupportedRATTypesIndicator() {
        return this.supportedRATTypesIndicator;
    }

    public SupportedFeatures getSupportedFeatures() {
        return this.supportedFeatures;
    }

    public boolean getTAdsDataRetrieval() {
        return this.tAdsDataRetrieval!=null;
    }

    public Boolean getHomogeneousSupportOfIMSVoiceOverPSSessions() {
    	if(this.homogeneousSupportOfIMSVoiceOverPSSessions==null)
    		return null;
    	
        return this.homogeneousSupportOfIMSVoiceOverPSSessions.getValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SGSNCapability");
        sb.append(" [");

        if (this.solsaSupportIndicator!=null) {
            sb.append("solsaSupportIndicator, ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.superChargerSupportedInServingNetworkEntity != null && this.superChargerSupportedInServingNetworkEntity.getSuperChargerInfo()!=null) {
            sb.append("superChargerSupportedInServingNetworkEntity=");
            sb.append(this.superChargerSupportedInServingNetworkEntity.getSuperChargerInfo());
            sb.append(", ");
        }

        if (this.gprsEnhancementsSupportIndicator!=null) {
            sb.append("gprsEnhancementsSupportIndicator, ");
        }

        if (this.supportedCamelPhases != null) {
            sb.append("supportedCamelPhases=");
            sb.append(this.supportedCamelPhases.toString());
            sb.append(", ");
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

        if (this.smsCallBarringSupportIndicator!=null) {
            sb.append("smsCallBarringSupportIndicator, ");
        }

        if (this.supportedRATTypesIndicator != null) {
            sb.append("supportedRATTypesIndicator=");
            sb.append(this.supportedRATTypesIndicator.toString());
            sb.append(", ");
        }

        if (this.supportedFeatures != null) {
            sb.append("supportedFeatures=");
            sb.append(this.supportedFeatures.toString());
            sb.append(", ");
        }

        if (this.tAdsDataRetrieval!=null) {
            sb.append("tAdsDataRetrieval, ");
        }

        if (this.homogeneousSupportOfIMSVoiceOverPSSessions != null) {
            sb.append("homogeneousSupportOfIMSVoiceOverPSSessions=");
            sb.append(this.homogeneousSupportOfIMSVoiceOverPSSessions.getValue());
        }

        sb.append("]");

        return sb.toString();
    }

}
