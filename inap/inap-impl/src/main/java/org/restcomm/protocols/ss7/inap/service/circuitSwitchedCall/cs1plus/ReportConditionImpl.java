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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ReportCondition;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ReportConditionImpl implements ReportCondition {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNNull reportAtEndOfConnection;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNInteger reportAtChargeLimit;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNNull reportImmediately;
    
	public ReportConditionImpl() {
    }

    public ReportConditionImpl(boolean value,boolean immediately) {
    	if(value) {
    		if(immediately)
    			this.reportImmediately=new ASNNull();
    		else
    			this.reportAtEndOfConnection=new ASNNull();
    	}
    }
    
    public ReportConditionImpl(Integer reportAtChargeLimit) {
    	if(reportAtChargeLimit!=null)
    		this.reportAtChargeLimit=new ASNInteger(reportAtChargeLimit,"ReportAtChargeLimit",0,32767,false);    		
    }

    public boolean getReportAtEndOfConnection() {
    	return reportAtEndOfConnection!=null;
    }

    public Integer getReportAtChargeLimit() {
    	if(reportAtChargeLimit==null || reportAtChargeLimit.getValue()==null)
    		return null;
    	
    	return reportAtChargeLimit.getValue().intValue();
    }

    public boolean getReportImmediately() {
    	return reportImmediately!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ReportCondition [");

        if (this.reportAtEndOfConnection != null) {
            sb.append(", reportAtEndOfConnection=");            
        }
        
        if (this.reportAtChargeLimit != null && this.reportAtChargeLimit.getValue()!=null) {
            sb.append(", reportAtChargeLimit=");
            sb.append(reportAtChargeLimit.getValue());
        }
        
        if (this.reportImmediately != null) {
            sb.append(", reportImmediately=");            
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(reportAtEndOfConnection==null && reportAtChargeLimit==null && reportImmediately==null)
			throw new ASNParsingComponentException("one of child items should be set for report condition", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}