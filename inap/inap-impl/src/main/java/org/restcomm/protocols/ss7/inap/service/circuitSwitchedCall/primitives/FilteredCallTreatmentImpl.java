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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InformationToSendImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteredCallTreatment;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class FilteredCallTreatmentImpl implements FilteredCallTreatment {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNOctetString sfBillingChargingCharacteristics;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1, defaultImplementation = InformationToSendImpl.class)
    private InformationToSend informationToSend;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNInteger maximumNumberOfCounters;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = CauseIsupImpl.class)
    private CauseIsup cause;
    
    public FilteredCallTreatmentImpl() {
    }

    public FilteredCallTreatmentImpl(ByteBuf sfBillingChargingCharacteristics,InformationToSend informationToSend,
    		Integer maximumNumberOfCounters,CauseIsup cause) {
    	if(sfBillingChargingCharacteristics!=null)
    		this.sfBillingChargingCharacteristics=new ASNOctetString(sfBillingChargingCharacteristics,"SFBillingChargingCharacteristics",null,null,false);
    	
    	this.informationToSend=informationToSend;
    	
    	if(maximumNumberOfCounters!=null)
    		this.maximumNumberOfCounters=new ASNInteger(maximumNumberOfCounters,"MaximumNumberOfCounters",1,100,false);
    		
    	this.cause=cause;
    }

    public ByteBuf getSFBillingChargingCharacteristics() {
    	if(sfBillingChargingCharacteristics==null)
    		return null;
    	
    	return sfBillingChargingCharacteristics.getValue();
    }

    public InformationToSend getInformationToSend() {
    	return informationToSend;
    }

    public Integer getMaximumNumberOfCounters() {
    	if(maximumNumberOfCounters==null)
    		return 1;
    	
    	return maximumNumberOfCounters.getIntValue();
    }

    public CauseIsup getCause() {
    	return cause;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FilteredCallTreatment [");

        if (this.sfBillingChargingCharacteristics != null && this.sfBillingChargingCharacteristics.getValue()!=null) {
            sb.append(", sfBillingChargingCharacteristics=");
            sb.append(sfBillingChargingCharacteristics.printDataArr());
        }

        if (this.informationToSend != null) {
            sb.append(", informationToSend=");
            sb.append(this.informationToSend);
        }
       
        if (this.maximumNumberOfCounters != null || this.maximumNumberOfCounters.getValue()!=null) {
            sb.append(", maximumNumberOfCounters=");
            sb.append(this.maximumNumberOfCounters);
        }

        if (this.cause != null) {
            sb.append(", cause=");
            sb.append(this.cause);
        }
        
        sb.append("]");

        return sb.toString();
    }
}