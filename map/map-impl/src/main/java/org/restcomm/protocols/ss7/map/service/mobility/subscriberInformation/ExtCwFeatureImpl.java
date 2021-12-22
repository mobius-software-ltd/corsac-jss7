/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSStatusImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * Created by vsubbotin on 26/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtCwFeatureImpl implements ExtCwFeature {
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private ExtBasicServiceCodeWrapperImpl basicService;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1,defaultImplementation = ExtSSStatusImpl.class)
    private ExtSSStatus ssStatus;

    public ExtCwFeatureImpl() {
    }

    public ExtCwFeatureImpl(ExtBasicServiceCode basicService, ExtSSStatus ssStatus) {
        if(basicService!=null)
        	this.basicService = new ExtBasicServiceCodeWrapperImpl(basicService);
        
        this.ssStatus = ssStatus;
    }

    public ExtBasicServiceCode getBasicService() {
    	if(this.basicService==null)
    		return null;
    	
        return this.basicService.getExtBasicServiceCode();
    }

    public ExtSSStatus getSsStatus() {
        return this.ssStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtCwFeature [");

        if (this.basicService != null && this.basicService.getExtBasicServiceCode()!=null) {
            sb.append("basicService=");
            sb.append(this.basicService.getExtBasicServiceCode());
        }
        if (this.ssStatus != null) {
            sb.append(", ssStatus=");
            sb.append(this.ssStatus);
        }

        sb.append("]");
        return sb.toString();
    }
}
