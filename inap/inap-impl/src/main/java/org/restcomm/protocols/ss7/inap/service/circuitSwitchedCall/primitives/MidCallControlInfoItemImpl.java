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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.MidCallControlInfoItem;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.MidCallInfoType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.MidCallReportType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class MidCallControlInfoItemImpl implements MidCallControlInfoItem {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1,defaultImplementation =  MidCallInfoTypeImpl.class)
    private MidCallInfoType midCallInfoType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1,defaultImplementation =  DigitsIsupImpl.class)
    private ASNMidCallReportType midCallReportType;

    public MidCallControlInfoItemImpl() {
    }

    public MidCallControlInfoItemImpl(MidCallInfoType midCallInfoType, MidCallReportType midCallReportType) {
    	this.midCallInfoType=midCallInfoType;
    	if(midCallReportType != null)
    		this.midCallReportType=new ASNMidCallReportType(midCallReportType);    		
    }

    public MidCallInfoType getMidCallInfoType() {
    	return midCallInfoType;
    }

    public MidCallReportType getMidCallReportType() {
    	if(midCallReportType==null) 
    		return null;
    	
    	return midCallReportType.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MidCallControlInfoItem [");

        if (this.midCallInfoType != null) {
            sb.append(", midCallInfoType=");
            sb.append(this.midCallInfoType);
        }

        if (this.midCallReportType != null && this.midCallReportType.getType()!=null) {
            sb.append(", midCallReportType=");
            sb.append(this.midCallReportType.getType());
        }
                
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(midCallInfoType==null)
			throw new ASNParsingComponentException("mid call info type should be set for mid call control info item", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}