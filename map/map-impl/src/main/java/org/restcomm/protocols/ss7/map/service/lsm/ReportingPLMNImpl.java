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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.primitives.PlmnId;
import org.restcomm.protocols.ss7.map.api.service.lsm.RANTechnology;
import org.restcomm.protocols.ss7.map.api.service.lsm.ReportingPLMN;
import org.restcomm.protocols.ss7.map.primitives.PlmnIdImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ReportingPLMNImpl implements ReportingPLMN {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = PlmnIdImpl.class)
    private PlmnId plmnId;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNRANTechnology ranTechnology;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull ranPeriodicLocationSupport;

    public ReportingPLMNImpl() {
    }

    public ReportingPLMNImpl(PlmnId plmnId, RANTechnology ranTechnology, boolean ranPeriodicLocationSupport) {
        this.plmnId = plmnId;
        
        if(ranTechnology!=null)
        	this.ranTechnology = new ASNRANTechnology(ranTechnology);
        	
        if(ranPeriodicLocationSupport)
        	this.ranPeriodicLocationSupport = new ASNNull();
    }

    public PlmnId getPlmnId() {
        return plmnId;
    }

    public RANTechnology getRanTechnology() {
    	if(ranTechnology==null)
    		return null;
    	
        return ranTechnology.getType();
    }

    public boolean getRanPeriodicLocationSupport() {
        return ranPeriodicLocationSupport!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReportingPLMN [");

        if (this.plmnId != null) {
            sb.append("plmnId=");
            sb.append(this.plmnId.toString());
        }
        if (this.ranTechnology != null) {
            sb.append(", ranTechnology=");
            sb.append(this.ranTechnology.getType());
        }
        if (this.ranPeriodicLocationSupport!=null) {
            sb.append(", ranPeriodicLocationSupport");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(plmnId==null)
			throw new ASNParsingComponentException("plmn ID should be set for reporting plmn", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}