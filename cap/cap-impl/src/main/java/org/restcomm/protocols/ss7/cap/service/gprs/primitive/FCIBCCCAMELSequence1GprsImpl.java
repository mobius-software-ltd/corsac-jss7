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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.FCIBCCCAMELSequence1Gprs;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.FreeFormatDataGprs;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNAppendFreeFormatData;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,lengthIndefinite = false)
public class FCIBCCCAMELSequence1GprsImpl implements FCIBCCCAMELSequence1Gprs {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1,defaultImplementation = FreeFormatDataGprsImpl.class)
    private FreeFormatDataGprs freeFormatData;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = PDPIDImpl.class)
    private PDPID pdpID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNAppendFreeFormatData appendFreeFormatData;

    public FCIBCCCAMELSequence1GprsImpl() {        
    }

    public FCIBCCCAMELSequence1GprsImpl(FreeFormatDataGprs freeFormatData, PDPID pdpID, AppendFreeFormatData appendFreeFormatData) {
        this.freeFormatData = freeFormatData;
        this.pdpID = pdpID;
        
        if(appendFreeFormatData!=null)
        	this.appendFreeFormatData = new ASNAppendFreeFormatData(appendFreeFormatData);        	
    }

    public FreeFormatDataGprs getFreeFormatData() {
        return this.freeFormatData;
    }

    public PDPID getPDPID() {
        return this.pdpID;
    }

    public AppendFreeFormatData getAppendFreeFormatData() {
    	if(this.appendFreeFormatData==null)
    		return null;
    	
        return this.appendFreeFormatData.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FCIBCCCAMELsequence1Gprs [");

        if (this.freeFormatData != null) {
            sb.append("freeFormatData=");
            sb.append(this.freeFormatData.toString());
            sb.append(", ");
        }

        if (this.pdpID != null) {
            sb.append("pdpID=");
            sb.append(this.pdpID.toString());
            sb.append(", ");
        }

        if (this.appendFreeFormatData != null && this.appendFreeFormatData.getType()!=null) {
            sb.append("appendFreeFormatData=");
            sb.append(this.appendFreeFormatData.getType().toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
