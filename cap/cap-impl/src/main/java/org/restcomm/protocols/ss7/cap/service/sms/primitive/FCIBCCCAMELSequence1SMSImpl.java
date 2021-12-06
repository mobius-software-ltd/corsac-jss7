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

import org.restcomm.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMS;
import org.restcomm.protocols.ss7.cap.primitives.ASNAppendFreeFormatDataImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 * @author alerant appngin
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,lengthIndefinite = false)
public class FCIBCCCAMELSequence1SMSImpl implements FCIBCCCAMELSequence1SMS {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = FreeFormatDataSMSImpl.class)
    private FreeFormatDataSMS freeFormatData;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNAppendFreeFormatDataImpl appendFreeFormatData;

    public FCIBCCCAMELSequence1SMSImpl() {
    }

    public FCIBCCCAMELSequence1SMSImpl(FreeFormatDataSMS freeFormatData, AppendFreeFormatData appendFreeFormatData) {
        this.freeFormatData = freeFormatData;
        
        if(appendFreeFormatData!=null) {
        	this.appendFreeFormatData = new ASNAppendFreeFormatDataImpl();
        	this.appendFreeFormatData.setType(appendFreeFormatData);
        }
    }

    public FreeFormatDataSMS getFreeFormatData() {
        return this.freeFormatData;
    }

    public AppendFreeFormatData getAppendFreeFormatData() {
    	if(this.appendFreeFormatData==null)
    		return null;
    	
        return this.appendFreeFormatData.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FCIBCCCAMELsequence1SMS [");

        if (this.freeFormatData != null) {
            sb.append("freeFormatData=");
            sb.append(freeFormatData.toString());
        }
        if (this.appendFreeFormatData != null) {
            sb.append(", appendFreeFormatData=");
            sb.append(appendFreeFormatData.getType());
        }

        sb.append("]");

        return sb.toString();
    }
}
