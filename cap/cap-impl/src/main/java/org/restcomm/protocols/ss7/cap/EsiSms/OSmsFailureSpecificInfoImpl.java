/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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
package org.restcomm.protocols.ss7.cap.EsiSms;

import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.ASNMOSMSCauseImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class OSmsFailureSpecificInfoImpl implements OSmsFailureSpecificInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNMOSMSCauseImpl failureCause;

    public OSmsFailureSpecificInfoImpl() {
    }

    public OSmsFailureSpecificInfoImpl(MOSMSCause failureCause) {
        if(failureCause!=null)
        	this.failureCause = new ASNMOSMSCauseImpl(failureCause);        	
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
