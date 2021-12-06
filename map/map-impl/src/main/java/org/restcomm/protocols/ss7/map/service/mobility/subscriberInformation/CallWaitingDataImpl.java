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

import java.util.List;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallWaitingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ExtCwFeature;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * Created by vsubbotin on 26/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CallWaitingDataImpl implements CallWaitingData {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private ExtCwFeatureListWrapperImpl cwFeatureList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull notificationToCSE;

    public CallWaitingDataImpl() {
    }

    public CallWaitingDataImpl(List<ExtCwFeature> cwFeatureList, boolean notificationToCSE) {
        if(cwFeatureList!=null)
        	this.cwFeatureList = new ExtCwFeatureListWrapperImpl(cwFeatureList);
        
        if(notificationToCSE)
        	this.notificationToCSE = new ASNNull();
    }

    public List<ExtCwFeature> getCwFeatureList() {
    	if(cwFeatureList==null)
    		return null;
    	
        return this.cwFeatureList.getExtCwFeature();
    }

    public boolean getNotificationToCSE() {
        return this.notificationToCSE!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallWaitingData [");

        if (this.cwFeatureList != null && this.cwFeatureList.getExtCwFeature()!=null) {
            sb.append("cwFeatureList=[");
            boolean firstItem = true;
            for (ExtCwFeature extCwFeature: cwFeatureList.getExtCwFeature()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(extCwFeature);
            }
            sb.append("], ");
        }
        if (this.notificationToCSE!=null) {
            sb.append("isNotificationToCSE, ");
        }

        sb.append("]");
        return sb.toString();
    }
}
