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
package org.restcomm.protocols.ss7.cap.api.EsiSms;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.ASNMOSMSCauseImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class OSmsFailureSpecificInfoImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNMOSMSCauseImpl failureCause;

    public OSmsFailureSpecificInfoImpl() {
    }

    public OSmsFailureSpecificInfoImpl(MOSMSCause failureCause) {
        if(failureCause!=null) {
        	this.failureCause = new ASNMOSMSCauseImpl();
        	this.failureCause.setType(failureCause);
        }
    }

    public MOSMSCause getFailureCause() {
    	if(failureCause==null)
    		return null;
    	
        return this.failureCause.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("OSmsFailureSpecificInfo [");

        if (this.failureCause != null && this.failureCause.getType()!=null) {
            sb.append("failureCause=");
            sb.append(failureCause.getType().toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
