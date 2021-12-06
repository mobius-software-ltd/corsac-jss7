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
package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import org.restcomm.protocols.ss7.cap.EsiSms.OSmsFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiSms.OSmsSubmissionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiSms.TSmsDeliverySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.EsiSms.TSmsFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsSubmissionSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsDeliverySpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EventSpecificInformationSMSImpl implements EventSpecificInformationSMS {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = OSmsFailureSpecificInfoImpl.class)
    private OSmsFailureSpecificInfo oSmsFailureSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = OSmsSubmissionSpecificInfoImpl.class)
    private OSmsSubmissionSpecificInfo oSmsSubmissionSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = TSmsFailureSpecificInfoImpl.class)
    private TSmsFailureSpecificInfo tSmsFailureSpecificInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1,defaultImplementation = TSmsDeliverySpecificInfoImpl.class)
    private TSmsDeliverySpecificInfo tSmsDeliverySpecificInfo;

    public EventSpecificInformationSMSImpl() {
        super();
    }

    public EventSpecificInformationSMSImpl(OSmsFailureSpecificInfo oSmsFailureSpecificInfo) {
        super();
        this.oSmsFailureSpecificInfo = oSmsFailureSpecificInfo;
    }

    public EventSpecificInformationSMSImpl(OSmsSubmissionSpecificInfo oSmsSubmissionSpecificInfo) {
        super();
        this.oSmsSubmissionSpecificInfo = oSmsSubmissionSpecificInfo;
    }

    public EventSpecificInformationSMSImpl(TSmsFailureSpecificInfo tSmsFailureSpecificInfo) {
        super();
        this.tSmsFailureSpecificInfo = tSmsFailureSpecificInfo;
    }

    public EventSpecificInformationSMSImpl(TSmsDeliverySpecificInfo tSmsDeliverySpecificInfo) {
        super();
        this.tSmsDeliverySpecificInfo = tSmsDeliverySpecificInfo;
    }

    public OSmsFailureSpecificInfo getOSmsFailureSpecificInfo() {
        return this.oSmsFailureSpecificInfo;
    }

    public OSmsSubmissionSpecificInfo getOSmsSubmissionSpecificInfo() {
        return this.oSmsSubmissionSpecificInfo;
    }

    public TSmsFailureSpecificInfo getTSmsFailureSpecificInfo() {
        return this.tSmsFailureSpecificInfo;
    }

    public TSmsDeliverySpecificInfo getTSmsDeliverySpecificInfo() {
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
