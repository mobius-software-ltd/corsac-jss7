/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMS;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNAppendFreeFormatData;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author alerant appngin
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,lengthIndefinite = false)
public class FCIBCCCAMELSequence1SMSImpl implements FCIBCCCAMELSequence1SMS {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = FreeFormatDataSMSImpl.class)
    private FreeFormatDataSMS freeFormatData;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNAppendFreeFormatData appendFreeFormatData;

    public FCIBCCCAMELSequence1SMSImpl() {
    }

    public FCIBCCCAMELSequence1SMSImpl(FreeFormatDataSMS freeFormatData, AppendFreeFormatData appendFreeFormatData) {
        this.freeFormatData = freeFormatData;
        
        if(appendFreeFormatData!=null)
        	this.appendFreeFormatData = new ASNAppendFreeFormatData(appendFreeFormatData);        	
    }

    public FreeFormatDataSMS getFreeFormatData() {
        return this.freeFormatData;
    }

    public AppendFreeFormatData getAppendFreeFormatData() {
    	if(this.appendFreeFormatData==null)
    		return AppendFreeFormatData.overwrite;
    	
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

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(freeFormatData==null)
			throw new ASNParsingComponentException("free format data should be set for fcibcc CAMEL sequence1 sms", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
