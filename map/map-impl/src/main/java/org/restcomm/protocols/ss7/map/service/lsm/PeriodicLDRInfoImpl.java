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
