/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.RequestedReportInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class RequestedReportInfoImpl implements RequestedReportInfo {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNNull accumulatedCharge;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNNull actualTariff;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNNull chargeableDuration;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false, index=-1)
    private ASNNull timeOfAnswer;
    
	public RequestedReportInfoImpl() {
    }

    public RequestedReportInfoImpl(boolean accumulatedCharge,boolean actualTariff,boolean chargeableDuration,boolean timeOfAnswer) {
    	if(accumulatedCharge)
    		this.accumulatedCharge=new ASNNull();    
    	
    	if(actualTariff)
    		this.actualTariff=new ASNNull();    
    	
    	if(chargeableDuration)
    		this.chargeableDuration=new ASNNull();    
    	
    	if(timeOfAnswer)
    		this.timeOfAnswer=new ASNNull();    
    }

    public boolean getAccumulatedCharge() {
    	return accumulatedCharge!=null;
    }

    public boolean getActualTariff() {
    	return actualTariff!=null;
    }

    public boolean getChargeableDuration() {
    	return chargeableDuration!=null;
    }

    public boolean getTimeOfAnswer() {
    	return timeOfAnswer!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("RequestedReportInfo [");

        if (this.accumulatedCharge != null) {
            sb.append(", accumulatedCharge=");            
        }
        
        if (this.actualTariff != null) {
            sb.append(", actualTariff=");            
        }
        
        if (this.chargeableDuration != null) {
            sb.append(", chargeableDuration=");            
        }
        
        if (this.timeOfAnswer != null) {
            sb.append(", timeOfAnswer=");            
        }
        
        sb.append("]");

        return sb.toString();
    }
}