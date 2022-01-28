/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 * @author amit bhayani
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
        this.subscriberDataStored = new ASNOctetString(subscriberDataStored);
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
}