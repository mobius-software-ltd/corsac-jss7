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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AOCGPRS;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CAMELSCIGPRSBillingChargingCharacteristicsImpl implements CAMELSCIGPRSBillingChargingCharacteristics {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = AOCGPRSImpl.class)
    private AOCGPRS aocGPRS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = PDPIDImpl.class)
    private PDPID pdpID;

    public CAMELSCIGPRSBillingChargingCharacteristicsImpl() {
    }

    public CAMELSCIGPRSBillingChargingCharacteristicsImpl(AOCGPRS aocGPRS, PDPID pdpID) {
        this.aocGPRS = aocGPRS;
        this.pdpID = pdpID;
    }

    public AOCGPRS getAOCGPRS() {
        return this.aocGPRS;
    }

    public PDPID getPDPID() {
        return this.pdpID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CAMELSCIGPRSBillingChargingCharacteristics [");

        if (this.aocGPRS != null) {
            sb.append("aocGPRS=");
            sb.append(this.aocGPRS.toString());
            sb.append(", ");
        }

        if (this.pdpID != null) {
            sb.append("pdpID=");
            sb.append(this.pdpID.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(aocGPRS==null)
			throw new ASNParsingComponentException("aoc gprs should be set for CAMEL sci gprs billing charging characteristics", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
