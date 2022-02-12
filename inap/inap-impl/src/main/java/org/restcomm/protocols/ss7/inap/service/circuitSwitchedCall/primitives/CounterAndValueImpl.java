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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CounterAndValue;

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
public class CounterAndValueImpl implements CounterAndValue {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNInteger counterID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNInteger counterValue;

    public CounterAndValueImpl() {
    }

    public CounterAndValueImpl(Integer counterID,Integer counterValue) {
    	if(counterID!=null)
    		this.counterID=new ASNInteger(counterID,"CounterID",0,99,false);
    		
    	if(counterValue!=null)
    		this.counterValue=new ASNInteger(counterValue,"CounterValue",0,255,false);    		
    }

    public Integer getCounterID() {
    	if(counterID==null)
    		return null;
    	
    	return counterID.getIntValue();
    }

    public Integer getCounterValue() {
    	if(counterValue==null)
    		return null;
    	
    	return counterValue.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CounterAndValue [");

        if (this.counterID != null && this.counterID.getValue()!=null) {
            sb.append(", counterID=");
            sb.append(this.counterID);
        }

        if (this.counterValue != null || this.counterValue.getValue()!=null) {
            sb.append(", counterValue=");
            sb.append(this.counterValue);
        }
                
        sb.append("]");

        return sb.toString();
    }
}