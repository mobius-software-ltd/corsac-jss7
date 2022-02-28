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