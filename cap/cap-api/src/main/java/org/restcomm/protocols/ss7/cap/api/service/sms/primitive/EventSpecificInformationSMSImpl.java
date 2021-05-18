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
package org.restcomm.protocols.ss7.cap.api.service.sms.primitive;

import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsSubmissionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsDeliverySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfoImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EventSpecificInformationSMSImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private OSmsFailureSpecificInfoImpl oSmsFailureSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private OSmsSubmissionSpecificInfoImpl oSmsSubmissionSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private TSmsFailureSpecificInfoImpl tSmsFailureSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private TSmsDeliverySpecificInfoImpl tSmsDeliverySpecificInfo;

    public EventSpecificInformationSMSImpl() {
        super();
    }

    public EventSpecificInformationSMSImpl(OSmsFailureSpecificInfoImpl oSmsFailureSpecificInfo) {
        super();
        this.oSmsFailureSpecificInfo = oSmsFailureSpecificInfo;
    }

    public EventSpecificInformationSMSImpl(OSmsSubmissionSpecificInfoImpl oSmsSubmissionSpecificInfo) {
        super();
        this.oSmsSubmissionSpecificInfo = oSmsSubmissionSpecificInfo;
    }

    public EventSpecificInformationSMSImpl(TSmsFailureSpecificInfoImpl tSmsFailureSpecificInfo) {
        super();
        this.tSmsFailureSpecificInfo = tSmsFailureSpecificInfo;
    }

    public EventSpecificInformationSMSImpl(TSmsDeliverySpecificInfoImpl tSmsDeliverySpecificInfo) {
        super();
        this.tSmsDeliverySpecificInfo = tSmsDeliverySpecificInfo;
    }

    public OSmsFailureSpecificInfoImpl getOSmsFailureSpecificInfo() {
        return this.oSmsFailureSpecificInfo;
    }

    public OSmsSubmissionSpecificInfoImpl getOSmsSubmissionSpecificInfo() {
        return this.oSmsSubmissionSpecificInfo;
    }

    public TSmsFailureSpecificInfoImpl getTSmsFailureSpecificInfo() {
        return this.tSmsFailureSpecificInfo;
    }

    public TSmsDeliverySpecificInfoImpl getTSmsDeliverySpecificInfo() {
        return this.tSmsDeliverySpecificInfo;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EventSpecificInformationSMS [");

        if (this.oSmsFailureSpecificInfo != null) {
            sb.append("oSmsFailureSpecificInfo=");
            sb.append(oSmsFailureSpecificInfo.toString());
        }
        if (this.oSmsSubmissionSpecificInfo != null) {
            sb.append(" oSmsSubmissionSpecificInfo=");
            sb.append(oSmsSubmissionSpecificInfo.toString());
        }
        if (this.tSmsFailureSpecificInfo != null) {
            sb.append(" tSmsFailureSpecificInfo=");
            sb.append(tSmsFailureSpecificInfo.toString());
        }
        if (this.tSmsDeliverySpecificInfo != null) {
            sb.append(" tSmsDeliverySpecificInfo=");
            sb.append(tSmsDeliverySpecificInfo.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
