/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public class SuperChargerInfoImpl implements SuperChargerInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNNull sendSubscriberData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNOctetString subscriberDataStored;

    public SuperChargerInfoImpl() {
    	
    }
    
    /**
     *
     */
    public SuperChargerInfoImpl(boolean sendSubscriberData) {
    	if(sendSubscriberData)
    		this.sendSubscriberData=new ASNNull();
    }

    /**
     * @param subscriberDataStored
     */
    public SuperChargerInfoImpl(ByteBuf subscriberDataStored) {
        this.subscriberDataStored = new ASNOctetString(subscriberDataStored,"SubscriberDataStored",1,6,false);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement .SuperChargerInfo#getSendSubscriberData()
     */
    public Boolean getSendSubscriberData() {
        return this.sendSubscriberData!=null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement .SuperChargerInfo#getSubscriberDataStored()
     */
    public ByteBuf getSubscriberDataStored() {
    	if(subscriberDataStored==null)
    		return null;
    	
    	return subscriberDataStored.getValue();    	
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SuperChargerInfo [");

        if (sendSubscriberData != null)
            sb.append("sendSubscriberData, ");
        if (subscriberDataStored != null && subscriberDataStored.getValue()!=null) {
            sb.append("subscriberDataStored=[");
            sb.append(subscriberDataStored.printDataArr());
            sb.append("], ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(sendSubscriberData==null && subscriberDataStored==null)
			throw new ASNParsingComponentException("either send subscriber data or subscriber data stored should be set for super charging info", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}