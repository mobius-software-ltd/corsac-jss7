/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

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
public class ToneImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger toneID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger duration;

    public ToneImpl() {
    }

    public ToneImpl(int toneID, Integer duration) {
        this.toneID = new ASNInteger();
        this.toneID.setValue(Long.valueOf(toneID));
        
        if(duration!=null) {
        	this.duration = new ASNInteger();
        	this.duration.setValue(duration.longValue());
        }
    }

    public int getToneID() {
    	if(toneID==null || toneID.getValue()==null)
    		return 0;
    	
        return toneID.getValue().intValue();
    }

    public Integer getDuration() {
    	if(duration==null || duration.getValue()==null)
    		return null;
    	
        return duration.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Tone [");

        if(this.toneID!=null) {
        	sb.append("toneID=");        
        	sb.append(this.toneID.getValue());	
        }
        
        if (this.duration != null) {
            sb.append(", duration=");
            sb.append(this.duration.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
}
