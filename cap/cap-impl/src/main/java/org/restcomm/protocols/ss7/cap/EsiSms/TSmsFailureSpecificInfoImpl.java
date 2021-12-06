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
package org.restcomm.protocols.ss7.cap.EsiSms;

import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MTSMSCause;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.MTSMSCauseImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TSmsFailureSpecificInfoImpl implements TSmsFailureSpecificInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = MTSMSCauseImpl.class)
    private MTSMSCause failureCause;

    public TSmsFailureSpecificInfoImpl() {
    }

    public TSmsFailureSpecificInfoImpl(MTSMSCause failureCause) {
        this.failureCause = failureCause;
    }

    public MTSMSCause getFailureCause() {
        return this.failureCause;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TSmsFailureSpecificInfo [");

        if (this.failureCause != null) {
            sb.append("failureCause=");
            sb.append(failureCause.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
