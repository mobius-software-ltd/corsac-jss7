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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.service.lsm.PeriodicLDRInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class PeriodicLDRInfoImpl implements PeriodicLDRInfo {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=0)
	private ASNInteger reportingAmount;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=2,constructed=false,index=1)
	private ASNInteger reportingInterval;

    public PeriodicLDRInfoImpl() {
    }

    public PeriodicLDRInfoImpl(int reportingAmount, int reportingInterval) {    	
        this.reportingAmount = new ASNInteger(reportingAmount,"ReportingAmount",1,8639999,false);
        this.reportingInterval = new ASNInteger(reportingInterval,"ReportingInterval",1,8639999,false);        
    }

    public int getReportingAmount() {
    	if(reportingAmount==null)
    		return 0;
    	
        return reportingAmount.getIntValue();
    }

    public int getReportingInterval() {
    	if(reportingInterval==null)
    		return 0;
    	
        return reportingInterval.getIntValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PeriodicLDRInfo [");

        sb.append("reportingAmount=");
        sb.append(this.reportingAmount);

        sb.append(", reportingInterval=");
        sb.append(this.reportingInterval);

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(reportingAmount==null)
			throw new ASNParsingComponentException("reporting amount should be set for periodic ldr info", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(reportingInterval==null)
			throw new ASNParsingComponentException("reporting interval should be set for periodic ldr info", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
