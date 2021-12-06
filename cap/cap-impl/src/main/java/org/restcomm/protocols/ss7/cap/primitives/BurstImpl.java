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

package org.restcomm.protocols.ss7.cap.primitives;

import org.restcomm.protocols.ss7.cap.api.primitives.Burst;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class BurstImpl implements Burst {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger numberOfBursts;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger burstInterval;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNInteger numberOfTonesInBurst;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ASNInteger toneDuration;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
    private ASNInteger toneInterval;

    public BurstImpl() {        
    }

    public BurstImpl(Integer numberOfBursts, Integer burstInterval, Integer numberOfTonesInBurst, Integer toneDuration, Integer toneInterval) {
    	if(numberOfBursts!=null) {
    		this.numberOfBursts = new ASNInteger();
    		this.numberOfBursts.setValue(numberOfBursts.longValue());
    	}
    	
    	if(burstInterval!=null) {
    		this.burstInterval = new ASNInteger();
    		this.burstInterval.setValue(burstInterval.longValue());
    	}
    	
    	if(numberOfTonesInBurst!=null) {
    		this.numberOfTonesInBurst = new ASNInteger();
    		this.numberOfTonesInBurst.setValue(numberOfTonesInBurst.longValue());
        }
    	
    	if(toneDuration!=null) {
    		this.toneDuration = new ASNInteger();
    		this.toneDuration.setValue(toneDuration.longValue());
    	}
    	
    	if(toneInterval!=null) {
    		this.toneInterval = new ASNInteger();
    		this.toneInterval.setValue(toneInterval.longValue());
    	}
    }

    public Integer getNumberOfBursts() {
    	if(numberOfBursts==null)
    		return null;
    	
        return numberOfBursts.getValue().intValue();
    }

    public Integer getBurstInterval() {
    	if(burstInterval==null)
    		return null;
    	
        return burstInterval.getValue().intValue();
    }

    public Integer getNumberOfTonesInBurst() {
    	if(numberOfTonesInBurst==null)
    		return null;
    	
        return numberOfTonesInBurst.getValue().intValue();
    }

    public Integer getToneDuration() {
    	if(toneDuration==null)
    		return null;
    	
        return toneDuration.getValue().intValue();
    }

    public Integer getToneInterval() {
    	if(toneInterval==null)
    		return null;
    	
        return toneInterval.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Burst [");
        if (this.numberOfBursts != null) {
            sb.append("numberOfBursts=");
            sb.append(numberOfBursts.getValue());
            sb.append(", ");
        }
        if (this.burstInterval != null) {
            sb.append("burstInterval=");
            sb.append(burstInterval.getValue());
            sb.append(", ");
        }
        if (this.numberOfTonesInBurst != null) {
            sb.append("numberOfTonesInBurst=");
            sb.append(numberOfTonesInBurst.getValue());
            sb.append(", ");
        }
        if (this.toneDuration != null) {
            sb.append("toneDuration=");
            sb.append(toneDuration.getValue());
            sb.append(", ");
        }
        if (this.toneInterval != null) {
            sb.append("toneInterval=");
            sb.append(toneInterval.getValue());
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }
}
