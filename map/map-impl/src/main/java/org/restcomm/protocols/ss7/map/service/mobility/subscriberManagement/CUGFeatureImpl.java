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
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CUGFeatureImpl implements CUGFeature {
	@ASNChoise
    private ExtBasicServiceCodeImpl basicService = null;
    
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
        
    	if(basicService!=null) {
    		if(basicService instanceof ExtBasicServiceCodeImpl)
    			this.basicService = (ExtBasicServiceCodeImpl)basicService;
    		else if(basicService.getExtBearerService()!=null)
    			this.basicService=new ExtBasicServiceCodeImpl(basicService.getExtBearerService());
    		else if(basicService.getExtTeleservice()!=null)
    			this.basicService=new ExtBasicServiceCodeImpl(basicService.getExtTeleservice());
    	}
    	
        if(preferentialCugIndicator!=null) {
        	this.preferentialCugIndicator = new ASNInteger();
        	this.preferentialCugIndicator.setValue(preferentialCugIndicator.longValue());
        }
        
        this.interCugRestrictions = interCugRestrictions;
        this.extensionContainer = extensionContainer;
    }

    public ExtBasicServiceCode getBasicService() {
        return this.basicService;
    }

    public Integer getPreferentialCugIndicator() {
    	if(this.preferentialCugIndicator==null)
    		return null;
    	
        return this.preferentialCugIndicator.getValue().intValue();
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

}
