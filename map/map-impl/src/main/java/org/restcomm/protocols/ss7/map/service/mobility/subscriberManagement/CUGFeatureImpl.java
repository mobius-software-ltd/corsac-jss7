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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictions;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CUGFeatureImpl implements CUGFeature {
	@ASNChoise(defaultImplementation = ExtBasicServiceCodeImpl.class)
    private ExtBasicServiceCode basicService = null;
    
	private ASNInteger preferentialCugIndicator = null;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = InterCUGRestrictionsImpl.class)
	private InterCUGRestrictions interCugRestrictions = null;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer = null;

    public CUGFeatureImpl() {
    }

    /**
     *
     */
    public CUGFeatureImpl(ExtBasicServiceCode basicService, Integer preferentialCugIndicator,
            InterCUGRestrictions interCugRestrictions, MAPExtensionContainer extensionContainer) {
        
    	this.basicService = basicService;
    		
        if(preferentialCugIndicator!=null)
        	this.preferentialCugIndicator = new ASNInteger(preferentialCugIndicator,"PreferentialCugIndicator",0,32767,false);
        	
        this.interCugRestrictions = interCugRestrictions;
        this.extensionContainer = extensionContainer;
    }

    public ExtBasicServiceCode getBasicService() {
        return this.basicService;
    }

    public Integer getPreferentialCugIndicator() {
    	if(this.preferentialCugIndicator==null)
    		return null;
    	
        return this.preferentialCugIndicator.getIntValue();
    }

    public InterCUGRestrictions getInterCugRestrictions() {
        return this.interCugRestrictions;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CUGFeature [");

        if (this.basicService != null) {
            sb.append("basicService=");
            sb.append(this.basicService.toString());
            sb.append(", ");
        }

        if (this.preferentialCugIndicator != null) {
            sb.append("preferentialCugIndicator=");
            sb.append(this.preferentialCugIndicator.toString());
            sb.append(", ");
        }

        if (this.interCugRestrictions != null) {
            sb.append("interCugRestrictions=");
            sb.append(this.interCugRestrictions.getInterCUGRestrictionsValue());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(interCugRestrictions==null)
			throw new ASNParsingComponentException("inter cug restrictions should be set for csg feature", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
