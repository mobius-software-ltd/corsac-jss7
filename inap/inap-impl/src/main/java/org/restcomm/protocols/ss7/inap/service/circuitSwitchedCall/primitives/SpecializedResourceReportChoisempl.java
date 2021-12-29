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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,lengthIndefinite = false)
public class SpecializedResourceReportChoisempl {
	private ASNNull announcementCompleted;
    
    @ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = false,index = -1)
	private ASNNull announcementStarted;
    
    public SpecializedResourceReportChoisempl() {
    }

    public SpecializedResourceReportChoisempl(boolean value,boolean isStarted) {
    	if(value) {
    		if(isStarted)
    			this.announcementStarted = new ASNNull();
    		else
    			this.announcementCompleted = new ASNNull();
    	}
    }

    public boolean getAnnouncementStarted() {
    	return announcementStarted!=null;
    }

    public boolean getAnnouncementCompleted() {
    	return announcementCompleted!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SpecializedResourceReportRequest [");
        if (this.announcementCompleted!=null) {
            sb.append(", announcementCompleted");            
        }
        
        if (this.announcementStarted!=null) {
            sb.append(", announcementStarted");            
        }
        
        sb.append("]");

        return sb.toString();
    }
}
