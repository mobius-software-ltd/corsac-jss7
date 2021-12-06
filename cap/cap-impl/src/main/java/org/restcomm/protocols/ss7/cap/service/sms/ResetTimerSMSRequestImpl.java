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
package org.restcomm.protocols.ss7.cap.service.sms;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.restcomm.protocols.ss7.cap.api.primitives.TimerID;
import org.restcomm.protocols.ss7.cap.api.service.sms.ResetTimerSMSRequest;
import org.restcomm.protocols.ss7.cap.primitives.ASNTimerIDImpl;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ResetTimerSMSRequestImpl extends SmsMessageImpl implements ResetTimerSMSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNTimerIDImpl timerID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger timerValue;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CAPExtensionsImpl.class)
    private CAPExtensions extensions;

    public ResetTimerSMSRequestImpl(TimerID timerID, int timerValue, CAPExtensions extensions) {
        super();
        
        if(timerID!=null) {
        	this.timerID = new ASNTimerIDImpl();
        	this.timerID.setType(timerID);
        }
        
        this.timerValue = new ASNInteger();
        this.timerValue.setValue(Long.valueOf(timerValue));
        this.extensions = extensions;
    }

    public ResetTimerSMSRequestImpl() {
        super();
    }

    @Override
    public TimerID getTimerID() {
    	if(this.timerID==null)
    		return null;
    	
        return this.timerID.getType();
    }

    @Override
    public int getTimerValue() {
    	if(this.timerValue==null || this.timerValue.getValue()==null)
    		return 0;
    	
        return this.timerValue.getValue().intValue();
    }

    @Override
    public CAPExtensions getExtensions() {
        return this.extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.resetTimerSMS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.resetTimerSMS;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ResetTimerSMSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.timerID != null && this.timerID.getType()!=null) {
            sb.append(", timerID=");
            sb.append(timerID.getType());
        }
        
        if(timerValue!=null && timerValue.getValue()!=null) {
        	sb.append(", timerValue=");
        	sb.append(timerValue);
        } else
        	sb.append(", timerValue=0");
        
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
