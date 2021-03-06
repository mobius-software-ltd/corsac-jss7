/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.primitives.ASNAppendFreeFormatDataImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.inap.api.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.SendingLegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class FCIBCCCAMELSequence1Impl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private FreeFormatDataImpl freeFormatData;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private SendingLegIDWrapperImpl partyToCharge;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNAppendFreeFormatDataImpl appendFreeFormatData;

    public FCIBCCCAMELSequence1Impl() {
    }

    public FCIBCCCAMELSequence1Impl(FreeFormatDataImpl freeFormatData, SendingLegIDImpl partyToCharge, AppendFreeFormatData appendFreeFormatData) {
        this.freeFormatData = freeFormatData;
        
        if(partyToCharge!=null)
        	this.partyToCharge = new SendingLegIDWrapperImpl(partyToCharge);
        
        if(appendFreeFormatData!=null) {
        	this.appendFreeFormatData = new ASNAppendFreeFormatDataImpl();
        	this.appendFreeFormatData.setType(appendFreeFormatData);
        }
    }

    public FreeFormatDataImpl getFreeFormatData() {
        return freeFormatData;
    }

    public SendingLegIDImpl getPartyToCharge() {
    	if(partyToCharge==null)
    		return null;
    	
        return partyToCharge.getSendingLegID();
    }

    public AppendFreeFormatData getAppendFreeFormatData() {
    	if(appendFreeFormatData==null)
    		return null;
    	
        return appendFreeFormatData.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FCIBCCCAMELsequence1 [");

        if (this.freeFormatData != null) {
            sb.append("freeFormatData=");
            sb.append(this.freeFormatData.toString());
            sb.append(", ");
        }
        if (this.partyToCharge != null) {
            sb.append(", partyToCharge=");
            sb.append(partyToCharge.toString());
        }
        if (this.appendFreeFormatData != null && this.appendFreeFormatData.getType()!=null) {
            sb.append(", appendFreeFormatData=");
            sb.append(appendFreeFormatData.getType());
        }

        sb.append("]");

        return sb.toString();
    }
}
