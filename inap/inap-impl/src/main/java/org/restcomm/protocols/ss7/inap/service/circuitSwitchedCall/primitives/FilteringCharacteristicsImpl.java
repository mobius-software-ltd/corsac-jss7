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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCharacteristics;

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
public class FilteringCharacteristicsImpl implements FilteringCharacteristics {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNInteger interval;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNInteger numberOfCalls;

    public FilteringCharacteristicsImpl() {
    }

    public FilteringCharacteristicsImpl(Integer value,Boolean isInterval) {
    	if(value!=null) {
    		if(isInterval) {
    			this.interval = new ASNInteger();
    			this.interval.setValue(value.longValue());
    		}
    		else {
    			this.numberOfCalls = new ASNInteger();
    			this.numberOfCalls.setValue(value.longValue());
    		}
    	}
    }

    public Integer getInterval() {
    	if(interval==null || interval.getValue()==null)
    		return null;
    	
        return interval.getValue().intValue();
    }

    public Integer getNumberOfCalls() {
    	if(numberOfCalls==null || numberOfCalls.getValue()==null)
    		return null;
    	
        return numberOfCalls.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FilteringCharacteristics [");

        if (this.interval != null && this.interval.getValue()!=null) {
            sb.append(", interval=");
            sb.append(interval.getValue());
        }
        
        if (this.numberOfCalls != null && this.numberOfCalls.getValue()!=null) {
            sb.append(", numberOfCalls=");
            sb.append(numberOfCalls.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
}
