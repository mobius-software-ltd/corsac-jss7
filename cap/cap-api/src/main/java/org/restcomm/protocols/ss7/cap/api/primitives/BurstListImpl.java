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

package org.restcomm.protocols.ss7.cap.api.primitives;

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
public class BurstListImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger warningPeriod;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private BurstImpl bursts;

    public BurstListImpl() {
    }

    public BurstListImpl(Integer warningPeriod, BurstImpl burst) {
    	if(warningPeriod!=null) {
    		this.warningPeriod = new ASNInteger();
    		this.warningPeriod.setValue(warningPeriod.longValue());    				
    	}
    	
        this.bursts = burst;
    }

    public Integer getWarningPeriod() {
    	if(warningPeriod==null)
    		return null;
    	
        return warningPeriod.getValue().intValue();
    }

    public BurstImpl getBursts() {
        return bursts;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("BurstList [");
        if (this.warningPeriod != null) {
            sb.append("warningPeriod=");
            sb.append(warningPeriod.getValue());
            sb.append(", ");
        }
        if (this.bursts != null) {
            sb.append("bursts=");
            sb.append(bursts);
            sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }
}