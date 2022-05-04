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

package org.restcomm.protocols.ss7.inap.EsiBcsm;

import org.restcomm.protocols.ss7.commonapp.api.isup.BackwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.commonapp.isup.BackwardCallIndicatorsIsupImpl;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AlertingSpecificInfo;

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
public class AlertingSpecificInfoImpl implements AlertingSpecificInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = BackwardCallIndicatorsIsupImpl.class)
    private BackwardCallIndicatorsIsup backwardCallIndicators;
    
	public AlertingSpecificInfoImpl() {
    }

    public AlertingSpecificInfoImpl(BackwardCallIndicatorsIsup backwardCallIndicators) {
    	this.backwardCallIndicators=backwardCallIndicators;               
    }

    public BackwardCallIndicatorsIsup getBackwardCallIndicators() {
        return backwardCallIndicators;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AlertingSpecificInfo [");

        if (this.backwardCallIndicators != null) {
            sb.append(", backwardCallIndicators= [");
            sb.append(backwardCallIndicators.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(backwardCallIndicators==null)
			throw new ASNParsingComponentException("backward call indicators not set for alerting specific info", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
