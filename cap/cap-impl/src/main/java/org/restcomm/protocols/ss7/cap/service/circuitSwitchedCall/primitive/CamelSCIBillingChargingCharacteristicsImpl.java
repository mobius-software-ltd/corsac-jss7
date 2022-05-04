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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswer;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequent;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELSCIBillingChargingCharacteristicsAlt;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CamelSCIBillingChargingCharacteristicsImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1, defaultImplementation = AOCBeforeAnswerImpl.class)
    private AOCBeforeAnswer aocBeforeAnswer;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = AOCSubsequentImpl.class)
    private AOCSubsequent aocSubsequent;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CAMELSCIBillingChargingCharacteristicsAltImpl.class)
    private CAMELSCIBillingChargingCharacteristicsAlt aocExtension;

    public CamelSCIBillingChargingCharacteristicsImpl() {
    }

    public CamelSCIBillingChargingCharacteristicsImpl(AOCBeforeAnswer aocBeforeAnswer) {
        this.aocBeforeAnswer = aocBeforeAnswer;
    }

    public CamelSCIBillingChargingCharacteristicsImpl(AOCSubsequent aocSubsequent) {
        this.aocSubsequent = aocSubsequent;
    }

    public CamelSCIBillingChargingCharacteristicsImpl(CAMELSCIBillingChargingCharacteristicsAlt aocExtension) {
        this.aocExtension = aocExtension;
    }

    public AOCBeforeAnswer getAOCBeforeAnswer() {
        return aocBeforeAnswer;
    }

    public AOCSubsequent getAOCSubsequent() {
        return aocSubsequent;
    }

    public CAMELSCIBillingChargingCharacteristicsAlt getAOCExtension() {
        return aocExtension;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CamelSCIBillingChargingCharacteristics [");

        if (this.aocBeforeAnswer != null) {
            sb.append("aocBeforeAnswer=");
            sb.append(aocBeforeAnswer.toString());
        }
        if (this.aocSubsequent != null) {
            sb.append(" aocSubsequent=");
            sb.append(aocSubsequent.toString());
        }
        if (this.aocExtension != null) {
            sb.append("aocExtension=");
            sb.append(aocExtension.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(aocBeforeAnswer==null && aocSubsequent==null && aocExtension==null)
			throw new ASNParsingComponentException("one of child items should be set for CAMEL sci billing charging characteristics", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
