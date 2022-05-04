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

package org.restcomm.protocols.ss7.inap.EsiBcsm;

import org.restcomm.protocols.ss7.commonapp.api.isup.BackwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.commonapp.isup.BackwardCallIndicatorsIsupImpl;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardGVNSIndicator;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.BackwardGVNSIndicatorImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AnswerSpecificInfoImpl implements AnswerSpecificInfo {
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = false,index = -1)
    private ASNInteger timeToAnswer;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 2,constructed = false,index = -1, defaultImplementation = BackwardCallIndicatorsIsupImpl.class)
    private BackwardCallIndicatorsIsup backwardCallIndicators;
    
	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 3,constructed = false,index = -1, defaultImplementation = BackwardGVNSIndicatorImpl.class)
    private BackwardGVNSIndicator backwardGVNSIndicator;
    
    public AnswerSpecificInfoImpl() {
    }

    public AnswerSpecificInfoImpl(Integer timeToAnswer,BackwardCallIndicatorsIsup backwardCallIndicators,BackwardGVNSIndicator backwardGVNSIndicator) {
    	if(timeToAnswer!=null)
        	this.timeToAnswer = new ASNInteger(timeToAnswer,"TimeToAnswer",0,2047,false);
        	
        this.backwardCallIndicators=backwardCallIndicators;
        this.backwardGVNSIndicator=backwardGVNSIndicator;        
    }

    public BackwardCallIndicatorsIsup getBackwardCallIndicators() {
        return backwardCallIndicators;
    }

    public BackwardGVNSIndicator getBackwardGVNSIndicator() {
        return backwardGVNSIndicator;
    }

    public Integer getTimeToAnswer() {
    	if(timeToAnswer==null)
    		return null;
    	
        return timeToAnswer.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AnswerSpecificInfo [");

        if (this.timeToAnswer != null) {
            sb.append("timeToAnswer= [");
            sb.append(timeToAnswer.getValue().toString());
            sb.append("]");
        }        
        if (this.backwardCallIndicators != null) {
            sb.append(", backwardCallIndicators= [");
            sb.append(backwardCallIndicators.toString());
            sb.append("]");
        }
        if (this.backwardGVNSIndicator != null) {
            sb.append(", backwardGVNSIndicator= [");
            sb.append(backwardGVNSIndicator.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
