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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceAddressInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.TriggerType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ServiceAddressInformationImpl implements ServiceAddressInformation {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1, defaultImplementation = MiscCallInfoImpl.class)
    private MiscCallInfo miscCallInfo;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNTriggerType triggerType;
    
    public ServiceAddressInformationImpl() {
    }

    public ServiceAddressInformationImpl(Integer serviceKey,MiscCallInfo miscCallInfo,TriggerType triggerType) {
    	if(serviceKey!=null)
    		this.serviceKey=new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);
    		
    	this.miscCallInfo=miscCallInfo;
    	
    	if(triggerType!=null)
    		this.triggerType=new ASNTriggerType(triggerType);    		
    }

    public Integer getServiceKey() {
    	if(serviceKey==null)
    		return null;
    	
    	return serviceKey.getIntValue();
    }

    public MiscCallInfo getMiscCallInfo() {
    	return miscCallInfo;
    }

    public TriggerType getTriggerType() {
    	if(triggerType==null)
    		return null;
    	
    	return triggerType.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ServiceAddressInformation [");

        if (this.serviceKey != null && this.serviceKey.getValue()!=null) {
            sb.append(", serviceKey=");
            sb.append(this.serviceKey);
        }

        if (this.miscCallInfo != null) {
            sb.append(", miscCallInfo=");
            sb.append(this.miscCallInfo);
        }
                
        if (this.triggerType != null && this.triggerType.getType()!=null) {
            sb.append(", triggerType=");
            sb.append(this.triggerType.getType());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(miscCallInfo==null)
			throw new ASNParsingComponentException("misc call info should be set for service address information", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}