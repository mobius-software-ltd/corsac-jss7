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

package org.restcomm.protocols.ss7.inap.charging;

import java.util.List;

import org.restcomm.protocols.ss7.inap.api.charging.ChargingReferenceIdentification;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNObjectIdentifier;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChargingReferenceIdentificationImpl implements ChargingReferenceIdentification {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNObjectIdentifier networkIdentification;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNInteger referenceID;

    public ChargingReferenceIdentificationImpl() {
    }

    public ChargingReferenceIdentificationImpl(List<Long> networkIdentification,Long referenceID) {
    	if(networkIdentification!=null) {
    		this.networkIdentification = new ASNObjectIdentifier();
    		this.networkIdentification.setValue(networkIdentification);    	
    	}
    	
    	if(referenceID!=null) {
    		this.referenceID = new ASNInteger();
    		this.referenceID.setValue(referenceID);
    	}
    }

    public List<Long> getNetworkIdentification() {
    	if(networkIdentification==null)
    		return null;
    	
        return networkIdentification.getValue();
    }

    public Long getReferenceID() {
    	if(referenceID==null)
    		return null;
    	
        return referenceID.getValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChargingReferenceIdentification [");

        if (this.networkIdentification != null && this.networkIdentification.getValue()!=null) {
            sb.append("networkIdentification=[");
            sb.append(ASNObjectIdentifier.printDataArrLong(this.networkIdentification.getValue()));
            sb.append("]");
        }
        if (this.referenceID != null && this.referenceID.getValue()!=null) {
            sb.append(", referenceID=");
            sb.append(referenceID.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
}
